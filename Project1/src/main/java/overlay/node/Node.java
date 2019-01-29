package overlay.node;

import overlay.wireformats.Event;

import java.util.Scanner;

public abstract class Node {

    public abstract void onEvent(Event event);
    public abstract String doCommand(String input);

    public void takeUserInput(){
        Scanner inputScanner = new Scanner(System.in);
        while(true){
            String input = inputScanner.nextLine();
            String output = doCommand(input);
            System.out.println(output);
        }
    }


}
