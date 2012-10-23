package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import br.rio.puc.inf.model.User;

public class AdminListFiles extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4839159265731929234L;
	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private User currentUser = null;
	private static ArrayList<File> encFiles = new ArrayList<File>();
	private JTable table;
	
	
	/**
	 * Create the panel.
	 */
	public AdminListFiles(final JPanel parentPanel,final CardLayout cl, User user) {
		setLayout(null);
		
		currentUser = user;
		
		JLabel lblTotalDeConsultas = new JLabel("Total de consultas do usu\u00E1rio:");
		lblTotalDeConsultas.setBounds(282, 11, 150, 14);
		add(lblTotalDeConsultas);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(435, 11, 46, 14);
		add(lblNewLabel);
		
		
	
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentUser.getGroupID() == 0) {
					//User is adm group
					
					cl.show(parentPanel, ADMINMENU);
					
				} else if (currentUser.getGroupID() == 1) {
					//User is user group
					
					cl.show(parentPanel, USRMENU);

					
				}
			}
		});
		btnVoltar.setBounds(652, 305, 89, 23);
		add(btnVoltar);
		
		
		DefaultTableModel model = new DefaultTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5266993133307624457L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		readFiles(model);
		
		
		table = new JTable(model);
		table.setBounds(10, 53, 765, 227);
		add(table);
		


	}
	
	
	private static void readFiles(DefaultTableModel modelTable) {
		
		 File folder = new File(".");
		    File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  if (listOfFiles[i].getName().endsWith(".enc")) {
		    		  encFiles.add(listOfFiles[i]);
		    		  modelTable.addRow(new Object[] {listOfFiles[i].getName(), null, null});
		    	  }
		      } 
		    }
		
		
	}
}
