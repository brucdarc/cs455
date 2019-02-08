import org.junit.Before;
import org.junit.Test;
import overlay.dijkstra.ShortestPath;
import overlay.node.MessagingNode;
import overlay.node.Registry;
import overlay.util.OverlayCreator;
import overlay.wireformats.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestShortestPath {


    @Test
    public void testDijkstras() throws Exception{
        LinkInfo l1 = new LinkInfo("aaaaa",1234,"bbbb",4563,3);
        LinkInfo l2 = new LinkInfo("hhh",1234,"aaa",4563,3);
        LinkInfo l3 = new LinkInfo("uu",1234,"gg",4563,2);
        LinkInfo l4 = new LinkInfo("owo",1234,"ss",4563,4);
        LinkInfo l5 = new LinkInfo("aaa",4563,"uu",1234,9);
        LinkInfo l6 = new LinkInfo("gg",4563,"owo",1234,9);
        LinkInfo l7 = new LinkInfo("ss",4563,"aaaaa",1234,9);
        LinkInfo l8 = new LinkInfo("bbbb",4563,"aaaaa",1234,9);
        LinkInfo l9 = new LinkInfo("gg",4563,"bbbb",4563,9);
        LinkInfo[] in = {l1,l2,l3,l4,l5,l6,l7,l8, l9};

        LinkWeights lw = new LinkWeights(5, in);
        ShortestPath sp = new ShortestPath();
        ArrayList<ShortestPath.Edge> edges = sp.initialize(lw);

        for(ShortestPath.Edge e:edges){
            System.out.println(e.weight + " " + e.vertex1.identifier + " " + e.vertex2.identifier);
        }

        for(ShortestPath.Vertex v: sp.vertArr){
            System.out.println(v.bestDistance + " " + v.identifier);
        }

        System.out.println();

        ArrayList<ShortestPath.Vertex> vs = sp.dijkstras("hhh:1234");

        for(ShortestPath.Vertex v : vs){
            System.out.println(v.bestDistance + " " + v.identifier);

        }

        assertEquals(vs.get(0).bestDistance,26);
        assertEquals(vs.get(1).bestDistance,23);
        assertEquals(vs.get(2).bestDistance,0);
        assertEquals(vs.get(3).bestDistance,3);
        assertEquals(vs.get(4).bestDistance,12);
        assertEquals(vs.get(5).bestDistance,14);
        assertEquals(vs.get(6).bestDistance,23);
        assertEquals(vs.get(7).bestDistance,27);
    }

}

