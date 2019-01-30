package overlay.wireformats;

import overlay.node.Registry;
/*
This class will work on a registry object to register a connection
The registry will then need to add it to the overlay, and send a register response message back to tell the node
whether it was able to successfully register or not.


 */

public class Register extends Protocol{
    public String IPAddress;
    public int port;

    //this method will need to second argument of what to register
    public void register(Registry registry){


    }
}
