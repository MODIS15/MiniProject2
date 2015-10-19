import Interfaces.ISink;

import java.io.IOException;
import java.net.*;

public class Sink implements ISink {

    private Socket sinkSocket;

    public Sink(String IPAddress)
    {
        try {
            sinkSocket = new Socket(IPAddress, 7000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void display() {

    }

    @Override
    public void listen() {

    }

    @Override
    public void subscribe() {
        try {
            sinkSocket.getOutputStream().write(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean unsubscribe() {
        return false;
    }


    public static void main(String[] args)
    {
        System.out.println("Ready to sync... Enter Server IP Address...");
        Sink sink = new Sink(System.console().readLine());
    }
}
