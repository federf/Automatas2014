
import automata.FA;

public class Main {

    public static void main(String[] args) {
        try {
            String curDir = System.getProperty("user.dir");
            FA automata = FA.parse_form_file(curDir + "/src/test/nfa2.dot");
            automata.delta(automata.initial_state(),'a');
            String toDot=automata.to_dot();
            System.out.println(toDot);

        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
