package overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
this is a protocol for sending a message. depending on what node recieves this
it will either be at its final destination or be passed on.

this protocol has event type 7
 */
public class Message extends Protocol{
    String source;
    String destination;


    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalString(source));
        byteOut.write(marshalString(destination));
        return byteOut.toByteArray();
    }

    public Message(byte[] data){
        this.eventData = data;
        this.eventType = demarshalInt(data);
        this.source = demarshalString(data,4);
        this.destination = demarshalString(data, (source.length() + 8) );
    }

    public Message(String source, String dest) throws IOException{
        this.source = source;
        this.destination = dest;
        this.eventType = 7;
        this.eventData = this.marshal();

    }

}
