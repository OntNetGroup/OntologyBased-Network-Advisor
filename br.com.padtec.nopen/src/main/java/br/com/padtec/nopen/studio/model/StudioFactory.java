package br.com.padtec.nopen.studio.model;

import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.InstanceFabricator;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.studio.service.StudioComponents;

public class StudioFactory {
	
	//=============================================================================================
	// General
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void createElement(DtoJointElement dtoElement, DtoJointElement dtoContainer) throws Exception
	{		
		String elemType = dtoElement.getType();		
		String elemId = dtoElement.getId();
		String elemName = dtoElement.getName();
		
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();
		String containerName = dtoContainer.getName();
		
		if(elemType.compareToIgnoreCase("TTF")==0 || elemType.compareToIgnoreCase("AF")==0) createTransportFunction(dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("IN")==0 || elemType.compareToIgnoreCase("OUT")==0) createPort(dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("LAYER")==0) insertContainer(dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("CARD")==0) createEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SLOT")==0)createEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SUBSLOT")==0) createEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SHELF")==0) createEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("RACK")==0) createEquipmentholder(dtoElement, dtoContainer);
		
		else if (elemType.compareToIgnoreCase("SUPERVISOR")==0) createSupervisor(dtoElement, dtoContainer);
		
		else {
			NOpenLog.appendLine("Error: Unexpected creation of element "+elemType+"::"+elemName+" at container "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected creation of element "+elemType+"::"+elemName+" at container "+containerType+"::"+containerName+"");			
		}		
	}
	
	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void deleteElement(DtoJointElement dtoElement, DtoJointElement dtoContainer) throws Exception
	{		
		String elemType = dtoElement.getType();		
		String elemId = dtoElement.getId();
		String elemName = dtoElement.getName();		
		
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();
		String containerName = dtoContainer.getName();
		
		if(elemType.compareToIgnoreCase("TTF")==0 || elemType.compareToIgnoreCase("AF")==0) deleteTransportFunction(dtoElement);
		
		else if(elemType.compareToIgnoreCase("IN")==0 || elemType.compareToIgnoreCase("OUT")==0) deletePort(dtoElement);
		
		else if(elemType.compareToIgnoreCase("LAYER")==0) deleteContainer(dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("CARD")==0) deleteEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SLOT")==0) deleteEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SUBSLOT")==0) deleteEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SHELF")==0) deleteEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("RACK")==0) deleteEquipmentholder(dtoElement, dtoContainer);
		
		else if (elemType.compareToIgnoreCase("SUPERVISOR")==0) deleteSupervisor(dtoElement);
		
		else {
			NOpenLog.appendLine("Error: Unexpected deletion of element "+elemType+"::"+elemName+" at container "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected deletion of element "+elemType+"::"+elemName+" at container "+containerType+"::"+containerName+"");			
		}	
		
	}
	
	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void createConnection(DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement, DtoJointElement dtoConnection) throws Exception
	{
		String srcType = dtoSourceElement.getType();		
		String srcId = dtoSourceElement.getId();		
		String srcName = dtoSourceElement.getName();
		
		String tgtType = dtoTargetElement.getType();		
		String tgtId = dtoTargetElement.getId();
		String tgtName = dtoTargetElement.getName();
		
		if(srcType.compareToIgnoreCase("TTF")==0 && tgtType.compareToIgnoreCase("AF")==0) createLink(dtoSourceElement, dtoTargetElement, dtoConnection);		
		else if(srcType.compareToIgnoreCase("AF")==0 && tgtType.compareToIgnoreCase("TTF")==0) createLink(dtoSourceElement, dtoTargetElement, dtoConnection);
		
		else {
			NOpenLog.appendLine("Error: Unexpected creation of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
			throw new Exception("Unexpected creation of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");				
		}
	}
	
	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void deleteConnection(DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement, DtoJointElement dtoConnection) throws Exception
	{
		String srcType = dtoSourceElement.getType();		
		String srcId = dtoSourceElement.getId();		
		String srcName = dtoSourceElement.getName();
		
		String tgtType = dtoTargetElement.getType();		
		String tgtId = dtoTargetElement.getId();
		String tgtName = dtoTargetElement.getName();
		
		if(srcType.compareToIgnoreCase("TTF")==0 && tgtType.compareToIgnoreCase("AF")==0) deleteLink(dtoSourceElement, dtoTargetElement, dtoConnection);		
		else if(srcType.compareToIgnoreCase("AF")==0 && tgtType.compareToIgnoreCase("TTF")==0) deleteLink(dtoSourceElement, dtoTargetElement, dtoConnection);
		
		else {
			NOpenLog.appendLine("Error: Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
			throw new Exception("Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");				
		}
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
			InstanceFabricator.createTTF(StudioComponents.studioRepository, tfId, tfName, containerId, containerName);						
		}
		else if(tfType.equals("AF") && containerType.equals("card")) 
		{
			InstanceFabricator.createAF(StudioComponents.studioRepository,tfId, tfName, containerId, containerName);						
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
			InstanceFabricator.deleteTF(StudioComponents.studioRepository,tfId, tfName, tfType);						
		}
		else {			
			NOpenLog.appendLine("Error: Unexpected deletion of Transport Function "+tfType+"::"+tfName);
			throw new Exception("Unexpected deletion of Transport Function "+tfType+"::"+tfName);	
		}			
	}
	
	public static void setTransportFunctionName(DtoJointElement dtoTransportFunction) 
	{
		//we won't need this, as it does not make sense.
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
				InstanceFabricator.createAFInput(StudioComponents.studioRepository,portId, portName, tfId, tfName);								
			}
			if(tfType.equals("TTF")) 
			{
				InstanceFabricator.createTTFInput(StudioComponents.studioRepository,portId, portName, tfId, tfName);				
			}
		}
		else if(portType.equals("out")) 
		{
			if(tfType.equals("AF"))
			{
				InstanceFabricator.createAFOutput(StudioComponents.studioRepository,portId, portName, tfId, tfName);				
			}
			if(tfType.equals("TTF"))
			{
				InstanceFabricator.createTTFOutput(StudioComponents.studioRepository,portId, portName, tfId, tfName);								
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
			InstanceFabricator.deletePort(StudioComponents.studioRepository,portId, portName, portType);						
		}		
		else{			
			NOpenLog.appendLine("Error: Unexpected deletion of Port "+portType+"::"+portName);
			throw new Exception("Unexpected deletion of Port "+portType+"::"+portName);
		}		
	}
	
	public static void setPortName(DtoJointElement dtoPort) 
	{
		//we won't need this, as it does not make sense.
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
			InstanceFabricator.insertLayerLink(StudioComponents.studioRepository,containerId, containerName, cardId, cardName);						
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
			InstanceFabricator.removeLayerLink(StudioComponents.studioRepository,containerId, containerName, cardId, cardName);						
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
				InstanceFabricator.changeLayerOfTTF(StudioComponents.studioRepository,tfId, tfName, srcContainerId, srcContainerName, tgtContainerId, tgtContainerName);								
			}
		}		
		else{
			NOpenLog.appendLine("Error: Unexpected change of Container of "+tfType+"::"+tfName+". From "+srcContainerType+"::"+srcContainerName+" to "+tgtContainerType+"::"+tgtContainerName+"");
			throw new Exception("Unexpected change of Container of "+tfType+"::"+tfName+". From "+srcContainerType+"::"+srcContainerName+" to "+tgtContainerType+"::"+tgtContainerName+"");
		}
	}

	//=============================================================================================
	// Equipment Holder
	//=============================================================================================
	
	public static void createEquipmentholder(DtoJointElement dtoEquipmentholder, DtoJointElement dtoContainer) throws Exception 
	{
		String holderType = dtoEquipmentholder.getType();		
		String holderId = dtoEquipmentholder.getId();		
		String holderName = dtoEquipmentholder.getName();
		
		String containerType = dtoContainer.getType();		
		String containerId = dtoContainer.getId();	
		String containerName = dtoContainer.getName();
		
		if(holderType.equals("card") && containerType.equals("slot"))
		{
			InstanceFabricator.createCardAtSlot(StudioComponents.studioRepository,holderId, holderName, containerId, containerName);			
		}
		else if(holderType.equals("card") && containerType.equals("subslot"))
		{
			InstanceFabricator.createCardAtSubSlot(StudioComponents.studioRepository,holderId, holderName, containerId, containerName);			
		}		
		else if(holderType.equals("slot") && containerType.equals("shelf"))
		{
			InstanceFabricator.createSlot(StudioComponents.studioRepository,holderId, holderName, containerId, containerName);						
		}
		else if(holderType.equals("subslot") && containerType.equals("slot"))
		{
			InstanceFabricator.createSubSlot(StudioComponents.studioRepository,holderId, holderName, containerId, containerName);						
		}
		else if(holderType.equals("shelf")  && containerType.equals("rack"))
		{
			InstanceFabricator.createShelf(StudioComponents.studioRepository,holderId, holderName, containerId, containerName);						
		}
		else if(holderType.equals("rack"))
		{
			InstanceFabricator.createRack(StudioComponents.studioRepository,holderId, holderName);						
		}
		else {
			NOpenLog.appendLine("Error: Unexpected creation of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected creation of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
		}
	}

	public static void deleteEquipmentholder(DtoJointElement dtoEquipmentholder, DtoJointElement dtoContainer) throws Exception 
	{
		String holderType = dtoEquipmentholder.getType();		
		String holderId = dtoEquipmentholder.getId();		
		String holderName = dtoEquipmentholder.getName();
		
		String containerType = dtoContainer.getType();		
		String containerName = dtoContainer.getName();
		
		if(holderType.equals("card")) {
			InstanceFabricator.deleteEquipment(StudioComponents.studioRepository,holderId, holderName, holderType);			
		}
		else if(holderType.equals("slot"))
		{
			InstanceFabricator.deleteEquipment(StudioComponents.studioRepository,holderId, holderName, holderType);	
		}
		else if(holderType.equals("subslot"))
		{
			InstanceFabricator.deleteEquipment(StudioComponents.studioRepository,holderId, holderName, holderType);	
		}
		else if(holderType.equals("shelf")) 
		{
			InstanceFabricator.deleteEquipment(StudioComponents.studioRepository,holderId, holderName, holderType);		
		}
		else if(holderType.equals("rack"))
		{
			InstanceFabricator.deleteEquipment(StudioComponents.studioRepository,holderId, holderName, holderType);		
		}
		else {
			NOpenLog.appendLine("Error: Unexpected deletion of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected deletion of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
		}
	}

	public static void setEquipmentName(DtoJointElement dtoEquipment) 
	{
		//we won't need this, as it does not make sense.
	}
	
	//=============================================================================================
	// Shelf
	//=============================================================================================
	
	public static void deleteShelf(DtoJointElement dtoShelf) throws Exception 
	{	
		String holderId = dtoShelf.getId();		
		String holderName = dtoShelf.getName();
		
		InstanceFabricator.deleteShelf(StudioComponents.studioRepository,holderId,holderName);	
	}

	//=============================================================================================
	// Slot
	//=============================================================================================
	
	public static void deleteSlot(DtoJointElement dtoSlot) throws Exception 
	{				
		String holderId = dtoSlot.getId();		
		String holderName = dtoSlot.getName();		
				
		InstanceFabricator.deleteSlot(StudioComponents.studioRepository,holderId,holderName);
	}

	//=============================================================================================
	// Card
	//=============================================================================================
	
	public static void deleteCard(DtoJointElement dtoCard) 
	{
		String holderId = dtoCard.getId();		
		String holderName = dtoCard.getName();		
				
		InstanceFabricator.deleteCard(StudioComponents.studioRepository,holderId,holderName);
	}

	public static String[] elementsWithNoConnection(DtoJointElement dtoCard) 
	{	
		//TODO
		return null;
	}
	
	//=============================================================================================
	// Supervisor
	//=============================================================================================
	
	public static void createSupervisor(DtoJointElement dtoSupervisor, DtoJointElement dtoHolder) throws Exception 
	{
		String supervisorId = dtoSupervisor.getId();		
		String supervisorName = dtoSupervisor.getName();		
			
		String holderId = dtoSupervisor.getId();		
		String holderName = dtoSupervisor.getName();	
		
		InstanceFabricator.createSupervisor(StudioComponents.studioRepository, supervisorId, supervisorName, holderId, holderName);
	}	
	
	public static void deleteSupervisor(DtoJointElement dtoSupervisor) 
	{
		String supervisorId = dtoSupervisor.getId();		
		String supervisorName = dtoSupervisor.getName();		
				
		InstanceFabricator.deleteSupervisor(StudioComponents.studioRepository, supervisorId, supervisorName);
	}
		
	public static void superviseCard(DtoJointElement dtoSupervisor, DtoJointElement dtoCard) 
	{
		//TODO
	}
	
	public static void unsuperviseCard(DtoJointElement dtoSupervisor, DtoJointElement dtoCard) 
	{
		//TODO
	}
	
	public static void canSupervise(DtoJointElement dtoSupervisor,
			DtoJointElement dtoCard) {
		// TODO Auto-generated method stub
		
	}

	public static void canUnsupervise(DtoJointElement dtoSupervisor,
			DtoJointElement dtoCard) {
		// TODO Auto-generated method stub
		
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
			InstanceFabricator.deleteLinkFromTTFToAF(StudioComponents.studioRepository,srcId, srcName, tgtId, tgtName);						
		}
		
		else if(srcType.equals("AF") && tgtType.equals("TTF"))
		{
			InstanceFabricator.deleteLinkFromAFToTTF(StudioComponents.studioRepository,srcId, srcName, tgtId, tgtName);						
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
			InstanceFabricator.createLinkFromTTFToAF(StudioComponents.studioRepository,srcTfId, srcName, tgtTfId, tgtName);						
		}
		
		else if(srcTfType.equals("AF") && tgtTfType.equals("TTF"))
		{
			InstanceFabricator.createLinkFromAFToTTF(StudioComponents.studioRepository,srcTfId, srcName, tgtTfId, tgtName);						
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
}
