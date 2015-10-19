import Interfaces.IServer;

import java.util.ArrayList;

public class Server implements IServer {
    private ArrayList<String> sinks;
    private ArrayList<String> sources;

    public Server()
    {
        //Arrange
        sinks = new ArrayList<>();
        sources = new ArrayList<>();
    }

    @Override
    public void notifySink(String message) {

    }
}
