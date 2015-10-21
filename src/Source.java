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
        System.out.println("Please input IP-address");
        ipAddress = System.console().readLine();

        portNumber = 7001;


        Source program = new Source();
        program.run();
    }


    //IdeaProjects/MiniProject2/src


    public  void run ()
    {
        try
        {
            while (true)
            {
                System.out.println("Type message and confirm with Enter");
                String userInput = System.console().readLine();
                inputInterpreter(userInput);
            }

           /*
            inputInterpreter("message1");
            inputInterpreter("message2");
            inputInterpreter("message3");
            */


        }
        catch (Exception e){System.out.println(e.getStackTrace());}
    }

    private  void inputInterpreter(String message)
    {
        message = message.trim().toLowerCase();

        switch (message)
        {

            case "exit":        disconnectFromServer();
                                running = false;
                                break;

            case "disconnect":  disconnectFromServer();
                                break;

            case "connect":     //Disconnecting from old server
                                disconnectFromServer();
                                //New setup
                                System.out.println("Change IP address to: ");
                                ipAddress = System.console().readLine();
                                System.out.println("Input port new portNumber¬l");
                                portNumber = Integer.parseInt(System.console().readLine());
                                //Connect to new server
                                connectToServer();
                                break;

            default:            connectToServer();
                                sendMessage(message);
                                disconnectFromServer();
                                break;
        }


    }

    public  void sendMessage(String message)
    {
        if(connected)
            try{
                dos.writeUTF(message);          // UTF is a string encoding see Sn. 4.4
                dos.flush();
                System.out.println("sending message: "+message);
            }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
            }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
            }catch (IOException e){System.out.println(e.getMessage());
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
        }
    }

    public  void disconnectFromServer()
    {
        try{
            dos.writeUTF("0");
            socket.close();
            socket = null;
            dis = null;
            dos = null;
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}