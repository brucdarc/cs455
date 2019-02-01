package overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
this message will be sent from the registry to nodes to start the message sending protocol.
It tells each node how many messages to send.

this protocol has event type 6
 */
public class TaskInitiate extends Protocol{
    public int rounds;


    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalInt(rounds));
        return byteOut.toByteArray();
    }

    public TaskInitiate(byte[] data){
        this.eventData = data;
        this.eventType = demarshalInt(data);
        this.rounds = demarshalInt(data, 4 );
    }

    public TaskInitiate(int rounds) throws IOException{
        this.rounds = rounds;
        this.eventType = 6;
        this.eventData = this.marshal();

    }
}
