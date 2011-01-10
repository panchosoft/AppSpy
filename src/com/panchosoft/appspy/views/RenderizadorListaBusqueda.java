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

import com.panchosoft.appspy.models.iTunesApp;
import com.panchosoft.appspy.AppSpyApp;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;

class RenderizadorListaBusqueda extends DefaultListCellRenderer {

    private Map<Object, ImageIcon> iconos = new HashMap<Object, ImageIcon>();
    private static ImageIcon iconoPredeterminado = new ImageIcon(RenderizadorListaApps.class.getResource("/com/panchosoft/appspy/resources/images/Terminal.png"));

    @Override
    public Component getListCellRendererComponent(JList list, final Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        final JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
        renderer.setBorder(null);
        if (isSelected) {
            renderer.setBackground(new Color(227, 225, 213));
            renderer.setForeground(Color.black);
        }

        final iTunesApp app = (iTunesApp) value;


        renderer.setToolTipText(app.getTrackName() + " (" + app.getVersion() + ")");
        if (iconos.get(app) != null) {
            renderer.setIcon(iconos.get(app));
        } else {
            renderer.setIcon(iconoPredeterminado);

            // Obtenemos el icono de internet
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {

                        try {
                            java.net.URL url_icono = new java.net.URL(app.getArtworkUrl60());
                            ImageIcon icono = AppSpyApp.getController().redimensionar(new ImageIcon(url_icono), 38, 38);
                            iconos.put(app, icono);
                            AppSpyApp.getMainFrame().repaint();
                        } catch (Exception ex) {
                        }
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }
            }).start();
        }
        return renderer;
    }
}
