package br.com.padtec.common.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoCompleteClass;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoGetPrevNextSpecProperty;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.exceptions.OKCoNameSpaceException;
import br.com.padtec.common.factory.DtoFactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;

import com.hp.hpl.jena.rdf.model.InfModel;

public class OKCoApp {
	
	//Keeps the list of individuals that were modified
	public static List<String> modifiedIndividualsURIs = new ArrayList<String>();
	
	//Keeps the individual that was selected with other useful informations
	public static DtoInstance individualSelected;
	public static DtoDefinitionClass definitionClassSelected;
	public static List<DtoCompleteClass> completeClassesFromSelected;
	public static List<DtoPropertyAndSubProperties> relationSpecializationsFromSelected;	
		
	/**
	 * Select a particular individual to be used later on.
	 * 
	 * @param individualURI
	 * @return
	 */
	public static DtoInstance selectIndividual(String individualURI)
	{
		individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI, true, true, true);
		return individualSelected;
	}
	
	/**
	 * Select a individual to be used later on.
	 * 
	 * @param newSelectedIndividual
	 */
	public static void selectIndividual(DtoInstance newSelectedIndividual)
	{
		individualSelected = newSelectedIndividual;
	}
	
	/**
	 * Select a particular cardinality definition to be used later on.
	 * 
	 * @param uriProperty
	 * @return
	 */
	public static DtoDefinitionClass selectDefinitionFromSelected(String uriProperty)
	{
		/** SOME */
		List<DtoDefinitionClass> someList = getSomeDefinitionsFromSelected();		
		definitionClassSelected = DtoFactoryUtil.getDefinitionFrom(someList, uriProperty);
		
		/** MIN */		
		List<DtoDefinitionClass> minList = getMinDefinitionsFromSelected();
		if(definitionClassSelected == null) definitionClassSelected = DtoFactoryUtil.getDefinitionFrom(minList, uriProperty);
		
		/** MAX */
		List<DtoDefinitionClass> maxList = getMaxDefinitionsFromSelected();
		if(definitionClassSelected == null) definitionClassSelected = DtoFactoryUtil.getDefinitionFrom(maxList, uriProperty);
		
		/** EXACTLY */
		List<DtoDefinitionClass> exactList = getExactDefinitionsFromSelected();
		if(definitionClassSelected == null)	definitionClassSelected = DtoFactoryUtil.getDefinitionFrom(exactList, uriProperty);
		
		return definitionClassSelected;
	}
			
	public static void setSelectedToModified()
	{
		setIsModified(getSelectedIndividualURI());
	}
	
	public static void setIsModified(String individualURI)
	{
		if(!modifiedIndividualsURIs.contains(individualURI)) modifiedIndividualsURIs.add(individualURI);
	}
	
	public static void clearModified()
	{
		modifiedIndividualsURIs.clear();
	}
	
	public static void clearSelected() 
	{		
		definitionClassSelected=null;
		individualSelected=null;
		completeClassesFromSelected.clear();		
		relationSpecializationsFromSelected.clear();		
	}

	public static List<String> getModifiedIndividuals()
	{
		return modifiedIndividualsURIs;
	}
		
	/**
	 * The selected cardinality definition.
	 * 
	 * @return
	 */
	public static DtoDefinitionClass getSelectedClassDefinition()
	{
		return definitionClassSelected;
	}
		
	/**
	 * The selected individual
	 */
	public static DtoInstance getSelectedIndividual()
	{
		return individualSelected;
	}
	
	/**
	 * The selected individual URI if any. Otherwise it returns null.
	 * 
	 * @return
	 */
	public static String getSelectedIndividualURI()
	{
		if(individualSelected!=null) return individualSelected.ns+individualSelected.name;
		else return null;
	}
			
	/**
	 * List of all SOME cardinality restrictions from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoDefinitionClass> getSomeDefinitionsFromSelected()
	{
		if(individualSelected!=null) 
		{
			if(individualSelected.ListSome.size()==0) setDefinitionsInSelected(OntCardinalityEnum.SOME);
			return individualSelected.getSomeDefinitionWithNoRepetition();
		}		
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	/**
	 * List of all MAX cardinality restrictions from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoDefinitionClass> getMaxDefinitionsFromSelected()
	{
		if(individualSelected!=null) 
		{
			if(individualSelected.ListMax.size()==0) setDefinitionsInSelected(OntCardinalityEnum.MAX);
			return individualSelected.getMaxDefinitionWithNoRepetition();
		}
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	/**
	 * List of all MIN cardinality restrictions from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoDefinitionClass> getMinDefinitionsFromSelected()
	{
		if(individualSelected!=null) 
		{
			if(individualSelected.ListMin.size()==0) setDefinitionsInSelected(OntCardinalityEnum.MIN);
			return individualSelected.getMinDefinitionWithNoRepetition();
		}
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	/**
	 * List of all EXACT cardinality restrictions from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoDefinitionClass> getExactDefinitionsFromSelected()
	{
		if(individualSelected!=null) 
		{
			if(individualSelected.ListExactly.size()==0) setDefinitionsInSelected(OntCardinalityEnum.EXACTLY);
			return individualSelected.getExactDefinitionWithNoRepetition();
		}
		else return new ArrayList<DtoDefinitionClass>();
		
	}
	
	/**
	 * List of all relations of the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoInstanceRelation> getRelationsFromSelected()
	{
		if(individualSelected!=null) 
		{
			return DtoQueryUtil.getRelations(UploadApp.getInferredModel(),getSelectedIndividualURI());			
		}
		else return new ArrayList<DtoInstanceRelation>();
	}
	
	/**
	 * List of all the relation specializations (relations and sub-relations) from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoPropertyAndSubProperties> getRelationSpecializationsFromSelected()
	{
		if(individualSelected!=null) 
		{
			if(individualSelected.ListSpecializationProperties.size()==0) setRelationSpecializationsInSelected();
			relationSpecializationsFromSelected = individualSelected.getSpecializationProperties();
			return relationSpecializationsFromSelected;
		}
		else return new ArrayList<DtoPropertyAndSubProperties>();
	}
	
	/**
	 * List of all classes that are complete from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoCompleteClass> getCompleteClassesFromSelected()
	{
		if(individualSelected!=null) 
		{
			if(individualSelected.ListCompleteClasses.size()==0) setClassSpecializationsInSelected();
			completeClassesFromSelected = individualSelected.getCompleteClasses();
			return completeClassesFromSelected;
		}
		else return new ArrayList<DtoCompleteClass>();
	}
		
	/**
	 * List of individuals at the range of the cardinality restriction selected.
	 * 
	 * @return
	 */
	public static List<DtoInstance> getIndividualsAtClassDefinitionRangeSelected()
	{
		InfModel model = UploadApp.getInferredModel();
		List<DtoInstance> dtoIndividuals = DtoQueryUtil.getIndividualsAtObjectPropertyRange(model, getSelectedIndividualURI(), definitionClassSelected.Relation, definitionClassSelected.Target);
		return dtoIndividuals;
	}
	
	/**
	 * List of data values at the range of the cardinality restriction selected.
	 * 
	 * @return
	 */
	public static List<DataPropertyValue> getDataValuesAtClassDefinitionRangeSelected()
	{
		List<DataPropertyValue> result = new ArrayList<DataPropertyValue>();		
		InfModel model = UploadApp.getInferredModel();
		List<String> individualsList = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, getSelectedIndividualURI(), definitionClassSelected.Relation, definitionClassSelected.Target);
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

	/**
	 * Get the Property from Selected alongside the next and previous properties.
	 */
	public static DtoGetPrevNextSpecProperty getPropertyWithNextAndPreviousFromSelected(String propertyURI)
	{
		DtoPropertyAndSubProperties dto = DtoFactoryUtil.getPropertyFrom(relationSpecializationsFromSelected, propertyURI);
		if(dto == null) return null;			
		boolean haveNext = false;
		boolean havePrev = false;
		DtoPropertyAndSubProperties dtoNext =  DtoFactoryUtil.getPropertyFrom(relationSpecializationsFromSelected, relationSpecializationsFromSelected.get(relationSpecializationsFromSelected.indexOf(dto)+1).Property);
		DtoPropertyAndSubProperties dtoPrev=  DtoFactoryUtil.getPropertyFrom(relationSpecializationsFromSelected, relationSpecializationsFromSelected.get(relationSpecializationsFromSelected.indexOf(dto)-1).Property);
		if(dtoNext != null) haveNext = true;
		if(dtoPrev != null) havePrev = true;
		DtoGetPrevNextSpecProperty data = new DtoGetPrevNextSpecProperty(individualSelected.name, individualSelected.ns, dto, haveNext, havePrev);				  
		return data;
	}
	
	/**
	 * Update and check specialization class for all instances one by one
	 */
	private static void setClassSpecializationsInSelected()
	{
		InfModel model = UploadApp.getInferredModel();
		
		// ------ Complete classes list ------//		
		ArrayList<DtoCompleteClass> completeClassesList = new ArrayList<DtoCompleteClass>();
		DtoCompleteClass dto = null;		
		if(individualSelected.ListClasses.size() == 1 && individualSelected.ListClasses.get(0).contains("Thing"))	//Case thing
		{
			//Case if the instance have no class selected - only Thing
			dto = new DtoCompleteClass();
			dto.CompleteClass = individualSelected.ListClasses.get(0);			
			for (String subClas : QueryUtil.getClassesURI(model)) 
			{
				if(subClas != null) dto.AddMember(subClas);
			}
			completeClassesList.add(dto);			
		} else {			
			for (String cls : individualSelected.ListClasses)
			{					
				HashMap<String,List<String>> map = QueryUtil.getCompleteClassesURI(cls, individualSelected.ListClasses, model);
				for(String completeClassURI: map.keySet())
				{
					DtoCompleteClass dtoCompleteClass = new DtoCompleteClass();
					dtoCompleteClass.setCompleteClass(completeClassURI);
					dtoCompleteClass.addAllMember(map.get(completeClassURI));
					completeClassesList.add(dtoCompleteClass);
				}											
			}
		}		
		individualSelected.ListCompleteClasses = completeClassesList;		
	}

	/**
	 * 	Update and check specialization class for all instances one by one
	 */
	private static void setRelationSpecializationsInSelected() 
	{
		InfModel model = UploadApp.getInferredModel();	
		
		ArrayList<DtoPropertyAndSubProperties> completePropertiesList = new ArrayList<DtoPropertyAndSubProperties>();
		DtoPropertyAndSubProperties dtoP = null;		
		//Get instance relations
		//List<DtoInstanceRelation> instanceListRelations = new ArrayList<DtoInstanceRelation>();
		List<DtoInstanceRelation> instanceListRelations = QueryUtil.getPropertiesAndIndividualsURI(UploadApp.getInferredModel(), individualSelected.ns + individualSelected.name);
		
		List<String> propertiesURIList = QueryUtil.getPropertiesURI(UploadApp.getInferredModel(), individualSelected.ns + individualSelected.name);
		/*
		for(String propertyURI: propertiesURIList){
			DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyURI;			      
		    List<String> ranges = QueryUtil.getRangeIndividualURI(model, individualSelected.ns + individualSelected.name, propertyURI);
		    //List<String> ranges = QueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI);
		    if (ranges!=null && ranges.size()>0) dtoItem.Target = ranges.get(0);
		    else dtoItem.Target = "";
		    instanceListRelations.add(dtoItem);
		}
		*/
		for (DtoInstanceRelation dtoInstanceRelation : instanceListRelations) 
		{	
			List<String> subPropertiesWithDomainAndRange = QueryUtil.getSubPropertiesURIExcluding(model,individualSelected.ns + individualSelected.name, dtoInstanceRelation.Property, dtoInstanceRelation.Target, propertiesURIList);
			if(subPropertiesWithDomainAndRange.size() > 0)
			{
				dtoP = new DtoPropertyAndSubProperties();
				dtoP.Property = dtoInstanceRelation.Property;
				String target = dtoInstanceRelation.Target;
				if(target != null && !target.equals(""))
				{
					dtoP.iTargetNs = target.split("#")[0] + "#";
					dtoP.iTargetName = target.split("#")[1];
				}
				dtoP.propertyType = QueryUtil.getPropertyURIType(model, dtoInstanceRelation.Property);				
				for (String sub : subPropertiesWithDomainAndRange) 
				{
					boolean ok = true;					
					List<String> distointSubPropOfProp = QueryUtil.getPropertiesURIDisjointWith(model,sub);
					for (String disjointrop : distointSubPropOfProp) 
					{						
						for (DtoInstanceRelation dtoWithRelation : instanceListRelations) 
						{
							if(dtoWithRelation.Property.equals(disjointrop)) // instance have this sub relation
							{
								ok = false;
								break;
							}
						}
					}					
					for (DtoInstanceRelation dtoWithRelation : instanceListRelations) 
					{					
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
				if(dtoP.SubProperties.size() > 0) completePropertiesList.add(dtoP);
			}			
		}		
		individualSelected.ListSpecializationProperties = completePropertiesList;	
	}

	private static void setDefinitionsInSelected(OntCardinalityEnum typeCompletness)
	{
		InfModel model = UploadApp.getInferredModel();
		
		for (String classURI : individualSelected.ListClasses) 
		{
			List<DtoDefinitionClass> definitions;
			if(typeCompletness.equals(OntCardinalityEnum.SOME)){
				definitions = DtoQueryUtil.getSomeCardinalityDefinitionsFrom(model, classURI);
			}else{
				definitions = DtoQueryUtil.getCardinalityDefinitionsFrom(model, classURI, typeCompletness);
			}			
			
			for (DtoDefinitionClass def : definitions) {
				switch (typeCompletness) {
					case SOME:
						if(!individualSelected.ListSome.contains(def)){
							individualSelected.ListSome.add(def);
						}
						break;
					case MIN:
						if(!individualSelected.ListMin.contains(def)){
							individualSelected.ListMin.add(def);
						}
						break;
					case MAX:
						if(!individualSelected.ListMax.contains(def)){
							individualSelected.ListMax.add(def);
						}
						break;
					case EXACTLY:
						if(!individualSelected.ListExactly.contains(def)){
							individualSelected.ListExactly.add(def);
						}
						break;
					case COMPLETE:
						
						break;
				}
				
				
			}
		}		
	}
	
}
