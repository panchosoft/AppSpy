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
package com.panchosoft.appspy.views;

import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.awt.Desktop;
import com.panchosoft.appspy.AppSpyApp;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 *
 * @author Francisco I. Leyva
 */
public class AppSpyVista extends javax.swing.JFrame {

    javax.swing.ImageIcon icon;
    private javax.swing.JDialog dirItunesView;
    private String itunesURL = "";
    private String apptrackrURL = "";
    public String itunesAppURL = "";
    private String itunesBusquedaURL = "";
    private String apptrackrBusquedaURL = "";
    public boolean buscandoActualizaciones = false;
    public boolean buscandoEnlaces = false;
    public boolean shiftPresionado = false;
    private boolean listaPaisesCargada = false;
    public ArrayList<String> versiones;
    public ArrayList<String> enlaces;
    public ArrayList<String> hostings;
    // Códigos de las tiendas
    public LinkedHashMap<String, String> tiendas = new LinkedHashMap<String, String>();

    public AppSpyVista() {
        icon = new javax.swing.ImageIcon(this.getClass().getResource("/com/panchosoft/appspy/resources/images/appspy_background.png"));

        FondoVista contentPane = new FondoVista();
        contentPane.setBackgroundImage(icon.getImage());

        this.setContentPane(contentPane);

        initComponents();
        iniciarComponentes();

    }

