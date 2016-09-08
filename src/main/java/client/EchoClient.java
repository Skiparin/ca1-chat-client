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
    }

}
