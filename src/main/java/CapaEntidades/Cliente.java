package CapaEntidades;

import TADs.PilaTransacciones;

public class Cliente extends Persona {
    private String numeroCuentaAhorros;
    private String numeroCuentaCorriente;
    private double saldoAhorros;
    private double saldoAhorroFlexible;
    private double saldoCorriente;
    private int transaccionesHoy;
    private double limiteDiarioTransaccion;
    private PilaTransacciones historial;
    private int contadorCheques;
    private boolean bloqueadoAhorros;
    private boolean bloqueadoCorriente;
    // Indican si el cliente REALMENTE dispone de cada cuenta (independiente de si
    // la tarjeta está bloqueada). Por defecto ambas activas para no afectar a
    // clientes ya existentes; Registro_Clientes ajusta esto según lo que el
    // usuario elige al registrarse, y AperturarCuenta/BancoServicios lo activa
    // cuando el admin aprueba la apertura de la segunda cuenta.
    private boolean cuentaAhorrosActiva = true;
    private boolean cuentaCorrienteActiva = true;
    // Credenciales independientes para la Cuenta Corriente.
    // El usuario/contraseña heredado de Persona corresponde a la Cuenta Ahorros.
    private String usuarioCorriente;
    private String contraseñaCorriente;

    // Historial persistente: cada entrada es {fecha, tipo, ctaOrigen, ctaDestino, beneficiario, monto, detalle}
    private final java.util.List<String[]> historialMovimientos = new java.util.ArrayList<>();

    public Cliente(String cedula, String nombre, String apellido, String correo,
            String telefono, int edad, String usuario, String contraseña,
            String numeroCuentaAhorros, String numeroCuentaCorriente) {
        super(cedula, nombre, apellido, correo, telefono, edad, usuario, contraseña);
        this.numeroCuentaAhorros = numeroCuentaAhorros;
        this.numeroCuentaCorriente = numeroCuentaCorriente;
        this.saldoAhorros = 50.0;
        this.saldoAhorroFlexible = 0.0;
        this.saldoCorriente = 0.0;
        this.transaccionesHoy = 0;
        this.limiteDiarioTransaccion = 500.0;
        this.historial = new PilaTransacciones();
        this.contadorCheques = 0;
        this.bloqueadoAhorros = false;
        this.bloqueadoCorriente = false;
    }

    /**
     * Asigna las credenciales de la Cuenta Corriente (usuario y contraseña distintos
     * a los de Ahorros). Se llama desde Registro_Clientes o desde precargar.
     */
    public void configurarCredencialesCorriente(String usuarioCorriente, String contraseñaCorriente) {
        this.usuarioCorriente = usuarioCorriente;
        this.contraseñaCorriente = contraseñaCorriente;
    }

    public String getUsuarioCorriente()    { return usuarioCorriente; }
    public String getContraseñaCorriente() { return contraseñaCorriente; }

    /**
     * Reasigna las credenciales de la Cuenta Ahorros (heredadas de Persona).
     * Se usa desde AperturarCuenta cuando un cliente que ya tenía solo Cuenta
     * Corriente solicita y se le aprueba también la Cuenta de Ahorros.
     */
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    // ============ Disponibilidad real de cada cuenta ============

    public boolean isCuentaAhorrosActiva()   { return cuentaAhorrosActiva; }
    public boolean isCuentaCorrienteActiva() { return cuentaCorrienteActiva; }

    public void setCuentaAhorrosActiva(boolean v)   { this.cuentaAhorrosActiva = v; }
    public void setCuentaCorrienteActiva(boolean v) { this.cuentaCorrienteActiva = v; }

    /** Un cliente es "interno" si sus cuentas usan los prefijos propios (AHO-/COR-). */
    public boolean esClienteInterno() {
        return numeroCuentaAhorros != null && numeroCuentaAhorros.startsWith("AHO-");
    }

    // ============ Operaciones de saldo ============

    public void depositarAhorros(double monto) throws Exception {
        if (monto <= 0) throw new Exception("El monto a depositar debe ser mayor a 0.");
        monto = Math.round(monto * 100.0) / 100.0; // 2 decimales
        this.saldoAhorros += monto;
        historial.push("AHORROS:" + (-monto));
    }

