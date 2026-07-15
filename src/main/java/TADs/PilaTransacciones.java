/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TADs;

import CapaEntidades.Cliente;

/**
 * Pila (LIFO) implementada con Nodo enlazado. Cada Cliente tiene su propia
 * instancia, y guarda el historial de operaciones para poder deshacerlas.
 *
 * @author Vanessa
 */
public class PilaTransacciones {
    private Nodo tope;

    public void push(Object t) {
        Nodo nuevo = new Nodo(t);
        nuevo.siguiente = tope;
        tope = nuevo;
    }

    public Object pop() {
        if (estaVacia()) {
            return null;
        }
        Object dato = tope.getDato();
        tope = tope.siguiente;
        return dato;
    }

    public Object peek() {
        if (estaVacia()) {
            return null;
        }
        return tope.getDato();
    }

    public boolean estaVacia() {
        return tope == null;
    }
}

