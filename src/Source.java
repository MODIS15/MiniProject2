public interface Source
{
    void sendMessage(String message);

    void connectToServer();

    void disconnectFromServer();
}

