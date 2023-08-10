// PERMITE MANEJAR LAS ACCIONES DE BOTONES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// PERMITE CREAR INTERFACES DE USUARIO
import javax.swing.*;

public class Super_Admin extends JFrame implements ActionListener{
    private JMenuBar barra; // BARRA SUPERIOR
    private JMenu menu1; // MENÚ DE OPCIONES
    private JMenuItem INVENTARIO, ACTUALIZAR_PRODUCTO, CRUD_CLIENTE, CRUD_USUARIO; // OPCIONES: INVENTARIO Y ACTUALIZAR PRODUCTO
    public Super_Admin() {
        barra = new JMenuBar(); // BARRA SUPERIOR
        setJMenuBar(barra);
        menu1 = new JMenu("MENU OPCIONES");
        barra.add(menu1); // SE AÑADE EL MENÚ A LA BARRA SUPERIOR

        menu1.add("");
        INVENTARIO = new JMenuItem("INVENTARIO");
        menu1.add(INVENTARIO); // SE AÑADE "INVENTARIO" EN EL MENÚ
        INVENTARIO.addActionListener(this);// SE HABILITA LA OPCIÓN PARA UNA ACCIÓN
        menu1.add("");
        ACTUALIZAR_PRODUCTO = new JMenuItem("ACTUALIZAR PRODUCTO");
        menu1.add(ACTUALIZAR_PRODUCTO); // SE AÑADE "ACTUALIZAR PRODUCTO" EN EL MENÚ
        ACTUALIZAR_PRODUCTO.addActionListener(this); // SE HABILITA LA OPCIÓN PARA UNA ACCIÓN
        menu1.add("");
        CRUD_CLIENTE = new JMenuItem("CRUD CLIENTE");
        menu1.add(CRUD_CLIENTE); // SE AÑADE "CRUD CLIENTE" EN EL MENÚ
        CRUD_CLIENTE.addActionListener(this); // SE HABILITA LA OPCIÓN PARA UNA ACCIÓN
        menu1.add("");
        CRUD_USUARIO = new JMenuItem("CRUD USUARIOS");
        menu1.add(CRUD_USUARIO); // SE AÑADE "CRUD CLIENTE" EN EL MENÚ
        CRUD_USUARIO.addActionListener(this); // SE HABILITA LA OPCIÓN PARA UNA ACCIÓN
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
        if(e.getSource()==CRUD_CLIENTE){ // SI SE SELECCIONA "ACTUALIZAR PRODUCTO" SE ABRE LA TABLA CORRESPONDIENTE
            JFrame frame=new JFrame(" CRUD CLIENTE");
            frame.setContentPane(new Admin_cliente().Panel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        if(e.getSource()==CRUD_USUARIO){ // SI SE SELECCIONA "ACTUALIZAR PRODUCTO" SE ABRE LA TABLA CORRESPONDIENTE
            JFrame frame=new JFrame("CRUD USUARIOS");
            frame.setContentPane(new Admin_usuarios().Panel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}