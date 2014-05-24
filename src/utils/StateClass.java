/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import automata.State;
import java.util.LinkedList;

/**
 * clase que implementa clases de estados las cuales seran utilizadas para el proceso
 * de minimizacion de un AFD
 * @author fede
 */


public class StateClass {
    //estado cabeza de la clase de estados indistinguibles entre si
    State header;
    //lista de nombres de los estados que serian indistinguibles del header
    LinkedList<String> states;

    //constructor de clase
    //recibe como parametro un estado que sera
    //la cabecera del conjunto
    public StateClass(State s){
        header=s;
        states=new LinkedList();
        states.add(s.name());
    }
    
    public State getHeader() {
        return header;
    }

    public void setHeader(State header) {
        this.header = header;
    }

    public LinkedList<String> getStates() {
        return states;
    }

    public void setStates(LinkedList<String> states) {
        this.states = states;
    }
    
    //metodo que dado un estado lo agrega al
    //conjunto de estados indistinguibles actual
    //si es que no esta incluido ya
    public void addState(State s){
        boolean agregar=true;
        if(!states.contains(s.name())){
            states.add(s.name());
        }
        
    }
}
