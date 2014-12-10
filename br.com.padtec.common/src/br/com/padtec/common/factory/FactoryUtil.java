package br.com.padtec.common.factory;

import java.util.List;

import br.com.padtec.common.queries.QueryUtil;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;

public class FactoryUtil {

	/**
	 * Create Individual.
	 * 
	 * @param model: OntModel
	 * @param newIndividual: new Individual URI
	 * @param sameAsList: Same As List of the new Individual URI
	 * @param diffList: Diff List of the new Individual URI
	 * @param srcIndividualURI: source individual URI
	 * @param relationURI: relation URI 
	 * @param rangeClassURI: range class URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel createIndividual(OntModel model, String individualURI, List<String> sameAsList, List<String> diffList, String srcIndividualURI, String relationURI, String rangeClassURI)
	{	
		Individual indInstance = model.getIndividual(srcIndividualURI);
		OntClass ClassImage = model.getOntClass(rangeClassURI);
		Property relation = model.getProperty(relationURI);		
		Individual newInstance = ClassImage.createIndividual(individualURI);		
		for (String s : diffList) 
		{
			Individual i = model.getIndividual(s);
			if(i!=null) i.setDifferentFrom(newInstance);
		}		
		for (String s : sameAsList) 
		{
			Individual i = model.getIndividual(s);
			if(i!=null) i.setSameAs(newInstance);
		}		
		indInstance.addProperty(relation, newInstance);				
		return model;
	}
	
	/**
	 * Delete Individual.
	 * 
	 * @param model: OntModel
	 * @param instance: Individual data transfer object
	 * 
	 * @author John Guerson
	 */
	static public OntModel deleteIndividual(OntModel model, String individualURI) 
	{
		Individual ind = model.getIndividual(individualURI);
		ind.remove();		
		return model;
	}
	
	/**
	 * Update Individual.
	 * 
	 * @param model: OntModel
	 * @param newIndividual: new Individual URI
	 * @param sameAsList: Same As List of the new Individual URI
	 * @param diffList: Diff List of the new Individual URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel updateIndividual(OntModel model, String individualURI, List<String> sameAsList, List<String> diffList) 
	{				
		Individual indInstance = model.getIndividual(individualURI);		
		for (String s : QueryUtil.getIndividualsURIDifferentFrom(model, individualURI)) 
		{
			Individual i = model.getIndividual(s);
			indInstance.removeDifferentFrom(i);
		}
		for (String s : QueryUtil.getIndividualsURISameAs(model,individualURI)) 
		{
			Individual i = model.getIndividual(s);
			indInstance.removeSameAs(i);
		}		
		for (String s : diffList) 
		{
			Individual i = model.getIndividual(s);
			if(i!=null) i.setDifferentFrom(indInstance);
		}		
		for (String s : sameAsList) 
		{
			Individual i = model.getIndividual(s);
			if(i!=null) i.setSameAs(indInstance);
		}					
		return model;
	}
		
	/**
	 * Create a value at the range of the data property relation.
	 * 
	 * @param model: OntModel
	 * @param value: value
	 * @param individualURI: individual URI
	 * @param relationURI: relation URI
	 * @param rangeClassURI: range class URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel createRangeDataPropertyValue(OntModel model, String value, String individualURI, String relationURI, String rangeClassURI) 
	{
		Individual indInstance = model.getIndividual(individualURI);
		Literal literal = model.createTypedLiteral(value,rangeClassURI);
		Property relation = model.getDatatypeProperty(relationURI);	
		indInstance.addProperty(relation, literal);		
		return model;
	}

	/**
	 * Delete a value from the range of the data property relation.
	 * 
	 * @param model: OntModel
	 * @param value: value
	 * @param individualURI: individual URI
	 * @param relationURI: relation URI
	 * @param rangeClassURI: range class URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel deleteRangeDataPropertyValue(OntModel model, String value, String individualURI, String relationURI, String rangeClassURI) 
	{
		Individual indInstance = model.getIndividual(individualURI);
		Literal literal = model.createTypedLiteral(value,rangeClassURI);
		Property relation = model.getDatatypeProperty(relationURI);	
		indInstance.removeProperty(relation, literal);				
		return model;
	}

	/**
	 * Create an object property.
	 * 
	 * @param model: OntModel
	 * @param srcIndividualURI: source individual URI
	 * @param relationURI: relation URI
	 * @param tgtIndividualURI: target individual URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel createObjectProperty(OntModel model, String srcIndividualURI, String relationURI, String tgtIndividualURI) 
	{		
		Individual indInstanceSource = model.getIndividual(srcIndividualURI);
		Individual indInstanceTarget = model.getIndividual(tgtIndividualURI);
		Property relation = model.getProperty(relationURI);
		indInstanceSource.addProperty(relation, indInstanceTarget);		
		return model;
	}
	
	/**
	 * Delete an object property.
	 * 
	 * @param model: OntModel
	 * @param srcIndividualURI: source individual URI
	 * @param relationURI: relation URI
	 * @param tgtIndividualURI: target individual URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel deleteObjectProperty(OntModel model, String srcIndividual, String relationURI, String tgtIndividualURI) 
	{
		Individual indInstanceSource = model.getIndividual(srcIndividual);
		Individual indInstanceTarget = model.getIndividual(tgtIndividualURI);
		Property relation = model.getProperty(relationURI);	
		indInstanceSource.removeProperty(relation, indInstanceTarget);				
		return model;
	}

	/**
	 * Create an individual in a given class.
	 * 
	 * @param model: OntModel
	 * @param individualURI: individual URI
	 * @param classURI: class URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel createIndividualOfClass(OntModel model, String individualURI, String classURI) 
	{	
		Individual indInstance = model.getIndividual(individualURI);
		OntClass cls = model.getOntClass(classURI);		
		indInstance.addOntClass(cls);		
		return model;
	}
	
	/**
	 * Delete an individual in a given class.
	 * 
	 * @param model: OntModel
	 * @param individualURI: individual URI
	 * @param classURI: class URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel deleteIndividualOfClass(OntModel model, String individualURI, String classURI) 
	{	
		Individual indInstance = model.getIndividual(individualURI);
		OntClass cls = model.getOntClass(classURI);		
		indInstance.removeOntClass(cls);		
		return model;
	}
	
	/**
	 * Set Same As.
	 * 
	 * @param model: OntModel
	 * @param individualURI1: individual URI 1
	 * @param individualURI2: individual URI 2
	 * 
	 * @author John Guerson
	 */
	static public OntModel setSameAs(OntModel model, String individualURI1, String individualURI2)
	{
		Individual i1 = model.getIndividual(individualURI1);
		Individual i2 = model.getIndividual(individualURI2);		
		i1.setSameAs(i2);
		i2.setSameAs(i1);		
		return model;
	}

	/**
	 * Set Different From.
	 * 
	 * @param model: OntModel
	 * @param individualURI1: individual URI 1
	 * @param individualURI2: individual URI 2
	 * 
	 * @author John Guerson
	 */
	static public OntModel setDifferentFrom(OntModel model, String individualURI1, String individualURI2)
	{
		Individual i1 = model.getIndividual(individualURI1);
		Individual i2 = model.getIndividual(individualURI2);		
		i1.setDifferentFrom(i2);
		i2.setDifferentFrom(i1);		
		return model;
	}

}