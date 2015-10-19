import Interfaces.IServer;
import sun.security.x509.IPAddressName;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements IServer {
    private ArrayList<Socket> sinks;
    private ArrayList<Socket> sources;
    private ServerSocket sinkSocket;
    private ServerSocket sourceSocket;



    public Server()
    {
        //Arrange
        sinks = new ArrayList<>();
        sources = new ArrayList<>();

        setSinks(); // Need to create a single thread
        setSources(); //Need to create a single thread


    }

    private void setSinks(){
        try {
            System.out.println("Waiting for connection...");
            sinkSocket = new ServerSocket(7000);
            while(true) {
                Socket s = sinkSocket.accept();
                sinks.add(s);
                System.out.println("Connection accepted " + s.getInetAddress());
                notifySink("Hej");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSources(){
        try {
            System.out.println("Waiting for connection from sources...");
            sinkSocket = new ServerSocket(7001);
            while(true) {
                Socket s = sourceSocket.accept();
                sources.add(s);
                //Need to create a method that listens to each source in a new thread! REMEMBER
                System.out.println("Connection accepted " + s.getInetAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void notifySink(String message) {
        for(Socket socket : sinks)
        {
            try {
                System.out.println("Printing");
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeUTF(message);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }



        }
    }


    public static void main(String[] args){
        Server server = new Server();
    }
}

