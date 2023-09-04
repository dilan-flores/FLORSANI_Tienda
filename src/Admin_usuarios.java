import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class Admin_usuarios {
    public JPanel Panel;
    Statement s; // ENVÍA CONSULTAS SQL A LA BDD
    ResultSet rs; // ALMACENA LOS RESULTADOS DE LA CONSULTA
    PreparedStatement ps; // PERMITE PARAMETRIZAR LAS CONSULTAS EN BDD (ACTUALIZAR, INSERCIÓN)
    int res; // RESULTADOS DE CONSULTA (INSERCIÓN, ACTUALIZAR, ELIMINAR)
    ResultSetMetaData rsmd; // SE OBTIENE LA INFORMACIÓN DE LOS METADATOS DE RS
    private JComboBox comboBoxCEDULA;
    private JButton buscarButton;
    private JFormattedTextField textNOMBRE;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JFormattedTextField textUSUARIO;
    private JFormattedTextField textCONTRASENIA;
    private JFormattedTextField textCedula_crear;
    private JButton agregarButton;
    private JComboBox comboBoxUSUARIO_CREAR;
    private JFormattedTextField textNombre_Crear;
    private JFormattedTextField textUser_Crear;
    private JFormattedTextField textContrasenia_Crear;
    DefaultTableModel modelo_admin = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS QUE SE MOSTRARÁN EN LA TABLA
    DefaultTableModel modelo_ventas = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS QUE SE MOSTRARÁN EN LA TABLA
    private JTable table_admin;
    private JTable table_ventas;
    private JComboBox comboBoxUSUARIO;
    String Tipo_Usuario;
    String ced;
    String tipo_user;
    Boolean encontrado;
    JFormattedTextField Verificar_Ced = new JFormattedTextField(); //

    public Admin_usuarios(){
        try{ // INICIO BDD CARGAR USUARIO ADMINISTRADORES
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT admin.*, login_admin.usuario_ad,login_admin.contrasenia_ad  FROM (admin,login_admin) WHERE ci_ad = FK_ci_ad");

            // SE OBTIENE LOS DATOS
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            // SE CREA LA TABLA Y EL MODELO
            modelo_admin = (DefaultTableModel) table_admin.getModel();

            // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
            modelo_admin.setColumnCount(0);// Se elimina la columna de la tabla
            modelo_admin.setRowCount(0); // Se elimina las filas de la tabla

            // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
            for (int i = 1; i <= columnCount; i++) {
                modelo_admin.addColumn(rsmd.getColumnName(i));
            }
            // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo_admin.addRow(row);
            }
            // CIERRE DE CONEXIÓN
            rs.close();
            s.close();
            conexion.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        } // FIN BDD CARGAR USUARIO ADMINISTRADOR

        try{ // INICIO BDD CARGAR USUARIO VENTAS
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT usuario_venta.*, login_venta.usuario_user, login_venta.contrasenia_user  FROM (usuario_venta,login_venta) WHERE ci_user = FK_ci_user");

            // SE OBTIENE LOS DATOS
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            // SE CREA LA TABLA Y EL MODELO
            modelo_ventas = (DefaultTableModel) table_ventas.getModel();

            // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
            modelo_ventas.setColumnCount(0);// Se elimina la columna de la tabla
            modelo_ventas.setRowCount(0); // Se elimina las filas de la tabla

            // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
            for (int i = 1; i <= columnCount; i++) {
                modelo_ventas.addColumn(rsmd.getColumnName(i));
            }
            // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo_ventas.addRow(row);
            }
            // CIERRE DE CONEXIÓN
            rs.close();
            s.close();
            conexion.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        } // FIN BDD CARGAR USUARIO VENTAS
        // SE CARGA EL COMBO BOX
        comboBoxUSUARIO.removeAllItems();
        comboBoxUSUARIO.addItem(" ");
        comboBoxUSUARIO.addItem("ADMINISTRADOR");
        comboBoxUSUARIO.addItem("VENTAS");

        // SE CARGA EL COMBO BOX
        comboBoxUSUARIO_CREAR.removeAllItems();
        comboBoxUSUARIO_CREAR.addItem(" ");
        comboBoxUSUARIO_CREAR.addItem("ADMINISTRADOR");
        comboBoxUSUARIO_CREAR.addItem("VENTAS");

        try {// INICIO BDD CARGAR DATOS DE USUARIOS
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: OBTENER PRODUCTOS
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT ci_ad AS cedula FROM admin UNION SELECT ci_user AS cedula FROM usuario_venta ");

            // ELIMINACIÓN DE DATOS INICIALES EN COMBO BOX
            comboBoxCEDULA.removeAllItems();
            // CREACIÓN DE UN ESPACIO EN BLANCO
            comboBoxCEDULA.addItem(" ");
            // SE OBTIENEN LOS DATOS REQUERIDOS DE PRODUCTOS
            while (rs.next()) {
                comboBoxCEDULA.addItem(rs.getString(1));
            }
            // CIERRE DE CONEXIÓN
            conexion.close();
            rs.close();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } // FIN BDD CARGAR DATOS DE USUARIOS

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encontrado=false;
                try { // INICIO BDD CARGAR PRODUCT OE INVENTARIO
                    // OBTIENE LA CÉDULA DE COMBO BOX
                    ced = (String)comboBoxCEDULA.getSelectedItem();
                    // OBTIENE EL TIPO DE USUARIO DE COMBO BOX
                    Tipo_Usuario = (String)comboBoxUSUARIO.getSelectedItem();
                    if("ADMINISTRADOR" == comboBoxUSUARIO.getSelectedItem()){ // SI SE SELECCIONA LA OPCIÓN DE ADMINISTRADOR
                        try{ // INICIO BDD CARGAR DATOS DE USUARIO ADMISTRADOR
                            //CONEXIÓN BDD
                            Connection conexion;
                            conexion = getConection();
                            // CONSULTA: CARGAR DATOS DE ADMINISTRADOR
                            s = conexion.createStatement();
                            rs = s.executeQuery("SELECT ci_ad FROM admin WHERE ci_ad = " + ced);
                            // SE OBTIENE LOS DATOS DE BDD DE ACUERDO CON LA CONSULTA
                            while(rs.next()){
                                Verificar_Ced.setText(rs.getString(1));
                                encontrado = true;
                            }
                            if(encontrado){ // SI EXISTE LA CÉDULA EN LA BDD ADMINISTRADOR
                                s = conexion.createStatement();
                                rs = s.executeQuery("SELECT (SELECT nombres_ad FROM admin WHERE ci_ad=FK_ci_ad), usuario_ad, contrasenia_ad FROM login_admin WHERE FK_ci_ad = " + ced);

                                // SE OBTIENE LOS DATOS DE BDD DE ACUERDO CON LA CONSULTA
                                while(rs.next()){
                                    textNOMBRE.setText(rs.getString(1));
                                    textUSUARIO.setText(rs.getString(2));
                                    textCONTRASENIA.setText(rs.getString(3));
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "USUARIO ADMINISTRADOR NO ENCONTRADO");
                                textNOMBRE.setText("");
                                textUSUARIO.setText("");
                                textCONTRASENIA.setText("");
                            }
                            // CIERRE DE CONEXIÓN
                            conexion.close();
                            rs.close();
                            s.close();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        } // FIN BDD CARGAR DATOS DE USUARIO ADMINISTRADOR
                    }
                    if("VENTAS" == comboBoxUSUARIO.getSelectedItem()){ // SI SE SELECCIONA LA OPCIÓN DE VENTAS
                        try{ // INICIO BDD CARGAR DATOS DE USUARIO ADMISTRADOR
                            //CONEXIÓN BDD
                            Connection conexion;
                            conexion = getConection();
                            // CONSULTA: CARGAR DATOS DE ADMINISTRADOR
                            s = conexion.createStatement();
                            rs = s.executeQuery("SELECT ci_user FROM usuario_venta WHERE ci_user = " + ced);
                            // SE OBTIENE LOS DATOS DE BDD DE ACUERDO CON LA CONSULTA
                            while(rs.next()){
                                Verificar_Ced.setText(rs.getString(1));
                                encontrado = true;
                            }
                            if(encontrado){ // SI EXISTE LA CÉDULA EN LA BDD ADMINISTRADOR
                                s = conexion.createStatement();
                                rs = s.executeQuery("SELECT (SELECT nombres_user FROM usuario_venta WHERE ci_user=FK_ci_user), usuario_user, contrasenia_user FROM login_venta WHERE FK_ci_user = " + ced);

                                // SE OBTIENE LOS DATOS DE BDD DE ACUERDO CON LA CONSULTA
                                while(rs.next()){
                                    textNOMBRE.setText(rs.getString(1));
                                    textUSUARIO.setText(rs.getString(2));
                                    textCONTRASENIA.setText(rs.getString(3));
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "USUARIO VENTAS NO ENCONTRADO");
                                textNOMBRE.setText("");
                                textUSUARIO.setText("");
                                textCONTRASENIA.setText("");
                            }
                            // CIERRE DE CONEXIÓN
                            conexion.close();
                            rs.close();
                            s.close();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        } // FIN BDD CARGAR DATOS DE USUARIO ADMINISTRADOR


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD CARGAR PRODUCTOE INVENTARIO
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Objects.equals(Tipo_Usuario, "ADMINISTRADOR")){
                    try{
                        // SE OBTIENE CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        //CONSULTA PARAMETRIZADA: ACTUALIZAR ADMIN
                        ps = conexion.prepareStatement("UPDATE admin SET nombres_ad=? WHERE ci_ad = " + ced);
                        ps.setString(1, textNOMBRE.getText());

                        // RESULTADO DE LA ACTUALIZACIÓN
                        res = ps.executeUpdate();
                        if (!(res > 0)) {
                            JOptionPane.showMessageDialog(null, "USUARIO ADMIN NO ACTUALIZADO");
                        }
                        ps = conexion.prepareStatement("UPDATE login_admin SET usuario_ad=?, contrasenia_ad=? WHERE FK_ci_ad = " + ced);
                        ps.setString(1, textUSUARIO.getText());
                        ps.setString(2, textCONTRASENIA.getText());

                        // RESULTADO DE LA ACTUALIZACIÓN
                        res = ps.executeUpdate();
                        if (!(res > 0)) {
                            JOptionPane.showMessageDialog(null, "USUARIO ADMIN NO ACTUALIZADO EN LOGIN");
                        }
                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        ps.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD ACTUALIZAR PRODUCTO
                    try{ // INICIO BDD CARGAR NUEVAMENTE USUARIO ADMINISTRADORES
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT admin.*, login_admin.usuario_ad,login_admin.contrasenia_ad  FROM (admin,login_admin) WHERE ci_ad = FK_ci_ad");

                        // SE OBTIENE LOS DATOS
                        rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        // SE CREA LA TABLA Y EL MODELO
                        modelo_admin = (DefaultTableModel) table_admin.getModel();

                        // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
                        modelo_admin.setColumnCount(0);// Se elimina la columna de la tabla
                        modelo_admin.setRowCount(0); // Se elimina las filas de la tabla

                        // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                        for (int i = 1; i <= columnCount; i++) {
                            modelo_admin.addColumn(rsmd.getColumnName(i));
                        }
                        // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            modelo_admin.addRow(row);
                        }
                        // CIERRE DE CONEXIÓN
                        rs.close();
                        s.close();
                        conexion.close();
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD CARGAR NUEVAMENTE USUARIO ADMINISTRADOR
                }
                if(Objects.equals(Tipo_Usuario, "VENTAS")){
                    try{
                        // SE OBTIENE CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        //CONSULTA PARAMETRIZADA: ACTUALIZAR ADMIN
                        ps = conexion.prepareStatement("UPDATE usuario_venta SET nombres_user=? WHERE ci_user = " + ced);
                        ps.setString(1, textNOMBRE.getText());

                        // RESULTADO DE LA ACTUALIZACIÓN
                        res = ps.executeUpdate();
                        if (!(res > 0)) {
                            JOptionPane.showMessageDialog(null, "USUARIO VENTAS NO ACTUALIZADO");
                        }
                        ps = conexion.prepareStatement("UPDATE login_venta SET usuario_user=?, contrasenia_user=? WHERE FK_ci_user = " + ced);
                        ps.setString(1, textUSUARIO.getText());
                        ps.setString(2, textCONTRASENIA.getText());

                        // RESULTADO DE LA ACTUALIZACIÓN
                        res = ps.executeUpdate();
                        if (!(res > 0)) {
                            JOptionPane.showMessageDialog(null, "USUARIO VENTAS NO ACTUALIZADO EN LOGIN");
                        }

                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        ps.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD ACTUALIZAR PRODUCTO
                    try{ // INICIO BDD CARGAR NUEVAMENTE USUARIO ADMINISTRADORES
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT admin.*, login_admin.usuario_ad,login_admin.contrasenia_ad  FROM (admin,login_admin) WHERE ci_ad = FK_ci_ad");

                        // SE OBTIENE LOS DATOS
                        rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        // SE CREA LA TABLA Y EL MODELO
                        modelo_admin = (DefaultTableModel) table_admin.getModel();

                        // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
                        modelo_admin.setColumnCount(0);// Se elimina la columna de la tabla
                        modelo_admin.setRowCount(0); // Se elimina las filas de la tabla

                        // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                        for (int i = 1; i <= columnCount; i++) {
                            modelo_admin.addColumn(rsmd.getColumnName(i));
                        }
                        // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            modelo_admin.addRow(row);
                        }
                        // CIERRE DE CONEXIÓN
                        rs.close();
                        s.close();
                        conexion.close();
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD CARGAR NUEVAMENTE USUARIO ADMINISTRADOR
                    try{ // INICIO BDD CARGAR USUARIO VENTAS
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT usuario_venta.*, login_venta.usuario_user, login_venta.contrasenia_user  FROM (usuario_venta,login_venta) WHERE ci_user = FK_ci_user");

                        // SE OBTIENE LOS DATOS
                        rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        // SE CREA LA TABLA Y EL MODELO
                        modelo_ventas = (DefaultTableModel) table_ventas.getModel();

                        // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
                        modelo_ventas.setColumnCount(0);// Se elimina la columna de la tabla
                        modelo_ventas.setRowCount(0); // Se elimina las filas de la tabla

                        // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                        for (int i = 1; i <= columnCount; i++) {
                            modelo_ventas.addColumn(rsmd.getColumnName(i));
                        }
                        // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            modelo_ventas.addRow(row);
                        }
                        // CIERRE DE CONEXIÓN
                        rs.close();
                        s.close();
                        conexion.close();
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD CARGAR USUARIO VENTAS
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN ELIMINAR USUARIO
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Objects.equals(Tipo_Usuario, "ADMINISTRADOR")){
                    try {// INICIO BDD ELIMINAR USUARIO ADMINISTRADOR
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA PARAMETRIZADA: ELIMINACIÓN DE LOGIN ADMINISTRADOR
                        ps = conexion.prepareStatement(" DELETE FROM login_admin WHERE FK_ci_ad = " + ced);
                        // RESULTADO DE LA ELIMINACION
                        res = ps.executeUpdate();
                        if (!(res > 0)) {
                            JOptionPane.showMessageDialog(null, "USUARIO ADMINISTRADOR NO ELIMINADO LOGIN");
                        }

                        //CONSULTA PARAMETRIZADA: ELIMINAR CLIENTE
                        ps = conexion.prepareStatement(" DELETE FROM admin WHERE ci_ad = " + ced);
                        // RESULTADO DE LA ELIMINACION
                        res = ps.executeUpdate();
                        if (!(res > 0)) {
                            JOptionPane.showMessageDialog(null, "USUARIO ADMINISTRADOR NO ELIMINADO");
                        }else{
                            textNOMBRE.setText("");
                            textUSUARIO.setText("");
                            textCONTRASENIA.setText("");
                        }
                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        ps.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD ELIMINAR USUARIO ADMINISTRADOR
                    try{ // INICIO BDD CARGAR NUEVAMENTE USUARIO ADMINISTRADORES
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT admin.*, login_admin.usuario_ad,login_admin.contrasenia_ad  FROM (admin,login_admin) WHERE ci_ad = FK_ci_ad");

                        // SE OBTIENE LOS DATOS
                        rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        // SE CREA LA TABLA Y EL MODELO
                        modelo_admin = (DefaultTableModel) table_admin.getModel();

                        // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
                        modelo_admin.setColumnCount(0);// Se elimina la columna de la tabla
                        modelo_admin.setRowCount(0); // Se elimina las filas de la tabla

                        // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                        for (int i = 1; i <= columnCount; i++) {
                            modelo_admin.addColumn(rsmd.getColumnName(i));
                        }
                        // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            modelo_admin.addRow(row);
                        }
                        // CIERRE DE CONEXIÓN
                        rs.close();
                        s.close();
                        conexion.close();
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD CARGAR NUEVAMENTE USUARIO ADMINISTRADOR
                }
                if(Objects.equals(Tipo_Usuario, "VENTAS")) {
                    try {// INICIO BDD ELIMINAR USUARIO VENTAS
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA PARAMETRIZADA: ELIMINACIÓN DE LOGIN ADMINISTRADOR
                        ps = conexion.prepareStatement(" DELETE FROM login_venta WHERE FK_ci_user = " + ced);
                        // RESULTADO DE LA ELIMINACION
                        res = ps.executeUpdate();
                        if (!(res > 0)) {
                            JOptionPane.showMessageDialog(null, "USUARIO VENTAS NO ELIMINADO LOGIN");
                        }else{
                            textNOMBRE.setText("");
                            textUSUARIO.setText("");
                            textCONTRASENIA.setText("");
                        }

                        //CONSULTA PARAMETRIZADA: ELIMINAR CLIENTE
                        ps = conexion.prepareStatement(" DELETE FROM usuario_venta WHERE ci_user = " + ced);
                        // RESULTADO DE LA ELIMINACION
                        res = ps.executeUpdate();
                        if (!(res > 0)) {
                            JOptionPane.showMessageDialog(null, "USUARIO VENTAS NO ELIMINADO");
                        }
                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        ps.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD ELIMINAR USUARIO VENTAS

                    try{ // INICIO BDD CARGAR NUEVAMENTE USUARIO VENTAS
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT usuario_venta.*, login_venta.usuario_user, login_venta.contrasenia_user  FROM (usuario_venta,login_venta) WHERE ci_user = FK_ci_user");

                        // SE OBTIENE LOS DATOS
                        rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        // SE CREA LA TABLA Y EL MODELO
                        modelo_ventas = (DefaultTableModel) table_ventas.getModel();

                        // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
                        modelo_ventas.setColumnCount(0);// Se elimina la columna de la tabla
                        modelo_ventas.setRowCount(0); // Se elimina las filas de la tabla

                        // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                        for (int i = 1; i <= columnCount; i++) {
                            modelo_ventas.addColumn(rsmd.getColumnName(i));
                        }
                        // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            modelo_ventas.addRow(row);
                        }
                        // CIERRE DE CONEXIÓN
                        rs.close();
                        s.close();
                        conexion.close();
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD CARGAR NUEVAMENTE USUARIO VENTAS
                }
                try {// INICIO BDD CARGAR DATOS DE USUARIOS
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA: OBTENER PRODUCTOS
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT ci_ad AS cedula FROM admin UNION SELECT ci_user AS cedula FROM usuario_venta ");

                    // ELIMINACIÓN DE DATOS INICIALES EN COMBO BOX
                    comboBoxCEDULA.removeAllItems();
                    // CREACIÓN DE UN ESPACIO EN BLANCO
                    comboBoxCEDULA.addItem(" ");
                    // SE OBTIENEN LOS DATOS REQUERIDOS DE PRODUCTOS
                    while (rs.next()) {
                        comboBoxCEDULA.addItem(rs.getString(1));
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD CARGAR DATOS DE USUARIOS
            }
        }); // INICIO ACCIÓN ELIMINAR USUARIO

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encontrado = false;
                Tipo_Usuario = (String)comboBoxUSUARIO_CREAR.getSelectedItem();
                if(Objects.equals(Tipo_Usuario, "ADMINISTRADOR")) { // SI SE SELECCIONA USUARIO ADMINISTRADOR
                    if(textCedula_crear.getText().length()==10){
                        try{ // INICIO BDD USUARIO ADMIN: VERIFICAR SI EL CLIENTE EXISTE
                            // SE OBTIENE CONEXIÓN BDD
                            Connection conexion;
                            conexion = getConection();
                            s = conexion.createStatement();
                            rs = s.executeQuery("SELECT * FROM admin");

                            // SE OBTIENE LOS RESULTADOS
                            while (rs.next()) {
                                String verificarCedula = rs.getString(1);
                                if(Objects.equals(verificarCedula, textCedula_crear.getText())){ // SI EL USUARIO EXISTE: NO SE AGREGA CLIENTE
                                    encontrado = true;
                                }
                            }
                            if(encontrado){
                                JOptionPane.showMessageDialog(null, "USUARIO EXISTE");
                            }else{
                                //CONSULTA PARAMETRIZADA: INSERTAR ADMIN
                                ps = conexion.prepareStatement(" INSERT INTO admin VALUES (?,?) ");
                                ps.setString(1, textCedula_crear.getText());
                                ps.setString(2, textNombre_Crear.getText());

                                // RESULTADO DE LA ACTUALIZACIÓN
                                res = ps.executeUpdate();
                                if (!(res > 0)) {
                                    JOptionPane.showMessageDialog(null, "DATOS DE ADMIN NO GUARDADO");
                                }

                                //CONSULTA PARAMETRIZADA: INSERTAR LOGIN DE ADMIN
                                ps = conexion.prepareStatement(" INSERT INTO login_admin VALUES (?,?,?) ");
                                ps.setString(1, textCedula_crear.getText());
                                ps.setString(2, textUser_Crear.getText());
                                ps.setString(3, textContrasenia_Crear.getText());

                                // RESULTADO DE LA ACTUALIZACIÓN
                                res = ps.executeUpdate();
                                if (!(res > 0)) {
                                    JOptionPane.showMessageDialog(null, "DATOS DE ADMIN NO GUARDADO");
                                }
                                // CIERRE DE CONEXIÓN
                                ps.close();
                            }
                            // CIERRE DE CONEXIÓN
                            conexion.close();
                            rs.close();
                            s.close();
                        }catch (Exception ex) {
                            ex.printStackTrace();
                        } // FIN BDD CLIENTES
                    }else{
                        JOptionPane.showMessageDialog(null, "RANGO INCORRECTO");
                    }
                    try{ // INICIO BDD CARGAR NUEVAMENTE USUARIO ADMINISTRADORES
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT admin.*, login_admin.usuario_ad,login_admin.contrasenia_ad  FROM (admin,login_admin) WHERE ci_ad = FK_ci_ad");

                        // SE OBTIENE LOS DATOS
                        rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        // SE CREA LA TABLA Y EL MODELO
                        modelo_admin = (DefaultTableModel) table_admin.getModel();

                        // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
                        modelo_admin.setColumnCount(0);// Se elimina la columna de la tabla
                        modelo_admin.setRowCount(0); // Se elimina las filas de la tabla

                        // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                        for (int i = 1; i <= columnCount; i++) {
                            modelo_admin.addColumn(rsmd.getColumnName(i));
                        }
                        // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            modelo_admin.addRow(row);
                        }
                        // CIERRE DE CONEXIÓN
                        rs.close();
                        s.close();
                        conexion.close();
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD CARGAR NUEVAMENTE USUARIO ADMINISTRADOR
                }

                if(Objects.equals(Tipo_Usuario, "VENTAS")) { // SI SE SELECCIONA USUARIO ADMINISTRADOR
                    if(textCedula_crear.getText().length()==10){
                        try{ // INICIO BDD USUARIO ADMIN: VERIFICAR SI EL CLIENTE EXISTE
                            // SE OBTIENE CONEXIÓN BDD
                            Connection conexion;
                            conexion = getConection();
                            s = conexion.createStatement();
                            rs = s.executeQuery("SELECT * FROM usuario_venta");

                            // SE OBTIENE LOS RESULTADOS
                            while (rs.next()) {
                                String verificarCedula = rs.getString(1);
                                if(Objects.equals(verificarCedula, textCedula_crear.getText())){ // SI EL USUARIO EXISTE: NO SE AGREGA CLIENTE
                                    encontrado = true;
                                }
                            }
                            if(encontrado){
                                JOptionPane.showMessageDialog(null, "USUARIO EXISTE");
                            }else{
                                //CONSULTA PARAMETRIZADA: INSERTAR ADMIN
                                ps = conexion.prepareStatement(" INSERT INTO usuario_venta VALUES (?,?) ");
                                ps.setString(1, textCedula_crear.getText());
                                ps.setString(2, textNombre_Crear.getText());

                                // RESULTADO DE LA ACTUALIZACIÓN
                                res = ps.executeUpdate();
                                if (!(res > 0)) {
                                    JOptionPane.showMessageDialog(null, "DATOS DE VENTAS NO GUARDADO");
                                }

                                //CONSULTA PARAMETRIZADA: INSERTAR LOGIN DE ADMIN
                                ps = conexion.prepareStatement(" INSERT INTO login_venta VALUES (?,?,?) ");
                                ps.setString(1, textCedula_crear.getText());
                                ps.setString(2, textUser_Crear.getText());
                                ps.setString(3, textContrasenia_Crear.getText());

                                // RESULTADO DE LA ACTUALIZACIÓN
                                res = ps.executeUpdate();
                                if (!(res > 0)) {
                                    JOptionPane.showMessageDialog(null, "DATOS DE VENTAS NO GUARDADO");
                                }
                                // CIERRE DE CONEXIÓN
                                ps.close();
                            }
                            // CIERRE DE CONEXIÓN
                            conexion.close();
                            rs.close();
                            s.close();
                        }catch (Exception ex) {
                            ex.printStackTrace();
                        } // FIN BDD CLIENTES
                    }else{
                        JOptionPane.showMessageDialog(null, "RANGO INCORRECTO");
                    }
                    try{ // INICIO BDD CARGAR NUEVAMENTE USUARIO VENTAS
                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT usuario_venta.*, login_venta.usuario_user, login_venta.contrasenia_user  FROM (usuario_venta,login_venta) WHERE ci_user = FK_ci_user");

                        // SE OBTIENE LOS DATOS
                        rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        // SE CREA LA TABLA Y EL MODELO
                        modelo_ventas = (DefaultTableModel) table_ventas.getModel();

                        // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
                        modelo_ventas.setColumnCount(0);// Se elimina la columna de la tabla
                        modelo_ventas.setRowCount(0); // Se elimina las filas de la tabla

                        // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                        for (int i = 1; i <= columnCount; i++) {
                            modelo_ventas.addColumn(rsmd.getColumnName(i));
                        }
                        // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            modelo_ventas.addRow(row);
                        }
                        // CIERRE DE CONEXIÓN
                        rs.close();
                        s.close();
                        conexion.close();
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } // FIN BDD CARGAR NUEVAMENTE USUARIO VENTAS
                }
                try {// INICIO BDD CARGAR DATOS DE USUARIOS
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA: OBTENER PRODUCTOS
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT ci_ad AS cedula FROM admin UNION SELECT ci_user AS cedula FROM usuario_venta ");

                    // ELIMINACIÓN DE DATOS INICIALES EN COMBO BOX
                    comboBoxCEDULA.removeAllItems();
                    // CREACIÓN DE UN ESPACIO EN BLANCO
                    comboBoxCEDULA.addItem(" ");
                    // SE OBTIENEN LOS DATOS REQUERIDOS DE PRODUCTOS
                    while (rs.next()) {
                        comboBoxCEDULA.addItem(rs.getString(1));
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD CARGAR DATOS DE USUARIOS
            }
        });
    }
    public static Connection getConection() { // SE REALIZA LA CONEXIÓN CON BDD
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
