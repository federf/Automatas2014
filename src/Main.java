
import automata.APD;
import automata.AutomataPila;
import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.NFALambda;
import automata.State;
import grep.ParserRecDes;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
             DFA dfa1 = (DFA) FA.parse_form_file("test/dfa3.dot");
             System.out.println("dfa interseccion su complemento, es vacio?: " + dfa1.intersection(dfa1.complement()).is_empty());

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

             //TestMinimization.test1
             DFA dfa5 = (DFA) FA.parse_form_file("test/dfa1.dot");
             System.out.println("1 -" + dfa5.minimizar().accepts("ab"));
             System.out.println("2 -" + dfa5.minimizar().accepts("abbbbb"));
             System.out.println("3 -" + dfa5.minimizar().accepts("bbbbb"));
             System.out.println("4 -" + dfa5.minimizar().accepts("a"));*/

            //dfa para test practica 3 ej 29 b
            DFA test = (DFA) FA.parse_form_file("test/testLenguaje2.dot");
            DFA test2 = (DFA) FA.parse_form_file("test/testLenguaje.dot");
            DFA test3 = (DFA) FA.parse_form_file("test/dfa1.dot");
            DFA test4 = (DFA) FA.parse_form_file("test/dfa2.dot");

            //System.out.println("mismo lenguaje con automatas iguales dfa1 con dfa1? (true): " + test3.sameLanguage(test3));
            System.out.println("mismo lenguaje con automatas iguales test1 y test2? (true): " + test.sameLanguage(test2));
            System.out.println("mismo lenguaje con automatas iguales dfa1 y dfa1? (true): " + test3.sameLanguage(test3));
            System.out.println("mismo lenguaje con automatas iguales dfa1 y dfa2? (false): " + test3.sameLanguage(test4));

            /*APD apd1 = (APD) AutomataPila.parse_from_file("test/apd1.dot");
             System.out.println();
             //System.out.println(apd1.to_dot());
             System.out.println("acepta aab?: "+apd1.accepts("aab"));
             System.out.println("------------------------------------");
             System.out.println("acepta aaab?: "+apd1.accepts("aaab"));
             System.out.println("------------------------------------");
             System.out.println("acepta aabb?: "+apd1.accepts("aabb"));*/
            //IntegrationTests.test3
//            NFALambda my_nfalambda = (NFALambda) FA.parse_form_file("test/nfalambda1.dot");
//            DFA dfa4 = my_nfalambda.toDFA();
//            System.out.println("acepta casa?: " + dfa4.accepts("casa"));
//            System.out.println("acepta asa?: " + dfa4.accepts("asa"));
//            System.out.println("acepta cas?: " + dfa4.accepts("cas"));
//            System.out.println("acepta asac?: " + dfa4.accepts("asac"));
            ParserRecDes p=new ParserRecDes();
            System.out.println("valida?: "+p.evaluar("a"));
            System.out.println("valida?: "+p.evaluar("a+b*"));
            System.out.println("valida?: "+p.evaluar("a++b*"));
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
