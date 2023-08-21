// PERMITE CREAR INTERFACES DE USUARIO
import javax.swing.*;
// PERMITE MANEJAR TABLAS
import javax.swing.table.DefaultTableModel;
// PERMITE MANEJAR LAS ACCIONES DE BOTONES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// PERMITE TRABAJR CON JDBC, PARA LA CONEXIÓN CON LA BASE DE DATOS
import java.sql.*;
// PERMITE TRABAJAR CON FECHAS Y HORAS
import java.util.Date;

// PERMITE RESTRINGUIR A SOLO NÚMEROS
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Admin_stock {
    public JPanel Panel; // CONTENEDOR DE ELEMENTOS
    Statement s; // ENVÍA CONSULTAS SQL A LA BDD
    ResultSet rs; // ALMACENA LOS RESULTADOS DE LA CONSULTA
    ResultSetMetaData rsmd; // SE OBTIENE LA INFORMACIÓN DE LOS METADATOS DE RS
    PreparedStatement ps; // PERMITE PARAMETRIZAR LAS CONSULTAS EN BDD (ACTUALIZAR, INSERCIÓN)
    int res; // RESULTADOS DE CONSULTA (INSERCIÓN, ACTUALIZAR, ELIMINAR)
    private JFormattedTextField textCODIGO; // ID DEL PRODUCTO
    private JFormattedTextField textPRODUCTO; // NOMBRE DEL PRODUCTO
    private JFormattedTextField textPRECIO; // PRECIO DEL PRODUCTO DE VENTA
    private JFormattedTextField textCANTIDAD; // CANTIDAD DE PRODUCTO EN INVENTARIO
    private JButton buscarButton; // BOTÓN BUSCAR PRODUCTO
    private JButton agregarNuevoButton; // BOTÓN AGREGAR NUEVO PRODUCTO
    private JButton agregarMasButton; // BOTÓN AGREGAR MÁS PRODUCTO
    private JTable table; // PERMITE MOSTRAR LOS DATOS EN FORMA DE TABLA
    private JFormattedTextField textFECHA; // MUESTRA LA FECHA ACTUAL
    private JFormattedTextField textGanancia; // GANANCIA DEL PRODUCTO
    private JFormattedTextField textPrecioVenta; // PRECIO DE VENTA DEL PRODUCTO
    private JFormattedTextField textPrecioTotal; // PRECIO TOTAL DE CADA PRODUCTO
    private JFormattedTextField textFechaRegistrada; // FECHA REGISTRADA DE LA FACTURA
    private JFormattedTextField textCANTIDAD_A_AGREGAR; // CANTIDAD DE PRODUCTO A AGREGAR
    DefaultTableModel modelo = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS QUE SE MOSTRARÁN EN LA TABLA
    boolean encontrado; // VERIFICA SI SE ENCONTRO LA INFORMACIÓN REQUERIDA EN LA BDD
    String cod; // OBTIENE EL ID DEL CLIENTE CON UN FORMATO ADECUADO
    JFormattedTextField actualizar_cantidad = new JFormattedTextField(); // RECIBE LA CANTIDAD DE PRODUCTO ACTUALIZADA(AUMENTADA)
    JFormattedTextField actualizar_precio_total = new JFormattedTextField(); // RECIBE LA ACTUALIZACIÓN DEL PRECIO EN INVENTARIO
    public Admin_stock(){
        // CONFIGURACIÓN DEL DOCUMENTFILTER PARA RESTRINGUIR LOS CARACTERES A NÚMEROS
        AbstractDocument doc = (AbstractDocument) textCANTIDAD.getDocument();
        AbstractDocument doc1 = (AbstractDocument) textCANTIDAD_A_AGREGAR.getDocument();
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
        doc1.setDocumentFilter(new DocumentFilter() {
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
        // SE OBTIENE LA FECHA ACTUAL
        Date fechaActual = new Date();

        // SE PRESENTA POR PANTALLA LA FECHA ACTUAL
        textFECHA.setValue(fechaActual);

        // BLOQUEO DE EDICIÓN EN PRODUCTOS E INVENTARIO
        textFECHA.setEnabled(false);
        textPRODUCTO.setEnabled(false);
        textPRECIO.setEnabled(false);
        textGanancia.setEnabled(false);
        textPrecioVenta.setEnabled(false);
        textFechaRegistrada.setEnabled(false);
        textCANTIDAD.setEnabled(false);
        textPrecioTotal.setEnabled(false);
        textCANTIDAD_A_AGREGAR.setEnabled(false);

        // DESHABILITA BOTONES
        agregarNuevoButton.setEnabled(false);
        agregarMasButton.setEnabled(false);

        try{// INICIO BDD CARGAR PRODUCTOS EN TABLA
            // SE OBTIENE LA CONEXIÓN
            Connection conexion;
            conexion = getConection();
            // CONSULTA: SE OBTIENE LOS DATOS DE PRODUCTO E INVENTARIO
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT producto.*, inventario.cantidad, inventario.precio_total  FROM (producto,inventario) WHERE id_producto=FK_id_producto");

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
        }// FIN BDD CARGAR PRODUCTOS EN TABLA

        buscarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN BUCAR PRODUCTO
            @Override
            public void actionPerformed(ActionEvent e) { // ESCUCHA ACTIVA
                encontrado = false;
                try { // INICIO BDD DATOS PRODUCTO
                    // SE OBTIENE LA CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();

                    // SE OBTIENE EL ID DEL PRODUCTO EN UN FORMATO ADECUADO
                    cod =  "\"" + textCODIGO.getText() + "\"";
                    // CONSULTA: SE OBTIENE INFORMACIÓN DEL PRODUCTO BUSCADO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT (Select producto from producto where id_producto = FK_id_producto) as producto," +
                                                    "(Select inversion from producto where id_producto = FK_id_producto) as inversion,"+
                                                    "(Select ganancia from producto where id_producto = FK_id_producto) as ganancia," +
                                                    "(Select precio_venta_unitario from producto where id_producto = FK_id_producto) precio_venta," +
                                                    "fecha_registro, cantidad, precio_total FROM inventario WHERE FK_id_producto=" + cod);

                    // SE OBTIENE LOS DATOS REQUERIDOS
                    while (rs.next()) {
                        textPRODUCTO.setText(rs.getString(1));
                        textPRECIO.setText(rs.getString(2));
                        textGanancia.setText(rs.getString(3));
                        textPrecioVenta.setText(rs.getString(4));
                        textFechaRegistrada.setText(rs.getString(5));
                        textCANTIDAD.setText(rs.getString(6));
                        textPrecioTotal.setText(rs.getString(7));
                        encontrado = true; // LOS DATOS FUERON ENCONTRADOS
                    }

                    if(!encontrado){ // SI EL PRODUCTO NO FUÉ ENCONTRADO
                        JOptionPane.showMessageDialog(null, "PRODUCTO NO ENCONTRADOS");
                        // SE HABILITAN LOS CAMPOS PARA AGREGAR PRODUCTO
                        textPRODUCTO.setEnabled(true);
                        textPRECIO.setEnabled(true);
                        textGanancia.setEnabled(true);
                        textFechaRegistrada.setEnabled(true);
                        textCANTIDAD.setEnabled(true);
                        textCANTIDAD_A_AGREGAR.setEnabled(false);
                        // SE VACÍAN LOS CAMPOS
                        textPRODUCTO.setText("");
                        textPRECIO.setText("");
                        textGanancia.setText("");
                        textPrecioVenta.setText("");
                        textFechaRegistrada.setText("");
                        textCANTIDAD.setText("");
                        textPrecioTotal.setText("");
                        // SE HABILITAN LOS BOTONES NECESARIOS
                        agregarNuevoButton.setEnabled(true);
                        agregarMasButton.setEnabled(false);
                    }else{
                        textPRODUCTO.setEnabled(false);
                        textPRECIO.setEnabled(false);
                        textGanancia.setEnabled(false);
                        textCANTIDAD.setEnabled(false);
                        textFechaRegistrada.setEnabled(true);
                        agregarNuevoButton.setEnabled(false);
                        agregarMasButton.setEnabled(true);
                        textCANTIDAD_A_AGREGAR.setEnabled(true);
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }// FIN BDD DATOS PRODUCTO
            }
        });// INICIO ACCIÓN BUCAR PRODUCTO

        agregarMasButton.addActionListener(new ActionListener() { // INICIO AGREGAR MÁS PRODUCTO
            @Override
            public void actionPerformed(ActionEvent e) {

                try{ // INICIO BDD ACTUALIZAR INVENTARIO
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // SE OBTIENE EL ID DEL PRODUCTO EN UN FORMATO ADECUADO
                    cod = "\"" + textCODIGO.getText() + "\"";
                    // CONSULTA: ACTUALIZACIÓN DE DATOS EN LA TABLA INVENTARIO
                    ps = conexion.prepareStatement("UPDATE inventario SET cantidad =?, precio_total=?, fecha_registro=?, fecha_actual=CURDATE() WHERE FK_id_producto = " + cod);
                    // SE CALCULA LA CANTIDAD ACTUALIZADA AL AGRGAR MÁS PRODUCTO
                    actualizar_cantidad.setText(String.valueOf(Integer.parseInt(textCANTIDAD.getText())+Integer.parseInt(textCANTIDAD_A_AGREGAR.getText())));
                    ps.setString(1, actualizar_cantidad.getText()); // SE INSERTA EN LA TABLA INVENTARIO
                    // SE CALCULA EL PRECIO CON EL AUMENTO DE CNATIDAD DE PRODUCTO
                    actualizar_precio_total.setText(String.valueOf(Integer.parseInt(actualizar_cantidad.getText())*Float.parseFloat(textPrecioVenta.getText())));
                    ps.setString(2, actualizar_precio_total.getText()); // SE INSERTA EL PRECIO ACTUALIZADO EN INVENTARIO
                    ps.setString(3, textFechaRegistrada.getText()); // SE INSERTA EN INVENTARIO LA FECHA PROPORCIONADA POR EL USUARIO

                    // SE PRESENTA POR PANTALLA LOS VALORES ACTUALIZADOS
                    textCANTIDAD.setText(actualizar_cantidad.getText());
                    textPrecioTotal.setText(actualizar_precio_total.getText());
                    //RESULTADO DE LA CONSULTA
                    res = ps.executeUpdate();
                    if(res >0){
                        JOptionPane.showMessageDialog(null,"PRODUCTO ACTUALIZADO");
                        // SE ELIMINA LA TABLA PARA CARGAR NUEVAMENTE CON LOS DATOS ACTUALIZADOS
                        modelo.setColumnCount(0);//SE ELIMINA LA COLUMNA DE LA TABLA
                        modelo.setRowCount(0);// SE ELIMINA LA FILA DE LA TABLA
                        textCANTIDAD_A_AGREGAR.setText("");
                    }else{
                        JOptionPane.showMessageDialog(null,"PRODUCTO NO ACTUALIZADO");
                    }
                    // CIERRE DE CONEXIÓN BDD
                    conexion.close();
                    ps.close();
                }catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD ACTUALIZAR INVENTARIO


                try{ // INICIO BDD CARGAR NUEVAMENTE PRODUCTO E INVENTARIO
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA: DATOS DE PRODUCTO E INVENTARIO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT producto.*, inventario.cantidad, inventario.precio_total  FROM (producto,inventario) WHERE id_producto=FK_id_producto");

                    // SE OBTIENE LOS DATOS
                    rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    // SE CREA LA TABLA Y EL MODELO
                    modelo = (DefaultTableModel) table.getModel();

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
                }// INICIO BDD CARGAR NUEVAMENTE PRODUCTO E INVENTARIO
            }
        }); // INICIO AGREGAR MÁS PRODUCTO

        agregarNuevoButton.addActionListener(new ActionListener() { // INICIO ACCIÓN AGREGAR NUEVO PRODUCTO
            @Override
            public void actionPerformed(ActionEvent e) {
                textPrecioVenta.setEnabled(false);
                try {// INICIO BDD INSERTAR PRODUCTOS E INVENTARIO
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();

                    // CONSULTA PARAMETRIZADA: INSERTAR UN NUEVO PRODUCTO
                    ps = conexion.prepareStatement("INSERT into producto values (?,?,?,?,?)");
                    ps.setString(1, textCODIGO.getText());
                    ps.setString(2, textPRODUCTO.getText());
                    ps.setString(3, textPRECIO.getText());
                    ps.setString(4, textGanancia.getText());
                    // SE CALCULA EL PRECIO DE VENTA: PRECIO + GANANCIA
                    textPrecioVenta.setText(String.valueOf(Float.parseFloat(textPRECIO.getText())+Float.parseFloat(textGanancia.getText())));
                    ps.setString(5, textPrecioVenta.getText());
                    // RESULTADOS DE LA ACTUALIZACIÓN
                    res = ps.executeUpdate();
                    if(!(res >0)){
                        JOptionPane.showMessageDialog(null,"PRODUCTO NO GUARDADO EN TABLA PRODUCTOS");

                    }
                    // SE OBTIENE LA FECHA DE REGISTRO DE LA FACTURA
                    String FR = textFechaRegistrada.getText();
                    // CONSULTA PARAMETRIZADA: SE INSERTA UNA NUEVO PRODUCTO EN INVENTARIO
                    ps = conexion.prepareStatement("Insert into inventario values (?,?,CURDATE(),?,?)");
                    ps.setString(1, textCODIGO.getText());
                    ps.setString(2, FR);
                    ps.setString(3, textCANTIDAD.getText());
                    textPrecioTotal.setText(String.valueOf(Integer.parseInt(textCANTIDAD.getText())*Float.parseFloat(textPrecioVenta.getText())));
                    ps.setString(4, textPrecioTotal.getText());
                    // RESULTADOS DE LA ACTUALIZACIÓN
                    res = ps.executeUpdate();
                    if(!(res >0)){
                        JOptionPane.showMessageDialog(null,"PRODUCTO NO GUARDADO EN LA TABLA INVENTARIO");

                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    ps.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // INICIO BDD INSERTAR PRODUCTOS E INVENTARIO

                try{//INICIO BDD SE CARGA NUEVAMENTE LOS DATOS DE PRODCUTO E INVENTARIO
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA: DATOS DE PRODUCTOS EN TABLA PRODUCTOS E INVENTARIO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT producto.*, inventario.cantidad, inventario.precio_total  FROM (producto,inventario) WHERE id_producto=FK_id_producto");
                    // SE OBTIENE LOS DATOS
                    rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    // SE CREA LA TALBA Y EL MODELO
                    modelo = (DefaultTableModel) table.getModel();
                    // SE ELIMINA LOS DATOS DE LA TABLA PARA CARGAR DE NUEVO
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
                }//FIN BDD SE CARGA NUEVAMENTE LOS DATOS DE PRODCUTO E INVENTARIO

            }
        }); // FIN ACCIÓN AGREGAR NUEVO PRODUCTO
    }
    public static Connection getConection() // SE REALIZA LA CONEXIÓN CON BDD
    {
        Connection conexion;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://192.168.100.161/tienda", "florcan", "1234"
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conexion;
    }
}
