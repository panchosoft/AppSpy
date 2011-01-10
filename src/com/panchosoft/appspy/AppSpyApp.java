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

import javax.swing.UIManager;
import com.panchosoft.appspy.controllers.AppSpyController;
import com.panchosoft.appspy.views.AppSpyVista;

/**
 *
 * @author Panchosoft
 */
public class AppSpyApp {

    private static AppSpyApp instancia;
    private static AppSpyVista mainframe;

    /**
     *
     * @return 
     */
    public static synchronized AppSpyApp getApplication() {
        if (instancia == null) {
            instancia = new AppSpyApp();
        }
        return instancia;
    }

    /**
     *
     * @return
     */
    public static synchronized AppSpyVista getMainFrame() {
        if (mainframe == null) {
            mainframe = new AppSpyVista();
        }
        return mainframe;
    }

    /**
     *
     * @return
     */
    public static synchronized AppSpyController getController() {
        return AppSpyController.getInstance();
    }

    private AppSpyApp() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                getMainFrame();
            }
        });
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.put("TabbedPane.contentBorderInsets", new java.awt.Insets(-1, 1, -1, 1));
            UIManager.put("TabbedPane.highlight", new java.awt.Color(0, 0, 0));
            UIManager.put("TabbedPane.contentAreaColor", new java.awt.Color(0, 0, 0));
            UIManager.put("TabbedPane.selected", java.awt.Color.red);
            UIManager.put("TabbedPane.background", java.awt.Color.red);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        AppSpyApp.getApplication();
    }
}
