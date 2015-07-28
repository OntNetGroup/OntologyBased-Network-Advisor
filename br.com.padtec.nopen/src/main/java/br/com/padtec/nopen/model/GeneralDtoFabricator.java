package br.com.padtec.nopen.model;

import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.okco.core.application.OKCoUploader;

public class GeneralDtoFabricator {
	
	//=============================================================================================
	// General: Element
	//=============================================================================================
	
	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void createElement(OKCoUploader repository, DtoJointElement dtoElement, DtoJointElement dtoContainer) throws Exception
	{		
		String elemType = dtoElement.getType();		
		String elemId = dtoElement.getId();
		String elemName = dtoElement.getName();
		
		if(elemType.compareToIgnoreCase("TTF")==0) SpecificDtoFabricator.createTransportFunction(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("AF")==0) SpecificDtoFabricator.createTransportFunction(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("MATRIX")==0) SpecificDtoFabricator.createTransportFunction(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("PHYSICAL_MEDIA")==0) SpecificDtoFabricator.createTransportFunction(repository,dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("IN")==0) SpecificDtoFabricator.createPort(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("OUT")==0)  SpecificDtoFabricator.createPort(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("MATRIX_INPUT")==0) SpecificDtoFabricator.createPort(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("MATRIX_OUTPUT")==0) SpecificDtoFabricator.createPort(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("AF_INPUT")==0) SpecificDtoFabricator.createPort(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("AF_OUTPUT")==0) SpecificDtoFabricator.createPort(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("TTF_INPUT")==0) SpecificDtoFabricator.createPort(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("TTF_OUTPUT")==0) SpecificDtoFabricator.createPort(repository,dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("OUTPUT_CARD")==0)  SpecificDtoFabricator.createOutputCard(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("INPUT_CARD")==0)  SpecificDtoFabricator.createInputCard(repository,dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("CARD")==0)  SpecificDtoFabricator.createEquipmentHolder(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SLOT")==0) SpecificDtoFabricator.createEquipmentHolder(repository, dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SUBSLOT")==0)  SpecificDtoFabricator.createEquipmentHolder(repository, dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SHELF")==0)  SpecificDtoFabricator.createEquipmentHolder(repository,dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("RACK")==0)  SpecificDtoFabricator.createEquipmentHolder(repository, dtoElement, dtoContainer);		
		
		else if (elemType.compareToIgnoreCase("SUPERVISOR")==0)  SpecificDtoFabricator.createSupervisor(repository, dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("CARD_LAYER")==0)  SpecificDtoFabricator.createCardLayer(repository, dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("EQUIPMENT")==0) SpecificDtoFabricator.createEquipment(repository,dtoElement, dtoContainer);
		
		else {
			if(dtoContainer!=null){
				String containerType = dtoContainer.getType();
				String containerId = dtoContainer.getId();
				String containerName = dtoContainer.getName();
				NOpenLog.appendLine("Error: Unexpected creation of element "+elemType+"::"+elemName+" at container "+containerType+"::"+containerName+"");
				throw new Exception("Unexpected creation of element "+elemType+"::"+elemName+" at container "+containerType+"::"+containerName+"");			
			}else{
				NOpenLog.appendLine("Error: Unexpected creation of element "+elemType+"::"+elemName);
				throw new Exception("Unexpected creation of element "+elemType+"::"+elemName);			
			}
		}		
	}
	
	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void deleteElement(OKCoUploader repository, DtoJointElement dtoElement, DtoJointElement dtoContainer) throws Exception
	{		
		String elemType = dtoElement.getType();		
		String elemId = dtoElement.getId();
		String elemName = dtoElement.getName();		
		
		String containerType = dtoContainer.getType();
		String containerId = dtoContainer.getId();
		String containerName = dtoContainer.getName();
		
		if(elemType.compareToIgnoreCase("TTF")==0 || elemType.compareToIgnoreCase("AF")==0)  SpecificDtoFabricator.deleteTransportFunction(repository, dtoElement);
		
		else if(elemType.compareToIgnoreCase("IN")==0 || elemType.compareToIgnoreCase("OUT")==0)  SpecificDtoFabricator.deletePort(repository, dtoElement);
		
		else if(elemType.compareToIgnoreCase("LAYER")==0)  SpecificDtoFabricator.deleteLinkFromCardLayerToLayer(repository, dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("CARD")==0)  SpecificDtoFabricator.deleteEquipmentholder(repository, dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SLOT")==0) SpecificDtoFabricator. deleteEquipmentholder(repository, dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SUBSLOT")==0)  SpecificDtoFabricator.deleteEquipmentholder(repository, dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SHELF")==0)  SpecificDtoFabricator.deleteEquipmentholder(repository, dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("RACK")==0)  SpecificDtoFabricator.deleteEquipmentholder(repository, dtoElement, dtoContainer);
		
		else if (elemType.compareToIgnoreCase("SUPERVISOR")==0)  SpecificDtoFabricator.deleteSupervisor(repository, dtoElement);
		
		else {
			NOpenLog.appendLine("Error: Unexpected deletion of element "+elemType+"::"+elemName+" at container "+containerType+"::"+containerName+"");
			throw new Exception("Unexpected deletion of element "+elemType+"::"+elemName+" at container "+containerType+"::"+containerName+"");			
		}	
		
	}
	
	//=============================================================================================
	// General: Connection
	//=============================================================================================

	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void createConnection(OKCoUploader repository, DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement) throws Exception
	{
		String srcType = dtoSourceElement.getType();		
		String srcId = dtoSourceElement.getId();		
		String srcName = dtoSourceElement.getName();
		
		String tgtType = dtoTargetElement.getType();		
		String tgtId = dtoTargetElement.getId();
		String tgtName = dtoTargetElement.getName();
		
		if(srcType.compareToIgnoreCase("TTF")==0 && tgtType.compareToIgnoreCase("AF")==0)  SpecificDtoFabricator.createLinkBetweenTFs(repository, dtoSourceElement, dtoTargetElement);		
		else if(srcType.compareToIgnoreCase("AF")==0 && tgtType.compareToIgnoreCase("TTF")==0)  SpecificDtoFabricator.createLinkBetweenTFs(repository, dtoSourceElement, dtoTargetElement);
		
		else {
			NOpenLog.appendLine("Error: Unexpected creation of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
			throw new Exception("Unexpected creation of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");				
		}
	}
	
	/**
	 * @author John Guerson
	 */
	@SuppressWarnings("unused")
	public static void deleteConnection(OKCoUploader repository, DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement) throws Exception
	{
		String srcType = dtoSourceElement.getType();		
		String srcId = dtoSourceElement.getId();		
		String srcName = dtoSourceElement.getName();
		
		String tgtType = dtoTargetElement.getType();		
		String tgtId = dtoTargetElement.getId();
		String tgtName = dtoTargetElement.getName();
		
		if(srcType.compareToIgnoreCase("TTF")==0 && tgtType.compareToIgnoreCase("AF")==0)  SpecificDtoFabricator.deleteLinkBetweenTFs(repository, dtoSourceElement, dtoTargetElement);		
		else if(srcType.compareToIgnoreCase("AF")==0 && tgtType.compareToIgnoreCase("TTF")==0)  SpecificDtoFabricator.deleteLinkBetweenTFs(repository, dtoSourceElement, dtoTargetElement);
		
		else {
			NOpenLog.appendLine("Error: Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
			throw new Exception("Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");				
		}
	}		
}
