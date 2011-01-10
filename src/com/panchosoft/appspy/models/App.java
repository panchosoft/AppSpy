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

import com.panchosoft.appspy.utils.Comparador;
import java.util.Map;
import java.io.File;

/**
 *
 * @author Francisco I. Leyva
 */
public final class App {

    // Atributos de clase
    private String nombreMostrado, ejecutable, icono, identificador,
            nombreEmbedido, version, SDK;
    private Double tamano;
    private File app_file;
    // Atributos cargados del archivo Info.plist
    private Map<String, Object> propiedades;

    private boolean actualizacionDisponible;
    private boolean ultimaVersion;
    private boolean actualizacionDesconocida;
    private boolean aplicacionIgnorada;


    private iTunesApp iapp; // iTunes Update App

    public App() {
    }

    /**
     *
     * @param diccionario
     */
    public App(Map<String, Object> diccionario) {
        this.propiedades = diccionario;
        //Inicializar las variables de clase con la info del diccionario
        this.setNombreMostrado(getAtributo("CFBundleDisplayName"));
        this.setEjecutable(getAtributo("CFBundleExecutable"));
        this.setIcono(getAtributo("CFBundleIconFile"));
        this.setIdentificador(getAtributo("CFBundleIdentifier"));
        this.setNombreEmbedido(getAtributo("CFBundleName"));

        if(getAtributo("CFBundleShortVersionString") != null){
            if(getAtributo("CFBundleVersion") != null){
                if(Comparador.getInstance().comparar(getAtributo("CFBundleShortVersionString"),
                        getAtributo("CFBundleVersion")) < 0){
                        this.setVersion(getAtributo("CFBundleVersion"));
                }else{
                    this.setVersion(getAtributo("CFBundleShortVersionString"));
                }
            }else{
                this.setVersion(getAtributo("CFBundleShortVersionString"));
            }
        }else{
            this.setVersion(getAtributo("CFBundleVersion"));
        }
        this.setSDK(getAtributo("DTSDKName"));
    }

    public String getAtributo(String clave) {
        return (String) propiedades.get(clave);
    }

    public String getEjecutable() {
        return ejecutable;
    }

    public void setEjecutable(String ejecutable) {
        this.ejecutable = ejecutable;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombreEmbedido() {
        return nombreEmbedido;
    }

    public void setNombreEmbedido(String nombreEmbedido) {
        this.nombreEmbedido = nombreEmbedido;
    }

    public String getNombreMostrado() {
        if(nombreMostrado != null && nombreMostrado.length() > 0)
        return nombreMostrado;

        return this.ejecutable;
    }

    public void setNombreMostrado(String nombreMostrado) {
        this.nombreMostrado = nombreMostrado;
    }

    public Map<String, Object> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Map<String, Object> propiedades) {
        this.propiedades = propiedades;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAppFile(File file) {
        this.app_file = file;
    }

    public File getAppFile() {
        return this.app_file;
    }

    @Override
    public String toString() {
        return this.getNombreMostrado() + " (" + this.getVersion() + ")";
    }

    public double getTamano() {
        if (this.tamano == null) {
            double tam = getAppFile().length();
            tam /= 1024;
            tam /= 1024;
            tamano = tam;
        }
        return tamano;
    }

    private void setTamano(Double tamano) {
        this.tamano = tamano;
    }

    public String getSDK() {
        return SDK;
    }

    public void setSDK(String SDK) {
        this.SDK = SDK;
    }

    /**
     * @return the actualizacionDisponible
     */
    public boolean isActualizacionDisponible() {
        return actualizacionDisponible;
    }

    /**
     * @param actualizacionDisponible the actualizacionDisponible to set
     */
    public void setActualizacionDisponible(boolean actualizacionDisponible) {
        this.actualizacionDisponible = actualizacionDisponible;
    }

    /**
     * @return the iapp
     */
    public iTunesApp getIapp() {
        return iapp;
    }

    /**
     * @param iapp the iapp to set
     */
    public void setIapp(iTunesApp iapp) {
        this.iapp = iapp;
    }

    /**
     * @return the ultimaVersion
     */
    public boolean isUltimaVersion() {
        return ultimaVersion;
    }

    /**
     * @param ultimaVersion the ultimaVersion to set
     */
    public void setUltimaVersion(boolean ultimaVersion) {
        this.ultimaVersion = ultimaVersion;
    }

    /**
     * @return the actualizacionDesconocida
     */
    public boolean isActualizacionDesconocida() {
        return actualizacionDesconocida;
    }

    /**
     * @param actualizacionDesconocida the actualizacionDesconocida to set
     */
    public void setActualizacionDesconocida(boolean actualizacionDesconocida) {
        this.actualizacionDesconocida = actualizacionDesconocida;
    }

    public boolean isAplicacionIgnorada() {
        return aplicacionIgnorada;
    }

    public void setAplicacionIgnorada(boolean aplicacionIgnorada) {
        this.aplicacionIgnorada = aplicacionIgnorada;
    }

}
