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
package com.panchosoft.appspy.controllers;

import com.panchosoft.appspy.AppSpyApp;
import com.panchosoft.appspy.AppSpyConfig;
import com.panchosoft.appspy.models.App;
import com.panchosoft.appspy.models.AppTrackerProxy;
import com.panchosoft.appspy.models.Verificador;
import com.panchosoft.appspy.utils.Comparador;
import com.panchosoft.appspy.models.Espia;
import com.panchosoft.appspy.models.iTunesApp;
import com.panchosoft.appspy.models.iTunesProxy;
import com.panchosoft.appspy.utils.ImagenesUtil;
import com.panchosoft.appspy.views.AppSpyVista;

import java.util.ArrayList;

/**
 *
 * @author Panchosoft
 */
public class AppSpyController {

    private static AppSpyController instancia;
    private AppSpyVista vista;

    private AppSpyController() {
        vista = AppSpyApp.getMainFrame();
    }

    /**
     *
     * @return
     */
    public static synchronized AppSpyController getInstance() {
        if (instancia == null) {
            instancia = new AppSpyController();
        }
        return instancia;
    }

    public boolean cargarListaApps() {
        // Obtenemos la lista de aplicaciones del equipo
        Espia importer = Espia.getInstance();

        // Verificamos si se encontró el Directorio de iTunes
        if (importer.buscarDirectorioItunes() == null || AppSpyApp.getMainFrame().shiftPresionado) {
            vista.obtenerDirItunes();
            return false;
        }

        //Cargar lista de Apps
        ArrayList<App> apps = importer.getApps();

        // Hacemos un bind con la lista de la vista
        vista.llenarListaApps(apps.toArray());
        vista.finalizarLlenadoLista();
        vista.ocultarCargandoApps();

        return true;
    }

    public String obtenerDirectorioItunes() {
        // Obtenemos la lista de aplicaciones del equipo
        Espia importer = Espia.getInstance();
        return importer.obtenerDirectorioItunes();
    }

    public void cargarListaApps(String path) {
        // Obtenemos la lista de aplicaciones del equipo
        Espia importer = Espia.getInstance();

        //Cargar lista de Apps
        importer.establecerDirectorioItunes(path);
        ArrayList<App> apps = importer.getApps();

        // Hacemos un bind con la lista de la vista
        vista.llenarListaApps(apps.toArray());
        vista.finalizarLlenadoLista();
        vista.ocultarCargandoApps();
    }

    public void buscarActualizaciones() {
        // Olvidamos las actualizaciones existentes
        olvidarActualizaciones();

        // Obtenemos la lista de aplicaciones del equipo
        Espia importer = Espia.getInstance();

        //Cargar lista de Apps
        ArrayList<App> apps = importer.getApps();

        int i = 0, total_updates = 0;
        for (App app : apps) {

            // Aplicación de iTunes
            iTunesApp iapp;


            String configAppID = AppSpyConfig.getInstance().obtenerPropiedad(app.getIdentificador());
            if (configAppID != null && configAppID.length() > 0) {
                if (configAppID.equals("ignored")) {
                    iapp = null;
                } else {
                    iapp = iTunesProxy.getInstance().obtenerAppPorID(configAppID);
                }
            } else {
                iapp = iTunesProxy.getInstance().obtenerApp(app.getNombreMostrado());
            }


            if (iapp != null) {
                if (Comparador.getInstance().comparar(app.getVersion(), iapp.getVersion()) < 0) {

                    // Actualizamos el estado
                    app.setActualizacionDisponible(true);
                    app.setIapp((iapp));

                    // Actualizamos el porcentaje
                    int porcentaje = (int) (((double) i / (double) apps.size()) * (double) 100);
                    vista.mostrarPorcentaje(porcentaje);
                    total_updates++;
                } else if (Comparador.getInstance().comparar(app.getVersion(), iapp.getVersion()) >= 0) {
                    app.setIapp(iapp);
                    app.setUltimaVersion(true);
                }
            } else {
                if (configAppID != null && configAppID.equals("ignored")) {
                    app.setAplicacionIgnorada(true);
                } else {
                    app.setActualizacionDesconocida(true);
                }
            }
            // Actualizamos la lista de la ventana
            vista.llenarListaApps(apps.toArray());
            vista.finalizarLlenadoLista();
            i++;
        }
        vista.ocultarCargandoActualizaciones(total_updates);
        vista.buscandoActualizaciones = false;
    }

    public void establecerPais(String codigo_pais) {
        iTunesProxy.getInstance().codigoPais = codigo_pais;
    }

    public void relacionarAplicaciones(Object app, Object buscado) {
        App aplicacion = (App) app;
        iTunesApp actualizacion = (iTunesApp) buscado;
        aplicacion.setIapp(actualizacion);

        if (Comparador.getInstance().comparar(aplicacion.getVersion(), actualizacion.getVersion()) < 0) {
            aplicacion.setActualizacionDisponible(true);
            aplicacion.setActualizacionDesconocida(false);
            aplicacion.setUltimaVersion(false);
        } else {
            aplicacion.setActualizacionDisponible(false);
            aplicacion.setActualizacionDesconocida(false);
            aplicacion.setUltimaVersion(true);
        }
        AppSpyApp.getMainFrame().repaint();

        AppSpyConfig.getInstance().guardarPropiedad(aplicacion.getIdentificador(), actualizacion.getiTunesId());
    }

