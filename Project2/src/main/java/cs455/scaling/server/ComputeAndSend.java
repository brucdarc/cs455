package cs455.scaling.server;

import cs455.scaling.datastructures.Batch;
import cs455.scaling.datastructures.ClientMessages;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ComputeAndSend extends Task {
    private static BlockingQueue<Batch> workloads = new LinkedBlockingQueue<Batch>();

    //give the task a batch that it will process and send out
    public ComputeAndSend(Batch b){
        workloads.add(b);
    }

    //use the batch
    //compute the hashes and send them back to the client
    public void resolve(){
        synchronized (workloads) {
            try {
                Batch work = workloads.take();
                work.processBatch();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
