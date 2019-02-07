package overlay.dijkstra;

import overlay.wireformats.LinkInfo;
import overlay.wireformats.LinkWeights;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ShortestPath {





    public ArrayList<Edge> initialize(LinkWeights overlay){
        Map<String, Vertex> verts = new HashMap<String, Vertex>();
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for(LinkInfo info: overlay.linkInfos){
            Vertex v1 = new Vertex(info.hostnameA + ":" + info.portNumberA);
            Vertex v2 = new Vertex(info.hostnameB + ":" + info.portNumberB);
            if(!verts.containsKey(v1)) verts.put(v1.identifier,v1);
            if(!verts.containsKey(v2)) verts.put(v2.identifier,v2);


            edges.add(new Edge(v1,v2,info.weight));
        }

        return edges;
    }

    public ArrayList<ArrayList<Vertex>> dijkstras(ArrayList<Edge> edges){
        PriorityQueue<Edge> minEdges = new PriorityQueue<Edge>(edges);





        return null;
    }


    public class Vertex{
         String identifier;
         boolean visited;
         ArrayList<Edge> edges;
         Vertex previous;



         private Vertex(String identifier){
             this.identifier = identifier;
             visited = false;
             previous = null;
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


    }



    public class Edge implements Comparable{
        public Vertex vertex1;
        public Vertex vertex2;
        public int weight;

        private Edge(Vertex v1, Vertex v2, int weight){
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

}

