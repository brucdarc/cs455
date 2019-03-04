package cs455.scaling.server;

public class WorkerThread implements Runnable{


    //allocate work according to thread pool in an infinite loop
    public void run(){
        while(true){
            try {
                ThreadPoolManager.allocateThread();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
