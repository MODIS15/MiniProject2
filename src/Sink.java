import Interfaces.ISink;

import java.net.DatagramSocket;

public class Sink implements ISink {

    private DatagramSocket socket;

    public Sink()
    {
        socket
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
