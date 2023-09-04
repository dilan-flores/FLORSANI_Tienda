// PERMITE CREAR INTERFACES DE USUARIO
import javax.swing.*;
// PERMITE MANEJAR TABLAS
import javax.swing.table.DefaultTableModel;
// PERMITE MANEJAR LAS ACCIONES DE BOTONES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// PERMITE TRABAJR CON JDBC, PARA LA CONEXIÓN CON LA BASE DE DATOS
import java.sql.*;

// PERMITE RESTRINGUIR A SOLO NÚMEROS
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
public class venta_producto {

    public JPanel panel; // CONTENEDOR DE ELEMENTOS
                                                                        /* SQL */
    Statement s; // ENVIA CONSULTAS SQL A LA BDD
    ResultSet rs; // ALMACENA LOS RESULTADOS DE LA CONSULTA
    PreparedStatement ps; // PERMITE PARAMETRIZAR LAS CONSULTAS EN BDD (ACTUALIZAR,INSERCIÓN)
    int res; // RESULTADO DE CONSULTA (INSERCIÓN,ACTUALIZACIÓN, ELIMINACIÓN)

                                                                        /* CLIENTE */
    private JFormattedTextField textCUENTA; // VALOR DE CUENTA DEL CLIENTE
    private JFormattedTextField textCEDULA; // CÉDULA DEL CLIENTE
    private JFormattedTextField textNOMBRE; // NOMBRE COMPLETO DEL CLIENTE
    private JButton buscarCLIENTE; // BOTÓN BUSCAR CLIENTE
    private JFormattedTextField textAGREGAR_A_CUENTA; // VALOR A AGREGAR EN LA CUENTA DEL CLIENTE
    private JButton Agregar_CuentaButton; // BOTÓN AGREGAR VALOR EN LA CUENTA DEL CLIENTE
    private JButton registrarCLIENTE; // BOTÓN REGISTRO DE CLIENTE

                                                                        /* PRODUCTO */
    private JFormattedTextField textCODIGO; // ID DEL PRODUCTO
    private JFormattedTextField textPRODUCTO; // NOMBRE DEL PRODUCTO
    private JFormattedTextField textPRECIO; // PRECIO DE VENTA DEL PRODUCTO
    private JFormattedTextField textCANTIDAD; // CANTIDAD EXISTENTE DE PRODUCTO
    private JFormattedTextField textCANTIDAD_A_COMPRAR; // CANTIDAD DE PRODUCTO A COMPRAR POR EL CLIENTE
    private JButton buscarPRODUCTO; // BOTÓN BUSCAR PRODUCTO
    private JButton agregarPRODUCTO; // BOTÓN AGREGAR PRODUCTO AL CARRITO
    private JButton guardarButton; // BOTÓN GUARDAR COMPRA DEL CLIENTE

                                                                        /* VARIABLES EXTRAS */
    private JButton LimpiarCajaButton; // LIMPIA EL CONTENIDO PARA UNA NUEVA VENTA
    private JTable table; // PERMITE MOSTRAR LOS DATOS EN FORMA DE TABLA
    private JButton CancelarButton;
    private JTable tablaListaProductos; // PERMITE MOSTRAR LOS DATOS EN FORMA DE TABLA LISTA DE PRODUCTOS
    private JTable tablaListaClientes;
    String cod; // OBTIENE EL ID DEL CLIENTE CON UN FORMATO ADECUADO
    String ced; // OBTIENE LA CÉDULA DEL CLIENTE EN UN FORMATO ADECUADO
    DefaultTableModel modelo = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS QUE SE MOSTRARÁN EN LA TABLA
    DefaultTableModel modelo_lista_producto = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS DE LISTA DE PRODUCTOS QUE SE MOSTRARÁN EN LA TABLA
    DefaultTableModel modelo_lista_cliente = new DefaultTableModel(); // (CONTENEDOR DE LA TABLA) PERMITE MANETENER Y DEFINIR LOS DATOS DE LISTA DE PRODUCTOS QUE SE MOSTRARÁN EN LA TABLA
    boolean encontrado; // VERIFICA SI SE ENCONTRO LA INFORMACIÓN REQUERIDA EN LA BDD
    JFormattedTextField precio_total_producto = new JFormattedTextField(); // RECIBE EL PRECIO TOTAL DE UN PRODUCTO (CANTIDAD * PRECIO DE VENTA)
    JFormattedTextField actualizar_cantidad = new JFormattedTextField(); // RECIBE LA CANTIDAD DE PRODUCTOS ACTUALIZADOS PARA INGRESAR EN INVENTARIO
    JFormattedTextField actualizar_precio_total = new JFormattedTextField(); // RECIBE LA ACTUALIZACÓN DEL PRECIO TOTAL DE UN PRODUCTO PARA INGRESAR EN INVENTARIO
    JFormattedTextField actualizar_valor_cuenta = new JFormattedTextField(); // RECIBE LA ACTUALIZACIÓN DEL VALOR DE CUENTA DEL CLIENTE
    String VALOR_A_PAGAR= String.valueOf(0.0); // REALIZA TODOS LOS CÁLCULOS PARA EL VALOR TOTAL DE LA COMPRA
    JFormattedTextField total_factura = new JFormattedTextField(); // RECIBE EL VALOR TOTAL DE LA COMPRA
    JFormattedTextField Num_factura = new JFormattedTextField(); // SE OBTIENE EL NÚMERO DE FACTURA CON NÚMERO SIGUIENTE AL ANTERIOR
    ResultSetMetaData rsmd; // SE OBTIENE LA INFORMACIÓN DE LOS METADATOS DE RS

