import Interfaces.ISink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
            sinkSocket = new Socket(ipAddress, 7000);
            subscribe();
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
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(sinkSocket.getInputStream()));
            display(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe() {
        try {
            OutputStream outputStream = sinkSocket.getOutputStream();
            outputStream.write(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe()
    {
        try {
            OutputStream outputStream = sinkSocket.getOutputStream();
            outputStream.write(0);
            outputStream.close();
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
        while(true) {
            sink.listen();
        }
    }
}
