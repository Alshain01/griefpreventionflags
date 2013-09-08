package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.events.PlayerEnterClaimEvent;
import src.john01dav.GriefPreventionFlags.events.PlayerExitClaimEvent;

/**
 * @author Alshain01
  */
public class PlayerMoveListener implements Listener {
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	private void onPlayerMove(PlayerMoveEvent e){
		Claim moveTo = ClaimManager.getClaimAtLocation(e.getTo());
		Claim moveFrom = ClaimManager.getClaimAtLocation(e.getFrom());

		// Leave quick, don't waste time on this
		if(moveTo == moveFrom || (moveFrom == null && moveTo == null)) { return; }
		
		Player player = e.getPlayer();
		// Event for moving into a claim
		if(moveTo != null) {
			// Player entered a claim
			if (moveFrom == null || (moveFrom != null && moveFrom.parent != moveTo && moveTo.parent != moveFrom)) {
			    // Player did not leave a claim OR player left a claim and neither was a parent of the other.
				PlayerEnterClaimEvent event = new PlayerEnterClaimEvent(player, moveTo, moveFrom);
				Bukkit.getServer().getPluginManager().callEvent(event);
				
				if (event.isCancelled()) {
					e.setCancelled(true);
				}
			}
		}
		
		// Event for moving out of a claim
		if(moveFrom != null) {
			// Player left a claim
			if (moveTo == null || (moveTo != null && moveFrom.parent != moveTo && moveTo.parent != moveFrom)) {
				// Player did not enter a new claim OR player entered a new claim and neither was a parent of the other.
				PlayerExitClaimEvent event = new PlayerExitClaimEvent(player, moveFrom, moveTo);
				Bukkit.getServer().getPluginManager().callEvent(event);
			
				if (event.isCancelled()) {
					e.setCancelled(true);
				}
			}
		}
	}
}
