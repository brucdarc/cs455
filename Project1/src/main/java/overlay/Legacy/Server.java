package overlay.Legacy;

import overlay.Legacy.Registry;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server{

//wrapper method for creating a server socket that gives additional information for error handling
//if for some reason the server cannot start up, and error message is shown and the program terminated
public ServerSocket startServer(int portNumber){
    try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
        return serverSocket;


    } catch (IOException e) {
        System.out.println("Could not establish server on port " + portNumber + " error: " + e);
        System.exit(1);
        return null;
    }

}

//test the first 10000 ports to find a port that is open on the machine
public int findOpenPort(){
    ArrayList<Integer> openports = new ArrayList<Integer>(10000);
    for( int portnumber = 1000; portnumber<10000; portnumber++){
        try (ServerSocket serverSocket = new ServerSocket(portnumber)) {
            serverSocket.close();
            return portnumber;
        }
        catch (IOException e) {return 0;}


    }
    return 0;

}












    public static void main(String[] args){

        try{
            ServerSocket s = new ServerSocket(4440);

            while(true){
                s.accept();
                System.out.println("running loop");
            }
        }
        catch(IOException e){
        }







        if(args.length<2){
            int portNumber = Integer.parseInt(args[0]);
            Registry registry = new Registry(portNumber);


        }

    }



}