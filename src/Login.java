// PERMITE CREAR INTERFACES DE USUARIO
import javax.swing.*;
// PERMITE TRABAJR CON JDBC, PARA LA CONEXIÓN CON LA BASE DE DATOS
import java.sql.*;
// PERMITE MANEJAR LAS ACCIONES DE BOTONES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login{
    Statement s;
    ResultSet rs;
    PreparedStatement ps;
    int res;
    private JFormattedTextField textUSUARIO;
    private JPasswordField textCONTRA;
    public JPanel PanelLogin;
    private JButton INGRESARButton;
    private JLabel Imagen;
    private JComboBox comboBox;
    private JLabel logo;
    JFormattedTextField contrasenia = new JFormattedTextField();//Recibe contraseña de cajero o administrado
    //JFormattedTextField ci_venta = new JFormattedTextField();//Recibe el C.I del usuario que realiza la venta
    //JFormattedTextField nombres_venta = new JFormattedTextField();//Recibe el nombre del cajero
    JFormattedTextField transaccion = new JFormattedTextField();// Proporciona un número de factura (transacción)
    Boolean correcto; // Verifica si es correcto el ingreso

    public Login(){

        Imagen.setIcon( new ImageIcon("img/perfil.png"));
        logo.setIcon( new ImageIcon("img/pets.png"));
        // Se carga los combo box
        comboBox.removeAllItems();
        comboBox.addItem(" ");
        comboBox.addItem("ADMINISTRADOR");
        comboBox.addItem("VENTAS");

        INGRESARButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                correcto = false;
                if("ADMINISTRADOR" == comboBox.getSelectedItem()){
                    try{
                        Connection conexion;
                        conexion = getConection();

                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT * FROM login_admin");

                        contrasenia.setText(new String(textCONTRA.getPassword()));

                        while (rs.next()) {
                            //Se verifica si es correcto el usuario y contraseña

                            if(textUSUARIO.getText().equals(rs.getString(2)) && contrasenia.getText().equals(rs.getString(3))){
                                //System.exit( 0 );
                                Admin admin= new Admin();
                                admin.setName("MENU-ADMINISTRADOR");
                                admin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                admin.pack();
                                //admin.setBounds(0,0,1000, 800);
                                admin.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                admin.setLocationRelativeTo(null);
                                admin.setVisible(true);
                                correcto = true;

                            }
                        }
                        if(!correcto){
                            JOptionPane.showMessageDialog(null, "El usuario o contraseña son incorrectos");
                        }
                        conexion.close();
                        rs.close();
                        s.close();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if("VENTAS" == comboBox.getSelectedItem()){//Si en el combo box se selecciona administrador
                    try{
                        Connection conexion;
                        conexion = getConection();

                        s = conexion.createStatement();
                        rs = s.executeQuery("SELECT * FROM login_venta");

                        contrasenia.setText(new String(textCONTRA.getPassword()));

                        while (rs.next()) {
                            //Verifica si es correcto el usuario y contraseña del cajero
                            if(textUSUARIO.getText().equals(rs.getString(2)) && contrasenia.getText().equals(rs.getString(3))){
                                JFrame frame=new JFrame("VENTA_PRODUCTO");
                                frame.setContentPane(new venta_producto().panel);
                                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                frame.pack();
                                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                frame.setLocationRelativeTo(null);
                                frame.setVisible(true);
                                correcto = true;
                            }
                        }
                        if(!correcto){
                            JOptionPane.showMessageDialog(null, "El usuario o contraseña son incorrectos");
                        }
                        conexion.close();
                        rs.close();
                        s.close();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                    /*if(correcto){// Si se ingresó correctamente
                        try{ //Se abre usuario_venta
                            Connection conexion;
                            conexion = getConection();

                            s = conexion.createStatement();
                            rs = s.executeQuery("SELECT nombres_user FROM usuario_venta WHERE ci_user =" + ci_venta.getText());

                            //Se obtiene el nombrey apellido con el id del cajero
                            while (rs.next()) {
                                nombres_venta.setText(rs.getString(1));
                            }
                            conexion.close();
                            rs.close();
                            s.close();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }*/

                        /*try { //Se abre la cabecera de transacción

                            Connection conexion;
                            conexion = getConection();

                            s = conexion.createStatement();
                            rs = s.executeQuery("SELECT num_f FROM cab_trans ORDER by num_f DESC LIMIT 1");
                            //Se obtiene el número de la última factura
                            while (rs.next()) {
                                transaccion.setText(rs.getString(1));
                                //encontrado = true;
                            }
                            String t = String.valueOf((Integer.parseUnsignedInt(transaccion.getText()) + 1));
                            transaccion.setText(t);

                            // Se ingresa el número de transacicón y el id del cajero
                            ps = conexion.prepareStatement("Insert into cab_trans (num_f) values (?)");
                            ps.setString(1, transaccion.getText());
                            ps.setString(2, ci_venta.getText());
                            //System.out.println("cab_trans: " + ps);

                            res = ps.executeUpdate();
                            if(!(res >0)){
                                JOptionPane.showMessageDialog(null,"NO GUARDADO");
                            }

                            conexion.close();
                            rs.close();
                            s.close();
                            ps.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }*/

                    //}
                }

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
