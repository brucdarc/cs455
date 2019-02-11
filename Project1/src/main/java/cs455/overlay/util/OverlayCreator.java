package cs455.overlay.util;

public class OverlayCreator {





    public static int findOppositeEven(int index, int size){
        int offset = size/2;
        int opposite = (index+offset)%size;
        return opposite;
    }

    public static int findFirstOppositeOdd(int index, int size){
        int offset = size/2;
        int firstOpposite = (index+offset)%size;
        return firstOpposite;
    }

    public static int[][] createConnectionsTable(int numOfNodes){
        int[][] result = new int[numOfNodes][numOfNodes];
        return result;
    }

    public static void createRing(int[][] connections){
        int length = connections.length;
        for(int i = 0; i<length;i++){
            connections[i][ (i+1)%length ] = 1;
            if(i != 0) {
                connections[i][ i-1] = 1;
            }
            else connections[i][length-1] = 1;
        }
    }
    /*
    if there is an odd number of connections for an even number of nodes, we have to link the nodes directly opposite of
    each other in the graph to allow for an odd number of connections
     */

    public static void linkMiddleEven(int[][] connections){
        int length = connections.length;
        for(int i = 0; i<length;i++){
            int middleIndex = findOppositeEven(i,length);
            connections[i][middleIndex] = 1;
        }
    }

    /*
    counterclockwise means pointer will move counter clockwise as it adds pairs
    clockwise means pointer will move clockwise

    both even and odd graphs need to link to pairs of nodes rotating around the circle from the originating node.
    in even graphs, the node opposite the current node links only if an odd number of connections is specified
    in odd graphs there is no directly opposite node, and this method has 2 cases to initialize the variables in the
    beggining that handles both
     */

    public static void linkPairs(int[][] connections, int numberOfPairs){
        int length = connections.length;
        for(int index = 0; index<length;index++){
            int counterClockwise;
            int clockwise;

            if(length%2 == 1) {
                counterClockwise = findFirstOppositeOdd(index, length);
                clockwise = (counterClockwise + 1) % length;
            }
            else{
                int middle = findOppositeEven(index,length);
                clockwise = (middle+1)%length;
                if(middle != 0){
                    counterClockwise = middle-1;
                }
                else{
                    counterClockwise = length-1;
                }
            }

            for(int pairsDone = 0; pairsDone<numberOfPairs; pairsDone++){
                connections[index][counterClockwise] = 1;
                connections[index][clockwise] = 1;

                clockwise = (clockwise+1)%length;
                if(counterClockwise != 0) {
                    counterClockwise = counterClockwise-1;
                }
                else counterClockwise = length-1;
            }
        }
    }

    public static int[][] createOverlay(int numNodes, int numLinks) throws Exception{
        /*
        only case where less than 2 links per node is valid
         */
        if(numLinks == 1 && numNodes == 2){
            int[][] res = {{0,1},{1,0}};
            return res;
        }
        /*
        if too few connections specified to create connected graph
         */
        if(numLinks<2) throw new Exception("Minimum of 2 connections per node needed");


        if(numLinks>numNodes-1) throw new Exception("cannot have more connections than nodes-1");

        /*
        case of odd number of nodes and odd connections per node. Invalid impossible case, throw exception
         */
        if(numLinks%2 == 1 && numNodes%2==1) throw new Exception("Illegal node and link number both cannot be odd");
        /*
        case of odd number of nodes and even connections. Valid case
        case of even number of nodes. both even and odd number of links per node are valid combinations here
         */
        int[][] results = createConnectionsTable(numNodes);
        createRing(results);
        //if odd links and even nodes, link middle portions so that an odd links per node can be formed
        if(numLinks%2 == 1 && numNodes%2 == 0){
            linkMiddleEven(results);
        }
        //-1 because we linked the outter ring which counts as a pair that is already linked on the very outside
        int numPairs = (numLinks/2) - 1;
        linkPairs(results,numPairs);
        return results;
    }


    public static void printConnections(int[][] connections){
        int length = connections.length;
        for(int i = 0; i<length;i++){
            System.out.print("{");
            for(int j = 0; j<length;j++){
                System.out.print(connections[i][j] + ", ");
            }
            System.out.println("},");
        }


    }

    public static boolean checkConnections(int[][] connections){
        int length = connections.length;
        for(int i = 0; i<length;i++){
            for(int j = 0; j<length;j++){
                if(connections[i][j] != connections [j][i]) return false;
            }
        }
        return true;
    }

    public static void main(String[] args){
        int numNodes = Integer.parseInt(args[0]);
        int numLinks = Integer.parseInt(args[1]);
        try {
            int[][] connections = createOverlay(numNodes, numLinks);
            printConnections(connections);
            System.out.println(checkConnections(connections));
        }
        catch (Exception e){
            System.out.println(e);
        }

    }


}
