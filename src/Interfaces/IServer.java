package Interfaces;

import java.net.Socket;

public interface IServer {

    void notifySink(Socket source);
}
