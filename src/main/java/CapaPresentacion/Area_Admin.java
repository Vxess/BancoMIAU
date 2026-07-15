/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package CapaPresentacion;

import CapaEntidades.Cheque;
import CapaEntidades.Cliente;
import CapaEntidades.Solicitud;
import CapaNegocio.BancoServicios;
import javax.swing.JOptionPane;

/**
 *
 * @author Asus
 */
public class Area_Admin extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Area_Admin.class.getName());

    private BancoServicios bancoServicios;
    private String usuarioAdmin;

    // Lista paralela a las filas de TblCuentasSolicitadas para recuperar el
    // objeto Solicitud a partir del índice de fila seleccionado.
    private final java.util.List<Solicitud> solicitudesMostradas = new java.util.ArrayList<>();

    public Area_Admin(BancoServicios bancoServicios, String usuarioAdmin) {
        this.bancoServicios = bancoServicios;
        this.usuarioAdmin = usuarioAdmin;
        initComponents();
        this.setLocationRelativeTo(null);
        jLabel6.setText(" " + bancoServicios.getNombreAdmin());
        jlbUsuario1.setText(" " + bancoServicios.getNombreAdmin());
        cargarSolicitudes();
        cargarClientes(null);
        cargarCheques();
        cargarLog();
    }

    // ========================= Solicitudes de Apertura =========================

    /**
     * Pinta TblCuentasSolicitadas con todas las solicitudes en estado Pendiente.
     * Columnas: Solicitante | Tipo de Cuenta | Detalle (ingresos, gastos, dir.) | Estado
     */
    private void cargarSolicitudes() {
        javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) TblCuentasSolicitadas.getModel();
        modelo.setColumnIdentifiers(new String[]{
            "Solicitante (Cédula)", "Tipo de Cuenta", "Detalle Financiero", "Estado"
        });
        modelo.setRowCount(0);
        solicitudesMostradas.clear();

        for (Solicitud s : bancoServicios.getSolicitudes()) {
            if (s.getEstado().equals("Pendiente")) {
                String tipoLabel = s.getTipo().equals("REGISTRO") ? "NUEVO REGISTRO" : s.getTipo();
                modelo.addRow(new Object[]{
                    s.getNombreCliente() + " — " + s.getCedulaCliente(),
                    tipoLabel,
                    s.getDetalle(),
                    s.getEstado()
                });
                solicitudesMostradas.add(s);
            }
        }
    }

    private Solicitud obtenerSolicitudSeleccionada() {
        int fila = TblCuentasSolicitadas.getSelectedRow();
        if (fila < 0 || fila >= solicitudesMostradas.size()) return null;
        return solicitudesMostradas.get(fila);
    }

    // ========================= Clientes / Auditoría =========================

    /**
     * Pinta TblClientes. Si cedula es null o vacío muestra todos;
     * si tiene valor filtra solo al que coincide con esa cédula.
     */
    private void cargarClientes(String cedula) {
        javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) TblClientes.getModel();
        modelo.setRowCount(0);

        for (Cliente c : bancoServicios.listarTodosClientes()) {
            if (cedula != null && !cedula.isEmpty() && !c.getCedula().equals(cedula)) {
                continue;
            }
            // Solo se muestra la(s) cuenta(s) que el cliente realmente tiene;
            // el estado de tarjeta bloqueada se indica aparte, sin ocultar la cuenta.
            StringBuilder cuentasTexto = new StringBuilder();
            if (c.isCuentaAhorrosActiva()) {
                cuentasTexto.append("AHO: ").append(c.getNumeroCuentaAhorros());
                if (c.isBloqueadoAhorros()) cuentasTexto.append(" [⛔ Tarjeta bloqueada]");
            }
            if (c.isCuentaCorrienteActiva()) {
                if (cuentasTexto.length() > 0) cuentasTexto.append("  |  ");
                cuentasTexto.append("COR: ").append(c.getNumeroCuentaCorriente());
                if (c.isBloqueadoCorriente()) cuentasTexto.append(" [⛔ Tarjeta bloqueada]");
            }
            if (cuentasTexto.length() == 0) {
                cuentasTexto.append("Sin cuentas activas");
            }
            modelo.addRow(new Object[]{
                c.getCedula(),
                c.getNombre() + " " + c.getApellido(),
                c.getCorreo(),
                cuentasTexto.toString(),
                String.format("Txn hoy: %d | Límite: $%.2f | Aho: $%.2f | Cor: $%.2f",
                    c.getTransaccionesHoy(), c.getLimiteDiarioTransaccion(),
                    c.getSaldoAhorros(), c.getSaldoCorriente())
            });
        }

        JlbMetricas.setText(String.valueOf(bancoServicios.contarClientes()));
    }

    // ========================= Cheques =========================

    /** Pinta TblCheques con todos los cheques pendientes de revisión. */
    private void cargarCheques() {
        javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) TblCheques.getModel();
        modelo.setRowCount(0);

        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Cheque ch : bancoServicios.listarChequesPendientes()) {
            modelo.addRow(new Object[]{
                ch.getNumeroCheque(),
                ch.getCedulaCliente(),
                ch.getBeneficiario() != null ? ch.getBeneficiario() : "—",
                "$ " + ch.getMonto(),
                fmt.format(ch.getFechaEmision()),
                ch.getEstado()
            });
        }
    }

    // ========================= Log de Auditoría =========================

    private void cargarLog() {
        javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) jTableAuditoriaLog.getModel();
        modelo.setColumnIdentifiers(new String[]{"Fecha/Hora", "Acción", "Detalle"});
        modelo.setRowCount(0);
        // Mostrar en orden inverso (más reciente primero)
        java.util.List<String[]> log = bancoServicios.getLogAuditoria();
        for (int i = log.size() - 1; i >= 0; i--) {
            modelo.addRow(log.get(i));
        }
    }

    /**
     * Creates new form Area_Admin
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jlbUsuario = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnRechazar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblCuentasSolicitadas = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtBusquedaCedula = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TblClientes = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnBloquearUsuario = new javax.swing.JButton();
        btnVerAlertas = new javax.swing.JButton();
        JlbMetricas = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableAuditoriaLog = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        btnLimpiarLog = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        btnCobrarCheque1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        TblCheques = new javax.swing.JTable();
        btnRechazarCheque1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel96 = new javax.swing.JPanel();
        jLabel136 = new javax.swing.JLabel();
        btnCerrarSesionCorriente = new javax.swing.JButton();
        jLabel143 = new javax.swing.JLabel();
        jlbUsuario1 = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/LOGO.png"))); // NOI18N
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 320, -1));

        jLabel2.setFont(new java.awt.Font("Microsoft New Tai Lue", 3, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("BIENVENIDO !!");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 60, 360, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel6.setBackground(new java.awt.Color(0, 102, 102));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/LOGO.png"))); // NOI18N
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 320, -1));

        jLabel4.setFont(new java.awt.Font("Microsoft New Tai Lue", 3, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Centro de Administrador");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 450, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Usuario:");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 150, -1, 30));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 150, 130, -1));

        jlbUsuario.setFont(new java.awt.Font("Serif", 3, 18)); // NOI18N
        jlbUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(jlbUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 142, 210, 40));

        jPanel15.setBackground(new java.awt.Color(0, 102, 102));

        jTabbedPane1.setBackground(new java.awt.Color(0, 102, 102));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoAdmin.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 1036, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 595, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Administrativo", jPanel2);

        jPanel3.setBackground(new java.awt.Color(18, 52, 83));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel9.setText("Cuentas por Autorizar:");

        btnRechazar.setBackground(new java.awt.Color(0, 102, 102));
        btnRechazar.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnRechazar.setForeground(new java.awt.Color(255, 255, 255));
        btnRechazar.setText("Rechazar");
        btnRechazar.addActionListener(this::btnRechazarActionPerformed);

        btnAceptar.setBackground(new java.awt.Color(0, 102, 102));
        btnAceptar.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnAceptar.setForeground(new java.awt.Color(255, 255, 255));
        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(this::btnAceptarActionPerformed);

        TblCuentasSolicitadas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Cédula", "Nombre", "Tipo de Cuenta", "Ingresos Netos", "Dirección", "Motivo", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TblCuentasSolicitadas);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(btnRechazar, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 884, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRechazar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jTabbedPane1.addTab("Solicitudes Pendientes", jPanel3);

        jPanel7.setBackground(new java.awt.Color(18, 52, 83));

        jTabbedPane2.setBackground(new java.awt.Color(18, 52, 83));
        jTabbedPane2.setForeground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Búsqueda de Cliente por Cédula:");

        txtBusquedaCedula.addActionListener(this::txtBusquedaCedulaActionPerformed);

        btnBuscarCliente.setBackground(new java.awt.Color(0, 102, 102));
        btnBuscarCliente.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnBuscarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente.setText("BUSQUEDA");
        btnBuscarCliente.addActionListener(this::btnBuscarClienteActionPerformed);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Clientes en el Sistema:");

        TblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Cédula", "Nombre", "Correo", "Cuenta", "Error de Cuenta"
            }
        ));
        jScrollPane3.setViewportView(TblClientes);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Métricas del Sistema:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Total Clientes:");

        btnBloquearUsuario.setBackground(new java.awt.Color(0, 102, 102));
        btnBloquearUsuario.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnBloquearUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnBloquearUsuario.setText("BLOQUEAR USUARIO");

        btnVerAlertas.setBackground(new java.awt.Color(0, 102, 102));
        btnVerAlertas.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnVerAlertas.setForeground(new java.awt.Color(255, 255, 255));
        btnVerAlertas.setText("DESBLOQUEAR USUARIO");
        btnVerAlertas.addActionListener(this::btnVerAlertasActionPerformed);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBusquedaCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(JlbMetricas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(423, 423, 423)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(btnBloquearUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)
                        .addComponent(btnVerAlertas, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBusquedaCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jLabel11)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(JlbMetricas, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnVerAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBloquearUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        jTabbedPane2.addTab("Auditoria Clientes", jPanel8);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setForeground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("------------------------------------------------------------------- REGISTRO DE AUDITORÍA -----------------------------------------------------------");

        jTableAuditoriaLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Fecha/ Hora", "Acción", "Detalle"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableAuditoriaLog);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        btnLimpiarLog.setText("Limpiar Log");
        btnLimpiarLog.addActionListener(this::btnLimpiarLogActionPerformed);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(119, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(btnLimpiarLog, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(251, 251, 251))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 790, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnLimpiarLog, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 34, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("Auditoria Administración", jPanel12);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1011, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Auditoría", jPanel7);

        jPanel9.setBackground(new java.awt.Color(18, 52, 83));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel15.setText("Cheques en Espera de Verificación:");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel16.setText("Acciones sobre el Cheque Seleccionado:");

        btnCobrarCheque1.setBackground(new java.awt.Color(0, 102, 102));
        btnCobrarCheque1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnCobrarCheque1.setForeground(new java.awt.Color(255, 255, 255));
        btnCobrarCheque1.setText("COBRAR CHEQUE");
        btnCobrarCheque1.addActionListener(this::btnCobrarCheque1ActionPerformed);

        TblCheques.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Número de Cheque", "Titular", "Beneficiario", "Monto", "Fecha Cobro", "Problema"
            }
        ));
        jScrollPane4.setViewportView(TblCheques);

        btnRechazarCheque1.setBackground(new java.awt.Color(0, 102, 102));
        btnRechazarCheque1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnRechazarCheque1.setForeground(new java.awt.Color(255, 255, 255));
        btnRechazarCheque1.setText("RECHAZAR CHEQUE");
        btnRechazarCheque1.addActionListener(this::btnRechazarCheque1ActionPerformed);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 842, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnCobrarCheque1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                .addComponent(btnRechazarCheque1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(87, 87, 87))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addGap(34, 34, 34)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCobrarCheque1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRechazarCheque1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cheques en espera", jPanel9);

        jPanel10.setBackground(new java.awt.Color(18, 52, 83));

        jPanel96.setBackground(new java.awt.Color(255, 255, 255));

        jLabel136.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Perfil.png"))); // NOI18N
        jLabel136.setText("jLabel28");

        btnCerrarSesionCorriente.setBackground(new java.awt.Color(0, 102, 102));
        btnCerrarSesionCorriente.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnCerrarSesionCorriente.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesionCorriente.setText("Cerrar Sesión");
        btnCerrarSesionCorriente.addActionListener(this::btnCerrarSesionCorrienteActionPerformed);

        jLabel143.setFont(new java.awt.Font("Segoe Script", 1, 18)); // NOI18N
        jLabel143.setText("Hasta la Próxima!!!");

        jlbUsuario1.setFont(new java.awt.Font("Serif", 3, 24)); // NOI18N
        jlbUsuario1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel96Layout = new javax.swing.GroupLayout(jPanel96);
        jPanel96.setLayout(jPanel96Layout);
        jPanel96Layout.setHorizontalGroup(
            jPanel96Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel96Layout.createSequentialGroup()
                .addGap(0, 45, Short.MAX_VALUE)
                .addComponent(btnCerrarSesionCorriente, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
            .addGroup(jPanel96Layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel96Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
            .addGroup(jPanel96Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel96Layout.createSequentialGroup()
                    .addContainerGap(150, Short.MAX_VALUE)
                    .addComponent(jlbUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(114, 114, 114)))
        );
        jPanel96Layout.setVerticalGroup(
            jPanel96Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel96Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel136)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCerrarSesionCorriente, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
            .addGroup(jPanel96Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel96Layout.createSequentialGroup()
                    .addContainerGap(234, Short.MAX_VALUE)
                    .addComponent(jlbUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(175, 175, 175)))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(328, Short.MAX_VALUE)
                .addComponent(jPanel96, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(257, 257, 257))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jPanel96, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cerrar Sesión", jPanel10);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        cargarClientes(txtBusquedaCedula.getText().trim());
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnCerrarSesionCorrienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionCorrienteActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?",
                "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Acceso_Administrador acceso = new Acceso_Administrador(bancoServicios);
            acceso.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnCerrarSesionCorrienteActionPerformed

    private void btnCobrarCheque1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarCheque1ActionPerformed
        int fila = TblCheques.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cheque de la tabla primero.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String numeroCheque = TblCheques.getValueAt(fila, 0).toString();

        try {
            Cheque resultado = bancoServicios.cobrarCheque(numeroCheque);

            if (resultado.getEstado().equals("Cobrado")) {
                JOptionPane.showMessageDialog(this,
                        " Cheque " + numeroCheque + " cobrado con éxito.\n"
                        + "Se descontaron $ " + resultado.getMonto()
                        + " de la cuenta del titular.",
                        "Cheque cobrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        " Cheque " + numeroCheque + " rechazado.\n"
                        + "Motivo: " + resultado.getEstado() + "\n"
                        + "El cliente verá la advertencia al iniciar sesión.",
                        "Fondos insuficientes", JOptionPane.WARNING_MESSAGE);
            }

            cargarCheques();
            cargarClientes(txtBusquedaCedula.getText().trim());
            cargarLog();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCobrarCheque1ActionPerformed

    private void btnRechazarCheque1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechazarCheque1ActionPerformed
        int fila = TblCheques.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cheque de la tabla primero.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String numeroCheque = TblCheques.getValueAt(fila, 0).toString();

        String motivo = JOptionPane.showInputDialog(this,
                "Indique el motivo del rechazo para el cheque " + numeroCheque + ":");
        if (motivo == null || motivo.trim().isEmpty()) return;

        try {
            bancoServicios.rechazarChequeManual(numeroCheque, motivo.trim());
            JOptionPane.showMessageDialog(this,
                    "Cheque " + numeroCheque + " rechazado.\n"
                    + "El cliente será notificado al iniciar sesión.",
                    "Cheque rechazado", JOptionPane.INFORMATION_MESSAGE);
            cargarCheques();
            cargarLog();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRechazarCheque1ActionPerformed

    private void txtBusquedaCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaCedulaActionPerformed
        // Busca al presionar Enter en el campo de cédula
        cargarClientes(txtBusquedaCedula.getText().trim());
    }//GEN-LAST:event_txtBusquedaCedulaActionPerformed

    private void btnVerAlertasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerAlertasActionPerformed
        // Refresca la tabla de clientes y métricas
        cargarClientes(txtBusquedaCedula.getText().trim());
    }//GEN-LAST:event_btnVerAlertasActionPerformed

    private void btnBloquearUsuarioActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = TblClientes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cedula = TblClientes.getValueAt(fila, 0).toString().trim();

        try {
            CapaEntidades.Cliente c = bancoServicios.obtenerClientePorCedula(cedula);
            if (c == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Seleccionar qué cuenta operar
            String[] tipos = {"Cuenta Ahorros", "Cuenta Corriente"};
            int tipoSel = JOptionPane.showOptionDialog(this,
                    "Seleccione la cuenta a gestionar para " + c.getNombre() + " " + c.getApellido() + ":",
                    "Gestionar cuenta", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, tipos, tipos[0]);
            if (tipoSel < 0) return;

            String tipoCuenta = (tipoSel == 0) ? "AHORROS" : "CORRIENTE";
            String tipoCuentaLabel = tipos[tipoSel];
            boolean estaBloqueada = bancoServicios.esCuentaBloqueada(cedula, tipoCuenta);

            if (estaBloqueada) {
                int resp = JOptionPane.showConfirmDialog(this,
                        "La " + tipoCuentaLabel + " de " + c.getNombre() + " está BLOQUEADA.\n¿Desea DESBLOQUEARLA?",
                        "Desbloquear", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    bancoServicios.desbloquearCuenta(cedula, tipoCuenta);
                    JOptionPane.showMessageDialog(this,
                            tipoCuentaLabel + " de " + c.getNombre() + " DESBLOQUEADA correctamente.",
                            "Cuenta desbloqueada", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                int resp = JOptionPane.showConfirmDialog(this,
                        "¿Bloquear la " + tipoCuentaLabel + " de " + c.getNombre() + "?\n"
                        + "El cliente NO podrá acceder a esa cuenta hasta que la desbloquee.",
                        "Bloquear cuenta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    bancoServicios.bloquearCuenta(cedula, tipoCuenta);
                    JOptionPane.showMessageDialog(this,
                            tipoCuentaLabel + " de " + c.getNombre() + " BLOQUEADA correctamente.",
                            "Cuenta bloqueada", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            cargarClientes(txtBusquedaCedula.getText().trim());
            cargarLog();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        Solicitud seleccionada = obtenerSolicitudSeleccionada();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una solicitud de la tabla primero.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            bancoServicios.resolverSolicitud(seleccionada.getId(), true);

            String mensaje;
            if (seleccionada.getTipo().equals("REGISTRO") && seleccionada.getClienteEnEspera() != null) {
                CapaEntidades.Cliente c = seleccionada.getClienteEnEspera();
                mensaje = "REGISTRO APROBADO para " + seleccionada.getNombreCliente() + "\n\n"
                        + "Cuenta Ahorros: " + c.getNumeroCuentaAhorros() + "\n"
                        + "Cuenta Corriente: " + c.getNumeroCuentaCorriente() + "\n"
                        + "Usuario Ahorros: " + c.getUsuario() + "\n"
                        + (c.getUsuarioCorriente() != null ? "Usuario Corriente: " + c.getUsuarioCorriente() + "\n" : "")
                        + "\nEl cliente ya puede ingresar al sistema con sus credenciales.";
            } else if (seleccionada.getTipo().equals("LIMITE")) {
                mensaje = "Solicitud de " + seleccionada.getNombreCliente() + " ACEPTADA.\n"
                        + "Nuevo límite aplicado: $ " + seleccionada.getMontoSolicitado();
            } else if (seleccionada.getTipo().equals("APERTURA")) {
                String detalleApertura = seleccionada.getDetalle() != null ? seleccionada.getDetalle() : "";
                String tipoCuentaTxt = detalleApertura.contains("Cuenta Corriente") ? "Cuenta Corriente" : "Cuenta Ahorros";
                mensaje = "APERTURA ACEPTADA para " + seleccionada.getNombreCliente() + "\n\n"
                        + "Se activó su " + tipoCuentaTxt + ".\n"
                        + "El cliente ya puede ingresar con el usuario que se le asignó al solicitarla.";
            } else {
                mensaje = "Solicitud de " + seleccionada.getNombreCliente() + " ACEPTADA.\n"
                        + "El cliente puede proceder con su trámite.";
            }

            JOptionPane.showMessageDialog(this, mensaje, "Solicitud aceptada", JOptionPane.INFORMATION_MESSAGE);
            cargarSolicitudes();
            cargarClientes(txtBusquedaCedula.getText().trim());
            cargarLog();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnRechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechazarActionPerformed
        Solicitud seleccionada = obtenerSolicitudSeleccionada();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una solicitud de la tabla primero.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            bancoServicios.resolverSolicitud(seleccionada.getId(), false);
            JOptionPane.showMessageDialog(this,
                    "Solicitud de " + seleccionada.getNombreCliente() + " RECHAZADA.",
                    "Solicitud rechazada", JOptionPane.INFORMATION_MESSAGE);
            cargarSolicitudes();
            cargarLog();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRechazarActionPerformed

    private void btnLimpiarLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarLogActionPerformed
        int conf = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea limpiar todo el log de auditoría?\n"
                + "Esta acción no se puede deshacer.",
                "Confirmar limpieza", javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);
        if (conf == javax.swing.JOptionPane.YES_OPTION) {
            bancoServicios.limpiarLogAuditoria();
            bancoServicios.registrarAuditoria("LOG LIMPIADO",
                    "El administrador " + usuarioAdmin + " limpió el log de auditoría.");
            cargarLog();
        }
    }//GEN-LAST:event_btnLimpiarLogActionPerformed

    /**
     * @param args the command line arguments
     */
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JlbMetricas;
    private javax.swing.JTable TblCheques;
    private javax.swing.JTable TblClientes;
    private javax.swing.JTable TblCuentasSolicitadas;
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnBloquearUsuario;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnCerrarSesionCorriente;
    private javax.swing.JButton btnCobrarCheque1;
    private javax.swing.JButton btnLimpiarLog;
    private javax.swing.JButton btnRechazar;
    private javax.swing.JButton btnRechazarCheque1;
    private javax.swing.JButton btnVerAlertas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel96;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableAuditoriaLog;
    private javax.swing.JLabel jlbUsuario;
    private javax.swing.JLabel jlbUsuario1;
    private javax.swing.JTextField txtBusquedaCedula;
    // End of variables declaration//GEN-END:variables
}
