package CapaEntidades;

/**
 *
 * @author Vanessa y Emily
 */
public class Cliente extends Persona {
    private String numeroCuentaAhorros;
    private String numeroCuentaCorriente;
    private double saldoAhorros;
    private double saldoCorriente;
    
    // Con  el UML se define que  con estos atributos se maneja el programa 
    private int transaccionesHoy;
    private double limiteDiarioTransaccion;

    // Cambios realizados en el contructor para cuenta corriente y de ahorros
    public Cliente(String cedula, String nombre, String apellido, String correo,
            String telefono, int edad, String usuario, String contraseña, 
            String numeroCuentaAhorros, String numeroCuentaCorriente) {
        super(cedula, nombre, apellido, correo, telefono, edad, usuario, contraseña);
        this.numeroCuentaAhorros = numeroCuentaAhorros;
        this.numeroCuentaCorriente = numeroCuentaCorriente;
        this.saldoAhorros = 0.0;
        this.saldoCorriente = 0.0;
        this.transaccionesHoy = 0;
        this.limiteDiarioTransaccion = 500.0; // Límite inicial estándar según el documento técnico
    }

    // Logica de opracion de cuentas 
    
    public void depositarAhorros(double monto) throws Exception {
        if (monto <= 0) throw new Exception("El monto a depositar debe ser mayor a 0.");
        this.saldoAhorros += monto;
    }

    public void retirarAhorros(double monto) throws Exception {
        if (monto <= 0) throw new Exception("El monto a retirar debe ser mayor a 0.");
        if (monto > this.saldoAhorros) throw new Exception("Fondos insuficientes en la Cuenta de Ahorros.");
        this.saldoAhorros -= monto;
    }

    public void depositarCorriente(double monto) throws Exception {
        if (monto <= 0) throw new Exception("El monto a depositar debe ser mayor a 0.");
        this.saldoCorriente += monto;
    }

    public void retirarCorriente(double monto) throws Exception {
        if (monto <= 0) throw new Exception("El monto a retirar debe ser mayor a 0.");
        if (monto > this.saldoCorriente) throw new Exception("Fondos insuficientes en la Cuenta Corriente.");
        this.saldoCorriente -= monto;
    }

    // Getters y setter para obtener la info que ingrese el cliente
    public String getNumeroCuentaAhorros() { return numeroCuentaAhorros; }
    public void setNumeroCuentaAhorros(String numeroCuentaAhorros) { this.numeroCuentaAhorros = numeroCuentaAhorros; }
    public String getNumeroCuentaCorriente() { return numeroCuentaCorriente; }
    public void setNumeroCuentaCorriente(String numeroCuentaCorriente) { this.numeroCuentaCorriente = numeroCuentaCorriente; }
    public double getSaldoAhorros() { return saldoAhorros; }
    public void setSaldoAhorros(double saldoAhorros) { this.saldoAhorros = saldoAhorros; }
    public double getSaldoCorriente() { return saldoCorriente; }
    public void setSaldoCorriente(double saldoCorriente) { this.saldoCorriente = saldoCorriente; }
    public int getTransaccionesHoy() { return transaccionesHoy; }
    public void setTransaccionesHoy(int transaccionesHoy) { this.transaccionesHoy = transaccionesHoy; }
    public double getLimiteDiarioTransaccion() { return limiteDiarioTransaccion; }
    public void setLimiteDiarioTransaccion(double limiteDiarioTransaccion) { this.limiteDiarioTransaccion = limiteDiarioTransaccion; }
}