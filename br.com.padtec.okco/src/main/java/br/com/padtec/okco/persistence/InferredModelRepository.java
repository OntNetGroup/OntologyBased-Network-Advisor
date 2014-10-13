package br.com.padtec.okco.persistence;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public interface InferredModelRepository {
	
	/** 
	 * Get the default name space of the inferred ontology model.
	 * 
	 * @author John Guerson
	 */
	public abstract String getNameSpace();
	
	/**
	 * Get the inferred Model of this repository
	 * 
	 * @author John Guerson
	 */
	public abstract InfModel getInferredOntModel();
	/**
	 * Clone the ontModel passed as argument and replace the existing base model in the repository for the new cloned one. 
	 * 
	 * @param model: OntModel 
	 * 
	 * @author John Guerson
	 */
	public abstract OntModel cloneReplacing(OntModel model);
	
	/**
	 * Erases the repository.
	 * 
	 * @author John Guerson
	 */
	public abstract void clear();
	
	/**
	 * Set the model of this repository
	 * 
	 * @param inferredModel
	 * 
	 * @author John Guerson
	 */
	public abstract void setInferredModel(InfModel inferredModel);

}

