
import org.junit.Test;
import overlay.node.MessagingNode;
import overlay.node.Registry;
import overlay.transport.TCPServerThread;

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
        int port = TCPServerThread.findOpenPort();
        System.out.println(port);
        Registry reg = new Registry(port);
        MessagingNode mes = new MessagingNode("myselver", 1237);
        String result = reg.doCommand("test");
        assertEquals(result, "Test message");
        result = mes.doCommand("test");
        assertEquals(result, "Test message");
    }

}
