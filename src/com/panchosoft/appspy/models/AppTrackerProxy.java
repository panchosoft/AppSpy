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
import java.util.regex.*;
import java.util.ArrayList;

public class AppTrackerProxy {

    private static AppTrackerProxy instancia;
    public ArrayList<String> versiones;
    public ArrayList<String> enlaces;
    public ArrayList<String> hostings;

    private AppTrackerProxy() {
    }

    public static AppTrackerProxy getInstance() {
        if (instancia == null) {
            instancia = new AppTrackerProxy();
        }
        return instancia;
    }

    public void obtenerEnlaces(String apptrackr_url) {
        try{
            // Contenido HTML de la página
            String respMessage = this.obtenerCodigoHTML(apptrackr_url);
            if (respMessage == null) {
                return;
            }

            // Variables
            int inicio_grupo = -1;
            int final_grupo = -1;
            int i = 0;
            versiones = new ArrayList<String>();
            enlaces = new ArrayList<String>();
            hostings = new ArrayList<String>();

            // Patrón a buscar
            String expr = "<(.*?)>VERSION (.*?)</(.*?)>";
            Pattern patt = Pattern.compile(expr, Pattern.DOTALL | Pattern.UNIX_LINES);

            Matcher m = patt.matcher(respMessage);
            while (m.find()) {

                // Variables
                String version = m.group(2);
                // Agregamos las versiones encontradas al arreglo
                versiones.add(version);
                // Capturamos el inicio y final del primer grupo de links
                if (i == 0 && inicio_grupo == -1) {
                    inicio_grupo = m.start();
                }
                if (i == 1 && final_grupo == -1) {
                    final_grupo = m.end();
                }
                i++;
    //            System.out.println(version + " - " + m.start() + " : " + m.end());
            }

            if(i == 0) return;
            if (final_grupo == -1) {
                final_grupo = respMessage.length() - 1;
            }
    //        System.out.println("");
    //        System.out.println(respMessage.substring(inicio_grupo, final_grupo));

            // Buscamos todos los enlaces para esta version

            // Patrón a buscar
            expr = "<li class=\"ipalink(.*?)\"><a href=\"(.*?)\" target=\"_blank\">(.*?)</a></li>";
            patt = Pattern.compile(expr, Pattern.DOTALL | Pattern.UNIX_LINES);

            Matcher s = patt.matcher(respMessage.substring(inicio_grupo, final_grupo));
    //        System.out.println("######################################################");
            while (s.find()) {
                // Variables
                enlaces.add(s.group(2));
                hostings.add(s.group(3));
    //            String version = s.group(1) + " : " + s.group(2);
    //
    //            System.out.println(version + " - " + s.start() + " : " + s.end());
            }
        }catch(Exception ex){
        System.out.println(ex.getMessage());
       }
    }

    public String obtenerCodigoHTML(String url) {
        try {
            URL dir = new URL(url);

            HttpURLConnection con = (HttpURLConnection) dir.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(50000);
            con.setReadTimeout(50000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; es-MX; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            con.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
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
            } catch (java.io.IOException ex) {}

            String respMessage = output.toString();
            return respMessage;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }
}
