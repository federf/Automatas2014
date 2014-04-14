import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import utils.Triple;
import automata.DFA;
import automata.State;


public class DFAStateQueryingTests {

	private DFA dummy_dfa;
	
	@Before
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setUp() throws Exception {
		Set<State> states = new HashSet<State>();
		Set<Character> alpha = new HashSet<Character>();
		Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
		State initial;
		Set<State> finals = new HashSet<State>();
		
		State s0 = new State("s0");
		State s1 = new State("s1");
		State s2 = new State("s2");
		states.add(s0);
		states.add(s1);
		states.add(s2);
		alpha.add('a');
		alpha.add('b');
		transitions.add(new Triple(s0, 'a', s1));
		transitions.add(new Triple(s1, 'a', s2));
		transitions.add(new Triple(s2, 'a', s0));
		transitions.add(new Triple(s0, 'b', s0));
		transitions.add(new Triple(s1, 'b', s1));
		transitions.add(new Triple(s2, 'b', s2));
		initial = s0;
		finals.add(s1);
		
		dummy_dfa = new DFA(states, alpha, transitions, initial, finals);
	}
	
	@Test
	public void test1() {
		assertTrue(dummy_dfa.states().size() == 3);	
	}
	
	@Test
	public void test2() {
		assertTrue(dummy_dfa.states().contains(dummy_dfa.initial_state()));	
	}
	
	@Test
	public void test3() {
		assertTrue(dummy_dfa.states().containsAll(dummy_dfa.final_states()));	
	}
	
	
	@Test
	public void test4() {
		Set<Character> _set = new HashSet<Character>();
		_set.add('a');
		_set.add('b');
		assertTrue(dummy_dfa.alphabet().equals(_set));	
	}	
	
	@Test
	public void test5() {
		State s0 = dummy_dfa.initial_state();
		State s1 = dummy_dfa.delta(s0, 'a');
		State s2 = dummy_dfa.delta(s1, 'a');
		
		assertEquals(dummy_dfa.delta(s0, 'b'),s0);
		assertEquals(dummy_dfa.delta(s1, 'b'),s1);
		assertEquals(dummy_dfa.delta(s2, 'b'),s2);
	}	
	
	@Test
	public void test6() {
		State s0 = dummy_dfa.initial_state();
		State s1 = dummy_dfa.delta(s0, 'a');
		State s2 = dummy_dfa.delta(s1, 'a');
		
		assertEquals(dummy_dfa.delta(s2, 'a'),s0);	
	}		
		
}
