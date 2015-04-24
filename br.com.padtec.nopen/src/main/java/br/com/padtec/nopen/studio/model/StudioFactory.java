package br.com.padtec.nopen.studio.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.studio.service.StudioComponents;

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
		
		NOpenLog.appendLine("TTF Created: "+ttfId+" at Layer: "+layerId);
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
		
		NOpenLog.appendLine("AF Created: "+afId+" at Card: "+cardId);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteTF(String tfId)
	{
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfId
		);
					
		NOpenLog.appendLine("TF Deleted: "+tfId);
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
		
		NOpenLog.appendLine("IN Created: "+portId+" at TF: "+tfId);
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

		NOpenLog.appendLine("OUT Created: "+portId+" at TF: "+tfId);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deletePort(String portId)
	{
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+portId
		);		
		
		NOpenLog.appendLine("Port Deleted: "+portId);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void insertLayer(String containerId, String cardId) throws Exception 
	{
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+cardId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf3_Card_Layer,
			StudioComponents.studioRepository.getNamespace()+containerId
		);		
		
		NOpenLog.appendLine("Layer Inserted: "+containerId+" at Card: "+cardId);
	}
		
	/**
	 * @author John Guerson
	 */
	public static void deleteLayer(String containerId, String cardId)
	{
		FactoryUtil.deleteObjectProperty(
			StudioComponents.studioRepository.getBaseModel(),
			StudioComponents.studioRepository.getNamespace()+cardId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf3_Card_Layer,
			StudioComponents.studioRepository.getNamespace()+containerId
		);
		
		NOpenLog.appendLine("Layer Removed: "+containerId+" at Card: "+cardId);
	}
	
	//=============================================================================================
	// Transport Function
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 * @author Jordana Salamon
	 */
	@SuppressWarnings("unused")
	public static void canCreateTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer) throws Exception
	{
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
				
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		if(tfType.equals("TTF") && containerType.equals("card"))
		{			
			NOpenLog.appendLine("Error: You cannot create a Trail Transport Function in a Card");
			throw new Exception("You cannot create a Trail Transport Function in a Card");
		}
		
		if(tfType.equals("AF") && containerType.equals("layer")) 
		{ 
			NOpenLog.appendLine("Error: You cannot create an Adaptation Function in a Layer");
			throw new Exception("You cannot create an Adaptation Function in a Layer");
		};
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer) throws Exception
	{
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
				
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		if(tfType.equals("TTF") && containerType.equals("layer")) 
		{
			createTTF(tfId, containerId);
		}
		else if(tfType.equals("AF") && containerType.equals("card")) 
		{
			createAF(tfId, containerId);
			
		}else{
			
			NOpenLog.appendLine("Error: Unexpected creation of transport function "+tfType+" \""+tfId+"\" within "+containerType+" \""+containerId+"\"");
			throw new Exception("Unexpected creation of transport function "+tfType+" \""+tfId+"\" within "+containerType+" \""+containerId+"\"");			
		}								
	}
		
	/**
	 * @author John Guerson
	 */
	public static void deleteTransportFunction(DtoJointElement dtoTransportFunction) throws Exception 
	{	
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
		
		if(tfType.equals("TTF") || tfType.equals("AF"))
		{
			deleteTF(tfId);			
		}
		else {			
			NOpenLog.appendLine("Error: Unexpected deletion of transport function "+tfType+" \""+tfId);
			throw new Exception("Unexpected deletion of transport function "+tfType+" \""+tfId);	
		}			
	}
	
	//=============================================================================================
	// Port
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
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
		else  if(portType.equals("out")) 
		{
			createOUT(portId, tfId);		
		
		} else {
			
			NOpenLog.appendLine("Error: Unexpected creation of port "+portType+" \""+portId+"\" within "+tfType+" \""+tfId+"\"");
			throw new Exception("Unexpected creation of port "+portType+" \""+portId+"\" within "+tfType+" \""+tfId+"\"");	
		}		
	}

	/**
	 * @author John Guerson
	 */
	public static void deletePort(DtoJointElement dtoPort) throws Exception 
	{				
		String portType = dtoPort.getType();		
		String portId = dtoPort.getId();
		
		if(portType.equals("in") || portType.equals("out"))
		{
			deletePort(portId);
		}		
		else{			
			NOpenLog.appendLine("Error: Unexpected deletion of port "+portType+" \""+portId);
			throw new Exception("Unexpected deletion of port "+portType+" \""+portId);
		}		
	}
	
	//=============================================================================================
	// Container
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void insertContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception 
	{		
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
		
		if(containerType.equals("layer"))
		{
			insertLayer(containerId, cardId);
		}		
		else{			
			NOpenLog.appendLine("Error: Unexpected insertion of container "+containerType+" \""+containerId+"\" within "+cardType+" \""+cardId+"\"");
			throw new Exception("Unexpected insertion of container "+containerType+" \""+containerId+"\" within "+cardType+" \""+cardId+"\"");			
		}
	}

	/**
	 * @author John Guerson
	 */
	public static void deleteContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{	
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
		
		if(containerType.equals("layer"))
		{
			deleteLayer(containerId, cardId);
		}		
		else{
			
			NOpenLog.appendLine("Error: Unexpected deletion of container "+containerType+" \""+containerId+"\" within "+cardType+" \""+cardId+"\"");
			throw new Exception("Unexpected deletion of container "+containerType+" \""+containerId+"\" within "+cardType+" \""+cardId+"\"");			
		}
	}
	
	/**
	 * @author John Guerson
	 */
	public static void changeContainer(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer)  throws Exception
	{	
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();

		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();		
		
		NOpenLog.appendLine("Error: Unexpected change of container "+containerType+" \""+containerId+"\" at "+tfType+" \""+tfId+"\"");
		throw new Exception("Unexpected change of container "+containerType+" \""+containerId+"\" at "+tfType+" \""+tfId+"\"");	
	}

	//=============================================================================================
	// Link
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void deleteLink(DtoJointElement dtoLink)  throws Exception
	{	
		String linkType = dtoLink.getType();
		String linkId = dtoLink.getId();
		
		NOpenLog.appendLine("Error: Unexpected deletion of link "+linkType+" \""+linkId+"\"");
		throw new Exception("Unexpected deletion of link "+linkType+" \""+linkId+"\"");		
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createLink(DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction, DtoJointElement dtoLink) throws Exception
	{	
		String linkType = dtoLink.getType();
		String linkId = dtoLink.getId();
		
		String srcTfType = dtoSourceTFunction.getType();		
		String srcTfId = dtoSourceTFunction.getId();		
		
		String tgtTfType = dtoSourceTFunction.getType();		
		String tgtTfId = dtoSourceTFunction.getId();
		
		NOpenLog.appendLine("Error: Unexpected creation of link "+linkType+" \""+linkId+"\" from "+srcTfType+" \""+srcTfId+"\" "+"to "+tgtTfType+" \""+tgtTfId+"\"");
		throw new Exception("Unexpected creation of link "+linkType+" \""+linkId+"\" from "+srcTfType+" \""+srcTfId+"\" "+"to "+tgtTfType+" \""+tgtTfId+"\"");			
	}

	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void canCreateLink(DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction) throws Exception
	{	
		String srcTfType = dtoSourceTFunction.getType();		
		String srcTfId = dtoSourceTFunction.getId();		
		
		String tgtTfType = dtoSourceTFunction.getType();		
		String tgtTfId = dtoSourceTFunction.getId();
	}		
}
