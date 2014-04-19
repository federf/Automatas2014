
import automata.DFA;
import automata.FA;

public class Main {

    public static void main(String[] args) {
        try {
            String curDir = System.getProperty("user.dir");
            DFA automata = (DFA) FA.parse_form_file(curDir + "/src/test/dfa1.dot");
            /*System.out.println("acepta la cadena? abb: " + automata.accepts("abb"));
            System.out.println("acepta la cadena? abbb: " + automata.accepts("abbb"));
            System.out.println("acepta la cadena? abbba: " + automata.accepts("abbba"));
            System.out.println("acepta la cadena? abc: " + automata.accepts("abc"));
            System.out.println("acepta la cadena? aba: " + automata.accepts("aba"));
            System.out.println("acepta la cadena? aa: " + automata.accepts("aa"));*/
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
