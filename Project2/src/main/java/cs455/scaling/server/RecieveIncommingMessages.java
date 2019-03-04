package cs455.scaling.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class RecieveIncommingMessages extends Task {

    SelectionKey key;

    public RecieveIncommingMessages(SelectionKey k){
        key = k;

    }



    //read all the messages possible out of a channel with incomming messages
    public void resolve() throws IOException{
        //grab the channel we already made for it
        SocketChannel clientChannel = (SocketChannel)key.channel();

        SocketAddress clientaddress = clientChannel.getRemoteAddress();
        //make a buffer to read the message
        ByteBuffer messageBuffer = ByteBuffer.allocate(8192);
        //read the message
        clientChannel.read(messageBuffer);
        //turn message and print it out for testing
        String result = new String(messageBuffer.array());
        System.out.println(result);

        //were going to need to add to the pool managers current batch,
        //so grab the lock so the pool manager doesnt try to create a new batch while we do that
        synchronized (ThreadPoolManager.currentBatch){
            //add message to the correct data structure, and correct place
            ThreadPoolManager.currentBatch.addMessage(result,clientChannel);
        }
    }
}
