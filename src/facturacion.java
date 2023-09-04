// PERMITE CREAR INTERFACES DE USUARIO
import javax.swing.*;
// PERMITE MANEJAR TABLAS
import javax.swing.table.DefaultTableModel;
// PERMITE MANEJAR LAS ACCIONES DE BOTONES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// PERMITE TRABAJR CON JDBC, PARA LA CONEXIÓN CON LA BASE DE DATOS
import java.sql.*;
public class facturacion {
    public JPanel Panel; // CONTENEDOR DE ELEMENTOS
    Statement s; // ENVIA CONSULTAS SQL A LA BDD
    ResultSet rs; // ALMACENA LOS RESULTADOS DE LA CONSULTA
    PreparedStatement ps; // PERMITE PARAMETRIZAR LAS CONSULTAS EN BDD (ACTUALIZAR,INSERCIÓN)
    ResultSetMetaData rsmd; // SE OBTIENE LA INFORMACIÓN DE LOS METADATOS DE RS
    int res; // RESULTADO DE CONSULTA (INSERCIÓN,ACTUALIZACIÓN, ELIMINACIÓN)
    private JFormattedTextField textFACTURA; // NÚMERO DE FACTURA
    private JFormattedTextField textFECHA; // FECHA DE FACTURACIÓN
    private JFormattedTextField textCEDULA; // NÚMERO DE CÉDULA DEL CLIENTE
    private JFormattedTextField textNOMBRE; // NOMBRES COPLETOS DEL CLIENTE
    private JFormattedTextField textCUENTA; // N° DE CUENTA DEL CLIENTE
    private JFormattedTextField textTOTAL; // VALOR FINAL DE LA FACTURA
    private JTable table; // PERMITE MOSTRAR LOS DATOS EN FORMA DE TABLA
    private JPanel Pago_Cliente; // PANEL DEL CLIENTE
    private JFormattedTextField textValor; // VALOR ENTREGADO POR EL CLIENTE DESPUÉS DE LA COMPRA
    private JButton PagoButton; // BOTÓN PAGO: CALCULA EL CAMBIO
    private JFormattedTextField textCambio; // RECIBE EL CAMBIO CORRESPONDIENTE
    private JButton agregarPagoButton; // BOTÓN AGREGAR PAGO: SI EL CLIENTE QUIERE ENTREGAR UN VALOR DESPUÉS DE LA COMPRA
    private JButton agregarACuentaButton; // BOTÓN AGREGAR A CUENTA: AGREGAR AL SALDO DE CUENTA
    String Fac;// RECIBE EL NÚMERO DE LA FACTURA CON UNA SINTÁXIS CORRECTA
    String ci;// RECIBE EL NÚMERO DE CÉDULA CON UNA SINTÁXIS CORRECTA
    DefaultTableModel modelo = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS QUE SE MOSTRARÁN EN LA TABLA
    JFormattedTextField actualizar_valor_cuenta = new JFormattedTextField();// RECIBE EL VALOR DE CUENTA ACTUALIZADO DEL CLIENTE

    public facturacion(){
        // BLOQUEO DE EDICIÓN EN CLIENTE
        Pago_Cliente.setVisible(false);
        textFECHA.setEnabled(false);
        textFACTURA.setEnabled(false);
        textCEDULA.setEnabled(false);
        textNOMBRE.setEnabled(false);
        textCUENTA.setEnabled(false);
        textTOTAL.setEnabled(false);

        try{// INICIO DBB CARGAR CABECERA DE FACTURA
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: ÚLTIMO NÚMERO DE LA FACTURA
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM cab_trans WHERE num_f = (SELECT MAX(CAST(num_f AS UNSIGNED)) FROM cab_trans)");
            // SE OBTIENE LOS DATOS DE LOS CAMPOS REQUERIDO
            while (rs.next()) {
                textFACTURA.setText(rs.getString(1));
                textFECHA.setText(rs.getString(2));
                textTOTAL.setText(rs.getString(3));
                textCEDULA.setText(rs.getString(4));
            }
            // CIERRE DE CONEXIÓN
            conexion.close();
            s.close();
        }catch(Exception ex){
            ex.printStackTrace();
        } // INICIO DBB CARGAR CABECERA DE FACTURA


        try{ // INICIO BDD CARGAR DETALLE DE FACTURA
            //CONEXION BDD
            Connection conexion;
            conexion = getConection();
            // SE OBTIENE LA CÉDULA EN UN FORMATO ADECUADO
            Fac = "\"" + textFACTURA.getText()+"\"";
            // CONSULTA: CARGAR DATOS DE DETALLE DE FACTURA
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT FKid_producto as PRODUCTO, cantidad_dt, precio_dt FROM det_trans WHERE FKnum_f = " + Fac);

            // SE OBTIENE LOS DATOS
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // SE CREA LA TABLA Y EL MODELO
            modelo = (DefaultTableModel) table.getModel();

            // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
            for (int i = 1; i <= columnCount; i++) {
                modelo.addColumn(rsmd.getColumnName(i));
            }

            // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo.addRow(row);
            }
            // CIERRE DE CONEXIÓN
            rs.close();
            s.close();
            conexion.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        } // FIN BDD CARGAR DETALLE DE FACTURA

