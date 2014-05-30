package automata;

import static automata.FA.Lambda;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import utils.Triple;
import utils.triplaMoore;

/* Implements a DFA (Deterministic Finite Atomaton).
 */
public class DFA extends FA {

    /*
     Variables globales para almacenar, representan las 5 partes de la tupla que
     es el AFD*/
    Set<State> estados;
    Set<Character> alfabeto;
    Set<Triple<State, Character, State>> delta;
    State inicial;
    Set<State> estados_finales;
    /*	
     * 	Construction
     */

    // Constructor
    public DFA(
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
    public State delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        if (from != null && !c.equals("")) {
            //verificamos que haya una transicion desde from por c
            //si la hay
            if (tieneTransicion(from, c)) {
                //la buscamos y vemos a que estado se dirige
                LinkedList<Triple<State, Character, State>> transiciones = new LinkedList(delta);
                State result = new State("");
                for (int i = 0; i < transiciones.size(); i++) {
                    Triple<State, Character, State> actual = transiciones.get(i);
                    if (actual.first().name().equals(from.name()) && actual.second().equals(c)) {
                        result = (actual.third());
                    }
                }
                //y retornamos dicho estado
                return result;
            } else {
                //en caso de que no haya transicion desde from por c
                //retornamos un estado con nombre vacio ""
                return new State("");
            }
        } else {
            System.out.println("Caracter o Estado Invalido");
            return new State("");
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
        //si la representacion es correcta y la string es valida
        if (rep_ok() && (string != null) && verify_string(string)) {
            //obtenemos los estados finales del automata
            LinkedList<State> estadosFinales = new LinkedList(estados_finales);
            //calculamos la delta acumulada desde el estado inicial con la cadena
            State resultadoDeltaAcum = deltaAcumulada(this.initial_state(), string);
            boolean result = false;
            //y luego verificamos si el estado al que se 
            // llego con la delta acumulada es un estado final
            if (!resultadoDeltaAcum.name().equals("")) {
                for (int i = 0; i < estadosFinales.size(); i++) {
                    State unFinal = estadosFinales.get(i);
                    result = result || (resultadoDeltaAcum.name().equals(unFinal.name()));
                }
                return result;
            } else {
                //en caso de que el resultado haya sido un estado con nombre vacio
                //retornamos falso
                return false;
            }
        } else {
            //si hay algun problema con el parametro o la representacion
            //retornamos falso
            return false;
        }
    }

    /**
     * Converts the automaton to a NFA.
     *
     * @return NFA recognizing the same
     * language.
     */
    public NFA toNFA() {
        assert rep_ok();
        // TODO
        //retornamos un NFA identico al DFA original
        return new NFA(this.states(), this.alphabet(), this.delta, this.inicial, this.final_states());
    }

    /**
     * Converts the automaton to a
     * NFALambda.
     *
     * @return NFALambda recognizing the
     * same language.
     */
    public NFALambda toNFALambda() {
        //creo que con agregarle una transicion lambda desde un estado a si mismo ya se vuelve NFALambda

        assert rep_ok();
        // TODO
        Set<Character> alfabetoLambda = this.alphabet();
        //agregamos Lambda al alfabeto
        alfabetoLambda.add(Lambda);
        //retornamos un NFALambda identico al DFA original pero con Lambda en su alfabeto
        NFALambda result = new NFALambda(this.states(), alfabetoLambda, this.delta, this.inicial, this.final_states());
        return result;
    }

    /**
     * Checks the automaton for language
     * emptiness.
     *
     * @returns True iff the automaton's
     * language is empty.
     */
    public boolean is_empty() {
        assert rep_ok();
        // TODO
        //conjunto de elementos alcanzables desde el estado inicial por cualquier transicion
        LinkedHashSet<State> alcanzables = new LinkedHashSet();
        //obtenemos que conjunto de estados son alcanzables desde el inicial
        alcanzables = (LinkedHashSet<State>) alcanzablesDesde(this.initial_state());
        //conjunto de resultado parcial
        LinkedHashSet<State> alcanzablesParcial = new LinkedHashSet();
        //mientras no se clausure el conjunto resultado
        while (!conjEstadosIguales(alcanzables, alcanzablesParcial)) {
            //    System.out.println("ENTRO AL CICLO...");
            //igualamos los conjuntos
            alcanzablesParcial.addAll(alcanzables);
            alcanzables.clear();
            alcanzables.addAll(alcanzablesParcial);

            System.out.println("alcanza: " + alcanzablesParcial.size());

            //conjunto de resultado interno al ciclo
            LinkedHashSet<State> alcanzablesInterno = new LinkedHashSet(alcanzablesParcial);
            for (State s : alcanzablesParcial) {

                System.out.println("ENTRO A UNION RECURSIVA...");
                System.out.println("desde " + s.name() + " alcanza: ");
                for (State s2 : alcanzablesDesde(s)) {
                    System.out.print(s2.name() + " ");
                }
                System.out.println();
                //calculamos el conjunto de estados alcanzables desde cada estado del conjunto anterior
                //por una transicion y luego unimos los resultados
                alcanzablesInterno = (LinkedHashSet<State>) unirConjuntosEstados(alcanzablesInterno, alcanzablesDesde(s));
            }

            //actualizamos el resultado parcial
            alcanzablesParcial.clear();
            alcanzablesParcial.addAll(alcanzablesInterno);
        }
        boolean vacio = true;
        //lista de nombres de estados finales
        LinkedList<String> nombresFinales = new LinkedList();
        //buscamos los nombres de los estados finales 
        for (State s : this.final_states()) {
            nombresFinales.add(s.name());
        }
        System.out.println("finales: " + nombresFinales.toString());
        //para todo estado alcanzable, vemos si alguno es final
        for (State s : alcanzables) {
            //el lenguaje sera vacio si no se llega a un estado final
            vacio = vacio && (!nombresFinales.contains(s.name()));
        }
        return vacio;
    }

    /**
     * Checks the automaton for language
     * infinity.
     *
     * @returns True iff the automaton's
     * language is finite.
     */
    public boolean is_finite() {// VERIFICAR, ENCONTRE UN CONTRAEJEMPLO

        assert rep_ok();
        boolean condicion = true;

        LinkedList<String> lista_de_primeros = new LinkedList<String>();

        for (Triple<State, Character, State> triple : this.delta) {

            if (!lista_de_primeros.contains(triple.first().name())) {
                lista_de_primeros.add(triple.first().name());
            }

            if (lista_de_primeros.contains(triple.third().name())) {
                condicion = false;
                break;
            }
        }
        System.out.println("ES FINITO?: " + condicion);
        return condicion;
    }

    /**
     * Returns a new automaton which
     * recognizes the complementary
     * language.
     *
     * @returns a new DFA accepting the
     * language's complement.
     */
    public DFA complement() {
        assert rep_ok();
        // TODO
        //nuevo conjunto de estados finales;
        LinkedHashSet<State> nuevosEstadosFinales = new LinkedHashSet();
        //obtenemos los nombres de los estados que posee el automata original
        LinkedList<String> estadosDFA = new LinkedList();
        //obtenemos todos los nombres uno por uno
        for (State s : this.states()) {
            estadosDFA.add(s.name());

        }
        //lista de nombres de los estados finales del DFA original        
        LinkedList<String> nombresFinalesDFA = new LinkedList();
        //obtenemos los nombres de los estados finales del dfa original
        for (State s : this.final_states()) {
            nombresFinalesDFA.add(s.name());
        }
        //lista de nombres de los nuevos estados finales
        LinkedList<String> new_nombresFinalesDFA = new LinkedList();
        //para todo estado
        for (String unNombre : estadosDFA) {
            //tal que no era final en el DFA original
            if (!nombresFinalesDFA.contains(unNombre)) {
                //lo convertimos en estado final del nuevo DFA
                new_nombresFinalesDFA.add(unNombre);
            }
        }
        //creamos el nuevo conjunto de estados finales
        for (String nombre_final : new_nombresFinalesDFA) {
            nuevosEstadosFinales.add(new State(nombre_final));
        }
        //estado final al cual irian todas las transiciones 
        //no definidas o no declaradas en el automata original
        State finalAdicional = new State("qAdic");
        //nuevo conjunto de estados
        LinkedHashSet<State> nuevosEstados = new LinkedHashSet(this.states());
        //le agregamos el nuevo estado al conjunto de estados
        nuevosEstados.add(finalAdicional);
        //y al conjunto de estados finales
        nuevosEstadosFinales.add(finalAdicional);
        //nuevo conjunto de transiciones
        LinkedHashSet<Triple<State, Character, State>> nuevasTransiciones = new LinkedHashSet(this.delta);
        //agregamos una transicion al estado agregado desde cada estado disponible
        for (State s : nuevosEstados) {
            //vemos con que caracteres no hay transiciones desde un estado dado
            for (Character c : this.alphabet()) {
                //para aquellos que no tenga transicion el automata original
                if (!tieneTransicion(s, c)) {
                    //creamos una transicion desde el estado corriente al nuevo estado final agregado
                    Triple<State, Character, State> transicionNueva = new Triple(s, c, finalAdicional);
                    //agregamos la transicion
                    nuevasTransiciones.add(transicionNueva);
                }
            }
        }
        //retornamos un automata finito deterministico
        //cuyos estados finales son todos los estados 
        //que antes no eran finales en el automata original
        DFA complemento = new DFA(nuevosEstados, this.alphabet(), nuevasTransiciones, this.initial_state(), nuevosEstadosFinales);

        return complemento;
    }

    /**
     * Returns a new automaton which
     * recognizes the kleene closure of
     * language.
     *
     * @returns a new DFA accepting the
     * language's complement.
     */
    public DFA star() { //implementado
        assert rep_ok();
        // TODO
        //nuevo conj de estados finales
        LinkedHashSet<State> nuevosFinales = new LinkedHashSet(this.final_states());
        //agregamos el estado inicial al nuevo conjunto de estados finales
        nuevosFinales.add(this.initial_state());
        //nuevo conjunto de transiciones
        LinkedHashSet<Triple<State, Character, State>> nuevasTransiciones = new LinkedHashSet(this.delta);
        //lista de transiciones que inician en el estado inicial
        LinkedHashSet<Triple<State, Character, State>> transInicial = new LinkedHashSet();
        //vemos que transiciones salian del estado inicial
        for (Triple<State, Character, State> t : this.delta) {
            if (t.first().name().equals(this.inicial.name())) {
                transInicial.add(t);
            }
        }
        //a todo estado final le agregamos transiciones como las del estado inicial
        for (Triple<State, Character, State> t : transInicial) {
            for (State s : this.final_states()) {
                Triple<State, Character, State> nuevaTransDesdeFinal = new Triple<State, Character, State>(s, t.second(), t.third());
                //agregamos al conj de transiciones, transiciones identicas a las que salen del inicio, 
                //con el mismo caracter, mismo estado final pero con estado inicial 
                //igual a todos los finales, en si, agregamos las transiciones que salen del nuevo final, 
                //cambiando su salida a cada final
                nuevasTransiciones.add(nuevaTransDesdeFinal);
            }
        }
        //retornamos el nuevo DFA
        return new DFA(this.states(), this.alphabet(), nuevasTransiciones, this.initial_state(), nuevosFinales);
    }

    /**
     * Returns a new automaton which
     * recognizes the union of both
     * languages, the one accepted by
     * 'this' and the one represented by
     * 'other'.
     *
     * @returns a new DFA accepting the
     * union of both languages.
     */
    public DFA union(DFA other) {
        assert rep_ok();
        assert other.rep_ok();
        // nuevo estado inicial
        State nuevoInicial = new State("{" + this.inicial.name() + "," + other.inicial.name() + "}");
        //nueva lista de estados del DFA union
        LinkedHashSet<State> nuevosEstados = new LinkedHashSet();
        //nuevo conjunto de transiciones del DFA union
        LinkedHashSet<Triple<State, Character, State>> nuevasTransiciones = new LinkedHashSet();
        //lista de nombres de los nuevos estados del automata union
        LinkedList<String> nombresNuevosEstados = new LinkedList();
        //para todo estado del 1er automata
        for (State s1 : this.states()) {
            //y todo estado del 2do automata
            for (State s2 : other.states()) {
                //creo un estado combinando un estado del 1er automata con uno del 2do automata
                String nuevoEstadoNombre = "{" + s1.name() + "," + s2.name() + "}";
                nombresNuevosEstados.add(nuevoEstadoNombre);
                State nuevoEstado = new State(nuevoEstadoNombre);
                //creamos el nuevo conjunto de estados
                nuevosEstados.add(nuevoEstado);
            }
        }
        //deltas resultado de aplicar a cada estado todo el alfabeto
        State resultDelta1 = new State("");
        State resultDelta2 = new State("");
        //resultado combinado de ambas deltas (nuevo estado combinado)
        State resultDeltaCombinado = new State("");
        //creamos el nuevo alfabeto union de ambos alfabetos
        LinkedHashSet<Character> nuevoAlfabetoUnion = new LinkedHashSet();
        nuevoAlfabetoUnion.addAll(this.alphabet());
        nuevoAlfabetoUnion.addAll(other.alphabet());
        //ciclo para crear el conjunto de transiciones
        for (State s1 : this.states()) {
            for (State s2 : other.states()) {
                for (Character c : nuevoAlfabetoUnion) {
//                    System.out.println();
                    //calculamos la delta de cada estado
                    resultDelta1 = this.delta(s1, c);
                    //con cada caracter del alfabeto
                    resultDelta2 = other.delta(s2, c);
                    //solo agregamos al conjunto de transiciones, aquellas que modifican al menos un estado
                    if (!resultDelta1.name().equals("") || !resultDelta2.name().equals("")) {
                        //si algunos de los deltas calculados es vacio, 
                        // se asigna el mismo estado, ya que no se puede avanzar por dicho caracter
                        if (resultDelta1.name().equals("")) {
                            resultDelta1 = s1;
                        }
                        if (resultDelta2.name().equals("")) {
                            resultDelta2 = s2;
                        }
                        //nombre del estado resultado de ambas deltas
                        String nombreCombinado = "{" + resultDelta1.name() + "," + resultDelta2.name() + "}";
                        //estado combinado desde donde sale la nueva transicion
                        State from = new State("{" + s1.name() + "," + s2.name() + "}");
                        //estado combinado hacia donde llega la nueva transicion
                        resultDeltaCombinado = new State(nombreCombinado);
                        //y en caso de no estar en el conjunto de estados, lo crea y lo agrega
                        if (!nombresNuevosEstados.contains(nombreCombinado)) {
                            nombresNuevosEstados.add(nombreCombinado);
                            nuevosEstados.add(resultDeltaCombinado);
                        }
                        //creamos una nueva transicion desde from por c hacia el estado
                        //resultado de aplicar delta a los estados del 1er y 2do automata con c
                        Triple<State, Character, State> nuevaTransicion = new Triple(from, c, resultDeltaCombinado);
                        //agregamos la transicion al conjunto de transiciones
                        nuevasTransiciones.add(nuevaTransicion);
                    }
                }
            }
        }
        //lista de nombres de los estados finales de this
        LinkedList<String> finalesThis = new LinkedList();
        //obtenemos los nombres de los estados finales de this
        for (State s : this.final_states()) {
            finalesThis.add(s.name());
        }
        //lista de nombres de los estados finales de other
        LinkedList<String> finalesOther = new LinkedList();
        //obtenemos los nombres de los estados finales de other
        for (State s : other.final_states()) {
            finalesOther.add(s.name());
        }
        //nuevo conjunto de estados finales del automata union
        LinkedHashSet<State> nuevosFinales = new LinkedHashSet();
        for (State s : nuevosEstados) {
            String nombre = s.name();
            System.out.println(nombre);
            String[] partes = nombre.split(",");
            partes[0] = partes[0].replace("{", "");
            partes[1] = partes[1].replace("}", "");
            //si alguno de los estados que formo el estado nuevo, era final de alguno de los DFA originales
            if (finalesThis.contains(partes[0]) || finalesOther.contains(partes[1])) {
                //agregamos el estado actual al conjunto de estados finales del automata union
                nuevosFinales.add(s);
            }

        }
        //retornamos el nuevo DFA union
        return new DFA(nuevosEstados, nuevoAlfabetoUnion, nuevasTransiciones, nuevoInicial, nuevosFinales);
        // TODO
    }

    /**
     * Returns a new automaton which
     * recognizes the intersection of
     * both languages, the one accepted
     * by 'this' and the one represented
     * by 'other'.
     *
     * @returns a new DFA accepting the
     * intersection of both languages.
     */
    public DFA intersection(DFA other) { //VERIFICAR
        assert rep_ok();
        assert other.rep_ok();
        // TODO
        if (this.alfabeto.equals(other.alfabeto)) {
            // nuevo estado inicial
            State nuevoInicial = new State("{" + this.inicial.name() + "," + other.inicial.name() + "}");
            //nueva lista de estados del DFA interseccion
            LinkedHashSet<State> nuevosEstados = new LinkedHashSet();
            //nuevo conjunto de transiciones del DFA interseccion
            LinkedHashSet<Triple<State, Character, State>> nuevasTransiciones = new LinkedHashSet();
            //creamos estados combinando los nombres de los estados de ambos automatas
            for (State s1 : this.states()) {
                for (State s2 : other.states()) {
                    //for (Character c : this.alphabet()) {
                    //para aquellos en que haya transicion definida en ambos automatas para c
                    //if (this.tieneTransicion(s1, c) && other.tieneTransicion(s2, c)) {
                    //creamos el estado nuevo
                    String nuevoEstadoNombre = "{" + s1.name() + "," + s2.name() + "}";
                    State nuevoEstado = new State(nuevoEstadoNombre);
                    //creamos el nuevo conjunto de estados
                    nuevosEstados.add(nuevoEstado);
                    //}
                    //}
                }
            }
            LinkedList<String> nombresNuevosEstados = new LinkedList();
            //obtenemos todos los nombres de los estados para verificar cuando hay que agregar un estado al conjunto y cuando no
            for (State s : nuevosEstados) {
                nombresNuevosEstados.add(s.name());
            }
            //deltas resultado de aplicar a cada estado todo el alfabeto
            State resultDelta1 = new State("");
            State resultDelta2 = new State("");
            //resultado combinado de ambas deltas (nuevo estado combinado)
            State resultDeltaCombinado = new State("");
            //creamos el nuevo alfabeto interseccion de ambos alfabetos
            LinkedHashSet<Character> nuevoAlfabetoInterseccion = new LinkedHashSet(this.alphabet());
            //ciclo para crear el conjunto de transiciones
            for (State s1 : this.states()) {
                for (State s2 : other.states()) {
                    for (Character c : nuevoAlfabetoInterseccion) {
                        //si ambos automatas cambian de estado por el caracter corriente
                        if (this.tieneTransicion(s1, c) && other.tieneTransicion(s2, c)) {
                            //calculamos la delta de cada estado con cada caracter del alfabeto
                            resultDelta1 = this.delta(s1, c);
                            resultDelta2 = other.delta(s2, c);
                            //estado combinado desde donde sale la nueva transicion
                            State from = new State("{" + s1.name() + "," + s2.name() + "}");
                            //crea un nuevo estado con el nombre combinado de los resultados
                            String nombreCombinado = "{" + resultDelta1.name() + "," + resultDelta2.name() + "}";
                            //y en caso de no estar en el conjunto de estados, lo crea y lo agrega
                            resultDeltaCombinado = new State(nombreCombinado);
                            if (!nombresNuevosEstados.contains(nombreCombinado)) {
                                //estado combinado hacia donde llega la nueva transicion
                                nombresNuevosEstados.add(nombreCombinado);
                            }
                            resultDeltaCombinado = new State(nombreCombinado);
                            //nueva transicion
                            Triple<State, Character, State> nuevaTransicion = new Triple(from, c, resultDeltaCombinado);
                            //agregamos la transicion al conjunto de transiciones
                            nuevasTransiciones.add(nuevaTransicion);
                        }
                    }
                }
            }
            LinkedList<String> finalesThis = new LinkedList();
            //obtenemos los nombres de los estados finales de this
            for (State s : this.final_states()) {
                finalesThis.add(s.name());
            }

            LinkedList<String> finalesOther = new LinkedList();
            //obtenemos los nombres de los estados finales de other
            for (State s : other.final_states()) {
                finalesOther.add(s.name());
            }
            //nuevo conjunto de estados finales del automata union
            LinkedHashSet<State> nuevosFinales = new LinkedHashSet();
            for (State s : nuevosEstados) {
                String nombre = s.name();
                System.out.println(nombre);
                String[] partes = nombre.split(",");
                partes[0] = partes[0].replace("{", "");
                partes[1] = partes[1].replace("}", "");
                //son estados finales de la interseccion aquellos 
                //cuyas partes son estados finales de los automatas originales
                if (finalesThis.contains(partes[0]) && finalesOther.contains(partes[1])) {
                    //agregamos el estado actual al conjunto de estados finales del automata interseccion
                    nuevosFinales.add(s);
                }

            }
            return new DFA(nuevosEstados, nuevoAlfabetoInterseccion, nuevasTransiciones, nuevoInicial, nuevosFinales);
        } else {
            //supongo que el estado null es el estado con nombre ""
            return new DFA(new LinkedHashSet<State>(), new LinkedHashSet<Character>(), new LinkedHashSet<Triple<State, Character, State>>(), new State(""), new LinkedHashSet<State>());
        }
    }

    @Override
    public boolean rep_ok() {
        // TODO: Check that the alphabet does not contains lambda.
        // TODO: Check that initial and final states are included in 'states'.
        // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
        // TODO: Check that the transition relation is deterministic.
        boolean inicOk = false;
        boolean finalesOk = true;
        boolean transicionesOk = true;
        //verificamos que el alfabeto no contiene Lambda
        boolean noLambda = !alfabeto.contains(Lambda);
        boolean deterministicOk = true;

        LinkedList<String> states = new LinkedList();
        //buscamos los nombres de los estados
        for (State s : estados) {
            states.add(s.name());
        }
        LinkedList<String> finales = new LinkedList();
        //buscamos los nombres de los estados finales
        for (State f : estados_finales) {
            finales.add(f.name());
        }
        //verificamos que el estado inicial pertenece al conjunto de estados
        for (int i = 0; i < states.size(); i++) {
            inicOk = inicOk || (states.get(i).equals(inicial.name()));
        }
        //verificamos que los estados finales pertenecen al conjunto de estados
        for (int i = 0; i < finales.size(); i++) {
            finalesOk = finalesOk && (states.contains(finales.get(i)));
        }

        //lista de transiciones para verificar si el AF es Det o NoDet
        LinkedList<Triple<State, Character, State>> transiciones = new LinkedList<Triple<State, Character, State>>(delta);
        //verificamos que las transiciones son validas 
        for (Triple<State, Character, State> t : delta) {
            // (los estados utilizados pertenecen al conjunto de estados y el simbolo utilizado pertenece al alfabeto)
            transicionesOk = transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()) && alfabeto.contains(t.second()));
        }
        //ciclo para determinar si el automata es deterministico o no
        for (int i = 0; i < transiciones.size(); i++) {
            for (int j = 0; j < transiciones.size(); j++) {
                if (i != j) {
                    Triple<State, Character, State> elem1 = transiciones.get(i);
                    Triple<State, Character, State> elem2 = transiciones.get(j);
                    if ((elem1.first().name().equals(elem2.first().name())) && (elem1.second().equals(elem2.second()))) {
                        deterministicOk = false;
                        i = transiciones.size();
                        j = transiciones.size();
                    }
                }
            }
        }

