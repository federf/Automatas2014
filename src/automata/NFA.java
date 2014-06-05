package automata;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import utils.Triple;

public class NFA extends FA {

    /*
     Variables globales para almacenar, representan las 5 partes de la tupla que
     es el AFN*/
    Set<State> estados;
    Set<Character> alfabeto;
    Set<Triple<State, Character, State>> delta;
    State inicial;
    Set<State> estados_finales;
    /*
     *  Construction
     */

    // Constructor
    public NFA(
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

    @Override
    public Set<State> delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        if (from != null && !c.equals("")) {// si from no es null y la cadena no es vacia (para lambda corresponde usar '_')
            LinkedList<Triple<State, Character, State>> transiciones = new LinkedList(delta);
            /*for (Triple<State, Character, State> t : delta) {
             transiciones.add(t);
             }*/
            Set<State> result = new LinkedHashSet<State>(); //set de resultado
            for (int i = 0; i < transiciones.size(); i++) {
                Triple<State, Character, State> actual = transiciones.get(i);
                if (actual.first().name().equals(from.name()) && actual.second().equals(c)) {
                    result.add(actual.third());
                }
            }

            /*String resultadoNombres = "[";
            for (State t : result) {
                resultadoNombres = resultadoNombres + t.name().toString() + ";";
            }
            resultadoNombres = resultadoNombres + "]";*/
            //System.out.println("resultado por " + c + " desde " + from.name() + ": " + resultadoNombres);
            return result;
        } else {
            System.out.println("Caracter o Estado Invalido");
            return new LinkedHashSet<State>();
        }
    }

    @Override
    public String to_dot() {
        assert rep_ok();
        // TODO
        String result = "digraph {\n";
        result = result + "    " + "inic[shape=point];\n";
        result = result + "    " + "inic->" + inicial.name() + ";\n";
        String transiciones = "\n"; //cadena con las transiciones
        for (Triple<State, Character, State> t : delta) {
            transiciones = transiciones + "    " + t.first().name() + "->" + t.third().name() + "[label=" + '"' + t.second() + '"' + "];\n";
        }
        String finales = "\n";
        for (State s : estados_finales) {
            finales = finales + "    " + s.name() + "[shape=doublecircle];\n";
        }
        result = result + transiciones + "\n" + finales + "}";
        return result;
    }

    /*
     *  Automata methods
     */
    @Override
    public boolean accepts(String string) {
        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        if (rep_ok() && (string != null) && verify_string(string)) {
            //calcular la delta acumulada de string y luego comparar el conjunto retornado con el conj de estados finales
            //lista de estados
            LinkedHashSet<State> acum = new LinkedHashSet();
            //agregamos el inicial
            acum.add(inicial);
            //aplicamos la delta acumulada
            Set<State> deltaAcum = deltaAcumulada(acum, string);
            boolean accepted = false;
            //al final, verificamos si el ultimo conjunto de estados contiene al menos un estado final
            for (State s : deltaAcum) {
                for (State s2 : estados_finales) {
                    //System.out.println("estado obtenido?: " + s.name() + " comparado a: " + s2.name());
                    //si es asi, la cadena es aceptada
                    accepted = accepted || (s.name().equals(s2.name()));
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
        State q0 = new State("{" + this.initial_state().name() + " }");
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
        //nueva lista de transiciones de set de estados en set de estados
        LinkedHashSet<Triple<Set<State>, Character, Set<State>>> nuevasTransiciones = new LinkedHashSet();
        for (Set<State> set : conjPotencia) {
            for (Character c : this.alphabet()) {
                //resultado de aplicar un caracter a un set de estados
                LinkedHashSet<State> resultadoSet = new LinkedHashSet();
                for (State estado : set) {
                    //calculamos la delta de cada estado del conjunto con el caracter actual
                    Set<State> deltaUnConjunto = delta(estado, c);
                    //unimos los resultados en un mismo conjunto resultado
                    resultadoSet = (LinkedHashSet<State>) unirConjuntosEstados(resultadoSet, deltaUnConjunto);
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
        DFA result = new DFA(new LinkedHashSet(nuevosEstadosSimples), this.alphabet(), transiciones, q0, nuevosFinales);
        /*
         LIMPIAMOS EL AUTOMATA, ELIMINANDO ESTADOS INALCANZABLES Y LAS TRANSICIONES QUE SALGAN DE DICHOS ESTADOS
         */
        result = result.limpiarAutomata(result.delta);
        return result;
    }

    @Override
    public boolean rep_ok() {
        // TODO: Check that the alphabet does not contains lambda.
        // TODO: Check that initial and final states are included in 'states'.
        // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
        boolean inicOk = false;
        boolean finalesOk = true;
        boolean transicionesOk = true;
        boolean noLambda = !alfabeto.contains(Lambda); //verificamos que el alfabeto no contiene Lambda

        LinkedList<String> states = new LinkedList();
        for (State s : estados) { //buscamos los nombres de los estados
            states.add(s.name());
        }
        //System.out.println("estados: " + states);
        LinkedList<String> finales = new LinkedList();
        for (State f : estados_finales) {  //buscamos los nombres de los estados finales
            finales.add(f.name());
        }
        //System.out.println("estados finales: " + finales);

        for (int i = 0; i < states.size(); i++) { //verificamos que el estado inicial pertenece al conjunto de estados
            inicOk = inicOk || (states.get(i).equals(inicial.name()));
        }

        for (int i = 0; i < finales.size(); i++) { //verificamos que los estados finales pertenecen al conjunto de estados
            finalesOk = finalesOk && (states.contains(finales.get(i)));
        }

        for (Triple<State, Character, State> t : delta) { //verificamos que las transiciones son validas 
            transicionesOk = transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()) && alfabeto.contains(t.second())); // (los estados utilizados pertenecen al conjunto de estados y el simbolo utilizado pertenece al alfabeto)
        }
        //System.out.println("transOk: " + transicionesOk + " finalesOk: " + finalesOk + " inicOk: " + inicOk + " noLamda en alfabeto: " + noLambda);

        return (inicOk && finalesOk && transicionesOk && noLambda);

    }

    public Set<State> deltaAcumulada(Set<State> est, String string) {
        LinkedHashSet<State> result = new LinkedHashSet();
        LinkedHashSet<State> resultadoParcial = new LinkedHashSet();//lista de estados ya obtenidos
        LinkedHashSet<State> conUnEstado = new LinkedHashSet();//conjunto resultado de delta sobre un unico estado
        if (!est.isEmpty() && !string.isEmpty()) {
            for (State s : est) {//para cada estado del conjunto pasado como parametro
                conUnEstado = (LinkedHashSet<State>) delta(s, string.charAt(0));//calculamos la delta con el 1er caracter de la cadena
                resultadoParcial = (LinkedHashSet<State>) unirConjuntosEstados(resultadoParcial, conUnEstado);//unimos los resultados parciales
            }
            if (resultadoParcial.isEmpty()) {//si el resultado parcial es vacio
                return new LinkedHashSet();//retornamos conjunto vacio
            } else {//sino, llamamos recursivamente con la subcadena pasada como parametro, sin su 1er elemento
                String subString = string.substring(1);
                result = (LinkedHashSet<State>) deltaAcumulada(resultadoParcial, subString);//llamada recursiva
                return result;
            }
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

    @Override
    public Set<Triple<State, Character, State>> transiciones() {
        return this.delta;
    }
}
