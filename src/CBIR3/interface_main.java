package CBIR3;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



public class interface_main {

    public static void main(String args[]){
    	JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			//UIManager.setLookAndFeel(new SubstanceOfficeBlue2007LookAndFeel());
			//UIManager.setLookAndFeel(new SubstanceAutumnLookAndFeel());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			String lookAndFeel =UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
        MainFrame var = new MainFrame("CBIR");
        var.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        var.setSize(1000, 700);
        var.setVisible(true);
        
    }

}
