package CapaEntidades;

public class Cheque {
    private String numeroCheque;
    private double monto;
    private String estado;
    private String cedulaCliente;
    private String cuentaCorriente;
    private String beneficiario;
    private java.util.Date fechaEmision;
    private java.util.Date fechaVencimiento; // fecha en que el cheque debe ser cobrado
    private boolean notificadoAlCliente;

    public Cheque(String numeroCheque, double monto) {
        this.numeroCheque = numeroCheque;
        this.monto = monto;
        this.estado = "Pendiente";
        this.fechaEmision = new java.util.Date();
        this.notificadoAlCliente = false;
    }

    public Cheque(String numeroCheque, double monto, String cedulaCliente,
            String cuentaCorriente, String beneficiario) {
        this(numeroCheque, monto);
        this.cedulaCliente = cedulaCliente;
        this.cuentaCorriente = cuentaCorriente;
        this.beneficiario = beneficiario;
    }

    /**
     * Constructor completo con fecha de vencimiento.
     * diasParaVencer: 0 = mismo día, 1 = siguiente día, 5, 10, 15, 30, 90...
     */
    public Cheque(String numeroCheque, double monto, String cedulaCliente,
            String cuentaCorriente, String beneficiario, int diasParaVencer) {
        this(numeroCheque, monto, cedulaCliente, cuentaCorriente, beneficiario);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, diasParaVencer);
        // Al final del día de vencimiento
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        this.fechaVencimiento = cal.getTime();
    }

    /** Devuelve true si el cheque ya pasó su fecha de vencimiento. */
    public boolean estaVencido() {
        if (fechaVencimiento == null) return false;
        return new java.util.Date().after(fechaVencimiento);
    }

    public String getFechaVencimientoFormateada() {
        if (fechaVencimiento == null) return "Sin vencimiento";
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(fechaVencimiento);
    }

    // Getters / Setters
    public String getNumeroCheque()   { return numeroCheque; }
    public double getMonto()           { return monto; }
    public String getEstado()          { return estado; }
    public void setEstado(String s)    { this.estado = s; }
    public String getCedulaCliente()   { return cedulaCliente; }
    public String getCuentaCorriente() { return cuentaCorriente; }
    public String getBeneficiario()    { return beneficiario; }
    public java.util.Date getFechaEmision()    { return fechaEmision; }
    public java.util.Date getFechaVencimiento(){ return fechaVencimiento; }
    public boolean isNotificadoAlCliente()     { return notificadoAlCliente; }
    public void setNotificadoAlCliente(boolean b) { this.notificadoAlCliente = b; }
}
