package br.com.padtec.common.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoCompleteClass;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.common.exceptions.OKCoNameSpaceException;
import br.com.padtec.common.factory.DtoFactoryUtil;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.graph.GraphPlotting;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.OntModelAPI;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.OntPropertyEnum;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;


public class OKCoApp {
	
	//Keeps the list of individuals that were modified
	public static List<String> modifiedIndividualsURIs = new ArrayList<String>();
	
	//Keeps the individuals that was selected  with other useful informations
	public static DtoInstance individualSelected;
	public static List<DtoCompleteClass> completeClassesFromSelected;
	public static List<DtoPropertyAndSubProperties> propertiesFromSelected;
	
	//Keeps a specific cardinality definition that was selected
	public static DtoDefinitionClass definitionClassSelected;
	
	/** 
	 * Return the list of all individuals from the ontology.
	 * It returns also all the classes of an individual as well as all the other individuals different and the same as this one.
	 *  
	 * @throws OKCoNameSpaceException
	 * 
	 * @author John Guerson
	 * 
	 * @param model
	 * @param clsEager Defines when the classes of an individual must be got eagerly 
	 * @param diffFromEager Defines when the "different from individuals" of an individual must be got eagerly
	 * @param sameAsEager Defines when the "same as individuals" of an individual must be got eagerly
	 */
	public static List<DtoInstance> getIndividuals(Boolean classesEager, Boolean diffFromEager, Boolean sameAsEager)
	{
		return DtoQueryUtil.getIndividuals(UploadApp.getInferredModel(), classesEager, diffFromEager, sameAsEager);
	}
	
	public static List<String> getModifiedIndividuals()
	{
		return modifiedIndividualsURIs;
	}
	
	public static String getSelectedIndividualURI()
	{
		return individualSelected.ns+individualSelected.name;
	}
	
