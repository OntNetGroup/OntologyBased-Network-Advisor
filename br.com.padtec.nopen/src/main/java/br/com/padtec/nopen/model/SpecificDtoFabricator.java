package br.com.padtec.nopen.model;

import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.okco.core.application.OKCoUploader;

public class SpecificDtoFabricator {
	
	//=============================================================================================
	// Specific: Transport Function
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
		
		if(tfType.compareToIgnoreCase("TTF")==0 && containerType.compareToIgnoreCase("CARD")==0)
		{			
			NOpenLog.appendLine("Error: A TTF should not be put into a Card (TTF::"+tfName+", Card::"+containerName+")");
			throw new Exception("A TTF should not be put into a Card (TTF::"+tfName+", Card::"+containerName+")");
		}
		
		if(tfType.compareToIgnoreCase("AF")==0 && containerType.compareToIgnoreCase("LAYER")==0) 
		{ 
			NOpenLog.appendLine("Error: An AF should not be put into a layer (AF::"+tfName+", Layer::"+containerName+")");
			throw new Exception("An AF should not be put into a Layer (AF::"+tfName+", Layer::"+containerName+")");
		};
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createTransportFunction(OKCoUploader repository, DtoJointElement dtoTransportFunction, DtoJointElement dtoContainer) throws Exception
	{
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
		String tfName = dtoTransportFunction.getName();
		
		if(dtoContainer!=null){
			
			String containerType = dtoContainer.getType();
			String containerId = dtoContainer.getId();
			String containerName = dtoContainer.getName();		
			
			if(tfType.compareToIgnoreCase("TTF")==0 && containerType.compareToIgnoreCase("LAYER")==0){
				InstanceFabricator.createTTFAtLayer(repository, tfId, tfName, containerId, containerName);						
			}
			else if(tfType.compareToIgnoreCase("AF")==0 && containerType.compareToIgnoreCase("CARD")==0) {
				InstanceFabricator.createAFAtCard(repository,tfId, tfName, containerId, containerName);				
			} 
			else if(tfType.compareToIgnoreCase("MATRIX")==0 && containerType.compareToIgnoreCase("CARD")==0) {
				InstanceFabricator.createMatrixAtCard(repository,tfId, tfName, containerId, containerName);
			}
			else if(tfType.compareToIgnoreCase("PHYSICAL_MEDIA")==0 && containerType.compareToIgnoreCase("CARD")==0){
				InstanceFabricator.createMatrixAtCard(repository,tfId, tfName, containerId, containerName);
			}else{			
				NOpenLog.appendLine("Error: Unexpected creation of Transport Function "+tfType+"::"+tfName+" at "+containerType+"::"+containerName+"");
				throw new Exception("Unexpected creation of Transport Function "+tfType+"::"+tfName+" at "+containerType+"::"+containerName+"");			
			}
		}else{
			if(tfType.compareToIgnoreCase("TTF")==0) {
				InstanceFabricator.createTTF(repository, tfId, tfName);						
			} else if(tfType.compareToIgnoreCase("AF")==0){			
				InstanceFabricator.createAF(repository,tfId, tfName);
			} else if(tfType.compareToIgnoreCase("MATRIX")==0){			
				InstanceFabricator.createMatrix(repository,tfId, tfName);
			} else if(tfType.compareToIgnoreCase("PHYSICAL_MEDIA")==0){			
				InstanceFabricator.createPhysicalMedia(repository,tfId, tfName);
			}else{			
				NOpenLog.appendLine("Error: Unexpected creation of Transport Function "+tfType+"::"+tfName);
				throw new Exception("Unexpected creation of Transport Function "+tfType+"::"+tfName);			
			}
		}
	}
		
