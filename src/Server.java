import Interfaces.IServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements IServer {
    private ArrayList<Socket> sinks;
    private ServerSocket sinkSocket;
    private ServerSocket sourceSocket;



    public Server()
    {
        sinks = new ArrayList<>();

        //Threading
        Runnable runSinks = () -> {
            setSinks();
        };
        Thread sinksThread = new Thread(runSinks);
        sinksThread.start();

        listenSources();
    }

    private void setSinks(){
        try {
            System.out.println("Waiting for connection...");
            sinkSocket = new ServerSocket(7000);
            while(true) {
                Socket s = sinkSocket.accept();
                sinks.add(s);
                System.out.println("Connection accepted " + s.getInetAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenSources(){
        try {
            System.out.println("Waiting for connection from sources...");
            sinkSocket = new ServerSocket(7001);
            while(true) {
                Socket sourceSocket = this.sourceSocket.accept();
                System.out.println("Connection from source: " + sourceSocket.getInetAddress()+ " was established.");

                notifySink(sourceSocket);
                sourceSocket.close();
                System.out.println("Waiting for connection from sources...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void notifySink(Socket source) {

        for(Socket socket : sinks)
        {
            try {
                if(socket.isConnected())
                {
                    System.out.println("Notifying");
                    DataInputStream message = new DataInputStream(source.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                    outputStream.writeUTF(message.readUTF());

                }
                else
                {
                    System.out.println("No connection was found for "+ socket.getInetAddress().toString()+ " and was removed");
                    sinks.remove(socket);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Notified");
    }




    public static void main(String[] args){
        Server server = new Server();
    }
}

