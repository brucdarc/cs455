package cs455.overlay.wireformats;
/*
this is a class to wrap all the data that needs to go into the linkweights linkinfo array when
the linkweights protocol is sent.
There will be an array of these with 1 for each link in the network.

This class is NOT a protocol
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LinkInfo{
    public String hostnameA;
    public int portNumberA;
    public String hostnameB;
    public int portNumberB;
    public int weight;



    public byte[] marshal() throws IOException{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(Protocol.marshalString(this.hostnameA));
        byteOut.write(Protocol.marshalInt(portNumberA));
        byteOut.write(Protocol.marshalString(this.hostnameB));
        byteOut.write(Protocol.marshalInt(portNumberB));
        byteOut.write(Protocol.marshalInt(weight));
        return byteOut.toByteArray();
    }

    public LinkInfo(String hostnameA, int portNumberA, String hostNameB, int portNumberB, int weight){
        this.hostnameA = hostnameA;
        this.portNumberA = portNumberA;
        this.hostnameB = hostNameB;
        this.portNumberB = portNumberB;
        this.weight = weight;
    }

    public String toString(){
        return hostnameA + ":" + portNumberA +"  " + hostnameB + ":" + portNumberB + " " + weight;
    }

}
