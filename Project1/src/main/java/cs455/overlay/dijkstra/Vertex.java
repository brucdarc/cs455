package cs455.overlay.dijkstra;

import java.util.ArrayList;

/*
This class is an abstraction of a node in the network.
Is has an identifier which is its port and ip address smashed together. It also has edges connected to it, and a bestDistance
which is the best case distance to reach this node from whatever node this code is running on.
The best distance will be updated continuously as dijkstra's runs and it gets better

It can be compared to another vertex by bestdistance, and can tell if its equal if it has the same identifier

It also has a previous node so we can backtrack a shortest path when we finish dijkstras
 */

public class Vertex implements Comparable{
    public String identifier;
    public boolean visited;
    public int bestDistance;
    public ArrayList<Edge> edges;
    public Vertex previous;



    public Vertex(String identifier){
        edges = new ArrayList<Edge>();
        this.identifier = identifier;
        visited = false;
        previous = null;
        bestDistance = Integer.MAX_VALUE;
    }

    public void setPrevious(Vertex previous){
        this.previous = previous;
    }



    public boolean equals(Object o){
        if(! (o instanceof Vertex)) return false;
        Vertex other = (Vertex)o;
        if(other.identifier.equals(identifier)) return true;
        return false;
    }

    public int compareTo(Object o){
        Vertex other = (Vertex) o;
        if(other.bestDistance > this.bestDistance) return -1;
        if(other.bestDistance == this.bestDistance) return 0;
        return 1;
    }

    public String toString(){
        return identifier;
    }


}
