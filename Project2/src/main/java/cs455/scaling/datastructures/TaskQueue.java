package cs455.scaling.datastructures;

import cs455.scaling.server.Task;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/*
wrapper data structure for the task queue that threads will pull from
acts like a queue and is synchronized for access to it
 */
public class TaskQueue {
    private Queue<Task> taskQueue;


    public TaskQueue(){
        synchronized (this) {
            taskQueue = new LinkedList<Task>();
        }
    }

    public synchronized void add(Task t){
        taskQueue.add(t);
    }

    public synchronized Task getNext(){
        return taskQueue.remove();
    }

    public synchronized Task peek(){
        return taskQueue.peek();
    }

    public synchronized boolean isEmpty(){
        return taskQueue.isEmpty();
    }





}
