/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grep;

import automata.DFA;
import automata.FA;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author fede
 */
public class grep {

    /**
     * metodo que dada una exp regular
     * evalua si es valida, en tal caso,
     * genera un AFD que la reconoce y
     * dado el path de un archivo de
     * texto evalua si en alguna linea
     * del archivo hay una cadena de
     * caracteres aceptada por el
     * automata generado
     *
     * @param expReg
     * @param path
     * @return void
     */
    public static void grep(String expReg, String path) throws FileNotFoundException, IOException, Exception {
        //si la exp regular es valida
        if (ParserRecDes.valida(expReg)) {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(path);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            // Leer el archivo linea por linea
            boolean acepta = false;
            while ((strLinea = buffer.readLine()) != null) {
                String sinEspacios = strLinea.trim();
//                System.out.println("linea: "+strLinea);
                for (int i = 0; i < sinEspacios.length(); i++) {
                    for (int j = i + 1; j < sinEspacios.length()+1; j++) {
                        String actual = sinEspacios.substring(i, j);
//                        System.out.println("cadena: "+actual);
                        //DFA automata=ParserRecDes.generarAutomata(expReg);//SIN IMPLEMENTAR
                        DFA automata = (DFA) FA.parse_form_file("test/dfa1.dot");//para probar
                        acepta = automata.accepts(actual);
//                        System.out.println("acepta "+actual+" ?: "+acepta);
                        if (acepta) {
                            System.out.println(strLinea);
                            break;
                        }
                    }
                    if (acepta) {
                        break;
                    }
                }
            }
        }
    }
}
