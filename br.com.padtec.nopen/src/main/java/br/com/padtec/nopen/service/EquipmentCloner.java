package br.com.padtec.nopen.service;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.InstanceFabricator;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.okco.core.application.OKCoUploader;

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
		List<String> subslots = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf5_Card_Subslot.toString(), 
			repository.getNamespace()+ConceptEnum.Subslot.toString()
		);		
		return subslots;
	}
	
	/** @author John Guerson */
	public static List<String> getSlotsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> slots = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf4_Card_Slot.toString(), 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);		
		return slots;
	}
		
	/** @author John Guerson */
	public static List<String> getSlotsURIFromSubSlot(OKCoUploader repository, String subslotURI)
	{		
		List<String> slots = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			subslotURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf10_Subslot_Slot.toString(), 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);		
		return slots;
	}
	
	/** @author John Guerson */
	public static List<String> getShelfsURIFromSlot(OKCoUploader repository, String slotURI)
	{		
		List<String> shelfs = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			slotURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf9_Slot_Shelf.toString(), 
			repository.getNamespace()+ConceptEnum.Shelf.toString()
		);		
		return shelfs;
	}

	/** @author John Guerson */
	public static List<String> getRacksURIFromShelf(OKCoUploader repository, String shelfURI)
	{		
		List<String> racks = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			shelfURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf8_Shelf_Rack.toString(), 
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
			repository.getNamespace()+RelationEnum.ComponentOf1_Card_TF_Card_Element.toString(), 
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
			repository.getNamespace()+RelationEnum.ComponentOf1_Card_TF_Card_Element.toString(), 
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
			repository.getNamespace()+RelationEnum.ComponentOf1_Card_TF_Card_Element.toString(), 
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
			repository.getNamespace()+RelationEnum.ComponentOf1_Card_TF_Card_Element.toString(), 
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
			repository.getNamespace()+RelationEnum.ComponentOf3_Card_Card_Layer.toString(), 
			repository.getNamespace()+ConceptEnum.Card_Layer.toString()
		);		
		return elems;
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
