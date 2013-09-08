package src.john01dav.GriefPreventionFlags.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.Cluster;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.GriefPreventionFlags;
import src.john01dav.GriefPreventionFlags.Messages;
import src.john01dav.GriefPreventionFlags.Flag.Type;

public abstract class Help extends Command {
	protected static boolean listFlags (CommandSender sender, String args[]) {
		if (!(sender instanceof Player)) {
			// Don't page for the console, give them all at once
			sender.sendMessage(Messages.ConsoleHelpHeader.get().replaceAll("<10>", Messages.Flag.get()));
			
			for (Type type : Type.values()) {
				sender.sendMessage(Messages.HelpTopic.get().replaceAll("<2>", type.getLocalName()).replaceAll("<4>", type.getDescription()));
			}
			return true;
		}
		
		int page = 1;
		int total = 1;
		
		//Get total pages
		//1 header per page
		//9 flags per page, except on the first which has a usage line and 8 flags
		total = ((Type.values().length + 1) / 9);
		if ((Type.values().length + 1) % 9 != 0)
			total++; // Add the last page, if the last page is not full (less than 9 flags)
		
		// Get page request from user
		if (args.length > 0) {
	        try {
		         page = Integer.parseInt(args[0]);
		         if (page < 1 || page > total)
		        	 page = 1;
	        } catch(Exception e){
	        	return false;
	        }
		}

		sender.sendMessage(Messages.HelpHeader.get().replaceAll("<5>", String.valueOf(page)).replaceAll("<6>", String.valueOf(total)).replaceAll("<10>", Messages.Flag.get()));
		
		// Setup for only displaying 10 lines at a time
		int linecount = 1;
		
		// Usage line.  Displays only on the first page.
		if (page == 1) {
			sender.sendMessage(Messages.HelpInfo.get().replaceAll("<10>", Messages.Flag.get().toLowerCase()));
			linecount++;
		}
		
		// Because the first page has 1 less flag count than the rest, 
		// manually initialize the loop counter by subtracting one from the 
		// start position of all pages other than the first.
		int loop = 0;
		if (page > 1) {
			loop = ((page-1)*9)-1;
		}
		
		
		// Show the flags
		for (; loop < Flag.Type.values().length; loop++) {
			sender.sendMessage(Messages.HelpTopic.get().replaceAll("<2>", Type.values()[loop].getLocalName()).replaceAll("<4>", Type.values()[loop].getDescription()));
			linecount++;
			
			if (linecount > 9) {
				return true; // Page is full, we're done
			}
		}
		return true; // Last page wasn't full (that's ok)
	}

	protected static boolean listCluster (CommandSender sender, String args[]) {
		Set<String> clusters = Cluster.getClusterNames();
		if (clusters == null) { 
			sender.sendMessage(Messages.NoClustersFound.get());
			return true; 
		}
		
		if (!(sender instanceof Player)) {
			// Don't page for the console, give them all at once
			sender.sendMessage(Messages.ConsoleHelpHeader.get().replaceAll("<10>", Messages.Cluster.get()));
			
			for (String c : clusters) {
				List<String> flags = Cluster.getCluster(c);
				if (flags == null) { continue; }
				StringBuilder description = new StringBuilder("");
				boolean first = true;
				
				for (String f : flags) {
					if(!first){
						description.append(" ,");
					} else {
						first = false;
					}
					
					description.append(f);
				}
				sender.sendMessage(Messages.HelpTopic.get().replaceAll("<2>", c).replaceAll("<4>", description.toString()));
			}
			return true;
		}
		
		int page = 1;
		int total = 1;
		
		//Get total pages
		//1 header per page
		//9 flags per page, except on the first which has a usage line and 8 flags
		total = ((clusters.size() + 1) / 9);
		if ((clusters.size() + 1) % 9 != 0) { 
			total++; // Add the last page, if the last page is not full (less than 9 flags) 
		}
		
		// Get page request from user
		if (args.length > 0) {
	        try {
		         page = Integer.parseInt(args[0]);
		         if (page < 1 || page > total)
		        	 page = 1;
	        } catch(Exception e){
	        	return false;
	        }
		}

		sender.sendMessage(Messages.HelpHeader.get().replaceAll("<5>", String.valueOf(page)).replaceAll("<6>", String.valueOf(total)).replaceAll("<10>", Messages.Cluster.get()));
		
		// Setup for only displaying 10 lines at a time
		int linecount = 1;
		
		// Usage line.  Displays only on the first page.
		if (page == 1) {
			sender.sendMessage(Messages.HelpInfo.get().replaceAll("<10>", Messages.Cluster.get().toLowerCase()));
			linecount++;
		}
		
		// Because the first page has 1 less flag count than the rest, 
		// manually initialize the loop counter by subtracting one from the 
		// start position of all pages other than the first.
		int loop = 0;
		if (page > 1) {
			loop = ((page-1)*9)-1;
		}
		
		String[] clusterArray = new String[clusters.size()];
		clusterArray = clusters.toArray(clusterArray);
		
		// Show the flags
		for (; loop < clusters.size(); loop++) {
			List<String> flags = Cluster.getCluster(clusterArray[loop]);
			if (flags == null) { continue; }
			StringBuilder description = new StringBuilder("");
			boolean first = true;

			for (String f : flags) {
				if(!first){
					description.append(" ,");
				} else {
					first = false;
				}
				
				description.append(f);
			}
			sender.sendMessage(Messages.HelpTopic.get().replaceAll("<2>", clusterArray[loop]).replaceAll("<4>", description.toString()));

			linecount++;
			
			if (linecount > 9) {
				return true; // Page is full, we're done
			}
		}
		return true; // Last page wasn't full (that's ok)
	}
	
	protected static boolean flagCount(CommandSender sender, String args[]) {
		// Check for proper command formatting
		if (args.length != 1) { return false; }
		
		Flag flag = new Flag();
		// List the requested flag
		flag = getFlag(sender, args[0]);
		if (flag == null) { return true; }
		
		sender.sendMessage(Messages.FlagCount.get().replaceAll("<7>", GriefPreventionFlags.instance.FlagCounts[flag.getType().ordinal()] + "").replaceAll("<2>", args[0]));
		return true;
	}
}
