package automata;

import static automata.FA.Lambda;
import java.util.HashSet;
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

    @Override
    public Set<State> delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        /*if (from != null && !c.equals("")) {
         // TODO
         System.out.println("estado from: " + from.name() + " caracter: " + c);
         LinkedList<Triple<State, Character, State>> transiciones = new LinkedList();
         for (Triple<State, Character, State> t : delta) {
         transiciones.add(t);
         }
         Set<State> result = new HashSet<State>(); //set de resultado
         result.add(from);
         for (int i = 0; i < transiciones.size(); i++) { //buscamos las transiciones desde from por c y agregamos el destino de estas al conjunto resultado
         Triple<State, Character, State> actual = transiciones.get(i);
         if (actual.first().name().equals(from.name()) && actual.second().equals(c)) {
         result.add(actual.third());
         }
         }

         LinkedList<Triple<State, Character, State>> transLambdaEvaluadas = new LinkedList();
         clausuraLambda(from, transLambdaEvaluadas, result);

         String resultadoNombres = "[";
         for (State t : result) {
         resultadoNombres = resultadoNombres + t.name() + ";";
         }
         resultadoNombres = resultadoNombres + "]";
         System.out.println("resultado: " + resultadoNombres);
         return result;
         } else {
         System.out.println("Caracter o Estado Invalido");
         return new HashSet<State>();
         }*/
        return null;

    }

    @Override
    public String to_dot() {
        assert rep_ok();
        assert rep_ok();
        // TODO
        String result = "digraph {\n";
        result = result + "    "+"inic[shape=point];\n";
        result = result + "    "+"inic->" + inicial.name() + ";\n";
        String transiciones = "\n"; //cadena con las transiciones normales
        String transLambda = "\n"; //cadena con las transiciones lambda
        for (Triple<State, Character, State> t : delta) {
            if (!t.second().equals(Lambda)) {
                transiciones = transiciones +"    "+ t.first().name() + "->" + t.third().name() + "[label=" + '"' + t.second() + '"' + "];\n";
            } else {
                transLambda = transLambda +"    "+ t.first().name() + "->" + t.third().name() + "[label=" + '"' + t.second() + '"' + "];\n";
            }
        }
        String finales = "\n";
        for (State s : estados_finales) {
            finales = finales +"    "+ s.name() + "[shape=doublecircle];\n";
        }
        result = result + transiciones +transLambda+ finales + "}";
        return result;
    }

    /*
     *  Automata methods
     */
    @Override
    public boolean accepts(String string
    ) {
        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        // TODO
        return false;
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
        return null;
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
        System.out.println("estados: " + states);
        LinkedList<String> finales = new LinkedList();
        for (State f : estados_finales) {  //buscamos los nombres de los estados finales
            finales.add(f.name());
        }
        System.out.println("estados finales: " + finales);

        for (int i = 0; i < states.size(); i++) { //verificamos que el estado inicial pertenece al conjunto de estados
            inicOk = inicOk || (states.get(i).equals(inicial.name()));
        }

        for (int i = 0; i < finales.size(); i++) { //verificamos que los estados finales pertenecen al conjunto de estados
            finalesOk = finalesOk && (states.contains(finales.get(i)));
        }

        for (Triple<State, Character, State> t : delta) { //verificamos que las transiciones son validas 
            transicionesOk = transicionesOk && (states.contains(t.first().name()) && states.contains(t.third().name()) && alfabeto.contains(t.second())); // (los estados utilizados pertenecen al conjunto de estados y el simbolo utilizado pertenece al alfabeto)
        }
        System.out.println("transOk: " + transicionesOk + " finalesOk: " + finalesOk + " inicOk: " + inicOk);

        return (inicOk && finalesOk && transicionesOk);
    }

    /*
     metodo que dado un estado realiza su clausura lambda
     sus parametros son el estado inicial de las transiciones
     y un conjunto de transiciones lambda ya evaluadas (al inicio es vacio, luego se utiliza para
     no evaluar mas de 1 vez cada transicion lambda)
     ademas se pasa el conjunto resultado que se actualiza en cada corrida (al inicio debe ser vacio)
     */
    private void clausuraLambda(State from, LinkedList<Triple<State, Character, State>> yaEvaluadas, Set<State> result) { //VERIFICAR Y CORREGIR
        assert states().contains(from);
        assert alphabet().contains(Lambda);
        if (from != null) {
            // TODO
            LinkedList<Triple<State, Character, State>> transiciones = new LinkedList(); //lista de transiciones lambda a evaluar
            for (Triple<State, Character, State> t : delta) {
                if (!yaEvaluadas.contains(t)) { //si la transicion que se quiere agregar aun no fue evaluada
                    transiciones.add(t); //la agrega a la lista
                }
            }
            result.add(from);
            LinkedList<String> nombresResult = new LinkedList();
            for (State s : result) {
                nombresResult.add(s.name());
            }
            for (int i = 0; i < transiciones.size(); i++) { //buscamos las transiciones lambda desde from y agregamos el destino de estas al conjunto resultado
                Triple<State, Character, State> actual = transiciones.get(i);
                if (actual.first().name().equals(from.name()) && actual.second().equals(Lambda) && !nombresResult.contains(actual.third().name())) {
                    result.add(actual.third());
                    yaEvaluadas.add(actual);
                }
            }
            for (State f : result) {
                clausuraLambda(f, yaEvaluadas, result);
            }

        } else {
            System.out.println("Caracter o Estado Invalido");
        }
    }

}
