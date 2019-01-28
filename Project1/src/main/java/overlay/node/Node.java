package overlay.node;

import overlay.wireformats.Event;

public interface Node {

    public void onEvent(Event event);
}
