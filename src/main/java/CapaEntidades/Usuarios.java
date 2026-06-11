/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaEntidades;

/**
 *
 * @author Vanessa
 */
public class Usuarios extends Persona{
    private String numeroCuentaAhorros;
    private String numeroCuentaCorriente;
    private double saldoAhorros;
    private double saldoCorriente;

    //constructor
    public Usuarios(String cedula, String nombre, String apellido, String correo,
            String telefono, int edad, String usuario, String contraseña, 
            String numeroCuentaAhorros, String numeroCuentaCorriente) {
        super(cedula, nombre, apellido, correo, telefono, edad, usuario, contraseña);
        this.numeroCuentaAhorros = numeroCuentaAhorros;
        this.numeroCuentaCorriente = numeroCuentaCorriente;
         // Saldo inicial
        this.saldoAhorros = 0.0;
        this.saldoCorriente = 0.0;
    }

    //getters y setters
    public String getNumeroCuentaAhorros() {
        return numeroCuentaAhorros;
    }

    public void setNumeroCuentaAhorros(String numeroCuentaAhorros) {
        this.numeroCuentaAhorros = numeroCuentaAhorros;
    }

    public String getNumeroCuentaCorriente() {
        return numeroCuentaCorriente;
    }

    public void setNumeroCuentaCorriente(String numeroCuentaCorriente) {
        this.numeroCuentaCorriente = numeroCuentaCorriente;
    }

    public double getSaldoAhorros() {
        return saldoAhorros;
    }

    public void setSaldoAhorros(double saldoAhorros) {
        this.saldoAhorros = saldoAhorros;
    }

    public double getSaldoCorriente() {
        return saldoCorriente;
    }

    public void setSaldoCorriente(double saldoCorriente) {
        this.saldoCorriente = saldoCorriente;
    }

   
    
}
