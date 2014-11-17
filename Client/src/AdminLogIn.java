import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Toolkit;


public class AdminLogIn extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminLogIn frame = new AdminLogIn();
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
	public AdminLogIn() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("Resources\\small-3498-2178234.png"));
		setTitle("Admin");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAdministration = new JLabel("Administration");
		lblAdministration.setIcon(new ImageIcon("Resources\\admin (16x16).jpg"));
		lblAdministration.setFont(new Font("Traditional Arabic", Font.BOLD, 20));
		lblAdministration.setBounds(132, 11, 179, 37);
		contentPane.add(lblAdministration);

		textField = new JTextField();
		textField.setBounds(177, 59, 179, 37);
		contentPane.add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(177, 129, 179, 37);
		contentPane.add(passwordField);

		JLabel lblAdminId = new JLabel("Admin ID :");
		lblAdminId.setFont(new Font("Footlight MT Light", Font.PLAIN, 16));
		lblAdminId.setBounds(45, 70, 122, 17);
		contentPane.add(lblAdminId);

		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setFont(new Font("Footlight MT Light", Font.PLAIN, 16));
		lblPassword.setBounds(45, 137, 122, 17);
		contentPane.add(lblPassword);

		//Log In to see the database
		JButton btnNewButton = new JButton("Log In");
		btnNewButton.setFont(new Font("Footlight MT Light", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String uid = textField.getText().toLowerCase().toString();
				String pswd = passwordField.getText().toString();

				Main.out.println("Admin");
				Main.out.println(uid);
				Main.out.println(pswd);

				//Send a userid and password to the server 
				//If the password and userid matches than server will send true
				if(Main.in.nextLine().equals("True"))
				{
					dispose();
					Database.main(null);
					System.out.println("Opening database...");
				}
				else
					JOptionPane.showMessageDialog(null, "Wrong Credentials");

			}
		});
		btnNewButton.setBounds(144, 197, 107, 37);
		contentPane.add(btnNewButton);
	}
}
