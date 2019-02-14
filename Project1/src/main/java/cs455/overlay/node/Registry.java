package cs455.overlay.node;

import cs455.overlay.transport.TCPSender;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.util.OverlayCreator;
import cs455.overlay.wireformats.*;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*
This class is a dataStructure that contains the linkWeights and messagingNodesList

The overlayCreator will probably need to be called on an instance of this class

This node will need to be able to handle all events that can happen to a registry
If a registry gets an event meant for a messaging node, it should throw an exception


 */
public class Registry extends Node{
    private LinkWeights linkWeights;
    private MessagingNodesList messagingNodesList;
    private TCPServerThread serverObject;
    private Thread serverThread;
    public ArrayList<Register> messengerNodes;
    public int port;
    public Map<String, Socket> sockets;
    int[][] connectionsTable;
    public int completeCounter;

    public int sumSent;
    public int sumRec;

    public LinkedHashMap<String,int[]> taskStats;


    @Override
    public void onEvent(Event event) throws IOException{
        if(event instanceof Register) register((Register)event);
        if(event instanceof Deregister) deregister((Deregister)event);
        if (event instanceof TaskComplete) handleTaskComplete((TaskComplete) event);
        if (event instanceof TaskSummaryResponse) handleTaskSummaryResponse((TaskSummaryResponse) event);

    }


    /*
    This constructor creates a TCPServerthread object,
    then wraps that in a thread object and starts the thread to begin server execution concurrently
    pass true to the serverObject so that it knows it is a registry
    pass this node into serverThread so it knows who its node is
     */

    public Registry(int port) throws IOException{
        this.sockets = new HashMap<String, Socket>();
        serverObject = new TCPServerThread(port, this);
        serverThread = new Thread(serverObject);
        messengerNodes = new ArrayList<Register>();
        serverThread.start();
        this.port = port;
        sumSent = 0;
        sumRec = 0;
    }

