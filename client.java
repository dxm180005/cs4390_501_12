import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
	
	//defining socket and input/output streams along with scanner for user intput.
    private static Socket clientSocket;
    private static BufferedReader inToClient;
    private static DataOutputStream outToServer;
    private static Scanner userEntered = new Scanner(System.in);
    
    //function to start connection to server
    public static void startConnection(String ip, int port) {
        try{
        	//create the client socket, the output to server stream, and the in to client stream
            clientSocket = new Socket(ip, port);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inToClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
            //Print successful connection to terminal
            System.out.print("Connection Success on port "+ port +"\n");
        } catch(Exception e){
        	//Unsuccessful connection print and exit
            System.out.print(e);
            System.exit(1);
        }
    }
    
    //function to send and receive packet to and from server
    public static void packet(String pkt){
        String recievedPkt;
        try{
        	System.out.print("Sending Packet: " + pkt);
        	//send packet
            outToServer.writeBytes(pkt);
            //receive response
            recievedPkt = inToClient.readLine();
        }catch(Exception e){
            recievedPkt = e.toString();
        }
        //divide received packet into a string array to access type and body
        String[] splitPkt = recievedPkt.split("\\|");
        //if type is "1" then print received body
        //splitPkt[0] == type
        //splitPkt[1] == clientName
        //splitPkt[2] == body/pay-load
        if(splitPkt[0].equals("1")) {
        	System.out.println("Calculation: " + splitPkt[2] + "\n");
        }
        //if type is "2" then call stopConnectio() function with body
        if(splitPkt[0].equals("2")) {
        	stopConnection(splitPkt[2]);
        }
    }

    //function to close socket, input/output stream, and scanner and also terminate program
    public static void stopConnection(String ack){
        if(ack.equals("ACK")){
            try {
                inToClient.close();
                outToServer.close();
                clientSocket.close();
                userEntered.close();
                System.out.println("Connections closed, terminating client application");
                System.exit(0);
            } catch (Exception e) {
                System.out.print(e);
            }
        } else {
        	System.out.println("Error closing connections, terminating client application anyway");
        	System.exit(1);
        }
    }

    public static void main(String argv[]) throws Exception {
    	//starts connection to server on port 4390
    	startConnection("127.0.0.1", 4390);
    	System.out.print("Client Running");
    	//get the client name
    	System.out.print("Enter your client name: ");
    	String clientName = userEntered.nextLine();
    	//intialize body and type for packet
    	String body = "Open Connection";
    	int type = 0;
    	//send packet that tests connection and indicates successful connection
    	packet("0|" + clientName + "|" + body + "\n");
    	//runs until type is 2
    	while(type != 2){
    		//Print different packet types
    		System.out.println("Types: 1 => Calculation, 2 => close connection");
    		//expects input of int "1" or "2"
    		System.out.print("Enter type 1 or 2:");
    		type = userEntered.nextInt();
    		//if packet type is 1 then get mathematical arguments and operand
    		if(type == 1) {
    			//expects a single int or double
    			System.out.print("Enter first number: ");
    			int firstNumber = userEntered.nextInt();
    			//expects "+" || "-" || "*" || "/"
    			System.out.print("Enter an operand(+ - * /):");
    			String operand = userEntered.next();
    			//expects a single in or double
    			System.out.print("Enter second number: ");
    			int secondNumber = userEntered.nextInt();
    			//create body to send to server
    			body = firstNumber + "|" + operand + "|" + secondNumber;
    		}
    		//if packet is type 2 then print "Closing"
    		else if(type == 2) {
    			System.out.println("Closing");
    			body = "Closing";
    		}
    		//calls packet function to send constructed packet to server
    		packet(type + "|" + clientName + "|" + body + "\n");
    	}
    }
}