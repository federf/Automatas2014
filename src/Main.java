
import automata.NFALambda;
import automata.FA;
import automata.State;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            String curDir = System.getProperty("user.dir");
            NFALambda automata = (NFALambda) FA.parse_form_file(curDir + "/src/test/nfalambda1.dot");
            /*System.out.println("acepta la cadena? automatas: " + automata.accepts("automatas"));
            System.out.println("acepta la cadena? lenguajes: " + automata.accepts("lenguajes"));
            System.out.println("acepta la cadena? ylenguajes: " + automata.accepts("ylenguajes"));
            System.out.println("acepta la cadena? y: " + automata.accepts("y"));
            /* System.out.println("acepta la cadena? cas: " + automata.accepts("cas"));
             System.out.println("acepta la cadena? casa: " + automata.accepts("casa"));*/ //System.out.println("acepta la cadena? casas: " + automata.accepts("casas"));
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
