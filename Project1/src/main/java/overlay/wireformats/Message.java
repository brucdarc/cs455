package overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
this is a protocol for sending a message. depending on what node recieves this
it will either be at its final destination or be passed on.

this protocol has event type 7
 */
public class Message extends Protocol{
    public String source;
    public String destination;
    public int communicatedValue;


    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalString(source));
        byteOut.write(marshalString(destination));
        /*
        have to do marshal int to make sure it gets properly padded with 0s and is a whole int.
        if you just write the int and it doesnt need extra bytes it wont write them
         */
        byteOut.write(marshalInt(communicatedValue));
        return byteOut.toByteArray();
    }

    public Message(byte[] data){
        this.eventData = data;
        this.eventType = demarshalInt(data);
        this.source = demarshalString(data,4);
        this.destination = demarshalString(data, (source.length() + 8) );
        this.communicatedValue = demarshalInt(data, (source.length() + 12 + destination.length() ));
    }

    public Message(String source, String dest, int communicatedValue) throws IOException{
        this.source = source;
        this.destination = dest;
        this.communicatedValue = communicatedValue;
        this.eventType = 7;
        this.eventData = this.marshal();

    }

}
