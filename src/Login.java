// PERMITE CREAR INTERFACES DE USUARIO
import javax.swing.*;
// PERMITE TRABAJR CON JDBC, PARA LA CONEXIÓN CON LA BASE DE DATOS
import java.sql.*;
// PERMITE MANEJAR LAS ACCIONES DE BOTONES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// PERMITE VISUALIZAR LAS IMÁGENES EN EL EJECUTABLE
import java.io.IOException;
import java.net.URL;
import java.awt.BorderLayout;

public class Login{
    public JPanel PanelLogin; // CONTENEDOR DE ELEMENTOS
    Statement s; // ENVIA CONSULTAS SQL A LA BDD
    ResultSet rs; // ALMACENA LOS RESULTADOS DE LA CONSULTA
    private JFormattedTextField textUSUARIO; // RECIBE USUARIO DEL USUARIO
    private JPasswordField textCONTRA; // RECIBE LA CONTRASEÑA DEL USUARIO
    private JButton INGRESARButton; // BOTÓN INGRESAR AL SISTEMA
    private JLabel Imagen; // ESPACIO DE IMAGEN DE PERRITOS
    private JComboBox comboBox; // COMBO BOX DE ELECCIÓN: ADMINISTRAR; VENTAS
    private JLabel logo; // ESPACIO DE IMAGEN DE USUARIO GENERAL
    JFormattedTextField contrasenia = new JFormattedTextField();// RECIBE LA CONTRASEÑA DE USUARIO
    Boolean correcto; // VERIFICA SI ES CORRECTO EL INGRESO

