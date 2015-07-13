package br.com.padtec.nopen.service;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.InstanceFabricator;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.jointjs.util.JointUtilManager;

public class EquipmentCloner {

	/** @author John Guerson */
	public static String getSupervisorURI(OKCoUploader repository, String equipmentURI)
	{
		List<String> supervisors = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			equipmentURI, 
			repository.getNamespace()+RelationEnum.INV_supervises_Equipment_Supervisor.toString(), 
			repository.getNamespace()+ConceptEnum.Supervisor.toString()
		);
		if(supervisors.size()>0) return supervisors.get(0);
		else return null;
	}
	
	/** @author John Guerson */
	public static List<String> getCardsURI(OKCoUploader repository, String supervisorURI)
	{
		List<String> cards = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			supervisorURI,
			repository.getNamespace()+RelationEnum.INV_supervises_card_Card_Supervisor.toString(), 
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		return cards;
	}
	
	/** @author John Guerson */
	public static List<String> getSubslotsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> subslots = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Subslot_Card.toString(), 
			repository.getNamespace()+ConceptEnum.Subslot.toString()
		);		
		return subslots;
	}
	
	/** @author John Guerson */
	public static List<String> getSlotsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> slots = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Slot_Card.toString(), 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);		
		return slots;
	}
		
	/** @author John Guerson */
	public static List<String> getSlotsURIFromSubSlot(OKCoUploader repository, String subslotURI)
	{		
		List<String> slots = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			subslotURI,
			repository.getNamespace()+RelationEnum.A_Slot_Subslot.toString(), 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);		
		return slots;
	}
	
	/** @author John Guerson */
	public static List<String> getShelfsURIFromSlot(OKCoUploader repository, String slotURI)
	{		
		List<String> shelfs = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			slotURI,
			repository.getNamespace()+RelationEnum.A_Shelf_Slot.toString(), 
			repository.getNamespace()+ConceptEnum.Shelf.toString()
		);		
		return shelfs;
	}

	/** @author John Guerson */
	public static List<String> getRacksURIFromShelf(OKCoUploader repository, String shelfURI)
	{		
		List<String> racks = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			shelfURI,
			repository.getNamespace()+RelationEnum.A_Rack_Shelf.toString(), 
			repository.getNamespace()+ConceptEnum.Rack.toString()
		);		
		return racks;
	}
	
	/** @author John Guerson */
	public static List<String> getTFsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(), 
			repository.getNamespace()+ConceptEnum.TF_Card_Element.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getPhysicalMediasURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(), 
			repository.getNamespace()+ConceptEnum.Physical_Media.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getMatrixURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(), 
			repository.getNamespace()+ConceptEnum.Matrix.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getAFsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(), 
			repository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getCardLayersURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_CardLayer.toString(), 
			repository.getNamespace()+ConceptEnum.Card_Layer.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static void cloneEquipmentFromJSON(String jsonEquipments, OKCoUploader tgtRepository) throws Exception
	{					
		PElement[] elems = (PElement[]) JointUtilManager.getJavaFromJSON(jsonEquipments, PElement[].class);				
		for(PElement pElem: elems)
		{
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Equipment.toString())==0) {
				InstanceFabricator.createEquipment(tgtRepository, pElem.getId(), pElem.getName());			
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0) {
				InstanceFabricator.createSupervisor(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Card.toString())==0) {
				InstanceFabricator.createCard(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0) {
				InstanceFabricator.createCardLayer(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0) {
				InstanceFabricator.createAF(tgtRepository, pElem.getId(), pElem.getName()); 
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Matrix.toString())==0) {
				InstanceFabricator.createMatrix(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Physical_Media.toString())==0) {
				InstanceFabricator.createPhysicalMedia(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0) {
				InstanceFabricator.createTTF(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Input_Card.toString())==0) {
				InstanceFabricator.createInputCard(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Output_Card.toString())==0) {
				InstanceFabricator.createOutputCard(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Output.toString())==0) {
				InstanceFabricator.createOutput(tgtRepository, pElem.getId(), pElem.getName()); 
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Input.toString())==0) {
				InstanceFabricator.createInput(tgtRepository, pElem.getId(), pElem.getName()); 
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function_Input.toString())==0){
				InstanceFabricator.createAFInput(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function_Output.toString())==0) {
				InstanceFabricator.createAFOutput(tgtRepository, pElem.getId(), pElem.getName()); 
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Matrix_Input.toString())==0) {
				InstanceFabricator.createMatrixInput(tgtRepository, pElem.getId(), pElem.getName()); 
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Matrix_Output.toString())==0) {
				InstanceFabricator.createMatrixOutput(tgtRepository, pElem.getId(), pElem.getName());
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function_Input.toString())==0) {
				InstanceFabricator.createTTFInput(tgtRepository, pElem.getId(), pElem.getName()); 
			}
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function_Output.toString())==0) {
				InstanceFabricator.createTTFOutput(tgtRepository, pElem.getId(), pElem.getName());
			}
		}		
	}
	
	/** @author John Guerson */
	public static void cloneLinksFromJSON(String jsonLinks, OKCoUploader tgtRepository) throws Exception
	{
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink pLink: links)
		{
			/** Card -> Input Card */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Input_Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.createLinkFromCardToInputCard(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());				 
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Input_Card.toString())==0){
				InstanceFabricator.createLinkFromCardToInputCard(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}			
			/** Card -> Output Card */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Output_Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.createLinkFromCardToOutputCard(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Output_Card.toString())==0){
				InstanceFabricator.createLinkFromCardToOutputCard(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}			
			/** Equipment -> Supervisor */
//			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Equipment.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0){
//				InstanceFabricator.createLinkFromSupervisorToEquipment(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
//			}
//			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Equipment.toString())==0){
//				InstanceFabricator.createLinkFromSupervisorToEquipment(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
//			}			
			/** Supervisor -> Card */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.superviseCard(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0){
				InstanceFabricator.superviseCard(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}			
			/** Card -> Card_Layer */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0){
				InstanceFabricator.createLinkFromCardToCardLayer(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.createLinkFromCardToCardLayer(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}						
			/** Card_Layer -> Trail Termination Function */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0){
				InstanceFabricator.createLinkFromCardLayerToTTF(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0){
				InstanceFabricator.createLinkFromCardLayerToTTF(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}			
			/** Card -> Adaptation Function */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0){
				InstanceFabricator.createLinkFromCardToCardElement(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.createLinkFromCardToCardElement(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}			
			/** Card -> Matrix */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Matrix.toString())==0){
				InstanceFabricator.createLinkFromCardToCardElement(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Matrix.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.createLinkFromCardToCardElement(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
			/** Card -> Physical Media */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Physical_Media.toString())==0){
				InstanceFabricator.createLinkFromCardToCardElement(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Physical_Media.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.createLinkFromCardToCardElement(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
		}
	}
	
	/** @author John Guerson */
	public static void cloneEquipment(String equipmentURI, OKCoUploader srcRepository, OKCoUploader tgtRepository) throws Exception
	{		
		/** Equipment */
		InstanceFabricator.createEquipment(tgtRepository, equipmentURI, equipmentURI);
			
		/** Supervisor */
		String supervisorURI = getSupervisorURI(srcRepository, equipmentURI);
		InstanceFabricator.createSupervisor(tgtRepository, supervisorURI, supervisorURI, equipmentURI, equipmentURI);
		
		/** Cards */
		List<String> cards = getCardsURI(srcRepository, supervisorURI);
		for(String cardURI: cards)
		{
			InstanceFabricator.createCardAtSupervisor(tgtRepository, cardURI, cardURI, supervisorURI, supervisorURI);
		}	
				
		List<String> slots = new ArrayList<String>();
		List<String> subslots = new ArrayList<String>();
		List<String> tfs = new ArrayList<String>();
		List<String> layers = new ArrayList<String>();
		
		for(String cardURI: cards)
		{			
			/** Slots */
			slots = getSlotsURIFromCard(srcRepository, cardURI);
			for(String slotURI: slots){
				InstanceFabricator.createSlotForCard(tgtRepository, slotURI, slotURI, cardURI, cardURI);
			}
			
			/** SubSlots */
			subslots = getSubslotsURIFromCard(srcRepository, cardURI);
			for(String subslotURI: subslots){
				InstanceFabricator.createSubSlotForCard(tgtRepository, subslotURI, subslotURI, cardURI, cardURI);
				List<String> slotsList = getSlotsURIFromSubSlot(srcRepository, subslotURI);
				for(String sURI: slotsList){
					InstanceFabricator.createSubSlotAtSlot(tgtRepository, subslotURI, subslotURI, sURI, sURI);
					if(!slots.contains(sURI)) slots.add(sURI);
				}				
			}
			
			/** TF Card Elements */
			tfs = getTFsURIFromCard(srcRepository,cardURI);
			for(String tfURI: tfs){
				List<String> classes = QueryUtil.getClassesURIFromIndividual(srcRepository.getInferredModel(), tfURI);
				for(String classURI: classes){
					/** AF */
					if(classURI.contains(ConceptEnum.Adaptation_Function.toString())) {
						InstanceFabricator.createAFAtCard(tgtRepository, tfURI, tfURI, cardURI, cardURI);
					}
					/** Physical Media */
					if(classURI.contains(ConceptEnum.Physical_Media.toString())) {
						InstanceFabricator.createPhysicalMediaAtCard(tgtRepository, tfURI, tfURI, cardURI, cardURI);
					}
					/** Matrix */
					if(classURI.contains(ConceptEnum.Matrix.toString())) {
						InstanceFabricator.createMatrixAtCard(tgtRepository, tfURI, tfURI, cardURI, cardURI);
					}
				}
			}		
			
			/** Card Layers */
			layers = getCardLayersURIFromCard(srcRepository,cardURI);
			for(String layerURI: layers){
				InstanceFabricator.createLayerAtCard(tgtRepository, layerURI, layerURI, cardURI, cardURI);
			}
		}
		
		/** Shelfs */
		List<String> shelfs = new ArrayList<String>();		
		for(String slotURI: slots)
		{			
			shelfs = getShelfsURIFromSlot(srcRepository, slotURI);			
			for(String shelfURI: shelfs){
				InstanceFabricator.createShelfForSlot(srcRepository, shelfURI, shelfURI, slotURI, slotURI);	
			}			
		}
		
		/** Racks */
		List<String> racks = new ArrayList<String>();		
		for(String shelfURI: shelfs)
		{			
			racks = getRacksURIFromShelf(srcRepository, shelfURI);			
			for(String rackURI: racks){
				InstanceFabricator.createRackForShelf(srcRepository, rackURI, rackURI, shelfURI, shelfURI);	
			}			
		}			
	}
}
