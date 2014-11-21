package br.com.padtec.common.util;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.ManagerInstances;
import br.com.padtec.common.util.Instance;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
//import com.hp.hpl.jena.sparql.function.CastXSD.Instance;

public class FactoryInstances {


	public OntModel CreateInstance(String instanceSource, String Relation, Instance instanceNew, String TargetClass, List<Instance> ListAllInstances, OntModel model)
	{
		ManagerInstances manager = new ManagerInstances(null);	
		
		//Get instance, class, property
		Individual indInstance = model.getIndividual(instanceSource);
		OntClass ClassImage = model.getOntClass(TargetClass);
		Property relation = model.getProperty(Relation);
		
		//Create individual	
		Individual newInstance = ClassImage.createIndividual(instanceNew.ns + instanceNew.name);
		
		//Add same and different
		for (String s : instanceNew.ListDiferentInstances) 
		{
			Instance ins = manager.getInstance(ListAllInstances, s);
			if(!(ins == null))
			{
				Individual i = model.getIndividual(s);
				i.setDifferentFrom(newInstance);
			}
		}
		
		for (String s : instanceNew.ListSameInstances) 
		{
			Instance ins = manager.getInstance(ListAllInstances, s);
			if(!(ins == null))
			{
				Individual i = model.getIndividual(s);
				i.setSameAs(newInstance);
			}
		}
		
		//Add relation
		indInstance.addProperty(relation, newInstance);
		
		//Update new instance values
		instanceNew.existInModel = true;
		
		//Update List of All instances
		//Aplication.ListAllInstances.add(instanceNew);
		
		return model;
	}
	
	
	public OntModel UpdateInstance(Instance instance, OntModel model, InfModel infModel, ArrayList<Instance> ListAllInstances)
	{
		ManagerInstances manager = new ManagerInstances(null);
		
		//Get instance, class, property
		Individual indInstance = model.getIndividual(instance.ns + instance.name);
		
		//Remove the different
		for (String s : QueryUtil.getIndividualsURIDifferentFrom(infModel, instance.ns + instance.name)) 
		{
			Individual i = model.getIndividual(s);
			indInstance.removeDifferentFrom(i);
		}
		
		//Remove the same
		for (String s : QueryUtil.getIndividualsURISameAs(infModel,instance.ns + instance.name)) 
		{
			Individual i = model.getIndividual(s);
			indInstance.removeSameAs(i);
		}
		
		//Add different
		for (String s : instance.ListDiferentInstances) 
		{
			Instance ins = manager.getInstance(ListAllInstances, s);
			if(!(ins == null))
			{
				Individual i = model.getIndividual(s);
				i.setDifferentFrom(indInstance);
			}
		}
		
		//Add same
		for (String s : instance.ListSameInstances) 
		{
			Instance ins = manager.getInstance(ListAllInstances, s);
			if(!(ins == null))
			{
				Individual i = model.getIndividual(s);
				i.setSameAs(indInstance);
			}
		}
					
		return model;
	}
	
	public OntModel DeleteInstance(Instance instance, OntModel model) {

		Individual ind = model.getIndividual(instance.ns + instance.name);
		ind.remove();	// remove every statement that mentions this resource as a subject or object of a statement.		
		return model;
	}

	public OntModel CreateTargetDataProperty(String instanceURI, String relationName, String value, String TargetClass, OntModel model) {
		
		//Get instance, class, property
		Individual indInstance = model.getIndividual(instanceURI);
		Literal literal = model.createTypedLiteral(value,TargetClass);
		Property relation = model.getDatatypeProperty(relationName);	
		indInstance.addProperty(relation, literal);
		
		return model;
	}

	public OntModel DeleteTargetDataProperty(String instanceURI, String relationName, String value, String TargetClass, OntModel model) {

		//Get instance, class, property
		Individual indInstance = model.getIndividual(instanceURI);
		Literal literal = model.createTypedLiteral(value,TargetClass);
		Property relation = model.getDatatypeProperty(relationName);	
		indInstance.removeProperty(relation, literal);
				
		return model;
	}

	public OntModel CreateRelationProperty(String instanceSourceURI, String relationName, String instanceTargetURI,	OntModel model) {
		
		Individual indInstanceSource = model.getIndividual(instanceSourceURI);
		Individual indInstanceTarget = model.getIndividual(instanceTargetURI);
		Property relation = model.getProperty(relationName);
		indInstanceSource.addProperty(relation, indInstanceTarget);
		
		return model;
	}
	
	public OntModel DeleteRelationProperty(String instanceSourceURI, String relationName, String instanceTargetURI,	OntModel model) {

		//Get instance, class, property
		Individual indInstanceSource = model.getIndividual(instanceSourceURI);
		Individual indInstanceTarget = model.getIndividual(instanceTargetURI);
		Property relation = model.getProperty(relationName);	
		indInstanceSource.removeProperty(relation, indInstanceTarget);
				
		return model;
	}

	public OntModel AddInstanceToClass(String instanceUri, String clsUri, OntModel model) {

		//Get instance, class, property
		Individual indInstance = model.getIndividual(instanceUri);
		OntClass cls = model.getOntClass(clsUri);
		
		//Add to class
		indInstance.addOntClass(cls);
		
		return model;
	}
	
	public OntModel RemoveInstanceOnClass(String instanceUri, String clsUri, OntModel model) {

		//Get instance, class, property
		Individual indInstance = model.getIndividual(instanceUri);
		OntClass cls = model.getOntClass(clsUri);
		
		//Remove individual on class
		indInstance.removeOntClass(cls);
		
		return model;
	}

}