    public void iniciarComponentes() {

        this.btnNueva.setVisible(false);
        this.separador5.setVisible(false);
        listaApps.getSelectionModel().addListSelectionListener(new SeleccionadorListaApps());
        listaApps.setCellRenderer(null);
        listaApps.setCellRenderer(new RenderizadorListaApps());

        listaBusqueda.getSelectionModel().addListSelectionListener(new SeleccionadorListaBusqueda());
        listaBusqueda.setCellRenderer(null);
        listaBusqueda.setCellRenderer(new RenderizadorListaBusqueda());

        this.lblNewestVersion.setVisible(false);
        this.lblTituloNueva.setVisible(false);
        this.lblUpdateImg.setVisible(false);
        this.lblTituloPrecio.setVisible(false);
        this.lblTituloTamano.setVisible(false);
        this.lblObtener.setVisible(false);
        this.lblDescripcion.setVisible(false);
        this.lblItunes.setVisible(false);
        this.lblApptrackr.setVisible(false);
        this.panelDescripcion.setVisible(false);
        this.lblTituloDescripcion.setVisible(false);
        this.lblPrecio.setVisible(false);
        this.lblTamano.setVisible(false);
        this.panelTabs.setBackgroundAt(0, Color.darkGray);
        this.panelTabs.setBackgroundAt(1, Color.darkGray);

        this.panelTabs.removeAll();
        this.panelTabs.addTab("update information", this.tabInfo);
        this.panelTabs.addTab("search", this.tabBuscar);


        // Deshabilitamos los componentes que no se pueden utilizar aun
        this.panelInfo.setVisible(false);
        this.menu.setVisible(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        if (esMac()) {
            this.setSize(this.getSize().width + 5, this.getSize().height);
            this.panelTabs.setSize(250, 500);
            this.panelTabs.setBounds(-15, 0, 250, 500);
        }

        this.panelActualizacion.setVisible(false);
//        this.cargarApps();


        // Respuesta del teclado
        this.setFocusable(true);
        this.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == 16) {
                    shiftPresionado = true;
//                    System.out.println("presionando..");
                    e.consume();
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == 16) {
                    shiftPresionado = false;
//                    System.out.println("liberando..");
                    e.consume();
                }
            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
            }
        });

        this.txtAppBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    if (AppSpyApp.getMainFrame().tiendas != null && AppSpyApp.getMainFrame().listaPaisesCargada) {
                        // Obtenemos el código del país
                        String codigo_pais = AppSpyApp.getMainFrame().tiendas.get(AppSpyApp.getMainFrame().txtPaises.getSelectedItem().toString());
                        AppSpyApp.getController().establecerPais(codigo_pais);
                    }
                    String clave = AppSpyApp.getMainFrame().txtAppBusqueda.getText();
                    AppSpyApp.getMainFrame().ejecutarBusqueda(clave);
                    AppSpyApp.getController().establecerPais("US");
                    e.consume();
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
//                if (e.getKeyCode() == 16) {
//
//                    System.out.println("enter.li..");
//                    e.consume();
//                }
            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
            }
        });

        // Cargamos las tiendas y sus códigos
        this.tiendas.put("USA", "US");
        this.tiendas.put("Argentina", "AR");
        this.tiendas.put("Armenia", "AM");
        this.tiendas.put("Australia", "AU");
        this.tiendas.put("Belgium", "BE");
        this.tiendas.put("Botswana", "BW");
        this.tiendas.put("Brazil", "BR");
        this.tiendas.put("Bulgaria", "BG");
        this.tiendas.put("Canada", "CA");
        this.tiendas.put("Chile", "CL");
        this.tiendas.put("China", "CN");
        this.tiendas.put("Colombia", "CO");
        this.tiendas.put("Costa Rica", "CR");
        this.tiendas.put("Croatia", "HR");
        this.tiendas.put("Czech Republic", "CS");
        this.tiendas.put("Denmark", "DK");
        this.tiendas.put("Deutschland", "DE");
        this.tiendas.put("Dominican Republic", "DM");
        this.tiendas.put("Ecuador", "EC");
        this.tiendas.put("Egypt", "EG");
        this.tiendas.put("El Salvador", "SV");
        this.tiendas.put("España", "ES");
        this.tiendas.put("Estonia", "EW");
        this.tiendas.put("Finland", "FI");
        this.tiendas.put("France", "FR");
        this.tiendas.put("Greece", "GR");
        this.tiendas.put("Guatemala", "GT");
        this.tiendas.put("Honduras", "HN");
        this.tiendas.put("Hong Kong", "HK");
        this.tiendas.put("Hungary", "HU");
        this.tiendas.put("India", "IN");
        this.tiendas.put("Indonesia", "ID");
        this.tiendas.put("Ireland", "IE");
        this.tiendas.put("Israel", "IL");
        this.tiendas.put("Italy", "IT");
        this.tiendas.put("Jamaica", "JM");
        this.tiendas.put("Vietnam", "VN");
        this.tiendas.put("Japan", "JP");
        this.tiendas.put("Jordan", "JO");
        this.tiendas.put("Kazakhstan", "KZ");
        this.tiendas.put("Kenya", "KE");
        this.tiendas.put("Korea", "KR");
        this.tiendas.put("Kuwait", "KW");
        this.tiendas.put("Latvia", "LV");
        this.tiendas.put("Lebanon", "LB");
        this.tiendas.put("Lithuania", "LT");
        this.tiendas.put("Luxembourg", "LU");
        this.tiendas.put("Macau", "MO");
        this.tiendas.put("Macedonia", "MK");
        this.tiendas.put("Madagascar", "MG");
        this.tiendas.put("Malaysia", "MY");
        this.tiendas.put("Mali", "ML");
        this.tiendas.put("Mauritius", "MU");
        this.tiendas.put("Mexico", "MX");
        this.tiendas.put("Netherlands", "NL");
        this.tiendas.put("New Zealand", "NZ");
        this.tiendas.put("Nicaragua", "NI");
        this.tiendas.put("Nigeria", "NI");
        this.tiendas.put("Norway", "NO");
        this.tiendas.put("Pakistan", "PK");
        this.tiendas.put("Panama", "PA");
        this.tiendas.put("Paraguay", "PY");
        this.tiendas.put("Peru", "PE");
        this.tiendas.put("Philippines", "PH");
        this.tiendas.put("Poland", "PL");
        this.tiendas.put("Portugal", "PT");
        this.tiendas.put("Qatar", "QA");
        this.tiendas.put("Republoc of Malta", "MT");
        this.tiendas.put("Republic of Moldova", "MD");
        this.tiendas.put("Romania", "RO");
        this.tiendas.put("Russia", "RU");
        this.tiendas.put("Saudi Arabia", "SA");
        this.tiendas.put("Schweiz", "CH");
        this.tiendas.put("Senegal", "SN");
        this.tiendas.put("Singapore", "SG");
        this.tiendas.put("Slovakia", "SK");
        this.tiendas.put("Slovenia", "SI");
        this.tiendas.put("South Africa", "ZA");
        this.tiendas.put("Sri Lanka", "LK");
        this.tiendas.put("Sweden", "SE");
        this.tiendas.put("Taiwan", "TW");
        this.tiendas.put("Thailand", "TH");
        this.tiendas.put("Tunisia", "TN");
        this.tiendas.put("Turkey", "TR");
        this.tiendas.put("Uganda", "UG");
        this.tiendas.put("United Arab Emirates", "AE");
        this.tiendas.put("Ukraine", "UK");
        this.tiendas.put("Uruguay", "UY");
        this.tiendas.put("Venezuela", "VE");


        this.txtPaises.removeAllItems();
        for (Object pais : tiendas.keySet().toArray()) {
            this.txtPaises.addItem(pais.toString());
        }
        this.listaPaisesCargada = true;

        Thread hiloNuevaVersion = new java.lang.Thread(new Runnable() {

            @Override
            public void run() {
//               System.out.println("corriendo hilo...");
                try {
                    Thread.currentThread().sleep(5000);
                } catch (Exception ex) {
                }
                AppSpyApp.getController().buscarNuevaVersion();
//                    System.out.println("terminado...");
            }
        }, "hilo_nueva_version");
        hiloNuevaVersion.start();
    }

    public void mostrarCargandoView() {
    }

    public void borrarListaApps() {
        this.listaApps.setModel(new javax.swing.DefaultListModel());
        this.panelActualizacion.setVisible(false);
    }

    public void llenarListaApps(Object[] elementos) {
        // Obtenemos el número de indice seleccionado
        int indice = listaApps.getMinSelectionIndex();

        // Borramos todos los elementos
//        this.listaApps.removeAll();

        // Agregamos el nuevo modelo
        javax.swing.DefaultListModel modelo = new javax.swing.DefaultListModel();
        for (Object elem : elementos) {
            if (elem != null) {
                modelo.addElement(elem);
            }
        }
        this.listaApps.setModel(modelo);
        this.lblItunesApps.setText("iTunes apps (" + modelo.size() + ")");

        // Volvemos a seleccionar el indice
//        listaApps.setValueIsAdjusting(true);
        if (!esMac()) {
            listaApps.setSelectedIndex(indice);
        }
//        listaApps.setValueIsAdjusting(false);
    }

    public void finalizarLlenadoLista() {

        // Actualizamos y activamos algunos componentes de la GUI

        this.btnEncontrarApps.setEnabled(false);
        this.btnEncontrarAct.setEnabled(true);

    }

    public void llenarListaBusqueda(Object[] elementos) {
        // Borramos todos los elementos
//        this.listaApps.removeAll();

        // Agregamos el nuevo modelo
        javax.swing.DefaultListModel modelo = new javax.swing.DefaultListModel();
        for (Object elem : elementos) {
            if (elem != null) {
                modelo.addElement(elem);
            }
        }
        this.listaBusqueda.setModel(modelo);


        // Actualizamos y activamos algunos componentes de la GUI
//        this.lblItunesApps.setText("iTunes apps (" + modelo.size() + ")");
//        this.btnEncontrarApps.setEnabled(false);
//        this.btnEncontrarAct.setEnabled(true);

        // Volvemos a seleccionar el indice
//        listaApps.setValueIsAdjusting(true);
//        if (!esMac()) {
//            listaApps.setSelectedIndex(indice);
//        }
//        listaApps.setValueIsAdjusting(false);
    }

    public void establecerLinksBusqueda(String itunesurl, String apptrackrurl) {
        this.itunesBusquedaURL = itunesurl;
        this.apptrackrBusquedaURL = "http://apptrackr.org/?act=viewapp&appid=" + apptrackrurl + "&AppSpy";
    }

    public void relacionarAplicacion() {

        // Vericamos que hay dos aplicaciones seleccionadas
        if (listaApps.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "You need to select an application from the iTunes Apps list.", "AppSpy", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (listaBusqueda.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "You need to select the application from the search results list that matches the current app.", "AppSpy", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtenemos el objeto seleccionado en la lista de la búsqueda
        Object buscado = listaBusqueda.getSelectedValue();
        Object app = listaApps.getSelectedValue();

        // Hacemos la relación en el controlador
        AppSpyApp.getController().relacionarAplicaciones(app, buscado);
    }

    public void ignorarAplicacion() {

        // Vericamos que hay aplicaciones seleccionadas
        if (listaApps.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "You need to select an application from the iTunes Apps list.", "AppSpy", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtenemos el objeto seleccionado
        Object app = listaApps.getSelectedValue();

        // Hacemos la relación en el controlador
        AppSpyApp.getController().ignorarAplicacion(app);
    }

    public void eliminarAplicacion() {

        // Vericamos que hay aplicaciones seleccionadas
        if (listaApps.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "You need to select an application from the iTunes Apps list.", "AppSpy", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtenemos el objeto seleccionado
        Object app = listaApps.getSelectedValue();

        // Hacemos la relación en el controlador
        AppSpyApp.getController().eliminarAplicacion(app);
    }

    // <editor-fold defaultstate="collapsed" desc="Elementos visuales">
    public void llenarInformacion(String nombre, javax.swing.ImageIcon icono, String version,
            String identificador, String ruta, String ejecutable, Double tamano, String SDK) {

        // Mostramos el panel si está escondido
        if (!panelInfo.isVisible()) {
            panelInfo.setVisible(true);
        }

        // Adaptamos ciertos campos
        String tamanio = this.redondear(tamano, 1) + " MB";
        // Cargamos la info
        this.lblAppName.setText(nombre);
        this.lblAppName.setToolTipText(nombre);
        this.lblAppVersion.setText(version);
        this.lblAppEjecutable.setText(ejecutable);
        this.lblAppIdentificador.setText(identificador);
        this.lblAppUbicacion.setText(ruta);
        this.lblAppUbicacion.setToolTipText(ruta);
        this.lblImagen.setIcon(icono);
        this.lblAppSize.setText(tamanio);
        this.lblAppSDK.setText(SDK);

        this.panelActualizacion.setVisible(false);
        this.enlaces = null;
        this.versiones = null;
        this.hostings = null;
//         AppSpyApp.getController().detenerHilo("hilo_buscando_links");
        this.buscandoEnlaces = false;
        this.limpiarPopup();

    }

    public void mostrarIcono(javax.swing.ImageIcon icono) {
        this.lblImagen.setIcon(icono);
    }

    public void mostrarActualizacion(String nombre, String version, String descripcion, String precio, String tamano,
            String img, String itunesURL, String apptrackrURL) {
        this.lblUpdateImg.setIcon(null);
        this.lblNewestVersion.setVisible(true);
        this.lblTituloNueva.setVisible(true);
        this.lblTituloPrecio.setVisible(true);
        this.lblTituloTamano.setVisible(true);
        this.lblObtener.setVisible(true);
        this.lblUpdateImg.setVisible(true);
        this.lblDescripcion.setVisible(true);

        this.lblTituloDescripcion.setVisible(true);
        this.lblPrecio.setVisible(true);
        this.lblItunes.setVisible(true);
        this.lblApptrackr.setVisible(true);

        try {
            Double mb = Double.parseDouble(tamano);
            mb /= 1024;
            mb /= 1024;
            this.lblTamano.setVisible(true);
            NumberFormat formato = new DecimalFormat("#.#");
            this.lblTamano.setText("" + formato.format(mb) + " MB");
        } catch (Exception ex) {
        }


        this.itunesAppURL = img;
        new java.lang.Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    java.net.URL where = new java.net.URL(AppSpyApp.getMainFrame().itunesAppURL);
                    javax.swing.ImageIcon icono = new javax.swing.ImageIcon(where);
                    AppSpyApp.getMainFrame().lblUpdateImg.setIcon(icono);
                } catch (Exception ex) {
                }
            }
        }).start();

        this.lblNewestVersion.setText(version);
        this.lblNewestVersion.setVisible(true);
        String decodificada = descripcion;
        try {
            byte[] utf8 = decodificada.getBytes("UTF-8");
            this.lblDescripcion.setText(new String(utf8, "UTF-8"));
        } catch (Exception ex) {
        }

        this.lblDescripcion.setCaretPosition(0);
        this.panelDescripcion.setVisible(true);
        this.lblPrecio.setText("$" + precio);
        this.itunesURL = itunesURL;

        this.apptrackrURL = "http://apptrackr.org/?act=viewapp&appid=" + apptrackrURL + "&AppSpy";

        this.panelTabs.removeAll();
        this.panelTabs.addTab("update information", this.tabInfo);
        this.panelTabs.addTab("search (or fix)", this.tabBuscar);
        this.panelActualizacion.setVisible(true);
    }

    public void mostrarUltimaVersion(String itunesURL, String apptrackrURL, String description, final String imagen) {
//        this.lblTituloAct.setText("this app is updated!");
        this.lblNewestVersion.setVisible(false);
        this.lblTituloNueva.setVisible(false);
        this.lblUpdateImg.setVisible(false);
        this.lblTituloPrecio.setVisible(false);
        this.lblTituloTamano.setVisible(false);
        this.lblObtener.setVisible(false);
        this.lblDescripcion.setVisible(false);
        this.lblItunes.setVisible(false);
        this.lblApptrackr.setVisible(false);
        this.panelDescripcion.setVisible(false);
        this.lblTituloDescripcion.setVisible(false);
        this.lblPrecio.setVisible(false);
        this.lblTamano.setVisible(false);

        this.itunesURL = itunesURL;

        this.apptrackrURL = "http://apptrackr.org/?act=viewapp&appid=" + apptrackrURL + "&AppSpy";

        this.panelTabs.removeAll();
        this.panelTabs.addTab("last version", this.tabLast);
        this.panelTabs.addTab("search (or fix)", this.tabBuscar);

        this.lblDescripcion1.setText(description);
        this.lblDescripcion1.setCaretPosition(0);
        this.panelActualizacion.setVisible(true);

        new java.lang.Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    java.net.URL where = new java.net.URL(imagen);
                    javax.swing.ImageIcon icono = new javax.swing.ImageIcon(where);
                    AppSpyApp.getMainFrame().lblUltimaImagen.setIcon(icono);
                } catch (Exception ex) {
                }
            }
        }).start();

