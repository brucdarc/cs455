
/*
This class will be the entry point for the project.
It will decide whether it is a registry or messaging node based on command line args
It will spawn a TCPserverthread, then idle in a while loop waiting for user commands
It will be able to interact with running threads to complete user commands


 */

import overlay.transport.TCPServerThread;

public class Startup {




//to start a registry node instead of a regular node, pass an additional arguments to startup class
public static void main(String[] argv){

    TCPServerThread serverThread;


    /*
    These 2 blocks will do things to specifically set this server up as a
    messenger or registry

    */

    if(argv.length>1){
        //initialize as registry
        serverThread = new TCPServerThread(true);
    }
    else{
        //initialize as messenger node
        serverThread = new TCPServerThread(false);
    }








}



}
