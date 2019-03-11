package cs455.scaling.server;

import cs455.scaling.datastructures.Batch;
import cs455.scaling.datastructures.ClientMessages;

public class ComputeAndSend extends Task {
    private Batch workload;

    //give the task a batch that it will process and send out
    public ComputeAndSend(Batch b){
        workload = b;
    }

    //use the batch
    //compute the hashes and send them back to the client
    public void resolve(){
        workload.processBatch();
    }
}
