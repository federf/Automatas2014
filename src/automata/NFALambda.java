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
        System.out.println();
        System.out.println("acepta " + string + "?");

        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        if (rep_ok() && (string != null) && verify_string(string)) {
            //calcular la delta acumulada de string y luego comparar el conjunto retornado con el conj de estados finales
            LinkedList<Triple<State, Character, State>> clausura = new LinkedList();//lista para calculo de clausura lamba del estado inicial
            LinkedHashSet<State> acum = new LinkedHashSet(clausuraLambda(this.initial_state(), clausura));  //calculamos la clausura lambda del estado inicial
            Set<State> deltaAcum = deltaAcumulada(acum, string); //aplicamos la delta acumulada
            boolean accepted = false;
            for (State s2 : this.final_states()) {
                System.out.println("estado: " + s2.name());
            }
            System.out.print("delta acumulada {");
            for (State s : deltaAcum) {
                System.out.print(s.name() + " ");
            }
            System.out.print("}");
            System.out.println();
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
        for (State s : this.final_states()) {//obtenemos los nombres de los estados finales originales para comparacion
            finalesOriginales.add(s.name());
        }
        State q0 = new State(inicial.name()); //crea un nuevo estado inicial para el AFD
        Set<Set<State>> conjPotencia = FA.powerSet(this.states());//conjunto potencia de los estados del NFA original
        Set<Set<State>> nuevosEstadosFinales = new LinkedHashSet();//lista de nuevos estados finales
        for (Set<State> set : conjPotencia) {
            LinkedList<String> nombresEstadosSet = new LinkedList();//lista de nombres de los estados del set
            for (State estado : set) {
                nombresEstadosSet.add(estado.name());
            }
            boolean esFinal = false;
            for (String finalNuevo : nombresEstadosSet) {
                esFinal = esFinal || (finalesOriginales.contains(finalNuevo));
            }
            if (esFinal) {//si el conj contiene almenos uno de los estados finales del automata original
                nuevosEstadosFinales.add(set);//lo agregamos al conj de nuevos estados finales
            }
        }

        LinkedHashSet<Triple<Set<State>, Character, Set<State>>> nuevasTransiciones = new LinkedHashSet();
        for (Set<State> set : conjPotencia) {
            for (Character c : this.alphabet()) {
                LinkedHashSet<State> resultadoSet = new LinkedHashSet();//resultado de aplicar un caracter a un set de estados
                for (State estado : set) {
                    Set<State> delta = delta(estado, c);//calculamos la delta de cada estado del conjunto con el caracter actual
                    resultadoSet = (LinkedHashSet<State>) unirConjuntosEstados(resultadoSet, delta); //unimos los resultados en un mismo conjunto resultado
                }
                Triple<Set<State>, Character, Set<State>> transicion = new Triple(set, c, resultadoSet); //creamos la transicion
                nuevasTransiciones.add(transicion); //y la agregamos al conjunto de transiciones
            }
        }

        LinkedList<Set<State>> listaConjEstados = new LinkedList(conjPotencia);//lista de conjuntos de estados
        System.out.println("cuanto tiene?: " + conjPotencia.size());

        /**
         * ****************************COMIENZO
         * TRANSFORMAR CONJ ESTADOS EN
         * ESTADOS*************************************
         */
        LinkedList<State> listaEstados = new LinkedList();//lista de estados
        for (int i = 0; i < listaConjEstados.size(); i++) {
            State nuevo = new State("q" + i); //creamos estados equivalentes
            listaEstados.add(nuevo);
        }
        LinkedHashSet<Triple<State, Character, State>> transicionesEstados = new LinkedHashSet();//nueva lista de transiciones
        for (Triple<Set<State>, Character, Set<State>> transicionSets : nuevasTransiciones) {//para todas las transiciones de set de estados en set de estados
            int indicePrimero = 0; //indices del 1er y 3er elementos de la transicion actual
            int indiceTercero = 0;
            for (int i = 0; i < listaConjEstados.size(); i++) {//ciclo para buscar el indice en que esta guardado el set de estados corriente
                if (conjEstadosIguales(transicionSets.first(), listaConjEstados.get(i))) {
                    indicePrimero = i;
                }
                if (conjEstadosIguales(transicionSets.third(), listaConjEstados.get(i))) {
                    indiceTercero = i;
                }
                Triple<State, Character, State> transicionEstados = new Triple(listaEstados.get(indicePrimero), transicionSets.second(), listaEstados.get(indiceTercero));//creamos para cada transicion de sets en sets una de estado en estado equivalente
                transicionesEstados.add(transicionEstados);
            }
        }

        /**
         * ********************CREACION
         * CONJ DE ESTADOS
         * FINALES******************
         */
        LinkedHashSet<State> listaEstadosFinales = new LinkedHashSet();//set de estados finales
        for (Set<State> set : nuevosEstadosFinales) {//para todo set que es estado final
            int indiceUnFinal = 0;//indice en que se ubica un set que es estado final
            for (int i = 0; i < listaConjEstados.size(); i++) {
                if (conjEstadosIguales(set, listaConjEstados.get(i))) {
                    listaEstadosFinales.add(listaEstados.get(i));
                }
            }
        }

        /**
         * *************************CREACION
         * DEL
         * DFA**********************************
         */
        DFA result = new DFA(new LinkedHashSet(listaEstados), this.alphabet(), transicionesEstados, this.initial_state(), listaEstadosFinales);
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
        //System.out.println("estados finales: " + finales);

        for (int i = 0; i < states.size(); i++) { //verificamos que el estado inicial pertenece al conjunto de estados
            inicOk = inicOk || (states.get(i).equals(inicial.name()));
        }

        for (int i = 0; i < finales.size(); i++) { //verificamos que los estados finales pertenecen al conjunto de estados
            finalesOk = finalesOk && (states.contains(finales.get(i)));
        }

        for (Triple<State, Character, State> t : delta) { //verificamos que las transiciones son validas 
            System.out.println(t.first().name() + " " + t.second() + " " + t.third().name());
            if(!t.second().equals(Lambda)){//si no es una transicion lambda verificamos que todos sus elementos sean correctos
            transicionesOk = transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()) && alfabeto.contains(t.second())); // (los estados utilizados pertenecen al conjunto de estados y el simbolo utilizado pertenece al alfabeto)
            }else{
                transicionesOk = transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()));//si es una transicion lambda solo verificamos los estados
            }
        }
        System.out.println("transOk: " + transicionesOk + " finalesOk: " + finalesOk + " inicOk: " + inicOk);

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

        System.out.println("");
        if (!string.isEmpty()) {
            System.out.println("caracter: " + string.charAt(0));
        } else {
            System.out.println("caracter: vacio");
        }

        System.out.println("est:");
        System.out.print("{");
        for (State s : est) {
            System.out.print(s.name() + " ");
        }
        System.out.println("}");

        LinkedHashSet<State> result = new LinkedHashSet();
        LinkedHashSet<State> resultadoParcial = new LinkedHashSet();//lista de estados ya obtenidos
        boolean puedeAvanzarPorCaracter = false;//booleano que indica que se puede avanzar consumiendo la cadena
        if (!est.isEmpty() && !string.isEmpty()) {
            for (State s : est) {//para todos los estados del conjunto de estados pasado como parametro
                puedeAvanzarPorCaracter = puedeAvanzarPorCaracter || (tieneTransiciones(s, string.charAt(0)));
                LinkedHashSet<State> conUnChar = (LinkedHashSet<State>) delta(s, string.charAt(0));//obtenemos la delta de un estado del conjunto con el 1er elemento de la cadena
                resultadoParcial = (LinkedHashSet<State>) unirConjuntosEstados(resultadoParcial, conUnChar);//unimos los resultados
            }

            System.out.println("resParcial:");
            System.out.print("{");
            for (State s : resultadoParcial) {
                System.out.print(s.name() + " ");
            }
            System.out.println("}");

            LinkedHashSet<State> resultadoClausura = new LinkedHashSet();//resultado de aplicar la clausura lambda al resultado parcial obtenido
            for (State s : resultadoParcial) {//clausuramos el resultado obtenido
                LinkedList<Triple<State, Character, State>> list = new LinkedList(); //lista para calculo clausura lambda
                resultadoClausura = (LinkedHashSet<State>) unirConjuntosEstados(resultadoClausura, clausuraLambda(s, list));
            }

            System.out.println("clausura:");
            System.out.print("{");
            for (State s : resultadoClausura) {
                System.out.print(s.name() + " ");
            }
            System.out.println("}");

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
                System.out.println("cadena vacia ");
                return est;
            } else {
                System.out.println("conjunto de estados vacio");
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
}
