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



     @Test
    public void testMarshalString(){
        String test = "hi Im bob";
        byte[] result = Protocol.marshalString(test);
        byte[] ba = {0,0,0,9,104,105,32,73,109,32,98,111,98};
        assertArrayEquals(ba , result);

    }



}
