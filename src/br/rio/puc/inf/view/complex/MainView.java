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
		

	}

	public void initializeEvents(final JPanel panel,
			final JButton btnNewButton, final JButton btnNewButton_1,
			final JButton btnNewButton_2, final JButton btnNewButton_3,
			final JButton btnNewButton_4, final JButton btnNewButton_5,
			final JButton btnOk) {
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if (AccessJDBC.VerifyUser(textField.getText())) {
						panel.setVisible(true);
						randomizeButtonArray(btnNewButton, btnNewButton_1,
								btnNewButton_2, btnNewButton_3, btnNewButton_4);
						currentUser = textField.getText();
					} else {
						JOptionPane.showMessageDialog(null,
								"Usuário inexistente!");
						if (panel.isVisible())
							panel.setVisible(false);
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
				if (pass.length() < 8) {
					JOptionPane.showMessageDialog(null,
							"Senha muito curta, mínimo de 8 dígitos!");
					return;
				} else if (pass.length() > 10) {
					JOptionPane.showMessageDialog(null,
							"Senha muito longa, máximo de 10 dígitos!");
					return;
				}

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
				
				
				// Senha dentro dos limites, verificação de correção
				User validUser = AccessJDBC.getUser(currentUser);
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
							break;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!auth) {
					JOptionPane.showMessageDialog(null,
							"Senha incorreta. Sua tentativa foi logada.");
				} else {
					// Verificar a chave do usuário
					String privateKeyFile = JOptionPane.showInputDialog(null, "Caminho para a chave privada..:", 
							"Chave Privada", 1);
					
					String passPhrase = JOptionPane.showInputDialog(null, "Entre com sua passphrase para a chave privada..:", 
							"Passphrase", 1);
					
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
						
						if (Cryptography.verifyDigitalSigByteArray(signature, bArray, pubKey))
							JOptionPane.showMessageDialog(null,
									"Login realizado com sucesso!");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					// Usuário foi autenticado!
					
					userView = new BasicMenuView(validUser, frame);
					setContentPane(userView);
					revalidate();
					repaint();


					
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
