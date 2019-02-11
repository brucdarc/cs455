package cs455.overlay.dijkstra;
import cs455.overlay.dijkstra.Vertex;

public class Edge implements Comparable{
    public Vertex vertex1;
    public Vertex vertex2;
    public int weight;

    public Edge(Vertex v1, Vertex v2, int weight){
        vertex1 = v1;
        vertex2 = v2;
        this.weight = weight;
    }

    public int compareTo(Object o){
        Edge other = (Edge) o;
        if(other.weight > this.weight) return -1;
        if(other.weight == this.weight) return 0;
        return 1;
    }


}
