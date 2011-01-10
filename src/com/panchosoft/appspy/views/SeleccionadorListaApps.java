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

import javax.swing.*;
import javax.swing.event.*;
import com.panchosoft.appspy.*;

class SeleccionadorListaApps implements ListSelectionListener {

    private static int ultimoIndice = -1;

    @Override
    public void valueChanged(ListSelectionEvent e) {
        boolean isAdjusting = e.getValueIsAdjusting();

        if (isAdjusting) {
            return;
        }


        if (!isAdjusting) {
            //Obtener la id presionada
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            try {
//                    System.out.println("ind: " + lsm.getMinSelectionIndex());
                if (lsm.getMinSelectionIndex() >= 0 && ultimoIndice != lsm.getMinSelectionIndex()) {
                    AppSpyApp.getController().cargarInformacionEnVista(lsm.getMinSelectionIndex());
                    AppSpyApp.getMainFrame().borrarBusqueda();
                    AppSpyApp.getMainFrame().activarBotonRelacionar();

                    ultimoIndice = lsm.getMinSelectionIndex();
                } else {
                }

            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
