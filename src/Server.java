import Interfaces.IServer;
import sun.security.x509.IPAddressName;

import java.util.ArrayList;

public class Server implements IServer {
    private ArrayList<IPAddressName> sinks;
    private ArrayList<IPAddressName> sources;

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
