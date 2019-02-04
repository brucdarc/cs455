package overlay.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/*
This is a class dedicated to sending data to across a socket.

The only thing we need to know here is that we are given the data in a
byte array.

Marshalling is abstracted to a higher level class.

*/
public class TCPSender {
    private Socket socket;
    private DataOutputStream dout;
    //this class is constructed with 1 socket, and 1 instance will handle
    //communication over just 1 specific socket
    public TCPSender(Socket socket) throws IOException{
        this.socket = socket;
        dout = new DataOutputStream(socket.getOutputStream());
    }

    /*
    synchronizing with the socket will make sure that only 1 socket sends messages at once.
    the same socket will still be able to recieve messages on that socket at the same time, because there
    is no synchronized block there.
     */

    public void sendData(byte[] dataToSend) throws IOException{
        synchronized (socket) {
            int dataLength = dataToSend.length;
            //output streams have various write method, we need to make sure
            //we write specifically the bits, and make sure none are cut off from an integer
            //because the receiving end needs to have a constant integer size to determine the length
            dout.writeInt(dataLength);
            //the 0 here represents the offset that we write the data with
            //we dont want an offset so it is set to 0
            dout.write(dataToSend, 0, dataLength);
            //the output stream needs to be flushed, to make sure all the data
            //is cleared out of buffers and sent through stream, and therefore
            //through the socket to another node
            dout.flush();
        }
    }





}
