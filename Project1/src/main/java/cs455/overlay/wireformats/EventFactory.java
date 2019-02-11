package cs455.overlay.wireformats;
//this class needs to be singleton instance
//that means that only 1 instance of this should ever exist
//public static final methods, or something to do with that will be needed

import java.io.IOException;

public final class EventFactory {

    private EventFactory() {

    }
    /*
    hit a switch statement off of the event type, then call the appropiate constructor to make the correct event
    each event has a numbered event typed as shown by the cases in the switch statement

    the constructors for the protocols demarshal the byte array for you, and create the object
     */

    public static Event makeEvent (byte[] input)throws IOException{
        int type = Protocol.demarshalInt(input);

        switch(type){
            case 1:
                //register
                return new Register(input);
            case 2:
                //register response
                return new RegisterResponse(input);
            case 3:
                //deregister
                return new Deregister(input);
            case 4:
                //deregister response
                return new DeregisterResponse(input);
            case 5:
                //messaging nodes list
                return new MessagingNodesList(input);
            case 6:
                //task initiate
                return new TaskInitiate(input);
            case 7:
                //sent message
                return new Message(input);
            case 8:
                //task complete
                return new TaskComplete(input);
            case 9:
                //Task summary request
                return new TaskSummaryRequest(input);
            case 10:
                //task summary response
                return new TaskSummaryResponse(input);
            case 11:
                //LinkWeights
                return new LinkWeights(input);
            default:
                throw new IOException("Recieved message is not of a valid message type");
        }

    }


}
