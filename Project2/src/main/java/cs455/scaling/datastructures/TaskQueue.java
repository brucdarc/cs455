package cs455.scaling.datastructures;

import cs455.scaling.server.Task;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
wrapper data structure for the task queue that threads will pull from
acts like a queue and is synchronized for access to it
 */
public class TaskQueue {
    private BlockingQueue<Task> taskQueue;


    public TaskQueue(){
        synchronized (this) {
            taskQueue = new LinkedBlockingQueue<Task>();
        }
    }

    public void add(Task t){
        taskQueue.add(t);
    }

    public Task getNext(){
        return taskQueue.remove();
    }

    public Task peek(){
        return taskQueue.peek();
    }

    public boolean isEmpty(){
        return taskQueue.isEmpty();
    }





}
