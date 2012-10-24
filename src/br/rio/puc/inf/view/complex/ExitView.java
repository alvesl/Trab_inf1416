package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.rio.puc.inf.control.db.AccessJDBC;
import br.rio.puc.inf.control.instruments.Log;
import br.rio.puc.inf.model.User;

public class ExitView extends JPanel {

	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private User currentUser = null;
	JButton btnSair;
	int numLogedPasswd = -1;
	JLabel lbMensagem;
	
	/**
	 * Create the panel.
	 * @param frame TODO
	 */
	public ExitView(final JFrame frame,final JPanel parentPanel, final CardLayout cl, User user) {
		setLayout(null);
		
		currentUser = user;
		
		JLabel lblTotalDeAcessos = new JLabel("Total de acessos do usu\u00E1rio:");
		lblTotalDeAcessos.setBounds(274, 11, 144, 14);
		add(lblTotalDeAcessos);
		
		JLabel lblNewLabel = new JLabel(Integer.toString(AccessJDBC.getNumLoged(currentUser.getUsername())));
		lblNewLabel.setBounds(420, 11, 46, 14);
		add(lblNewLabel);
		
		lbMensagem = new JLabel("");
		lbMensagem.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbMensagem.setBounds(100, 103, 583, 151);
		add(lbMensagem);
		
		
		
		btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Log.registerMessage(9902, currentUser.getUsername()); // LOG: Botão sair pressionado por <login_name>
				frame.dispose();
				Log.registerMessage(1002, null); // LOG: Sistema encerrado
			}
		});
		btnSair.setBounds(540, 305, 89, 23);
		add(btnSair);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Log.registerMessage(9003, currentUser.getUsername()); // LOG: Botão voltar de sair para o menu principal pressionado por <login_name>
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
		btnVoltar.setBounds(654, 305, 89, 23);
		add(btnVoltar);

	}
	
	// Set exit Message
	public void setMessage()
	{
		numLogedPasswd = AccessJDBC.getNumLogedPasswd(currentUser.getUsername());
		if(numLogedPasswd == 3)
		{
			lbMensagem.setText("Sua senha está vencida. \n" +
					"Para sair, é necessário ter uma senha pessoal válida para o próximo acesso. \n" +
					"Pressione o botão Voltar de Sair para o Menu Principal.");
			btnSair.setVisible(false);
		}
		else
		{
			lbMensagem.setText("Pressione o botão Sair para confirmar");
			btnSair.setVisible(true);
		}
		
	}

}
