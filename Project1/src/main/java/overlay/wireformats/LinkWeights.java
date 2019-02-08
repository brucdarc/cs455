package overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
This protocal will be send from the registry to all connected messaging nodes
the messaging nodes will use the information

event type 11
 */
public class LinkWeights extends Protocol{
    public int numberOfLinks;
    public LinkInfo[] linkInfos;



    public byte[] marshal()throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(marshalInt(this.eventType));
        byteOut.write(marshalInt(numberOfLinks));
        for(LinkInfo l: linkInfos){
            byteOut.write(l.marshal());
        }
        return byteOut.toByteArray();
    }

    public LinkWeights(byte[] data){
        this.eventType = demarshalInt(data);
        this.numberOfLinks = demarshalInt(data,4);
        int offset = 8;
        ArrayList<LinkInfo> tempPeers = new ArrayList<LinkInfo>(numberOfLinks);
        while(offset<data.length){
            String peerIpA = demarshalString(data,offset);
            offset += peerIpA.length() + 4;
            int peerPortA = demarshalInt(data,offset);
            offset+= 4;
            String peerIpB = demarshalString(data,offset);
            offset += peerIpB.length() + 4;
            int peerPortB = demarshalInt(data,offset);
            offset+= 4;
            int weight = demarshalInt(data,offset);
            offset+= 4;

            tempPeers.add(new LinkInfo(peerIpA,peerPortA, peerIpB,peerPortB,weight));
        }
        /*
        this is some weird thing where I cant cast the array directly to the object type I want
        Instead you have to call the to array method with an empty array of the same object type
        and same amount of elements.
         */
        this.linkInfos = tempPeers.toArray(new LinkInfo[tempPeers.size()]);

    }

    public LinkWeights(int numberOfLinks, LinkInfo[] linkInfos) throws IOException{
        this.eventType = 11;
        this.numberOfLinks = numberOfLinks;
        this.linkInfos = linkInfos;
        this.eventData = this.marshal();
    }

    public String toString(){
        String result = numberOfLinks + "L  ";
        for(LinkInfo in : linkInfos){
            result += in;
            result += "\n";
        }
        return result;
    }
}
