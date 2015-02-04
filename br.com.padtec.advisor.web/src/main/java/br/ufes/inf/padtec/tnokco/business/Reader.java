package br.ufes.inf.padtec.tnokco.business;
import java.io.BufferedReader;
import java.io.IOException;


public class Reader {
	
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
			
		} catch (IOException e) {
			
			System.out.println("Problem to read the file");
			return null;
		}		
		return str;

	}

}
