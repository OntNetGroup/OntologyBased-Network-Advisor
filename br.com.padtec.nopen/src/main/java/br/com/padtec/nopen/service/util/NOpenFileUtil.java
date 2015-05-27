package br.com.padtec.nopen.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NOpenFileUtil {

	public static String path;
	
	// JSON
	public static String topologyJSONFolder;
	public static String templateJSONFolder;
	public static String equipmentJSONFolder;
	public static String provisioningJSONFolder;
	
	/* 
	 * path for the transport function attributes
	 * each filename is the name of a layer
	 * each file is a JSON which contains the attributes definition for a TTF inside that layer
	 */	
	public static String ituConfigurationJSONFolder;
	
	// OWL
	public static String templateOWLFolder;
	public static String equipmentOWLFolder;
	public static String provisioningOWLFolder;
	
	@SuppressWarnings("resource")
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
		
		NOpenFileUtil.templateJSONFolder = NOpenFileUtil.path + "/nopen/repository/template/json/";
		NOpenFileUtil.templateOWLFolder = NOpenFileUtil.path + "/nopen/repository/template/owl/";
		
		NOpenFileUtil.equipmentJSONFolder = NOpenFileUtil.path + "/nopen/repository/equipment/json/";
		NOpenFileUtil.equipmentOWLFolder = NOpenFileUtil.path + "/nopen/repository/equipment/owl/";
		
		NOpenFileUtil.provisioningJSONFolder = NOpenFileUtil.path + "/nopen/repository/provisioning/json/";
		NOpenFileUtil.provisioningOWLFolder = NOpenFileUtil.path + "/nopen/repository/provisioning/owl/";
		
		NOpenFileUtil.ituConfigurationJSONFolder = NOpenFileUtil.path + "/nopen/repository/data-object/json/";
		

		// if SO = Windows
		if(System.getProperty("os.name").contains("Windows")){		
			NOpenFileUtil.topologyJSONFolder = NOpenFileUtil.topologyJSONFolder.replaceAll("/", "\\\\");
			
			NOpenFileUtil.templateJSONFolder = NOpenFileUtil.templateJSONFolder.replaceAll("/", "\\\\");	
			NOpenFileUtil.templateOWLFolder = NOpenFileUtil.templateOWLFolder.replaceAll("/", "\\\\");	
			
			NOpenFileUtil.equipmentJSONFolder = NOpenFileUtil.templateJSONFolder.replaceAll("/", "\\\\");	
			NOpenFileUtil.equipmentOWLFolder = NOpenFileUtil.templateOWLFolder.replaceAll("/", "\\\\");	
			
			NOpenFileUtil.provisioningJSONFolder = NOpenFileUtil.provisioningJSONFolder.replaceAll("/", "\\\\");	
			NOpenFileUtil.provisioningOWLFolder = NOpenFileUtil.provisioningOWLFolder.replaceAll("/", "\\\\");	
			
			NOpenFileUtil.ituConfigurationJSONFolder = NOpenFileUtil.ituConfigurationJSONFolder.replaceAll("/", "\\\\");
		}

		NOpenFileUtil.createRepository(NOpenFileUtil.topologyJSONFolder);
		
		NOpenFileUtil.createRepository(NOpenFileUtil.templateJSONFolder);
		NOpenFileUtil.createRepository(NOpenFileUtil.templateOWLFolder);
		
		NOpenFileUtil.createRepository(NOpenFileUtil.equipmentJSONFolder);
		NOpenFileUtil.createRepository(NOpenFileUtil.equipmentOWLFolder);
		
		NOpenFileUtil.createRepository(NOpenFileUtil.provisioningJSONFolder);
		NOpenFileUtil.createRepository(NOpenFileUtil.provisioningOWLFolder);
		
		NOpenFileUtil.createRepository(NOpenFileUtil.ituConfigurationJSONFolder);
	}

	/**
	 * Procedure to replace slash.
	 * @param path
	 */
	public static String replaceSlash(String value){
		if(System.getProperty("os.name").contains("Windows")){		
			return value = value.replaceAll("/", "\\\\");
		}
		
		return value;
	}
	
	
	
	/**
	 * Procedure to create Topology folders if they do not exist.
	 * @param path
	 */
	public static void createTopologyRepository(String path){
		NOpenFileUtil.createRepository(NOpenFileUtil.topologyJSONFolder + path);	
	}
	
	/**
	 * Procedure to create Template folders if they do not exist.
	 * @param path
	 */
	public static void createTemplateRepository(String path){
		NOpenFileUtil.createRepository(NOpenFileUtil.templateJSONFolder + path);	
	}
	
	/**
	 * Procedure to create Equipment folders if they do not exist.
	 * @param path
	 */
	public static void createEquipmentRepository(String path){
		NOpenFileUtil.createRepository(NOpenFileUtil.equipmentJSONFolder + path);	
	}
	
	/**
	 * Procedure to create Provisioning folders if they do not exist.
	 * @param path
	 */
	public static void createProvisioningRepository(String path){
		NOpenFileUtil.createRepository(NOpenFileUtil.provisioningJSONFolder + path);	
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
	 * Procedure to check if Template file already exist in repository.
	 * @param filename
	 * @return
	 */
	public static Boolean checkTemplateFileExist(String filename){
		
		if(NOpenFileUtil.checkFileExist(NOpenFileUtil.templateJSONFolder + filename)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Procedure to check if Equipment file already exist in repository.
	 * @param filename
	 * @return
	 */
	public static Boolean checkEquipmentFileExist(String filename){
		
		if(NOpenFileUtil.checkFileExist(NOpenFileUtil.equipmentJSONFolder + filename)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Procedure to check if provisioning file already exist in repository.
	 * @param filename
	 * @return
	 */
	public static Boolean checkProvisioningFileExist(String filename){
		
		if(NOpenFileUtil.checkFileExist(NOpenFileUtil.provisioningJSONFolder + filename)){
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
	 * Procedure for create a Topology JSON file.
	 * @param filename 
	 * @return
	 */
	public static File createTopologyJSONFile(String filename){
		return createFile(NOpenFileUtil.topologyJSONFolder, filename + ".json");
	}
	
	/**
	 * Procedure for create a Template JSON file.
	 * @param filename 
	 * @return
	 */
	public static File createTemplateJSONFile(String filename){
		return createFile(NOpenFileUtil.templateJSONFolder, filename + ".json");
	}
	
	/**
	 * Procedure for create a Template OWL file. 
	 * @param filename 
	 * @return
	 */
	public static File createTemplateOWLFile(String filename){
		return createFile(NOpenFileUtil.templateOWLFolder, filename + ".owl");
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
	 * Procedure for create a Provisioning JSON file.
	 * @param filename 
	 * @return
	 */
	public static File createProvisioningJSONFile(String filename){
		return createFile(NOpenFileUtil.provisioningJSONFolder, filename + ".json");
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
    public static String[] getAllTopologyJSONFileNames(){
    	return getAllFolderNames(NOpenFileUtil.topologyJSONFolder);
    }
    
    /**
     * Procedure to get all template JSON file names. 
     * @return
     */
    public static String[] getAllTemplateJSONFileNames(){
    	return getAllFolderNames(NOpenFileUtil.templateJSONFolder);
    }
    
    /**
     * Procedure to get all template OWL file names. 
     * @return
     */
    public static String[] getAllTemplateOWLFileNames(){
    	return getAllFolderNames(NOpenFileUtil.templateOWLFolder);    	
    }
    
    
    /**
     * Procedure to get all equipment JSON file names. 
     * @return
     */
    public static String[] getAllEquipmentJSONFileNames(){
    	return getAllFolderNames(NOpenFileUtil.templateJSONFolder);
    }
    
    /**
     * Procedure to get all provisioning JSON file names. 
     * @return
     */
    public static String[] getAllProvisioningJSONFileNames(){
    	return getAllFolderNames(NOpenFileUtil.provisioningJSONFolder);
    }
    
    /**
     * Generic procedure to gett all folder names
     * @param path
     * @param extension
     * @return
     */
    private static String[] getAllFolderNames(String path){
    	
    	File folder = new File(path);
    	
    	FilenameFilter filter = new FilenameFilter(){
    		
			@Override
			public boolean accept(File dir, String name) {
				
				if(name.substring(0, 1).equals(".") || dir.isHidden()){
					return false;
				}
				
				return true;
			}
    		
    	};
    	
    	return folder.list(filter);
    	
    }
    
    /**
     * Generic procedure to gett all file names
     * @param path
     * @param extension
     * @return
     */
    @SuppressWarnings("unused")
	private static String[] getAllFileNamesWithExtension(String path, String extension){
    	
    	File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		String[] fileNames = {};
		int count = 0;
		
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	    	  
	    	  String filename = listOfFiles[i].getName();
	    	  String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

	    	  if(ext.equals(extension)){
	    		  String name = filename.substring(0, filename.lastIndexOf("."));
	    		  fileNames[count] = name;
	    		  count++;
	    	  }
	    	  
	      } else if (listOfFiles[i].isDirectory()) {

	      }
	    }
		
		return fileNames;
    	
    }
    
    
    /**
     * Procedure to open a Topology file as String.
     * @param filename
     * @return
     */
    public static String openTopologyJSONFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.topologyJSONFolder, filename);
    }
    
    /**
     * Procedure to open an Template file as String.
     * @param filename
     * @return
     */
    public static String openTemplateJSONFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.templateJSONFolder, filename);
    }
    
    /**
     * Procedure to open a Template OWL file as String.
     * @param filename
     * @return
     */
    public static String openTemplateOWLFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.templateOWLFolder, filename);
    }
    
    /**
     * Procedure to open an Equipment file as String.
     * @param filename
     * @return
     */
    public static String openEquipmentJSONFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.templateJSONFolder, filename);
    }
    
    /**
     * Procedure to open an Provisioning file as String.
     * @param filename
     * @return
     */
    public static String openProvisioningJSONFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.provisioningJSONFolder, filename);
    }
	
    /**
     * Procedure to open an ITU configuration file as String.
     * @param filename
     * @return
     */
    public static String openItuConfigurationJSONFileAsString(String filename){
    	return openFileAsString(NOpenFileUtil.ituConfigurationJSONFolder, filename);
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
			//e.printStackTrace();
		}
		
		return content;
    	
    }
    
    /**
     * Procedure to parse a String[] to a JSON String file.
     * @param filename
     * @return
     */
    public static String parseStringToJSON(String property, String[] elements){
    	
    	JsonArray json = new JsonArray();

		for(String element : elements){
			JsonObject j = new JsonObject();
			j.addProperty(property, element);
			
			json.add(j);
		}
		
		return json.toString();
    	
    }
    
}
