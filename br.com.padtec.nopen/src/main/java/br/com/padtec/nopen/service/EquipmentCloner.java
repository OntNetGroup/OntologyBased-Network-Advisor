package br.com.padtec.nopen.service;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.InstanceFabricator;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.okco.core.application.OKCoUploader;

public class EquipmentCloner {

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
	
	public static List<String> getCardsURI(OKCoUploader repository, String supervisorURI)
	{
		List<String> cards = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			supervisorURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf_Supervisor_Card.toString(), 
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		return cards;
	}
	
	public static List<String> getSubslotsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> subslots = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf6_Card_Subslot.toString(), 
			repository.getNamespace()+ConceptEnum.Subslot.toString()
		);		
		return subslots;
	}
	
	public static List<String> getSlotsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> slots = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf5_Card_Slot.toString(), 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);		
		return slots;
	}
		
	public static List<String> getSlotsURIFromSubSlot(OKCoUploader repository, String subslotURI)
	{		
		List<String> slots = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			subslotURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf11_Subslot_Slot.toString(), 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);		
		return slots;
	}
	
	public static List<String> getShelfsURIFromSlot(OKCoUploader repository, String slotURI)
	{		
		List<String> shelfs = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			slotURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf10_Slot_Shelf.toString(), 
			repository.getNamespace()+ConceptEnum.Shelf.toString()
		);		
		return shelfs;
	}
	
	public static List<String> getRacksURIFromShelf(OKCoUploader repository, String shelfURI)
	{		
		List<String> racks = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			shelfURI,
			repository.getNamespace()+RelationEnum.INV_ComponentOf9_Shelf_Rack.toString(), 
			repository.getNamespace()+ConceptEnum.Rack.toString()
		);		
		return racks;
	}
		
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
