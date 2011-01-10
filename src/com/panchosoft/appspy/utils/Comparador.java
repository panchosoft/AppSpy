package com.panchosoft.appspy.utils;
import java.util.regex.Pattern;
/**
 *
 * @author Panchosoft
 */
public class Comparador {

    // Singleton
    private static Comparador instancia;
    private Comparador(){}
    public static Comparador getInstance(){
        if(instancia == null)
            instancia = new Comparador();

        return instancia;
    }

//    public static void main(String... args) {
//        comparar("1.0", "1.1");
//        comparar("1.0.1", "1.1");
//        comparar("1.9", "1.10");
//        comparar("1.a", "1.9");
//    }

    public int comparar(String v1, String v2) {
        String s1 = versionNormalizada(v1);
        String s2 = versionNormalizada(v2);
        int cmp = s1.compareTo(s2);
        return cmp;
    }

    public String versionNormalizada(String version) {
        return versionNormalizada(version, ".", 4);
    }

    public static String versionNormalizada(String version, String sep, int maxWidth) {
        String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            if(s.equals("00")) s = "0";
            sb.append(String.format("%" + maxWidth + 's', s));
        }
        return sb.toString();
    }

}
