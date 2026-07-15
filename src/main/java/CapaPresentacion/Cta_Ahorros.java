/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package CapaPresentacion;

import CapaEntidades.Cliente;
import CapaEntidades.Solicitud;
import CapaNegocio.BancoServicios;
import javax.swing.JOptionPane;

/**
 *
 * @author Asus
 */
public class Cta_Ahorros extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Cta_Ahorros.class.getName());
    private javax.swing.table.DefaultTableModel modeloMovimientos;

    /**
     * Creates new form Cta_Corriente
     */
    private BancoServicios bancoServicios;
    private Cliente clienteActual;

    public Cta_Ahorros(BancoServicios bancoServicios, Cliente cliente) {
        this.bancoServicios = bancoServicios;
        this.clienteActual = cliente;
        initComponents();
        this.setLocationRelativeTo(null);
        inicializarTabla();
        inicializarTablaBolsillos();
        cargarDatosCliente();
        cargarSolicitudesCliente();
        actualizarEstadoTarjetas();
        // Agregar búsqueda de beneficiario por cédula al presionar Enter
        txtCuentaTransferenciaInternaAhorros.addActionListener(e -> buscarBeneficiarioInterno());
    }

    /** Busca por número de cuenta interna y rellena txtNombreBeneficiario. */
    private void buscarBeneficiarioInterno() {
        String cuenta = txtCuentaTransferenciaInternaAhorros.getText().trim();
        if (cuenta.isEmpty()) return;
        Cliente dest = bancoServicios.getRepositorioUsuarios().buscarPorCuenta(cuenta);
        if (dest == null || !dest.esClienteInterno()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró ninguna cuenta interna con ese número.\n"
                    + "Formatos válidos: AHO-100001, COR-100001",
                    "No encontrado", JOptionPane.WARNING_MESSAGE);
            txtNombreBeneficiario.setText("");
        } else {
            txtNombreBeneficiario.setText(dest.getNombre() + " " + dest.getApellido());
        }
    }

    /** Pinta TblSolicitudesClientes con las solicitudes de ESTE cliente. */
    private void cargarSolicitudesCliente() {
        javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) TblSolicitudesClientes.getModel();
        modelo.setColumnIdentifiers(new String[]{"#", "Tipo", "Detalle", "Estado"});
        modelo.setRowCount(0);
        for (CapaEntidades.Solicitud s : bancoServicios.getSolicitudes()) {
            if (s.getCedulaCliente().equals(clienteActual.getCedula())) {
                modelo.addRow(new Object[]{s.getId(), s.getTipo(), s.getDetalle(), s.getEstado()});
            }
        }
    }

    private void cargarDatosCliente() {
        jlbNumeroCuenta.setText(clienteActual.getNumeroCuentaAhorros());
        jlbNumeroCuenta3.setText(clienteActual.getNumeroCuentaAhorros());
        jlbNumeroCuenta4.setText(clienteActual.getNumeroCuentaAhorros());
        jlbNumeroCuenta5.setText(clienteActual.getNumeroCuentaAhorros());
        jlbSaldo.setText("$ " + clienteActual.getSaldoAhorros());
        jlbSaldoDisponibleAhorro.setText("$ " + clienteActual.getSaldoAhorros());
        jlbUsuarioAhorros.setText(clienteActual.getUsuario());
        jlbUsuarioAhorros1.setText(clienteActual.getUsuario());
        jlbMontoAhorroFlexible.setText("$ " + clienteActual.getSaldoAhorroFlexible());
        SaldoCuenta.setText("$ " + clienteActual.getSaldoAhorroFlexible());
        JlbTarjeta11.setText("Tarjeta Débito: **** " + ultimosDigitos(clienteActual.getNumeroCuentaAhorros()));
        JlbTarjeta12.setText("Tarjeta Virtual: **** " + ultimosDigitos(clienteActual.getNumeroCuentaAhorros() + "V"));
    }

    // Genera una "visual" de los últimos 4 caracteres de la cuenta, solo para mostrar en la tarjeta
    private String ultimosDigitos(String cuenta) {
        if (cuenta == null || cuenta.length() < 4) {
            return "0000";
        }
        return cuenta.substring(cuenta.length() - 4);
    }

    // Configurar tabla de movimientos
    private void inicializarTabla() {
        modeloMovimientos = new javax.swing.table.DefaultTableModel(
                new String[]{"Fecha", "Tipo", "Cta. Origen", "Cta. Destino", "Beneficiario", "Monto ($)", "Detalle"}, 0
        ) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        jTable1.setModel(modeloMovimientos);
        // Cargar historial previo que persiste en el objeto Cliente
        for (String[] mov : clienteActual.getHistorialMovimientos()) {
            modeloMovimientos.addRow(mov);
        }
    }

    private void registrarMovimiento(String tipo, String ctaOrigen, String ctaDestino,
            String beneficiario, double monto, String detalle) {
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());
        String montoStr = String.format("%.2f", monto);
        String[] fila = {fecha, tipo, ctaOrigen, ctaDestino, beneficiario, montoStr, detalle};
        modeloMovimientos.addRow(fila);
        clienteActual.agregarMovimiento(fecha, tipo, ctaOrigen, ctaDestino, beneficiario, montoStr, detalle);
        // Actualizar saldo en pantalla
        jlbSaldo.setText(String.format("$ %.2f", clienteActual.getSaldoAhorros()));
        jlbSaldoDisponibleAhorro.setText(String.format("$ %.2f", clienteActual.getSaldoAhorros()));
        actualizarContadorTransacciones();
    }

    private void actualizarContadorTransacciones() {
        int disponibles = 5 - clienteActual.getTransaccionesHoy();
        jlbTransaccionesDisponibles.setText( disponibles + "/5");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel18 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jlbNumeroCuenta1 = new javax.swing.JLabel();
        jlbSaldo1 = new javax.swing.JLabel();
        btnBuscarCliente1 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jlbNumeroCuenta6 = new javax.swing.JLabel();
        jlbSaldoDisponibleAhorro1 = new javax.swing.JLabel();
        jlbNumeroCuenta7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jlbUsuarioAhorros = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jlbNumeroCuenta = new javax.swing.JLabel();
        jlbSaldo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jlbTransaccionesDisponibles = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jlbNumeroCuenta4 = new javax.swing.JLabel();
        btnBuscarCliente = new javax.swing.JButton();
        txtNombreBeneficiario = new javax.swing.JTextField();
        txtMMontoTransferir = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDetalle = new javax.swing.JTextArea();
        btnTransferir = new javax.swing.JButton();
        txtCuentaTransferenciaInternaAhorros = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jlbNumeroCuenta5 = new javax.swing.JLabel();
        txtBeneficiarioTransferirOtrosBancos = new javax.swing.JTextField();
        txtMontoTransferirOtrosBancos = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDetalle1 = new javax.swing.JTextArea();
        btnTransferirOtrosBancos = new javax.swing.JButton();
        txtCuentaDestinoTransferirOtrosBancos = new javax.swing.JTextField();
        btnBuscarCliente2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane16 = new javax.swing.JTabbedPane();
        jPanel96 = new javax.swing.JPanel();
        jPanel101 = new javax.swing.JPanel();
        jLabel124 = new javax.swing.JLabel();
        jLabel154 = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        JlbTarjeta11 = new javax.swing.JLabel();
        JlbTarjeta12 = new javax.swing.JLabel();
        jLabel158 = new javax.swing.JLabel();
        jLabel159 = new javax.swing.JLabel();
        jLabel160 = new javax.swing.JLabel();
        jlbEstadoTarjeta1 = new javax.swing.JLabel();
        jlbEstadoTarjeta2 = new javax.swing.JLabel();
        btnBloquearTarjeta2 = new javax.swing.JButton();
        btnBloquearTarjeta1 = new javax.swing.JButton();
        jLabel165 = new javax.swing.JLabel();
        jPanel102 = new javax.swing.JPanel();
        jPanel103 = new javax.swing.JPanel();
        btnAperturar = new javax.swing.JButton();
        btnSolicitar = new javax.swing.JButton();
        jLabel161 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        btnLimite = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        TblSolicitudesClientes = new javax.swing.JTable();
        jLabel163 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel17 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jlbNumeroCuenta2 = new javax.swing.JLabel();
        jlbSaldoDisponibleAhorro = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jlbMontoAhorroFlexible = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtMontoAhorroFlexible = new javax.swing.JTextField();
        btnMontoAhorrar = new javax.swing.JButton();
        btnRetirarAhorros = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        btnSimularInteresAhorro = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jlbNumeroCuenta8 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jlbNumeroCuenta3 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        SaldoCuenta = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtTasaInteres = new javax.swing.JTextField();
        JCMeses = new javax.swing.JComboBox<>();
        btnCalcularInteres = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jlbInteresGanado = new javax.swing.JLabel();
        jlbSaldoFinal = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        txtNombreBolsillo = new javax.swing.JTextField();
        btnDepositarBolsillo = new javax.swing.JButton();
        btnRetirarBolsillo = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtMetaBolsillo = new javax.swing.JTextField();
        btnCrearBolsillo = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        TblBolsillos = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        btnCerrarSesionCliente = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jlbUsuarioAhorros1 = new javax.swing.JLabel();

        jLabel20.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel20.setText("Número de Cuenta:");

        jLabel21.setFont(new java.awt.Font("Roboto", 2, 18)); // NOI18N
        jLabel21.setText("Saldo: ");

        jlbNumeroCuenta1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jlbNumeroCuenta1.setText("AHO-xxxxx");

        jlbSaldo1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jlbSaldo1.setText("0.0$");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(58, 58, 58)
                        .addComponent(jlbSaldo1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbNumeroCuenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jlbNumeroCuenta1))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jlbSaldo1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        btnBuscarCliente1.setBackground(new java.awt.Color(0, 102, 102));
        btnBuscarCliente1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBuscarCliente1.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente1.setText("Buscar Cliente");
        btnBuscarCliente1.addActionListener(this::btnBuscarCliente1ActionPerformed);

        jLabel30.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel30.setText("Número de Cuenta:");

        jLabel31.setFont(new java.awt.Font("Roboto", 2, 18)); // NOI18N
        jLabel31.setText("Saldo Disponible: ");

        jlbNumeroCuenta6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jlbSaldoDisponibleAhorro1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jlbNumeroCuenta7, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbNumeroCuenta6, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlbSaldoDisponibleAhorro1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jlbNumeroCuenta6)
                    .addComponent(jlbNumeroCuenta7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jlbSaldoDisponibleAhorro1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/LOGO.png"))); // NOI18N
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 320, -1));

        jLabel2.setFont(new java.awt.Font("Microsoft New Tai Lue", 3, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("BIENVENIDO !!");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 60, 360, -1));

        jlbUsuarioAhorros.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N
        jlbUsuarioAhorros.setForeground(new java.awt.Color(255, 255, 255));
        jlbUsuarioAhorros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(jlbUsuarioAhorros, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 130, 203, 52));

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(0, 102, 102));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setName(""); // NOI18N

        jPanel3.setBackground(new java.awt.Color(18, 52, 83));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Movimientos");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Cuenta destino", "Beneficiario", "Tipo", "Monto", "Saldo Resultante", "Detalle"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setDoubleBuffered(true);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 687, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel4.setText("Número de Cuenta:");

        jLabel5.setFont(new java.awt.Font("Roboto", 2, 18)); // NOI18N
        jLabel5.setText("Saldo: ");

        jlbNumeroCuenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jlbSaldo.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(58, 58, 58)
                        .addComponent(jlbSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jlbNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jlbSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel8.setText("Detalles Cuenta");

        jLabel37.setText("Transferencias Disponibles");

        jlbTransaccionesDisponibles.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jlbTransaccionesDisponibles.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jlbTransaccionesDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jlbTransaccionesDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", jPanel3);

        jPanel4.setBackground(new java.awt.Color(18, 52, 83));

        jTabbedPane3.setBackground(new java.awt.Color(18, 52, 83));
        jTabbedPane3.setForeground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel7.setText("Número de cuenta de Destino");

        jLabel9.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel9.setText("Nombre Beneficiario");

        jLabel10.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel10.setText("Monto a Transferir($)");

        jLabel11.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel11.setText("Concepto o Detalle");

        jLabel6.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel6.setText("Cuenta de Origen:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbNumeroCuenta4, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel6)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbNumeroCuenta4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnBuscarCliente.setBackground(new java.awt.Color(0, 102, 102));
        btnBuscarCliente.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBuscarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.addActionListener(this::btnBuscarClienteActionPerformed);

        txtNombreBeneficiario.setEditable(false);

        txtMMontoTransferir.setText("0.00");

        txtDetalle.setColumns(20);
        txtDetalle.setRows(5);
        jScrollPane2.setViewportView(txtDetalle);

        btnTransferir.setBackground(new java.awt.Color(0, 102, 102));
        btnTransferir.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnTransferir.setForeground(new java.awt.Color(255, 255, 255));
        btnTransferir.setText("TRANSFERIR");
        btnTransferir.addActionListener(this::btnTransferirActionPerformed);

        txtCuentaTransferenciaInternaAhorros.addActionListener(this::txtCuentaTransferenciaInternaAhorrosActionPerformed);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(219, 219, 219)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtMMontoTransferir, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                                    .addGap(32, 32, 32)
                                    .addComponent(txtNombreBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(txtCuentaTransferenciaInternaAhorros, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane2)
                                    .addComponent(btnTransferir, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(12, 12, 12)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCuentaTransferenciaInternaAhorros, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNombreBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(txtMMontoTransferir, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTransferir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Transferencia Interna", jPanel13);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel14.setText("Número de cuenta de Destino");

        jLabel15.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel15.setText("Nombre Beneficiario");

        jLabel16.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel16.setText("Monto a Transferir($)");

        jLabel17.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel17.setText("Concepto o Detalle");

        jLabel18.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel18.setText("Cuenta de Origen:");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlbNumeroCuenta5, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel18)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbNumeroCuenta5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtBeneficiarioTransferirOtrosBancos.setEditable(false);
        txtBeneficiarioTransferirOtrosBancos.addActionListener(this::txtBeneficiarioTransferirOtrosBancosActionPerformed);

        txtMontoTransferirOtrosBancos.setText("0.00");

        txtDetalle1.setColumns(20);
        txtDetalle1.setRows(5);
        jScrollPane3.setViewportView(txtDetalle1);

        btnTransferirOtrosBancos.setBackground(new java.awt.Color(0, 102, 102));
        btnTransferirOtrosBancos.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnTransferirOtrosBancos.setForeground(new java.awt.Color(255, 255, 255));
        btnTransferirOtrosBancos.setText("TRANSFERIR");
        btnTransferirOtrosBancos.addActionListener(this::btnTransferirOtrosBancosActionPerformed);

        btnBuscarCliente2.setBackground(new java.awt.Color(0, 102, 102));
        btnBuscarCliente2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBuscarCliente2.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente2.setText("Buscar Cliente");
        btnBuscarCliente2.addActionListener(this::btnBuscarCliente2ActionPerformed);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(219, 219, 219)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3)
                                    .addComponent(btnTransferirOtrosBancos, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtCuentaDestinoTransferirOtrosBancos, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtMontoTransferirOtrosBancos, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                            .addGap(32, 32, 32)
                                            .addComponent(txtBeneficiarioTransferirOtrosBancos, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(29, 29, 29)
                                .addComponent(btnBuscarCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(172, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCuentaDestinoTransferirOtrosBancos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBeneficiarioTransferirOtrosBancos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addComponent(txtMontoTransferirOtrosBancos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTransferirOtrosBancos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Transferencias Otros Bancos", jPanel14);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 968, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Transferencia", jPanel4);

        jPanel2.setBackground(new java.awt.Color(18, 52, 83));

        jTabbedPane16.setBackground(new java.awt.Color(18, 52, 83));
        jTabbedPane16.setForeground(new java.awt.Color(255, 255, 255));

        jPanel96.setBackground(new java.awt.Color(18, 52, 83));

        jPanel101.setBackground(new java.awt.Color(255, 255, 255));
        jPanel101.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel124.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/TarjetaDebito.png"))); // NOI18N
        jPanel101.add(jLabel124, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 332, 234));

        jLabel154.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/TarjetaCredito.png"))); // NOI18N
        jLabel154.setText("jLabel29");
        jPanel101.add(jLabel154, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, 347, 243));

        jLabel155.setFont(new java.awt.Font("Segoe UI Variable", 3, 36)); // NOI18N
        jLabel155.setText("Tarjetas");
        jPanel101.add(jLabel155, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 186, -1));

        jLabel156.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel156.setText("Estado:");
        jPanel101.add(jLabel156, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 400, 59, 26));

        JlbTarjeta11.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jPanel101.add(JlbTarjeta11, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 350, 195, 38));

        JlbTarjeta12.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jPanel101.add(JlbTarjeta12, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 350, 195, 38));

        jLabel158.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/franja.png"))); // NOI18N
        jLabel158.setText("jLabel12");
        jPanel101.add(jLabel158, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, -1));

        jLabel159.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel159.setText("Número de Tarjeta: ");
        jPanel101.add(jLabel159, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 320, 210, 26));

        jLabel160.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel160.setText("Número de Tarjeta:");
        jPanel101.add(jLabel160, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 320, 240, 26));
        jPanel101.add(jlbEstadoTarjeta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 390, 230, 40));
        jPanel101.add(jlbEstadoTarjeta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 390, 210, 40));

        btnBloquearTarjeta2.setBackground(new java.awt.Color(0, 102, 102));
        btnBloquearTarjeta2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBloquearTarjeta2.setForeground(new java.awt.Color(255, 255, 255));
        btnBloquearTarjeta2.setText("Bloquear Tarjeta");
        btnBloquearTarjeta2.addActionListener(this::btnBloquearTarjeta2ActionPerformed);
        jPanel101.add(btnBloquearTarjeta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 470, 320, 40));

        btnBloquearTarjeta1.setBackground(new java.awt.Color(0, 102, 102));
        btnBloquearTarjeta1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBloquearTarjeta1.setForeground(new java.awt.Color(255, 255, 255));
        btnBloquearTarjeta1.setText("Bloquear Tarjeta");
        btnBloquearTarjeta1.addActionListener(this::btnBloquearTarjeta1ActionPerformed);
        jPanel101.add(btnBloquearTarjeta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 470, 320, 40));

        jLabel165.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel165.setText("Estado:");
        jPanel101.add(jLabel165, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 390, 59, 26));

        javax.swing.GroupLayout jPanel96Layout = new javax.swing.GroupLayout(jPanel96);
        jPanel96.setLayout(jPanel96Layout);
        jPanel96Layout.setHorizontalGroup(
            jPanel96Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel96Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel101, javax.swing.GroupLayout.PREFERRED_SIZE, 971, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel96Layout.setVerticalGroup(
            jPanel96Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel96Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel101, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane16.addTab("Tarjetas", jPanel96);

        jPanel102.setBackground(new java.awt.Color(18, 52, 83));

        jPanel103.setBackground(new java.awt.Color(255, 255, 255));
        jPanel103.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAperturar.setBackground(new java.awt.Color(0, 102, 102));
        btnAperturar.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnAperturar.setForeground(new java.awt.Color(255, 255, 255));
        btnAperturar.setText("Aperturar una nueva cuenta");
        btnAperturar.addActionListener(this::btnAperturarActionPerformed);
        jPanel103.add(btnAperturar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 520, 40));

        btnSolicitar.setBackground(new java.awt.Color(0, 102, 102));
        btnSolicitar.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnSolicitar.setForeground(new java.awt.Color(255, 255, 255));
        btnSolicitar.setText("Solicitar Tarjeta de Crédito");
        btnSolicitar.addActionListener(this::btnSolicitarActionPerformed);
        jPanel103.add(btnSolicitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 520, 40));

        jLabel161.setFont(new java.awt.Font("Segoe UI Variable", 3, 36)); // NOI18N
        jLabel161.setText("Solicitudes");
        jPanel103.add(jLabel161, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 199, 42));

        jLabel162.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/franja.png"))); // NOI18N
        jLabel162.setText("jLabel12");
        jPanel103.add(jLabel162, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, -1));

        btnLimite.setBackground(new java.awt.Color(0, 102, 102));
        btnLimite.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnLimite.setForeground(new java.awt.Color(255, 255, 255));
        btnLimite.setText("Modificar Limite diario");
        btnLimite.addActionListener(this::btnLimiteActionPerformed);
        jPanel103.add(btnLimite, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 330, 70));

        TblSolicitudesClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cédula", "Nombre", "Petición", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane17.setViewportView(TblSolicitudesClientes);

        jPanel103.add(jScrollPane17, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 770, 250));

        jLabel163.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel163.setText("Estado de Solicitudes del usuario:");
        jPanel103.add(jLabel163, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        javax.swing.GroupLayout jPanel102Layout = new javax.swing.GroupLayout(jPanel102);
        jPanel102.setLayout(jPanel102Layout);
        jPanel102Layout.setHorizontalGroup(
            jPanel102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel102Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel103, javax.swing.GroupLayout.PREFERRED_SIZE, 971, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel102Layout.setVerticalGroup(
            jPanel102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel102Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel103, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane16.addTab("Solicitudes", jPanel102);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jTabbedPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 1011, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Otros Servicios", jPanel2);

        jPanel1.setBackground(new java.awt.Color(18, 52, 83));

        jTabbedPane2.setBackground(new java.awt.Color(18, 52, 83));
        jTabbedPane2.setForeground(new java.awt.Color(255, 255, 255));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Roboto", 2, 18)); // NOI18N
        jLabel23.setText("Saldo Disponible: ");

        jlbNumeroCuenta2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jlbSaldoDisponibleAhorro.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addGap(26, 26, 26)
                .addComponent(jlbSaldoDisponibleAhorro, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlbNumeroCuenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addComponent(jlbNumeroCuenta2)
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addComponent(jlbSaldoDisponibleAhorro, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(22, 22, 22))))
        );

        jLabel25.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel25.setText("Total de monto ahorrado en la cuenta de");

        jlbMontoAhorroFlexible.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N

        jLabel26.setFont(new java.awt.Font("Roboto", 2, 18)); // NOI18N
        jLabel26.setText("Ahorro Flexible:");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(62, 62, 62)
                        .addComponent(jlbMontoAhorroFlexible, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbMontoAhorroFlexible, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(15, 15, 15)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jLabel24.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel24.setText("Estimado Cliente, en esta sección puede aportar o retirar el monto designado a continuación:");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel27.setText("Monto:");

        btnMontoAhorrar.setBackground(new java.awt.Color(0, 102, 102));
        btnMontoAhorrar.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnMontoAhorrar.setForeground(new java.awt.Color(255, 255, 255));
        btnMontoAhorrar.setText("Guardar monto en Alcancia");
        btnMontoAhorrar.addActionListener(this::btnMontoAhorrarActionPerformed);

        btnRetirarAhorros.setBackground(new java.awt.Color(0, 102, 102));
        btnRetirarAhorros.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnRetirarAhorros.setForeground(new java.awt.Color(255, 255, 255));
        btnRetirarAhorros.setText("Retirar Alcancia");
        btnRetirarAhorros.addActionListener(this::btnRetirarAhorrosActionPerformed);

        jLabel29.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel29.setText("Si desea conocer cuanto interes ");

        btnSimularInteresAhorro.setBackground(new java.awt.Color(0, 102, 102));
        btnSimularInteresAhorro.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnSimularInteresAhorro.setForeground(new java.awt.Color(255, 255, 255));
        btnSimularInteresAhorro.setText("Simulación de Interes");
        btnSimularInteresAhorro.addActionListener(this::btnSimularInteresAhorroActionPerformed);

        jlbNumeroCuenta8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel22.setText("Número de Cuenta:");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jlbNumeroCuenta3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlbNumeroCuenta8, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jlbNumeroCuenta8))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlbNumeroCuenta3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel22)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel17Layout.createSequentialGroup()
                                    .addGap(36, 36, 36)
                                    .addComponent(txtMontoAhorroFlexible, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnRetirarAhorros, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnMontoAhorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(107, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSimularInteresAhorro, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMontoAhorroFlexible, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(btnMontoAhorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRetirarAhorros, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addComponent(jLabel29)
                .addGap(50, 50, 50)
                .addComponent(btnSimularInteresAhorro, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ahorro Flexible", jPanel17);

        jPanel25.setBackground(new java.awt.Color(18, 52, 83));

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Saldo Actual:");
        jPanel26.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 86, 24));
        jPanel26.add(SaldoCuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 238, 44));

        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel26.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(502, 22, -1, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Tasa de interés:");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setText("Periodo:");

        txtTasaInteres.addActionListener(this::txtTasaInteresActionPerformed);

        JCMeses.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Mes", "2 Meses", "3 Meses", "4 Meses", "5 Meses", "6 Meses", "7 Meses", "8 Meses", "9 Meses", "10 Meses", "11 Meses", "12 Meses" }));

        btnCalcularInteres.setText("Simular Rendimiento");
        btnCalcularInteres.addActionListener(this::btnCalcularInteresActionPerformed);

        jLabel32.setText("Ingrese una tasa anual y el período para ver cuánto crecerá su ahorro flexible");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                                .addComponent(btnCalcularInteres, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTasaInteres)
                                    .addComponent(JCMeses, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(16, 16, 16))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtTasaInteres, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JCMeses, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel19)))
                .addGap(42, 42, 42)
                .addComponent(btnCalcularInteres, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel26.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 520, 260));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setText("Saldo al final del período:");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setText("Interés ganado:");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel39.setText("Advertencia: Esta es una simulación, no compromete su saldo actual.");

        jLabel40.setText("---------------------------   RESULTADOS ---------------------------");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)))
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbInteresGanado, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbSaldoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48))))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jlbInteresGanado, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jlbSaldoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel26.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 520, 160));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Interes.png"))); // NOI18N
        jPanel26.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 400, 520));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 991, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Simulador de Interés", jPanel25);

        jPanel21.setBackground(new java.awt.Color(18, 52, 83));

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel38.setText("Nombre Bolsillo:");

        txtNombreBolsillo.addActionListener(this::txtNombreBolsilloActionPerformed);

        btnDepositarBolsillo.setBackground(new java.awt.Color(0, 102, 102));
        btnDepositarBolsillo.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnDepositarBolsillo.setForeground(new java.awt.Color(255, 255, 255));
        btnDepositarBolsillo.setText("Depositar Bolsillo");
        btnDepositarBolsillo.addActionListener(this::btnDepositarBolsilloActionPerformed);

        btnRetirarBolsillo.setBackground(new java.awt.Color(0, 102, 102));
        btnRetirarBolsillo.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnRetirarBolsillo.setForeground(new java.awt.Color(255, 255, 255));
        btnRetirarBolsillo.setText("Retirar Bolsillo");
        btnRetirarBolsillo.addActionListener(this::btnRetirarBolsilloActionPerformed);

        jLabel42.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel42.setText("-------------------------------------- Ahorro Programado --------------------------------------");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel43.setText("Meta Bolsillo:");

        txtMetaBolsillo.addActionListener(this::txtMetaBolsilloActionPerformed);

        btnCrearBolsillo.setBackground(new java.awt.Color(0, 102, 102));
        btnCrearBolsillo.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnCrearBolsillo.setForeground(new java.awt.Color(255, 255, 255));
        btnCrearBolsillo.setText("Crear Bolsillo");
        btnCrearBolsillo.addActionListener(this::btnCrearBolsilloActionPerformed);

        TblBolsillos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nombre", "Meta $", "Acumulado", "Avance %"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblBolsillos.setToolTipText("");
        jScrollPane4.setViewportView(TblBolsillos);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(173, 173, 173))
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnRetirarBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDepositarBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCrearBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNombreBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMetaBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel42)
                .addGap(28, 28, 28)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMetaBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(btnCrearBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDepositarBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRetirarBolsillo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Bolsillos de ahorro programado", jPanel21);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1004, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ahorro Flexible", jPanel1);

        jPanel6.setBackground(new java.awt.Color(18, 52, 83));

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Perfil.png"))); // NOI18N
        jLabel28.setText("jLabel28");

        btnCerrarSesionCliente.setBackground(new java.awt.Color(0, 102, 102));
        btnCerrarSesionCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesionCliente.setText("Cerrar Sesión");
        btnCerrarSesionCliente.addActionListener(this::btnCerrarSesionClienteActionPerformed);

        jLabel13.setFont(new java.awt.Font("Segoe Script", 1, 18)); // NOI18N
        jLabel13.setText("Hasta la Próxima!!!");

        jlbUsuarioAhorros1.setFont(new java.awt.Font("Serif", 3, 36)); // NOI18N
        jlbUsuarioAhorros1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(0, 45, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addComponent(btnCerrarSesionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124))))
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                    .addContainerGap(144, Short.MAX_VALUE)
                    .addComponent(jlbUsuarioAhorros1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(120, 120, 120)))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCerrarSesionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                    .addContainerGap(236, Short.MAX_VALUE)
                    .addComponent(jlbUsuarioAhorros1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(173, 173, 173)))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(316, Short.MAX_VALUE)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(257, 257, 257))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cerrar Sesión", jPanel6);

        jPanel7.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 610));
        jTabbedPane1.getAccessibleContext().setAccessibleName("Cuenta Ahorros");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        buscarBeneficiarioInterno();
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnTransferirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferirActionPerformed
        String cuentaDestino = txtCuentaTransferenciaInternaAhorros.getText().trim();
        if (cuentaDestino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de cuenta destino.\nEjemplo: AHO-100002 o COR-100001", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente destino = bancoServicios.getRepositorioUsuarios().buscarPorCuenta(cuentaDestino);
        if (destino == null || !destino.esClienteInterno()) {
            JOptionPane.showMessageDialog(this, "No se encontró una cuenta interna con ese número.\nFormatos: AHO-100001, COR-100001",
                    "No encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String montoTxt = txtMMontoTransferir.getText().trim();
        String detalleUsuario = (txtDetalle != null) ? txtDetalle.getText().trim() : "";

        try {
            double monto = Double.parseDouble(montoTxt);
            monto = Math.round(monto * 100.0) / 100.0; // 2 decimales

            bancoServicios.transferenciaInternaDesdeAhorros(clienteActual.getCedula(), cuentaDestino, monto);

            registrarMovimiento("TRANSF. INTERNA",
                    clienteActual.getNumeroCuentaAhorros(), cuentaDestino,
                    destino.getNombre() + " " + destino.getApellido(), monto, detalleUsuario);
            JOptionPane.showMessageDialog(this, "¡Transferencia interna exitosa!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtCuentaTransferenciaInternaAhorros.setText("");
            txtMMontoTransferir.setText("");
            txtNombreBeneficiario.setText("");
            if (txtDetalle != null) txtDetalle.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en Transferencia", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnTransferirActionPerformed

    private void btnTransferirOtrosBancosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferirOtrosBancosActionPerformed
        String cuentaDestino = txtCuentaDestinoTransferirOtrosBancos.getText().trim();
        if (cuentaDestino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de cuenta destino.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente destino = bancoServicios.getRepositorioUsuarios().buscarPorCuenta(cuentaDestino);
        String beneficiarioNombre = (destino != null) ? destino.getNombre() + " " + destino.getApellido() : cuentaDestino;
        String detalleUsuario = (txtDetalle1 != null) ? txtDetalle1.getText().trim() : "";

        try {
            double monto = Double.parseDouble(txtMontoTransferirOtrosBancos.getText().trim());
            monto = Math.round(monto * 100.0) / 100.0;
            double total = Math.round((monto + BancoServicios.COMISION_INTERBANCARIA) * 100.0) / 100.0;

            int ok = JOptionPane.showConfirmDialog(this,
                    "Se aplicará una comisión de $" + BancoServicios.COMISION_INTERBANCARIA
                    + " por transferencia a otro banco.\nTotal a descontar: $" + total
                    + "\n¿Desea continuar?",
                    "Confirmar comisión", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;

            bancoServicios.transferenciaExternaDesdeAhorros(clienteActual.getCedula(), cuentaDestino, monto);

            registrarMovimiento("TRANSF. OTRO BANCO",
                    clienteActual.getNumeroCuentaAhorros(), cuentaDestino,
                    beneficiarioNombre, monto,
                    "Comisión $" + BancoServicios.COMISION_INTERBANCARIA + (detalleUsuario.isEmpty() ? "" : " | " + detalleUsuario));
            JOptionPane.showMessageDialog(this, "¡Transferencia a otro banco enviada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtCuentaDestinoTransferirOtrosBancos.setText("");
            txtMontoTransferirOtrosBancos.setText("");
            if (txtBeneficiarioTransferirOtrosBancos != null) txtBeneficiarioTransferirOtrosBancos.setText("");
            if (txtDetalle1 != null) txtDetalle1.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en Transferencia Interbancaria", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnTransferirOtrosBancosActionPerformed

    private void txtBeneficiarioTransferirOtrosBancosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBeneficiarioTransferirOtrosBancosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBeneficiarioTransferirOtrosBancosActionPerformed

    private void txtCuentaTransferenciaInternaAhorrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCuentaTransferenciaInternaAhorrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCuentaTransferenciaInternaAhorrosActionPerformed

    private void btnCerrarSesionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionClienteActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Acceso_Cuentas acceso = new Acceso_Cuentas(bancoServicios);
            acceso.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnCerrarSesionClienteActionPerformed

    private void btnBuscarCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCliente1ActionPerformed
        // Botón de la cabecera: permite consultar rápidamente a otro cliente por cédula
        String cedula = JOptionPane.showInputDialog(this, "Ingrese la cédula del cliente a consultar:");
        if (cedula == null || cedula.trim().isEmpty()) {
            return;
        }
        Cliente consultado = bancoServicios.obtenerClientePorCedula(cedula.trim());
        if (consultado == null) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con esa cédula.",
                    "No encontrado", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cliente: " + consultado.getNombre() + " " + consultado.getApellido()
                    + "\nCuenta Ahorros: " + consultado.getNumeroCuentaAhorros()
                    + "\nCuenta Corriente: " + consultado.getNumeroCuentaCorriente(),
                    "Cliente encontrado", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarCliente1ActionPerformed

    private void btnBuscarCliente2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCliente2ActionPerformed
        // Botón de la pestaña "Transferir a otros bancos": busca por número de cuenta destino
        String cuenta = txtCuentaDestinoTransferirOtrosBancos.getText().trim();

        if (cuenta.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese el número de cuenta destino para buscar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente destinatario = bancoServicios.getRepositorioUsuarios().buscarPorCuenta(cuenta);

        if (destinatario == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró ninguna cuenta con ese número.", "No encontrado",
                    JOptionPane.WARNING_MESSAGE);
            txtBeneficiarioTransferirOtrosBancos.setText("");
            return;
        }

        txtBeneficiarioTransferirOtrosBancos.setText(destinatario.getNombre() + " " + destinatario.getApellido());
    }//GEN-LAST:event_btnBuscarCliente2ActionPerformed

    private void btnLimiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimiteActionPerformed
        String entrada = JOptionPane.showInputDialog(this,
            "Su límite diario actual es $ " + clienteActual.getLimiteDiarioTransaccion()
            + "\nIngrese el nuevo límite diario que desea solicitar:");

        if (entrada == null || entrada.trim().isEmpty()) {
            return;
        }

        try {
            double nuevoLimite = Double.parseDouble(entrada.trim());

            if (nuevoLimite <= 0) {
                JOptionPane.showMessageDialog(this, "El límite debe ser un monto positivo.",
                    "Monto inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Solicitud solicitud = new Solicitud(
                "LIMITE",
                clienteActual.getCedula(),
                clienteActual.getNombre() + " " + clienteActual.getApellido(),
                clienteActual.getNumeroCuentaCorriente(),
                nuevoLimite,
                "Solicitud de aumento de límite diario a $ " + nuevoLimite
            );

            bancoServicios.emitirSolicitud(solicitud);

            JOptionPane.showMessageDialog(this,
                "Su solicitud de nuevo límite ($ " + nuevoLimite + ") fue enviada al área "
                + "administrativa para su aprobación o rechazo.",
                "Solicitud enviada", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.",
                "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnLimiteActionPerformed

    private void btnSolicitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSolicitarActionPerformed
        String motivo = JOptionPane.showInputDialog(this,
            "Describa brevemente el motivo de su solicitud:");

        if (motivo == null || motivo.trim().isEmpty()) {
            return;
        }

        Solicitud solicitud = new Solicitud(
            "GENERAL",
            clienteActual.getCedula(),
            clienteActual.getNombre() + " " + clienteActual.getApellido(),
            clienteActual.getNumeroCuentaCorriente(),
            0,
            motivo.trim()
        );

        bancoServicios.emitirSolicitud(solicitud);
        cargarSolicitudesCliente();
        JOptionPane.showMessageDialog(this,
            "¡Solicitud enviada! El área administrativa la revisará pronto.",
            "Solicitud enviada", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnSolicitarActionPerformed

    private void btnAperturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAperturarActionPerformed
        AperturarCuenta aperturar = new AperturarCuenta(bancoServicios, clienteActual);
        aperturar.setVisible(true);
    }//GEN-LAST:event_btnAperturarActionPerformed

    private void btnSimularInteresAhorroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimularInteresAhorroActionPerformed
        // Saltar a la pestaña Simulador de Interés y pre-cargar el saldo
        jTabbedPane2.setSelectedIndex(jTabbedPane2.getTabCount() - 2);
        // Pre-llenar la tasa sugerida del banco (4.5% anual típico en Ecuador)
        txtTasaInteres.setEditable(true);
        txtTasaInteres.setText("4.5");
        // Mostrar saldo actual del ahorro flexible como base
        jlbSaldoFinal.setText(String.format("Saldo base: $ %.2f (ahorro flexible)", clienteActual.getSaldoAhorroFlexible()));
        jlbInteresGanado.setText("Interés ganado: $ 0.00");
    }//GEN-LAST:event_btnSimularInteresAhorroActionPerformed

    private void btnRetirarAhorrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetirarAhorrosActionPerformed
        try {
            double monto = Double.parseDouble(txtMontoAhorroFlexible.getText().trim());

            if (monto <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.");
                return;
            }
            if (monto > clienteActual.getSaldoAhorroFlexible()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Fondos insuficientes en alcancía. Ahorro flexible: $ " + clienteActual.getSaldoAhorroFlexible());
                return;
            }

            clienteActual.setSaldoAhorroFlexible(clienteActual.getSaldoAhorroFlexible() - monto);
            clienteActual.setSaldoAhorros(clienteActual.getSaldoAhorros() + monto);

            registrarMovimiento("RETIRO ALCANCÍA", "AHORRO FLEXIBLE", clienteActual.getNumeroCuentaAhorros(), clienteActual.getNombre(), monto, "Retiro de ahorro flexible");
            cargarDatosCliente();
            txtMontoAhorroFlexible.setText("");

            javax.swing.JOptionPane.showMessageDialog(this, "¡Retiro exitoso!");

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un monto válido.");
        }
    }//GEN-LAST:event_btnRetirarAhorrosActionPerformed

    private void btnMontoAhorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMontoAhorrarActionPerformed
        try {
            double monto = Double.parseDouble(txtMontoAhorroFlexible.getText().trim());

            if (monto <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.");
                return;
            }
            if (monto > clienteActual.getSaldoAhorros()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Fondos insuficientes. Saldo disponible: $ " + clienteActual.getSaldoAhorros());
                return;
            }

            clienteActual.setSaldoAhorros(clienteActual.getSaldoAhorros() - monto);
            clienteActual.setSaldoAhorroFlexible(clienteActual.getSaldoAhorroFlexible() + monto);

            // ← AQUÍ se llama después de la operación exitosa
            registrarMovimiento("GUARDAR ALCANCÍA", clienteActual.getNumeroCuentaAhorros(), "AHORRO FLEXIBLE", clienteActual.getNombre(), monto, "Movimiento a ahorro flexible");
            cargarDatosCliente();
            txtMontoAhorroFlexible.setText("");

            javax.swing.JOptionPane.showMessageDialog(this, "¡Guardado exitoso!");

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un monto válido.");
        }
    }//GEN-LAST:event_btnMontoAhorrarActionPerformed

    private void txtTasaInteresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTasaInteresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTasaInteresActionPerformed

    private void btnCalcularInteresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularInteresActionPerformed
        String tasaTxt  = txtTasaInteres.getText().trim();

        // Buscar el campo de meses — puede llamarse txtMeses o similar
        // Lo buscamos por el panel de simulación
        double saldoBase = clienteActual.getSaldoAhorroFlexible();

        if (saldoBase <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No tiene saldo en su Ahorro Flexible para simular."
                    + "Deposite primero en el Ahorro Flexible.",
                    "Sin saldo base", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tasaTxt.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ingrese una tasa de interés anual (%).",
                    "Campo vacío", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double tasaAnual  = Double.parseDouble(tasaTxt.replace(",", "."));

            if (tasaAnual <= 0 || tasaAnual > 100) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "La tasa debe estar entre 0.01% y 100%.",
                        "Tasa inválida", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Leer meses directamente del JComboBox JCMeses (sin pedir al usuario)
            String opcionMeses = JCMeses.getSelectedItem().toString(); // "1 Mes", "4 Meses", etc.
            int meses = Integer.parseInt(opcionMeses.split(" ")[0]);

            // Fórmula de interés compuesto mensual
            double tasaMensual = (tasaAnual / 100.0) / 12.0;
            double saldoFinal  = bancoServicios.simularInteresCompuesto(saldoBase, tasaMensual, meses);
            double interes     = saldoFinal - saldoBase;

            jlbSaldoFinal.setText(String.format(" $ %.2f", saldoFinal));
            jlbInteresGanado.setText(String.format("$ %.2f  (Tasa: %.2f%% anual | %d meses)", interes, tasaAnual, meses));

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ingrese valores numéricos válidos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCalcularInteresActionPerformed

    // ===== BOLSILLOS DE AHORRO PROGRAMADO =====
    // Estructura: {nombre, meta, acumulado}
    private javax.swing.table.DefaultTableModel modeloBolsillos;
    private final java.util.List<double[]> datosBolsillos = new java.util.ArrayList<>();
    private final java.util.List<String> nombresBolsillos = new java.util.ArrayList<>();

    private void inicializarTablaBolsillos() {
        modeloBolsillos = new javax.swing.table.DefaultTableModel(
                new String[]{"Nombre", "Meta ($)", "Acumulado ($)", "Avance (%)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        TblBolsillos.setModel(modeloBolsillos);
    }

    private void refrescarTablaBolsillos() {
        modeloBolsillos.setRowCount(0);
        for (int i = 0; i < nombresBolsillos.size(); i++) {
            double meta = datosBolsillos.get(i)[0];
            double acum = datosBolsillos.get(i)[1];
            double pct  = meta > 0 ? (acum / meta) * 100 : 0;
            modeloBolsillos.addRow(new Object[]{
                nombresBolsillos.get(i),
                String.format("$ %.2f", meta),
                String.format("$ %.2f", acum),
                String.format("%.1f%%", pct)
            });
        }
    }

    private void btnDepositarBolsilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositarBolsilloActionPerformed
        int fila = TblBolsillos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un bolsillo de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String montoTxt = JOptionPane.showInputDialog(this,
                "Monto a depositar en \"" + nombresBolsillos.get(fila) + "\":");
        if (montoTxt == null || montoTxt.trim().isEmpty()) return;

        try {
            double monto = Math.round(Double.parseDouble(montoTxt.trim()) * 100.0) / 100.0;
            if (monto <= 0) throw new Exception("El monto debe ser mayor a 0.");
            if (monto > clienteActual.getSaldoAhorros()) throw new Exception("Fondos insuficientes en Cuenta de Ahorros.");

            clienteActual.setSaldoAhorros(clienteActual.getSaldoAhorros() - monto);
            datosBolsillos.get(fila)[1] += monto;
            registrarMovimiento("DEPÓSITO BOLSILLO",
                    clienteActual.getNumeroCuentaAhorros(), nombresBolsillos.get(fila),
                    clienteActual.getNombre(), monto, "Depósito al bolsillo: " + nombresBolsillos.get(fila));
            refrescarTablaBolsillos();
            cargarDatosCliente();
            JOptionPane.showMessageDialog(this, "¡Depósito al bolsillo exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDepositarBolsilloActionPerformed

    private void btnRetirarBolsilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetirarBolsilloActionPerformed
        int fila = TblBolsillos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un bolsillo de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String montoTxt = JOptionPane.showInputDialog(this,
                "Monto a retirar de \"" + nombresBolsillos.get(fila) + "\":");
        if (montoTxt == null || montoTxt.trim().isEmpty()) return;

        try {
            double monto = Math.round(Double.parseDouble(montoTxt.trim()) * 100.0) / 100.0;
            if (monto <= 0) throw new Exception("El monto debe ser mayor a 0.");
            if (monto > datosBolsillos.get(fila)[1]) throw new Exception("El bolsillo no tiene suficiente saldo.");

            datosBolsillos.get(fila)[1] -= monto;
            clienteActual.setSaldoAhorros(clienteActual.getSaldoAhorros() + monto);
            registrarMovimiento("RETIRO BOLSILLO",
                    nombresBolsillos.get(fila), clienteActual.getNumeroCuentaAhorros(),
                    clienteActual.getNombre(), monto, "Retiro del bolsillo: " + nombresBolsillos.get(fila));
            refrescarTablaBolsillos();
            cargarDatosCliente();
            JOptionPane.showMessageDialog(this, "¡Retiro del bolsillo exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRetirarBolsilloActionPerformed

    private void txtNombreBolsilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreBolsilloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreBolsilloActionPerformed

    private void txtMetaBolsilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMetaBolsilloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMetaBolsilloActionPerformed

    private void btnCrearBolsilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearBolsilloActionPerformed
        String nombre = txtNombreBolsillo.getText().trim();
        String metaTxt = txtMetaBolsillo.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del bolsillo.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (metaTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la meta de ahorro.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double meta = Math.round(Double.parseDouble(metaTxt) * 100.0) / 100.0;
            if (meta <= 0) {
                JOptionPane.showMessageDialog(this, "La meta debe ser mayor a 0.", "Meta inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            nombresBolsillos.add(nombre);
            datosBolsillos.add(new double[]{meta, 0.0});
            refrescarTablaBolsillos();
            txtNombreBolsillo.setText("");
            txtMetaBolsillo.setText("");
            JOptionPane.showMessageDialog(this,
                    "¡Bolsillo " + nombre + " creado! Meta: $" + String.format("%.2f", meta),
                    "Bolsillo creado", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido en la Meta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCrearBolsilloActionPerformed

    private void btnBloquearTarjeta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBloquearTarjeta1ActionPerformed
        boolean bloqueada = clienteActual.isBloqueadoAhorros();
        int conf = JOptionPane.showConfirmDialog(this,
                bloqueada ? "La Tarjeta de Ahorros está BLOQUEADA. ¿Desea DESBLOQUEARLA?"
                          : "¿Desea BLOQUEAR la Tarjeta de Débito de Ahorros?\nNo podrá realizar transacciones hasta desbloquearla.",
                bloqueada ? "Desbloquear tarjeta" : "Bloquear tarjeta",
                JOptionPane.YES_NO_OPTION,
                bloqueada ? JOptionPane.QUESTION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            clienteActual.setBloqueadoAhorros(!bloqueada);
            actualizarEstadoTarjetas();
            JOptionPane.showMessageDialog(this,
                    "Tarjeta de Ahorros " + (!bloqueada ? "BLOQUEADA" : "DESBLOQUEADA") + " correctamente.",
                    "Estado actualizado", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnBloquearTarjeta1ActionPerformed

    private void btnBloquearTarjeta2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBloquearTarjeta2ActionPerformed
        boolean bloqueada = clienteActual.isBloqueadoAhorros();
        int conf = JOptionPane.showConfirmDialog(this,
                bloqueada ? "La Tarjeta de Ahorros está BLOQUEADA. ¿Desea DESBLOQUEARLA?"
                          : "¿Desea BLOQUEAR la Tarjeta Virtual de Ahorros?",
                bloqueada ? "Desbloquear tarjeta" : "Bloquear tarjeta",
                JOptionPane.YES_NO_OPTION,
                bloqueada ? JOptionPane.QUESTION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            clienteActual.setBloqueadoAhorros(!bloqueada);
            actualizarEstadoTarjetas();
            JOptionPane.showMessageDialog(this,
                    "Tarjeta de Ahorros " + (!bloqueada ? "BLOQUEADA" : "DESBLOQUEADA") + " correctamente.",
                    "Estado actualizado", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnBloquearTarjeta2ActionPerformed


   

    private void actualizarEstadoTarjetas() {
        jlbEstadoTarjeta1.setText(clienteActual.isBloqueadoAhorros() ? "⛔ BLOQUEADA" : "✔ ACTIVA");
        jlbEstadoTarjeta1.setForeground(clienteActual.isBloqueadoAhorros()
                ? java.awt.Color.RED : new java.awt.Color(0, 128, 0));
        jlbEstadoTarjeta2.setText(clienteActual.isBloqueadoAhorros() ? "⛔ BLOQUEADA" : "✔ ACTIVA");
        jlbEstadoTarjeta2.setForeground(clienteActual.isBloqueadoAhorros()
                ? java.awt.Color.RED : new java.awt.Color(0, 128, 0));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> JCMeses;
    private javax.swing.JLabel JlbTarjeta11;
    private javax.swing.JLabel JlbTarjeta12;
    private javax.swing.JLabel SaldoCuenta;
    private javax.swing.JTable TblBolsillos;
    private javax.swing.JTable TblSolicitudesClientes;
    private javax.swing.JButton btnAperturar;
    private javax.swing.JButton btnBloquearTarjeta1;
    private javax.swing.JButton btnBloquearTarjeta2;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarCliente1;
    private javax.swing.JButton btnBuscarCliente2;
    private javax.swing.JButton btnCalcularInteres;
    private javax.swing.JButton btnCerrarSesionCliente;
    private javax.swing.JButton btnCrearBolsillo;
    private javax.swing.JButton btnDepositarBolsillo;
    private javax.swing.JButton btnLimite;
    private javax.swing.JButton btnMontoAhorrar;
    private javax.swing.JButton btnRetirarAhorros;
    private javax.swing.JButton btnRetirarBolsillo;
    private javax.swing.JButton btnSimularInteresAhorro;
    private javax.swing.JButton btnSolicitar;
    private javax.swing.JButton btnTransferir;
    private javax.swing.JButton btnTransferirOtrosBancos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel101;
    private javax.swing.JPanel jPanel102;
    private javax.swing.JPanel jPanel103;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel96;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane16;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel jlbEstadoTarjeta1;
    private javax.swing.JLabel jlbEstadoTarjeta2;
    private javax.swing.JLabel jlbInteresGanado;
    private javax.swing.JLabel jlbMontoAhorroFlexible;
    private javax.swing.JLabel jlbNumeroCuenta;
    private javax.swing.JLabel jlbNumeroCuenta1;
    private javax.swing.JLabel jlbNumeroCuenta2;
    private javax.swing.JLabel jlbNumeroCuenta3;
    private javax.swing.JLabel jlbNumeroCuenta4;
    private javax.swing.JLabel jlbNumeroCuenta5;
    private javax.swing.JLabel jlbNumeroCuenta6;
    private javax.swing.JLabel jlbNumeroCuenta7;
    private javax.swing.JLabel jlbNumeroCuenta8;
    private javax.swing.JLabel jlbSaldo;
    private javax.swing.JLabel jlbSaldo1;
    private javax.swing.JLabel jlbSaldoDisponibleAhorro;
    private javax.swing.JLabel jlbSaldoDisponibleAhorro1;
    private javax.swing.JLabel jlbSaldoFinal;
    private javax.swing.JLabel jlbTransaccionesDisponibles;
    private javax.swing.JLabel jlbUsuarioAhorros;
    private javax.swing.JLabel jlbUsuarioAhorros1;
    private javax.swing.JTextField txtBeneficiarioTransferirOtrosBancos;
    private javax.swing.JTextField txtCuentaDestinoTransferirOtrosBancos;
    private javax.swing.JTextField txtCuentaTransferenciaInternaAhorros;
    private javax.swing.JTextArea txtDetalle;
    private javax.swing.JTextArea txtDetalle1;
    private javax.swing.JTextField txtMMontoTransferir;
    private javax.swing.JTextField txtMetaBolsillo;
    private javax.swing.JTextField txtMontoAhorroFlexible;
    private javax.swing.JTextField txtMontoTransferirOtrosBancos;
    private javax.swing.JTextField txtNombreBeneficiario;
    private javax.swing.JTextField txtNombreBolsillo;
    private javax.swing.JTextField txtTasaInteres;
    // End of variables declaration//GEN-END:variables
}
