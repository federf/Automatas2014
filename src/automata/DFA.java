package automata;

import static automata.FA.Lambda;
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
        estados=states;
        alfabeto=alphabet;
        delta=transitions;
        inicial=initial;
        estados_finales=final_states;
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
        // TODO
        return null;
    }

    @Override
    public String to_dot() {
        assert rep_ok();
        // TODO
        return null;
    }

    /*
     *  Automata methods
     */
    @Override
    public boolean accepts(String string) {
        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        // TODO
        return false;
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
        return false;
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
        return null;
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
        boolean inicOk=false;
        boolean finalesOk=true;
        boolean transicionesOk=true;
        boolean noLambda=!alfabeto.contains(Lambda); //verificamos que el alfabeto no contiene Lambda
        boolean deterministicOk=true;
        
        LinkedList<String> states=new LinkedList();
        for(State s:estados){ //buscamos los nombres de los estados
            states.add(s.name());
        }
        System.out.println("estados: "+states);
        LinkedList<String>finales=new LinkedList();
        for(State f:estados_finales){  //buscamos los nombres de los estados finales
            finales.add(f.name());
        }
        System.out.println("estados finales: "+finales);
        
        
        for(int i=0; i<states.size();i++){ //verificamos que el estado inicial pertenece al conjunto de estados
            inicOk=inicOk || (states.get(i).equals(inicial.name()));
        }
        
        for(int i=0; i<finales.size();i++){ //verificamos que los estados finales pertenecen al conjunto de estados
            finalesOk=finalesOk && (states.contains(finales.get(i)));
        }
        
        
        LinkedList<Triple<State, Character, State>> transiciones = new LinkedList<Triple<State, Character, State>>(); //lista de transiciones para verificar si el AF es Det o NoDet
        for(Triple<State, Character, State> t : delta){ //verificamos que las transiciones son validas 
            transicionesOk=transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()) && alfabeto.contains(t.second())); // (los estados utilizados pertenecen al conjunto de estados y el simbolo utilizado pertenece al alfabeto)
            transiciones.add(t);
        }
        for (int i = 0; i < transiciones.size(); i++) { //ciclo para determinar si el automata es deterministico o no
                for (int j = 0; j < transiciones.size(); j++) {
                    if (i != j) {
                        Triple<State, Character, State> elem1 = transiciones.get(i);
                        Triple<State, Character, State> elem2 = transiciones.get(j);
                        if ((elem1.first().name().equals(elem2.first().name())) && (elem1.second().equals(elem2.second()))) {
                            deterministicOk=false;
                            i=transiciones.size();
                            j=transiciones.size();
                        }
                    }
                }
            }
        
        System.out.println("transOk: "+transicionesOk+" finalesOk: "+finalesOk+" inicOk: "+inicOk +" noLamda en alfabeto: "+noLambda+" deterministicOk: "+deterministicOk);
        
		return (inicOk && finalesOk && transicionesOk && noLambda && deterministicOk);
        
    }

}
