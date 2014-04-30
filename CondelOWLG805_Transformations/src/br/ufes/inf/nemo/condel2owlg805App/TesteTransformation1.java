package br.ufes.inf.nemo.condel2owlg805App;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import br.ufes.inf.nemo.condelOwlg805.Condel2owlG805;
import br.ufes.inf.nemo.condelOwlg805.ReaderCondel;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class TesteTransformation1 {

	public static void main(String[] args) throws FileNotFoundException {

		OntModel model = null;
		String separator = "%-%-%";
		
		String g805file = "C://Users//fabio_000//Desktop//OntologiasOwl//G800Completa.owl";
		String outFileName = "C:/Users/fabio_000/Desktop/output2.owl";
		
		String condelFile = "C://Users//fabio_000//Desktop//outputCondel.txt";
		
		model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(g805file);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + g805file + " not found");
		}		
		model.read(in,null);
		
		String codeWithSeparator = ReaderCondel.readFile(condelFile, separator);
		
		model = Condel2owlG805.transformToOWL(model, codeWithSeparator);
		
		FileOutputStream output = new FileOutputStream(outFileName);
		model.write(output, "RDF/XML");
	}

}
