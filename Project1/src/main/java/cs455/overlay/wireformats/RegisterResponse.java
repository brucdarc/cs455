package cs455.overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
this message will be sent back to a node after it registers.
if the registration is not successful, the byte code should tell the node that it failed, and additional information should be provided

register response has the event type of 2


 */
public class RegisterResponse extends Protocol {
    public byte statusCode;
    public String additionalInfo;

    /*
    marshal this object using a ByteArrayOutputStream
     */
    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(statusCode);
        byteOut.write(marshalString(additionalInfo));
        return byteOut.toByteArray();
    }
    /*
    constructor demarshals if constructed from byte array
     */

    public RegisterResponse(byte[] data){
        this.eventType = demarshalInt(data);
        this.statusCode = data[4];
        this.additionalInfo = demarshalString(data, 5);
        this.eventData = data;

    }
    /*
    constructor that constructs from normal arguments, throws exception if it cant marshal
    the data properly
     */

    public RegisterResponse(byte statusCode, String additionalInfo) throws IOException{
        this.statusCode = statusCode;
        this.additionalInfo = additionalInfo;
        this.eventType = 2;
        this.eventData = this.marshal();
    }





}
