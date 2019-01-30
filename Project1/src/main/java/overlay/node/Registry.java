package overlay.node;

import overlay.transport.TCPServerThread;
import overlay.wireformats.Event;
import overlay.wireformats.LinkWeights;
import overlay.wireformats.MessagingNodesList;

import java.io.IOException;

/*
This class is a dataStructure that contains the linkWeights and messagingNodesList

The overlayCreator will probably need to be called on an instance of this class

This node will need to be able to handle all events that can happen to a registry
If a registry gets an event meant for a messaging node, it should throw an exception


 */
public class Registry extends Node{
    private LinkWeights linkWeights;
    private MessagingNodesList messagingNodesList;
    private TCPServerThread serverObject;
    private Thread serverThread;

    @Override
    public void onEvent(Event event){

    }
    /*
    This constructor creates a TCPServerthread object,
    then wraps that in a thread object and starts the thread to begin server execution concurrently
    pass true to the serverObject so that it knows it is a registry
    pass this node into serverThread so it knows who its node is
     */

    public Registry() throws IOException{
        serverObject = new TCPServerThread(true, this);
        serverThread = new Thread(serverObject);
        serverThread.start();
    }

    /*
    This method is overriden, and should only take commands that are applicable to a registry node
    All commands should call to the server to make something happen
     */
    @Override
    public String doCommand(String input) {
        String[] words = input.split("\\s+");
        switch(words[0]){
            case "test":
                return "Test message";
            case "list-messaging-nodes":
                return "Not implemented yet";
            case "list-weights":
                return "Not implemented yet";
            case "send-overlay-link-weights":
                return "Not implemented yet";
            case "setup-overlay":
                return "Not implemented yet";
            case "start":
                return "Not implemented yet";
            default:
                return "Command not recognized";

        }
    }
    //accessor method
    public MessagingNodesList getMessagingNodesList() {
        return messagingNodesList;
    }
    //accessor method
    public LinkWeights getLinkWeights() {
        return linkWeights;
    }

    /*
    Simple main method that will start the server then put the main thread into taking user input.
    Information about the server should probably be stored as class variables
    Handles exceptions here at the highest possible level
    */
    public static void main(String[] argv){
        try {
            Registry thisMachinesRegistry = new Registry();
            thisMachinesRegistry.takeUserInput();
        }
        catch(IOException e){
            System.out.println("could not start server: " + e);
        }
    }
}
