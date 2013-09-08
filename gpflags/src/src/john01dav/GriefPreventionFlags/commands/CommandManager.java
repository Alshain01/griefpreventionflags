package src.john01dav.GriefPreventionFlags.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Command handler for GriefPreventionFlags
 * 
 * @author john01dav
 * @author Alshain01
 */
public abstract class CommandManager {
	/**
	 * Executes the given command, returning its success 
	 * 
	 * @param sender Source of the command
	 * @param cmd    Command which was executed
	 * @param label  Alias of the command which was used
	 * @param args   Passed command arguments
	 * @return		 true if a valid command, otherwise false
	 */
	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String command = cmd.getName().toLowerCase();

		// Claim Flag
		if(command.equalsIgnoreCase("setflag")) {
			return Flag_Claim.set(sender, args);
		} else if(command.equalsIgnoreCase("getflag")) {
			return Flag_Claim.get(sender, args);
		} else if(command.equalsIgnoreCase("removeflag")) {
			return Flag_Claim.delete(sender, args);
		
		// Global Flag
		} else if(command.equalsIgnoreCase("setflagglobal")) {
			return Flag_Global.set(sender, args);
		} else if(command.equalsIgnoreCase("getflagglobal")) {
			return Flag_Global.get(sender, args);
		} else if(command.equalsIgnoreCase("removeflagglobal")) {
			return Flag_Global.delete(sender, args);
			
		// Claim Cluster
		} else if(command.equalsIgnoreCase("setflagcluster")) {
			return Cluster_Claim.set(sender, args);
		} else if(command.equalsIgnoreCase("removeflagcluster")) {
			return Cluster_Claim.delete(sender, args);
			
		// Global Cluster
		} else if(command.equalsIgnoreCase("setclusterglobal")) {
			return Cluster_Global.set(sender, args);
		} else if(command.equalsIgnoreCase("removeclusterglobal")) {
			return Cluster_Global.delete(sender, args);

		// Messages
		} else if(command.equalsIgnoreCase("getflagmessage")) {
			return Message.get(sender, args);
		} else if(command.equalsIgnoreCase("removeflagmessage")) {
			return Message.delete(sender, args);
		} else if(command.equalsIgnoreCase("setflagmessage")) {
			return Message.set(sender, args);
			
		// Help Commands
		} else if(command.equalsIgnoreCase("flags")) {
			return Help.listFlags(sender, args);
		} else if(command.equalsIgnoreCase("flagcount")) {
			return Help.flagCount(sender, args);
		} else if(command.equalsIgnoreCase("clusters")) {
			return Help.listCluster(sender, args);
			
		// Trust
		} else if(command.equalsIgnoreCase("setflagtrust")) {
			return Trust.set(sender, args);
		} else if(command.equalsIgnoreCase("getflagtrust")) {
			return Trust.get(sender, args);
		} else if(command.equalsIgnoreCase("removeflagtrust")) {
			return Trust.delete(sender, args);

		} else { return false; }
	}
}