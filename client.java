import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {

    private static Socket clientSocket;
    private static BufferedReader inToClient;
    private static DataOutputStream outToServer;
    private static Scanner userEntered = new Scanner(System.in);
    
    public static void startConnection(String ip, int port) {
        try{
            clientSocket = new Socket(ip, port);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inToClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
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
            outToServer.writeBytes(pkt);
            recievedPkt = inToClient.readLine();
        }catch(Exception e){
            recievedPkt = e.toString();
        }
        System.out.println("Packet recieved: " + recievedPkt);
        String[] splitPkt = recievedPkt.split("|");
        String returnMe = splitPkt[2];
        return returnMe;
    }

    public static void stopConnection(String clientName){
        String closeConnectionPkt = "2|" + clientName + "|Closing";
        String ack = packet(closeConnectionPkt);

        if(ack == "ACK"){
            try {
                in.close();
                outToServer.close();
                clientSocket.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }
    }

    public static void main(String argv[]) throws Exception {
    	startConnection("127.0.0.1", 1337);
    	System.out.print("Enter your client name: ");
    	String clientName = userEntered.nextLine();
    	String body = "Open Connection";
    	int type = 0;
    	packet("0|" + clientName + "|" + body + "\n");
    	while(type != 2){
    		System.out.println("Types: 1 => Calculation, 2 => close connection");
    		System.out.print("Enter type 1 or 2:");
    		type = userEntered.nextInt();
    		if(type == 1) {
    			System.out.print("Enter first number: ");
    			int firstNumber = userEntered.nextInt();
    			System.out.print("Enter an operand(+ - * /):");
    			String operand = userEntered.next();	
    			System.out.print("Enter second number: ");
    			int secondNumber = userEntered.nextInt();
    			body = firstNumber + "|" + operand + "|" + secondNumber;
    		}
    		else if(type == 2) {
    			System.out.println("Closing");
    		}
    		System.out.print(type + "|" + clientName + "|" + body + "\n");
    		packet(type + "|" + clientName + "|" + body + "\n");
    	}
    	
    	stopConnection(clientName);
    }
}