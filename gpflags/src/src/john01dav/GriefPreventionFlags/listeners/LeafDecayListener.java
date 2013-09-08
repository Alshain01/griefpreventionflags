package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;

public class LeafDecayListener implements Listener{
	@EventHandler(ignoreCancelled = true)
	private void onLeafDecay(LeavesDecayEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getBlock().getLocation());
		Flag flag = new Flag(Type.LeafDecay);
		if(claim == null) {
			e.setCancelled(!flag.isUnclaimedAllowed(e.getBlock().getWorld()));
		} else {
			e.setCancelled(!flag.isAllowed(claim));
		}
	}
}
