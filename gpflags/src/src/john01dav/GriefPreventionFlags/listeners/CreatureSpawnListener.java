package src.john01dav.GriefPreventionFlags.listeners;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import src.john01dav.GriefPreventionFlags.ClaimManager;
import src.john01dav.GriefPreventionFlags.Flag;
import src.john01dav.GriefPreventionFlags.Flag.Type;

/**
 * @author john01dav
 * @author Alshain01
  */
public class CreatureSpawnListener implements Listener{
	@EventHandler(ignoreCancelled = true)
	private void onCreatureSpawn(CreatureSpawnEvent e){
		Claim claim = ClaimManager.getClaimAtLocation(e.getLocation());
		World world = e.getEntity().getWorld(); 
		Flag flag = new Flag(Type.SpawnMob);
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}

		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.VILLAGE_INVASION) {
			flag.setType(Type.SpawnInvasion);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		

		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
			flag.setType(Type.SpawnEgg);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.JOCKEY) {
			flag.setType(Type.SpawnJockey);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.LIGHTNING) {
			flag.setType(Type.SpawnLightning);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE) {
			flag.setType(Type.SpawnGolem);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
			flag.setType(Type.SpawnByPlugin);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
			flag.setType(Type.Spawner);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
			flag.setType(Type.SpawnerEgg);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
			flag.setType(Type.SlimeSplit);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM) {
			flag.setType(Type.BuildGolem);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN) {
			flag.setType(Type.BuildSnowman);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_WITHER) {
			flag.setType(Type.BuildWither);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CHUNK_GEN) {
			flag.setType(Type.SpawnChunk);
			if (claim == null) {
				e.setCancelled(!flag.isUnclaimedAllowed(world));
			} else {
				e.setCancelled(!flag.isAllowed(claim));
			}
			return;
		}
		
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {
			if (e.getEntityType() == EntityType.VILLAGER) {
				flag.setType(Type.BreedVillager);
				if (claim == null) {
					e.setCancelled(!flag.isUnclaimedAllowed(world));
				} else {
					e.setCancelled(!flag.isAllowed(claim));
				}
			}
			return;
		}
		
		flag.setType(Type.SpawnOther);
		if (claim == null) {
			e.setCancelled(!flag.isUnclaimedAllowed(world));
		} else {
			e.setCancelled(!flag.isAllowed(claim));
		}
		return;
	}
}
