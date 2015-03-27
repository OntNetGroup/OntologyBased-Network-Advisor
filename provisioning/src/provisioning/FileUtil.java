package provisioning;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

	public static final String OWL_EXT = ".owl";
	public static final String TXT_EXT = ".txt";
	
	public void listFile(String ext) {
		//File emptyFile = new File("");
		//File dir = new File(emptyFile.getAbsolutePath());
		String[] list = getFileList(ext);
		for (String file : list) {
			//String temp = new StringBuffer(dir.getPath()).append(File.separator).append(file).toString();
			System.out.println(file);
		}
	}

	public static String[] getFileList(String ext){
		GenericExtFilter filter = new GenericExtFilter(ext);
		 
		File emptyFile = new File("");
		File dir = new File(emptyFile.getAbsolutePath());
 
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
	
	public static String chooseFile(String fileContaining, String ext) throws Exception{
		String[] files = FileUtil.getFileList(ext);
		List<String> filesAux = Arrays.asList(files);
//		Main.myBubbleSort(filesAux, 1);
//		
//		files = (String[]) filesAux.toArray();
		
		Integer owlFileIndex = Main.chooseOne(filesAux, ext + " files", ext + " file containing " + fileContaining + ": ");
//		System.out.println("--- " + ext + " files ---");
//		for (int i = 0; i < files.length; i++) {
//			System.out.println((i+1) + " - " + files[i]);
//		}
//		
//		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//		do {
//			System.out.print("Choose a " + ext + " file containing " + fileContaining + ": ");
//			owlFileIndex = Integer.valueOf(bufferRead.readLine());
//		} while (owlFileIndex < 1 || owlFileIndex > files.length);
        
                
        System.out.println(fileContaining + " file choosen: " + files[owlFileIndex]);
        System.out.println();

        return files[owlFileIndex];
	}
}
