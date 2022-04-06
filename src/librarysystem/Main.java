package librarysystem;

import java.awt.EventQueue;
import javax.swing.JFrame;
import utils.Util;

public class Main {
	public static void main(String[] args) {
	      EventQueue.invokeLater(() -> 
	         {
	            LibrarySystem.INSTANCE.setTitle("Library Management");
	            LibrarySystem.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            LibrarySystem.INSTANCE.init();
	            Util.centerFrameOnDesktop(LibrarySystem.INSTANCE);
	            LibrarySystem.INSTANCE.setVisible(true);
	         });
	   }
}
