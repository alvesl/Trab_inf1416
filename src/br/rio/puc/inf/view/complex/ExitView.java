package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.rio.puc.inf.model.User;

public class ExitView extends JPanel {

	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private User currentUser = null;
	
	/**
	 * Create the panel.
	 */
	public ExitView(final JPanel parentPanel,final CardLayout cl, User user) {
		setLayout(null);
		
		currentUser = user;
		
		JLabel lblTotalDeAcessos = new JLabel("Total de acessos do usu\u00E1rio:");
		lblTotalDeAcessos.setBounds(274, 11, 144, 14);
		add(lblTotalDeAcessos);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(420, 11, 46, 14);
		add(lblNewLabel);
		
		JLabel lblSemMensagensDe = new JLabel("Sem mensagens de sa\u00EDda.");
		lblSemMensagensDe.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSemMensagensDe.setBounds(100, 103, 583, 88);
		add(lblSemMensagensDe);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setBounds(540, 305, 89, 23);
		add(btnSair);
		
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
		btnVoltar.setBounds(654, 305, 89, 23);
		add(btnVoltar);

	}

}
