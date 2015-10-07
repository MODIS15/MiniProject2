/**
 * Created by FamilienMarstrand on 10/7/2015.
 */
public interface Source
{
    void sendMessage(String message);

    void connectToServer();

    void disconnectFromServer();
}

