
import automata.DFA;
import automata.FA;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author fede
 */
public class TestSameLanguage {

    private static DFA test;
    private static DFA test2;
    private static DFA test3;
    private static DFA test4;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        test2 = (DFA) FA.parse_form_file("test/testLenguaje.dot");
        test = (DFA) FA.parse_form_file("test/testLenguaje2.dot");
        test3 = (DFA) FA.parse_form_file("test/dfa1.dot");
        test4 = (DFA) FA.parse_form_file("test/dfa2.dot");
    }

    
    @Test
	public void test1() throws Exception {
		assertTrue(test.sameLanguage(test2));
		assertTrue(test3.sameLanguage(test3));
		assertFalse(test3.sameLanguage(test4));
		assertFalse(test2.sameLanguage(test4));
	}
}
