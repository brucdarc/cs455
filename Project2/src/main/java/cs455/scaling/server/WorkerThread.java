package cs455.scaling.server;

public class WorkerThread implements Runnable{

    Task task;

    public WorkerThread(){
        task = null;
    }

    //allocate work according to thread pool in an infinite loop
    public void run(){
        while(true){
            try {
                task = ThreadPoolManager.addToPool(this);
                if(task != null) task.resolve();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
