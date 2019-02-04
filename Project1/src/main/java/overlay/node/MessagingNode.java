package overlay.node;

import overlay.transport.TCPSender;
import overlay.transport.TCPServerThread;
import overlay.wireformats.Event;
import overlay.wireformats.Register;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MessagingNode extends Node{
public String registryHostname;
public int registryPortnumber;
public TCPServerThread serverObject;
public Thread serverThread;

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
        this.sockets = new ArrayList<Socket>();
        int port = TCPServerThread.findOpenPort();
        serverObject = new TCPServerThread(port, this);
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
        if(argv.length < 2){
            System.out.println("Incorrect starting arguments");
            System.exit(1);
        }

        String registryHostname = argv[0];
        int registryPortnumber = Integer.parseInt(argv[1]);

        /*
        all the stuff that happens here on startup should be refactored.

        so for it sends a register request to the registry
         */
        // TODO: 2/4/19
        try{
            MessagingNode thisMachinesNode = new MessagingNode();
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = thisMachinesNode.serverObject.serverSocket.getLocalPort();
            System.out.println(ip + " " + port);
            Register request = new Register(ip, port);
            Socket regSock = new Socket(registryHostname,registryPortnumber);
            TCPSender regSender = new TCPSender(regSock);
            System.out.println("did stuff");
            regSender.sendData(request.eventData);



            thisMachinesNode.takeUserInput();
         }
        catch(IOException e){
            System.out.println("could not start server: " + e);
        }
    }
}
