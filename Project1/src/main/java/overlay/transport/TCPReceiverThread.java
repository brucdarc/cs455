package overlay.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

//this class will run as a thread when the server needs to recieve data
//there will be a constructor that inputs a socket that will be used for communication
public class TCPReceiverThread implements Runnable{
    private Socket socket;
    private DataInputStream din;


    //this class will be instantiated for each incomming connection that want
    //to send data to the server this class runs on
    //somewhere outside of the class, the serversocket will spawn
    //a normal socket for a connection and create an instance of this
    //class with it, then run this class with another thread to allow
    //concurrency
    public TCPReceiverThread(Socket socket) throws IOException {
        this.socket = socket;
        din = new DataInputStream(socket.getInputStream());
    }

    //in all protocols, the first incomming int from the datastream will be its size
    //in the binary information comming across, we will always have the first 4 bytes
    //reserved for that int
    public void run(){

    }

}
