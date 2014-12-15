package br.com.padtec.common.transformation.condel;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class TesteTransformation2 {

	public static void main(String[] args) throws FileNotFoundException {

		OntModel model = null;
		
		String g805file = "C://Users//fabio_000//Desktop//output.owl";
		String outFileName = "C://Users//fabio_000//Desktop//outputCondel.txt";
		
		model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(g805file);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + g805file + " not found");
		}		
		model.read(in,null);
		
		ArrayList<String> instructions = OwlG805toCondel.transformToCondel(model);
		
		/* Creating things for file */
		
		File file = new File(outFileName);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				
			}
		}
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			
		}
		
		BufferedWriter bw = new BufferedWriter(fw);
		
		/* Writing on file */
		
		try {
			for (String ins : instructions) 
			{				
				bw.write(ins);		        
				bw.newLine();	
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			bw.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		
	}

}
