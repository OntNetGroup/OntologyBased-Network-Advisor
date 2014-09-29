package br.ufes.inf.nemo.okco.model.inference;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public interface OntologyReasoner {
	
	public abstract InfModel run(OntModel model);

}
