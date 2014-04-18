package automata;

import static automata.FA.Lambda;
import java.util.Iterator;
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

    @Override
    public Set<State> delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        LinkedList<Triple<State, Character, State>> trans = new LinkedList();
        Set<State> h = clausuraLambda(from, trans);
        /*System.out.println("Estados clausura E:");
         for (State s : h) {
         System.out.println(" " + s.name());
         }*/
        if (from != null && !c.equals("")) {
            // TODO
            //System.out.println("estado from: " + from.name() + " caracter: " + c);
            LinkedList<Triple<State, Character, State>> transiciones = new LinkedList();
            for (Triple<State, Character, State> t : delta) {
                transiciones.add(t);
            }
            Set<State> result = new LinkedHashSet<>(); //set de resultado
            result.add(from);
            for (int i = 0; i < transiciones.size(); i++) { //buscamos las transiciones desde from por c y agregamos el destino de estas al conjunto resultado
                Triple<State, Character, State> actual = transiciones.get(i);
                if (actual.first().name().equals(from.name()) && actual.second().equals(c)) {
                    result.add(actual.third());
                }
            }
            /*String resultadoNombres = "[";
             for (State t : result) {
             resultadoNombres = resultadoNombres + t.name() + ";";
             }
             resultadoNombres = resultadoNombres + "]";
             System.out.println("resultado: " + resultadoNombres);*/
            result.addAll(h);
            return result;
        } else {
            System.out.println("Caracter o Estado Invalido");
            return new LinkedHashSet<State>();
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
    @Override
    public boolean accepts(String string
    ) {

        System.out.println();
        System.out.println("acepta " + string + "?");

        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        //calcular la delta acumulada de string y luego comparar el conjunto retornado con el conj de estados finales
        //Set<State> deltaAcum = deltaAcumulada(string);
        boolean accepted = false;
        /*for (State s : deltaAcum) { //al final, el ultimo conjunto de estados contiene al menos un estado final
            for (State s2 : estados_finales) {
                System.out.println("estado obtenido?: " + s.name() + " comparado a: " + s2.name());
                accepted = accepted || (s.name().equals(s2.name())); //si es asi, la cadena es aceptada
            }
        }*/

        return accepted;
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
                result.addAll(resultRecursivo);
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
     */
    public Set<State> deltaAcumulada(String string) {
        LinkedHashSet<State> initial = new LinkedHashSet(); //set desde el cual se calculara la delta
        initial.add(inicial); //al inicio comienza con el estado inicial solo
        LinkedHashSet<State> resultGen; //resultado generado por delta
        resultGen = (LinkedHashSet<State>) delta(inicial, string.charAt(0));//conjunto inicial
        System.out.println("estado evaluado: " + inicial.name() + " caracter: " + string.charAt(0));
        System.out.println("estados generados: ");
        for (State s : resultGen) {
            System.out.println(s.name());
        }
        if (string.length() > 1) {
            int i = 1;
            while (i < string.length() && !resultGen.isEmpty()) {
                System.out.println("");
                System.out.println("estados disponibles");
                for (State s : resultGen) {
                    System.out.println(s.name());
                }
                LinkedList<String> nombresResultGenNuevo=new LinkedList();// lista total de nombres generados para el conjunto
                LinkedHashSet<State> resultGenNuevo=new LinkedHashSet();//resultado nuevo de aplicar delta con el siguiente caracter a todos los estados anteriores
                for (State s : resultGen) { //para cada estado en el conjunto de estados desde donde se calcula la delta
                    Character c = string.charAt(i); //con el i-esimo caracter de string
                    System.out.println("estado evaluado: " + s.name());
                    System.out.println("caracter evaluado: " + c);
                    LinkedList<String> nombresEstados = new LinkedList();
                    Set<State> unResultActual = (LinkedHashSet<State>) delta(s, c); //delta calculada con el caracter actual y uno de los estados del conjunto
                    for (State s2 : unResultActual) { //obtenemos los nombres de los estados generados
                        nombresEstados.add(s2.name());
                    }
                    LinkedList<String> resultActualNombres = new LinkedList(); //lista de nombres de estados obtenidos
                    for (String t : nombresEstados) { //vemos que estados falta agregar al conjunto resultado (verificamos por nombre solo)
                        if (!resultActualNombres.contains(t)) {
                            resultActualNombres.add(t); //y los agregamos a la lista de nombres
                        }
                    }
                    
                    
                    for (String t : nombresEstados) { //vemos que estados falta agregar al conjunto resultado (verificamos por nombre solo)
                        if (!resultActualNombres.contains(t)) {
                            resultActualNombres.add(t); //y los agregamos a la lista de nombres
                        }
                    }
                }
                for(String str:nombresResultGenNuevo){ //generamos la nueva lista de estados
                    State nuevo=new State(str);
                    resultGenNuevo.add(nuevo);
                }
                //resultGen=resultGenNuevo; //reescribimos el conjunto de proximos estados a evaluar
                System.out.println("Estados obtenidos por delta: ");
                for (State s : resultGen) {
                    System.out.println(s.name());
                }
                i++;
            }
        }
        return resultGen;
    }
}
