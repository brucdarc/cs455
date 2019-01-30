package overlay.wireformats;
/*
this message will be sent from the registry to nodes to start the message sending protocol.
It tells each node how many messages to send.
 */
public class TaskInitiate extends Protocol{
    public int rounds;
}
