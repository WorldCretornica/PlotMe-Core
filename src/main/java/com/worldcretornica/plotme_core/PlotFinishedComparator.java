package com.worldcretornica.plotme_core;

import java.util.Comparator;

public class PlotFinishedComparator implements Comparator<Plot> {

    @Override
    public int compare(Plot plot1, Plot plot2) {
        if (plot1.getFinishedDate().compareTo(plot2.getFinishedDate()) == 0) {
            return plot1.getOwner().compareTo(plot2.getOwner());
        } else {
            return plot1.getFinishedDate().compareTo(plot2.getFinishedDate());
        }
    }
}
