// PERMITE CREAR INTERFACES DE USUARIO
import javax.swing.*;
// PERMITE MANEJAR TABLAS
import javax.swing.table.DefaultTableModel;
// PERMITE TRABAJR CON JDBC, PARA LA CONEXIÓN CON LA BASE DE DATOS
import java.sql.*;
// PERMITE MANEJAR LAS ACCIONES DE BOTONES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// PERMITE RESTRINGUIR A SOLO NÚMEROS
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Admin_producto {
    Statement s;
    ResultSet rs;
    PreparedStatement ps;
    int res;
    ResultSetMetaData rsmd;
    public JPanel Panel;
    private JComboBox id_Producto;
    private JFormattedTextField textPRODUCTO;
    private JFormattedTextField textPRECIO;
    private JLabel Ganancia;
    private JFormattedTextField textGanancia;
    private JLabel PrecioVenta;
    private JFormattedTextField textPrecioVenta;
    private JFormattedTextField textCANTIDAD;
    private JTable table;
    private JButton buscarButton;
    private JLabel ValorTotal;
    private JFormattedTextField textPrecioTotal;
    private JButton actualizarButton;
    private JLabel FechaRegistro;
    private JFormattedTextField textFECHA;
    private JFormattedTextField textFechaRegistrada;
    DefaultTableModel modelo = new DefaultTableModel();

    // Almacena el ID del producto
    String cod;
    // Almacena la actualización de precio de venta
    JFormattedTextField actualizar_precio_venta = new JFormattedTextField();/*actualizacón de precios de producto en inventario*/
    // Almacena la actualización de precio total
    JFormattedTextField actualizar_precio_total = new JFormattedTextField();/*actualizacón de precios de producto en inventario*/

    public Admin_producto(){
        AbstractDocument doc = (AbstractDocument) textCANTIDAD.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                StringBuilder sb = new StringBuilder();
                for (char c : string.toCharArray()) {
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                super.insertString(fb, offset, sb.toString(), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, null, attrs);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (char c : text.toCharArray()) {
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                super.replace(fb, offset, length, sb.toString(), attrs);
            }
        });
        textPrecioVenta.setEnabled(false);
        textPrecioTotal.setEnabled(false);
        textFECHA.setEnabled(false);
        textFechaRegistrada.setEnabled(false);
        textCANTIDAD.setEnabled(false);

        try{ // Se abre tabla "productos e inventario"
            Connection conexion;
            conexion = getConection();

            s = conexion.createStatement();
            rs = s.executeQuery("SELECT producto.*, inventario.cantidad, inventario.precio_total  FROM (producto,inventario) WHERE id_producto=FK_id_producto");

            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Create JTable and set model
            /*table = new JTable();*/
            modelo = (DefaultTableModel) table.getModel();

            modelo.setColumnCount(0);// Se elimina la columna de la tabla
            modelo.setRowCount(0); // Se elimina las filas de la tabla

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

        try {/*Almacena en los JComboBox todos los datos de la BD*/
            Connection conexion;
            conexion = getConection();

            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM producto ");

            // Elimina datos iniciales en ComboBox
            id_Producto.removeAllItems();
            id_Producto.addItem(" ");
            while (rs.next()) {
                id_Producto.addItem(rs.getString(1));
            }
            conexion.close();
            rs.close();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conexion;
                    conexion = getConection();

                    cod = (String)id_Producto.getSelectedItem();
                    System.out.println(cod);
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT (Select producto from producto where id_producto = FK_id_producto) as producto," +
                            "(Select inversion from producto where id_producto = FK_id_producto) as precio_unitario," +
                            "(Select ganancia from producto where id_producto = FK_id_producto) as ganancia," +
                            "(Select precio_venta_unitario from producto where id_producto = FK_id_producto) as precio_venta," +
                            "fecha_registro, fecha_actual, cantidad, precio_total FROM inventario WHERE FK_id_producto =" + cod);

                    while (rs.next()) {
                        textPRODUCTO.setText(rs.getString(1));
                        textPRECIO.setText(rs.getString(2));
                        textGanancia.setText(rs.getString(3));
                        textPrecioVenta.setText(rs.getString(4));
                        textFechaRegistrada.setText(rs.getString(5));
                        textFECHA.setText(rs.getString(6));
                        textCANTIDAD.setText(rs.getString(7));
                        textPrecioTotal.setText(rs.getString(8));
                    }

                    /*if(!encontrado){
                        JOptionPane.showMessageDialog(null, "CAJERO NO ENCONTRADOS");
                    }else{
                        modelo.setColumnCount(0);// Se elimina la columna de la tabla
                        modelo.setRowCount(0);// Se elimina la fila de la tabla
                    }*/
                    conexion.close();
                    rs.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {// Se abre la tabla Productos
                    Connection conexion;
                    conexion = getConection();
                    ps = conexion.prepareStatement("UPDATE producto SET producto=?, inversion=? , ganancia=? , precio_venta_unitario=? WHERE id_producto = " + cod);
                    actualizar_precio_venta.setText(String.valueOf(Float.parseFloat(textPRECIO.getText())+Float.parseFloat(textGanancia.getText())));
                    ps.setString(1, textPRODUCTO.getText());
                    ps.setString(2, textPRECIO.getText());
                    ps.setString(3, textGanancia.getText());
                    ps.setString(4, actualizar_precio_venta.getText());

                    textPrecioVenta.setText(actualizar_precio_venta.getText());


                    res = ps.executeUpdate();
                    if (!(res > 0)) {
                        JOptionPane.showMessageDialog(null, "DATOS NO GUARDADOS EN PRODUCTOS");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {// Se abre la tabla Inventario
                    Connection conexion;
                    conexion = getConection();
                    ps = conexion.prepareStatement("UPDATE inventario SET precio_total=? WHERE FK_id_producto = " + cod);
                    actualizar_precio_total.setText(String.valueOf(Integer.parseInt(textCANTIDAD.getText())*Float.parseFloat(textPrecioVenta.getText())));
                    ps.setString(1, actualizar_precio_total.getText());

                    textPrecioTotal.setText(actualizar_precio_total.getText());

                    res = ps.executeUpdate();
                    if (!(res > 0)) {
                        JOptionPane.showMessageDialog(null, "DATOS NO GUARDADOS EN INVENTARIO");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try{ // Se abre tabla "productos e inventario"
                    Connection conexion;
                    conexion = getConection();

                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT producto.*, inventario.cantidad, inventario.precio_total  FROM (producto,inventario) WHERE id_producto=FK_id_producto");

                    rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    // Create JTable and set model
                    /*table = new JTable();*/
                    modelo = (DefaultTableModel) table.getModel();

                    modelo.setColumnCount(0);// Se elimina la columna de la tabla
                    modelo.setRowCount(0); // Se elimina las filas de la tabla

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
            }
        });
    }
    public static Connection getConection() {
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
