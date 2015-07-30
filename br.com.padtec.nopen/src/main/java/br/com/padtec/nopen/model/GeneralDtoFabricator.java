package br.com.padtec.nopen.model;

import com.hp.hpl.jena.ontology.Individual;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.okco.core.application.OKCoUploader;

public class GeneralDtoFabricator {
	
	/** @author John Guerson */
	public static void createElement(OKCoUploader repository, DtoJointElement dtoElement) throws Exception
	{	
		String elemType = dtoElement.getType();		
		String elemId = dtoElement.getId();
		String elemName = dtoElement.getName();
		
		if(elemType==null || elemId==null) {
			NOpenLog.appendLine("Error: Argument is null. Unexpected creation of element "+elemType+"::"+elemName);
			throw new Exception("Argument is null. Unexpected creation of element "+elemType+"::"+elemName);		
		}
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+elemId, 
			repository.getNamespace()+elemType
		);
		i.setLabel(elemName,"EN");		
	}
	
	/** @author John Guerson */
	public static void deleteElement(OKCoUploader repository, DtoJointElement dtoElement) throws Exception
	{		
		String elemType = dtoElement.getType();		
		String elemId = dtoElement.getId();
		String elemName = dtoElement.getName();		
		
		if(elemType==null || elemId==null) {
			NOpenLog.appendLine("Error: Argument is null. Unexpected deletion of element "+elemType+"::"+elemName);
			throw new Exception("Argument is null. Unexpected deletion of element "+elemType+"::"+elemName);		
		}
		
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+elemId
		);		
	}
	
	public static void createComponentOf(OKCoUploader repository, DtoJointElement dtoSource, DtoJointElement dtoTarget) throws Exception
	{			
		String srcId = dtoSource.getId();
		String srcType = dtoSource.getType();
			
		String tgtId = dtoTarget.getId();
		String tgtType = dtoTarget.getType();
		
		if(srcId==null) {
			NOpenLog.appendLine("Error: Argument is null. Unexpected deletion of connectio involving "+srcId+"::"+srcType);
			throw new Exception("Argument is null. Unexpected deletion of connection involving "+srcId+"::"+srcType);		
		}
		
		if(tgtId==null) {
			NOpenLog.appendLine("Error: Argument is null. Unexpected deletion of connection involving "+tgtId+"::"+tgtType);
			throw new Exception("Argument is null. Unexpected deletion of connection involving "+tgtId+"::"+tgtType);		
		}
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+srcId, 
			repository.getNamespace()+RelationEnum.componentOf.toString(),
			repository.getNamespace()+tgtId
		);
	}
}