    public void retirarAhorros(double monto) throws Exception {
        if (monto <= 0) throw new Exception("El monto a retirar debe ser mayor a 0.");
        monto = Math.round(monto * 100.0) / 100.0; // 2 decimales
        if (monto > this.saldoAhorros) throw new Exception("Fondos insuficientes en la Cuenta de Ahorros.");
        this.saldoAhorros -= monto;
        historial.push("AHORROS:" + monto);
    }

    public void depositarCorriente(double monto) throws Exception {
        if (monto <= 0) throw new Exception("El monto a depositar debe ser mayor a 0.");
        monto = Math.round(monto * 100.0) / 100.0; // 2 decimales
        this.saldoCorriente += monto;
        historial.push("CORRIENTE:" + (-monto));
    }

    public void retirarCorriente(double monto) throws Exception {
        if (monto <= 0) throw new Exception("El monto a retirar debe ser mayor a 0.");
        monto = Math.round(monto * 100.0) / 100.0; // 2 decimales
        if (monto > this.saldoCorriente) throw new Exception("Fondos insuficientes en la Cuenta Corriente.");
        this.saldoCorriente -= monto;
        historial.push("CORRIENTE:" + monto);
    }

    public void resetearContadorDiario() { this.transaccionesHoy = 0; }

    public String generarNumeroCheque() {
        contadorCheques++;
        return "CHQ-" + numeroCuentaCorriente + "-" + String.format("%04d", contadorCheques);
    }

    // ============ Historial de movimientos ============

    /** Agrega un movimiento al historial persistente. */
    public void agregarMovimiento(String fecha, String tipo, String ctaOrigen,
            String ctaDestino, String beneficiario, String monto, String detalle) {
        historialMovimientos.add(new String[]{fecha, tipo, ctaOrigen, ctaDestino, beneficiario, monto, detalle});
    }

    public java.util.List<String[]> getHistorialMovimientos() {
        return historialMovimientos;
    }

    // ============ Bloqueo independiente por cuenta ============

    public boolean isBloqueadoAhorros()    { return bloqueadoAhorros; }
    public boolean isBloqueadoCorriente()  { return bloqueadoCorriente; }

    public void setBloqueadoAhorros(boolean b)   { this.bloqueadoAhorros = b; }
    public void setBloqueadoCorriente(boolean b) { this.bloqueadoCorriente = b; }

    // Compatibilidad (usado en Acceso_Cuentas para el check genérico)
    public boolean isBloqueado() { return bloqueadoAhorros && bloqueadoCorriente; }

    // ============ Getters / Setters ============

    public String getNumeroCuentaAhorros()  { return numeroCuentaAhorros; }
    public void setNumeroCuentaAhorros(String v) { this.numeroCuentaAhorros = v; }

    public String getNumeroCuentaCorriente()  { return numeroCuentaCorriente; }
    public void setNumeroCuentaCorriente(String v) { this.numeroCuentaCorriente = v; }

    public double getSaldoAhorros()  { return saldoAhorros; }
    public void setSaldoAhorros(double v) { this.saldoAhorros = v; }

    public double getSaldoAhorroFlexible()  { return saldoAhorroFlexible; }
    public void setSaldoAhorroFlexible(double v) { this.saldoAhorroFlexible = v; }

    public double getSaldoCorriente()  { return saldoCorriente; }
    public void setSaldoCorriente(double v) { this.saldoCorriente = v; }

    public int getTransaccionesHoy()  { return transaccionesHoy; }
    public void setTransaccionesHoy(int v) { this.transaccionesHoy = v; }

    public double getLimiteDiarioTransaccion() { return limiteDiarioTransaccion; }
    public void setLimiteDiarioTransaccion(double v) { this.limiteDiarioTransaccion = v; }

    public PilaTransacciones getHistorial() { return historial; }

    public String getNombre()  { return nombre; }
    public void setNombre(String v) { this.nombre = v; }

    public String getApellido()  { return apellido; }
    public void setApellido(String v) { this.apellido = v; }

    public int getEdad() { return edad;}
    public void setEdad(int edad) { this.edad = edad; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {  this.telefono = telefono; }
    
    
    
}
