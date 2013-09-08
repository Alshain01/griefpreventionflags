package src.john01dav.GriefPreventionFlags;

import java.util.HashSet;
import java.util.Set;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.command.*;

import src.john01dav.GriefPreventionFlags.listeners.EntityDamageListener;
import src.john01dav.GriefPreventionFlags.Flag.Type;
import src.john01dav.GriefPreventionFlags.commands.Admin;
import src.john01dav.GriefPreventionFlags.commands.CommandManager;
import src.john01dav.GriefPreventionFlags.data.YamlDataStore;
import src.john01dav.GriefPreventionFlags.listeners.*;
import src.john01dav.GriefPreventionFlags.metrics.MetricsManager;

/**
 * GriefPreventionFlags
 * 
 * @author john01dav
 * @author Alshain01
 */
public class GriefPreventionFlags extends JavaPlugin{
	protected YamlDataStore dataStore;
	public int[] FlagCounts = new int[Type.values().length];
	
	public static GriefPreventionFlags instance;
	
	// Contains a list of players who have recently been sent an AllowEntry/AllowLeave message
	
	public Set<String> playersMessaged = new HashSet<String>();
	private BukkitTask playerCleanupTask;
	private PlayerCleanupTask playerMessageCleanupRunnable;
		
//	private BukkitTask databaseCleanupTask;
//	protected DatabaseCleanupTask databaseCleanupRunnable;
	
	public final Boolean isDevBuild = false;
	
