package src.john01dav.GriefPreventionFlags.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.ryanhamshire.GriefPrevention.Claim;

/**
 * Event that occurs when a player first enters a new claim.
 * 
 * @author Alshain01
 */
public class PlayerEnterClaimEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	Claim claim;
	Player player;
	Claim exitClaim;
	boolean cancel = false;
	
	/**
	 * Class Constructor
	 * @param player The player involved with the event
	 * @param claim  The claim involved with the event
	 * @param claimLeft The claim the player left to get to this claim
	 */
	public PlayerEnterClaimEvent (Player player, Claim claim, Claim claimLeft) {
		this.claim = claim;
		this.player = player;
		this.exitClaim = claimLeft;
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
	 * @returns The claim the player left to enter this claim.  Null if entering from unclaimed area.
	 */
	public Claim getClaimLeft() {
		return exitClaim;
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
