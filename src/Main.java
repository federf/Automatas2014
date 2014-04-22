
import automata.DFA;
import automata.FA;

public class Main {

    public static void main(String[] args) {
        try {
            String curDir = System.getProperty("user.dir");
            DFA automata = (DFA) FA.parse_form_file(curDir + "/test/test/dfa1.dot");
            automata.is_finite();
            /*DFA dfa=automata.toDFA();
            String string=dfa.to_dot();
            System.out.println(string);*/
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
