package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.SystemColor;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.rio.puc.inf.control.db.AccessJDBC;
import br.rio.puc.inf.model.User;


public class BasicMenuView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4594097656087367918L;
	private JPanel renderPanel;
	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private final String EXIT = "To exit the program";
	/**
	 * Create the panel.
	 */
	public BasicMenuView(User user, JFrame frame) {
		
		JLabel lbLogin = new JLabel("Login: " + user.getUsername());
		lbLogin.setBounds(349, 11, 87, 14);
		JLabel lbGroup = new JLabel("Grupo: " + AccessJDBC.getGroupName(user.getGroupID()));
		lbGroup.setBounds(349, 39, 149, 14);
		JLabel lbDescription = new JLabel("Nome: " + user.getFullName());
		lbDescription.setBounds(349, 64, 89, 14);
		setLayout(null);
		add(lbLogin);
		add(lbGroup);
		add(lbDescription);
		
		renderPanel = new JPanel(new CardLayout());
		renderPanel.setBounds(0, 98, 784, 339);
		add(renderPanel);
		
		CardLayout cl = (CardLayout) renderPanel.getLayout();
		
		//Criação de todas as views contidas no card
		AdminMenuView admMenu = new AdminMenuView(renderPanel,cl);
		renderPanel.add(admMenu, ADMINMENU);
		
		UserMenuView usrMenu = new UserMenuView(renderPanel, cl);
		renderPanel.add(usrMenu, USRMENU);
		
		AdminCreateView admCreate = new AdminCreateView(renderPanel, cl, user);
		renderPanel.add(admCreate, CREATE);
		
		AdminEditMenu admEdit = new AdminEditMenu(renderPanel, cl, user);
		renderPanel.add(admEdit, EDIT);
		
		AdminListFiles admList = new AdminListFiles(renderPanel, cl, user);
		renderPanel.add(admList, LIST);
		
		ExitView exit = new ExitView(renderPanel, cl, user);
		renderPanel.add(exit, EXIT);
		
		// Renderizar corretamente o painel do usuário ou do adm
		if (user.getGroupID() == 0) {
			//User is adm group
			
			cl.show(renderPanel, ADMINMENU);
			
		} else if (user.getGroupID() == 1) {
			//User is user group
			
			cl.show(renderPanel, USRMENU);

			
		}

	}
}
