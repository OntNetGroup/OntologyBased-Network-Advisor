package br.com.padtec.okco.persistence;

import com.hp.hpl.jena.rdf.model.InfModel;

public interface InferredModelRepository {
	
	public abstract String getNameSpace();
	public abstract InfModel getInferredOntModel();
	
}
