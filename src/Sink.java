import Interfaces.ISink;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Sink implements ISink {

    private DatagramSocket inputSocket;
    private DatagramSocket outputSocket;


    public Sink()
    {
        try
        {
            inputSocket = new DatagramSocket(7000);
            outputSocket = new DatagramSocket(7001);
        }
        catch (SocketException e)
        {
            System.out.println("Datagram Socket creation failed...");
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
    public boolean subscribe() {
        return false;
    }

    @Override
    public boolean unsubscribe() {
        return false;
    }
}
