/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import utils.Triple;

/**
 *
 * @author fede
 */
public abstract class AutomataPila {

    public static final Character Lambda = '_';
    public static final Character initialSymbol = '@';//simbolo inicial de la pila

    public static AutomataPila parse_from_file(String path) throws Exception {
        // TODO
        try {
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
            LinkedList<Triple<State, String, State>> transiciones = new LinkedList<Triple<State, String, State>>(); //lista de transiciones para verificar si el AF es Det o NoDet
            Set<Triple<State, String, State>> delta = new LinkedHashSet<Triple<State, String, State>>(); //conjunto de triplas (estado, caracter, estado) que representan la delta
            /**
             * *****************************ESTADO
             * INICIAL**********************
             */
            State inicial = null; //estado inicial
            /**
             * ****************************ESTADOS
             * FINALES**********************
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
                if (!strLinea.isEmpty()) { //si la linea leida no es vacia, vemos que tenemos que agregar
                    strLinea = strLinea.trim();//quitamos los espacios innecesarios
                    System.out.println("");
                    if (strLinea.contains("inic->")) { //si la linea tiene el formato de un estado inicial
                        estadoLeido = strLinea.split("->")[1];
                        estadoLeido = estadoLeido.replace(";", "");
                        if (nombresEstados.isEmpty()) {
                            nombresEstados.add(estadoLeido);
                        }
                        if (!nombresEstados.contains(estadoLeido)) { // si no es vacio el conjunto de estados y no contiene el estado, se lo agrega
                            nombresEstados.add(estadoLeido);
                        }
                        inicial = new State(estadoLeido); // se asigna el estado creado al estado inicial de la tupla que representa el automata
                        estados.add(inicial);
                    }
                    if (strLinea.contains("label")) {
                        String[] partes = strLinea.split("->"); //separamos la linea en 2 partes en "->"
                        String estado = partes[0].trim();
                        String resto = partes[1];
                        State s = new State(estado);//creamos el 1er estado 
                        if (!nombresEstados.contains(estado)) { //si no esta incluido en el conjunto de estados
                            nombresEstados.add(estado); // lo agrega
                        }
                        /**
                         * ****************************************************
                         */
                        partes = resto.split(" "); //buscamos el 2do estado
                        String estado2 = partes[0].trim();
                        State s2 = new State(estado2); //creamos el 2do estado
                        if (!nombresEstados.contains(estado2)) { //si no esta en el conjunto de estados, lo agrega
                            nombresEstados.add(estado2);
                        }

                        /**
                         * ******************************************************
                         */
                        String label = strLinea.substring(strLinea.indexOf('"')+1, strLinea.lastIndexOf('"')); //busca la etiqueta de la transicion
                        System.out.println("label: " + label);
                        String[] partesLabel = label.split("/");
                        if (!alfabeto.contains(partesLabel[0].charAt(0))) { //si no esta incluida en el alfabetos
                            System.out.println("partes");
                            for (int i = 0; i < partesLabel.length; i++) {
                                System.out.println(partesLabel[i]);
                            }
                            System.out.println("tiene: " + partesLabel.length);
                            System.out.println("agrega al alfabeto: " + partesLabel[0].charAt(0));
                            alfabeto.add(partesLabel[0].charAt(0)); //la agrega
                        }
                        Triple<State, String, State> t = new Triple(s, label, s2);
                        if (!delta.contains(t)) {
                            delta.add(t);
                            transiciones.add(t);
                        }
                    }
                }

                if (strLinea.contains("[shape=doublecircle];")) {
                    String estado = strLinea.replace("[shape=doublecircle];", "");
                    if (!nombresEstados.contains(estado)) {
                        nombresEstados.add(estado);
                    }

                    LinkedList<String> nombresFinales = new LinkedList();//lista de nombres de los estados finales
                    for (State s : estados_finales) { //leemos los nombres de los estados finales que ya tenemos
                        nombresFinales.add(s.name());
                    }
                    State s = new State(estado);
                    if (!nombresFinales.contains(estado)) {
                        estados_finales.add(s);
                    }
                }

            }
            // Cerramos el archivo
            entrada.close();
            LinkedList<String> estadosYaCreados = new LinkedList();
            for (State s : estados) { //leemos los nombres de los estados que ya creamos
                estadosYaCreados.add(s.name());
            }
            for (int i = 0; i < nombresEstados.size(); i++) {
                if (!estadosYaCreados.contains(nombresEstados.get(i))) {
                    String nombre = nombresEstados.get(i);
                    State e = new State(nombre);
                    estados.add(e);
                }
            }

            /*
             correccion de direcciones de memoria en las transiciones
             */
            Set<Triple<State, String, State>> transicionesCorregidas = new LinkedHashSet();
            for (Triple<State, String, State> transicion : delta) {
                State primerEstado = new State("");
                for (State s : estados) {
                    if (s.name().equals(transicion.first().name())) {
                        primerEstado = s;
                    }
                }
                State tercerEstado = new State("");
                for (State s : estados) {
                    if (s.name().equals(transicion.third().name())) {
                        tercerEstado = s;
                    }
                }
                String cadena = transicion.second();
                transicion = new Triple(primerEstado, cadena, tercerEstado);
                transicionesCorregidas.add(transicion);
            }
            delta = transicionesCorregidas;

            System.out.println("alfabeto:");
            for (Character c:alfabeto) {
                System.out.println(c);
            }
            System.out.println("estados:");
            for (State s : estados) {
                System.out.println(s.name());
            }
            System.out.println("inicial:" + inicial.name());
            System.out.println("estados finales:");
            for (State s : estados_finales) {
                System.out.println(s.name());
            }
            System.out.println("transiciones:");
            for (Triple<State, String, State> t : delta) {
                System.out.println(t.first().name() + " -> " + t.second() + " -> " + t.third().name());
            }

            /**
             * ********************************************************
             */
            //System.out.println("cant estados: " + estados.size());
            return new APD(inicial,estados, estados_finales, delta, alfabeto);

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
            Character c = s.charAt(i);
            verify = verify && (this.alphabet().contains(c) && (!c.equals(Lambda)));
        }
        return verify;
    }

    /**
     * @return True iff the automaton is
     * in a consistent state.
     */
    public abstract boolean rep_ok();

}
