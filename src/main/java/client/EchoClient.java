package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

public class EchoClient extends Observable {

    private Socket socket;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    private final AtomicBoolean stopped = new AtomicBoolean(true);

    public void connect(String address, int port) throws UnknownHostException, IOException {
        stopped.set(true);
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
    }

    public void send(String msg) {
        output.println(msg);
    }

    public void stop() throws IOException {
        stopped.set(true);
        output.println(ProtocolStrings.STOP);
    }

    public boolean isStopped() {
        return stopped.get();
    }

    public void receive() {
        while (true) {
            String msg = input.nextLine();
            if (msg.equals(ProtocolStrings.STOP)) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            setChanged();
            notifyObservers(msg);
        }
        //Why are we calling this recursively? There should be no need.
        //receive();
    }

    //Why do we still have a main here?
    public static void main(String[] args) {
        int port = 7777;
        String ip = "138.68.78.143";
        if (args.length == 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }

        //TODO: Can this be deleted?
//    try {
//      EchoClient tester = new EchoClient();      
//      tester.connect(ip, port);
//      System.out.println("Sending 'Hello world'");
//      tester.send("Hello World");
//      System.out.println("Waiting for a reply");
//      System.out.println("Received: " + tester.receive()); //Important Blocking call         
//      tester.stop();      
//      //System.in.read();      
//    } catch (UnknownHostException ex) {
//      Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
//    } catch (IOException ex) {
//      Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
//    }
    }
}
