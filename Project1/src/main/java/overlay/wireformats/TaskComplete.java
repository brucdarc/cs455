package overlay.wireformats;
/*
This messagge will be sent from a messaging node to the registry when the messaging node is finished sending messages
after a command to do so.
The variables are to help the registry identify which node this is.
 */
public class TaskComplete extends Protocol{
    public String nodeIPAddress;
    public String nodePortNumber;
}
