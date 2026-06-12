/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaEntidades;

/**
 *
 * @author Vanessa
 */
public class ListaDoble {
    private Nodo inicio;
    private Nodo fin;

    public void insertarAlFinal(Cliente nuevoCliente) {
        Nodo nuevoIngreso = new Nodo(nuevoCliente);
        if (inicio == null) {
            inicio = nuevoIngreso;
            fin = nuevoIngreso;
        } else {
            fin.siguiente = nuevoIngreso;
            nuevoIngreso.anterior = fin;
            fin = nuevoIngreso;
        }
    }

    public Cliente buscarPorUsuario(String usuario) {
        Nodo actual = inicio;
        while (actual != null) {
            if (actual.getUsuarios().getUsuario().equalsIgnoreCase(usuario)) {
                return actual.getUsuarios();
            }
            actual = actual.siguiente;
        }
        //Si no se encuentra retornar nulo
        return null; 
        
    }
}
