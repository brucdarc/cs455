package cs455.scaling.server;
import cs455.scaling.datastructures.Batch;
import cs455.scaling.datastructures.TaskQueue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server{

    public static void main(String[] args){
        if(args.length < 1) System.exit(1);
        int port = Integer.parseInt(args[0]);

        System.out.println("starting server on port " + port);
        //grab the final single instance of the taskqueue that the pool manager uses
        TaskQueue taskQueue = ThreadPoolManager.taskQueue;
        //initialize the current batch for the first time here so that we know it
        //is created before we spawn any threads
        ThreadPoolManager.currentBatch = new Batch();

        //start the thread pool manager and give it the batch time
        ThreadPoolManager threadPoolManager = new ThreadPoolManager(Integer.parseInt(args[3]));
        Thread poolManagerThread = new Thread(threadPoolManager);
        poolManagerThread.start();

        //spawn as many workers as the args call for
        int numWorkers = Integer.parseInt(args[1]);
        for(int i =0; i<numWorkers;i++) {
            WorkerThread w = new WorkerThread();
            Thread thread = new Thread(w);
            thread.start();
        }


        try {
            Selector selector = Selector.open();



            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress("localhost",port);

            serverSocketChannel.bind(address);

            serverSocketChannel.configureBlocking(false);

            int ops = serverSocketChannel.validOps();
            SelectionKey selectionKey = serverSocketChannel.register(selector,ops);


            while(true) {
                //System.out.println("selecting");
                //selects the keys, but doesnt give them to you yet
                selector.select();
                //actually get the keys so that we can use them
                Set<SelectionKey> keys = selector.selectedKeys();
                //get an iterator from the keyset that will feed us keys
                Iterator<SelectionKey> keyIterator = keys.iterator();

                //go over all the keys and handle them
                while(keyIterator.hasNext()){
                    System.out.println("inside iterator loop: ");
                    SelectionKey currentKey = keyIterator.next();

                    //accept the key correctly
                    if(currentKey.isAcceptable()){
                        //create a task to be allocated to a worker thread
                        AcceptConnection acceptTask = new AcceptConnection(serverSocketChannel, currentKey, selector);
                        //add task to some sort of task queue
                        taskQueue.add(acceptTask);
                    }

                    //read the key if it needs to be read
                    else if(currentKey.isReadable()){
                        System.out.println("reading: ");
                        //create a task for receiving the messages
                        RecieveIncommingMessages recTask = new RecieveIncommingMessages(currentKey);
                        //add to the task queue
                        taskQueue.add(recTask);
                    }

                    //whatever happens make sure we take the key out of the set so we
                    //can move on to the next one
                    keyIterator.remove();
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}