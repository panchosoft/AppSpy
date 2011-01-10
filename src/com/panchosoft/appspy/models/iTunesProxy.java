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

import com.panchosoft.appspy.utils.Base64;
import java.net.*;
import org.json.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author Panchosoft
 */
public class iTunesProxy {
    // Singleton

    private static iTunesProxy instancia;

    private iTunesProxy() {
    }

    ;
    public String codigoPais = "US";

    public static iTunesProxy getInstance() {
        if (instancia == null) {
            instancia = new iTunesProxy();
        }

        return instancia;
    }

    public iTunesApp[] buscarApp(String appName, int limit) {
        // Hacemos una búsqueda en iTunes
        String datos_json = buscarDatosAppJSON(appName, 15);

        if (datos_json == null || datos_json.length() == 0) {
            return null;
        }

        try {
            JSONObject ob = new JSONObject(datos_json);

            if (ob == null) {
                return null;
            }

            int totalResultados = ob.getInt("resultCount");
            if (totalResultados == 0) {
                return null;
            }

            iTunesApp[] appsencontradas = new iTunesApp[totalResultados];

            for (int c = 0; c < totalResultados; c++) {
                JSONArray resultados = ob.getJSONArray("results");

                if (resultados == null) {
                    return null;
                }


                JSONObject appdata = resultados.getJSONObject(c);

                if (appdata == null) {
                    return null;
                }

                // Creamos el objeto iTunesApp
                iTunesApp app = new iTunesApp();
                app.setArtistId(appdata.opt("artistId").toString());
                app.setArtistName(appdata.optString("artistName"));
                app.setArtistViewUrl(appdata.optString("artistViewUrl"));
                app.setArtworkUrl100(appdata.optString("artworkUrl100"));
                app.setArtworkUrl60(appdata.optString("artworkUrl60"));
                app.setContentAdvisoryRating(appdata.optString("advisorRating"));
                app.setDescription(appdata.optString("description"));
                app.setFileSizeBytes(appdata.optString("fileSizeBytes"));
                // Generos
                JSONArray generos = appdata.getJSONArray("genreIds");
                String[] generosApp = new String[generos.length()];
                for (int i = 0; i < generos.length(); i++) {
                    generosApp[i] = generos.getString(i);
                }
                app.setGenreIds(generosApp);
                // Generos nombres
                generos = appdata.getJSONArray("genres");
                generosApp = new String[generos.length()];
                for (int i = 0; i < generos.length(); i++) {
                    generosApp[i] = generos.getString(i);
                }
                app.setGenres(generosApp);
                // Lenguajes
                JSONArray lenguajes = appdata.getJSONArray("genres");
                String[] lenguajesApp = new String[lenguajes.length()];
                for (int i = 0; i < lenguajes.length(); i++) {
                    lenguajesApp[i] = lenguajes.getString(i);
                }
                app.setLanguageCodesISO2(lenguajesApp);
                app.setPrice(appdata.optString("price"));
                app.setPrimaryGenreId(appdata.optString("primaryGenreId"));
                app.setPrimaryGenreName(appdata.optString("primaryGenreName"));
                app.setReleaseDate(appdata.optString("releaseDate"));
                // Capturas
                JSONArray capturas = appdata.getJSONArray("screenshotUrls");
                String[] capturasApp = new String[capturas.length()];
                for (int i = 0; i < capturas.length(); i++) {
                    capturasApp[i] = capturas.getString(i);
                }
                app.setScreenshotUrls(capturasApp);
                app.setSellerName(appdata.optString("sellerName"));
                app.setSellerUrl(appdata.optString("sellerUrl"));
                // Dispositivos soportados
                JSONArray dispositivos = appdata.getJSONArray("supportedDevices");
                String[] dispositivosApp = new String[dispositivos.length()];
                for (int i = 0; i < dispositivos.length(); i++) {
                    dispositivosApp[i] = dispositivos.getString(i);
                }
                app.setSupportedDevices(dispositivosApp);
                app.setTrackCensoredName(appdata.optString("trackCensoredName"));
                app.setTrackName(appdata.optString("trackName"));
                app.setTrackViewUrl(appdata.optString("trackViewUrl"));
                app.setVersion(appdata.optString("version"));

                appsencontradas[c] = app;

            }
            return appsencontradas;
        } catch (JSONException ex) {
            Logger.getLogger("com.panchosoft.itunes").log(Level.WARNING, "Error: " + ex.getMessage());
        }

        return null;
    }

