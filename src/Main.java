
import automata.DFA;
import automata.FA;
import automata.State;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("prueba funcionalidad test1-IntegrationTests.java");
            DFA my_dfa = (DFA) FA.parse_form_file("test/test/dfa1.dot");
            System.out.println("acepta ab?:" + my_dfa.toNFA().accepts("ab"));
            System.out.println("acepta abbbbb?:" + my_dfa.toNFA().accepts("abbbbb"));
            System.out.println("acepta bbbbb?:" + my_dfa.toNFA().accepts("bbbbb"));
            System.out.println("acepta a?:" + my_dfa.toNFA().accepts("a"));
            
            DFA my_dfa2 = (DFA) FA.parse_form_file("test/test/dfa3.dot");
            System.out.println("vacio dfa3?: "+my_dfa2.is_empty());

            /*Set<State> alcanzablesInicial=my_dfa.alcanzablesDesde(my_dfa.initial_state());
             System.out.println("alcanzables desde el estado inicial");
             System.out.println("{");
             for(State s:alcanzablesInicial){
             System.out.print(s.name()+" ");
             }
             System.out.println("}");*/
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
