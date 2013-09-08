package src.john01dav.GriefPreventionFlags.commands;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.GriefPreventionFlags;
import src.john01dav.GriefPreventionFlags.Messages;
import src.john01dav.GriefPreventionFlags.Flag.Type;

public class Flag_Global extends Command {
	protected static boolean get(CommandSender sender, String args[]) {
		World world = null;
		Flag flag = null;
		
		// Global - handles console different than player
		if (!(sender instanceof Player)) {
			
			// Console must provide a world name.
			if (args.length != 2) {
				sender.sendMessage("getflagglobal <world> <flag>");
				return true;
			}
			
			world = GriefPreventionFlags.instance.getServer().getWorld(args[0]);
			
			if (world == null) {
				sender.sendMessage(args[2] + " is not a valid world.");
				return true;
			}
			
			flag = getFlag(sender, args[1]);
		} else {
			// Player uses the world they are in.
			if (args.length != 1) { return false; }
			
			world = ((Player)sender).getWorld();
			flag = getFlag(sender, args[0]);
		}
	
		if (flag == null) { return true; }

	
		// Display the flag
		Boolean isAllowed = flag.isAllowed(world);;
		sender.sendMessage(Messages.GetFlagGlobal.get().replaceAll("<2>", flag.getType().getLocalName()).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
		return true;
	}
	
	protected static boolean set(CommandSender sender, String[] args) {
		World world = null;
		Flag flag = null;
		String value = null;
		
		// Global - handles console different than player
		if (!(sender instanceof Player)) {

			// Console must provide a world name.
			if (args.length < 2 || args.length > 3) {
				sender.sendMessage("setflagglobal <world> <flag> [value]");
				return true;
			}
			
			world = GriefPreventionFlags.instance.getServer().getWorld(args[0]);
			
			if (world == null) {
				sender.sendMessage(args[3] + " is not a valid world.");
				return true;
			}
			
			if (args.length == 3) {
				value = args[3];
			}
			
			flag = getFlag(sender, args[1]);
		} else {
			// Player uses the world they are in.
			if (args.length < 1 || args.length > 2) { return false; }
			
			world = ((Player)sender).getWorld();
			
			if (args.length == 2) {
				value = args[2];
			}
			
			flag = getFlag(sender, args[1]);
		}
		
		if (flag == null) {	return true; }
		
		// Determine new flag state
        Boolean isAllowed;
        if (value != null) {
        	// Explicit command definition
            try{
            	isAllowed = Boolean.parseBoolean(value);
            }catch(Exception e){
            	sender.sendMessage(Messages.ValueError.get());
            	return true;
            }
		} else {
			// Toggle switch
			GriefPreventionFlags.instance.Debug("Toggle Command");
			isAllowed = !flag.isAllowed(world);
		}
		
        // Set the flag
    	if(flag.setValue(world, isAllowed, sender)) {
    		sender.sendMessage(Messages.SetFlagGlobal.get().replaceAll("<2>", flag.getType().getLocalName()).replaceAll("<3>", getValue(isAllowed).toLowerCase()));
    	}
        return true;
	}
	
	protected static boolean delete(CommandSender sender, String[] args) {
		World world = null;
		Flag flag = null;
		
		// Global - handles console different than player
		if (!(sender instanceof Player)) {
			
			// Console must provide a world name.
			if (args.length < 1 || args.length > 2) {
				sender.sendMessage("removeflagglobal <world> [flag]");
				return true;
			}
			
			world = GriefPreventionFlags.instance.getServer().getWorld(args[0]);
			
			if (world == null) {
				sender.sendMessage(args[0] + " is not a valid world.");
				return true;
			}
			
			if (args.length == 2) {
				flag = getFlag(sender, args[1]);
				if (flag == null) {	return true; }
			}
		} else {
			// Player uses the world they are in.
			if (args.length > 1) { return false; }
			
			world = ((Player)sender).getWorld();
			
			if (args.length == 1) {
				flag = getFlag(sender, args[0]);
				if (flag == null) {	return true; }
			}
		}

		// Removing single flag type
		if (flag != null) {
			// Check that the player can set the flag type at this location
			if(flag.removeValue(world, sender)) {
				sender.sendMessage(Messages.RemoveFlagGlobal.get().replaceAll("<2>", flag.getType().getLocalName()));
			}
			
        	return true;
		}
		
		// Removing all flags
		boolean success = true;
		flag = new Flag();

		for (Type t : Type.values()) {
			flag.setType(t);

			// Remove global flag
			if (!flag.removeValue(world, sender)) {
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