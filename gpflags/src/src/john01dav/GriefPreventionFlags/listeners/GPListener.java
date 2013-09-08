package src.john01dav.GriefPreventionFlags.listeners;

import java.util.List;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import me.ryanhamshire.GriefPrevention.events.SiegeStartEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;
/**
 * Listener for Grief Prevention events
 * @author Alshain01
 */
public class GPListener implements Listener {

	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onClaimDeleted(ClaimDeletedEvent e) {
		// Cleanup the database, keep the file from growing too large.
		Claim claim = e.getClaim();
		Flag flag = new Flag();
		
		// We do this to ensure the flag counts are updated
		for (Type type : Type.values()) {
			flag.setType(type);
			flag.removeValue(claim, null);
		}
		
		ClaimManager.removeClaim(e.getClaim());
	}
	
	@EventHandler (ignoreCancelled = true)
	private void onSiegeStart(SiegeStartEvent e) {
		List<Claim> siegedClaims = e.getSiegeData().claims;
		Flag flag = new Flag(Type.Siege);
		Player player = e.getSiegeData().attacker;
		for(Claim c : siegedClaims) {
			if (!flag.getValue(c) && !flag.hasBypassPermission(c, player)) {
				player.sendMessage(flag.getMessage(c)
						.replaceAll("<0>", player.getName()));
				e.setCancelled(true);
				return;
			}
		}
	}
}