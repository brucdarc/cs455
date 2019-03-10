package cs455.scaling.server;

import cs455.scaling.datastructures.Batch;
import cs455.scaling.datastructures.TaskQueue;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.Semaphore;

public class ThreadPoolManager implements Runnable{
    //initialize this as a final variable here so only one taskQueue is ever created on
    //a server
    public static final TaskQueue taskQueue = new TaskQueue();
    public static Batch currentBatch;
    //should never have more than 1000 threads running at once
    //sem is needed to control problem of having any number able to add
    //but needing to block all of them if creating a new batch
    public static Semaphore batchSem = new Semaphore(1000);
    private long batchTime;
    private long batchSize;

    public ThreadPoolManager(long batchSize, long batchTime){
        this.batchSize = batchSize;
        this.batchTime = batchTime;
    }



    /*
    the pool manager should check for different jobs that need to be done.
    it needs to keep track of the batch timer and process the current batch when that is up

    it also needs to allocate threads to accept connections, read data in, and process batches.

     */


    public static void allocateThread() throws Exception {
        //check the task queue and if it has something at the top grab it and give it to a this thread
        //this is a compound action so we must grab the lock for the taskQueue object
        synchronized (taskQueue) {
            if (!taskQueue.isEmpty()) {
                //grab a task out of the front of the queue and resolve it in the thread that is calling this method
                Task nextTask = taskQueue.getNext();
                nextTask.resolve();
            }
        }
    }


    //run method that will keep a thread spinning on checking batch times and
    //creating new batches
    //only 1 instance of ThreadPoolManager should ever exist at one.
    public void run(){
        Long batchStart = System.currentTimeMillis();
        ComputeAndSend sendTask;
        //initialize batch and time

        while(true){
            boolean isTimeUp = false; // System.currentTimeMillis() - batchStart > batchTime;
            boolean isBatchFull = (batchSize <= currentBatch.getCount().longValue());
            //System.out.println("Batch size " + batchSize + " is full? " + isBatchFull + " count " + currentBatch.getCount());
            //check if batch is full
            //check if batch time is up
            if(isTimeUp | isBatchFull){
                //System.out.println("Batch size " + batchSize + " is full? " + isBatchFull + " count " + currentBatch.getCount());
                try {
                    //aquire all permits so you wait for all adding to
                    //finish then prevent adding until you release them
                    batchSem.acquire(1000);
                    System.out.println("Processing batch");
                    //remove current batch
                    //create new batch
                    batchStart = System.currentTimeMillis();
                    Batch oldBatch = currentBatch;
                    currentBatch = new Batch();
                    //create a batch process task
                    sendTask = new ComputeAndSend(oldBatch);
                    //add to task queue
                    taskQueue.add(sendTask);
                }
                catch (Exception e){
                }
                finally {
                    //return permits to pool no matter what
                    batchSem.release(1000);
                }


            }
        }
    }
}
