package br.com.padtec.common.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoCommitMaxCard;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.dto.DtoStatus;
import br.com.padtec.common.dto.DtoViewSelectInstance;
import br.com.padtec.common.factory.DtoFactoryUtil;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntPropertyEnum;

import com.hp.hpl.jena.ontology.OntModel;

public class CommiterApp {
	
	//Keeps the new individuals to be created in the model, later on, in the commit.
	public static List<DtoInstance> newIndividualsCommitList = new ArrayList<DtoInstance>();
	//Keeps the new data values to be created in the model, later on, in the commit.
	public static List<DataPropertyValue> newDataValuesCommitList = new ArrayList<DataPropertyValue>();
	
	public static void clearCommitLists()
	{
		newDataValuesCommitList.clear();
		newIndividualsCommitList.clear();
	}
	
	/**
	 * This method does not add this new individual to the model and to the modified list.
	 * Instead, it only adds this individual to the set of list of new individuals to be created in the model when the method commitNewIndividuals() was called.
	 */
	public static DtoInstance createNewIndividualAtCommitList(String name, String[] arraySame, String[] arrayDif)
	{
		ArrayList<String> listSame = new ArrayList<String>();
		if(arraySame!=null && arraySame[0]!="") listSame.addAll(Arrays.asList(arraySame));		
		ArrayList<String> listDif = new ArrayList<String>();
		if(arrayDif!=null && arrayDif[0]!="") listDif.addAll(Arrays.asList(arrayDif));
		DtoInstance dtoIndividual = new DtoInstance(UploadApp.getBaseRepository().getNameSpace(), name, null, listDif, listSame, false);
		newIndividualsCommitList.add(dtoIndividual);
		return dtoIndividual;
	}
		
