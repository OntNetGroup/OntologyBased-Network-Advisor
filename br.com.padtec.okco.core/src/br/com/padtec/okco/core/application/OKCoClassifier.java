package br.com.padtec.okco.core.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.padtec.common.dto.DtoClassifyInstancePost;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.factory.DtoFactoryUtil;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.types.OntPropertyEnum;

import com.hp.hpl.jena.ontology.OntModel;

public class OKCoClassifier {
	
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
		OntModel basemodel = OKCoUploader.getBaseModel();
		for (String cls : classesList) 
		{
			try {				
				/** Create an individual for this class */
				if(!cls.equals("")) FactoryUtil.createIndividualOfClass(basemodel, OKCoSelector.getSelectedIndividualURI(), cls);
				
			} catch (Exception e){
				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
		}
		try {
			/** Update of the individual selected */
			OKCoSelector.selectIndividual(DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), OKCoSelector.getSelectedIndividualURI(),true,true,true));
			
		} catch (Exception e) {
			dtoResult.setMessage(e.getMessage());
			dtoResult.setIsSucceed(false);
			/** Remove all created */
			for (String clsAux : classesList)
			{				
				if(!clsAux.equals(""))FactoryUtil.deleteIndividualOfClass(basemodel,OKCoSelector.getSelectedIndividualURI(), clsAux);				
			}
			/** Update of the individual selected */
			OKCoSelector.selectIndividual(DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), OKCoSelector.getSelectedIndividualURI(),true,true,true));
			return dtoResult;
		}		
		OKCoSelector.setSelectedToModified();
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
		DtoPropertyAndSubProperties dtoSpec =  DtoFactoryUtil.getPropertyFrom(OKCoSelector.getRelationSpecializationsFromSelected(), dto.arraySubProp);		
		OntModel basemodel = OKCoUploader.getBaseModel();		
		for (String subRel : propertiesList) 
		{
			try {
				if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY))
				{	
					/** Create Data Property */
					if(!subRel.equals(""))FactoryUtil.createRangeDataPropertyValue(basemodel, dtoSpec.iTargetNs.split("\\^\\^")[0], OKCoSelector.getSelectedIndividualURI(), subRel, dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName);											
				}else{
					/** Create Object Property */
					if(!subRel.equals(""))FactoryUtil.createObjectProperty(basemodel,OKCoSelector.getSelectedIndividualURI(),subRel, dtoSpec.iTargetNs + dtoSpec.iTargetName);
				}					
			}catch (Exception e){
				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
		}
		try {				
			/** Update the individual selected */
			OKCoSelector.selectIndividual(DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), OKCoSelector.getSelectedIndividualURI(),true,true,true));
		}catch (Exception e){
			dtoResult.setMessage(e.getMessage());
			dtoResult.setIsSucceed(false);
			/** Remove all created */
			for (String subRelAux : propertiesList) 
			{
				if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY)){						
					/** Delete Data Property */
					if(!subRelAux.equals(""))FactoryUtil.deleteRangeDataPropertyValue(basemodel, dtoSpec.iTargetNs.split("\\^\\^")[0], OKCoSelector.getSelectedIndividualURI(), subRelAux, dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName);
				}else{
					/** Delete Object Property */
					if(!subRelAux.equals(""))FactoryUtil.createObjectProperty(basemodel, OKCoSelector.getSelectedIndividualURI(), subRelAux,dtoSpec.iTargetNs + dtoSpec.iTargetName);					
				}
			}
			/** Update Individual Selected */
			OKCoSelector.selectIndividual(DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), OKCoSelector.getSelectedIndividualURI(),true,true,true));
			return dtoResult;
		}
		OKCoSelector.setSelectedToModified();
		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("ok");
		return dtoResult;			
	}
}
