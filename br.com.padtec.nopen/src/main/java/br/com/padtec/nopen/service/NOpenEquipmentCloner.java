package br.com.padtec.nopen.service;

import com.jointjs.util.JointUtilManager;

import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.GeneralDtoFabricator;
import br.com.padtec.nopen.model.InstanceFabricator;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.nopen.provisioning.service.InterfaceStructure;
import br.com.padtec.nopen.studio.service.PerformBind;
import br.com.padtec.okco.core.application.OKCoUploader;

public class NOpenEquipmentCloner {
	
	/** @author John Guerson */
	public static void cloneEquipmentFromJSON(String jsonEquipments, OKCoUploader tgtRepository) throws Exception
	{					
		PElement[] elems = (PElement[]) JointUtilManager.getJavaFromJSON(jsonEquipments, PElement[].class);		
		for(PElement pElem: elems)
		{
			/** we need DtoJointElement as parameter*/
			DtoJointElement dtoElem = new DtoJointElement();
			dtoElem.setId(pElem.getId());
			dtoElem.setName(pElem.getName());
			dtoElem.setType(pElem.getType());

			GeneralDtoFabricator.createElement(tgtRepository, dtoElem);
		}		
	}
	
	private static boolean isTF(String type)
	{
		if(type.compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0) return true;
		if(type.compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0) return true;
		if(type.compareToIgnoreCase(ConceptEnum.Matrix.toString())==0) return true;
		if(type.compareToIgnoreCase(ConceptEnum.Physical_Media.toString())==0) return true;
		return false;
	}
	
	private static boolean isInterface(String type)
	{
		if(type.compareToIgnoreCase(ConceptEnum.Output_Card.toString())==0) return true;
		if(type.compareToIgnoreCase(ConceptEnum.Input_Card.toString())==0) return true;		
		return false;
	}
	
	/** @author John Guerson */
	public static void cloneLinksFromJSON(String jsonLinks, OKCoUploader tgtRepository) throws Exception
	{
		
		System.out.println(jsonLinks);
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink pLink: links)
		{
			String sourceId = pLink.getSource();
			String sourceType = pLink.getSourceType();			
			String targetId = pLink.getTarget();
			String targetType = pLink.getTargetType();

			DtoJointElement dtoSource = new DtoJointElement();
			dtoSource.setId(sourceId);
			dtoSource.setType(sourceType);
			
			DtoJointElement dtoTarget = new DtoJointElement();
			dtoTarget.setId(targetId);
			dtoTarget.setType(targetType);			
			
			if((isTF(sourceType) || isInterface(sourceType)) && (isTF(targetType))){			
				PerformBind.applyBindsWithoutVerification(dtoSource, dtoTarget, tgtRepository);
			}
			else if(isInterface(sourceType) && isInterface(targetType)){
				InterfaceStructure.applyPreProvisioningBinds("Vertical", sourceId, targetId, tgtRepository);
			}
			else {
				InstanceFabricator.createComponentOfRelation(dtoSource, dtoTarget, tgtRepository);
			}
		}			
	}
}
