
import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.State;
import java.util.LinkedHashSet;
import utils.Triple;

public class Main {

    public static void main(String[] args) {
        try {
            /*System.out.println("prueba funcionalidad test1-IntegrationTests.java"); //-OK
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
            System.out.println("1 -" + my_dfa.toNFALambda().accepts("ab"));
            System.out.println("2 -" + my_dfa.toNFALambda().accepts("abbbbb"));
            System.out.println("3 -" + my_dfa.toNFALambda().accepts("bbbbb"));
            System.out.println("4 -" + my_dfa.toNFALambda().accepts("a"));

            System.out.println("dfa1 finito?: " + my_dfa.is_finite());

            // DFAAutomataMethodsTests.test14 -OK
            DFA dfa = (DFA) FA.parse_form_file("test/dfa3.dot");
            System.out.println("dfa interseccion su complemento, es vacio?: " + dfa.intersection(dfa.complement()).is_empty());
          
            // DFAAutomataMethodsTests.test15 -OK
            DFA dfa2 = (DFA) FA.parse_form_file("test/dfa2.dot");
            DFA dfa3 = (DFA) FA.parse_form_file("test/dfa3.dot");
            DFA union = dfa2.union(dfa3);
            System.out.println("acepta a?: "+union.accepts("a")+" acepta bbbb?: "+union.accepts("bbbb"));
            */
            //IntegrationTests.test2
            NFA my_nfa=(NFA) FA.parse_form_file("test/nfa1.dot");
            System.out.println("NFA");
            System.out.println("acepta ab?: "+my_nfa.accepts("ab"));
            System.out.println("acepta abaaaaa?: "+my_nfa.accepts("abaaaaa"));
            System.out.println("acepta abbbb?: "+my_nfa.accepts("abbbb"));
            System.out.println("acepta a?: "+my_nfa.accepts("a"));
            System.out.println();
            System.out.println("DFA");
            DFA my_dfa=my_nfa.toDFA();
            
            System.out.println("estado inicial: "+my_dfa.initial_state().name());
            System.out.println();
            
            System.out.println("estados");
            for(State s:my_dfa.states()){
                System.out.println(s.name());
            }
            System.out.println();
            System.out.println("estados finales");
            for(State s:my_dfa.final_states()){
                System.out.println(s.name());
            }
            System.out.println();
            System.out.println("transiciones");
            for(Triple<State,Character,State> t:my_dfa.transiciones()){
                System.out.println(t.first().name()+" -> "+t.second()+" -> "+t.third().name());
            }
            System.out.println("acepta ab?: "+my_dfa.accepts("ab"));
            System.out.println("acepta abaaaaa?: "+my_dfa.accepts("abaaaaa"));
            System.out.println("acepta abbbb?: "+my_dfa.accepts("abbbb"));
            System.out.println("acepta a?: "+my_dfa.accepts("a"));
            

        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
