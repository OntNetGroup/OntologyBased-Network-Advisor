package br.com.padtec.nopen.studio.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;


public class StudioFactory {

	public static void createTTF(String ttfName)
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfName, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.TTF.toString()
		);
	}
	
	public static void createAF(String afName)
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+afName, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.AF.toString()
		);
	}
	
	public static void createTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{
		String tfType = dtoTransportFunction.getType();
		String tfName = dtoTransportFunction.getName();
		String containerType = dtoContainer.getType();
		String containerName = dtoContainer.getName();
		
		System.out.println(tfType);
		System.out.println(tfName);
		System.out.println(containerType);
		System.out.println(containerName);		
	}
		
	public static boolean createPort(String portID, String transportFunctionID) 
	{
		return true;
	}

	public static boolean deleteLink(String id) 
	{	
		return true;
	}

	public static void insertContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception 
	{		
		//TODO
	}

	public static void deleteContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{	
		//TODO
	}

	public static boolean canCreateTransportFunction(String tFunctionID, String tFunctionType, String containerName, String containerType, String cardID) 
	{
		return true;
	}
	
	public static boolean deleteTransportFunction(String id) 
	{	
		return true;
	}

	public static boolean changeContainer(String tFunctionID, String containerName, String containerType, String cardID) 
	{	
		return true;
	}
	
	public static boolean deletePort(String id) 
	{				
		return true;
	}

	public static boolean createLink(String sourceTFunctionID, String targetTFunctionID) 
	{	
		return false;
	}

	public static boolean canCreateLink(String sourceTFunctionID, String targetTFunctionID) 
	{	
		return false;
	}
	
	public static void createEquipmentHolder(String id_EquipmentHolder, OKCoUploader repository)
	{
		String individualURI = repository.getNamespace()+id_EquipmentHolder;
		if(!QueryUtil.individualExists(repository.getBaseModel(), individualURI)){
			String classURI = repository.getNamespace()+ConceptEnum.EQUIPMENT_HOLDER.toString();
			FactoryUtil.createInstanceIndividual(repository.getBaseModel(), individualURI, classURI);
		}
	}
}
