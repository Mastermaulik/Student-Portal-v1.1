import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JScrollPane;
import java.awt.Toolkit;


public class viewFile extends JFrame {

	private JPanel contentPane;
	private static final Integer BUFFER_SIZE = 1000;
	private static final int CHUNK_SIZE = 1024;
	private static String name;
    private static final File _downloadFile = new File("downloads/");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		name = args[0];
		if (!_downloadFile.exists()) {
            if (!_downloadFile.mkdirs()) {
                System.err.println("Error: Could not create download directory");
            }
        }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					viewFile frame = new viewFile();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public viewFile() throws IOException, ClassNotFoundException {
		setIconImage(Toolkit.getDefaultToolkit().getImage("Resources\\small-3498-2178234.png"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(name);
		setBounds(100, 100, 824, 454);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblRecentlyUploadedFile = new JLabel("Recently Uploaded File :");
		lblRecentlyUploadedFile.setBounds(603, 21, 138, 14);
		lblRecentlyUploadedFile.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));

		final JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		//For the discussion
		ObjectOutputStream oos = new ObjectOutputStream(Main.clientSocket.getOutputStream());  
		 ObjectInputStream ois = new ObjectInputStream(Main.clientSocket.getInputStream());  
		byte [] buffer = new byte[BUFFER_SIZE];
		Object o = ois.readObject();
		Integer bytesRead = 0;  

		do {  
			o = ois.readObject();  

			if (!(o instanceof Integer)) {  
				JOptionPane.showMessageDialog(null, "Something is wrong");  
			}  

			bytesRead = (Integer)o;  
			o = ois.readObject();  

			if (!(o instanceof byte[])) {  
				JOptionPane.showMessageDialog(null, "Something is wrong");  
			}  

			buffer = (byte[])o;  
			// 3. Write data to output file.  
			textPane.setText(new String(buffer));  
		} while (bytesRead == BUFFER_SIZE); 
		Main.in.nextLine();
		oos.flush();
//		String[] fileList = {"1"};
		ArrayList<String> fileList = getFileList();
		

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(603, 41, 200, 348);
		contentPane.add(scrollPane_1);
		
		final Box verticalBox = Box.createVerticalBox();
		verticalBox.setFont(new Font("Footlight MT Light", Font.BOLD, 15));
		
		scrollPane_1.setViewportView(verticalBox);
		final JList list_1 = new JList(fileList.toArray());
		list_1.setBackground(UIManager.getColor("List.background"));
		list_1.setFixedCellHeight(25);
		verticalBox.add(list_1);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(712, 397, 91, 23);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list_1.removeAll();
				Vector<String> l = new Vector<>();
				for(String str : getFileList())
				{
					l.addElement(str);
				}
				list_1.setListData(l);
				Main.out.println("Read Only");
				Main.out.println(name);
				ObjectOutputStream oos = null;  
				 ObjectInputStream ois = null;
				try {
					oos = new ObjectOutputStream(Main.clientSocket.getOutputStream());
					ois = new ObjectInputStream(Main.clientSocket.getInputStream());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Object o = null;
				try {
					o = ois.readObject();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Integer bytesRead = 0; 
				byte [] buffer = new byte[BUFFER_SIZE];
				do {  
					try {
						o = ois.readObject();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  

					if (!(o instanceof Integer)) {  
						JOptionPane.showMessageDialog(null, "Something is wrong");  
					}  

					bytesRead = (Integer)o;  
					try {
						o = ois.readObject();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  

					if (!(o instanceof byte[])) {  
						JOptionPane.showMessageDialog(null, "Something is wrong");  
					}  

					buffer = (byte[])o;  
					// 3. Write data to output file.  
					textPane.setText(new String(buffer));  
				} while (bytesRead == BUFFER_SIZE); 
				Main.in.nextLine();
				try {
					oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		contentPane.setLayout(null);
		
		final JLabel label = new JLabel("");
		label.setBounds(15, 400, 667, 15);
		contentPane.add(label);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 41, 570, 348);
		contentPane.add(scrollPane);
		
		
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
		contentPane.add(lblRecentlyUploadedFile);
		
		list_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList)evt.getSource();
				if (evt.getClickCount() == 2) {
					Main.out.println("SendFile");
					String fileName = (String)list.getSelectedValue();
					try {
						Main.out.println(InetAddress.getByName(Inet4Address.getLocalHost().getHostAddress()).toString());
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Main.out.println(name);
					Main.out.println(fileName);
					try {
						getfile(fileName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					label.setText("Finished Downloading File : "+fileName);
				} 
			}

		});
		contentPane.add(btnRefresh);
		
		
	}
	
	
//List the recently uploaded files for the current topic.
	private ArrayList<String> getFileList() {
		// TODO Auto-generated method stub
		ArrayList<String> arr = new ArrayList<String>();
		Main.out.println("GFL");
		Main.out.println(name);
		String temp = Main.in.nextLine();
		while(!temp.equals("Exit File"))
		{
			arr.add(temp);
			temp = Main.in.nextLine();
		}
		if(arr.isEmpty())arr.add("No recent files..");
		
		return arr;
	}
//Download file from server
	private void getfile(String fileName) throws IOException {
		// TODO Auto-generated method stub
		int port2=Main.portNumber+200;

		ServerSocket tempserver = null;

		Socket tempclient = null;
		

		try {
			tempserver = new ServerSocket(port2,0,InetAddress.getByName(Inet4Address.getLocalHost().getHostAddress()));
		} catch (IOException e) {
			System.out.println(e);
		}
		
		tempclient=tempserver.accept();
		
		DataInputStream dis=new DataInputStream(tempclient.getInputStream());
		String fname=dis.readUTF();
		
	
		//it might be needed to send the path to which to upload the file
		//sending the path to which the user can upload the file
		
		File file=new File(_downloadFile,fname);
		long filesize=dis.readLong();

		saveFile(file,tempclient.getInputStream());
		tempclient.close();
		tempserver.close();
	}
//Save file to the local machine
 private static void saveFile(File file, InputStream inStream) {
	        FileOutputStream fileOut = null;
	        try {
	            fileOut = new FileOutputStream(file);

	            byte[] buffer = new byte[CHUNK_SIZE];
	            int bytesRead;
	            int pos = 0;
	            while ((bytesRead = inStream.read(buffer, 0, CHUNK_SIZE)) >= 0) {
	                pos += bytesRead;
	                //System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
	                fileOut.write(buffer, 0, bytesRead);
	            }
	        } catch (IOException e) { 
	            e.printStackTrace();
	        } finally {
	            if (fileOut != null) {
	                try {
	                    fileOut.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        System.out.println("Finished, filesize = " + file.length());
	    }

}
