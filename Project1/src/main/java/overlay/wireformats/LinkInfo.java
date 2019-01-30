package overlay.wireformats;
/*
this is a class to wrap all the data that needs to go into the linkweights linkinfo array when
the linkweights protocol is sent.
There will be an array of these with 1 for each link in the network.

This class is NOT a protocol
 */

public class LinkInfo {
    public String hostnameA;
    public int portNumberA;
    public String hostNameB;
    public int portNumberB;
    public int weight;
}
