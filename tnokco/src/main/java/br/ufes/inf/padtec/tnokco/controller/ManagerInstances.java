package br.ufes.inf.padtec.tnokco.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.common.dto.DtoCompleteClass;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.OntModelAPI;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.OntPropertyEnum;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class ManagerInstances {
		
	private FactoryUtil factory;

	public ManagerInstances(FactoryUtil factory)
	{		
		this.factory = factory;
	}

	public OntModel CreateInstance(String instanceSource, String Relation, DtoInstance instanceNew, String TargetClass, List<DtoInstance> ListAllInstances, OntModel model)
	{
		return FactoryUtil.createIndividual(model,instanceNew.ns+instanceNew.name,instanceNew.ListSameInstances,instanceNew.ListDiferentInstances,instanceSource, Relation, TargetClass);
	}
	
	public OntModel DeleteInstance(DtoInstance instance, OntModel model)
	{
		return FactoryUtil.deleteIndividual(model, instance.ns+instance.name);
	}
	
	public OntModel CreateInstanceList(String instanceSource, ArrayList<DtoInstance> list, String Relation, String TargetClass, ArrayList<DtoInstance> ListAllInstances, OntModel model)
	{
		
		for (DtoInstance instance : list) {
			if(instance.existInModel == false)
			{
				model = FactoryUtil.createIndividual(model,instance.ns+instance.name,instance.ListSameInstances,instance.ListDiferentInstances,instanceSource, Relation, TargetClass);
				//Aplication.ListAllInstances.add(instance);
			}
		}
		
		return model;
		
		
	}
	
	public void UpdateInstanceAndRelations(List<DtoInstance> listInstances, List<DtoDefinitionClass> dtoRelationsList, OntModel model, InfModel infModel, String ns)
	{		
		System.out.println("\nManager Instances: updating instance and relations()...");
		for (DtoDefinitionClass dto : dtoRelationsList)
		{			
			List<String> listInstancesOfDomain =QueryUtil.getIndividualsURI(infModel, dto.Source);
			if(listInstancesOfDomain.size() > 0)	//Check if are need to create
			{
				for (String instanceName : listInstancesOfDomain)
				{					
					//---SOME---//
					
					if(dto.TypeCompletness.equals(OntCardinalityEnum.SOME))
					{
						boolean existInstanceTarget = QueryUtil.existsIndividualsAtPropertyRange(infModel, instanceName, dto.Relation, dto.Target);
						if(existInstanceTarget)
						{
							//Do nothing
							
						} else {
							
							//Check if individual already exist in list
							DtoInstance instance = this.getInstance(listInstances, instanceName);
							if(instance == null)
							{
								ArrayList<String> listClasses = new ArrayList<String>();
								listClasses.add(dto.Source);
								instance = new DtoInstance(ns, instanceName.replace(ns, ""), listClasses, QueryUtil.getIndividualsURIDifferentFrom(infModel, instanceName), QueryUtil.getIndividualsURISameAs(infModel, instanceName), true);
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
					
					if(dto.TypeCompletness.equals(OntCardinalityEnum.MIN))
					{
						int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(infModel, instanceName, dto.Relation, dto.Target);
						if (quantityInstancesTarget < Integer.parseInt(dto.Cardinality))	//Min restriction
						{
							DtoInstance instance = this.getInstance(listInstances, instanceName);
							if(instance == null)
							{
								ArrayList<String> listClasses = new ArrayList<String>();
								listClasses.add(dto.Source);
								instance = new DtoInstance(ns, instanceName.replace(ns, ""), listClasses, QueryUtil.getIndividualsURIDifferentFrom(infModel, instanceName), QueryUtil.getIndividualsURISameAs(infModel, instanceName),true);
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
					
					if(dto.TypeCompletness.equals(OntCardinalityEnum.MAX))
					{
						int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(infModel, instanceName, dto.Relation, dto.Target);
						if (quantityInstancesTarget > Integer.parseInt(dto.Cardinality))	//Max restriction
						{
							DtoInstance instance = this.getInstance(listInstances, instanceName);
							if(instance == null)
							{
								ArrayList<String> listClasses = new ArrayList<String>();
								listClasses.add(dto.Source);
								instance = new DtoInstance(ns, instanceName.replace(ns, ""), listClasses, QueryUtil.getIndividualsURIDifferentFrom(infModel, instanceName), QueryUtil.getIndividualsURISameAs(infModel, instanceName),true);
								boolean existDto = DtoDefinitionClass.existDto(dto, instance.ListMax);
								if(!existDto)
								{
									instance.ListMax.add(dto);
								}
								listInstances.add(instance);
				
							} else {
								
								boolean existDto = DtoDefinitionClass.existDto(dto, instance.ListMax);
								if(!existDto)
								{
									instance.ListMax.add(dto);
								}	
							}
						}
					}
					
					//---EXACLTY---//
					
					if(dto.TypeCompletness.equals(OntCardinalityEnum.EXACTLY))
					{
						int quantityInstancesTarget =QueryUtil.countIndividualsURIAtPropertyRange(infModel, instanceName, dto.Relation, dto.Target);
						if (quantityInstancesTarget != Integer.parseInt(dto.Cardinality))	//Exactly restriction
						{
							DtoInstance instance = this.getInstance(listInstances, instanceName);
							if(instance == null)
							{
								ArrayList<String> listClasses = new ArrayList<String>();
								listClasses.add(dto.Source);
								instance = new DtoInstance(ns, instanceName.replace(ns, ""), listClasses, QueryUtil.getIndividualsURIDifferentFrom(infModel, instanceName), QueryUtil.getIndividualsURISameAs(infModel, instanceName),true);
								boolean existDto = DtoDefinitionClass.existDto(dto, instance.ListExactly);
								if(!existDto)
								{
									instance.ListExactly.add(dto);
								}
								listInstances.add(instance);
				
							} else {
								
								boolean existDto = DtoDefinitionClass.existDto(dto, instance.ListExactly);
								if(!existDto)
								{
									instance.ListExactly.add(dto);
								}	
							}
						}
					}
					
					//---COMPLETE---//
				}
			}			
		}	
	}

	public void UpdateInstanceSpecialization(List<DtoInstance> listAllInstances, OntModel model,	InfModel infModel, String ns) {
		
		System.out.println("\nManager Instances: updating instance specialization()...");
		//update and check specialization class for all instances one by one		
		
		for (DtoInstance instanceSelected : listAllInstances) 
		{			
			// ------ Complete classes list ------//
			
			ArrayList<DtoCompleteClass> ListCompleteClsInstaceSelected = new ArrayList<DtoCompleteClass>();
			DtoCompleteClass dto = null;
			
			if(instanceSelected.ListClasses.size() == 1 && instanceSelected.ListClasses.get(0).contains("Thing"))	//Case thing
			{
				//Case if the instance have no class selected - only Thing
				dto = new DtoCompleteClass();
				dto.CompleteClass = instanceSelected.ListClasses.get(0);
				for (String subClas : OntModelAPI.getClassesURI(model)) {
					if(subClas != null)
						dto.AddMember(subClas);
				}
				ListCompleteClsInstaceSelected.add(dto);
				
			} else {
				
				for (String cls : instanceSelected.ListClasses)
				{					
					HashMap<String,List<String>> map = QueryUtil.getCompleteClassesURI(cls, instanceSelected.ListClasses, infModel);
					for(String completeClassURI: map.keySet()){
						DtoCompleteClass dtoCompleteClass = new DtoCompleteClass();
						dtoCompleteClass.setCompleteClass(completeClassURI);
						dtoCompleteClass.addAllMember(map.get(completeClassURI));
						ListCompleteClsInstaceSelected.add(dtoCompleteClass);
					}											
				}
			}
			
			instanceSelected.ListCompleteClasses = ListCompleteClsInstaceSelected;			
			
			// ------ Complete properties list ------//
			
			ArrayList<DtoPropertyAndSubProperties> ListSpecializationProperties = new ArrayList<DtoPropertyAndSubProperties>();
			DtoPropertyAndSubProperties dtoP = null;
			
			//Get instance relations
			List<DtoInstanceRelation> instanceListRelations = new ArrayList<DtoInstanceRelation>();
			List<String> propertiesURIList = QueryUtil.getPropertiesURI(infModel, instanceSelected.ns + instanceSelected.name);
			for(String propertyURI: propertiesURIList){
				DtoInstanceRelation dtoItem = new DtoInstanceRelation();
			    dtoItem.Property = propertyURI;			      
			    List<String> ranges = QueryUtil.getRangeURIs(infModel, propertyURI);;
			    if (ranges!=null && ranges.size()>0) dtoItem.Target = ranges.get(0);
			    else dtoItem.Target = "";
			    instanceListRelations.add(dtoItem);
			}
			
			for (DtoInstanceRelation dtoInstanceRelation : instanceListRelations) 
			{			
				List<String> subPropertiesWithDomainAndRange = QueryUtil.getSubPropertiesURIExcluding(infModel,instanceSelected.ns + instanceSelected.name, dtoInstanceRelation.Property, dtoInstanceRelation.Target, propertiesURIList);

				if(subPropertiesWithDomainAndRange.size() > 0)
				{
					dtoP = new DtoPropertyAndSubProperties();
					dtoP.Property = dtoInstanceRelation.Property;
					dtoP.iTargetNs = dtoInstanceRelation.Target.split("#")[0] + "#";
					dtoP.iTargetName = dtoInstanceRelation.Target.split("#")[1];
					dtoP.propertyType = QueryUtil.getPropertyURIType(infModel, dtoInstanceRelation.Property);
					
					for (String sub : subPropertiesWithDomainAndRange) 
					{
						boolean ok = true;
						
						List<String> distointSubPropOfProp = QueryUtil.getPropertiesURIDisjointWith(infModel,sub);
						for (String disjointrop : distointSubPropOfProp) {
							
							for (DtoInstanceRelation dtoWithRelation : instanceListRelations) {
								if(dtoWithRelation.Property.equals(disjointrop)) // instance have this sub relation
								{
									ok = false;
									break;
								}
							}
						}
						
						for (DtoInstanceRelation dtoWithRelation : instanceListRelations) {
						
							if(dtoWithRelation.Property.equals(sub)) // instance have this sub relation
							{
								ok = false;
								break;
							}
						}						
						
						
						if(ok == true)
						{
							dtoP.SubProperties.add(sub);
						}
					}
					
					if(dtoP.SubProperties.size() > 0)
						ListSpecializationProperties.add(dtoP);
				}			
			}
			
			instanceSelected.ListSpecializationProperties = ListSpecializationProperties;						
		}
		
	}
	
	public DtoInstance getInstance(List<DtoInstance> listInstances, String instanceName) {		
		
		for (DtoInstance instance : listInstances) {
			System.out.println("Comparing: "+instance.ns + instance.name);
			System.out.println("With: "+instanceName);
			if((instance.ns + instance.name).equals(instanceName))
			{
				return instance;
			}
		}
		
		return null;
	}
		
	public List<DtoInstance> getIntersectionOf(List<DtoInstance> listAllInstances, List<String> listInstancesName) {

		List<DtoInstance> list = new ArrayList<DtoInstance>();
		
		for (String iName : listInstancesName) 
		{			
			for (DtoInstance instance : listAllInstances) 
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

	public ArrayList<DtoInstance> getAllInstances(InfModel infModel) throws OKCoExceptionInstanceFormat {
		System.out.println("\nManager Instances: Getting all isntances()...");
		//NS from model
		
		ArrayList<DtoInstance> listInstances = new ArrayList<DtoInstance>();
		List<String> listInstancesDto = QueryUtil.getIndividualsURIFromAllClasses(infModel);
    	for (String dto : listInstancesDto) {
    		
    		if(! dto.contains("#"))
    		{
    			//nonstandard NS
    			throw new OKCoExceptionInstanceFormat("Entity namespace problem. The " + dto +" have to followed by \"#\".");
    		}
    		String nameSpace = dto.split("#")[0] + "#";
    		String name = dto.split("#")[1];
    		listInstances.add(new DtoInstance(nameSpace, name, QueryUtil.getClassesURI(infModel, dto), QueryUtil.getIndividualsURIDifferentFrom(infModel, dto), QueryUtil.getIndividualsURISameAs(infModel, dto),true));
		}
		
		return listInstances;
	}

	public OntModel UpdateInstanceInModel(DtoInstance instance, OntModel model, InfModel infModel, ArrayList<DtoInstance> ListAllInstances) {
		return FactoryUtil.updateIndividual(model, instance.ns+instance.name,instance.ListSameInstances,instance.ListDiferentInstances);
	}
	
	public void UpdateInstanceSameAndDifferentFrom(InfModel infModel, DtoInstance instance)
	{
		instance.ListDiferentInstances = QueryUtil.getIndividualsURIDifferentFrom(infModel, instance.ns + instance.name);
		instance.ListSameInstances = QueryUtil.getIndividualsURISameAs(infModel, instance.ns + instance.name);
	}
	
	public OntModel CreateTargetDataProperty(String instanceURI, String relation, String value, String TargetClass, OntModel model) {
		
		return FactoryUtil.createRangeDataPropertyValue(model, value, instanceURI, relation, TargetClass);
	}
	
	public OntModel DeleteTargetDataProperty(String instanceURI, String relation, String value, String TargetClass, OntModel model) {
		
		return FactoryUtil.deleteRangeDataPropertyValue(model, value, instanceURI, relation, TargetClass);
	}
	
	public OntModel CreateRelationProperty(String instanceSourceURI, String relation, String instanceTargetURI, OntModel model)
	{
		return FactoryUtil.createObjectProperty(model, instanceSourceURI, relation, instanceTargetURI);
	}
	
	public OntModel DeleteRelationProperty(String instanceSourceURI, String relation, String instanceTargetURI, OntModel model)
	{
		return FactoryUtil.deleteObjectProperty(model, instanceSourceURI, relation, instanceTargetURI);
	}

	public OntModel AddInstanceToClass(String instanceUri, String cls, OntModel model) {

		return FactoryUtil.createIndividualOfClass(model,instanceUri, cls);
	}
	
	public OntModel RemoveInstanceOnClass(String instanceUri, String cls, OntModel model) {

		return FactoryUtil.deleteIndividualOfClass(model, instanceUri, cls);
	}
	
	public OntModel CreateInstanceAuto(String instanceSource, DtoDefinitionClass dtoSelected, DtoInstance newInstance, OntModel model, InfModel infModel, List<DtoInstance> ListAllInstances) {
		
		return this.CreateInstance(instanceSource, dtoSelected.Relation, newInstance, dtoSelected.Target, ListAllInstances, model);
		
	}
	
	public OntModel ClassifyInstanceAuto(DtoInstance instance, OntModel model, InfModel infModel) {
		
		/* Check the subclasses are disjoint and complete */
		for (DtoCompleteClass dto : instance.ListCompleteClasses) 
		{
			boolean isDisjoint = true;
			for (String subCls : dto.Members)
			{
				for (String subCls2 : dto.Members) 
				{
					if(! subCls.equals(subCls2))
					{
						boolean result = QueryUtil.isClassesURIDisjoint(infModel, subCls, subCls2); /* Return true if subCls is disjoint of subCls2 */
						if(result == true)
						{
							//Not disjoint
							isDisjoint = false;
							
						} else {
							
							//isDisjoint = true;
						}
					}
				}
				
				if(isDisjoint == false)
				{
					break;
				}
			}
			
			if(isDisjoint == true && dto.Members.size() > 0 )
			{
				//Classify random
				model = this.AddInstanceToClass(instance.ns + instance.name, dto.Members.get(0), model);
			}
		}
		
		
		return model;
		
	}

	public OntModel CompleteInstanceAuto(DtoInstance instance, String modelNameSpace, OntModel model, InfModel infModel, List<DtoInstance> ListAllInstances)
	{
		//Classify instance classes
		model = this.ClassifyInstanceAuto(instance, model, infModel);
		
		//complete relations
		for (DtoDefinitionClass dto : instance.ListSome) 
		{
			if(dto.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY))
			{
				//create the the new instance
				String instanceName = dto.Target.split("#")[1] + "-" + (QueryUtil.getIndividualsURI(infModel, dto.Target).size() + 1);
				ArrayList<String> listSame = new ArrayList<String>();		  
				ArrayList<String> listDif = new ArrayList<String>();
				ArrayList<String> listClasses = new ArrayList<String>();
				DtoInstance newInstance = new DtoInstance(modelNameSpace, instanceName, listClasses, listDif, listSame, false);
				
				model = this.CreateInstanceAuto(instance.ns + instance.name, dto, newInstance, model, infModel, ListAllInstances);
			}
		}
		for (DtoDefinitionClass dto : instance.ListMin) 
		{
			if(dto.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY))
			{
				int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(infModel, instance.ns + instance.name, dto.Relation, dto.Target);
				
				ArrayList<String> listDif = new ArrayList<String>();
				while(quantityInstancesTarget < Integer.parseInt(dto.Cardinality))
				{
					//create the the new instance
					String instanceName = dto.Target.split("#")[1] + "-" + (quantityInstancesTarget + 1);
					ArrayList<String> listSame = new ArrayList<String>();		  
					ArrayList<String> listClasses = new ArrayList<String>();
					DtoInstance newInstance = new DtoInstance(modelNameSpace, instanceName, listClasses, listDif, listSame, false);
					
					model = this.CreateInstanceAuto(instance.ns + instance.name, dto, newInstance, model, infModel, ListAllInstances);				
					listDif.add(newInstance.ns + newInstance.name);
					quantityInstancesTarget ++;
				}
			}
					
		}
		for (DtoDefinitionClass dto : instance.ListExactly) 
		{
			if(dto.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY))
			{
				int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(infModel, instance.ns + instance.name, dto.Relation, dto.Target);
				
				// Case 1 - same as min
				if(quantityInstancesTarget < Integer.parseInt(dto.Cardinality))
				{
					ArrayList<String> listDif = new ArrayList<String>();
					while(quantityInstancesTarget < Integer.parseInt(dto.Cardinality))
					{
						//create the the new instance
						String instanceName = dto.Target.split("#")[1] + "-" + (quantityInstancesTarget + 1);
						ArrayList<String> listSame = new ArrayList<String>();		  
						ArrayList<String> listClasses = new ArrayList<String>();
						DtoInstance newInstance = new DtoInstance(modelNameSpace, instanceName, listClasses, listDif, listSame, false);
						
						model = this.CreateInstanceAuto(instance.ns + instance.name, dto, newInstance, model, infModel, ListAllInstances);				
						listDif.add(newInstance.ns + newInstance.name);
						quantityInstancesTarget ++;
					}
				}
				
				// Case 2 - more individuals than necessary
				if(quantityInstancesTarget > Integer.parseInt(dto.Cardinality))
				{
											
				}
			}
		}
		
		
		return model;
	}

	public ArrayList<DtoDefinitionClass> removeRepeatValuesOn(DtoInstance instanceSelected, OntCardinalityEnum type) {

		ArrayList<DtoDefinitionClass> listDefinition = new ArrayList<DtoDefinitionClass>();
		
		if(type.equals(OntCardinalityEnum.SOME))
		{
			for (DtoDefinitionClass dto : instanceSelected.ListSome) 
			{				
				boolean exist = false;
				for (DtoDefinitionClass dto2 : listDefinition) {
					if(dto.sameAs(dto2))
					{
						exist = true;
						break;
					}
				}
				
				if(exist == false)
				{
					listDefinition.add(dto);
					//dto.print();
				}			
			}
			
		} else if(type.equals(OntCardinalityEnum.MIN))
		{
			for (DtoDefinitionClass dto : instanceSelected.ListMin) 
			{			
				boolean exist = false;
				for (DtoDefinitionClass dto2 : listDefinition) {

					//Doesn't compare the source
					if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
					{
						exist = true;
						break;
					}
				}
				
				if(exist == false)
				{
					listDefinition.add(dto);
					//dto.print();
				}			
			}
			
		} else  if(type.equals(OntCardinalityEnum.MAX))
		{
			for (DtoDefinitionClass dto : instanceSelected.ListMax) 
			{			
				boolean exist = false;
				for (DtoDefinitionClass dto2 : listDefinition) {

					//Doesn't compare the source
					if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
					{
						exist = true;
						break;
					}
				}
				
				if(exist == false)
				{
					listDefinition.add(dto);
					//dto.print();
				}			
			}
			
		} else if(type.equals(OntCardinalityEnum.EXACTLY))
		{
			for (DtoDefinitionClass dto : instanceSelected.ListExactly) 
			{			
				boolean exist = false;
				for (DtoDefinitionClass dto2 : listDefinition) {

					//Doesn't compare the source
					if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
					{
						exist = true;
						break;
					}
				}
				
				if(exist == false)
				{
					listDefinition.add(dto);
					//dto.print();
				}			
			}
		}
		
		
		return listDefinition;
	}

	public ArrayList<String> getClassesToClassify(DtoInstance instanceSelected, InfModel infModel) {

		//Get all the subclasses without repeat
		ArrayList<String> listClassesMembersTmpWithoutRepeat = new ArrayList<String>();
		for (DtoCompleteClass dto : instanceSelected.ListCompleteClasses) {
			for (String clsComplete : dto.Members) {
				if(! listClassesMembersTmpWithoutRepeat.contains(clsComplete))
				{
					listClassesMembersTmpWithoutRepeat.add(clsComplete);					
				} 
			}
		}
		
		//Remove disjoint subclasses from some super class
		ArrayList<String> listClassesMembersTmp = new ArrayList<String>();
		for (DtoCompleteClass dto : instanceSelected.ListCompleteClasses) 
		{
			List<String> listDisjoint = QueryUtil.getDisjointClassesURIs(infModel, dto.CompleteClass);
			//List<String> listDisjoint = QueryUtil.getClassesURIDisjointWith(infModel,dto.CompleteClass);
			for (String clc : listClassesMembersTmpWithoutRepeat) 
			{
				if(! listDisjoint.contains(clc))
				{
					listClassesMembersTmp.add(clc);
				}
			}			
		}
		return listClassesMembersTmp;
	}

	public OntModel setSameInstances(String i1URI, String i2URI, OntModel model)
	{
		Individual i1 = model.getIndividual(i1URI);
		Individual i2 = model.getIndividual(i2URI);
		
		i1.setSameAs(i2);
		i2.setSameAs(i1);
		
		return model;
	}
	
	public OntModel setDifferentInstances(String i1URI, String i2URI, OntModel model)
	{
		Individual i1 = model.getIndividual(i1URI);
		Individual i2 = model.getIndividual(i2URI);
		
		i1.setDifferentFrom(i2);
		i2.setDifferentFrom(i1);
		
		return model;
	}
}
