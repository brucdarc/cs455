package overlay.transport;

/*
This class will run the main serversocket, and spawn TCPReciever threads

Will need to scan ports to find an open port

 */

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TCPServerThread implements Runnable{

    boolean isRegistry;

    public TCPServerThread(boolean isRegistry){
        this.isRegistry = isRegistry;
    }


    //test the first 60000 ports to find a port that is open on the machine
    public ServerSocket findOpenPort() throws IOException {
        for( int portnumber = 1000; portnumber<60000; portnumber++){
            try (ServerSocket serverSocket = new ServerSocket(portnumber)) {
                return serverSocket;
            }
            catch (IOException e) {}


        }
        throw new IOException("No open port found on machine.");

    }


    public void run() {

    }
}
