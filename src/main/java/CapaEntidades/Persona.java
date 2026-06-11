/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaEntidades;

/**
 *
 * @author Asus
 */
public class Persona {
    protected String cedula;
    protected String nombre;
    protected String apellido;
    protected String correo;
    protected String telefono;
    protected int edad;
    protected String usuario;
    protected String contraseña;

    public Persona(String cedula, String nombre, String apellido, String correo, String telefono, int edad, String usuario, String contraseña) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.edad = edad;
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
 
}
