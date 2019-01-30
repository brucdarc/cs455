
import org.junit.Test;
import overlay.node.MessagingNode;
import overlay.node.Registry;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestStartup {



    @Test
    public void testTesting() {
        System.out.println("HIHIHI");
        assertEquals(6, 6);
    }

    @Test
    public void testRegistryMessenger()throws IOException{
        Registry reg = new Registry();
        MessagingNode mes = new MessagingNode();
        String result = reg.doCommand("test");
        assertEquals(result, "Test message");
        result = mes.doCommand("test");
        assertEquals(result, "Test message");
    }

}
