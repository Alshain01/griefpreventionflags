package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;
import src.john01dav.GriefPreventionFlags.GriefPreventionFlags;
import src.john01dav.GriefPreventionFlags.events.*;

/**
 * Listener for GriefPreventionFlags events
 * @author Alshain01
 */
public class GPFlagsListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onFlagSet(FlagSetEvent e) {
		//Metrics logging
		Flag flag = new Flag(e.getFlagType());
		
		// Did the flag value really change?
		if (flag.getValue(e.getClaim()) != null && e.getNewValue() == flag.getValue(e.getClaim())) { return; }
		
		// Flag changed value
		if (e.getNewValue() != e.getFlagType().getDefault()) {
			// Flag was set
			GriefPreventionFlags.instance.FlagCounts[e.getFlagType().ordinal()]++;
		} else {
			// Flag was unset
			GriefPreventionFlags.instance.FlagCounts[e.getFlagType().ordinal()]--;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onFlagDelete(FlagDeleteEvent e) {
		//Metrics logging
		Flag flag = new Flag(e.getFlagType());
		
		// Flag was at the default, don't count it
		if (flag.getValue(e.getClaim()) == e.getFlagType().getDefault()) { return; }

		// Flag being removed was previously set
		GriefPreventionFlags.instance.FlagCounts[e.getFlagType().ordinal()]--;
	}
	
	@EventHandler (ignoreCancelled = true)
	private void onPlayerEnterClaim(PlayerEnterClaimEvent e) {
		Flag flag = new Flag(Type.AllowEntry);
		Claim claim = e.getClaim();
		if(flag.isAllowed(claim)) { return; }
		
		Player player = e.getPlayer();
		if (flag.hasBypassPermission(claim, player) || player.getName().equals(claim.getOwnerName())) { return; }
		
		// Player is not allowed in this claim.
		if (!GriefPreventionFlags.instance.playersMessaged.contains(player.getName())) {
			// Player was not told that recently
			GriefPreventionFlags.instance.playersMessaged.add(player.getName());
			player.sendMessage(flag.getMessage(claim)
					.replaceAll("<0>", e.getPlayer().getName()));
		}
		e.setCancelled(true);
	}
	
	@EventHandler (ignoreCancelled = true)
	private void onPlayerExitClaim(PlayerExitClaimEvent e) {
		Flag flag = new Flag(Type.AllowLeave);
		Claim claim = e.getClaim();
		if(flag.isAllowed(claim)) { return; }
		
		Player player = e.getPlayer();
		if (flag.hasBypassPermission(claim, player) || player.getName().equals(claim.getOwnerName())) { return; }
		
		// Player is not allowed to leave this claim.
		if (!GriefPreventionFlags.instance.playersMessaged.contains(player.getName())) {
			// Player was not told that recently
			GriefPreventionFlags.instance.playersMessaged.add(player.getName());
			player.sendMessage(flag.getMessage(claim)
					.replaceAll("<0>", e.getPlayer().getName()));
		}
		e.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onPlayerEnterClaimMonitor(PlayerEnterClaimEvent e) {
		Flag flag = new Flag(Type.NotifyEnter);
		Claim claim = e.getClaim();
		Player player = e.getPlayer();

		if (flag.isAllowed(claim)) {
			if (player.getName().equals(claim.getOwnerName())) { return; } // Don't annoy the claim owner
			
			flag.setType(Type.AllowLeave);
			if (flag.hasBypassPermission(claim, player) || flag.isAllowed(claim)){ // Guards against messages during forcible re-entry - event re-fires
				flag.setType(Type.NotifyEnter);
				e.getPlayer().sendMessage(flag.getMessage(claim)
						.replaceAll("<0>", e.getPlayer().getName()));
			}
		}
		
//		flag.setType(Type.Sprint);
//		if(player.isSprinting() && !flag.isAllowed(claim) && !flag.hasBypassPermission(claim, e.getPlayer())) {
//			player.setSprinting(false);
//			player.sendMessage(flag.getMessage(claim)
//					.replaceAll("<0>", e.getPlayer().getName())); 
//		}
//		
//		flag.setType(Type.Sneak);
//		if(player.isSneaking() && !flag.isAllowed(claim) && !flag.hasBypassPermission(claim, e.getPlayer())) {
//			player.setSneaking(false);
//			player.sendMessage(flag.getMessage(claim)
//					.replaceAll("<0>", e.getPlayer().getName())); 
//		}
//		
//		flag.setType(Type.Flight);
//		if(player.isFlying() && !flag.isAllowed(claim) && !flag.hasBypassPermission(claim, e.getPlayer())) {
//			player.teleport(player.getWorld().getHighestBlockAt(player.getLocation()).getLocation(), TeleportCause.PLUGIN);
//			player.setFlying(false);
//			player.sendMessage(flag.getMessage(claim)
//					.replaceAll("<0>", e.getPlayer().getName())); 
//		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onPlayerExitClaimMonitor(PlayerExitClaimEvent e) {
		Flag flag = new Flag(Type.NotifyExit);
		Claim claim = e.getClaim();
		Player player = e.getPlayer();
		
		if (e.getClaimEntered() == null) {  // No message when entering adjacent claim.
			if (flag.isAllowed(claim)) {
				if (!player.getName().equals(claim.getOwnerName())) { // Don't bug the owner	
					flag.setType(Type.AllowEntry);
					if (flag.hasBypassPermission(claim, player) || flag.isAllowed(claim)){  //Guards against messages for forcible removal
						flag.setType(Type.NotifyExit);
						e.getPlayer().sendMessage(flag.getMessage(claim)
								.replaceAll("<0>", e.getPlayer().getName()));
					}
				}
			}
		}
	}
}