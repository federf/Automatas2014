
import automata.DFA;
import automata.FA;
import automata.NFA;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("prueba funcionalidad test1-IntegrationTests.java");
            DFA my_dfa = (DFA) FA.parse_form_file("test/test/dfa1.dot");
            System.out.println("acepta ab?:"+my_dfa.toNFA().accepts("ab"));
            System.out.println("acepta abbbbb?:"+my_dfa.toNFA().accepts("abbbbb"));
            System.out.println("acepta bbbbb?:"+my_dfa.toNFA().accepts("bbbbb"));
            System.out.println("acepta a?:"+my_dfa.toNFA().accepts("a"));
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
