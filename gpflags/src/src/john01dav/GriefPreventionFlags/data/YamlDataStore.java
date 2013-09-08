package src.john01dav.GriefPreventionFlags.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import src.john01dav.GriefPreventionFlags.data.CustomYML;

public class YamlDataStore {
	private CustomYML data;
	private CustomYML world;
	private CustomYML flag;
	private CustomYML cluster;
	private CustomYML messages;
	
	private CustomYML getYml(String path) {
		String[] pathList = path.split("\\.");
		
		if (pathList[0].equalsIgnoreCase("world")) { return world; }
		else if (pathList[0].equalsIgnoreCase("flag")) { return flag; }
		else if (pathList[0].equalsIgnoreCase("cluster")) { return cluster; }
		else if (pathList[0].equalsIgnoreCase("messages")) { return messages; }
		else { return data; }
	}
	
	public YamlDataStore(JavaPlugin plugin){
		data = new CustomYML(plugin, "data.yml");
		world = new CustomYML(plugin, "world.yml");
		flag = new CustomYML(plugin, "flag.yml");
		cluster = new CustomYML(plugin, "cluster.yml");
		messages = new CustomYML(plugin, "messages.yml");
	}
	
	public boolean reload(JavaPlugin plugin) {
		data.reloadCustomConfig();
		world.reloadCustomConfig();
		flag.reloadCustomConfig();
		cluster.reloadCustomConfig();
		messages.reloadCustomConfig();
		return true;
	}
	
	public void write(String path, String value) {
		CustomYML cYml = getYml(path);
		cYml.getCustomConfig().set(path, value);
		cYml.saveCustomConfig();
	}

	public void write(String path, List<String> list) {
		CustomYML cYml = getYml(path);
		cYml.getCustomConfig().set(path, list);
		cYml.saveCustomConfig();
	}

	public List<String> readList(String path) {
		CustomYML cYml = getYml(path);
		
		List<?> listData = cYml.getCustomConfig().getList(path);
		if(listData == null) { return null; }
		
		List<String> stringData = new ArrayList<String>();
		
		for (int o = 0; o < listData.size(); o++) {
			stringData.add(((String)listData.get(o)).toLowerCase());
		}
		return stringData;
	}

	public Set<String> readKeys(String path) {
		CustomYML cYml = getYml(path);
		if (isSet(path)) {
			return cYml.getCustomConfig().getConfigurationSection(path).getKeys(false);
		}
		return new HashSet<String>();
	}

	public String read(String path) {
		CustomYML cYml = getYml(path);
		return (isSet(path)) ? cYml.getCustomConfig().getString(path) : "true";
	}

	public int readInt(String path) {
		CustomYML cYml = getYml(path);
		return (isSet(path)) ? cYml.getCustomConfig().getInt(path) : 0;
	}

	public Boolean isSet(String path) {
		CustomYML cYml = getYml(path);
		
		String pathValue = cYml.getCustomConfig().getString(path);
		return (pathValue != null);
	}

	public boolean create(JavaPlugin plugin) {
		// Don't change the version here, not needed
		this.setVersion("1.4.0");
		return true;
	}

	public void update(JavaPlugin plugin) {
		DatabaseManager.UpgradeDatabase(plugin, this);
	}

	public boolean exists(JavaPlugin plugin) {
		File fileObject = new File(plugin.getDataFolder() + "\\data.yml");
		return fileObject.exists();
	}

	public int getVersionMajor() {
		String ver = read("data.database.version");
		
		// Correct for old "2-digit" version storage
		if (ver.length() <= 3) { ver += ".0"; }
		
		String[] version = ver.split("\\.");
		return Integer.valueOf(version[0]);
	}
	
	public int getVersionMinor() {
		String ver = read("data.database.version");
		if (ver.length() <= 3) { ver += ".0"; }
		
		String[] version = ver.split("\\.");
		return Integer.valueOf(version[1]);
	}

	public int getBuild() {
		String ver = read("data.database.version");
		if (ver.length() <= 3) { return 0; }
		
		String[] version = ver.split("\\.");
		return Integer.valueOf(version[2]);
	}
	
	public void setVersion(String version) {
		write("data.database.version", version);
	}
}
