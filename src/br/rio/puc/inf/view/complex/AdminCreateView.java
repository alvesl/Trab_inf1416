package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.rio.puc.inf.model.User;

public class AdminCreateView extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private User currentUser = null;

	/**
	 * Create the panel.
	 */
	public AdminCreateView(final JPanel parentPanel,final CardLayout cl, User user) {
		setLayout(null);
		
		currentUser = user;
		
		JLabel lblTotalDeUsurios = new JLabel("Total de usu\u00E1rios do sistema:");
		lblTotalDeUsurios.setBounds(247, 26, 147, 14);
		add(lblTotalDeUsurios);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(429, 26, 46, 14);
		add(lblNewLabel);
		
		JLabel lblNomeDeUsurio = new JLabel("Nome de usu\u00E1rio:");
		lblNomeDeUsurio.setBounds(167, 75, 91, 14);
		add(lblNomeDeUsurio);
		
		JLabel lblLoginName = new JLabel("Login name:");
		lblLoginName.setBounds(167, 117, 65, 14);
		add(lblLoginName);
		
		JLabel lblGrupo = new JLabel("Grupo:");
		lblGrupo.setBounds(167, 155, 38, 14);
		add(lblGrupo);
		
		JLabel lblSenhaPessoal = new JLabel("Senha pessoal:");
		lblSenhaPessoal.setBounds(167, 188, 80, 14);
		add(lblSenhaPessoal);
		
		JLabel lblConfirmaoDaSenha = new JLabel("Confirma\u00E7\u00E3o da senha pessoal:");
		lblConfirmaoDaSenha.setBounds(167, 223, 157, 14);
		add(lblConfirmaoDaSenha);
		
		JLabel lblCaminhoDoArquivo = new JLabel("Caminho do arquivo com a chave p\u00FAblica:");
		lblCaminhoDoArquivo.setBounds(167, 261, 203, 14);
		add(lblCaminhoDoArquivo);
		
		textField = new JTextField();
		textField.setBounds(257, 72, 339, 20);
		add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(371, 258, 225, 20);
		add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(232, 114, 364, 20);
		add(textField_2);
		textField_2.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(247, 185, 349, 20);
		add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(324, 220, 272, 20);
		add(passwordField_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0 - Adminstrador", "1 - Usu\u00E1rio"}));
		comboBox.setBounds(215, 152, 381, 20);
		add(comboBox);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.setBounds(557, 305, 89, 23);
		add(btnCadastrar);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (currentUser.getGroupID() == 0) {
					//User is adm group
					
					cl.show(parentPanel, ADMINMENU);
					
				} else if (currentUser.getGroupID() == 1) {
					//User is user group
					
					cl.show(parentPanel, USRMENU);

					
				}
						
			}
		});
		btnVoltar.setBounds(656, 305, 89, 23);
		add(btnVoltar);

	}
}
