# Project 2 #

This project contains a server that will accept and hash messages from a large number of clients.
The communications are done with Java Nio library. The server has a thread pool and does message batching.

## Scripts ##

### startup.sh ###

Takes arguments: machineList numberOfNodes serverIp serverPort messageRate batchSize batchTime (in milliseconds)
    
Run this program from a gnome terminal for it to properly open terminal tabs
    
### kill.sh ###

Arguments: userName

Kills all java processes of that user on each 120 machine. Use with you're username to kill all clients



## Classes ##

### Client ###
Sends hashes to server, starts client receiver thread, keeps track of messages sent and recieved.

### ClientReceiver ###
A thread that checks hashes coming back to clients verifies them and counts them.

### Batch ###
Holds heads of ClientMessage objects in a linked list

### ClientMessages ###
Contains a link list of messages for each client. Has logic to help a thread hash and send messages back to clients

### TaskQueue ###
A queue containing tasks for threads to do. Has thread safety build into it

### AcceptConnection ###
A task that takes a key and processes incoming connection

### RecieveIncommingMessages ###
A task that reads a message from a readable channel and adds it to the batch

### Server ###
The entry point of the Server program that spins on a serversocketchannel handling connecting and connected clients. Spawns all other threads.

### Task ###
An abstract class that defines a resolve method for some unit of work

### ThreadPoolManager ###
Handles thread allocation, sending out batches, and statistic collection and printing.

### WorkerThread ###
Adds itself to the thread pool, and resolves tasks as it is assigned them.

### Hash ###
Has a static message to hash byte arrays

### Playground + PlaygroundClient ###
Ignore, classes used for learning mechanics


