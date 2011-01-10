package com.panchosoft.appspy.utils;

import java.io.*;
import java.net.*;
import java.util.*;
import com.panchosoft.appspy.factories.*;
import java.util.logging.Level;
/**
 * 
 * @author Francisco I. Leyva
 */
public class HTTP {

    private static HTTP instancia;

    private HTTP() {

    }

    public static HTTP getInstance() {
        if (instancia == null) {
            instancia = new HTTP();
        }
        return instancia;
    }

    public String post(String uri, HashMap parametros) {
        try {
            String resultado = "";
            String datos = "";
            // Send data
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            // Construct data
            if(parametros != null && parametros.size() > 0){
                Iterator claves = parametros.keySet().iterator();
                while(claves.hasNext()){
                    String clave = (String)claves.next();
                    if(datos.length() == 0){
                        datos = URLEncoder.encode(clave, "UTF-8")
                                + "=" + URLEncoder.encode((String)parametros.get(clave), "UTF-8");
                    }else{
                        datos += "&" + URLEncoder.encode(clave, "UTF-8")
                                + "=" + URLEncoder.encode((String)parametros.get(clave), "UTF-8");
                    }
                }
            }

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(datos);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                resultado += line;
            }
            wr.close();
            rd.close();
            return resultado;
        } catch (Exception e) {
            AppSpyFactory.getLog().log(Level.SEVERE, e.getMessage() + "\n " + uri);
        }
        return null;
    }
}
