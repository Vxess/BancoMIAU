package CapaEntidades;

/**
 *
 * @author Vanessa y Emily
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
        return null; 
    }

    // NUEVO MÉTODO COMPLETO: Requerido por el Administrador para localizar cuentas por Cédula
    public Cliente buscarPorCedula(String cedula) {
        Nodo actual = inicio;
        while (actual != null) {
            if (actual.getUsuarios().getCedula().equals(cedula)) {
                return actual.getUsuarios();
            }
            actual = actual.siguiente;
        }
        return null; 
    }
}