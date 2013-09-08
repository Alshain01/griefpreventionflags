package src.john01dav.GriefPreventionFlags.commands;

import java.util.Set;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.command.CommandSender;

import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.GriefPreventionFlags;
import src.john01dav.GriefPreventionFlags.Flag.Type;
import src.john01dav.GriefPreventionFlags.data.YamlDataStore;

public abstract class Admin {
	// Command for plug-in data maintenance
	// Keep this top level to access protected dataStore.
	public static boolean onCommand(CommandSender sender, String[] args, YamlDataStore dataStore) {
		if(args.length < 1) { return false; }
		if(args[0].equalsIgnoreCase("reload")) {
			if(args.length > 1) { return false; }
			return ReloadData(dataStore);
		} else if (args[0].equalsIgnoreCase("getyamlvalue")){
			if(args.length > 2) { return false; }
			return GetYamlValue(sender, args[1], dataStore);
		} else if (args[0].equalsIgnoreCase("compactdb")) {
			if(args.length > 1) { return false; }
			return CompactDatabase(dataStore);
		}
		return false;
	}

	private static boolean ReloadData(YamlDataStore dataStore) {
		GriefPreventionFlags.instance.reloadConfig();
		dataStore.reload(GriefPreventionFlags.instance);
		GriefPreventionFlags.instance.LoadFlagCounts();
		GriefPreventionFlags.instance.getLogger().info("GriefPreventionFlags Reloaded");
		return true;
	}
	
	private static boolean GetYamlValue(CommandSender sender, String path, YamlDataStore dataStore) {
		sender.sendMessage(dataStore.read(path));
		return true;
	}
	
	private static boolean CompactDatabase(YamlDataStore dataStore) {
		Set<String> claims = dataStore.readKeys("data");
		
		for(String currentClaim : claims) {
			// Ignore pre-defined entries
			if((currentClaim.equalsIgnoreCase("database")) || (currentClaim.equalsIgnoreCase("global"))){ continue; }
			
			Claim claim;
			try {
				claim = GriefPrevention.instance.dataStore.getClaim(Long.parseLong(currentClaim));
			} catch (NumberFormatException e) {
				// Something bad is in the database. Remove it.
				GriefPreventionFlags.instance.getLogger().warning("Removing invalid claim ID. Claim: " + currentClaim);
				dataStore.write("data." + currentClaim, (String)null);
				continue;
			}
			
			// Remove claims that don't exist in GriefPrevention.
			if(claim == null){
				GriefPreventionFlags.instance.getLogger().info("Removing data for claim the doesn't exist. Claim: " + currentClaim);
				for (Type type : Type.values()) {
					dataStore.write("data." + currentClaim + type.toString().toLowerCase(), (String)null);
				}
				dataStore.write("data." + currentClaim, (String)null);
				GriefPreventionFlags.instance.getLogger().info("Claim Removed!");
				GriefPreventionFlags.instance.LoadFlagCounts();  // Unmanaged deletion.  Need to reload.
			}
			
			
			Set<String> flags = dataStore.readKeys("data." + currentClaim);
			for (String currentFlag : flags) {
				Flag flag = new Flag();
				if((currentFlag.contains("trust")) || (currentFlag.equalsIgnoreCase("message"))){
					String flagname = currentFlag;
					flagname.replace("message", "");
					flagname.replace("trust", "");
					if(!flag.setType(flagname)) {
						GriefPreventionFlags.instance.getLogger().info("Removing message or trustlist for unknown flag type. Claim: " + currentClaim + "Data: " + currentFlag);
						dataStore.write("data." + currentClaim + "." + currentFlag, (String)null);							
					}
					continue; 
				}

				if (!flag.setType(currentFlag)) { 
					GriefPreventionFlags.instance.getLogger().info("Removing unknown flag data. Claim: " + currentClaim + "Flag: " + currentFlag);
					dataStore.write("data." + currentClaim + "." + currentFlag, (String)null);
					continue;
				}

				if (flag.getValue(claim) == flag.getValue(GriefPreventionFlags.instance.getServer().getWorld(claim.getClaimWorldName()))) {
					GriefPreventionFlags.instance.getLogger().info("Removing claim flag matching the global value. Claim: " + currentClaim + "Flag: " + currentFlag);
					flag.removeValue(claim, null);
					continue;
				}
			}
			
			flags = dataStore.readKeys("data." + currentClaim);
			if (flags.size() == 0) {
				GriefPreventionFlags.instance.getLogger().info("Removing claim data with no flags. Claim: " + currentClaim);
				dataStore.write("data." + currentClaim, (String)null);
			}
		}
		GriefPreventionFlags.instance.LoadFlagCounts();  // Possible unmanaged deletion.  Need to reload.
		GriefPreventionFlags.instance.getLogger().info("Database Compacted");
		return true;
	}
}