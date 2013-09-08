package src.john01dav.GriefPreventionFlags.commands;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Messages;
/**
 * Top level class for command based functions
 * 
 * @author john01dav
 * @author Alshain01
 */
public abstract class Command {
	protected static String getValue(boolean value) {
        return (value) ? Messages.ValueColorTrue.get() : Messages.ValueColorFalse.get();
	}

	protected static boolean allPermitted(Flag flag, Claim claim, Player player) {
		return (flagPermitted(flag, player) && claimPermitted(claim, player)) ? true : false;
	}
	
	protected static boolean flagPermitted(Flag flag, Player player) {
		if (!flag.hasPermission(player)) {
			player.sendMessage(Messages.FlagPermError.get().replaceAll("<10>", Messages.Flag.get().toLowerCase()));
			return false;
		}
		return true;
	}
	
	protected static boolean claimPermitted(Claim claim, Player player) {
		// Check that the player can set a flag at this location
		if (!ClaimManager.hasClaimPermission(claim, player)) {
			if (claim != null) {
				player.sendMessage(Messages.ClaimPermError.get().replaceAll("<1>", claim.getOwnerName()).replaceAll("<10>", Messages.Flag.get().toLowerCase()));
			} else {
				player.sendMessage(Messages.UnclaimedPermError.get().replaceAll("<10>", Messages.Flag.get().toLowerCase()));
			}
			return false;
		}
		return true;
	}
	
	protected static Flag getFlag(CommandSender sender, String flagname) {
		Flag flag = new Flag();
		if(!flag.setType(flagname)) {
			sender.sendMessage(Messages.InvalidFlagError.get().replaceAll("<2>", flagname).replaceAll("<10>", Messages.Flag.get().toLowerCase()));
			return null;
		}
		return flag;
	}
	
	protected static Player getPlayer(CommandSender sender) {
		if (!(sender instanceof Player)) { 
			sender.sendMessage(Messages.NoConsoleError.get());
			return null; 
		}
		return (Player) sender;
	}
	
	protected static boolean isClaimed(Player player) {
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		if(claim == null) {
			player.sendMessage(Messages.NoClaimError.get());
			return false;
		}
		return true;
	}
}
