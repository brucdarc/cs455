package cs455.overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
after a messaging node recieves the summaryRequest, it will send back a message using this protocol containing
various data about the bulk sending task that just occured

the number of counters seem to be to count the number of messages going through the system.

Im not sure what the sumnation of messages fields are for.

event type 10
 */
public class TaskSummaryResponse extends Protocol{
    public String nodeIPAddress;
    public int nodePortNumber;
    public int numberOfMessagesSent;
    public int sumOfSentMessages;
    public int numberOfMessagesReceived;
    public int sumOfReceivedMessages;
    public int numberOfMessagesRelayed;




    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalString(nodeIPAddress));
        byteOut.write(marshalInt(nodePortNumber));
        byteOut.write(marshalInt(numberOfMessagesSent));
        byteOut.write(marshalInt(sumOfSentMessages));
        byteOut.write(marshalInt(numberOfMessagesReceived));
        byteOut.write(marshalInt(sumOfReceivedMessages));
        byteOut.write(marshalInt(numberOfMessagesRelayed));
        return byteOut.toByteArray();
    }

    /*
    there is not a demarshal method in protocol classes, rather the a constructor handles demarshalling
    if instanciated with a byte array.
     */
    public TaskSummaryResponse(byte[] data){
        this.eventData = data;
        this.eventType = demarshalInt(data);
        this.nodeIPAddress = demarshalString(data, 4);
        this.nodePortNumber = demarshalInt(data, (8 + nodeIPAddress.length()) );

        int offset = 12 + nodeIPAddress.length();
        this.numberOfMessagesSent = demarshalInt(data, offset);
        offset += 4;
        this.sumOfSentMessages = demarshalInt(data, offset);
        offset += 4;
        this.numberOfMessagesReceived = demarshalInt(data, offset);
        offset += 4;
        this.sumOfReceivedMessages = demarshalInt(data, offset);
        offset += 4;
        this.numberOfMessagesRelayed = demarshalInt(data, offset);
    }

    public TaskSummaryResponse (String IPAddress, int port, int numSent, int sumSent, int numRec, int sumRec, int numRelayed) throws IOException{
        this.nodeIPAddress = IPAddress;
        this.nodePortNumber = port;
        this.numberOfMessagesSent = numSent;
        this.numberOfMessagesReceived = numRec;
        this.numberOfMessagesRelayed = numRelayed;
        this.sumOfSentMessages = sumSent;
        this.sumOfReceivedMessages = sumRec;
        this.eventType = 10;
        this.eventData = this.marshal();

    }
}


