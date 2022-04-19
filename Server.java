import java.net.*;
import java.io.*;
import java.util.HashMap;

public class Server {

	public static void main(String argv[]) throws Exception 
    	{ 
      		String clientSentence; 
      		String capitalizedSentence; 
		
		//HashMap<S

		ServerSocket welcomeSocket; 

		try{
      			welcomeSocket = new ServerSocket(6789); 
			System.out.println("Server succesfully created on Port 6789");
		


		 
			while(true) { 
	  
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println(); 

				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 


				DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream()); 
				capitalizedSentence = "";
				while((clientSentence = inFromClient.readLine()) != null)
				{
					System.out.println(clientSentence);
					capitalizedSentence += clientSentence;


				} 
				
	
				outToClient.writeBytes(capitalizedSentence);

				connectionSocket.close(); 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	} 

	
	public class connectRecord {

		SocketAddress clientIP; 



	}


}

