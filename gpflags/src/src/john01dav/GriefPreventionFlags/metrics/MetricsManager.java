package src.john01dav.GriefPreventionFlags.metrics;

import java.io.IOException;

import org.bukkit.World;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;
import src.john01dav.GriefPreventionFlags.GriefPreventionFlags;
import src.john01dav.GriefPreventionFlags.metrics.Metrics.Graph;

public class MetricsManager {
	
	public static void StartMetrics() {
		try {
		    Metrics metrics = new Metrics(GriefPreventionFlags.instance);
	
		    // Construct a graph, which can be immediately used and considered as valid
		    Graph claimGraph = metrics.createGraph("Flags Used in Claims");
		    for(Type t : Type.values()){
		    	final Flag flag = new Flag(t);
		    	claimGraph.addPlotter(new Metrics.Plotter(t.toString()) {
		            @Override
		            public int getValue() {
	            		return GriefPreventionFlags.instance.FlagCounts[flag.getType().ordinal()];
		            }
		    	});
		    }
		    
		    Graph globalGraph = metrics.createGraph("Flags Used Globally");
		    for(Type t : Type.values()){
			    final Flag flag = new Flag(t);
		    	globalGraph.addPlotter(new Metrics.Plotter(t.toString()) {
		            @Override
		            public int getValue() {
		            	int count = 0;
		            	for(World w : GriefPreventionFlags.instance.getServer().getWorlds()) {
			            	if (flag.getValue(w) != null && flag.getValue(w) != flag.getType().getDefault()) {
			            		count ++;
			            	}
		            	}
		            	return count;
		            }
		    	});
		    }
		    
		    Graph unclaimedGraph = metrics.createGraph("Flags Used for Unclaimed Areas");
		    for(Type t : Type.values()){
			    final Flag flag = new Flag(t);
		    	unclaimedGraph.addPlotter(new Metrics.Plotter(t.toString()) {
		            @Override
		            public int getValue() {
		            	int count = 0;
		            	for(World w : GriefPreventionFlags.instance.getServer().getWorlds()) {
			            	if (flag.getUnclaimedValue(w) != null && flag.getUnclaimedValue(w) != flag.getType().getDefault()) {
			            		count ++;
			            	}
		            	}
		            	return count;
		            }
		    	});
		    }
		    
		    metrics.start();
		} catch (IOException e) {
		    GriefPrevention.instance.getLogger().info(e.getMessage());
		}
	}
}
