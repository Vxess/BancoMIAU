/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package BancoMIAU;

import CapaEntidades.Cliente;
import CapaNegocio.BancoServicios;
import CapaPresentacion.PaginaPrincipal;

/**
 *
 * @author Asus
 */
public class BancoMIAU {

   public static void main(String[] args) {
       // 1. CONFIGURACIÓN VISUAL (Recomendado para que se vea moderno)
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // Si falla, arranca con el diseño básico de Java
            }
        }

        // 2. CONEXIÓN LÓGICA: Instancia única del servicio en memoria
        BancoServicios bancoServicios = new BancoServicios();
        
        // 3. CONEXIÓN DE INTERFAZ: Se pasa por parámetro al constructor corregido
        PaginaPrincipal pantallaInicio = new PaginaPrincipal(bancoServicios);
        
        // 4. ENCENDIDO: Hace visible el menú principal
        pantallaInicio.setVisible(true);
/*
        

        System.out.println("=== ARRANQUE DEL MODO DE PRUEBAS LOGICAS ===");
        
        try {
            // 1. Instanciamos la capa de negocio en memoria
            BancoServicios bancoServicios = new BancoServicios();
            System.out.println("[OK] Capa de negocio e infraestructura inicializada con exito.");

            // 2. Intentamos registrar un cliente de prueba de forma logica
            System.out.println("\n--- Probando Registro de Cliente ---");
            bancoServicios.registrarCliente(
                "1755555555", "Carlos", "Mendoza", 
                "carlos@correo.com", "0987654321", 
                25, "carlos99", "clave123"
            );
            System.out.println("[OK] Cliente registrado correctamente en la Lista Doble.");

            // 3. Probar la autenticacion (Login) del cliente registrado
            System.out.println("\n--- Probando Autenticacion ---");
            String rol = bancoServicios.autenticarUsuario("carlos99", "clave123");
            System.out.println("[OK] Autenticacion exitosa. Rol detectado: " + rol);

            // 4. Extraer los datos guardados en la lista para verificar integridad
            Cliente clienteVerificado = bancoServicios.obtenerCliente("carlos99");
            if (clienteVerificado != null) {
                System.out.println("[DATOS] Nombre: " + clienteVerificado.getUsuario());
                System.out.println("[DATOS] Cuenta Ahorros asignada: " + clienteVerificado.getNumeroCuentaAhorros());
                System.out.println("[DATOS] Cuenta Corriente asignada: " + clienteVerificado.getNumeroCuentaCorriente());
            }

            System.out.println("\n=============================================");
            System.out.println("COMPILACION CORRECTA! El motor del banco funciona.");
            System.out.println("=============================================");

        } catch (Exception e) {
            System.out.println("\n[ERROR] Ocurrio un fallo en las pruebas logicas: " + e.getMessage());
        }
*/
    }

    
}
