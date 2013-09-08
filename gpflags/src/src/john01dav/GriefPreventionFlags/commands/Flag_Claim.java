package src.john01dav.GriefPreventionFlags.commands;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;
import src.john01dav.GriefPreventionFlags.Messages;

public abstract class Flag_Claim extends Command{

	
	protected static boolean get(CommandSender sender, String args[]) {
		// Check for proper command formatting
		if (args.length > 1) { return false; }

		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
			
		// Acquire the claim.
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		// Acquire the world
		World world = player.getWorld();

		Flag flag = new Flag();

		if (args.length == 1) {
			// List the requested flag
			flag = getFlag(sender, args[0]);
			if (flag == null) { return true; }

			Boolean isAllowed;
			if (claim == null) {
				isAllowed = flag.getUnclaimedValue(world);
				if (isAllowed == null) {
					isAllowed = flag.getType().getDefault();
				}
				
				player.sendMessage(Messages.GetFlagUnclaimed.get().replaceAll("<2>", args[0]).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
			} else {
				isAllowed = flag.getValue(claim);
				
				if (isAllowed != null) {
					player.sendMessage(Messages.GetFlag.get().replaceAll("<2>", args[0]).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
				} else {
					isAllowed = flag.isAllowed(world);
					player.sendMessage(Messages.InheritedFlag.get().replaceAll("<2>", args[0]).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
				}
			}
			return true;
		}
		
		// No flag provided, list all set flags for the claim
		StringBuilder message = new StringBuilder(Messages.GetAllFlags.get());
		boolean first = true; // Governs whether we insert a comma or not (true means no)
		
		for (Type t : Type.values()) {
			flag.setType(t);
			
			// Get the flag's value
			Boolean value;
			if (claim != null) {
				value = flag.getValue(claim);
			} else {
				value = flag.getUnclaimedValue(world);
			}
			
			// Output the flag name
			if (value != null && value != t.getDefault()) {
				if (!first) {
					message.append(", ");
				} else {
					first = false;
				}
				message.append(t.getLocalName());
			}
		}
		message.append(".");
		sender.sendMessage(message.toString());

		return true;
	}

	protected static boolean set(CommandSender sender, String[] args) {
		// Check for proper command formatting
		if(args.length < 1 || args.length > 2){ return false; }

		// Acquire the flag type requested in the command
		Flag flag = getFlag(sender, args[0]);
		if (flag == null) {	return true; }

		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
		
    	World world = player.getWorld();
		
		// Acquire the claim.
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());

		
		// Check that the player can set the flag type at this location
		if (!allPermitted(flag, claim, player)) { return true; }

		// Determine new flag state
        Boolean isAllowed;
        if (args.length > 1) {
        	// Explicit command definition
            try{
            	isAllowed = Boolean.parseBoolean(args[1]);
            } catch(Exception e) {
            	sender.sendMessage(Messages.ValueError.get());
            	return true;
            }
		} else {
			// Toggle switch
			if (claim != null) {
				// Claim
				isAllowed = !flag.isAllowed(claim);
			} else {
				// Unclaimed
				isAllowed = flag.getUnclaimedValue(world);
				if (isAllowed == null) {
					isAllowed = !flag.getType().getDefault();
				} else {
					isAllowed = !isAllowed;
				}
			}
		}
		

		
        // Set the flag
        if (claim == null) {
        	if(flag.setUnclaimedValue(world, isAllowed, sender)) {
           		player.sendMessage(Messages.SetFlagUnclaimed.get().replaceAll("<2>", args[0]).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
        	}
        	return true;
        }

        if(flag.setValue(claim, isAllowed, player)) {
       		player.sendMessage(Messages.SetFlag.get().replaceAll("<2>", args[0]).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
       	}
    	return true;
	}
	
	protected static boolean delete(CommandSender sender, String[] args) {
		// Check for proper command formatting
		if(args.length > 1){ return false; }

		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
		
		World world = player.getWorld();
		
		// Acquire the claim.
		//if (!isClaimed(player)) { return true; }
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		// Check that the player can set the flag type at this location
		if (!claimPermitted(claim, player)) { return true; }

		Flag flag = new Flag();

		// Removing single flag type
		if (args.length == 1) {
			// Acquire the flag type requested in the command
			flag = getFlag(sender, args[0]);
			if (flag == null) { return true; }
			
			// Check that the player can set the flag type at this location
			if (!flagPermitted(flag, player)) { return true; }
			
			if(claim == null) {
				if(flag.removeUnclaimedValue(world, sender)) {
					player.sendMessage(Messages.RemoveFlagUnclaimed.get().replaceAll("<2>", args[0]));
				}
				return true;
			}
			
			if(flag.removeValue(claim, player)) {
				player.sendMessage(Messages.RemoveFlag.get().replaceAll("<2>", args[0]));
			}
        	return true;
		}
		
		// Removing all flags if the player has permission
		boolean success = true;

		for (Type t : Type.values()) {
			flag.setType(t);
			if (flag.hasPermission(player)) {
				if (claim != null) {
					if (!flag.removeValue(claim, player)) {
						success = false;
					}
				} else {
					if (!flag.removeUnclaimedValue(world, sender)) {
						success = false;
					}
				}
			} else {
				success = false;
			}
		}
		
		if (success) {
			sender.sendMessage(Messages.RemoveAllFlags.get());
		} else {
			sender.sendMessage(Messages.RemoveAllFlagsError.get());
		}
		return true;
	}
	

}