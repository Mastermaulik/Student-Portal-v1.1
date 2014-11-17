import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.*;

public class Client 
{
	Connection client;
	static Statement st;
	static ResultSet rs;

	// The server socket.
	static ServerSocket serverSocket = null;
	// The client socket.
	static Socket clientSocket = null;
	
	static public int portnumber=12345;
	// This chat server can accept up to maxClientsCount clients' connections.
	private static final int maxClientsCount = 10;
	private static final clientThread[] threads = new clientThread[maxClientsCount];
	
	static String base="Files\\";
	
	public Client() throws SQLException
	{
		connect();
	}
	
	public static boolean addtopic(String topic) 
	{
		String path=base+topic+"\\";
		String sql="INSERT INTO Topics(TopicName,Path) VALUES('"+topic+"','"+path+"');";
		
			try {
				st.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("false");
				e.printStackTrace();
				return false;
			}
		System.out.println("DADASDWEW");
		return true;
	}
	
	//gives the path to which the files concerned to a topic are stored
	public static String getpathtotopic(String topic)
	{
		String path=base+topic+"\\";
		return path;
	}
	
	public static boolean searchuser(String user,String pass) throws SQLException 
	{

		String sql="select UserID from LoginDetails where UserID LIKE '"+user+"' and Password LIKE '"+pass+"'";
		System.out.println(sql);
		rs=st.executeQuery(sql);
		int count=0;//stores number of rows
		
		while(rs.next())
			count++;
		
		if(count>0)//if only 1 exists
		{
			System.out.println("There is a login from the Client Side UserID: "+user);
			return true;
		}
		else
		{
			System.out.println(count+"...................");
			return false;
		}

	}
	
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	public static boolean adduser(String name,String gpa,String user,String pass)
	{
			System.out.println(name+" "+gpa+" "+user+" "+pass);
			String sql="INSERT INTO LoginDetails(UserID,Password,Name,GPA) VALUES('"+user+"','"+pass+"','"+name+"','"+gpa+"')";
			System.out.println(sql);
			try 
			{
					st.execute(sql);
					return true;
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					return false;
			}	
		
	}
	public static ArrayList<ArrayList<String>> getuserlist(PrintStream os) throws SQLException
	{
		String sql="select Name,GPA,UserID,Password from LoginDetails";
		
		ArrayList<ArrayList<String>> feedback = new ArrayList<ArrayList<String>>();
		ArrayList<String> feed = null;
		
		try 
		{
			ResultSet rs = st.executeQuery(sql);

			ResultSetMetaData rsm = rs.getMetaData();
			feed = new ArrayList<String>();

			for(int y = 1;y<rsm.getColumnCount();y++)
			{
				feed.add(rsm.getColumnName(y));
			}
			feedback.add(feed);
			

			while(rs.next())
			{
				feed = new ArrayList<String>();
				for(int i=1;i<=rsm.getColumnCount();i++)
				{

					feed.add(rs.getString(i));
				}
				feedback.add(feed);
			}
		} catch (SQLException e) {
			//handler
			System.out.println(e);
		}
		return feedback;
	}
	
	
	public static ArrayList<ArrayList<String>> searchuserlist(PrintStream os,String match) throws SQLException
	{
		String sql="select Name,GPA,UserID,Password from LoginDetails where Name LIKE '%"+match+"%' OR UserID LIKE '%"+match+"%';";
		
		System.out.println(sql);
		ArrayList<ArrayList<String>> feedback = new ArrayList<ArrayList<String>>();
		ArrayList<String> feed = null;

		try 
		{
			ResultSet rs = st.executeQuery(sql);
						
			ResultSetMetaData rsm = rs.getMetaData();
			feed = new ArrayList<String>();

			for(int y = 1;y<rsm.getColumnCount();y++)
			{

				feed.add(rsm.getColumnName(y));
			}
			feedback.add(feed);
			

			while(rs.next())
			{
				feed = new ArrayList<String>();
				for(int i=1;i<=rsm.getColumnCount();i++)
				{

					feed.add(rs.getString(i));
				}
				feedback.add(feed);
			}
		} catch (SQLException e) {
			//handler
			System.out.println(e);
		}
		return feedback;
	}
	
	public static void senduserlist(PrintStream os,String match) throws SQLException
	{
		ArrayList<ArrayList<String>> reply=	searchuserlist(os,match);
		
		int i=1;
		System.out.println("Sending the user list row-wise");
		for(ArrayList<String> tmp : reply)
		{
			if(i!=1)
			{
				os.println(tmp.toString());
				System.out.println("Sending User data to Admins  "+tmp.toString());
			}
			i++;
		}
		os.println("ExitR");
	}
	
