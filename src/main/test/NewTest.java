import com.worldcretornica.plotme_core.api.event.Event;
import com.worldcretornica.plotme_core.api.event.eventbus.EventBus;
import com.worldcretornica.plotme_core.api.event.eventbus.Subscribe;
import org.junit.Test;

public class NewTest {

    @Test
    public void listen() {
        EventBus eventBus = new EventBus();
        eventBus.register(this);
        eventBus.post(new FakerEvent());
    }

    @Subscribe
    public void working(FakerEvent event) {
        System.out.println("faker event");
    }

    @Subscribe
    public void working(FakeEvent event) {
        System.out.println("fake event");
    }

    private class FakeEvent implements Event {

    }

    private class FakerEvent extends FakeEvent {

    }
}
