package automata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import java.util.Set;
import utils.Triple;

public abstract class FA {

    public static final Character Lambda = '_';

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
            String automataACrear = "AFD";
            /**
             * *****************************ESTADOS**********************
             */
            Set<State> estados = new LinkedHashSet<State>();  //conjunto de estados
            LinkedList<String> nombresEstados = new LinkedList<String>(); //lista de los nombres de los estados
            /**
             * *****************************ALFABETO**********************
             */
            Set<Character> alfabeto = new LinkedHashSet<Character>(); //conjunto de caracteres del alfabeto del automata
            /**
             * *****************************DELTA**********************
             */
            LinkedList<Triple<State, Character, State>> transiciones = new LinkedList<Triple<State, Character, State>>(); //lista de transiciones para verificar si el AF es Det o NoDet
            Set<Triple<State, Character, State>> delta = new LinkedHashSet<Triple<State, Character, State>>(); //conjunto de triplas (estado, caracter, estado) que representan la delta
            /**
             * *****************************ESTADO INICIAL**********************
             */
            State inicial = null; //estado inicial
            /**
             * *****************************ESTADOS FINALES**********************
             */
            Set<State> estados_finales = new LinkedHashSet<State>(); //conjunto de estados finales
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
                    //State s = new State(estadoLeido); //crea el estado inicial como tal
                    if (nombresEstados.isEmpty()) {
                        nombresEstados.add(estadoLeido);
                    }
                    if (!nombresEstados.contains(estadoLeido)) { // si no es vacio el conjunto de estados y no contiene el estado, se lo agrega
                        nombresEstados.add(estadoLeido); //ERROR ACA, NO ESTA BIEN INICIALIZADO EL SET DE ESTADOS AL PARECER
                    }
                    inicial = new State(estadoLeido); // se asigna el estado creado al estado inicial de la tupla que representa el automata
                    System.out.println("estado inicial guardado: " + inicial.name());
                }
                if (strLinea.contains("label") && strLinea.contains(Lambda.toString())) { //si es una transicion lambda
                    System.out.println("TRANSICION LAMBDA");
                    automataACrear = "AFNLambda";
                    char charCorriente = strLinea.charAt(0);
                    String estado = ""; //nombre estado leido
                    int hasta = strLinea.indexOf("-");
                    for (int i = 4; i < hasta; i++) { //lee el 1er estado (inicio de la transicion)
                        charCorriente = strLinea.charAt(i);
                        estado = estado + charCorriente;
                    }
                    System.out.println("ULTIMO CHAR LEIDO  " + charCorriente);
                    State s = new State(estado);
                    estado = estado.trim();
                    System.out.println("ESTADO LEIDO   " + estado);
                    if (!nombresEstados.contains(estado)) { //si no esta incluido en el conjunto de estados
                        nombresEstados.add(estado); // lo agrega
                    }

                    /**
                     * ****************************************************************
                     */
                    int desde = strLinea.lastIndexOf("q");
                    hasta = strLinea.indexOf("[") - 1;
                    charCorriente = strLinea.charAt(desde);
                    String estado2 = "";
                    for (int i = desde; i < hasta; i++) { //lee el 2do estado (final de la transicion)
                        charCorriente = strLinea.charAt(i);
                        estado2 = estado2 + charCorriente;
                    }
                    estado2 = estado2.trim();
                    State s2 = new State(estado2);
                    System.out.println("---------->>");
                    System.out.println("ESTADO LEIDO  2 " + estado2);

                    if (!nombresEstados.contains(estado2)) { //si no esta en el conjunto de estados, lo agrega
                        nombresEstados.add(estado2);
                    }
                    /**
                     * ****************************************************************
                     */
                    if (!alfabeto.contains(Lambda)) { //agrega lambda al alfabeto
                        alfabeto.add(Lambda);
                    }
                    Triple<State,Character,State> t = new Triple<State, Character, State>(s, Lambda, s2);
                    System.out.println("trans Lambda agregada: "+t.first().name().toString()+"->"+t.second().toString()+"->"+t.third().name().toString());

                    if (!delta.contains(t)) {
                        delta.add(t);
                        transiciones.add(t);
                    }
                } else {
                    if (strLinea.contains("label")) {
                        System.out.println("TRANSICION NORMAL");
                        char charCorriente = strLinea.charAt(0);
                        String estado = ""; //nombre estado leido
                        int hasta = strLinea.indexOf("-");
                        for (int i = 4; i < hasta; i++) { //lee el 1er estado (inicio de la transicion)
                            charCorriente = strLinea.charAt(i);
                            estado = estado + charCorriente;
                        }
                        System.out.println("ULTIMO CHAR LEIDO  " + charCorriente);

                        State s = new State(estado);

                        estado = estado.trim();

                        System.out.println("ESTADO LEIDO   " + estado);
                        if (!nombresEstados.contains(estado)) { //si no esta incluido en el conjunto de estados
                            nombresEstados.add(estado); // lo agrega
                        }
                        /**
                         * ****************************************************
                         */
                        int desde = strLinea.lastIndexOf("q");
                        hasta = strLinea.indexOf("[") - 1;
                        charCorriente = strLinea.charAt(desde);
                        String estado2 = "";
                        for (int i = desde; i < hasta; i++) { //lee el 2do estado (final de la transicion)
                            charCorriente = strLinea.charAt(i);
                            estado2 = estado2 + charCorriente;
                        }
                        State s2 = new State(estado2);
                        estado2 = estado2.trim();
                        System.out.println("---------->>");
                        System.out.println("ESTADO LEIDO  2 " + estado2);
                        if (!nombresEstados.contains(estado2)) { //si no esta en el conjunto de estados, lo agrega
                            nombresEstados.add(estado2);
                        }
                        /**
                         * ******************************************************
                         */
                        char label = strLinea.charAt(strLinea.indexOf('"') + 1); //busca la etiqueta de la transicion
                        if (!alfabeto.contains(label)) { //si no esta incluida en el alfabetos
                            alfabeto.add(label); //la agrega
                        }
                        Triple<State,Character,State> t = new Triple<State, Character, State>(s, label, s2);
                        System.out.println("trans Lambda agregada: "+t.first().name().toString()+"->"+t.second().toString()+"->"+t.third().name().toString());
                        
                        if (!delta.contains(t)) {
                            delta.add(t);
                            transiciones.add(t);
                        }
                    }
                }

                if (strLinea.contains("[shape=doublecircle];")) {
                    System.out.println("ESTADO FINAL");
                    char charCorriente = strLinea.charAt(0);
                    String estado = ""; //nombre estado leido
                    int hasta = strLinea.indexOf("[");
                    for (int i = 4; i < hasta; i++) { //lee el 1er estado (inicio de la transicion)
                        charCorriente = strLinea.charAt(i);
                        estado = estado + charCorriente;
                    }
                    if(!nombresEstados.contains(estado)){
                        nombresEstados.add(estado);
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
            if (automataACrear.equals("AFD")) {
                for (int i = 0; i < transiciones.size(); i++) {
                    for (int j = 0; j < transiciones.size(); j++) {
                        if (i != j) {
                            Triple<State, Character, State> elem1 = transiciones.get(i);
                            Triple<State, Character, State> elem2 = transiciones.get(j);
                            if ((elem1.first().name().equals(elem2.first().name())) && (elem1.second().equals(elem2.second()))) {
                                automataACrear = "AFN";
                                i = transiciones.size();
                                j = transiciones.size();
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < nombresEstados.size(); i++) {
                String nombre = nombresEstados.get(i);
                State e = new State(nombre);
                estados.add(e);
            }
            System.out.println("cant estados: "+estados.size());

            if (automataACrear.equals("AFNLambda")) {
                System.out.println("CREO AFNLAMBDA");
                return new NFALambda(estados, alfabeto, delta, inicial, estados_finales);
            } else {

                if (automataACrear.equals("AFN")) {
                    System.out.println("CREO AFN");
                    return new NFA(estados, alfabeto, delta, inicial, estados_finales);
                } else {
                    System.out.println("CREO AFD");
                    return new DFA(estados, alfabeto, delta, inicial, estados_finales);
                }
            }

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
