package br.com.padtec.advisor.core.application;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class SubRelationEnforcer {

	protected OKCoUploader repository;
	
	public SubRelationEnforcer(OKCoUploader repository)
	{
		this.repository = repository;
	}
	
	/**
	 * Enforce Sub Relations.
	 * 
	 * @return
	 */
	public OntModel run()
	{				
		InfModel inferredModel = repository.getInferredModel();
		OntModel baseModel = repository.getBaseModel();
		
		List<DtoInstance> allInstances = DtoQueryUtil.getIndividuals(inferredModel, true, true, true);		
		for (DtoInstance instance : allInstances) 
		{		
			/** Get all relations of that instance */
			List<DtoInstanceRelation> dtoRelationsList = new ArrayList<DtoInstanceRelation>();
			List<String> propertiesURIList = QueryUtil.getPropertiesURI(repository.getInferredModel(), instance.ns + instance.name);
			for(String propertyURI: propertiesURIList)
			{
				DtoInstanceRelation dtoItem = new DtoInstanceRelation();
			    dtoItem.Property = propertyURI;
			    List<String> ranges = QueryUtil.getRangeURIs(repository.getInferredModel(), propertyURI);
			    if(ranges.size()>0) dtoItem.Target = ranges.get(0);
			    else dtoItem.Target = "";
			    dtoRelationsList.add(dtoItem);
			}
			
			List<String> sourceClassesURIList = QueryUtil.getClassesURIFromIndividual(inferredModel, instance.ns + instance.name);
			for (DtoInstanceRelation dtoRelation : dtoRelationsList) 
			{					
				String propertyURI = dtoRelation.Property;
									
				/** Get domain and range from property */
				List<String> domainList = QueryUtil.getDomainURIs(inferredModel, propertyURI);
				List<String> rangeList = QueryUtil.getRangeURIs(inferredModel, propertyURI);
				
				/** Get sub-properties */
				List<String> subproperties = QueryUtil.getSubPropertiesURI(inferredModel, propertyURI, true, true);
				
				/** Get classes of the target */
				String targetURI = dtoRelation.Target;
				List<String> targetClassesURIList =  QueryUtil.getClassesURIFromIndividual(inferredModel,targetURI);
				
				for (String subproperty : subproperties) 
				{				
					boolean existInDomainsSource = false;					
					List<String> subDomainList = QueryUtil.getDomainURIs(inferredModel, subproperty);
					for(String domain: subDomainList)
					{
						if(sourceClassesURIList.contains(domain) && ! (domainList.contains(domain))) existInDomainsSource = true;							
					}
					
					boolean existInRangeTarget = false;
					List<String> subRangeList = QueryUtil.getRangeURIs(inferredModel, subproperty);
					for(String range: subRangeList)
					{							
						if(targetClassesURIList.contains(range) && ! (rangeList.contains(range))) existInRangeTarget = true; 						
					}						
					
					if(existInDomainsSource == true && existInRangeTarget == true)
					{
						//FactoryUtil.createObjectProperty(baseModel,instance.ns + instance.name, subproperty, targetURI);
					}
				}
			}
		}		
		return baseModel;
	}
}
