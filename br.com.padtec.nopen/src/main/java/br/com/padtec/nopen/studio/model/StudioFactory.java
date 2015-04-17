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
			StudioComponents.studioRepository.getNamespace()+RelationEnum.INV_ComponentOf7_Trail_Termination_Function_Layer,
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
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf1_Card_TF_Card_Element,
			StudioComponents.studioRepository.getNamespace()+afName
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createIN(String portName, String tfName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+portName, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Input.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfName, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf16_Transport_Function_Input,
			StudioComponents.studioRepository.getNamespace()+portName
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createOUT(String portName, String tfName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+portName, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Output.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfName, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf17_Transport_Function_Output,
			StudioComponents.studioRepository.getNamespace()+portName
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
		String tfName = dtoTransportFunction.getName();
				
		String containerType = dtoContainer.getType();
		String containerName = dtoContainer.getName();

		String cardType = dtoCard.getType();
		String cardName = dtoCard.getName();
		
		if(tfType.equals("TTF") && containerType.equals("layer")) 
		{
			createTTF(tfName, containerName);
		}
		
		if(tfType.equals("AF") && containerType.equals("card")) 
		{
			createAF(tfName, cardName);
		}
				
		System.out.println("\nCreating... ");
		System.out.println("\tTransport Function: \""+tfName+"\"-\""+tfType+"\"");
		System.out.println("\tat Container: \""+containerName+"\"-\""+containerType+"\"");
		System.out.println("\tat Card: \""+cardName+"\"-\""+cardType+"\"");						
	}
		
	public static void deleteTransportFunction(DtoJointElement dtoTransportFunction) throws Exception 
	{	
		String tfType = dtoTransportFunction.getType();		
		String tfName = dtoTransportFunction.getName();
		
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfName
		);
		
		System.out.println("\nDeleting... ");
		System.out.println("\tTransport Function: \""+tfName+"\"-\""+tfType+"\"");		
	}
	
	public static void createPort(DtoJointElement dtoPort, DtoJointElement dtoTransportFunction) throws Exception 
	{
		String tfType = dtoTransportFunction.getType();		
		String tfName = dtoTransportFunction.getName();		
		
		String portType = dtoPort.getType();		
		String portName = dtoPort.getName();		
		
		if(portType.equals("in")) 
		{
			createIN(portName, tfName);
		}
		
		if(portType.equals("out")) 
		{
			createOUT(portName, tfName);
		}
		
		System.out.println("\nCreating... ");
		System.out.println("\tPort: \""+portName+"\"-\""+portType+"\"");
		System.out.println("\tat Transport Function: \""+tfName+"\"-\""+tfType+"\"");
	}

	public static void deletePort(DtoJointElement dtoPort) throws Exception 
	{				
		String portType = dtoPort.getType();		
		String portName = dtoPort.getName();
		
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+portName
		);
		
		System.out.println("\nDeleting... ");
		System.out.println("\tPort: \""+portName+"\"-\""+portType+"\"");		
	}
	
	public static void insertContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception 
	{		
		String containerType = dtoContainer.getType();
		String containerName = dtoContainer.getName();

		String cardType = dtoCard.getType();
		String cardName = dtoCard.getName();
		
		System.out.println("\nInserting... ");
		System.out.println("\tContainer: \""+containerName+"\"-\""+containerType+"\"");
		System.out.println("\tat Card: \""+cardName+"\"-\""+cardType+"\"");
	}

	public static void deleteContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{	
		String containerType = dtoContainer.getType();
		String containerName = dtoContainer.getName();

		String cardType = dtoCard.getType();
		String cardName = dtoCard.getName();
		
		System.out.println("\nDeleting... ");
		System.out.println("\tContainer: \""+containerName+"\"-\""+containerType+"\"");
		System.out.println("\tat Card: \""+cardName+"\"-\""+cardType+"\"");
	}
	
	public static void changeContainer(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer, DtoJointElement dtoCard)  throws Exception
	{	
		//TODO
	}

	public static void deleteLink(DtoJointElement dtoLink)  throws Exception
	{	
		String linkType = dtoLink.getType();
		String linkName = dtoLink.getName();
		
		System.out.println("\nDeleting... ");
		System.out.println("\tLink: \""+linkName+"\"-\""+linkType+"\"");		
	}
	
	public static void createLink(DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction, DtoJointElement dtoLink) throws Exception
	{	
		String linkType = dtoLink.getType();
		String linkName = dtoLink.getName();
		
		String srcTfType = dtoSourceTFunction.getType();		
		String srcTfName = dtoSourceTFunction.getName();		
		
		String tgtTfType = dtoSourceTFunction.getType();		
		String tgtTfName = dtoSourceTFunction.getName();
		
		System.out.println("\nCreating... ");
		System.out.println("\tLink: \""+linkName+"\"-\""+linkType+"\"");
		System.out.println("\tfrom Transport Function: \""+srcTfName+"\"-\""+srcTfType+"\"");
		System.out.println("\tto Transport Function: \""+tgtTfName+"\"-\""+tgtTfType+"\"");
	}

	@SuppressWarnings("unused")
	public static void canCreateLink(DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction) throws Exception
	{	
		String srcTfType = dtoSourceTFunction.getType();		
		String srcTfName = dtoSourceTFunction.getName();		
		
		String tgtTfType = dtoSourceTFunction.getType();		
		String tgtTfName = dtoSourceTFunction.getName();
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
