import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import utils.Triple;
import automata.NFA;
import automata.State;


public class NFAStateQueryingTests {

	private NFA dummy_nfa;
	
	private State s0;
	
	private State s1;
	
	private State s2;
	
	
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
		transitions.add(new Triple(s1, 'b', s2));
		transitions.add(new Triple(s2, 'a', s0));
		transitions.add(new Triple(s0, 'b', s0));
		transitions.add(new Triple(s1, 'b', s1));
		transitions.add(new Triple(s2, 'b', s2));
		initial = s0;
		finals.add(s1);
		
		this.s0 = s0;
		this.s1 = s1;
		this.s2 = s2;
		
		dummy_nfa = new NFA(states, alpha, transitions, initial, finals);
	}
	
	@Test
	public void test1() {
		assertTrue(dummy_nfa.states().size() == 3);	
	}
	
	@Test
	public void test2() {
		assertTrue(dummy_nfa.states().contains(dummy_nfa.initial_state()));	
	}
	
	@Test
	public void test3() {
		assertTrue(dummy_nfa.states().containsAll(dummy_nfa.final_states()));	
	}
	
	
	@Test
	public void test4() {
		Set<Character> _set = new HashSet<Character>();
		_set.add('a');
		_set.add('b');
		assertTrue(dummy_nfa.alphabet().equals(_set));	
	}	
	
	@Test
	public void test5() {
		assertTrue(dummy_nfa.delta(s0, 'a').size() == 1);
		assertTrue(dummy_nfa.delta(s1, 'b').size() == 2);
	}	
			
		
}
