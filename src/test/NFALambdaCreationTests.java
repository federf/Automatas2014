import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import utils.Triple;
import automata.FA;
import automata.NFALambda;
import automata.State;


public class NFALambdaCreationTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void creation_test1() {
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
		transitions.add(new Triple(s0, FA.Lambda, s1));
		transitions.add(new Triple(s1, 'a', s2));
		transitions.add(new Triple(s2, 'a', s0));
		transitions.add(new Triple(s0, 'b', s0));
		transitions.add(new Triple(s0, 'b', s1));
		transitions.add(new Triple(s1, FA.Lambda, s1));
		transitions.add(new Triple(s2, 'b', s2));
		initial = s0;
		finals.add(s1);
		
		NFALambda my_nfa = new NFALambda(states, alpha, transitions, initial, finals);
		
		assertTrue(my_nfa.rep_ok());	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalArgumentException.class)
	// An exception should be thrown if the initial state does not belong to states.
	public void creation_test2() {
		Set<State> states = new HashSet<State>();
		Set<Character> alpha = new HashSet<Character>();
		Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
		State initial;
		Set<State> finals = new HashSet<State>();
		
		State s0 = new State("s0");
		State s1 = new State("s1");
		states.add(s1);
		alpha.add('a');
		transitions.add(new Triple(s1, 'a', s1));
		initial = s0;
		finals.add(s1);
		
		NFALambda my_nfa = new NFALambda(states, alpha, transitions, initial, finals);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalArgumentException.class)
	// An exception should be thrown if the final states are not included in states.
	public void creation_test3() {
		Set<State> states = new HashSet<State>();
		Set<Character> alpha = new HashSet<Character>();
		Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
		State initial;
		Set<State> finals = new HashSet<State>();
		
		State s0 = new State("s0");
		State s1 = new State("s1");
		states.add(s0);
		alpha.add('a');
		transitions.add(new Triple(s0, 'a', s0));
		initial = s0;
		finals.add(s1);
		
		NFALambda my_nfa = new NFALambda(states, alpha, transitions, initial, finals);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalArgumentException.class)
	// The transitions should use only states that are listed in states
	public void creation_test4() {
		Set<State> states = new HashSet<State>();
		Set<Character> alpha = new HashSet<Character>();
		Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
		State initial;
		Set<State> finals = new HashSet<State>();
		
		State s0 = new State("s0");
		State s1 = new State("s1");
		states.add(s0);
		alpha.add('a');
		transitions.add(new Triple(s0, 'a', s0));
		transitions.add(new Triple(s1, 'a', s1));
		initial = s0;
		finals.add(s0);
		
		NFALambda my_nfa = new NFALambda(states, alpha, transitions, initial, finals);
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	// The transitions should use alphabet characters listed in the alphabet.
	// With the exception of Lambda.
	public void creation_test5() {
		Set<State> states = new HashSet<State>();
		Set<Character> alpha = new HashSet<Character>();
		Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
		State initial;
		Set<State> finals = new HashSet<State>();
		
		State s0 = new State("s0");
		states.add(s0);
		alpha.add('a');
		transitions.add(new Triple(s0, FA.Lambda, s0));
		
		initial = s0;
		finals.add(s0);
		
		NFALambda my_nfa = new NFALambda(states, alpha, transitions, initial, finals);
		assertTrue(my_nfa.rep_ok());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected=IllegalArgumentException.class)
	// The transitions should use alphabet characters listed in the alphabet.
	public void creation_test6() {
		Set<State> states = new HashSet<State>();
		Set<Character> alpha = new HashSet<Character>();
		Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
		State initial;
		Set<State> finals = new HashSet<State>();
		
		State s0 = new State("s0");
		states.add(s0);
		alpha.add('a');
		transitions.add(new Triple(s0, 'b', s0));
		
		initial = s0;
		finals.add(s0);
		
		NFALambda my_nfa = new NFALambda(states, alpha, transitions, initial, finals);
	}
	

	@Test
	public void test1() throws Exception {
		FA nfa1 = FA.parse_form_file("test/nfalambda1");
		FA nfa2 = FA.parse_form_file("test/nfalambda2");
		assertTrue(nfa1 instanceof NFALambda);
		assertTrue(nfa2 instanceof NFALambda);
	}
	

}
