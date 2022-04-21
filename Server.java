import java.net.*;

public class Server {
	
	/**
		This function creates and runs a calculation server.
		
		New Threads are created for each user connection.
		New Threads are using ServerNode.java as the Thread class.
	*/
	public static void main(String argv[]) throws Exception 
	{ 
      		

		ServerSocket welcomeSocket; 

		try{
			
			//create socket that redirects incoming packets to the correct thread
			welcomeSocket = new ServerSocket(4390); 
			System.out.println("Server succesfully created on Port 4390");
		


			//main server loop
			//constantly loops to accept new packets
			while(true) 
			{ 
				//accept a packet
				Socket connectionSocket = welcomeSocket.accept(); 
				
				//Start a new thread for the new connection
				ServerNode connection = new ServerNode(connectionSocket);
				connection.start();

			} 

		}
		catch(Exception e)
		{
			//print error and exit with errors
			e.printStackTrace();
			System.exit(1);
		} 
	}


}

