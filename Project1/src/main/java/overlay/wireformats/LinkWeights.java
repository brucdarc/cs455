package overlay.wireformats;
/*
This protocal will be send from the registry to all connected messaging nodes
the messaging nodes will use the information
 */
public class LinkWeights extends Protocol{
    public int numberOfLinks;
    public LinkInfo[] linkInfos;
}
