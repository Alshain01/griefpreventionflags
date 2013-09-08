package src.john01dav.GriefPreventionFlags;

import java.util.HashSet;
import java.util.Set;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/** 
 * Class for ensuring the database exists and is up to date.
 * 
 * @author john01dav
 * @author Alshain01
 */
public abstract class ClaimManager {
	/**
	 * @param loc The location to retrieve the claim
	 * @return A Grief Prevention Claim at the specified location, null if does not exist.
	 */
	public static Claim getClaimAtLocation(Location loc){
		return GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);
	}
	
	/**
	 * @param loc The location to check the claim
	 * @return True if a claim exists at the provided location
	 */
	public static Boolean isClaimAtLocation(Location loc){
		return getClaimAtLocation(loc) == null;
	}
	
	/**
	 *  
	 * @param claim The claim to get the flags.
	 * @param includeInactive If true, all flags set in the data store are returned.  
	 * If false, only those that alter default Minecraft behavior are returned. 
	 * @return The set containing the flags.
	 */
	public static Set<String> getFlags(Claim claim, boolean includeInactive) {
		Set<String> flags = GriefPreventionFlags.instance.dataStore.readKeys("flags," + claim.getID());
		if (includeInactive) { return flags; }
		
		Flag flag = new Flag();
		Set<String> activeFlags = new HashSet<String>();
		for(String s : flags) {
			if(flag.setType(s) && flag.getValue(claim) != flag.getType().getDefault()) {
				activeFlags.add(s);
			}
		}
		return activeFlags;
	}
	
	/**
	 * Check locations to see if a player has permission to set flags here.
	 * 
	 * @param claim The location to check.
	 * @param player The player to check permission for.
	 * @return True if the player has permission 
	 */
	 public static boolean hasClaimPermission(Claim claim, Player player) {
		if (claim == null && player.hasPermission("gpflags.setflag.unclaimed")) { return true; }
		if (claim != null) {
			if (claim.getOwnerName().equalsIgnoreCase(player.getName())) { return true;	}
			if (!claim.isAdminClaim() && player.hasPermission("gpflags.setflag.others")) { return true;	}
			if (claim.isAdminClaim() && player.hasPermission("gpflags.setflag.admin")) { return true; }
		}
		return false;
	}
	
	 /**
	 * Check locations to see if a player has permission to set flags here.
	 * 
	 * @param claim The location to check.
	 * @param player The player to check permission for.
	 * @return True if the player has permission 
	 */
	public static boolean hasClusterPermission(Claim claim, Player player) {
		if (claim.getOwnerName().equalsIgnoreCase(player.getName())) { return true; }
		if (!claim.isAdminClaim() && player.hasPermission("gpflags.setcluster.others")) { return true; }
		if (claim.isAdminClaim() && player.hasPermission("gpflags.setcluster.admin")) { return true; }
		return false;
	}
	
	/**
	 * Removes a claim from the data store.
	 * DO NOT USE THIS! VERY DANGEROUS!
	 * 
	 * @param claim The claim to remove.
	 */
	public static void removeClaim(Claim claim) {
		// Must stay public, only for internal use.
		//TODO: Find a way to hide this.
		if(claim.parent != null){ return; }
		
   		GriefPreventionFlags.instance.getLogger().info("Removing GriefPreventionFlags data for claim ID %a".replaceAll("%a", String.valueOf(claim.getID())));
		GriefPreventionFlags.instance.dataStore.write("data." + claim.getID(), (String)null);
	}
}
