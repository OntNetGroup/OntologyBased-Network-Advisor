package br.com.padtec.okco.core.application;

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
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.OntPropertyEnum;
import br.com.padtec.okco.core.exception.OKCoException;

import com.hp.hpl.jena.ontology.OntModel;

public class OKCoCommiter {
	
	protected OKCoUploader repository; 
	protected OKCoSelector selector; 
	protected OKCoReasoner reasoner; 
	
	public OKCoCommiter(OKCoUploader repository, OKCoSelector selector, OKCoReasoner reasoner)
	{
		this.repository = repository;
		this.selector = selector;
		this.reasoner= reasoner;
	}	
	
	//Keeps the new individuals to be created in the model, later on, in the commit.
	public List<DtoInstance> newIndividualsCommitList = new ArrayList<DtoInstance>();
	//Keeps the new data values to be created in the model, later on, in the commit.
	public List<DataPropertyValue> newDataValuesCommitList = new ArrayList<DataPropertyValue>();
	
	public void clearCommitLists()
	{
		newDataValuesCommitList.clear();
		newIndividualsCommitList.clear();
	}
	
	/**
	 * This method does not add this new individual to the model and to the modified list.
	 * Instead, it only adds this individual to the set of list of new individuals to be created in the model when the method commitNewIndividuals() was called.
	 * @throws OKCoException 
	 */
	public DtoInstance createNewIndividualAtCommitList(String name, String[] arraySame, String[] arrayDif) throws OKCoException
	{		
		ArrayList<String> listSame = new ArrayList<String>();
		if(arraySame!=null && arraySame[0]!="") listSame.addAll(Arrays.asList(arraySame));		
		ArrayList<String> listDif = new ArrayList<String>();
		if(arrayDif!=null && arrayDif[0]!="") listDif.addAll(Arrays.asList(arrayDif));
		DtoInstance dtoIndividual = new DtoInstance(repository.getBaseRepository().getNameSpace(), name, null, listDif, listSame, false);
		newIndividualsCommitList.add(dtoIndividual);
		return dtoIndividual;
	}
		
	/** 
	 * It does not make sense to check this in the OKCo application.
	 */
	@Deprecated
	public void checkIndividualsViolateMaxCardinality() throws OKCoException
	{		
		DtoInstance dtoIndividual = selector.getSelectedIndividual();
		DtoDefinitionClass dtoDefinition = selector.getSelectedClassDefinition();		
		if(dtoDefinition.TypeCompletness==OntCardinalityEnum.MAX)
		{	
			Integer number = Integer.parseInt(dtoDefinition.Cardinality);
			List<String> individuals = QueryUtil.getIndividualsURIAtPropertyRange(repository.getBaseModel(), dtoIndividual.uri, dtoDefinition.Relation);
			int newIndividuals = newIndividualsCommitList.size();
			if((individuals.size()+newIndividuals)>number) 
			{ 
				String message = "Max Cardinality Restriction Violated!\n\n "+
				"This property cannot have bear more than "+number+" individuals. \n"+
				"You tried to create "+newIndividuals+" new individuals in a property with already "+individuals.size()+" individuals.\n";
				new OKCoException(message); 
			}				
		}		
	}
	
	public DtoInstance addExistingIndividualAtCommitList(String individualURI)
	{
		DtoInstance dtoIndividual = DtoQueryUtil.getIndividualByName(repository.getInferredModel(), individualURI,true,true,true);
		newIndividualsCommitList.add(dtoIndividual);	
		return dtoIndividual;
	}
	
	/** 
	 * Remove the recent individual that was going to be created later. 
	 * This individual is not in the model yet. Thus, this method only removes it from the list of new individuals.
	 */  
	public void removeNewIndividualFromCommitList(String individualURI)
	{		
		DtoFactoryUtil.removeIndividualFrom(newIndividualsCommitList, individualURI);
	}
	
	/**
	 * Get individual in the commit list as an editing element.
	 *  
	 * @param individualURI
	 * @return
	 */
	public DtoViewSelectInstance getEditingIndividualFromCommitList(String individualURI)
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
	public DtoViewSelectInstance getEditingIndividualFromModel(String individualURI)
	{
		DtoInstance dtoIndividual = DtoQueryUtil.getIndividualByName(repository.getInferredModel(), individualURI,true,true,true);
		List<DtoInstance> allIndividuals = DtoQueryUtil.getIndividuals(repository.getInferredModel(), true, true, true);
		DtoViewSelectInstance dto = new DtoViewSelectInstance(dtoIndividual, allIndividuals);
		return dto;
	}	
	
