package automata;

import static automata.FA.Lambda;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import utils.Triple;

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
            if(tieneTransicion(from, c)){
                LinkedList<Triple<State, Character, State>> transiciones = new LinkedList(delta);
                /*for (Triple<State, Character, State> t : delta) {
                    transiciones.add(t);
                }*/
                State result = new State("");
                for (int i = 0; i < transiciones.size(); i++) {
                    Triple<State, Character, State> actual = transiciones.get(i);
                    if (actual.first().name().equals(from.name()) && actual.second().equals(c)) {
                        result = (actual.third());
                    }
                }
                System.out.println("resultado desde " + from.name() + " con " + c + ": " + result.name());
                return result;
            }else{
                System.out.println("no hay transicion desde "+from.name()+" con "+c);
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
        if (rep_ok() && (string != null) && verify_string(string)) {
            LinkedList<State> estadosFinales = new LinkedList(estados_finales);
            State resultadoDeltaAcum = deltaAcumulada(inicial, string);
            boolean result = false;
            if (!resultadoDeltaAcum.name().equals("")) {
                for (int i = 0; i < estadosFinales.size(); i++) {
                    State unFinal = estadosFinales.get(i);
                    result = result || (resultadoDeltaAcum.name().equals(unFinal.name()));
                }
                return result;
            } else {
                return false;
            }
        } else {
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
        return null;
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
        return null;
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
        return false;
    }

    /**
     * Checks the automaton for language
     * infinity.
     *
     * @returns True iff the automaton's
     * language is finite.
     */
    public boolean is_finite() {
        assert rep_ok();
        // TODO
        boolean finito=true;
        for(Triple<State,Character,State> t:delta){ //verificamos que no haya ciclos hacia un mismo estado
            finito=finito && (!t.first().name().equals(t.third().name())); //toda transicion tiene un inicio distinto a su llegada (no hay ciclos sobre un estado)
        }
        for(Triple<State,Character,State> t:delta){ //verificamos que no haya ciclos de la forma a->b b->a
            for(Triple<State,Character,State> t2:delta){
                if(!t.equals(t2)){ //si no son la misma transicion
                    finito=finito && (t.third().name().equals(t2.first().name())&&(!t2.third().name().equals(t.first().name())));
                    /*
                    se cumple que para todo par de transiciones tal que el final de la primera es el inicio de la segunda
                    el final de la segunda es distinto al inicio de la primera
                    */
                }
            }
        }
        // !!! faltaria verificar recursivamente si no existe un camino de transiciones validas que genere un ciclo
        return finito;
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
        LinkedHashSet<State> new_final_states=new LinkedHashSet();//nuevo conjunto de estados finales;
        LinkedList<String> estadosDFA=new LinkedList(); //obtenemos los nombres de los estados que posee el automata original
        for(State s: this.states()){ //obtenemos todos los nombres uno por uno
            estadosDFA.add(s.name());
        }
        LinkedList<String> nombresFinalesDFA=new LinkedList();//lista de nombres de los estados finales del DFA original
        for(State s:this.final_states()){ //obtenemos los nombres de los estados finales del dfa original
            nombresFinalesDFA.add(s.name());
        }
        LinkedList<String> new_nombresFinalesDFA=new LinkedList(); //lista de nombres de los nuevos estados finales
        for(String unNombre: estadosDFA){//para todo estado
            if(!nombresFinalesDFA.contains(unNombre)){ //tal que no era final en el DFA original
                new_nombresFinalesDFA.add(unNombre); //lo convertimos en estado final del nuevo DFA
            }
        }
        for(String nombre_final:new_nombresFinalesDFA){
            new_final_states.add(new State(nombre_final));
        }
        DFA complemento=new DFA(this.states(),this.alphabet(),this.delta,this.initial_state(),new_final_states);//retornamos un automata finito deterministico
        //cuyos estados finales son todos los estados que antes no eran finales en el automata original
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
    public DFA star() {
        assert rep_ok();
        // TODO
        return null;
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
        // TODO
        return null;
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
    public DFA intersection(DFA other) {
        assert rep_ok();
        assert other.rep_ok();
        // TODO
        return null;
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
        boolean noLambda = !alfabeto.contains(Lambda); //verificamos que el alfabeto no contiene Lambda
        boolean deterministicOk = true;

        LinkedList<String> states = new LinkedList();
        for (State s : estados) { //buscamos los nombres de los estados
            states.add(s.name());
        }
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

        LinkedList<Triple<State, Character, State>> transiciones = new LinkedList<Triple<State, Character, State>>(delta); //lista de transiciones para verificar si el AF es Det o NoDet
        for (Triple<State, Character, State> t : delta) { //verificamos que las transiciones son validas 
            transicionesOk = transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()) && alfabeto.contains(t.second())); // (los estados utilizados pertenecen al conjunto de estados y el simbolo utilizado pertenece al alfabeto)
        }
        for (int i = 0; i < transiciones.size(); i++) { //ciclo para determinar si el automata es deterministico o no
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
        System.out.println("");
        if (est != null && !string.isEmpty()) {
            State result=est;
            State parcial;
            for(int i=0; i<string.length(); i++){
                parcial=delta(result, string.charAt(i));
                System.out.println("delta desde "+result.name()+" con "+string.charAt(i)+": "+parcial.name());
                if(parcial.name().equals("")){
                    System.out.println("res parcial vacio con "+result.name()+" y "+string.charAt(i));
                    i=string.length();
                }
                result=parcial;
            }
            System.out.println("resultado delta acumulada desde "+est.name()+" con "+string+": "+result.name());
            return result;
        } else {
            return est;
        }

    }
    
    /*
    metodo que dado un estado y un caracter c devuelve true si existe una transicion
    saliente desde el estado con etiqueta c
    */
    public boolean tieneTransicion(State s, Character c){
        boolean result=false;
        for(Triple<State, Character, State> t: delta){
            result=result || (t.first().name().equals(s.name()) && t.second().equals(c));
        }
        return result;
    }
}
