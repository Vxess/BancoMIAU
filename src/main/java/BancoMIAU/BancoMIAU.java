/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package BancoMIAU;

import CapaNegocio.BancoServicios;
import CapaPresentacion.PaginaPrincipal;

/**
 *
 * @author Asus
 */
public class BancoMIAU {

    public static void main(String[] args) {
       // 1. CONFIGURACIÓN VISUAL (Opcional, pero recomendado): 
        // Cambia el diseño aburrido de Java por el del sistema operativo (Windows/Mac)
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, arranca con el diseño por defecto de Java
        }

        // 2. CONEXIÓN LÓGICA: Creas la única instancia de la base de datos en memoria.
        // Aquí se ejecuta el constructor que quema tus usuarios de prueba (Vanessa y Ámbar).
        BancoServicios bancoServicios = new BancoServicios();
        
        // 3. CONEXIÓN DE INTERFAZ: Instancias tu pantalla de bienvenida (JFrame)
        // y le pasas la lógica por medio de su constructor.
        PaginaPrincipal pantallaInicio = new PaginaPrincipal(bancoServicios);
        
        // 4. ENCENDIDO: Haces visible el menú principal para el usuario
        pantallaInicio.setVisible(true);
    }
    
}
