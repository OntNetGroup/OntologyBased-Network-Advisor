package br.com.padtec.transformation.condel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ReaderCondel {
	
	public static String readFile(String path, String separator)
	{
		String result = "";
		
		try {		
			
			FileReader reader = new FileReader(path);	
			BufferedReader in = new BufferedReader(reader);	

			while (in.ready()) 
			{
				String str = in.readLine();
				result = result + str + separator;										
			}
			
			in.close();
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			return null;
		}
		
		return result;	
	}


}
