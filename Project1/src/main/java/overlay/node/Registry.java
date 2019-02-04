package overlay.node;

import overlay.transport.TCPServerThread;
import overlay.wireformats.Event;
import overlay.wireformats.LinkWeights;
import overlay.wireformats.MessagingNodesList;
import overlay.wireformats.Register;
import sun.security.x509.IPAddressName;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public int port;
    public Map<String, Socket> sockets;
    @Override
    public void onEvent(Event event){
        if(event instanceof Register) register((Register)event);

    }
    /*
    This constructor creates a TCPServerthread object,
    then wraps that in a thread object and starts the thread to begin server execution concurrently
    pass true to the serverObject so that it knows it is a registry
    pass this node into serverThread so it knows who its node is
     */

    public Registry(int port) throws IOException{
        this.sockets = new HashMap<String, Socket>();
        serverObject = new TCPServerThread(port, this);
        serverThread = new Thread(serverObject);
        serverThread.start();
        this.port = port;
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

    //handles the register event
    /*
    this is incomplete right now, need to remove print statements and add logic to send register response back
     */

    // TODO: 2/4/19
    public void register(Register registerRequest){
        System.out.println("here");
        Socket sock = registerRequest.socket;
        String ip = registerRequest.IPAddress;
        System.out.println(ip);
        sockets.put(ip,sock);
        System.out.println(ip + " " + registerRequest.port);
        System.out.println(sockets.get(ip));
    }

    /*
    Simple main method that will start the server then put the main thread into taking user input.
    Information about the server should probably be stored as class variables
    Handles exceptions here at the highest possible level
    */
    public static void main(String[] argv){
        if(argv.length < 1){
            System.out.println("specify port number");
            System.exit(1);
        }

        int port = Integer.parseInt(argv[0]);

        try {
            Registry thisMachinesRegistry = new Registry(port);
            thisMachinesRegistry.takeUserInput();
        }
        catch(IOException e){
            System.out.println("could not start server: " + e);
        }
    }
}