    public iTunesApp obtenerAppPorID(String appID) {
        // Hacemos una búsqueda en iTunes
        String datos_json = obtenerDatosAppJSON(appID);

        if (datos_json == null || datos_json.length() == 0) {
            return null;
        }

        try {
            JSONObject ob = new JSONObject(datos_json);

            if (ob == null) {
                return null;
            }

            JSONArray resultados = ob.getJSONArray("results");

            if (resultados == null) {
                return null;
            }

            JSONObject appdata = resultados.getJSONObject(0);

            if (appdata == null) {
                return null;
            }

            // Creamos el objeto iTunesApp
            iTunesApp app = new iTunesApp();
            app.setArtistId(appdata.opt("artistId").toString());
            app.setArtistName(appdata.optString("artistName"));
            app.setArtistViewUrl(appdata.optString("artistViewUrl"));
            app.setArtworkUrl100(appdata.optString("artworkUrl100"));
            app.setArtworkUrl60(appdata.optString("artworkUrl60"));
            app.setContentAdvisoryRating(appdata.optString("advisorRating"));
            app.setDescription(appdata.optString("description"));
            app.setFileSizeBytes(appdata.optString("fileSizeBytes"));
            // Generos
            JSONArray generos = appdata.getJSONArray("genreIds");
            String[] generosApp = new String[generos.length()];
            for (int i = 0; i < generos.length(); i++) {
                generosApp[i] = generos.getString(i);
            }
            app.setGenreIds(generosApp);
            // Generos nombres
            generos = appdata.getJSONArray("genres");
            generosApp = new String[generos.length()];
            for (int i = 0; i < generos.length(); i++) {
                generosApp[i] = generos.getString(i);
            }
            app.setGenres(generosApp);
            // Lenguajes
            JSONArray lenguajes = appdata.getJSONArray("genres");
            String[] lenguajesApp = new String[lenguajes.length()];
            for (int i = 0; i < lenguajes.length(); i++) {
                lenguajesApp[i] = lenguajes.getString(i);
            }
            app.setLanguageCodesISO2(lenguajesApp);
            app.setPrice(appdata.optString("price"));
            app.setPrimaryGenreId(appdata.optString("primaryGenreId"));
            app.setPrimaryGenreName(appdata.optString("primaryGenreName"));
            app.setReleaseDate(appdata.optString("releaseDate"));
            // Capturas
            JSONArray capturas = appdata.getJSONArray("screenshotUrls");
            String[] capturasApp = new String[capturas.length()];
            for (int i = 0; i < capturas.length(); i++) {
                capturasApp[i] = capturas.getString(i);
            }
            app.setScreenshotUrls(capturasApp);
            app.setSellerName(appdata.optString("sellerName"));
            app.setSellerUrl(appdata.optString("sellerUrl"));
            // Dispositivos soportados
            JSONArray dispositivos = appdata.getJSONArray("supportedDevices");
            String[] dispositivosApp = new String[dispositivos.length()];
            for (int i = 0; i < dispositivos.length(); i++) {
                dispositivosApp[i] = dispositivos.getString(i);
            }
            app.setSupportedDevices(dispositivosApp);
            app.setTrackCensoredName(appdata.optString("trackCensoredName"));
            app.setTrackName(appdata.optString("trackName"));
            app.setTrackViewUrl(appdata.optString("trackViewUrl"));
            app.setVersion(appdata.optString("version"));

            return app;
        } catch (JSONException ex) {
            Logger.getLogger("com.panchosoft.itunes").log(Level.WARNING, "Error: " + ex.getMessage());
        }

        return null;
    }

