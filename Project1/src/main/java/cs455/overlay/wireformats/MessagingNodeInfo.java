package cs455.overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
this is a class that describes a messaging node by its ip and portnumber
currently it is used in the messagingNodesList protocol to communicate to
a node what peers is has with a list of these.

This class is NOT a protocol
 */
public class MessagingNodeInfo{
    public String nodeHostName;
    public int nodePortnum;


    public MessagingNodeInfo( String nodeHostName, int nodePortnum){
        this.nodeHostName = nodeHostName;
        this.nodePortnum = nodePortnum;
    }

    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(Protocol.marshalString(nodeHostName));
        byteOut.write(Protocol.marshalInt(nodePortnum));
        return byteOut.toByteArray();
    }


}
