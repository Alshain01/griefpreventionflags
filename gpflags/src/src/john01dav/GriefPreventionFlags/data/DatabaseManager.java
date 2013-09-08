package src.john01dav.GriefPreventionFlags.data;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import src.john01dav.GriefPreventionFlags.GriefPreventionFlags;
import src.john01dav.GriefPreventionFlags.Flag.Type;

/** 
 * Class for ensuring the database is up to date.
 * 
 * @author Alshain01
 */
public abstract class DatabaseManager {
	/**
	 * Upgrades the database from 1.4 or higher to the current revision level.
	 */
	protected static void UpgradeDatabase(JavaPlugin plugin, YamlDataStore dataStore) {
		/* To ensure a proper upgrade from any previous version:
		 * 1. Never change a released build upgrade code. EVER!  
		 * 	  Instead add code to change it from what it is already in the next version.
		 * 2. Never use a return in THIS function.
		 * 3. Always encapsulate the last upgrades below the current one.
		 * 4. Move the upgrade message to the outer most encapsulation and update. 
		 * 
		 * Example
		 * if(version < 1.7) {
		 * 		getLogger().info("Updating database to version 1.7.");
		 * 		if(version < 1.6) {
		 * 			if(version < 1.5) {
		 * 				// Do stuff to upgrade from 1.4 to 1.5
		 * 			}
		 * 			// Do stuff to upgrade from 1.5 to 1.6
		 * 		}
		 * 		// Do stuff to upgrade from 1.6 to 1.7
		 * 		getLogger().info("Database update complete.");
		 * 	} else {
		 * 	...
		 */
		if(dataStore.getVersionMajor() <= 1)
			if(dataStore.getVersionMinor() < 6) {
				plugin.getLogger().info("Updating database to version 1.6.");
				
				if(dataStore.getVersionMinor() < 5) {	
					Upgrade15(dataStore);
				}
				Upgrade16(dataStore);
				plugin.getLogger().info("Database update complete.");
		} else {
			plugin.getLogger().info("Database at current version.");
		}
	}
	
