package overlay.wireformats;

import overlay.node.Registry;
/*
This class will work on a registry object to deregister a connection

The register might need to send a response to tell the node it deregistered successfully, Im not sure at this time

*/
public class Deregister extends Protocol{
    public String IPAddress;
    public int port;


    //this method will need another argument of what to deregister
    public void deregister(Registry registry){

    }
}
