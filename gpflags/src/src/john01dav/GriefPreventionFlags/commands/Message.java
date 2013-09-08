package src.john01dav.GriefPreventionFlags.commands;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;

public abstract class Message extends Command{
	protected static boolean get(CommandSender sender, String[] args) {
		if (args.length != 1) {	return false; }
		
		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
		
		// Acquire the claim.
		if (!isClaimed(player)) { return true; }
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		// Acquire the requested flag
		Flag flag = getFlag(sender, args[0]);
		if (flag == null) { return true; }
		
		// Send the message
		player.sendMessage(flag.getMessage(claim).replaceAll("<0>", player.getName()));
		return true;
	}
	
	protected static boolean set(CommandSender sender, String[] args) {
		if(args.length < 2) { return false; }
		
		// Acquire the flag type requested in the command
		Flag flag = getFlag(sender, args[0]);
		if (flag == null) { return true; }
		
		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
		
		// Acquire the claim.
		if (!isClaimed(player)) { return true; }
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		// Check that the player can set the flag type at this location
		if (!allPermitted(flag, claim, player)) { return true; }
		
		// Build the message from the remaining arguments
		StringBuilder message = new StringBuilder();
		for (int x = 1; x < args.length; x++) {

			message.append(args[x]);
			if (x < args.length - 1) {
				message.append(" ");
			}
		}

		if(flag.setMessage(claim, message.toString(), player)) {;
			player.sendMessage(flag.getMessage(claim));
		}
		return true;
	}
	
	protected static boolean delete(CommandSender sender, String[] args) {
		if(args.length != 1){ return false;	}
		
		// Acquire the flag type requested in the command
		Flag flag = getFlag(sender, args[0]);
		if (flag == null) { return true; }
		
		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
		
		// Acquire the claim.
		if (!isClaimed(player)) { return true; }
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		// Check that the player can set the flag type at this location
		if (!allPermitted(flag, claim, player)) { return true; }
		
		if (flag.removeMessage(claim, player)) {;
			player.sendMessage(flag.getMessage(claim));
		}
		return true;
	}
}