	/**
	 * Upgrades for version 1.6
	 */
	private static void Upgrade16(YamlDataStore dataStore) {
		
		// This code will not work with SQL, however,.
		// SQL Support did not exist prior to the world data file.
		// Therefore the data file is empty if SQL is selected.
		if(dataStore instanceof YamlDataStore) {
			// Move the global off to it's own file for multi-world support.
			List<World> worlds = GriefPreventionFlags.instance.getServer().getWorlds();
			Set<String> flags = dataStore.readKeys("data.global");
			for (String f : flags) {
				// In order to behave like legacy versions of GPFlags,
				// we will assign the global values to ALL worlds.
				// The operator can change it from there.
				String value = dataStore.read("data.global." + f);
				for (World w : worlds) {
					dataStore.write("world." + w.getName() + ".global." + f, value);
				}
			}
			dataStore.write("data.global", (String)null);
		}
		
		// Made this flag a player flag.
		dataStore.write("flag.breeding.message", ChatColor.RED + "You are not allowed to breed animals in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.breeding.unclaimedmessage", ChatColor.RED + "You are not allowed to breed animals here.");
		
		// Unclaimed Area Flags
		dataStore.write("Messages.GetFlagUnclaimed.Text", ChatColor.AQUA + "The flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "is <3> " + ChatColor.AQUA + "in the world.");
		dataStore.write("Messages.GetFlagUnclaimed.Notes", "<2>: The flag name requested by the user.  <3>: The value of the flag.");
		dataStore.write("Messages.SetFlagUnclaimed.Text", ChatColor.AQUA + "Set the flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "to <3> " + ChatColor.AQUA + "in the world.");
		dataStore.write("Messages.SetFlagUnclaimed.Notes", "<2>: The flag name requested by the user.  <3>: The new value of the flag.");
		dataStore.write("Messages.RemoveFlagUnclaimed.Text", ChatColor.AQUA + "Returned the flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "to the global setting in the world.");
		dataStore.write("Messages.RemoveFlagUnclaimed.Notes", "<2>: The flag name requested by the user.");
		dataStore.write("Messages.UnclaimedPermError.Text", ChatColor.RED + "You do not have permission to set a <10> in unclaimed areas.");
		dataStore.write("Messages.UnclaimedPermError.Notes", "<10> flag or cluster");

		// Messages for the unclaimed area
		dataStore.write("flag.fishing.unclaimedmessage", ChatColor.RED + "You are not allowed to fish here.");
		dataStore.write("flag.eat.unclaimedmessage", ChatColor.RED + "You are not allowed to eat here.");
		dataStore.write("flag.portal.unclaimedmessage", ChatColor.RED + "You are not allowed use portals here.");
		dataStore.write("flag.allowtpout.unclaimedmessage", ChatColor.RED + "You are not allowed to teleport from here.");
		dataStore.write("flag.allowtpin.unclaimedmessage", ChatColor.RED + "You are not allowed to teleport to here.");
		dataStore.write("flag.trading.unclaimedmessage", ChatColor.RED + "You are not allowed to trade here.");
		dataStore.write("flag.commands.unclaimedmessage", ChatColor.RED + "Commands are disabled here.");
		dataStore.write("flag.itemdrop.unclaimedmessage", ChatColor.RED + "You are not allowed to drop items here.");
		
		// New Flags
		dataStore.write("flag.siege.name", "siege");
		dataStore.write("flag.siege.description", "Allows or denies players sieging a non-administrative claim.");
		dataStore.write("flag.siege.message", ChatColor.RED + "Your are not allowed to siege " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.trapdoor.name", "trapdoor");
		dataStore.write("flag.trapdoor.description", "Allows or denies players with AccessTrust from using trapdoors.");
		dataStore.write("flag.trapdoor.message", ChatColor.RED + "You are not allowed to use trapdoors in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.trapdoor.unclaimedmessage", ChatColor.RED + "You are not allowed to use trapdoors here.");
		dataStore.write("flag.inheritparent.name", "inheritparent");
		dataStore.write("flag.inheritparent.description", "Causes a subdivision to inherit the flags set for the parent claim.");
		dataStore.write("flag.doorbreak.name", "doorbreak");
		dataStore.write("flag.doorbreak.description", "Toggles Zombies breaking doors in a claim on Hard difficulty level.");
		dataStore.write("flag.spawnchunk.name", "spawnchunk");
		dataStore.write("flag.spawnchunk.description", "Toggles creatures spawning due to chunk generation.");
		dataStore.write("flag.spawnother.name", "spawnother");
		dataStore.write("flag.spawnother.description", "Toggles creatures spawning for any reason not handled by other flags.");
		
		dataStore.write("flag.damageblockexplode.name", "damageblockexplode");
		dataStore.write("flag.damageblockexplode.description", "Toggles damage caused by being in the area when a block explodes.");
		dataStore.write("flag.damagecontact.name", "damagecontact");
		dataStore.write("flag.damagecontact.description", "Toggles damage caused when a player contacts a block such as a cactus.");
		dataStore.write("flag.damagedrown.name", "damagedrown");
		dataStore.write("flag.damagedrown.description", "Toggles damage caused by running out of air while in water.");
		dataStore.write("flag.damagefall.name", "damagefall");
		dataStore.write("flag.damagefall.description", "Toggles damage caused when an player falls a distance greater than 3 blocks.");
		dataStore.write("flag.damageblockfall.name", "damageblockfall");
		dataStore.write("flag.damageblockfall.description", "Toggles damage caused by being hit by a falling block.");
		dataStore.write("flag.damagefire.name", "damagefire");
		dataStore.write("flag.damagefire.description", "Toggles damage caused by direct exposure to fire.");
		dataStore.write("flag.damageburn.name", "damageburn");
		dataStore.write("flag.damageburn.description", "Toggles damage caused due to burns caused by fire.");
		dataStore.write("flag.damagelava.name", "damagelava");
		dataStore.write("flag.damagelava.description", "Toggles damage caused by direct exposure to lava.");
		dataStore.write("flag.damagelightning.name", "damagelightning");
		dataStore.write("flag.damagelightning.description", "Toggles damage caused by being struck by lightning.");
		dataStore.write("flag.damagemagic.name", "damagemagic");
		dataStore.write("flag.damagemagic.description", "Toggles damage caused by being hit by a damage potion or spell.");
		dataStore.write("flag.damagemelting.name", "damagemelting");
		dataStore.write("flag.damagemelting.description", "Toggles damage caused due to a snowman melting.");
		dataStore.write("flag.damagepoison.name", "damagepoison");
		dataStore.write("flag.damagepoison.description", "Toggles damage caused due to an ongoing poison effect.");
		dataStore.write("flag.damagestarve.name", "damagestarve");
		dataStore.write("flag.damagestarve.description", "Toggles damage caused by starving due to having an empty hunger bar.");
		dataStore.write("flag.damagesuffocate.name", "damagesuffocate");
		dataStore.write("flag.damagesuffocate.description", "Toggles damage caused by being put in a block.");
		dataStore.write("flag.damagesuicide.name", "damagesuicide");
		dataStore.write("flag.damagesuicide.description", "Toggles damage by committing suicide using the command /kill");
		dataStore.write("flag.damagethorns.name", "damagethorns");
		dataStore.write("flag.damagethorns.description", "Toggles damage caused in retaliation to another attack by the Thorns enchantment.");
		dataStore.write("flag.damagevoid.name", "damagevoid");
		dataStore.write("flag.damagevoid.description", "Toggles damage caused by falling into the void.");
		dataStore.write("flag.damagewither.name", "damagewither");
		dataStore.write("flag.damagewither.description", "Toggles damage caused by Wither potion effect.");
		dataStore.write("flag.damageother.name", "damageother");
		dataStore.write("flag.damageother.description", "Toggles damage caused by anything not handled by other flags.");
		dataStore.write("flag.damage", (String)null);
		
		// Cluster change
		dataStore.write("cluster.spawnmonster",  Arrays.asList("SpawnByPlugin", "SpawnInvasion", "SpawnJockey", "SpawnLightning", "SpawnMob", "Spawner", "SpawnChunk", "SpawnOther"));
		
		List<String> DamageCluster = Arrays.asList("DamageBlockExplode", "DamageContact", "DamageDrown", "DamageFall", "DamageBlockFall", "DamageFire", "DamageBurn",
				"DamageLava", "DamageLightning", "DamageMagic", "DamageMelting", "DamagePoison", "DamageStarve", "DamageSuffocate",
				"DamageSuicide", "DamageThorns", "DamageVoid", "DamageWither", "DamageOther");
				
		dataStore.write("cluster.damage", DamageCluster);
		dataStore.write("cluster.alldamage", (String)null);
		
		
		Set<String> flags = dataStore.readKeys("data");
		for(String key : flags) {
			if (key != "database") {
				if(dataStore.isSet("data." + key + ".damage") && dataStore.read("data." + key + ".damage").contains("true")) {
					for(String flag : DamageCluster) {
						dataStore.write("data." + key + "." + flag.toLowerCase(), "true");
					}
					dataStore.write("data." + key + ".damage", (String)null);
	
				}
			}
		}
		
		dataStore.setVersion("1.6.0");
	}
	
	/**
	 * Upgrades for version 1.5
	 * -FINAL DO NOT CHANGE EVER!-
	 */
	private static void Upgrade15(YamlDataStore dataStore) {
		// Set the flag "nice" names for localization
		for (Type type : Type.values()) {
			dataStore.write("flag." + type.toString().toLowerCase() +".name", type.toString().toLowerCase());
		}

		// Set the flag descriptions for localization
		dataStore.write("flag.allowentry.description", "Allows or blocks player entry into claim.");
		dataStore.write("flag.allowleave.description", "Allows or blocks a player from leaving a claim.");
		dataStore.write("flag.allowtpin.description", "Allows or blocks teleporting into a claim.");
		dataStore.write("flag.allowtpout.description", "Allows or blocks teleporting from a claim.");
		dataStore.write("flag.breeding.description", "Toggles animal and villager breeding.");
		dataStore.write("flag.breedvillager.description", "Toggles villager breeding.");
		dataStore.write("flag.buildgolem.description", "Toggles ability to build Iron Golems.");
		dataStore.write("flag.buildsnowman.description", "Toggles ability to build Snow Golems.");
		dataStore.write("flag.buildwither.description", "Toggles ability to build the Wither.");
		dataStore.write("flag.commands.description", "Toggles ability to use slash commands.");
		dataStore.write("flag.damage.description", "Toggles damage taken by players by non living entities.");
		dataStore.write("flag.dragoneggtp.description", "Toggles allowing a dragon egg to teleport.");
		dataStore.write("flag.eat.description", "Allows or blocks eating or drinking in the claim.");
		dataStore.write("flag.experience.description", "Allows or blocks gaining experience in the claim.");
		dataStore.write("flag.fishing.description", "Allows or blocks fishing from inside the claim.");
		dataStore.write("flag.grass.description", "Toggles grass grass spreading.");
		dataStore.write("flag.healing.description", "Toggles ability to heal.");
		dataStore.write("flag.hunger.description", "Toggles getting hungry.");
		dataStore.write("flag.ice.description", "Toggles ice forming in snowy biomes.");
		dataStore.write("flag.icemelt.description", "Toggles ice melting due to light level.");
		dataStore.write("flag.itemdrop.description", "Allows or blocks dropping items.");
		dataStore.write("flag.itempickup.description", "Allows or blocks picking up items.");
		dataStore.write("flag.keepexpondeath.description", "Toggles loss of exp on death in claim.");
		dataStore.write("flag.leafdecay.description", "Toggles leaf decay over time.");
		dataStore.write("flag.level.description", "Allows or blocks gaining levels in the claim.");
		dataStore.write("flag.lightning.description", "Toggles lightning striking in a claim.");
		dataStore.write("flag.monsterdamage.description", "Toggles damage taken from monsters.");
		dataStore.write("flag.notifyenter.description", "Claim entry notifications for non-owners.");
		dataStore.write("flag.notifyexit.description", "Claim entry notifications for non-owners.");
		dataStore.write("flag.portal.description", "Allows or blocks the use of portals in the claim.");
		dataStore.write("flag.potionsplash.description", "Toggles disabling potion splash intensity.");
		dataStore.write("flag.pvp.description", "Toggles PvP in administrator claims.");
		dataStore.write("flag.slimesplit.description", "Toggles ability for slime to split.");
		dataStore.write("flag.snow.description", "Toggles snow forming in snowy biomes.");
		dataStore.write("flag.snowmelt.description", "Toggles snow melting due to light level.");
		dataStore.write("flag.spawnbyplugin.description", "Toggles mob spawning by 3rd party plugins.");
		dataStore.write("flag.spawnegg.description", "Toggles mob spawning by thrown eggs.");
		dataStore.write("flag.spawngolem.description", "Toggles spawning of golems in villages.");
		dataStore.write("flag.spawninvasion.description", "Toggles village invasions.");
		dataStore.write("flag.spawnjockey.description", "Toggles spawning of jockeys.");
		dataStore.write("flag.spawnlightning.description", "Toggles mob spawning by lightning strike.");
		dataStore.write("flag.spawnmob.description", "Toggles mob spawning by natural means.");
		dataStore.write("flag.spawner.description", "Toggles mob Spawning by monster spawners.");
		dataStore.write("flag.spawneregg.description", "Toggles using creative mode monster eggs.");
		dataStore.write("flag.spendexp.description", "Toggles exp loss when using enchanting or anvil.");
		dataStore.write("flag.trading.description", "Allows or blocks trading with villagers.");
		
		// Set default custom messages for player flags
		dataStore.write("flag.notifyenter.message", ChatColor.AQUA + "Now entering " + ChatColor.GOLD + "<1>" + ChatColor.AQUA + "'s claim.");
		dataStore.write("flag.notifyexit.message", ChatColor.AQUA + "Now leaving " + ChatColor.GOLD + "<1>" + ChatColor.AQUA + "'s claim.");
		dataStore.write("flag.allowentry.message", ChatColor.RED + "You are not allowed in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.allowleave.message", ChatColor.RED + "You are not allowed to leave " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.fishing.message", ChatColor.RED + "You are not allowed to fish in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.eat.message", ChatColor.RED + "You are not allowed to eat in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.portal.message", ChatColor.RED + "You are not allowed use portals in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.allowtpout.message", ChatColor.RED + "You are not allowed to teleport from " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.allowtpin.message", ChatColor.RED + "You are not allowed to teleport to " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.trading.message", ChatColor.RED + "You are not allowed to trade in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.commands.message", ChatColor.RED + "Commands are disabled in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		dataStore.write("flag.itemdrop.message", ChatColor.RED + "You are not allowed to drop items in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claim.");
		
		// Set other localizations
		// Command Errors
		dataStore.write("Messages.NoClaimError.Text", ChatColor.RED + "Stand in the claim in which you want to get or set a flag.");
		dataStore.write("Messages.NoConsoleError.Text", ChatColor.RED + "This command cannot be performed from the console.");
		dataStore.write("Messages.InvalidFlagError.Text", ChatColor.YELLOW + "<2> " + ChatColor.RED + "is not a valid <10>.");
		dataStore.write("Messages.InvalidFlagError.Notes", "<2>: The flag name requested by the user. <10>: flag or cluster");
		dataStore.write("Messages.InvalidTrustError.Text", ChatColor.YELLOW + "<2> " + ChatColor.RED + "has no player trusted for this claim.");
		dataStore.write("Messages.InvalidTrustError.Notes", "<2>: The flag name requested by the user.");
		dataStore.write("Messages.ValueError.Text", ChatColor.RED + "Value must be true or false.");
		dataStore.write("Messages.NoClustersFound.Text", ChatColor.RED + "There are no clusters defined on this server.");
		dataStore.write("Messages.SetFlagTrust.Text", ChatColor.RED + "Failed to add trust for one or more players.");
		dataStore.write("Messages.RemoveFlagTrustError.Text", ChatColor.RED + "Failed to remove trust for one or more players.");
		dataStore.write("Messages.RemoveAllFlagsError.Text", ChatColor.RED + "Failed to remove one or more flags.");
		dataStore.write("Messages.SetMultipleFlagsError.Text", ChatColor.RED + "Failed to set one or more flags.");
		
		// Permission Errors
		dataStore.write("Messages.FlagPermError.Text", ChatColor.RED + "You do not have permission to use that <10>.");
		dataStore.write("Messages.FlagPermError.Notes", "<10>: flag or cluster");
		
		dataStore.write("Messages.ClaimPermError.Text", ChatColor.RED + "You do not have permission to set a <10> in " + ChatColor.GOLD + "<1>" + ChatColor.RED + "'s claims.");
		dataStore.write("Messages.ClaimPermError.Notes", "<1>: The claim owner's name. <10> flag or cluster");
		
		// Claim flags
		dataStore.write("Messages.GetFlag.Text", ChatColor.AQUA + "The flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "is <3> " + ChatColor.AQUA + "in this claim.");
		dataStore.write("Messages.GetFlag.Notes", "<2>: The flag name requested by the user.  <3>: The value of the flag.");
		dataStore.write("Messages.SetFlag.Text", ChatColor.AQUA + "Set the flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "to <3> " + ChatColor.AQUA + "in this claim.");
		dataStore.write("Messages.SetFlag.Notes", "<2>: The flag name requested by the user.  <3>: The new value of the flag.");
		dataStore.write("Messages.RemoveFlag.Text", ChatColor.AQUA + "Returned the flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "to the global setting in this claim.");
		dataStore.write("Messages.RemoveFlag.Notes", "<2>: The flag name requested by the user.");
		dataStore.write("Messages.InheritedFlag.Text", ChatColor.AQUA + "The flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "is inheriting the global value <3> " + ChatColor.AQUA + "in this claim.");
		dataStore.write("Messages.InheritedFlag.Notes", "<2>: The flag name requested by the user.  <3>: The global value of the flag.");
		dataStore.write("Messages.FlagCount.Text", ChatColor.AQUA + "There are currently " + ChatColor.GREEN + "<7> " + ChatColor.AQUA + "claims with the " + ChatColor.GOLD + "<2> " + ChatColor.AQUA + "flag set.");
		dataStore.write("Messages.FlagCount.Notes", "<2>: The flag name.  <7>: Total flag count.");
		
		// Claim Flags Trust
		dataStore.write("Messages.GetFlagTrust.Text", ChatColor.AQUA + "The following players have " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "trust: " + ChatColor.YELLOW);
		dataStore.write("Messages.SetFlagTrust.Text", ChatColor.AQUA + "Added the player(s) to the trust list for " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + ".");
		dataStore.write("Messages.SetFlagTrust.Notes", "<2>: The flag name.");
		dataStore.write("Messages.RemoveFlagTrust.Text", ChatColor.AQUA + "Removed the player(s) to the trust list for " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + ".");
		dataStore.write("Messages.RemoveFlagTrust.Notes", "<2>: The flag name");
		
		// Global Flags
		dataStore.write("Messages.GetFlagGlobal.Text", ChatColor.AQUA + "The flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "is <3> " + ChatColor.AQUA + "globally.");
		dataStore.write("Messages.GetFlagGlobal.Notes", "<2>: The flag name requested by the user.  <3>: The value of the flag.");
		dataStore.write("Messages.SetFlagGlobal.Text", ChatColor.AQUA + "Set the flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "to <3> " + ChatColor.AQUA + "globally.");
		dataStore.write("Messages.SetFlagGlobal.Notes", "<2>: The flag name requested by the user.  <3>: The new value of the flag.");
		dataStore.write("Messages.RemoveFlagGlobal.Text", ChatColor.AQUA + "Returned the global flag " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "to the plugin-in default setting.");
		dataStore.write("Messages.RemoveFlagGlobal.Notes", "<2>: The flag name requested by the user.");
		
		// All Flags
		dataStore.write("Messages.GetAllFlags.Text", ChatColor.AQUA + "The following flags are set: " + ChatColor.YELLOW);
		dataStore.write("Messages.RemoveAllFlags.Text", ChatColor.AQUA + "Returned all flags to the global or default setting.");
		
		// Clusters
		dataStore.write("Messages.SetCluster.Text", ChatColor.AQUA + "Set all flags in the cluster " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "to <3> " + ChatColor.AQUA + "in this claim.");
		dataStore.write("Messages.SetCluster.Notes", "<2>: The cluster name requested by the user.  <3>: The new value of the cluster.");
		dataStore.write("Messages.RemoveCluster.Text", ChatColor.AQUA + "Returned all flags in the cluster " + ChatColor.YELLOW + "<2> " + ChatColor.AQUA + "to the global or default setting.");
		dataStore.write("Messages.RemoveCluster.Notes", "<2>: The cluster name requested by the user.");
		
		// Help
		dataStore.write("Messages.ConsoleHelpHeader.Text", ChatColor.GOLD + "---------" + ChatColor.WHITE + " <10>: Index " + ChatColor.GOLD + "---------------------------");
		dataStore.write("Messages.HelpHeader.Text", ChatColor.GOLD + "---------" + ChatColor.WHITE + " <10>: Index (<5>/<6>)" + ChatColor.GOLD + "--------------------");
		dataStore.write("Messages.HelpHeader.Notes", "<5>: The current help page.  <6>: The total number of help pages. <10>: The Index Type (Flag or Group)");
		dataStore.write("Messages.HelpInfo.Text", ChatColor.GRAY + "Use /<10>s [n] to get page n of <10>s.");
		dataStore.write("Messages.HelpInfo.Notes", ChatColor.GRAY + "<10>: flag or cluster");
		dataStore.write("Messages.HelpTopic.Text", ChatColor.GOLD + "<2>" + ChatColor.WHITE + ": <4>");
		dataStore.write("Messages.HelpTopic.Notes", "<2>: The flag or cluster name.  <4>: The flag description.");
		
		// Substitutions
		dataStore.write("Messages.ValueColorTrue.Text", ChatColor.GREEN.toString() + "True");
		dataStore.write("Messages.ValueColorFalse.Text", ChatColor.RED.toString() +  "False");
		dataStore.write("Messages.Flag.Text", "Flag");
		dataStore.write("Messages.Cluster.Text", "Cluster");
		
		// Set up the default clusters
		dataStore.write("cluster.alldamage",  Arrays.asList("Damage", "Pvp", "MonsterDamage"));
		dataStore.write("cluster.buildcreature",  Arrays.asList("BuildGolem", "BuildSnowman", "BuildWither"));
		dataStore.write("cluster.jail",  Arrays.asList("AllowEntry", "AllowLeave", "AllowTpIn", "AllowTpOut"));
		dataStore.write("cluster.notify",  Arrays.asList("NotifyExit", "NotifyEnter"));
		dataStore.write("cluster.spawnmonster",  Arrays.asList("SpawnByPlugin", "SpawnInvasion", "SpawnJockey", "SpawnLightning", "SpawnMob", "Spawner"));
		
		// Update the database version.
		dataStore.setVersion("1.5.0");
	}
}
