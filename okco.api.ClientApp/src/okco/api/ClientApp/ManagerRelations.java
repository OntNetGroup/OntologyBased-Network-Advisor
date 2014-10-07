package okco.api.ClientApp;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.InfModelQueryUtil;
import br.com.padtec.okco.domain.DomainRange;
import br.com.padtec.okco.domain.DtoInstanceRelation;
import br.com.padtec.okco.domain.Instance;
import br.com.padtec.okco.domain.ManagerInstances;
import br.com.padtec.okco.domain.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.domain.RelationDomainRangeList;
import br.com.padtec.okco.domain.Search;

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
			ListAllInstances = manager.getAllInstances(model, infModel, NS);			
			for (Instance instance : ListAllInstances) 
			{				
				List<String> sourceInstanceClasses = InfModelQueryUtil.getClassesURI(infModel,instance.ns + instance.name);
				
				//Relations from instance
				ArrayList<DtoInstanceRelation> dtoInstanceRelations = search.GetInstanceRelations(infModel, instance.ns + instance.name);
				for (DtoInstanceRelation instanceRelation : dtoInstanceRelations) 
				{					
					String property = instanceRelation.Property;
					String targetInstance = instanceRelation.Target;
					List<String> targetInstanceClasses = InfModelQueryUtil.getClassesURI(infModel,targetInstance);
					
					//Get domain-range from property
					ArrayList<DomainRange> propDomainRangeList = search.GetDomainRangeFromProperty(property, infModel);
					
					//List auxiliary for take the sub-properties with domain and range
					ArrayList<RelationDomainRangeList> listPropDomainRange = search.GetSubPropertiesWithDomaninAndRange(property, infModel);
					for (RelationDomainRangeList relationDomainRange : listPropDomainRange) 
					{				
						boolean existInDomainsSource = false;
						boolean existInRangeTarget = false;
						
						for (DomainRange domainRange : relationDomainRange.listDomainRange) {
							
							if(sourceInstanceClasses.contains(domainRange.Domain) && ! (DomainRange.haveDomainList(domainRange.Domain, propDomainRangeList)) )
							{
								existInDomainsSource = true;	
							}
							if(targetInstanceClasses.contains(domainRange.Range) && ! (DomainRange.haveRangeinList(domainRange.Range, propDomainRangeList)) )
							{
								existInRangeTarget = true;
							}						
						}
						
						if(existInDomainsSource == true && existInRangeTarget == true)
						{
							//Check if the sub property domain and range are not the same as super property
							
							
							//relation have domain and range available
							model = manager.CreateRelationProperty(instance.ns + instance.name, relationDomainRange.Relation, targetInstance, model);
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
