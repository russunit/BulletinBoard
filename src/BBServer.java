import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BBServer {
	
	private ServerSocket server;
    private Socket clientConnection;
    private ArrayList<ClientConnectionHandler> connections;
    private ArrayList<Message> messages;
    
    private int maxClients = 10;
    
    private int portNumber;
    
    public BBServer(int portNumber)
    {
    	server = null;
        clientConnection = null;
        this.portNumber = portNumber;
        connections = new ArrayList<ClientConnectionHandler>();
        messages = new ArrayList<Message>();
    }
    
    public void listen() throws IOException
    { 
        server = new ServerSocket(portNumber); 
    }
    
    public void acceptConnection() throws IOException
    {
        clientConnection = server.accept();
        ClientConnectionHandler cch = new ClientConnectionHandler(clientConnection);
        
        if (checkActiveConnections() < maxClients)
        {
            connections.add(cch);
            cch.start();
        }  
         
    }
    
    public void terminate() 
    {
        try
        {
            server.close();
        }
        catch(IOException e)
        {
            System.out.println("Error terminating connection");
        }
    }
    
    public int checkActiveConnections() throws IOException
    {
        //Check how many threads are still active
        ClientConnectionHandler cch = null;
        int activeConnections = 0; 
        
        for (int i = 0; i < connections.size(); i++)
        {
                cch = connections.get(i);
                if (cch.isConnected())
                    activeConnections++;
        }
        return activeConnections;
    }
    
    
    
    
    
    

	public static void main(String[] args) 
	{
		BBServer BBS = new BBServer(Integer.parseInt(args[0]));
		
		try
        {  
            //Make the server listen on the given port 
            BBS.listen();
            
            while (true)
            {
                //Wait until a client connects
                BBS.acceptConnection();
            }
            
            
        }
         catch (IOException e)
         {

             System.out.println("Unable to connect");
         }
        finally
        {
            BBS.terminate();
        }

	}

}
