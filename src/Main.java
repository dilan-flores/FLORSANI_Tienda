// PERMITE MANEJAR INTERFACES DE USUARIO
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //ABRE LA VENTANA "LOGIN"
        JFrame frame=new JFrame("LOGIN");
        frame.setContentPane(new Login().PanelLogin);
        //frame.setContentPane(new venta_producto().panel);
        //frame.setContentPane(new Admin_stock().Panel);
        //frame.setContentPane(new facturacion().Panel);
        //frame.setContentPane(new Admin_producto().Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setBounds(0,0,900, 600);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}