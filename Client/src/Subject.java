import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Toolkit;


public class Subject extends JFrame {

	private static final Integer BUFFER_SIZE = 1000;
	private static String subject;
	private JPanel contentPane;
	private static String name;
	private static JLabel label;
	static Subject frame;
	static JButton btnRefresh;
    private static final File _downloadFile = new File("downloads/");
//	private static JTextArea textArea;
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
					frame = new Subject();
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
	public Subject() throws IOException, ClassNotFoundException {
		setIconImage(Toolkit.getDefaultToolkit().getImage("Resources\\small-3498-2178234.png"));
		setResizable(false);
		System.out.println(name);
//		Main.out.println("Write Permission");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(name);
		setBounds(100, 100, 824, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblRecentlyUploadedFile = new JLabel("Recently Uploaded File :");
		lblRecentlyUploadedFile.setBounds(601, 21, 138, 14);
		lblRecentlyUploadedFile.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));

		final JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBounds(15, 41, 568, 293);
		contentPane.add(scrollPane);
		
		
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
		
		//content of the discussion
		ObjectOutputStream oos = new ObjectOutputStream(Main.clientSocket.getOutputStream());  
		final ObjectInputStream ois = new ObjectInputStream(Main.clientSocket.getInputStream());  
		byte [] buffer = new byte[BUFFER_SIZE];
		Object o = ois.readObject();
		Integer bytesRead = 0;  

		do {  
			o = ois.readObject();  

			if (!(o instanceof Integer)) {  
				JOptionPane.showMessageDialog(null, "Something is wrong");  
			}  

			bytesRead = (Integer)o; 
			System.out.println(bytesRead+" "+o);
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
		final ArrayList<String> fileList =  getFileList();
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(15, 360, 568, 50);
		contentPane.add(scrollPane_1);
		
		final JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		textArea_1.setFont(new Font("Footlight MT Light", Font.PLAIN, 13));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(601, 41, 200, 293);
		contentPane.add(scrollPane_2);
		
		Box verticalBox = Box.createVerticalBox();
		scrollPane_2.setViewportView(verticalBox);
		final JList list = new JList(fileList.toArray());
		list.setBackground(UIManager.getColor("List.background"));
		list.setFixedCellHeight(20);
		verticalBox.add(list);
		
		//Refreah the content of the discussion
		btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		btnRefresh.setBounds(697, 387, 104, 23);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
					System.out.println(bytesRead+" "+o);
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
				list.removeAll();
				Vector<String> l = new Vector<>();
				for(String str : getFileList())
				{
					l.addElement(str);
				}
				list.setListData(l);
			}
		});
		
		//Submit to send whatever you written in the textfield to the server
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		btnSubmit.setBounds(697, 361, 104, 23);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String str = textArea_1.getText().trim();
				if(str.isEmpty()){
					JOptionPane.showMessageDialog(null, "You can't submit empty string");
				}
				else{
					Main.out.flush();
					Main.out.println("AddTo");
					Main.out.println(name);
					Main.out.println("< "+LogIn.userId+" >:"+str);
					Main.out.flush();
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
					//			        Object o = null;
					//					try {
					//						o = ois.readObject();
					//					} catch (ClassNotFoundException e1) {
					//						// TODO Auto-generated catch block
					//						e1.printStackTrace();
					//					} catch (IOException e1) {
					//						// TODO Auto-generated catch block
					//						e1.printStackTrace();
					//					}
					//			        	Integer bytesRead = 0;  

					do {  
						try {
							o = ois.readObject();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}  

						if (!(o instanceof Integer)) {  
							JOptionPane.showMessageDialog(null, "Something is wrong");  
						}  

						bytesRead = (Integer)o;  
						//			        		if(bytesRead == 0)
						//			        			textArea.setText("Be the first to comment");
						//			        		else{
						try {
							o = ois.readObject();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
					textArea_1.setText("");
				}
			}
		});
		
		//Upload a file to the server
		JButton btnUploadAFile = new JButton("Upload");
		btnUploadAFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = textArea_1.getText().trim();
				File file = new File(path);
				label.setText("Uploading file : "+file.getName()+"...");
				
				//Check if the file exist to the client side
		        if(!file.exists()||fileList.contains(file.getName()))
		        {
		        	label.setText("");
		        	JOptionPane.showMessageDialog(null, "File Not Found or already exists..");
		        }
		        else{
		        	
		        	Main.out.println("UploadFile");
		        	Main.out.println(name);
		        	sendFile(path);
		        	btnRefresh.doClick();
		        }
			}
		});
		btnUploadAFile.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		btnUploadAFile.setBounds(593, 361, 94, 49);
		
		JLabel lblTipTo = new JLabel("TIP : To upload a file to the server add a path in textfield and hit \"upload\".");
		lblTipTo.setBounds(15, 340, 568, 14);
		lblTipTo.setFont(new Font("Footlight MT Light", Font.PLAIN, 14));
		contentPane.setLayout(null);
		
		
		contentPane.add(btnUploadAFile);
		contentPane.add(btnSubmit);
		contentPane.add(btnRefresh);
		contentPane.add(lblTipTo);
		
		
		contentPane.add(lblRecentlyUploadedFile);
		
		
		
		label = new JLabel("");
		label.setBounds(15, 432, 568, 14);
		contentPane.add(label);
		
		//Double click the file name to download file to the local machine.
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList)evt.getSource();
				if (evt.getClickCount() == 2) {
					Main.out.println("SendFile");
					String fileName = (String)list.getSelectedValue();
					label.setText("Downloading file: "+fileName+"...");
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
					label.setText("Finished Downloading File : "+fileName+"...");
				} 
			}

		});