        return (inicOk && finalesOk && transicionesOk && noLambda && deterministicOk);

    }

    public State deltaAcumulada(State est, String string) {
        //si la cadena es valida y el estado tambien
        if (est != null && !string.isEmpty()) {
            State result = est;
            State parcial;
            //vamos evaluando la delta obtenida caracter por caracter de la string 
            for (int i = 0; i < string.length(); i++) {
                parcial = delta(result, string.charAt(i));
                if (parcial.name().equals("")) {
                    i = string.length();
                }
                result = parcial;
            }
            return result;
        } else {
            return est;
        }

    }

    /*
     metodo que dado un estado y un caracter c devuelve true si existe una transicion
     saliente desde el estado con etiqueta c
     */
    public boolean tieneTransicion(State s, Character c) {
        boolean result = false;
        for (Triple<State, Character, State> t : this.transiciones()) {
            result = result || (t.first().name().equals(s.name()) && t.second().equals(c));
        }
        return result;
    }

    /*
     metodo que dado un estado devuelve el conjunto de estados alcanzables desde el mismo transitando una sola transicion
     */
    public Set<State> alcanzablesDesde(State s) {
        //conjunto resultado general
        LinkedHashSet<State> result = new LinkedHashSet();
        //el estado es alcanzable desde si mismo
        result.add(s);
        //para toda transicion
        for (Triple<State, Character, State> transicion : this.delta) {
            //que salga desde el estado parametro
            if (transicion.first().name().equals(s.name())) {
                if (!result.contains(transicion.third())) {
                    //agregamos el estado al cual se dirige siempre que 
                    // no este ya contenido en el conjunto resultado
                    result.add(transicion.third());
                }
            }
        }
        return result;
    }

    public Set<Triple<State, Character, State>> transiciones() {
        return this.delta;
    }

    /*
     metodo que dado un automata lo completa de modo que
     desde todo estado hay una transicion definida para cada
     simbolo del alfabeto
     */
    public DFA completarDFA() {

        System.out.println("---completar---");
        //nuevo conjunto de estados
        LinkedHashSet<State> newStates = new LinkedHashSet(this.states());
        //nuevo estado al cual se dirigiran todas las transiciones
        // que falten y seran definidas mas adelante
        State nuevo = new State("adic");
        newStates.add(nuevo);

        //nuevo conjunto de transiciones
        LinkedHashSet<Triple<State, Character, State>> newTransitions = new LinkedHashSet(this.transiciones());
        for (Character c : this.alphabet()) {
            Triple<State, Character, State> nuevaAdic = new Triple(nuevo, c, nuevo);
            newTransitions.add(nuevaAdic);
        }
        //para todo estado del nuevo conjunto de estados
        for (State s : this.states()) {
            //para cada caracter del alfabeto
            for (Character c : this.alphabet()) {
                //vemos si no existe una transicion desde el estado por el caracter corriente
                if (!tieneTransicion(s, c)) {
                    //si no existe la creamos y agregamos al nuevo conj de transiciones
                    Triple<State, Character, State> nuevaTrans = new Triple(s, c, nuevo);
                    newTransitions.add(nuevaTrans);
                }
            }
        }

        //retornamos el nuevo AFD completamente definido
        System.out.println("---FIN COMPLETAR--");
        System.out.println();
        DFA result = new DFA(newStates, this.alphabet(), newTransitions, this.initial_state(), this.final_states());
        /*for (Character c : this.alphabet()) {
         System.out.println("delta desde adic con " + c + " : " + result.delta(nuevo, c).name());
         }
         LinkedList<String> estadosOriginales=new LinkedList();
         for(State s: this.states()){
         estadosOriginales.add(s.name());
         }
         boolean alcanzable=false;
         for(Triple<State, Character, State> t: newTransitions){
         if(estadosOriginales.contains(t.first().name())){
         if(t.third().name().equals(nuevo.name())){
         alcanzable=true;
         }
         }
         }
         System.out.println("adic es alcanzable desde algun original?: "+alcanzable);*/
        return result;

    }

    //metodo que dado un automata y su conjunto de transiciones, elimina los estados inalcanzables
    public DFA limpiarAutomata(Set<Triple<State, Character, State>> transiciones) {
        //nuevo conjunto de transiciones, el cual se modificara y sera parte del resultado
        LinkedList<Triple<State, Character, State>> nuevasTransiciones = new LinkedList(transiciones);
        //lista de estados (nombres de estados)
        LinkedList<String> nombresEstados = new LinkedList();
        //nueva lista de estados para el DFA resultado
        LinkedHashSet<State> nuevosEstados = new LinkedHashSet();
        //nueva lista de estados de estados para el DFA resultado
        LinkedHashSet<State> nuevosEstadosFinales = new LinkedHashSet(this.final_states());
        //obtenemos los nombres de los estados
        for (State s : this.states()) {
            nombresEstados.add(s.name());
        }
        //lista de nombres de estados, a modificar, o sea el resultado de quitar algunos estados inalcanzables
        LinkedList<String> nombresModificados = new LinkedList();
        //mientras las listas de nombres tengan distintas longitudes
        while (nombresModificados.size() != nombresEstados.size()) {
            //System.out.println("nombresEstados antes de entrar a modificar: " + nombresEstados.toString());
            //igualamos las listas de nombres
            nombresModificados = new LinkedList(nombresEstados);
            nombresEstados = new LinkedList(nombresModificados);
            //y comenzamos a verificar si la lista puede minimizarse, es decir, borrar estados inalcanzables
            //para todo estado del conjunto
            for (String s : nombresEstados) {
                if (!s.equals("{}")) {
//                    System.out.println("estado " + s);
//                    int i = nombresEstados.indexOf(s);
//                    System.out.println("indice: " + i);
                    //si el estado evaluado no es el estado inicial
                    if (!s.equals(this.initial_state().name())) {
                        //variable booleana que indica si un estado es alcanzable desde algun otro estado
                        boolean alcanzable = false;
                        //vemos si hay alguna transicion que llegue a el
                        for (Triple<State, Character, State> transicion : nuevasTransiciones) {
                            //si alguna transicion llega al estado, es alcanzable
                            //            System.out.println("transicion: " + transicion.first().name() + " -> " + transicion.second() + " -> " + transicion.third().name());
                            if (transicion.third().name().equals(s)) {
                                alcanzable = true;
                            }
                        }
                        //         System.out.println("estado " + s + " alcanzable? " + alcanzable);
                        //System.out.println("salio de alcanzable");
                        //si no es alcanzable se elimina el estado de la lista
                        if (!alcanzable) {
                            nombresModificados.remove(s);
                            //y eliminamos todas las transiciones que salian desde dicho estado
                            //lista de transiciones a eliminar
                            LinkedList<Triple<State, Character, State>> transARemover = new LinkedList();
                            //vemos que transiciones debemos eliminar
                            for (Triple<State, Character, State> t : nuevasTransiciones) {
                                if (t.first().name().equals(s)) {
                                    //                   System.out.println("remueve " + t.first().name() + " -> " + t.second() + " -> " + t.third().name());
                                    transARemover.add(t);
                                }
                            }
                            nuevasTransiciones.removeAll(transARemover);
                        }

                    }
                }
            }
            //igualamos los conjuntos al final
            nombresEstados = new LinkedList(nombresModificados);
            nombresModificados = new LinkedList(nombresEstados);

        }
        //creamos el nuevo conjunto de estados minimizado (sin los estados inalcanzables)
        for (String nombre : nombresEstados) {
            State estado = new State(nombre);
            nuevosEstados.add(estado);
        }
        //vemos que estados finales fueron removidos del conjunto de estados
        for (State estadoFinal : nuevosEstadosFinales) {
            boolean contenido = false;
            //vemos para todo estado del nuevo conjunto de estados
            for (State s : nuevosEstados) {
                //si el final corriente esta en el conjunto de estados
                if (estadoFinal.name().equals(s.name())) {
                    contenido = true;
                }
            }
            //si el estado final evaluado no pertenece al nuevo conjunto de estados, se elimina
            if (!contenido) {
                nuevosEstadosFinales.remove(estadoFinal);
            }
        }
        //nuevo conjunto de transiciones
        LinkedHashSet<Triple<State, Character, State>> nuevaDelta = new LinkedHashSet(nuevasTransiciones);
        //creamos el DFA resultado
        return new DFA(nuevosEstados, this.alphabet(), nuevaDelta, this.initial_state(), nuevosEstadosFinales);
    }

    /*
     metodo que dado un AFD lo minimiza
     */
    public DFA minimizar() {
        //limpiamos el automata quitando estados inalcanzables
        DFA limpio = this.limpiarAutomata(this.transiciones());
        DFA completo = limpio.completarDFA();

        //lista de estados con acceso directo
        LinkedList<State> estados = new LinkedList(completo.states());
        //listas de clases de estados indistinguibles entre si
        //antes de calcular delta
        LinkedList<LinkedList<State>> clasesIndistinguibles1 = new LinkedList();
        //luego de calcular delta y reagrupar estados
        LinkedList<LinkedList<State>> clasesIndistinguibles2 = new LinkedList();
        //primero separamos los estados en finales y no finales
        //lista de estados finales
        LinkedList<State> finales = new LinkedList();
        //lista de nombres de los estados finales para seleccionar los no finales
        LinkedList<String> nombresFinales = new LinkedList();

        for (State f : completo.final_states()) {
            //si el estado final corriente no es el inicial ----------!!!CONSULTAR!!!
            //if (!f.name().equals(limpio.initial_state().name())) {
            finales.add(f);
            nombresFinales.add(f.name());
            //}
        }
        //lista de estados no finales
        LinkedList<State> noFinales = new LinkedList();
        //seleccionamos los estados no finales del conj de estados

        LinkedList<String> nombresNoFinales = new LinkedList();

        for (State s : completo.states()) {
            //si el nombre del estado no es el nombre de un estado final
            if (!nombresFinales.contains(s.name())) {
                //lo agregamos al conj de estados no finales
                noFinales.add(s);
                nombresNoFinales.add(s.name());
            }
        }

        //System.out.println("finales" + nombresFinales.toString());
        //System.out.println("no finales" + nombresNoFinales.toString());
        //agregamos las listas como la 1era particion de estados
        clasesIndistinguibles1.add(finales);
        clasesIndistinguibles1.add(noFinales);

        //si alguno de los conjuntos creados no es unitario
        if ((finales.size() > 1 || noFinales.size() > 1)) {
            //mientras la particion antes y luego de buscar reducir la cantidad de estados
            //no tengan el mismo tamaño
            while (clasesIndistinguibles1.size() != clasesIndistinguibles2.size()) {

                //System.out.println("entro al ciclo " + clasesIndistinguibles1.size() + " " + clasesIndistinguibles2.size());
                //comenzamos a ver si el conjunto puede ser dividido
                //para cada conjunto vemos si podemos dividirlo en partes
                for (LinkedList<State> unConj : clasesIndistinguibles1) {
                    if (unConj.size() != 1) {

                        LinkedList<String> nombresConjuntoActual = new LinkedList();
                        for (State s : unConj) {
                            nombresConjuntoActual.add(s.name());
                        }
                  //      System.out.println();
                        //      System.out.println("conj actual :" + nombresConjuntoActual.toString());

                        //lista de resultado de aplicar a todo estado del conjunto la delta con cada caracter del alfabeto
                        LinkedList<LinkedList<String>> listaResultadosDeltaUnConjunto = new LinkedList();
                        //      System.out.println(" ------conjunto ----------");
                        for (State s : unConj) {
                            //          System.out.println("estado: asadfasdfasd " + s.name());
                            if (!s.name().equals("")) {
                                //lista de resultados para un estado
                                LinkedList<String> resultadoUnEstado = new LinkedList();
                                for (Character c : completo.alphabet()) {
                                    //                  System.out.println("nombre estado a evaluar: " + s.name() + " caracter " + c + " tiene transicion?: " + completo.tieneTransicion(s, c));
                                    //                  System.out.println("resultado delta : " + completo.delta(s, c).name());
                                    State resultadoDelta = completo.delta(s, c);
                                    //estado resultado de delta(s,c) con todo estado s del conj de estados y caracter c del alfabeto
                                    State resultado = clasesIndistinguibles1.get(perteneceA(completo.delta(s, c), clasesIndistinguibles1)).getFirst();
                  //                  System.out.println("delta " + s.name() + " con " + c + " " + completo.delta(s, c).name() + " pertenece a: " + resultado.name());

                                    //System.out.println("pertenece a: "+clasesIndistinguibles1.get(perteneceA(delta(s,c),clasesIndistinguibles1)).getFirst().name());
                                    if (resultado.name().equals("")) {
                                        resultado = s;
                                    }
                                    //agregamos el resultado a la lista
                                    resultadoUnEstado.add(resultado.name());
                                }
                                //agregamos la lista de resultados a la lista general
                                listaResultadosDeltaUnConjunto.add(resultadoUnEstado);
                            }
                        }
                        //para todo resultado agrupamos los estados que tuvieron el mismo resultado
                        LinkedList<String> estadosYaEvaluados = new LinkedList();
                        for (int i = 0; i < listaResultadosDeltaUnConjunto.size(); i++) {
                            //          System.out.println();
                            //          System.out.println(" evaluados antes:" + estadosYaEvaluados);
                            //          System.out.println(" ------ciclo creacion conjuntos ----------");
                            //si el iesimo estado no fue evaluado
                            //          System.out.println("contiene " + unConj.get(i).name() + " " + estadosYaEvaluados.contains(unConj.get(i).name()));
                            if (!estadosYaEvaluados.contains(unConj.get(i).name())) {
                                //              System.out.println("condicion: " + !estadosYaEvaluados.contains(estados.get(i).name()));
                                //conjunto de estados cuya delta con todo el alfabeto es identica
                                LinkedList<State> unConjunto = new LinkedList();
                                //creamos un conjunto de indistinguibles para el
                                unConjunto.add(unConj.get(i));
                                //              System.out.println("evaluo -1: " + unConj.get(i).name());
                                //y lo marcamos como evaluado
                                estadosYaEvaluados.add(unConj.get(i).name());
                                for (int j = i + 1; j < listaResultadosDeltaUnConjunto.size(); j++) {

                                    //                  System.out.println(" estados " + unConj.get(i).name() + " " + unConj.get(j).name());
                                    //si ambos estados estan el el conjunto actual
                                    if (nombresConjuntoActual.contains(unConj.get(i).name()) && nombresConjuntoActual.contains(unConj.get(j).name())) {

                                        //si el j-esimo estado aun no fue evaluado y marcado como indistinguible aun,
                                        //podemos ver si es indistinguible al i-esimo estado
                                        //                      System.out.println("contiene " + unConj.get(j).name() + " " + estadosYaEvaluados.contains(unConj.get(j).name()));
                                        if (!estadosYaEvaluados.contains(unConj.get(j).name())) {
                                            //si dos conjuntos de resultados son iguales
                                            //                          System.out.println("resultado " + unConj.get(i).name() + " " + listaResultadosDeltaUnConjunto.get(i).toString());
                                            //                          System.out.println("resultado " + unConj.get(j).name() + " " + listaResultadosDeltaUnConjunto.get(j).toString());
                                            if (listaResultadosDeltaUnConjunto.get(i).equals(listaResultadosDeltaUnConjunto.get(j))) {
                                                //agregamos el estado j-esimo a la clase de estados indistinguibles del i-esimo estado
                                                unConjunto.add(unConj.get(j));
                                                //y lo marcamos como ya evaluado
                                                //                              System.out.println("evaluo -2: " + unConj.get(j).name());
                                                estadosYaEvaluados.add(unConj.get(j).name());
                                            }
                                        }
                                    }
                                }

                                LinkedList<String> nombresConj = new LinkedList();
                                for (State estado : unConjunto) {
                                    nombresConj.add(estado.name());
                                }

                                //            System.out.println("conjunto:   {" + nombresConj.toString() + "}");
                                //agregamos el conjunto de estados indistinguibles creado a la nueva particion
                                clasesIndistinguibles2.add(unConjunto);

                                //            System.out.println();
                                //            System.out.println("evaluados despues:" + estadosYaEvaluados);
                            }
                        }
                    } else {
                        if (!clasesIndistinguibles2.contains(unConj)) {
                            clasesIndistinguibles2.add(unConj);
                        }

                    }
                }

                System.out.println("---------------");
                System.out.println("cantidad antes: " + clasesIndistinguibles1.size());
                System.out.println("cantidad despues: " + clasesIndistinguibles2.size());

                //si la cantidad de estados no cambio luego de intentar minimizar
                if (clasesIndistinguibles1.size() == clasesIndistinguibles2.size()) {
                    System.out.println();
                    System.out.println("no cambio la cantidad de conjuntos, terminar");
                    //igualamos las divisiones antes y luego de minimizar para salir del ciclo
                    clasesIndistinguibles1 = new LinkedList(clasesIndistinguibles2);
                    clasesIndistinguibles2 = new LinkedList(clasesIndistinguibles1);
                } else {
                    //sino, actualizamos la division y ciclamos de nuevo
                    System.out.println();
                    System.out.println("cambio la cantidad de conjuntos, recursion");
                    clasesIndistinguibles1 = new LinkedList(clasesIndistinguibles2);
                    clasesIndistinguibles2.clear();

                }
            }

            System.out.println("salio de ciclo, a resultado");
            //si cambio la cantidad de estados
            //comenzamos a crear el nuevo automata minimizado
            if (completo.states().size() > clasesIndistinguibles1.size()) {
                //System.out.println("entro al if");
                //lista de nuevos estados
                LinkedList<State> listaNuevosEstados = new LinkedList();
                //lista de estados originales equivalentes a los nuevos
                LinkedList<String> estadosEquivalentes = new LinkedList();
                //creamos el nuevo conjunto de estados como una lista

                /*System.out.println("cantidad estados al inicio:" + this.states().size());
                 System.out.println("cantidad conj:" + clasesIndistinguibles1.size());*/
                for (LinkedList<State> s : clasesIndistinguibles1) {
                    estadosEquivalentes.add(s.getFirst().name());
                    //creamos el nombre del nuevo estado combinando el nombre de todos los estados del conjunto
                    String nombreEstadoNuevo = "{";
                    for (State e : s) {
                        nombreEstadoNuevo = nombreEstadoNuevo + e.name() + " ";
                    }
                    nombreEstadoNuevo = nombreEstadoNuevo + "}";
                    State nuevo = new State(nombreEstadoNuevo);
                    listaNuevosEstados.add(nuevo);
                }

                //System.out.println("estados equivalentes:" + estadosEquivalentes.toString());
                //nuevo conjunto de transiciones
                LinkedHashSet<Triple<State, Character, State>> nuevasTransiciones = new LinkedHashSet();
                //creamos el nuevo conjunto de transiciones
                for (Triple<State, Character, State> transicionVieja : completo.transiciones()) {
                    //System.out.println("transicion actual: " + transicionVieja.first().name() + " -> " + transicionVieja.second() + " -> " + transicionVieja.third().name());
                    State primero = transicionVieja.first();
                    State tercero = transicionVieja.third();
                    //System.out.println("primero nombre: " + primero.name() + " tercero nombre: " + tercero.name());
                    State pertenecePrimero = clasesIndistinguibles1.get(perteneceA(primero, clasesIndistinguibles1)).getFirst();
                    State perteneceTercero = clasesIndistinguibles1.get(perteneceA(tercero, clasesIndistinguibles1)).getFirst();
                    //si ambos estados de la transicion estan incluidos en los estados utilizados
                    if (estadosEquivalentes.contains(pertenecePrimero.name()) && estadosEquivalentes.contains(perteneceTercero.name())) {
                        //obtenemos los indices de los estados nuevos que serian sus equivalentes
                        int indicePrimero = estadosEquivalentes.indexOf(pertenecePrimero.name());
                        int indiceTercero = estadosEquivalentes.indexOf(perteneceTercero.name());
                        //buscamos los estados para crear la nueva transicion
                        State nuevoPrimero = listaNuevosEstados.get(indicePrimero);
                        State nuevoTercero = listaNuevosEstados.get(indiceTercero);
                        //creamos la nueva transicion
                        Triple<State, Character, State> nuevaTransicion = new Triple(nuevoPrimero, transicionVieja.second(), nuevoTercero);
                        //y la agregamos al nuevo conjunto de transiciones
                        //si es que aun no fue agregada
                        if (!nuevasTransiciones.isEmpty()) {
                            if (!transRepetida(nuevaTransicion, nuevasTransiciones)) {
                                nuevasTransiciones.add(nuevaTransicion);
                                //          System.out.println("transicion agregada: " + nuevaTransicion.first().name() + " -> " + nuevaTransicion.second() + " -> " + nuevaTransicion.third().name());
                            }/* else {
                             System.out.println("repetida");
                             }*/

                        } else {
                            nuevasTransiciones.add(nuevaTransicion);
                            //    System.out.println("transicion agregada: " + nuevaTransicion.first().name() + " -> " + nuevaTransicion.second() + " -> " + nuevaTransicion.third().name());
                        }
                    }
                }

                //System.out.println("cantidad transiciones " + nuevasTransiciones.size());
                //nuevo conjunto de estados finales
                LinkedHashSet<State> nuevosFinales = new LinkedHashSet();
                //creamos el nuevo conjunto de estados finales
                //para todo estado final del automata original, vemos cuales siguen estando en el minimizado
                for (State finalViejo : completo.final_states()) {
                    //si esta en la lista de estados equivalentes creada antes
                    if (estadosEquivalentes.contains(finalViejo.name())) {
                        //obtenemos el indice en que esta su equivalente
                        int indiceNuevoFinal = estadosEquivalentes.indexOf(finalViejo.name());
                        //System.out.println("final agregado: " + finalViejo.name());
                        //y buscamos su equivalente para agregarlo al nuevo conjunto de estados finales
                        nuevosFinales.add(listaNuevosEstados.get(indiceNuevoFinal));
                    }
                }
                //System.out.println("cant finales: " + nuevosFinales.size());
                //System.out.println("inicial viejo:" + completo.initial_state().name());
                State inicialNuevo = new State("{" + clasesIndistinguibles1.get(perteneceA(completo.initial_state(), clasesIndistinguibles1)).getFirst().name() + " }");
                //System.out.println("inicial nuevo: " + inicialNuevo.name());

                //creamos el automata minimizado
                DFA minimizado = new DFA(new LinkedHashSet(listaNuevosEstados), completo.alphabet(), nuevasTransiciones, inicialNuevo, nuevosFinales);
                return minimizado;
            } else {//si no cambia la cantidad de estados devolvemos el mismo automata original
                System.out.println("entro al else");
                System.out.println("no es posible minimizarlo");
                return completo;
            }
        } else {
            System.out.println("no es posible minimizarlo, solo son dos nodos");
            return completo;
        }
    }

    /*
     metodo que dado un estado y una particion de estados retorna el indice de 
     la particiona la cual pertenece retorna -1 si el estado no pertenece a ningun conjunto, 
     lo cual indicaria que se debe crear un conjunto
     */
    public int perteneceA(State estado, LinkedList<LinkedList<State>> particion) {
        //System.out.println("estado a evaluar en pertenece a : " + estado.name());
        int indice = -1;
        for (int i = 0; i < particion.size(); i++) {
            for (State s : particion.get(i)) {
                if (s.name().equals(estado.name())) {
                    indice = i;
                    i = particion.size();
                }
            }
        }
        return indice;
    }

    /*
     metodo que dado un conj de transiciones y una transicion
     retorna true si la transicion no esta en el conjunto
     */
    public boolean transRepetida(Triple<State, Character, State> trans, LinkedHashSet<Triple<State, Character, State>> transiciones) {
        boolean repetida = false;
        for (Triple<State, Character, State> t : transiciones) {
            repetida = repetida || (t.first().name().equals(trans.first().name()) && t.second().equals(trans.second()) && t.third().name().equals(trans.third().name()));
        }
        return repetida;
    }

    /*
     metodo que dados dos DFA devuelve true si tienen el mismo lenguaje
     */
    public boolean mismoLenguaje(DFA other) {
        boolean result = true;
        //si tienen distinto alfabeto, no pueden tener el mismo lenguaje
        if (!this.alphabet().equals(other.alphabet())) {
            //retornamos false
            return false;
        } else {//si tienen el mismo alfabeto
            //minimizamos los automatas
            DFA minimThis = this.minimizar();
            DFA minimOther = other.minimizar();
            //si tienen distinta cantidad de estados, no tienen el mismo lenguaje y retornamos false
            if (minimThis.states().size() != minimOther.states().size()) {
                return false;
            }
            //si uno tiene mas estados finales que el otro, retornamos false
            if (minimThis.final_states().size() != minimOther.final_states().size()) {
                return false;
            }
            //si uno tiene mas transiciones que otro, retornamos false
            if (minimThis.transiciones().size() != minimOther.transiciones().size()) {
                return false;
            }
            //si llego aca, es que tienen la misma cantidad de estados, estados finales y transiciones
            //listas de estados de ambos automatas minimizados
            LinkedList<State> estadosThis = new LinkedList(minimThis.states());
            LinkedList<State> estadosOther = new LinkedList(minimOther.states());

            //lista de estados al avanzar sobre this desde el inicial por todas las transiciones posibles un nivel a la vez
            LinkedList<State> estadosPorThis = new LinkedList();
            //caracteres por los cuales se avanzo en this a cada estado
            LinkedList<Character> caracteresUsadosThis = new LinkedList();

            //lista de estados al avanzar sobre other desde el inicial por todas las transiciones posibles un nivel a la vez
            LinkedList<State> estadosPorOther = new LinkedList();
            //caracteres por los cuales se avanzo en this a cada estado
            LinkedList<Character> caracteresUsadosOther = new LinkedList();

            //listas de nombres de los estados finales de ambos automatas
            LinkedList<String> nombresFinalesThis = new LinkedList();
            LinkedList<String> nombresFinalesOther = new LinkedList();
            //obtenemos los nombres de los estados de ambos automatas
            for (State s : minimThis.final_states()) {
                nombresFinalesThis.add(s.name());
            }

            for (State s : minimOther.final_states()) {
                nombresFinalesOther.add(s.name());
            }

            //avanzamos desde el inicial por todos los caracteres posibles en ambos automatas
            //si alguno de los automatas no tiene una transicion por el caracter 
            //corriente desde el inicial y el otro si, retornamos false
            for (Character c : minimThis.alphabet()) {
                //si ambos automatas tienen transiciones desde el inicial por el caracter c
                System.out.println(c + " : " + minimThis.tieneTransicion(minimThis.inicial, c) + " " + minimOther.tieneTransicion(minimOther.inicial, c));

                if (minimThis.tieneTransicion(minimThis.inicial, c)) {
                    if (minimOther.tieneTransicion(minimOther.inicial, c)) {
                        //calculamos los resultados y los guardamos

                        //System.out.println("ambos tienen transicion por " + c);
                        estadosPorThis.add(minimThis.delta(minimThis.inicial, c));
                        caracteresUsadosThis.add(c);
                        estadosPorOther.add(minimOther.delta(minimOther.inicial, c));
                        caracteresUsadosOther.add(c);
                    } else {
                        //si el 2do no tiene transicion, desde el inicial con c, retornamos false

                        //System.out.println("el 2do no tiene por " + c);
                        return false;
                    }
                } else {//si el primer automata no tiene una transicion desde inicial con c y el 2do si la tiene
                    if (minimOther.tieneTransicion(minimOther.inicial, c)) {

                        //                        System.out.println("el 1ero no tiene y el 2do si por " + c);
                        //retornamos false
                        return false;
                    } else {//caso contrario no hacemos nada y cambiaria de caracter
                        System.out.println("ninguno tiene transicion por " + c + " : " + minimThis.tieneTransicion(minimThis.inicial, c) + " " + minimOther.tieneTransicion(minimOther.inicial, c));
                    }
                }
            }
            //si algun caracter usado no es el mismo en ambos automatas, retornamos false
            for (int i = 0; i < caracteresUsadosThis.size(); i++) {
                if (!caracteresUsadosThis.get(i).equals(caracteresUsadosOther.get(i))) {
                    return false;
                }
            }

            //lista de estados ya visitados
            LinkedList<String> YaEvaluados = new LinkedList();
            //marcamos que ya se paso por el inicial
            YaEvaluados.add(minimThis.initial_state().name());

            //mientras los conjuntos de estados sean iguales y no se hayan evaluado todos los estados de los automatas
            while (estadosPorThis.size() == estadosPorOther.size() && (YaEvaluados.size() < minimThis.states().size())) {
                caracteresUsadosThis.clear();
                caracteresUsadosOther.clear();

                /*System.out.println("entro ciclo while - YaEvaluados - " + YaEvaluados.size() + " - estados -" + this.states().size());
                 System.out.println(estadosPorThis.size() + " - - " + estadosPorOther.size());
                 System.out.println("ya evaluados " + YaEvaluados.toString());
                 System.out.println("ya evaluados size" + YaEvaluados.size());*/
                //listas de resultado interno al ciclo
                LinkedList<State> porThis = new LinkedList();
                LinkedList<State> porOther = new LinkedList();
                for (int i = 0; i < estadosPorThis.size(); i++) {
                    /*System.out.println("iesimo estado this " + estadosPorThis.get(i).name());
                     System.out.println("iesimo estado other " + estadosPorOther.get(i).name());
                     System.out.println("condicion if: " + YaEvaluados.contains(estadosPorThis.get(i).name()));*/
                    //si el estado corriente aun no fue evaluado, lo evaluamos sino no 
                    if (!YaEvaluados.contains(estadosPorThis.get(i).name())) {
                        for (Character c : this.alphabet()) {
                            //si algun automata no tiene transicion desde el i-esimo elemento por algun caracter c, y el otro si
                            //retornamos false, sino seguimos evaluando

                            if (minimThis.tieneTransicion(estadosPorThis.get(i), c)) {
                                if (minimOther.tieneTransicion(estadosPorOther.get(i), c)) {

                                    System.out.println("ambos tienen transicion por " + c);

                                    //calculamos los resultados y los guardamos
                                    estadosPorThis.add(minimThis.delta(estadosPorThis.get(i), c));
                                    caracteresUsadosThis.add(c);
                                    estadosPorOther.add(minimOther.delta(estadosPorOther.get(i), c));
                                    caracteresUsadosOther.add(c);
                                } else {
                                    //si el 2do no tiene transicion, desde el i-esimo estado con c, retornamos false
                                    System.out.println("el 2do no tiene por " + c);
                                    return false;
                                }
                            } else {//si el primer automata no tiene una transicion desde el i-esimo estado con c y el 2do si la tiene
                                if (minimOther.tieneTransicion(estadosPorOther.get(i), c)) {
                                    System.out.println("el 1ero no tiene y el 2do si por " + c);
                                    //retornamos false
                                    return false;
                                } else {//caso contrario no hacemos nada y cambiaria de caracter
                                    System.out.println("ninguno tiene transicion por " + c + " : " + minimThis.tieneTransicion(estadosPorThis.get(i), c) + " " + minimOther.tieneTransicion(estadosPorOther.get(i), c));
                                }
                            }
                        }
                        //agregamos los estados que ya visitamos
                        //tecnicamente si son iguales visitaremos estados equivalentes y siempre la misma cantidad en cada momento
                        //habra sido visitada en cada automata
                        if (!YaEvaluados.contains(estadosPorThis.get(i).name())) {
                            YaEvaluados.add(estadosPorThis.get(i).name());
                        }
                    }
                }

                //si los caracteres con que se evaluaron no tienen el mismo orden en la lista
                //de caracteres utilizados en cada automata al evaluar, devolvemos false
                for (int i = 0; i < caracteresUsadosThis.size(); i++) {
                    if (!caracteresUsadosThis.get(i).equals(caracteresUsadosOther.get(i))) {
                        return false;
                    }
                }
                //si al evaluar resultados, los i-esimos elementos de cada lista de resultados
                //uno es final y el otro no, retornamos false, sino seguimos evaluando
                for (int i = 0; i < porThis.size(); i++) {
                    if (!nombresFinalesThis.contains(porThis.get(i).name()) || !nombresFinalesOther.contains(porOther.get(i).name())) {
                        return false;
                    }
                }
                //actualizamos los estados en los que el cada automata quedo "parado"
                //y volvemos a ciclar
                estadosPorThis = new LinkedList(porThis);
                estadosPorOther = new LinkedList(porOther);
            }
            return result;
        }
    }

    //metodo que dados dos DFA devuelve true si tienen el mismo lenguaje
    //IMPLEMENTADO CON TEOREMA DE MOORE
    public boolean sameLanguage(DFA other) {
        //si tienen distinto alfabeto, no pueden tener el mismo lenguaje
        if (!this.alphabet().equals(other.alphabet())) {
            //retornamos false
            return false;
        } else {//si tienen el mismo alfabeto
            //minimizamos los automatas
            DFA minimThis = this.minimizar();
            DFA minimOther = other.minimizar();
            //si tienen distinta cantidad de estados, no tienen el mismo lenguaje y retornamos false
            if (minimThis.states().size() != minimOther.states().size()) {
                return false;
            }
            //si uno tiene mas estados finales que el otro, retornamos false
            if (minimThis.final_states().size() != minimOther.final_states().size()) {
                return false;
            }
            //si uno tiene mas transiciones que otro, retornamos false
            if (minimThis.transiciones().size() != minimOther.transiciones().size()) {
                return false;
            }
            //lista de triplas que representan los nodos del arbol de moore en que estariamos ubicados
            LinkedList<triplaMoore> triplas = new LinkedList();
            State inicialThis = minimThis.initial_state();
            State inicialOther = minimOther.initial_state();

            //lista de elementos ya evaluados
            LinkedList<String> nombresYaEvaluados = new LinkedList();
            //marcamos los iniciales de ambos automatas como ya evaluadas
            nombresYaEvaluados.add(inicialThis.name());
            nombresYaEvaluados.add(inicialOther.name());

            for (Character c : minimThis.alphabet()) {
                //y agregamos los resultados de las deltas a los ya evaluados
                String cad = "" + c;
                State deltaA = minimThis.delta(minimThis.initial_state(), c);
                if (!nombresYaEvaluados.contains(deltaA.name())) {
                    nombresYaEvaluados.add(deltaA.name());
                }
                triplaMoore nuevaTriplaA = new triplaMoore(minimThis.initial_state(), cad, deltaA);
                triplas.add(nuevaTriplaA);
            }
            for (Character c : minimOther.alphabet()) {
                //y agregamos los resultados de las deltas a los ya evaluados
                String cad = "" + c;
                State deltaB = minimOther.delta(minimOther.initial_state(), c);
                if (!nombresYaEvaluados.contains(deltaB.name())) {
                    nombresYaEvaluados.add(deltaB.name());
                }
                triplaMoore nuevaTriplaB = new triplaMoore(minimOther.initial_state(), cad, deltaB);
                triplas.add(nuevaTriplaB);
            }

            //para toda tripla vemos si su 3er elem es final en algun automata y en el otro tambien
            for (int i = 0; i < triplas.size() / 2; i++) {
                for (int j = i + triplas.size() / 2; j < triplas.size(); j++) {
                    //si el i-esimo elemento (pertenece a la minimizacion del this) y el j-esimo elemento (pertenece
                    //a la minimizacion de other) son finales seguimos, si alguno es final y el otro no, retornamos false
                    //si el i-esimo no es final y el j-esimo si, retornamos false
                    if (!minimThis.esFinal(triplas.get(i).getEstadoSalida()) && minimOther.esFinal(triplas.get(j).getEstadoSalida())) {
                        return false;
                    }
                    //si el i-esimo es final y el j-esimo no, retornamos false
                    if (minimThis.esFinal(triplas.get(i).getEstadoSalida()) && !minimOther.esFinal(triplas.get(j).getEstadoSalida())) {
                        return false;
                    }
                }
            }
            //si sale del for es porque no encontro dos elementos i-esimo y j-esimo tal que uno era final y el otro no

            //mientras queden elementos por evaluar
            while (nombresYaEvaluados.size() < (minimThis.states().size() * 2)) {
                //nueva lista de triplas
                LinkedList<triplaMoore> nuevasTriplas = new LinkedList();
                String cadena = "";
                for (triplaMoore t : triplas) {
                    cadena = t.getCadenaUsada();
                    for (Character c : minimThis.alphabet()) {
                        cadena = cadena + c;

                        State deltaA = minimThis.deltaAcumulada(minimThis.initial_state(), cadena);
                        nombresYaEvaluados.add(deltaA.name());
                        triplaMoore nuevaTriplaA = new triplaMoore(minimThis.initial_state(), cadena, deltaA);
                        nuevasTriplas.add(nuevaTriplaA);

                        State deltaB = minimOther.deltaAcumulada(minimOther.initial_state(), cadena);
                        nombresYaEvaluados.add(deltaB.name());
                        triplaMoore nuevaTriplaB = new triplaMoore(minimOther.initial_state(), cadena, deltaB);
                        nuevasTriplas.add(nuevaTriplaB);
                    }
                }

                //para toda tripla vemos si su 3er elem es final en algun automata y en el otro tambien
                for (int i = 0; i < triplas.size() / 2; i++) {
                    for (int j = i + triplas.size() / 2; j < triplas.size(); j++) {
                        //si el i-esimo elemento (pertenece a la minimizacion del this) y el j-esimo elemento (pertenece
                        //a la minimizacion de other) son finales seguimos, si alguno es final y el otro no, retornamos false
                        //si el i-esimo no es final y el j-esimo si, retornamos false
                        if (!minimThis.esFinal(triplas.get(i).getEstadoSalida()) && minimOther.esFinal(triplas.get(j).getEstadoSalida())) {
                            return false;
                        }
                        //si el i-esimo es final y el j-esimo no, retornamos false
                        if (minimThis.esFinal(triplas.get(i).getEstadoSalida()) && !minimOther.esFinal(triplas.get(j).getEstadoSalida())) {
                            return false;
                        }
                    }
                }
                //si sale del for es porque no encontro dos elementos i-esimo y j-esimo tal que uno era final y el otro no

                //luego de calcular las nuevas triplas, actualizamos el conjunto
                triplas = new LinkedList(nuevasTriplas);
            }

            LinkedList<LinkedList<Character>> estadosConCicloThis = new LinkedList();
            LinkedList<LinkedList<Character>> estadosConCicloOther = new LinkedList();
            //para toda tripla vemos si su 3er elem es final en algun automata y en el otro tambien
            for (int i = 0; i < triplas.size() / 2; i++) {
                for (int j = i + triplas.size() / 2; j < triplas.size(); j++) {
                    //si el i-esimo elemento del primer automata tiene ciclo a si mismo por algun caracter y el j-esimo tambien
                    //tiene ciclo por el mismo caracter
                    //para todo caracter del alfabeto
                    State actualA = triplas.get(i).getEstadoAlQueLlega();
                    State actualB = triplas.get(j).getEstadoAlQueLlega();
                    LinkedHashSet ciclosActualA=minimThis.ciclaPor(actualA);
                    LinkedHashSet ciclosActualB=minimOther.ciclaPor(actualB);
                    System.out.println("estado A."+actualA.name()+" cicla por "+ciclosActualA.toString());
                    System.out.println("estado B."+actualB.name()+" cicla por "+ciclosActualB.toString());
                    if(!ciclosActualA.equals(ciclosActualB)){
                        return false;
                    }
                }
            }
            //si sale del for es porque no encontro dos elementos i-esimo y j-esimo tal que uno era final y el otro no

            //si sale del ciclo, es que ambos automatas son equivalentes, por ende retornamos true
            return true;
        }

    }

    /*
     metodo que dado un estado devuelve si es final
     */
    public boolean esFinal(State s) {
        LinkedList<String> nombresFinales = new LinkedList();
        for (State f : this.final_states()) {
            nombresFinales.add(f.name());
        }
        return (nombresFinales.contains(s.name()));
    }

    /*
     metodo que dado un estado devuelve el conj de caracteres por los cuales tiene un ciclo a si mismo
     */
    public LinkedHashSet<Character> ciclaPor(State s) {
        LinkedHashSet<Character> result = new LinkedHashSet();
        System.out.println("caract");
        for (Character c : this.alphabet()) {
            
            if (tieneTransicion(s, c)) {
                if (this.delta(s, c).name().equals(s.name())) {
                    result.add(c);
                    System.out.println(c);
                }
            }
        }
        return result;
    }
}
