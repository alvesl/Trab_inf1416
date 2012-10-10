package br.rio.puc.inf.view.complex;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import br.rio.puc.inf.control.db.AccessJDBC;

public class MainView extends JFrame {


	private static final long serialVersionUID = -5827215159300028894L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		//Instanciar conexão com DB
		AccessJDBC.init(); 
		
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
		
		JLabel lblBemVindoAo = new JLabel("Bem vindo ao Trabalho 1 da disciplina INF1416. Digite seu Login para come\u00E7ar.");
		lblBemVindoAo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblBemVindoAo.setBounds(119, 99, 593, 27);
		contentPane.add(lblBemVindoAo);
		
		JPanel panel = new JPanel();
		panel.setBounds(267, 238, 190, 43);
		panel.setVisible(false);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(0, 0, 34, 14);
		panel.add(lblSenha);
		
		textField_1 = new JTextField();
		textField_1.setBounds(45, 0, 145, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		// Inicializa todos os eventos
		initializeEvents(panel);
	}
	public void initializeEvents(final JPanel panel) {
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if (AccessJDBC.VerifyUser(textField.getText())) {
						panel.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "Usuário inexistente!");
						if (panel.isVisible())
							panel.setVisible(false);
					}
					
				}
			}
		});
		
	}
}
