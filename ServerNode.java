import java.net.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;
import java.lang.Thread;

public class ServerNode extends Thread {
	
	public static ReentrantLock crfilelock = new ReentrantLock(); //Lock used to make file writes thread safe
	public static final String crfilename = "connection_records.log"; //filename to log to
	
	String name; //name of the user
	Socket connectionSocket; //established connection with the server
	ConnectRecord record; //Record logging useful connection information
	
	
	/**
	 * Constructs a ServerNode with a connection instance
	 * 
	 * @param connectionSocket - A single user's connection to the established server
	 * 
	 */
	public ServerNode(Socket connectionSocket) 
	{ 
		this.name = "";
		this.connectionSocket = connectionSocket;
	}
	
	/**
	 *  Server Thread function which processes a user's packets and returns the desired result.
	 *  
	 *  User's can establish a connection, perform a calculation, and close the connection.
	 *  
	 */
	@Override
	public void run()
	{
		String clientSentence = ""; //temp variable for holding the client packet
		String response = "\n"; //temp variable for holding the server response
		try{
			
			//Establish IO Stream parsers
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			/* MAIN LOOP
			 * All packets are one-line.
			 * Reads in a packet, processes it, and waits for the next packet.
			 */
			while((clientSentence = inFromClient.readLine()) != null)
			{
				
				/* Parse the packet
				 * Each packet is one-line with arguments delimited by the pipeline "|"
				 */
				String[] fields = clientSentence.split("\\|");
				
				/*
				 * If the packet is Type 0, then its a connection start packet.
				 */
				if(fields[0].equals("0"))
				{
					
					this.name = fields[1]; //set the name
					this.record = new ConnectRecord(this.name, connectionSocket.getRemoteSocketAddress()); //initialize log info
					
					//set response to ACK the start of the connection
					response = "0|" + fields[1] + "|ACK\n";
					
					

				}
				
				/*
				 * If the packet is Type 1, then its a calculation request.
				 */
				else if(fields[0].equals("1"))
				{
					/*	
					 *	Parse the packet into the operator and operands
					 */
					double n1 = Double.parseDouble(fields[2]);
					double n2 = Double.parseDouble(fields[4]);
					
					/*
					 * Interpret the operand, and perform the associated operation on the operands, and store in ans.
					 */
					double ans = 0;
					String op = fields[3];
					if(op.contains("+")) ans = n1 + n2;
					else if(op.contains("-")) ans = n1 - n2;
					else if(op.contains("*")) ans = n1*n2;
					else if(op.contains("/")) ans = n1/n2;
					else {};
					
					//set the response to return the result of the calculation
					response = "1|" + fields[1] + "|"  + ans + "\n"; 

				}
				
				/*
				 * If the packet is Type 2, then its connection close packet
				 */
				else if(fields[0].equals("2"))
				{
					
					//set the response to ACK the connection close
					response = "2|" + fields[1] + "|ACK\n";
					
					//output the response
					outToClient.writeBytes(response);
					outToClient.flush();
					
					//Indicate to the user info logger that the connection has ended.
					record.end();
					
					//break out of the main loop
					break;
				}

				//Send the response to the user
				outToClient.writeBytes(response);
				outToClient.flush();


			} 
			

			//If we gracefully broke out of the loop
			//then the user wants to close the socket
			//so we close the socket
			connectionSocket.close(); 
		}
		catch(IOException e)
		{
			/*
			 * If error, output error, close the connection, then interrupt the current thread
			 */
			e.printStackTrace();
			try {
				connectionSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			super.interrupt();
		}
	}
	

	/**
	 * 
	 * A helper class to log User Connection Information to a file.
	 *
	 */
	private static class ConnectRecord {

		SocketAddress clientIP; //User's IP Address and port
		String name; //User name
		long start; //EpochTime when connection was started
		long end; //EpochTime when connection was closed
		long duration; //How long the connection was open

		/**
		 * Constructs a User Connection Record
		 * @param name - username
		 * @param clientIP - user IP Address and port
		 */
		public ConnectRecord(String name, SocketAddress clientIP) {
			this.name = name;
			this.clientIP = clientIP;
			this.start = System.currentTimeMillis(); //set connection start time to now
			this.end = 0;
			this.duration = 0;

		}
		
		public void end() {
			
			this.end = System.currentTimeMillis(); //set connection end time to now
			this.duration = end - start; //Calculate duration
			//TODO Write out to file.
			
			//Acquire the File lock to ensure thread safe file writes
			ServerNode.crfilelock.lock();
			
			/*
			 * Write the contents of this record class to the logging file.
			 * See toString() for information on what content is output.
			 */
			try {
		      FileWriter logout = new FileWriter(ServerNode.crfilename, true);
		      logout.write(toString() + "\n"); //call native content to string function
		      logout.close();
		    } catch (IOException e) {
		 
		      e.printStackTrace();
		      
		    }
			
			//Release the lock
			ServerNode.crfilelock.unlock();
			
		}

		/**
		 * Outputs username, user-ip, start-time, end-time, and connection duration of user connection as a formatted string.
		 * @return String - [tag:value, ...]
		 */
		@Override
		public String toString() {

			return "[Username: " + name + ", User-IP:" + clientIP + ", Start-Time(EpochTime in ms):" + start + ", End-Time(EpochTime in ms):" + end + ", Duration(in ms):" + duration + "]";
		}
	}


}

