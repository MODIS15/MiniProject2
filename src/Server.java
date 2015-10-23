import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The server class is responsible for notifying all connected sinks whenever a
 * source or sources have published a message
 */
public class Server {
    private ArrayList<DataOutputStream> sinks;
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
                sinks.add(new DataOutputStream(s.getOutputStream()));
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

                Runnable sinkrunnable = () -> {
                    notifySink(s);
                };
                Thread sinkThread = new Thread(sinkrunnable);
                sinkThread.start();

                System.out.println("Waiting for connection from sources...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        //Getting input stream
        DataInputStream dataInputStream = null;
        try {dataInputStream = new DataInputStream(source.getInputStream());}
        catch (IOException e) {e.printStackTrace();}
        if(dataInputStream == null) return;

        //Distribute message to sinks
        try {
            DataOutputStream outputStream;
            String message = dataInputStream.readUTF();

            for(DataOutputStream stream : sinks)
            {
                try {
                    PrintWriter out = new PrintWriter(stream, true);
                    if(!out.checkError())
                    {
                        System.out.println("Notifying");
                        stream.writeUTF(message);
                    }
                    else
                    {
                        System.out.println("No connection was found and was removed");
                        sinks.remove(stream);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            System.out.println("Notified");



        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args){
        Server server = new Server();
    }
}

