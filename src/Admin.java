// PERMITE MANEJAR LAS ACCIONES DE BOTONES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// PERMITE CREAR INTERFACES DE USUARIO
import javax.swing.*;

public class Admin extends JFrame implements ActionListener{
    private JMenuBar barra; // BARRA SUPERIOR
    private JMenu menu1; // MENÚ DE OPCIONES
    private JMenuItem INVENTARIO, ACTUALIZAR_PRODUCTO; // OPCIONES: INVENTARIO Y ACTUALIZAR PRODUCTO
    public Admin() {
        barra = new JMenuBar(); // BARRA SUPERIOR
        setJMenuBar(barra);
        menu1 = new JMenu("MENU OPCIONES");
        barra.add(menu1); // SE AÑADE EL MENÚ A LA BARRA SUPERIOR

        INVENTARIO = new JMenuItem("INVENTARIO");
        menu1.add(INVENTARIO); // SE AÑADE "INVENTARIO" EN EL MENÚ
        INVENTARIO.addActionListener(this);// SE HABILITA LA OPCIÓN PARA UNA ACCIÓN

        ACTUALIZAR_PRODUCTO = new JMenuItem("ACTUALIZAR PRODUCTO");
        menu1.add(ACTUALIZAR_PRODUCTO); // SE AÑADE "ACTUALIZAR PRODUCTO" EN EL MENÚ
        ACTUALIZAR_PRODUCTO.addActionListener(this); // SE HABILITA LA OPCIÓN PARA UNA ACCIÓN
    }
    public void actionPerformed(ActionEvent e){ // ACCIONES
        if(e.getSource()==INVENTARIO){ // SI SE SELECCIONA "INVENTARIO" SE ABRE LA TABLA CORRESPONDIENTE
            JFrame frame=new JFrame("INVENTARIO");
            frame.setContentPane(new Admin_stock().Panel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            //frame.setBounds(0,0,1000, 800);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        if(e.getSource()==ACTUALIZAR_PRODUCTO){ // SI SE SELECCIONA "ACTUALIZAR PRODUCTO" SE ABRE LA TABLA CORRESPONDIENTE
            JFrame frame=new JFrame("ACTUALIZAR PRODUCTO");
            frame.setContentPane(new Admin_producto().Panel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            //frame.setBounds(0,0,1000, 800);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}