	public static void senduserlist(PrintStream os) throws SQLException
	{
		ArrayList<ArrayList<String>> reply=	getuserlist(os);
		
		int i=1;
		System.out.println("Sending the user list row-wise");
		for(ArrayList<String> tmp : reply)
		{
			if(i!=1)
			{
				os.println(tmp.toString());
				System.out.println("Sending User data to Admins  "+tmp.toString());
			}
			i++;
		}
		
		os.println("ExitR");
	}
	
	public static void deleteuser(String userid) throws SQLException
	{
		String sql= "delete from LoginDetails where userid like '"+userid+"';";
		System.out.println(sql);
		st.execute(sql);
	}
	public static ArrayList<ArrayList<String>> gettopiclist() throws SQLException
	{
		String sql="select TopicName,Path from Topics";
		ArrayList<ArrayList<String>> feedback = new ArrayList<ArrayList<String>>();
		ArrayList<String> feed = null;

		try {
			ResultSet rs = st.executeQuery(sql);

			ResultSetMetaData rsm = rs.getMetaData();
			feed = new ArrayList<String>();

			for(int y = 1;y<rsm.getColumnCount();y++){

				feed.add(rsm.getColumnName(y));
			}
			feedback.add(feed);

			while(rs.next()){
				feed = new ArrayList<String>();
				for(int i=1;i<=rsm.getColumnCount();i++){
					feed.add(rs.getString(i));
				}
				feedback.add(feed);
			}



		} catch (SQLException e) {
			//handler
			System.out.println("SQL Exception");
		}
		return feedback;


	}
	public static void sendtopiclist(PrintStream os) throws SQLException
	{
		ArrayList<ArrayList<String>> reply=	gettopiclist();
		int i=1;
		System.out.println("Sending Topic List to Client");
		for(ArrayList<String> tmp : reply)
		{
			if(i!=1)
			{
				os.println(tmp.get(0));
				System.out.println("Sending the following topic "+tmp.get(0));
			}
			i++;
		}
		//Just an exit statement for client to know that the list has ended
		os.println("ExitR");
	}
	public static void addnewfile(String topic,String filename) throws SQLException
	{
		String path=base+topic;
		String sql="INSERT INTO FileList(Topic,FileName) VALUES('"+topic+"','"+filename+"');";
		st.execute(sql);
	}
	
