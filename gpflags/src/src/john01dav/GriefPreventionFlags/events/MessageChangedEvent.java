package src.john01dav.GriefPreventionFlags.events;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import src.john01dav.GriefPreventionFlags.Flag.Type;
/** 
 * Event for that occurs when a message is set, changed or removed.
 * This event may occur if a message is changed to the same 
 * string that is currently set or if there is nothing to remove. 
 * 
 * @author Alshain01
 */
public class MessageChangedEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private Claim claim;
	private Type type;
	private String message;
	private Player player;
	private boolean cancel = false;
	
	/** 
	 * Class Constructor
	 * 
	 * @param claim	  	The claim the flag is being set for.
	 * @param trustee 	The player the trust is changing for.
	 * @param isTrusted	True if the player is being added, false if being removed. 
	 * @param player  	The player changing the trust.
	 * 
	 */
	public MessageChangedEvent(Claim claim, Type type, String message, Player player) {
		this.claim = claim;
		this.type = type;
		this.message = message;
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
	 * @returns The new message (null if being removed).
	 */
	public String getMessage() {
		return message;
	}
	
	/** 
	 * HandlerList for FlagSetEvent
	 * 
	 * @return A list of event handlers, stored per-event. Based on lahwran's fevents
	 */	
    public HandlerList getHandlers() {
        return handlers;
    }
    
	/** 
	 * Static HandlerList for FlagSetEvent
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