    public iTunesApp obtenerApp(String appName) {

        // Hacemos una búsqueda en iTunes
        String datos_json = buscarDatosAppJSON(appName, 1);

        if (datos_json == null || datos_json.length() == 0) {
            return null;
        }

        try {
            JSONObject ob = new JSONObject(datos_json);

            if (ob == null) {
                return null;
            }

            JSONArray resultados = ob.getJSONArray("results");

            if (resultados == null) {
                return null;
            }

            JSONObject appdata = resultados.getJSONObject(0);

            if (appdata == null) {
                return null;
            }

            // Creamos el objeto iTunesApp
            iTunesApp app = new iTunesApp();
            app.setArtistId(appdata.opt("artistId").toString());
            app.setArtistName(appdata.optString("artistName"));
            app.setArtistViewUrl(appdata.optString("artistViewUrl"));
            app.setArtworkUrl100(appdata.optString("artworkUrl100"));
            app.setArtworkUrl60(appdata.optString("artworkUrl60"));
            app.setContentAdvisoryRating(appdata.optString("advisorRating"));
            app.setDescription(appdata.optString("description"));
            app.setFileSizeBytes(appdata.optString("fileSizeBytes"));
            // Generos
            JSONArray generos = appdata.getJSONArray("genreIds");
            String[] generosApp = new String[generos.length()];
            for (int i = 0; i < generos.length(); i++) {
                generosApp[i] = generos.getString(i);
            }
            app.setGenreIds(generosApp);
            // Generos nombres
            generos = appdata.getJSONArray("genres");
            generosApp = new String[generos.length()];
            for (int i = 0; i < generos.length(); i++) {
                generosApp[i] = generos.getString(i);
            }
            app.setGenres(generosApp);
            // Lenguajes
            JSONArray lenguajes = appdata.getJSONArray("genres");
            String[] lenguajesApp = new String[lenguajes.length()];
            for (int i = 0; i < lenguajes.length(); i++) {
                lenguajesApp[i] = lenguajes.getString(i);
            }
            app.setLanguageCodesISO2(lenguajesApp);
            app.setPrice(appdata.optString("price"));
            app.setPrimaryGenreId(appdata.optString("primaryGenreId"));
            app.setPrimaryGenreName(appdata.optString("primaryGenreName"));
            app.setReleaseDate(appdata.optString("releaseDate"));
            // Capturas
            JSONArray capturas = appdata.getJSONArray("screenshotUrls");
            String[] capturasApp = new String[capturas.length()];
            for (int i = 0; i < capturas.length(); i++) {
                capturasApp[i] = capturas.getString(i);
            }
            app.setScreenshotUrls(capturasApp);
            app.setSellerName(appdata.optString("sellerName"));
            app.setSellerUrl(appdata.optString("sellerUrl"));
            // Dispositivos soportados
            JSONArray dispositivos = appdata.getJSONArray("supportedDevices");
            String[] dispositivosApp = new String[dispositivos.length()];
            for (int i = 0; i < dispositivos.length(); i++) {
                dispositivosApp[i] = dispositivos.getString(i);
            }
            app.setSupportedDevices(dispositivosApp);
            app.setTrackCensoredName(appdata.optString("trackCensoredName"));
            app.setTrackName(appdata.optString("trackName"));
            app.setTrackViewUrl(appdata.optString("trackViewUrl"));
            app.setVersion(appdata.optString("version"));

            return app;
        } catch (JSONException ex) {
            Logger.getLogger("com.panchosoft.itunes").log(Level.WARNING, "Error: " + ex.getMessage());
        }

        return null;
    }

