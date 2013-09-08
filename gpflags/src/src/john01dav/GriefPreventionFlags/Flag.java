package src.john01dav.GriefPreventionFlags;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.GriefPreventionFlags.events.*;

import me.ryanhamshire.GriefPrevention.Claim;

/** 
 * Class for flag management and data store reading and writing.
 * 
 * @author john01dav
 * @author Alshain01
 */
public class Flag {
    private Type type;

    // ---------- Private Methods ----------
    /** 
     * Returns the claim ID of the claim.
     * If the claim is a subdivision, returns the
     * claim ID of the parent claim and the SubClaim ID as
     * a YML data path.
     * 
     * @param claim The Grief Prevention Claim to return the ID.
     * @return      The claim ID.
     */
    private String getClaimID(Claim claim) {
    	// It's a parent claim
    	if (claim.parent == null) { return String.valueOf(claim.getID()); }
       	// If we are trying to set or get the InheritParent flag, always return the subdivision ID.
    	if (this.type == Type.InheritParent) { return claim.parent.getID() + "." + claim.getSubClaimID(); }

    	// Otherwise check the InheritParent flag.
    	
    	
    	// Check the local flag
    	String dataLocation = "data." + claim.parent.getID() + "." + claim.getSubClaimID() + "." + Type.InheritParent.getName();
    	if (GriefPreventionFlags.instance.dataStore.isSet(dataLocation) 
    			&& GriefPreventionFlags.instance.dataStore.read(dataLocation).toLowerCase().contains("true")) { 
    		return String.valueOf(claim.parent.getID());
    	}
    	
    	// Check the global flag. Assumes default is true if not set.
    	String worldLocation = "world." +  claim.getClaimWorldName() + ".global." + Type.InheritParent.getName();
    	if (!GriefPreventionFlags.instance.dataStore.isSet(dataLocation)) {
        	if (!GriefPreventionFlags.instance.dataStore.isSet(worldLocation) 
        			|| GriefPreventionFlags.instance.dataStore.read(worldLocation).toLowerCase().contains("true")) {
        		return String.valueOf(claim.parent.getID());
        	}
    	}
    	
    	// We can only reach this if either the local or global flag are explicitly defined as false.
    	return claim.parent.getID() + "." + claim.getSubClaimID();
    }
    
    /** 
     * @param claim The Grief Prevention Claim to return the ID.
     * @return      True if the claim specific flag has been set.
     */
    private boolean isDataSet(Claim claim){
    	return GriefPreventionFlags.instance.dataStore.isSet("data." + getClaimID(claim) + "." + type.getName());
    }
    
    /** 
     * @param world The World to return the ID.
     * @return      True if the unclaimed specific flag has been set.
     */
    private boolean isUnclaimedDataSet(World world){
    	return GriefPreventionFlags.instance.dataStore.isSet("world." + world.getName() + ".unclaimed." + type.getName());
    }
    
    /** 
     * @param world The World to return the ID.
     * @return      True if the global flag has been set.
     */
    private boolean isDataSet(World world){
    	return GriefPreventionFlags.instance.dataStore.isSet("world." + world.getName() + ".global." + type.getName());
    }
    
    // ---------- Public Methods ----------
    /** 
     * Enumeration for setting the flag type.
     */
	public enum Type {
		Fishing(true), AllowEntry(true), AllowLeave(true), AllowTpIn(true), AllowTpOut(true), Breeding(true), BreedVillager(true), BuildGolem(true),
		BuildSnowman(true),	BuildWither(true), Commands(true), DoorBreak(true), DragonEggTp(true), Eat(true),
		Experience(true), Grass(true), Healing(true), Hunger(true), Ice(true), IceMelt(true), InheritParent(true), Siege(true),
		ItemDrop(true), ItemPickup(true), KeepExpOnDeath(false), LeafDecay(true), Level(true), Lightning(true),	MonsterDamage(true), 
		NotifyEnter(false), NotifyExit(false), Portal(true), PotionSplash(true), Pvp(true), SlimeSplit(true), Snow(true), 
		SnowMelt(true), SpawnByPlugin(true), SpawnEgg(true), SpawnGolem(true), SpawnInvasion(true), SpawnJockey(true),
		SpawnLightning(true), SpawnMob(true), Spawner(true), SpawnerEgg(true), SpendExp(true), SpawnChunk(true), SpawnOther(true), Trading(true), TrapDoor(true),
		DamageBlockExplode(true), DamageContact(true), DamageDrown(true), DamageFall(true), DamageBlockFall(true), DamageFire(true),
		DamageLava(true), DamageBurn(true), DamageLightning(true), DamageMagic(true), DamageMelting(true), DamagePoison(true), DamageStarve(true),
		DamageSuffocate(true), DamageSuicide(true), DamageThorns(true), DamageVoid(true), DamageWither(true), DamageOther(true);
		
