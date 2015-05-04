package br.com.padtec.nopen.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;

public class InstanceFabricator {
	
	//================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void createTTF(OKCoUploader repository, String ttfId, String ttfName, String layerId, String layerName) throws Exception
	{	
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Function.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.INV_ComponentOf7_Trail_Termination_Function_Layer,
			repository.getNamespace()+layerId
		);
		
		NOpenLog.appendLine(repository.getName()+": TTF "+ttfName+" creted at Layer "+layerName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createAF(OKCoUploader repository, String afId, String afName, String cardId, String cardName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.ComponentOf1_Card_TF_Card_Element,
			repository.getNamespace()+afId
		);
		
		NOpenLog.appendLine(repository.getName()+": AF "+afName+" created at Card "+cardName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteTF(OKCoUploader repository, String tfId, String tfName, String tfType)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+tfId
		);
		
		NOpenLog.appendLine(repository.getName()+": TF "+tfType+"-"+tfName+" deleted");
	}
		
	/**
	 * @author John Guerson
	 */
	public static void createAFOutput(OKCoUploader repository, String outputId, String outputName, String afId, String afName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+outputId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Output.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+RelationEnum.ComponentOf15_Adaptation_Function_Adaptation_Output,
			repository.getNamespace()+outputId
		);		
		
		NOpenLog.appendLine(repository.getName()+": AF Output "+outputName+" created at AF: "+afName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createAFInput(OKCoUploader repository, String inputId, String inputName, String afId, String afName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+inputId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Input.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+RelationEnum.ComponentOf14_Adaptation_Function_Adaptation_Input,
			repository.getNamespace()+inputId
		);		
		
		NOpenLog.appendLine(repository.getName()+": AF Input "+inputName+" created at AF: "+afName);
	}
		
	/**
	 * @author John Guerson
	 */
	public static void createTTFOutput(OKCoUploader repository, String outputId, String outputName, String ttfId, String ttfName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+outputId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Output.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.ComponentOf20_Trail_Termination_Function_Trail_Termination_Output,
			repository.getNamespace()+outputId
		);		
		
