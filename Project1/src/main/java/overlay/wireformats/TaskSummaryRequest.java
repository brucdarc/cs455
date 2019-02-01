package overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
this is an empty message, (except for messagetype)
the registry sends this message to the nodes to ask for metadata on the sending messages to each other task that they
all just completed

event type 9
 */
public class TaskSummaryRequest extends Protocol{
    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        return byteOut.toByteArray();
    }

    /*
    there is not a demarshal method in protocol classes, rather the a constructor handles demarshalling
    if instanciated with a byte array.
     */
    public TaskSummaryRequest(byte[] data){
        this.eventData = data;
        this.eventType = demarshalInt(data);
    }

    public TaskSummaryRequest() throws IOException{
        this.eventType = 9;
        this.eventData = this.marshal();

    }
}
