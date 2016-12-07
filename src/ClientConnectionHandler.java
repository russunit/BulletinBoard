import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

// TODO this class is a stub; Dan said he had a cch written so this is the placeholder.
// for my BBServer to work we need these 3 methods (including the constructor)


public class ClientConnectionHandler extends Thread {
	
	private Message newMessage;
	private User newUser;
	
	
	
	
	public boolean hasMessage;
	public boolean hasNewUser;
	public boolean hasLogout;
	
	
	private User currentUser;
	private User tempUser;
	private User userToLogOut;
	private boolean isLoggedIn;
	
	private ArrayList<User> userList;
	private ArrayList<Message> messageList;
	
	private ArrayList<User> loggedInUserList;
	
	private Socket connection;
    private InputStream clientInput;
    private OutputStream clientOutput;
    private Scanner scanner;
    private OutputStreamWriter osw;
    
    private String option = "";
    
    private String uName = "";
    private String uPass = "";
    
    private String mTitle = "";
    private String mTopic = "";
    private String mContent = "";
    private User mAuthor = null;
    private Date mDate = null;
    
    private int displaySize = 0;
    private boolean validChoice = false;

	public ClientConnectionHandler(Socket clientConnection, ArrayList<User> uL, ArrayList<Message> mL, ArrayList<User> lIU) 
	{
		connection = clientConnection;
		this.userList = uL;
		this.loggedInUserList = lIU;
		this.messageList = mL;
		
		this.newMessage = null;
		this.currentUser = null;
		
		this.hasMessage = false;
		this.isLoggedIn = false;
		this.hasNewUser = false;
        
        try
        {
            clientInput = connection.getInputStream();
            clientOutput = connection.getOutputStream();
            scanner = new Scanner(clientInput);
            osw = new OutputStreamWriter(clientOutput);
        }
        catch(IOException e)
        {
            System.out.println("Error reading/writing from/to client");
        }
        
		
	}

	@Override
	public void run() 
	{
		try
        {
            osw.write("Welcome to BBServer Console.\r\n");
            osw.write("This is the development, pre-GUI configuration for testing\r\n");
            
            osw.flush();
            
           

                                   
            //loop to continue asking options
            while(!option.equalsIgnoreCase("exit"))
            {
            	osw.write("\r\n\r\nPlease enter an option:\r\n");
                osw.write("1. Log in\r\n");
                osw.write("2. Sign up\r\n");
                osw.write("3. Log out\r\n");
                osw.write("4. Post Message\r\n");
                osw.write("5. View recent messages\r\n");
                osw.write("6. VIEW ALL USERS\r\n");
                osw.write("7. VIEW ACTIVE USERS\r\n");
                
                osw.write("...Or type exit to quit.\r\n");
                

                
                osw.flush();
                option = scanner.nextLine();
            	
            	if(option.equals("1"))
            	{signInOption();}
            	else if(option.equals("2"))
            	{signUpOption();}
            	else if(option.equals("3"))
            	{signOutOption();}
            	else if(option.equals("4"))
            	{postMessageOption();}
            	else if(option.equals("5"))
            	{viewMessagesOption();}
            	else if(option.equals("6"))
            	{viewAllUsersOption();}
            	else if(option.equals("7"))
            	{viewActiveUsersOption();}
            	else if(option.equalsIgnoreCase("exit"))
            	{exitOption();}
            	else
            	{invalidOption();}
            }

        }
        catch(IOException e)
        {
            System.out.println("Error reading/writing from/to client");
        }

	}

	public boolean isConnected()  throws IOException
    {
        boolean stillConnected = true;
        stillConnected = isAlive();
        return stillConnected;
    }
	
	public boolean hasMessage()
	{
		return this.hasMessage;
	}
	
	public Message getMessage()
	{
		return this.newMessage;
	}
	
