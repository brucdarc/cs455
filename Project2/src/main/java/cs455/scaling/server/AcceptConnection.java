package cs455.scaling.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;

public class AcceptConnection extends Task{
    private ServerSocketChannel serverSocketChannel;
    private SelectionKey key;
    private Selector selector;

    public AcceptConnection(ServerSocketChannel s, SelectionKey k, Selector sel){
        serverSocketChannel = s;
        key = k;
        selector = sel;
    }

    public void resolve() throws IOException{
        //grab the connection off of the top of the server channel
        SocketChannel incommingClientChannel = serverSocketChannel.accept();
        //make sure we dont block in our program
        incommingClientChannel.configureBlocking(false);
        //put the channel back into the keyset so we
        //can now read what it has to say to us
        //register the connection as readable for the selector to pick up
        incommingClientChannel.register(selector, SelectionKey.OP_READ);
    }

}