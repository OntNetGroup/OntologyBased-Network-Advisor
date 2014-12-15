package br.ufes.inf.padtec.tnokco.business;


import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.UploadApp;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
import br.ufes.inf.padtec.tnokco.controller.HomeController;
import br.ufes.inf.padtec.tnokco.controller.ManagerInstances;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class ManagerRelations {
		
	private ManagerInstances manager;
	
	public ManagerRelations(ManagerInstances manager)
	{		
		this.manager = manager;
	}
	
	public OntModel EnforceSubRelation(OntModel model, InfModel infModel, String NS)
	{			
		ArrayList<DtoInstance> ListAllInstances;
		try {
			
			// Get all instances			
			ListAllInstances = manager.getAllInstances(infModel);			
			for (DtoInstance instance : ListAllInstances) 
			{				
				List<String> sourceInstanceClasses = QueryUtil.getClassesURI(infModel,instance.ns + instance.name);
				
				//Relations from instance
				List<DtoInstanceRelation> dtoInstanceRelations = new ArrayList<DtoInstanceRelation>();
				List<String> propertiesURIList = QueryUtil.getPropertiesURI(HomeController.InfModel, instance.ns + instance.name);
				for(String propertyURI: propertiesURIList){
					DtoInstanceRelation dtoItem = new DtoInstanceRelation();
				    dtoItem.Property = propertyURI;
				    List<String> ranges = QueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI);
				    if(ranges.size()>0) dtoItem.Target = ranges.get(0);
				    else dtoItem.Target = "";
				    dtoInstanceRelations.add(dtoItem);
				}
				
				for (DtoInstanceRelation instanceRelation : dtoInstanceRelations) 
				{					
					String property = instanceRelation.Property;
					String targetInstance = instanceRelation.Target;
					List<String> targetInstanceClasses =  QueryUtil.getClassesURI(infModel,targetInstance);
					
					//Get domain-range from property
					List<String> domainList = QueryUtil.getDomainURIs(infModel, property);
					List<String> rangeList = QueryUtil.getRangeURIs(infModel, property);
										
					//List auxiliary for take the sub-properties with domain and range
					List<String> subproperties = QueryUtil.getSubPropertiesURI(infModel, property, true, true);
					for (String subproperty : subproperties) 
					{				
						boolean existInDomainsSource = false;
						boolean existInRangeTarget = false;
						List<String> domainSubPropertyList = QueryUtil.getDomainURIs(infModel, subproperty);
						for(String domain: domainSubPropertyList){
							if(sourceInstanceClasses.contains(domain) && ! (domainList.contains(domain)))
							{
								existInDomainsSource = true;	
							}
						}
						List<String> rangeSubPropertyList = QueryUtil.getRangeURIs(infModel, subproperty);
						for(String range: rangeSubPropertyList){							
							if(targetInstanceClasses.contains(range) && ! (rangeList.contains(range))) 
							{
								existInRangeTarget = true;
							}						
						}						
						if(existInDomainsSource == true && existInRangeTarget == true)
						{
							//Check if the sub property domain and range are not the same as super property
							
							
							//relation have domain and range available
							model = manager.CreateRelationProperty(instance.ns + instance.name, subproperty, targetInstance, model);
						}
					}
				}
			}
			
		} catch (OKCoExceptionInstanceFormat e) {

			e.printStackTrace();
		}		
		
		return model;
	}

}
