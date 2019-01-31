package overlay.wireformats;
/*
this message will be sent to all nodes to tell them who their peers that they connect to are.
 */

public class MessagingNodesList extends Protocol{
    public int numOfPeers;
    public MessagingNode[] peers;
}
