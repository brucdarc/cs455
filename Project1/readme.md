#Distributed Systems Project 1 #

##Introduction ##
This is a piece of software that allows for the creation and usage of a simple distributed network for sending random messages. 

There are two types of servers, a MessagingNode that participates in the task, and the Registry which is like the control center.

There is a bash script included that will start the program on different machines for you using the ssh command, and assumes you have ssh keys set up.

##Startup Instructions ##
Run the startup.sh bash script using a gnome terminal. It will not open tabs for the messaging nodes nicely otherwise.

The script takes 4 required arguments

Machine List: A list that contains the name of the CS department machines to use a messaging nodes. There is one for the 120 lab in the folder named machines.

Number of Nodes: How many Messaging Nodes to Start

Registry Ip: The IP of the machine you are currently running on

Registry Port: The port you want to open the registry server on.

The script will spawn tabs in the same terminal for each messaging node and a seperate terminal running the registry.

To start message sending run the setup-overlay, send-overlay-link-weights, and start commands.

##Registry Commands ##

list-messaging-nodes: Prints out the IP and Port of all registered messaging nodes.

list-weights: Prints the vertices and weight of each link in the network

setup-overlay number-of-connections: creates number-of-connections to another node at every node

send-overlay-link-weights: Sends information about the edges of the graph to each node so it knows how to route messages

start number-of-rounds: tells each node to send a number of rounds, each round involves sending 5 messages to a randomly chosen sink. Displays statistics when done.

##Messenging Node Commands ##

print-shortest-path: Outputs the best way to send a message to each destination in the network

exit-overlay; Deregisters a node and removes it from the system.

register: Manually re-register a node after exiting the overlay.

##Description of Classes ##

####dijkstras ###

Edge: Represents an edge in the network graph. Has two vertices and a weight

Vertex: Represents a vertex in the graph. Has identifier for a machine.

Shortest Path: Puts the overlay data into edge and vertex classes then runs Dijkstras Shortest Path to figure out how to route packets in the network.

####node ###

Node: An abstract class that shares server functionality between Registry and MessagingNodes

MessagingNode: Main method starts a messaging node server. Communicates with registry and sends messages to other messaging nodes

Registry: A main control center for all the messaging nodes. Handles the creation of the network between the nodes, and sends commands to the nodes.

####transport ###

TCPReceiverThread: A thread that spins on an already open socket waiting for input, then invokes registry onevent function when it get something

TCPSender: Constructed with a socket, has a method that sends an byte array over that socket. It is not a thread.

TCPServerThread: Keeps a serversocket open, creates receiver threads when it gets connections

####util ###

Overlay Creator: Runs an algorithm to determine if a network setup that the user asked for is legal, and if so creates a table of connections, where each row is a node, and if there is a 1 in the column that represents a connection to another node. The registry uses this to send all the nodes their neighbors.

Odd Number of Nodes and Odd Number of Connections per node is the only illegal graph case, all others are valid. 

####wireformats ###

Event: An interface that all protocols implement

Protocol: An abstract class that is extended by anything that can marshal and demarshal itself to be send over the network.
These can be constructed from a byte array of class variables, and will create the other in the constructor.

Deregister: Deregister a node

DeregisterResponse: Tell node it was deregistered successfully

EventFactory: Turn an array of bytes into an event type object

LinkInfo: A wrapper class for a link in the linkweights protocol.

LinkWeights: Send to nodes to describe network

Message: Has a payload and destination

MessagingNodeInfo: Wrapper class for one neighbor send in MessagingNodesList protocol

MessagingNodesList: Tells each node who to connect to.

Register: MessagingNode tells the Registry it exists and is open for business

RegisterResponse: Registry tells the node if it was able to register correctly.

TaskInitiate: Registry tells each node to send messages in a number of rounds.

TaskComplete: Each node tells registry it has finished sending rounds of messages

TaskSummaryRequest: Registry requests statistics about message sending command

TaskSummaryResponse: MessagingNodes send Registry Statistics