//        this.itunesBusquedaURL = itunesurl;
//        this.apptrackrBusquedaURL = "http://apptrackr.org/?act=viewapp&appid=" + apptrackrurl + "&AppSpy";
    }

    public void mostrarVersionDesconocida() {
//        this.lblTituloAct.setText("this app update is unknown");
        this.lblNewestVersion.setVisible(false);
        this.lblTituloNueva.setVisible(false);
        this.lblUpdateImg.setVisible(false);
        this.lblTituloPrecio.setVisible(false);
        this.lblTituloTamano.setVisible(false);
        this.lblObtener.setVisible(false);
        this.lblDescripcion.setVisible(false);
        this.lblItunes.setVisible(false);
        this.lblApptrackr.setVisible(false);
        this.panelDescripcion.setVisible(false);
        this.lblTituloDescripcion.setVisible(false);
        this.lblPrecio.setVisible(false);
        this.lblTamano.setVisible(false);

//        this.lblTituloSearch.setVisible(true);
        this.panelTabs.removeAll();
        this.panelTabs.addTab("not found", this.tabDesc);
        this.panelTabs.addTab("fix error", this.tabBuscar);
        this.panelActualizacion.setVisible(true);
    }

    public void mostrarCargandoApps() {
        this.btnEncontrarAct.setEnabled(false);
        this.btnEncontrarApps.setText("loading apps...");
        this.btnEncontrarApps.setIcon(
                new javax.swing.ImageIcon(this.getClass().getResource("/com/panchosoft/appspy/resources/images/cargando.gif")));
    }

    public void mostrarCargandoActualizaciones() {
        this.btnEncontrarAct.setText("searching updates...");
        this.btnEncontrarAct.setIcon(
                new javax.swing.ImageIcon(this.getClass().getResource("/com/panchosoft/appspy/resources/images/cargando.gif")));
    }

    public void ocultarCargandoApps() {
        this.btnEncontrarApps.setText("reload apps");
        this.btnEncontrarApps.setIcon(
                new javax.swing.ImageIcon(this.getClass().getResource("/com/panchosoft/appspy/resources/images/reload.png")));
    }

    public void ocultarCargandoActualizaciones(int total) {
        this.btnEncontrarApps.setEnabled(true);
        this.btnEncontrarAct.setText(total + " updates found");
        this.btnEncontrarAct.setIcon(
                new javax.swing.ImageIcon(this.getClass().getResource("/com/panchosoft/appspy/resources/images/valida.png")));
    }

    public void mostrarPorcentaje(int porcentaje) {
        this.btnEncontrarAct.setText("searching updates (" + porcentaje + "%)");
    }

    public void cancelarCargando() {
        this.ocultarCargandoApps();
    }

    public void borrarBusqueda() {
        this.txtAppBusqueda.setText("");
        this.listaBusqueda.setModel(new javax.swing.DefaultListModel());
        this.lblResultadosBusqueda.setText("results");
        this.lbliTunesBusqueda.setEnabled(false);
        this.lblApptrackrBusqueda.setEnabled(false);
        this.lblUltimaImagen.setIcon(null);
    }

    public void activarBotonRelacionar() {
        if (listaApps.getSelectedIndex() >= 0 && listaBusqueda.getSelectedIndex() >= 0) {
            this.btnRelacionar.setEnabled(true);
        } else {
            this.btnRelacionar.setEnabled(false);
        }
    }

    public void establecerTextoBotonRelacionar(String texto) {
        this.btnRelacionar.setText(texto);
    }
