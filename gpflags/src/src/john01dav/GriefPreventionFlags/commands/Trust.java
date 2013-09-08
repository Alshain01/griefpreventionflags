package src.john01dav.GriefPreventionFlags.commands;

import java.util.List;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Messages;

public abstract class Trust extends Command {
	protected static boolean get(CommandSender sender, String[] args) {
		// Check for proper command formatting
		if (args.length != 1) { return false; }
		
		// Acquire the player
		Player player = getPlayer(sender);
		if(player == null) { return true; }
		
		// Acquire the claim.
		if (!isClaimed(player)) { return true; }
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		// Acquire the requested flag
		Flag flag = getFlag(sender, args[0]);
		if (flag == null) { return true; }
		
		List<String> trustList = flag.getTrustList(claim);
		if(trustList == null) {
			player.sendMessage(Messages.InvalidTrustError.get().replaceAll("<2>", flag.getType().getLocalName()));
			return true;
		}
		
		// List all set flags
		StringBuilder message = new StringBuilder(Messages.GetFlagTrust.get());
		boolean first = true; // Governs whether we insert a comma or not (true means no)
		
		for (String p : trustList) {	
			// Output the flag name
			if (!first) {
				message.append(", ");
			} else {
				first = false;
			}
			message.append(p);
		}
	
		message.append(".");
		sender.sendMessage(message.toString());

		return true;
	}
	
	protected static boolean set(CommandSender sender, String[] args) {
		// Check for proper command formatting
		if (args.length < 2) { return false; }
		
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
		
		boolean success = true;
		for(int p = 1; p < args.length; p++) {
			if(!flag.setTrust(claim, args[p], player)) {
				success = false;
			}
		}
		if (success) {
			player.sendMessage(Messages.SetFlagTrust.get().replaceAll("<2>", flag.getType().getLocalName()));
		} else {
			player.sendMessage(Messages.SetFlagTrustError.get());
		}
		return true;
	}
	
	protected static boolean delete(CommandSender sender, String[] args) {
		if(args.length < 1){ return false;	}
		
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
		
		List<String> trustList = flag.getTrustList(claim);
		if(trustList == null) {
			player.sendMessage(Messages.InvalidTrustError.get().replaceAll("<2>", flag.getType().getLocalName()));
			return true;
		}
		
		boolean success = true;
		// Remove all players
		if (args.length == 1) {
			for (String p : trustList) {
				if (!flag.removeTrust(claim, p, player)) {
					success = false;
				}
			}
		} else {
			// Remove 1 or more players
			for (int p = 1; p < args.length; p++) {
				if (!flag.removeTrust(claim, args[p], player)) {
					success = false;
				}
			}
		}

		if (success) {
			player.sendMessage(Messages.RemoveFlagTrust.get().replaceAll("<2>", flag.getType().getLocalName()));
		} else {
			player.sendMessage(Messages.RemoveFlagTrustError.get());
		}
		
		return true;
	}
}
