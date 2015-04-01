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
	public static String topologyFileFolder;
	public static String owlFileFolder;
	
	public void setPath(String path) {
		NOpenFileUtil.path = path;
		NOpenFileUtil.topologyFileFolder = NOpenFileUtil.path + "topology";
		NOpenFileUtil.owlFileFolder = NOpenFileUtil.path + "owl";
		
		NOpenFileUtil.createRepository(NOpenFileUtil.topologyFileFolder);
		NOpenFileUtil.createRepository(NOpenFileUtil.owlFileFolder);
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
		
		if(NOpenFileUtil.checkFileExist(NOpenFileUtil.topologyFileFolder + filename)){
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
	public static File createOWLFile(String filename){
		return createFile(NOpenFileUtil.owlFileFolder, filename + ".owl");
	}
	
	/**
	 * Procedure for create a Topology JSON file.
	 * @param filename 
	 * @return
	 */
	public static File createTopologyJSONFile(String filename){
		return createFile(NOpenFileUtil.topologyFileFolder, filename + ".json");
	}

	/** 
	 * Generic Procedure for creating a File.
	 * @param path
	 * @param filename
	 * @return
	 */
    private static File createFile (String path, String filename) 
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
    	return getAllFileNames(NOpenFileUtil.topologyFileFolder, "json");
    }
    
    /**
     * Procedure to get all topology OWL file names. 
     * @return
     */
    public static HashSet<String> getAllOWLFileNames(){
    	return getAllFileNames(NOpenFileUtil.owlFileFolder, "owl");    	
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
    public static String openTopologyFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.topologyFileFolder, filename);
    }
    
    /**
     * Procedure to open a OWL file as String.
     * @param filename
     * @return
     */
    public static String openOWLFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.owlFileFolder, filename);
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
