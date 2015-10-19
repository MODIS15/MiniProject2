public interface Server {
    //Collection<Sink> sinks;
    //Collection<Source> sources;

    void notifySink(String message);
}
