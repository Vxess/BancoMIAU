/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaEntidades;

/**
 * Representa una solicitud emitida por un cliente desde su cuenta
 * (Ahorros o Corriente) hacia el área administrativa, para que un
 * administrador la acepte o la rechace (por ejemplo: aumento de límite
 * diario, apertura de una cuenta adicional, u otro trámite general).
 *
 * @author Asus
 */
public class Solicitud {

    private static int contadorIds = 1;

    private final int id;
    private String tipo;              // "LIMITE", "APERTURA", "GENERAL", "REGISTRO"
    private String cedulaCliente;
    private String nombreCliente;
    private String cuentaAsociada;
    private double montoSolicitado;
    private String detalle;
    private String estado;            // "Pendiente", "Aceptada", "Rechazada"
    // Solo para tipo "REGISTRO": cliente en espera de aprobación administrativa
    private CapaEntidades.Cliente clienteEnEspera;

    public Solicitud(String tipo, String cedulaCliente, String nombreCliente,
            String cuentaAsociada, double montoSolicitado, String detalle) {
        this.id = contadorIds++;
        this.tipo = tipo;
        this.cedulaCliente = cedulaCliente;
        this.nombreCliente = nombreCliente;
        this.cuentaAsociada = cuentaAsociada;
        this.montoSolicitado = montoSolicitado;
        this.detalle = detalle;
        this.estado = "Pendiente";
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getCuentaAsociada() {
        return cuentaAsociada;
    }

    public double getMontoSolicitado() {
        return montoSolicitado;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CapaEntidades.Cliente getClienteEnEspera() {
        return clienteEnEspera;
    }

    public void setClienteEnEspera(CapaEntidades.Cliente clienteEnEspera) {
        this.clienteEnEspera = clienteEnEspera;
    }

    @Override
    public String toString() {
        return "#" + id + " | " + nombreCliente + " (CC: " + cedulaCliente + ") | "
                + tipo + (montoSolicitado > 0 ? " $ " + montoSolicitado : "")
                + " | " + estado + " | " + detalle;
    }
}
