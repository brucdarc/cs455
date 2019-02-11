import org.junit.Test;
import cs455.overlay.node.MessagingNode;
import cs455.overlay.node.Registry;
import cs455.overlay.wireformats.*;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class TestMarshaling {
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

    @Test
    public void testDemarshalString(){
        String test = "hi Im bob";
        byte[] ba = {0,0,0,9,104,105,32,73,109,32,98,111,98};
        String result = Protocol.demarshalString(ba);
        assertEquals(test , result);

    }

    @Test
    public void testStringMarshalEdgeCases() {
        String test = "1";
        byte[] ba = {0, 0, 0, 1, 49};
        String result = Protocol.demarshalString(ba);
        System.out.println(result);
        assertEquals(test, result);
        String test2 = "";
        byte[] ba2 = {0, 0, 0, 0};
        String result2 = Protocol.demarshalString(ba2);
        System.out.println(result2);
        assertEquals(test2, result2);
    }

    @Test
    public void testRegister() throws IOException{
        Register register = new Register("hihihi", 4440);
        byte[] marshaled = register.marshal();

        Register onOtherNode = new Register(marshaled);

        assertEquals(register.IPAddress,onOtherNode.IPAddress);
        assertEquals(register.port,onOtherNode.port);
        assertEquals(register.eventType,onOtherNode.eventType);
    }

    @Test
    public void testRegisterResponse() throws IOException{
        RegisterResponse rr = new RegisterResponse((byte)1, "WHIP IT OUT");
        byte[] marshaled = rr.marshal();

        RegisterResponse onOtherNode = new RegisterResponse(marshaled);

        assertEquals(rr.additionalInfo,onOtherNode.additionalInfo);
        assertEquals(rr.statusCode,onOtherNode.statusCode);
        assertEquals(rr.eventType,onOtherNode.eventType);
    }

    @Test
    public void testDeregister() throws IOException{
        Deregister deregister = new Deregister("hihihi", 4440);
        byte[] marshaled = deregister.marshal();

        Deregister onOtherNode = new Deregister(marshaled);

        assertEquals(deregister.IPAddress,onOtherNode.IPAddress);
        assertEquals(deregister.port,onOtherNode.port);
        assertEquals(deregister.eventType,onOtherNode.eventType);
    }

    @Test
    public void testdeRegisterResponse() throws IOException{
        DeregisterResponse rr = new DeregisterResponse((byte)1, "WHIP IT OUT");
        byte[] marshaled = rr.marshal();

        DeregisterResponse onOtherNode = new DeregisterResponse(marshaled);

        assertEquals(rr.additionalInfo,onOtherNode.additionalInfo);
        assertEquals(rr.statusCode,onOtherNode.statusCode);
        assertEquals(rr.eventType,onOtherNode.eventType);
    }


    @Test
    public void testMessagingNodesList() throws IOException{

        MessagingNodeInfo m1 = new MessagingNodeInfo("Ipaddress", 1234);
        MessagingNodeInfo m2 = new MessagingNodeInfo("Ipaddress", 12345);
        MessagingNodeInfo m3 = new MessagingNodeInfo("Ipaddress", 123456);
        MessagingNodeInfo[] nodes = {m1,m2,m3};


        MessagingNodesList mlist = new MessagingNodesList(3,nodes);
        MessagingNodesList otherlist = new MessagingNodesList(mlist.eventData);


        assertEquals(mlist.numOfPeers,otherlist.numOfPeers);

        assertEquals(mlist.peers[0].nodeHostName,otherlist.peers[0].nodeHostName);
        assertEquals(mlist.peers[0].nodePortnum,otherlist.peers[0].nodePortnum);
        assertEquals(mlist.peers[1].nodeHostName,otherlist.peers[1].nodeHostName);
        assertEquals(mlist.peers[1].nodePortnum,otherlist.peers[1].nodePortnum);
        assertEquals(mlist.peers[2].nodeHostName,otherlist.peers[2].nodeHostName);
        assertEquals(mlist.peers[2].nodePortnum,otherlist.peers[2].nodePortnum);

    }

    @Test
    public void testMessagingNodesList2() throws IOException{

        MessagingNodeInfo m1 = new MessagingNodeInfo("Ipaddress", 1234);
        MessagingNodeInfo[] nodes = {m1};


        MessagingNodesList mlist = new MessagingNodesList(3,nodes);
        MessagingNodesList otherlist = new MessagingNodesList(mlist.eventData);


        assertEquals(mlist.numOfPeers,otherlist.numOfPeers);
        assertEquals(mlist.peers[0].nodeHostName,otherlist.peers[0].nodeHostName);
        assertEquals(mlist.peers[0].nodePortnum,otherlist.peers[0].nodePortnum);


    }

    @Test
    public void testMessagingNodesList3() throws IOException{

        MessagingNodeInfo[] nodes = {};


        MessagingNodesList mlist = new MessagingNodesList(3,nodes);
        MessagingNodesList otherlist = new MessagingNodesList(mlist.eventData);


        assertEquals(mlist.numOfPeers,otherlist.numOfPeers);
        assertArrayEquals(mlist.peers,otherlist.peers);


    }

    @Test
    public void testTaskInitiate() throws IOException{
        TaskInitiate rr = new TaskInitiate(8);
        byte[] marshaled = rr.marshal();

        TaskInitiate onOtherNode = new TaskInitiate(marshaled);

        assertEquals(rr.rounds,onOtherNode.rounds);
        assertEquals(rr.eventType,onOtherNode.eventType);
    }

    @Test
    public void testTaskComplete() throws IOException{
        TaskComplete rr = new TaskComplete("ipaddress", 1234);
        byte[] marshaled = rr.marshal();

        TaskComplete onOtherNode = new TaskComplete(marshaled);

        assertEquals(rr.nodePortNumber,onOtherNode.nodePortNumber);
        assertEquals(rr.nodeIPAddress,onOtherNode.nodeIPAddress);
        assertEquals(rr.eventType,onOtherNode.eventType);
    }

    @Test
    public void testTaskSummaryRequest() throws IOException{
        TaskSummaryRequest rr = new TaskSummaryRequest();
        byte[] marshaled = rr.marshal();

        TaskSummaryRequest onOtherNode = new TaskSummaryRequest(marshaled);

        assertEquals(rr.eventType,onOtherNode.eventType);
    }

    @Test
    public void testLinkWeigts() throws IOException{

        LinkInfo[] nodes = {};


        LinkWeights mlist = new LinkWeights(0,nodes);
       LinkWeights otherlist = new LinkWeights(mlist.eventData);


        assertEquals(mlist.numberOfLinks,otherlist.numberOfLinks);
        assertArrayEquals(mlist.linkInfos,otherlist.linkInfos);


    }

    @Test
    public void testLinkWeigts1() throws IOException{
        LinkInfo l1 = new LinkInfo("myhostA", 444, "Myhost b", 6785, 4);
        LinkInfo l2 = new LinkInfo("myhostAA", 4443, "Myhost bb", 67851, 5);
        LinkInfo l3 = new LinkInfo("myhostAAA", 4442, "Myhost bbb", 67856, 6);
        LinkInfo[] nodes = {l1,l2,l3};


        LinkWeights mlist = new LinkWeights(0,nodes);
        LinkWeights otherlist = new LinkWeights(mlist.eventData);


        assertEquals(mlist.numberOfLinks,otherlist.numberOfLinks);

        assertEquals(mlist.linkInfos[0].hostnameA, otherlist.linkInfos[0].hostnameA);
        assertEquals(mlist.linkInfos[0].hostnameB, otherlist.linkInfos[0].hostnameB);
        assertEquals(mlist.linkInfos[0].portNumberA, otherlist.linkInfos[0].portNumberA);
        assertEquals(mlist.linkInfos[0].portNumberB, otherlist.linkInfos[0].portNumberB);
        assertEquals(mlist.linkInfos[0].weight, otherlist.linkInfos[0].weight);

        assertEquals(mlist.linkInfos[1].hostnameA, otherlist.linkInfos[1].hostnameA);
        assertEquals(mlist.linkInfos[1].hostnameB, otherlist.linkInfos[1].hostnameB);
        assertEquals(mlist.linkInfos[1].portNumberA, otherlist.linkInfos[1].portNumberA);
        assertEquals(mlist.linkInfos[1].portNumberB, otherlist.linkInfos[1].portNumberB);
        assertEquals(mlist.linkInfos[1].weight, otherlist.linkInfos[1].weight);

        assertEquals(mlist.linkInfos[2].hostnameA, otherlist.linkInfos[2].hostnameA);
        assertEquals(mlist.linkInfos[2].hostnameB, otherlist.linkInfos[2].hostnameB);
        assertEquals(mlist.linkInfos[2].portNumberA, otherlist.linkInfos[2].portNumberA);
        assertEquals(mlist.linkInfos[2].portNumberB, otherlist.linkInfos[2].portNumberB);
        assertEquals(mlist.linkInfos[2].weight, otherlist.linkInfos[2].weight);




    }

    @Test
    public void testTaskSummaryResponse() throws IOException{
        TaskSummaryResponse rr = new TaskSummaryResponse("ipaddress",1,2,3,4,5,6);
        byte[] marshaled = rr.marshal();

        TaskSummaryResponse onOtherNode = new TaskSummaryResponse(marshaled);

        assertEquals(rr.eventType,onOtherNode.eventType);
        assertEquals(rr.nodeIPAddress,onOtherNode.nodeIPAddress);
        assertEquals(rr.nodePortNumber,onOtherNode.nodePortNumber);
        assertEquals(rr.numberOfMessagesReceived,onOtherNode.numberOfMessagesReceived);
        assertEquals(rr.numberOfMessagesSent,onOtherNode.numberOfMessagesSent);
        assertEquals(rr.sumOfReceivedMessages,onOtherNode.sumOfReceivedMessages);
        assertEquals(rr.sumOfSentMessages,onOtherNode.sumOfSentMessages);
        assertEquals(rr.numberOfMessagesRelayed,onOtherNode.numberOfMessagesRelayed);
    }

    @Test
    public void testMessage() throws IOException{
        Message r = new Message("ba", "dest",5);
        Message other = new Message(r.marshal());

        assertEquals(r.eventType, other.eventType);
        assertEquals(r.source, other.source);
        assertEquals(r.destination,other.destination);
        assertEquals(r.communicatedValue, other.communicatedValue);
    }











}
