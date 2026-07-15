/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TADs;

import CapaEntidades.Cheque;


/**
 * Cola circular implementada con nodos enlazados: el último nodo siempre
 * apunta de vuelta al primero ("frente"), formando el circuito.
 * Usada por BancoService para gestionar la revisión de cheques.
 *
 * @author Vanessa
 */
public class ColaCircularCheques {
    private Nodo frente;
    private Nodo finalNodo;
    private int tamaño;
    private int maxCapacidad;

    public ColaCircularCheques() {
        this.frente = null;
        this.finalNodo = null;
        this.tamaño = 0;
        this.maxCapacidad = 20; // Capacidad por defecto
    }

    public ColaCircularCheques(int maxCapacidad) {
        this();
        this.maxCapacidad = maxCapacidad;
    }

    public void encolar(Cheque c) throws Exception {
        if (estaLlena()) {
            throw new Exception("No se puede encolar: la cola de cheques está llena.");
        }
        Nodo nuevo = new Nodo(c);
        if (frente == null) {
            frente = nuevo;
            finalNodo = nuevo;
            nuevo.siguiente = nuevo; // se enlaza a sí mismo (circular)
        } else {
            nuevo.siguiente = frente;
            finalNodo.siguiente = nuevo;
            finalNodo = nuevo;
        }
        tamaño++;
    }

    public Cheque desencolar() {
        if (estaVacia()) {
            return null;
        }
        Cheque c = (Cheque) frente.getDato();
        if (frente == finalNodo) {
            // Era el único elemento
            frente = null;
            finalNodo = null;
        } else {
            frente = frente.siguiente;
            finalNodo.siguiente = frente; // se mantiene el circuito
        }
        tamaño--;
        return c;
    }

    public boolean estaLlena() {
        return tamaño == maxCapacidad;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public int getTamaño() {
        return tamaño;
    }

    /**
     * Saca de la cola el cheque con ese número (sin importar su posición) y
     * deja el resto de la cola intacta, en el mismo orden. Devuelve null si
     * no se encontró.
     */
    public Cheque extraerPorNumero(String numeroCheque) throws Exception {
        int total = tamaño;
        Cheque encontrado = null;

        for (int i = 0; i < total; i++) {
            Cheque actual = desencolar();
            if (encontrado == null && actual.getNumeroCheque().equals(numeroCheque)) {
                encontrado = actual; // este NO se vuelve a encolar
            } else {
                encolar(actual);
            }
        }
        return encontrado;
    }

    /**
     * Devuelve una lista (snapshot) de todos los cheques actualmente en la
     * cola, sin modificar su orden ni su contenido. Útil para pintar la
     * tabla de Area_Admin.
     */
    public java.util.List<Cheque> listarTodos() {
        java.util.List<Cheque> lista = new java.util.ArrayList<>();
        Nodo actual = frente;
        for (int i = 0; i < tamaño; i++) {
            lista.add((Cheque) actual.getDato());
            actual = actual.siguiente;
        }
        return lista;
    }
}


