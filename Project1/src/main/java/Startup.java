
/*
This class will be the entry point for the project.
It will decide whether it is a registry or messaging node based on command line args
It will spawn a TCPserverthread, then idle in a while loop waiting for user commands
It will be able to interact with running threads to complete user commands


 */

import overlay.transport.TCPServerThread;

import java.util.Scanner;

public class Startup {
/*
This method parses incoming commands and decides what to do with them.
Most commands will involve calling a method of TCPServerThread.
currently most commands have placeholders for when the they are implemented on the server.
 */
public static String doCommand(String input, TCPServerThread TCPserver){
    String[] words = input.split("\\s+");

    if(words[0].equals("test")){
        return "This is a test messageee.";
    }
    if(words[0].equals("list-messaging-nodes")){
        if(!isRegistry) return "Invalid command, this node is a messenger node";
        return "this is a placeholder until the serverthread can access messaging nodes list";
    }

    if(words[0].equals("list-weights")){
        if(!isRegistry) return "Invalid command, this node is a messenger node";
        return "This is a placeholder until serverthread has functionality to access weights";
    }

    if(words[0].equals("send-overlay-link-weights")){
        if(!isRegistry) return "Invalid command, this node is a messenger node";
        return "This is placeholder until server has functionality.";
    }

    //the following two cases need more fuctionallity
    //a variable must follow them.
    if(words[0].equals("setup-overlay")){
        if(!isRegistry) return "Invalid command, this node is a messenger node";
        return "This is placeholder until server has functionality.";
    }

    if(words[0].equals("start")){
        if(!isRegistry) return "Invalid command, this node is a messenger node";
        return "This is placeholder until server has functionality.";
    }


    System.out.println(words);
    return "Invalid command, type help for list of commands";
}


//to start a registry node instead of a regular node, pass an additional arguments to startup class
public static void main(String[] argv){

    TCPServerThread TCPserver;

    /*
    These 2 blocks will do things to specifically set this server up as a
    messenger or registry


    */




    TCPserver = new TCPServerThread(false);

    //spawn a thread that executes the run method of TCPServerThread
    //thread is actually different object, but hold TCPserver, so I can
    //call methods from TCPserver to get things, or make it do things
    Thread serverThread = new Thread(TCPserver);
    serverThread.start();


    Scanner inputScanner = new Scanner(System.in);
    while(true){
        String input = inputScanner.nextLine();
        String output = doCommand(input, TCPserver);
        System.out.println(output);
    }






}



}
