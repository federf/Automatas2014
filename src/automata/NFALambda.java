package automata;

import static automata.FA.Lambda;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

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
        Set<State> clausura = clausuraLambda(from, trans);
        if (from != null && !c.equals("")) {
            LinkedList<Triple<State, Character, State>> transiciones = new LinkedList(delta);
            Set<State> result = new LinkedHashSet<>(); //set de resultado
            result.add(from);
            for (int i = 0; i < transiciones.size(); i++) { //buscamos las transiciones desde from por c y agregamos el destino de estas al conjunto resultado
                Triple<State, Character, State> actual = transiciones.get(i);
                if (actual.first().name().equals(from.name()) && actual.second().equals(c)) {
                    result.add(actual.third());
                }
            }
            result.addAll(clausura);
            return result;
        } else {
            if (from != null && c.equals("")) {
                System.out.println("Caracter Invalido (es vacio)");
                return clausuraLambda(from, new LinkedList());
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
    @Override
    public boolean accepts(String string
    ) {
        System.out.println();
        System.out.println("acepta " + string + "?");

        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        if (rep_ok() && (string != null) && verify_string(string)) {
            //calcular la delta acumulada de string y luego comparar el conjunto retornado con el conj de estados finales
            LinkedHashSet<State> acum = new LinkedHashSet();  //lista de estados
            acum.add(inicial); //agregamos el inicial
            Set<State> deltaAcum = deltaAcumulada(acum, string); //aplicamos la delta acumulada
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
     ademas requiere de un conjunto de estados sobre los cuales aplicara la delta y calculara la clausuraE.
     Al inicio se le pasaria un conj que contiene el estado inicial
     */
    public Set<State> deltaAcumulada(Set<State> est, String string) { //CONSULTAR TRATAMIENTO POR LAMBDA!!!!
        System.out.println("estados: " + est.toString() + " string: " + string);
        LinkedList<State> listaEstados = new LinkedList(est); //conjunto de estados parametro (sobre los cuales aplicar delta con el 1er elem de la string parametro)
        LinkedHashSet<State> result = new LinkedHashSet(); //conjunto resultado
        if (!est.isEmpty() && !string.isEmpty()) { //si los parametros no son vacios
            LinkedList<String> nombresEstados = new LinkedList(); //lista de estados que ya fueron obtenidos o fueron pasados como parametros
            for (State s : listaEstados) {
                nombresEstados.add(s.name());
            }
            for (State s : listaEstados) {
                Character primerElem = string.charAt(0);
                LinkedHashSet<State> deltaUnElem = (LinkedHashSet<State>) delta(s, primerElem);//calculamos la delta con el 1er elem de la string
                LinkedList<String> nombresDeltaUnElem = new LinkedList(); //lista de nombres de los estados obtenidos
                for (State delta : deltaUnElem) { //leemos los nombres de los estados obtenidos
                    nombresDeltaUnElem.add(delta.name());
                }

                for (String str : nombresDeltaUnElem) { //para todo nombre calculado con delta
                    if (!nombresEstados.contains(str)) { //si el nombre no esta en la lista de los pasados como parametro o ya calculado
                        nombresEstados.add(str); //se lo agrega para luego agregarlo a la lista de resultado como un objeto State
                    }
                }
            }
            String substring = string.substring(1);
            result.clear();
            for (String estado : nombresEstados) { //creamos el conjunto de estados resultado
                result.add(new State(estado));
            }
            boolean llamarConLambda = false; //variable para verificar si es posible ejecutar una transicion lambda
            for (State s : est) { //verificamos si en el conjunto de estados pasado como parametro, alguno tiene transicion lambda
                llamarConLambda = llamarConLambda || tieneTransiciones(s, Lambda);
            }
            LinkedHashSet<State> llamadaConLambda = new LinkedHashSet();
            if (llamarConLambda) {
                llamadaConLambda = (LinkedHashSet<State>) deltaAcumulada(result, Lambda + substring);
            }
            LinkedList<String> nombresLlamadaLambda = new LinkedList(); //lista de nombres de los estados resultado de llamar con lambda+substring
            if (!llamadaConLambda.isEmpty()) { //si el resultado de la llamada con lambda no dio vacio
                for (State s : llamadaConLambda) { //buscamos los nombres de todos los estados del conjunto
                    nombresLlamadaLambda.add(s.name());
                }
            }
            LinkedHashSet<State> resultadoFinal = new LinkedHashSet(); //conjunto resultado final
            if (!result.isEmpty()) { //si el conjunto resultado no es vacio
                resultadoFinal = (LinkedHashSet<State>) deltaAcumulada(result, substring); //realizamos la llamada con la subcadena de string sin su primer elem
            }
            LinkedList<String> nombresResultadoFinal = new LinkedList(); //lista de nombres de los estados resultantes de la llamada anterior
            if (!resultadoFinal.isEmpty()) { //si el resultado de la llamada anterior no dio vacio
                for (State s : resultadoFinal) { //buscamos los nombres de todos los estados del conjunto
                    nombresResultadoFinal.add(s.name());
                }
            }
            for (String s : nombresLlamadaLambda) { //para todo elemento obtenido de llamar con lambda+substring
                if (!nombresResultadoFinal.contains(s)) { //si no esta contenido en el conjunto resultado de llamar con substring
                    resultadoFinal.add(new State(s)); // agregamos el estado al conjunto resultado final
                }
            }
            return resultadoFinal;

        } else {
            if (string.isEmpty()) { // si la cadena pasada es vacia (o sea, no se pueden consumir mas elementos para realizar transiciones)
                //result=new LinkedHashSet(est);
                LinkedList<String> nombresResultados = new LinkedList();//lista de nombres de los estados obtenidos entre todas las clausuras lambda aplicadas a cada elemento
                LinkedHashSet<State> clausuraLambdaUnElem; //conjunto resultado de aplicar la clausura lambda a un elemento del conj pasado como parametro
                for (State s : est) { //para cada elemento del conjunto, calculamos su clausura lambda
                    clausuraLambdaUnElem = (LinkedHashSet<State>) clausuraLambda(s, new LinkedList<Triple<State, Character, State>>());
                    LinkedList<String> nombreObtenidos = new LinkedList();
                    for (State s2 : clausuraLambdaUnElem) { //obtenemos los nombres de los estados obtenidos con la clausura lambda anterior
                        nombreObtenidos.add(s2.name());
                    }
                    nombresResultados.add(s.name()); //agregamos todo el conjunto a la lista resultado ya que cada elemento esta incluido en su clausura
                    for (String str : nombreObtenidos) {
                        if (!nombresResultados.contains(str)) {
                            nombresResultados.add(str); //agregamos los nombres de los estados que antes no estaban en el conjunto solucion
                        }
                    }
                }
                result.clear();
                for (String str : nombresResultados) { //creamos el conjunto de estados resultado
                    State s = new State(str);
                    result.add(s);
                }
                return result; //retornamos el conj resultado de aplicar la clausura lambda al conj pasado como parametro
            } else {
                if (est.isEmpty()) {
                    return est; //si el conjunto pasado como parametro es vacio retornamos el mismo conjunto (vacio)
                }
            }
        }
        return null;
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
            result = result || (actual.first().name().equals(s.name()) && (actual.second().equals(Lambda)));
        }
        return result;
    }
}
