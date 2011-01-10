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
package com.panchosoft.appspy;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

/**
 *
 * @author Panchosoft
 */
public class AppSpyConfig {

    // Variables necesarias
    private String ruta_archivo = (new java.io.File("appspy.xml")).getAbsolutePath();
    boolean existe = false;
    private Properties propiedades;
    private File archivo;
    // Singleton
    private static AppSpyConfig instancia;

    // Constructor de clase
    private AppSpyConfig() {
        // Intentamos cargar el archivo de configuración
        archivo = new File(ruta_archivo);
        if (!archivo.exists()) {
            try {
                if (archivo.createNewFile()) {
                    existe = true;
                    this.crearPropiedadesPredeterminadas();
                    this.guardarPropiedades();
                } else {
                    existe = false;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            // Cargamos las propiedes existentes
            try {
                existe = true;
                this.cargarPropiedades();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     *
     * @return La única instacia del objeto AppSpyConfig
     */
    public static AppSpyConfig getInstance() {
        if (instancia == null) {
            instancia = new AppSpyConfig();
        }
        return instancia;
    }

    private void crearPropiedadesPredeterminadas() {
        propiedades = new Properties();
        propiedades.setProperty("appspy.autoload", "false");
        propiedades.setProperty("appspy.totalsearchresults", "10");
    }

    private void guardarPropiedades() {
        OutputStream propsFile;
        try {
            propsFile = new FileOutputStream(archivo);
            propiedades.storeToXML(propsFile, "AppSpy properties");
            propsFile.close();
        } catch (IOException ioe) {
            System.out.println("I/O Exception " + ioe.getMessage());
            System.exit(0);
        }
    }

    /**
     *
     * @param nombre
     * @param valor
     */
    public void guardarPropiedad(String nombre, String valor) {
        if (this.propiedades == null) {
            return;
        }
        this.propiedades.setProperty(nombre, valor);

        this.guardarPropiedades();
    }

    /**
     *
     * @param nombre
     * @return
     */
    public String obtenerPropiedad(String nombre) {
        if (this.propiedades == null) {
            return null;
        }
        if (nombre == null) {
            return null;
        }
        return this.propiedades.getProperty(nombre);
    }

    private boolean cargarPropiedades() {
        InputStream propsFile;
        if (propiedades == null) {
            propiedades = new Properties();
        }

        try {
            propsFile = new FileInputStream(archivo);
            propiedades.loadFromXML(propsFile);
            propsFile.close();
            return true;
        } catch (IOException ioe) {
            System.out.println("I/O Exception: " + ioe.getMessage());
            System.exit(0);
        }
        return false;
    }
}
