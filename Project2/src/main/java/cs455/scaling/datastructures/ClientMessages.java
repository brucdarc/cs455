package cs455.scaling.datastructures;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class ClientMessages {

    private LinkedList<String> messages;
    private SocketChannel clientChannel;

    public ClientMessages(SocketChannel clientChannel){
        synchronized (this) {
            messages = new LinkedList<String>();
            this.clientChannel = clientChannel;
        }
    }

    public synchronized SocketChannel getClientChannel() {
        return clientChannel;
    }

    public synchronized void add(String message){
        messages.add(message);
    }
}
