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

	/**
	 * Create the panel.
	 */
	public AdminEditMenu(final JPanel parentPanel,final CardLayout cl, User user) {
		setLayout(null);
		
		currentUser = user;
		
		JLabel lblTotalDeAlteraes = new JLabel("Total de altera\u00E7\u00F5es feitas pelo usu\u00E1rio:");
		lblTotalDeAlteraes.setBounds(231, 11, 187, 14);
		add(lblTotalDeAlteraes);
		
		final JLabel lbChanges = new JLabel("0 altera\u00E7\u00F5es");
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
		passwordField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				warn();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				warn();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				warn();
			}
			
			public void warn() {
				if(!(new String(passwordField.getPassword()).equals("")) || !(new String(passwordField_1.getPassword()).equals("")))
					changePasswd = 1;
				else
					changePasswd = 0;
				lbChanges.setText(Integer.toString(changePasswd + changePubKey) + " altera\u00E7\u00F5es");
			}
		});
		passwordField.setBounds(268, 90, 378, 20);
		add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				warn();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				warn();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				warn();
			}
			
			public void warn() {
				if(!(new String(passwordField_1.getPassword()).equals("")) || !(new String(passwordField.getPassword()).equals("")))
					changePasswd = 1;
				else
					changePasswd = 0;
				lbChanges.setText(Integer.toString(changePasswd + changePubKey) + " altera\u00E7\u00F5es");
			}
		});
		passwordField_1.setBounds(348, 127, 298, 20);
		add(passwordField_1);
		
		tfPublicKey = new JTextField();
		tfPublicKey.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				warn();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				warn();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				warn();
			}
			
			public void warn() {
				if(!tfPublicKey.getText().equals(""))
					changePubKey = 1;
				else
					changePubKey = 0;
				lbChanges.setText(Integer.toString(changePasswd + changePubKey) + " altera\u00E7\u00F5es");
			}
		});
		tfPublicKey.setBounds(399, 174, 247, 20);
		add(tfPublicKey);
		tfPublicKey.setColumns(10);
		
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
		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Recuperar informações preenchidas
				
				boolean passwdChanged = false;
				String passwd = new String (passwordField.getPassword());
				
				String passwdCheck = new String (passwordField_1.getPassword());
				
				String publicKey = tfPublicKey.getText();
				
				String error = new String();
				
				// Compor mensagens de erro
				
				if(changePasswd == 1) {
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
				
				String encodedBytes = null;
				try {
					if(changePubKey == 1) {
						PublicKey pubKey = Cryptography.getPublicKeyFile(publicKey);
						byte[] keyBytes = pubKey.getEncoded();
						encodedBytes = new BASE64Encoder().encode(keyBytes);
					}
					
				} catch (Exception e1) {
					error = error + "Chave publica não encontrada.\n";
				}
				
				if (error.isEmpty()) {
					// Recadastro com sucesso, update no banco!
					
					if(changePasswd == 1)
					{
						currentUser.setPassword(passwd);
						AccessJDBC.updateDbPasswd(currentUser);
					}
					
					if(changePubKey == 1)
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
