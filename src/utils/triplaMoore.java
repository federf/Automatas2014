/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import automata.State;

/**
 *
 * @author fede
 */
public class triplaMoore {
    State estadoSalida;
    String cadenaUsada;
    State estadoAlQueLlega;

    
    public triplaMoore(State estadoSalida) {
        this.estadoSalida = estadoSalida;
        this.cadenaUsada = "";
        this.estadoAlQueLlega = new State("error");
    }
    
    public triplaMoore(State estadoSalida, String cadenaUsada, State estadoAlQueLlega) {
        this.estadoSalida = estadoSalida;
        this.cadenaUsada = cadenaUsada;
        this.estadoAlQueLlega = estadoAlQueLlega;
    }

    public State getEstadoSalida() {
        return estadoSalida;
    }

    public void setEstadoSalida(State estadoSalida) {
        this.estadoSalida = estadoSalida;
    }

    public String getCadenaUsada() {
        return cadenaUsada;
    }

    public void setCadenaUsada(String cadenaUsada) {
        this.cadenaUsada = cadenaUsada;
    }

    public State getEstadoDestino() {
        return estadoAlQueLlega;
    }

    public void setEstadoDestino(State estadoAlQueLlega) {
        this.estadoAlQueLlega = estadoAlQueLlega;
    }
    
    @Override
    public String toString(){
        String result=""+this.getEstadoSalida().name()+" -> "+this.getCadenaUsada()+" -> "+this.getEstadoDestino().name();
        return result;
    }
}