    /*
    This method is overriden, and should only take commands that are applicable to a registry node
    All commands should call to the server to make something happen
     */
    @Override
    public String doCommand(String input) {
        try {
            String[] words = input.split("\\s+");
            switch (words[0]) {
                case "test":
                    return "Test message";
                case "list-messaging-nodes":{
                    String result = "";
                    for(Register r:messengerNodes){
                        result += r.IPAddress + ":" +r.port + "\n";
                    }
                    return result;
                }

                case "list-weights":
                    return linkWeights.toString();
                case "send-overlay-link-weights":{
                    for(Socket socket:sockets.values()){
                        TCPSender sender = new TCPSender(socket);
                        sender.sendData(linkWeights.eventData);
                    }
                    return "Sent link weights to all nodes";
                }

                case "setup-overlay": {
                    try {
                        if (words.length < 2)
                            throw new IOException("Too few arguments specify number of links per node");
                        int links = Integer.parseInt(words[1]);
                        constructOverlay(links);
                        createLinkWeights();
                        return "successfully sent overlay information to messaging nodes";
                    } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                        return "failed to setup overlay";
                    }

                }
                case "start": {
                    if (words.length < 2) throw new IOException("Too few arguments specify rounds");
                    taskStats = new LinkedHashMap<String,int[]>();
                    TaskInitiate taskInitiate = new TaskInitiate(Integer.parseInt(words[1]));
                    for(int index = 0; index<messengerNodes.size();index++){
                        Socket toNode = messengerNodes.get(index).socket;
                        TCPSender sender = new TCPSender(toNode);
                        sender.sendData(taskInitiate.marshal());
                        completeCounter = 0;
                    }
                    return "sent all nodes task initiate with number of rounds";

                }

                default:
                    return "Command not recognized";

            }
        }
        catch (Exception e){
            return "Something went wrong with the command " + e;
        }
    }
    //accessor method
    public MessagingNodesList getMessagingNodesList() {
        return messagingNodesList;
    }
    //accessor method
    public LinkWeights getLinkWeights() {
        return linkWeights;
    }

    //handles the register event
    /*
    the register is invalid if there is an ip matmatch between socket and request, or if the ip is already registered.

    a register response is sent back to the mess node to tell it if it registered successfully.
    if the registration was valid the connection is added to the sockets map that maps ip strings to sockets
     */

    // TODO: 2/4/19
    public synchronized void register(Register registerRequest) throws IOException{
        //default respose for successful registration
        RegisterResponse registerResponse = new RegisterResponse((byte)0,"registered successfully");
        Socket sock = registerRequest.socket;
        String ip = registerRequest.IPAddress;
        String key = ip + ":" + registerRequest.port;

        synchronized (sockets) {
            /*
            Contains because the socket address often has extra information
            test if ip mismatch
             */
            if (!sock.getRemoteSocketAddress().toString().contains(ip)) {
                registerResponse = new RegisterResponse((byte) 1, "connection ip does not match ip in request");
                System.out.println("IP " + ip + " failed to register: mismatched hostnames");
            }

            // if there ip is already registered, tell it that
            else if (sockets.containsKey(key)) {
                registerResponse = new RegisterResponse((byte) 1, "Your ip:port is already resgistered");
                System.out.println("Ip " + ip + " tried to register twice");
            }
            //if the registration is valid add it to the table
            else {

                sockets.put(key, sock);
                messengerNodes.add(registerRequest);
                System.out.println("Revieved registration from " + ip);
                System.out.println("Size of messengernodes array: " + messengerNodes.size());
            }

        }
        TCPSender sender = new TCPSender(sock);
        sender.sendData(registerResponse.eventData);



    }
    /*
    functions similarly to register, but has different cases for failure, and remove the node from list instead of adding it
     */

    public synchronized void deregister(Deregister deregisterRequest) throws IOException{
        DeregisterResponse deregisterResponse = new DeregisterResponse((byte)0,"registered successfully");
        Socket sock = deregisterRequest.socket;
        String ip = deregisterRequest.IPAddress;
        String key = ip + ":" + deregisterRequest.port;

        synchronized (sockets) {
            /*
            Contains because the socket address often has extra information
            test if ip mismatch
             */
            if (!sock.getRemoteSocketAddress().toString().contains(ip)) {
                deregisterResponse = new DeregisterResponse((byte) 1, "connection ip does not match ip in request");
                System.out.println("IP " + ip + " failed to deregister: mismatched hostnames");
            }

            // if there ip is already registered, tell it that
            else if (!sockets.containsKey(key)) {
                deregisterResponse = new DeregisterResponse((byte) 1, "Your ip is not resgistered");
                System.out.println("Ip " + ip + " tried to deregister but was not in regitry");
            }
            //if the registration is valid add it to the table
            else {
                sockets.remove(key);
                /*
                using a removeif to remove the register with the ip address of the node
                that want to deregister
                 */
                messengerNodes.removeIf(register -> {
                    if(register.IPAddress.equals(ip) && register.port == deregisterRequest.port) return true;
                    else return false;
                });
                System.out.println("Revieved deregistration from " + ip);
                System.out.println("Size of messengernodes array: " + messengerNodes.size());
            }

        }
        TCPSender sender = new TCPSender(sock);
        System.out.println("sending to " + sock);
        sender.sendData(deregisterResponse.eventData);



    }

    /*
    construct the overlay by looping through each messaging node, and connecting it
    to the next consecutive n elements wrapping around the node list if necessary.

   There will be a method run before this that makes sure that the graph type is legal before trying
   to set it up.

   runs the connections table set up from the overlay creator class. It returns table of ints where 1 represents a link
   and 0 represents no link. If the setup is illegal, overlay creator will throw an exception
     */

    public void constructOverlay(int numberOfNeighbors) throws Exception{
        connectionsTable = OverlayCreator.createOverlay(messengerNodes.size(),numberOfNeighbors);

        for(int index = 0; index<messengerNodes.size();index++){
            ArrayList<MessagingNodeInfo> neighbors = getConnectionsFromNode(connectionsTable[index],index);
            MessagingNodesList connections = new MessagingNodesList(neighbors.size(), neighbors.toArray(new MessagingNodeInfo[neighbors.size()]));
            Socket toNode = messengerNodes.get(index).socket;
            TCPSender sender = new TCPSender(toNode);
            sender.sendData(connections.marshal());
        }

        MessagingNodeInfo[] messagingNodeInfos = new MessagingNodeInfo[messengerNodes.size()];
        for(int i = 0;i<messengerNodes.size();i++){
            Register r = messengerNodes.get(i);
            MessagingNodeInfo mn = new MessagingNodeInfo(r.IPAddress,r.port);
            messagingNodeInfos[i] = mn;
        }
        messagingNodesList = new MessagingNodesList(messagingNodeInfos.length,messagingNodeInfos);
    }

    /*
    helper method that does an inner loop for construct Overlay.
    takes one connection array of into and creates an arraylist of connections to be sent to a particular node
     */

    public ArrayList<MessagingNodeInfo> getConnectionsFromNode(int[] connections, int indexOfNode) throws IOException{
        ArrayList<MessagingNodeInfo> result = new ArrayList<MessagingNodeInfo>();
        for(int neighbor = 0; neighbor<connections.length; neighbor++){
            if(connections[neighbor] == 1){
                String linkedHostname = messengerNodes.get(neighbor).IPAddress;
                int linkedPort = messengerNodes.get(neighbor).port;
                MessagingNodeInfo linked = new MessagingNodeInfo(linkedHostname,linkedPort);
                result.add(linked);
            }
        }
        return result;
    }


    public void createLinkWeights() throws IOException{
        int numNodes = connectionsTable.length;
        ArrayList<LinkInfo> linkInfos = new ArrayList<LinkInfo>();
        for(int i = 0;i<numNodes;i++){
            for(int k = i;k<numNodes;k++){
                if(connectionsTable[i][k] == 1) {
                    Register node1 = messengerNodes.get(i);
                    Register node2 = messengerNodes.get(k);
                    //random weight 1 through 9
                    int weight = ThreadLocalRandom.current().nextInt(1,10);
                    
                    LinkInfo link = new LinkInfo(node1.IPAddress, node1.port, node2.IPAddress, node2.port, weight);
                    linkInfos.add(link);
                }

            }
        }

        LinkWeights result = new LinkWeights(linkInfos.size(), linkInfos.toArray(new LinkInfo[linkInfos.size()]));
        this.linkWeights = result;

    }


    public synchronized void handleTaskComplete(TaskComplete t) throws IOException{
       completeCounter++;
       if(completeCounter == messengerNodes.size()){
           System.out.println("All nodes have completed sending messages");

           try {
               Thread.sleep(2000);
           }
           catch (InterruptedException e){
               System.out.println("YOU SHALL NOT INTERRUPT ME");
           }

           TaskSummaryRequest req = new TaskSummaryRequest();

           for(Socket socket:sockets.values()){
               TCPSender sender = new TCPSender(socket);
               sender.sendData(req.eventData);
           }
       }





    }


    private synchronized void handleTaskSummaryResponse(TaskSummaryResponse event) {
        int[] nodeStats = {event.numberOfMessagesSent,event.sumOfSentMessages,event.numberOfMessagesReceived,event.sumOfReceivedMessages,event.numberOfMessagesRelayed};
        String ident = event.nodeIPAddress + ":" + event.nodePortNumber;
        taskStats.put(ident,nodeStats);

        if(taskStats.size() == messengerNodes.size()){
            System.out.println("Node Address \t\t" + String.format("%15s%15s%15s%15s%15s", "Num Sent", "Sum sent", "Num Rec", "Sum Rec", "Num Relayed" ));
            int[] results = {0,0,0,0,0};

            for(String key: taskStats.keySet()){
                int [] data = taskStats.get(key);
                for(int k = 0; k<5;k++){
                    results[k] += data[k];
                }
                System.out.println(key + " \t " + printArray(data));
            }

            System.out.println("Totals \t\t\t "  + printArray(results));
        }
    }

    public String printArray(int[] input){
        String result = "";


        for(int i = 0; i< input.length; i++){
            result += String.format("%15d",input[i]);
        }

        return result;
    }



    /*
        Simple main method that will start the server then put the main thread into taking user input.
        Information about the server should probably be stored as class variables
        Handles exceptions here at the highest possible level
        */
    public static void main(String[] argv){
        if(argv.length < 1){
            System.out.println("specify port number");
            System.exit(1);
        }

        int port = Integer.parseInt(argv[0]);

        try {
            Registry thisMachinesRegistry = new Registry(port);
            thisMachinesRegistry.takeUserInput();
        }
        catch(IOException e){
            System.out.println("could not start server: " + e);
        }
    }
}
