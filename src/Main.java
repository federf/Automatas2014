
import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.NFALambda;
import automata.State;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("prueba funcionalidad test1-IntegrationTests.java");
            DFA my_dfa = (DFA) FA.parse_form_file("test/dfa1.dot");
            System.out.println("acepta ab?:" + my_dfa.toNFA().accepts("ab"));
            System.out.println("acepta abbbbb?:" + my_dfa.toNFA().accepts("abbbbb"));
            System.out.println("acepta bbbbb?:" + my_dfa.toNFA().accepts("bbbbb"));
            System.out.println("acepta a?:" + my_dfa.toNFA().accepts("a"));

            /*DFA my_dfa2 = (DFA) FA.parse_form_file("test/dfa3.dot");
             System.out.println("vacio dfa3?: "+my_dfa2.is_empty());*/
            /*NFALambda my_nfa3 = (NFALambda) FA.parse_form_file("test/nfalambda2.dot");
             System.out.println("acepta casa?: " + my_nfa3.accepts("casa"));
             System.out.println("acepta cas?: " + my_nfa3.accepts("cas"));
             System.out.println("acepta asa?: " + my_nfa3.accepts("asa"));
             System.out.println("acepta casa?: " + my_nfa3.accepts("casac"));
             System.out.println("acepta casacasacasaca?: " + my_nfa3.accepts("casacasacasaca"));*/
            //System.out.println("acepta casacasa?: "+my_nfa3.accepts("casacasa"));
            /*NFALambda my_nfa3 = (NFALambda) FA.parse_form_file("test/nfalambda1.dot");
            System.out.println("acepta casa?: " + my_nfa3.accepts("casa"));
            System.out.println("acepta asa?: " + my_nfa3.accepts("asa"));
            System.out.println("acepta ca?: " + my_nfa3.accepts("ca"));
            System.out.println("acepta c?: " + my_nfa3.accepts("c"));
            System.out.println("acepta asasasasa?: " + my_nfa3.accepts("asasasasa"));
            System.out.println("acepta casas?: " + my_nfa3.accepts("casas"));
                    //System.out.println("acepta aba?: "+dfa_del_nfa.accepts("aba"));*/
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
