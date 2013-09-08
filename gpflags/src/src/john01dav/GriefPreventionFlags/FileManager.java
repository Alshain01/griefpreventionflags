package src.john01dav.GriefPreventionFlags;
import java.io.*;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

/**
 * Class for handling file management.
 * 
 * @author john01dav
 */
public abstract class FileManager {
	protected static Boolean fileExists(String file){
		File fileObject = new File(file);
		return fileObject.exists();
	}
	
	protected static void rename(String oldFile, String newFile) {
		File oldFileObject = new File(oldFile);
		File newFileObject = new File(newFile);
		oldFileObject.renameTo(newFileObject);
	}
	
	protected static String getFileContents(String file){
		if(!fileExists(file)){ return null; }
		
		try{
			FileReader fileReader =  new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String newLine = reader.readLine(), finalLine = "";
			
			while(newLine != null){
				finalLine += ("/n" + newLine);
				newLine = reader.readLine();
			}
			
			reader.close();
			fileReader.close();
			
			return finalLine;
			
		}catch(Exception e){
	    	e.printStackTrace();
	    	return null;
	    }
		
	}
	
/* OBSOLETE - NO MORE FILE WRITING!
   protected static void setFileContents(String file, String contents){
		try{
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			
			writer.write(contents);
			
			writer.close();
			fileWriter.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
*/
	
	protected static void delete(File f) throws IOException {
	  if (f.isDirectory()) {
	    for (File c : f.listFiles()) {
	      delete(c);
	    }
	  }
	  if (!f.delete()) {
	    throw new FileNotFoundException("Failed to delete file: " + f);
	  }
	}
	
	protected static void convert(String dataPath){
		File dataFolder = new File(dataPath);
		File[] dataFiles = dataFolder.listFiles();
		
		for(int x = 0;x < dataFiles.length; x++){
			if(dataFiles[x].isDirectory()){
				try {
					if(dataFiles[x].getName().equals("global") || GriefPrevention.instance.dataStore.getClaim(Integer.valueOf(dataFiles[x].getName())) != null) { // Make sure there is a claim for the data
						GriefPreventionFlags.instance.getLogger().info("Converting claim %a of %b claims to yaml format.".replaceAll("%a", (x + 1) + "").replaceAll("%b", dataFiles.length + ""));
						parseFolder(dataFiles[x].getAbsolutePath(), dataFiles[x].getName());
					} else {
						GriefPreventionFlags.instance.getLogger().info("Skipping flag data conversion for deleted claim with ID %a.".replaceAll("%a", (dataFiles[x].getName()) + ""));
					}
				} catch (NumberFormatException e) {
					GriefPreventionFlags.instance.getLogger().info("Skipping conversion on invalid data folder named %a.".replaceAll("%a", (dataFiles[x].getName()) + ""));
				}
			}
		}

		try {
			delete(dataFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * @return True if the GriefPreventionFlags has a 1.3 file system
	 */	
	protected static Boolean needsConversion(){
		if(fileExists("./plugins/GriefPreventionFlags/data/") & !fileExists("./plugins/GreifPreventionFlags/data.yml")){
			return true;
		}else{
			return false;
		}
	}
	
	/** 
	 * Converts a single claims flag contents
	 * 
	 * @param folderPath The path of the folder to parse
	 * @param claimid    The id of the claim to be written to the new data format
	 */	
	protected static void parseFolder(String folderPath, String claimid){
		File folder = new File(folderPath);
		File[] flagFiles = folder.listFiles();
		
		for(int x=0;x<flagFiles.length;x++){
			if(!flagFiles[x].isDirectory()){
				String flagname = flagFiles[x].getName();
				
				// Account for changes in flag names between versions
				if (flagname.equalsIgnoreCase("Mob-Spawning")) {
					flagname = "spawnmob";
				} else if (flagname.equalsIgnoreCase("Leaf-Decay")) {
					flagname = "leafdecay";
				}
				
				String value = getFileContents(flagFiles[x].getAbsolutePath()).replaceAll("/n", "");
				String path = ("data." + claimid + "." + flagname).toLowerCase();
				GriefPreventionFlags.instance.dataStore.write(path, value);
			}
		}
	}
}
