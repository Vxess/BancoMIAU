/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaNegocio;

import CapaEntidades.Administrador;
import CapaEntidades.Cheque;
import CapaEntidades.Cliente;
import CapaEntidades.Solicitud;
import TADs.ColaCircularCheques;
import TADs.ListaDoble;

/**
 * Implementa IServicioAdministrativo e IServicioBancario.
 * Internamente gestiona todos los usuarios (ListaDobleUsuarios) y la cola
 * de revisión de cheques (ColaCircularCheques).
 *
 * @author Vanessa
 */
public class BancoServicios implements IServicioAdministrativo, IServicioBancario {

    private ListaDoble repositorioUsuarios;
    private ColaCircularCheques colaRevision;
    private final java.util.List<Solicitud> solicitudes = new java.util.ArrayList<>();
    private final java.util.List<Cheque> historialCheques = new java.util.ArrayList<>();
    // Contador global secuencial de cheques para todo el banco (no por cliente)
    private int contadorGlobalCheques = 0;
    // Log de auditoría: cada entrada es {fecha, accion, detalle}
    private final java.util.List<String[]> logAuditoria = new java.util.ArrayList<>();

    // No aparece como atributo explícito en el diagrama de BancoService, pero
    // es necesario mantener una referencia al administrador único del sistema
    // para poder validar sus credenciales en login().
    private Administrador adminUnico;

    public BancoServicios() {
        this.repositorioUsuarios = new ListaDoble();
        this.colaRevision = new ColaCircularCheques();
        this.adminUnico = new Administrador("1711111111", "Admin", "General", "admin", "admin123", "ADM001");
    }

    // ======================= IServicioBancario =======================

