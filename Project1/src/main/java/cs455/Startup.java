package cs455;

/*











                 This class has been refactored, and should only be used for reference












*/

/*
This class will be the entry point for the project.
It will decide whether it is a registry or messaging node based on command line args
It will spawn a TCPserverthread, then idle in a while loop waiting for user commands
It will be able to interact with running threads to complete user commands


 */

import cs455.overlay.transport.TCPServerThread;

import java.util.Scanner;

public class Startup {


public static void main(String[] argv){

    GrabKey a = new GrabKey();
    GrabKey b = new GrabKey();
    GrabKey[] keys = {a,b};

    GrabKeyGrabber threadA = new GrabKeyGrabber("aa",keys,0);
    GrabKeyGrabber threadB = new GrabKeyGrabber("bb",keys,0);
    GrabKeyGrabberUnsafe threadC = new GrabKeyGrabberUnsafe("cc",keys,0);

    Thread tA = new Thread(threadA);
    Thread tB = new Thread(threadB);
    Thread tC = new Thread(threadC);
     tA.start();
     tB.start();
    tC.start();





}



}
