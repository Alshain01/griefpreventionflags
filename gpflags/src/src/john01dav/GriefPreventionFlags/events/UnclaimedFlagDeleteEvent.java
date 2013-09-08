package src.john01dav.GriefPreventionFlags.events;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import src.john01dav.GriefPreventionFlags.Flag.Type;

/**
 * Event that occurs when a global flag is deleted.
 * @author Alshain01
 *
 */
public class UnclaimedFlagDeleteEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private World world;
	private Type type;
	private CommandSender sender;
	private boolean cancel = false;
	
	/** 
	 * Class Constructor
	 * 
	 * @param type       The flag type being deleted
	 * @param sender	 The player attempting to delete the flag
	 */
	public UnclaimedFlagDeleteEvent(World world, Type type, CommandSender sender) {
		this.type = type;
		this.sender = sender;
		this.world = world;
	}
	
	/** 
	 * @returns The flag type associated with the event.
	 */
	public Type getFlagType() {
		return type;
	}
	
	/** 
	 * @returns The World the global flag is being set for.
	 */
	public World getWorld() {
		return world;
	}
	
	/** 
	 * @returns The player associated with the event. Null if no sender involved (caused by plug-in).
	 */
	public CommandSender getSender() {
		return sender;
	}
	
	/** 
	 * HandlerList for GlobalFlagDeleteEvent
	 * 
	 * @return A list of event handlers, stored per-event. Based on lahwran's fevents
	 */	
    public HandlerList getHandlers() {
        return handlers;
    }
    
	/** 
	 * Static HandlerList for GlobalFlagDeleteEvent
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
