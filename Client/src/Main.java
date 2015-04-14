import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.SystemColor;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Font;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.BevelBorder;
import java.awt.Toolkit;


public class Main {

	private JFrame frmStudentPortal;

	private static ArrayList<String> title = new ArrayList<>();
	public  static ArrayList<String> getTitle() {
		return title;
	}
	public static void addTitle(String t) {
		title.add(t);
	}
	public static String host;
	public static int portNumber;
	static Socket clientSocket = null;
	static Scanner in = null;
	static PrintStream out = null;
	private JList list;
	static JButton btnRefresh;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				portNumber = 12345;
				host = "10.100.66.156"; //IP address of the server

				try {
					clientSocket = new Socket(host, portNumber);//connect to server
					in = new Scanner(clientSocket.getInputStream());
					out = new PrintStream(clientSocket.getOutputStream());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Server not found");
					System.exit(0);
//					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Server not found");
					System.exit(0);
//					e.printStackTrace();
				}
				////////////////////////////////
				
				try {
					Main window = new Main();
					window.frmStudentPortal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStudentPortal = new JFrame();
		frmStudentPortal.setIconImage(Toolkit.getDefaultToolkit().getImage("Resources\\small-3498-2178234.png"));
		frmStudentPortal.getContentPane().setBackground(Color.WHITE);
		frmStudentPortal.setTitle("Student Portal");
		frmStudentPortal.setResizable(false);
		frmStudentPortal.setBounds(100, 100, 639, 589);
		frmStudentPortal.getContentPane().setLayout(null);
		
		JLabel lblTopicList = DefaultComponentFactory.getInstance().createTitle("Topic List :");
		lblTopicList.setFont(new Font("Footlight MT Light", Font.PLAIN, 18));
		lblTopicList.setBounds(42, 99, 198, 24);
		frmStudentPortal.getContentPane().add(lblTopicList);
		
		
		//For adding a new topic
		JButton btnAddNewTopic = new JButton("Add New Topic");
		btnAddNewTopic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				topic.main(null);
			}
		});
		btnAddNewTopic.setFont(new Font("Footlight MT Light", Font.PLAIN, 15));
		btnAddNewTopic.setBounds(373, 101, 151, 23);
		frmStudentPortal.getContentPane().add(btnAddNewTopic);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setBounds(10, 134, 613, 334);
		frmStudentPortal.getContentPane().add(scrollPane);
		
		 
		title = getFileListFromServer(); // get the list of ongoing discussion
		String []arr = {"No Discussion"};
		if(title.isEmpty())list = new JList(arr);
		else
			list = new JList(title.toArray());
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
		 list.setBorder(lineBorder);
		 list.setFixedCellHeight(20);
		 list.setFont(new Font("Footlight MT Light", Font.BOLD, 16));
		scrollPane.setViewportView(list);
		list.setBackground(SystemColor.inactiveCaptionBorder);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("Resources\\welcome1.png"));
		label.setBounds(10, 11, 433, 93);
		frmStudentPortal.getContentPane().add(label);
		
		
		//For SignUp form
		JButton btnNewButton = new JButton("Sign Up");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SignUp.main(null);
			}
		});
		btnNewButton.setFont(new Font("Footlight MT Light", Font.PLAIN, 17));
		btnNewButton.setBounds(329, 494, 261, 35);
		frmStudentPortal.getContentPane().add(btnNewButton);
		
		
		//For admin login and manipulate database. 
		JButton btnNewButton_1 = new JButton("Admin LogIn");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminLogIn.main(null);
			}
		});
		btnNewButton_1.setFont(new Font("Footlight MT Light", Font.PLAIN, 17));
		btnNewButton_1.setBounds(42, 494, 261, 35);
		frmStudentPortal.getContentPane().add(btnNewButton_1);
		
		//Refresh to see anyone has added a new topic
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				out.println("Refresh");
				title = getFileListFromServer();
				list.setListData(title.toArray());
			}
		});
		
		
		//Double click to open a discussion
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList)evt.getSource();
				if (evt.getClickCount() == 2) {
					int index = list.locationToIndex(evt.getPoint());
					String [] args = {String.valueOf(index)};
					LogIn.main(args);
				} 
			}
		});
		btnRefresh.setFont(new Font("Footlight MT Light", Font.PLAIN, 15));
		btnRefresh.setBounds(534, 101, 89, 23);
		frmStudentPortal.getContentPane().add(btnRefresh);
		
		frmStudentPortal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStudentPortal.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				Main.out.println("Exit");
				System.exit(0);
			}
		});
	}

	//Get the current ongoing discussion
	private ArrayList<String> getFileListFromServer() {
		// TODO Auto-generated method stub
		ArrayList<String> tmp = new ArrayList<>();
		while(Main.in.hasNextLine()){
			String kj = Main.in.nextLine();
			if(kj.equals("ExitR"))
				break;
			tmp.add(kj);
		}
		//		System.out.println(tmp.toString());
		return tmp;
	}
	
}
