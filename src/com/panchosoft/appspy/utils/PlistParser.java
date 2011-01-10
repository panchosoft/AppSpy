package com.panchosoft.appspy.utils;

import com.panchosoft.appspy.factories.AppSpyFactory;
import java.io.*;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.*;
import xmlwisep.Plist;
import xmlwisep.XmlParseException;
import com.panchosoft.appspy.utils.plistparser.*;

/**
 *
 * @author Francisco I. Leyva
 */
public class PlistParser {

    // Singleton
    private static PlistParser parser;

    private PlistParser() {
    }

    public static synchronized PlistParser getInstance() {
        if (parser == null) {
            parser = new PlistParser();
        }
        return parser;
    }

    public Map<String, Object> parse(File archivo) {
        Map<String, Object> diccionario;
        try {
            if(esPListBinario(archivo)){
                BinaryPListParser bplr = new BinaryPListParser();
                XMLElement xml = bplr.parse(archivo);              
                return Plist.fromXml(xml.toString().trim());
            }else{
                diccionario = Plist.load(archivo.getAbsoluteFile());
                return diccionario;
            }
        } catch (XmlParseException ex) {
            AppSpyFactory.getLog().log(Level.INFO, ex.getMessage());
        } catch (IOException ex) {
            AppSpyFactory.getLog().log(Level.WARNING, ex.getMessage());
        }
        return null;
    }
    public boolean esPListBinario(File archivo){
        RandomAccessFile raf = null;
        byte[] buf = null;
        try {
            raf = new RandomAccessFile(archivo, "r");

            // Parse the HEADER
            // ----------------
            //  magic number ("bplist")
            //  file format version ("00")
            int bpli = raf.readInt();
            int st00 = raf.readInt();
            if (bpli != 0x62706c69 || st00 != 0x73743030) {
                return false;
            }
        }catch(Exception ex){}
        return true;

    }
}
