package cs455.overlay.dijkstra;

import cs455.overlay.wireformats.LinkInfo;
import cs455.overlay.wireformats.LinkWeights;
import cs455.overlay.dijkstra.Vertex;
import cs455.overlay.dijkstra.Edge;


import java.net.Socket;
import java.util.*;

public class ShortestPath {
    public Map<String, Vertex> verts;
    public ArrayList<Vertex> vertArr;
    public ArrayList<Edge> edges;




    /*
    This method takes the linkweights object that comes from the protocol,
    and transforms it into vertex and edge objects which are easier to work with.
    Vertex = MessagingNode
    Edge = link between two MessagingNodes
     */
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

    /*
    Runs dijkstras shortest path, on the transformed data stored in class variables
    it adds previous and best distance to vertex classes as it goes to store information
    about best paths
     */
    public ArrayList<Vertex> dijkstras(String startIdentifier){
        Vertex start = verts.get(startIdentifier);

        start.bestDistance = 0;

        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<Vertex>();

        priorityQueue.add(start);

        while(!priorityQueue.isEmpty()) {
            Vertex current = priorityQueue.poll();
            current.visited = true;

            for (Edge edge : current.edges) {
                Vertex other;
                if (edge.vertex1.equals(current)) other = edge.vertex2;
                else other = edge.vertex1;
                int pathWeight = current.bestDistance + edge.weight;
                if (other.bestDistance > pathWeight) {
                    other.bestDistance = pathWeight;
                    other.previous = current;
                    //priorityQueue.remove(other);
                    priorityQueue.add(other);

                }
            }
        }


        return vertArr;
    }

    /*
    iterates backward by the link list of previous that nodes have formed to get all the best paths,
    returns the shortest path to a given node as an arraylist
     */
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
    /*
    gets the shortest path to each node, looks to see which nodes messages need to go to next to get there,
    then adds the destination identifier and nexthop identifier to a map that allows the messagingNodes
    to quickly determine where to send an incomming message
     */

    public Map<String, String> makeNextHopMap() {
        Map<String, String> result = new HashMap<String,String>();

        for(Vertex vert: vertArr){
            ArrayList<Vertex> path =getSolutionPath(vert);
            //System.out.println("making map " + vert + " Path: " + path);
            if(path.size()>1) result.put(vert.identifier, path.get(1).identifier);
        }

        return result;

    }


}

