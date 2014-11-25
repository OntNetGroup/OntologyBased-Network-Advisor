package br.com.padtec.common.factory;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;

import com.hp.hpl.jena.ontology.OntModel;

public class DtoFactoryUtil {

	static public OntModel createIndividual(OntModel model, DtoInstance newIndividual, String srcIndividualURI, String relationURI, String rangeClassURI)
	{	
		newIndividual.existInModel=true;
		return FactoryUtil.createIndividual(model, newIndividual.ns+newIndividual.name, newIndividual.ListSameInstances, newIndividual.ListDiferentInstances, srcIndividualURI, relationURI, rangeClassURI);
	}
	
	static public OntModel createIndividual(OntModel model, DtoInstance newIndividual, String srcIndividualURI, DtoDefinitionClass dtoDefinitionClass)
	{	
		newIndividual.existInModel=true;
		return FactoryUtil.createIndividual(model, newIndividual.ns+newIndividual.name, newIndividual.ListSameInstances, newIndividual.ListDiferentInstances, srcIndividualURI, dtoDefinitionClass.Relation, dtoDefinitionClass.Target);
	}
	
	static public OntModel createIndividuals(OntModel model, List<DtoInstance> newIndividualList, String srcIndividualURI, String relationURI, String rangeClassURI)	
	{		
		for (DtoInstance newIndividual : newIndividualList) 
		{
			if(newIndividual.existInModel == false)
			{
				newIndividual.existInModel=true;
				model = createIndividual(model,newIndividual,srcIndividualURI, relationURI, rangeClassURI);
			}
		}		
		return model;		
	}
	
	static public OntModel updateIndividual(OntModel model, DtoInstance individual)
	{	
		return FactoryUtil.updateIndividual(model, individual.ns+individual.name, individual.ListSameInstances, individual.ListDiferentInstances);
	}
	
	static public OntModel deleteIndividual(OntModel model, DtoInstance individual)
	{
		return FactoryUtil.deleteIndividual(model, individual.ns+individual.name);
	}
	
	//=======================================================
	// Utility methods for manipulating a list of data transfer objects
	//=======================================================
	
	static public List<DtoInstance> intersection(List<DtoInstance> individualsList, List<String> individualsURIs) 
	{
		List<DtoInstance> list = new ArrayList<DtoInstance>();		
		for (String iName : individualsURIs) 
		{			
			for (DtoInstance instance : individualsList) 
			{				
				if(iName.equals(instance.ns + instance.name))
				{
					list.add(instance);
					break;
				}				
			}
		}		
		return list;
	}
	
	static public List<DtoDefinitionClass> removeRepeatedInSomeDefinition(DtoInstance individualDto)
	{
		ArrayList<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass dto : individualDto.ListSome) 
		{				
			boolean exist = false;
			for (DtoDefinitionClass dto2 : result) 
			{
				if(dto.sameAs(dto2)){exist = true; break; }
			}			
			if(!exist){ result.add(dto); }			
		}
		return result;
	}
	
	static public List<DtoDefinitionClass> removeRepeatedInMinDefinition(DtoInstance individualDto)
	{
		ArrayList<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass dto : individualDto.ListMin) 
		{				
			boolean exist = false;
			for (DtoDefinitionClass dto2 : result) 
			{
				//Doesn't compare the source
				if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
				{ exist = true; break; }
			}			
			if(!exist){ result.add(dto); }			
		}
		return result;
	}

	static public List<DtoDefinitionClass> removeRepeatedInMaxDefinition(DtoInstance individualDto)
	{
		ArrayList<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass dto : individualDto.ListMax) 
		{				
			boolean exist = false;
			for (DtoDefinitionClass dto2 : result) 
			{
				//Doesn't compare the source
				if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
				{ exist = true; break; }
			}			
			if(!exist){ result.add(dto); }			
		}
		return result;
	}	
	
	static public List<DtoDefinitionClass> removeRepeatedInExactDefinition(DtoInstance individualDto)
	{
		ArrayList<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass dto : individualDto.ListExactly) 
		{				
			boolean exist = false;
			for (DtoDefinitionClass dto2 : result) 
			{
				//Doesn't compare the source
				if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
				{ exist = true; break; }
			}			
			if(!exist){ result.add(dto); }			
		}
		return result;
	}	
}
