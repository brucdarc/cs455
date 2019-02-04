package overlay.util;

import overlay.wireformats.Event;

import java.util.Queue;

/*
A wrapper class for a queue to make sure that access to an eventqueue is synchronized among threads

I believe that synchronized on two methods makes sure that only 1 of the methods can be executing at once
 */
public final class EventQueue {
    private Queue<Event> theQueue;


    public synchronized void add(Event e){
        theQueue.add(e);
    }

    public  synchronized Event remove(){
        return theQueue.remove();
    }


}
