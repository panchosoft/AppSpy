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

import java.net.*;

/**
 *
 * @author Panchosoft
 */
public class Verificador {

    private Verificador() {
    }

    public static Verificador getInstance() {
        if (instancia == null) {
            instancia = new Verificador();
        }

        return instancia;
    }
    private static Verificador instancia;
    private final String currentVersion = "0.9.9";

    public boolean nuevaVersionDisponible() {

        try {
            String lastVersion = obtenerHTMLPagina(
                    "http://labs.panchosoft.com/appspy/version.txt");
            if (!currentVersion.equals(lastVersion.trim())) {
                return true;
            }
        } catch (Exception ex) {
        }

        return false;
    }

    public String obtenerHTMLPagina(String url) {
        try {
            URL dir = new URL(url);

            HttpURLConnection con = (HttpURLConnection) dir.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(50000);
            con.setReadTimeout(50000);
            con.connect();

            java.io.InputStreamReader input = new java.io.InputStreamReader(con.getInputStream(), "UTF-8");
            final int CHARS_PER_PAGE = 1; //counting spaces
            final char[] buffer = new char[CHARS_PER_PAGE];
            StringBuilder output = new StringBuilder(CHARS_PER_PAGE);
            try {
                for (int read = input.read(buffer, 0, buffer.length);
                        read != -1;
                        read = input.read(buffer, 0, buffer.length)) {
                    if (buffer[0] == '“' || buffer[0] == '”') {
                        buffer[0] = '"';
                    }

                    output.append(buffer, 0, read);
                }
            } catch (java.io.IOException ignore) {
            }

            String respMessage = output.toString();
            return respMessage;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }
}
