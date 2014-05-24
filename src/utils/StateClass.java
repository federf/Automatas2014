/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import automata.State;
import java.util.LinkedHashSet;

/**
 * clase que implementa clases de estados las cuales seran utilizadas para el proceso
 * de minimizacion de un AFD
 * @author fede
 */


public class StateClass {
    //estado cabeza de la clase de estados indistinguibles entre si
    State header;
    //conjuntos de estados indistinguibles entre si
    LinkedHashSet<State> states;

    //constructor de clase
    //recibe como parametro un estado que sera
    //la cabecera del conjunto
    public StateClass(State s){
        header=s;
        states=new LinkedHashSet();
        states.add(s);
    }
    
    public State getHeader() {
        return header;
    }

    public void setHeader(State header) {
        this.header = header;
    }

    public LinkedHashSet<State> getStates() {
        return states;
    }

    public void setStates(LinkedHashSet<State> states) {
        this.states = states;
    }
    
    //metodo que dado un estado lo agrega al
    //conjunto de estados indistinguibles actual
    //si es que no esta incluido ya
    public void addState(State s){
        boolean agregar=true;
        for(State state:this.states){
            if(state.name().equals(s.name())){
                agregar=false;
            }
        }
        if(agregar){
            states.add(s);
        }
        
    }
}
