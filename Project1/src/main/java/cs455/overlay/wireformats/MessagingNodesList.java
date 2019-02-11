package cs455.overlay.wireformats;
/*
this message will be sent to all nodes to tell them who their peers that they connect to are.
Event type of 5
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MessagingNodesList extends Protocol{
    public int numOfPeers;
    public MessagingNodeInfo[] peers;

    /*
    marshals all data, including looping through the array to marshal each messagingnode peer
     */
    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalInt(numOfPeers));
        for(MessagingNodeInfo m : peers){
            byteOut.write(m.marshal());
        }
        return byteOut.toByteArray();
    }

    public MessagingNodesList(byte[] data){
        this.eventType = demarshalInt(data);
        this.numOfPeers = demarshalInt(data,4);
        int offset = 8;
        ArrayList<MessagingNodeInfo> tempPeers = new ArrayList<MessagingNodeInfo>(numOfPeers);
        while(offset<data.length){
            String peerIp = demarshalString(data,offset);
            offset += peerIp.length() + 4;
            int peerPort = demarshalInt(data,offset);
            offset+= 4;
            tempPeers.add(new MessagingNodeInfo(peerIp,peerPort));
        }
        /*
        this is some weird thing where I cant cast the array directly to the object type I want
        Instead you have to call the to array method with an empty array of the same object type
        and same amount of elements.
         */
        peers = tempPeers.toArray(new MessagingNodeInfo[tempPeers.size()]);

    }

    public MessagingNodesList(int numOfPeers, MessagingNodeInfo[] peers) throws IOException{
        this.eventType = 5;
        this.numOfPeers = numOfPeers;
        this.peers = peers;
        this.eventData = this.marshal();
    }

    public String toString(){
        String result = "";
        for(MessagingNodeInfo p: peers){
            result  += p.nodeHostName + ":" + p.nodePortnum + "\n";
        }
        return result;
    }




}
