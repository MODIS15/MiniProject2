import Interfaces.ISource;
import java.net.*;
import java.io.*;

import java.io.IOException;

public class Source implements ISource {

    static String ipAddress;
    static int portNumber;
    static boolean running, connected;


    static Socket socket;
    static DataInputStream dis;
    static DataOutputStream dos;

    public static void main(String[] args)
    {
        running = true;

        ipAddress = args[0];
        portNumber = Integer.parseInt(args[1]);

        //TO DO - Connect to server
        //connectToServer();
        //Running the program
        //run();
    }

    public  void run ()
    {
        try {
            while (running)
                messageHandeling(System.console().readLine());
        }
        catch (Exception e){System.out.println(e.getStackTrace());}
    }

    private  void messageHandeling(String message)
    {
        if (message.trim() == "exit")
        {
            disconnectFromServer();
            running = false;
        }
        else if(message.trim() == "disconnect"){disconnectFromServer();}
        else if (message.contains("connect"))
        {
            //Disconnecting from old server
            disconnectFromServer();
            //New setup
            String[] ipAndPort = message.split(" ");
            ipAddress = ipAndPort[1];
            portNumber = Integer.parseInt(ipAndPort[2]);
            //Connect to new server
            connectToServer();
        }
        else {sendMessage(message);}
    }

    public  void sendMessage(String message)
    {
        if(connected)
            try{
                dos.writeUTF(message);          // UTF is a string encoding see Sn. 4.4
            }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
            }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
            }catch (IOException e){System.out.println("readline:"+e.getMessage());
            }
        else
            System.out.println("Program is not connected, connect before sending messages!");
    }

    public  void connectToServer()
    {
        socket = null;
        try{
            socket = new Socket(ipAddress, portNumber);
            dis = new DataInputStream( socket.getInputStream());
            dos =new DataOutputStream( socket.getOutputStream());
            connected = true;
        }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch (IOException e){System.out.println("readline:"+e.getMessage());
        }finally {if(socket!=null) try {socket.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
    }

    public  void disconnectFromServer()
    {
        try{
            dos.writeUTF("0");
            socket = null;
            dis = null;
            dos = null;
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}