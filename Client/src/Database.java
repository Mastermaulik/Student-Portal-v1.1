import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;


public class Database extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JButton btnAddAStudent;
	private JLabel lblName_1;
	private JLabel lblUserId_1;
	private JLabel lblPassword;
	private JLabel lblCgpa;
	private JButton btnDeleteSelectedUser;
	private String users;
	private	JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Database frame = new Database();
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
	public Database() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("Resources\\small-3498-2178234.png"));
		setResizable(false);
		setTitle("DataBase");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 778, 542);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblUserId = new JLabel("Keyword :");
		lblUserId.setBounds(5, 343, 59, 14);
		lblUserId.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		
		 final Vector<Object> columnNames = new Vector<Object>();
		 columnNames.add("Name");
		 columnNames.add("CGPA");
		 columnNames.add("UserID");
		 columnNames.add("Password");

		 String[] temp = new String[4];
		 final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		 while(!(users = Main.in.nextLine()).equals("ExitR"))
			{
				temp= users.substring(1,users.length()-1).split(", ");
				Vector<Object> vector = new Vector<Object>();
				vector.add(temp[0].toString());
				vector.add(temp[1].toString());
				vector.add(temp[2].toString());
				vector.add(temp[3].toString());
				data.add(vector);
			}

		 scrollPane_1 = new JScrollPane();
		 scrollPane_1.setBounds(10, 11, 752, 309);
		 contentPane.add(scrollPane_1);

		 
		 table = new JTable(new DefaultTableModel(data, columnNames));
		 scrollPane_1.setViewportView(table);
		 table.setFillsViewportHeight(true);
		 table.setColumnSelectionAllowed(true);
		 table.setCellSelectionEnabled(true);
		 table.getTableHeader().setFont(new Font("Footlight MT Light", Font.BOLD, 17));
		 table.setRowHeight(30);
		 table.setBackground(new Color(240, 248, 255));

		 
		textField_1 = new JTextField();
		textField_1.setBounds(82, 339, 561, 20);
		textField_1.setColumns(10);
		
		
		//Search keyword if that matches with userid or name
		final JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(673, 336, 84, 23);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String userid = textField_1.getText().toString().trim();
				
				Main.out.println("Search");
				Main.out.println(userid);
//				System.out.println(userid);
				table.clearSelection();
				String[] temp = new String[4];
				 Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				 while(!(users = Main.in.nextLine()).equals("ExitR"))
					{
//					 System.out.println(users);
						temp= users.substring(1,users.length()-1).split(", ");
//						System.out.println(String.valueOf(temp[1]));
						Vector<Object> vector = new Vector<Object>();
						vector.add(temp[0].toString());
						vector.add(temp[1].toString());
						vector.add(temp[2].toString());
						vector.add(temp[3].toString());
						data.add(vector);
					}
				 table.setModel(new DefaultTableModel(data,columnNames));
			}
		});
		btnSearch.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		
		JLabel label = new JLabel("");
		label.setBounds(15, 370, 628, 17);
		label.setIcon(new ImageIcon("Resources\\Line.png"));
		
		textField_2 = new JTextField();
		textField_2.setBounds(75, 390, 203, 20);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(75, 428, 203, 20);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(413, 390, 166, 20);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(413, 428, 166, 20);
		textField_5.setColumns(10);
		
		//Add a new data to the server database
		btnAddAStudent = new JButton("Add a student");
		btnAddAStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = textField_2.getText().trim();
				String userid = textField_3.getText().trim();
				String cgpa = textField_5.getText().trim();
				String password = textField_4.getText().trim();
				boolean flag = true;
				
				//Checks for the inserted values.
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
				//If all the inserted values are correct
				if(flag)
				{
					Main.out.println("Signup");
					Main.out.println(name);
					Main.out.println(cgpa);
					Main.out.println(userid);
					Main.out.println(password);
					if(Main.in.nextLine().startsWith("T")){
						JOptionPane.showMessageDialog(null, "Successfully signed up");
						textField_2.setText("");
						textField_3.setText("");
						textField_4.setText("");
						textField_5.setText("");
						btnSearch.doClick();
					}
					else
						JOptionPane.showMessageDialog(null, "Something went wrong");
				}
			}
		});
		btnAddAStudent.setBounds(597, 428, 137, 23);
		btnAddAStudent.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		
		lblName_1 = new JLabel("Name :");
		lblName_1.setBounds(5, 394, 66, 14);
		lblName_1.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		
		lblUserId_1 = new JLabel("User ID :");
		lblUserId_1.setBounds(5, 432, 51, 14);
		lblUserId_1.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		
		lblPassword = new JLabel("Password :");
		lblPassword.setBounds(328, 394, 75, 14);
		lblPassword.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		
		lblCgpa = new JLabel("CGPA :");
		lblCgpa.setBounds(328, 432, 75, 14);
		lblCgpa.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		
		//Delete a selected user.
		btnDeleteSelectedUser = new JButton("Delete Selected User");
		btnDeleteSelectedUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main.out.println("DeleteUser");
				int x = table.getSelectedRow();
				int y = 2;
				String userid = table.getModel().getValueAt(x, y).toString();
				
				Main.out.println(userid);
//				System.out.println(userid+"//////////");
				if(Main.in.nextLine().startsWith("T"))
				{
					JOptionPane.showMessageDialog(null, "Successfully Deleted");
					btnSearch.doClick();
				}
			}
		});
		btnDeleteSelectedUser.setBounds(137, 469, 411, 23);
		btnDeleteSelectedUser.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		contentPane.setLayout(null);
		
		contentPane.add(btnDeleteSelectedUser);
		contentPane.add(lblName_1);
		contentPane.add(lblUserId_1);
		contentPane.add(textField_3);
		contentPane.add(textField_2);
		contentPane.add(lblCgpa);
		contentPane.add(lblPassword);
		contentPane.add(textField_5);
		contentPane.add(textField_4);
		contentPane.add(btnAddAStudent);
		contentPane.add(lblUserId);
		contentPane.add(textField_1);
		contentPane.add(label);
		contentPane.add(btnSearch);
	}

	//Returns true if all the characters are numeric
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	//Returns if all the characters are numeric
	private boolean isAlpha(String str) {
		// TODO Auto-generated method stub
		 for (char c : str.toCharArray())
		    {
		        if (!Character.isAlphabetic(c)&&!Character.isSpace(c)) return false;
		    }
		    return true;
	}
}
