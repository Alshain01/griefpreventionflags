package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;

/**
 * Listener for player based events
 * @author john01dav
 * @author Alshain01
 */
public class PlayerListener implements Listener{
	private void sendMessage(Player player, Flag flag, Claim claim) {
		if (claim != null) {
			player.sendMessage(flag.getMessage(claim)
					.replaceAll("<0>", player.getName()));
		} else {
			player.sendMessage(flag.getType().getUnclaimedMessage());
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onPlayerTeleport(PlayerTeleportEvent e) {
		Claim tpFrom = ClaimManager.getClaimAtLocation(e.getFrom());
		Claim tpTo = ClaimManager.getClaimAtLocation(e.getTo());
		Flag flag = new Flag(Type.AllowTpOut);
		Player player = e.getPlayer();
		World world = player.getWorld();
		

		// Teleport out of claim
		if (!flag.hasBypassPermission(tpFrom, player)) {
			if (tpFrom == null && !flag.isUnclaimedAllowed(world)
					|| (tpFrom != null && !flag.isAllowed(tpFrom))) {
					e.setCancelled(true);
					sendMessage(player, flag, tpFrom);
					return;
			} 
		}
		
		flag.setType(Type.AllowTpIn);
		// Teleport into claim
		if (!flag.hasBypassPermission(tpTo, player)) {
			if (tpTo == null && !flag.isUnclaimedAllowed(world)
					|| (tpTo != null && !flag.isAllowed(tpTo))) {
					e.setCancelled(true);
					sendMessage(player, flag, tpTo);
			} 
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.Breeding);
		Entity entity = e.getRightClicked();
		
		
		if ((entity instanceof Villager)) {
			// Villager trading
			flag.setType(Type.Trading);
			if (flag.hasBypassPermission(claim, player)) { return; }
			
			if ((claim == null && !flag.isUnclaimedAllowed(world))
					|| (claim != null && !flag.isAllowed(claim))) {
				e.setCancelled(true);
			}
		} else if ((entity instanceof Animals)) {
			// 1. This is not a "taming" flag, so let it be tamed.
			// 2. This is not a "feeding flag, so let it be fed.
			if ((entity instanceof Tameable) 
					&& (!((Tameable)entity).isTamed()
					|| ((LivingEntity)entity).getHealth() != ((LivingEntity)entity).getMaxHealth())) { return; }
			
			if (flag.hasBypassPermission(claim, player)) { return; }
			
			if ((claim == null && !flag.isUnclaimedAllowed(world))
					|| (claim != null && !flag.isAllowed(claim))) {
				e.setCancelled(true);
			}
		}
		
		if (e.isCancelled()) {
			sendMessage(player, flag, claim);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getMaterial() == Material.TRAP_DOOR) {
			Flag flag = new Flag(Type.TrapDoor);
			if(flag.hasBypassPermission(claim, player)) { return; }

			if ((claim == null && !flag.isUnclaimedAllowed(world))
					|| (claim != null && !flag.isAllowed(claim))) {
					e.setCancelled(true);
					sendMessage(player, flag, claim);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onPlayerFish(PlayerFishEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.Fishing);
		
		if(flag.hasBypassPermission(claim, player)) { return; }
		
		if ((claim == null && !flag.isUnclaimedAllowed(world))
				|| (claim != null && !flag.isAllowed(claim))) {
				e.setCancelled(true);
				sendMessage(player, flag, claim);
		} 
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.Eat);
		
		if(flag.hasBypassPermission(claim, player)) { return; }

		if ((claim == null && !flag.isUnclaimedAllowed(world))
				|| (claim != null && !flag.isAllowed(claim))) {
				e.setCancelled(true);
				sendMessage(player, flag, claim);
		} 
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onPlayerPortal(PlayerPortalEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.Portal);
		
		if(flag.hasBypassPermission(claim, player)) { return; }
		
		if ((claim == null && !flag.isUnclaimedAllowed(world))
				|| (claim != null && !flag.isAllowed(claim))) {
				e.setCancelled(true);
				sendMessage(player, flag, claim);
		} 
	}
	
	@EventHandler
	private void onPlayerExpChange(PlayerExpChangeEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.Experience);
		
		if ((claim == null && !flag.isUnclaimedAllowed(world))
				|| (claim != null && !flag.isAllowed(claim))) {
			if (e.getAmount() > 0) {
				e.setAmount(0);
			}
		} 
	}
	
	@EventHandler
	private void onPlayerLevelChange(PlayerLevelChangeEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.Level);
		
		if(flag.hasBypassPermission(claim, player)) { return; }
		
		if ((claim == null && !flag.isUnclaimedAllowed(world))
				|| (claim != null && !flag.isAllowed(claim))) {
			if(e.getNewLevel() > e.getOldLevel()) {
				// You can't actually stop this event
				// but you can make it ineffective by reducing a players level before they gain it back
				player.setLevel(player.getLevel() - (e.getNewLevel() - e.getOldLevel()));
				player.setExp(0.9999f);
			} 
		} 
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.Commands);

		if ((claim == null && !flag.isUnclaimedAllowed(world))
				|| (claim != null && !flag.isAllowed(claim))) {
				e.setCancelled(true);
				sendMessage(player, flag, claim);
		} 
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerPickupItem(PlayerPickupItemEvent e){
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.ItemPickup);

		if (flag.hasBypassPermission(claim,player)) { return; }
		
		if ((claim == null && !flag.isUnclaimedAllowed(world))
				|| (claim != null && !flag.isAllowed(claim))) {
				e.setCancelled(true);
		} 
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerDropItem(PlayerDropItemEvent e){
		Player player = e.getPlayer();
		World world = player.getWorld();
		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
		Flag flag = new Flag(Type.ItemDrop);
		
		if (flag.hasBypassPermission(claim, player)) { return; }
		
		if ((claim == null && !flag.isUnclaimedAllowed(world))
				|| (claim != null && !flag.isAllowed(claim))) {
				e.setCancelled(true);
				sendMessage(player, flag, claim);
		} 
	}
	
//	@EventHandler(ignoreCancelled = true)
//	private void onPlayerToggleSprint(PlayerToggleSprintEvent e){
//		Player player = e.getPlayer();
//		World world = player.getWorld();
//		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
//		Flag flag = new Flag(Type.Sprint);
//		
//		if (flag.hasBypassPermission(claim, player)) { return; }
//		if (!e.isSprinting()) { return; }
//		
//		if ((claim == null && !flag.isUnclaimedAllowed(world))
//				|| (claim != null && !flag.isAllowed(claim))) {
//				e.setCancelled(true);
//				sendMessage(player, flag, claim);
//		} 
//	}
//	
//	@EventHandler(ignoreCancelled = true)
//	private void onPlayerSneak(PlayerToggleSneakEvent e)	{
//		Player player = e.getPlayer();
//		World world = player.getWorld();
//		Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
//		Flag flag = new Flag(Type.Sneak);
//		
//		if (flag.hasBypassPermission(claim, player)) { return; }
//		if (!e.isSneaking()) { return; }
//		
//		if ((claim == null && !flag.isUnclaimedAllowed(world))
//				|| (claim != null && !flag.isAllowed(claim))) {
//				e.setCancelled(true);
//				sendMessage(player, flag, claim);
//		} 
//	}
//	
//	@EventHandler(ignoreCancelled = true)
//	private void onPlayerFlight(PlayerToggleFlightEvent e)	{
//		Player player = e.getPlayer();
//		World world = player.getWorld();
//		Claim claim = ClaimManager.getClaimAtLocation(e.getPlayer().getLocation());
//		Flag flag = new Flag(Type.Flight);
//
//		if (flag.hasBypassPermission(claim, player)) { return; }
//		if (!e.isFlying()) { return; }
//		
//		if ((claim == null && !flag.isUnclaimedAllowed(world))
//				|| (claim != null && !flag.isAllowed(claim))) {
//				e.setCancelled(true);
//				sendMessage(player, flag, claim);
//		} 
//	}
}
