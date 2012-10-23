package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AdminMenuView extends JPanel {

	/**
	 * 
	 */
	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private final String EXIT = "To exit the program";
	
	/**
	 * Create the panel.
	 */
	public AdminMenuView(final JPanel parentPanel,final CardLayout cl) {
		setLayout(null);
		
		JButton btnNewButton = new JButton("Cadastrar novo usu\u00E1rio");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cl.show(parentPanel, CREATE);
				
			}
		});
		btnNewButton.setBounds(304, 39, 181, 23);
		add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Alterar atributos de um usu\u00E1rio");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(parentPanel, EDIT);
				
			}
		});
		btnNewButton_1.setBounds(304, 91, 181, 23);
		add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Consultar pasta de arquivos secretos de um usu\u00E1rio");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(parentPanel, LIST);
			}
		});
		btnNewButton_2.setBounds(304, 140, 181, 23);
		add(btnNewButton_2);
		
		JButton btnSairDoSistema = new JButton("Sair do Sistema");
		btnSairDoSistema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(parentPanel, EXIT);
			}
		});
		btnSairDoSistema.setBounds(304, 199, 181, 23);
		add(btnSairDoSistema);

	}
}