// </editor-fold>

    public void obtenerDirItunes() {
        if (dirItunesView == null) {
            dirItunesView = new AppSpyBuscador(this);
            dirItunesView.setLocationRelativeTo(this);
        }
        this.dirItunesView.setVisible(true);
    }

    public double redondear(double nD, int nDec) {
        return Math.round(nD * Math.pow(10, nDec)) / Math.pow(10, nDec);
    }

    private void cargarApps() {
        // Cargamos las aplicaciones en un hilo separado para no congelar la UI
        Thread hiloCargado = new java.lang.Thread(new Runnable() {

            @Override
            public void run() {
                mostrarCargandoApps();
                AppSpyApp.getMainFrame().borrarListaApps();
                AppSpyApp.getController().olvidarAplicaciones();

                boolean lista_cargada = AppSpyApp.getController().cargarListaApps();

                // Buscamos actualizaciones
                if (lista_cargada) {
                    AppSpyApp.getMainFrame().buscandoActualizaciones = true;
                    mostrarCargandoActualizaciones();
                    AppSpyApp.getController().buscarActualizaciones();
                }
            }
        }, "hilo_busqueda");
        hiloCargado.start();
    }

    public void cargarActualizaciones() {
        if (this.buscandoActualizaciones) {
            return;
        }

        // Cargamos las aplicaciones en un hilo separado para no congelar la UI
        Thread hiloCargado = new java.lang.Thread(new Runnable() {

            @Override
            public void run() {
                // Buscamos actualizaciones
                AppSpyApp.getMainFrame().buscandoActualizaciones = true;
                mostrarCargandoActualizaciones();
                AppSpyApp.getController().buscarActualizaciones();
            }
        }, "hilo_actualizaciones");

        hiloCargado.start();
    }

    public void ejecutarBusqueda(final String clave) {
        this.borrarBusqueda();
        this.txtAppBusqueda.setText(clave);
        this.mostrarIconoBuscando(true);
        // Realizamos las búsqueda en un hilo separado
        Thread hiloCargado = new java.lang.Thread(new Runnable() {

            @Override
            public void run() {
                AppSpyApp.getController().ejecutarBusqueda(clave);
            }
        }, "hilo_buscar");
        hiloCargado.start();
    }

    public void ejecutarBusquedaLinks(Object fuente) {
        if (this.enlaces != null) {
            this.popupApptrackr.show((javax.swing.JComponent) fuente, 15, 15);
            return;
        }
        if (this.buscandoEnlaces) {
            this.limpiarPopup();
            this.popupApptrackr.show((javax.swing.JComponent) fuente, 15, 15);
            return;
        }
        if (this.popupApptrackr.isVisible()) {
            this.popupApptrackr.setVisible(false);
            return;
        }

        // Borramos los enlaces anteriormente encontrados
        this.limpiarPopup();

        // Mostramos el popup
        ((javax.swing.JMenuItem) this.popupApptrackr.getComponent(1)).setText("getting links...");
        ((javax.swing.JMenuItem) this.popupApptrackr.getComponent(1)).setIcon(new javax.swing.ImageIcon(
                this.getClass().getResource("/com/panchosoft/appspy/resources/images/cargando16.gif")));
        this.popupApptrackr.pack();
        this.popupApptrackr.show((javax.swing.JComponent) fuente, 15, 15);

//        AppSpyApp.getController().detenerHilo("hilo_buscando_links");
        // Ejecutamos la búsqueda de links en segundo plano
        // Realizamos las búsqueda en un hilo separado
        Thread hiloBuscando = new java.lang.Thread(new Runnable() {

            @Override
            public void run() {
//                System.out.println("corriendo hilo..");
                AppSpyApp.getMainFrame().buscandoEnlaces = true;
                // Obtenemos los links en la página de Apptrackr
                AppSpyApp.getController().obtenerLinksApptrackr(
                        AppSpyApp.getMainFrame().apptrackrURL);


                if (AppSpyApp.getMainFrame().enlaces == null
                        || AppSpyApp.getMainFrame().enlaces.isEmpty()) {
                    ((javax.swing.JMenuItem) AppSpyApp.getMainFrame().popupApptrackr.getComponent(1)).setText("couldn't get links :(");
                    AppSpyApp.getMainFrame().limpiarPopup();
                    return;
                }

                // Limpiamos nuevamente el popup
                AppSpyApp.getMainFrame().limpiarPopup();

                // Agregamos los links al menu
                for (final String enlace : AppSpyApp.getMainFrame().enlaces) {
                    final javax.swing.JMenuItem menu = new javax.swing.JMenuItem(enlace);
                    menu.addActionListener(new java.awt.event.ActionListener() {

                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent e) {
//                            System.out.println("dando click en " + menu.getText());
                            try {
                                Desktop d = Desktop.getDesktop();
                                d.browse(new java.net.URI(enlace.trim()));
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    });

                    AppSpyApp.getMainFrame().popupApptrackr.add(menu);
                    AppSpyApp.getMainFrame().popupApptrackr.pack();
                    AppSpyApp.getMainFrame().popupApptrackr.validate();
                }

                if (AppSpyApp.getMainFrame().versiones != null
                        || !AppSpyApp.getMainFrame().versiones.isEmpty()) {
                    ((javax.swing.JMenuItem) AppSpyApp.getMainFrame().popupApptrackr.getComponent(1)).setIcon(new javax.swing.ImageIcon(
                            this.getClass().getResource("/com/panchosoft/appspy/resources/images/terminado.png")));

                    ((javax.swing.JMenuItem) AppSpyApp.getMainFrame().popupApptrackr.getComponent(1)).setText("apptrackr last version: " + AppSpyApp.getMainFrame().versiones.get(0));
                } else {
                    ((javax.swing.JMenuItem) AppSpyApp.getMainFrame().popupApptrackr.getComponent(1)).setText("couldn't get links :(");
//                                    AppSpyApp.getMainFrame().limpiarPopup();
                }
                AppSpyApp.getMainFrame().popupApptrackr.pack();
                AppSpyApp.getMainFrame().popupApptrackr.validate();
//                System.out.println("Terminado...");
                AppSpyApp.getMainFrame().buscandoEnlaces = false;
            }
        }, "hilo_buscando_links");
        hiloBuscando.start();
    }

    public void limpiarPopup() {
//        System.out.println("Contado antes: " + popupApptrackr.getComponentCount());
        // Borramos los enlaces anteriormente encontrados
        int total = popupApptrackr.getComponentCount();
        for (int i = 0; i < total; i++) {
            if (i >= 3) {
//                System.out.println("Eliminado: " + i);
                popupApptrackr.remove(3);
//                popupApptrackr.remove(i);
            }
        }
//        System.out.println("Contado después: " + popupApptrackr.getComponentCount());
        this.popupApptrackr.pack();
        this.popupApptrackr.validate();
        this.validate();
    }

    public Object obtenerElementoBusqueda(int indice) {
        if (indice >= 0) {
            return this.listaBusqueda.getSelectedValue();
        }

        return null;
    }

    public void mostrarIconoBuscando(boolean activar) {
        if (activar) {
            this.lblResultadosBusqueda.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/com/panchosoft/appspy/resources/images/searching.gif")));
        } else {
            this.lblResultadosBusqueda.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/com/panchosoft/appspy/resources/images/search.png")));
        }
    }

    public void activarBotonesBusquedaDescarga() {
        this.lbliTunesBusqueda.setEnabled(true);
        this.lblApptrackrBusqueda.setEnabled(true);
    }

    public void mostrarResultados(int total) {
        if (total > 0) {
            this.lblResultadosBusqueda.setText(total + " results");
        } else {
            this.lblResultadosBusqueda.setText("not found");
        }

    }

    public void abrirCarpeta(String dir) {
        try {
            Desktop d = Desktop.getDesktop();
            if (Desktop.isDesktopSupported()) {
                d.open(new java.io.File(dir));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean esWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        //windows
        return (os.indexOf("win") >= 0);
    }

    public boolean esMac() {
        String os = System.getProperty("os.name").toLowerCase();
        //Mac
        return (os.indexOf("mac") >= 0);
    }

    public boolean esUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        //linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    public void mostrarNuevaVersion() {
        this.btnNueva.setVisible(true);
        this.separador5.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupApptrackr = new javax.swing.JPopupMenu();
        mnuTitulo = new javax.swing.JMenuItem();
        mnuObteniendo = new javax.swing.JMenuItem();
        mnuSeparador = new javax.swing.JPopupMenu.Separator();
        barra = new javax.swing.JToolBar();
        btnEncontrarApps = new javax.swing.JButton();
        separador1 = new javax.swing.JToolBar.Separator();
        btnEncontrarAct = new javax.swing.JButton();
        separador2 = new javax.swing.JToolBar.Separator();
        btnAbrirCarpeta = new javax.swing.JButton();
        separador3 = new javax.swing.JToolBar.Separator();
        btnBuscar = new javax.swing.JButton();
        separador4 = new javax.swing.JToolBar.Separator();
        btnAcerca = new javax.swing.JButton();
        separador5 = new javax.swing.JToolBar.Separator();
        btnNueva = new javax.swing.JButton();
        lblItunesApps2 = new javax.swing.JLabel();
        lblItunesApps = new javax.swing.JLabel();
        barra_lista_apps = new javax.swing.JScrollPane();
        listaApps = new javax.swing.JList();
        panelInfo = new javax.swing.JPanel();
        lblAppName = new javax.swing.JLabel();
        lblMascara = new javax.swing.JLabel();
        lblImagen = new javax.swing.JLabel();
        lblInformacion = new javax.swing.JLabel();
        lblSize1 = new javax.swing.JLabel();
        lblAppSDK = new javax.swing.JLabel();
        lblAppIdentificador = new javax.swing.JLabel();
        lblAppVersion = new javax.swing.JLabel();
        lblInformacion3 = new javax.swing.JLabel();
        lblSize = new javax.swing.JLabel();
        lblAppSize = new javax.swing.JLabel();
        lblAppEjecutable = new javax.swing.JLabel();
        lblInformacion4 = new javax.swing.JLabel();
        lblAppUbicacion = new javax.swing.JLabel();
        lblInformacion1 = new javax.swing.JLabel();
        lblInformacion2 = new javax.swing.JLabel();
        lblIgnorar = new javax.swing.JLabel();
        lblEliminar = new javax.swing.JLabel();
        panelActualizacion = new javax.swing.JPanel();
        panelTabs = new javax.swing.JTabbedPane();
        tabInfo = new javax.swing.JPanel();
        lblTituloNueva = new javax.swing.JLabel();
        lblNewestVersion = new javax.swing.JLabel();
        lblMaskMini = new javax.swing.JLabel();
        lblUpdateImg = new javax.swing.JLabel();
        lblTituloTamano = new javax.swing.JLabel();
        lblTamano = new javax.swing.JLabel();
        lblTituloPrecio = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        lblObtener = new javax.swing.JLabel();
        lblItunes = new javax.swing.JLabel();
        lblApptrackr = new javax.swing.JLabel();
        lblTituloDescripcion = new javax.swing.JLabel();
        panelDescripcion = new javax.swing.JScrollPane();
        lblDescripcion = new javax.swing.JTextArea();
        tabLast = new javax.swing.JPanel();
        lblTituloLast = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblTituloLast1 = new javax.swing.JLabel();
        lblItunes1 = new javax.swing.JLabel();
        lblApptrackr1 = new javax.swing.JLabel();
        lblTituloDescripcion1 = new javax.swing.JLabel();
        panelDescripcion1 = new javax.swing.JScrollPane();
        lblDescripcion1 = new javax.swing.JTextArea();
        lblMinLast = new javax.swing.JLabel();
        lblUltimaImagen = new javax.swing.JLabel();
        tabDesc = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        scrollMensaje = new javax.swing.JScrollPane();
        lbMensaje = new javax.swing.JTextArea();
        btnArreglar = new javax.swing.JButton();
        tabBuscar = new javax.swing.JPanel();
        lblSearApp = new javax.swing.JLabel();
        txtAppBusqueda = new javax.swing.JTextField();
        barraBusqueda = new javax.swing.JScrollPane();
        listaBusqueda = new javax.swing.JList();
        lblResultadosBusqueda = new javax.swing.JLabel();
        btnRelacionar = new javax.swing.JButton();
        lbliTunesBusqueda = new javax.swing.JLabel();
        lblApptrackrBusqueda = new javax.swing.JLabel();
        txtPaises = new javax.swing.JComboBox();
        menu = new javax.swing.JMenuBar();
        mnuAppupdater = new javax.swing.JMenu();
        mnuAbout = new javax.swing.JMenu();

        mnuTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/apptrackr.png"))); // NOI18N
        mnuTitulo.setText("go to apptrackr");
        mnuTitulo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnuTitulo.setFocusPainted(true);
        mnuTitulo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTituloClicked(evt);
            }
        });
        popupApptrackr.add(mnuTitulo);

        mnuObteniendo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/cargando16.gif"))); // NOI18N
        mnuObteniendo.setText("getting links...");
        popupApptrackr.add(mnuObteniendo);
        popupApptrackr.add(mnuSeparador);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AppSpy [Beta] - Panchosoft.com Labs");
        setBackground(new java.awt.Color(34, 34, 34));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Segoe UI", 0, 10));
        setIconImage((new javax.swing.ImageIcon(this.getClass().getResource("/com/panchosoft/appspy/resources/images/iconapp.png"))).getImage());
        setName("MainFrame"); // NOI18N
        setResizable(false);

        barra.setBackground(new java.awt.Color(51, 51, 51));
        barra.setBorder(null);
        barra.setFloatable(false);
        barra.setRollover(true);
        barra.setOpaque(false);

        btnEncontrarApps.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnEncontrarApps.setForeground(new java.awt.Color(255, 255, 255));
        btnEncontrarApps.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/Spotlight Blue Button.png"))); // NOI18N
        btnEncontrarApps.setText("find apps");
        btnEncontrarApps.setToolTipText("Find your iPhone/iPod touch applications. Hold down the SHIFT key to manually select the applications folder.\n\n");
        btnEncontrarApps.setBorderPainted(false);
        btnEncontrarApps.setFocusable(false);
        btnEncontrarApps.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEncontrarApps.setIconTextGap(6);
        btnEncontrarApps.setMargin(new java.awt.Insets(5, 14, 5, 14));
        btnEncontrarApps.setOpaque(false);
        btnEncontrarApps.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncontrarAppsActionPerformed(evt);
            }
        });
        barra.add(btnEncontrarApps);
        barra.add(separador1);

        btnEncontrarAct.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnEncontrarAct.setForeground(new java.awt.Color(255, 255, 255));
        btnEncontrarAct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/Transfer.png"))); // NOI18N
        btnEncontrarAct.setText("search for updates");
        btnEncontrarAct.setBorderPainted(false);
        btnEncontrarAct.setEnabled(false);
        btnEncontrarAct.setFocusable(false);
        btnEncontrarAct.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEncontrarAct.setIconTextGap(6);
        btnEncontrarAct.setMargin(new java.awt.Insets(5, 14, 5, 14));
        btnEncontrarAct.setOpaque(false);
        btnEncontrarAct.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncontrarActActionPerformed(evt);
            }
        });
        barra.add(btnEncontrarAct);
        barra.add(separador2);

        btnAbrirCarpeta.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnAbrirCarpeta.setForeground(new java.awt.Color(255, 255, 255));
        btnAbrirCarpeta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/folder_32.png"))); // NOI18N
        btnAbrirCarpeta.setText("open ipa's folder");
        btnAbrirCarpeta.setBorderPainted(false);
        btnAbrirCarpeta.setFocusable(false);
        btnAbrirCarpeta.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAbrirCarpeta.setIconTextGap(6);
        btnAbrirCarpeta.setOpaque(false);
        btnAbrirCarpeta.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirCarpetaActionPerformed(evt);
            }
        });
        barra.add(btnAbrirCarpeta);
        barra.add(separador3);

        btnBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/search_32.png"))); // NOI18N
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFocusable(false);
        btnBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBuscar.setIconTextGap(6);
        btnBuscar.setLabel("search");
        btnBuscar.setOpaque(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        barra.add(btnBuscar);
        barra.add(separador4);

        btnAcerca.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnAcerca.setForeground(new java.awt.Color(255, 255, 255));
        btnAcerca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/smiley.png"))); // NOI18N
        btnAcerca.setText("about");
        btnAcerca.setBorderPainted(false);
        btnAcerca.setFocusable(false);
        btnAcerca.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAcerca.setIconTextGap(6);
        btnAcerca.setOpaque(false);
        btnAcerca.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcercaActionPerformed(evt);
            }
        });
        barra.add(btnAcerca);
        barra.add(separador5);

        btnNueva.setFont(new java.awt.Font("Segoe UI", 3, 14));
        btnNueva.setForeground(new java.awt.Color(255, 255, 255));
        btnNueva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/bubble_32.png"))); // NOI18N
        btnNueva.setText("new version!");
        btnNueva.setToolTipText("get the new version available of AppSpy! :)");
        btnNueva.setBorderPainted(false);
        btnNueva.setFocusable(false);
        btnNueva.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNueva.setIconTextGap(6);
        btnNueva.setOpaque(false);
        btnNueva.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaActionPerformed(evt);
            }
        });
        barra.add(btnNueva);

        lblItunesApps2.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblItunesApps2.setForeground(new java.awt.Color(255, 255, 255));
        lblItunesApps2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItunesApps2.setText("information");

        lblItunesApps.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblItunesApps.setForeground(new java.awt.Color(255, 255, 255));
        lblItunesApps.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItunesApps.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/ApplicationsFolder_16x16.png"))); // NOI18N
        lblItunesApps.setText("iTunes apps");

        barra_lista_apps.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listaApps.setBackground(new java.awt.Color(51, 51, 51));
        listaApps.setFont(new java.awt.Font("Segoe UI", 0, 12));
        listaApps.setForeground(new java.awt.Color(255, 255, 255));
        listaApps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listaApps.setCellRenderer(null);
        listaApps.setDoubleBuffered(true);
        listaApps.setMaximumSize(new java.awt.Dimension(74, 64));
        listaApps.setMinimumSize(new java.awt.Dimension(74, 64));
        listaApps.setValueIsAdjusting(true);
        barra_lista_apps.setViewportView(listaApps);

        panelInfo.setOpaque(false);
        panelInfo.setLayout(null);

        lblAppName.setFont(new java.awt.Font("Trebuchet MS", 3, 12));
        lblAppName.setForeground(new java.awt.Color(255, 255, 255));
        lblAppName.setPreferredSize(new java.awt.Dimension(140, 20));
        panelInfo.add(lblAppName);
        lblAppName.setBounds(10, 20, 180, 20);

        lblMascara.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMascara.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/mask.png"))); // NOI18N
        lblMascara.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMascara.setIconTextGap(0);
        panelInfo.add(lblMascara);
        lblMascara.setBounds(150, 10, 160, 150);

        lblImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblImagen.setIconTextGap(0);
        panelInfo.add(lblImagen);
        lblImagen.setBounds(150, 10, 160, 150);

        lblInformacion.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblInformacion.setForeground(new java.awt.Color(204, 204, 204));
        lblInformacion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblInformacion.setText("identifier");
        lblInformacion.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelInfo.add(lblInformacion);
        lblInformacion.setBounds(10, 150, 150, 20);

        lblSize1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblSize1.setForeground(new java.awt.Color(204, 204, 204));
        lblSize1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSize1.setText("sdk");
        lblSize1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelInfo.add(lblSize1);
        lblSize1.setBounds(160, 250, 60, 20);

        lblAppSDK.setFont(new java.awt.Font("Trebuchet MS", 3, 12));
        lblAppSDK.setForeground(new java.awt.Color(255, 255, 255));
        lblAppSDK.setPreferredSize(new java.awt.Dimension(140, 20));
        panelInfo.add(lblAppSDK);
        lblAppSDK.setBounds(160, 270, 110, 20);

        lblAppIdentificador.setFont(new java.awt.Font("Trebuchet MS", 3, 12));
        lblAppIdentificador.setForeground(new java.awt.Color(255, 255, 255));
        lblAppIdentificador.setPreferredSize(new java.awt.Dimension(140, 20));
        panelInfo.add(lblAppIdentificador);
        lblAppIdentificador.setBounds(10, 170, 270, 20);

        lblAppVersion.setFont(new java.awt.Font("Trebuchet MS", 3, 12));
        lblAppVersion.setForeground(new java.awt.Color(255, 255, 255));
        lblAppVersion.setPreferredSize(new java.awt.Dimension(140, 20));
        panelInfo.add(lblAppVersion);
        lblAppVersion.setBounds(10, 70, 140, 20);

        lblInformacion3.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblInformacion3.setForeground(new java.awt.Color(204, 204, 204));
        lblInformacion3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblInformacion3.setText("executable");
        lblInformacion3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelInfo.add(lblInformacion3);
        lblInformacion3.setBounds(10, 100, 140, 20);

        lblSize.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblSize.setForeground(new java.awt.Color(204, 204, 204));
        lblSize.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSize.setText("size");
        lblSize.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelInfo.add(lblSize);
        lblSize.setBounds(10, 250, 60, 20);

        lblAppSize.setFont(new java.awt.Font("Trebuchet MS", 3, 12));
        lblAppSize.setForeground(new java.awt.Color(255, 255, 255));
        lblAppSize.setPreferredSize(new java.awt.Dimension(140, 20));
        panelInfo.add(lblAppSize);
        lblAppSize.setBounds(10, 270, 110, 20);

        lblAppEjecutable.setFont(new java.awt.Font("Trebuchet MS", 3, 12));
        lblAppEjecutable.setForeground(new java.awt.Color(255, 255, 255));
        lblAppEjecutable.setPreferredSize(new java.awt.Dimension(140, 20));
        panelInfo.add(lblAppEjecutable);
        lblAppEjecutable.setBounds(10, 120, 140, 20);

        lblInformacion4.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblInformacion4.setForeground(new java.awt.Color(204, 204, 204));
        lblInformacion4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblInformacion4.setText("location");
        lblInformacion4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelInfo.add(lblInformacion4);
        lblInformacion4.setBounds(10, 200, 140, 20);

        lblAppUbicacion.setFont(new java.awt.Font("Trebuchet MS", 3, 10));
        lblAppUbicacion.setForeground(new java.awt.Color(255, 255, 255));
        lblAppUbicacion.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblAppUbicacion.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblAppUbicacion.setPreferredSize(new java.awt.Dimension(140, 20));
        panelInfo.add(lblAppUbicacion);
        lblAppUbicacion.setBounds(10, 220, 260, 30);

        lblInformacion1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblInformacion1.setForeground(new java.awt.Color(204, 204, 204));
        lblInformacion1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblInformacion1.setText("name");
        lblInformacion1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelInfo.add(lblInformacion1);
        lblInformacion1.setBounds(10, 0, 120, 20);

        lblInformacion2.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblInformacion2.setForeground(new java.awt.Color(204, 204, 204));
        lblInformacion2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblInformacion2.setText("version");
        lblInformacion2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelInfo.add(lblInformacion2);
        lblInformacion2.setBounds(10, 50, 120, 20);

        lblIgnorar.setBackground(new java.awt.Color(16, 16, 16));
        lblIgnorar.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblIgnorar.setForeground(new java.awt.Color(204, 204, 204));
        lblIgnorar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/bookmark_16.png"))); // NOI18N
        lblIgnorar.setText("ignore");
        lblIgnorar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblIgnorar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblIgnorarMouseClicked(evt);
            }
        });
        panelInfo.add(lblIgnorar);
        lblIgnorar.setBounds(180, 390, 60, 20);

        lblEliminar.setBackground(new java.awt.Color(16, 16, 16));
        lblEliminar.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblEliminar.setForeground(new java.awt.Color(204, 204, 204));
        lblEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/delete_16.png"))); // NOI18N
        lblEliminar.setText("delete");
        lblEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEliminar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEliminarMouseClicked(evt);
            }
        });
        panelInfo.add(lblEliminar);
        lblEliminar.setBounds(250, 390, 60, 20);

        panelActualizacion.setBackground(new java.awt.Color(51, 51, 51));
        panelActualizacion.setOpaque(false);
        panelActualizacion.setLayout(null);

        panelTabs.setBackground(new java.awt.Color(51, 51, 51));
        panelTabs.setFocusable(false);

        tabInfo.setBackground(new java.awt.Color(51, 51, 51));
        tabInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloNueva.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTituloNueva.setForeground(new java.awt.Color(204, 204, 204));
        lblTituloNueva.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTituloNueva.setText("new version:");
        lblTituloNueva.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tabInfo.add(lblTituloNueva, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 90, -1));

        lblNewestVersion.setFont(new java.awt.Font("Trebuchet MS", 3, 14));
        lblNewestVersion.setForeground(new java.awt.Color(255, 255, 255));
        lblNewestVersion.setPreferredSize(new java.awt.Dimension(140, 20));
        tabInfo.add(lblNewestVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 70, -1));

        lblMaskMini.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/maskmin.png"))); // NOI18N
        tabInfo.add(lblMaskMini, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 70, 60));
        tabInfo.add(lblUpdateImg, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 70, 60));

        lblTituloTamano.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTituloTamano.setForeground(new java.awt.Color(204, 204, 204));
        lblTituloTamano.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTituloTamano.setText("size:");
        lblTituloTamano.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tabInfo.add(lblTituloTamano, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 40, -1));

        lblTamano.setFont(new java.awt.Font("Trebuchet MS", 3, 14));
        lblTamano.setForeground(new java.awt.Color(255, 255, 255));
        lblTamano.setPreferredSize(new java.awt.Dimension(140, 20));
        tabInfo.add(lblTamano, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 60, -1));

        lblTituloPrecio.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTituloPrecio.setForeground(new java.awt.Color(204, 204, 204));
        lblTituloPrecio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTituloPrecio.setText("price:");
        lblTituloPrecio.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tabInfo.add(lblTituloPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 80, -1));

        lblPrecio.setFont(new java.awt.Font("Trebuchet MS", 3, 14));
        lblPrecio.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecio.setPreferredSize(new java.awt.Dimension(140, 20));
        tabInfo.add(lblPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 70, -1));

        lblObtener.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblObtener.setForeground(new java.awt.Color(204, 204, 204));
        lblObtener.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblObtener.setText("download:");
        lblObtener.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tabInfo.add(lblObtener, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 70, -1));

        lblItunes.setFont(new java.awt.Font("Trebuchet MS", 3, 14));
        lblItunes.setForeground(new java.awt.Color(255, 255, 255));
        lblItunes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/itunes_icon16.png"))); // NOI18N
        lblItunes.setToolTipText("Download this app on iTunes");
        lblItunes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblItunes.setPreferredSize(new java.awt.Dimension(140, 20));
        lblItunes.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblItunesMouseClicked(evt);
            }
        });
        tabInfo.add(lblItunes, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 20, -1));

        lblApptrackr.setFont(new java.awt.Font("Trebuchet MS", 3, 14));
        lblApptrackr.setForeground(new java.awt.Color(255, 255, 255));
        lblApptrackr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/apptrackr.png"))); // NOI18N
        lblApptrackr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblApptrackr.setPreferredSize(new java.awt.Dimension(140, 20));
        lblApptrackr.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblApptrackrMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblMostrarPopup(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblOcultarPopup(evt);
            }
        });
        tabInfo.add(lblApptrackr, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 20, -1));

        lblTituloDescripcion.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTituloDescripcion.setForeground(new java.awt.Color(204, 204, 204));
        lblTituloDescripcion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTituloDescripcion.setText("description:");
        lblTituloDescripcion.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tabInfo.add(lblTituloDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 80, -1));

        panelDescripcion.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelDescripcion.setForeground(new java.awt.Color(255, 255, 255));
        panelDescripcion.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelDescripcion.setOpaque(false);

        lblDescripcion.setBackground(new java.awt.Color(51, 51, 51));
        lblDescripcion.setColumns(20);
        lblDescripcion.setEditable(false);
        lblDescripcion.setFont(new java.awt.Font("Segoe UI", 0, 10));
        lblDescripcion.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcion.setLineWrap(true);
        lblDescripcion.setRows(5);
        lblDescripcion.setWrapStyleWord(true);
        lblDescripcion.setBorder(null);
        panelDescripcion.setViewportView(lblDescripcion);

        tabInfo.add(panelDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 210, 280));

        panelTabs.addTab("update information", tabInfo);

        tabLast.setBackground(new java.awt.Color(51, 51, 51));
        tabLast.setLayout(null);

        lblTituloLast.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblTituloLast.setForeground(new java.awt.Color(204, 204, 204));
        lblTituloLast.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTituloLast.setText("last version installed!");
        lblTituloLast.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tabLast.add(lblTituloLast);
        lblTituloLast.setBounds(10, 65, 124, 18);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/tick_48.png"))); // NOI18N
        tabLast.add(jLabel1);
        jLabel1.setBounds(47, 11, 48, 48);

        lblTituloLast1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTituloLast1.setForeground(new java.awt.Color(204, 204, 204));
        lblTituloLast1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTituloLast1.setText("check download links:");
        lblTituloLast1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblTituloLast1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        tabLast.add(lblTituloLast1);
        lblTituloLast1.setBounds(10, 101, 132, 20);

        lblItunes1.setFont(new java.awt.Font("Trebuchet MS", 3, 14));
        lblItunes1.setForeground(new java.awt.Color(255, 255, 255));
        lblItunes1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/itunes_icon16.png"))); // NOI18N
        lblItunes1.setToolTipText("Download this app on iTunes");
        lblItunes1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblItunes1.setPreferredSize(new java.awt.Dimension(140, 20));
        lblItunes1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblItunes1MouseClicked(evt);
            }
        });
        tabLast.add(lblItunes1);
        lblItunes1.setBounds(152, 101, 20, 20);

        lblApptrackr1.setFont(new java.awt.Font("Trebuchet MS", 3, 14));
        lblApptrackr1.setForeground(new java.awt.Color(255, 255, 255));
        lblApptrackr1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/apptrackr.png"))); // NOI18N
        lblApptrackr1.setToolTipText("");
        lblApptrackr1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblApptrackr1.setPreferredSize(new java.awt.Dimension(140, 20));
        lblApptrackr1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblApptrackr1MouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblApptrackr1MouseEntered(evt);
            }
        });
        tabLast.add(lblApptrackr1);
        lblApptrackr1.setBounds(182, 101, 20, 20);

        lblTituloDescripcion1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTituloDescripcion1.setForeground(new java.awt.Color(204, 204, 204));
        lblTituloDescripcion1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTituloDescripcion1.setText("description:");
        lblTituloDescripcion1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tabLast.add(lblTituloDescripcion1);
        lblTituloDescripcion1.setBounds(10, 127, 80, 20);

        panelDescripcion1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelDescripcion1.setForeground(new java.awt.Color(255, 255, 255));
        panelDescripcion1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelDescripcion1.setOpaque(false);

        lblDescripcion1.setBackground(new java.awt.Color(51, 51, 51));
        lblDescripcion1.setColumns(20);
        lblDescripcion1.setEditable(false);
        lblDescripcion1.setFont(new java.awt.Font("Segoe UI", 0, 10));
        lblDescripcion1.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcion1.setLineWrap(true);
        lblDescripcion1.setRows(5);
        lblDescripcion1.setWrapStyleWord(true);
        lblDescripcion1.setBorder(null);
        panelDescripcion1.setViewportView(lblDescripcion1);

        tabLast.add(panelDescripcion1);
        panelDescripcion1.setBounds(10, 153, 210, 269);

        lblMinLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/maskmin.png"))); // NOI18N
        tabLast.add(lblMinLast);
        lblMinLast.setBounds(152, 11, 70, 60);
        tabLast.add(lblUltimaImagen);
        lblUltimaImagen.setBounds(152, 11, 70, 60);

        panelTabs.addTab("last version", tabLast);

        tabDesc.setBackground(new java.awt.Color(51, 51, 51));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 11));
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/bomba.png"))); // NOI18N
        jLabel2.setText("this app cannot be reconized :(");

        scrollMensaje.setBorder(null);

        lbMensaje.setBackground(new java.awt.Color(51, 51, 51));
        lbMensaje.setColumns(20);
        lbMensaje.setEditable(false);
        lbMensaje.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lbMensaje.setForeground(new java.awt.Color(204, 204, 204));
        lbMensaje.setLineWrap(true);
        lbMensaje.setRows(5);
        lbMensaje.setText("Probably it is a cracked app that lost identifying information.\n\nYou can fix this searching this app in the \"fix error\" tab, selecting the correct app and finally clicking the button \"Asociate app\". :)\n");
        lbMensaje.setWrapStyleWord(true);
        lbMensaje.setBorder(null);
        scrollMensaje.setViewportView(lbMensaje);

        btnArreglar.setBackground(new java.awt.Color(51, 51, 51));
        btnArreglar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/Run.png"))); // NOI18N
        btnArreglar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnArreglar.setLabel("search and fix this app");
        btnArreglar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArreglarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabDescLayout = new javax.swing.GroupLayout(tabDesc);
        tabDesc.setLayout(tabDescLayout);
        tabDescLayout.setHorizontalGroup(
                tabDescLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabDescLayout.createSequentialGroup().addContainerGap().addGroup(tabDescLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(scrollMensaje, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE).addComponent(btnArreglar, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)).addContainerGap()));
        tabDescLayout.setVerticalGroup(
                tabDescLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(tabDescLayout.createSequentialGroup().addContainerGap().addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(scrollMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnArreglar).addContainerGap(155, Short.MAX_VALUE)));

        panelTabs.addTab("not found", tabDesc);

        tabBuscar.setBackground(new java.awt.Color(51, 51, 51));
        tabBuscar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSearApp.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblSearApp.setForeground(new java.awt.Color(204, 204, 204));
        lblSearApp.setText("search");
        tabBuscar.add(lblSearApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        txtAppBusqueda.setFont(new java.awt.Font("Segoe UI", 2, 12));
        tabBuscar.add(txtAppBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 140, 20));

        barraBusqueda.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listaBusqueda.setBackground(new java.awt.Color(51, 51, 51));
        listaBusqueda.setFont(new java.awt.Font("Segoe UI", 0, 12));
        listaBusqueda.setForeground(new java.awt.Color(255, 255, 255));
        listaBusqueda.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listaBusqueda.setFixedCellHeight(40);
        barraBusqueda.setViewportView(listaBusqueda);

        tabBuscar.add(barraBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 79, 205, 270));

        lblResultadosBusqueda.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblResultadosBusqueda.setForeground(new java.awt.Color(204, 204, 204));
        lblResultadosBusqueda.setText("results");
        tabBuscar.add(lblResultadosBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 91, 14));

        btnRelacionar.setBackground(new java.awt.Color(51, 51, 51));
        btnRelacionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/label_32.png"))); // NOI18N
        btnRelacionar.setText("Associate app");
        btnRelacionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRelacionar.setEnabled(false);
        btnRelacionar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelacionarActionPerformed(evt);
            }
        });
        tabBuscar.add(btnRelacionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 205, -1));

        lbliTunesBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/itunes_icon16.png"))); // NOI18N
        lbliTunesBusqueda.setToolTipText("View selected app on iTunes");
        lbliTunesBusqueda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbliTunesBusqueda.setEnabled(false);
        lbliTunesBusqueda.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbliTunesBusquedaMouseClicked(evt);
            }
        });
        tabBuscar.add(lbliTunesBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, -1, -1));

        lblApptrackrBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/panchosoft/appspy/resources/images/apptrackr.png"))); // NOI18N
        lblApptrackrBusqueda.setToolTipText("View selected app on AppTrackr.org");
        lblApptrackrBusqueda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblApptrackrBusqueda.setEnabled(false);
        lblApptrackrBusqueda.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblApptrackrBusquedaMouseClicked(evt);
            }
        });
        tabBuscar.add(lblApptrackrBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, -1, -1));

        txtPaises.setFont(new java.awt.Font("Segoe UI", 0, 10));
        txtPaises.setMaximumRowCount(20);
        txtPaises.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"USA"}));
        txtPaises.setBorder(null);
        txtPaises.setLightWeightPopupEnabled(false);
        txtPaises.setMaximumSize(new java.awt.Dimension(38, 16));
        txtPaises.setOpaque(false);
        txtPaises.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPaisesActionPerformed(evt);
            }
        });
        tabBuscar.add(txtPaises, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 70, 20));

        panelTabs.addTab("search", tabBuscar);

        panelActualizacion.add(panelTabs);
        panelTabs.setBounds(0, 0, 230, 450);

        mnuAppupdater.setText("AppSpy");
        menu.add(mnuAppupdater);

        mnuAbout.setText("Acerca de");
        menu.add(mnuAbout);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(19, 19, 19).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(lblItunesApps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(barra_lista_apps, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addComponent(panelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(96, 96, 96).addComponent(lblItunesApps2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE).addComponent(panelActualizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(30, 30, 30).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblItunesApps).addComponent(lblItunesApps2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(panelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE).addComponent(barra_lista_apps, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))).addComponent(panelActualizacion, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)).addGap(21, 21, 21)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Eventos de botones">
    private void btnEncontrarActActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncontrarActActionPerformed
        this.cargarActualizaciones();
    }//GEN-LAST:event_btnEncontrarActActionPerformed

    private void btnEncontrarAppsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncontrarAppsActionPerformed
        this.requestFocus(true);
        this.cargarApps();
    }//GEN-LAST:event_btnEncontrarAppsActionPerformed

    private void btnAbrirCarpetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirCarpetaActionPerformed
        this.abrirCarpeta(AppSpyApp.getApplication().getController().obtenerDirectorioItunes());
    }//GEN-LAST:event_btnAbrirCarpetaActionPerformed

    private void lblItunesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblItunesMouseClicked
        // Abrimos la URL de la aplicación en iTunes
        if (itunesURL == null || itunesURL.length() == 0) {
            return;
        }

        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new java.net.URI(itunesURL));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_lblItunesMouseClicked

    private void btnAcercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcercaActionPerformed
        AppSpyAcerca about = new AppSpyAcerca(this, true);

        about.setVisible(true);
    }//GEN-LAST:event_btnAcercaActionPerformed

    private void lblApptrackrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblApptrackrMouseClicked
        // Abrimos la URL de la aplicación en iTunes
        if (apptrackrURL == null || apptrackrURL.length() == 0) {
            return;
        }

        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new java.net.URI(apptrackrURL));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_lblApptrackrMouseClicked

    private void lblItunes1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblItunes1MouseClicked
        // Abrimos la URL de la aplicación en iTunes
        if (itunesURL == null || itunesURL.length() == 0) {
            return;
        }

        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new java.net.URI(itunesURL));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_lblItunes1MouseClicked

    private void lblApptrackr1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblApptrackr1MouseClicked
        // Abrimos la URL de la aplicación en iTunes
        if (apptrackrURL == null || apptrackrURL.length() == 0) {
            return;
        }

        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new java.net.URI(apptrackrURL));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_lblApptrackr1MouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        if (panelTabs.getTabCount() > 0) {
            for (int i = 0; i < panelTabs.getTabCount(); i++) {
                if (panelTabs.getTitleAt(i).equals("search (or fix)")) {
                    panelTabs.setSelectedIndex(i);
                    this.panelTabs.setVisible(true);
                    this.panelActualizacion.setVisible(true);
                    return;
                }
            }
            panelTabs.addTab("search", this.tabBuscar);
            panelTabs.setSelectedComponent(this.tabBuscar);
        } else {
            panelTabs.addTab("search", this.tabBuscar);
            panelTabs.setSelectedComponent(this.tabBuscar);
        }

        this.txtAppBusqueda.setText("");

        this.listaBusqueda.setModel(new javax.swing.DefaultListModel());
        this.panelTabs.setVisible(true);
        this.panelActualizacion.setVisible(true);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnRelacionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelacionarActionPerformed
        // Obtenemos los nombres de los elementos seleccionados
        String nombreApp = this.listaApps.getSelectedValue().toString();
        String nombreAct = this.listaBusqueda.getSelectedValue().toString();

        if (javax.swing.JOptionPane.showConfirmDialog(this, "Do you want to associate the \"" + nombreApp + "\" app with \"" + nombreAct + "\"?", "AppSpy",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE)
                == javax.swing.JOptionPane.YES_OPTION) {

            this.relacionarAplicacion();
        }

    }//GEN-LAST:event_btnRelacionarActionPerformed

    private void lbliTunesBusquedaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbliTunesBusquedaMouseClicked
        // Validamos si está activado el componente
        if (!this.lbliTunesBusqueda.isEnabled()) {
            return;
        }

        // Abrimos la URL de la aplicación en iTunes
        if (this.itunesBusquedaURL == null || itunesBusquedaURL.length() == 0) {
            return;
        }

        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new java.net.URI(itunesBusquedaURL));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_lbliTunesBusquedaMouseClicked

    private void lblApptrackrBusquedaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblApptrackrBusquedaMouseClicked
        // Validamos si está activado el componente
        if (!this.lblApptrackrBusqueda.isEnabled()) {
            return;
        }

        // Abrimos la URL de la aplicación en iTunes
        if (this.apptrackrBusquedaURL == null || this.apptrackrBusquedaURL.length() == 0) {
            return;
        }

        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new java.net.URI(this.apptrackrBusquedaURL));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_lblApptrackrBusquedaMouseClicked

    private void btnArreglarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArreglarActionPerformed
        if (panelTabs.getTabCount() > 0) {
            for (int i = 0; i < panelTabs.getTabCount(); i++) {
                if (panelTabs.getTitleAt(i).equals("fix error")) {
                    panelTabs.setSelectedIndex(i);
                    return;
                }
            }
        }
    }//GEN-LAST:event_btnArreglarActionPerformed

    private void txtPaisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPaisesActionPerformed
    }//GEN-LAST:event_txtPaisesActionPerformed

    private void lblIgnorarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblIgnorarMouseClicked
        if (this.buscandoActualizaciones) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please try again when the program finishes checking for updates.", "AppSpy",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        if (javax.swing.JOptionPane.showConfirmDialog(this, "Do you want to ignore this application?", "AppSpy",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE)
                == javax.swing.JOptionPane.YES_OPTION) {

            this.ignorarAplicacion();
        }
    }//GEN-LAST:event_lblIgnorarMouseClicked

    private void lblEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEliminarMouseClicked
        if (this.buscandoActualizaciones) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please try again when the program finishes checking for updates.", "AppSpy",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        if (javax.swing.JOptionPane.showConfirmDialog(this, "Do you want to delete this application?", "AppSpy",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE)
                == javax.swing.JOptionPane.YES_OPTION) {

            this.eliminarAplicacion();
        }
    }//GEN-LAST:event_lblEliminarMouseClicked

    private void lblMostrarPopup(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMostrarPopup
        this.ejecutarBusquedaLinks(evt.getSource());
    }//GEN-LAST:event_lblMostrarPopup

    private void lblOcultarPopup(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOcultarPopup
        //this.popupApptrackr.setVisible(false);
    }//GEN-LAST:event_lblOcultarPopup

    private void lblApptrackr1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblApptrackr1MouseEntered

        this.ejecutarBusquedaLinks(evt.getSource());
    }//GEN-LAST:event_lblApptrackr1MouseEntered

    private void mnuTituloClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTituloClicked
        if (apptrackrURL == null || apptrackrURL.length() == 0) {
            return;
        }
        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new java.net.URI(apptrackrURL));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_mnuTituloClicked

    private void btnNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaActionPerformed
        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new java.net.URI("http://labs.panchosoft.com/appspy/"));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnNuevaActionPerformed
