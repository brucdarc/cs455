package overlay.node;

import overlay.wireformats.Event;

public class MessagingNode extends Node{



/*
This method will need to handle all events that can happen to a messaging node.
An exception should be thrown if the event if an event meant for a registry


 */
    @Override
    public void onEvent(Event event){

    }

    @Override
    public String doCommand(String input) {
        return null;
    }
}
