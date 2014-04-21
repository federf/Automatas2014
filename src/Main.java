
import automata.NFA;
import automata.FA;
import automata.State;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            String curDir = System.getProperty("user.dir");
            NFA automata = (NFA) FA.parse_form_file(curDir + "/src/test/nfa1.dot");
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
