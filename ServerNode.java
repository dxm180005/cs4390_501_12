import java.net.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;
import java.lang.Thread;

public class ServerNode extends Thread {
	String name;
	Socket connectionSocket;
	String clientSentence; 
	String response;
	ConnectRecord record;
	public static ReentrantLock lock = new ReentrantLock();

	public ServerNode(Socket connectionSocket) throws Exception 
	{ 
		this.name = "";
		this.connectionSocket = connectionSocket;
		this.clientSentence = "";
		this.response = "";
	}

	@Override
	public void run()
	{
		try{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			response = "\n";
			clientSentence = "";	
			System.out.println("A: " + connectionSocket);				

			while((clientSentence = inFromClient.readLine()) != null)
			{
				
				//clientSentence = inFromClient.readLine();
				//clientSentence = inFromClient.readLine();

				//if(clientSentence.equals("SIGTERM")) System.exit(1);
				
				System.out.println("IN WHILE LOOP");
				System.out.println(clientSentence);
				String[] fields = clientSentence.split("\\|");
				if(fields[0].equals("0"))
				{
					//String name = fields[1];
					this.name = fields[1];	
					this.record = new ConnectRecord(this.name, connectionSocket.getRemoteSocketAddress());	
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
					outToClient.writeBytes(response);
					outToClient.flush();
					record.end();
					break;
				}

				
				outToClient.writeBytes(response);
				outToClient.flush();


			} 
			


			connectionSocket.close(); 
		}
		catch(IOException e)
		{
			e.printStackTrace();
			super.interrupt();
		}
	}
	

	
	private static class ConnectRecord {

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
			//TODO Write out to file.
			
			ServerNode.lock.lock();
			
			
			
			ServerNode.lock.unlock();
			
		}

		@Override
		public String toString() {

			return "[" + name + ", " + clientIP + ", " + start + ", " + end + ", " + duration + "]";
		}
	}


}

