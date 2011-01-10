/*
 * Copyright (c) Panchosoft
 * Developed by Francisco I. Leyva
 * AppSpy - http://labs.panchosoft.com/appspy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.panchosoft.appspy.models;

import com.panchosoft.appspy.factories.AppSpyFactory;
import com.panchosoft.appspy.AppSpyConfig;
import com.panchosoft.appspy.utils.ImagenesUtil;
import com.panchosoft.appspy.utils.PlistParser;
import java.util.logging.*;
import java.util.jar.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;

public final class Espia {
    // Singleton

    private static Espia instancia;
    // Variables de clase
    private String directorioItunes = null;
    private ArrayList<App> apps = null;
    private File app_archivos[] = null;
    private String RUTA_TEMP = System.getProperty("java.io.tmpdir")
            + File.separator + "appspy" + File.separator;

    public static Espia getInstance() {
        if (instancia == null) {
            instancia = new Espia();
        }
        return instancia;
    }

    private Espia(String directorio) {
        // Crear directorio temporal si no existe
        File dirtmp = new File(RUTA_TEMP);
        if (!dirtmp.exists()) {
            boolean creado = dirtmp.mkdir();
            if (!creado) {
                RUTA_TEMP = System.getProperty("java.io.tmpdir");
            }
        }

        establecerDirectorioItunes(directorio);
        importarApps(obtenerDirectorioItunes());
    }

    private Espia() {
        // Crear directorio temporal si no existe
        File dirtmp = new File(RUTA_TEMP);
        if (!dirtmp.exists()) {
            boolean creado = dirtmp.mkdir();
            if (!creado) {
                RUTA_TEMP = System.getProperty("java.io.tmpdir");
            }
        }

        establecerDirectorioItunes(buscarDirectorioItunes());
        importarApps(obtenerDirectorioItunes());
    }

    public void importarApps(String directorio) {
    }

    public javax.swing.ImageIcon getAppIcon(App app, int ancho) {
        // Comprobamos si ya existe el archivo iTunesArtwork
        File imagen = new File(RUTA_TEMP + app.getAppFile().hashCode() + File.separator + "iTunesArtwork");
        if (imagen == null || !imagen.exists()) {
            imagen = leerArchivoDesdeZip(app.getAppFile(), "iTunesArtwork", RUTA_TEMP + app.getAppFile().hashCode());
        }
        if (imagen == null) {
            return null;
        }

        ImageIcon icono = new javax.swing.ImageIcon(imagen.getPath());

        return ImagenesUtil.getInstance().redimensionarImagen(icono, 140, 140);
    }

    public App getApp(File archivo) {
        if (!archivo.getAbsolutePath().endsWith(".ipa")) {
            return null;
        }
        // Obtenemos el contenido del archivo plist
        File plist = leerArchivoDesdeZip(archivo, "Info.plist", RUTA_TEMP + archivo.hashCode());
        if (plist != null) {
            Map<String, Object> dicc = PlistParser.getInstance().parse(plist);
            if (dicc != null) {
                App app = new App(dicc);
                app.setAppFile(archivo);
                return app;
            } else {
                AppSpyFactory.getLog().log(Level.WARNING, "No se pudo parsear el archivo plist. \n {0}", archivo.getAbsolutePath());
            }
        } else {
            AppSpyFactory.getLog().log(Level.WARNING, "No se pudo obtener el archivo plist. \n {0}", archivo.getAbsolutePath());
        }
        return null;
    }

    public ArrayList<App> getApps() {
        if (apps == null) {
            apps = new ArrayList<App>();
            File[] archivos = getAppsFiles();
            int i = 0;
            for (File archivo : archivos) {
                App temp = getApp(archivo);
                if (temp != null) {
                    apps.add(temp);
                }
            }
        }
        return this.apps;
    }

    public void olvidarApps() {
        this.apps = null;
        this.app_archivos = null;
        System.gc();
    }

    public File[] getAppsFiles() {
        if (app_archivos == null) {
            app_archivos = this.leerArchivosDirectorio(obtenerDirectorioItunes());
        }
        return app_archivos;
    }

    private File[] leerArchivosDirectorio(String directorio) {
        // Detener si se proporciona un directorio no válido
        if (directorio == null || directorio.equals("")) {
            AppSpyFactory.getLog().log(Level.SEVERE, "Directorio de iTunes no encontrado...");
            return null;
        }
        // Intentamos acceder al directorio
        File dir = new File(directorio);
        if (dir.isDirectory() && dir.canRead()) {
            // Log: Exito al accesar
            AppSpyFactory.getLog().log(Level.FINE, "Leyendo directorio iTunes..");

            // Regresamos la lista de archivos
            File[] appslist = dir.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".ipa"));
                }
            });
            if (appslist == null || appslist.length == 0) {
                return null;
            }
            return dir.listFiles();
        } else {
            AppSpyFactory.getLog().log(Level.SEVERE, "No es un directorio o no se tienen permisos de lectura...\n{0}", dir.toString());
            return null;
        }
    }

    private File leerArchivoDesdeZip(File origen, String nom_archivo, String destinoDir) {
        if (origen == null || nom_archivo == null || !origen.isFile() || nom_archivo.equals("")) {
            return null;
        }
        try {
            // Creamos una carpeta de trabajo
            JarFile jar = new JarFile(origen);
            Enumeration archivos = jar.entries();
            while (archivos.hasMoreElements()) {
                JarEntry file = (JarEntry) archivos.nextElement();

                if (file.getName().endsWith(nom_archivo)) {
                    // Creamos el directorio de destino si no existe
                    File ipa_dir = new File(destinoDir);
                    ipa_dir.mkdir();

                    // Creamos el archivo
                    File f = new java.io.File(ipa_dir + File.separator + nom_archivo);
                    InputStream is = jar.getInputStream(file); // get the input stream
                    FileOutputStream fos = new FileOutputStream(f);
                    while (is.available() > 0) {  // write contents of 'is' to 'fos'
                        fos.write(is.read());
                    }
                    fos.close();
                    is.close();
                    return f;
                }
            }
        } catch (IOException ex) {
            AppSpyFactory.getLog().log(Level.WARNING, "{0}\n{1}", new Object[]{ex.getMessage(), origen.toString()});
        }
        return null;
    }

    public String buscarDirectorioItunes() {
        File dir;

        if (directorioItunes != null && !directorioItunes.isEmpty()) {
            dir = new File(directorioItunes);
            if (dir.exists() && dir.isDirectory()) {
                return directorioItunes;
            }
        }
        // Verificamos si existe una ruta guardada en el archivo App.config
        String lastdir = AppSpyConfig.getInstance().obtenerPropiedad("lastdir");
        if (lastdir != null && !lastdir.isEmpty()) {
            dir = new File(lastdir);
            if (dir.exists() && dir.isDirectory()) {
                return lastdir;
            }
        }

        // Intentamos con la carpeta predeterminada de iTunes <9 en OSX, Win Vista y 7
        directorioItunes = System.getProperty("user.home");
        directorioItunes += File.separator + "Music" + File.separator + "iTunes"
                + File.separator + "Mobile Applications";
        dir = new File(directorioItunes);
        if (dir.exists() && dir.isDirectory()) {
            return directorioItunes;
        }

        // Intentamos con la carpeta predeterminada de iTunes 9 en OSX, Win Vista y 7
        directorioItunes = System.getProperty("user.home");
        directorioItunes += File.separator + "Music" + File.separator + "iTunes"
                + File.separator + "iTunes Media" + File.separator + "Mobile Applications";
        dir = new File(directorioItunes);
        if (dir.exists() && dir.isDirectory()) {
            return directorioItunes;
        }

        // Directorios en Windows XP, diferenes lenguajes
        String _ = File.separator;
        String[] dmusica = new String[18];
        dmusica[0] = "My Documents" + _ + "My Music"; // Inglés
        dmusica[1] = "Mis Documentos" + _ + "Mi Música"; // Español
        dmusica[2] = "Mes documents" + _ + "Ma musique"; // Francés
        dmusica[3] = "Eigene Dokumente" + _ + "Eigene Musik"; // Alemán
        dmusica[4] = "Documenti" + _ + "Musica"; // Italiano
        dmusica[5] = "Mijn documenten" + _ + "Mijn muziek"; // Países bajos
        dmusica[6] = "Dokumenter" + _ + "Musik"; // Danés
        dmusica[7] = "マイ ドキュメント" + _ + "マイ ミュージック"; // Japonés
        dmusica[8] = "My Documents" + _ + "My Music"; // Chino
        dmusica[9] = "Omat tiedostot" + _ + "Omat musiikkitiedostot"; // Suomi
        dmusica[10] = "My Documents" + _ + "내 음악"; // Koreano
        dmusica[11] = "Mine dokumenter" + _ + "Min musikk"; // ¿?
        dmusica[12] = "Moje dokumenty" + _ + "Moja muzyka"; // Polaco
        dmusica[13] = "Meus documentos" + _ + "Minhas músicas"; // Portugués brasileño
        dmusica[14] = "Os meus documentos" + _ + "A minha música"; // Portugués
        dmusica[15] = "Мои документы" + _ + "Моя музыка"; // Ruso
        dmusica[16] = "Mina dokument" + _ + "Min musik"; // Sueco
        dmusica[17] = "My Documents" + _ + "我的音樂"; // Tailandés


        for (String dm : dmusica) {
            //Intentamos con el directorio de WinXP inglés
            directorioItunes = System.getProperty("user.home") + File.separator + dm
                    + File.separator + "iTunes" + File.separator
                    + "Mobile Applications" + File.separator;
            dir = new File(directorioItunes);
            if (dir.exists() && dir.isDirectory()) {
                return directorioItunes;
            }

            //Intentamos con el directorio de iTunes 9 de WinXP inglés
            directorioItunes = System.getProperty("user.home") + File.separator + dm
                    + File.separator + "iTunes" + File.separator
                    + "iTunes Media" + File.separator + "Mobile Applications" + File.separator;
            dir = new File(directorioItunes);

            if (dir.exists() && dir.isDirectory()) {
                return directorioItunes;
            }
        }
        return null;
    }

    public String obtenerDirectorioItunes() {
        return directorioItunes;
    }

    public void establecerDirectorioItunes(String dirItunes) {
        this.directorioItunes = dirItunes;
        if (this.directorioItunes != null) {
            AppSpyConfig.getInstance().guardarPropiedad("lastdir", this.directorioItunes);
        }
    }

    public boolean esDirectorioValido(String path) {
        File[] archivos = this.leerArchivosDirectorio(path);

        if (archivos != null) {
            return true;
        }

        return false;
    }
}
