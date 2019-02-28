package cs455.scaling;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class PlaygroundClient {



    public static void main(String[] argv) throws Exception{
        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 1111);
        SocketChannel crunchifyClient = SocketChannel.open(crunchifyAddr);
        int i = 0;
        while(true){
            String message = "Message " + i +"\n";
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            crunchifyClient.write(buffer);
            buffer.clear();
            i++;
        }


    }
}
