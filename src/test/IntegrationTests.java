import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.NFALambda;


public class IntegrationTests {
	
	static DFA my_dfa;
	
	static NFA my_nfa;
	
	static NFALambda my_nfalambda;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		my_dfa = (DFA) FA.parse_form_file("test/dfa1");
		my_nfa = (NFA) FA.parse_form_file("test/nfa1");
		my_nfalambda = (NFALambda) FA.parse_form_file("test/nfalambda1");
	}

	@Test
	public void test1() {
		assertTrue(my_dfa.toNFA().accepts("ab"));
		assertTrue(my_dfa.toNFA().accepts("abbbbb"));
		assertFalse(my_dfa.toNFA().accepts("bbbbb"));
		assertFalse(my_dfa.toNFA().accepts("a"));
	}
	
	@Test
	public void test2() {
		assertTrue(my_nfa.toDFA().accepts("ab"));
		assertTrue(my_nfa.toDFA().accepts("abaaaaa"));
		assertFalse(my_nfa.toDFA().accepts("abbbb"));
		assertFalse(my_nfa.toDFA().accepts("a"));
	}
	
	@Test
	public void test3() {
		assertTrue(my_nfalambda.toDFA().accepts("casa"));
		assertTrue(my_nfalambda.toDFA().accepts("asa"));
		assertFalse(my_nfalambda.toDFA().accepts("cas"));
		assertFalse(my_nfalambda.toDFA().accepts("asac"));
	}
	
	
}
