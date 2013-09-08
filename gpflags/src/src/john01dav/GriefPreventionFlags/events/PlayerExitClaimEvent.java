package src.john01dav.GriefPreventionFlags.events;

import me.ryanhamshire.GriefPrevention.Claim;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event that occurs when a player leaves a claim.
 * 
 * @author Alshain01
 */
public class PlayerExitClaimEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	Claim claim;
	Claim claimEntered;
	Player player;
	
	boolean cancel = false;
	
	/**
	 * Class Constructor
	 * @param player The player involved with the event
	 * @param claim  The claim involved with the event
	 * @param claimEntered The claim the player entered when leaving.
	 */
	public PlayerExitClaimEvent (Player player, Claim claim, Claim claimEntered) {
		this.claim = claim;
		this.player = player;
	}
	
	/** 
	 * @returns The Player associated with the event.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/** 
	 * @returns The Claim associated with the event.
	 */
	public Claim getClaim() {
		return claim;
	}
	
	/**
	 * @returns The Claim the player entered when leaving. Null if entered unclaimed area.
	 */
	public Claim getClaimEntered() {
		return claimEntered;
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
