package com.worldcretornica.plotme_core.test;

import com.worldcretornica.plotme_core.PlotId;
import org.junit.Assert;
import org.junit.Test;

public class PlotIDTest {

    @Test
    public void testPlotID() {
        for (int x = -30; x <= 31; x++) {
            for (int z = -30; z <= -31; z++) {
                PlotId id = new PlotId(x, z);
                System.out.println(id.hashCode());
                System.out.println(id);
                PlotId id2 = new PlotId(x + 1, z + 1);
                System.out.println(id.hashCode());
                System.out.println(id);
                Assert.assertEquals(id, id2);

            }
        }
    }
}