package cs455.overlay.transport;

import cs455.overlay.node.Node;
import cs455.overlay.util.EventQueue;
import cs455.overlay.wireformats.Deregister;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;
import cs455.overlay.wireformats.Register;

import javax.sound.midi.Soundbank;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

//this class will run as a thread when the server needs to recieve data
//there will be a constructor that inputs a socket that will be used for communication
public class TCPReceiverThread implements Runnable{
    public Node serversNode;
    private Socket socket;
    private DataInputStream din;


    //this class will be instantiated for each incomming connection that want
    //to send data to the server this class runs on
    //somewhere outside of the class, the serversocket will spawn
    //a normal socket for a connection and create an instance of this
    //class with it, then run this class with another thread to allow
    //concurrency
    public TCPReceiverThread(Socket socket, Node thisMachine) throws IOException {
        this.serversNode = thisMachine;
        this.socket = socket;
        din = new DataInputStream(socket.getInputStream());
    }

    /*
    In all protocols, the first incomming int from the datastream will be its size.

    In the binary information comming across, we will always have the first 4 bytes
    reserved for that int.

    This run method needs to take the incomming bytes, store them in an array, then pass it
    to an unmarshalling method

    The run method also does not allow you to throw exceptions, so we have to
    handle them inside it.

    When this class is instantiated as a thread, the run method is the entry point
    for code execution
    */
    public void run(){
        int dataLength;


        //make sure that the socket is still open, if it is closed our pointer to it
        //will point to a null object
        while(socket != null) {
            try {
                dataLength = din.readInt();

                byte[] data = new byte[dataLength];
                din.readFully(data, 0, dataLength);

                Event event = (EventFactory.makeEvent(data));

                if(event instanceof Register){
                    Register reg = (Register) event;
                    reg.socket = socket;
                    event = reg;
                }

                if(event instanceof Deregister){
                    Deregister reg = (Deregister) event;
                    reg.socket = socket;
                    event = reg;
                }

                serversNode.onEvent(event);


            }
            //two blocks that allow us to differentiate between different types of errors
            //useful in debugging
            /*
            get rid of the break statements before pushing final version, helpful for debugging, but
            server should not stop if it gets a socket error
             */
            catch (SocketException se){
                System.out.println(se.getMessage());
                break;
            }
            catch (IOException ioe){
                System.out.println(ioe.getMessage());
                break;
            }

            //here im going to need to do something with the byte array
            //I probably need to pass it back up to something else



        }

    }




}
