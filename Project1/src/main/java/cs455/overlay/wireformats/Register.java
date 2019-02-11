package cs455.overlay.wireformats;

import cs455.overlay.node.Registry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
/*
This class will work on a registry object to register a connection
The registry will then need to add it to the overlay, and send a register response message back to tell the node
whether it was able to successfully register or not.

Register has the event type of 1

 */

public class Register extends Protocol{
    public String IPAddress;
    public int port;
    public Socket socket;

    //this method will need to second argument of what to register
    public void register(Registry registry){


    }
    /*
    This method takes the current object and converts it into a byte array
    Throws an exception because there may be problems writing to the byte out stream
     */

    public byte[] marshal()throws IOException{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalString(IPAddress));
        byteOut.write(marshalInt(port));
        return byteOut.toByteArray();
    }

    /*
    there is not a demarshal method in protocol classes, rather the a constructor handles demarshalling
    if instanciated with a byte array.
     */
    public Register(byte[] data){
        this.eventData = data;
        this.eventType = demarshalInt(data);
        this.IPAddress = demarshalString(data, 4);
        this.port = demarshalInt(data, (8 + IPAddress.length()) );
    }

    public Register (String IPAddress, int port) throws IOException{
        this.IPAddress = IPAddress;
        this.port = port;
        this.eventType = 1;
        this.eventData = this.marshal();

    }

    public boolean equals(Register other){
        if(this.IPAddress.equals(other.IPAddress) && this.port == other.port && this.eventType == other.eventType)
            return true;
        else return false;

    }

}
