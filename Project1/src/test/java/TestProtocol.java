import org.junit.Test;
import overlay.node.MessagingNode;
import overlay.node.Registry;
import overlay.wireformats.Protocol;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class TestProtocol {
    @Test
    public void testMarshalInt(){
        int i = 334567654;
        byte[] marshalled = Protocol.marshalInt(i);
        byte[] expected = {19,-15,24,-26};
        assertArrayEquals(marshalled, expected);
    }

    @Test
    public void testDemarshalInt(){
        byte[] input = {19,-15,24,-26};
        System.out.println(Protocol.demarshalInt(input));
        assertEquals(334567654,Protocol.demarshalInt(input));
    }

    @Test
    public void testDemarshalIntOffset(){
        byte[] input = {0, 19,-15,24,-26};
        System.out.println(Protocol.demarshalInt(input, 1));
        assertEquals(334567654,Protocol.demarshalInt(input, 1));
    }
}
