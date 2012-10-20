package br.rio.puc.inf.view.complex;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class BasicUserView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4594097656087367918L;

	/**
	 * Create the panel.
	 */
	public BasicUserView(String currentUser) {
		setLayout(null);
		
		JLabel lblBemVindoUser = new JLabel("Bem vindo "+currentUser+"!");
		lblBemVindoUser.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblBemVindoUser.setBounds(287, 11, 268, 21);
		add(lblBemVindoUser);

	}

}
