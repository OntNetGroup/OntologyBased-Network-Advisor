package br.com.padtec.nopen.studio.equip.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.nopen.studio.model.StudioFactory;
import br.com.padtec.nopen.studio.service.StudioComponents;

import com.jointjs.util.JointUtilManager;

@Controller
public class EquipmentStudioController {

	@RequestMapping("/equipment-studio")
	public String equipmentStudioRequest() 
	{
		return "equipment-studio/equipment-studio";
	}

	/** Procedure to create an Equipment holder inside another equipment or a rack
	 * @param equipmentholder
	 * @param container
	 * @return
	 */
	@RequestMapping(value = "/insertEquipmentholder", method = RequestMethod.POST)
	public @ResponseBody String insertEquipmentholder(@RequestParam("equipmentholder") String equipmentholder,@RequestParam("container") String container )
	{
		DtoJointElement dtoEquipmentholder = (DtoJointElement) JointUtilManager.getJavaFromJSON(equipmentholder, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);

		try{
			StudioFactory.insertEquipmentholder(dtoEquipmentholder, dtoContainer);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";		
	}
	
	/** Procedure to remove an Equipmentholder
	 * @param equipment
	 * @param container
	 * @return
	 */
	@RequestMapping(value = "/removeEquipmentholder", method = RequestMethod.POST)
	public @ResponseBody String removeEquipmentholder(@RequestParam("equipment") String equipment , @RequestParam("container") String container )
	{
		DtoJointElement dtoEquipmentholder = (DtoJointElement) JointUtilManager.getJavaFromJSON(equipment, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);

		try{
			StudioFactory.removeEquipmentholder(dtoEquipmentholder, dtoContainer);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
	
	/** Procedure to select the supervisors technology
	 * @param supervisor
	 * @param technology
	 * @return
	 */
	@RequestMapping(value = "/setTechnology", method = RequestMethod.POST)
	public @ResponseBody String setTechnology(@RequestParam("supervisor") String supervisor,@RequestParam("technology") String technology )
	{
		DtoJointElement dtosupervisor = (DtoJointElement) JointUtilManager.getJavaFromJSON(supervisor, DtoJointElement.class);
		
//		try{
//			StudioFactory.setTechnology(dtosupervisor);
//		}catch(Exception e){
//			e.printStackTrace();
//			return e.getLocalizedMessage();
//		}		
		return "success";		
	}
	
	/** Procedure to get all the technologies
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/getTechnologies", method = RequestMethod.POST)
	public @ResponseBody String[] getTechnologies()
	{   
		return NOpenQueryUtil.getAllTechnologiesNames(StudioComponents.studioRepository.getBaseModel());		
	}
	
	
	/** Procedure to create a Supervisor
	 * @param card
	 * @param slot
	 * @return
	 */
	@RequestMapping(value = "/insertSupervisor", method = RequestMethod.POST)
	public @ResponseBody String insertSupervisor(@RequestParam("supervisor") String supervisor,@RequestParam("slot") String slot )
	{
		DtoJointElement dtoSupervisor = (DtoJointElement) JointUtilManager.getJavaFromJSON(supervisor, DtoJointElement.class);
		DtoJointElement dtoSlot = (DtoJointElement) JointUtilManager.getJavaFromJSON(slot, DtoJointElement.class);
		NOpenQueryUtil.getAllTechnologiesNames(StudioComponents.studioRepository.getBaseModel());
//		try{
//			StudioFactory.insertSupervisor(dtoSupervisor, dtoSlot);
//		}catch(Exception e){
//			e.printStackTrace();
//			return e.getLocalizedMessage();
//		}		
		return "success";		
	}
	
	/** Procedure to remove a supervisor
	 * @param supervisor
	 * @param ?
	 * @return
	 */
	@RequestMapping(value = "/removeSupervisor", method = RequestMethod.POST)
	public @ResponseBody String removeSupervisor(@RequestParam("supervisor") String supervisor)
	{
		DtoJointElement dtoSupervisor = (DtoJointElement) JointUtilManager.getJavaFromJSON(supervisor, DtoJointElement.class);
		
		try{
			StudioFactory.removeSupervisor(dtoSupervisor);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
	
	/** Procedure to create a Card
	 * @param supervisor
	 * @param slot
	 * @return
	 */
	@RequestMapping(value = "/insertCard", method = RequestMethod.POST)
	public @ResponseBody String insertCard(@RequestParam("card") String card,@RequestParam("slot") String slot )
	{
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(slot, DtoJointElement.class);

		try{
			StudioFactory.insertEquipmentholder(dtoCard, dtoContainer);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";		
	}
			
	/** Procedure to remove a card
	 * @param card
	 * @param ?
	 * @return
	 */
	@RequestMapping(value = "/removeCard", method = RequestMethod.POST)
	public @ResponseBody String removeCard(@RequestParam("card") String card)
	{
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		try{
			StudioFactory.removeCard(dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
	
	
	/** Procedure to remove a shelf
	 * @param shelf
	 * @param ?
	 * @return
	 */
	@RequestMapping(value = "/removeShelf", method = RequestMethod.POST)
	public @ResponseBody String removeShelf(@RequestParam("shelf") String shelf)
	{
		DtoJointElement dtoShelf = (DtoJointElement) JointUtilManager.getJavaFromJSON(shelf, DtoJointElement.class);
		
		try{
			StudioFactory.removeShelf(dtoShelf);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
		
	/** Procedure to remove a slot
	 * @param slot
	 * @param ?
	 * @return
	 */
	@RequestMapping(value = "/removeSlot", method = RequestMethod.POST)
	public @ResponseBody String removeSlot(@RequestParam("slot") String slot)
	{
		DtoJointElement dtoSlot = (DtoJointElement) JointUtilManager.getJavaFromJSON(slot, DtoJointElement.class);
		
		try{
			StudioFactory.removeSlot(dtoSlot);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
			
	
	/* ----- Save/Load graph  ----- */
	
	
	/**
	 * Procedure to check if a Equipment file exist
	 * @param filename
	 * @return
	 */	
	@RequestMapping("/checkEquipmentFile")
	public @ResponseBody String checkEquipmentFile(@RequestParam("filename") String filename) 
	{				
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		if(NOpenFileUtil.checkEquipmentFileExist(filename))
		{
			return "exist";
		}
		return null;
	}
	
	/**
	 * Procedure to delete all ITU files inside of a Equipment Folder.
	 * @param filename
	 */	
	@RequestMapping("/deleteITUFiles")
	public @ResponseBody void deleteITUFiles(@RequestParam("filename") String filename) {
		
		String path = NOpenFileUtil.replaceSlash(NOpenFileUtil.equipmentJSONFolder + filename + "/itu/");
		File dir = new File(path);		
		if(dir.exists())
		{
			for(File file : dir.listFiles())
			{ 
				file.delete();
			}
		}
	}
	
	/**
	 * Procedure to save a Equipment.
	 * @param filename
	 * @param graph
	 */
	@RequestMapping("/saveEquipment")
	public @ResponseBody void saveEquipment(@RequestParam("filename") String filename, @RequestParam("graph") String graph) 
	{
		NOpenFileUtil.createEquipmentRepository(filename);
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename);
		
		try {
			File file = NOpenFileUtil.createEquipmentJSONFile(filename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Procedure to savel all ITU files of a Equipment.
	 * @param path
	 * @param ituFilename
	 * @param graph
	 */
	@RequestMapping("/saveITUFiles")
	public @ResponseBody void saveITUFiles(@RequestParam("path") String path, @RequestParam("filename") String ituFilename, @RequestParam("graph") String graph) 
	{		
		path = path + "/itu/";
		path = NOpenFileUtil.replaceSlash(path);
		NOpenFileUtil.createEquipmentRepository(path);		
		try {
			File file = NOpenFileUtil.createEquipmentJSONFile(path + ituFilename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Procedure to get all Equipments saved.
	 * @return
	 */
	@RequestMapping(value = "/getAllEquipments", method = RequestMethod.GET)
	protected @ResponseBody String getAllEquipments()
	{			
		String[] equipments = NOpenFileUtil.getAllEquipmentJSONFileNames();
		return NOpenFileUtil.parseStringToJSON("equipment", equipments);		
	}
	
	/**
	 * Procedure to open a specific Equipment.
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/openEquipment", method = RequestMethod.POST)
	protected @ResponseBody String openEquipment(@RequestParam("filename") String filename)
	{		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");	
		return NOpenFileUtil.openEquipmentJSONFileAsString(filename);
		
	}
	
	/**
	 * Procedure to open a ITU file of a Equipment.
	 * @param path
	 * @param ituFilename
	 * @return
	 */
	@RequestMapping(value = "/openITUFile", method = RequestMethod.POST)
	protected @ResponseBody String openITUFile(@RequestParam("path") String path, @RequestParam("filename") String ituFilename)
	{		
		path = path + "/itu/" + ituFilename + ".json";
		path = NOpenFileUtil.replaceSlash(path);		
		return NOpenFileUtil.openEquipmentJSONFileAsString(path);		
	}

}