    @Override
    public boolean login(String id, String pass) {
        // 1. Administrador único
        if (adminUnico.getUsuario().equals(id) && adminUnico.getContraseña().equals(pass)) {
            return true;
        }
        // 2. Cliente: verifica credenciales de Ahorros O de Corriente
        TADs.Nodo actual = repositorioUsuarios.getInicio();
        while (actual != null) {
            CapaEntidades.Cliente c = (CapaEntidades.Cliente) actual.getDato();
            // Login de Cuenta Ahorros
            if (c.getUsuario().equals(id) && c.getContraseña().equals(pass) && c.isCuentaAhorrosActiva()) {
                return true;
            }
            // Login de Cuenta Corriente (credenciales independientes)
            if (c.getUsuarioCorriente() != null
                    && c.getUsuarioCorriente().equals(id)
                    && c.getContraseñaCorriente() != null
                    && c.getContraseñaCorriente().equals(pass)
                    && c.isCuentaCorrienteActiva()) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * origen: cédula del cliente que envía el dinero (la sesión activa)
     * destino: número de cuenta (ahorros o corriente) que recibe el dinero
     */
    public void transferenciaInterbancaria(String origen, String destino, double monto) throws Exception {
        Cliente clienteOrigen = repositorioUsuarios.buscarPorCedula(origen);
        Cliente clienteDestino = repositorioUsuarios.buscarPorCuenta(destino);

        if (clienteOrigen == null) throw new Exception("Error: Cliente de origen no encontrado.");
        if (clienteDestino == null) throw new Exception("Error: Cuenta de destino no encontrada.");

        if (clienteOrigen.getTransaccionesHoy() >= 5) {
            throw new Exception("Transferencia Rechazada: se alcanzó el número máximo de transacciones diarias.");
        }
        if (monto > clienteOrigen.getLimiteDiarioTransaccion()) {
            throw new Exception("Transferencia Rechazada: el monto supera el límite diario permitido.");
        }

        clienteOrigen.retirarAhorros(monto);
        clienteDestino.depositarAhorros(monto);

        clienteOrigen.setTransaccionesHoy(clienteOrigen.getTransaccionesHoy() + 1);
    }

    /**
     * Variante para transferencias que SALEN de una Cuenta Corriente.
     * La original (transferenciaInterbancaria) siempre afecta saldoAhorros;
     * esta sí descuenta de saldoCorriente y deposita en el producto correcto
     * del destinatario (Corriente si la cuenta destino es su cuenta corriente,
     * Ahorros en caso contrario).
     */
    public void transferenciaDesdeCorriente(String origenCedula, String destino, double monto) throws Exception {
        Cliente clienteOrigen = repositorioUsuarios.buscarPorCedula(origenCedula);
        Cliente clienteDestino = repositorioUsuarios.buscarPorCuenta(destino);

        if (clienteOrigen == null) throw new Exception("Error: Cliente de origen no encontrado.");
        if (clienteDestino == null) throw new Exception("Error: Cuenta de destino no encontrada.");

        if (clienteOrigen.getTransaccionesHoy() >= 5) {
            throw new Exception("Transferencia Rechazada: se alcanzó el número máximo de transacciones diarias.");
        }
        if (monto > clienteOrigen.getLimiteDiarioTransaccion()) {
            throw new Exception("Transferencia Rechazada: el monto supera el límite diario permitido.");
        }

        clienteOrigen.retirarCorriente(monto);

        if (destino.equals(clienteDestino.getNumeroCuentaCorriente())) {
            clienteDestino.depositarCorriente(monto);
        } else {
            clienteDestino.depositarAhorros(monto);
        }

        clienteOrigen.setTransaccionesHoy(clienteOrigen.getTransaccionesHoy() + 1);
    }

    public void deshacerUltimaAccion(String cedula) throws Exception {
        Cliente c = repositorioUsuarios.buscarPorCedula(cedula);
        if (c == null) throw new Exception("Error: Cliente no encontrado.");

        Object registro = c.getHistorial().pop();
        if (registro == null) {
            throw new Exception("No hay transacciones para deshacer.");
        }

        // El registro tiene formato "TIPO:delta", por ejemplo "AHORROS:-100.0"
        String[] partes = ((String) registro).split(":");
        String tipo = partes[0];
        double delta = Double.parseDouble(partes[1]);

        if (tipo.equals("AHORROS")) {
            c.setSaldoAhorros(c.getSaldoAhorros() + delta);
        } else if (tipo.equals("CORRIENTE")) {
            c.setSaldoCorriente(c.getSaldoCorriente() + delta);
        }
    }

    public double simularInteresCompuesto(double saldo, double tasa, int meses) {
        return saldo * Math.pow((1 + tasa), meses);
    }

    // ===================== IServicioAdministrativo =====================

    @Override
    public void registrarNuevoCliente(Cliente c) throws Exception {
        // Validar edad mínima (18 años)
        if (c.getEdad() < 18) {
            throw new Exception("El cliente debe ser mayor de edad (mínimo 18 años). "
                    + "Edad ingresada: " + c.getEdad() + " años.");
        }
        // Validar que la cédula no exista ya
        if (repositorioUsuarios.buscarPorCedula(c.getCedula()) != null) {
            throw new Exception("Error: ya existe un cliente registrado con esa cédula.");
        }
        // Validar que los usuarios no estén duplicados
        if (obtenerClientePorUsuario(c.getUsuario()) != null) {
            throw new Exception("El usuario de Ahorros \"" + c.getUsuario() + "\" ya está en uso.");
        }
        if (c.getUsuarioCorriente() != null && obtenerClientePorUsuario(c.getUsuarioCorriente()) != null) {
            throw new Exception("El usuario de Corriente \"" + c.getUsuarioCorriente() + "\" ya está en uso.");
        }
        repositorioUsuarios.insertar(c);
    }

    /**
     * Versión extendida de registro que valida además que las dos contraseñas
     * coincidan (se usa desde Registro_Clientes pasando ambos campos).
     */
    public void registrarNuevoClienteConConfirmacion(Cliente c, String confirmacionContrasena) throws Exception {
        if (!c.getContraseña().equals(confirmacionContrasena)) {
            throw new Exception("Las contraseñas no coinciden. Por favor, verifíquelas.");
        }
        registrarNuevoCliente(c);
    }

    @Override
    public void gestionarLimiteSeguridad(String cedula, double nuevoMonto) throws Exception {
        if (nuevoMonto < 0) throw new Exception("Error: el límite debe ser un monto positivo.");
        Cliente c = repositorioUsuarios.buscarPorCedula(cedula);
        if (c == null) throw new Exception("Error: no se encontró ningún cliente con esa cédula.");
        c.setLimiteDiarioTransaccion(nuevoMonto);
    }

    /**
     * Como ColaCircularCheques solo permite acceso FIFO (encolar/desencolar),
     * para "marcar" un cheque que no está al frente, se recorre toda la cola
     * una vez, actualizando el que coincida y reinsertando todos en el mismo orden.
     */
    @Override
    public void marcarChequeComoRebotado(String idCheque) throws Exception {
        int totalActual = colaRevision.getTamaño();
        boolean encontrado = false;

        for (int i = 0; i < totalActual; i++) {
            Cheque ch = colaRevision.desencolar();
            if (ch.getNumeroCheque().equals(idCheque)) {
                ch.setEstado("Rebotado");
                encontrado = true;
            }
            colaRevision.encolar(ch);
        }

        if (!encontrado) {
            throw new Exception("Error: no se encontró un cheque con el número " + idCheque + " en la cola de revisión.");
        }
    }

    @Override
    public void precargarClientesPrueba() {
        try {
            // --- CLIENTES DEL MISMO BANCO ---
            Cliente vanessa = new Cliente("1722222222", "Vanessa", "Ingenieria", "vanessa@epn.edu.ec", "0999999991", 20, "vanessa", "vane123", "AHO-100001", "COR-100001");
            vanessa.configurarCredencialesCorriente("vanessa_c", "vane123c");

            Cliente emily = new Cliente("1733333333", "Emily", "Ingenieria", "emily@epn.edu.ec", "0999999992", 19, "emily", "emi123", "AHO-100002", "COR-100002");
            emily.configurarCredencialesCorriente("emily_c", "emi123c");

            registrarNuevoCliente(vanessa);
            registrarNuevoCliente(emily);

            // --- SIMULACIÓN DE CLIENTES DE OTROS BANCOS ---
            // Cuentas simuladas de Banco Pichincha (Prefijo PICH)
            Cliente carlosOtrosBancos = new Cliente("1744444444", "Carlos (Pichincha)", "Perez", "carlos@pichincha.com", "0999999993", 25, "carlos_pich", "pich123", "PICH-888001", "PICH-999001");

            // Cuentas simuladas de Produbanco (Prefijo PROD)
            Cliente mariaOtrosBancos = new Cliente("1755555555", "Maria (Produbanco)", "Gomez", "maria@produbanco.com", "0999999994", 30, "maria_prod", "prod123", "PROD-777002", "PROD-666002");

            registrarNuevoCliente(carlosOtrosBancos);
            registrarNuevoCliente(mariaOtrosBancos);

            System.out.println("¡Precarga de datos exitosa! Incluye cuentas locales y de otros bancos.");

        } catch (Exception e) {
            System.out.println("Error al precargar los datos de prueba: " + e.getMessage());
        }
    }

    public ListaDoble getRepositorioUsuarios() {
        return repositorioUsuarios;
    }

    public ColaCircularCheques getColaRevision() {
        return colaRevision;
    }
    
    // Nuevo método para obtener el cliente tras el inicio de sesión exitoso
    public CapaEntidades.Cliente obtenerClientePorUsuario(String usuario) {
        TADs.Nodo actual = repositorioUsuarios.getInicio();
        while (actual != null) {
            CapaEntidades.Cliente c = (CapaEntidades.Cliente) actual.getDato();
            if (c.getUsuario().equals(usuario)) return c;
            if (usuario.equals(c.getUsuarioCorriente())) return c;
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Dado el usuario devuelve "AHORROS", "CORRIENTE" o "AMBAS" según qué
     * prefijo tiene su cuenta de ahorros. Los clientes registrados por
     * Registro_Clientes tienen siempre ambas; en el futuro se podría usar
     * para cuentas de un solo tipo.
     * El campo "tipoCuentaAsociada" en Cliente (si existiese) determinaría
     * esto; por ahora la lógica se basa en si la cuenta de ahorros comienza
     * con "AHO-" y la corriente con "COR-".
     */
    /**
     * Determina exactamente qué cuenta corresponde al usuario que inició sesión.
     * Cada usuario está asociado a UNA sola cuenta: el usuario de Persona → Ahorros,
     * el usuarioCorriente → Corriente. Nunca devuelve "AMBAS".
     */
    public String getTipoCuentaAsociada(String usuario) {
        TADs.Nodo actual = repositorioUsuarios.getInicio();
        while (actual != null) {
            CapaEntidades.Cliente c = (CapaEntidades.Cliente) actual.getDato();
            if (c.getUsuario().equals(usuario)) return "AHORROS";
            if (usuario.equals(c.getUsuarioCorriente())) return "CORRIENTE";
            actual = actual.siguiente;
        }
        return null;
    }
  
    // Busqueda de cédula por lista doble
    public CapaEntidades.Cliente obtenerClientePorCedula(String cedula) {
        return repositorioUsuarios.buscarPorCedula(cedula);
    }

    // ======================= Solicitudes hacia Administración =======================

    // ======================= Generación de credenciales =======================

    /**
     * Genera un nombre de usuario único (no elegido por ningún otro cliente,
     * ni de Ahorros ni de Corriente) a partir del nombre y apellido dados.
     */
    public String generarUsuarioUnico(String nombre, String apellido) {
        String base = ((nombre == null ? "" : nombre) + (apellido == null ? "" : apellido))
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "");
        if (base.isEmpty()) {
            base = "cliente";
        }
        java.util.Random rnd = new java.util.Random();
        String candidato;
        do {
            candidato = base + (rnd.nextInt(9000) + 1000); // 4 dígitos aleatorios
        } while (obtenerClientePorUsuario(candidato) != null);
        return candidato;
    }

    /**
     * Genera una contraseña aleatoria de 8 caracteres (letras y números,
     * evitando caracteres ambiguos como 0/O o 1/l).
     */
    public String generarPasswordSegura() {
        String caracteres = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        java.util.Random rnd = new java.util.Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(caracteres.charAt(rnd.nextInt(caracteres.length())));
        }
        return sb.toString();
    }

    /**
     * Registra una nueva solicitud (límite, apertura, general) para que el
     * área administrativa la revise y la acepte o rechace.
     */
    public void emitirSolicitud(Solicitud s) {
        solicitudes.add(s);
    }

    /**
     * Lista completa de solicitudes (para que Area_Admin las pinte en su tabla).
     */
    public java.util.List<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    /**
     * Resuelve una solicitud pendiente. Si se acepta y es de tipo "LIMITE",
     * aplica automáticamente el nuevo límite diario al cliente.
     */
    public void resolverSolicitud(int idSolicitud, boolean aceptar) throws Exception {
        for (Solicitud s : solicitudes) {
            if (s.getId() == idSolicitud) {
                if (aceptar) {
                    s.setEstado("Aceptada");
                    registrarAuditoria("SOLICITUD ACEPTADA", "#" + idSolicitud + " | " + s.getTipo() + " | " + s.getNombreCliente());
                    if (s.getTipo().equals("LIMITE")) {
                        gestionarLimiteSeguridad(s.getCedulaCliente(), s.getMontoSolicitado());
                    } else if (s.getTipo().equals("REGISTRO") && s.getClienteEnEspera() != null) {
                        // Al aceptar un registro, se crea la cuenta en el sistema
                        repositorioUsuarios.insertar(s.getClienteEnEspera());
                        registrarAuditoria("CUENTA CREADA",
                                "Cliente " + s.getNombreCliente() + " | " + s.getClienteEnEspera().getNumeroCuentaAhorros());
                    } else if (s.getTipo().equals("APERTURA")) {
                        // Al aceptar la apertura, se activa la cuenta solicitada.
                        // Las credenciales ya fueron generadas y asignadas al
                        // Cliente desde AperturarCuenta; aquí solo se habilita.
                        // El tipo de cuenta se identifica desde el detalle
                        // (texto: "Solicitud de apertura: Cuenta X | ...").
                        Cliente cli = repositorioUsuarios.buscarPorCedula(s.getCedulaCliente());
                        if (cli != null) {
                            String detalle = s.getDetalle() != null ? s.getDetalle() : "";
                            if (detalle.contains("Cuenta Corriente")) {
                                cli.setCuentaCorrienteActiva(true);
                                registrarAuditoria("CUENTA APERTURADA", "Cliente " + s.getNombreCliente() + " | Cuenta Corriente");
                            } else {
                                cli.setCuentaAhorrosActiva(true);
                                registrarAuditoria("CUENTA APERTURADA", "Cliente " + s.getNombreCliente() + " | Cuenta Ahorros");
                            }
                        }
                    }
                } else {
                    s.setEstado("Rechazada");
                    registrarAuditoria("SOLICITUD RECHAZADA", "#" + idSolicitud + " | " + s.getTipo() + " | " + s.getNombreCliente());
                }
                return;
            }
        }
        throw new Exception("Error: no se encontró la solicitud #" + idSolicitud);
    }

    /**
     * Registra una solicitud de NUEVO REGISTRO de cliente.
     * El cliente NO se crea todavía — queda en espera hasta qSue el admin acepte.
     */
    public void solicitarNuevoRegistro(CapaEntidades.Cliente clientePendiente,
            String detalleAdicional) throws Exception {
        // Validar que cédula y usuario no existan ya
        if (repositorioUsuarios.buscarPorCedula(clientePendiente.getCedula()) != null) {
            throw new Exception("Ya existe una cuenta con esa cédula.");
        }
        if (obtenerClientePorUsuario(clientePendiente.getUsuario()) != null) {
            throw new Exception("El usuario '" + clientePendiente.getUsuario() + "' ya está en uso.");
        }

        Solicitud sol = new Solicitud(
                "REGISTRO",
                clientePendiente.getCedula(),
                clientePendiente.getNombre() + " " + clientePendiente.getApellido(),
                "PENDIENTE DE APROBACIÓN",
                0,
                "Solicitud de registro nuevo | " + detalleAdicional
        );
        sol.setClienteEnEspera(clientePendiente);
        solicitudes.add(sol);
    }

    // ======================= Cheques (Cuenta Corriente) =======================

    // ======================= Auditoría =======================

    /** Registra una acción del administrador en el log de auditoría. */
    public void registrarAuditoria(String accion, String detalle) {
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        logAuditoria.add(new String[]{fecha, accion, detalle});
    }

    public java.util.List<String[]> getLogAuditoria() {
        return logAuditoria;
    }

    public void limpiarLogAuditoria() {
        logAuditoria.clear();
    }

    // ======================= Cheques =======================

    /**
     * Genera un número de cheque GLOBAL y lo encola en revisión.
     * diasVencimiento: 0=mismo día, 1=siguiente, 5, 10, 15, 30, 90.
     */
    public Cheque registrarCheque(Cliente cliente, double monto,
            String beneficiario, int diasVencimiento) throws Exception {
        if (monto <= 0) throw new Exception("El monto del cheque debe ser mayor a 0.");
        contadorGlobalCheques++;
        String numero = "CHQ-" + String.format("%06d", contadorGlobalCheques);
        Cheque nuevoCheque = new Cheque(numero, monto, cliente.getCedula(),
                cliente.getNumeroCuentaCorriente(), beneficiario, diasVencimiento);
        colaRevision.encolar(nuevoCheque);
        return nuevoCheque;
    }

    // Mantener compatibilidad con llamadas sin días
    public Cheque registrarCheque(Cliente cliente, double monto, String beneficiario) throws Exception {
        return registrarCheque(cliente, monto, beneficiario, 0);
    }

    public String previewNumeroCheque() {
        return "CHQ-" + String.format("%06d", contadorGlobalCheques + 1);
    }

    // ======================= Bloqueo de cuentas =======================

    // ======================= Bloqueo de cuentas (independiente) =======================

    /** Bloquea una cuenta específica. tipoCuenta = "AHORROS" o "CORRIENTE". */
    public void bloquearCuenta(String cedula, String tipoCuenta) throws Exception {
        Cliente c = repositorioUsuarios.buscarPorCedula(cedula);
        if (c == null) throw new Exception("Cliente no encontrado.");
        if (tipoCuenta.equals("AHORROS"))    c.setBloqueadoAhorros(true);
        else if (tipoCuenta.equals("CORRIENTE")) c.setBloqueadoCorriente(true);
        else throw new Exception("Tipo de cuenta inválido: " + tipoCuenta);
        registrarAuditoria("BLOQUEO", "Cuenta " + tipoCuenta + " de " + c.getNombre() + " " + c.getApellido() + " | Cédula: " + cedula);
    }

    /** Desbloquea una cuenta específica. tipoCuenta = "AHORROS" o "CORRIENTE". */
    public void desbloquearCuenta(String cedula, String tipoCuenta) throws Exception {
        Cliente c = repositorioUsuarios.buscarPorCedula(cedula);
        if (c == null) throw new Exception("Cliente no encontrado.");
        if (tipoCuenta.equals("AHORROS"))    c.setBloqueadoAhorros(false);
        else if (tipoCuenta.equals("CORRIENTE")) c.setBloqueadoCorriente(false);
        else throw new Exception("Tipo de cuenta inválido: " + tipoCuenta);
        registrarAuditoria("DESBLOQUEO", "Cuenta " + tipoCuenta + " de " + c.getNombre() + " " + c.getApellido() + " | Cédula: " + cedula);
    }

    public boolean esCuentaBloqueada(String cedula, String tipoCuenta) {
        Cliente c = repositorioUsuarios.buscarPorCedula(cedula);
        if (c == null) return false;
        if (tipoCuenta.equals("AHORROS"))    return c.isBloqueadoAhorros();
        if (tipoCuenta.equals("CORRIENTE"))  return c.isBloqueadoCorriente();
        return false;
    }

    /** Nombre completo del administrador único del sistema. */
    public String getNombreAdmin() {
        return adminUnico.getUsuario(); // "admin"
    }

    // ======================= Clientes / Auditoría =======================

    /**
     * Lista solo los clientes INTERNOS del banco (excluye los clientes
     * precargados de otros bancos como Pichincha, Produbanco, etc.).
     */
    public java.util.List<Cliente> listarTodosClientes() {
        java.util.List<Cliente> lista = new java.util.ArrayList<>();
        TADs.Nodo actual = repositorioUsuarios.getInicio();
        while (actual != null) {
            Cliente c = (Cliente) actual.getDato();
            if (c.esClienteInterno()) {
                lista.add(c);
            }
            actual = actual.siguiente;
        }
        return lista;
    }

    public int contarClientes() {
        return listarTodosClientes().size();
    }

    // ======================= Transferencias con comisión =======================

    /** Comisión fija para transferencias a otros bancos ($0.45). */
    public static final double COMISION_INTERBANCARIA = 0.45;

    /**
     * Transferencia INTERNA (mismo banco, cuenta Ahorros como origen).
     * El origen se identifica por cédula; el destino por número de cuenta.
     */
    public void transferenciaInternaDesdeAhorros(String cedulaOrigen, String cuentaDestino,
            double monto) throws Exception {
        Cliente origen = repositorioUsuarios.buscarPorCedula(cedulaOrigen);
        Cliente destino = repositorioUsuarios.buscarPorCuenta(cuentaDestino);

        if (origen == null) throw new Exception("Cliente de origen no encontrado.");
        if (destino == null) throw new Exception("Cuenta destino no encontrada en el banco.");
        if (!destino.esClienteInterno())
            throw new Exception("Las transferencias internas solo aplican a cuentas de este banco.");
        if (origen.getTransaccionesHoy() >= 5)
            throw new Exception("Límite diario de transferencias alcanzado (máx. 5).");
        if (monto > origen.getLimiteDiarioTransaccion())
            throw new Exception("El monto supera el límite diario de $ " + origen.getLimiteDiarioTransaccion());

        origen.retirarAhorros(monto);
        if (cuentaDestino.equals(destino.getNumeroCuentaCorriente())) {
            destino.depositarCorriente(monto);
        } else {
            destino.depositarAhorros(monto);
        }
        origen.setTransaccionesHoy(origen.getTransaccionesHoy() + 1);
    }

    /**
     * Transferencia EXTERNA (otro banco, cuenta Ahorros como origen).
     * Aplica comisión de $0.45 adicional al monto.
     */
    public void transferenciaExternaDesdeAhorros(String cedulaOrigen, String cuentaDestino,
            double monto) throws Exception {
        Cliente origen = repositorioUsuarios.buscarPorCedula(cedulaOrigen);
        Cliente destino = repositorioUsuarios.buscarPorCuenta(cuentaDestino);

        if (origen == null) throw new Exception("Cliente de origen no encontrado.");
        if (destino == null) throw new Exception("Cuenta destino no encontrada.");
        if (origen.getTransaccionesHoy() >= 5)
            throw new Exception("Límite diario de transferencias alcanzado (máx. 5).");

        double totalADescontar = monto + COMISION_INTERBANCARIA;
        if (totalADescontar > origen.getSaldoAhorros())
            throw new Exception("Fondos insuficientes. Necesita $ " + totalADescontar
                    + " (monto + comisión $" + COMISION_INTERBANCARIA + ").");
        if (monto > origen.getLimiteDiarioTransaccion())
            throw new Exception("El monto supera el límite diario de $ " + origen.getLimiteDiarioTransaccion());

        origen.retirarAhorros(totalADescontar); // se descuenta monto + comisión
        destino.depositarAhorros(monto);         // el destinatario recibe solo el monto
        origen.setTransaccionesHoy(origen.getTransaccionesHoy() + 1);
    }

    /**
     * Transferencia INTERNA (mismo banco, cuenta Corriente como origen).
     */
    public void transferenciaInternaDesdeCorreinte(String cedulaOrigen, String cuentaDestino,
            double monto) throws Exception {
        Cliente origen = repositorioUsuarios.buscarPorCedula(cedulaOrigen);
        Cliente destino = repositorioUsuarios.buscarPorCuenta(cuentaDestino);

        if (origen == null) throw new Exception("Cliente de origen no encontrado.");
        if (destino == null) throw new Exception("Cuenta destino no encontrada en el banco.");
        if (!destino.esClienteInterno())
            throw new Exception("Las transferencias internas solo aplican a cuentas de este banco.");
        if (origen.getTransaccionesHoy() >= 5)
            throw new Exception("Límite diario de transferencias alcanzado (máx. 5).");
        if (monto > origen.getLimiteDiarioTransaccion())
            throw new Exception("El monto supera el límite diario de $ " + origen.getLimiteDiarioTransaccion());

        origen.retirarCorriente(monto);
        if (cuentaDestino.equals(destino.getNumeroCuentaCorriente())) {
            destino.depositarCorriente(monto);
        } else {
            destino.depositarAhorros(monto);
        }
        origen.setTransaccionesHoy(origen.getTransaccionesHoy() + 1);
    }

    /**
     * Transferencia EXTERNA (otro banco, cuenta Corriente como origen).
     * Aplica comisión de $0.45 adicional al monto.
     */
    public void transferenciaExternaDesdeCorreinte(String cedulaOrigen, String cuentaDestino,
            double monto) throws Exception {
        Cliente origen = repositorioUsuarios.buscarPorCedula(cedulaOrigen);
        Cliente destino = repositorioUsuarios.buscarPorCuenta(cuentaDestino);

        if (origen == null) throw new Exception("Cliente de origen no encontrado.");
        if (destino == null) throw new Exception("Cuenta destino no encontrada.");
        if (origen.getTransaccionesHoy() >= 5)
            throw new Exception("Límite diario de transferencias alcanzado (máx. 5).");

        double totalADescontar = monto + COMISION_INTERBANCARIA;
        if (totalADescontar > origen.getSaldoCorriente())
            throw new Exception("Fondos insuficientes. Necesita $ " + totalADescontar
                    + " (monto + comisión $" + COMISION_INTERBANCARIA + ").");
        if (monto > origen.getLimiteDiarioTransaccion())
            throw new Exception("El monto supera el límite diario de $ " + origen.getLimiteDiarioTransaccion());

        origen.retirarCorriente(totalADescontar);
        destino.depositarAhorros(monto);
        origen.setTransaccionesHoy(origen.getTransaccionesHoy() + 1);
    }

    // ======================= Cheques =======================

    public java.util.List<Cheque> listarChequesPendientes() {
        return colaRevision.listarTodos();
    }

    public java.util.List<Cheque> getHistorialCheques() {
        return historialCheques;
    }

    public Cheque cobrarCheque(String numeroCheque) throws Exception {
        Cheque cheque = colaRevision.extraerPorNumero(numeroCheque);
        if (cheque == null) throw new Exception("Cheque " + numeroCheque + " no encontrado en revisión.");

        // Verificar vencimiento antes de cobrar
        if (cheque.estaVencido()) {
            cheque.setEstado("Rechazado: cheque vencido el " + cheque.getFechaVencimientoFormateada());
            historialCheques.add(cheque);
            registrarAuditoria("CHEQUE VENCIDO",
                    numeroCheque + " | $" + cheque.getMonto() + " | " + cheque.getBeneficiario());
            return cheque;
        }

        Cliente titular = repositorioUsuarios.buscarPorCedula(cheque.getCedulaCliente());
        if (titular == null) {
            cheque.setEstado("Rechazado: titular no encontrado");
            registrarAuditoria("CHEQUE RECHAZADO", numeroCheque + " | Titular no encontrado");
        } else if (titular.getSaldoCorriente() >= cheque.getMonto()) {
            titular.retirarCorriente(cheque.getMonto());
            cheque.setEstado("Cobrado");
            registrarAuditoria("CHEQUE COBRADO",
                    numeroCheque + " | $" + String.format("%.2f", cheque.getMonto())
                    + " | " + cheque.getBeneficiario()
                    + " | Titular: " + titular.getNombre());
        } else {
            cheque.setEstado("Rechazado por fondos insuficientes");
            registrarAuditoria("CHEQUE RECHAZADO",
                    numeroCheque + " | Fondos insuficientes | Titular: " + titular.getNombre());
        }
        historialCheques.add(cheque);
        return cheque;
    }

    public Cheque rechazarChequeManual(String numeroCheque, String motivo) throws Exception {
        Cheque cheque = colaRevision.extraerPorNumero(numeroCheque);
        if (cheque == null) throw new Exception("Cheque " + numeroCheque + " no encontrado.");
        cheque.setEstado("Rechazado: " + motivo);
        historialCheques.add(cheque);
        registrarAuditoria("CHEQUE RECHAZADO MANUAL",
                numeroCheque + " | Motivo: " + motivo);
        return cheque;
    }

    public java.util.List<Cheque> obtenerNotificacionesChequesPendientes(String cedula) {
        java.util.List<Cheque> resultado = new java.util.ArrayList<>();
        for (Cheque ch : historialCheques) {
            if (ch.getCedulaCliente() != null && ch.getCedulaCliente().equals(cedula)
                    && !ch.isNotificadoAlCliente() && ch.getEstado().startsWith("Rechazado")) {
                resultado.add(ch);
            }
        }
        return resultado;
    }
    
   // -------------- Controlar Usuarios -----------------------
    public boolean existeNombreUsuario(String usuario) {
        if (usuario == null) return false;
        
        // Comprobar con el administrador
        if (adminUnico != null && adminUnico.getUsuario().equalsIgnoreCase(usuario)) {
            return true;
        }

        // Comprobar contra todos los clientes registrados
        TADs.Nodo actual = repositorioUsuarios.getInicio();
        while (actual != null) {
            Cliente c = (Cliente) actual.getDato();
            if (c.getUsuario() != null && c.getUsuario().equalsIgnoreCase(usuario)) {
                return true;
            }
            if (c.getUsuarioCorriente() != null && c.getUsuarioCorriente().equalsIgnoreCase(usuario)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
}
