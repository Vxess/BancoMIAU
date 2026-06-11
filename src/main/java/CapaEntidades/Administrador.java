/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaEntidades;

/**
 *
 * @author Vanessa
 */
public class Administrador extends Persona {
    private String codigoEmpleado;

    public Administrador(String cedula, String nombre, String apellido, String usuario, 
                         String contraseña, String codigoEmpleado) {
        super(cedula, nombre, apellido, "", "", 0, usuario, contraseña);
        this.codigoEmpleado = codigoEmpleado;
    }
//getters y setters
    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }
    
}
