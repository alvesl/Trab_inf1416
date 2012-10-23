package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.rio.puc.inf.model.User;

public class AdminEditMenu extends JPanel {
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField textField;
	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private User currentUser = null;

	/**
	 * Create the panel.
	 */
	public AdminEditMenu(final JPanel parentPanel,final CardLayout cl, User user) {
		setLayout(null);
		
		currentUser = user;
		
		JLabel lblTotalDeAlteraes = new JLabel("Total de altera\u00E7\u00F5es feitas pelo usu\u00E1rio:");
		lblTotalDeAlteraes.setBounds(231, 11, 187, 14);
		add(lblTotalDeAlteraes);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(444, 11, 46, 14);
		add(lblNewLabel);
		
		JLabel lblFormulrioDeAlterao = new JLabel("Formul\u00E1rio de Altera\u00E7\u00E3o:");
		lblFormulrioDeAlterao.setBounds(303, 36, 118, 14);
		add(lblFormulrioDeAlterao);
		
		JLabel lblNewLabel_1 = new JLabel("Senha pessoal:");
		lblNewLabel_1.setBounds(191, 93, 73, 14);
		add(lblNewLabel_1);
		
		JLabel lblConfirmaoDaSenha = new JLabel("Confirma\u00E7\u00E3o da senha pessoal:");
		lblConfirmaoDaSenha.setBounds(191, 130, 150, 14);
		add(lblConfirmaoDaSenha);
		
		JLabel lblCaminhoDoArquivo = new JLabel("Caminho do arquivo com a chave p\u00FAblica:");
		lblCaminhoDoArquivo.setBounds(191, 177, 198, 14);
		add(lblCaminhoDoArquivo);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(268, 90, 378, 20);
		add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(348, 127, 298, 20);
		add(passwordField_1);
		
		textField = new JTextField();
		textField.setBounds(399, 174, 247, 20);
		add(textField);
		textField.setColumns(10);
		
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
		btnVoltar.setBounds(665, 305, 89, 23);
		add(btnVoltar);
		
		JButton btnAlterar = new JButton("Alterar");
		btnAlterar.setBounds(557, 305, 89, 23);
		add(btnAlterar);

	}

}
