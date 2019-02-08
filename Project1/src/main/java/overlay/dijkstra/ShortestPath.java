package overlay.dijkstra;

import overlay.wireformats.LinkInfo;
import overlay.wireformats.LinkWeights;


import java.util.*;

public class ShortestPath {
    public Map<String, Vertex> verts;
    public ArrayList<Vertex> vertArr;
    public ArrayList<Edge> edges;





    public ArrayList<Edge> initialize(LinkWeights overlay){
        vertArr = new ArrayList<Vertex>();
        Map<String, Vertex> verts = new HashMap<String, Vertex>();
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for(LinkInfo info: overlay.linkInfos){
            Vertex v1 = new Vertex(info.hostnameA + ":" + info.portNumberA);
            Vertex v2 = new Vertex(info.hostnameB + ":" + info.portNumberB);
            if(!verts.containsKey(v1.identifier)) {
                verts.put(v1.identifier,v1);
                vertArr.add(v1);
            }
            if(!verts.containsKey(v2.identifier)){
                verts.put(v2.identifier,v2);
                vertArr.add(v2);
            }

            Vertex correctV1 =  verts.get(v1.identifier);
            Vertex correctV2 =  verts.get(v2.identifier);


            Edge edge = new Edge(correctV1,correctV2,info.weight);
            edges.add(edge);
            correctV1.edges.add(edge);
            correctV2.edges.add(edge);

        }

        this.vertArr = vertArr;
        this.verts = verts;
        this.edges = edges;

        return edges;
    }

    public ArrayList<Vertex> dijkstras(String startIdentifier){
        Vertex start = verts.get(startIdentifier);

        start.bestDistance = 0;

        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<Vertex>();

        priorityQueue.add(start);

        while(!priorityQueue.isEmpty()){
            Vertex current = priorityQueue.poll();
            current.visited = true;

            for(Edge edge: current.edges){
                Vertex other;
                if(edge.vertex1.equals(current)) other = edge.vertex2;
                else other = edge.vertex1;
                int pathWeight = current.bestDistance + edge.weight;
                if(other.bestDistance > pathWeight){
                    other.bestDistance = pathWeight;
                    other.previous = current;
                    priorityQueue.remove(other);
                    priorityQueue.add(other);

                }

            }


        }

        return vertArr;
    }

    public ArrayList<Vertex> getSolutionPath(Vertex end){
        ArrayList<Vertex> path = new ArrayList<Vertex>();
        path.add(end);
        Vertex current = end;
        while(current.bestDistance != 0){
            current = current.previous;
            path.add(0,current);
        }

        return path;
    }
    



    public class Vertex implements Comparable{
         public String identifier;
         public boolean visited;
         public int bestDistance;
         public ArrayList<Edge> edges;
         public Vertex previous;



         private Vertex(String identifier){
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

    public static void main(String[] argv) {

    }


}

