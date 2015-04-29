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
	public static void deleteTF(String tfId)
	{
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+tfId
		);
	}
		
	/**
	 * @author John Guerson
	 */
	public static void createAFOutput(String outputId, String afId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+outputId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Adaptation_Output.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+afId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf15_Adaptation_Function_Adaptation_Output,
			StudioComponents.studioRepository.getNamespace()+outputId
		);		
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createAFInput(String inputId, String afId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+inputId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Adaptation_Input.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+afId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf14_Adaptation_Function_Adaptation_Input,
			StudioComponents.studioRepository.getNamespace()+inputId
		);		
	}
		
	/**
	 * @author John Guerson
	 */
	public static void createTTFOutput(String outputId, String ttfId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+outputId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Trail_Termination_Output.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf20_Trail_Termination_Function_Trail_Termination_Output,
			StudioComponents.studioRepository.getNamespace()+outputId
		);		
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createTTFInput(String inputId, String ttfId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+inputId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Trail_Termination_Input.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf21_Trail_Termination_Function_Trail_Termination_Input,
			StudioComponents.studioRepository.getNamespace()+inputId
		);		
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
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void changeLayerOfTTF(String ttfId, String srcLayerId, String tgtLayerId) throws Exception
	{	
		FactoryUtil.deleteObjectProperty(
			StudioComponents.studioRepository.getBaseModel(),
			StudioComponents.studioRepository.getNamespace()+ttfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.INV_ComponentOf7_Trail_Termination_Function_Layer,
			StudioComponents.studioRepository.getNamespace()+srcLayerId
		);
		
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.INV_ComponentOf7_Trail_Termination_Function_Layer,
			StudioComponents.studioRepository.getNamespace()+tgtLayerId
		);
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void createLinkFromTTFToAF(String ttfId, String afId) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+ttfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.binds_Trail_Termination_Function_Adaptation_Function,
			StudioComponents.studioRepository.getNamespace()+afId
		);
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void createLinkFromAFToTTF(String afId, String ttfId) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+afId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.binds_Adaptation_Function_Trail_Termination_Function,
			StudioComponents.studioRepository.getNamespace()+ttfId
		);
	}
	
	
	public static void deleteLinkFromTTFToAF(String srcTTF, String tgtAF)
	{
		FactoryUtil.deleteObjectProperty(
			StudioComponents.studioRepository.getBaseModel(),
			StudioComponents.studioRepository.getNamespace()+srcTTF, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.binds_Trail_Termination_Function_Adaptation_Function,
			StudioComponents.studioRepository.getNamespace()+tgtAF
		);
	}
	
	public static void deleteLinkFromAFToTTF(String srcAF, String tgtTTF)
	{
		FactoryUtil.deleteObjectProperty(
			StudioComponents.studioRepository.getBaseModel(),
			StudioComponents.studioRepository.getNamespace()+srcAF, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.binds_Adaptation_Function_Trail_Termination_Function,
			StudioComponents.studioRepository.getNamespace()+tgtTTF
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createRack(String rackId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+rackId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Rack.toString()
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createShelf(String shelfId, String rackId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+shelfId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Shelf.toString()
		);


		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+rackId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf9_Rack_Shelf,
			StudioComponents.studioRepository.getNamespace()+shelfId
		);		
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSlot(String slotId, String shelfId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+slotId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Slot.toString()
		);

		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+shelfId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf10_Shelf_Slot,
			StudioComponents.studioRepository.getNamespace()+slotId
		);	
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSubSlot(String subslotId, String slotId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+subslotId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Subslot.toString()
		);

		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+slotId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf11_Slot_Subslot,
			StudioComponents.studioRepository.getNamespace()+subslotId
		);	
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createCardAtSlot(String cardId, String slotId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+cardId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Card.toString()
		);

		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+slotId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf4_Slot_Card,
			StudioComponents.studioRepository.getNamespace()+cardId
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createCardAtSubSlot(String cardId, String subslotId) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+cardId, 
			StudioComponents.studioRepository.getNamespace()+ConceptEnum.Card.toString()
		);

		FactoryUtil.createInstanceRelation(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+subslotId, 
			StudioComponents.studioRepository.getNamespace()+RelationEnum.ComponentOf5_Subslot_Card,
			StudioComponents.studioRepository.getNamespace()+cardId
		);
	}
	
	
	/**
	 * @author John Guerson
	 */
	public static void deleteEquipment(String holderId, String holderType) throws Exception
	{
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+holderId			
		);		
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
		String tfName = dtoTransportFunction.getName();
		
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();
		String containerName = dtoContainer.getName();
		
		if(tfType.equals("TTF") && containerType.equals("card"))
		{			
			NOpenLog.appendLine("Error: A TTF should not be put into a Card (TTF::"+tfName+", Card::"+containerName+")");
			throw new Exception("A TTF should not be put into a Card (TTF::"+tfName+", Card::"+containerName+")");
		}
		
		if(tfType.equals("AF") && containerType.equals("layer")) 
		{ 
			NOpenLog.appendLine("Error: An AF should not be put into a layer (AF::"+tfName+", Layer::"+containerName+")");
			throw new Exception("An AF should not be put into a Layer (AF::"+tfName+", Layer::"+containerName+")");
		};
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createTransportFunction(DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer) throws Exception
	{
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
		String tfName = dtoTransportFunction.getName();
		
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();
		String containerName = dtoContainer.getName();
		
		if(tfType.equals("TTF") && containerType.equals("layer")) 
		{
			createTTF(tfId, containerId);
			NOpenLog.appendLine("TTF Created: "+tfName+" at Layer: "+containerName);			
		}
		else if(tfType.equals("AF") && containerType.equals("card")) 
		{
			createAF(tfId, containerId);
			NOpenLog.appendLine("AF Created: "+tfName+" at Card: "+containerName);			
		}else{
			
			NOpenLog.appendLine("Error: Unexpected creation of Transport Function "+tfType+"::"+tfName+" at "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected creation of Transport Function "+tfType+"::"+tfName+" at "+containerType+"::"+containerName+"");			
		}								
	}
		
	/**
	 * @author John Guerson
	 */
	public static void deleteTransportFunction(DtoJointElement dtoTransportFunction) throws Exception 
	{	
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
		String tfName = dtoTransportFunction.getName();
		
		if(tfType.equals("TTF") || tfType.equals("AF"))
		{
			deleteTF(tfId);			
			NOpenLog.appendLine("TF "+tfType+" Deleted: "+tfName);			
		}
		else {			
			NOpenLog.appendLine("Error: Unexpected deletion of Transport Function "+tfType+"::"+tfName);
			throw new Exception("Unexpected deletion of Transport Function "+tfType+"::"+tfName);	
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
		String tfName = dtoTransportFunction.getName();
		
		String portType = dtoPort.getType();		
		String portId = dtoPort.getId();		
		String portName = dtoPort.getName();
		
		if(portType.equals("in")) 
		{
			if(tfType.equals("AF"))	
			{
				createAFInput(portId, tfId);				
				NOpenLog.appendLine("Adaptation Input Created: "+portName+" at AF: "+tfName);				
			}
			if(tfType.equals("TTF")) 
			{
				createTTFInput(portId, tfId);				
				NOpenLog.appendLine("Trail Termination Input Created: "+portName+" at TTF: "+tfName);
			}
		}
		else if(portType.equals("out")) 
		{
			if(tfType.equals("AF"))
			{
				createAFOutput(portId, tfId);
				NOpenLog.appendLine("Adaptation Output Created: "+portName+" at AF: "+tfName);
			}
			if(tfType.equals("TTF"))
			{
				createTTFOutput(portId, tfId);
				NOpenLog.appendLine("Trail Termination Output Created: "+portName+" at TTF: "+tfName);				
			}
		
		} else {
			
			NOpenLog.appendLine("Error: Unexpected creation of Port "+portType+"::"+portName+" at "+tfType+"::"+tfName+"");
			throw new Exception("Unexpected creation of Port "+portType+"::"+portName+" at "+tfType+"::"+tfName+"");	
		}		
	}

	/**
	 * @author John Guerson
	 */
	public static void deletePort(DtoJointElement dtoPort) throws Exception 
	{				
		String portType = dtoPort.getType();		
		String portId = dtoPort.getId();
		String portName = dtoPort.getName();
		
		if(portType.equals("in") || portType.equals("out"))
		{
			deletePort(portId);
			NOpenLog.appendLine("Port "+portType+"Deleted: "+portName);			
		}		
		else{			
			NOpenLog.appendLine("Error: Unexpected deletion of Port "+portType+"::"+portName);
			throw new Exception("Unexpected deletion of Port "+portType+"::"+portName);
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
		String containerName = dtoContainer.getName();
		
		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
		String cardName = dtoCard.getName();
		
		if(containerType.equals("layer"))
		{
			insertLayer(containerId, cardId);
			NOpenLog.appendLine("Layer Inserted: "+containerId+" at Card: "+cardId);			
		}		
		else{			
			NOpenLog.appendLine("Error: Unexpected insertion of Container "+containerType+"::"+containerName+" at "+cardType+"::"+cardName+"");
			throw new Exception("Unexpected insertion of Container "+containerType+"::"+containerName+" at "+cardType+"::"+cardName+"");			
		}
	}

	/**
	 * @author John Guerson
	 */
	public static void deleteContainer(DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{	
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();
		String containerName = dtoContainer.getName();
		
		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
		String cardName = dtoCard.getName();
		
		if(containerType.equals("layer"))
		{
			deleteLayer(containerId, cardId);
			NOpenLog.appendLine("Layer Removed: "+containerId+" at Card: "+cardId);			
		}		
		else{
			
			NOpenLog.appendLine("Error: Unexpected deletion of Container "+containerType+"::"+containerName+" at "+cardType+"::"+cardName+"");
			throw new Exception("Unexpected deletion of Container "+containerType+"::"+containerName+" at "+cardType+"::"+cardName+"");			
		}
	}
	
	/**
	 * @author John Guerson
	 */
	public static void changeContainer(DtoJointElement dtoTransportFunction, DtoJointElement srcContainer, DtoJointElement tgtContainer, DtoJointElement dtoCard)  throws Exception
	{	
		String srcContainerType = srcContainer.getType();
		String srcContainerId = srcContainer.getId();
		String srcContainerName = srcContainer.getName();
		
		String tgtContainerType = tgtContainer.getType();
		String tgtContainerId = tgtContainer.getId();
		String tgtContainerName = tgtContainer.getName();
		
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();		
		String tfName = dtoTransportFunction.getName();
		
		if(srcContainerType.equals("layer") || tgtContainerType.equals("layer"))
		{
			if(tfType.equals("TTF")) 
			{
				changeLayerOfTTF(tfId, srcContainerId, tgtContainerId);
				NOpenLog.appendLine("TTF's Layer Changed From "+srcContainerName+" to "+tgtContainerName);				
			}
		}		
		else{
			NOpenLog.appendLine("Error: Unexpected change of Container of "+tfType+"::"+tfName+". From "+srcContainerType+"::"+srcContainerName+" to "+tgtContainerType+"::"+tgtContainerName+"");
			throw new Exception("Unexpected change of Container of "+tfType+"::"+tfName+". From "+srcContainerType+"::"+srcContainerName+" to "+tgtContainerType+"::"+tgtContainerName+"");
		}
	}

	//=============================================================================================
	// Link
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void deleteLink(DtoJointElement dtoLink, DtoJointElement srcTFunction, DtoJointElement tgtTFunction)  throws Exception
	{		
		String srcType = srcTFunction.getType();
		String srcId = srcTFunction.getId();
		String srcName = srcTFunction.getName();
		
		String tgtType = tgtTFunction.getType();
		String tgtId = tgtTFunction.getId();
		String tgtName = tgtTFunction.getName();
		
		if(srcType.equals("TTF") && tgtType.equals("AF"))
		{
			deleteLinkFromTTFToAF(srcId, tgtId);
			NOpenLog.appendLine("Link Deleted from TTF to AF: "+srcName+" -> "+tgtName);			
		}
		
		else if(srcType.equals("AF") && tgtType.equals("TTF"))
		{
			deleteLinkFromAFToTTF(srcId, tgtId);
			NOpenLog.appendLine("Link Deleted from AF to TTF: "+srcName+" -> "+tgtName);			
		}
		else{
			NOpenLog.appendLine("Error: Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
			throw new Exception("Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
		}
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createLink(DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction, DtoJointElement dtoLink) throws Exception
	{	
		String srcTfType = dtoSourceTFunction.getType();		
		String srcTfId = dtoSourceTFunction.getId();		
		String srcName = dtoSourceTFunction.getName();
		
		String tgtTfType = dtoTargetTFunction.getType();		
		String tgtTfId = dtoTargetTFunction.getId();
		String tgtName = dtoTargetTFunction.getName();
		
		if(srcTfType.equals("TTF") && tgtTfType.equals("AF"))
		{
			createLinkFromTTFToAF(srcTfId, tgtTfId);
			NOpenLog.appendLine("Link Created from TTF to AF: "+srcName+" -> "+tgtName);			
		}
		
		else if(srcTfType.equals("AF") && tgtTfType.equals("TTF"))
		{
			createLinkFromAFToTTF(srcTfId, tgtTfId);
			NOpenLog.appendLine("Link Created from AD to TTF: "+srcName+" -> "+tgtName);			
		}
		else{
			NOpenLog.appendLine("Error: Unexpected creation of Link from "+srcTfType+"::"+srcName+" to "+tgtTfType+"::"+tgtName+"");
			throw new Exception("Unexpected creation of Link from "+srcTfType+"::"+srcName+" to "+tgtTfType+"::"+tgtName+"");			
		}			
	}

	/**
	 * @author John Guerson
	 */
	public static void canCreateLink(DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction) throws Exception
	{	
		String srcTfType = dtoSourceTFunction.getType();				
		String srcTfName = dtoSourceTFunction.getName();
		
		String tgtTfType = dtoTargetTFunction.getType();		
		String tgtTfName = dtoTargetTFunction.getName();
		
		if(srcTfType.equals("TTF") && tgtTfType.equals("TTF"))
		{
			NOpenLog.appendLine("Error: A TTF cannot be linked to another TTF ("+srcTfName+" -> "+tgtTfName+")");
			throw new Exception("A TTF cannot be linked to another TTF ("+srcTfName+" -> "+tgtTfName+")");
		}
		
		if(srcTfType.equals("AF") && tgtTfType.equals("AF"))
		{
			NOpenLog.appendLine("Error: An AF cannot be linked to another AF  ("+srcTfName+" -> "+tgtTfName+")");
			throw new Exception("An AF cannot be linked to another AF  ("+srcTfName+" -> "+tgtTfName+")");	
		}
	}

	//=============================================================================================
	// Equipment Holder
	//=============================================================================================
	
	public static void insertEquipmentholder(DtoJointElement dtoEquipmentholder, DtoJointElement dtoContainer) throws Exception 
	{
		String holderType = dtoEquipmentholder.getType();		
		String holderId = dtoEquipmentholder.getId();		
		String holderName = dtoEquipmentholder.getName();
		
		String containerType = dtoContainer.getType();		
		String containerId = dtoContainer.getId();	
		String containerName = dtoContainer.getName();
		
		if(holderType.equals("card") && containerType.equals("slot"))
		{
			createCardAtSlot(holderId, containerId);
			NOpenLog.appendLine("Card Created: "+holderName+" at Slot: "+containerName);
		}
		else if(holderType.equals("card") && containerType.equals("subslot"))
		{
			createCardAtSubSlot(holderId, containerId);
			NOpenLog.appendLine("Card Created: "+holderName+" at SubSlot: "+containerName);			
		}		
		else if(holderType.equals("slot") && containerType.equals("shelf"))
		{
			createSlot(holderId, containerId);
			NOpenLog.appendLine("Slot Created: "+holderName+" at Shelf: "+containerName);			
		}
		else if(holderType.equals("subslot") && containerType.equals("slot"))
		{
			createSubSlot(holderId, containerId);
			NOpenLog.appendLine("SubSlot Created: "+holderName+" at Slot: "+containerName);			
		}
		else if(holderType.equals("shelf")  && containerType.equals("rack"))
		{
			createShelf(holderId, containerId);
			NOpenLog.appendLine("Shelf Created: "+holderName+" at Rack: "+containerName);			
		}
		else if(holderType.equals("rack"))
		{
			createRack(holderId);
			NOpenLog.appendLine("Rack Created: "+holderName);			
		}
		else {
			NOpenLog.appendLine("Error: Unexpected creation of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected creation of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
		}
	}

	public static void removeEquipmentholder(DtoJointElement dtoEquipmentholder, DtoJointElement dtoContainer) throws Exception 
	{
		String holderType = dtoEquipmentholder.getType();		
		String holderId = dtoEquipmentholder.getId();		
		String holderName = dtoEquipmentholder.getName();
		
		String containerType = dtoContainer.getType();		
		String containerName = dtoContainer.getName();
		
		if(holderType.equals("card")) {
			deleteEquipment(holderId, holderType);
			NOpenLog.appendLine("Card Deleted: "+holderId);
		}
		else if(holderType.equals("slot"))
		{
			deleteEquipment(holderId, holderType);	
			NOpenLog.appendLine("Slot Deleted: "+holderId);
		}
		else if(holderType.equals("subslot"))
		{
			deleteEquipment(holderId, holderType);	
			NOpenLog.appendLine("SubSlot Deleted: "+holderId);
		}
		else if(holderType.equals("shelf")) 
		{
			deleteEquipment(holderId, holderType);		
			NOpenLog.appendLine("Shelf Deleted: "+holderId);
		}
		else if(holderType.equals("rack"))
		{
			deleteEquipment(holderId, holderType);		
			NOpenLog.appendLine("Rack Deleted: "+holderId);
		}
		else {
			NOpenLog.appendLine("Error: Unexpected deletion of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected deletion of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
		}
	}

	//=============================================================================================
	// Shelf
	//=============================================================================================
	
	public static void removeShelf(DtoJointElement dtoShelf) throws Exception 
	{	
		String holderId = dtoShelf.getId();		
		String holderName = dtoShelf.getName();
		
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+holderId			
		);
			
		NOpenLog.appendLine("Shelf Deleted: "+holderName);	
	}

	//=============================================================================================
	// Slot
	//=============================================================================================
	
	public static void removeSlot(DtoJointElement dtoSlot) throws Exception 
	{				
		String holderId = dtoSlot.getId();		
		String holderName = dtoSlot.getName();		
				
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+holderId			
		);
			
		NOpenLog.appendLine("Slot Deleted: "+holderName);
	}

	//=============================================================================================
	// Card
	//=============================================================================================
	
	public static void removeCard(DtoJointElement dtoCard) 
	{
		String holderId = dtoCard.getId();		
		String holderName = dtoCard.getName();		
				
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+holderId			
		);
			
		NOpenLog.appendLine("Card Deleted: "+holderName);
	}

	//=============================================================================================
	// Supervisor
	//=============================================================================================
	
	public static void removeSupervisor(DtoJointElement dtoSupervisor) 
	{
		String holderId = dtoSupervisor.getId();		
		String holderName = dtoSupervisor.getName();		
				
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+holderId			
		);
			
		NOpenLog.appendLine("Supervisor Deleted: "+holderName);		
	}		
}
