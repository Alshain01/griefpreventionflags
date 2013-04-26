package src.john01dav.GriefPreventionFlags;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		switch (cmd.getName().toLowerCase) {
			case "setflag":
				return setFlag(sender, args, false);
			case "setflagglobal":
				return setFlag(sender, args, true);
			default:
				return false;
		}
	}
			
	private boolean setFlag(CommandSender sender, String[] args, Boolean isGlobal)		
		if(args.length < 1 || args.length > 2) { return false; } else {
			Player player;
			Claim claim;
			
			if (!isGlobal) {
				// Verify command is issued from the game, not the server console
				if(JCore.senderIsConsole(sender)){
					sender.sendMessage(ChatColor.RED + "Only players can do that command!");
					return true;
				}
				player = (Player) sender;
				
				// Verify the player is standing in a claim that we can set a flag to
				if(JCore.isClaimAtLocation(player.getLocation())){
					player.sendMessage(ChatColor.RED + "Stand in the claim that you want to set a flag for.");
					return true;
				}
				claim = JCore.getClaimAtLocation(player.getLocation());
			}
			
            int flag = GriefPreventionFlags.instance.claimmanager.getFlagId(args[0]);
            if(flag < 0){
            	player.sendMessage(ChatColor.RED + "Invalid Flag!");
            	return true;
            }
            
            // Get the state to set the flag.
            Boolean isAllowed;
            if (args.length > 1)
            {
            	// Explicit command definition
	            try{
	            	isAllowed = Boolean.parseBoolean(args[1]);
	            }catch(Exception e){
	            	player.sendMessage(ChatColor.RED + "Value must be a boolean!");
	            	return true;
	            }
            }else{
            	// Toggle switch
            	if (isGlobal)
            		isAllowed = !GriefPreventionFlags.instance.claimmanager.flags[flag].isAllowedGlobally();
            	else
            		isAllowed = !GriefPreventionFlags.instance.claimmanager.flags[flag].isAllowedInClaim(claim);
            }
            
            // Set the flag and tell the player
            if (isGlobal) {
            	GriefPreventionFlags.instance.claimmanager.flags[flag].setAllowedGlobally(isAllowed);
                sender.sendMessage(ChatColor.GREEN + "Set flag %flag to %value Globally.".replaceAll("%flag", args[0]).replaceAll("%value", String.valueof(isAllowed)));
            } else {
	            GriefPreventionFlags.instance.claimmanager.flags[flag].setAllowedInClaim(claim, isAllowed);
	            player.sendMessage(ChatColor.GREEN + "Set flag %flag to %value in your claim.".replaceAll("%flag", args[0]).replaceAll("%value", String.valueof(isAllowed)));
            }
			return true;
		}
	}
}
