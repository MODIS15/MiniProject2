import Interfaces.ISink;

import java.io.*;
import java.net.*;


/**
 * This class is responsible for receiving incoming messages from a given server,
 * through subscription.
 */
public class Sink implements ISink {

    private Socket sinkSocket;

    public Sink()
    {
        setOnTerminateEvent();
        initiate();
    }

    /**
     * Initialize a connection to a given host (IP-address and port)
     */
    private void initiate(){
        try {
            String ipAddress = System.console().readLine();
            sinkSocket = new Socket(ipAddress, 7000); //auto-subscribe when new connection is made.
            System.out.println("Connected to "+ ipAddress);
        }
        catch(UnknownHostException l)
        {
            System.out.println("Invalid IP. Please try again");
            initiate();
        }
        catch(IOException m)
        {
            m.printStackTrace();
            System.out.println("Please try again");
            initiate();
        }
    }

    /**
     * Display a given message in the console.
     * @param message
     */
    public void display(String message) {
        System.out.println(message);
    }

    /**
     * Listens for any messages sent from a host that the sink is subscribed to.
     */
    @Override
    public void listen() {
        System.out.println("Listening...");
        DataInputStream input;
        try {
            while(true)
            {
               input = new DataInputStream(sinkSocket.getInputStream());
               String s = input.readUTF();
               display(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close connection to host.
     */
    @Override
    public void unsubscribe()
    {
        try {
            sinkSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * EventHandler that closes the connection to a host when the program is terminated
     */
    private void setOnTerminateEvent()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           unsubscribe();
        }));
    }

    public static void main(String[] args)
    {
        System.out.println("Ready to sync... Enter Server IP Address...");
        Sink sink = new Sink();
        sink.listen();
    }
}
