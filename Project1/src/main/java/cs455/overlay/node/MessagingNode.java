package cs455.overlay.node;

import cs455.overlay.dijkstra.ShortestPath;
import cs455.overlay.dijkstra.Vertex;
import cs455.overlay.dijkstra.Edge;
import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MessagingNode extends Node{
    public String myHostname;
    public int myPort;
    public String myIdentifier;
    public String registryHostname;
    public int registryPortnumber;
    public TCPServerThread serverObject;
    public Thread serverThread;
    public MessagingNodesList peers;
    public Map<String,Socket> connections;
    public LinkWeights linkWeights;
    public ShortestPath shortestPath;
    public Map<String, String> nextHop;
    public Socket regSock;

    public int messagesSent;
    public int messagesRec;
    public int sumSent;
    public int sumRec;
    public int numRel;

    /*
    This method will need to handle all events that can happen to a messaging node.
    An exception should be thrown if the event if an event meant for a registry


     */
    @Override
    public void onEvent(Event event){
        try {
            if (event instanceof RegisterResponse) handleRegistryResponse((RegisterResponse) event);
            if (event instanceof DeregisterResponse) handleDeregistryResponse((DeregisterResponse) event);
            if (event instanceof MessagingNodesList) handleOverlayCreation((MessagingNodesList) event);
            if (event instanceof LinkWeights) handleLinkWeights((LinkWeights) event);
            if (event instanceof Message) handleMessage((Message) event);
            if (event instanceof TaskInitiate) handleTaskInitiate((TaskInitiate) event);
            if (event instanceof Register) handlePeerRegistration((Register) event);
            if (event instanceof TaskSummaryRequest) handleTaskSummaryRequest((TaskSummaryRequest) event);

        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }



        /*
    This constructor creates a TCPServerthread object,
    then wraps that in a thread object and starts the thread to begin server execution concurrently
    Pass false to the serverObject so it knows that it is a messenger
    pass this node into serverThread so it knows who its node is
    All commands should call to the server to make something happen
     */


    public MessagingNode(String registryHostname, int registryPortnumber) throws IOException{
        this.registryHostname = registryHostname;
        this.registryPortnumber = registryPortnumber;
        int port = TCPServerThread.findOpenPort();
        serverObject = new TCPServerThread(port, this);
        serverThread = new Thread(serverObject);
        serverThread.start();
        myHostname = InetAddress.getLocalHost().getHostAddress();
        myPort = serverObject.serverSocket.getLocalPort();
        connections = new HashMap<String,Socket>();
        myIdentifier = myHostname + ":" + myPort;

        numRel = 0;
        messagesRec = 0;
        messagesSent = 0;
        sumRec = 0;
        sumSent = 0;
    }
    /*
    This method is overrriden, it only allows commands to be taken that are applicable to a messenger node.
    Some commands will have an additional arguments that will need to be used when calling the appropriate action for each
    command.
     */
    @Override
    public String doCommand(String input) throws IOException{
        String[] words = input.split("\\s+");
        switch(words[0]){
            case "test":
                return "Test message";
            case "print-shortest-path":
                return printShortestPath();
            case "register":
                sendRegisterRequest();
                return "sent registration request";
            case "exit-overlay":
                sendDeregisterRequest();
                return "sent deregister request";
            default:
                return "Command not recognized";
        }
    }
    /*
    creates a string that describes the shortest paths to all nodes
     */

    public String printShortestPath(){
        String result = "";
        for(Vertex v: shortestPath.vertArr){
            ArrayList<Vertex> path = shortestPath.getSolutionPath(v);
            for(int i = 0; i<path.size();i++){
                result += path.get(i).identifier;
                if(i<path.size()-1){
                    result += "--";
                    result += findEdge(path.get(i),path.get(i).identifier,path.get(i+1).identifier).weight;
                    result += "--";
                }

            }
            result += ",\n";

        }
        return result;
    }

    /*
    helper method of print shortest path, brute force find an edge
     */

    public Edge findEdge(Vertex v, String id1, String id2){
        for(Edge e:v.edges){
            if(e.vertex1.identifier.equals(id1) && e.vertex2.identifier.equals(id2)) return e;
            if(e.vertex1.identifier.equals(id2) && e.vertex2.identifier.equals(id1)) return e;
        }
        return null;
    }

    /*
    This method sends the register request request to the registry
     */

    public void sendRegisterRequest() throws IOException{
        String ip = myHostname;
        int port = myPort;
        Register request = new Register(ip, port);
        Socket regSock = new Socket(registryHostname,registryPortnumber);
        this.regSock = regSock;
        TCPReceiverThread receiver = new TCPReceiverThread(regSock, this);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
        TCPSender regSender = new TCPSender(regSock);
        regSender.sendData(request.eventData);

    }
    //handles registry response. tells the node that if registered correctly or incorrectly
    public void handleRegistryResponse(RegisterResponse registerResponse){
        if(registerResponse.statusCode == 1){
        System.out.println("Could not register: " + registerResponse.additionalInfo);
        }
        else System.out.println("Registered successfully: " + registerResponse.additionalInfo);

    }

    /*
    send a deregister request to registry to deregister this node
     */

    public void sendDeregisterRequest() throws IOException{
        String ip = myHostname;
        int port = myPort;
        Deregister request = new Deregister(ip, port);
        Socket regSock = new Socket(registryHostname,registryPortnumber);
        TCPReceiverThread receiver = new TCPReceiverThread(regSock, this);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
        TCPSender regSender = new TCPSender(regSock);
        regSender.sendData(request.eventData);
    }

    /*

     */
    //handles registry response. tells the node that if registered correctly or incorrectly
    //kills the node
    public void handleDeregistryResponse(DeregisterResponse deregisterResponse){
        if(deregisterResponse.statusCode == 1){
            System.out.println("Could not deregister: " + deregisterResponse.additionalInfo);
        }
        else System.out.println("Deregistered successfully: " + deregisterResponse.additionalInfo);

        System.exit(0);

    }

    /*
    sets the peers local variable, then calls a method to initiate connections to peers
    */
    public void handleOverlayCreation(MessagingNodesList peers) throws IOException{
        this.peers = peers;
        System.out.println("recieved peers");
        connectToPeers();
    }

    /*
    uses comparisons between the hostnames of nodes to make sure that only 1 node initates a connections
    if there is a tie, ie two nodes running on the same hostname, the port is used to break the tie

    it should be impossible for 2 nodes to have the name hostname and port
     */

    public void connectToPeers() throws IOException{
        for(MessagingNodeInfo peer: peers.peers){
            if(peer.nodeHostName.compareTo(myHostname) > 0){
                connectToPeer(peer);
            }
            if(peer.nodeHostName.compareTo(myHostname) == 0){
                if(peer.nodePortnum > myPort){
                    connectToPeer(peer);
                }
            }
        }
    }

    /*
    initiates a connection to a peer, and creates a reciever thread for it
    adds the peers socket to our connections map
     */

    public synchronized void connectToPeer(MessagingNodeInfo peer) throws IOException{
        Socket socket = new Socket(peer.nodeHostName,peer.nodePortnum);
        String key = peer.nodeHostName + ":" + peer.nodePortnum;
        TCPReceiverThread receiver = new TCPReceiverThread(socket, this);
        synchronized (connections) {
            connections.put(key, socket);
        }
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        Register myinfo = new Register(myHostname,myPort);
        TCPSender sender = new TCPSender(socket);
        sender.sendData(myinfo.eventData);

        //System.out.println("connected to peer: " + key);
    }

    /*
    handles a peer connecting onto us by adding the peers socket into our connections map
     */

    public synchronized void handlePeerRegistration(Register peerInfo){
        String key = peerInfo.IPAddress + ":" + peerInfo.port;
        synchronized (connections) {
            connections.put(key, peerInfo.socket);
        }
        //System.out.println("connected onto by peer: " + key);
    }

    /*
    this functions calls the shortest path class to run dijkstras algorithm, and construct a map
    that allows mapping of a destination address to the next address a message needs to be sent to
    to reach that destination.
     */

    public void handleLinkWeights(LinkWeights linkWeights){
        this.linkWeights = linkWeights;
        //System.out.println("Recieved link weights from registry");

        shortestPath = new ShortestPath();
        shortestPath.initialize(linkWeights);
        shortestPath.dijkstras(myIdentifier);
        nextHop = shortestPath.makeNextHopMap();
        //do dijkstras and make next hop map

        //System.out.println(linkWeights);
        //System.out.println(nextHop);
    }

    public void handleMessage(Message m) throws IOException{
        String destination = m.destination;
        //System.out.println("entered message handling to : " + destination + " : I am : " + myIdentifier);
        if(destination.equals(myIdentifier)) handleMessageForMe(m);
        else relayMessage(m);
    }

    /*
    this function send a message to the nexthop it need to get to the destination, and increments the relayed messages counter
     */
    public void relayMessage(Message m) throws IOException{
        //System.out.println("entered relay method");

        try {
            String nextNode = nextHop.get(m.destination);
            Socket nextNodeSock = connections.get(nextNode);
            //System.out.println("relaying " + nextNode + " socket " + nextNodeSock);
            synchronized (this) {
                numRel++;
            }
            TCPSender sender = new TCPSender(nextNodeSock);
            sender.sendData(m.eventData);
        }
        catch (NullPointerException e){
            System.out.println("null pointer encountered");
            System.out.println(peers + ":" + connections);
        }


    }

    public synchronized void handleMessageForMe(Message m){

        sumRec += m.communicatedValue;
        messagesRec++;

    }

    /*
    Do x rounds of sending messages to a sink
    5 messages per round

     */

    public void handleTaskInitiate(TaskInitiate taskInitiate) throws IOException{
        messagesRec = 0;
        messagesSent = 0;
        sumRec = 0;
        sumSent = 0;
        numRel = 0;

        System.out.println("Sending " + taskInitiate.rounds + " Messages");



        int rounds = taskInitiate.rounds;

        ArrayList<Vertex> nodes = new ArrayList<Vertex>(shortestPath.vertArr);
        Vertex me = new Vertex(myIdentifier);
        nodes.remove(me);

        Random random = new Random();
        try {
            for (int i = 0; i < rounds; i++) {
                Socket sock;
                int rand = random.nextInt(nodes.size());
                String dest = nodes.get(rand).identifier;
                //System.out.println("Sending message to : " + dest);
                String source = myIdentifier;
                for(int k = 0; k<5;k++) {
                    Message mess = new Message(source, dest, random.nextInt(10));

                    String nextPlace = nextHop.get(dest);
                    //System.out.println("nexthop : " + nextPlace);
                    sock = connections.get(nextPlace);

                    //System.out.println(sock);
                    TCPSender sender = new TCPSender(sock);
                    sender.sendData(mess.eventData);
                    messagesSent++;
                    sumSent += mess.communicatedValue;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {

            Thread.sleep(15000);
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        System.out.println("Done sending messages");
        System.out.println("numsent " + messagesSent);
        System.out.println("sumsent " + sumSent);
        System.out.println("numrel " + numRel);
        System.out.println("numrec " + messagesRec);
        System.out.println("sumrec " + sumRec);

        TaskComplete done = new TaskComplete(myHostname,myPort);
        TCPSender sender = new TCPSender(regSock);
        sender.sendData(done.eventData);

    }

    /*
    send statistics back to registry
     */
    private synchronized void handleTaskSummaryRequest(TaskSummaryRequest event) throws IOException {
        TaskSummaryResponse taskSummaryResponse = new TaskSummaryResponse(myHostname,myPort,messagesSent,sumSent,messagesRec,sumRec,numRel);

        TCPSender toReg = new TCPSender(regSock);
        toReg.sendData(taskSummaryResponse.eventData);
        messagesRec = 0;
        messagesSent = 0;
        sumRec = 0;
        sumSent = 0;
        numRel = 0;
    }

    /*
    Simple main method that will start the server then put the main thread into taking user input.
    Information about the server should probably be stored as class variables
    Handles exceptions here at the highest possible level
     */
    public static void main(String[] argv){
        if(argv.length < 2){
            System.out.println("Incorrect starting arguments");
            System.exit(1);
        }

        String registryHostname = argv[0];
        int registryPortnumber = Integer.parseInt(argv[1]);

        /*
        all the stuff that happens here on startup should be refactored.

        so for it sends a register request to the registry

        handles respose
         */
        try{
            MessagingNode thisMachinesNode = new MessagingNode(registryHostname,registryPortnumber);
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = thisMachinesNode.serverObject.serverSocket.getLocalPort();
            System.out.println("Server running on: " + ip + " " + port);
            thisMachinesNode.sendRegisterRequest();
            thisMachinesNode.takeUserInput();
         }
        catch(IOException e){
            System.out.println("could not start server: " + e);
        }
    }
}
