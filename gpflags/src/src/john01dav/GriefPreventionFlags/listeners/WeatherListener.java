package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;

/**
 * @author Alshain01
  */
public class WeatherListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	private void onLightningStrike(LightningStrikeEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getLightning().getLocation());
		Flag flag = new Flag(Type.Lightning);
		if(claim == null) {
			e.setCancelled(!flag.isUnclaimedAllowed(e.getWorld()));
		} else {
			e.setCancelled(!flag.isAllowed(claim));
		}
	}
}
