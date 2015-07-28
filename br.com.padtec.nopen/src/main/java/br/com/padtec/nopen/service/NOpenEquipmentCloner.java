package br.com.padtec.nopen.service;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.GeneralDtoFabricator;
import br.com.padtec.nopen.model.InstanceFabricator;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.jointjs.util.JointUtilManager;

public class NOpenEquipmentCloner {
	
	/** @author John Guerson */
	public static void cloneEquipmentFromJSON(String jsonEquipments, OKCoUploader tgtRepository) throws Exception
	{					
		PElement[] elems = (PElement[]) JointUtilManager.getJavaFromJSON(jsonEquipments, PElement[].class);		
		for(PElement pElem: elems)
		{
			//=============================================================
			//this is the new code for the cloning
			//if an error occur in these part you can go back to the previous code 
			//shown below and commented
			//=============================================================
			DtoJointElement dtoElem = new DtoJointElement();
			dtoElem.setId(pElem.getId());
			dtoElem.setName(pElem.getName());
			dtoElem.setType(pElem.getType().toUpperCase());			
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0)
				dtoElem.setType("TTF");
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function_Input.toString())==0)
				dtoElem.setType("TTF_INPUT");
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function_Output.toString())==0)
				dtoElem.setType("TTF_OUTPUT");
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0)
				dtoElem.setType("AF");
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function_Input.toString())==0)
				dtoElem.setType("AF_INPUT");
			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function_Output.toString())==0)
				dtoElem.setType("AF_OUTPUT");			
			/** generic creation of elements */
			GeneralDtoFabricator.createElement(tgtRepository, dtoElem, null);
			//===============================================================			
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Equipment.toString())==0) {
//				InstanceFabricator.createEquipment(tgtRepository, pElem.getId(), pElem.getName());			
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Rack.toString())==0) {
//				InstanceFabricator.createRack(tgtRepository, pElem.getId(), pElem.getName());			
//			}
//			 if(pElem.getType().compareToIgnoreCase(ConceptEnum.Shelf.toString())==0) {
//				InstanceFabricator.createShelf(tgtRepository, pElem.getId(), pElem.getName());			
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0) {
//				InstanceFabricator.createSlot(tgtRepository, pElem.getId(), pElem.getName());			
//			}			
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0) {
//				InstanceFabricator.createSupervisor(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Card.toString())==0) {
//				InstanceFabricator.createCard(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0) {
//				InstanceFabricator.createCardLayer(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Input_Card.toString())==0) {
//				InstanceFabricator.createInputCard(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Output_Card.toString())==0) {
//				InstanceFabricator.createOutputCard(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Output.toString())==0) {
//				InstanceFabricator.createOutput(tgtRepository, pElem.getId(), pElem.getName()); 
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Input.toString())==0) {
//				InstanceFabricator.createInput(tgtRepository, pElem.getId(), pElem.getName()); 
//			}						
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0) {
//				InstanceFabricator.createTTF(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0) {
//				InstanceFabricator.createAF(tgtRepository, pElem.getId(), pElem.getName()); 
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Matrix.toString())==0) {
//				InstanceFabricator.createMatrix(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Physical_Media.toString())==0) {
//				InstanceFabricator.createPhysicalMedia(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function_Input.toString())==0) {
//				InstanceFabricator.createTTFInput(tgtRepository, pElem.getId(), pElem.getName()); 
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function_Output.toString())==0) {
//				InstanceFabricator.createTTFOutput(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function_Input.toString())==0){
//				InstanceFabricator.createAFInput(tgtRepository, pElem.getId(), pElem.getName());
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Adaptation_Function_Output.toString())==0) {
//				InstanceFabricator.createAFOutput(tgtRepository, pElem.getId(), pElem.getName()); 
//			}
//			
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Matrix_Input.toString())==0) {
//				InstanceFabricator.createMatrixInput(tgtRepository, pElem.getId(), pElem.getName()); 
//			}
//			if(pElem.getType().compareToIgnoreCase(ConceptEnum.Matrix_Output.toString())==0) {
//				InstanceFabricator.createMatrixOutput(tgtRepository, pElem.getId(), pElem.getName());
//			}			
		}		
	}
	
	/** @author John Guerson */
	public static void cloneLinksFromJSON(String jsonLinks, OKCoUploader tgtRepository) throws Exception
	{
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink pLink: links)
		{
			/** Rack -> Shelf */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Rack.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Shelf.toString())==0){
				InstanceFabricator.createLinkFromRackToShelf(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());				 
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Shelf.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Rack.toString())==0){
				InstanceFabricator.createLinkFromRackToShelf(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}	
			/** Shelf -> Slot */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Shelf.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0){
				InstanceFabricator.createLinkFromShelfToSlot(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());				 
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Shelf.toString())==0){
				InstanceFabricator.createLinkFromShelfToSlot(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
			/** Slot -> Subslot */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Subslot.toString())==0){
				InstanceFabricator.createLinkFromSlotToSubSlot(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());				 
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Subslot.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0){
				InstanceFabricator.createLinkFromSlotToSubSlot(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
			/** Slot -> Card */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.createLinkFromSlotToCard(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());				 
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0){
				InstanceFabricator.createLinkFromSlotToCard(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
			/** Subslot -> Card */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Subslot.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0){
				InstanceFabricator.createLinkFromSubSlotToCard(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());				 
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Subslot.toString())==0){
				InstanceFabricator.createLinkFromSubSlotToCard(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
			/** Slot -> Supervisor */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0){
				InstanceFabricator.createLinkFromSlotToSupervisor(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());				 
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Slot.toString())==0){
				InstanceFabricator.createLinkFromSlotToSupervisor(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
			/** Subslot -> Supervisor */
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Subslot.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0){
				InstanceFabricator.createLinkFromSubSlotToSupervisor(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());				 
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Subslot.toString())==0){
				InstanceFabricator.createLinkFromSubSlotToSupervisor(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
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
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Equipment.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0){
				InstanceFabricator.createLinkFromSupervisorToEquipment(tgtRepository, pLink.getTarget(), pLink.getTarget(), pLink.getSource(), pLink.getSource());
			}
			if(pLink.getSourceType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0 && pLink.getTargetType().compareToIgnoreCase(ConceptEnum.Equipment.toString())==0){
				InstanceFabricator.createLinkFromSupervisorToEquipment(tgtRepository, pLink.getSource(), pLink.getSource(), pLink.getTarget(), pLink.getTarget());
			}			
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
		String supervisorURI = NOpenQueryUtil.getSupervisorURI(srcRepository, equipmentURI);
		InstanceFabricator.createSupervisorForEquipment(tgtRepository, supervisorURI, supervisorURI, equipmentURI, equipmentURI);
		
		/** Cards */
		List<String> cards = NOpenQueryUtil.getCardsURI(srcRepository, supervisorURI);
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
			slots = NOpenQueryUtil.getSlotsURIFromCard(srcRepository, cardURI);
			for(String slotURI: slots){
				InstanceFabricator.createSlotForCard(tgtRepository, slotURI, slotURI, cardURI, cardURI);
			}
			
			/** SubSlots */
			subslots = NOpenQueryUtil.getSubslotsURIFromCard(srcRepository, cardURI);
			for(String subslotURI: subslots){
				InstanceFabricator.createSubSlotForCard(tgtRepository, subslotURI, subslotURI, cardURI, cardURI);
				List<String> slotsList = NOpenQueryUtil.getSlotsURIFromSubSlot(srcRepository, subslotURI);
				for(String sURI: slotsList){
					InstanceFabricator.createSubSlotAtSlot(tgtRepository, subslotURI, subslotURI, sURI, sURI);
					if(!slots.contains(sURI)) slots.add(sURI);
				}				
			}
			
			/** TF Card Elements */
			tfs = NOpenQueryUtil.getTFsURIFromCard(srcRepository,cardURI);
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
			layers = NOpenQueryUtil.getCardLayersURIFromCard(srcRepository,cardURI);
			for(String layerURI: layers){
				InstanceFabricator.createLayerAtCard(tgtRepository, layerURI, layerURI, cardURI, cardURI);
			}
		}
		
		/** Shelfs */
		List<String> shelfs = new ArrayList<String>();		
		for(String slotURI: slots)
		{			
			shelfs = NOpenQueryUtil.getShelfsURIFromSlot(srcRepository, slotURI);			
			for(String shelfURI: shelfs){
				InstanceFabricator.createShelfForSlot(srcRepository, shelfURI, shelfURI, slotURI, slotURI);	
			}			
		}
		
		/** Racks */
		List<String> racks = new ArrayList<String>();		
		for(String shelfURI: shelfs)
		{			
			racks = NOpenQueryUtil.getRacksURIFromShelf(srcRepository, shelfURI);			
			for(String rackURI: racks){
				InstanceFabricator.createRackForShelf(srcRepository, rackURI, rackURI, shelfURI, shelfURI);	
			}			
		}			
	}
}
