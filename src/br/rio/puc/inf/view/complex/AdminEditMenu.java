package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PublicKey;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sun.misc.BASE64Encoder;
import br.rio.puc.inf.control.db.AccessJDBC;
import br.rio.puc.inf.control.instruments.Cryptography;
import br.rio.puc.inf.control.instruments.Log;
import br.rio.puc.inf.control.instruments.PasswordTest;
import br.rio.puc.inf.model.User;

public class AdminEditMenu extends JPanel {
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField tfPublicKey;
	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private User currentUser = null;
	
	private int changePubKey = 0;
	private int changePasswd = 0;
	private JLabel lbChanges;

	/**
	 * Create the panel.
	 */
	public AdminEditMenu(final JPanel parentPanel,final CardLayout cl, User user) {
		setLayout(null);
		
		currentUser = user;
		
		JLabel lblTotalDeAlteraes = new JLabel("Total de altera\u00E7\u00F5es feitas pelo usu\u00E1rio:");
		lblTotalDeAlteraes.setBounds(231, 11, 187, 14);
		add(lblTotalDeAlteraes);
		
		lbChanges = new JLabel("0 altera\u00E7\u00F5es");
		lbChanges.setText(Integer.toString(AccessJDBC.getNumChanges(currentUser.getUsername())) + " altera\u00E7\u00F5es"); 
		lbChanges.setBounds(428, 11, 110, 14);
		add(lbChanges);
		
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
		
		tfPublicKey = new JTextField();
		tfPublicKey.setBounds(399, 174, 247, 20);
		add(tfPublicKey);
		tfPublicKey.setColumns(10);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					
					Log.registerMessage(7003, currentUser.getUsername()); // LOG: Botão voltar de alterar para o menu principal pressionado por <login_name>
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
		btnVoltar.setBounds(665, 305, 89, 23);
		add(btnVoltar);
		
		JButton btnAlterar = new JButton("Alterar");
		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
								
				//Recuperar informações preenchidas
				String passwd = new String (passwordField.getPassword());
				
				String passwdCheck = new String (passwordField_1.getPassword());
				
				String publicKey = tfPublicKey.getText();
				
				String error = new String();
				
				// Compor mensagens de erro
				
				if(!passwd.equals("") || !passwdCheck.equals("")) {
					
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
					catch (Exception e) {
						error = error + "Formato inválido. Senha só pode ser formada por números.\n";
					}
					

					
	
				}
				
				String encodedBytes = null;
				try {
					if(!publicKey.equals("")) {
						PublicKey pubKey = Cryptography.getPublicKeyFile(publicKey);
						byte[] keyBytes = pubKey.getEncoded();
						encodedBytes = new BASE64Encoder().encode(keyBytes);
					}
					
				} catch (Exception e1) {
					error = error + "Chave publica não encontrada.\n";
				}
				
				if (error.isEmpty()) {
					// Recadastro com sucesso, update no banco!
					
					Log.registerMessage(7002, currentUser.getUsername()); // LOG: Botão alterar pressionado por <login_name>
					lbChanges.setText(Integer.toString(AccessJDBC.getNumChanges(currentUser.getUsername())) + " altera\u00E7\u00F5es"); 
					
					if(!passwd.equals(""))
					{
						currentUser.setPassword(passwd);
						AccessJDBC.updateDbPasswd(currentUser);
						AccessJDBC.updateNumLogedPasswd(currentUser.getUsername(), 0);
					}
					
					if(!publicKey.equals(""))
					{
						currentUser.setPublicKey(encodedBytes);
						AccessJDBC.updatePublicKey(currentUser);
					}
						
					// Limpar a tela
					clearView();
										
					// Retornar a tela principal
					if (currentUser.getGroupID() == 0) {
						//User is adm group
						cl.show(parentPanel, ADMINMENU);
					} else if (currentUser.getGroupID() == 1) {
						//User is user group
						cl.show(parentPanel, USRMENU);						
					}
					Log.registerMessage(5001, currentUser.getUsername()); // LOG: Tela principal apresentada para <login_name>
					
				} else {
					JOptionPane.showMessageDialog(null,
							"Um ou mais problemas foram encontrados, cadastro não realizado!\n" + error);
				}
				
				
				
			}
		});
		btnAlterar.setBounds(557, 305, 89, 23);
		add(btnAlterar);

	}
	
	private void clearView() {
		// Limpa a tela de cadastro após um sucesso
		
		tfPublicKey.setText("");
		passwordField.setText("");
		passwordField_1.setText("");
		
		
		
	}

}
