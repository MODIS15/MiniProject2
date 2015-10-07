import java.util.Collection;

/**
 * Created by FamilienMarstrand on 10/7/2015.
 */
public interface Server {
    //Collection<Sink> sinks;
    //Collection<Source> sources;

    void notify_sink(String message);
}
