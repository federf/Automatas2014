package automata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
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
                    if (strLinea.contains("label") && strLinea.contains(Lambda.toString())) { //si es una transicion lambda
                        String[] partes = strLinea.split("->"); //separamos la linea en 2 partes en "->"
                        String estado = partes[0].trim();
                        automataACrear = "AFNLambda";
                        String resto = partes[1];
                        State s = new State(estado);
                        if (!nombresEstados.contains(estado)) { //si no esta incluido en el conjunto de estados
                            nombresEstados.add(estado); // lo agrega
                        }

                        /**
                         * ****************************************************************
                         */
                        partes = resto.split(" "); //buscamos el 2do estado
                        String estado2 = partes[0].trim();
                        State s2 = new State(estado2);
                        if (!nombresEstados.contains(estado2)) { //si no esta en el conjunto de estados, lo agrega
                            nombresEstados.add(estado2);
                        }
                        /**
                         * ****************************************************************
                         */
                        if (!alfabeto.contains(Lambda)) { //agrega lambda al alfabeto
                            alfabeto.add(Lambda);
                        }
                        Triple<State, Character, State> t = new Triple<State, Character, State>(s, Lambda, s2);
                        if (!delta.contains(t)) {
                            delta.add(t);
                            transiciones.add(t);
                        }
                    } else {
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
                            char label = strLinea.charAt(strLinea.indexOf('"') + 1); //busca la etiqueta de la transicion
                            if (!alfabeto.contains(label)) { //si no esta incluida en el alfabetos
                                alfabeto.add(label); //la agrega
                            }
                            Triple<State, Character, State> t = new Triple<State, Character, State>(s, label, s2);
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
            Set<Triple<State,Character,State>> transicionesCorregidas=new LinkedHashSet();
            for(Triple<State,Character,State> transicion: delta){
                State primerEstado=new State("");
                for(State s: estados){
                    if(s.name().equals(transicion.first().name())){
                        primerEstado=s;
                    }
                }
                State tercerEstado=new State("");
                for(State s: estados){
                    if(s.name().equals(transicion.third().name())){
                        tercerEstado=s;
                    }
                }
                Character caracter=transicion.second();
                transicion=new Triple(primerEstado,caracter,tercerEstado);
                transicionesCorregidas.add(transicion);
            }
            delta=transicionesCorregidas;
            
            /***********************************************************/
            
            System.out.println("cant estados: " + estados.size());

            if (automataACrear.equals("AFNLambda")) {
                NFALambda NFALamb = new NFALambda(estados, alfabeto, delta, inicial, estados_finales);
                if (NFALamb.rep_ok()) {
                    System.out.println("CREO AFNLAMBDA");
                    return NFALamb;
                } else {
                    return null;
                }
            } else {

                if (automataACrear.equals("AFN")) {
                    NFA Nfa = new NFA(estados, alfabeto, delta, inicial, estados_finales);
                    if (Nfa.rep_ok()) {
                        System.out.println("CREO AFN");
                        return Nfa;
                    } else {
                        return null;
                    }

                } else {
                    DFA Dfa = new DFA(estados, alfabeto, delta, inicial, estados_finales);
                    if (Dfa.rep_ok()) {
                        System.out.println("CREO AFD");
                        return Dfa;
                    } else {
                        return null;
                    }

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

    /*
     metodo que dado un conjunto (set) de estados retorna su conjunto potencia
     */
    public static <State> Set<Set<State>> powerSet(Set<State> list) {
        Set<Set<State>> ps = new LinkedHashSet<Set<State>>();
        ps.add(new LinkedHashSet<State>());   // add the empty set

        // for every item in the original list
        for (State item : list) {
            Set<Set<State>> newPs = new LinkedHashSet<Set<State>>();

            for (Set<State> subset : ps) {
                // copy all of the current powerset's subsets
                newPs.add(subset);

                // plus the subsets appended with the current item
                Set<State> newSubset = new LinkedHashSet<State>(subset);
                newSubset.add(item);
                newPs.add(newSubset);
            }

            // powerset is now powerset of list.subSet(0, list.indexOf(item)+1)
            ps = newPs;
        }
        return ps;
    }

    /*
     metodo que dados dos conjuntos de estados, retorna la union entre ambos conjuntos
     */
    public Set<State> unirConjuntosEstados(Set<State> unConj, Set<State> otroConj) {
        LinkedHashSet<State> result = new LinkedHashSet();
        LinkedList<String> nombresEstadosUno = new LinkedList();//lista de nombres del 1er conjunto y en el cual se agregaran los elementos
        LinkedList<String> nombresEstadosOtro = new LinkedList();
        for (State s : unConj) {//obtenemos los nombres de los estados para comparar 
            nombresEstadosUno.add(s.name());
        }
        for (State s : otroConj) {
            nombresEstadosOtro.add(s.name());
        }
        for (String nombre : nombresEstadosOtro) {//vemos que estados del 2do conjunto no estan en el 1ero
            if (!nombresEstadosUno.contains(nombre)) {
                nombresEstadosUno.add(nombre);
            }
        }
        for (String nombre : nombresEstadosUno) {//creamos el conjunto de estados resultado
            State nuevo = new State(nombre);
            result.add(nuevo);
        }
        return result;
    }

    /*
     metodo que dados dos conjuntos de estados retorna si son identicos (contienen los mismos estados)
     */
    public boolean conjEstadosIguales(Set<State> unConj, Set<State> otroConj) {
        boolean iguales = true;
        if (unConj.size() == otroConj.size()) {
            LinkedList<String> nombresEstadosUno = new LinkedList();//lista de nombres de los estados del 1er conj
            for (State s : unConj) {
                nombresEstadosUno.add(s.name());
            }
            LinkedList<String> nombresEstadosOtro = new LinkedList();//lista de nombres de los estados del 2do conj
            for (State s : otroConj) {
                nombresEstadosOtro.add(s.name());
            }
            for (String nombre : nombresEstadosUno) {
                iguales = iguales && (nombresEstadosOtro.contains(nombre));
            }
            return iguales;
        } else {
            return false;
        }
    }

}
