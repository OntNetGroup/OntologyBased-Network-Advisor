package br.com.padtec.nopen.studio.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;
import br.com.padtec.nopen.service.util.NOpenFactoryUtil;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;


public class StudioFactory {

	public static void createTechnology(String techName) throws Exception
	{
		NOpenFactoryUtil.createTechnology(ProvisioningComponents.provisioningRepository, techName);
	}
	
	public static void createLayer(OKCoUploader repository, String layerName, String techName) throws Exception
	{
		NOpenFactoryUtil.createLayer(ProvisioningComponents.provisioningRepository,layerName, techName);
	}
	
	public static void createTTF(String ttfName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfName, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Trail_Termination_Function.toString()
		);
	}
	
	public static void createAF(String afName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+afName, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
		);
	}
	
	public static void createTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{
//		String tfType = dtoTransportFunction.getType();
//		
//		String tfName = dtoTransportFunction.getName();
//		if(tfName==null) tfName = "UnNamed";
//		
//		String containerType = dtoContainer.getType();
//		String containerName = dtoContainer.getName();
//		if(containerName==null) containerName = "UnNamed";
//		
//		if(tfType.equals("TTF")) createTTF(tfName);
//		if(tfType.equals("AF")) createTTF(tfName);
//		 
//		System.out.println("Transport Function: "+tfName+":"+tfType);
//		System.out.println("Container: "+containerName+":"+containerType);				
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
	
	public static void createEquipmentHolder(String id_EquipmentHolder, OKCoUploader repository) throws Exception
	{
		String individualURI = repository.getNamespace()+id_EquipmentHolder;
		if(!QueryUtil.individualExists(repository.getBaseModel(), individualURI)){
			String classURI = repository.getNamespace()+ConceptEnum.Equipment_Holder.toString();
			FactoryUtil.createInstanceIndividual(repository.getBaseModel(), individualURI, classURI);
		}
	}
}