	public static ArrayList<String> getfilelist(PrintStream os,String topic) throws SQLException
	{
		String sql="SELECT FileName FROM FileList WHERE Topic LIKE '"+topic+"';";
		System.out.println(sql);
		ArrayList<String> feed = new ArrayList<String>();

		try 
		{
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next())
			{
				feed.add(rs.getString(1).toString());
			}
		} catch (SQLException e) {
			//handler
			e.printStackTrace();
			System.out.println("SQL Exception");
		}
		return feed;


	}
	public static void sendfilelist(PrintStream os,String topic) throws SQLException
	{
		ArrayList<String> reply=getfilelist(os,topic);
		
		if(reply.isEmpty())
		{
			System.out.println("EMPTY");
		}
		System.out.println("Sending File List to Client");
		for(String tmp : reply)
		{
			
			os.println(tmp.toString());
			System.out.println("Sending the following file name "+tmp.toString());
			
		}
		//Just an exit statement for client to know that the list has ended
		os.println("Exit File");
	}
	static int CHUNK_SIZE=1024; 
	public static boolean saveFile(File file, InputStream inStream,long size) 
	{
	        FileOutputStream fileOut = null;
	        try {
	            fileOut = new FileOutputStream(file);

	            byte[] buffer = new byte[CHUNK_SIZE];
	            int bytesRead;
	            int pos = 0;
	            while ((bytesRead = inStream.read(buffer, 0, CHUNK_SIZE)) >= 0) {
	                pos += bytesRead;
	                System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
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
	        if(file.length()==size)
	        {
	        	return true;
	        }
	        else
	        	return false;
	       
	}

	public static void sendFile(String path,String client) throws UnknownHostException, IOException {
    	
        if (path == null) {
            throw new NullPointerException("Path is null");
        }
        System.out.println(client);
        Socket socket=new Socket(client,Client.portnumber+200);
        File file = new File(path);
        try {
        	
        	
            System.out.println("Connecting to server...");
            System.out.println("Connected to server at "
                    + socket.getInetAddress());
            
            try (DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));) {
            	     	
            	dos.writeUTF(file.getName());
                dos.writeLong(file.length());
                System.out.println(file.getName()+"..");
                writeFile(file, dos);
              
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
	
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
	
	
	public void connect() throws SQLException 
	{
		// TODO Auto-generated method stub
		String driver="sun.jdbc.odbc.JdbcOdbcDriver";
		try 
		{
			Class.forName(driver);
			String db="jdbc:odbc:List of Topics";
			client=DriverManager.getConnection(db);
			st=client.createStatement();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL error");
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) throws IOException 
	{
		// TODO Auto-generated method stub
		
		try {
			Client client=new Client();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			serverSocket = new ServerSocket(portnumber,0,InetAddress.getByName(Inet4Address.getLocalHost().getHostAddress()));
		} catch (IOException e) {
			System.out.println(e);
		}
		
		while(true)
		{
			clientSocket=serverSocket.accept();
			
			int i=0;
			for(i=0;i<maxClientsCount;i++)
			{
				if(threads[i]==null)
				{
					(threads[i]=new clientThread(clientSocket,threads)).start();
					break;
				}
			}
			if(i==maxClientsCount)
			{
				PrintStream os=new PrintStream(clientSocket.getOutputStream());
				os.println("Server is too Busy. Try again Later.");
				os.close();
				clientSocket.close();
			}
		}
		
		
	}

}
class clientThread extends Thread 
{

	private String clientName = null;
	private  DataInputStream is;
	private PrintStream os ;
	private Socket clientSocket = null;
	private final clientThread[] threads;
	private int maxClientsCount;

	public clientThread(Socket clientSocket, clientThread[] threads) 
	{
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
	}

	public void run() 
	{
		int maxClientsCount = this.maxClientsCount;
		clientThread[] threads = this.threads;

		try
		{
			/*
			 * Create input and output streams for this client.
			 */	
			is = new DataInputStream(this.clientSocket.getInputStream());
			os = new PrintStream(this.clientSocket.getOutputStream());
			String feed=null;
			
			try 
			{
				os.flush();
				Client.sendtopiclist(os);
				System.out.println("Sending data.....");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("DB was not connected");
				e.printStackTrace();
			}

			while(true)
			{
				os.flush();
				feed=is.readLine().trim();
				
				if(feed!=null)
				{
					System.out.println("Got the following feed from the Client: "+feed);
					
					if(feed.contains("Create File"))
					{
						File file = null;
						String name=is.readLine().trim();
						System.out.println("Creating a new Topic by the name: "+name);
						
						if(name != null)
						{
							file = new File(Client.base+name);
							if(!file.exists())
							{
								
								if(file.mkdir())
								{
									file=new File(Client.base+name+"\\"+name);
									if(!file.exists())
									{
										if(!file.createNewFile())
										{
											os.println("False");
											System.out.println("Unsuccessful in creating new Topic");
										}
										else
										{
											FileWriter fw = new FileWriter(file,true); //the true will append the new data
											fw.write("\nSubject : "+name);//appends the string to the file
											fw.close();
											
											boolean flag=Client.addtopic(name);
											
											if(flag)
												os.println("True");
											else
												os.println("False");
											System.out.println("Successfully created a new Topic");
										}
									}
								}
							}
						}
						
					}
					else if(feed.contains("UploadFile"))
					{
						String topic=is.readLine().trim();
						System.out.println(topic);
						
						int port2=Client.portnumber+100;

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
						
						System.out.println(topic);
						String _downloadDir=Client.base+topic+"\\";
						File _downloadfile=new File(_downloadDir);
						
						
						if(!_downloadfile.exists())
						{
							_downloadfile.mkdir();
							
						}
					
						File file=new File(_downloadfile,fname);
						long filesize=dis.readLong();

						boolean flag=Client.saveFile(file,tempclient.getInputStream(),filesize);
			
						System.out.print("Creating a new File in the topic"+topic);
						System.out.println(" with the name "+fname);
						tempclient.close();
						tempserver.close();
						
						if(flag)
						{
							try {
								Client.addnewfile(topic, fname);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else if(feed.contains("SendFile"))
					{
						String ip=is.readLine().trim();
						String topic=is.readLine().trim();
						String fname=is.readLine().trim();
						
						String path=Client.base+topic+"\\"+fname;
						System.out.println(ip);
						Client.sendFile(path,ip.substring(1, ip.length()));
						System.out.println("successfully sent the file to IP "+ip.substring(1,ip.length()));
					}
					else if(feed.contains("GFL"))
					{
						//we need to know which topic is to be refreshed
						String topic=is.readLine().trim();
						try {
							Client.sendfilelist(os, topic);
						} catch (SQLException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					else if(feed.contains("Refresh"))
					{
						try {
							Client.sendtopiclist(os);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(feed.contains("Read Only"))
					{
						String sub=is.readLine().trim();
						
						//as it is stored in a new folder that has the same name as the topic
						String path=Client.base+sub+"\\";
						File file=null;
						file = new File(path+""+sub);
						System.out.println(sub);
						System.out.println("Opening through following path "+path+""+sub);
						
						ObjectInputStream ois =null;
						ois=new ObjectInputStream(this.clientSocket.getInputStream());  
						
						ObjectOutputStream oos =null;
						oos=new ObjectOutputStream(this.clientSocket.getOutputStream());  
						System.out.println("Successful");
						
						oos.writeObject(file.getName());  
						FileInputStream fis=null;
						fis = new FileInputStream(file);
						
						System.out.println("In read only");
						
						byte [] buffer = new byte[1000];  
						Integer bytesRead = 0;  
						boolean flag=false;
						
						while ((bytesRead = fis.read(buffer)) > 0) 
						{  
							oos.writeObject(bytesRead);
							System.out.println(bytesRead);
							oos.writeObject(Arrays.copyOf(buffer, buffer.length));  
							flag=true;
						}
						System.out.println(flag);
						if(!flag)
						{
							os.println("False");
							System.out.println("The TopicList is completely Empty.Sending this Information to client");
						}
						
						fis.close();
						os.println("Random");
						oos.flush();
						
						
					}
					else if(feed.contains("Signup"))
					{
						String name=is.readLine().trim();
						String gpa=is.readLine().trim();
						String user=is.readLine().trim();
						String pass=is.readLine().trim();
						
						System.out.println("There is a new user to be created");
						boolean flag=false;
						flag=Client.adduser(name, gpa, user, pass);
						System.out.println(flag);
						if(flag)
							os.println("True");
						else
							os.println("False");
					}
					else if(feed.contains("AddTo"))
					{
						String subject=is.readLine().trim();
						String message=is.readLine().trim();
						
						String path=Client.base+subject+"\\";
						String filename= path+""+subject;
						FileWriter fw = new FileWriter(filename,true); //the true will append the new data
						System.out.println(message+" Has been added to the subject "+subject);
						fw.write("\n"+message);//appends the string to the file
						fw.close();
						
					}
					else if(feed.contains("Write"))
					{
						String userid=is.readLine().trim();
						String pass=is.readLine().trim();
						
						
						String subject=is.readLine().trim();
						System.out.println(userid+" Wants to write to a topic "+subject);

						if(Client.searchuser(userid, pass))
						{	
							os.println("True");
							System.out.println("User has got full authentication to Write File :"+subject);
							
							
							String path=Client.base+subject+"\\";
							//after proper authentication,send the files in the subject he opened
							
							System.out.println(path+""+subject);
							
							File file=null;
							file = new File(path+""+subject);  
						
							
							System.out.println("Opening through following path "+path+""+subject);
							ObjectInputStream ois = new ObjectInputStream(this.clientSocket.getInputStream());  
							ObjectOutputStream oos = new ObjectOutputStream(this.clientSocket.getOutputStream());  

							oos.writeObject(file.getName());  
							
							FileInputStream fis=null;
							fis = new FileInputStream(file);  
							byte [] buffer = new byte[1000];  
							Integer bytesRead = 0; 
							while ((bytesRead = fis.read(buffer)) > 0) 
							{  
								oos.writeObject(bytesRead);
								System.out.println(bytesRead);
								oos.writeObject(Arrays.copyOf(buffer, buffer.length));  
							}
							fis.close();
							oos.flush();
							
						}
						else
						{
							System.out.println("False");
							os.println("False");
						}
						os.println("Random");
						
					}
					else if(feed.contains("Admin"))
					{
						String user=is.readLine().trim();
						String pass=is.readLine().trim();
						
						if(user.equals("admin") && pass.equals("IAmAdmin") )
						{
							os.println("True");
							System.out.println("Admin Login Successful");
							try {
								Client.senduserlist(os);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
							os.println("False");
					}
					else if(feed.contains("Search"))
					{
						System.out.println("YES");
						String match=is.readLine().trim();
						System.out.println(match);
						try {
							Client.senduserlist(os, match);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(feed.contains("DeleteUser"))
					{
						String userid=is.readLine().trim();
						
						try {
							Client.deleteuser(userid);
							os.println("True");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							os.println("False");
							e.printStackTrace();
						}
						
					}
					else if(feed.contains("Exit"))
					{
						break;
					}
				}	
			}
	
			is.close();
			os.close();
			clientSocket.close();
			
		}catch (IOException | SQLException e) 
		{
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
	
}