    public void obtenerLinksApptrackr(String url) {
        // Obtenemos los enlaces de Apptrackr
        AppTrackerProxy.getInstance().obtenerEnlaces(url);

        // Guardamos referencia
        vista.enlaces = AppTrackerProxy.getInstance().enlaces;
        vista.hostings = AppTrackerProxy.getInstance().hostings;
        vista.versiones = AppTrackerProxy.getInstance().versiones;
    }

    public void ignorarAplicacion(Object app) {
        // Actualizamos la información en la vista
        App aplicacion = (App) app;
        aplicacion.setActualizacionDisponible(false);
        aplicacion.setActualizacionDesconocida(false);
        aplicacion.setUltimaVersion(false);
        aplicacion.setAplicacionIgnorada(true);
        AppSpyApp.getMainFrame().repaint();

        // Guardamos la configuración
        AppSpyConfig.getInstance().guardarPropiedad(aplicacion.getIdentificador(), "ignored");
    }

    public void eliminarAplicacion(Object app) {
        // Si actualmente se están buscando actualizaciones
        // evitamos que se elimine una aplicación
        if (vista.buscandoActualizaciones) {
            return;
        }

        // Actualizamos las propiedades de la aplicación
        App aplicacion = (App) app;
        aplicacion.setActualizacionDisponible(false);
        aplicacion.setActualizacionDesconocida(false);
        aplicacion.setUltimaVersion(false);
        aplicacion.setAplicacionIgnorada(false);

        // Intentamos eliminar el archivo de la aplicación
        try {
            java.io.File fapp = aplicacion.getAppFile();
            if (fapp.delete()) {
                //Cargar lista de Apps
                ArrayList<App> apps = Espia.getInstance().getApps();
                apps.remove(aplicacion);
                // Hacemos un bind con la lista de la vista
                vista.llenarListaApps(apps.toArray());
            } else {
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        // Actualizamos la vista
        AppSpyApp.getMainFrame().repaint();
    }

    public boolean ejecutarBusqueda(String clave) {
        //Cargar lista de Apps
        iTunesApp[] resultados = iTunesProxy.getInstance().buscarApp(clave, 10);

        if (resultados != null) {
            vista.llenarListaBusqueda(resultados);
            vista.mostrarIconoBuscando(false);
            vista.mostrarResultados(resultados.length);
        } else {
            vista.mostrarResultados(0);
            vista.mostrarIconoBuscando(false);
        }

        return true;
    }

    public void cargarInformacionEnVista(int indice) {
        // Obtenemos la App del índice seleccionado
        final App app = Espia.getInstance().getApps().get(indice);

        vista.llenarInformacion(app.getNombreMostrado(),
                null,
                app.getVersion(), app.getIdentificador(),
                app.getAppFile().getPath(), app.getEjecutable(),
                app.getTamano(), app.getSDK());

        new Thread(new Runnable() {

            @Override
            public void run() {
                vista.mostrarIcono(Espia.getInstance().getAppIcon(app, 128));
            }
        }).start();

        if (app.isActualizacionDisponible()) {
            vista.mostrarActualizacion(app.getIapp().getTrackName(), app.getIapp().getVersion(),
                    app.getIapp().getDescription(), app.getIapp().getPrice(), app.getIapp().getFileSizeBytes(),
                    app.getIapp().getArtworkUrl60(), app.getIapp().getTrackViewUrl(), app.getIapp().getiTunesId());
        } else if (app.isActualizacionDesconocida()) {
            vista.mostrarVersionDesconocida();
        } else if (app.isUltimaVersion()) {
            vista.mostrarUltimaVersion(app.getIapp().getTrackViewUrl(),
                    app.getIapp().getiTunesId(), app.getIapp().getDescription(),
                    app.getIapp().getArtworkUrl60());
        }
    }

    public void olvidarActualizaciones() {
        // Obtenemos la lista de aplicaciones del equipo
        Espia importer = Espia.getInstance();

        //Cargar lista de Apps
        ArrayList<App> apps = importer.getApps();

        int i = 0;
        for (App app : apps) {
            app.setIapp(null);
            app.setActualizacionDisponible(false);
            app.setActualizacionDesconocida(false);
            app.setUltimaVersion(false);
            i++;
        }
        // Actualizamos la lista de la ventana
        vista.llenarListaApps(apps.toArray());
        vista.finalizarLlenadoLista();
    }

    public void olvidarAplicaciones() {
        // Obtenemos la lista de aplicaciones del equipo
        Espia importer = Espia.getInstance();
        importer.olvidarApps();
    }

    public boolean esDirectorioValido(String path) {
        return Espia.getInstance().esDirectorioValido(path);
    }

    public javax.swing.ImageIcon redimensionar(javax.swing.ImageIcon imagen, int ancho, int alto) {
        return ImagenesUtil.getInstance().redimensionarImagen(imagen, alto, alto);
    }

    public void buscarNuevaVersion() {
        boolean nueva = Verificador.getInstance().nuevaVersionDisponible();
        if (nueva) {
            vista.mostrarNuevaVersion();
        }
    }
}
