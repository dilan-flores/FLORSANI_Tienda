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
    Statement s;
    ResultSet rs;
    PreparedStatement ps;
    ResultSetMetaData rsmd;
    int res;
    private JFormattedTextField textFACTURA;
    private JFormattedTextField textFECHA;
    public JPanel Panel;
    private JFormattedTextField textCEDULA;
    private JFormattedTextField textNOMBRE;
    private JFormattedTextField textCUENTA;
    private JFormattedTextField textTOTAL;
    private JTable table;
    private JButton agregarPagoButton;
    private JPanel Pago_Cliente;
    private JFormattedTextField textValor;
    private JButton PagoButton;
    private JFormattedTextField textCambio;
    private JButton agregarACuentaButton;
    String Fac;// Obtiene el número de de factura con una sintáxis correcta
    String ci;// Obtiene la cédula del cliente con una sintáxis correcta
    DefaultTableModel modelo = new DefaultTableModel();

    JFormattedTextField actualizar_valor_cuenta = new JFormattedTextField();/*actualizacón el valor de cuenta del cliente*/

    public facturacion(){
        Pago_Cliente.setVisible(false);

        textFECHA.setEnabled(false);
        textFACTURA.setEnabled(false);
        textCEDULA.setEnabled(false);
        textNOMBRE.setEnabled(false);
        textCUENTA.setEnabled(false);
        textTOTAL.setEnabled(false);

        try{//Cabecera de transacción
            Connection conexion;
            conexion = getConection();

            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM cab_trans ORDER by num_f DESC LIMIT 1");

            while (rs.next()) {
                textFACTURA.setText(rs.getString(1));
                textFECHA.setText(rs.getString(2));
                textTOTAL.setText(rs.getString(3));
                textCEDULA.setText(rs.getString(4));
            }

            conexion.close();
            s.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }


        try{ // Se abre tabla "det_trans": detalle de la factura
            Connection conexion;
            conexion = getConection();

            Fac = "\"" + textFACTURA.getText()+"\"";
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT FKid_producto as PRODUCTO, cantidad_dt, precio_dt FROM det_trans WHERE FKnum_f = " + Fac);

            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();


            // Create JTable and set model
            /*table = new JTable();*/
            modelo = (DefaultTableModel) table.getModel();

            // Add columns to table model
            for (int i = 1; i <= columnCount; i++) {
                modelo.addColumn(rsmd.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo.addRow(row);
            }
            rs.close();
            s.close();
            conexion.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        try { // Se abre tabla "cliente"
            Connection conexion;
            conexion = getConection();

            ci = textCEDULA.getText();
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM cliente WHERE ci_cl =" + ci);

            while (rs.next()) {
                if (textCEDULA.getText().equals(rs.getString(1))){
                    textNOMBRE.setText(rs.getString(2));
                    textCUENTA.setText(rs.getString(3));
                } else {
                    JOptionPane.showMessageDialog(null, "DATOS NO ENCONTRADOS");
                }
            }
            conexion.close();
            rs.close();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        agregarPagoButton.addActionListener(new ActionListener() {/*CERRAR Y PASAR A VENTANA ANTERIOR(LOGIN)*/
            @Override
            public void actionPerformed(ActionEvent e) {
                Pago_Cliente.setVisible(true);
            }
        });
        PagoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pago_Cliente.setVisible(true);
                textCambio.setText(String.valueOf(Float.parseFloat(textValor.getText()) - Float.parseFloat(textTOTAL.getText())));
            }
        });
        agregarACuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{//Se abre la tabla "cliente"
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

                    conexion.close();//importante!!!!
                    ps.close();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }// Fin tabla "cliente"
            }
        });
    }

    public static Connection getConection() throws RuntimeException {
        Connection conexion;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda", "root", "root"
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conexion;
    }
}
