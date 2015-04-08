package br.com.padtec.nopen.studio.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.common.queries.QueryUtil;


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
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.TTF.toString()
		);
	}
	
	public static boolean createTransportFunction(String tFunctionID,String tFunctionType,String containerName,String containerType, String cardID)
	{
		if(tFunctionType.equals("ttf"))
		{
			if(containerType!=null && containerType.equals("layer"))
			{		
				//create ttf
				createTTF(tFunctionID);

				//create link tFunctionID -> containerName
				FactoryUtil.createInstanceRelation(
					StudioComponents.studioRepository.getBaseModel(), 
					StudioComponents.studioRepository.getNamespace()+tFunctionID, 
					StudioComponents.studioRepository.getNamespace()+RelationEnum.APPLIES.toString(), 
					StudioComponents.studioRepository.getNamespace()+containerName
				);
			}
			
			System.out.println("Card: "+cardID+" - Transport Function \""+tFunctionID+"\":"+tFunctionType+" created at Container \""+containerName+"\":"+containerType);
		}
		if(tFunctionType.equals("af"))
		{
			createAF(tFunctionID);				
			System.out.println("Card: "+cardID+" - Transport Function \""+tFunctionID+"\":"+tFunctionType+" created at Container \""+containerName+"\":"+containerType);
		}
		
		return true;
	}
		
	public static boolean createPort(String portID, String transportFunctionID) 
	{
		return true;
	}

	public static boolean deleteLink(String id) 
	{	
		return true;
	}

	public static boolean insertContainer(String containerName, String containerType, String cardID) 
	{		
		return true;
	}

	public static boolean deleteContainer(String containerName, String containerType, String cardID) 
	{	
		return true;
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
