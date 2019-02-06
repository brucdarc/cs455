import org.junit.Before;
import org.junit.Test;
import overlay.node.MessagingNode;
import overlay.node.Registry;
import overlay.util.OverlayCreator;
import overlay.wireformats.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestOverlaySetup {
    ArrayList<Register> regs;

    @Before
    public void initialize() throws IOException {
        regs = new ArrayList<Register>();
        regs.add(new Register("a", 1111));
        regs.add(new Register("b", 1111));
        regs.add(new Register("c", 1111));
        regs.add(new Register("d", 1111));
        regs.add(new Register("e", 1111));


    }

    @Test
    public void TestEvenOpposite(){
        int a = OverlayCreator.findOppositeEven(4,10);
        assertEquals(9,a);

        int b = OverlayCreator.findOppositeEven(3,60);
        assertEquals(33,b);

        int c = OverlayCreator.findOppositeEven(0,6);
        assertEquals(3,c);

        int d = OverlayCreator.findOppositeEven(5,6);
        assertEquals(2,d);
    }
    /*
    test overlay creation for various values of nodes and links
     */

    @Test
    public void TestOverlay() throws Exception{
        int[][] result =    {{0, 1, 1, 1, 1,},
                            {1, 0, 1, 1, 1, },
                            {1, 1, 0, 1, 1, },
                            {1, 1, 1, 0, 1, },
                            {1, 1, 1, 1, 0, }};
        assertArrayEquals(result,OverlayCreator.createOverlay(5,4));

        int[][] result2 = {{0, 1, 0, 0, 1, 1, 0, 0, 1, },
                {1, 0, 1, 0, 0, 1, 1, 0, 0, },
                {0, 1, 0, 1, 0, 0, 1, 1, 0, },
                {0, 0, 1, 0, 1, 0, 0, 1, 1, },
                {1, 0, 0, 1, 0, 1, 0, 0, 1, },
                {1, 1, 0, 0, 1, 0, 1, 0, 0, },
                {0, 1, 1, 0, 0, 1, 0, 1, 0, },
                {0, 0, 1, 1, 0, 0, 1, 0, 1, },
                {1, 0, 0, 1, 1, 0, 0, 1, 0, }};

        assertArrayEquals(result2,OverlayCreator.createOverlay(9,4));


        int [][] result3 = {{0, 1, 1, 1, 0, 1, 1, 1, },
                {1, 0, 1, 1, 1, 0, 1, 1, },
                {1, 1, 0, 1, 1, 1, 0, 1, },
                {1, 1, 1, 0, 1, 1, 1, 0, },
                {0, 1, 1, 1, 0, 1, 1, 1, },
                {1, 0, 1, 1, 1, 0, 1, 1, },
                {1, 1, 0, 1, 1, 1, 0, 1, },
                {1, 1, 1, 0, 1, 1, 1, 0, },};

        assertArrayEquals(result3,OverlayCreator.createOverlay(8,6));

        int[][] result4 = {{0, 1, 0, 0, 0, 1, 0, 0, 0, 1, },
                {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, },
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 0, },
                {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, },
                {0, 0, 0, 1, 0, 1, 0, 0, 0, 1, },
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, },
                {0, 1, 0, 0, 0, 1, 0, 1, 0, 0, },
                {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, },
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 1, },
                {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, }};

        assertArrayEquals(result4,OverlayCreator.createOverlay(10,3));

        try{
            OverlayCreator.createOverlay(7,3);
            assertEquals(1,2);
        }
        catch (Exception e){
            System.out.println("correctly exited with failure for trying to create an overlay with odd nodes and odd links");
        }


    }

}