	public void setMessage(String content, String topic, String title, User author, Date date)
	{
		this.newMessage = new Message(content, topic, title, author, date );
		this.hasMessage = true;
		messageList.add(newMessage);
	}
	
	public void clearMessage()
	{
		this.newMessage = null;
		this.hasMessage = false;
	}
	
	public User getNewUser()
	{
		return this.newUser;
	}
	
	public void setNewUser(User u)
	{
		this.newUser = u;
		this.hasNewUser = true;
		userList.add(tempUser);
	}
	
	public void clearNewUser()
	{
		this.newUser = null;
		this.hasNewUser = false;
	}
	
	public boolean hasNewUser()
	{
		return this.hasNewUser;
	}
	
	public boolean containsUser(User u)
	{
		for(int x = 0; x < userList.size(); x++)
		{
			if(u.nameMatches(userList.get(x).name))
				return true;
		}
		return false;
	}
	
	public boolean containsMessage( Message m)
	{
		for(int x = 0; x < messageList.size(); x++)
		{
			if(m.titleMatches(messageList.get(x).title))
				return true;
		}
		return false;
	}
	
	public boolean containsLoggedInUser(User u)
	{
		
		System.out.println(loggedInUserList.size());
		
		
		for(int x = 0; x < loggedInUserList.size(); x++)
		{
			if(u.nameMatches(loggedInUserList.get(x).name))
				return true;
		}
		return false;
	}
	
	public int getUserListSize()
	{
		return userList.size();
	}
	
	public int getMessageListSize()
	{
		return messageList.size();
	}
	
	public void setUserList(ArrayList<User> uL)
	{
		this.userList = uL;
	}
	
	public void setMessageList(ArrayList<Message> mL)
	{
		this.messageList = mL;
	}
	
	
	//menu options
	public void signInOption() throws IOException
	{
		osw.write("Log in:\r\n");
		osw.flush();
		
		if(!isLoggedIn)
		{
			osw.write("User name: ");
    		osw.flush();
    		uName = scanner.nextLine();
    		
    		osw.write("\r\nPassword: ");
    		osw.flush();
    		uPass = scanner.nextLine();
    		
    		tempUser = new User (uName, uPass);
    		if(containsLoggedInUser(tempUser))
    		{
    			osw.write("\r\nSorry, "+tempUser.name+" is already logged in.\r\n");
        		osw.flush();
    		}
    		else if(containsUser(tempUser) && tempUser.passwordMatches(uPass))
    		{
    			currentUser = tempUser;
    			isLoggedIn = true;
    			loggedInUserList.add(currentUser);
    			osw.write("\r\nLogged in as "+currentUser.name+".\r\n");
        		osw.flush();
    		}
    		else
    		{
    			osw.write("\r\nSorry, user name or password was incorrect. \r\n");
        		osw.flush();
    		}
		}
		else
		{
			osw.write("\r\nYou are already logged in as "+currentUser.name+ ".\r\n");
    		osw.flush();
		}
	}
	
	public void signUpOption() throws IOException
	{
		osw.write("Sign up:\r\n");
		osw.write("User name: ");
		osw.flush();
		uName = scanner.nextLine();
		osw.write("\r\nPassword: ");
		osw.flush();
		uPass = scanner.nextLine();
		
		tempUser = new User (uName, uPass);
		if(containsUser(tempUser))
		{
    		osw.write("\r\nSorry, this user already exists. \r\n");
    		osw.flush();
		}
		else
		{
			osw.write("\r\nUser "+tempUser.name+" added.\r\n");
    		osw.flush();
    		
    		setNewUser(tempUser);
		}
	}
	
	public void signOutOption() throws IOException
	{
		osw.write("Log out:\r\n");
		osw.flush();
		
		if(isLoggedIn)
		{
			logUserOut();
			
			osw.write("\r\nSigned out.\r\n");
    		osw.flush();
		}
		else
		{
			osw.write("\r\nSorry, you are not signed in.\r\n");
    		osw.flush();
		}
	}
	