		NOpenLog.appendLine(repository.getName()+": TTF Output "+outputName+" created at TTF: "+ttfName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createTTFInput(OKCoUploader repository, String inputId, String inputName, String ttfId, String ttfName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+inputId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Input.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.ComponentOf21_Trail_Termination_Function_Trail_Termination_Input,
			repository.getNamespace()+inputId
		);		
		
		NOpenLog.appendLine(repository.getName()+": TTF Input "+inputName+" created at TTF: "+ttfName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deletePort(OKCoUploader repository, String portId, String portName, String portType)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+portId
		);		
		
		NOpenLog.appendLine(repository.getName()+": Port "+portType+"-"+portName+" deleted");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void insertLayerLink(OKCoUploader repository, String layerId, String layerName, String cardId, String cardName) throws Exception 
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.ComponentOf3_Card_Layer,
			repository.getNamespace()+layerId
		);
		
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" inserted at Card: "+cardName);
	}
		
	/**
	 * @author John Guerson
	 */
	public static void removeLayerLink(OKCoUploader repository, String layerId, String layerName, String cardId, String cardName)
	{
		FactoryUtil.deleteObjectProperty(
			repository.getBaseModel(),
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.ComponentOf3_Card_Layer,
			repository.getNamespace()+layerId
		);		
		
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" removed from Card "+cardName);
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void changeLayerOfTTF(OKCoUploader repository, String ttfId, String ttfName, String srcLayerId, String srcLayerName, String tgtLayerId, String tgtLayerName) throws Exception
	{	
		FactoryUtil.deleteObjectProperty(
			repository.getBaseModel(),
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.INV_ComponentOf7_Trail_Termination_Function_Layer,
			repository.getNamespace()+srcLayerId
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.INV_ComponentOf7_Trail_Termination_Function_Layer,
			repository.getNamespace()+tgtLayerId
		);
		
		NOpenLog.appendLine(repository.getName()+": TTF's Layer Changed From "+srcLayerName+" to "+tgtLayerName);
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void createLinkFromTTFToAF(OKCoUploader repository, String ttfId, String ttfName, String afId, String afName) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.binds_Trail_Termination_Function_Adaptation_Function,
			repository.getNamespace()+afId
		);
		
		NOpenLog.appendLine(repository.getName()+": Link created from TTF "+ttfName+" to AF "+afName);
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void createLinkFromAFToTTF(OKCoUploader repository, String afId, String afName, String ttfId, String ttfName) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+RelationEnum.binds_Adaptation_Function_Trail_Termination_Function,
			repository.getNamespace()+ttfId
		);
		
		NOpenLog.appendLine(repository.getName()+": Link created from AF "+afName+" to TTF "+ttfName);
	}	
	
	public static void deleteLinkFromTTFToAF(OKCoUploader repository, String srcTTFId, String srcTTFName, String tgtAFId, String tgtAFName)
	{
		FactoryUtil.deleteObjectProperty(
			repository.getBaseModel(),
			repository.getNamespace()+srcTTFId, 
			repository.getNamespace()+RelationEnum.binds_Trail_Termination_Function_Adaptation_Function,
			repository.getNamespace()+tgtAFId
		);
		
		NOpenLog.appendLine(repository.getName()+": Link Deleted from TTF "+srcTTFName+" to AF "+tgtAFName);
	}
	
	public static void deleteLinkFromAFToTTF(OKCoUploader repository, String srcAFId, String srcAFName, String tgtTTFId, String tgtTTFName)
	{
		FactoryUtil.deleteObjectProperty(
			repository.getBaseModel(),
			repository.getNamespace()+srcAFId, 
			repository.getNamespace()+RelationEnum.binds_Adaptation_Function_Trail_Termination_Function,
			repository.getNamespace()+tgtTTFId
		);
		
		NOpenLog.appendLine(repository.getName()+": Link Deleted from AF "+srcAFName+" to TTF "+tgtTTFName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createRack(OKCoUploader repository, String rackId, String rackName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+rackId, 
			repository.getNamespace()+ConceptEnum.Rack.toString()
		);
		
		NOpenLog.appendLine(repository.getName()+": Rack "+rackName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createShelf(OKCoUploader repository, String shelfId, String shelfName, String rackId, String rackName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+shelfId, 
			repository.getNamespace()+ConceptEnum.Shelf.toString()
		);


		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+rackId, 
			repository.getNamespace()+RelationEnum.ComponentOf9_Rack_Shelf,
			repository.getNamespace()+shelfId
		);		
		
		NOpenLog.appendLine(repository.getName()+": Shelf "+shelfName+" created at Rack: "+rackName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSlot(OKCoUploader repository, String slotId, String slotName, String shelfId, String shelfName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId, 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);

		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+shelfId, 
			repository.getNamespace()+RelationEnum.ComponentOf10_Shelf_Slot,
			repository.getNamespace()+slotId
		);	
		
		NOpenLog.appendLine(repository.getName()+": Slot "+slotName+" created at Shelf: "+shelfName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSubSlot(OKCoUploader repository, String subslotId, String subslotName, String slotId, String slotName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+subslotId, 
			repository.getNamespace()+ConceptEnum.Subslot.toString()
		);

		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId, 
			repository.getNamespace()+RelationEnum.ComponentOf11_Slot_Subslot,
			repository.getNamespace()+subslotId
		);	
		
		NOpenLog.appendLine(repository.getName()+": SubSlot "+subslotName+" created at Slot: "+slotName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createCard(OKCoUploader repository, String cardName) throws Exception
	{		
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardName,
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created");		
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createCardAtSlot(OKCoUploader repository, String cardId, String cardName, String slotId, String slotName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+ConceptEnum.Card.toString()
		);

		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId, 
			repository.getNamespace()+RelationEnum.ComponentOf4_Slot_Card,
			repository.getNamespace()+cardId
		);
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created at Slot: "+slotName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createCardAtSubSlot(OKCoUploader repository, String cardId, String cardName, String subslotId, String subslotName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+ConceptEnum.Card.toString()
		);

		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+subslotId, 
			repository.getNamespace()+RelationEnum.ComponentOf5_Subslot_Card,
			repository.getNamespace()+cardId
		);
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created at SubSlot: "+subslotName);
	}	
	
	/**
	 * @author John Guerson
	 */
	public static void deleteEquipment(OKCoUploader repository, String holderId, String holderName, String holderType) throws Exception
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+holderId			
		);		
		
		NOpenLog.appendLine(repository.getName()+": Equipment Holder "+holderType+"-"+holderName+" deleted");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void insertSupervisor(OKCoUploader repository, String supervisorId, String supervisorName, String holderId, String holderName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+supervisorId, 
			repository.getNamespace()+ConceptEnum.Supervisor.toString()
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+holderId, 
			repository.getNamespace()+RelationEnum.ComponentOf6_Equipment_Holder_Supervisor,
			repository.getNamespace()+supervisorId
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteShelf(OKCoUploader repository, String shelfId, String shelfName)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+shelfId			
		);
			
		NOpenLog.appendLine(repository.getName()+": Shelf "+shelfName+" deleted");	
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteSlot(OKCoUploader repository, String slotId, String slotName)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId			
		);
			
		NOpenLog.appendLine(repository.getName()+": Slot "+slotName+" deleted");	
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteCard(OKCoUploader repository, String cardId, String cardName)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId			
		);
			
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" deleted");	
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSupervisor(OKCoUploader repository, String supervisorId, String supervisorName, String holderId, String holderName) throws Exception
	{
		FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+supervisorId,			 
			repository.getNamespace()+ConceptEnum.Supervisor.toString()			
		);
			
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+holderId,			 
			repository.getNamespace()+RelationEnum.ComponentOf6_Equipment_Holder_Supervisor,
			repository.getNamespace()+supervisorId
		);
		
		NOpenLog.appendLine(repository.getName()+": Supervisor "+supervisorName+" created for Equipment Holder "+holderName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteSupervisor(OKCoUploader repository, String supervisorId, String supervisorName)
	{
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+supervisorId			
		);
			
		NOpenLog.appendLine(repository.getName()+": Supervisor "+supervisorName+" deleted");		
	}
	
	//================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void createTechnology(OKCoUploader repository, String techName) throws Exception
	{
		String indURI = repository.getNamespace()+techName;		
		String techURI = repository.getNamespace()+ConceptEnum.Technology.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI,techURI);
		NOpenLog.appendLine(repository.getName()+": Technology "+techName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteTechnology(OKCoUploader repository, String techName) 
	{		
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+techName);
		NOpenLog.appendLine(repository.getName()+": Technology "+techName+" deleted");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createLayer(OKCoUploader repository, String layerName, String techName) throws Exception
	{
		String indURI = repository.getNamespace()+layerName;		
		String layerURI = repository.getNamespace()+ConceptEnum.Layer.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI, layerURI);
		
		String ind2URI = repository.getNamespace()+techName;
		
		String techToLayerURI = repository.getNamespace()+RelationEnum.ComponentOf8_Technology_Layer.toString();
		
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),ind2URI, techToLayerURI, indURI);
		
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" created for Technology "+techName);
	}	

	/**
	 * @author John Guerson
	 */
	public static void deleteLayer(OKCoUploader repository, String layerName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+layerName);
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" deleted");
	}
		
	/**
	 * @author John Guerson
	 */
	public static void createService(OKCoUploader repository, String serviceName, String layerName, String techName) throws Exception
	{
		String indLayerURI = repository.getNamespace()+layerName;		
		String indServURI = repository.getNamespace()+serviceName;
		String serviceURI = repository.getNamespace()+ConceptEnum.Service.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indServURI, serviceURI);		
		
		//Exception thrown: relation is UNDECLARED. This is weird...
		//FactoryUtil.createInstanceRelation(repository.getBaseModel(),indLayerURI, RelationEnum.implements_Layer_Service.toString(), indServURI);		
		
		NOpenLog.appendLine(repository.getName()+": Service "+serviceName+" created for Layer "+layerName+" and Tech "+techName);
	}	
	
	/**
	 * @author John Guerson
	 */
	public static void deleteService(OKCoUploader repository, String serviceName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+serviceName);
		NOpenLog.appendLine(repository.getName()+": Service "+serviceName+" deleted");
	}
			
	/**
	 * @author John Guerson
	 */
	public static void createEquipmentHolder(String equipHolderId, OKCoUploader repository) throws Exception
	{
		String individualURI = repository.getNamespace()+equipHolderId;
		if(!QueryUtil.individualExists(repository.getBaseModel(), individualURI))
		{
			String classURI = repository.getNamespace()+ConceptEnum.Equipment_Holder.toString();
			FactoryUtil.createInstanceIndividual(repository.getBaseModel(), individualURI, classURI);
			NOpenLog.appendLine(repository.getName()+": Equipment Holder "+equipHolderId+" created");
		}
	}
}
