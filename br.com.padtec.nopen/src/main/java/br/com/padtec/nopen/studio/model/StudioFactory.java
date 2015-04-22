package br.com.padtec.nopen.studio.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;

public class StudioFactory {

	/**
	 * @author John Guerson
	 */
	public static void createTTF(String ttfId, String layerId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Trail_Termination_Function.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.INV_ComponentOf7_Trail_Termination_Function_Layer,
			StudioComponents.studioRepository.getNamespace()+layerId
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createAF(String afId, String cardId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+afId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+cardId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf1_Card_TF_Card_Element,
			StudioComponents.studioRepository.getNamespace()+afId
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createIN(String portId, String tfId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+portId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Input.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf16_Transport_Function_Input,
			StudioComponents.studioRepository.getNamespace()+portId
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createOUT(String portId, String tfId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+portId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Output.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf17_Transport_Function_Output,
			StudioComponents.studioRepository.getNamespace()+portId
		);
	}
	
	/**
	 * @author Jordana Salamon
	 */
	@SuppressWarnings("unused")
	public static void canCreateTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
				
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
						
		if(tfType.equals("TTF") && containerType=="card")
		{			

		}
		
		if(tfType.equals("AF") && containerType=="layer") 
		{ 

		};
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
				
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
		
		if(tfType.equals("TTF") && containerType.equals("layer")) 
		{
			createTTF(tfId, containerId);
		}
		
		if(tfType.equals("AF") && containerType.equals("card")) 
		{
			createAF(tfId, cardId);
		}
				
		System.out.println("\nCreating... ");
		System.out.println("\tTransport Function: \""+tfId+"\"-\""+tfType+"\"");
		System.out.println("\tat Container: \""+containerId+"\"-\""+containerType+"\"");
		System.out.println("\tat Card: \""+cardId+"\"-\""+cardType+"\"");						
	}
		
	public static void deleteTransportFunction(DtoJointElement dtoTransportFunction) throws Exception 
	{	
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
		
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfId
		);
		
		System.out.println("\nDeleting... ");
		System.out.println("\tTransport Function: \""+tfId+"\"-\""+tfType+"\"");		
	}
	
	public static void createPort(DtoJointElement dtoPort, DtoJointElement dtoTransportFunction) throws Exception 
	{
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();		
		
		String portType = dtoPort.getType();		
		String portId = dtoPort.getId();		
		
		if(portType.equals("in")) 
		{
			createIN(portId, tfId);
		}
		
		if(portType.equals("out")) 
		{
			createOUT(portId, tfId);
		}
		
		System.out.println("\nCreating... ");
		System.out.println("\tPort: \""+portId+"\"-\""+portType+"\"");
		System.out.println("\tat Transport Function: \""+tfId+"\"-\""+tfType+"\"");
	}

	public static void deletePort(DtoJointElement dtoPort) throws Exception 
	{				
		String portType = dtoPort.getType();		
		String portId = dtoPort.getId();
		
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+portId
		);
		
		System.out.println("\nDeleting... ");
		System.out.println("\tPort: \""+portId+"\"-\""+portType+"\"");		
	}
	
	public static void insertContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception 
	{		
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
		
		System.out.println("\nInserting... ");
		System.out.println("\tContainer: \""+containerId+"\"-\""+containerType+"\"");
		System.out.println("\tat Card: \""+cardId+"\"-\""+cardType+"\"");
	}

	public static void deleteContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{	
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
		
		System.out.println("\nDeleting... ");
		System.out.println("\tContainer: \""+containerId+"\"-\""+containerType+"\"");
		System.out.println("\tat Card: \""+cardId+"\"-\""+cardType+"\"");
	}
	
	public static void changeContainer(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer, DtoJointElement dtoCard)  throws Exception
	{	
		//TODO
	}

	public static void deleteLink(DtoJointElement dtoLink)  throws Exception
	{	
		String linkType = dtoLink.getType();
		String linkId = dtoLink.getId();
		
		System.out.println("\nDeleting... ");
		System.out.println("\tLink: \""+linkId+"\"-\""+linkType+"\"");		
	}
	
	public static void createLink(DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction, DtoJointElement dtoLink) throws Exception
	{	
		String linkType = dtoLink.getType();
		String linkId = dtoLink.getId();
		
		String srcTfType = dtoSourceTFunction.getType();		
		String srcTfId = dtoSourceTFunction.getId();		
		
		String tgtTfType = dtoSourceTFunction.getType();		
		String tgtTfId = dtoSourceTFunction.getId();
		
		System.out.println("\nCreating... ");
		System.out.println("\tLink: \""+linkId+"\"-\""+linkType+"\"");
		System.out.println("\tfrom Transport Function: \""+srcTfId+"\"-\""+srcTfType+"\"");
		System.out.println("\tto Transport Function: \""+tgtTfId+"\"-\""+tgtTfType+"\"");
	}

	@SuppressWarnings("unused")
	public static void canCreateLink(DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction) throws Exception
	{	
		String srcTfType = dtoSourceTFunction.getType();		
		String srcTfId = dtoSourceTFunction.getId();		
		
		String tgtTfType = dtoSourceTFunction.getType();		
		String tgtTfId = dtoSourceTFunction.getId();
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
