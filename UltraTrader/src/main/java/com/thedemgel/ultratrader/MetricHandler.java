package com.thedemgel.ultratrader;

import org.bukkit.Bukkit;
import org.mcstats.Metrics;

import java.io.IOException;

public class MetricHandler {
    public Metrics metrics;
    public Metrics.Graph numberOfShopsGraph;

    public MetricHandler() {
        try {
            metrics = new Metrics(UltraTrader.getInstance());
            numberOfShopsGraph();
            //metrics.start();
        } catch (IOException e) {
            Bukkit.getLogger().info("Metrics has failed to load...");
        }
    }

    private void npcVsBlockGraph() {

    }

    public Metrics getMetrics() {
        return metrics;
    }

    private void numberOfShopsGraph() {
        numberOfShopsGraph = metrics.createGraph("Number of Shops");

        numberOfShopsGraph.addPlotter(new Metrics.Plotter("Total Shops") {
            @Override
            public int getValue() {
                return UltraTrader.getStoreHandler().getShops().size();
            }
        });
    }
}
