package src.john01dav.GriefPreventionFlags.commands;

import java.util.List;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Cluster;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Messages;

public abstract class Cluster_Claim extends Command {
	protected static boolean claimPermitted(Claim claim, Player player) {
		// Check that the player can set a cluster at this location
		if (!ClaimManager.hasClusterPermission(claim, player)) {
			player.sendMessage(Messages.ClaimPermError.get().replaceAll("<1>", claim.getOwnerName()).replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
			return false;
		}
		return true;
	}

	protected static boolean set(CommandSender sender, String[] args) {
		// Check for proper command formatting
		if(args.length != 2){ return false; }
		
		// Acquire the cluster type requested in the command
		List<String> cluster = Cluster.getCluster(args[0].toLowerCase());
		if (cluster == null) { 
			sender.sendMessage(Messages.InvalidFlagError.get().replaceAll("<2>", args[0]).replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
			return true; 
		}
		
		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
		
		World world = player.getWorld();
		
		// Acquire the claim.
		//if (!isClaimed(player)) { return true; }
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		// Check that the player can set the cluster type at this location
		if (!claimPermitted(claim, player)) { return true; }
		
		// Check that the player can set the cluster type at this location
		if (!player.hasPermission("gpflags.setcluster." + args[0])) {
			sender.sendMessage(Messages.FlagPermError.get().replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
			return true;
		}
		
		// Determine new flag state
        Boolean isAllowed;
        try{
           	isAllowed = Boolean.parseBoolean(args[1]);
        }catch(Exception e){
           	sender.sendMessage(Messages.ValueError.get());
           	return true;
        }
        
		Flag flag = new Flag();
		boolean success = true;

		// Set the flags
        for (String f : cluster) {
        	if (flag.setType(f)) {
        		if (claim != null) {
	        		if(!flag.setValue(claim, isAllowed, player)) {
	        			success = false;
	        		}
        		} else {
        			if(!flag.setUnclaimedValue(world, isAllowed, sender)) {
        				success = false;
        			}
        		}
        		continue;
        	}
        	success = false;
        	sender.sendMessage("Invald clusters.yml entry: " + f);
        }
        
        if(success) {
        	sender.sendMessage(Messages.SetCluster.get().replaceAll("<2>", args[0]).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
        } else {
        	sender.sendMessage(Messages.SetMultipleFlagsError.get());
        }
        return true;
	}
	
	protected static boolean delete(CommandSender sender, String[] args) {
		// Check for proper command formatting
		if(args.length != 1){ return false; }

		// Acquire the cluster type requested in the command
		List<String> cluster = Cluster.getCluster(args[0].toLowerCase());
		if (cluster == null) { 
			sender.sendMessage(Messages.InvalidFlagError.get().replaceAll("<2>", args[0]).replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
			return true; 
		}
		
		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
		
		World world = player.getWorld();
		
		// Acquire the claim.
		//if (!isClaimed(player)) { return true; }
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		// Check that the player can set the flag type at this location
		if (!claimPermitted(claim, player)) { return true; }
		
		// Check that the player can set the cluster type at this location
		if (!player.hasPermission("gpflags.setcluster." + args[0])) {
			sender.sendMessage(Messages.FlagPermError.get().replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
			return true;
		}
		
		Flag flag = new Flag();
		boolean success = true;
		
		// Removing all flags
		for (String f : cluster) {
			flag.setType(f);
        	if (flag.setType(f)) {
        		if (claim != null) {
	        		if (!flag.removeValue(claim, player)) {
	        			success = false;
	        		}
        		} else {
        			if (!flag.removeUnclaimedValue(world, sender)) {
        				success = false;
        			}
        		}
        		continue;
        	}
        	success = false;
       		sender.sendMessage("Invald clusters.yml entry: " + f);
		}
		
		if (success) {
			sender.sendMessage(Messages.RemoveCluster.get().replaceAll("<2>", args[0]));
		} else {
			sender.sendMessage(Messages.RemoveAllFlagsError.get());
		}
    	return true;
	}
}
