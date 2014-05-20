
import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.State;
import java.util.LinkedHashSet;
import utils.Triple;

public class Main {

    public static void main(String[] args) {
        try {
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
            
            // DFAAutomataMethodsTests.test14 -OK
            DFA dfa = (DFA) FA.parse_form_file("test/dfa3.dot");
            System.out.println("dfa interseccion su complemento, es vacio?: " + dfa.intersection(dfa.complement()).is_empty());
          
            // DFAAutomataMethodsTests.test15 -OK
            DFA dfa2 = (DFA) FA.parse_form_file("test/dfa2.dot");
            DFA dfa3 = (DFA) FA.parse_form_file("test/dfa3.dot");
            DFA union = dfa2.union(dfa3);
            System.out.println("acepta a?: "+union.accepts("a")+" acepta bbbb?: "+union.accepts("bbbb"));
            
            
            //IntegrationTests.test1 -OK
            DFA my_dfa = (DFA) FA.parse_form_file("test/dfa1.dot");
            System.out.println("1 -" + my_dfa.toNFALambda().accepts("ab"));
            System.out.println("2 -" + my_dfa.toNFALambda().accepts("abbbbb"));
            System.out.println("3 -" + my_dfa.toNFALambda().accepts("bbbbb"));
            System.out.println("4 -" + my_dfa.toNFALambda().accepts("a"));

            System.out.println("dfa1 finito?: " + my_dfa.is_finite());

            //IntegrationTests.test2 -OK
            NFA my_nfa=(NFA) FA.parse_form_file("test/nfa1.dot");
            DFA my_dfa2=my_nfa.toDFA();
            System.out.println("acepta ab?: "+my_dfa2.accepts("ab"));
            System.out.println("acepta abaaaaa?: "+my_dfa2.accepts("abaaaaa"));
            System.out.println("acepta abbbb?: "+my_dfa2.accepts("abbbb"));
            System.out.println("acepta a?: "+my_dfa2.accepts("a"));
            

        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
