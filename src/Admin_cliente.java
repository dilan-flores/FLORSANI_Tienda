import com.mysql.cj.x.protobuf.MysqlxCrud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class Admin_cliente {
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
    private JFormattedTextField textCLIENTE_CREAR;
    private JFormattedTextField textNOMBRE_CREAR;
    private JButton agregarButton;
    DefaultTableModel modelo = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS QUE SE MOSTRARÁN EN LA TABLA
    private JTable table; // PERMITE MOSTRAR LOS DATOS EN FORMA DE TABLA
    private JFormattedTextField textCUENTA;
    private JFormattedTextField textVALOR_CREAR;

    String ced;
    Boolean encontrado;
    public Admin_cliente(){
        try{ // INICIO BDD CARGAR PRODUCTOS E INVENTARIO
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT *  FROM cliente");

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
            rs = s.executeQuery("SELECT * FROM cliente ");

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
        } // FIN BDD CARGAR PRODCUTOS

        buscarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN BUSCAR CLIENTE
            @Override
            public void actionPerformed(ActionEvent e) {
                try { // INICIO BDD CARGAR CLIENTE
                    // OBTIENE CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // OBTIENE EL ID DEL PRODUCTO DEL COMBO BOX
                    ced = (String)comboBoxCEDULA.getSelectedItem();
                    //System.out.println(cod);
                    // CONSULTA: SE OBTIENE LOS DATOS DE LA TABLA PRODUCTOS POR MEDIO DE LA TABLA INVENTARIO Y CON UN ID ESPECÍFICO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT * FROM cliente WHERE ci_cl =" + ced);

                    // SE OBTIENE LOS RESULTADOS Y SE PRESENTAN POR PANTALLA
                    while (rs.next()) {
                        textNOMBRE.setText(rs.getString(2));
                        textCUENTA.setText(rs.getString(3));
                    }
                    //CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD CARGAR CLIENTE
            }
        }); // FIN ACCIÓN BUSCAR CLIENTE

        actualizarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN ACTUALIZAR CLIENTE
            @Override
            public void actionPerformed(ActionEvent e) {
                try {// INICIO BDD ACTUALIZAR PRODUCTO
                    // SE OBTIENE CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    //CONSULTA PARAMETRIZADA: ACTUALIZAR NOMBRE Y VALOR DE CUENTA DEL CLIENTE
                    ps = conexion.prepareStatement("UPDATE cliente SET nombres_cl=?, estado_cuenta=? WHERE ci_cl = " + ced);
                    ps.setString(1, textNOMBRE.getText());
                    ps.setString(2, textCUENTA.getText());

                    // RESULTADO DE LA ACTUALIZACIÓN
                    res = ps.executeUpdate();
                    if (!(res > 0)) {
                        JOptionPane.showMessageDialog(null, "CLIENTE NO ACTUALIZADO");
                    }
                    // CIERRE DE CONEXIÓN
                    ps.close();
                    conexion.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD ACTUALIZAR PRODUCTO

                try{ // INICIO BDD CARGAR PRODUCTOS E INVENTARIO
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT *  FROM cliente");

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
            }
        }); // FIN ACCIÓN ACTUALIZAR CLIENTE

        eliminarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN ELIMINAR CLIENTE
            @Override
            public void actionPerformed(ActionEvent e) {
                encontrado = false;
                try{ // INICIO BDD CABECERA DE FACTURA
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();

                    ced = (String)comboBoxCEDULA.getSelectedItem();
                    // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT FKci_cl  FROM cab_trans WHERE FKci_cl=" + ced);

                    // SE OBTIENE LOS RESULTADOS Y SE PRESENTAN POR PANTALLA
                    while (rs.next()) {
                        String verificar_cedula = rs.getString(1);
                        encontrado = true;
                    }

                    if(encontrado){
                        JOptionPane.showMessageDialog(null, "CLIENTE NO ELIMINADO: EXISTE REGISTRO EN FACTURA");
                    }else{
                        try {// INICIO BDD ELIMINAR CLIENTE
                            // SE OBTIENE CONEXIÓN BDD
                            //Connection conexion;
                            //conexion = getConection();
                            //CONSULTA PARAMETRIZADA: ELIMINAR CLIENTE
                            ps = conexion.prepareStatement(" DELETE FROM cliente WHERE ci_cl = " + ced);
                            //ps.setString(1, ced);

                            // RESULTADO DE LA ACTUALIZACIÓN
                            res = ps.executeUpdate();
                            if (!(res > 0)) {
                                JOptionPane.showMessageDialog(null, "CLIENTE NO ELIMINADO");
                            }else{
                                textNOMBRE.setText("");
                                textCUENTA.setText("");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } // FIN BDD ELIMINAR CLIENTE
                    }
                    //CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD CABECERA DE FACTURA

                try{ // INICIO BDD CARGAR PRODUCTOS E INVENTARIO
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT *  FROM cliente");

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
            }
        }); // FIN ACCIÓN ELIMINAR CLIENTE

        agregarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN AGREGAR CLIENTE
            @Override
            public void actionPerformed(ActionEvent e) {
                encontrado = false;
                try {// INICIO BDD AGREGAR NUEVO USUARIO
                    if(textCLIENTE_CREAR.getText().length()==10){ // CÉDULA DE 10 DÍGITOS
                        try{ // INICIO BDD CLIENTES: VERIFICAR SI EL CLIENTE EXISTE
                            // SE OBTIENE CONEXIÓN BDD
                            Connection conexion;
                            conexion = getConection();
                            s = conexion.createStatement();
                            rs = s.executeQuery("SELECT *  FROM cliente");

                            // SE OBTIENE LOS RESULTADOS
                            while (rs.next()) {
                                String verificarCliente = rs.getString(1);
                                if(Objects.equals(verificarCliente, textCLIENTE_CREAR.getText())){ // SI EL CLIENTE EXISTE: NO SE AGREGA CLIENTE
                                    encontrado = true;
                                }
                            }
                            if(encontrado){
                                JOptionPane.showMessageDialog(null, "CLIENTE EXISTE");
                            }else{
                                //CONSULTA PARAMETRIZADA: INSERTAR NUEVO CLIENTE
                                ps = conexion.prepareStatement(" INSERT INTO cliente VALUES (?,?,?) ");
                                ps.setString(1, textCLIENTE_CREAR.getText());
                                ps.setString(2, textNOMBRE_CREAR.getText());
                                ps.setString(3, textVALOR_CREAR.getText());

                                // RESULTADO DE LA ACTUALIZACIÓN
                                res = ps.executeUpdate();
                                if (!(res > 0)) {
                                    JOptionPane.showMessageDialog(null, "DATOS DE CLIENTE NO GUARDADO");
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD AGREGAR NUEVO USUARIO

                try{ // INICIO BDD CARGAR PRODUCTOS E INVENTARIO
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT *  FROM cliente");

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

                try {// INICIO BDD CARGAR NUEVAMENTE LOS PRODUCTOS EN COMBO BOX
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA: OBTENER PRODUCTOS
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT * FROM cliente ");

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
                } // FIN BDD CARGAR NUEVAMENTE LOS PRODUCTOS EN COMBO BOX
            }
        }); // FIN ACCIÓN AGREGAR CLIENTE
    }

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
