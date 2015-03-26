package br.com.padtec.nopen.itustudio.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ITUStudioSerializator {

	public static void serialize(String graphJSON, String fileName) throws IOException 
	{
		createFile("src/main/resources/joint/"+fileName+".joint");
		return;
	}

	public static String deserialize(String fileName) throws IOException 
	{	
		String content = readFile("src/main/resources/joint/"+fileName+".joint");
		return content;
	}

	/** Procedure for creating a File */	
    public static File createFile (String path) throws IOException
    {    	
		File file = new File(path);		
		if (!file.exists()) {			
			file.createNewFile();		
		}
		return file;
	}
    
    public static String readFile (String filePath) throws IOException
   	{
   		String result = new String();
   		
   		FileInputStream fstream = new FileInputStream(filePath);			
   		DataInputStream in = new DataInputStream(fstream);
   		BufferedReader br = new BufferedReader(new InputStreamReader(in));
   		String strLine;			
   		while ((strLine = br.readLine()) != null)   
   		{
   			result += strLine+"\n";
   		}
   		in.close();		
   		return result;
   	}
    
    public static void writeToFile (String content, String filePath) throws IOException
   	{
   		File file = createFile(filePath);
   		FileWriter fw = new FileWriter(file,false);
   		PrintWriter pWriter = new PrintWriter(new BufferedWriter(fw));		
   		pWriter.println(content);		
   		pWriter.close();
   		fw.close();						
   	}
}