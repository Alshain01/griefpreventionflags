package src.john01dav.GriefPreventionFlags;

import java.util.List;
import java.util.Set;

/** 
 * Class for flag cluster data store reading.
 * 
 * @author Alshain01
 */
public abstract class Cluster {
	// Do this to protect YAMLDataManager
	/**
	 * Retrieves a cluster from the data store.
	 * 
	 * @param cluster The cluster name to retrieve
	 * @return A list containing the cluster.  Null if it doesn't exist.
	 */
	public static List<String> getCluster(String cluster) {
		return GriefPreventionFlags.instance.dataStore.readList("cluster." + cluster.toLowerCase());
	}
	
	/**
	 * @return An array of cluster names configured on the server.
	 */
	public static Set<String> getClusterNames() {
		return GriefPreventionFlags.instance.dataStore.readKeys("cluster");
	}
	
	/**
	 * @param cluster A string cluster name.
	 * @return True if the string is a valid cluster name.
	 */
	public static boolean isCluster(String cluster) {
		return (getClusterNames().contains(cluster.toLowerCase())); 
	}
}
