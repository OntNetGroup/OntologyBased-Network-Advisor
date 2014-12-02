package br.com.padtec.common.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoClassifyInstancePost;
import br.com.padtec.common.dto.DtoCommitMaxCard;
import br.com.padtec.common.dto.DtoCompleteClass;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoGetPrevNextSpecProperty;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.dto.DtoViewSelectInstance;
import br.com.padtec.common.exceptions.OKCoNameSpaceException;
import br.com.padtec.common.factory.DtoFactoryUtil;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.graph.GraphPlotting;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntPropertyEnum;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;


public class OKCoApp {
	
	//Keeps the list of individuals that were modified
	public static List<String> modifiedIndividualsURIs = new ArrayList<String>();
	
	//Keeps the individual that was selected with other useful informations
	public static DtoInstance individualSelected;
	public static List<DtoCompleteClass> completeClassesFromSelected;
	public static List<DtoPropertyAndSubProperties> propertiesFromSelected;
	
	//Keeps the cardinality definition that was selected
	public static DtoDefinitionClass definitionClassSelected;
	
	//Keeps the new individuals to be created in the model, later on, in the commit.
	public static List<DtoInstance> newIndividualsCommitList;
	//Keeps the new data values to be created in the model, later on, in the commit.
	public static List<DataPropertyValue> newDataValuesCommitList;
		