	/**
	 * @author John Guerson
	 */
	public static void deleteTransportFunction(OKCoUploader repository, DtoJointElement dtoTransportFunction) throws Exception 
	{	
		String tfType = dtoTransportFunction.getType();		
		String tfId = dtoTransportFunction.getId();
		String tfName = dtoTransportFunction.getName();
		
		if(tfType.compareToIgnoreCase("TTF")==0 || tfType.compareToIgnoreCase("AF")==0)
		{
			InstanceFabricator.deleteTF(repository,tfId, tfName, tfType);						
		}
		else {			
			NOpenLog.appendLine("Error: Unexpected deletion of Transport Function "+tfType+"::"+tfName);
			throw new Exception("Unexpected deletion of Transport Function "+tfType+"::"+tfName);	
		}			
	}
	
	public static void setTransportFunctionName(DtoJointElement dtoTransportFunction) 
	{
		//?
	}
	
	//=============================================================================================
	// Specific: Port
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void createPort(OKCoUploader repository, DtoJointElement dtoPort, DtoJointElement dtoTransportFunction) throws Exception 
	{	
		String portType = dtoPort.getType();		
		String portId = dtoPort.getId();		
		String portName = dtoPort.getName();
	
		if(dtoTransportFunction!=null)
		{
			String tfType = dtoTransportFunction.getType();		
			String tfId = dtoTransportFunction.getId();		
			String tfName = dtoTransportFunction.getName();
			
			if(portType.compareToIgnoreCase("IN")==0){
				if(tfType.compareToIgnoreCase("AF")==0){
					InstanceFabricator.createAFInputAtAF(repository,portId, portName, tfId, tfName);								
				}else if(tfType.compareToIgnoreCase("TTF")==0){
					InstanceFabricator.createTTFInputAtTTF(repository,portId, portName, tfId, tfName);				
				}else if(tfType.compareToIgnoreCase("MATRIX")==0){
					InstanceFabricator.createMatrixInputAtMatrix(repository,portId, portName, tfId, tfName);				
				}else{
					InstanceFabricator.createInput(repository, portId, portName);					
				}
			}
			else if(portType.compareToIgnoreCase("OUT")==0) {
				if(tfType.compareToIgnoreCase("AF")==0) {
					InstanceFabricator.createAFOutput(repository,portId, portName, tfId, tfName);				
				} else if(tfType.compareToIgnoreCase("TTF")==0){
					InstanceFabricator.createTTFOutput(repository,portId, portName, tfId, tfName);				
				} else if(tfType.compareToIgnoreCase("MATRIX")==0){
					InstanceFabricator.createMatrixOutputAtMatrix(repository,portId, portName, tfId, tfName);
				}else{
					InstanceFabricator.createOutput(repository, portId, portName);					
				}
			}else{				
				NOpenLog.appendLine("Error: Unexpected creation of Port "+portType+"::"+portName+" at "+tfType+"::"+tfName+"");
				throw new Exception("Unexpected creation of Port "+portType+"::"+portName+" at "+tfType+"::"+tfName+"");	
			}		
		}else{
			if(portType.compareToIgnoreCase("IN")==0) {				
				InstanceFabricator.createInput(repository,portId, portName);			
			} else if(portType.compareToIgnoreCase("OUT")==0) {				
				InstanceFabricator.createOutput(repository,portId, portName);
			}else if(portType.compareToIgnoreCase("MATRIX_INPUT")==0) {				
				InstanceFabricator.createMatrixInput(repository,portId, portName);			
			}else if(portType.compareToIgnoreCase("MATRIX_OUTPUT")==0) {				
				InstanceFabricator.createMatrixOutput(repository,portId, portName);
			}else if(portType.compareToIgnoreCase("AF_OUTPUT")==0) {				
				InstanceFabricator.createAFOutput(repository,portId, portName);
			}else if(portType.compareToIgnoreCase("AF_INPUT")==0) {				
				InstanceFabricator.createAFInput(repository,portId, portName);
			}else if(portType.compareToIgnoreCase("TTF_INPUT")==0) {				
				InstanceFabricator.createTTFInput(repository,portId, portName);
			}else if(portType.compareToIgnoreCase("TTF_OUTPUT")==0) {				
				InstanceFabricator.createTTFOutput(repository,portId, portName);
			}else{				
				NOpenLog.appendLine("Error: Unexpected creation of Port "+portType+"::"+portName);
				throw new Exception("Unexpected creation of Port "+portType+"::"+portName);	
			}
		}
	}

