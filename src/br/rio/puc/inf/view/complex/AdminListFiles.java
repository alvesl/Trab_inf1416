package br.rio.puc.inf.view.complex;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import sun.misc.BASE64Decoder;
import br.rio.puc.inf.control.instruments.Cryptography;
import br.rio.puc.inf.control.instruments.Log;
import br.rio.puc.inf.model.User;

public class AdminListFiles extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4839159265731929234L;
	private final String ADMINMENU = "The user menu";
	private final String USRMENU = "The low access user menu";
	private final String CREATE = "To create a new user menu";
	private final String EDIT = "To edit a user";
	private final String LIST = "To list all user files";
	private User currentUser = null;
	private static ArrayList<File> encFiles = new ArrayList<File>();
	private JTable table;
	private JTable table_1;
	
	
	/**
	 * Create the panel.
	 */
	public AdminListFiles(final JPanel parentPanel,final CardLayout cl, final User user) {
		
		currentUser = user;
		setLayout(null);
		
		JLabel lblTotalDeConsultas = new JLabel("Total de consultas do usu\u00E1rio:");
		lblTotalDeConsultas.setBounds(282, 11, 150, 14);
		add(lblTotalDeConsultas);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(435, 11, 46, 14);
		add(lblNewLabel);
		
		
	
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(652, 305, 89, 23);
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Log.registerMessage(8002, currentUser.getUsername()); // LOG: Botão voltar de consultar para o menu principal pressionado por
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
		add(btnVoltar);
		

		DefaultTableModel model = new DefaultTableModel() {

			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		model.addColumn("Arquivo");
		model.addColumn("HEX Assinatura Digital");
		model.addColumn("HEX Envelope Digital");
		
		readFiles(model);
		
		
		table = new JTable(model);
		table.setBounds(10, 53, 765, 227);

		add(table);
		
		   table.addMouseListener(new MouseAdapter() {

		        @Override
		        public void mouseClicked(MouseEvent e) {

		            int row = table.rowAtPoint(e.getPoint());
		            int col = table.columnAtPoint(e.getPoint());

		            Object selectedObj = table.getValueAt(row, col);
		            if (col == 0) {
		            	// Realizar rotina de decriptação
		            	
		            	String privFile = (String) selectedObj;
		            	privFile = privFile.substring(0, privFile.length()-4); 
		            	
		    			try {
							byte[] envelopeBytes = Cryptography.getEncFile(privFile + ".env");
							byte[] encFileBytes = Cryptography.getEncFile(privFile + ".enc");
							byte[] signatureBytes = Cryptography.getEncFile(privFile + ".asd");
							
			    			// Decriptar o envelope digital
			    			Cipher cipher;

							cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			    			PrivateKey privKey = user.getPkey();
			    			cipher.init(Cipher.DECRYPT_MODE, privKey);
							
			    			byte[] decryptedEnvelope = cipher.doFinal(envelopeBytes);
			    			
			    			// gerar chave DES utilizando a semente do envelope
			    			Key key = Cryptography.generateDESKey(decryptedEnvelope);
			    			Cipher cipherB= Cipher.getInstance("DES/ECB/PKCS5Padding");
			    			cipherB.init(Cipher.DECRYPT_MODE, key);
			    			
			    			
			    			
			    			
			    			// Descriptar o arquivo codificado
			    			byte[] decFileBytes = cipherB.doFinal(encFileBytes);
			    			
			    			FileOutputStream fos = new FileOutputStream((String) privFile);
			    			fos.write(decFileBytes);
			    			fos.close();
			    			
			    			// Verificar assinatura digital
			    			Signature sig = Signature.getInstance("MD5WithRSA");
							KeyFactory rsaKeyFac =  KeyFactory.getInstance("RSA");
							byte[] decodedBytes = new BASE64Decoder().decodeBuffer(user.getPublicKey()); //Recupera os array de bytes da string do DB
							X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedBytes);
							RSAPublicKey pubKey;
							   pubKey = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
			    			sig.initVerify(pubKey);
			    			sig.update((decFileBytes));
			    			if (sig.verify(signatureBytes)) {
								JOptionPane.showMessageDialog(null,
										"Assinatura verificada, arquivo está íntegro!");
			    			} else {
								JOptionPane.showMessageDialog(null,
										"Erro, assinatura não verificada, arquivo corrompido!");
								return;
			    			}
			    			
			    			
			    			
			    			
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null,
									"Erro, verifique se algum arquivo está faltando ou corrompido!");
							return;
						}
		    			
		            	
		            }
		        }
		    });


	}
	
	
	private static void readFiles(DefaultTableModel modelTable) {
		
		 File folder = new File(".");
		    File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  if (listOfFiles[i].getName().endsWith(".enc")) {
		    		  encFiles.add(listOfFiles[i]);
		    		  modelTable.addRow(new Object[] {listOfFiles[i].getName(), null, null});
		    	  }
		      } 
		    }
		
		
	}
}
