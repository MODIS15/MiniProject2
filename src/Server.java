import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * The server class is responsible for notifying all connected sinks whenever a
 * source or sources have published a message
 */
public class Server {
    private ArrayList<Socket> sinks;
    private ServerSocket sinkSocket;
    private ServerSocket sourceSocket;

    public Server()
    {
        sinks = new ArrayList<>();

        //Threading
        Runnable runSinks = () -> listenSinks();
        Thread sinksThread = new Thread(runSinks);
        sinksThread.start();

        listenSources();
    }

    /**
     *The method is constantly listening for sockets to accept.
     *When a connection is established to a socket, the socket is added to the collections of sinks.
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
                Socket s = this.sourceSocket.accept();
                System.out.println("Connection from source: " + s.getInetAddress() + " was established.");
                getStreamFromSource(s);

                s.close();
                System.out.println("Waiting for connection from sources...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve stream from a connected source and notify sinks for every message in the stream.
     * The connection is maintained for 15 seconds
     * @param sourceSocket
     */
    private void getStreamFromSource(Socket sourceSocket) {
        Runnable setTimeout = () -> {
            try {
                int timeout = 15000;
                sourceSocket.setSoTimeout(timeout);
                while(true){
                    notifySink(sourceSocket);
                }
            } catch (SocketException e)
            {
                try {
                    sourceSocket.close();
                    System.out.println("Timeout for: "+sourceSocket.getInetAddress().toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
        Thread sourceTimeoutThread = new Thread(setTimeout);
        sourceTimeoutThread.start();
    }

    /**
     *Retrieves a message from the source socket.
     *Goes through all sockets stored in the list of sinks. If a socket is connected it
     *
     *If there is no connection to the sink, then the sink has unsubscribed. The sink
     *is then removed from the list of sinks.
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
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                if(!out.checkError())
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