	/**
	 * @author John Guerson
	 */
	public static void deletePort(OKCoUploader repository, DtoJointElement dtoPort) throws Exception 
	{				
		String portType = dtoPort.getType();		
		String portId = dtoPort.getId();
		String portName = dtoPort.getName();
		
		if(portType.compareToIgnoreCase("IN")==0 || portType.compareToIgnoreCase("OUT")==0)
		{
			InstanceFabricator.deletePort(repository,portId, portName, portType);						
		}		
		else{			
			NOpenLog.appendLine("Error: Unexpected deletion of Port "+portType+"::"+portName);
			throw new Exception("Unexpected deletion of Port "+portType+"::"+portName);
		}		
	}
	
	public static void setPortName(DtoJointElement dtoPort) 
	{
		//?
	}	
	
	//=============================================================================================
	// Specific: Card Layer
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void linkCardLayerToCard(OKCoUploader repository, DtoJointElement dtoCardLayer, DtoJointElement dtoCard) throws Exception 
	{			
		if(dtoCard!=null && dtoCardLayer!=null){
			String containerType = dtoCardLayer.getType();
			String containerId = dtoCardLayer.getId();
			String containerName = dtoCardLayer.getName();
			
			String cardId = dtoCard.getId();
			String cardName = dtoCard.getName();
		
			if(containerType.compareToIgnoreCase("CARD_LAYER")==0 || containerType.compareToIgnoreCase("LAYER")==0) {
				InstanceFabricator.createLinkFromCardToCardLayer(repository,containerId, containerName, cardId, cardName);						
			} else{
				NOpenLog.appendLine("Error: Unexpected linking of Card Layer "+containerName+" at Card "+cardName);
				throw new Exception("Unexpected linking of Card Layer "+containerName+" at Card "+cardName);			
			}
		}
	}

	/**
	 * @author John Guerson
	 */
	public static void createCardLayer(OKCoUploader repository, DtoJointElement dtoCardLayer, DtoJointElement dtoCard) throws Exception 
	{	
		String containerId = dtoCardLayer.getId();
		String containerName = dtoCardLayer.getName();
		
		if(dtoCard!=null){
			
			String cardId = dtoCard.getId();
			String cardName = dtoCard.getName();
			
			InstanceFabricator.createCardLayerAtCard(repository, containerId, containerName, cardId, cardName);			
		} else{			
			InstanceFabricator.createCardLayer(repository, containerId, containerName);
		}
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteLinkFromCardLayerToLayer(OKCoUploader repository, DtoJointElement dtoContainer, DtoJointElement dtoCard) throws Exception
	{	
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();
		String containerName = dtoContainer.getName();
		
		String cardType = dtoCard.getType();
		String cardId = dtoCard.getId();
		String cardName = dtoCard.getName();
		
		if(containerType.compareToIgnoreCase("LAYER")==0 || containerType.compareToIgnoreCase("CARD_LAYER")==0)
		{
			InstanceFabricator.deleteLinkFromCardToCardLayer(repository,containerId, containerName, cardId, cardName);						
		}		
		else{
			
			NOpenLog.appendLine("Error: Unexpected deletion of Container "+containerType+"::"+containerName+" at "+cardType+"::"+cardName+"");
			throw new Exception("Unexpected deletion of Container "+containerType+"::"+containerName+" at "+cardType+"::"+cardName+"");			
		}
	}
	
	/**
	 * @author John Guerson
	 */
	public static void changeLayerOfTTF(OKCoUploader repository, DtoJointElement dtoTransportFunction, DtoJointElement srcContainer, DtoJointElement tgtContainer, DtoJointElement dtoCard)  throws Exception
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
		
		if(srcContainerType.compareToIgnoreCase("LAYER")==0 || tgtContainerType.compareToIgnoreCase("LAYER")==0)
		{
			if(tfType.compareToIgnoreCase("TTF")==0) 
			{
				InstanceFabricator.changeLayerOfTTF(repository,tfId, tfName, srcContainerId, srcContainerName, tgtContainerId, tgtContainerName);								
			}
		}		
		else{
			NOpenLog.appendLine("Error: Unexpected change of Container of "+tfType+"::"+tfName+". From "+srcContainerType+"::"+srcContainerName+" to "+tgtContainerType+"::"+tgtContainerName+"");
			throw new Exception("Unexpected change of Container of "+tfType+"::"+tfName+". From "+srcContainerType+"::"+srcContainerName+" to "+tgtContainerType+"::"+tgtContainerName+"");
		}
	}

