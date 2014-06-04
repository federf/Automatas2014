
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
public class TestMinimization {

    private static DFA dfa;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dfa = (DFA) FA.parse_form_file("test/dfa1.dot");
    }
    
    
    @Test
	public void test1() throws Exception {
		assertTrue(dfa.minimizar().accepts("ab"));
		assertTrue(dfa.minimizar().accepts("abbbbb"));
		assertFalse(dfa.minimizar().accepts("bbbbb"));
		assertFalse(dfa.minimizar().accepts("a"));
	}
}
