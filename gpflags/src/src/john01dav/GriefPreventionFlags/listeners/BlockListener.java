package src.john01dav.GriefPreventionFlags.listeners;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

/**
 * Listener for block events
 * 
 * @author john01dav
 * @author Alshain01
 */
public class BlockListener implements Listener{
	@EventHandler(ignoreCancelled = true)
	private void onBlockForm(BlockFormEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getBlock().getLocation());
		
		Flag flag = new Flag(Type.Snow);
		if (e.getNewState().getType() == Material.SNOW) {
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getBlock().getWorld()));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
		}

		flag.setType(Type.Ice);
		if (e.getNewState().getType() == Material.ICE) {
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getBlock().getWorld()));
			} else {
				e.setCancelled(!(flag.isAllowed(claim)));
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onBlockFade(BlockFadeEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getBlock().getLocation());

		Flag flag = new Flag(Type.SnowMelt);
		if (e.getBlock().getType() == Material.SNOW) {
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getBlock().getWorld()));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
		}

		flag.setType(Type.IceMelt);
		if (e.getBlock().getType() == Material.ICE) {
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getBlock().getWorld()));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onBlockSpread(BlockSpreadEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getBlock().getLocation());

		Flag flag = new Flag(Type.Grass);
		if (e.getBlock().getType() == Material.GRASS) {
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getBlock().getWorld()));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onBlockFromTo(BlockFromToEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getBlock().getLocation());
		
		Flag flag = new Flag(Type.DragonEggTp);
		if (e.getBlock().getType() == Material.DRAGON_EGG) {
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getBlock().getWorld()));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
		}
	}
}