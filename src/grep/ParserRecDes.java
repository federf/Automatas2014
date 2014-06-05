/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grep;

import automata.DFA;
import automata.FA;
import automata.NFALambda;
import automata.State;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import utils.Triple;

/**
 * clase que implementa un parser
 * recursivo descendente LL(1) para
 * expresiones regulares
 *
 * @author fede
 */
public class ParserRecDes {

    //contador para estados
    private static int cont = 0;

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

    //dfa total
    public static DFA dfaFinal;

    public static boolean valida(String s) {
        cadenaEvaluar = s + "#";
        try {
            cadenaNueva = S();
            System.out.println("cadena nueva: " + cadenaNueva + " cadenaEvaluar: " + s);
            return s.equals(cadenaNueva);
        } catch (notMatchException e) {
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
            return (T() + E2());
        } else {
            throw new notMatchException("Not match Exception.");
        }

    }

    private static String E2() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (tokenCorr.equals('+')) {
            cadenaEvaluar = cadenaEvaluar.substring(1);
            return (tokenCorr + T() + E2());
        } else {
            if (tokenCorr.equals('#') || tokenCorr.equals(')')) {
                return "";
            } else {
                throw new notMatchException("Not match Exception.");
            }
        }
    }

    private static String T() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (SDirecTFT2.contains(tokenCorr.toString())) {
            return (F() + T2());
        } else {
            throw new notMatchException("Not match Exception.");
        }
    }

    private static String T2() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);

        if (tokenCorr.equals('.')) {
            cadenaEvaluar = cadenaEvaluar.substring(1);
            return (tokenCorr + F() + T2());
        } else {
            if (tokenCorr.equals('+') || tokenCorr.equals('#') || tokenCorr.equals(')')) {
                return "";
            } else {
                throw new notMatchException("Not match Exception.");
            }
        }

    }

    private static String F() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (SDirecFLF2.contains(tokenCorr.toString())) {
            return (L() + F2());
        } else {
            throw new notMatchException("Not match Exception.");
        }
    }

    private static String F2() throws notMatchException {
        Character tokenCorr = cadenaEvaluar.charAt(0);
        if (tokenCorr.equals('*')) {
            cadenaEvaluar = cadenaEvaluar.substring(1);
            return (tokenCorr + F2());
        } else {
            if (SDirecF2Epsilon.contains(tokenCorr.toString())) {
                return "";
            } else {
                throw new notMatchException("Not match Exception.");
            }
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
                return (tokenCorr + e + ")");
            } else {
                throw new notMatchException("Not match Exception.");
            }
        } else {
            if (alfabeto.contains(tokenCorr.toString())) {
                return tokenCorr.toString();
            } else {
                throw new notMatchException("Not match Exception.");
            }
        }
    }

    /**
     * metodo que dada una expresion,
     * evalua si es valida, y en caso de
     * que lo sea genera y retorna el
     * DFA que la reconoce
     *
     * @return DFA
     */
    public static DFA generarDFAUnitario(String exp) {
        //si la expresion es valida
        if (valida(exp)) {
            //si tiene longitud 1, o sea es un solo caracter
            if (exp.length() == 1) {
                LinkedHashSet<Character> Alfabeto = new LinkedHashSet();
                Alfabeto.add(exp.charAt(0));
                State inicial = new State("q" + cont);
                cont++;
                State estFinal = new State("q" + cont);
                cont++;
                Triple<State, Character, State> trans = new Triple(inicial, exp.charAt(0), estFinal);
                LinkedHashSet<Triple<State, Character, State>> transiciones = new LinkedHashSet();
                transiciones.add(trans);
                LinkedHashSet<State> estados = new LinkedHashSet();
                estados.add(estFinal);
                estados.add(inicial);
                LinkedHashSet<State> finales = new LinkedHashSet();
                finales.add(estFinal);
                return new DFA(estados, Alfabeto, transiciones, inicial, finales);
            }
        }
        return null;
    }

    public DFA agregarAsterisco(FA automata) {
        State nuevoInicial = new State("q" + cont);
        cont++;
        State nuevoFinal = new State("q" + cont);
        cont++;
        LinkedHashSet<State> nuevosEstados = new LinkedHashSet(automata.states());
        nuevosEstados.add(nuevoFinal);
        nuevosEstados.add(nuevoInicial);
        LinkedHashSet<State> nuevosFinales = new LinkedHashSet();
        nuevosFinales.add(nuevoFinal);
        LinkedHashSet<Character> nuevoAlfabeto = new LinkedHashSet(automata.alphabet());
        LinkedHashSet<Triple<State, Character, State>> nuevasTrans = new LinkedHashSet(automata.transiciones());
        for (State f : automata.final_states()) {
            if (!nuevoAlfabeto.contains(FA.Lambda)) {
                nuevoAlfabeto.add(FA.Lambda);
            }
            Triple<State, Character, State> trans = new Triple(f, FA.Lambda, automata.initial_state());
            nuevasTrans.add(trans);
            Triple<State, Character, State> trans2 = new Triple(f, FA.Lambda, nuevoFinal);
            nuevasTrans.add(trans2);
        }
        Triple<State, Character, State> trans1 = new Triple(nuevoInicial, FA.Lambda, nuevoFinal);
        nuevasTrans.add(trans1);
        Triple<State, Character, State> trans2 = new Triple(nuevoInicial, FA.Lambda, automata.initial_state());
        nuevasTrans.add(trans2);

        NFALambda nfaL = new NFALambda(nuevosEstados, nuevoAlfabeto, nuevasTrans, nuevoInicial, nuevosFinales);
        DFA dfa = nfaL.toDFA();
        return dfa;
    }

    public DFA agregarBifurcacion(FA automata, FA automata2) {
        LinkedHashSet<State> nuevosEstados = (LinkedHashSet<State>) FA.unirConjuntosEstados(automata.states(), automata2.states());

        State nuevoInicial = new State("q" + cont);
        cont++;

        State nuevoFinal = new State("q" + cont);
        cont++;
        LinkedHashSet<State> nuevosFinales = new LinkedHashSet();
        nuevosFinales.add(nuevoFinal);

        LinkedHashSet<Character> nuevoAlfabeto = new LinkedHashSet(automata.alphabet());
        nuevoAlfabeto.addAll(automata2.alphabet());

        LinkedHashSet<Triple<State, Character, State>> nuevasTrans = new LinkedHashSet(automata.transiciones());
        for (State f : automata.final_states()) {
            if (!nuevoAlfabeto.contains(FA.Lambda)) {
                nuevoAlfabeto.add(FA.Lambda);
            }
            Triple<State, Character, State> trans = new Triple(f, FA.Lambda, nuevoFinal);
            nuevasTrans.add(trans);
        }
        for (State f : automata2.final_states()) {
            if (!nuevoAlfabeto.contains(FA.Lambda)) {
                nuevoAlfabeto.add(FA.Lambda);
            }
            Triple<State, Character, State> trans = new Triple(f, FA.Lambda, nuevoFinal);
            nuevasTrans.add(trans);
        }
        Triple<State, Character, State> trans = new Triple(nuevoInicial, FA.Lambda, automata.initial_state());
        nuevasTrans.add(trans);
        Triple<State, Character, State> trans2 = new Triple(nuevoInicial, FA.Lambda, automata2.initial_state());
        nuevasTrans.add(trans2);

        NFALambda nfaL = new NFALambda(nuevosEstados, nuevoAlfabeto, nuevasTrans, nuevoInicial, nuevosFinales);
        DFA dfa = nfaL.toDFA();
        return dfa;
    }

    public DFA concatenar(FA automata, FA automata2) {
        LinkedHashSet<State> nuevosEstados = (LinkedHashSet<State>) FA.unirConjuntosEstados(automata.states(), automata2.states());

        LinkedHashSet<State> nuevosFinales = new LinkedHashSet(automata2.final_states());
        State nuevoInicial = automata.initial_state();

        LinkedHashSet<Character> nuevoAlfabeto = new LinkedHashSet(automata.alphabet());
        nuevoAlfabeto.addAll(automata2.alphabet());

        LinkedHashSet<Triple<State, Character, State>> nuevasTrans = new LinkedHashSet(automata.transiciones());
        for (State f : automata.final_states()) {
            if (!nuevoAlfabeto.contains(FA.Lambda)) {
                nuevoAlfabeto.add(FA.Lambda);
            }
            Triple<State, Character, State> trans = new Triple(f, FA.Lambda, automata2.initial_state());
            nuevasTrans.add(trans);
        }

        NFALambda nfaL = new NFALambda(nuevosEstados, nuevoAlfabeto, nuevasTrans, nuevoInicial, nuevosFinales);
        DFA dfa = nfaL.toDFA();
        return dfa;
    }

    
    /**
     * metodo que dado una expresion genera el automata que la acepta
     * @param expReg
     * @return 
     */
    static DFA generarAutomata(String expReg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
