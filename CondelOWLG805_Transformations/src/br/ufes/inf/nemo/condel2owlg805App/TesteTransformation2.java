package br.ufes.inf.nemo.condel2owlg805App;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.ufes.inf.nemo.condelOwlg805.OwlG805toCondel;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class TesteTransformation2 {

	public static void main(String[] args) throws FileNotFoundException {

		OntModel model = null;
		
		String g805file = "C:/Users/fabio_000/Desktop/output.owl";
		String outFileName = "C:/Users/fabio_000/Desktop/outputCondel.txt";
		
		model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(g805file);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + g805file + " not found");
		}		
		model.read(in,null);
		
		File file = OwlG805toCondel.transformToCondel(outFileName, model);
		
	}

}
