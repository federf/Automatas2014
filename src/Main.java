
import automata.NFA;
import automata.FA;
import automata.State;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            String curDir = System.getProperty("user.dir");
            NFA automata = (NFA) FA.parse_form_file(curDir + "/src/test/nfa1.dot");
            Set<Set<State>> potencia = FA.powerSet(automata.states());
            System.out.println("cantidad subconjuntos: "+potencia.size()+" (2^"+automata.states().size()+")");
            for (Set<State> s : potencia) {
                System.out.println();
                System.out.println("un conjunto:");
                System.out.print("{");
                for (State estado : s) {
                    System.out.print(estado.name() + ",");
                }
                System.out.print("}");
                System.out.println();
            }
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
