package overlay.node;

import overlay.wireformats.Event;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/*
I have chosen to make this class an abstract class instead of an interface so I can avoid repeating some code.
The takeuserinput method will be needed to be exactly the same for registry and messengers
 */
public abstract class Node {
    public ArrayList<Socket> sockets;


    public abstract void onEvent(Event event) throws IOException;
    /*
    This method is intended to be different for messaging nodes and registries.
    There are no commands shared between messengers and registries
    so this method should be completely different for both classes.
     */
    public abstract String doCommand(String input) throws IOException;

    /*
    This is a method that both children will use to take user input in a while loop. It reads a string
    from system in, then runs the do command on it, then prints the output. This method should be called after a
    server thread is spawned so that the thread can be running concurrently.
     */
    public void takeUserInput() throws IOException{
        Scanner inputScanner = new Scanner(System.in);
        while(true){
            String input = inputScanner.nextLine();
            String output = doCommand(input);
            System.out.println(output);
        }
    }


}
