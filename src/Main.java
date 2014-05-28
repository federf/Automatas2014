
import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.NFALambda;
import automata.State;
import java.util.LinkedHashSet;
import utils.Triple;

public class Main {

    public static void main(String[] args) {
        try {
            /*
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
            System.out.println("acepta a?: " + union.accepts("a") + " acepta bbbb?: " + union.accepts("bbbb"));

            //IntegrationTests.test1 -OK
            DFA my_dfa = (DFA) FA.parse_form_file("test/dfa1.dot");
            System.out.println("1 -" + my_dfa.toNFALambda().accepts("ab"));
            System.out.println("2 -" + my_dfa.toNFALambda().accepts("abbbbb"));
            System.out.println("3 -" + my_dfa.toNFALambda().accepts("bbbbb"));
            System.out.println("4 -" + my_dfa.toNFALambda().accepts("a"));

            System.out.println("dfa1 finito?: " + my_dfa.is_finite());

            //IntegrationTests.test2 -OK
            NFA my_nfa = (NFA) FA.parse_form_file("test/nfa1.dot");
            DFA my_dfa2 = my_nfa.toDFA();
            System.out.println("acepta ab?: " + my_dfa2.accepts("ab"));
            System.out.println("acepta abaaaaa?: " + my_dfa2.accepts("abaaaaa"));
            System.out.println("acepta abbbb?: " + my_dfa2.accepts("abbbb"));
            System.out.println("acepta a?: " + my_dfa2.accepts("a"));

            //IntegrationTests.test3
            NFALambda my_nfalambda = (NFALambda) FA.parse_form_file("test/nfalambda1.dot");
            DFA dfa4 = my_nfalambda.toDFA();
            System.out.println("acepta casa?: " + dfa4.accepts("casa"));
            System.out.println("acepta asa?: " + dfa4.accepts("asa"));
            System.out.println("acepta cas?: " + dfa4.accepts("cas"));
            System.out.println("acepta asac?: " + dfa4.accepts("asac"));*/
            
            //dfa para test practica 3 ej 29 b
            DFA test=(DFA) FA.parse_form_file("test/dfaTestMinim.dot");
            System.out.println(test.accepts("1"));
            System.out.println(test.accepts("11"));
            System.out.println(test.accepts("0"));
            System.out.println(test.accepts("01")); 
            DFA minim=test.minimizacion();
            System.out.println(minim.accepts("1"));
            System.out.println(minim.accepts("11"));
            System.out.println(minim.accepts("0"));
            System.out.println(minim.accepts("01"));
            
            /*DFA dfa1=(DFA) FA.parse_form_file("test/dfa1.dot");
            System.out.println("1 -" + dfa1.accepts("ab"));
            System.out.println("2 -" + dfa1.accepts("abbbbb"));
            System.out.println("3 -" + dfa1.accepts("bbbbb"));
            System.out.println("4 -" + dfa1.accepts("a"));
            DFA minimDfa1=dfa1.minimizacion();
            System.out.println("1 -" + minimDfa1.accepts("ab"));
            System.out.println("2 -" + minimDfa1.accepts("abbbbb"));
            System.out.println("3 -" + minimDfa1.accepts("bbbbb"));
            System.out.println("4 -" + minimDfa1.accepts("a"));*/
            


        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
