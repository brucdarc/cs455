package cs455.overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
/*
registry sends this message back to let a node know it is deregistered and can go offline


deregister has event type of 4
 */

public class DeregisterResponse extends Protocol{
    public byte statusCode;
    public String additionalInfo;

    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(statusCode);
        byteOut.write(marshalString(additionalInfo));
        return byteOut.toByteArray();
    }


    public DeregisterResponse(byte[] data){
        this.eventType = demarshalInt(data);
        this.statusCode = data[4];
        this.additionalInfo = demarshalString(data, 5);
        this.eventData = data;

    }

    public DeregisterResponse(byte statusCode, String additionalInfo) throws IOException{
        this.statusCode = statusCode;
        this.additionalInfo = additionalInfo;
        this.eventType = 4;
        this.eventData = this.marshal();
    }

}
