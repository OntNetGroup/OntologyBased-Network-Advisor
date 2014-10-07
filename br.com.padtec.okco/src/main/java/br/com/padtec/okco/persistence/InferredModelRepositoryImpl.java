package br.com.padtec.okco.persistence;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class InferredModelRepositoryImpl implements InferredModelRepository {

	/** Inferred ontology model */
	private InfModel inferredModel;

	/** Default namespace of this inferred ontolgy model */
	private String modelNameSpace;
	
	/** Constructor */
	public InferredModelRepositoryImpl()
	{
		inferredModel = ModelFactory.createOntologyModel();
		modelNameSpace = inferredModel.getNsPrefixURI("");	
	}
	
	/**
	 * Get the inferred Model of this repository
	 * 
	 * @author John Guerson
	 */
	public InfModel getInferredOntModel()
	{
		return inferredModel;		
	}
	
	/** 
	 * Get the default name space of the inferred ontology model.
	 * 
	 * @author John Guerson
	 */
	public String getNameSpace()
	{
		modelNameSpace = inferredModel.getNsPrefixURI("");
		return modelNameSpace;
	}
	
}
