package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PublicKey;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sun.misc.BASE64Encoder;

import br.rio.puc.inf.control.db.AccessJDBC;
import br.rio.puc.inf.control.instruments.Cryptography;
import br.rio.puc.inf.control.instruments.Log;
import br.rio.puc.inf.control.instruments.PasswordTest;
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
	private JComboBox comboBox;
	private JLabel lblNewLabel;

	/**
	 * Create the panel.
	 */
	public AdminCreateView(final JPanel parentPanel,final CardLayout cl, User user) {
		setLayout(null);
		
		currentUser = user;
		
		JLabel lblTotalDeUsurios = new JLabel("Total de usu\u00E1rios do sistema:");
		lblTotalDeUsurios.setBounds(247, 26, 147, 14);
		add(lblTotalDeUsurios);
		
		int num = AccessJDBC.getNumRegisteredUsers();
		lblNewLabel = new JLabel(Integer.toString(num) + " usu\u00E1rios");
		lblNewLabel.setBounds(393, 26, 108, 14);
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
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0 - Adminstrador", "1 - Usu\u00E1rio"}));
		comboBox.setBounds(215, 152, 381, 20);
		add(comboBox);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Log.registerMessage(6002, currentUser.getUsername()); // LOG: Botão cadastrar pressionado por <login_name>
				
				//Recuperar informações preenchidas
				String username = textField_2.getText();
				
				Integer group = comboBox.getSelectedIndex();
				
				String fullName = textField.getText();
				
				String passwd = new String (passwordField.getPassword());
				
				String passwdCheck = new String (passwordField_1.getPassword());
				
				String publicKey = textField_1.getText();
				
				String error = new String();
				
				// Compor mensagens de erro
				
				if (username.isEmpty() || fullName.isEmpty() || passwd.isEmpty() || publicKey.isEmpty() )
					error = error + "Não são aceitos campos vazios.\n";
				
				// Verificar se já existe usuário
				if (AccessJDBC.VerifyUser(username)) {
					error = error + "Nome de usuário já existe.\n";
					
				}
				
				try {
					Integer.parseInt(passwd);
					
					if (!passwd.equals(passwdCheck)) {
						error = error + "Senha e confirmação de senha não coincidem.\n";
					}
					
					if (PasswordTest.findRepetition(passwd)) {
						error = error + "Repetição na senha encontrada, favor alterar.\n";
					}
					
					if (PasswordTest.findSequence(passwd)) {
						error = error + "Sequência na senha encontrada, favor alterar.\n";
					}
					
					if (!PasswordTest.testLenght(passwd)) {
						error = error + "Senha deve ter no mínimo 8 e no máximo 10 caracteres.\n";
					}
				}
				catch (Exception exp) {
					error = error + "Formato inválido. Senha só pode ser formada por números.\n";
				}
				
				PublicKey pubKey = null;
				try {
					pubKey = Cryptography.getPublicKeyFile(publicKey);
				} catch (Exception e1) {
					error = error + "Chave publica não encontrada.\n";
				}
				
				
				
				if (error.isEmpty()) {
					// Cadastro com sucesso, inserir no banco!
					byte[] keyBytes = pubKey.getEncoded();
					String encodedBytes = new BASE64Encoder().encode(keyBytes);
					
					User user = new User(username, fullName, group, passwd, encodedBytes );
					// Insert user in DB
					AccessJDBC.insertUser(user);
					
					int num = AccessJDBC.getNumRegisteredUsers();
					lblNewLabel.setText(Integer.toString(num) + " usu\u00E1rios");
					
					// Limpar a tela
					clearView();
										
					
				} else {
					JOptionPane.showMessageDialog(null,
							"Um ou mais problemas foram encontrados, cadastro não realizado!\n" + error);
				}
				
				
			}
		});
		btnCadastrar.setBounds(557, 305, 89, 23);
		add(btnCadastrar);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Log.registerMessage(6003, currentUser.getUsername()); // LOG: Botão voltar de cadastrar para o menu principal pressionado por <login_name>
				if (currentUser.getGroupID() == 0) {
					//User is adm group
					cl.show(parentPanel, ADMINMENU);
				} else if (currentUser.getGroupID() == 1) {
					//User is user group
					
					cl.show(parentPanel, USRMENU);
				}
				Log.registerMessage(5001, currentUser.getUsername()); // LOG: Tela principal apresentada para <login_name>
			}
		});
		btnVoltar.setBounds(656, 305, 89, 23);
		add(btnVoltar);

	}
	
	private void clearView() {
		// Limpa a tela de cadastro após um sucesso
		
		textField.setText("");
		comboBox.setSelectedIndex(0);
		textField_1.setText("");
		textField_2.setText("");
		passwordField.setText("");
		passwordField_1.setText("");
		
		
		
	}
}
