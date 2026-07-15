/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TADs;


/**
 *
 * @author Vanessa
 */

public class Nodo {
    //Se instancia un Objeto para que se use para Clientes, Cheques o cualquier TAD
    private Object dato; 
    public Nodo siguiente;
    public Nodo anterior;

    // Constructor
    public Nodo(Object dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }

    // Getters y Setters universales
    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }
}