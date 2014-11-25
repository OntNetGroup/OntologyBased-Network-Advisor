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
}
