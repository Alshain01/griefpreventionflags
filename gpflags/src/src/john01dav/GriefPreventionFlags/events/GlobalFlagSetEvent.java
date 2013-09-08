package src.john01dav.GriefPreventionFlags.events;

import src.john01dav.GriefPreventionFlags.Flag.Type;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** 
 * Event that occurs when a global flag value is set
 * @author Alshain01
 * 
 */
public class GlobalFlagSetEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private Type type;
	private CommandSender sender;
	private World world;
	private boolean value;
	private boolean cancel = false;
	
	/** 
	 * Class Constructor
	 * 
	 * @param type   The flag type being set
	 * @param sender The sender setting the flag
	 * @param value  The value the flag is being set to. 
	 */
	public GlobalFlagSetEvent(World world, Type type, CommandSender sender, boolean value) {
		this.type = type;
		this.sender = sender;
		this.value = value;
		this.world = world;
	}
	
	/** 
	 * @returns The flag type associated with the event.
	 */
	public Type getFlagType() {
		return type;
	}
	
	/** 
	 * @returns The CommandSender associated with the event. Null if no sender involved (caused by plug-in).
	 */
	public CommandSender getSender() {
		return sender;
	}
	
	/** 
	 * @returns The World the global flag is being set for.
	 */
	public World getWorld() {
		return world;
	}
	
	/** 
	 * @returns The new value of the flag
	 */
	public boolean getNewValue() {
		return value;
	}
	
	/**
	 * HandlerList for GlobalFlagSetEvent
	 * 
	 * @return A list of event handlers, stored per-event. Based on lahwran's fevents
	 */	
    public HandlerList getHandlers() {
        return handlers;
    }
    
	/** 
	 * Static HandlerList for GlobalFlagSetEvent
	 * 
	 * @return A list of event handlers, stored per-event. Based on lahwran's fevents
	 */
    public static HandlerList getHandlerList() {
        return handlers;
    }

	/** 
	 * Gets the cancellation state of this event. A cancelled event will not be executed in the server, but will still pass to other plugins
	 *  
     * @returns true if this event is cancelled
	 */
	@Override
	public boolean isCancelled() {
		return cancel;
	}

    /**
     * Sets the cancellation state of this event. A cancelled event will not be executed in the server, but will still pass to other plugins. 
	 *
     *@param cancel - true if you wish to cancel this event
	 */
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
