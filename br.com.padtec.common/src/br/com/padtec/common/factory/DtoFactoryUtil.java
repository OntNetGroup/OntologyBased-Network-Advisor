package br.com.padtec.common.factory;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoCompleteClass;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.queries.QueryUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

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
	
	public static void createAndClassifyIndividualAutomatically(OntModel model, InfModel inferredModel, DtoInstance dtoIndividual) 
	{		
		// Check if the subclasses are disjoint and complete
		for (DtoCompleteClass dto : dtoIndividual.ListCompleteClasses) 
		{
			boolean isDisjoint = true;
			for (String subCls : dto.Members)
			{
				for (String subCls2 : dto.Members) 
				{
					if(!subCls.equals(subCls2))
					{
						boolean result = QueryUtil.isClassesURIDisjoint(inferredModel, subCls, subCls2);
						if(result == true) isDisjoint = false;
					}
				}				
				if(isDisjoint == false) break;
			}			
			if(isDisjoint == true && dto.Members.size() > 0)
			{
				//Classify randomly
				FactoryUtil.createIndividualOfClass(model, dtoIndividual.ns + dtoIndividual.name, dto.Members.get(0));
			}
		}
	}
}
