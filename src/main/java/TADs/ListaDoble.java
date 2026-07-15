/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TADs;

import CapaEntidades.Cliente;

/**
 *
 * @author Vanessa y Emily
 */

public class ListaDoble {
    private Nodo inicio;
    private Nodo fin;

    public void insertar(Cliente c) {
        Nodo nuevo = new Nodo(c);
        if (inicio == null) {
            inicio = nuevo;
            fin = nuevo;
        } else {
            nuevo.anterior = fin;
            fin.siguiente = nuevo;
            fin = nuevo;
        }
    }

    public Cliente buscarPorCedula(String cedula) {
        Nodo actual = inicio;
        while (actual != null) {
            Cliente c = (Cliente) actual.getDato();
            if (c.getCedula().equals(cedula)) {
                return c;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    // Busca tanto por número de cuenta de ahorros como de cuenta corriente
    public Cliente buscarPorCuenta(String numCuenta) {
        Nodo actual = inicio;
        while (actual != null) {
            Cliente c = (Cliente) actual.getDato();
            if (c.getNumeroCuentaAhorros().equals(numCuenta) || c.getNumeroCuentaCorriente().equals(numCuenta)) {
                return c;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    // recorrer y mostrar todos los clientes en una tabla.
    public java.util.List<Cliente> obtenerTodos() {
        java.util.List<Cliente> lista = new java.util.ArrayList<>();
        Nodo actual = inicio;
        while (actual != null) {
            lista.add((Cliente) actual.getDato());
            actual = actual.siguiente;
        }
        return lista;
    }
    
    //Getters y setters

    public Nodo getInicio() {
        return inicio;
    }

    public void setInicio(Nodo inicio) {
        this.inicio = inicio;
    }

    public Nodo getFin() {
        return fin;
    }

    public void setFin(Nodo fin) {
        this.fin = fin;
    }
    
}