	public static DtoInstance selectIndividual(String individualURI)
	{
		individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI);
		return individualSelected;
	}
	
	public static List<DtoDefinitionClass> getSomeDefinitionsFromSelected()
	{
		if(individualSelected!=null) return individualSelected.getSomeDefinitionWithNoRepetition();
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	public static List<DtoDefinitionClass> getMaxDefinitionsFromSelected()
	{
		if(individualSelected!=null) return individualSelected.getMaxDefinitionWithNoRepetition();
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	public static List<DtoDefinitionClass> getMinDefinitionsFromSelected()
	{
		if(individualSelected!=null) return individualSelected.getMinDefinitionWithNoRepetition();
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	public static List<DtoDefinitionClass> getExactDefinitionsFromSelected()
	{
		if(individualSelected!=null) return individualSelected.getExactDefinitionWithNoRepetition();
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	public static List<DtoCompleteClass> getCompleteClassesFromSelected()
	{
		if(individualSelected!=null) {
			completeClassesFromSelected = individualSelected.getCompleteClasses();
			return completeClassesFromSelected;
		}
		else return new ArrayList<DtoCompleteClass>();
	}
	
	public static List<DtoPropertyAndSubProperties> getPropertiesFromSelected()
	{
		if(individualSelected!=null) {
			propertiesFromSelected = individualSelected.getPropertiesAndSubProperties();
			return propertiesFromSelected;
		}
		else return new ArrayList<DtoPropertyAndSubProperties>();
	}
	
	public static List<DtoInstanceRelation> getRelationsFromSelected()
	{
		if(individualSelected!=null) {
			return DtoQueryUtil.getRelations(UploadApp.getInferredModel(),getSelectedIndividualURI());			
		}
		else return new ArrayList<DtoInstanceRelation>();
	}
	
	public static DtoDefinitionClass selectDefinitionFromSelected(String uriProperty)
	{
		OKCoApp.definitionClassSelected = DtoDefinitionClass.get(individualSelected.ListSome, uriProperty);
		if(OKCoApp.definitionClassSelected == null) OKCoApp.definitionClassSelected = DtoDefinitionClass.get(individualSelected.ListMin, uriProperty);
		if(OKCoApp.definitionClassSelected == null) OKCoApp.definitionClassSelected = DtoDefinitionClass.get(individualSelected.ListMax, uriProperty);
		if(OKCoApp.definitionClassSelected == null)	OKCoApp.definitionClassSelected = DtoDefinitionClass.get(individualSelected.ListExactly, uriProperty);
		return definitionClassSelected;
	}
	
	public static List<DtoInstance> getIndividualsAtClassDefinitionRangeSelected()
	{
		String uriIndividualSelected = individualSelected.ns + individualSelected.name;
		InfModel model = UploadApp.getInferredModel();
		List<DtoInstance> dtoIndividuals = DtoQueryUtil.getIndividualsAtObjectPropertyRange(model,uriIndividualSelected, definitionClassSelected.Relation, definitionClassSelected.Target);
		return dtoIndividuals;
	}
	
	public static List<DataPropertyValue> getDataValuesAtClassDefinitionRangeSelected()
	{
		List<DataPropertyValue> result = new ArrayList<DataPropertyValue>();
		String uriIndividualSelected = individualSelected.ns + individualSelected.name;
		InfModel model = UploadApp.getInferredModel();
		List<String> individualsList = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, uriIndividualSelected, definitionClassSelected.Relation, definitionClassSelected.Target);
		for(String individualURI: individualsList)
		{
			DataPropertyValue data = new DataPropertyValue();
			data.value = individualURI.split("\\^\\^")[0];
			data.classValue = definitionClassSelected.Target;
			data.existInModel = true;
			result.add(data);
		}
		return result;
	}
	
	public static void createNewIndividualAtClassDefinitionRangeSelected(Integer idNumber, List<String> differentFromList)
	{
		String individualName = definitionClassSelected.Target.split("#")[1] + "-" + (idNumber + 1);				
		DtoInstance newDtoIndividual = new DtoInstance(UploadApp.baseRepository.getNameSpace(), individualName, null, differentFromList, null, false);
		OntModel model = UploadApp.getBaseModel();
		FactoryUtil.createIndividual(model, 
			newDtoIndividual.ns+newDtoIndividual.name, 
			newDtoIndividual.ListSameInstances, 
			newDtoIndividual.ListDiferentInstances, 
			individualSelected.ns+individualSelected.name, 
			definitionClassSelected.Relation, 
			definitionClassSelected.Target
		);
		OKCoApp.modifiedIndividualsURIs.add(newDtoIndividual.ns + newDtoIndividual.name);
		if(differentFromList!=null) differentFromList.add(newDtoIndividual.ns + newDtoIndividual.name);	
	}
		
	public static void createNewIndividualAtClassDefinitionRange(DtoInstance dtoIndividual, DtoDefinitionClass dtoDefinitionClass, Integer idNumber, List<String> differentFromList)
	{
		String individualName = dtoDefinitionClass.Target.split("#")[1] + "-" + (idNumber + 1);				
		DtoInstance newDtoIndividual = new DtoInstance(UploadApp.baseRepository.getNameSpace(), individualName, null, differentFromList, null, false);
		OntModel model = UploadApp.getBaseModel();
		FactoryUtil.createIndividual(model, 
			newDtoIndividual.ns+newDtoIndividual.name, 
			newDtoIndividual.ListSameInstances, 
			newDtoIndividual.ListDiferentInstances, 
			dtoIndividual.ns+dtoIndividual.name, 
			dtoDefinitionClass.Relation, 
			dtoDefinitionClass.Target
		);
		OKCoApp.modifiedIndividualsURIs.add(newDtoIndividual.ns + newDtoIndividual.name);
		if(differentFromList!=null) differentFromList.add(newDtoIndividual.ns + newDtoIndividual.name);	
	}
	
	public static void setIsModified(String individualURI)
	{
		if(!modifiedIndividualsURIs.contains(individualURI)) modifiedIndividualsURIs.add(individualURI);
	}
	
	public static void createNewIndividualsAutomatically(DtoInstance dtoIndividual)
	{
		DtoFactoryUtil.createAndClassifyIndividualAutomatically(UploadApp.getBaseModel(), UploadApp.getInferredModel(), dtoIndividual);
		
		for (DtoDefinitionClass dtoDefinitionClass : dtoIndividual.ListSome) 
		{
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY))
			{
				int individualsNumber = QueryUtil.getIndividualsURI(UploadApp.getInferredModel(), dtoDefinitionClass.Target).size()+ 1;
				/** ==================================================
				 * Create a New Individual at the Range of the Class Definition
				 *  =================================================== */				
				OKCoApp.createNewIndividualAtClassDefinitionRange(dtoIndividual, dtoDefinitionClass, individualsNumber, null);				
			}
		}
		
		for (DtoDefinitionClass dtoDefinitionClass : dtoIndividual.ListMin) 
		{
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY))
			{
				int individualsNumber = QueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), dtoIndividual.ns + dtoIndividual.name, dtoDefinitionClass.Relation, dtoDefinitionClass.Target);
				ArrayList<String> listDifferentFrom = new ArrayList<String>();
				while(individualsNumber < Integer.parseInt(dtoDefinitionClass.Cardinality))
				{
					/** ==================================================
					 * Create a New Individual at the Range of the Class Definition
					 *  =================================================== */
					OKCoApp.createNewIndividualAtClassDefinitionRange(dtoIndividual, dtoDefinitionClass, individualsNumber, listDifferentFrom);					
									
					individualsNumber ++;
				}				
			}					
		}
		
		for (DtoDefinitionClass dtoDefinitionClass : dtoIndividual.ListExactly) 
		{
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY))
			{				
				int individualsNumber = QueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), dtoIndividual.ns + dtoIndividual.name, dtoDefinitionClass.Relation, dtoDefinitionClass.Target);					
				ArrayList<String> listDifferentFrom = new ArrayList<String>();
				if(individualsNumber < Integer.parseInt(dtoDefinitionClass.Cardinality))
				{
					while(individualsNumber < Integer.parseInt(dtoDefinitionClass.Cardinality))
					{
						/** ==================================================
						 * Create a New Individual at the Range of the Class Definition
						 *  =================================================== */
						OKCoApp.createNewIndividualAtClassDefinitionRange(dtoIndividual, dtoDefinitionClass, individualsNumber, listDifferentFrom);	
						
						individualsNumber ++;
					}
				}
			}
		}
	}
	
	public static String getGraphValues(String typeView, String individualURI, GraphPlotting graphPlotting)
	{		
		String valuesGraph = new String();				
		if(typeView.equals("ALL")) valuesGraph  = graphPlotting.getArborStructureFor(UploadApp.getInferredModel());
		else if(individualURI != null)
		{			
			DtoInstance dtoIndividual = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI);
			if(typeView.equals("IN")) 
			{				
				valuesGraph  = graphPlotting.getArborStructureComingInOf(UploadApp.getInferredModel(), dtoIndividual.ns + dtoIndividual.name);
			}
			else if(typeView.equals("OUT")) 
			{					
				valuesGraph  = graphPlotting.getArborStructureComingOutOf(UploadApp.getInferredModel(), dtoIndividual.ns + dtoIndividual.name);	
			}			
		}	
		return valuesGraph;
	}
		
	public static void getClassSpecializationsFromSelected() {
		InfModel model = UploadApp.getInferredModel();
		System.out.println("\nManager Instances: updating instance specialization()...");
		//update and check specialization class for all instances one by one		
		
		// ------ Complete classes list ------//
		
		ArrayList<DtoCompleteClass> ListCompleteClsInstaceSelected = new ArrayList<DtoCompleteClass>();
		DtoCompleteClass dto = null;
		
		if(individualSelected.ListClasses.size() == 1 && individualSelected.ListClasses.get(0).contains("Thing"))	//Case thing
		{
			//Case if the instance have no class selected - only Thing
			dto = new DtoCompleteClass();
			dto.CompleteClass = individualSelected.ListClasses.get(0);
			
			for (String subClas : QueryUtil.getClassesURI(model)) {
				if(subClas != null)
					dto.AddMember(subClas);
			}
			ListCompleteClsInstaceSelected.add(dto);
			
		} else {
			
			for (String cls : individualSelected.ListClasses)
			{					
				HashMap<String,List<String>> map = QueryUtil.getCompleteClassesURI(cls, individualSelected.ListClasses, model);
				for(String completeClassURI: map.keySet()){
					DtoCompleteClass dtoCompleteClass = new DtoCompleteClass();
					dtoCompleteClass.setCompleteClass(completeClassURI);
					dtoCompleteClass.addAllMember(map.get(completeClassURI));
					ListCompleteClsInstaceSelected.add(dtoCompleteClass);
				}											
			}
		}
		
		individualSelected.ListCompleteClasses = ListCompleteClsInstaceSelected;
		
	}
	
	public static void getRelationSpecializationsFromSelected() {
		InfModel model = UploadApp.getInferredModel();
		System.out.println("\nManager Instances: updating instance specialization()...");
		//update and check specialization class for all instances one by one		
		
		// ------ Complete properties list ------//
		
		ArrayList<DtoPropertyAndSubProperties> ListSpecializationProperties = new ArrayList<DtoPropertyAndSubProperties>();
		DtoPropertyAndSubProperties dtoP = null;
		
		//Get instance relations
		List<DtoInstanceRelation> instanceListRelations = new ArrayList<DtoInstanceRelation>();
		List<String> propertiesURIList = QueryUtil.getPropertiesURI(UploadApp.getInferredModel(), individualSelected.ns + individualSelected.name);
		for(String propertyURI: propertiesURIList){
			DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyURI;			      
		    List<String> ranges = QueryUtil.getRangeIndividualURI(model, individualSelected.ns + individualSelected.name, propertyURI);
		    //List<String> ranges = QueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI);
		    if (ranges!=null && ranges.size()>0) dtoItem.Target = ranges.get(0);
		    else dtoItem.Target = "";
		    instanceListRelations.add(dtoItem);
		}
		
		for (DtoInstanceRelation dtoInstanceRelation : instanceListRelations) 
		{			
			List<String> subPropertiesWithDomainAndRange = QueryUtil.getSubPropertiesURIExcluding(model,individualSelected.ns + individualSelected.name, dtoInstanceRelation.Property, dtoInstanceRelation.Target, propertiesURIList);

			if(subPropertiesWithDomainAndRange.size() > 0)
			{
				dtoP = new DtoPropertyAndSubProperties();
				dtoP.Property = dtoInstanceRelation.Property;
				String target = dtoInstanceRelation.Target;
				if(target != null && !target.equals("")){
					dtoP.iTargetNs = target.split("#")[0] + "#";
					dtoP.iTargetName = target.split("#")[1];
				}
				dtoP.propertyType = QueryUtil.getPropertyURIType(model, dtoInstanceRelation.Property);
				
				for (String sub : subPropertiesWithDomainAndRange) 
				{
					boolean ok = true;
					
					List<String> distointSubPropOfProp = QueryUtil.getPropertiesURIDisjointWith(model,sub);
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
		
		individualSelected.ListSpecializationProperties = ListSpecializationProperties;						
	
		
	}
	
	//============================================================
	//============================================================
	//============================================================
	
	public static List<DtoInstance> ListAllInstances;	
	public static List<DtoDefinitionClass> ModelDefinitions;		
	// Save the new instances before commit in views (completePropertyObject and completePropertyData)
	//Instances to add in relation
	public static ArrayList<DtoInstance> listNewInstancesRelation;
	//DataValues to add in relation
	public static ArrayList<DataPropertyValue> listNewDataValuesRelation;
	
	public static void clear() 
	{
		modifiedIndividualsURIs.clear();
		ListAllInstances=null;	
	}
		
	public static void updateModifiedList()
	{
		for (DtoInstance i : ListAllInstances) 
		{
			String s = i.ns + i.name;
			if (modifiedIndividualsURIs.contains(s)) i.setModified(true);			
		}
	}
	
	//Check the validity of this method
	public static void updateLists() throws InconsistentOntologyException, OKCoExceptionInstanceFormat 
	{	
		System.out.println("Updating Lists()...");
		InfModel inferredModel = UploadApp.getInferredModel();
		OntModel Model = UploadApp.getBaseModel();
    	// Refresh list of instances
    	ListAllInstances = DtoQueryUtil.getIndividuals(inferredModel, true, true, true);
    	//Get model definitions on list of instances	    	
	  	ModelDefinitions = DtoQueryUtil.getClassDefinitions(inferredModel);			
		// Organize data (Update the list of all instances)			
    	UpdateInstanceAndRelations(Model, UploadApp.getBaseRepository().getNameSpace(), ListAllInstances, ModelDefinitions);			
		UpdateInstanceSpecialization(ListAllInstances, Model, inferredModel, UploadApp.getBaseRepository().getNameSpace());			
    }
	
	//Check the validity of this method
	public static void updateAddingToLists(String instanceURI) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{							
		System.out.println("Updating and Adding to Lists()...");
		InfModel inferredModel = UploadApp.getInferredModel();
		OntModel Model = UploadApp.getBaseModel();
	    //Get model definitions on list of instances	    	
		List<DtoDefinitionClass> intanceDefinitions = DtoQueryUtil.getClassDefinitions(inferredModel, instanceURI);
		ModelDefinitions.addAll(intanceDefinitions);			
		// Organize data (Update the list of all instances)			
		UpdateInstanceAndRelations(Model, UploadApp.getBaseRepository().getNameSpace(), ListAllInstances, intanceDefinitions);			
		UpdateInstanceSpecialization(ListAllInstances, Model, inferredModel, UploadApp.getBaseRepository().getNameSpace());			
	}
	

	public static DtoInstance getInstance(List<DtoInstance> listInstances, String instanceName) {		
		
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
	
	public static void UpdateInstanceAndRelations(OntModel model, String ns, List<DtoInstance> listInstances, List<DtoDefinitionClass> dtoRelationsList)
	{		
		System.out.println("\nManager Instances: updating instance and relations()...");
		for (DtoDefinitionClass dto : dtoRelationsList)
		{			
			List<String> listInstancesOfDomain =QueryUtil.getIndividualsURI(model, dto.Source);
			if(listInstancesOfDomain.size() > 0)	//Check if are need to create
			{
				for (String instanceName : listInstancesOfDomain)
				{					
					//---SOME---//
					
					if(dto.TypeCompletness.equals(OntCardinalityEnum.SOME))
					{
						boolean existInstanceTarget = QueryUtil.existsIndividualsAtPropertyRange(model, instanceName, dto.Relation, dto.Target);
						if(existInstanceTarget)
						{
							//Do nothing
							
						} else {
							
							//Check if individual already exist in list
							DtoInstance instance = getInstance(listInstances, instanceName);
							if(instance == null)
							{
								ArrayList<String> listClasses = new ArrayList<String>();
								listClasses.add(dto.Source);
								instance = new DtoInstance(ns, instanceName.replace(ns, ""), listClasses, QueryUtil.getIndividualsURIDifferentFrom(model, instanceName), QueryUtil.getIndividualsURISameAs(model, instanceName), true);
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
						int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(model, instanceName, dto.Relation, dto.Target);
						if (quantityInstancesTarget < Integer.parseInt(dto.Cardinality))	//Min restriction
						{
							DtoInstance instance = getInstance(listInstances, instanceName);
							if(instance == null)
							{
								ArrayList<String> listClasses = new ArrayList<String>();
								listClasses.add(dto.Source);
								instance = new DtoInstance(ns, instanceName.replace(ns, ""), listClasses, QueryUtil.getIndividualsURIDifferentFrom(model, instanceName), QueryUtil.getIndividualsURISameAs(model, instanceName),true);
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
						int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(model, instanceName, dto.Relation, dto.Target);
						if (quantityInstancesTarget > Integer.parseInt(dto.Cardinality))	//Max restriction
						{
							DtoInstance instance = getInstance(listInstances, instanceName);
							if(instance == null)
							{
								ArrayList<String> listClasses = new ArrayList<String>();
								listClasses.add(dto.Source);
								instance = new DtoInstance(ns, instanceName.replace(ns, ""), listClasses, QueryUtil.getIndividualsURIDifferentFrom(model, instanceName), QueryUtil.getIndividualsURISameAs(model, instanceName),true);
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
						int quantityInstancesTarget =QueryUtil.countIndividualsURIAtPropertyRange(model, instanceName, dto.Relation, dto.Target);
						if (quantityInstancesTarget != Integer.parseInt(dto.Cardinality))	//Exactly restriction
						{
							DtoInstance instance = getInstance(listInstances, instanceName);
							if(instance == null)
							{
								ArrayList<String> listClasses = new ArrayList<String>();
								listClasses.add(dto.Source);
								instance = new DtoInstance(ns, instanceName.replace(ns, ""), listClasses, QueryUtil.getIndividualsURIDifferentFrom(model, instanceName), QueryUtil.getIndividualsURISameAs(model, instanceName),true);
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
	
	public static void UpdateInstanceSpecialization(List<DtoInstance> listAllInstances, OntModel model, InfModel infModel, String ns) {
		
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
			List<String> propertiesURIList = QueryUtil.getPropertiesURI(UploadApp.getInferredModel(), instanceSelected.ns + instanceSelected.name);
			for(String propertyURI: propertiesURIList){
				DtoInstanceRelation dtoItem = new DtoInstanceRelation();
			    dtoItem.Property = propertyURI;			      
			    List<String> ranges = QueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI);;
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
					String target = dtoInstanceRelation.Target;
					if(target != null && !target.equals("")){
						dtoP.iTargetNs = target.split("#")[0] + "#";
						dtoP.iTargetName = target.split("#")[1];
					}
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
}
