import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

import javax.swing.JPasswordField;


public class SignUp extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUp frame = new SignUp();
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
	public SignUp() {
		setTitle("Sign Up");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 489, 476);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(157, 66, 239, 32);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setFont(new Font("Footlight MT Light", Font.PLAIN, 18));
		lblName.setBounds(30, 66, 115, 32);
		contentPane.add(lblName);
		
		JLabel lblUserId = new JLabel("User ID :");
		lblUserId.setFont(new Font("Footlight MT Light", Font.PLAIN, 18));
		lblUserId.setBounds(30, 122, 115, 32);
		contentPane.add(lblUserId);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(157, 122, 239, 32);
		contentPane.add(textField_1);
		
		JLabel lblCgpa = new JLabel("CGPA");
		lblCgpa.setFont(new Font("Footlight MT Light", Font.PLAIN, 18));
		lblCgpa.setBounds(30, 182, 115, 32);
		contentPane.add(lblCgpa);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(157, 182, 239, 32);
		contentPane.add(textField_2);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setFont(new Font("Footlight MT Light", Font.PLAIN, 18));
		lblPassword.setBounds(30, 242, 115, 32);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(157, 242, 239, 32);
		contentPane.add(passwordField);
		
		//SignUp for the forum
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = textField.getText().trim();
				String userid = textField_1.getText().trim();
				String cgpa = textField_2.getText().trim();
				String password = passwordField.getText().trim();
				boolean flag = true;
				
				//Some relevent checks for the input
				if(!isNumeric(userid) && flag) {
					flag = false;
					JOptionPane.showMessageDialog(null, "Wrong user-ID");
				}
				if(!isAlpha(name) && flag)
				{
					flag = false;
					JOptionPane.showMessageDialog(null, "Name should contains only alphabets");
				}
				double x=-1;
				try{
					x = Double.parseDouble(cgpa);
				}
				catch(NumberFormatException e)
				{
					flag = false;
					JOptionPane.showMessageDialog(null, "CGPA should be a number");
				}
				if(x<0 || x>10 && flag)
				{
					flag = false;
					JOptionPane.showMessageDialog(null, "CGPA must be less than 10 and greater than 0");
				}
				if(password.length()<8 && flag)
				{
					flag = false;
					JOptionPane.showMessageDialog(null, "Password length must greater or equal to 8");
				}
				//If all the input are correct
				if(flag)
				{
					Main.out.println("Signup");
					Main.out.println(name);
					Main.out.println(cgpa);
					Main.out.println(userid);
					Main.out.println(password);
					if(Main.in.nextLine().startsWith("T")){
						JOptionPane.showMessageDialog(null, "Successfully signed up");
						dispose();
					}
					else
						JOptionPane.showMessageDialog(null, "Something went wrong");
				}
			}

			
		});
		btnSubmit.setFont(new Font("Footlight MT Light", Font.PLAIN, 18));
		btnSubmit.setBounds(183, 348, 131, 32);
		contentPane.add(btnSubmit);
		
		
	}
	//Returns true if all characters are numbers
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	//Retuens true if all characters are alphabets
	private boolean isAlpha(String str) {
		// TODO Auto-generated method stub
		 for (char c : str.toCharArray())
		    {
		        if (!Character.isAlphabetic(c)&&!Character.isSpace(c)) return false;
		    }
		    return true;
	}
}
