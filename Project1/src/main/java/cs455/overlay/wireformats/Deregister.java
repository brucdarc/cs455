package cs455.overlay.wireformats;

import cs455.overlay.node.Registry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
This class will work on a registry object to deregister a connection

The register might need to send a response to tell the node it deregistered successfully, Im not sure at this time

deregister has event type of 3
*/
public class Deregister extends Protocol{
    public String IPAddress;
    public int port;
    public Socket socket;



    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalString(IPAddress));
        byteOut.write(marshalInt(port));
        return byteOut.toByteArray();
    }

    public Deregister(byte[] data){
        this.eventData = data;
        this.eventType = demarshalInt(data);
        this.IPAddress = demarshalString(data, 4);
        this.port = demarshalInt(data, (8 + IPAddress.length()) );
    }

    public Deregister (String IPAddress, int port) throws IOException{
        this.IPAddress = IPAddress;
        this.port = port;
        this.eventType = 3;
        this.eventData = this.marshal();

    }


}
