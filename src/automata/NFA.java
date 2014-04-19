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
            
            String resultadoNombres = "[";
            for (State t : result) {
                resultadoNombres = resultadoNombres + t.name().toString() + ";";
            }
            resultadoNombres = resultadoNombres + "]";
            System.out.println("resultado por "+c+" desde "+from.name()+": " + resultadoNombres);
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
        String result="digraph {\n";
        result=result+"    "+"inic[shape=point];\n";
        result=result+"    "+"inic->"+inicial.name()+";\n";
        String transiciones="\n"; //cadena con las transiciones
        for(Triple<State,Character,State> t:delta){
            transiciones=transiciones+"    "+t.first().name()+"->"+t.third().name()+"[label="+'"'+t.second()+'"'+"];\n";
        }
        String finales="\n";
        for(State s:estados_finales){
            finales=finales+"    "+s.name()+"[shape=doublecircle];\n";
        }
        result=result+transiciones+"\n"+finales+"}";
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
            LinkedHashSet<State> acum = new LinkedHashSet();  //lista de estados
            acum.add(inicial); //agregamos el inicial
            Set<State> deltaAcum = deltaAcumulada(acum, string); //aplicamos la delta acumulada*/
            boolean accepted = false;
            for (State s : deltaAcum) { //al final, el ultimo conjunto de estados contiene al menos un estado final
                for (State s2 : estados_finales) {
                    System.out.println("estado obtenido?: " + s.name() + " comparado a: " + s2.name());
                    accepted = accepted || (s.name().equals(s2.name())); //si es asi, la cadena es aceptada
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
        
        State q0=new State(inicial.name()); //crea un nuevo estado inicial para el AFD
        
        
        return null;
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
        System.out.println("transOk: " + transicionesOk + " finalesOk: " + finalesOk + " inicOk: " + inicOk + " noLamda en alfabeto: " + noLambda);

        return (inicOk && finalesOk && transicionesOk && noLambda);

    }

    
    public Set<State> deltaAcumulada(Set<State> est, String string) {
        //System.out.println("estados: " + est.toString() + " string: " + string);
        LinkedList<State> listaEstados = new LinkedList(est); //conjunto de estados para la 1er aplicacion
        LinkedHashSet<State> result = new LinkedHashSet();
        LinkedHashSet<State> resultadoParcial = new LinkedHashSet();//lista de estados ya obtenidos
        LinkedList<String> nombresResParcial = new LinkedList();//lista de nombres de los estados que ya se tienen
        if (!est.isEmpty() && !string.isEmpty()) {
            /*for (State s : est) { //buscamos todos los estados pasados como parametros
                listaEstados.add(s);
            }*/
            for (int i = 0; i < listaEstados.size(); i++) { //calculamos la delta de todo el conjunto de estados parametro con el 1er elemento de la cadena
                LinkedHashSet<State> conUnChar = (LinkedHashSet<State>) delta(listaEstados.get(i), string.charAt(0));//obtenemos la delta de todo el conjunto de estados con el 1er elemento de la cadena
                LinkedList<String> nombresEstadosObtenidos = new LinkedList();
                for (State s : conUnChar) { //leemos todos los nombres de los estados obtenidos con delta
                    nombresEstadosObtenidos.add(s.name());
                }
                nombresResParcial.clear();
                for (State s : resultadoParcial) {//actualizamos los nombres de los estados que ya se tienen
                    nombresResParcial.add(s.name());
                }
                for (int j = 0; j < nombresEstadosObtenidos.size(); j++) { //para todo nombre en la lista de nombres de elementos obtenidos por delta
                    if (!nombresResParcial.contains(nombresEstadosObtenidos.get(j))) { //si no esta incluido en la lista de nombres de estados ya obtenidos
                        nombresResParcial.add(nombresEstadosObtenidos.get(j)); //se lo agrega
                    }
                }
            }
            resultadoParcial.clear();//vaciamos la lista de resultado parcial(por si acaso no estaba vacia)
            if (!nombresResParcial.isEmpty()) {// si la lista de proximos elementos no es vacia
                for (String s : nombresResParcial) { //agregamos los estados correspondientes
                    State nuevo = new State(s);
                    resultadoParcial.add(nuevo);
                }
            } else { //sino devolvemos lista vacia
                return new LinkedHashSet();
            }
            String substring = string.substring(1);
            result = (LinkedHashSet<State>) deltaAcumulada(resultadoParcial, substring);
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
}
