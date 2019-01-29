package overlay.node;

import overlay.wireformats.Event;
import overlay.wireformats.LinkWeights;
import overlay.wireformats.MessagingNodesList;
/*
This class is a dataStructure that contains the linkWeights and messagingNodesList

The overlayCreator will probably need to be called on an instance of this class

This node will need to be able to handle all events that can happen to a registry
If a registry gets an event meant for a messaging node, it should throw an exception


 */
public class Registry extends Node{
    private LinkWeights linkWeights;
    private MessagingNodesList messagingNodesList;

    @Override
    public void onEvent(Event event){

    }

    @Override
    public String doCommand(String input) {
        return null;
    }

    public MessagingNodesList getMessagingNodesList() {
        return messagingNodesList;
    }

    public LinkWeights getLinkWeights() {
        return linkWeights;
    }
}
