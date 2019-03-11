package cs455.scaling.client;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

public class ClientReceiver implements Runnable {

    SocketChannel myChannel;
    BlockingQueue<byte[]> expectedHashes;

    public ClientReceiver(SocketChannel myChannel, BlockingQueue<byte[]> expectedHashes){
        this.myChannel = myChannel;
        this.expectedHashes = expectedHashes;
    }

    public void run(){
        System.out.println("Starting receiver thread");

        while(true){
            try {
                //sha1 hashes are 160 bits long so we only need to allocate 20 bytes
                ByteBuffer messageBuffer = ByteBuffer.allocate(20);

                int bytesRead = 0;
                while (bytesRead != 20) {
                    bytesRead += myChannel.read(messageBuffer);
                }
                byte[] hash = messageBuffer.array();
                byte[] expected = expectedHashes.take();

                String hashString = new BigInteger(1,hash).toString();
                String expectedString = new BigInteger(1,expected).toString();
                //see if hashes match
                if(expectedString.equals(hashString)){
                    System.out.println("hashes match");
                }
                else{
                    //System.out.println("not the same " + expected.length() + " : " + hash.length);
                    System.out.println("Mismatched hash: " + expectedString + " : " + hashString);
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
