/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kaspe
 */
public class ClientTest {

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static Thread server;

    public ClientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        server = new Thread(() -> {
            try {
                serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress("localhost", 7777));
                do {
                    Socket client = serverSocket.accept(); //Important Blocking call
                    try (PrintWriter outClient1 = new PrintWriter(client.getOutputStream());
                            BufferedReader inClient1 = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
                        String message = inClient1.readLine(); //IMPORTANT blocking call
                        outClient1.println("Welcome");
                    }
                    try {
                        client.close();
                    } catch (IOException ex) {
                    }
                } while (keepRunning);
            } catch (IOException ex) {
            }
        });
        server.start();

    }

    @AfterClass
    public static void tearDownClass() {
        keepRunning = false;

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void connect() throws IOException {
        Socket client = new Socket("localhost", 7777);
        Scanner input = new Scanner(client.getInputStream());
        PrintWriter output = new PrintWriter(client.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        new Thread ( () -> {
        String message = input.nextLine();
        Assert.assertEquals(message, "Welcome");
            
        }).start();
    }
    
    @Test
    public void send() throws IOException {
        Socket client = new Socket("localhost", 7777);
        Scanner input = new Scanner(client.getInputStream());
        PrintWriter output = new PrintWriter(client.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        new Thread ( () -> {
        String message = input.nextLine();
        Assert.assertEquals(message, "Welcome");
            
        }).start();
    }
    
}
