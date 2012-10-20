package br.rio.puc.inf.view.complex;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import br.rio.puc.inf.control.db.AccessJDBC;
import br.rio.puc.inf.model.User;
import javax.swing.JDesktopPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.SystemColor;

public class BasicUserView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4594097656087367918L;

	/**
	 * Create the panel.
	 */
	public BasicUserView(User user, JFrame frame) {
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(SystemColor.inactiveCaption);
		
		JLabel lbLogin = new JLabel("Login: " + user.getUsername());
		JLabel lbGroup = new JLabel("Grupo: " + AccessJDBC.getGroupName(user.getGroupID()));
		JLabel lbDescription = new JLabel("Nome: " + user.getFullName());
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lbLogin)
					.addContainerGap(612, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lbGroup)
					.addContainerGap(612, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lbDescription)
					.addContainerGap(612, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lbLogin)
					.addGap(18)
					.addComponent(lbGroup)
					.addGap(18)
					.addComponent(lbDescription)
					.addGap(23)
					.addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
}
