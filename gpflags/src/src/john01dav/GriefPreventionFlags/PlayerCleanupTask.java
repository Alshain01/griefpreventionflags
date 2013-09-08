package src.john01dav.GriefPreventionFlags;

import org.bukkit.scheduler.BukkitRunnable;

public class PlayerCleanupTask extends BukkitRunnable {
	public void run() {
		GriefPreventionFlags.instance.playersMessaged.clear();
	}
}
