package overlay.node;

import overlay.transport.TCPServerThread;
import overlay.wireformats.Event;

import java.io.IOException;

public class MessagingNode extends Node{

TCPServerThread serverObject;
Thread serverThread;

/*
This method will need to handle all events that can happen to a messaging node.
An exception should be thrown if the event if an event meant for a registry


 */
    @Override
    public void onEvent(Event event){

    }

        /*
    This constructor creates a TCPServerthread object,
    then wraps that in a thread object and starts the thread to begin server execution concurrently
    Pass false to the serverObject so it knows that it is a messenger
    pass this node into serverThread so it knows who its node is
    All commands should call to the server to make something happen
     */


    public MessagingNode() throws IOException{
        serverObject = new TCPServerThread(false, this);
        serverThread = new Thread(serverObject);
        serverThread.start();
    }
    /*
    This method is overrriden, it only allows commands to be taken that are applicable to a messenger node.
    Some commands will have an additional arguments that will need to be used when calling the appropriate action for each
    command.
     */
    @Override
    public String doCommand(String input) {
        String[] words = input.split("\\s+");
        switch(words[0]){
            case "test":
                return "Test message";
            case "print-shortest-path":
                return "Not implemented yet";
            case "exit-overlay":
                return "Not implemented yet";
            default:
                return "Command not recognized";
        }
    }
    /*
    Simple main method that will start the server then put the main thread into taking user input.
    Information about the server should probably be stored as class variables
    Handles exceptions here at the highest possible level
     */
    public static void main(String[] argv){
        try{
            MessagingNode thisMachinesNode = new MessagingNode();
            thisMachinesNode.takeUserInput();
         }
        catch(IOException e){
            System.out.println("could not start server: " + e);
        }
    }
}