    public Login(){
        // SE AGREGA LAS IMÁGENES CORRESPONDIENTES
        String perfilImageUrl = "https://res.cloudinary.com/dh7xuwoyg/image/upload/v1693585109/Florcan_Tienda/pets_rbvhok.png";
        //JLabel perfilImageLabel = new JLabel();
        try {
            URL url = new URL(perfilImageUrl);
            logo.setIcon(new ImageIcon(url));
        } catch (IOException e) {
            // Manejar errores en la carga de la imagen
            e.printStackTrace();
            System.err.println("La imagen de perfil no se pudo cargar desde la URL de Cloudinary.");
        }

        // Cargar el logo desde Cloudinary
        String logoImageUrl = "https://res.cloudinary.com/dh7xuwoyg/image/upload/v1693585099/Florcan_Tienda/perfil_uydrxo.png";
        //JLabel logoImageLabel = new JLabel();
        try {
            URL url = new URL(logoImageUrl);
            Imagen.setIcon(new ImageIcon(url));
        } catch (IOException e) {
            // Manejar errores en la carga de la imagen
            e.printStackTrace();
            System.err.println("El logo no se pudo cargar desde la URL de Cloudinary.");
        }

        // SE CARGA EL COMBO BOX
        comboBox.removeAllItems();
        comboBox.addItem(" ");
        comboBox.addItem("ADMINISTRADOR");
        comboBox.addItem("VENTAS");
        comboBox.addItem("SUPER_ADMIN");
        INGRESARButton.addActionListener(new ActionListener(){ // INICIO ACCIÓN INGRESAR AL SISTEMA
            @Override
            public void actionPerformed(ActionEvent e){
                correcto = false; // SE INICIALIZA COMO INGRESO INCORRECTO
                // SI EN EL COMBO BOX SE SELECCIONA LA IPCIÓN DE "ADMINISTRADOR"
                if("ADMINISTRADOR" == comboBox.getSelectedItem()){
                    try{ // INICIO BDD CARGAR DATOS DEL LOGIN DE ADMINISTRADOR
                        //CONEXIÓN BDD
                        Connection conexion;
                        conexion = getConection();
                        // CONSULTA: CARGAR DATOS DE LOGIN
                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT * FROM login_admin");
                        // SE OBTIENE LA CONTRASEÑA INGRESADO POR EL USUARIO
                        contrasenia.setText(new String(textCONTRA.getPassword()));

                        // SE OBTIENE LOS DATOS DE BDD DE ACUERDO A LA CONSULTA
                        while (rs.next()) {
                            // VERIFICA SI EL USUARIO Y CONTRASEÑA SON CORRECTOS
                            if(textUSUARIO.getText().equals(rs.getString(2)) && contrasenia.getText().equals(rs.getString(3))){
                                // SE ABRE LA VENTANA DE ADMINISTRACIÓN
                                Admin admin= new Admin();
                                admin.setName("MENU-ADMINISTRADOR");
                                admin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                admin.pack();
                                admin.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                admin.setLocationRelativeTo(null);
                                admin.setVisible(true);
                                correcto = true; // INGRESO CORRECTO
                            }
                        }
                        if(!correcto){ // SI EL INGRESO NO ES CORRECTO
                            JOptionPane.showMessageDialog(null, "El usuario o contraseña son incorrectos");
                        }
                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        rs.close();
                        s.close();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    } // FIN BDD CARGAR DATOS DEL LOGIN DE ADMINISTRADOR
                }
                // SI EN EL COMBO BOX SE SELECCIONA "VENTAS"
                if("VENTAS" == comboBox.getSelectedItem()){
                    try{ // INICIO BDD CARGAR DATOS DE LOGIN DE VENTA
                        Connection conexion;
                        conexion = getConection();

                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT * FROM login_venta");

                        contrasenia.setText(new String(textCONTRA.getPassword()));

                        while (rs.next()) {
                            //Verifica si es correcto el usuario y contraseña del cajero
                            if(textUSUARIO.getText().equals(rs.getString(2)) && contrasenia.getText().equals(rs.getString(3))){
                                // SE ABRE LA VENTADA DE VENTAS CORRESPONDIENTE
                                JFrame frame=new JFrame("VENTA_PRODUCTO");
                                frame.setContentPane(new venta_producto().panel);
                                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                frame.pack();
                                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                frame.setLocationRelativeTo(null);
                                frame.setVisible(true);
                                correcto = true; // SE INGRESÓ CORRECTAMENTE
                            }
                        }
                        if(!correcto){ // ACCESO INCORRECTO
                            JOptionPane.showMessageDialog(null, "El usuario o contraseña son incorrectos");
                        }
                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        rs.close();
                        s.close();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    } // FIN BDD CARGAR DATOS DE LOGIN DE VENTA
                }
                // SI EN EL COMBO BOX SE SELECCIONA "VENTAS"
                if("SUPER_ADMIN" == comboBox.getSelectedItem()){
                    try{ // INICIO BDD CARGAR DATOS DE LOGIN DE VENTA
                        Connection conexion;
                        conexion = getConection();

                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT * FROM login_sa");

                        contrasenia.setText(new String(textCONTRA.getPassword()));

                        while (rs.next()) {
                            //Verifica si es correcto el usuario y contraseña del cajero
                            if(textUSUARIO.getText().equals(rs.getString(2)) && contrasenia.getText().equals(rs.getString(3))){
                                // SE ABRE LA VENTANA DE ADMINISTRACIÓN
                                Super_Admin admin= new Super_Admin();
                                admin.setName("MENU - SUPER ADMINISTRADOR");
                                admin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                admin.pack();
                                admin.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                admin.setLocationRelativeTo(null);
                                admin.setVisible(true);
                                correcto = true; // INGRESO CORRECTO
                            }
                        }
                        if(!correcto){ // ACCESO INCORRECTO
                            JOptionPane.showMessageDialog(null, "El usuario o contraseña son incorrectos");
                        }
                        // CIERRE DE CONEXIÓN
                        conexion.close();
                        rs.close();
                        s.close();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    } // FIN BDD CARGAR DATOS DE LOGIN DE VENTA
                }
            }
        }); // FIN ACCIÓN INGRESAR AL SISTEMA
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