	/**
	 * Select a particular individual to be used later on.
	 * 
	 * @param individualURI
	 * @return
	 */
	public static DtoInstance selectIndividual(String individualURI)
	{
		individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI);
		return individualSelected;
	}
	
	/**
	 * Select a particular cardinality definition to be used later on.
	 * 
	 * @param uriProperty
	 * @return
	 */
	public static DtoDefinitionClass selectDefinitionFromSelected(String uriProperty)
	{
		OKCoApp.definitionClassSelected = DtoDefinitionClass.get(individualSelected.ListSome, uriProperty);
		if(OKCoApp.definitionClassSelected == null) OKCoApp.definitionClassSelected = DtoDefinitionClass.get(individualSelected.ListMin, uriProperty);
		if(OKCoApp.definitionClassSelected == null) OKCoApp.definitionClassSelected = DtoDefinitionClass.get(individualSelected.ListMax, uriProperty);
		if(OKCoApp.definitionClassSelected == null)	OKCoApp.definitionClassSelected = DtoDefinitionClass.get(individualSelected.ListExactly, uriProperty);
		return definitionClassSelected;
	}
	
	/**
	 * Set this individual to isModified = true.
	 * 
	 * @param individualURI
	 */
	public static void setIsModified(String individualURI)
	{
		if(!modifiedIndividualsURIs.contains(individualURI)) modifiedIndividualsURIs.add(individualURI);
	}
	
	public static void clearModified()
	{
		modifiedIndividualsURIs.clear();
	}
	
	public static void clear() 
	{		
		modifiedIndividualsURIs.clear();
		definitionClassSelected=null;
		individualSelected=null;
		completeClassesFromSelected.clear();		
		propertiesFromSelected.clear();
		newDataValuesCommitList.clear();
		newIndividualsCommitList.clear();
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
	 * List of all individuals that were modified.
	 * 
	 * @return
	 */
	public static List<String> getModifiedIndividuals()
	{
		return modifiedIndividualsURIs;
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
		if(individualSelected!=null) return individualSelected.getSomeDefinitionWithNoRepetition();
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	/**
	 * List of all MAX cardinality restrictions from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoDefinitionClass> getMaxDefinitionsFromSelected()
	{
		if(individualSelected!=null) return individualSelected.getMaxDefinitionWithNoRepetition();
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	/**
	 * List of all MIN cardinality restrictions from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoDefinitionClass> getMinDefinitionsFromSelected()
	{
		if(individualSelected!=null) return individualSelected.getMinDefinitionWithNoRepetition();
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	/**
	 * List of all EXACT cardinality restrictions from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoDefinitionClass> getExactDefinitionsFromSelected()
	{
		if(individualSelected!=null) return individualSelected.getExactDefinitionWithNoRepetition();
		else return new ArrayList<DtoDefinitionClass>();
	}
	
	/**
	 * List of all properties and sub-properties from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoPropertyAndSubProperties> getPropertiesFromSelected()
	{
		if(individualSelected!=null) {
			propertiesFromSelected = individualSelected.getPropertiesAndSubProperties();
			return propertiesFromSelected;
		}
		else return new ArrayList<DtoPropertyAndSubProperties>();
	}
	
	/**
	 * List of all relations of the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoInstanceRelation> getRelationsFromSelected()
	{
		if(individualSelected!=null) {
			return DtoQueryUtil.getRelations(UploadApp.getInferredModel(),getSelectedIndividualURI());			
		}
		else return new ArrayList<DtoInstanceRelation>();
	}
		
	/**
	 * Get the graph values for the graph visualization.
	 * 
	 * @return
	 */
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
	
	/**
	 * This method does not add this new individual to the model and to the modified list.
	 * Instead, it only adds this individual to the set of list of new individuals to be created in the model when the method commitNewIndividuals() was called.
	 */
	public static DtoInstance createNewIndividualAtCommitList(String name, String[] arraySame, String[] arrayDif)
	{
		ArrayList<String> listSame = new ArrayList<String>(Arrays.asList(arraySame));		
		ArrayList<String> listDif = new ArrayList<String>(Arrays.asList(arrayDif));		
		DtoInstance dtoIndividual = new DtoInstance(UploadApp.baseRepository.getNameSpace(), name, null, listDif, listSame, false);
		newIndividualsCommitList.add(dtoIndividual);
		return dtoIndividual;
	}
	
	/**
	 * This method does not add this data value to the model.
	 * Instead, it only adds this data value to the set of list of new data value to be created in the model when the method commitNewDataValues() was called.
	 */
	public static DataPropertyValue createNewDataValueAtCommitList(String dataValue)
	{
		DataPropertyValue data = new DataPropertyValue();
		data.value = dataValue;
		data.classValue = definitionClassSelected.Target;
		data.existInModel = false;
		newDataValuesCommitList.add(data);
		return data;
	}
	
	public static DtoInstance addExistingIndividualAtCommitList(String individualURI)
	{
		DtoInstance dtoIndividual = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI);
		newIndividualsCommitList.add(dtoIndividual);		
		return dtoIndividual;
	}
	
	/** 
	 * Remove the recent individual that was going to be created later. 
	 * This individual is not in the model yet. Thus, this method only removes it from the list of new individuals.
	 */  
	public static void removeNewIndividualFromCommitList(String individualURI)
	{		
		DtoFactoryUtil.removeIndividualFrom(newIndividualsCommitList, individualURI);		
	}
	
	/** 
	 * Remove the data values that was going to be created later. 
	 * This data value is not in the model yet. Thus, this method only removes it from the list of new data values.
	 */
	public static void removeNewDataValueFromCommitList(String individualURI)
	{		
		DtoFactoryUtil.removeDataValueFrom(newDataValuesCommitList, individualURI);		
	}
	
	/**
	 * Get individual in the commit list as an editing element.
	 *  
	 * @param individualURI
	 * @return
	 */
	public static DtoViewSelectInstance getEditingIndividualFromCommitList(String individualURI)
	{
		DtoInstance dtoIndividual = DtoFactoryUtil.getIndividualFrom(newIndividualsCommitList, individualURI);
		DtoViewSelectInstance dto = new DtoViewSelectInstance(dtoIndividual, newIndividualsCommitList);
		return dto;
	}
	
	/**
	 * Get individual in the model as an editing element.
	 *  
	 * @param individualURI
	 * @return
	 */
	public static DtoViewSelectInstance getEditingIndividualFromModel(String individualURI)
	{
		DtoInstance dtoIndividual = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI);
		List<DtoInstance> allIndividuals = DtoQueryUtil.getIndividuals(UploadApp.getInferredModel(), true, true, true);
		DtoViewSelectInstance dto = new DtoViewSelectInstance(dtoIndividual, allIndividuals);
		return dto;
	}
	
	/**
	 * Running the reasoner, storing a temporary model and cleaning the list of modified.
	 * 
	 * @return
	 */
	public static DtoResult runReasoner()
	{
		DtoResult dto = new DtoResult();
		try {			
			/** Running reasoner... */
			UploadApp.substituteInferredModelFromBaseModel(true);			
			/** Storing a temporary model... */
			UploadApp.storeTemporaryModelFromBaseModel();
			/** Clean List of modified individuals */
			clearModified();
		}
		catch (Exception e) 
		{
			/** Roll back to the temporary model stored, running the reasoner. */  
			UploadApp.rollBack(true);			
			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			dto.setMessage(error);
			dto.setIsSucceed(false);			
		}
		dto.setIsSucceed(true);
		dto.setMessage("ok");
		return dto;		
	}
	
	/**
	 * List of all classes that are complete from the selected individual.
	 * 
	 * @return
	 */
	public static List<DtoCompleteClass> getCompleteClassesFromSelected()
	{
		if(individualSelected!=null) {
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
		String uriIndividualSelected = individualSelected.ns + individualSelected.name;
		InfModel model = UploadApp.getInferredModel();
		List<DtoInstance> dtoIndividuals = DtoQueryUtil.getIndividualsAtObjectPropertyRange(model,uriIndividualSelected, definitionClassSelected.Relation, definitionClassSelected.Target);
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
	
	
	/**
	 * Create a new individual at the range of the cardinality restriction that was selected.
	 * 
	 * @param idNumber
	 * @param differentFromList
	 */
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
		
	/**
	 * Create new Individual at the range of the cardinality restriction that was selected
	 * 
	 * @param dtoIndividual
	 * @param dtoDefinitionClass
	 * @param idNumber
	 * @param differentFromList
	 */
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
	
	/**
	 * Create new individuals automatically. It creates and classify the new individuals automatically according to their cardinality restrictions.
	 * 	
	 * @param dtoIndividual
	 */
	public static void createNewIndividualsAutomatically(DtoInstance dtoIndividual)
	{
		DtoFactoryUtil.createAndClassifyIndividualAutomatically(UploadApp.getBaseModel(), UploadApp.getInferredModel(), dtoIndividual);		
		for (DtoDefinitionClass dtoDefinitionClass : dtoIndividual.ListSome) 
		{
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY))
			{
				int individualsNumber = QueryUtil.getIndividualsURI(UploadApp.getInferredModel(), dtoDefinitionClass.Target).size()+ 1;
				/** Create a New Individual at the Range of the Class Definition */				
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
					/** Create a New Individual at the Range of the Class Definition */
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
						/** Create a New Individual at the Range of the Class Definition */
						OKCoApp.createNewIndividualAtClassDefinitionRange(dtoIndividual, dtoDefinitionClass, individualsNumber, listDifferentFrom);						
						individualsNumber ++;
					}
				}
			}
		}
	}
	
	/**
	 * Performs the commit of the new individuals to be created.
	 * It updates the inferred model from the base model using or not the inference engine.
	 */		
	public static DtoResult commitNewIndividuals(String useReasoning)
	{		
		DtoResult dtoResult = new DtoResult();		
		if(newIndividualsCommitList.size()<=0) 
		{ 
			/** Nothing to do... */
			dtoResult.setIsSucceed(true); 
			dtoResult.setMessage("nothing"); 
			return dtoResult; 
		}		
		OntModel basemodel = UploadApp.getBaseModel();
		boolean isCreate = false;
		for (DtoInstance dtoIndividual : newIndividualsCommitList) 
		{
			try{					
				if(!dtoIndividual.existInModel) 
				{
					/** Create instance */						
					DtoFactoryUtil.createIndividual(basemodel, dtoIndividual, getSelectedIndividualURI(), definitionClassSelected.Relation, definitionClassSelected.Target);						
					isCreate = true;
				}else{
					/** Selected instance */						
					FactoryUtil.createObjectProperty(basemodel, getSelectedIndividualURI(), definitionClassSelected.Relation, dtoIndividual.ns+dtoIndividual.name);						
					isCreate = false;
				}
				if(useReasoning.equals("true")) 
				{
					/** Running the reasoner */
					UploadApp.substituteInferredModelFromBaseModel(true); 
				}else {
					/** Without running the reasoner */
					UploadApp.substituteInferredModelFromBaseModel(false);
					OKCoApp.setIsModified(dtoIndividual.ns + dtoIndividual.name);
				}			 
			}catch (Exception e) {
				/** Delete Individual */
				if(isCreate) DtoFactoryUtil.deleteIndividual(UploadApp.getBaseModel(),dtoIndividual);
				else FactoryUtil.deleteObjectProperty(basemodel, getSelectedIndividualURI(), OKCoApp.definitionClassSelected.Relation, dtoIndividual.ns + dtoIndividual.name);				
				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
		}		
		if(useReasoning.equals("false")) setIsModified(getSelectedIndividualURI());
		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("ok");
		return dtoResult;
	}
	
	/**
	 * Performs the commit of the new data values to be created.
	 * It updates the inferred model from the base model using or not the inference engine.
	 */	
	public static DtoResult commitDataValues(String useReasoner)
	{
		DtoResult dtoResult = new DtoResult();
		if(newDataValuesCommitList.size()<=0) 
		{ 
			/** Nothing to do... */
			dtoResult.setIsSucceed(true); 
			dtoResult.setMessage("nothing"); 
			return dtoResult; 
		}		
		OntModel basemodel = UploadApp.getBaseModel();
		for (DataPropertyValue dataTarget : newDataValuesCommitList) 
		{
			try {
				if(!dataTarget.existInModel)
				{
					/** Create Data Value */
					FactoryUtil.createRangeDataPropertyValue(basemodel, dataTarget.value, getSelectedIndividualURI(), definitionClassSelected.Relation, definitionClassSelected.Target);											
					dataTarget.existInModel = true;
				}
				if(useReasoner.equals("true"))
				{
					/** Running the reasoner */
					UploadApp.substituteInferredModelFromBaseModel(true);
				} else {
					/** Without running the reasoner */
					UploadApp.substituteInferredModelFromBaseModel(false);
					OKCoApp.setIsModified(getSelectedIndividualURI());
				}
			} catch (Exception e) {
				/** Delete Data Value */
				FactoryUtil.deleteRangeDataPropertyValue(basemodel, dataTarget.value, getSelectedIndividualURI(), definitionClassSelected.Relation, definitionClassSelected.Target);
				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
		}
		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("ok");
		return dtoResult;
	}
	
	/**
	 * Performs the commit of the max cardinalities. 
	 * It updates the inferred model from the base model using or not the inference engine.
	 */
	public static DtoResult commitMaxCardinalities(DtoCommitMaxCard dto)
	{
		DtoResult dtoResult = new DtoResult();		
		try {			
			String separatorValues = "%&&%";
			String[] arrayValues = dto.ListInstanceDifSameIds.split(separatorValues);
			for (String val : arrayValues) 
			{
				if(val.contains("x"))
				{	
					String[] parts = val.split("x");
					String type = parts[0];
					String uriSource = parts[1];
					String uriTarget = parts[2];					
					DtoInstance s1 = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriSource);
					DtoInstance s2 = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriTarget);					
					if(type.equals("dif"))
					{
						OntModel basemodel = UploadApp.getBaseModel();
						FactoryUtil.setDifferentFrom(basemodel, s1.ns + s1.name, s2.ns + s2.name);											
					} 
					else if (type.equals("same"))
					{
						OntModel basemodel = UploadApp.getBaseModel();
						FactoryUtil.setSameAs(basemodel, s1.ns + s1.name, s2.ns + s2.name);												
					}else{						
						dtoResult.setMessage("error");
						dtoResult.setIsSucceed(false);
						return dtoResult;
					}					
					modifiedIndividualsURIs.add(s1.ns + s1.name);
					modifiedIndividualsURIs.add(s2.ns + s2.name);
				}
			}			
			if(dto.runReasoner.equals("true"))
			{
				/** Running the reasoner, storing the temporary model and cleaning the list of modified */
				OKCoApp.runReasoner();				
			} 
			else if (!dto.runReasoner.equals("false"))
			{				
				dtoResult.setMessage("error");
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}			
		} catch (Exception e) {			
			dtoResult.setMessage("error");
			dtoResult.setIsSucceed(false);
			return dtoResult;
		}
		dtoResult.setMessage("ok");
		dtoResult.setIsSucceed(true);
		return dtoResult;
	}
	
	/**
	 * Classifies individuals classes.
	 */
	public static DtoResult classifyIndividualsClasses(String[] classes)
	{		
		DtoResult dtoResult = new DtoResult();
		List<String> classesList = new ArrayList<String>(Arrays.asList(classes));			
		if(classesList.size()<=0)
		{
			dtoResult.setIsSucceed(true);
			dtoResult.setMessage("nothing");
			return dtoResult;			
		}
		OntModel basemodel = UploadApp.getBaseModel();
		for (String cls : classesList) 
		{
			try {				
				/** Create an individual for this class */
				if(!cls.equals("")) FactoryUtil.createIndividualOfClass(basemodel, getSelectedIndividualURI(), cls);
				
			} catch (Exception e){
				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
		}
		try {
			/** Update of the individual selected */
			OKCoApp.individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), getSelectedIndividualURI());
			
		} catch (Exception e) {
			dtoResult.setMessage(e.getMessage());
			dtoResult.setIsSucceed(false);
			/** Remove all created */
			for (String clsAux : classesList)
			{				
				if(!clsAux.equals(""))FactoryUtil.deleteIndividualOfClass(basemodel,getSelectedIndividualURI(), clsAux);				
			}
			/** Update of the individual selected */
			OKCoApp.individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), getSelectedIndividualURI());
			return dtoResult;
		}		
		setIsModified(getSelectedIndividualURI());
		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("ok");
		return dtoResult;
	}
		
	/**
	 * Classifies individuals properties.
	 */
	public static DtoResult classifyIndividualsProperties(String[] properties, DtoClassifyInstancePost dto)
	{
		DtoResult dtoResult = new DtoResult();
		List<String> propertiesList = new ArrayList<String>(Arrays.asList(properties));		
		if(propertiesList.size()<=0) 
		{
			dtoResult.setIsSucceed(true);
			dtoResult.setMessage("nothing");
			return dtoResult;
		}
		DtoPropertyAndSubProperties dtoSpec =  DtoFactoryUtil.getPropertyFrom(propertiesFromSelected, dto.arraySubProp);		
		OntModel basemodel = UploadApp.getBaseModel();		
		for (String subRel : propertiesList) 
		{
			try {
				if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY))
				{	
					/** Create Data Property */
					if(!subRel.equals(""))FactoryUtil.createRangeDataPropertyValue(basemodel, dtoSpec.iTargetNs.split("\\^\\^")[0], getSelectedIndividualURI(), subRel, dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName);											
				}else{
					/** Create Object Property */
					if(!subRel.equals(""))FactoryUtil.createObjectProperty(basemodel,getSelectedIndividualURI(),subRel, dtoSpec.iTargetNs + dtoSpec.iTargetName);
				}					
			}catch (Exception e){
				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
		}
		try {				
			/** Update the individual selected */
			OKCoApp.individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), getSelectedIndividualURI());
		}catch (Exception e){
			dtoResult.setMessage(e.getMessage());
			dtoResult.setIsSucceed(false);
			/** Remove all created */
			for (String subRelAux : propertiesList) 
			{
				if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY)){						
					/** Delete Data Property */
					if(!subRelAux.equals(""))FactoryUtil.deleteRangeDataPropertyValue(basemodel, dtoSpec.iTargetNs.split("\\^\\^")[0], getSelectedIndividualURI(), subRelAux, dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName);
				}else{					
					/** Delete Object Property */
					if(!subRelAux.equals(""))FactoryUtil.createObjectProperty(basemodel, getSelectedIndividualURI(), subRelAux,dtoSpec.iTargetNs + dtoSpec.iTargetName);					
				}
			}
			/** Update Individual Selected */
			OKCoApp.individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), getSelectedIndividualURI());
			return dtoResult;
		}
		setIsModified(getSelectedIndividualURI());
		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("ok");
		return dtoResult;			
	}
	
	/**
	 * Get the Property from Selected alongside the next and previous properties.
	 */
	public static DtoGetPrevNextSpecProperty getPropertyWithNextAndPreviousFromSelected(String propertyURI)
	{
		DtoPropertyAndSubProperties dto = DtoFactoryUtil.getPropertyFrom(propertiesFromSelected, propertyURI);
		if(dto == null) return null;			
		boolean haveNext = false;
		boolean havePrev = false;
		DtoPropertyAndSubProperties dtoNext =  DtoFactoryUtil.getPropertyFrom(propertiesFromSelected, propertiesFromSelected.get(propertiesFromSelected.indexOf(dto)+1).Property);
		DtoPropertyAndSubProperties dtoPrev=  DtoFactoryUtil.getPropertyFrom(propertiesFromSelected, propertiesFromSelected.get(propertiesFromSelected.indexOf(dto)-1).Property);
		if(dtoNext != null) haveNext = true;
		if(dtoPrev != null) havePrev = true;
		DtoGetPrevNextSpecProperty data = new DtoGetPrevNextSpecProperty(individualSelected.name, individualSelected.ns, dto, haveNext, havePrev);				  
		return data;
	}
	
	/**
	 * Update and check specialization class for all instances one by one
	 */
	public static void setClassSpecializationsInSelected()
	{
		InfModel model = UploadApp.getInferredModel();
		
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

	/**
	 * 	Update and check specialization class for all instances one by one
	 */
	public static void setRelationSpecializationsInSelected() 
	{
		InfModel model = UploadApp.getInferredModel();
	
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
	
}
