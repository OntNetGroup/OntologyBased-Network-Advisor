package br.com.padtec.nopen.studio.model;

import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.service.NOpenLog;

public class StudioGeneralFactory {
	
	//=============================================================================================
	// General: Element
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
		
		if(elemType.compareToIgnoreCase("TTF")==0 || elemType.compareToIgnoreCase("AF")==0) StudioSpecificFactory.createTransportFunction(dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("IN")==0 || elemType.compareToIgnoreCase("OUT")==0)  StudioSpecificFactory.createPort(dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("LAYER")==0)  StudioSpecificFactory.insertContainer(dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("CARD")==0)  StudioSpecificFactory.createEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SLOT")==0) StudioSpecificFactory.createEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SUBSLOT")==0)  StudioSpecificFactory.createEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SHELF")==0)  StudioSpecificFactory.createEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("RACK")==0)  StudioSpecificFactory.createEquipmentholder(dtoElement, dtoContainer);
		
		else if (elemType.compareToIgnoreCase("SUPERVISOR")==0)  StudioSpecificFactory.createSupervisor(dtoElement, dtoContainer);
		
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
		
		if(elemType.compareToIgnoreCase("TTF")==0 || elemType.compareToIgnoreCase("AF")==0)  StudioSpecificFactory.deleteTransportFunction(dtoElement);
		
		else if(elemType.compareToIgnoreCase("IN")==0 || elemType.compareToIgnoreCase("OUT")==0)  StudioSpecificFactory.deletePort(dtoElement);
		
		else if(elemType.compareToIgnoreCase("LAYER")==0)  StudioSpecificFactory.deleteContainer(dtoElement, dtoContainer);
		
		else if(elemType.compareToIgnoreCase("CARD")==0)  StudioSpecificFactory.deleteEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SLOT")==0) StudioSpecificFactory. deleteEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SUBSLOT")==0)  StudioSpecificFactory.deleteEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("SHELF")==0)  StudioSpecificFactory.deleteEquipmentholder(dtoElement, dtoContainer);
		else if(elemType.compareToIgnoreCase("RACK")==0)  StudioSpecificFactory.deleteEquipmentholder(dtoElement, dtoContainer);
		
		else if (elemType.compareToIgnoreCase("SUPERVISOR")==0)  StudioSpecificFactory.deleteSupervisor(dtoElement);
		
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
	public static void createConnection(DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement, DtoJointElement dtoConnection) throws Exception
	{
		String srcType = dtoSourceElement.getType();		
		String srcId = dtoSourceElement.getId();		
		String srcName = dtoSourceElement.getName();
		
		String tgtType = dtoTargetElement.getType();		
		String tgtId = dtoTargetElement.getId();
		String tgtName = dtoTargetElement.getName();
		
		if(srcType.compareToIgnoreCase("TTF")==0 && tgtType.compareToIgnoreCase("AF")==0)  StudioSpecificFactory.createLink(dtoSourceElement, dtoTargetElement, dtoConnection);		
		else if(srcType.compareToIgnoreCase("AF")==0 && tgtType.compareToIgnoreCase("TTF")==0)  StudioSpecificFactory.createLink(dtoSourceElement, dtoTargetElement, dtoConnection);
		
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
		
		if(srcType.compareToIgnoreCase("TTF")==0 && tgtType.compareToIgnoreCase("AF")==0)  StudioSpecificFactory.deleteLink(dtoSourceElement, dtoTargetElement, dtoConnection);		
		else if(srcType.compareToIgnoreCase("AF")==0 && tgtType.compareToIgnoreCase("TTF")==0)  StudioSpecificFactory.deleteLink(dtoSourceElement, dtoTargetElement, dtoConnection);
		
		else {
			NOpenLog.appendLine("Error: Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");
			throw new Exception("Unexpected deletion of Link from "+srcType+"::"+srcName+" to "+tgtType+"::"+tgtName+"");				
		}
	}
	
	//=============================================================================================
	// General: Verification
	//=============================================================================================

	public static String[] elementsWithNoConnection(DtoJointElement dtoCard) 
	{	
		//TODO
		return null;
	}
}
