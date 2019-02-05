package overlay.node;

import overlay.transport.TCPReceiverThread;
import overlay.transport.TCPSender;
import overlay.transport.TCPServerThread;
import overlay.wireformats.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MessagingNode extends Node{
    public String myHostname;
    public int myPort;
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
        if(event instanceof RegisterResponse)  handleRegistryResponse((RegisterResponse)event);
        if(event instanceof DeregisterResponse)  handleDeregistryResponse((DeregisterResponse)event);
    }

        /*
    This constructor creates a TCPServerthread object,
    then wraps that in a thread object and starts the thread to begin server execution concurrently
    Pass false to the serverObject so it knows that it is a messenger
    pass this node into serverThread so it knows who its node is
    All commands should call to the server to make something happen
     */


    public MessagingNode(String registryHostname, int registryPortnumber) throws IOException{
        this.sockets = new ArrayList<Socket>();
        this.registryHostname = registryHostname;
        this.registryPortnumber = registryPortnumber;
        int port = TCPServerThread.findOpenPort();
        serverObject = new TCPServerThread(port, this);
        serverThread = new Thread(serverObject);
        serverThread.start();
        myHostname = InetAddress.getLocalHost().getHostAddress();
        myPort = serverObject.serverSocket.getLocalPort();
    }
    /*
    This method is overrriden, it only allows commands to be taken that are applicable to a messenger node.
    Some commands will have an additional arguments that will need to be used when calling the appropriate action for each
    command.
     */
    @Override
    public String doCommand(String input) throws IOException{
        String[] words = input.split("\\s+");
        switch(words[0]){
            case "test":
                return "Test message";
            case "print-shortest-path":
                return "Not implemented yet";
            case "exit-overlay":
                return "Not implemented yet";
            case "register":
                sendRegisterRequest();
                return "sent registration request";
            case "deregister":
                sendDeregisterRequest();
                return "sent deregister request";
            default:
                return "Command not recognized";
        }
    }

    /*
    This method sends the register request request to the registry
     */

    public void sendRegisterRequest() throws IOException{
        String ip = myHostname;
        int port = myPort;
        Register request = new Register(ip, port);
        Socket regSock = new Socket(registryHostname,registryPortnumber);
        TCPReceiverThread receiver = new TCPReceiverThread(regSock, this);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
        TCPSender regSender = new TCPSender(regSock);
        regSender.sendData(request.eventData);

    }
    //handles registry response. tells the node that if registered correctly or incorrectly
    public void handleRegistryResponse(RegisterResponse registerResponse){
        if(registerResponse.statusCode == 1){
        System.out.println("Could not register: " + registerResponse.additionalInfo);
        }
        else System.out.println("Registered successfully: " + registerResponse.additionalInfo);

    }

    /*
    send a deregister request to registry to deregister this node
     */

    public void sendDeregisterRequest() throws IOException{
        String ip = myHostname;
        int port = myPort;
        Deregister request = new Deregister(ip, port);
        Socket regSock = new Socket(registryHostname,registryPortnumber);
        TCPReceiverThread receiver = new TCPReceiverThread(regSock, this);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
        TCPSender regSender = new TCPSender(regSock);
        regSender.sendData(request.eventData);
    }

    /*

     */
    //handles registry response. tells the node that if registered correctly or incorrectly
    public void handleDeregistryResponse(DeregisterResponse deregisterResponse){
        if(deregisterResponse.statusCode == 1){
            System.out.println("Could not deregister: " + deregisterResponse.additionalInfo);
        }
        else System.out.println("Deregistered successfully: " + deregisterResponse.additionalInfo);

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

        handles respose
         */
        // TODO: 2/4/19
        try{
            MessagingNode thisMachinesNode = new MessagingNode(registryHostname,registryPortnumber);
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = thisMachinesNode.serverObject.serverSocket.getLocalPort();
            System.out.println("Server running on: " + ip + " " + port);
            thisMachinesNode.sendRegisterRequest();
            thisMachinesNode.takeUserInput();
         }
        catch(IOException e){
            System.out.println("could not start server: " + e);
        }
    }
}
