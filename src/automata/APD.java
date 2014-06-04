/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import utils.Triple;

/**
 * clase que implementa Automatas a Pila
 * Deterministicos con aceptacion por
 * estado final
 *
 * @author fede
 */
public class APD extends AutomataPila {

    State initial;//estado inicial
    LinkedHashSet<State> states;//conjunto de estados
    LinkedHashSet<State> final_states;//conjunto de estados finales
    LinkedList<Character> stack; //pila, es una lista de caracter con tratamiento LIFO
    LinkedHashSet<Triple<State, String, State>> delta;//conjunto de transiciones 
    LinkedHashSet<Character> alphabet;//alfabeto

    public APD(State initial, Set<State> states, Set<State> final_states, Set<Triple<State, String, State>> delta, Set<Character> alphabet) {
        this.initial = initial;
        this.states = (LinkedHashSet<State>) states;
        this.final_states = (LinkedHashSet<State>) final_states;
        this.stack = new LinkedList();
        stack.add(initialSymbol);
        this.delta = (LinkedHashSet<Triple<State, String, State>>) delta;
        this.alphabet = (LinkedHashSet<Character>) alphabet;
    }

    @Override
    public Set<State> states() {
        return this.states;
    }

    @Override
    public Set<Character> alphabet() {
        return this.alphabet;
    }

    @Override
    public State initial_state() {
        return this.initial;
    }

    @Override
    public Set<State> final_states() {
        return this.final_states;
    }

    public LinkedList<Character> stack() {
        return this.stack;
    }

    /*
     metodo que dado un estado from y un caracter c del alfabeto
     devuelve el estado al cual se pasa desde from por una transicion etiquetada con c
     y realiza los cambios al stack necesarios
     en caso de que no haya transicion desde from etiquetada con c, se retorna un estado con nombre
     vacio
     */
    @Override
    public State delta(State from, Character c) {
        System.out.println();
        //para toda transicion
        System.out.println("from: " + from.name() + " por: " + c + " stack antes: " + stack.toString() + " tope: " + stack.getLast());
        for (Triple<State, String, State> t : this.transitions()) {
            //si sale de from
            if (t.first().name().equals(from.name())) {
                String[] partesSecond = t.second().split("/");
                char[] topePilaAntes = partesSecond[1].toCharArray();
                char[] topePilaDespues = partesSecond[2].toCharArray();
                //y el primer elemento de su tripla elem/tope_pila_antes/tope_pila_despues
                if (partesSecond[0].equals(c.toString())) {
                    //comparamos el tope de la pila con el estado de la pila antes en la transicion
                    if (this.stack.getLast().equals(topePilaAntes[0])) {
                        //si el tope de la pila luego, es lambda, desapilamos el tope de la pila
                        System.out.println("tiene que hacer: " + topePilaDespues[0]);
                        if (topePilaDespues[0] == Lambda) {
                            this.stack.removeLast();
                        } else {//sino, apilamos el primer elemento de la transicion
                            this.stack.removeLast();
                            for(int i=0; i<topePilaDespues.length; i++){
                                Character unCarac=topePilaDespues[i];
                                this.stack.addLast(unCarac);
                            }
                        }
                        System.out.println("trans: " + t.second());
                        System.out.println("retorna: " + t.third().name());
                        System.out.println("from: " + from.name() + " por: " + c + " stack despues: " + stack.toString() + " tope: " + stack.getLast());

                        //y devolvemos el estado al cual la transicion se dirige
                        return t.third();
                    }
                }
            }
        }
        //si sale del ciclo es que no encontro transicion desde from por c, devolvemos un estado con nombre vacio 
        //para indicar esto
        return new State("");
    }

    /*
     metodo que dada una cadena y el estado inicial calcula la delta acumulada desde el inicial consumiendo 
     toda la cadena y retorna el estado donde termina
     */
    public State deltaAcumulada(State est, String string) {
        //si la cadena es valida y el estado tambien
        if (est != null && !string.isEmpty()) {
            State result = est;
            State parcial;
            //vamos evaluando la delta obtenida caracter por caracter de la string 
            for (int i = 0; i < string.length(); i++) {
                parcial = delta(result, string.charAt(i));
                //si el resultado obtenido es vacio, dejamos de evaluar y retornamos el estado con nombre vacio
                if (parcial.name().equals("")) {
                    i = string.length();
                }
                result = parcial;
            }
            //reseteamos la pila
            stack.clear();
            stack.add(initialSymbol);
            //y retornamos el resultado
            return result;
        } else {
            return est;
        }

    }

    public Set<Triple<State, String, State>> transitions() {
        return this.delta;
    }

    @Override

    public String to_dot() {
        assert rep_ok();
        String result = "digraph {\n";
        result = result + "    " + "inic[shape=point];\n";
        result = result + "    " + "inic->" + initial.name() + ";\n";
        String transiciones = "\n"; //cadena con las transiciones
        for (Triple<State, String, State> t : delta) {
            transiciones = transiciones + "    " + t.first().name() + "->" + t.third().name() + "[label=" + '"' + t.second() + '"' + "];\n";
        }
        String finales = "\n";
        for (State s : final_states) {
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
            LinkedList<State> estadosFinales = new LinkedList(final_states);
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
        boolean noLambda = !alphabet.contains(Lambda);
        boolean deterministicOk = true;

        LinkedList<String> nombresEstados = new LinkedList();
        //buscamos los nombres de los estados
        for (State s : states) {
            nombresEstados.add(s.name());
        }
        LinkedList<String> finales = new LinkedList();
        //buscamos los nombres de los estados finales
        for (State f : final_states) {
            finales.add(f.name());
        }
        //verificamos que el estado inicial pertenece al conjunto de estados
        for (int i = 0; i < states.size(); i++) {
            inicOk = inicOk || (nombresEstados.get(i).equals(initial.name()));
        }
        //verificamos que los estados finales pertenecen al conjunto de estados
        for (int i = 0; i < finales.size(); i++) {
            finalesOk = finalesOk && (nombresEstados.contains(finales.get(i)));
        }

        //lista de transiciones para verificar si el AF es Det o NoDet
        LinkedList<Triple<State, String, State>> transiciones = new LinkedList(delta);
        //verificamos que las transiciones son validas 
        for (Triple<State, String, State> t : delta) {
            // (los estados utilizados pertenecen al conjunto de estados y el simbolo utilizado pertenece al alfabeto)
            transicionesOk = transicionesOk && (nombresEstados.contains(t.first().name()) && nombresEstados.contains(t.third().name()) && alphabet.contains(t.second().charAt(0)));
        }
        //ciclo para determinar si el automata es deterministico o no
        for (int i = 0; i < transiciones.size(); i++) {
            for (int j = 0; j < transiciones.size(); j++) {
                if (i != j) {
                    Triple<State, String, State> elem1 = transiciones.get(i);
                    Triple<State, String, State> elem2 = transiciones.get(j);
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

}
