package src.john01dav.GriefPreventionFlags;

/**
 * Class for retrieving localized messages.
 * 
 * @author Alshain01
 *
 */
public enum Messages {
	NoClaimError, NoConsoleError, InvalidFlagError, FlagPermError, ValueError, ClaimPermError, GetFlag, SetFlag, SetFlagGlobal,
	GetFlagGlobal, GetAllFlags, InheritedFlag, RemoveFlag, RemoveFlagGlobal, RemoveAllFlags, RemoveAllFlagsError, ValueColorTrue, ValueColorFalse,
	ConsoleHelpHeader, HelpHeader, HelpTopic, HelpInfo, FlagCount, GetFlagUnclaimed, SetFlagUnclaimed, RemoveFlagUnclaimed,
	SetFlagTrust, SetFlagTrustError, GetFlagTrust, RemoveFlagTrust, RemoveFlagTrustError, InvalidTrustError,
	SetCluster, RemoveCluster, NoClustersFound, Flag, Cluster, SetMultipleFlagsError, UnclaimedPermError;
	
	/**
	 * @return A localized message
	 */
	public String get() {
		if (!GriefPreventionFlags.instance.dataStore.isSet("Messages." + this.toString() + ".Text")) {
			GriefPreventionFlags.instance.getLogger().warning("ERROR: Invalid Messages.yml Message for " + this.toString());
			return "ERROR: Invalid Messages.yml Message for " + this.toString();
		}
		String message = GriefPreventionFlags.instance.dataStore.read("Messages." + this.toString() + ".Text");
		return message;
	}	
}