//		addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent winEvt) {
//                Main.out.println("\\quit");
//                dispose();
//            }
//        });
	}

	//Get the recently uploaded file list for the current topic.
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
	//Upload a file to the server
	private static void sendFile(String path) {
    	
        if (path == null) {
            JOptionPane.showMessageDialog(null,"Path is null");
        }

        File file = new File(path);
        if(!file.exists())
        {
        	JOptionPane.showMessageDialog(null, "File Not Found");
        }
        else{
        	try {
        		Socket socket = new Socket(Main.host,Main.portNumber+100);
        		System.out.println("Connecting to server...");
        		System.out.println("Connected to server at "
        				+ socket.getInetAddress());

        		try (DataOutputStream dos = new DataOutputStream(
        				new BufferedOutputStream(socket.getOutputStream()));) {

        			dos.writeUTF(file.getName());
        			dos.writeLong(file.length());
        			System.out.println(file.getName()+"..");

        			frame.label.setText("Sending " + file.getName() + " ("
        					+ file.length() + " bytes) to server...");
        			writeFile(file, dos);
        			frame.label.setText("Finished sending " + file.getName()
        					+ " to server...");

        		}
        	} catch (IOException e) {
        		JOptionPane.showMessageDialog(null, "File Not Found");
        		e.printStackTrace();
        	}
        }
    }
	static int CHUNK_SIZE = 1024;
    private static void writeFile(File file, OutputStream outStream) {
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(file);
            byte[] buffer = new byte[CHUNK_SIZE];
            int pos = 0;
            int bytesRead;
            while ((bytesRead = reader.read(buffer, 0, CHUNK_SIZE)) >= 0) {
                outStream.write(buffer, 0, bytesRead);
                outStream.flush();
                pos += bytesRead;
                System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
            }
        } catch (IndexOutOfBoundsException e) {
        	frame.label.setText("Error while reading file");
            e.printStackTrace();
        } catch (IOException e) {
        	frame.label.setText("Error while writing " + file.toString() + " to output stream");
            e.printStackTrace();
        } 
    }
    
    //Download the selected file
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