// </editor-fold>
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barra;
    private javax.swing.JScrollPane barraBusqueda;
    private javax.swing.JScrollPane barra_lista_apps;
    private javax.swing.JButton btnAbrirCarpeta;
    private javax.swing.JButton btnAcerca;
    private javax.swing.JButton btnArreglar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEncontrarAct;
    private javax.swing.JButton btnEncontrarApps;
    private javax.swing.JButton btnNueva;
    private javax.swing.JButton btnRelacionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextArea lbMensaje;
    private javax.swing.JLabel lblAppEjecutable;
    private javax.swing.JLabel lblAppIdentificador;
    private javax.swing.JLabel lblAppName;
    private javax.swing.JLabel lblAppSDK;
    private javax.swing.JLabel lblAppSize;
    private javax.swing.JLabel lblAppUbicacion;
    private javax.swing.JLabel lblAppVersion;
    private javax.swing.JLabel lblApptrackr;
    private javax.swing.JLabel lblApptrackr1;
    private javax.swing.JLabel lblApptrackrBusqueda;
    private javax.swing.JTextArea lblDescripcion;
    private javax.swing.JTextArea lblDescripcion1;
    private javax.swing.JLabel lblEliminar;
    private javax.swing.JLabel lblIgnorar;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JLabel lblInformacion;
    private javax.swing.JLabel lblInformacion1;
    private javax.swing.JLabel lblInformacion2;
    private javax.swing.JLabel lblInformacion3;
    private javax.swing.JLabel lblInformacion4;
    private javax.swing.JLabel lblItunes;
    private javax.swing.JLabel lblItunes1;
    private javax.swing.JLabel lblItunesApps;
    private javax.swing.JLabel lblItunesApps2;
    private javax.swing.JLabel lblMascara;
    private javax.swing.JLabel lblMaskMini;
    private javax.swing.JLabel lblMinLast;
    private javax.swing.JLabel lblNewestVersion;
    private javax.swing.JLabel lblObtener;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblResultadosBusqueda;
    private javax.swing.JLabel lblSearApp;
    private javax.swing.JLabel lblSize;
    private javax.swing.JLabel lblSize1;
    private javax.swing.JLabel lblTamano;
    private javax.swing.JLabel lblTituloDescripcion;
    private javax.swing.JLabel lblTituloDescripcion1;
    private javax.swing.JLabel lblTituloLast;
    private javax.swing.JLabel lblTituloLast1;
    private javax.swing.JLabel lblTituloNueva;
    private javax.swing.JLabel lblTituloPrecio;
    private javax.swing.JLabel lblTituloTamano;
    private javax.swing.JLabel lblUltimaImagen;
    private javax.swing.JLabel lblUpdateImg;
    private javax.swing.JLabel lbliTunesBusqueda;
    private javax.swing.JList listaApps;
    private javax.swing.JList listaBusqueda;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenu mnuAbout;
    private javax.swing.JMenu mnuAppupdater;
    private javax.swing.JMenuItem mnuObteniendo;
    private javax.swing.JPopupMenu.Separator mnuSeparador;
    private javax.swing.JMenuItem mnuTitulo;
    private javax.swing.JPanel panelActualizacion;
    private javax.swing.JScrollPane panelDescripcion;
    private javax.swing.JScrollPane panelDescripcion1;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JTabbedPane panelTabs;
    private javax.swing.JPopupMenu popupApptrackr;
    private javax.swing.JScrollPane scrollMensaje;
    private javax.swing.JToolBar.Separator separador1;
    private javax.swing.JToolBar.Separator separador2;
    private javax.swing.JToolBar.Separator separador3;
    private javax.swing.JToolBar.Separator separador4;
    private javax.swing.JToolBar.Separator separador5;
    private javax.swing.JPanel tabBuscar;
    private javax.swing.JPanel tabDesc;
    private javax.swing.JPanel tabInfo;
    private javax.swing.JPanel tabLast;
    private javax.swing.JTextField txtAppBusqueda;
    private javax.swing.JComboBox txtPaises;
    // End of variables declaration//GEN-END:variables
}
