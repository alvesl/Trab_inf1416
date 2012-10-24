package br.rio.puc.inf.view.complex;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import sun.misc.BASE64Decoder;
import br.rio.puc.inf.control.db.AccessJDBC;
import br.rio.puc.inf.control.instruments.Cryptography;
import br.rio.puc.inf.control.instruments.Digest;
import br.rio.puc.inf.control.instruments.Log;
import br.rio.puc.inf.model.User;

public class MainView extends JFrame {

	private static final long serialVersionUID = -5827215159300028894L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private String currentUser;
	private static String currentPass;
	private BasicMenuView userView;
	private JFrame frame = this;
	User validUser;
	
	private int loginAttempts = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		// Instanciar conexão com DB
		AccessJDBC.init();
		
		currentPass = new String();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		
		Log.registerMessage(1001, null); // LOG: Sistema iniciado
		
		setTitle("INF1416 - Grupo 3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 785, 538);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();

		textField.setBounds(312, 210, 145, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(267, 213, 35, 14);
		contentPane.add(lblLogin);

		JLabel lblBemVindoAo = new JLabel(
				"Bem vindo ao Trabalho 1 da disciplina INF1416. Digite seu Login para come\u00E7ar.");
		lblBemVindoAo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblBemVindoAo.setBounds(119, 99, 593, 27);
		contentPane.add(lblBemVindoAo);
		
		
		JPanel panel = new JPanel();
		panel.setBounds(267, 238, 190, 152);
		panel.setVisible(false);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(0, 0, 34, 14);
		panel.add(lblSenha);

		passwordField = new JPasswordField();
		passwordField.setEditable(false);
		passwordField.setBounds(44, 0, 146, 20);
		panel.add(passwordField);

		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(0, 49, 50, 23);
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(69, 49, 50, 23);
		panel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("New button");
		btnNewButton_2.setBounds(0, 83, 50, 23);
		panel.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("New button");
		btnNewButton_3.setBounds(69, 83, 50, 23);
		panel.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("New button");
		btnNewButton_4.setBounds(140, 49, 50, 23);
		panel.add(btnNewButton_4);

		JButton btnNewButton_5 = new JButton("<-");
		btnNewButton_5.setBounds(140, 83, 50, 23);
		panel.add(btnNewButton_5);

		JButton btnOk = new JButton("OK");

		btnOk.setBounds(44, 117, 98, 23);
		panel.add(btnOk);

		// Inicializa todos os eventos
		initializeEvents(panel, btnNewButton, btnNewButton_1, btnNewButton_2,
				btnNewButton_3, btnNewButton_4, btnNewButton_5, btnOk);
		Log.registerMessage(2001, null); // LOG: Autenticação Etapa 1 iniciada
		

	}

	public void initializeEvents(final JPanel panel,
			final JButton btnNewButton, final JButton btnNewButton_1,
			final JButton btnNewButton_2, final JButton btnNewButton_3,
			final JButton btnNewButton_4, final JButton btnNewButton_5,
			final JButton btnOk) {
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				String username = textField.getText();
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if (AccessJDBC.VerifyUser(username)) {
						validUser = AccessJDBC.getUser(username);
						currentUser = validUser.getUsername();
						
						Date currentDatetime = new Date(System.currentTimeMillis());
						java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDatetime.getTime()); 
						
						Timestamp lastBlock = AccessJDBC.getLastBlockByUser(username);
						
						
						if ((lastBlock == null) || (timestamp.getTime() - lastBlock.getTime() > 120000)) {
							loginAttempts = 0;
							Log.registerMessage(2003, username); // LOG: Login name <login_name> identificado com acesso liberado.
							Log.registerMessage(2002, null); // LOG: Autenticação etapa 1 encerrada
							panel.setVisible(true);
							randomizeButtonArray(btnNewButton, btnNewButton_1,
									btnNewButton_2, btnNewButton_3, btnNewButton_4);
							Log.registerMessage(3001, username); // LOG: Autenticação etapa 2 iniciada para <login_name>.
						}
						else {
							JOptionPane.showMessageDialog(null,
									"Usuário bloqueado!");
							if (panel.isVisible())
								panel.setVisible(false);
							Log.registerMessage(2004, username); // LOG: Login name <login_name> identificado com acesso bloqueado.
						}
							
					} 
					else {
						JOptionPane.showMessageDialog(null,
								"Usuário inexistente!");
						if (panel.isVisible())
							panel.setVisible(false);
						Log.registerMessage(2005, username); // LOG: Login name <login_name> não identificado.
					}

				}
			}
		});

		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				currentPass = currentPass + btnNewButton.getText();
				randomizeButtonArray(btnNewButton, btnNewButton_1,
						btnNewButton_2, btnNewButton_3, btnNewButton_4);
				passwordField.setText(new String(passwordField.getPassword())
						+ " ");		
				
			}
			
		});

		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				currentPass = currentPass + btnNewButton_1.getText();
				randomizeButtonArray(btnNewButton, btnNewButton_1,
						btnNewButton_2, btnNewButton_3, btnNewButton_4);
				passwordField.setText(new String(passwordField.getPassword())
						+ " ");
			}
		});

		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				currentPass = currentPass + btnNewButton_2.getText();
				randomizeButtonArray(btnNewButton, btnNewButton_1,
						btnNewButton_2, btnNewButton_3, btnNewButton_4);
				passwordField.setText(new String(passwordField.getPassword())
						+ " ");
			}
		});

		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				currentPass = currentPass + btnNewButton_3.getText();
				randomizeButtonArray(btnNewButton, btnNewButton_1,
						btnNewButton_2, btnNewButton_3, btnNewButton_4);
				passwordField.setText(new String(passwordField.getPassword())
						+ " ");
			}
		});

		btnNewButton_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				currentPass = currentPass + btnNewButton_4.getText();
				randomizeButtonArray(btnNewButton, btnNewButton_1,
						btnNewButton_2, btnNewButton_3, btnNewButton_4);
				passwordField.setText(new String(passwordField.getPassword())
						+ " ");
			}
		});

		btnNewButton_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String pass = new String(passwordField.getPassword());
				if (pass.length() > 1) {
					pass = pass.substring(1);
				} else {
					pass = "";
				}
				passwordField.setText(pass);
				
				if (currentPass.length() > 0) {
					currentPass = currentPass.substring(0, currentPass.length()-2);
				}
				
			}
		});

		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String pass = new String(passwordField.getPassword());
				
				// Gerar todas as possibilidades de senhas
				String[] passArray = new String[(int) Math.pow(2, currentPass.length() / 2)];
				
				for (int j=0; j< passArray.length; j++) {
					passArray[j] = new String();
				}
				
				for (int i = 0 ; i < currentPass.length()/2 ; i++) {
					int mustChange = (int) (passArray.length / ( Math.pow(2, i+1)  ));
					int count = 0;
					int toAdd = 0;
					
					for (int j=0; j< passArray.length; j++) {
						if (count == mustChange) {
							count = 0;
							if (toAdd == 1) {
								toAdd = 0;
							} else
								toAdd = 1;
						}
						
						passArray[j] = passArray[j] + currentPass.charAt(2*i+toAdd); 
								
						count++;
					}
					
					
					
				}
				
				
				// Verifica Senha
				User toAuthUsr = new User(currentUser, null, -1, null, null);
				toAuthUsr.setID(AccessJDBC.getUserID(currentUser));
				String passToAuth = null;
				boolean auth = false;
				try {
					for (int j=0; j< passArray.length; j++) {
						toAuthUsr.setPassword(passArray[j]);
						passToAuth = User.generateDbPassword(toAuthUsr, "MD5");
						if (Digest.compareDigest(passToAuth, validUser.getDbPassword())) {
							auth = true;
							Log.registerMessage(3003, currentUser); // LOG: Senha pessoal verificada positivamente para <login_name>
							Log.registerMessage(3002, currentUser); // LOG: Autenticação etapa 2 encerrada para <login_name>
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (!auth) {
					Log.registerMessage(3004, currentUser); // LOG: Senha pessoal verificada negativamente para <login_name>
					loginAttempts ++;
					if(loginAttempts == 1) {
						Log.registerMessage(3005, currentUser); // LOG: Primeiro erro da senha pessoal contabilizado para <login_name>
						JOptionPane.showMessageDialog(null,
								"Senha incorreta. Sua tentativa foi logada.");
					}
						
					if(loginAttempts == 2) {
						Log.registerMessage(3006, currentUser); // LOG: Segundo erro da senha pessoal contabilizado para <login_name>
						JOptionPane.showMessageDialog(null,
								"Senha incorreta. Sua tentativa foi logada.");
					}
					if(loginAttempts == 3) {
						JOptionPane.showMessageDialog(null,
								"Senha incorreta. Usuário bloqueado.");
						Log.registerMessage(3007, currentUser); // LOG: Terceiro erro da senha pessoal contabilizado para <login_name>
						Log.registerMessage(3008, currentUser); // LOG: Acesso do usuario <login_name> bloqueado pela autenticação etapa 2
						panel.setVisible(false);	
						passwordField.setText("");
					}
				} 
				else {
					loginAttempts = 0;
					Log.registerMessage(4001, currentUser); // LOG: Autenticação etapa 3 iniciada para <login_name>
					
					// Verificar a chave do usuário
					while(loginAttempts < 3) {
						
						String privateKeyFile = JOptionPane.showInputDialog(null, "Caminho para a chave privada..:", 
								"Chave Privada", 1);
						
						//String passPhrase = JOptionPane.showInputDialog(null, "Entre com sua passphrase para a chave privada..:", 
								//"Passphrase", 1);
						JPasswordField pField = new JPasswordField(10);
						String passPhrase = null;
						JPanel pPanel = new JPanel();
					    pPanel.add(new JLabel("Entre com sua passphrase para a chave privada..: "));
					    pPanel.add(pField);
					    int result = JOptionPane.showConfirmDialog(null, pPanel);
					    if (result == JOptionPane.OK_OPTION) {
					        passPhrase = new String(pField.getPassword());
					    }
						
						try {
							PrivateKey privKey = Cryptography.getPrivateKeyFile(privateKeyFile, passPhrase);
							validUser.setPkey(privKey);
							// Generates random array of bytes and sign it
							byte[] bArray = new byte[512];
							new Random().nextBytes(bArray);
							byte[] signature = Cryptography.signByteArraySymmetric(bArray, privKey);
							
							KeyFactory rsaKeyFac =  KeyFactory.getInstance("RSA");
							byte[] decodedBytes = new BASE64Decoder().decodeBuffer(validUser.getPublicKey()); //Recupera os array de bytes da string do DB
							X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedBytes);
							RSAPublicKey pubKey;
							   pubKey = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
							
							if (Cryptography.verifyDigitalSigByteArray(signature, bArray, pubKey)) {
								Log.registerMessage(4003, currentUser); // LOG: Chave privada verificada positivamente para <login_name>
								JOptionPane.showMessageDialog(null,
										"Login realizado com sucesso!");
								break;
							}
								
							
						} 
						catch (Exception e) {
							loginAttempts++;
							if(loginAttempts == 1) {
								Log.registerMessage(4004, currentUser); // LOG: Primeiro erro da chave privada contabilizado para <login_name>
								JOptionPane.showMessageDialog(null, "Chave privada incorreta. Sua tentativa foi logada");
							}
							if(loginAttempts == 2) {
								Log.registerMessage(4005, currentUser); // LOG: Segundo erro da chave privada contabilizado para <login_name>
								JOptionPane.showMessageDialog(null, "Chave privada incorreta. Sua tentativa foi logada");
							}
							if(loginAttempts == 3) {
								JOptionPane.showMessageDialog(null, "Chave privada incorreta. Usuário Bloqueado");
								Log.registerMessage(4006, currentUser); // LOG: Terceiro erro da chave privada contabilizado para <login_name>
								Log.registerMessage(4007, currentUser); // LOG: Acesso do usuario <login_name> bloqueado pela autenticação etapa 3
								panel.setVisible(false);
							}
						}
					}
					
					if (loginAttempts < 3){
						Log.registerMessage(4002, currentUser); // LOG: Autenticação etapa 3 encerrada para <login_name>						
						// Usuário foi autenticado!
						
						// Increment number of loged times using current password
						int num = AccessJDBC.getNumLogedPasswd(currentUser);
						AccessJDBC.updateNumLogedPasswd(currentUser, ++num);
						
						userView = new BasicMenuView(validUser, frame);
						setContentPane(userView);
						revalidate();
						repaint();
					}


					
				}

			}
		});

	}
	

	public void randomizeButtonArray(final JButton btnNewButton,
			final JButton btnNewButton_1, final JButton btnNewButton_2,
			final JButton btnNewButton_3, final JButton btnNewButton_4) {
		ArrayList<Integer> newArray = new ArrayList<Integer>();
		Random pr = new Random(); // Seed

		for (int i = 0; newArray.size() < 10; i++) {
			Integer random = pr.nextInt(10);
			if (!newArray.contains(random)) {
				newArray.add(random);
			}

		}

		btnNewButton.setText(newArray.get(1).toString()
				+ newArray.get(2).toString());
		btnNewButton_1.setText(newArray.get(3).toString()
				+ newArray.get(4).toString());
		btnNewButton_2.setText(newArray.get(5).toString()
				+ newArray.get(6).toString());
		btnNewButton_3.setText(newArray.get(7).toString()
				+ newArray.get(8).toString());
		btnNewButton_4.setText(newArray.get(9).toString()
				+ newArray.get(0).toString());

		return;
	}
}
