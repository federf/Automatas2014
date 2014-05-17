
import automata.DFA;
import automata.FA;
import automata.NFA;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("prueba funcionalidad test1-IntegrationTests.java"); //-OK
            DFA my_dfa = (DFA) FA.parse_form_file("test/dfa1.dot");
            System.out.println("acepta ab?:" + my_dfa.toNFA().accepts("ab"));
            System.out.println("acepta abbbbb?:" + my_dfa.toNFA().accepts("abbbbb"));
            System.out.println("acepta bbbbb?:" + my_dfa.toNFA().accepts("bbbbb"));
            System.out.println("acepta a?:" + my_dfa.toNFA().accepts("a"));

            //NFAAutomataMethodsTests.test1 -OK
            NFA nfa = (NFA) FA.parse_form_file("test/nfa1.dot");
            System.out.println("acepta ab?: " + nfa.accepts("ab"));
            //NFAAutomataMethodsTests.test2 -OK
            System.out.println("acepta abaaaaaaaa?: " + nfa.accepts("abaaaaaaaa"));
            //NFAAutomataMethodsTests.test3 -OK
            System.out.println("acepta abaaaaaaab?: " + nfa.accepts("abaaaaaaab"));
            nfa = (NFA) FA.parse_form_file("test/nfa2.dot");
            //NFAAutomataMethodsTests.test5 -OK
            System.out.println(nfa.accepts("aaaaaaaaaaaaaaa"));

            //IntegrationTests.test1 -OK
            System.out.println("1 -"+my_dfa.toNFALambda().accepts("ab"));
            System.out.println("2 -"+my_dfa.toNFALambda().accepts("abbbbb"));
            System.out.println("3 -"+my_dfa.toNFALambda().accepts("bbbbb"));
            System.out.println("4 -"+my_dfa.toNFALambda().accepts("a"));

        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
