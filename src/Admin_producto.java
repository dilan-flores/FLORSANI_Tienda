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
    public JPanel Panel; // // CONTENEDOR DE ELEMENTOS
    Statement s; // ENVÍA CONSULTAS SQL A LA BDD
    ResultSet rs; // ALMACENA LOS RESULTADOS DE LA CONSULTA
    PreparedStatement ps; // PERMITE PARAMETRIZAR LAS CONSULTAS EN BDD (ACTUALIZAR, INSERCIÓN)
    int res; // RESULTADOS DE CONSULTA (INSERCIÓN, ACTUALIZAR, ELIMINAR)
    ResultSetMetaData rsmd; // SE OBTIENE LA INFORMACIÓN DE LOS METADATOS DE RS
    private JComboBox id_Producto; // COMBO-BOX DE PRODUCTOS
    private JFormattedTextField textPRODUCTO; // NOMBRE DEL PRODUCTO
    private JFormattedTextField textPRECIO; // PRECIO DEL PRODUCTO
    private JFormattedTextField textGanancia; // GANACIA DE PRODCUTOS
    private JFormattedTextField textPrecioVenta; // PRECIO DE VENTA DEL PRODUCTO
    private JFormattedTextField textCANTIDAD; // CANTIDAD DE PRODUCTO EXISTENTE
    private JTable table; // PERMITE MOSTRAR LOS DATOS EN FORMA DE TABLA
    private JButton buscarButton; // BOTÓN BUSCAR PRODUCTO
    private JFormattedTextField textPrecioTotal; // PRECIO TOTAL DE CADA PRODUCTO
    private JButton actualizarButton; // BOTÓN ACTUALIZAR: ACTUALIZASR CANTIDAD, PRECIO DEL PRODUCTO
    private JFormattedTextField textFECHA; // FECHA ACTUAL
    private JFormattedTextField textFechaRegistrada; // FECHA REGISTRADA DE LA FACTURA
    DefaultTableModel modelo = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS QUE SE MOSTRARÁN EN LA TABLA
    String cod; // RECIBE EL ID DEL PRODUCTO EN UN FORMATO ADECUADO
    JFormattedTextField actualizar_precio_venta = new JFormattedTextField(); // ACTUALIZAR EL PRECIO DE VENTA DEL PRODUCTO
    JFormattedTextField actualizar_precio_total = new JFormattedTextField(); // ACTUALIZAR EL PRECIO TOTAL DEL PRODUCTO EN INVENTARIO

    public Admin_producto(){
        // CONFIGURACIÓN DEL DOCUMENTFILTER PARA RESTRINGUIR LOS CARACTERES A NÚMEROS
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
        // BLOQUEO DE EDICIÓN EN PRODUCTOS E INVENTARIO
        textPrecioVenta.setEnabled(false);
        textPrecioTotal.setEnabled(false);
        textFECHA.setEnabled(false);
        textFechaRegistrada.setEnabled(false);
        textCANTIDAD.setEnabled(false);

        try{ // INICIO BDD CARGAR PRODUCTOS E INVENTARIO
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT producto.*, inventario.cantidad, inventario.precio_total, inventario.fecha_registro as Fecha_factura  FROM (producto,inventario) WHERE id_producto=FK_id_producto");

            // SE OBTIENE LOS DATOS
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            // SE CREA LA TABLA Y EL MODELO
            modelo = (DefaultTableModel) table.getModel();

            // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
            modelo.setColumnCount(0);// Se elimina la columna de la tabla
            modelo.setRowCount(0); // Se elimina las filas de la tabla

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
        } // INICIO BDD CARGAR PRODUCTOS E INVENTARIO

        try {// INICIO BDD CARGAR PRODCUTOS
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: OBTENER PRODUCTOS
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM producto ");

            // ELIMINACIÓN DE DATOS INICIALES EN COMBO BOX
            id_Producto.removeAllItems();
            id_Producto.addItem(" ");
            // SE OBTIENEN LOS DATOS REQUERIDOS DE PRODUCTOS
            while (rs.next()) {
                id_Producto.addItem(rs.getString(1));
            }
            // CIERRE DE CONEXIÓN
            conexion.close();
            rs.close();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } // FIN BDD CARGAR PRODCUTOS

        buscarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN BUSCAR PRODCUTO
            @Override
            public void actionPerformed(ActionEvent e) {
                try { // INICIO BDD CARGAR PRODUCTOE INVENTARIO
                    // OBTIENE CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // OBTIENE EL ID DEL PRODUCTO DEL COMBO BOX
                    cod = (String)id_Producto.getSelectedItem();
                    //System.out.println(cod);
                    // CONSULTA: SE OBTIENE LOS DATOS DE LA TABLA PRODUCTOS POR MEDIO DE LA TALBLA INVENTARIO Y CON UN ID ESPECÍFICO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT (Select producto from producto where id_producto = FK_id_producto) as producto," +
                            "(Select inversion from producto where id_producto = FK_id_producto) as precio_unitario," +
                            "(Select ganancia from producto where id_producto = FK_id_producto) as ganancia," +
                            "(Select precio_venta_unitario from producto where id_producto = FK_id_producto) as precio_venta," +
                            "fecha_registro, fecha_actual, cantidad, precio_total FROM inventario WHERE FK_id_producto =" + cod);

                    // SE OBTIENE LOS RESULTADOS Y SE PRESENTAN POR PANTALLA
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
                    //CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD CARGAR PRODUCTOE INVENTARIO
            }
        }); // FIN ACCIÓN BUSCAR PRODUCTO
        actualizarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN ACTUALIZAR PRODUCTO
            @Override
            public void actionPerformed(ActionEvent e) {
                try {// INICIO BDD ACTUALIZAR PRODUCTO
                    // SE OBTIENE CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    //CONSULTA PARAMETRIZADA: ACTUALIZAR EL NOMBRE, INVERSIÓN, GANANCIA Y ASOCIADOS
                    ps = conexion.prepareStatement("UPDATE producto SET producto=?, inversion=? , ganancia=? , precio_venta_unitario=? WHERE id_producto = " + cod);
                    actualizar_precio_venta.setText(String.valueOf(Float.parseFloat(textPRECIO.getText())+Float.parseFloat(textGanancia.getText())));
                    ps.setString(1, textPRODUCTO.getText());
                    ps.setString(2, textPRECIO.getText());
                    ps.setString(3, textGanancia.getText());
                    ps.setString(4, actualizar_precio_venta.getText());
                    // SE PRESENTA POR PANTALLA EL PRECIO DE VENTA ACTUALIZADO
                    textPrecioVenta.setText(actualizar_precio_venta.getText());

                    // RESULTADO DE LA ACTUALIZACIÓN
                    res = ps.executeUpdate();
                    if (!(res > 0)) {
                        JOptionPane.showMessageDialog(null, "DATOS NO GUARDADOS EN PRODUCTOS");
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    ps.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD ACTUALIZAR PRODUCTO

                try {// INICIO BDD ACTUALIZAR INVENTARIO
                    // SE OBTIENE CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA PARAMETRIZADA: ACTUALIZACIÓN PRECIO TOTAL EN INVENTARIO
                    ps = conexion.prepareStatement("UPDATE inventario SET precio_total=? WHERE FK_id_producto = " + cod);
                    // SE CALCULA EL PRECIO TOTAL: CANTIDAD * PRECIO DE VENTA
                    actualizar_precio_total.setText(String.valueOf(Integer.parseInt(textCANTIDAD.getText())*Float.parseFloat(textPrecioVenta.getText())));
                    ps.setString(1, actualizar_precio_total.getText());
                    // SE PRESENTA POR PANTALLA EL PRECIO TOTAL ACTUALIZADO
                    textPrecioTotal.setText(actualizar_precio_total.getText());
                    // RESULTADOS DE LA ACTUALIZACIÓN
                    res = ps.executeUpdate();
                    if (!(res > 0)) {
                        JOptionPane.showMessageDialog(null, "DATOS NO GUARDADOS EN INVENTARIO");
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    ps.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // INICIO BDD ACTUALIZAR INVENTARIO

                try{ // INICIO BDD CARGAR PRODUCTOS E INVENTARIO
                    Connection conexion;
                    conexion = getConection();

                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT producto.*, inventario.cantidad, inventario.precio_total, inventario.fecha_registro as Fecha_factura  FROM (producto,inventario) WHERE id_producto=FK_id_producto");

                    // SE OBTIENE LOS DATOS
                    rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    // SE CREA LA TALBA Y EL MODELO
                    modelo = (DefaultTableModel) table.getModel();

                    // SE ELIMINA EL CONTENIDO DE LA TABLA PARA CARGAR DE NUEVO
                    modelo.setColumnCount(0);// Se elimina la columna de la tabla
                    modelo.setRowCount(0); // Se elimina las filas de la tabla

                    // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                    for (int i = 1; i <= columnCount; i++) {
                        modelo.addColumn(rsmd.getColumnName(i));
                    }

                    // SE AGREGA LOS DATOS A LA TABLA
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
                } // FIN BDD CARGAR PRODUCTOS E INVENTARIO
            }
        });
    } // FIN ACCIÓN ACTUALIZAR PRODUCTO
    public static Connection getConection() { // SE REALIZA LA CONEXIÓN CON BDD
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
