package automata;

import static automata.FA.Lambda;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import utils.Triple;

public class NFALambda extends FA {

    /*
     Variables globales para almacenar, representan las 5 partes de la tupla que
     es el AFNLambda
     */
    Set<State> estados;
    Set<Character> alfabeto;
    Set<Triple<State, Character, State>> delta;
    State inicial;
    Set<State> estados_finales;

    /*
     *  Construction
     */
    // Constructor
    public NFALambda(
            Set<State> states,
            Set<Character> alphabet,
            Set<Triple<State, Character, State>> transitions,
            State initial,
            Set<State> final_states)
            throws IllegalArgumentException {
        // TODO
        estados = states;
        alfabeto = alphabet;
        delta = transitions;
        inicial = initial;
        estados_finales = final_states;
    }

    /*
     *	State querying 
     */

    /*
     *	State querying 
     */
    @Override
    public Set<State> states() {
        // TODO
        return estados;
    }

    @Override
    public Set<Character> alphabet() {
        // TODO
        return alfabeto;
    }

    @Override
    public State initial_state() {
        // TODO
        return inicial;
    }

    @Override
    public Set<State> final_states() {
        // TODO
        return estados_finales;
    }

    /**
     * metodo que dado un estado from y
     * un caracter c, retorna el conj de
     * estados a los cuales puede
     * avanzarse desde el estado from
     * por el caracter c
     *
     * @param from
     * @param c
     * @return
     */
    @Override
    public Set<State> delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        LinkedList<Triple<State, Character, State>> trans = new LinkedList();//lista de transiciones
        //Set<State> clausura = clausuraLambda(from, trans);//clausuramos el estado 
        Set<State> result = new LinkedHashSet();
        //Set<State> result = clausuraLambda(from, trans); //set de resultado
        if (from != null && !c.equals("")) {
            for (Triple<State, Character, State> t : this.delta) {//para toda transicion
                if (t.first().name().equals(from.name()) && t.second().equals(c)) {//tal que sale desde from por c
                    //LinkedList<Triple<State, Character, State>> listaTransiciones = new LinkedList();//lista solo para calculo clausura lambda
                    //result = unirConjuntosEstados(result, clausuraLambda(t.third(), listaTransiciones)); //calculamos la clausura del estado al que llega y lo agregamos al resultado
                    result.add(t.third());
                }
            }
            return result;
        } else {
            if (from != null && c.equals("")) {
                System.out.println("Caracter Invalido (es vacio)");
                return result;
            } else {
                System.out.println("Estado invalido (es vacio)");
                return new LinkedHashSet();
            }

        }

    }

    @Override
    public String to_dot() {
        assert rep_ok();
        assert rep_ok();
        // TODO
        String result = "digraph {\n";
        result = result + "    " + "inic[shape=point];\n";
        result = result + "    " + "inic->" + inicial.name() + ";\n";
        String transiciones = "\n"; //cadena con las transiciones normales
        String transLambda = "\n"; //cadena con las transiciones lambda
        for (Triple<State, Character, State> t : delta) {
            if (!t.second().equals(Lambda)) {
                transiciones = transiciones + "    " + t.first().name() + "->" + t.third().name() + "[label=" + '"' + t.second() + '"' + "];\n";
            } else {
                transLambda = transLambda + "    " + t.first().name() + "->" + t.third().name() + "[label=" + '"' + t.second() + '"' + "];\n";
            }
        }
        String finales = "\n";
        for (State s : estados_finales) {
            finales = finales + "    " + s.name() + "[shape=doublecircle];\n";
        }
        result = result + transiciones + transLambda + finales + "}";
        return result;
    }

    /*
     *  Automata methods
     */
    @Override //CONSULTAR, TENGO UN PROBLEMA CON EL TEMA DE LAS LLAMADAS CON LAMBDA EN DELTAACUMULADA
    public boolean accepts(String string
    ) {
        /*System.out.println();
        System.out.println("acepta " + string + "?");*/

        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        if (rep_ok() && (string != null) && verify_string(string)) {
            //calcular la delta acumulada de string y luego comparar el conjunto retornado con el conj de estados finales
            LinkedList<Triple<State, Character, State>> clausura = new LinkedList();//lista para calculo de clausura lamba del estado inicial
            LinkedHashSet<State> acum = new LinkedHashSet(clausuraLambda(this.initial_state(), clausura));  //calculamos la clausura lambda del estado inicial
            Set<State> deltaAcum = deltaAcumulada(acum, string); //aplicamos la delta acumulada
            boolean accepted = false;
            /*for (State s2 : this.final_states()) {
                System.out.println("estado: " + s2.name());
            }
            //System.out.print("delta acumulada {");
            for (State s : deltaAcum) {
                System.out.print(s.name() + " ");
            }
            System.out.print("}");
            System.out.println();*/
            for (State s : deltaAcum) { //al final, el ultimo conjunto de estados contiene al menos un estado final
                for (State s2 : this.final_states()) {
                    if (s.name().equals(s2.name())) { //si es asi, la cadena es aceptada
                        accepted = true;
                    }
                }
            }
            return accepted;
        } else {
            return false;
        }
    }

    /**
     * Converts the automaton to a DFA.
     *
     * @return DFA recognizing the same
     * language.
     */
    public DFA toDFA() {
        assert rep_ok();
        // TODO

        LinkedList<String> finalesOriginales = new LinkedList();
        //obtenemos los nombres de los estados finales originales para comparacion
        for (State s : this.final_states()) {
            finalesOriginales.add(s.name());
        }
        //crea un nuevo estado inicial para el AFD
        //set resultado de clausura, para calculo clausura lambda del estado inicial del automata
        //original
        LinkedHashSet<State> clausuraPrimerElem = new LinkedHashSet();
        //clausuramos el resultado obtenido
        //lista para calculo clausura lambda
        LinkedList<Triple<State, Character, State>> listPrimerElem = new LinkedList();
        clausuraPrimerElem = new LinkedHashSet(clausuraLambda(this.initial_state(), listPrimerElem));
        String nombreInicial = "{";
        for (State s : clausuraPrimerElem) {
            nombreInicial = nombreInicial + s.name() + " ";
        }
        nombreInicial = nombreInicial + "}";
        State q0 = new State(nombreInicial);
        //conjunto potencia de los estados del NFA original-//lista de conjuntos de estados
        Set<Set<State>> conjPotencia = FA.powerSet(this.states());
        LinkedHashSet<Set<State>> newConjPotencia = new LinkedHashSet(conjPotencia);
        //lista de nuevos estados finales
        Set<Set<State>> nuevosEstadosFinales = new LinkedHashSet();
        for (Set<State> set : conjPotencia) {
            //lista de nombres de los estados del set
            LinkedList<String> nombresEstadosSet = new LinkedList();
            for (State estado : set) {
                //obtenemos los nombres de los estados de cada set
                nombresEstadosSet.add(estado.name());
            }
            boolean esFinal = false;
            //vemos si alguno era final en el automata original
            for (String finalNuevo : nombresEstadosSet) {
                esFinal = esFinal || (finalesOriginales.contains(finalNuevo));
            }
            //si el conj contiene almenos uno de los estados finales del automata original
            if (esFinal) {
                //lo agregamos al conj de nuevos estados finales
                nuevosEstadosFinales.add(set);
            }
        }
        //creamos el nuevo alfabeto sin lambda
        LinkedHashSet<Character> nuevoAlfabeto = new LinkedHashSet(this.alphabet());
        nuevoAlfabeto.remove(Lambda);

        //nueva lista de transiciones de set de estados en set de estados
        LinkedHashSet<Triple<Set<State>, Character, Set<State>>> nuevasTransiciones = new LinkedHashSet();
        for (Set<State> set : conjPotencia) {
            for (Character c : nuevoAlfabeto) {
                //if (!c.equals(Lambda)) {
                //resultado de aplicar un caracter a un set de estados
                LinkedHashSet<State> resultadoSet = new LinkedHashSet();
                for (State estado : set) {
                    //calculamos la delta de cada estado del conjunto con el caracter actual
                    Set<State> deltaUnConjunto = delta(estado, c);
                    //set para almacenar el resultado de aplicar la clausura lambda al resultado parcial obtenido
                    LinkedHashSet<State> resultadoClausura = new LinkedHashSet();
                    //clausuramos el resultado obtenido
                    for (State s : deltaUnConjunto) {
                        //lista para calculo clausura lambda
                        LinkedList<Triple<State, Character, State>> list = new LinkedList();
                        resultadoClausura = (LinkedHashSet<State>) unirConjuntosEstados(resultadoClausura, clausuraLambda(s, list));
                    }
                    //unimos los resultados en un mismo conjunto resultado
                    resultadoSet = (LinkedHashSet<State>) unirConjuntosEstados(resultadoSet, resultadoClausura);

                }
                //si el resultado obtenido no es vacio
                if (!resultadoSet.isEmpty()) {
                    //para todo conjunto de estados del conjunto potencia
                    for (Set<State> conj : newConjPotencia) {
                        //vemos si el resultado obtenido ya esta presente, ya sea con el mismo orden
                        // de elementos o como permutacion de alguno
                        if (conjEstadosIguales(conj, resultadoSet)) {
                            resultadoSet = (LinkedHashSet<State>) conj;
                        }
                    }
                    //creamos la nueva transicion
                    Triple<Set<State>, Character, Set<State>> transicion = new Triple(set, c, resultadoSet);
                    //y la agregamos al conjunto de transiciones
                    nuevasTransiciones.add(transicion);
                }
                //}
            }
        }

        //lista de sets de estados
        LinkedList<Set<State>> listaSetEstados = new LinkedList(conjPotencia);
        //lista de estados
        LinkedList<State> nuevosEstadosSimples = new LinkedList();

        //lista de nombres de los nuevos estados simples
        LinkedList<String> nombreNuevosEstadosSimples = new LinkedList();

        //para cada set creamos un estado cuyo nombre combina el nombre de sus elementos
        for (Set<State> set : listaSetEstados) {
            String nombre = "{";
            for (State s : set) {
                nombre = nombre + s.name() + " ";
            }
            nombre = nombre + "}";
            //si no se agrego aun el estado
            // y el nombre del estado no es vacio, lo cual indicaria un conjunto vacio
            // (el cual fue generado al generar el conj potencia de los estados del automata original)
            if (!nombreNuevosEstadosSimples.contains(nombre) && !nombre.equals("{}")) {
                //lo creamos y agregamos al conjunto de estados
                State nuevo = new State(nombre);
                nuevosEstadosSimples.add(nuevo);
                nombreNuevosEstadosSimples.add(nombre);
            }
        }

        //creamos transiciones equivalentes a las de set en set, pero de estado en estado
        //lista de transiciones de estado en estado
        LinkedList<Triple<State, Character, State>> transicionesSimples = new LinkedList();

        for (Triple<Set<State>, Character, Set<State>> transSet : nuevasTransiciones) {
            //nombre del primer conjunto de estados
            String nombrePrimerSet = "{";
            for (State s : transSet.first()) {
                nombrePrimerSet = nombrePrimerSet + s.name() + " ";
            }
            nombrePrimerSet = nombrePrimerSet + "}";
            //nombre del segundo conjunto de estados
            String nombreSegundoSet = "{";
            for (State s : transSet.third()) {
                nombreSegundoSet = nombreSegundoSet + s.name() + " ";
            }
            nombreSegundoSet = nombreSegundoSet + "}";
            //creamos y agregamos una transicion simple de estado a estado
            //equivalente a la transicion de set en set evaluada
            State primero = new State(nombrePrimerSet);
            State segundo = new State(nombreSegundoSet);
            Triple<State, Character, State> transicionNueva = new Triple(primero, transSet.second(), segundo);
            transicionesSimples.add(transicionNueva);
        }
        //nuevo conjunto de transiciones de estado en estado
        LinkedHashSet<Triple<State, Character, State>> transiciones = new LinkedHashSet(transicionesSimples);

        /**
         * ********************CREACION
         * CONJ DE ESTADOS
         * FINALES******************
         */
        //set de nuevos estados finales
        LinkedHashSet<State> nuevosFinales = new LinkedHashSet();
        //para todo estado del conjunto de estados, tal que contiene algun
        //estado final del automata original, lo agregamos al nuevo conjunto de estados finales
        for (State s : nuevosEstadosSimples) {
            for (String nombreFinal : finalesOriginales) {
                //si el nombre de uno de los nuevos estados
                //contiene el nombre de algun estado final del automata original
                if (s.name().contains(nombreFinal)) {
                    //agregamos el estado al conjunto de estados finales
                    nuevosFinales.add(s);
                }
            }
        }

        /**
         * *************************CREACION
         * DEL
         * DFA**********************************
         */
        DFA result = new DFA(new LinkedHashSet(nuevosEstadosSimples), nuevoAlfabeto, transiciones, q0, nuevosFinales);
        /*
         LIMPIAMOS EL AUTOMATA, ELIMINANDO ESTADOS INALCANZABLES Y LAS TRANSICIONES QUE SALGAN DE DICHOS ESTADOS
         */
        result = result.limpiarAutomata(result.delta);
        return result;
    }

    @Override
    public boolean rep_ok() {
        // TODO: Check that initial and final states are included in 'states'.
        // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
        boolean inicOk = false;
        boolean finalesOk = true;
        boolean transicionesOk = true;

        LinkedList<String> states = new LinkedList();
        for (State s : estados) { //buscamos los nombres de los estados
            states.add(s.name());
        }
        //System.out.println("estados: " + states);
        LinkedList<String> finales = new LinkedList();
        for (State f : estados_finales) {  //buscamos los nombres de los estados finales
            finales.add(f.name());
        }

        for (int i = 0; i < states.size(); i++) { //verificamos que el estado inicial pertenece al conjunto de estados
            inicOk = inicOk || (states.get(i).equals(inicial.name()));
        }

        for (int i = 0; i < finales.size(); i++) { //verificamos que los estados finales pertenecen al conjunto de estados
            finalesOk = finalesOk && (states.contains(finales.get(i)));
        }

        for (Triple<State, Character, State> t : delta) { //verificamos que las transiciones son validas 
            //System.out.println(t.first().name() + " " + t.second() + " " + t.third().name());
            if (!t.second().equals(Lambda)) {//si no es una transicion lambda verificamos que todos sus elementos sean correctos
                transicionesOk = transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()) && alfabeto.contains(t.second())); // (los estados utilizados pertenecen al conjunto de estados y el simbolo utilizado pertenece al alfabeto)
            } else {
                transicionesOk = transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()));//si es una transicion lambda solo verificamos los estados
            }
        }

        return (inicOk && finalesOk && transicionesOk);
    }

    /*
     metodo que calcula la clausura lambda de un estado.
     recibe como parametros el estado, y una lista de transiciones que llevara registro de las transiciones ya evaluadas
     para evitar repeticiones durante la recursion
     */
    public Set<State> clausuraLambda(State s, LinkedList<Triple<State, Character, State>> t) {
        LinkedList<State> e = new LinkedList(); //lista de estados que luego formaran el resultado
        Set<State> result = new LinkedHashSet(); //conjunto resultado
        Set<State> resultRecursivo = new LinkedHashSet(); //conjunto resultado recursividad
        e.add(s);
        LinkedList<Triple<State, Character, State>> transLambdaYaEvaluadas = t; //lista que lleva registro de las trans lambda ya evaluadas para no repetir
        //su evaluacion
        LinkedList<Triple<State, Character, State>> transLambda = new LinkedList();//lista de transiciones lambda existentes pertenecientes al estado
        for (Triple<State, Character, State> trans : delta) { //para toda transicion
            if (trans.first().name().equals(s.name()) && trans.second().equals(Lambda)) { //si es lambda y no esta incluida en la lista de transiciones lambda
                transLambda.add(trans); //se agrega a la lista de transiciones lamdba que salen del estado parametro
            }
        }
        for (int i = 0; i < transLambda.size(); i++) {
            if (!transLambdaYaEvaluadas.contains(transLambda.get(i))) { //si aun no se evaluo la transicion corriente
                e.add(transLambda.get(i).third()); //agregamos el estado a cual llega al conjunto clausura
                transLambdaYaEvaluadas.add(transLambda.get(i)); //marcamos la transicion como ya evaluada, agregandola a la lista de transiciones lambda evaluadas
            }
        }
        for (int i = 0; i < e.size(); i++) {
            result.add(e.get(i));
        }
        for (int i = 0; i < e.size(); i++) { //llamada recursiva
            if (!e.get(i).name().equals(s.name())) {
                resultRecursivo = clausuraLambda(e.get(i), transLambdaYaEvaluadas);
                result = unirConjuntosEstados(result, resultRecursivo);
            }
        }

        if (result.isEmpty()) {
            return new LinkedHashSet();
        } else {
            return result;
        }
    }

    /*
     metodo que dada una string calcula la delta acumulada
     ademas requiere de un conjunto de estados sobre los cuales aplicara la delta y calculara la clausuraE.
     Al inicio se le pasaria un conj que contiene el estado inicial
     */
    public Set<State> deltaAcumulada(Set<State> est, String string) { //CONSULTAR TRATAMIENTO POR LAMBDA!!!! FALTA AGREGARLO, EL QUE ESTA ES EL TRATAMIENTO DEL NFA SIN LAMBDA

        /*System.out.println("est:");
        System.out.print("{");
        for (State s : est) {
            System.out.print(s.name() + " ");
        }
        System.out.println("}");*/

        LinkedHashSet<State> result = new LinkedHashSet();
        LinkedHashSet<State> resultadoParcial = new LinkedHashSet();//lista de estados ya obtenidos
        boolean puedeAvanzarPorCaracter = false;//booleano que indica que se puede avanzar consumiendo la cadena
        if (!est.isEmpty() && !string.isEmpty()) {
            for (State s : est) {//para todos los estados del conjunto de estados pasado como parametro
                puedeAvanzarPorCaracter = puedeAvanzarPorCaracter || (tieneTransiciones(s, string.charAt(0)));
                LinkedHashSet<State> conUnChar = (LinkedHashSet<State>) delta(s, string.charAt(0));//obtenemos la delta de un estado del conjunto con el 1er elemento de la cadena
                resultadoParcial = (LinkedHashSet<State>) unirConjuntosEstados(resultadoParcial, conUnChar);//unimos los resultados
            }

            /*System.out.println("resParcial:");
            System.out.print("{");
            for (State s : resultadoParcial) {
                System.out.print(s.name() + " ");
            }
            System.out.println("}");*/

            LinkedHashSet<State> resultadoClausura = new LinkedHashSet();//resultado de aplicar la clausura lambda al resultado parcial obtenido
            for (State s : resultadoParcial) {//clausuramos el resultado obtenido
                LinkedList<Triple<State, Character, State>> list = new LinkedList(); //lista para calculo clausura lambda
                resultadoClausura = (LinkedHashSet<State>) unirConjuntosEstados(resultadoClausura, clausuraLambda(s, list));
            }

            /*System.out.println("clausura:");
            System.out.print("{");
            for (State s : resultadoClausura) {
                System.out.print(s.name() + " ");
            }
            System.out.println("}");*/

            if (puedeAvanzarPorCaracter) {
                String substring = string.substring(1);//quitamos el 1er elemento de la cadena
                result = (LinkedHashSet<State>) deltaAcumulada(resultadoClausura, substring);//llamamos recursivamente con un elemento menos
            } else {
                boolean cicloInfinito = false;
                for (State s : est) {
                    for (State s2 : resultadoParcial) {
                        cicloInfinito = cicloInfinito || (s.name().equals(s2.name()));
                    }
                }
                if (cicloInfinito) {
                    return new LinkedHashSet();
                }
            }
            return result;
        } else {
            if (string.isEmpty()) {
                //System.out.println("cadena vacia ");
                return est;
            } else {
                //System.out.println("conjunto de estados vacio");
                return new LinkedHashSet<State>();
            }
        }
    }

    /*
     metodo que dado un estado y un caracter retorna true si tiene transicion etiquetada con el caracter
     o alguna transicion lambda
     */
    public boolean tieneTransiciones(State s, Character c) {
        boolean result = false;
        LinkedList<Triple<State, Character, State>> trans = new LinkedList();
        for (Triple<State, Character, State> t : delta) {
            trans.add(t);
        }
        for (int i = 0; i < trans.size(); i++) {
            Triple<State, Character, State> actual = trans.get(i);
            result = result || (actual.first().name().equals(s.name()) && (actual.second().equals(c)));
            //result = result || (actual.first().name().equals(s.name()) && (actual.second().equals(Lambda)));
        }
        return result;
    }

    @Override
    public Set<Triple<State, Character, State>> transiciones() {
        return this.delta;
    }
}
