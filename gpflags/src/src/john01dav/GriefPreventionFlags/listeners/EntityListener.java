package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;

/**
 * Listener for Entity based events
 * 
 * @author john01dav
 * @author Alshain01
 */
public class EntityListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	private void onPlayerDeath(PlayerDeathEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getEntity().getLocation());
		Flag flag = new Flag(Type.KeepExpOnDeath);
		
		if ((claim == null && flag.isUnclaimedAllowed(e.getEntity().getWorld()))
				|| (claim != null && flag.isAllowed(claim))) {
			e.setKeepLevel(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onEntityBreakDoor(EntityBreakDoorEvent e) {
		Claim claim = ClaimManager.getClaimAtLocation(e.getEntity().getLocation());
		
		Flag flag = new Flag(Type.DoorBreak);
		if (claim == null) { 
			e.setCancelled(!flag.isUnclaimedAllowed(e.getBlock().getWorld()));
		} else {
			e.setCancelled(!flag.isAllowed(claim));
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onEntityRegainHealth(EntityRegainHealthEvent e){
		if(!(e.getEntity() instanceof Player)) { return; }
		Claim claim = ClaimManager.getClaimAtLocation(e.getEntity().getLocation());
		
		Flag flag = new Flag(Type.Healing);
		if (claim == null) {
			e.setCancelled(!flag.isUnclaimedAllowed(e.getEntity().getWorld()));
		} else {
			e.setCancelled(!flag.isAllowed(claim));
		}
	}
	
	@EventHandler(ignoreCancelled = true) 
	private void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
		if (!(e.getEntity() instanceof Player)){ return; } 
		Claim claim = ClaimManager.getClaimAtLocation(e.getEntity().getLocation());

		// A player is being damaged in a claim.
		Entity damager = e.getDamager();
		if (damager instanceof Player || (damager instanceof Projectile && ((Projectile)damager).getShooter() instanceof Player)) {
			Flag flag = new Flag(Type.Pvp);
			if ((claim == null && !flag.isUnclaimedAllowed(e.getEntity().getWorld()))
					|| (claim != null && !flag.isAllowed(claim))) {
				// PvP is not allowed in this claim, should we stop it?
				if (!GriefPrevention.instance.dataStore.getPlayerData(((Player)e.getEntity()).getName()).inPvpCombat()) {
					// Don't interfere with a battle, you can't attack and then retreat to a protected claim (that's cheating)
					// Uses GP's timer (15 second default)
					if(claim == null || (claim != null && claim.siegeData == null)) {
						// If your under siege, your on your own.  That's part of the game.
						e.setCancelled(true);
					}
				}
			}
		} else if(damager instanceof Monster || (damager instanceof Projectile && ((Projectile)damager).getShooter() instanceof Monster)) {
			Flag flag = new Flag(Type.MonsterDamage);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getEntity().getWorld()));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
		} else {
			Flag flag = new Flag(Type.DamageOther);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getEntity().getWorld()));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onFoodLevelChange(FoodLevelChangeEvent e){
		if(!(e.getEntity() instanceof Player)) { return; }
		Claim claim = ClaimManager.getClaimAtLocation(e.getEntity().getLocation());
		Flag flag = new Flag(Type.Hunger);
		
		if (e.getFoodLevel() < ((Player)e.getEntity()).getFoodLevel()) {
			if(claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(e.getEntity().getWorld()));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onPotionSplash(PotionSplashEvent e){
		for (LivingEntity player : e.getAffectedEntities()) {
			if (player instanceof Player) {
				Claim claim = ClaimManager.getClaimAtLocation(player.getLocation());
				Flag flag = new Flag(Type.PotionSplash);

				if ((claim == null && !flag.isUnclaimedAllowed(player.getWorld()))
						|| (claim != null && !flag.isAllowed(claim))) {
							
					// Essentially cancels it.
					// Only way to cancel on a player by player basis instead of the whole effect.
					e.setIntensity(player, 0);
				}
			}
		}
	}
}
