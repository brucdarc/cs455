package overlay.transport;

/*
This class will run the main serversocket, and spawn TCPReciever threads

Will need to scan ports to find an open port

 */

import overlay.node.Node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServerThread implements Runnable{

    boolean isRegistry;
    Node serversNode;
    ServerSocket serverSocket;

    /*
    The serverthread had a variable that tells it if it is a messenger or registry

    The constructor initializes this bool.
    The constructor also calls findOpenPort, which scans all ports and
    starts a serversocket on an open one
    the findopen port will initialize the serversocket class variable
     */
    public TCPServerThread(boolean isRegistry, Node serversNode) throws IOException{
        findOpenPort();
        this.isRegistry = isRegistry;
    }


    //test the first 60000 ports to find a port that is open on the machine
    //print out the port number so we know which port the server started on
    //return the serversocket object
    //set the serversocket class variable to be the serversocket that we just opened
    public ServerSocket findOpenPort() throws IOException {
        for( int portnumber = 1000; portnumber<60000; portnumber++){
            try {
                ServerSocket serverSocket = new ServerSocket(portnumber);
                System.out.println("Server started on port: " + portnumber);
                this.serverSocket = serverSocket;
                return serverSocket;
            }
            catch (IOException e){

            }




        }
        throw new IOException("No open port found on machine.");

    }

/*
The run method should keep the server busy in a while loop waiting to accept incomming connections
the serversocket accepts a connection, and creates a socket object that can be used for further communication
The serverThread here will spawn a reciever thread to handle communication with that particular node
 */
    public void run() {
        System.out.println(serverSocket);

        while(true){
            try{
                Socket socket = serverSocket.accept();
            }
            catch(IOException e){
                System.out.println("Could not accept incomming connection: exception: " + e);
            }
        }


    }
}
