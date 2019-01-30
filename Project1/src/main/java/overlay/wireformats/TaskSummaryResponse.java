package overlay.wireformats;
/*
after a messaging node recieves the summaryRequest, it will send back a message using this protocol containing
various data about the bulk sending task that just occured

the number of counters seem to be to count the number of messages going through the system.

Im not sure what the sumnation of messages fields are for.
 */
public class TaskSummaryResponse extends Protocol{
    public String nodeIPAddress;
    public String nodePortNumber;
    public int numberOfMessagesSent;
    public int sumOfSentMessages;
    public int numberOfMessagesReceived;
    public int sumOfReceivedMessages;
    public int numberOfMessagesRelayed;
}
