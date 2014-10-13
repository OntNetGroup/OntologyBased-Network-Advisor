package br.com.padtec.okco.persistence;

import br.com.padtec.common.queries.OntModelAPI;

import com.hp.hpl.jena.ontology.OntModel;
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
		
	/** Constructor */
	public InferredModelRepositoryImpl(InfModel inferredModel)
	{		
		this.inferredModel = inferredModel;
		modelNameSpace = inferredModel.getNsPrefixURI("");	
	}
	
	/**
	 * Set the model of this repository
	 * 
	 * @param inferredModel
	 * 
	 * @author John Guerson
	 */
	public void setInferredModel(InfModel inferredModel)
	{
		this.inferredModel = inferredModel;
		this.modelNameSpace = inferredModel.getNsPrefixURI("");
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
	
	/**
	 * Clone the ontModel passed as argument and replace the existing base model in the repository for the new cloned one. 
	 * 
	 * @param model: OntModel 
	 * 
	 * @author John Guerson
	 */
	public OntModel cloneReplacing(OntModel model)
	{
		OntModel newModel = OntModelAPI.clone(model);
        inferredModel = newModel;
        return newModel;
	}
	
	/**
	 * Erases the repository.
	 * 
	 * @author John Guerson
	 */
	public void clear()
	{
		modelNameSpace="";
		inferredModel=null;
	}
}
