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

	

	
	//=============================================================================================
	// Supervisor and technology
	//=============================================================================================
	
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
				
		try{
			StudioFactory.insertSupervisor(dtoSupervisor, dtoSlot);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
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
	
	/** Procedure to supervise a card
	 * @param card
	 * @param supervisor
	 * @return
	 */
	@RequestMapping(value = "/superviseCard", method = RequestMethod.POST)
	public @ResponseBody String superviseCard(@RequestParam("supervisor") String supervisor,@RequestParam("card") String card )
	{
		DtoJointElement dtoSupervisor = (DtoJointElement) JointUtilManager.getJavaFromJSON(supervisor, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
				
//		try{
//			StudioFactory.superviseCard(dtoSupervisor, dtoCard);
//		}catch(Exception e){
//			e.printStackTrace();
//			return e.getLocalizedMessage();
//		}		
		return "success";		
	}
	
	/** Procedure to unsupervise a card
	 * @param card
	 * @param supervisor
	 * @return
	 */
	@RequestMapping(value = "/unsuperviseCard", method = RequestMethod.POST)
	public @ResponseBody String unsuperviseCard(@RequestParam("supervisor") String supervisor,@RequestParam("card") String card )
	{
		DtoJointElement dtoSupervisor = (DtoJointElement) JointUtilManager.getJavaFromJSON(supervisor, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
				
//		try{
//			StudioFactory.unsuperviseCard(dtoSupervisor, dtoCard);
//		}catch(Exception e){
//			e.printStackTrace();
//			return e.getLocalizedMessage();
//		}		
		return "success";		
	}
	
	/** Procedure to select the supervisor technology
	 * @param supervisor
	 * @param technology
	 * @param slot
	 * @return
	 */
	
	
	@RequestMapping(value = "/setTechnology", method = RequestMethod.POST)
	public @ResponseBody String setTechnology(@RequestParam("supervisor") String supervisor,@RequestParam("technology") String technology )
	{
		DtoJointElement dtosupervisor = (DtoJointElement) JointUtilManager.getJavaFromJSON(supervisor, DtoJointElement.class);
//		DtoJointElement dtotech = (DtoJointElement) JointUtilManager.getJavaFromJSON(technology, DtoJointElement.class);
		//TODO (To Gabriel/Missael) I need of the specific equipment in which this supervisor is linked to (John)
		
		try{
			//StudioFactory.setTechnology(dtosupervisor, dtotech, dtoquip);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
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
	
	
	//=============================================================================================
	// Card
	//=============================================================================================
	
	/** Procedure to remove a card
	 * @param card
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/removeCard", method = RequestMethod.POST)
	public @ResponseBody String removeCard(@RequestParam("card") String card)
	{
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		try{
			StudioFactory.deleteCard(dtoCard);
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
			
	
	//=============================================================================================
	// Generic equipment
	//=============================================================================================
	
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
	
	/** Procedure to remove an Equipment holder
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

	
	/** Procedure to rename an Equipment
	 * @param equipment
	 * @return
	 */
	@RequestMapping(value = "/setEquipmentName", method = RequestMethod.POST)
	public @ResponseBody String setEquipmentName(@RequestParam("equipment") String equipment)
	{
		DtoJointElement dtoEquipment = (DtoJointElement) JointUtilManager.getJavaFromJSON(equipment, DtoJointElement.class);
		
		
		return "success";
	}	
	
	//=============================================================================================
	// Shelf
	//=============================================================================================
	
	/** Procedure to remove a shelf
	 * @param shelf
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/removeShelf", method = RequestMethod.POST)
	public @ResponseBody String removeShelf(@RequestParam("shelf") String shelf)
	{
		DtoJointElement dtoShelf = (DtoJointElement) JointUtilManager.getJavaFromJSON(shelf, DtoJointElement.class);
		
		try{
			StudioFactory.deleteShelf(dtoShelf);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
		
	//=============================================================================================
	// Slot
	//=============================================================================================
	
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
			StudioFactory.deleteSlot(dtoSlot);
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
	@RequestMapping("/checkTemplateFile")
	public @ResponseBody String checkTemplateFile(@RequestParam("filename") String filename) 
	{				
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		if(NOpenFileUtil.checkTemplateFileExist(filename))
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
		
		String path = NOpenFileUtil.replaceSlash(NOpenFileUtil.templateJSONFolder + filename + "/itu/");
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
	 * Procedure to save a Template.
	 * @param filename
	 * @param graph
	 */
	@RequestMapping("/saveTemplate")
	public @ResponseBody void saveTemplate(@RequestParam("filename") String filename, @RequestParam("graph") String graph) 
	{
		NOpenFileUtil.createTemplateRepository(filename);
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename);
		
		try {
			File file = NOpenFileUtil.createTemplateJSONFile(filename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Procedure to save all ITU files of a Equipment.
	 * @param path
	 * @param ituFilename
	 * @param graph
	 */
	@RequestMapping("/saveITUFiles")
	public @ResponseBody void saveITUFiles(@RequestParam("path") String path, @RequestParam("filename") String ituFilename, @RequestParam("graph") String graph) 
	{		
		path = path + "/itu/";
		path = NOpenFileUtil.replaceSlash(path);
		NOpenFileUtil.createTemplateRepository(path);		
		try {
			File file = NOpenFileUtil.createTemplateJSONFile(path + ituFilename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Procedure to get all Templates saved.
	 * @return
	 */
	@RequestMapping(value = "/getAllTemplates", method = RequestMethod.GET)
	protected @ResponseBody String getAllTemplates()
	{			
		String[] templates = NOpenFileUtil.getAllTemplateJSONFileNames();
		return NOpenFileUtil.parseStringToJSON("template", templates);		
	}
	
	/**
	 * Procedure to open a specific Template.
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/openTemplate", method = RequestMethod.POST)
	protected @ResponseBody String openTemplate(@RequestParam("filename") String filename)
	{		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");	
		return NOpenFileUtil.openTemplateJSONFileAsString(filename);
		
	}
	
	

}