	public void postMessageOption() throws IOException
	{
		if(isLoggedIn)
		{
			osw.write("Post message:\r\n");
			osw.flush();
			
			osw.write("\r\nMessage title: ");
			osw.flush();
			mTitle = scanner.nextLine();
			
			osw.write("\r\nMessage topic: ");
			osw.flush();
			mTopic = scanner.nextLine();
			
			osw.write("\r\nMessage content: ");
			osw.flush();
			mContent = scanner.nextLine();
			
			mDate = new Date();
			mAuthor = this.currentUser;
			
			setMessage(mContent, mTopic, mTitle, mAuthor, mDate);
			osw.write("\r\nMessage \""+ mTitle + "\" posted.\r\n");
			osw.flush();
		}
		else
		{
			osw.write("Please Sign in to post messages.\r\n");
			osw.flush();
		}
		
		
		
	}
	
	public void viewMessagesOption() throws IOException
	{
		
		if(isLoggedIn)
		{
			osw.write("Display messages:\r\n");
			osw.flush();
			
			if(messageList.size() < 10)
				displaySize = messageList.size();
			else
				displaySize = 10;
			
			if(displaySize == 0)
			{
				osw.write("No messages to display.\r\n");
				osw.flush();
			}
			else
			{
				for(int x = 0; x < displaySize; x++)
				{
					//this will probably show more details.
					osw.write((x+1) + " " + messageList.get(x).title + "\r\n");
				}
				osw.write("\r\n Please choose a message, 1 - "+ displaySize + "\r\n");
				osw.flush();
				
				option = scanner.nextLine();
				
				validChoice = false;
				for(int x = 0; x < displaySize; x++)
					if(Integer.parseInt(option) == x+1)
					{
						osw.write("\r\n"+messageList.get(x).displayString()+"\r\n");
						osw.flush();
						validChoice = true;
					}
				if(!validChoice)
				{
					invalidOption();
				}
			}
		}
		else
		{
			osw.write("Please Sign in to display messages.\r\n");
			osw.flush();
		}
	}
	
	public void viewAllUsersOption() throws IOException
	{
		osw.write("USERS:\r\n");
		osw.flush();
		for(int x = 0; x < userList.size(); x++)
		{
			osw.write(userList.get(x).name + "\r\n");
			osw.write(userList.get(x).password + "\r\n\r\n");

		}
	}
	
	public void viewActiveUsersOption() throws IOException
	{
		osw.write("LOGGED IN USERS:\r\n");
		osw.flush();
		for(int x = 0; x < loggedInUserList.size(); x++)
		{
			osw.write(loggedInUserList.get(x).name + "\r\n");
			osw.write(loggedInUserList.get(x).password + "\r\n\r\n");

		}
	}
	
	public void exitOption() throws IOException
	{
		logUserOut();
		osw.write("Goodbye.\r\n");
		osw.flush();
	}
	
	public void invalidOption() throws IOException
	{
		osw.write("\r\n Invalid entry.\r\n");
		osw.flush();
	}
	
	public int getLoggedInUserListSize()
	{
		return loggedInUserList.size();
	}
	
	public void setLoggedInUserList(ArrayList<User> list)
	{
		this.loggedInUserList = list;
	}
	
	public User getCurrentUser()
	{
		return this.currentUser;
	}
	
	public boolean hasLogout()
	{
		return hasLogout;
	}
	
	public void clearLogout()
	{
		hasLogout = false;
		userToLogOut = null;
	}
	
	public User getUserToLogOut()
	{
		return userToLogOut;
	}
	
	public void logUserOut() throws IOException
	{
		userToLogOut = currentUser;
		hasLogout = true;
		isLoggedIn = false;
		loggedInUserList.remove(loggedInUserList.indexOf(currentUser));
		
		currentUser = null;
	}
	
	

}
