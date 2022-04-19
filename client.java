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
        }
    }

    public static String packet(String pkt){
        String recievedPkt;
        try{
        	System.out.print("Sending Packet: " + pkt);
            out.writeBytes(pkt);
            recievedPkt = in.readLine();
        }catch(Exception e){
            recievedPkt = e.toString();
        }
        return recievedPkt;
    }

    public static void stopConnection(){
        String closeConnectionPkt = "close";
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
    	startConnection("127.0.0.1", 1337);
    	String recievedPkt = packet("Test");
    	System.out.print("Packet recieved: " + recievedPkt);
    	
    	stopConnection();
    }
}