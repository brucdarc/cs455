public class GrabKeyGrabberUnsafe implements Runnable{
    public String output;
    public GrabKey[] keys;
    public int index;

    public GrabKeyGrabberUnsafe(String s, GrabKey[] keys, int index){
        this.output = s;
        this.keys = keys;
        this.index = index;

    }
    public void run(){

        synchronized (keys[index]) {
            keys[index].print(output);
        }

    }
}