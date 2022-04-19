import java.net.*;
import java.io.*;

public class Server {

	public static void main(String argv[]) throws Exception 
    	{ 
      		String clientSentence; 
      		String capitalizedSentence; 
		
		ServerSocket welcomeSocket; 

		try{
      			welcomeSocket = new ServerSocket(6789); 
			System.out.println("Server succesfully created on Port 6789");
		


		 
			while(true) { 
	  
				Socket connectionSocket = welcomeSocket.accept(); 

				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 


				DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream()); 

				while((clientSentence = inFromClient.readLine()) != null)
				{
					System.out.println(clientSentence);
				


				} 

				//outToClient.writeBytes(capitalizedSentence); 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 
    } 






}

