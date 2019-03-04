package cs455.scaling.datastructures;

import cs455.scaling.client.Client;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class Batch {
    /*
    this class needs to contain one batch.
     */
    private LinkedList<ClientMessages> clients;

    //simple constructor create datastructure
    public Batch(){
        clients = new LinkedList<ClientMessages>();
    }

    //add a message to a specific clients linked list
    public void addMessage(String message, SocketChannel client){
        //figure out which client the message is from and add it to that clients linked list
        for(ClientMessages c: clients){
            //make do only for client that works
            if(c.getClientChannel().equals(client)){
                c.add(message);
                //return so the rest of loop doesn't waste time
                return;
            }
        }

    }

    public void processBatch(){
        //compute hashes and send them back to each client
    }





}