		private boolean def;
	    /** 
	     * Class constructor.
	     * <p>
	     * Creates a new enumeration value.
	     * 
	     * @param def The plug-in default value of the flag.
	     */
		private Type(boolean def) {
			this.def = def;
		}
		
	    /**
	     * @return The fixed name of this type.
	     */  
		public String getName() {
			return this.toString().toLowerCase();
		}
		
	    /**
	     * @return The localized name of this type.
	     */  
		public String getLocalName() {
			String name = GriefPreventionFlags.instance.dataStore.read("flag." + this.toString().toLowerCase() + ".name");
			if (name == null) {
				GriefPreventionFlags.instance.getLogger().warning("ERROR: Invalid Flags.yml Flag Name for " + this.toString());
				return "ERROR: Invalid Flags.yml Flag Name for " + this.toString();
			}
			return name;
		}
		
	    /**
	     * @return The localized description of this type.
	     */  
		public String getDescription() {
			String desc = GriefPreventionFlags.instance.dataStore.read("flag." + this.toString().toLowerCase() + ".description");
			if (desc == null) {
				GriefPreventionFlags.instance.getLogger().warning("ERROR: Invalid Flags.yml Description for " + this.getLocalName());
				return "ERROR: Invalid Flags.yml Description";
			}
			return desc;
		}
		
		/**
		 * @return The plug-in default value for the flag type.
		 */
		public boolean getDefault() {
			return this.def;
		}

	    /** 
	     * Returns the flag's bypass permission for this flag type.
	     * <p>
	     * example: gpflags.bypass.allowentry
	     * 
	     * @return The flag type's bypass permission node
	     */
		public String getBypassPermission() {
			return "gpflags.bypass." + this.toString().toLowerCase();
		}
		
	    /** 
	     * Returns the flag permission for this flag type.
	     * <p>
	     * example: gpflags.flags.pvp
	     * 
	     * @return The flag type's permission node
	     */
		public String getFlagPermission() {
			return "gpflags.flags." + this.toString().toLowerCase();
		}
		
		/**
		 * Returns the message associated with the flag
		 */
		public String getMessage() {
			String dataPath = "flag." + this.toString().toLowerCase() + ".message";
			if (!GriefPreventionFlags.instance.dataStore.isSet(dataPath)) {
				GriefPreventionFlags.instance.getLogger().warning("ERROR: Invalid Flags.yml Message for" + this.getLocalName());
				return "ERROR: Invalid Flags.yml Message";
			}
			return GriefPreventionFlags.instance.dataStore.read(dataPath);
		}
		
		/**
		 * Returns the message associated with the flag
		 */
		public String getUnclaimedMessage() {
			String dataPath = "flag." + this.toString().toLowerCase() + ".unclaimedmessage";
			if (!GriefPreventionFlags.instance.dataStore.isSet(dataPath)) {
				GriefPreventionFlags.instance.getLogger().warning("ERROR: Invalid Flags.yml Message for" + this.getLocalName());
				return "ERROR: Invalid Flags.yml Message";
			}
			return GriefPreventionFlags.instance.dataStore.read(dataPath);
		}
	}
    
    /** 
     * Class constructor.
     * <p>
     * Creates a flag with the AllowEntry type.
     * The type should be changed after creation as necessary.
     */
	public Flag() {
		// Dummy placeholder.
		// For use with setType(String)
		// Done this way so you can give feedback for a bad string.
		this.type = Type.AllowEntry; 
	}
	
    /** 
     * Class constructor.
     * <p>
     * Creates a flag with the the specified type.
     * 
     * @param type The flag type to be created. 
     */
	public Flag(Type type) {
    	this.type = type;
    }
    
