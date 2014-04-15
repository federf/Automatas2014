package automata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.util.Set;

public abstract class FA {

    public static final Character Lambda = '_';

    /* Creation */
    /**
     * Parses and returns a finite
     * automaton form the given file.
     * The type of the automaton
     * returned is the appropriate one
     * for the automaton represented in
     * the file (i.e. if the file
     * contains the representation of an
     * automaton that is
     * non-deterministic but has no
     * lambda transitions, then an
     * instance of NFA must be
     * returned).
     *
     * @param path Path to the file
     * containing the specification of
     * an FA.
     * @return An instance of DFA, NFA
     * or NFALambda, corresponding to
     * the automaton represented in the
     * file.
     * @throws Exception Throws an
     * exception if there is an error
     * during the parsing process.
     */
    public static FA parse_form_file(String path) throws Exception {
        // TODO
        try {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(path);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            // Leer el archivo linea por linea
            while ((strLinea = buffer.readLine()) != null) {
                // Imprimimos la línea por pantalla
                //System.out.println (strLinea); //A IMPLEMENTAR
            }
            // Cerramos el archivo
            entrada.close();
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
        return null;
    }

    /*
     * 	State Querying
     */
    /**
     * @return the atomaton's set of
     * states.
     */
    public abstract Set<State> states();

    /**
     * @return the atomaton's alphabet.
     */
    public abstract Set<Character> alphabet();

    /**
     * @return the atomaton's initial
     * state.
     */
    public abstract State initial_state();

    /**
     * @return the atomaton's final
     * states.
     */
    public abstract Set<State> final_states();

    /**
     * Query for the automaton's
     * transition function.
     *
     * @return A state or a set of
     * states (depending on whether the
     * automaton is a DFA, NFA or
     * NFALambda) corresponding to the
     * successors of the given state via
     * the given character according to
     * the transition function.
     */
    public abstract Object delta(State from, Character c);

    /**
     * @return Returns the DOT code
     * representing the automaton.
     */
    public abstract String to_dot();

    /*
     * 	Automata Methods 
     */
    /**
     * Tests whether a string belongs to
     * the language of the current
     * finite automaton.
     *
     * @param string String to be tested
     * for acceptance.
     * @return Returns true iff the
     * current automaton accepts the
     * given string.
     */
    public abstract boolean accepts(String string);

    /**
     * Verifies whether the string is
     * composed of characters in the
     * alphabet of the automaton.
     *
     * @return True iff the string
     * consists only of characters in
     * the alphabet.
     */
    public boolean verify_string(String s) { //IMPLEMENTADO
        // TODO
        boolean verify = true;
        for (int i = 0; i < s.length(); i++) {
            verify = verify && (alphabet().contains(s.charAt(i)));
        }
        return verify;
    }

    /**
     * @return True iff the automaton is
     * in a consistent state.
     */
    public abstract boolean rep_ok();

}
