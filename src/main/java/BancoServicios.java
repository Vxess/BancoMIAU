package CapaNegocio;

import CapaEntidades.Administrador;
import CapaEntidades.ListaDoble;
import CapaEntidades.Cliente;

/**
 *
 * @author Vanessa y Emily
 */
public class BancoServicios {
    private ListaDoble listaClientes;
    private Administrador adminUnico; 

    public BancoServicios() {
        this.listaClientes = new ListaDoble();
        
        this.adminUnico = new Administrador("1711111111", "Admin", "General", "admin", "admin123", "ADM001");
        precargarClientesPrueba();
    }

    // Rgistro de clientes 
    public void registrarCliente(String cedula, String nombre, String apellido, String correo, 
                                 String telefono, int edad, String usuario, String password) 
        throws Exception {
        if (edad < 18) {
            throw new Exception("Error: El cliente debe ser mayor de 18 años para abrir una cuenta.");
        }
        if (listaClientes.buscarPorUsuario(usuario) != null) {
            throw new Exception("Error: El nombre de usuario ya se encuentra registrado.");
        }

        // Generación  de numeros de cuenta distintos
        String numAhorros = "AHO-" + (int)(Math.random() * 900000 + 100000);
        String numCorriente = "COR-" + (int)(Math.random() * 900000 + 100000);

        Cliente nuevo = new Cliente(cedula, nombre, apellido, correo, telefono, edad, usuario, password, numAhorros, numCorriente);
        listaClientes.insertarAlFinal(nuevo);
    }

    // Reconocimiento de roles de cada usuario 
    public String autenticarUsuario(String usuario, String password) throws Exception {
        if (adminUnico.getUsuario().equals(usuario) && adminUnico.getContraseña().equals(password)) {
            return "ADMIN";
        }
        
        Cliente cliente = listaClientes.buscarPorUsuario(usuario);
        if (cliente != null && cliente.getContraseña().equals(password)) {
            return "CLIENTE";
        }

        throw new Exception("Error: Credenciales incorrectas o usuario inexistente.");
    }

    public Cliente obtenerCliente(String usuario) {
        return listaClientes.buscarPorUsuario(usuario);
    }

    private void precargarClientesPrueba() {
        try {
            registrarCliente("1722222222", "Vanessa", "Ingenieria", "vanessa@epn.edu.ec", "0999999991", 20, "vanessa", "vane123");
            registrarCliente("1733333333", "Emily", "Ingenieria", "Emily@epn.edu.ec", "0999999992", 19, "Emily", "Emi123");
        } catch (Exception e) {
            // Errores de precarga controlados en memoria
        }
    }

    //Cambios más importantes
     *Función para hacer depositos
     */
    public void depositar(String usuario, String tipoCuenta, double monto) throws Exception {
        Cliente c = obtenerCliente(usuario);
        if (c == null) throw new Exception("Error: Cliente no localizado en el sistema.");
        
        if (tipoCuenta.equalsIgnoreCase("AHORROS")) {
            c.depositarAhorros(monto);
        } else if (tipoCuenta.equalsIgnoreCase("CORRIENTE")) {
            c.depositarCorriente(monto);
        } else {
            throw new Exception("Error: Tipo de cuenta desconocido.");
        }
    }

    /**
     * Función para evaluar los retiros 
     */
    public void retirar(String usuario, String tipoCuenta, double monto) throws Exception {
        Cliente c = obtenerCliente(usuario);
        if (c == null) throw new Exception("Error: Cliente no localizado en el sistema.");
        
        if (tipoCuenta.equalsIgnoreCase("AHORROS")) {
            c.retirarAhorros(monto);
        } else if (tipoCuenta.equalsIgnoreCase("CORRIENTE")) {
            c.retirarCorriente(monto);
        } else {
            throw new Exception("Error: Tipo de cuenta desconocido.");
        }
    }

    /**
     * Función de transferencia entre distintas cuentas 
     */
    public void realizarTransferencia(String usuarioOrigen, String tipoOrigen, String cedulaDestino, double monto) throws Exception {
        Cliente origen = obtenerCliente(usuarioOrigen);
        Cliente destino = listaClientes.buscarPorCedula(cedulaDestino);
        
        if (origen == null) throw new Exception("Error: Su sesión no es válida o la cuenta origen no existe.");
        if (destino == null) throw new Exception("Error: La cédula del beneficiario no coincide con ningún cliente.");
        
        if (monto > origen.getLimiteDiarioTransaccion()) {
            throw new Exception("Transferencia Rechazada: El monto supera el límite diario asignado por el Administrador.");
        }

        // Ver de que tipo de cuente viene el dinero
        if (tipoOrigen.equalsIgnoreCase("AHORROS")) {
            origen.retirarAhorros(monto);
        } else {
            origen.retirarCorriente(monto);
        }

        // asegurar el destino de los depositos
        destino.depositarAhorros(monto);
        
        // Registro de transacciones diarias
        origen.setTransaccionesHoy(origen.getTransaccionesHoy() + 1);
    }

    //Funciones especificas para administrador
    public void administrarLimiteSeguridad(String usuarioAdmin, String cedulaCliente, double nuevoLimite) throws Exception {
        if (!adminUnico.getUsuario().equals(usuarioAdmin)) {
            throw new Exception("Acceso Denegado: Solo el perfil Administrativo puede modificar las reglas del sistema.");
        }
        if (nuevoLimite < 0) {
            throw new Exception("Error: El límite asignado debe ser una cantidad positiva.");
        }
        
        Cliente c = listaClientes.buscarPorCedula(cedulaCliente);
        if (c == null) throw new Exception("Error: No se encontró ningún cliente registrado con la cédula ingresada.");
        
        c.setLimiteDiarioTransaccion(nuevoLimite);
    }
    