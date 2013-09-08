package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;

public class EntityDamageListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	private void onEntityDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) { return; }
		if(e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.PROJECTILE) {
			// Handled by subclass events
			return;
		}
		
		Claim claim = ClaimManager.getClaimAtLocation(e.getEntity().getLocation());
		World world = e.getEntity().getWorld(); 
		Flag flag = new Flag(Type.DamageBlockExplode);
		
		if (e.getCause() == DamageCause.BLOCK_EXPLOSION) {
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.CONTACT) {
			flag.setType(Type.DamageContact);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.DROWNING) {
			flag.setType(Type.DamageDrown);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.FALL) {
			flag.setType(Type.DamageFall);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.FALLING_BLOCK) {
			flag.setType(Type.DamageBlockFall);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.FIRE) {
			flag.setType(Type.DamageFire);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.FIRE_TICK) {
			flag.setType(Type.DamageBurn);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.LAVA) {
			flag.setType(Type.DamageLava);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.LIGHTNING) {
			flag.setType(Type.DamageLightning);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.MAGIC) {
			flag.setType(Type.DamageMagic);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.MELTING) {
			flag.setType(Type.DamageMelting);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.POISON) {
			flag.setType(Type.DamagePoison);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}

		if (e.getCause() == DamageCause.STARVATION) {
			flag.setType(Type.DamageStarve);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.SUFFOCATION) {
			flag.setType(Type.DamageSuffocate);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.SUICIDE) {
			flag.setType(Type.DamageSuicide);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.THORNS) {
			flag.setType(Type.DamageThorns);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.VOID) {
			flag.setType(Type.DamageVoid);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getCause() == DamageCause.WITHER) {
			flag.setType(Type.DamageWither);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		flag.setType(Type.DamageOther);
		if (claim == null) {
			e.setCancelled(!flag.isUnclaimedAllowed(world));
		} else {
			e.setCancelled(!flag.isAllowed(claim));
		}
	}
}