	public static DtoInstance addExistingIndividualAtCommitList(String individualURI)
	{
		DtoInstance dtoIndividual = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI,true,true,true);
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
		DtoInstance dtoIndividual = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI,true,true,true);
		List<DtoInstance> allIndividuals = DtoQueryUtil.getIndividuals(UploadApp.getInferredModel(), true, true, true);
		DtoViewSelectInstance dto = new DtoViewSelectInstance(dtoIndividual, allIndividuals);
		return dto;
	}	
	
	/**
	 * Create a new individual at the range of the cardinality restriction that was selected.
	 * 
	 * @param idNumber
	 * @param differentFromList
	 */
	public static void createNewIndividualAtClassDefinitionRangeSelected(Integer idNumber, List<String> differentFromList)
	{
		String individualName = OKCoApp.getSelectedClassDefinition().Target.split("#")[1] + "-" + (idNumber + 1);				
		DtoInstance newDtoIndividual = new DtoInstance(UploadApp.getBaseRepository().getNameSpace(), individualName, null, differentFromList, null, false);
		OntModel model = UploadApp.getBaseModel();
		FactoryUtil.createIndividual(model, 
			newDtoIndividual.ns+newDtoIndividual.name, 
			newDtoIndividual.ListSameInstances, 
			newDtoIndividual.ListDiferentInstances, 
			OKCoApp.getSelectedIndividualURI(), 
			OKCoApp.getSelectedClassDefinition().Relation, 
			OKCoApp.getSelectedClassDefinition().Target
		);
		OKCoApp.setIsModified(newDtoIndividual.ns + newDtoIndividual.name);
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
		DtoInstance newDtoIndividual = new DtoInstance(UploadApp.getBaseRepository().getNameSpace(), individualName, null, differentFromList, null, false);
		OntModel model = UploadApp.getBaseModel();
		FactoryUtil.createIndividual(model, 
			newDtoIndividual.ns+newDtoIndividual.name, 
			newDtoIndividual.ListSameInstances, 
			newDtoIndividual.ListDiferentInstances, 
			dtoIndividual.ns+dtoIndividual.name, 
			dtoDefinitionClass.Relation, 
			dtoDefinitionClass.Target
		);
		OKCoApp.setIsModified(newDtoIndividual.ns + newDtoIndividual.name);
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
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY) && dtoDefinitionClass.status.equals(DtoStatus.NOT_SATISFIED))
			{
				int individualsNumber = QueryUtil.getIndividualsURI(UploadApp.getInferredModel(), dtoDefinitionClass.Target).size()+ 1;
				/** Create a New Individual at the Range of the Class Definition */				
				createNewIndividualAtClassDefinitionRange(dtoIndividual, dtoDefinitionClass, individualsNumber, null);				
			}
		}		
		for (DtoDefinitionClass dtoDefinitionClass : dtoIndividual.ListMin) 
		{
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY) && dtoDefinitionClass.status.equals(DtoStatus.NOT_SATISFIED))
			{
				int individualsNumber = QueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), dtoIndividual.ns + dtoIndividual.name, dtoDefinitionClass.Relation, dtoDefinitionClass.Target);
				ArrayList<String> listDifferentFrom = new ArrayList<String>();
				while(individualsNumber < Integer.parseInt(dtoDefinitionClass.Cardinality))
				{
					/** Create a New Individual at the Range of the Class Definition */
					createNewIndividualAtClassDefinitionRange(dtoIndividual, dtoDefinitionClass, individualsNumber, listDifferentFrom);									
					individualsNumber ++;
				}				
			}					
		}
		for (DtoDefinitionClass dtoDefinitionClass : dtoIndividual.ListExactly) 
		{
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY) && dtoDefinitionClass.status.equals(DtoStatus.NOT_SATISFIED))
			{				
				int individualsNumber = QueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), dtoIndividual.ns + dtoIndividual.name, dtoDefinitionClass.Relation, dtoDefinitionClass.Target);					
				ArrayList<String> listDifferentFrom = new ArrayList<String>();
				if(individualsNumber < Integer.parseInt(dtoDefinitionClass.Cardinality))
				{
					while(individualsNumber < Integer.parseInt(dtoDefinitionClass.Cardinality))
					{
						/** Create a New Individual at the Range of the Class Definition */
						createNewIndividualAtClassDefinitionRange(dtoIndividual, dtoDefinitionClass, individualsNumber, listDifferentFrom);						
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
			dtoResult.setMessage("ok"); 
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
					DtoFactoryUtil.createIndividual(basemodel, dtoIndividual, OKCoApp.getSelectedIndividualURI(), OKCoApp.getSelectedClassDefinition().Relation, OKCoApp.getSelectedClassDefinition().Target);						
					isCreate = true;
				}else{
					/** Selected instance */						
					FactoryUtil.createObjectProperty(basemodel, OKCoApp.getSelectedIndividualURI(), OKCoApp.getSelectedClassDefinition().Relation, dtoIndividual.ns+dtoIndividual.name);						
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
				else FactoryUtil.deleteObjectProperty(basemodel, OKCoApp.getSelectedIndividualURI(), OKCoApp.getSelectedClassDefinition().Relation, dtoIndividual.ns + dtoIndividual.name);				
				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
		}		
		if(useReasoning.equals("false")) OKCoApp.setIsModified(OKCoApp.getSelectedIndividualURI());
		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("ok");
		return dtoResult;
	}
	
	/** 
	 * Remove the data values that was going to be created later. 
	 * This data value is not in the model yet. Thus, this method only removes it from the list of new data values.
	 */
	public static void removeNewDataValueFromCommitList(String uri)
	{		
		DtoFactoryUtil.removeDataValueFrom(newDataValuesCommitList, uri);
	}
	
	/**
	 * This method does not add this data value to the model.
	 * Instead, it only adds this data value to the set of list of new data value to be created in the model when the method commitNewDataValues() was called.
	 */
	public static DataPropertyValue createNewDataValueAtCommitList(String dataValue)
	{
		DataPropertyValue data = new DataPropertyValue();
		data.value = dataValue;
		data.classValue = OKCoApp.getSelectedClassDefinition().Target;	
		data.classValueEncoded = OKCoApp.getSelectedClassDefinition().uriTargetEncoded;
		data.existInModel = false;
		newDataValuesCommitList.add(data);
		return data;
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
					FactoryUtil.createRangeDataPropertyValue(basemodel, dataTarget.value, OKCoApp.getSelectedIndividualURI(), OKCoApp.getSelectedClassDefinition().Relation, OKCoApp.getSelectedClassDefinition().Target);											
					dataTarget.existInModel = true;
				}
				if(useReasoner.equals("true"))
				{
					/** Running the reasoner */
					UploadApp.substituteInferredModelFromBaseModel(true);
				} else {
					/** Without running the reasoner */
					UploadApp.substituteInferredModelFromBaseModel(false);
					OKCoApp.setIsModified(OKCoApp.getSelectedIndividualURI());
				}
			} catch (Exception e) {
				/** Delete Data Value */
				FactoryUtil.deleteRangeDataPropertyValue(basemodel, dataTarget.value, OKCoApp.getSelectedIndividualURI(), OKCoApp.getSelectedClassDefinition().Relation, OKCoApp.getSelectedClassDefinition().Target);
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
					DtoInstance s1 = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriSource,true,true,true);
					DtoInstance s2 = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriTarget,true,true,true);					
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
					OKCoApp.setIsModified(s1.ns + s1.name);
					OKCoApp.setIsModified(s2.ns + s2.name);
				}
			}			
			if(dto.runReasoner.equals("true"))
			{
				/** Running the reasoner, storing the temporary model and cleaning the list of modified */
				ReasoningApp.runReasoner();				
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
}
