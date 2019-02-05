import org.junit.Before;
import org.junit.Test;
import overlay.node.MessagingNode;
import overlay.node.Registry;
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

}
