import java.net.*;

public class Server {

	public static void main(String argv[]) throws Exception 
	{ 
      		

		ServerSocket welcomeSocket; 

		try{
      		welcomeSocket = new ServerSocket(4390); 
			System.out.println("Server succesfully created on Port 4390");
		


		 
			while(true) 
			{ 
	  
				Socket connectionSocket = welcomeSocket.accept();
				ServerNode connection = new ServerNode(connectionSocket);
				connection.start();

			} 

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		} 
	}


}