    /**
     * Retrieves the type of this Flag instance using the enumeration.
     * <p>
     * getType Does not represent data stored in the database.
     * instead it is the flag type that will determine what data is used
     * when retrieving or setting data using this instance of Flag.
     * 
     * @Return The Type the flag is set to.
     */
    public Type getType() {
    	return this.type;
    }
    
    /**
     * Sets the type of Flag using the enumeration.
     * <p>
     * setType does not alter information in the database,
     * instead it sets the flag type to operate with
     * when retrieving or setting data using this instance of Flag.
     * 
     * @param type The Type to set the flag to.
     */
    public void setType(Type type) {
    	this.type = type;
    }
    
    /**
     * Sets the type of Flag using a string.
     * <p>
     * setType does not alter information in the database,
     * instead it sets the flag type to operate with
     * when retrieving or setting data using this instance of Flag.
     * 
     * @param flag The flag that corresponds to a Type using the localized name.
     * @return     False if the string is not a valid flag.
     */
    public boolean setType(String flag) {
    	if (flag == null) { return false; }
    	
		for (Type t : Type.values()) {
			if (flag.equalsIgnoreCase(t.getLocalName())) {
				this.type = t;
				return true;
			}
		}
		return false;
    }
    
    /**
     * Retrieves the global value of the flag for the primary world
     * 
     * @param world The world to get the global value for.
     * @return The global value of the flag. Returns null if not set.
     * @deprecated As of release 1.6, replaced by {@link #getValue(World)}
     */
    @Deprecated
    public Boolean getValue(){
    	World world = GriefPreventionFlags.instance.getServer().getWorlds().get(0); 
    	return getValue(world);
    }
    
    /**
     * Retrieves the global value of the flag
     * 
     * @param world The world to get the global value for.
     * @return The global value of the flag. Returns null if not set.
     */
    public Boolean getValue(World world){
    	String dataLocation = "world." + world.getName() + ".global." + this.type.getName();
    	
    	if (!GriefPreventionFlags.instance.dataStore.isSet(dataLocation)) { return null; }
    	if (GriefPreventionFlags.instance.dataStore.read(dataLocation).toLowerCase().contains("true")) { return true; }
    	return false;
    }
    
    /**
     * Retrieves the claim specific value of the flag.  
     *
     * @param  claim   The Grief Prevention Claim to retrieve the flag.
     * @return         The claim specific value of the flag.  Returns null if not set.
     */
    public Boolean getValue(Claim claim){
    	if(claim == null){ return null; }
    	String dataLocation = "data." + getClaimID(claim) + "." + this.type.getName();
    	
    	if (!GriefPreventionFlags.instance.dataStore.isSet(dataLocation)) { return null; }
    	if (GriefPreventionFlags.instance.dataStore.read(dataLocation).toLowerCase().contains("true")) { return true; }
    	return false;
    }
    
    /**
     * Retrieves the unclaimed area value of the flag
     * 
     * @param world The world to get the unclaimed value for.
     * @return The unclaimed value of the flag. Returns null if not set.
     */
    public Boolean getUnclaimedValue(World world){
    	String dataLocation = "world." + world.getName() + ".unclaimed." + this.type.getName();
    	
    	if (!GriefPreventionFlags.instance.dataStore.isSet(dataLocation)) { return null; }
    	if (GriefPreventionFlags.instance.dataStore.read(dataLocation).toLowerCase().contains("true")) { return true; }
    	return false;
    }
    
    /**
     * Returns the effective value of the flag.
     * <p>
     * Returns the claim specific value of the claim,
     * if the claim specific value has not been set the
     * global value of the flag is returned instead. If
     * the global value has not been set either, the plug-in
     * default value is returned.
     *
     * @param  claim Grief Prevention Claim class, can be null global and default only for primary world only.
     * @return       The effective value of the flag.
     */
    public boolean isAllowed(Claim claim) {
    	World world;
    	if (claim == null) { 
    		world = GriefPreventionFlags.instance.getServer().getWorlds().get(0); 
    	} else {
    		world = GriefPreventionFlags.instance.getServer().getWorld(claim.getClaimWorldName());
    	}
    	
        if(this.isDataSet(claim)) {
            return this.getValue(claim);
        } else if (this.isDataSet(world)) {
        	return this.getValue(world);
    	} else {
    		return this.type.getDefault();
        }
    }
    
