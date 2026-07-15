/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package CapaNegocio;

/**
 *
 * @author Asus
 */
public interface IServicioBancario {
    boolean login(String id, String pass);
    void transferenciaInterbancaria(String origen, String destino, double monto) throws Exception;
    void deshacerUltimaAccion(String cedula) throws Exception;
    double simularInteresCompuesto(double saldo, double tasa, int meses);
}
