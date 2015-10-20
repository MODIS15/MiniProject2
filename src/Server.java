import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Socket> sinks;
    private ServerSocket sinkSocket;
    private ServerSocket sourceSocket;



    public Server()
    {
        sinks = new ArrayList<>();

        //Threading
        Runnable runSinks = () -> {
            listenSinks();
        };
        Thread sinksThread = new Thread(runSinks);
        sinksThread.start();

        listenSources();
    }

    /**
     *The method starts an endless while-loop and waits for sockets to accept.
     *A connection is established to the socket and the socket is added to the collections of sinks.
     */

    private void listenSinks(){
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
            sourceSocket = new ServerSocket(7001);
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

    /**
     *Retrieves amessage from the source socket.
     *Goes through all sockets store din the list, sinks. If a socket is connected
     *@paramsource
     */


    private void notifySink(Socket source) {

        //Getting message from input stream
        DataInputStream message = null;
        try {message = new DataInputStream(source.getInputStream());}
        catch (IOException e) {e.printStackTrace();}
        if(message == null) return;

        //Distribute message to sinks
        DataOutputStream outputStream;
        for(Socket socket : sinks)
        {
            try {
                if(socket.isConnected())
                {
                    System.out.println("Notifying");
                    outputStream = new DataOutputStream(socket.getOutputStream());

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

