package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.rio.puc.inf.model.User;

public class AdminListFiles extends JPanel {
	private JTable table;
	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private User currentUser = null;
	
	
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
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Arquivo", "Hex Assinatura Digital", "Hex Envelope Digital"
			}
		));
		table.setBounds(34, 54, 714, 228);
		add(table);
		
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

	}
}