	/**
	 * Create a new individual at the range of the cardinality restriction that was selected.
	 * 
	 * @param idNumber
	 * @param differentFromList
	 */
	public void createNewIndividualAtClassDefinitionRangeSelected(Integer idNumber, List<String> differentFromList)
	{
		String individualName = selector.getSelectedClassDefinition().Target.split("#")[1] + "-" + (idNumber + 1);				
		DtoInstance newDtoIndividual = new DtoInstance(repository.getBaseRepository().getNameSpace(), individualName, null, differentFromList, null, false);
		OntModel model = repository.getBaseModel();
		FactoryUtil.createIndividual(model, 
			newDtoIndividual.ns+newDtoIndividual.name, 
			newDtoIndividual.ListSameInstances, 
			newDtoIndividual.ListDiferentInstances, 
			selector.getSelectedIndividualURI(), 
			selector.getSelectedClassDefinition().Relation, 
			selector.getSelectedClassDefinition().Target
		);
		selector.setIsModified(newDtoIndividual.ns + newDtoIndividual.name);
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
	public void createNewIndividualAtClassDefinitionRange(DtoInstance dtoIndividual, DtoDefinitionClass dtoDefinitionClass, Integer idNumber, List<String> differentFromList)
	{
		String individualName = dtoDefinitionClass.Target.split("#")[1] + "-" + (idNumber + 1);				
		DtoInstance newDtoIndividual = new DtoInstance(repository.getBaseRepository().getNameSpace(), individualName, null, differentFromList, null, false);
		OntModel model = repository.getBaseModel();
		FactoryUtil.createIndividual(model, 
			newDtoIndividual.ns+newDtoIndividual.name, 
			newDtoIndividual.ListSameInstances, 
			newDtoIndividual.ListDiferentInstances, 
			dtoIndividual.ns+dtoIndividual.name, 
			dtoDefinitionClass.Relation, 
			dtoDefinitionClass.Target
		);
		selector.setIsModified(newDtoIndividual.ns + newDtoIndividual.name);
		if(differentFromList!=null) differentFromList.add(newDtoIndividual.ns + newDtoIndividual.name);	
	}
	
	/**
	 * Create new individuals automatically. It creates and classify the new individuals automatically according to their cardinality restrictions.
	 * 	
	 * @param dtoIndividual
	 */
	public void createNewIndividualsAutomatically(DtoInstance dtoIndividual)
	{
		DtoFactoryUtil.createAndClassifyIndividualAutomatically(repository.getBaseModel(), repository.getInferredModel(), dtoIndividual);		
		for (DtoDefinitionClass dtoDefinitionClass : dtoIndividual.ListSome) 
		{
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY) && dtoDefinitionClass.status.equals(DtoStatus.NOT_SATISFIED))
			{
				int individualsNumber = QueryUtil.getIndividualsURI(repository.getInferredModel(), dtoDefinitionClass.Target).size()+ 1;
				/** Create a New Individual at the Range of the Class Definition */				
				createNewIndividualAtClassDefinitionRange(dtoIndividual, dtoDefinitionClass, individualsNumber, null);				
			}
		}		
		for (DtoDefinitionClass dtoDefinitionClass : dtoIndividual.ListMin) 
		{
			if(dtoDefinitionClass.PropertyType.equals(OntPropertyEnum.OBJECT_PROPERTY) && dtoDefinitionClass.status.equals(DtoStatus.NOT_SATISFIED))
			{
				int individualsNumber = QueryUtil.countIndividualsURIAtPropertyRange(repository.getInferredModel(), dtoIndividual.ns + dtoIndividual.name, dtoDefinitionClass.Relation, dtoDefinitionClass.Target);
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
				int individualsNumber = QueryUtil.countIndividualsURIAtPropertyRange(repository.getInferredModel(), dtoIndividual.ns + dtoIndividual.name, dtoDefinitionClass.Relation, dtoDefinitionClass.Target);					
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
	public DtoResult commitNewIndividuals(String useReasoning)
	{		
		DtoResult dtoResult = new DtoResult();		
		if(newIndividualsCommitList.size()<=0) 
		{ 
			/** Nothing to do... */
			dtoResult.setIsSucceed(true); 
			dtoResult.setMessage("ok"); 
			return dtoResult; 
		}		
		OntModel basemodel = repository.getBaseModel();
		boolean isCreate = false;
		for (DtoInstance dtoIndividual : newIndividualsCommitList) 
		{
			try{					
				if(!dtoIndividual.existInModel) 
				{
					/** Create instance */						
					DtoFactoryUtil.createIndividual(basemodel, dtoIndividual, selector.getSelectedIndividualURI(), selector.getSelectedClassDefinition().Relation, selector.getSelectedClassDefinition().Target);						
					isCreate = true;
				}else{
					/** Selected instance */						
					FactoryUtil.createObjectProperty(basemodel, selector.getSelectedIndividualURI(), selector.getSelectedClassDefinition().Relation, dtoIndividual.ns+dtoIndividual.name);						
					isCreate = false;
				}
				if(useReasoning.equals("true")) 
				{
					/** Running the reasoner */
					repository.substituteInferredModelFromBaseModel(true); 
				}else {
					/** Without running the reasoner */
					repository.substituteInferredModelFromBaseModel(false);
					selector.setIsModified(dtoIndividual.ns + dtoIndividual.name);
				}			 
			}catch (Exception e) {
				/** Delete Individual */
				if(isCreate) DtoFactoryUtil.deleteIndividual(repository.getBaseModel(),dtoIndividual);
				else FactoryUtil.deleteObjectProperty(basemodel, selector.getSelectedIndividualURI(), selector.getSelectedClassDefinition().Relation, dtoIndividual.ns + dtoIndividual.name);				
				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
		}		
		if(useReasoning.equals("false")) selector.setIsModified(selector.getSelectedIndividualURI());
		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("ok");
		return dtoResult;
	}
	
	/** 
	 * Remove the data values that was going to be created later. 
	 * This data value is not in the model yet. Thus, this method only removes it from the list of new data values.
	 */
	public void removeNewDataValueFromCommitList(String uri)
	{		
		DtoFactoryUtil.removeDataValueFrom(newDataValuesCommitList, uri);
	}
	
	/**
	 * This method does not add this data value to the model.
	 * Instead, it only adds this data value to the set of list of new data value to be created in the model when the method commitNewDataValues() was called.
	 */
	public DataPropertyValue createNewDataValueAtCommitList(String dataValue)
	{
		DataPropertyValue data = new DataPropertyValue();
		data.value = dataValue;
		data.classValue = selector.getSelectedClassDefinition().Target;	
		data.classValueEncoded = selector.getSelectedClassDefinition().uriTargetEncoded;
		data.existInModel = false;
		newDataValuesCommitList.add(data);
		return data;
	}
	
	/**
	 * Performs the commit of the new data values to be created.
	 * It updates the inferred model from the base model using or not the inference engine.
	 */	
	public DtoResult commitDataValues(String useReasoner)
	{
		DtoResult dtoResult = new DtoResult();
		if(newDataValuesCommitList.size()<=0) 
		{ 
			/** Nothing to do... */
			dtoResult.setIsSucceed(true); 
			dtoResult.setMessage("nothing"); 
			return dtoResult; 
		}		
		OntModel basemodel = repository.getBaseModel();
		for (DataPropertyValue dataTarget : newDataValuesCommitList) 
		{
			try {
				if(!dataTarget.existInModel)
				{
					/** Create Data Value */
					FactoryUtil.createRangeDataPropertyValue(basemodel, dataTarget.value, selector.getSelectedIndividualURI(), selector.getSelectedClassDefinition().Relation, selector.getSelectedClassDefinition().Target);											
					dataTarget.existInModel = true;
				}
				if(useReasoner.equals("true"))
				{
					/** Running the reasoner */
					repository.substituteInferredModelFromBaseModel(true);
				} else {
					/** Without running the reasoner */
					repository.substituteInferredModelFromBaseModel(false);
					selector.setIsModified(selector.getSelectedIndividualURI());
				}
			} catch (Exception e) {
				/** Delete Data Value */
				FactoryUtil.deleteRangeDataPropertyValue(basemodel, dataTarget.value, selector.getSelectedIndividualURI(), selector.getSelectedClassDefinition().Relation, selector.getSelectedClassDefinition().Target);
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
	public DtoResult commitMaxCardinalities(DtoCommitMaxCard dto)
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
					DtoInstance s1 = DtoQueryUtil.getIndividualByName(repository.getInferredModel(), uriSource,true,true,true);
					DtoInstance s2 = DtoQueryUtil.getIndividualByName(repository.getInferredModel(), uriTarget,true,true,true);					
					if(type.equals("dif"))
					{
						OntModel basemodel = repository.getBaseModel();
						FactoryUtil.setDifferentFrom(basemodel, s1.ns + s1.name, s2.ns + s2.name);											
					} 
					else if (type.equals("same"))
					{
						OntModel basemodel = repository.getBaseModel();
						FactoryUtil.setSameAs(basemodel, s1.ns + s1.name, s2.ns + s2.name);												
					}else{						
						dtoResult.setMessage("error");
						dtoResult.setIsSucceed(false);
						return dtoResult;
					}					
					selector.setIsModified(s1.ns + s1.name);
					selector.setIsModified(s2.ns + s2.name);
				}
			}			
			if(dto.runReasoner.equals("true"))
			{
				/** Running the reasoner, storing the temporary model and cleaning the list of modified */
				reasoner.runReasoner();				
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
