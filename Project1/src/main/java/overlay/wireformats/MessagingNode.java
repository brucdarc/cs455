package overlay.wireformats;
/*
this is a class that describes a messaging node by its ip and portnumber
currently it is used in the messagingNodesList protocol to communicate to
a node what peers is has with a list of these.

This is NOT a protocol
 */
public class MessagingNode {
    public String nodeHostName;
    public int nodePortnum;
}