	/**
	 * Called when this plug-in is enabled
	 */
	public void onEnable(){
		instance = this;
		
		// Create the configuration file if it doesn't exist
		this.saveDefaultConfig();
		
		if(isDevBuild){
			this.getLogger().warning("This is a development build. The quality of these builds is simply \"it compiles\" the function or loadability of them is not guarunteed.");
		}
		
		// Updated in 1.6 to match parent node for easier programming (see YamlDataStore).
		if (FileManager.fileExists(this.getDataFolder() + "\\flags.yml")) {
			FileManager.rename(this.getDataFolder() + "\\flags.yml", this.getDataFolder() + "\\flag.yml");
		}
		if (FileManager.fileExists(this.getDataFolder() + "\\clusters.yml")) {
			FileManager.rename(this.getDataFolder() + "\\clusters.yml", this.getDataFolder() + "\\cluster.yml");
		}
		
		// Create the specific implementation of DataStore
		// TODO: Add sub-interface for SQL
		dataStore = new YamlDataStore(this);

		if(FileManager.needsConversion()){
			// Update from 1.3
			// Should only occur on first start
			// No need to check if dataStore is YAML, since that is the default.
			dataStore.create(this);
			FileManager.convert(this.getDataFolder() + "\\data");
		} else if (!dataStore.exists(this)) {
			// New installation
			if (!dataStore.create(this)) {
				this.getLogger().warning("Failed to create database schema. Shutting down GriefPreventionFlags.");
				this.getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		// Update the data to current as needed.
		dataStore.update(this);
		
		// Load plugin events and data
		registerEvents(this.getServer().getPluginManager());
		
		playerMessageCleanupRunnable = new PlayerCleanupTask();
		playerCleanupTask = playerMessageCleanupRunnable.runTaskTimerAsynchronously(instance, 0, 100);
		
		LoadFlagCounts();
		if (!isDevBuild) {
			MetricsManager.StartMetrics();
		}
		
		//No longer needed for GP 7.8, handled by event or manually by /gpflags compactdb command.
/*		databaseCleanupRunnable = new DatabaseCleanupTask();
		databaseCleanupTask = databaseCleanupRunnable.runTaskTimer(instance, 0, 600);*/

		this.getLogger().info("GriefPreventionFlags Has Been Enabled!");
        forcePlugin(getServer().getPluginManager(), "GriefPrevention");
	}
	
	/**
	 * Called when this plug-in is disabled 
	 */
	public void onDisable(){
//		databaseCleanupTask.cancel();
		playerCleanupTask.cancel();
		getLogger().info("GriefPreventionFlags Has Been Disabled.");
	}
    
	private void forcePlugin(PluginManager pm, String plugin){
		getLogger().info("Checking for plugin %plugin".replaceAll("%plugin", plugin));
		if(!pm.isPluginEnabled(plugin)){
			getLogger().severe("Plugin %plugin not enabled! Shutting down GriefPreventionFlags");
			pm.disablePlugin(this);
		}
	}
	
	/**
	 * Executes the given command, returning its success 
	 * 
	 * @param sender Source of the command
	 * @param cmd    Command which was executed
	 * @param label  Alias of the command which was used
	 * @param args   Passed command arguments 
	 * @return		 true if a valid command, otherwise false
	 * 
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		//Is done this way so that future commands added will not be needed to be manually added to onenable (ex. what would it look like with 500 commands...)
		if (cmd.getName().equalsIgnoreCase("gpflags")) {
			if(args.length < 1) { return false; }
			return Admin.onCommand(sender, args, dataStore);
		}
		return CommandManager.onCommand(sender, cmd, label, args);
	}
	
	private void registerEvents(PluginManager pm) {
		pm.registerEvents(new GPListener(), instance);
		pm.registerEvents(new GPFlagsListener(), instance);
		
		pm.registerEvents(new PlayerListener(), instance);
		pm.registerEvents(new EntityListener(), instance);
		
		if(Boolean.valueOf(this.getConfig().getString("GriefPreventionFlags.EventListener.Block"))) {
			pm.registerEvents(new BlockListener(), instance);
		}
		if(Boolean.valueOf(this.getConfig().getString("GriefPreventionFlags.EventListener.Inventory"))) {
			pm.registerEvents(new InventoryListener(), instance);
		}
		if(Boolean.valueOf(this.getConfig().getString("GriefPreventionFlags.EventListener.Weather"))) {
			pm.registerEvents(new WeatherListener(), instance);
		}
		if (Boolean.valueOf(this.getConfig().getString("GriefPreventionFlags.EventListener.PlayerMove"))) {
			pm.registerEvents(new PlayerMoveListener(), instance);
		}
		if (Boolean.valueOf(this.getConfig().getString("GriefPreventionFlags.EventListener.CreatureSpawn"))) {
			pm.registerEvents(new CreatureSpawnListener(), instance);
		}
		if (Boolean.valueOf(this.getConfig().getString("GriefPreventionFlags.EventListener.LeafDecay"))) {
			pm.registerEvents(new LeafDecayListener(), instance);
		}
		if (Boolean.valueOf(this.getConfig().getString("GriefPreventionFlags.EventListener.EntityDamage"))) {
			pm.registerEvents(new EntityDamageListener(), instance);
		}
	}
	
	/**
	 * Loads the flag counts into memory.
	 */
	public void LoadFlagCounts() {
		Set<String> claims = GriefPreventionFlags.instance.dataStore.readKeys("data");
		Flag flag = new Flag();
		for(String currentClaim : claims){	
			// Ignore pre-defined entries
			if((currentClaim.equalsIgnoreCase("database")) || (currentClaim.equalsIgnoreCase("global"))){ continue;	}
			
			Claim claim; 
			try {
				claim = GriefPrevention.instance.dataStore.getClaim(Long.parseLong(currentClaim));
			} catch (NumberFormatException e) {
				this.getLogger().warning("Skipping flag counts for invalid claim ID " + currentClaim + ". Consider running command \"gpflags compactdb\"");
				continue;
			}
			for (Type type : Type.values()) {
				flag.setType(type);
				if (flag.getValue(claim) != null) {
					FlagCounts[flag.getType().ordinal()]++;
				}
			}
		}
	}
	
	public void Debug(String message) {
		if (isDevBuild) {
			this.getLogger().info("DEBUG: " + message);
		}
	}
}
