import org.junit.Test;
import cs455.overlay.node.MessagingNode;
import cs455.overlay.node.Registry;
import cs455.overlay.wireformats.*;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestEventFactory {
    @Test
    public void testFactory() throws IOException{
        Register r = new Register("hihihi", 1234);

        Event a1 = EventFactory.makeEvent(r.marshal());

        assertEquals( (a1 instanceof Register), true  );

        RegisterResponse rr = new RegisterResponse((byte)0, "blablabla");

        Event a2 = EventFactory.makeEvent(rr.marshal());

        assertEquals( (a2 instanceof RegisterResponse), true  );

    }

    @Test
    public void testFactory2() throws IOException{
        Deregister r = new Deregister("hihihi", 1234);

        Event a1 = EventFactory.makeEvent(r.marshal());

        assertEquals( (a1 instanceof Deregister), true  );

        DeregisterResponse rr = new DeregisterResponse((byte)0, "blablabla");

        Event a2 = EventFactory.makeEvent(rr.marshal());

        assertEquals( (a2 instanceof DeregisterResponse), true  );

    }

    @Test
    public void testFactory3() throws IOException{
        MessagingNodeInfo m1 = new MessagingNodeInfo("myhost", 1234);
        MessagingNodeInfo m2 = new MessagingNodeInfo("myhost", 1234);
        MessagingNodeInfo m3 = new MessagingNodeInfo("myhost", 1234);
        MessagingNodeInfo[] marray = {m1,m2,m3};
        MessagingNodesList ml = new MessagingNodesList(3, marray);

        Event a1 = EventFactory.makeEvent(ml.marshal());

        assertEquals( a1 instanceof MessagingNodesList, true);


    }

    @Test
    public void testFactory4() throws IOException{
        LinkInfo l1 = new LinkInfo("myhostA", 444, "Myhost b", 6785, 4);
        LinkInfo l2 = new LinkInfo("myhostAA", 4443, "Myhost bb", 67851, 5);
        LinkInfo l3 = new LinkInfo("myhostAAA", 4442, "Myhost bbb", 67856, 6);
        LinkInfo[] nodes = {l1,l2,l3};


        LinkWeights mlist = new LinkWeights(3,nodes);

        Event a1 = EventFactory.makeEvent(mlist.marshal());

        assertEquals( a1 instanceof LinkWeights, true);


    }

    @Test
    public void testFactoryBrokenEvent() throws IOException{
        Register broken = new Register("fdsfad",1234);
        broken.eventType = 30;
        try{
            Event ai = EventFactory.makeEvent(broken.marshal());
            assertEquals(1,0);
        }
        catch(IOException e){
            assertEquals(1,1);
        }
    }

    @Test
    public void testFactory5() throws IOException{
        TaskInitiate r = new TaskInitiate(12);

        Event a1 = EventFactory.makeEvent(r.marshal());

        assertEquals( (a1 instanceof TaskInitiate), true  );

        TaskComplete rr = new TaskComplete("ipppp",123);

        Event a2 = EventFactory.makeEvent(rr.marshal());

        assertEquals( (a2 instanceof TaskComplete), true  );

    }

    @Test
    public void testFactory6() throws IOException {
        Message r = new Message("ba", "dest",5);

        Event a1 = EventFactory.makeEvent(r.marshal());

        assertEquals((a1 instanceof Message), true);

        TaskSummaryRequest rr = new TaskSummaryRequest();

        Event a2 = EventFactory.makeEvent(rr.marshal());

        assertEquals((a2 instanceof TaskSummaryRequest), true);

    }

    @Test
    public void testFactory7() throws IOException {
        TaskSummaryResponse r = new TaskSummaryResponse("aaaa",1,2,3,4,5,6);

        Event a1 = EventFactory.makeEvent(r.marshal());

        assertEquals((a1 instanceof TaskSummaryResponse), true);

    }









}
