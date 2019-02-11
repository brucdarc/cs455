package cs455.overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
This messagge will be sent from a messaging node to the registry when the messaging node is finished sending messages
after a command to do so.
The variables are to help the registry identify which node this is.

this protocol has event id type 8
 */
public class TaskComplete extends Protocol{
    public String nodeIPAddress;
    public int nodePortNumber;

    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalString(nodeIPAddress));
        byteOut.write(marshalInt(nodePortNumber));
        return byteOut.toByteArray();
    }

    public TaskComplete(byte[] data){
        this.eventData = data;
        this.eventType = demarshalInt(data);
        this.nodeIPAddress = demarshalString(data,4);
        this.nodePortNumber = demarshalInt(data, (nodeIPAddress.length() + 8) );
    }

    public TaskComplete(String ip, int port) throws IOException{
        this.nodeIPAddress = ip;
        this.nodePortNumber = port;
        this.eventType = 8;
        this.eventData = this.marshal();

    }
}
