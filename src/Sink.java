public interface Sink
{
    void display();

    void listen();

    boolean subscribe();

    boolean unsubscribe();
}