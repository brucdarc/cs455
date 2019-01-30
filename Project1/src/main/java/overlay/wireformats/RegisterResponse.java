package overlay.wireformats;
/*
this message will be sent back to a node after it registers.
if the registration is not successful, the byte code should tell the node that it failed, and additional information should be provided
 */
public class RegisterResponse extends Protocol {
    public byte statusCode;
    public String additionalInfo;
}
