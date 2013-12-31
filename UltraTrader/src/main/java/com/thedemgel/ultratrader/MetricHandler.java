package com.thedemgel.ultratrader;

import org.bukkit.Bukkit;
import org.mcstats.Metrics;

import java.io.IOException;

public class MetricHandler {
    Metrics metrics;

    public MetricHandler() {
        try {
            metrics = new Metrics(UltraTrader.getInstance());
            numberOfShopsGraph();
            metrics.start();
        } catch (IOException e) {
            Bukkit.getLogger().info("Metrics has failed to load...");
        }
    }

    private void npcVsBlockGraph() {

    }

    private void numberOfShopsGraph() {
        Metrics.Graph numberOfShopsGraph = metrics.createGraph("Number of Shops");

        numberOfShopsGraph.addPlotter(new Metrics.Plotter() {
            @Override
            public int getValue() {
                return UltraTrader.getStoreHandler().getShops().size();
            }
        });
    }
}
