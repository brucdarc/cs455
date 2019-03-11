package cs455.scaling.datastructures;

import cs455.scaling.client.Client;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Batch {
    /*
    this class needs to contain one batch.
     */
    private LinkedList<ClientMessages> clients;
    private volatile AtomicLong count;
    private Set<SocketChannel> setOfClients;

    //simple constructor create datastructure
    public Batch(){
        clients = new LinkedList<ClientMessages>();
        count = new AtomicLong(0);
        setOfClients = new HashSet<SocketChannel>();

    }

    public synchronized AtomicLong getCount() {
        return count;
    }

    //add a message to a specific clients linked list
    public void addMessage(byte[] message, SocketChannel client){

        //figure out which client the message is from and add it to that clients linked list

        /*
        This statement looks horrible, but if you trace through the parallel execution
        carefully, this section actually makes everything much faster and safer
         */
        //check if you need to add before synchronizing, so if a thread doesnt need to add
        //we dont slow down with synchronization barriers
        if (!setOfClients.contains(client)) {
            //multiple threads could enter here though for adding the same client
            //make sure they go sequentially after each other
            synchronized (setOfClients) {
                //only add if you are the first thread though this block
                //if not skip adding, because the thread who passed synchronization first
                //already added it
                if (!setOfClients.contains(client)) {
                    //compound check then add operation needs to be synchronized
                    try {
                        System.out.println("adding client " + client.getRemoteAddress());
                    }
                    catch (Exception e){}
                    ClientMessages newClient = new ClientMessages(client);
                    //add puts element at end of linked list, so we can traverse list
                    //to add messages at the same time as adding clients
                    setOfClients.add(client);
                    clients.add(newClient);
                }
            }
        }

        //traverses starting at head
        for(ClientMessages c: clients){
            //make do only for client that works
            if(c.getClientChannel().equals(client)){
                c.add(message);
                synchronized (this) {
                    count.getAndIncrement();
                }
                //System.out.println("adding message " + count);
                //return so the rest of loop doesn't waste time
                return;
            }
        }

        /*
        if a client isnt in this list, aka execution makes it to here, add that client in
         */


    }

    public void processBatch(){
        //compute hashes and send them back to each client

        for(ClientMessages cl: clients){
            System.out.println("sending back to " + cl.getClientChannel());
            cl.sendMessages();
        }
    }





}
