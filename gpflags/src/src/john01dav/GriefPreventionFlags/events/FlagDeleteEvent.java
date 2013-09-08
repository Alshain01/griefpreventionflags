package src.john01dav.GriefPreventionFlags.events;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import src.john01dav.GriefPreventionFlags.Flag.Type;
/** 
 * Event for that occurs when a flag is deleted.
 * This event does not occur if a flag is turned off, 
 * only if it is completely removed.
 * @author Alshain01
 */
public class FlagDeleteEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private Claim claim;
	private Type type;
	private Player player;
	private boolean cancel = false;
	
	/** 
	 * Class Constructor
	 * 
	 * @param folderPath The claim the flag is being deleted from.
	 * @param type       The flag type being deleted
	 * @param player	 The player attempting to delete the flag
	 */
	public FlagDeleteEvent(Claim claim, Type type, Player player) {
		this.claim = claim;
		this.type = type;
		this.player = player;
	}
	
	/** 
	 * @returns The claim associated with the event.
	 */
	public Claim getClaim() {
		return claim;
	}
	
	/** 
	 * @returns The flag type associated with the event.
	 */
	public Type getFlagType() {
		return type;
	}
	
	/** 
	 * @returns The player associated with the event. Null if no sender involved (caused by plug-in).
	 */
	public Player getPlayer() {
		return player;
	}
	
	/** 
	 * HandlerList for FlagDeleteEvent
	 * 
	 * @return A list of event handlers, stored per-event. Based on lahwran's fevents
	 */	
    public HandlerList getHandlers() {
        return handlers;
    }
    
	/** 
	 * Static HandlerList for FlagDeleteEvent
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