package Business;

import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntModel;

import Domain.DtoDefinitionClass;
import Domain.Instance;
import Domain.EnumPropertyType;

public class ManagerInstances {
	
	private Search search;
	private OntModel model;

	public ManagerInstances(Search search, OntModel model)
	{
		this.search = search;
		this.model = model;
	}
	
	public void AddInstanceAndRelations(ArrayList<Instance> listInstances, ArrayList<DtoDefinitionClass> dtoRelationsList, EnumPropertyType type)
	{
		// Some relations
		for (DtoDefinitionClass dto : dtoRelationsList) 
		{			
			ArrayList<String> listInstancesOfDomain = this.search.GetInstancesFromClass(model, dto.Source);
			if(listInstancesOfDomain.size() > 0)	//Check if are need to create
			{
				for (String instanceName : listInstancesOfDomain) 
				{					
					//---SOME---//
					
					if(type.equals(EnumPropertyType.SOME))
					{
						boolean existInstanceTarget = this.search.CheckExistInstanceTarget(model, instanceName, dto.Relation, dto.Target);
						if(existInstanceTarget)
						{
							//Do nothing
							
						} else {
							
							//Check if individual already exist in list
							Instance instance = this.getInstance(listInstances, instanceName);
							if(instance == null)
							{
								instance = new Instance(instanceName, search.GetDifferentInstancesFrom(model, instanceName), search.GetSameInstancesFrom(model, instanceName), true);
								boolean existDto = DtoDefinitionClass.existDto(dto, instance.ListSome);
								if(!existDto)
								{
									instance.ListSome.add(dto);
								}
								listInstances.add(instance);
				
							} else {
								boolean existDto = DtoDefinitionClass.existDto(dto, instance.ListSome);
								if(!existDto)
								{
									instance.ListSome.add(dto);
								}
								
							}
						}
					}
					
					//---MIN---//
					
					if(type.equals(EnumPropertyType.MIN))
					{
						int quantityInstancesTarget = this.search.CheckExistInstancesTargetCardinality(model, instanceName, dto.Relation, dto.Target, dto.Cardinality);
						if (quantityInstancesTarget < Integer.parseInt(dto.Cardinality))	//Min restriction
						{
							Instance instance = this.getInstance(listInstances, instanceName);
							if(instance == null)
							{
								instance = new Instance(instanceName, search.GetDifferentInstancesFrom(model, instanceName), search.GetSameInstancesFrom(model, instanceName),true);
								boolean existDto = DtoDefinitionClass.existDto(dto, instance.ListMin);
								if(!existDto)
								{
									instance.ListMin.add(dto);
								}
								listInstances.add(instance);
				
							} else {
								
								boolean existDto = DtoDefinitionClass.existDto(dto, instance.ListMin);
								if(!existDto)
								{
									instance.ListMin.add(dto);
								}	
							}
						}
					}
					
					//---MAX---//
					//---EXACLTY---//
					//---COMPLETE---//
				}
			}			
		}	
	}

	public Instance getInstance(ArrayList<Instance> listInstances, String instanceName) {		
		
		for (Instance instance : listInstances) {
			if(instance.URI.equals(instanceName))
			{
				return instance;
			}
		}
		
		return null;
	}

	
	public ArrayList<Instance> getIntersectionOf(ArrayList<Instance> listAllInstances, ArrayList<String> listInstancesName) {

		ArrayList<Instance> list = new ArrayList<Instance>();
		
		for (String iName : listInstancesName) 
		{			
			for (Instance instance : listAllInstances) 
			{				
				if(iName.equals(instance.URI))
				{
					list.add(instance);
					break;
				}				
			}
		}		
		return list;
	}

}
