package br.com.padtec.advisor.application.util;
import java.io.BufferedReader;
import java.io.IOException;


public class FileReader {
	
	public String readFile(BufferedReader in)
	{		
		String str = "";
		try {			
			//FileReader leitor = new FileReader(in);			
			//BufferedReader in = new BufferedReader(leitor);			
			while (in.ready()) 
			{
				str += in.readLine();
				str += "\n";
			}			
			in.close();			
		}catch(IOException e){			
			System.err.println("An IO problem occurred while reading the file");
			return null;
		}		
		return str;
	}

}
