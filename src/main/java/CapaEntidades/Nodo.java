/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaEntidades;

/**
 *
 * @author Vanessa
 */
public class Nodo {
    private Cliente usuarios;
    public Nodo siguiente;
    public Nodo anterior;

    //constructor
    public Nodo(Cliente usuarios) {
        this.usuarios = usuarios;
        this.siguiente = null;
        this.anterior = null;
    }

    // getter y setter
    public Cliente getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Cliente usuarios) {
        this.usuarios = usuarios;
    }
    
    
}
