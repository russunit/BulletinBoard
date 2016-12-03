import java.io.IOException;
import java.net.Socket;

// TODO this class is a stub; Dan said he had a cch written so this is the placeholder.
// for my BBServer to work we need these 3 methods (including the constructor)


public class ClientConnectionHandler extends Thread {

	public ClientConnectionHandler(Socket clientConnection) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public boolean isConnected()  throws IOException
    {
        boolean stillConnected = true;
        stillConnected = isAlive();
        return stillConnected;
    }

}