    /**
     * Returns the effective value of the global flag.
     * <p>
     * Returns the global value of the flag.
     * If the global value has not been set either, the plug-in
     * default value is returned.
     *
     * @param  world The world to get the global flag for, can be null default only.
     * @return       The effective value of the flag.
     */
    public boolean isAllowed(World world) {
        if(world != null && this.isDataSet(world)) {
            return this.getValue(world);
    	} else {
    		return this.type.getDefault();
        }
    }
    
    /**
     * Returns the effective value of the unclaimed flag.
     * <p>
     * Returns the unclaimed value of the flag.
     * If the unclaimed value has not been set, the plug-in
     * default value is returned.
     *
     * @param  world The world to get the global flag for, can be null default only.
     * @return       The effective value of the flag.
     */
    public boolean isUnclaimedAllowed(World world) {
        if(world != null && this.isUnclaimedDataSet(world)) {
            return this.getUnclaimedValue(world);
    	} else {
    		return this.type.getDefault();
        }
    }
    
    /**
     * Sets the global value of the flag for the primary world.  
     *
     * @param value The value to set.
     * @param sender The sender changing the flag
     * @return False if the event was cancelled.
     * @deprecated As of release 1.6, replaced by {@link #setValue(World, boolean, CommandSender)}
     * 
     */
    @Deprecated
    public Boolean setValue(boolean value, CommandSender sender){
    	World world = GriefPreventionFlags.instance.getServer().getWorlds().get(0); 
    	return setValue(world, value, sender);
    }

    /**
     * Sets the global value of the flag.  
     *
     * @param value The value to set.
     * @param sender The sender changing the flag
     * @return False if the event was cancelled.
     * 
     */
    public boolean setValue(World world, boolean value, CommandSender sender){
        GlobalFlagSetEvent event = new GlobalFlagSetEvent(world, this.type, sender, value);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
        	GriefPreventionFlags.instance.Debug("SetFlagGlobalEvent was cancelled.");
        	return false; 
        }

