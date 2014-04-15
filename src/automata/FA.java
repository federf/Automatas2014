package automata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import java.util.Set;
import utils.Triple;

public abstract class FA {

    public static final Character Lambda = '_';

    // variables para representar los elementos de la 5upla que representan a los automatas
    /*Set<State> estados;  //conjunto de estados
     Set<Character> alfabeto; //conjunto de caracteres del alfabeto del automata
     Set<Triple<State, Character, State>> delta; //conjunto de triplas (estado, caracter, estado) que representan la delta
     State inicial; //estado inicial
     Set<State> estados_finales; //conjunto de estados finales*/

    /* Creation */
    /**
     * Parses and returns a finite
     * automaton form the given file.
     * The type of the automaton
     * returned is the appropriate one
     * for the automaton represented in
     * the file (i.e. if the file
     * contains the representation of an
     * automaton that is
     * non-deterministic but has no
     * lambda transitions, then an
     * instance of NFA must be
     * returned).
     *
     * @param path Path to the file
     * containing the specification of
     * an FA.
     * @return An instance of DFA, NFA
     * or NFALambda, corresponding to
     * the automaton represented in the
     * file.
     * @throws Exception Throws an
     * exception if there is an error
     * during the parsing process.
     */
    public static FA parse_form_file(String path) throws Exception {
        // TODO
        try {
            Set<State> estados = new HashSet<State>();  //conjunto de estados
            Set<Character> alfabeto = new HashSet<Character>(); //conjunto de caracteres del alfabeto del automata
            Set<Triple<State, Character, State>> delta = new HashSet<Triple<State, Character, State>>(); //conjunto de triplas (estado, caracter, estado) que representan la delta
            State inicial = null; //estado inicial
            Set<State> estados_finales = new HashSet<State>(); //conjunto de estados finales

            String estadoLeido = "";
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(path);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            // Leer el archivo linea por linea
            while ((strLinea = buffer.readLine()) != null) {
                System.out.println("");
                if (strLinea.contains("inic->")) { //si la linea tiene el formato de un estado inicial
                    System.out.println("ESTADO INICIAL");
                    int inicLect = strLinea.indexOf(">") + 1; //busca el caracter por donde comenzar a leer el nombre del estado inicial
                    for (int i = inicLect; i < strLinea.length() - 1; i++) {
                        estadoLeido = estadoLeido + strLinea.charAt(i); //lee caracter por caracter el nombre del estado inicial
                    }
                    State s = new State(estadoLeido); //crea el estado inicial como tal
                    if (estados.isEmpty()) {
                        estados.add(s);
                    }

                    if (!estados.contains(s)) { // si no es vacio el conjunto de estados y no contiene el estado, se lo agrega
                        estados.add(s); //ERROR ACA, NO ESTA BIEN INICIALIZADO EL SET DE ESTADOS AL PARECER
                    }
                    inicial = s; // se asigna el estado creado al estado inicial de la tupla que representa el automata
                    System.out.println("estado guardado"+s.name());
                }
                if (strLinea.contains("label") && strLinea.contains(Lambda.toString())) { //si es una transicion lambda
                    System.out.println("TRANSICION LAMBDA");
                    char charCorriente = strLinea.charAt(0);
                    String estado = ""; //nombre estado leido
                    int hasta = strLinea.indexOf("-");
                    for (int i = 0; i < hasta; i++) { //lee el 1er estado (inicio de la transicion)
                        charCorriente = strLinea.charAt(i);
                        estado = estado + charCorriente;
                    }

                    System.out.println("ULTIMO CHAR LEIDO  " + charCorriente);

                    State s = new State(estado);
                    System.out.println("ESTADO LEIDO   " + s.name());
                    if (!estados.contains(s)) { //si no esta incluido en el conjunto de estados
                        estados.add(s); // lo agrega
                    }

                    /**
                     * ****************************************************************
                     */
                    int desde = strLinea.lastIndexOf("q");
                    hasta = strLinea.indexOf("[") - 1;
                    charCorriente = strLinea.charAt(desde);
                    estado = "";
                    for (int i = desde; i < hasta; i++) { //lee el 2do estado (final de la transicion)
                        charCorriente = strLinea.charAt(i);
                        estado = estado + charCorriente;
                    }
                    State s2 = new State(estado);
                    System.out.println("---------->>");
                    System.out.println("ESTADO LEIDO  2 " + s2.name());

                    if (!estados.contains(s2)) { //si no esta en el conjunto de estados, lo agrega
                        estados.add(s2);
                    }

                    /**
                     * ****************************************************************
                     */
                    if (!alfabeto.contains(Lambda)) { //agrega lambda al alfabeto
                        alfabeto.add(Lambda);
                    }
                    Triple t = new Triple(s, Lambda, s2);
                    if (!delta.contains(t)) {
                        delta.add(t);
                    }
                } else {
                    if (strLinea.contains("label")) {
                        System.out.println("TRANSICION NORMAL");
                        char charCorriente = strLinea.charAt(0);
                        String estado = ""; //nombre estado leido
                        int hasta = strLinea.indexOf("-");
                        for (int i = 0; i < hasta; i++) { //lee el 1er estado (inicio de la transicion)
                            charCorriente = strLinea.charAt(i);
                            estado = estado + charCorriente;
                        }

                        System.out.println("ULTIMO CHAR LEIDO  " + charCorriente);

                        State s = new State(estado);
                        System.out.println("ESTADO LEIDO   " + s.name());
                        if (!estados.contains(s)) { //si no esta incluido en el conjunto de estados
                            estados.add(s); // lo agrega
                        }

                        /**
                         * ****************************************************
                         */
                        int desde = strLinea.lastIndexOf("q");
                        hasta = strLinea.indexOf("[") - 1;
                        charCorriente = strLinea.charAt(desde);
                        estado = "";
                        for (int i = desde; i < hasta; i++) { //lee el 2do estado (final de la transicion)
                            charCorriente = strLinea.charAt(i);
                            estado = estado + charCorriente;
                        }
                        State s2 = new State(estado);
                        System.out.println("---------->>");
                        System.out.println("ESTADO LEIDO  2 " + s2.name());

                        if (!estados.contains(s2)) { //si no esta en el conjunto de estados, lo agrega
                            estados.add(s2);
                        }

                        /**
                         * ******************************************************
                         */
                        char label = strLinea.charAt(strLinea.indexOf('"') + 1); //busca la etiqueta de la transicion
                        if (!alfabeto.contains(label)) { //si no esta incluida en el alfabetos
                            alfabeto.add(label); //la agrega
                        }
                        Triple t = new Triple(s, label, s2);
                        if (!delta.contains(t)) {
                            delta.add(t);
                        }
                    }
                }
                if (strLinea.contains("[shape=doublecircle];")) {
                    System.out.println("ESTADO FINAL");
                    char charCorriente = strLinea.charAt(0);
                    String estado = ""; //nombre estado leido
                    int hasta = strLinea.indexOf("[");
                    for (int i = 0; i < hasta; i++) { //lee el 1er estado (inicio de la transicion)
                        charCorriente = strLinea.charAt(i);
                        estado = estado + charCorriente;
                    }
                    State s = new State(estado);
                    if (!estados_finales.contains(s)) {
                        estados_finales.add(s);
                    }
                    System.out.println("ESTADO FINAL AGREGADO " + s.name());
                }

            }
            // Cerramos el archivo
            entrada.close();
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
        return null;
    }

    /*
     * 	State Querying
     */
    /**
     * @return the atomaton's set of
     * states.
     */
    public abstract Set<State> states();

    /**
     * @return the atomaton's alphabet.
     */
    public abstract Set<Character> alphabet();

    /**
     * @return the atomaton's initial
     * state.
     */
    public abstract State initial_state();

    /**
     * @return the atomaton's final
     * states.
     */
    public abstract Set<State> final_states();

    /**
     * Query for the automaton's
     * transition function.
     *
     * @return A state or a set of
     * states (depending on whether the
     * automaton is a DFA, NFA or
     * NFALambda) corresponding to the
     * successors of the given state via
     * the given character according to
     * the transition function.
     */
    public abstract Object delta(State from, Character c);

    /**
     * @return Returns the DOT code
     * representing the automaton.
     */
    public abstract String to_dot();

    /*
     * 	Automata Methods 
     */
    /**
     * Tests whether a string belongs to
     * the language of the current
     * finite automaton.
     *
     * @param string String to be tested
     * for acceptance.
     * @return Returns true iff the
     * current automaton accepts the
     * given string.
     */
    public abstract boolean accepts(String string);

    /**
     * Verifies whether the string is
     * composed of characters in the
     * alphabet of the automaton.
     *
     * @return True iff the string
     * consists only of characters in
     * the alphabet.
     */
    public boolean verify_string(String s) { //IMPLEMENTADO
        // TODO
        boolean verify = true;
        for (int i = 0; i < s.length(); i++) {
            verify = verify && (alphabet().contains(s.charAt(i)));
        }
        return verify;
    }

    /**
     * @return True iff the automaton is
     * in a consistent state.
     */
    public abstract boolean rep_ok();

}
