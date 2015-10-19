import Interfaces.ISink;

import java.io.*;
import java.net.*;

public class Sink implements ISink {

    private Socket sinkSocket;

    public Sink()
    {
        setOnTerminateEvent();
        initiate();
    }

    private void initiate(){
        try {
            String ipAddress = System.console().readLine();
            sinkSocket = new Socket(ipAddress, 7000); //auto-subscribe
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


    public void display(String message) {
        System.out.println(message);
    }

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

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe()
    {
        try {
            sinkSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