        // Set the flag
        String dataLocation = "world." + world.getName() + ".global." + this.type.getName();
        GriefPreventionFlags.instance.Debug("Data Location" + dataLocation);
        GriefPreventionFlags.instance.dataStore.write(dataLocation, String.valueOf(value));
        return true;
    }
    
    /**
     * Sets the unclaimed area value of the flag.  
     *
     * @param value The unclaimed value to set.
     * @param sender The sender changing the flag
     * @return False if the event was cancelled.
     * 
     */
    public boolean setUnclaimedValue(World world, boolean value, CommandSender sender){
        UnclaimedFlagSetEvent event = new UnclaimedFlagSetEvent(world, this.type, sender, value);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) { return false; }

        // Set the flag
        String dataLocation = "world." + world.getName() + ".unclaimed." + this.type.getName();
        GriefPreventionFlags.instance.Debug("Data Location" + dataLocation);
        GriefPreventionFlags.instance.dataStore.write(dataLocation, String.valueOf(value));
        return true;
    }
    
    /**
     * Sets the claim specific value of the flag.  
     *
     * @param claim The Grief Prevention Claim to set the flag.
     * @param player The player changing the flag
     * @param value The value to set.
     * @return False if the event was cancelled or the claim was null.
     */
    public boolean setValue(Claim claim, boolean value, Player player){
    	GriefPreventionFlags.instance.Debug("Processing Flag.setValue Claim Overload");
        FlagSetEvent event = new FlagSetEvent(claim, this.type, player, value);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled())
        {
        	GriefPreventionFlags.instance.Debug("SetFlagEvent was cancelled.");
        	return false;
        }

        // Set the flag
        String dataLocation = "data." + getClaimID(claim) + "." + this.type.getName();
        GriefPreventionFlags.instance.dataStore.write(dataLocation, String.valueOf(value));
        return true;
    }    
    
    /**
     * Removes the claim specific flag for the claim, returning it to the global value.
     * This may be invoked even if the existence of the flag is not known.
     *
     * @param claim The Grief Prevention Claim to remove the flag from.
     * @param player The player removing the flag
     * @return False if the event was cancelled or the claim was null.
     */
    public boolean removeValue(Claim claim, Player player){
    	if (this.getValue(claim) == null) { return false; }
        FlagDeleteEvent event = new FlagDeleteEvent(claim, this.type, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) { return false; }

        // Set the flag
       	String dataLocation= "data." + getClaimID(claim) + "." + this.type.getName();
       	GriefPreventionFlags.instance.dataStore.write(dataLocation, (String)null);
       	return true;
    }
    
    /**
     * Removes the global flag for the flag, returning it to the plug-in default.
     * This may be invoked even if the existence of the flag is not known.
     * 
     * @param sender The user removing the flag.
     * @return False if the event was cancelled.
     * @deprecated As of release 1.6, replaced by {@link #removeValue(World, CommandSender)}
     */    
    @Deprecated
    public Boolean removeValue(CommandSender sender){
    	World world = GriefPreventionFlags.instance.getServer().getWorlds().get(0); 
    	return removeValue(world, sender);
    }
    
    /**
     * Removes the global flag for the flag, returning it to the plug-in default.
     * This may be invoked even if the existence of the flag is not known.
     * 
     * @param sender The user removing the flag.
     * @return False if the event was cancelled.
     */
    public boolean removeValue(World world, CommandSender sender) {
    	GlobalFlagDeleteEvent event = new GlobalFlagDeleteEvent(world, this.type, sender);
    	Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) { return false; }  	
        
        // Set the flag
        String dataLocation= "world." + world.getName() + ".global." + this.type.getName();
        GriefPreventionFlags.instance.dataStore.write(dataLocation, (String)null);
        return true;
    }
    
    /**
     * Removes the global flag for the flag, returning it to the plug-in default.
     * This may be invoked even if the existence of the flag is not known.
     * 
     * @param sender The user removing the flag.
     * @return False if the event was cancelled.
     */
    public boolean removeUnclaimedValue(World world, CommandSender sender) {
    	UnclaimedFlagDeleteEvent event = new UnclaimedFlagDeleteEvent(world, this.type, sender);
    	Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) { return false; }  	
        
        // Set the flag
        String dataLocation= "world." + world.getName() + ".unclaimed." + this.type.getName();
        GriefPreventionFlags.instance.dataStore.write(dataLocation, (String)null);
        return true;
    }
    
    /**
     * Get the bypass permission status of the provided player for
     * Returns true if the player is an Op, has gpflags.*, has gpflags.flags.*,
     * or has the flag specific bypass permission for Flag.getType()
     *
     * @param Player The player for which you want to check the permission status.
     * @return       True if the player has permission to change the flag.
     */
    public boolean hasPermission(Player player) {
		if (player.isOp() 
				|| player.hasPermission("gpflags.*") 
				|| player.hasPermission("gpflags.flags.*")
				|| player.hasPermission(this.type.getFlagPermission())) {
			return true;
		}
		return false;
    }
    
   /**
    * Get the bypass permission status of the provided player for
    * Returns true if the player is an Op, has gpflags.*, has gpflags.bypass.*,
    * has the flag specific bypass permission for Flag.getType() or is on the trust list for non-null claims.
    *
    * @param Claim  The claim to check trust for.
    * @param Player The player for which you want to check the permission status.
    * @return       True if the player has permission to bypass the flag effects.
    */
   public boolean hasBypassPermission(Claim claim, Player player) {
	   
	   if (claim != null) {
		   List<String> trustList = GriefPreventionFlags.instance.dataStore.readList("data." + getClaimID(claim) + "." + this.type.getName() + "trust");
		   if (trustList != null && trustList.contains(player.getName().toLowerCase())) {
			   return true;
		   }
	   }
	   
	   if (player.isOp()
			   || player.hasPermission("gpflags.*") 
			   || player.hasPermission("gpflags.bypass.*")
			   || player.hasPermission(this.type.getBypassPermission())) {
		   return true;
	   }
	   return false;
   }
   
   /**
    * Adds a player name to the trust 
    * list for this flag in the provided claim.
    * 
    * @param claim  The claim with trust access.
    * @param trustee The player to add.
    * @param player	The player requesting the change (may be null)
    * @return False if the event was cancelled or the player was already in the list.
    */
   public boolean setTrust(Claim claim, String trustee, Player player) {
	   // Set the trust
	   List<String> trustList = GriefPreventionFlags.instance.dataStore.readList("data." + getClaimID(claim) + "." + this.type.getName() + "trust");

	   if (trustList != null) {
		   if (trustList.contains(trustee.toLowerCase())) { return false; } // Player was already in the list!
		   trustList.add(trustee.toLowerCase());
	   } else {
		   trustList = Arrays.asList(trustee.toLowerCase());
	   }
	   
	   TrustChangedEvent event = new TrustChangedEvent(claim, this.type, trustee, true, player);
	   Bukkit.getServer().getPluginManager().callEvent(event);
	   if (event.isCancelled()) { return false; }
	   
	   // Set the list
	   GriefPreventionFlags.instance.dataStore.write("data." + getClaimID(claim) + "." + this.type.getName() + "trust", trustList);
	   return true;
   }
   
   /**
    * Removes a player name from the trust
    * list for this flag
    * 
    * @param claim  The claim with trust access.
    * @param player The player to add.
    * @return False if the event was cancelled or the player was not in the list.
    */
   public boolean removeTrust(Claim claim, String trustee, Player player) {
	   // Remove the trust
	   List<String> trustList = GriefPreventionFlags.instance.dataStore.readList("data." + getClaimID(claim) + "." + this.type.getName() + "trust");
	   if (trustList == null) { return false; }
	   int index = trustList.indexOf(trustee.toLowerCase()); 
	   if (index == -1) { return false; } 
	   
	   TrustChangedEvent event = new TrustChangedEvent(claim, this.type, trustee, false, player);
	   Bukkit.getServer().getPluginManager().callEvent(event);
	   if (event.isCancelled()) { return false; }
	   
	   trustList.remove(index);
	   GriefPreventionFlags.instance.dataStore.write("data." + getClaimID(claim) + "." + this.type.getName() + "trust", trustList);
	   
	   return true;
   }
   
   /**
    * @param claim 	The claim to get the trust list.
    * @return		The trust list (null if there is none)
    */
   public List<String> getTrustList(Claim claim) {
	   return GriefPreventionFlags.instance.dataStore.readList("data." + getClaimID(claim) + "." + this.type.getName() + "trust");
   }
   
   /**
    * Set the custom message associated with this flag type in a claim
    * 
    * @param claim		The claim to set the message/
    * @param message	The message to set.
    * @param player		The player making the change.
    * @return False if the event was cancelled or the claim was null.
    */
   public boolean setMessage(Claim claim, String message, Player player) {
	   if(claim == null) { return false; }
	   
	   MessageChangedEvent event = new MessageChangedEvent(claim, this.type, message, player);
	   Bukkit.getServer().getPluginManager().callEvent(event);
	   if (event.isCancelled()) { return false; }
	   
	   String dataLocation= "data." + getClaimID(claim) + "." + this.type.getName() + "message";
	   GriefPreventionFlags.instance.dataStore.write(dataLocation, message);
	   return true;
   }

   /**
    * Returns a custom message for the flag type in a claim.
    * If the message has not been set, the message is loaded from flags.yml
    * 
    * @param claim
    * @return The message for the claim and flag type
    */
   public String getMessage(Claim claim) {
	   if(claim == null){ return null; }
	
	   String dataLocation = "data." + getClaimID(claim) + "." + this.type.getName() + "message";
	
	   if (!GriefPreventionFlags.instance.dataStore.isSet(dataLocation)) {
		   return this.type.getMessage().replaceAll("<1>", claim.getOwnerName());
	   }
	   return GriefPreventionFlags.instance.dataStore.read(dataLocation).replaceAll("<1>", claim.getOwnerName()); // They can use their own color codes
	}
   
   /**
    * Removes the custom message for the flag type in a claim
    * This returns the message back to flags.yml settings
    * 
    * @param claim The claim to remove the message from.
    * @return False if the event was cancelled or the claim was null.
    */
   public boolean removeMessage(Claim claim, Player player) {
	   if(claim == null) { return false; }
	   
	   MessageChangedEvent event = new MessageChangedEvent(claim, this.type, null, player);
	   Bukkit.getServer().getPluginManager().callEvent(event);
	   if (event.isCancelled()) { return false; }
	   
	   String dataLocation= "data." + getClaimID(claim) + "." + this.type.getName() + "message";
	   GriefPreventionFlags.instance.dataStore.write(dataLocation, (String)null);
	   return true;
   }
}