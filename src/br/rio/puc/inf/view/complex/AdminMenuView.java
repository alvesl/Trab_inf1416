package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import br.rio.puc.inf.control.instruments.Log;
import br.rio.puc.inf.model.User;

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
	 * @param user TODO
	 * @param exit TODO
	 */
	public AdminMenuView(final JPanel parentPanel,final CardLayout cl, final User user,final ExitView exit) {
		setLayout(null);
		
		JButton btnNewButton = new JButton("Cadastrar novo usu\u00E1rio");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Log.registerMessage(5002, user.getUsername()); // LOG: Opção 1 do menu principal selecionada por <login_name>.
				cl.show(parentPanel, CREATE);				
				Log.registerMessage(6001, user.getUsername()); // LOG: Tela de cadastro apresentada para <login_name>
			}
		});
		btnNewButton.setBounds(304, 39, 181, 23);
		add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Alterar atributos de um usu\u00E1rio");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Log.registerMessage(5003, user.getUsername()); // LOG: Opção 2 do menu principal selecionada por <login_name>
				cl.show(parentPanel, EDIT);		
				Log.registerMessage(7001, user.getUsername()); // LOG: Tela de alteração apresentada para <login_name>
			}
		});
		btnNewButton_1.setBounds(304, 91, 181, 23);
		add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Consultar pasta de arquivos secretos de um usu\u00E1rio");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Log.registerMessage(5004, user.getUsername()); // LOG: Opção 3 do menu principal selecionada por <login_name>
				cl.show(parentPanel, LIST);
				Log.registerMessage(8001, user.getUsername()); // LOG: Tela de consulta apresentada para <login_name>.
			}
		});
		btnNewButton_2.setBounds(304, 140, 181, 23);
		add(btnNewButton_2);
		
		JButton btnSairDoSistema = new JButton("Sair do Sistema");
		btnSairDoSistema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Log.registerMessage(5005, user.getUsername()); // LOG: Opção 4 do menu principal selecionada por <login_name>.
				cl.show(parentPanel, EXIT);
				exit.setMessage();
				Log.registerMessage(9001, user.getUsername()); // LOG: Tela de saída apresentada para <login_name>
			}
		});
		btnSairDoSistema.setBounds(304, 199, 181, 23);
		add(btnSairDoSistema);

	}
}