	//=============================================================================================
	// Specific: Equipment Holder
	//=============================================================================================
	
	public static void createEquipmentHolder(OKCoUploader repository, DtoJointElement dtoEquipmentholder, DtoJointElement dtoContainer) throws Exception 
	{
		String holderType = dtoEquipmentholder.getType();		
		String holderId = dtoEquipmentholder.getId();		
		String holderName = dtoEquipmentholder.getName();
		
		if(dtoContainer!=null)
		{
			String containerType = dtoContainer.getType();		
			String containerId = dtoContainer.getId();	
			String containerName = dtoContainer.getName();
			
			if(holderType.compareToIgnoreCase("CARD")==0 && containerType.compareToIgnoreCase("SLOT")==0){
				InstanceFabricator.createCardAtSlot(repository,holderId, holderName, containerId, containerName);			
			}
			else if(holderType.compareToIgnoreCase("CARD")==0 && containerType.compareToIgnoreCase("SUBSLOT")==0){
				InstanceFabricator.createCardAtSubSlot(repository,holderId, holderName, containerId, containerName);			
			}		
			else if(holderType.compareToIgnoreCase("SLOT")==0 && containerType.compareToIgnoreCase("SHELF")==0){
				InstanceFabricator.createSlotAtShelf(repository,holderId, holderName, containerId, containerName);						
			}
			else if(holderType.compareToIgnoreCase("SUBSLOT")==0 && containerType.compareToIgnoreCase("SLOT")==0){
				InstanceFabricator.createSubSlotAtSlot(repository,holderId, holderName, containerId, containerName);						
			}
			else if(holderType.compareToIgnoreCase("SHELF")==0  && containerType.compareToIgnoreCase("RACK")==0){
				InstanceFabricator.createShelfAtRack(repository,holderId, holderName, containerId, containerName);						
			}
			else if(holderType.compareToIgnoreCase("RACK")==0){
				InstanceFabricator.createRack(repository,holderId, holderName);						
			}
			else {
				NOpenLog.appendLine("Error: Unexpected creation of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
				throw new Exception("Unexpected creation of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
			}
		}else{
			if(holderType.compareToIgnoreCase("CARD")==0){
				InstanceFabricator.createCard(repository,holderId, holderName);			
			}
			else if(holderType.compareToIgnoreCase("SLOT")==0){
				InstanceFabricator.createSlot(repository,holderId, holderName);						
			}
			else if(holderType.compareToIgnoreCase("SUBSLOT")==0){
				InstanceFabricator.createSubSlot(repository,holderId, holderName);						
			}
			else if(holderType.compareToIgnoreCase("SHELF")==0 ){
				InstanceFabricator.createShelf(repository,holderId, holderName);						
			}
			else if(holderType.compareToIgnoreCase("RACK")==0){
				InstanceFabricator.createRack(repository,holderId, holderName);						
			}
			else {
				NOpenLog.appendLine("Error: Unexpected creation of Equipment "+holderType+"::"+holderName);
				throw new Exception("Unexpected creation of Equipment "+holderType+"::"+holderName);
			}
		}
	}

	public static void deleteEquipmentholder(OKCoUploader repository, DtoJointElement dtoEquipmentholder, DtoJointElement dtoContainer) throws Exception 
	{
		String holderType = dtoEquipmentholder.getType();		
		String holderId = dtoEquipmentholder.getId();		
		String holderName = dtoEquipmentholder.getName();
		
		String containerType = dtoContainer.getType();		
		String containerName = dtoContainer.getName();
		
		if(holderType.compareToIgnoreCase("CARD")==0) {
			InstanceFabricator.deleteEquipment(repository,holderId, holderName, holderType);			
		}
		else if(holderType.compareToIgnoreCase("SLOT")==0)
		{
			InstanceFabricator.deleteEquipment(repository,holderId, holderName, holderType);	
		}
		else if(holderType.compareToIgnoreCase("SUBSLOT")==0)
		{
			InstanceFabricator.deleteEquipment(repository,holderId, holderName, holderType);	
		}
		else if(holderType.compareToIgnoreCase("SHELF")==0) 
		{
			InstanceFabricator.deleteEquipment(repository,holderId, holderName, holderType);		
		}
		else if(holderType.compareToIgnoreCase("RACK")==0)
		{
			InstanceFabricator.deleteEquipment(repository,holderId, holderName, holderType);		
		}
		else {
			NOpenLog.appendLine("Error: Unexpected deletion of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected deletion of Equipment "+holderType+"::"+holderName+" at "+containerType+"::"+containerName+"");
		}
	}

	public static void setEquipmentName(DtoJointElement dtoEquipment) 
	{
		//?
	}
	
	//=============================================================================================
	// Specific: Shelf
	//=============================================================================================
	
	public static void deleteShelf(OKCoUploader repository, DtoJointElement dtoShelf) throws Exception 
	{	
		String holderId = dtoShelf.getId();		
		String holderName = dtoShelf.getName();
		
		InstanceFabricator.deleteShelf(repository,holderId,holderName);	
	}

	//=============================================================================================
	// Specific: Slot
	//=============================================================================================
	
	public static void deleteSlot(OKCoUploader repository, DtoJointElement dtoSlot) throws Exception 
	{				
		String holderId = dtoSlot.getId();		
		String holderName = dtoSlot.getName();		
				
		InstanceFabricator.deleteSlot(repository,holderId,holderName);
	}

	//=============================================================================================
	// Specific: Card
	//=============================================================================================
	
	public static void createOutputCard(OKCoUploader repository, DtoJointElement dtoElement, DtoJointElement dtoContainer) throws Exception 
	{			
		String elemId = dtoElement.getId();		
		String elemName = dtoElement.getName();
		
		if(dtoContainer!=null)
		{
			String containerType = dtoContainer.getType();		
			String containerId = dtoContainer.getId();	
			String containerName = dtoContainer.getName();
			
			if(containerType.compareToIgnoreCase("CARD")==0){
				InstanceFabricator.createOutputCardAtCard(repository, elemId, elemName, containerId, containerName);
			}else{
				NOpenLog.appendLine("Error: Unexpected creation of Output Card"+elemName+" at "+containerName+"");
				throw new Exception("Unexpected creation of Output Card"+elemName+" at "+containerName+"");	
			}			
		}else{
			InstanceFabricator.createOutputCard(repository, elemId, elemName);
		}
	}

	public static void createInputCard(OKCoUploader repository, DtoJointElement dtoElement, DtoJointElement dtoContainer) throws Exception 
	{			
		String elemId = dtoElement.getId();		
		String elemName = dtoElement.getName();
		
		if(dtoContainer!=null)
		{
			String containerType = dtoContainer.getType();		
			String containerId = dtoContainer.getId();	
			String containerName = dtoContainer.getName();
			
			if(containerType.compareToIgnoreCase("CARD")==0){
				InstanceFabricator.createInputCardAtCard(repository, elemId, elemName, containerId, containerName);
			}else{
				NOpenLog.appendLine("Error: Unexpected creation of Input Card"+elemName+" at "+containerName+"");
				throw new Exception("Unexpected creation of Input Card"+elemName+" at "+containerName+"");	
			}			
		}else{
			InstanceFabricator.createInputCard(repository, elemId, elemName);
		}
	}
	public static void deleteCard(OKCoUploader repository, DtoJointElement dtoCard) 
	{
		String holderId = dtoCard.getId();		
		String holderName = dtoCard.getName();		
				
		InstanceFabricator.deleteCard(repository,holderId,holderName);
	}
	
	public static String[] elementsWithNoConnection(DtoJointElement dtoCard) 
	{	
		return null;
	}
	
	//=============================================================================================
	// Specific: Equipment
	//=============================================================================================
	
	public static void createEquipment(OKCoUploader repository, DtoJointElement dtoEquipment, DtoJointElement dtoSupervisor) throws Exception
	{
		String equipmentId = dtoEquipment.getId();		
		String equipmentName = dtoEquipment.getName();		
		
		if(dtoSupervisor!=null)
		{
			String supervisorId = dtoSupervisor.getId();		
			String supervisorName = dtoSupervisor.getName();
			
			InstanceFabricator.createEquipmentForSupervisor(repository, equipmentId, equipmentName, supervisorId, supervisorName);			
		}else{
			InstanceFabricator.createEquipment(repository, equipmentId, equipmentName);
		}
	}
	
	//=============================================================================================
	// Specific: Supervisor
	//=============================================================================================
	
	public static void createSupervisor(OKCoUploader repository, DtoJointElement dtoSupervisor, DtoJointElement dtoHolder) throws Exception 
	{
		String supervisorId = dtoSupervisor.getId();		
		String supervisorName = dtoSupervisor.getName();		
		
		if(dtoHolder!=null){
			String holderId = dtoHolder.getId();		
			String holderName = dtoHolder.getName();	
			String holderType = dtoHolder.getType();
			
			if(holderType.compareToIgnoreCase("EQUIPMENT")==0){
				InstanceFabricator.createSupervisorForEquipment(repository, supervisorId, supervisorName, holderId, holderName);				
			}else if(holderType.compareToIgnoreCase("SLOT")==0){
				InstanceFabricator.createSupervisorForSlot(repository, supervisorId, supervisorName, holderId, holderName);			
			}else if(holderType.compareToIgnoreCase("SUBSLOT")==0){
				InstanceFabricator.createSupervisorForSubSlot(repository, supervisorId, supervisorName, holderId, holderName);
			}else if(holderType.compareToIgnoreCase("CARD")==0){
				InstanceFabricator.createSupervisorForCard(repository, supervisorId, supervisorName, holderId, holderName);
			}else{
				NOpenLog.appendLine("Error: Unexpected creation of Supervisor "+supervisorName+"::"+holderName+" at "+holderType+"::"+holderName+"");
				throw new Exception("Unexpected creation of Supervisor "+supervisorName+" at "+holderType+"::"+holderName+"");
			}
		}else{
			InstanceFabricator.createSupervisor(repository, supervisorId, supervisorName);			
		}
	}
	
	public static void deleteSupervisor(OKCoUploader repository, DtoJointElement dtoSupervisor) 
	{
		String supervisorId = dtoSupervisor.getId();		
		String supervisorName = dtoSupervisor.getName();		
				
		InstanceFabricator.deleteSupervisor(repository, supervisorId, supervisorName);
	}
		
	public static void superviseCard(OKCoUploader repository, DtoJointElement dtoSupervisor, DtoJointElement dtoCard) throws Exception 
	{
		String supervisorId = dtoSupervisor.getId();		
		String supervisorName = dtoSupervisor.getName();		
			
		String cardId = dtoCard.getId();		
		String cardName = dtoCard.getName();	
		
		InstanceFabricator.superviseCard(repository, supervisorId, supervisorName, cardId, cardName);
	}
	
	public static void unsuperviseCard(OKCoUploader repository, DtoJointElement dtoSupervisor, DtoJointElement dtoCard)  throws Exception 
	{
		String supervisorId = dtoSupervisor.getId();		
		String supervisorName = dtoSupervisor.getName();		
			
		String cardId = dtoSupervisor.getId();		
		String cardName = dtoSupervisor.getName();	
		
		InstanceFabricator.unsuperviseCard(repository, supervisorId, supervisorName, cardId, cardName);
	}
	
	public static void canSupervise(DtoJointElement dtoSupervisor, DtoJointElement dtoCard) 
	{
		//?
	}

	public static void canUnsupervise(DtoJointElement dtoSupervisor, DtoJointElement dtoCard) 
	{
		//?
	}
	
	public static void setTechnology(DtoJointElement dtoSupervisor, String technology) 
	{
		//?
	}
	
	//=============================================================================================
	// Specific: Link
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void deleteLinkBetweenTFs(OKCoUploader repository, DtoJointElement srcTFunction, DtoJointElement tgtTFunction)  throws Exception
	{		
		String srcType = srcTFunction.getType();
		String srcId = srcTFunction.getId();
		String srcName = srcTFunction.getName();
		
		String tgtType = tgtTFunction.getType();
		String tgtId = tgtTFunction.getId();
		String tgtName = tgtTFunction.getName();
		
		if(srcType.compareToIgnoreCase("TTF")==0 && tgtType.compareToIgnoreCase("AF")==0)
		{
			InstanceFabricator.deleteLinkFromTTFToAF(repository,srcId, srcName, tgtId, tgtName);						
		}
		
		else if(srcType.compareToIgnoreCase("AF")==0 && tgtType.compareToIgnoreCase("TTF")==0)
		{
			InstanceFabricator.deleteLinkFromAFToTTF(repository,srcId, srcName, tgtId, tgtName);						
		}
		else{
			NOpenLog.appendLine("Error: Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
			throw new Exception("Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
		}
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createLinkBetweenTFs(OKCoUploader repository, DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction) throws Exception
	{	
		String srcTfType = dtoSourceTFunction.getType();		
		String srcTfId = dtoSourceTFunction.getId();		
		String srcName = dtoSourceTFunction.getName();
		
		String tgtTfType = dtoTargetTFunction.getType();		
		String tgtTfId = dtoTargetTFunction.getId();
		String tgtName = dtoTargetTFunction.getName();
		
		if(srcTfType.compareToIgnoreCase("TTF")==0 && tgtTfType.compareToIgnoreCase("AF")==0)
		{
			InstanceFabricator.createLinkFromTTFToAF(repository,srcTfId, srcName, tgtTfId, tgtName);						
		}
		
		else if(srcTfType.compareToIgnoreCase("AF")==0 && tgtTfType.compareToIgnoreCase("TTF")==0)
		{
			InstanceFabricator.createLinkFromAFToTTF(repository,srcTfId, srcName, tgtTfId, tgtName);						
		}
		else{
			NOpenLog.appendLine("Error: Unexpected creation of Link from "+srcTfType+"::"+srcName+" to "+tgtTfType+"::"+tgtName+"");
			throw new Exception("Unexpected creation of Link from "+srcTfType+"::"+srcName+" to "+tgtTfType+"::"+tgtName+"");			
		}			
	}

	/**
	 * @author John Guerson
	 */
	public static void canCreateLinkBetweenTFs(OKCoUploader repository, DtoJointElement dtoSourceTFunction, DtoJointElement dtoTargetTFunction) throws Exception
	{	
		String srcTfType = dtoSourceTFunction.getType();				
		String srcTfName = dtoSourceTFunction.getName();
		
		String tgtTfType = dtoTargetTFunction.getType();		
		String tgtTfName = dtoTargetTFunction.getName();
		
		if(srcTfType.compareToIgnoreCase("TTF")==0 && tgtTfType.compareToIgnoreCase("TTF")==0)
		{
			NOpenLog.appendLine("Error: A TTF cannot be linked to another TTF ("+srcTfName+" -> "+tgtTfName+")");
			throw new Exception("A TTF cannot be linked to another TTF ("+srcTfName+" -> "+tgtTfName+")");
		}
		
		if(srcTfType.compareToIgnoreCase("AF")==0 && tgtTfType.compareToIgnoreCase("AF")==0)
		{
			NOpenLog.appendLine("Error: An AF cannot be linked to another AF  ("+srcTfName+" -> "+tgtTfName+")");
			throw new Exception("An AF cannot be linked to another AF  ("+srcTfName+" -> "+tgtTfName+")");	
		}
	}
}
