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

	public static String repository;
	public static String topologyFileFolder;
	public static String owlFileFolder;
	
	public void setRepository(String repository) {
		NOpenFileUtil.repository = repository;
	}

	public void setTopologyFileFolder(String topologyFileFolder) {
		NOpenFileUtil.topologyFileFolder = NOpenFileUtil.repository + topologyFileFolder;
		NOpenFileUtil.createRepoitory(NOpenFileUtil.topologyFileFolder);
	}
	
	public void setOwlFileFolder(String owlFileFolder) {
		NOpenFileUtil.owlFileFolder = NOpenFileUtil.repository + owlFileFolder;
		NOpenFileUtil.createRepoitory(NOpenFileUtil.owlFileFolder);
	}
	
	public static void createRepoitory(String path){
		
		File file = new File(path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
	}
	
	public static Boolean checkTopologyFileExist(String filename){
		
		if(NOpenFileUtil.fileExist(NOpenFileUtil.topologyFileFolder + filename)){
			return true;
		}
		
		return false;
	}
	
	private static Boolean fileExist(String path){
		
		File f = new File(path);
		if(f.exists()){
			return true;
		}
		
		return false;
	}
	
	
	public static File createOWLFile(String filename){
		return createFile(NOpenFileUtil.owlFileFolder, filename);
	}
	
	public static File createTopologyFile(String filename){
		return createFile(NOpenFileUtil.topologyFileFolder, filename);
	}

	/** Procedure for creating a File */	
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
	
    
    public static void writeToFile (File file, String content) throws IOException
   	{
    	PrintStream printStream = new PrintStream(file);
		printStream.print(content);
		printStream.close();				
   	}
    
    public static HashSet<String> getAllTopplogyFiles(){
    	return getAllFiles(NOpenFileUtil.topologyFileFolder, "json");
    }
    public static HashSet<String> getAllOWLFiles(){
    	return getAllFiles(NOpenFileUtil.owlFileFolder, "owl");    	
    }
    
    public static HashSet<String> getAllFiles(String path, String extension){
    	
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
    
    public static String openTopologyFile(String filename){
    	return openFile(NOpenFileUtil.topologyFileFolder, filename);
    }
    
    public static String openOWLFile(String filename){
    	return openFile(NOpenFileUtil.owlFileFolder, filename);
    }
	
    public static String openFile(String path, String filename){
    	
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
