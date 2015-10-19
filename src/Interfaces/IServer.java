package Interfaces;

public interface IServer {
    //Collection<Interfaces.ISink> sinks;
    //Collection<Interfaces.ISource> sources;

    void notifySink(String message);
}
