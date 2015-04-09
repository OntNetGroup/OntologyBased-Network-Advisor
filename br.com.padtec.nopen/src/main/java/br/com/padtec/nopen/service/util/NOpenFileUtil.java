package br.com.padtec.nopen.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NOpenFileUtil {

	public static String path;
	
	// JSON
	public static String topologyJSONFolder;
	public static String equipmentJSONFolder;
	public static String provisioningJSONFolder;
	
	// OWL
	public static String equipmentOWLFolder;
	public static String provisioningOWLFolder;
	
	public static String convertStreamToString(java.io.InputStream is) 
	{
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	/**
	 * Procedure to set the NOpen Folders Path.
	 * @param path
	 */
	public void setPath(String path) {
		
		NOpenFileUtil.path = System.getProperty("user.home") + path;
		
		NOpenFileUtil.topologyJSONFolder = NOpenFileUtil.path + "/nopen/repository/topology/json/";
		
		NOpenFileUtil.equipmentJSONFolder = NOpenFileUtil.path + "/nopen/repository/equipment/json/";
		NOpenFileUtil.equipmentOWLFolder = NOpenFileUtil.path + "/nopen/repository/equipment/owl/";
		
		NOpenFileUtil.provisioningJSONFolder = NOpenFileUtil.path + "/nopen/repository/provisioning/json/";
		NOpenFileUtil.provisioningOWLFolder = NOpenFileUtil.path + "/nopen/repository/provisioning/owl/";
		

		// if SO = Windows
		if(System.getProperty("os.name").contains("Windows")){		
			NOpenFileUtil.topologyJSONFolder = NOpenFileUtil.topologyJSONFolder.replaceAll("/", "\\\\");
			
			NOpenFileUtil.equipmentJSONFolder = NOpenFileUtil.equipmentJSONFolder.replaceAll("/", "\\\\");	
			NOpenFileUtil.equipmentOWLFolder = NOpenFileUtil.equipmentOWLFolder.replaceAll("/", "\\\\");	
			
			NOpenFileUtil.provisioningJSONFolder = NOpenFileUtil.provisioningJSONFolder.replaceAll("/", "\\\\");	
			NOpenFileUtil.provisioningOWLFolder = NOpenFileUtil.provisioningOWLFolder.replaceAll("/", "\\\\");	
		}

		NOpenFileUtil.createRepository(NOpenFileUtil.topologyJSONFolder);
		
		NOpenFileUtil.createRepository(NOpenFileUtil.equipmentJSONFolder);
		NOpenFileUtil.createRepository(NOpenFileUtil.equipmentOWLFolder);
		
		NOpenFileUtil.createRepository(NOpenFileUtil.provisioningJSONFolder);
		NOpenFileUtil.createRepository(NOpenFileUtil.provisioningOWLFolder);
	}

	/**
	 * Procedure to create folders if they do not exist.
	 * @param path
	 */
	private static void createRepository(String path){
		
		File file = new File(path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
	}
	
	/**
	 * Procedure to check if topology file already exist in repository.
	 * @param filename
	 * @return
	 */
	public static Boolean checkTopologyFileExist(String filename){
		
		if(NOpenFileUtil.checkFileExist(NOpenFileUtil.topologyJSONFolder + filename)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Procedure to check if equipment file already exist in repository.
	 * @param filename
	 * @return
	 */
	private static Boolean checkEquipmentFileExist(String filename){
		
		if(NOpenFileUtil.checkFileExist(NOpenFileUtil.equipmentJSONFolder + filename)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Generic procedure to check if file already exist in repository.
	 * @param filename
	 * @return
	 */
	private static Boolean checkFileExist(String path){
		
		File f = new File(path);
		if(f.exists()){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Procedure for create a OWL file. 
	 * @param filename 
	 * @return
	 */
	public static File createEquipmentOWLFile(String filename){
		return createFile(NOpenFileUtil.equipmentOWLFolder, filename + ".owl");
	}
		
	/**
	 * Procedure for create a Topology JSON file.
	 * @param filename 
	 * @return
	 */
	public static File createTopologyJSONFile(String filename){
		return createFile(NOpenFileUtil.topologyJSONFolder, filename + ".json");
	}
	
	/**
	 * Procedure for create a Equipment JSON file.
	 * @param filename 
	 * @return
	 */
	public static File createEquipmentJSONFile(String filename){
		return createFile(NOpenFileUtil.equipmentJSONFolder, filename + ".json");
	}

	/** 
	 * Generic Procedure for creating a File.
	 * @param path
	 * @param filename
	 * @return
	 */
    public static File createFile (String path, String filename) 
    {    	
		File file = new File(path + filename);		
		if (!file.exists()) {			
			try{
				file.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}			
		}
		return file;
	}
	
    /**
     * Procedure for write a String content in a File.
     * @param file
     * @param content
     * @throws IOException
     */
    public static void writeToFile (File file, String content) throws IOException
   	{
    	PrintStream printStream = new PrintStream(file);
		printStream.print(content);
		printStream.close();				
   	}
    
    /**
     * Procedure to get all topology JSON file names. 
     * @return
     */
    public static HashSet<String> getAllTopplogyJSONFileNames(){
    	return getAllFileNames(NOpenFileUtil.topologyJSONFolder, "json");
    }
    
    /**
     * Procedure to get all equipment JSON file names. 
     * @return
     */
    public static HashSet<String> getAllEquipmentJSONFileNames(){
    	return getAllFileNames(NOpenFileUtil.equipmentJSONFolder, "json");
    }
    
    /**
     * Procedure to get all provisioning JSON file names. 
     * @return
     */
    public static HashSet<String> getAllProvisioningJSONFileNames(){
    	return getAllFileNames(NOpenFileUtil.provisioningJSONFolder, "json");
    }
    
    /**
     * Procedure to get all topology OWL file names. 
     * @return
     */
    public static HashSet<String> getAllEquipmentOWLFileNames(){
    	return getAllFileNames(NOpenFileUtil.equipmentOWLFolder, "owl");    	
    }
    
    
    /**
     * Generic procedure to gett all file names
     * @param path
     * @param extension
     * @return
     */
    private static HashSet<String> getAllFileNames(String path, String extension){
    	
    	File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		HashSet<String> fileHashSet = new HashSet<String>();
		
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	    	  
	    	  String filename = listOfFiles[i].getName();
	    	  String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

	    	  if(ext.equals(extension)){
	    		  String name = filename.substring(0, filename.lastIndexOf("."));
	    		  fileHashSet.add(name);
	    	  }
	    	  
	      } else if (listOfFiles[i].isDirectory()) {

	      }
	    }
		
		return fileHashSet;
    	
    }
    
    /**
     * Procedure to open a topology file as String.
     * @param filename
     * @return
     */
    public static String openTopologyJSONFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.topologyJSONFolder, filename);
    }
    
    /**
     * Procedure to open an equipment file as String.
     * @param filename
     * @return
     */
    public static String openEquipmentJSONFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.equipmentJSONFolder, filename);
    }
    
    /**
     * Procedure to open a OWL file as String.
     * @param filename
     * @return
     */
    public static String openEquipmentOWLFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.equipmentOWLFolder, filename);
    }
	
    /**
     * Generic procedure to open a file as String.
     * @param filename
     * @return
     */
    private static String openFileAsString(String path, String filename){
    	
    	String content = "";
    	
		try {
 
			String line;
			BufferedReader br = new BufferedReader(new FileReader(path + filename));
 
			while ((line = br.readLine()) != null) {
				content = content + line;
			}
			
			br.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
    	
    }
    
    /**
     * Procedure to parse a HashSet<String> to JSON String file.
     * @param filename
     * @return
     */
    public static String parseHashSetToJSON(String property, HashSet<String> hashSet){
    	
    	JsonArray json = new JsonArray();

		for(String element : hashSet){
			JsonObject j = new JsonObject();
			j.addProperty(property, element);
			
			json.add(j);
		}
		
		return json.toString();
    	
    }
    
}