    public venta_producto() {

        // CONFIGURACIÓN DEL DOCUMENTFILTER PARA RESTRINGUIR LOS CARACTERES A NÚMEROS
        AbstractDocument doc = (AbstractDocument) textCANTIDAD_A_COMPRAR.getDocument();
        AbstractDocument doc2 = (AbstractDocument) textCEDULA.getDocument();
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
        doc2.setDocumentFilter(new DocumentFilter() {
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

        // BLOQUEO DE EDICIÓN EN CLIENTE
        textNOMBRE.setEnabled(false);
        textCUENTA.setEnabled(false);
        textAGREGAR_A_CUENTA.setEnabled(false);

        // BLOQUEO DE EDICIÓN EN PRODUCTOS
        textPRODUCTO.setEnabled(false);
        textPRECIO.setEnabled(false);
        textCANTIDAD.setEnabled(false);
        textCANTIDAD_A_COMPRAR.setEnabled(false);

        // EVITA LA ACCIÓN DE BOTÓN
        registrarCLIENTE.setEnabled(false);
        Agregar_CuentaButton.setEnabled(false);
        agregarPRODUCTO.setEnabled(false);

        guardarButton.setVisible(false); // INICIALIZACIÓN DE BOTÓN GUARDAR COMO NO VISIBLE

        encontrado = false; // INICIALIZACIÓN COMO NO ENCONTRADO

        try{ // INICIO BDD CARGAR PRODUCTOS E INVENTARIO
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT producto.id_producto, producto.producto FROM producto");

            // SE OBTIENE LOS DATOS
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            // SE CREA LA TABLA Y EL MODELO
            modelo_lista_producto = (DefaultTableModel) tablaListaProductos.getModel();

            // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
            modelo_lista_producto.setColumnCount(0);// Se elimina la columna de la tabla
            modelo_lista_producto.setRowCount(0); // Se elimina las filas de la tabla

            // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
            for (int i = 1; i <= columnCount; i++) {
                modelo_lista_producto.addColumn(rsmd.getColumnName(i));
            }
            // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo_lista_producto.addRow(row);
            }
            // CIERRE DE CONEXIÓN
            rs.close();
            s.close();
            conexion.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        } // FIN BDD CARGAR PRODUCTOS E INVENTARIO

        try{ // INICIO BDD CARGAR CLIENTE
            // CONEXIÓN BDD
            Connection conexion;
            conexion = getConection();
            // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT cliente.ci_cl, cliente.nombres_cl FROM cliente");

            // SE OBTIENE LOS DATOS
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            // SE CREA LA TABLA Y EL MODELO
            modelo_lista_cliente = (DefaultTableModel) tablaListaClientes.getModel();

            // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
            modelo_lista_cliente.setColumnCount(0);// Se elimina la columna de la tabla
            modelo_lista_cliente.setRowCount(0); // Se elimina las filas de la tabla

            // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
            for (int i = 1; i <= columnCount; i++) {
                modelo_lista_cliente.addColumn(rsmd.getColumnName(i));
            }
            // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo_lista_cliente.addRow(row);
            }
            // CIERRE DE CONEXIÓN
            rs.close();
            s.close();
            conexion.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        } // FIN BDD CARGAR CLIENTE

                                                                        /* PRODUCTO */

        buscarPRODUCTO.addActionListener(new ActionListener() { //INICIO ACCIÓN DE BUSCAR PRODUCTO
            @Override
            public void actionPerformed(ActionEvent e) {
                encontrado = false; // INICIALIZACIÓN COMO NO ENCONTRADO
                try { // INICIO BDD BUSCAR DATOS DE PRODUCTO
                    // SE REALIZA LA CONEXIÓN
                    Connection conexion;
                    conexion = getConection();
                    // SE OBTIENE EL ID DEL PRODUCTO CON UN FORMATO ADECUADO
                    cod = "\"" + textCODIGO.getText() + "\"";
                    // CONSULTA: DATOS DE LA TABLA PRODUCTOS E INVENTARIO
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT (Select producto from producto where id_producto = FK_id_producto) as producto, (Select precio_venta_unitario from producto where id_producto = FK_id_producto) as precio_venta, cantidad FROM inventario WHERE FK_id_producto=" + cod);

                    // SE OBTIENE LOS DATOS DE LOS CAMPOS REQUERIDO
                    while (rs.next()) {
                        textPRODUCTO.setText(rs.getString(1));
                        textPRECIO.setText(rs.getString(2));
                        textCANTIDAD.setText(rs.getString(3));
                        encontrado = true; // SE ENCONTRÓ LOS DATOS DE CAMPOS
                    }

                    if(!encontrado){ // NO SE ENCONTRÓ
                        JOptionPane.showMessageDialog(null, "PRODUCTO NO ENCONTRADOS");
                        // SE VACIAN LOS JFormattedTextField
                        textPRODUCTO.setText("");
                        textPRECIO.setText("");
                        textCANTIDAD.setText("");
                        textCANTIDAD_A_COMPRAR.setText("");
                        // SE INHABILITA
                        textCANTIDAD_A_COMPRAR.setEnabled(false);
                        agregarPRODUCTO.setEnabled(false);
                    }else{
                        // SE HABILITA PARA AGREGAR EN CARRITO
                        textCANTIDAD_A_COMPRAR.setEnabled(true);
                        agregarPRODUCTO.setEnabled(true);
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // FIN BDD BUSCAR DATOS DE PRODUCTO
            }
        }); //FIN ACCIÓN DE BUSCAR PRODUCTO

        agregarPRODUCTO.addActionListener(new ActionListener() { // INICIO ACCIÓN AGREGAR PRODUCTO
            @Override
            public void actionPerformed(ActionEvent e) {
                try{ // INICIO BDD ACTUALIZAR INVENTARIO
                    // RESTRICCIÓN: SI EL VALOR A AGREGAR ES MENOR O IGUAL A LA CANTIDAD DE PRODCUTOS EXISTENTES
                    if(Integer.parseInt(textCANTIDAD_A_COMPRAR.getText()) <= Integer.parseInt(textCANTIDAD.getText())){
                        // PRECIO DE CADA PRODUCTO PARA AGREGAR AL CARRITO
                        precio_total_producto.setText(String.valueOf(Integer.parseInt(textCANTIDAD_A_COMPRAR.getText())*Float.parseFloat(textPRECIO.getText())));
                        // FUNCIÓN: AGREGA EL PRODUCTO EN LA TABLA
                        agregar();

                        // SE OBTIONE LA CONEXIÓN CON BDD
                        Connection conexion;
                        conexion = getConection();
                        // SE OBTIENE EL ID DEL PRODUCTO CON UN FORMATO ADECUADO
                        cod = "\"" + textCODIGO.getText() + "\"";
                        // CONSULTA PARAMETRIZADA EN INVENTARIO
                        ps = conexion.prepareStatement("UPDATE inventario SET cantidad=?, precio_total=? WHERE FK_id_producto = " + cod);
                        // SE CALCULA LA ACTUALIZACIÓN DE CANTIDAD DE PRODUCTOS EXISTENTES
                        actualizar_cantidad.setText(String.valueOf(Integer.parseInt(textCANTIDAD.getText())-Integer.parseInt(textCANTIDAD_A_COMPRAR.getText())));
                        ps.setString(1, actualizar_cantidad.getText()); // SE INSERTA LA ACTUALIZACIÓN EN INVENTARIO
                        // SE CALCULA LA ACTUALIZACIÓN DE PRECIO RESPECTO A LA CANTIDAD DE PRODUCTOS EN EXISTENTES
                        actualizar_precio_total.setText(String.valueOf(Integer.parseInt(actualizar_cantidad.getText())*Float.parseFloat(textPRECIO.getText())));
                        ps.setString(2, actualizar_precio_total.getText()); // SE INSERTA LA ACTUALIZACIÓN EN INVENTARIO
                        // SE PRESENTA POR PANTALLA LA ACTUALIZACIÓN DE CANTIDAD
                        textCANTIDAD.setText(actualizar_cantidad.getText());
                        // SE VACÍA EL CAMPO
                        textAGREGAR_A_CUENTA.setText("");

                        // RESULTADOS DE LA CONSULTA
                        res = ps.executeUpdate();
                        if(!(res >0)){
                            JOptionPane.showMessageDialog(null,"CANTIDAD DE PRODUCTO NO ACTUALIZADO");
                        }
                        // SE CIERRA LA CONEXIÓN
                        conexion.close();
                        ps.close();
                    }else{
                        JOptionPane.showMessageDialog(null,"EXISTE "+ textCANTIDAD.getText() + " " + textPRODUCTO.getText() + " EN INVENTARIO");
                    }

                }catch (Exception ex) {
                    ex.printStackTrace();
                }
                // SE HABILITA EL BOTÓN GUARDAR SI SE ESPECIFICÓ UN CLIENTE Y SI SE AGREGÓ UN PRODUCTO EN CARRITO
                if ((!textNOMBRE.getText().isEmpty()) && ((!textCANTIDAD_A_COMPRAR.getText().isEmpty()))){
                    guardarButton.setVisible(true);
                }
            } // FIN BDD ACTUALIZAR INVENTARIO
        }); // FIN ACCIÓN AGREGAR PRODUCTO

                                                                        /*  CLIENTE   */

        buscarCLIENTE.addActionListener(new ActionListener() { // INICIO ACCIÓN BUSCAR CLIENTE
            @Override
            public void actionPerformed(ActionEvent e) {
                encontrado = false;
                try { // INICIO BDD BUSCAR CLIENTE
                    // INHABILITA CAMPOS DE CLIENTE
                    textNOMBRE.setEnabled(false);
                    textCUENTA.setEnabled(false);
                    registrarCLIENTE.setEnabled(false);
                    // RESTRICCIÓN: NÚMERO DE CÉDULA CON 10 DÍGITOS
                    if(textCEDULA.getText().length()==10){
                        // CONEXIÓN CON LA BDD
                        Connection conexion;
                        conexion = getConection();
                        // SE OBTIENE LA CÉDULA EN UN FORMATO ADECUADO
                        ced = "\"" + textCEDULA.getText() + "\"";
                        //CONSULTA EN CLIENTE
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT * FROM cliente WHERE ci_cl = " + ced);
                        // RECIBE LOS DATOS REQUERIDOS
                        while (rs.next()) {
                            textNOMBRE.setText(rs.getString(2));
                            textCUENTA.setText(rs.getString(3));
                            encontrado = true; // LA CONSULTA FUÉ EXITOSA
                        }

                        if(!encontrado){
                            JOptionPane.showMessageDialog(null, "CLIENTE NO ENCONTRADOS");
                            // SE VACÍAN LOS CAMPOS DE JFormattedTextField
                            textNOMBRE.setText("");
                            textCUENTA.setText("");
                            textNOMBRE.setEnabled(true);
                            textCUENTA.setEnabled(true);
                            registrarCLIENTE.setEnabled(true);
                            textAGREGAR_A_CUENTA.setEnabled(false);
                            Agregar_CuentaButton.setEnabled(false);
                        }else{
                            // SE HABILITAR LOS CAMPOS
                            registrarCLIENTE.setEnabled(false); // COMO EXISTE EL CLIENTE, SE DESHABILITA REGISTRAR CLIENTE
                            textAGREGAR_A_CUENTA.setEnabled(true);
                            Agregar_CuentaButton.setEnabled(true);
                            textAGREGAR_A_CUENTA.setEnabled(true);
                            Agregar_CuentaButton.setEnabled(true);
                        }
                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        rs.close();
                        s.close();
                    }else{
                        JOptionPane.showMessageDialog(null, "RANGO INCORRECTO");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }// FIN BDD BUSCAR CLIENTE
                // SE HABILITA EL BOTÓN GUARDAR SI SE ESPECIFICÓ UN CLIENTE Y SI SE AGREGÓ UN PRODUCTO EN CARRITO
                if ((!textNOMBRE.getText().isEmpty()) && ((!textCANTIDAD_A_COMPRAR.getText().isEmpty()))){
                    guardarButton.setVisible(true);
                }
            }
        }); // FIN ACCIÓN BUSCAR CLIENTE

        Agregar_CuentaButton.addActionListener(new ActionListener() { // INICIO ACCIÓN AGREGAR VALOR CUENTA
            @Override
            public void actionPerformed(ActionEvent e) {
                try{// INICIO BDD ACTUALIZAR VALOR DE CUENTA DE CLIENTE
                    // SE CREA CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // RECIBE LA CÉDULA CON UN FORMATO ADECUADO
                    ced = "\"" + textCEDULA.getText() + "\"";
                    ps = conexion.prepareStatement("UPDATE cliente SET estado_cuenta=? WHERE ci_cl = " + ced);
                    // SE CALCULAR EL VALOR DE CUENTA
                    actualizar_valor_cuenta.setText(String.valueOf(Float.parseFloat(textAGREGAR_A_CUENTA.getText())+Float.parseFloat(textCUENTA.getText())));

                    //System.out.print(actualizar_valor_cuenta.getText());
                    ps.setString(1, actualizar_valor_cuenta.getText()); // SE INSERTA EL VALOR DE CUENTA ACTUALIZADO
                    // PRESENTA EL VALOR DE CUENTA ACTUALIZADO
                    textCUENTA.setText(actualizar_valor_cuenta.getText());
                    // SE VACÍA
                    textAGREGAR_A_CUENTA.setText("");

                    // RESULTADO DE LA CONSULTA
                    res = ps.executeUpdate();
                    if(!(res >0)){
                        JOptionPane.showMessageDialog(null,"CANTIDAD DE PRODUCTO NO ACTUALIZADO");
                    }

                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    ps.close();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }// INICIO BDD ACTUALIZAR VALOR DE CUENTA DE CLIENTE
            }
        }); // FIN ACCIÓN AGREGAR VALOR CUENTA

        registrarCLIENTE.addActionListener(new ActionListener() { // INICIO ACCION REGISTRO CLIENTE
            @Override
            public void actionPerformed(ActionEvent e) {
                try {// INICIO BDD INSERTAR NUEVO CLIENTE
                    // SE OBTIENE LA CONECCIÓN BDD
                    Connection conexion;
                    conexion = getConection();
                    // CONSULTA PARAMETRIZADA
                    ps = conexion.prepareStatement("Insert into cliente values (?,?,?)");
                    ps.setString(1, textCEDULA.getText());
                    ps.setString(2, textNOMBRE.getText());
                    ps.setString(3, textCUENTA.getText());
                    // RSULTADOS DE LA CONSULTA
                    res = ps.executeUpdate();
                    if(res >0){
                        JOptionPane.showMessageDialog(null,"CLIENTE " + textNOMBRE.getText() + " AGREGADO");
                        // SE INHABILITA LA EDICIÓN
                        textNOMBRE.setEnabled(false);
                        textCUENTA.setEnabled(false);

                        // VACIAR CONTENIDO DE TABLA
                        modelo_lista_cliente.setColumnCount(0);
                        modelo_lista_cliente.setRowCount(0);
                        modelo_lista_cliente.fireTableStructureChanged(); // Actualiza la estructura de la tabla
                        tablaListaClientes.repaint();

                        // CARGAR NUEVAMENTE LA TABLA
                        try{ // INICIO BDD CARGAR CLIENTE

                            // NO SE ESTABLECE CONEXIÓN PORQUE AÚN NO SE CIERRA LA PREVIA CONEXIÓN
                            // CONSULTA: CARGAR DATOS DE PRODUCTO E INVENTARIO
                            s = conexion.createStatement();
                            rs = s.executeQuery("SELECT cliente.ci_cl, cliente.nombres_cl FROM cliente");

                            // SE OBTIENE LOS DATOS
                            rsmd = rs.getMetaData();
                            int columnCount = rsmd.getColumnCount();
                            // SE CREA LA TABLA Y EL MODELO
                            modelo_lista_cliente = (DefaultTableModel) tablaListaClientes.getModel();

                            // ELIMINACIÓN DE DATOS EN LA TABLA PARA CARGAR NUEVAMENTE
                            modelo_lista_cliente.setColumnCount(0);// Se elimina la columna de la tabla
                            modelo_lista_cliente.setRowCount(0); // Se elimina las filas de la tabla

                            // SE AGREGA LAS COLUMNAS A LA TABLA DEL MODELO
                            for (int i = 1; i <= columnCount; i++) {
                                modelo_lista_cliente.addColumn(rsmd.getColumnName(i));
                            }
                            // SE VAN OBTENIENDO LOS DATOS E INSERTANDO EN LA TABLA
                            while (rs.next()) {
                                Object[] row = new Object[columnCount];
                                for (int i = 1; i <= columnCount; i++) {
                                    row[i - 1] = rs.getObject(i);
                                }
                                modelo_lista_cliente.addRow(row);
                            }
                            // CIERRE DE CONEXIÓN
                            //rs.close();
                            //s.close();
                            //conexion.close();
                        }catch (Exception ex) {
                            ex.printStackTrace();
                        } // FIN BDD CARGAR CLIENTE
                    }else{
                        JOptionPane.showMessageDialog(null,"CLIENTE NO GUARDADO");
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    ps.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }// FIN BDD INSERTAR NUEVO CLIENTE
            }
        });// FIN ACCION REGISTRO CLIENTE

        // SE AGREGA LA CABECERA DE LA TABLA
        String[] titulo = new String[]{"CÓDIGO", "PRODUCTO", "CANTIDAD", "PRECIO"};
        modelo.setColumnIdentifiers(titulo);
        table.setModel(modelo);

        guardarButton.addActionListener(new ActionListener() { // INICIO ACCIÓN GUARDAR COMPRA
            @Override
            public void actionPerformed(ActionEvent e) {
                try {// INICIO BDD INSERCIÓN CABECERA DE FACTURA
                    // SE AGREGA EL VALOR TOTAL DE LA COMPRA
                    total_factura.setText(VALOR_A_PAGAR);
                    // SE OBTIENE LA CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();

                    // CONSULTA
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT COUNT(*) FROM cab_trans");
                    if (rs.next()) {
                        int rowCount = rs.getInt(1);
                        if (rowCount == 0) { // SI LA CABECERA DE FACTURA ESTÁ FACÍA SE INGRESA "1"
                            Num_factura.setText("1");
                        } else {  // SE OBTIENE EL VALOR DE LA FACTURA ANTERIOR Y SE AUMENTA PARA LA NUEVA FACTURA
                            rs = s.executeQuery("SELECT num_f FROM cab_trans ORDER BY CAST(num_f AS SIGNED) DESC LIMIT 1");
                            while (rs.next()) {
                                Num_factura.setText(rs.getString(1));
                                //encontrado = true;
                            }
                            String factura = String.valueOf((Integer.parseUnsignedInt(Num_factura.getText()) + 1));
                            Num_factura.setText(factura);
                        }
                    }

                    // CONSULTA PARAMETRIZADA: SE INSERTA LA CABECERA DE FACTURA
                    ps = conexion.prepareStatement("INSERT into cab_trans values (?,CURDATE(),?,?)");
                    ps.setString(1, Num_factura.getText());
                    ps.setString(2, total_factura.getText());
                    ps.setString(3, textCEDULA.getText());

                    //System.out.println(ps);
                    // RESULTADOS DE LA CONSULTA
                    res = ps.executeUpdate();
                    if (!(res > 0)) {
                        JOptionPane.showMessageDialog(null, "DATOS EN CABECERA DE FACTURA NO GUARDADOS");
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                    ps.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }// FIN BDD INSERCIÓN CABECERA DE FACTURA

                try { // INCIO BDD INSERCIÓN DETALLE DE FACTURA

                    // SE OBTIENE EL MODELO DE LA TABLA
                    javax.swing.table.TableModel modeloTabla = table.getModel();

                    // RECORRE LAS FILAS DE LA TABLA
                    int filas = modeloTabla.getRowCount();
                    for (int fila = 0; fila < filas; fila++) {
                        //System.out.println("filas: " + filas);
                        //System.out.println("fila: " + fila);
                        // SE OBTIENE LOS VALORES DE CADA CELDA EN LA FILA ACTUAL
                        String cod_producto = modeloTabla.getValueAt(fila, 0).toString();
                        //System.out.println("Cod del producto: " + cod_producto);
                        //String producto = modeloTabla.getValueAt(fila, 1).toString();
                        int cantidad = Integer.parseInt(modeloTabla.getValueAt(fila, 2).toString());
                        double precio = Double.parseDouble(modeloTabla.getValueAt(fila, 3).toString());

                        // CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: INSERCIÓN DE TABLA EN DETALLE DE FACTURA
                        ps = conexion.prepareStatement("INSERT INTO det_trans (FKnum_f, FKid_producto, cantidad_dt, precio_dt) VALUES (?, ?, ?, ?)");
                        ps.setString(1, Num_factura.getText());
                        ps.setInt(2, Integer.parseInt(cod_producto));
                        ps.setInt(3, cantidad);
                        ps.setDouble(4, precio);
                        // RESULTADOS DE LA CONSULTA
                        res = ps.executeUpdate();
                        if(!(res >0)){
                            JOptionPane.showMessageDialog(null,"NO GUARDADO EN DETALLE DE FACTURA");
                        }

                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        ps.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } // FIN BDD INSERCIÓN DETALLE DE FACTURA

                try{// INICIO BDD ACTUALIZAR ESTADO DE CUENTA DEL CLIENTE
                    // CONEXIÓN BDD
                    Connection conexion;
                    conexion = getConection();

                    // SE OBTIENE LA CÉDULA CON UN FORMATO ADECUADO
                    ced = "\"" + textCEDULA.getText() + "\"";
                    // CONSULTA: SE OBTIENE EL ESTADO DE CUENTA DEL CLIENTE
                    s = conexion.createStatement();
                    rs = s.executeQuery("SELECT estado_cuenta FROM cliente WHERE ci_cl =" + ced);
                    // SE RECIBE EL ESTADO DE CUENTA DEL CLIENTE
                    while (rs.next()) {
                        textCUENTA.setText(rs.getString(1));
                    }

                    // CONSULTA: ACTUALIZACIÓN ESTADO DE CUENTA DEL CLIENTE
                    ps = conexion.prepareStatement("UPDATE cliente SET estado_cuenta=? WHERE ci_cl = " + ced);
                    // SE CALCULA EL VALOR DE CUENTA ACTUALIZADO DEL CLIENTE
                    actualizar_valor_cuenta.setText(String.valueOf(Float.parseFloat(textCUENTA.getText())-Float.parseFloat(total_factura.getText())));
                    // SE INSERTA EL VALOR DE CUENTA ACTUALIZADO EN LA TABLA CLIENTE
                    ps.setString(1, actualizar_valor_cuenta.getText());

                    // RESULTADO DE LA CONSULTA
                    res = ps.executeUpdate();
                    if(!(res >0)){
                        JOptionPane.showMessageDialog(null,"CANTIDAD DE PRODUCTO NO ACTUALIZADO");
                    }
                    // CIERRE DE CONEXIÓN
                    conexion.close();
                    rs.close();
                    s.close();
                    ps.close();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }// FIN BDD ACTUALIZAR ESTADO DE CUENTA DEL CLIENTE

                // SE PASA A LA VENTANA DE FACTURACIÓN
                JFrame frame=new JFrame("FACTURACION");
                frame.setContentPane(new facturacion().Panel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        }); // FIN ACCIÓN GUARDAR COMPRA

        LimpiarCajaButton.addActionListener(new ActionListener() { // INICIO ACCIÓN LIMPIAR CAJA
            @Override
            public void actionPerformed(ActionEvent e) {
                // VACIAR CONTENIDO
                modelo.setColumnCount(0);
                modelo.setRowCount(0);
                modelo.fireTableStructureChanged(); // Actualiza la estructura de la tabla
                table.repaint();
                // SE AGREGA LA CABECERA DE LA TABLA
                String[] titulo = new String[]{"CÓDIGO", "PRODUCTO", "CANTIDAD", "PRECIO"};
                modelo.setColumnIdentifiers(titulo);
                table.setModel(modelo);
                textCEDULA.setText("");
                textNOMBRE.setText("");
                textCUENTA.setText("");
                textCODIGO.setText("");
                textPRODUCTO.setText("");
                textAGREGAR_A_CUENTA.setText("");
                textPRECIO.setText("");
                textCANTIDAD.setText("");
                textCANTIDAD_A_COMPRAR.setText("");

                // VALORES DE FACTURA CON VALOR CERO
                VALOR_A_PAGAR = String.valueOf(0.0); // REALIZA TODOS LOS CÁLCULOS PARA EL VALOR TOTAL DE LA COMPRA
                total_factura.setText(VALOR_A_PAGAR); // RECIBE EL VALOR TOTAL DE LA COMPRA
            }
        }); // FIN ACCIÓN LIMPIAR CAJA
        CancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { // INCIO BDD INSERCIÓN DETALLE DE FACTURA

                    // SE OBTIENE EL MODELO DE LA TABLA
                    javax.swing.table.TableModel modeloTabla = table.getModel();

                    // RECORRE LAS FILAS DE LA TABLA
                    int filas = modeloTabla.getRowCount();
                    for (int fila = 0; fila < filas; fila++) {
                        // SE OBTIENE LOS VALORES DE CADA CELDA EN LA FILA ACTUAL
                        String cod_producto = modeloTabla.getValueAt(fila, 0).toString();
                        int cantidad = Integer.parseInt(modeloTabla.getValueAt(fila, 2).toString());
                        double precio = Double.parseDouble(modeloTabla.getValueAt(fila, 3).toString());

                        Connection conexion;
                        conexion = getConection();

                        //CONSULTA EN CLIENTE
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT cantidad,precio_total FROM inventario WHERE FK_id_producto = " + cod_producto);
                        // RECIBE LOS DATOS REQUERIDOS
                        while (rs.next()) {
                            textCANTIDAD.setText(rs.getString(1));
                            textPRECIO.setText(rs.getString(2));
                        }
                        // CONSULTA: INSERCIÓN DE TABLA EN DETALLE DE FACTURA
                        ps = conexion.prepareStatement("UPDATE inventario SET cantidad=?, precio_total=? WHERE FK_id_producto = " + cod_producto);
                        // SE REESTABLECE LOS VALORES REDUCIDOS DE STOCK
                        textCANTIDAD.setText(String.valueOf(Integer.parseInt(textCANTIDAD.getText()) + cantidad));
                        textPRECIO.setText(String.valueOf(Float.parseFloat(textPRECIO.getText())+ precio));

                        ps.setInt(1, Integer.parseInt(textCANTIDAD.getText()));
                        ps.setDouble(2, Double.parseDouble(textPRECIO.getText()));
                        // RESULTADOS DE LA CONSULTA
                        res = ps.executeUpdate();
                        if(!(res >0)){
                            JOptionPane.showMessageDialog(null,"INVENTARIO NO ACTUALIZADO");
                        }

                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        ps.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } // FIN BDD INSERCIÓN DETALLE DE FACTURA

                // VACIAR CONTENIDO
                modelo.setColumnCount(0);
                modelo.setRowCount(0);
                modelo.fireTableStructureChanged(); // Actualiza la estructura de la tabla
                table.repaint();
                // SE AGREGA LA CABECERA DE LA TABLA
                String[] titulo = new String[]{"CÓDIGO", "PRODUCTO", "CANTIDAD", "PRECIO"};
                modelo.setColumnIdentifiers(titulo);
                table.setModel(modelo);
                //textCEDULA.setText("");
                //textNOMBRE.setText("");
                //textCUENTA.setText("");
                textCODIGO.setText("");
                textPRODUCTO.setText("");
                textAGREGAR_A_CUENTA.setText("");
                textPRECIO.setText("");
                textCANTIDAD.setText("");
                textCANTIDAD_A_COMPRAR.setText("");

                VALOR_A_PAGAR = String.valueOf(0.0); // REALIZA TODOS LOS CÁLCULOS PARA EL VALOR TOTAL DE LA COMPRA
                total_factura.setText(VALOR_A_PAGAR); // RECIBE EL VALOR TOTAL DE LA COMPRA
            }
        });
    }


    public void agregar(){// INICIO ACCIÓN AGREGAR PRODUCTOS A LA TABLA

        int filaProductoExistente = -1;
        // SE RECORRE LA TABLA
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {
            String codigoEnTabla = (String) modelo.getValueAt(fila, 0);
            // SI EL PRODUCTO EXISTE EN LA TABLA
            if (textCODIGO.getText().equals(codigoEnTabla)) {
                filaProductoExistente = fila; // SE OBTIENE EL NÚMERO DE FILA DE LA TABLA
                break; // CUANDO SE ENCUENTRA EL PRODUCTO, SE SALE AUTOMÁTICAMENTE
            }
        }
        // SI EXISTE EL PRODUCTO EN LA TABLA
        if (filaProductoExistente != -1) {
            // ACTUALIZAR LA TABLA: CANTIDAD Y PRECIO
            int cantidadActual = Integer.parseInt((String) modelo.getValueAt(filaProductoExistente, 2));
            double precioActual = Double.parseDouble((String) modelo.getValueAt(filaProductoExistente, 3));
            int nuevaCantidad = cantidadActual + Integer.parseInt(textCANTIDAD_A_COMPRAR.getText());
            double nuevoPrecio = precioActual + Double.parseDouble(precio_total_producto.getText());
            modelo.setValueAt(Integer.toString(nuevaCantidad), filaProductoExistente, 2);
            modelo.setValueAt(Double.toString(nuevoPrecio), filaProductoExistente, 3);
        } else { // EL PRODUCTO NO EXISTE EN LA TABLA
            // SE AGREGA EL PRODUCTO A LA TABLA
            modelo.addRow(new Object[]{textCODIGO.getText(),textPRODUCTO.getText(),textCANTIDAD_A_COMPRAR.getText(),precio_total_producto.getText()});
        }
        // SE VA CALCULANDO EL VALOR TOTAL PARA LA FACTURA
        VALOR_A_PAGAR = String.valueOf(Double.parseDouble(VALOR_A_PAGAR) + Double.parseDouble(precio_total_producto.getText()));
        //
        modelo.fireTableDataChanged();
        table.repaint();
    }// FIN ACCIÓN AGREGAR PRODUCTOS A LA TABLA

    public static Connection getConection() throws RuntimeException { // CONEXIÓN CON LA BDD
        Connection conexion;
        try { // SE INGRESA DATOS DE LA BDD
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
