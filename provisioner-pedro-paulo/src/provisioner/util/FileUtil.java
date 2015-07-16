package provisioner.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

	public static final String OWL_EXT = ".owl";
	public static final String TXT_EXT = ".txt";
	
//	public void listFile(String ext) {
//		//File emptyFile = new File("");
//		//File dir = new File(emptyFile.getAbsolutePath());
//		String[] list = getFileList(ext);
//		for (String file : list) {
//			//String temp = new StringBuffer(dir.getPath()).append(File.separator).append(file).toString();
//			System.out.println(file);
//		}
//	}

	public static String[] getFileList(String path, String ext){
		GenericExtFilter filter = new GenericExtFilter(ext);
		 
		File emptyFile = new File("");
		File dir = new File(emptyFile.getAbsolutePath()+"/"+path);
 
		if( ! dir.isDirectory() ){
			System.out.println("Directory does not exists : " + dir.getPath());
			return null;
		}
 
		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);
		
		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return null;
		}
 
		return list;
	}
	
	// inner class, generic extension filter
	public static class GenericExtFilter implements FilenameFilter {
 
		private String ext;
 
		public GenericExtFilter(String ext) {
			this.ext = ext;
		}
 
		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}
	
	public static String chooseFile(String message1, String path, String ext, String message2, int optional) throws Exception{
		String[] files = FileUtil.getFileList(path, ext);
		List<String> filesAux = Arrays.asList(files);
		
		Integer owlFileIndex = ConsoleUtil.chooseOne(filesAux, ext + " files", message1, optional, true);
        
		if(optional == 1 && owlFileIndex <= 0){
			return "";
		}else{
			System.out.println(message2 + files[owlFileIndex]);
	        System.out.println();

	        return path + files[owlFileIndex];
		}
        
	}
	
	public static void createDirs(){
		ArrayList<String> dirs = new ArrayList<String>();
		dirs.add("resources/");
		dirs.add("resources/declared/");
		dirs.add("resources/output/");
		dirs.add("resources/output/tester/");
		dirs.add("resources/owl/");
		dirs.add("resources/possible/");
		
		for (String dir : dirs) {
			File outDir = new File(dir);
			if(!outDir.exists()){
				outDir.mkdirs();
			}
		}		
	}
}
