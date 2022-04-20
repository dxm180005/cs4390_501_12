import java.net.*;
import java.io.*;
import java.util.HashMap;

public class Server {

	public static void main(String argv[]) throws Exception 
    	{ 
      		String clientSentence; 
      		String response; 
		
		HashMap<String, ConnectRecord> connections = new HashMap<String, ConnectRecord>();

		ServerSocket welcomeSocket; 

		try{
      			welcomeSocket = new ServerSocket(1337); 
			System.out.println("Server succesfully created on Port 1337");
		


		 
			while(true) { 
	  
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println(); 

				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 


				DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream()); 
				response = "\n";
				clientSentence = "";	
				System.out.println("A: " + connectionSocket);				

				while((clientSentence = inFromClient.readLine()) != null)
				{
					
					//clientSentence = inFromClient.readLine();
					clientSentence = inFromClient.readLine();

					//if(clientSentence.equals("SIGTERM")) System.exit(1);
					
					System.out.println("IN WHILE LOOP");
					System.out.println(clientSentence);
					String[] fields = clientSentence.split("\\|");
					if(fields[0].equals("0"))
					{
						//String name = fields[1];
						if(connections.containsKey(fields[1])) response = "3|";
						connections.put(fields[1], new ConnectRecord(fields[1], connectionSocket.getRemoteSocketAddress()));
						System.out.println(connections);	
						response = "0|" + fields[1] + "|ACK\n";
						
						

					}
					else if(fields[0].equals("1"))
					{

						double n1 = Double.parseDouble(fields[2]);
						double n2 = Double.parseDouble(fields[4]);
						double ans = 0;
						String op = fields[3];
						if(op.contains("+")) ans = n1 + n2;
						else if(op.contains("-")) ans = n1 - n2;
						else if(op.contains("*")) ans = n1*n2;
						else if(op.contains("/")) ans = n1/n2;
						else {};

						response = "1|" + fields[1] + "|"  + ans + "\n"; 

					}
					else if(fields[0].equals("2"))
					{
						response = "2|" + fields[1] + "|ACK\n";
							
						ConnectRecord endR = connections.remove(fields[1]);
						endR.end();
						System.out.println(endR);
						break;
					}

					
					outToClient.writeBytes(response);
					


				} 
				
	

				connectionSocket.close(); 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	} 

	
	public static class ConnectRecord {

		SocketAddress clientIP; 
		String name;
		long start;
		long end;
		long duration;

		public ConnectRecord(String name, SocketAddress clientIP) {
			this.name = name;
			this.clientIP = clientIP;
			this.start = System.currentTimeMillis();
			this.end = 0;
			this.duration = 0;

		}
		
		public void end() {

			this.end = System.currentTimeMillis();
			this.duration = end - start;

		}

		@Override
		public String toString() {

			return "[" + name + ", " + clientIP + ", " + start + ", " + end + ", " + duration + "]";
		}
	}


}

