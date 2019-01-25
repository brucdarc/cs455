import java.net.ServerSocket;

public class Registry extends Server implements Runnable{
    public int port;


    Registry(int port){
        this.port = port;

    }

    //return information about the registered messaging nodes to display to the terminal
    public String list_messaging_nodes(){
        return null;
    }

    //return information about the link wieghts to display back in the terminal
    public String list_weights(){
        return null;
    }

    //sets up an overlay datastructure,
    public String setup_overlay(int number_of_connections){
        return null;
    }



    //send message to all nodes with the entire overlay including link weights
    public String send_overlay_link_weights(){
        return null;
    }








    public void run(){
        ServerSocket serverSocket = this.startServer(port);
        while(true){








        }
    }


}
