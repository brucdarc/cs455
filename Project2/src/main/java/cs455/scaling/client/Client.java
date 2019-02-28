package cs455.scaling.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class Client {



    public static void main(String[] args) throws Exception{
        if(args.length<2) {
            System.out.println("wrong arguments. Requires: serverPort sendingRate");
        }
        //know the server port
        int port = Integer.parseInt(args[0]);
        //determine the rate of message sending and set a corresponding time for a thread to sleep to achieve that.
        int rate = Integer.parseInt(args[1]);
        int sleepTime =  1000/rate;

        //initialize the socketChannel that will talk to the server
        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", port);
        SocketChannel crunchifyClient = SocketChannel.open(crunchifyAddr);

        Random rand = new Random();
        while(true){
            //create random 8kb message
            byte[] message = new byte[8192];
            rand.nextBytes(message);
            ByteBuffer buffer = ByteBuffer.wrap(message);
            //send the message
            crunchifyClient.write(buffer);
            //sleep for the right ammount of time so that we achieve the rate we want
            Thread.sleep(sleepTime);
        }

    }
}
