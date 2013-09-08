package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;

public class InventoryListener implements Listener{
	@EventHandler(ignoreCancelled = true)
	private void onEnchantItem(EnchantItemEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getEnchantBlock().getLocation());
		
		Flag flag = new Flag(Type.SpendExp);
		if((claim == null && !flag.isUnclaimedAllowed(e.getEnchanter().getWorld()))
				|| (claim != null && !flag.isAllowed(claim))) {
			e.setExpLevelCost(0);
		}
	}
}