        try { // INICIO BDD CARGAR DATOS DE UN CLIENTE
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            //
            ci = textCEDULA.getText();
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM cliente WHERE ci_cl =" + ci);
            // SE OBTIENEN LOS DATOS REQUERIDOS DE CLIENTES
            while (rs.next()) {
                if (textCEDULA.getText().equals(rs.getString(1))){
                    textNOMBRE.setText(rs.getString(2));
                    textCUENTA.setText(rs.getString(3));
                } else {
                    JOptionPane.showMessageDialog(null, "DATOS NO ENCONTRADOS");
                }
            }
            // CIERRE DE CONEXIÓN BDD
            conexion.close();
            rs.close();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } // FIN BDD CARGAR DATOS DE UN CLIENTE

        agregarPagoButton.addActionListener(new ActionListener() {// INICIO ACCIÓN AGREGAR PAGO
            @Override
            public void actionPerformed(ActionEvent e) {
                Pago_Cliente.setVisible(true);
            } // SE ACTIVA EL BOTON
        }); // FIN ACCIÓN AGREGAR
        PagoButton.addActionListener(new ActionListener() { // INICIO ACCIÓN PAGO
            @Override
            public void actionPerformed(ActionEvent e) {
                Pago_Cliente.setVisible(true); //
                // SE CALCULA EL CAMBIO Y SE PRESENTA POR PANTALLA
                textCambio.setText(String.valueOf(Float.parseFloat(textValor.getText()) - Float.parseFloat(textTOTAL.getText())));
            }
        }); // FIN ACCIÓN PAGO
        agregarACuentaButton.addActionListener(new ActionListener() { // INICIO ACCIÓN AGREGAR A CUENTA
            @Override
            public void actionPerformed(ActionEvent e) {
                try{ // INICIO BDD CARGAR CLIENTE
                    Connection conexion;
                    conexion = getConection();

                    String cod2 = "\"" + textCEDULA.getText() + "\"";
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT estado_cuenta FROM cliente WHERE ci_cl =" + cod2);
                    while (rs.next()) {
                        textCUENTA.setText(rs.getString(1));
                    }

                    ps = conexion.prepareStatement("UPDATE cliente SET estado_cuenta=? WHERE ci_cl = " + cod2);
                    actualizar_valor_cuenta.setText(String.valueOf(Float.parseFloat(textValor.getText())+Float.parseFloat(textCUENTA.getText())));
                    ps.setString(1, actualizar_valor_cuenta.getText());
                    textCUENTA.setText(actualizar_valor_cuenta.getText());

                    res = ps.executeUpdate();
                    if(!(res >0)){
                        JOptionPane.showMessageDialog(null,"CANTIDAD DE PRODUCTO NO ACTUALIZADO");
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    ps.close();
                    s.close();
                    rs.close();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }// INICIO BDD CARGAR CLIENTE
            }
        }); // FIN ACCIÓN AGREGAR A CUENTA
    }

    public static Connection getConection() throws RuntimeException { // SE GENERA LA CONEXIÓN CON LA BDD
        Connection conexion;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost/tienda", "root", "root"
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conexion;
    }
}
