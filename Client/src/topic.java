import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Toolkit;


public class topic extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					topic frame = new topic();
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
	public topic() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("Resources\\small-3498-2178234.png"));
		setTitle("Add Topic");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 463, 248);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Create a new Subject :");
		lblNewLabel.setFont(new Font("Footlight MT Light", Font.BOLD, 19));
		lblNewLabel.setBounds(53, 35, 244, 42);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setFont(new Font("Footlight MT Light", Font.PLAIN, 15));
		textField.setBounds(53, 75, 340, 48);
		contentPane.add(textField);
		textField.setColumns(10);

		
		//Submit to add a new topic
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setFont(new Font("Footlight MT Light", Font.PLAIN, 18));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.out.println("Create File ");
				String topic = textField.getText();
				ArrayList<String> t = Main.getTitle();
				//Check if a new topic that you want to create is exists or not.
				if(t.contains(topic))
					JOptionPane.showMessageDialog(null, "Topic Already Exists");
				else{
					Main.out.println(topic);
					String s = Main.in.nextLine();
					//				System.out.println();
					if(s.equals("True")){
						Main.btnRefresh.doClick();
						dispose();
					}
					else{
						JOptionPane.showMessageDialog(null, "File already exists or something went wrong");
					}
				}
			}
		});
		btnSubmit.setBounds(173, 152, 124, 31);
		contentPane.add(btnSubmit);
	}

}
