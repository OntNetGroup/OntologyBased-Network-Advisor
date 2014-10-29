package br.com.padtec.common.persistence;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public interface OntologyReasoner {
	
	public abstract InfModel run(BaseModelRepository baseRepository);
	public abstract InfModel run(OntModel baseModel);
}
