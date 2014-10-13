

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.InfModelQueryUtil;
import br.com.padtec.okco.application.AppLoader;
import br.com.padtec.okco.domain.DtoInstanceRelation;
import br.com.padtec.okco.domain.Instance;
import br.com.padtec.okco.domain.ManagerInstances;
import br.com.padtec.okco.domain.Search;
import br.com.padtec.okco.domain.exceptions.OKCoExceptionInstanceFormat;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class ManagerRelations {
	
	private Search search;
	private ManagerInstances manager;
	
	public ManagerRelations(Search search, ManagerInstances manager)
	{
		this.search = search;
		this.manager = manager;
	}
	
	public OntModel EnforceSubRelation(OntModel model, InfModel infModel, String NS)
	{			
		ArrayList<Instance> ListAllInstances;
		try {
			
			// Get all instances			
			ListAllInstances = manager.getAllInstances(infModel);			
			for (Instance instance : ListAllInstances) 
			{				
				List<String> sourceInstanceClasses = InfModelQueryUtil.getClassesURI(infModel,instance.ns + instance.name);
				
				//Get instance relations
				List<DtoInstanceRelation> dtoInstanceRelations = new ArrayList<DtoInstanceRelation>();
				List<String> propertiesURIList = InfModelQueryUtil.getPropertiesURI(AppLoader.InfModel, instance.ns + instance.name);
				for(String propertyURI: propertiesURIList){
					DtoInstanceRelation dtoItem = new DtoInstanceRelation();
				    dtoItem.Property = propertyURI;
				    dtoItem.Target = InfModelQueryUtil.getRangeURIs(AppLoader.InfModel, propertyURI).get(0);
				    dtoInstanceRelations.add(dtoItem);
				}

				//Relations from instance
				for (DtoInstanceRelation instanceRelation : dtoInstanceRelations) 
				{					
					String property = instanceRelation.Property;
					String targetInstance = instanceRelation.Target;
					List<String> targetInstanceClasses = InfModelQueryUtil.getClassesURI(infModel,targetInstance);
					
					//Get domain-range from property
					List<String> domainList = InfModelQueryUtil.getDomainURIs(infModel, property);
					List<String> rangeList = InfModelQueryUtil.getRangeURIs(infModel, property);
										
					//List auxiliary for take the sub-properties with domain and range
					List<String> subproperties = InfModelQueryUtil.getSubPropertiesURI(infModel, property, true, true);
					for (String subproperty : subproperties) 
					{				
						boolean existInDomainsSource = false;
						boolean existInRangeTarget = false;
						List<String> domainSubPropertyList = InfModelQueryUtil.getDomainURIs(infModel, subproperty);
						for(String domain: domainSubPropertyList){
							if(sourceInstanceClasses.contains(domain) && ! (domainList.contains(domain)))
							{
								existInDomainsSource = true;	
							}
						}
						List<String> rangeSubPropertyList = InfModelQueryUtil.getRangeURIs(infModel, subproperty);
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
