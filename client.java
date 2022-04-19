import java.io.*;
import java.net.*;

public class client {

    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private BufferedReader userEntered = new BufferedReader(new InputStreamReader(System.in));
    
    public void startConnetcion(String ip, int port) {
        try{
            clientSocket = new Socket(ip, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
        } catch(Exception e){
            System.out.print(e);
        }
    }

    public String packet(String pkt){
        String recievedPkt;
        try{
            out.writeBytes(pkt);
            recievedPkt = in.readLine();
        }catch(Exception e){
            recievedPkt = e.toString();
        }
        return recievedPkt;
    }

    public void stopConnection(){
        String closeConnectionPkt = "";
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
}