package overlay.dijkstra;

import java.util.ArrayList;

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
