package src.john01dav.GriefPreventionFlags.commands;

import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.Cluster;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.GriefPreventionFlags;
import src.john01dav.GriefPreventionFlags.Messages;

public class Cluster_Global extends Command {
	protected static boolean set(CommandSender sender, String[] args) {
		World world = null;
		List<String> cluster = null;
		String value = null;
		String clusterName = null;
		
		// Global - handles console different than player
		if (!(sender instanceof Player)) {
			if (args.length != 3) {
				sender.sendMessage("setclusterglobal <world> <cluster> <value>");
				return true;
			}
			
			world = GriefPreventionFlags.instance.getServer().getWorld(args[0]);
			
			if (world == null) {
				sender.sendMessage(args[0] + " is not a valid world.");
				return true;
			}
			
			// Acquire the cluster type requested in the command
			cluster = Cluster.getCluster(args[1].toLowerCase());
			if (cluster == null) { 
				sender.sendMessage(Messages.InvalidFlagError.get().replaceAll("<2>", args[1]).replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
				return true; 
			}
			clusterName = args[1];
			value = args[2];
		} else {
			if (args.length != 2) { return false; }
			world = ((Player)sender).getWorld();
			
			// Acquire the cluster type requested in the command
			cluster = Cluster.getCluster(args[0].toLowerCase());
			if (cluster == null) { 
				sender.sendMessage(Messages.InvalidFlagError.get().replaceAll("<2>", args[0]).replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
				return true; 
			}
			clusterName = args[0];
			value = args[1];
		}
		
		// Determine new flag state
        Boolean isAllowed;
        try{
           	isAllowed = Boolean.parseBoolean(value);
        }catch(Exception e){
           	sender.sendMessage(Messages.ValueError.get());
           	return true;
        }
        
		Flag flag = new Flag();
		boolean success = true;
        // Set the flags
		
        for (String f : cluster) {
        	if (flag.setType(f)) {
       			if(!flag.setValue(world, isAllowed, sender)) {
       				success = false;
       			}
       			continue;
        	}
        	success = false;
        	sender.sendMessage("Invald clusters.yml entry: " + f);
        }
        
        if(success) {
        	sender.sendMessage(Messages.SetCluster.get().replaceAll("<2>", clusterName).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
        } else {
        	sender.sendMessage(Messages.SetMultipleFlagsError.get());
        }
        return true;
	}
	
	protected static boolean delete(CommandSender sender, String[] args) {
		// Check for proper command formatting
		if(args.length > 2){ return false; }

		World world = null;
		List<String> cluster = null;
		String clusterName = null;
		
		// Global - handles console different than player
		if (!(sender instanceof Player)) {
			if (args.length != 2) {
				sender.sendMessage("removeclusterglobal <world> <cluster>");
				return true;
			}
			
			world = GriefPreventionFlags.instance.getServer().getWorld(args[0]);
			
			if (world == null) {
				sender.sendMessage(args[0] + " is not a valid world.");
				return true;
			}
			
			// Acquire the cluster type requested in the command
			cluster = Cluster.getCluster(args[1].toLowerCase());
			if (cluster == null) { 
				sender.sendMessage(Messages.InvalidFlagError.get().replaceAll("<2>", args[1]).replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
				return true; 
			}
			
			clusterName = args[1];
		} else {
			if (args.length != 1) { return false; }
			world = ((Player)sender).getWorld();
			
			// Acquire the cluster type requested in the command
			cluster = Cluster.getCluster(args[0].toLowerCase());
			if (cluster == null) { 
				sender.sendMessage(Messages.InvalidFlagError.get().replaceAll("<2>", args[0]).replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
				return true; 
			}
			clusterName = args[0];
		}
		
		Flag flag = new Flag();
		boolean success = true;
		
		// Removing all flags
		for (String f : cluster) {
			flag.setType(f);
        	if (flag.setType(f)) {
    			if(!flag.removeValue(world, sender)) {
    				success = false;
    			}
    			continue;
        	}
        	success = false;
       		sender.sendMessage("Invald clusters.yml entry: " + f);
		}
		
		if (success) {
			sender.sendMessage(Messages.RemoveCluster.get().replaceAll("<2>", clusterName));
		} else {
			sender.sendMessage(Messages.RemoveAllFlagsError.get());
		}
    	return true;
	}
}
