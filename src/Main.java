
import automata.NFA;
import automata.FA;

public class Main {

    public static void main(String[] args) {
        try {
            String curDir = System.getProperty("user.dir");
            NFA automata = (NFA) FA.parse_form_file(curDir + "/src/test/nfa1.dot");
            /*DFA dfa=automata.toDFA();
            String string=dfa.to_dot();
            System.out.println(string);*/
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