    private String obtenerDatosAppJSON(String appID) {
        try {

//            URL dir = new URL("http://ax.itunes.apple.com/WebObjects/MZStore.woa/wa/viewSoftware?id=281966695&mt=1");
//            URL dir = new URL("http://ax.itunes.apple.com/WebObjects/MZStore.woa/wa/viewTopLegacy?id=25204&popId=38&genreId=36");

            String iurl = xorMessage(Base64.decodeToObject("rO0ABXQAWgYVGRFJW0oPGUMRGxsHARJDwIADBAkLTw4OHlrAgAoGCBIGHRELTwMEB1syCwMiAxkRBhoSQiwpJxEBEwgyFgYTBwIIEl0DCg9OGsCAXAMWIg4CCgYEWgcFUA==").toString(), "namaste");
            URL dir = new URL(iurl + appID + "&entity=software");

            HttpURLConnection con = (HttpURLConnection) dir.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "iTunes/9.0.2 (Macintosh; Intel Mac OS X 10.5.8) AppleWebKit/531.21.8");
            con.setRequestProperty("X-Apple-Store-Front", "143441-1");
            con.setRequestProperty("Accept-Charset", "utf-8,ISO-8859-1;q=0.7,*;q=0.7");
            con.connect();


//            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream(), "UTF-8"));

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


//		String respMessage = "";
//		String response = reader.readLine();
//
//		while(response != null ) {
//			respMessage += response;
////                        Character c = (char)reader.read();
////                        if(c != null && c == '”'){
////                            c = '"';
////                        }
//			try{
//                            response = reader.readLine();
////                            reader.read();
//                            response = response.replace('"', '"');
//                        }catch(Exception ex){
//                            System.out.println("AQUI: " + reader.read()  + " : " +ex.getMessage());
//                        }
//
//		}
//                System.out.println(respMessage);
            return respMessage;
        } catch (Exception ex) {
            Logger.getLogger("com.panchosoft.itunes").log(Level.WARNING, "Error: " + ex.getMessage());
        }
        return null;
    }

    private String buscarDatosAppJSON(String appName, int limit) {
        try {

            // Limpiamos los espacios en blanco de la url
            appName = appName.replaceAll(" ", "+");
//            URL dir = new URL("http://ax.itunes.apple.com/WebObjects/MZStore.woa/wa/viewSoftware?id=281966695&mt=1");
//            URL dir = new URL("http://ax.itunes.apple.com/WebObjects/MZStore.woa/wa/viewTopLegacy?id=25204&popId=38&genreId=36");

            String iurl = xorMessage(Base64.decodeToObject("rO0ABXQAXAYVGRFJW0oPGUMRGxsHARJDwIADBAkLTw4OHlrAgAoGCBIGHRELTwMEB1syCwMiAxkRBhoSQiwpJxEBEwgyFgYTBwIIEl0DCg9OGsCAXAMWPQQMExAcWhoEHwxO").toString(), "namaste");
            URL dir = new URL(iurl + appName + "&media=software&entity=software&attribute=allTitle&country=" + this.codigoPais + "&limit=" + limit);

            HttpURLConnection con = (HttpURLConnection) dir.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "iTunes/9.0.2 (Macintosh; Intel Mac OS X 10.5.8) AppleWebKit/531.21.8");
            con.setRequestProperty("X-Apple-Store-Front", "143441-1");

            con.connect();

//            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream(), "UTF-8"));

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
//            String respMessage = "";
//            String response = reader.readLine();
//
//            while (response != null) {
//                respMessage += response;
//
//                response = reader.readLine();
//            }

            return respMessage;
        } catch (Exception ex) {
            Logger.getLogger("com.panchosoft.itunes").log(Level.WARNING, "Error: " + ex.getMessage());
        }
        return null;
    }
    public static final String DEFAULT_ENCODING = "UTF-8";

//      public void main(){
//           String txt="http://ax.phobos.apple.com.edgesuite.net/WebObjects/MZStoreServices.woa/wa/wsSearch?term=" ;
//           txt = "http://ax.phobos.apple.com.edgesuite.net/WebObjects/MZStoreServices.woa/wa/wsLookup?id=";
////                       http://ax.phobos.apple.com.edgesuite.net/WebObjects/MZStoreServices.woa/wa/wsLookup?id=343687281&entity=software
//           String key= "namaste";
//           System.out.println(txt+" XOR-ed to: "+(txt=xorMessage( txt, key )));
//           try{
//               String encoded=Base64.encodeObject(txt);
//               System.out.println( " is encoded to: "+encoded+" and that is decoding to: "+ (txt=Base64.decodeToObject( encoded ).toString()));
//                System.out.print( "XOR-ing back to original: "+xorMessage( txt, key ) );
//           }catch(Exception ex){}
//
//      }
    public static String xorMessage(String message, String key) {
        try {
            if (message == null || key == null) {
                return null;
            }

            char[] keys = key.toCharArray();
            char[] mesg = message.toCharArray();

            int ml = mesg.length;
            int kl = keys.length;
            char[] newmsg = new char[ml];

            for (int i = 0; i < ml; i++) {
                newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
            }//for i
            mesg = null;
            keys = null;
            return new String(newmsg);
        } catch (Exception e) {
            return null;
        }
    }//xorMessage
}
