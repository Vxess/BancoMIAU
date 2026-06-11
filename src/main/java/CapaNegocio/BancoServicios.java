/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaNegocio;

import CapaEntidades.Administrador;
import CapaEntidades.ListaDoble;
import CapaEntidades.Usuarios;

/**
 *
 * @author Vanessa
 */
public class BancoServicios {
    private ListaDoble listaClientes;
    private Administrador adminUnico; // Administrador por defecto en memoria

    public BancoServicios() {
        this.listaClientes = new ListaDoble();
        // Quemamos un Administrador por defecto para pruebas
        this.adminUnico = new Administrador("1711111111", "Admin", "General", "admin", "admin123", "ADM001");
        precargarClientesPrueba();
    }

    // REGISTRO CON VALIDACIONES
    public void registrarCliente(String cedula, String nombre, String apellido, String correo, 
                                 String telefono, int edad, String usuario, String password) 
        throws Exception {
        // Verificar la edad del cliente
        if (edad < 18) {
            throw new Exception("Error: El cliente debe ser mayor de 18 años para abrir una cuenta.");
        }
        if (listaClientes.buscarPorUsuario(usuario) != null) {
            throw new Exception("Error: El nombre de usuario ya se encuentra registrado.");
        }

        // Generación automática de números de cuenta aleatorios
        String numAhorros = "AHO-" + (int)(Math.random() * 900000 + 100000);
        String numCorriente = "COR-" + (int)(Math.random() * 900000 + 100000);

        Usuarios nuevo = new Usuarios(cedula, nombre, apellido, correo, telefono, edad, usuario, password, numAhorros, numCorriente);
        listaClientes.insertarAlFinal(nuevo);
    }

    // LOGIN CON IDENTIFICACIÓN DE ROL
    public String autenticarUsuario(String usuario, String password) throws Exception {
        // 1. Validar si es Admin
        if (adminUnico.getUsuario().equals(usuario) && adminUnico.getContraseña().equals(password)) {
            return "ADMIN";
        }
        
        // 2. Validar si es Cliente
        Usuarios cliente = listaClientes.buscarPorUsuario(usuario);
        if (cliente != null && cliente.getContraseña().equals(password)) {
            return "CLIENTE";
        }

        throw new Exception("Error: Credenciales incorrectas o usuario inexistente.");
    }

    public Usuarios obtenerCliente(String usuario) {
        return listaClientes.buscarPorUsuario(usuario);
    }

    private void precargarClientesPrueba() {
        try {
            registrarCliente("1722222222", "Vanessa", "Ingenieria", "vanessa@epn.edu.ec", "0999999991", 20, "vanessa", "vane123");
            registrarCliente("1733333333", "Emily", "Ingenieria", "Emily@epn.edu.ec", "0999999992", 19, "Emily", "Emi123");
        } catch (Exception e) {
            // Erreores de precarga controlados
        }
    }

}
