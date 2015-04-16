package br.com.padtec.nopen.studio.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;
import br.com.padtec.nopen.service.util.NOpenFactoryUtil;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;


public class StudioFactory {

	/**
	 * @author John Guerson
	 */
	public static void createTTF(String ttfName, String layerName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfName, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Trail_Termination_Function.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfName, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.applies_Trail_Termination_Function_Layer,
			StudioComponents.studioRepository.getNamespace()+layerName
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createAF(String afName, String cardName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+afName, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+cardName, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf_Card_Transport_Function,
			StudioComponents.studioRepository.getNamespace()+afName
		);
	}
	
	/**
	 * @author Jordana Salamon
	 */
	@SuppressWarnings("unused")
	public static void canCreateTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{
		String tfType = dtoTransportFunction.getType();		
		String tfName = dtoTransportFunction.getName();
				
		String containerType = dtoContainer.getType();
		String containerName = dtoContainer.getName();

		String cardType = dtoCard.getType();
		String cardName = dtoCard.getName();
						
		if(tfType.equals("TTF") && containerType=="card")
		{			
			/** TODO: This connection cannot be done. 
			 *  
			 *  Jordana...
			 *  throw new Execption()...
			 *  
			 **/ 
		}
		
		if(tfType.equals("AF") && containerType=="layer") 
		{ 
			/** TODO: This connection cannot be done. 
			 *  
			 *  Jordana...
			 *  throw new Execption()...
			 *  
			 **/ 
		};
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{
		String tfType = dtoTransportFunction.getType();		
		String tfName = dtoTransportFunction.getName();
				
		String containerType = dtoContainer.getType();
		String containerName = dtoContainer.getName();

		String cardType = dtoCard.getType();
		String cardName = dtoCard.getName();
		
		if(tfType.equals("TTF") && containerType=="layer") 
		{
			createTTF(tfName, containerName);
		}
		
		if(tfType.equals("AF") && containerType=="card") 
		{
			createAF(tfName, cardName);
		}
				
		System.out.println("\nCreating... ");
		System.out.println("\tTransport Function: \""+tfName+"\"-\""+tfType+"\"");
		System.out.println("\tat Container: \""+containerName+"\"-\""+containerType+"\"");
		System.out.println("\tat Card: \""+cardName+"\"-\""+cardType+"\"");						
	}
		
	@SuppressWarnings("unused")
	public static void deleteTransportFunction(DtoJointElement dtoTransportFunction) throws Exception 
	{	
		String tfType = dtoTransportFunction.getType();		
		String tfName = dtoTransportFunction.getName();
		
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfName
		);
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
