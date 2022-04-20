import java.io.*;
import java.net.*;

public class client {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static DataOutputStream out;
    private static BufferedReader userEntered = new BufferedReader(new InputStreamReader(System.in));
    
    public static void startConnection(String ip, int port) {
        try{
            clientSocket = new Socket(ip, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
            System.out.print("Connection Success on port 1337\n");
        } catch(Exception e){
            System.out.print(e);
            System.exit(1);
        }
    }

    public static String packet(String pkt){
        String recievedPkt;
        try{
        	System.out.print("Sending Packet: " + pkt);
            out.writeBytes(pkt);
            //out.flush();
            recievedPkt = in.readLine();
        }catch(Exception e){
            recievedPkt = e.toString();
        }
        System.out.print("Packet recieved: " + recievedPkt);
       
        return recievedPkt;
    }

    public static void stopConnection(){
        String closeConnectionPkt = "SIGTERM";
        String ack = packet(closeConnectionPkt);

        if(ack == "ackClose"){
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }
    }

    public static void main(String argv[]) throws Exception {
    	int destPort = 1337;
    	startConnection("127.0.0.1", destPort);
    	System.out.print("Enter your client name: ");
    	String clientName = userEntered.readLine();
    	String body = "Open Connection";
    	int type = 0;
    	packet("0|" + clientName + "|" + body + "\n");
    	while(type != 2){
    		System.out.println("Types: 1 => Calculation, 2 => close connection");
    		System.out.print("Enter type 1 or 2:");
    		type = userEntered.read();
    		if(type == 1) {
    			System.out.print("Enter simple calculation with spaces(i.e. 1 * 2):");
    			body = userEntered.readLine();
    		}
    		else if(type == 2) {
    			System.out.println("Closing");
    		}
    		packet(type + "|" + clientName + "|" + body + "\n");
    	}
    	
    	stopConnection();
    }
}