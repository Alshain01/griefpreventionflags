package src.john01dav.GriefPreventionFlags;
import java.util.Set;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.scheduler.BukkitRunnable;

import src.john01dav.GriefPreventionFlags.Flag.Type;



/** 
 * Class for ensuring the database exists and is up to date.
 * @author john01dav
 */
public class DatabaseCleanupTask extends BukkitRunnable{

	public void run(){
		Set<String> claims = GriefPreventionFlags.instance.dataStore.readKeys("data");
		
		for(String currentClaim : claims) {
			
			// Ignore pre-defined entries
			if((currentClaim.equalsIgnoreCase("database")) || (currentClaim.equalsIgnoreCase("global"))){ continue; }
			Claim claim;
			try {
				claim = GriefPrevention.instance.dataStore.getClaim(Long.parseLong(currentClaim));
			} catch (NumberFormatException e) {
				// Something bad is in the database.
				GriefPreventionFlags.instance.getLogger().warning("Removing invalid claim ID '" + currentClaim + "' from data.yml");
				GriefPreventionFlags.instance.dataStore.write("data." + currentClaim, (String)null);
				continue;
			}

			if(claim == null){
				GriefPreventionFlags.instance.getLogger().info("Removing non-existent claim with id " + currentClaim + " from the database.");
				for (Type type : Type.values()) {
					GriefPreventionFlags.instance.dataStore.write("data." + currentClaim + type.toString().toLowerCase(), (String)null);
				}
				GriefPreventionFlags.instance.dataStore.write("data." + currentClaim, (String)null);
				GriefPreventionFlags.instance.getLogger().info("Claim Removed!");
				GriefPreventionFlags.instance.LoadFlagCounts();  // Unmanaged deletion.  Need to reload.
			}
		}
		
	}
}
