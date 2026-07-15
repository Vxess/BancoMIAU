/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package CapaNegocio;

import CapaEntidades.Cliente;

/**
 *
 * @author Asus
 */
public interface IServicioAdministrativo {
    void registrarNuevoCliente(Cliente c) throws Exception;
    void gestionarLimiteSeguridad(String cedula, double nuevoMonto) throws Exception;
    void marcarChequeComoRebotado(String idCheque) throws Exception;
    void precargarClientesPrueba();
}
