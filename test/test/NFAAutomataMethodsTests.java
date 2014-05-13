package test;

import static org.junit.Assert.*;

import org.junit.Test;

import automata.NFA;
import automata.FA;


public class NFAAutomataMethodsTests { 

	// Tests for NFA1
	
	@Test
	public void test1() throws Exception {
		NFA nfa = (NFA) FA.parse_form_file("test/test/nfa1.dot");
		assertTrue(nfa.accepts("ab"));
	}
	
	@Test
	public void test2() throws Exception {
		NFA nfa = (NFA) FA.parse_form_file("test/test/nfa1.dot");
		assertTrue(nfa.accepts("abaaaaaaaa"));
	}

	@Test
	public void test3() throws Exception {
		NFA nfa = (NFA) FA.parse_form_file("test/test/nfa1.dot");
		assertFalse(nfa.accepts("abaaaaaaab"));
	}
	
	// Tests for NFA2
	
	@Test
	public void test4() throws Exception {
		NFA nfa = (NFA) FA.parse_form_file("test/test/nfa2.dot");
		assertTrue(nfa.accepts("a"));
	}
	
	@Test
	public void test5() throws Exception {
		NFA nfa = (NFA) FA.parse_form_file("test/test/nfa2.dot");
		assertTrue(nfa.accepts("aaaaaaaaaaaaaaa"));
	}	
	
	@Test
	public void test6() throws Exception {
		NFA nfa = (NFA) FA.parse_form_file("test/test/nfa2.dot");
		assertFalse(nfa.accepts(""));
	}
	
}
