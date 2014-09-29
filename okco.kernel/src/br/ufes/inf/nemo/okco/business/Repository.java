package br.ufes.inf.nemo.okco.business;

import java.io.InputStream;

import com.hp.hpl.jena.ontology.OntModel;

public interface Repository {
	
	public abstract void readBaseOntModel(String inputFileName);
	public abstract void readBaseOntModel(InputStream in);
	public abstract OntModel getBaseOntModel();	
	public abstract OntModel clone(OntModel ontModel);
	public abstract String getNameSpace(OntModel model);
	public abstract void saveBaseOntModel(String path);
	public abstract void saveBaseOntModel();
	public abstract void printOutBaseOntModel();
	public abstract String getBaseOntModelAsString();
}
