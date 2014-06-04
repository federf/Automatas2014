/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grep;

import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * clase que implementa un parser
 * recursivo descendente LL(1) para
 * expresiones regulares
 *
 * @author fede
 */
public class ParserRecDes {

    //lista de simbolos terminales
    String terminales = "().*+abcdefghijklmnopqrstuvwxyz"; //contiene (, ), +,*,., a ... z
    //token corriente
    Character tokenCorr = ' ';
    //conjuntos de simbolos directrices de cada produccion (solo las que tienen mas de 1 simbolo)
    // de (S -> E#)
    private static final String SDirecSE = "(abcdefghijklmnopqrstuvwxyz"; //contiene (, a ... z
    // de (E -> TE2)
    private static final String SDirecETE2 = "(abcdefghijklmnopqrstuvwxyz"; //contiene (, a ... z
    // de (T -> FT2)
    private static final String SDirecTFT2 = "(abcdefghijklmnopqrstuvwxyz"; //contiene (, a ... z
    // de (F -> LF2)
    private static final String SDirecFLF2 = "(abcdefghijklmnopqrstuvwxyz"; //contiene (, a ... z
    // de (F2 -> epsilon)
    private static final String SDirecF2Epsilon = ".+#)";// contiene ., * y #

    private static final String alfabeto = "abcdefghijklmnopqrstuvwxyz";//contiene caracteres de a a z

    private static String cadenaEvaluar;
    private static String cadenaNueva;
    
    public static boolean evaluar(String s) {
        cadenaEvaluar= s+"#";
        try {
            cadenaNueva = S();
            return s.equals(cadenaNueva);
        } catch(notMatchException e) {
            return false;
        }
    }
    
    private static String S() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (SDirecSE.contains(tokenCorr.toString())) {
            return (E());
        } else {
            throw new notMatchException("Not match Exception."); 
        }
    }
    
    private static String E() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (SDirecETE2.contains(tokenCorr.toString())) {
            return (T()+E2());
        } else {
            throw new notMatchException("Not match Exception."); 
        }
        
    }    
    
    private static String E2() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (tokenCorr.equals('+')) {
            cadenaEvaluar = cadenaEvaluar.substring(1);
            return (tokenCorr+T()+E2());
        } else {
            if (tokenCorr.equals('#') || tokenCorr.equals(')')) {
                return "";
            } else 
                throw new notMatchException("Not match Exception."); 
        }
    }
    
    private static String T() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (SDirecTFT2.contains(tokenCorr.toString())) {
            return (F()+T2());
        } else 
            throw new notMatchException("Not match Exception."); 
    }
    
    private static String T2() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);

        if (tokenCorr.equals('.')) {
            cadenaEvaluar = cadenaEvaluar.substring(1);        
            return (tokenCorr+F()+T2());
        } else {
            if (tokenCorr.equals('+') || tokenCorr.equals('#')|| tokenCorr.equals(')') )
                return "";
            else 
                throw new notMatchException("Not match Exception.");                 
        }
        
    }    
    
    private static String F() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (SDirecFLF2.contains(tokenCorr.toString())) {
            return (L()+F2());
        } else 
            throw new notMatchException("Not match Exception."); 
    }

    private static String F2() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (tokenCorr.equals('*')) {
            cadenaEvaluar = cadenaEvaluar.substring(1);
            return (tokenCorr+F2());
        } else {
            if (SDirecF2Epsilon.contains(tokenCorr.toString())) {
                return "";
            } else 
                throw new notMatchException("Not match Exception."); 
        }
    }
    
    private static String L() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        cadenaEvaluar = cadenaEvaluar.substring(1);
        if (tokenCorr.equals('(')) {
            String e = E();
            Character c2 = cadenaEvaluar.charAt(0);
            if (c2.equals(')')) {
                cadenaEvaluar = cadenaEvaluar.substring(1);
                return (tokenCorr+e+")");
            } else 
                throw new notMatchException("Not match Exception."); 
        } else {
            if (alfabeto.contains(tokenCorr.toString()))
                return tokenCorr.toString();
            else 
                throw new notMatchException("Not match Exception."); 
        }
    }
}
