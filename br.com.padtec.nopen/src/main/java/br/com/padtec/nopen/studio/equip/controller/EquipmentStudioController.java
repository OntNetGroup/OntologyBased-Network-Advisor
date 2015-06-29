package br.com.padtec.nopen.studio.equip.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.InstanceFabricator;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.nopen.studio.model.StudioSpecificFactory;
import br.com.padtec.nopen.studio.service.PerformBind;
import br.com.padtec.nopen.studio.service.StudioComponents;

import com.jointjs.util.JointUtilManager;

@Controller
public class EquipmentStudioController {

	@RequestMapping("/equipment-studio")
	public String equipmentStudioRequest() 
	{
		return "equipment-studio/equipment-studio";
	}

	/* ======================================================================================
	 * GET
	 * ======================================================================================*/
	
	/** Procedure to get all the technologies */
	@RequestMapping(value = "/getTechnologies", method = RequestMethod.POST)
	public @ResponseBody String[] getTechnologies()
	{   
		return NOpenQueryUtil.getAllTechnologiesNames(StudioComponents.studioRepository.getBaseModel());		
	}
	
	/* ======================================================================================
	 * Create
	 * ======================================================================================*/
	
	/** Insert content on a container */
	@RequestMapping(value = "/EquipStudioInsertContainer", method = RequestMethod.POST)
	public @ResponseBody String insertContainer(@RequestParam("container") String container, @RequestParam("content") String content) 
	{	
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		DtoJointElement dtoContent = (DtoJointElement) JointUtilManager.getJavaFromJSON(content, DtoJointElement.class);
		
		try{
			InstanceFabricator.createComponentOfRelation(dtoContainer, dtoContent);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";		
	}
	
	/** Bind the source element to the target element */
	/**
	 * @param sourceElement: id, name and type of the source element
	 * @param targetElement: id, name and type of the target element
	 * @param bind: id, name and type of the bind (name = id, type = "link")
	 * @return: "success" if the binding was successful
	 */
	@RequestMapping(value = "/EquipStudioPerformBind", method = RequestMethod.POST)
	public @ResponseBody String performBind(@RequestParam("sourceElement") String sourceElement, @RequestParam("targetElement") String targetElement,
	@RequestParam("bind") String bind) 
	{	
		DtoJointElement dtoSourceElement = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceElement, DtoJointElement.class);
		DtoJointElement dtoTargetElement = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetElement, DtoJointElement.class);
		
		try{
			PerformBind.applyEquipmentBinds(dtoSourceElement, dtoTargetElement);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		

		return "success";		
	}	
		
	/* ======================================================================================
	 * Delete
	 * ======================================================================================*/
	
	
	/** Procedure to remove an Equipment holder
	 * @param equipmentholder
	 * @param container
	 * @return
	 */
	@RequestMapping(value = "/deleteEquipmentholder", method = RequestMethod.POST)
	public @ResponseBody String deleteEquipmentholder(@RequestParam("equipmentholder") String equipmentholder , @RequestParam("container") String container )
	{
		DtoJointElement dtoEquipmentholder = (DtoJointElement) JointUtilManager.getJavaFromJSON(equipmentholder, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);

		try{
			StudioSpecificFactory.deleteEquipmentholder(dtoEquipmentholder, dtoContainer);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}	
		
	//=============================================================================================
	// Link
	//=============================================================================================
	
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
				
		try{
			StudioSpecificFactory.superviseCard(dtoSupervisor, dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
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
				
		try{
			StudioSpecificFactory.unsuperviseCard(dtoSupervisor, dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
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
		 
		
		try{
			StudioSpecificFactory.setTechnology(dtosupervisor, technology);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";		
	}
	
	//=============================================================================================
	// Verification
	//=============================================================================================
	
	/** Check if a bind can be performed from the source element to the target element (just check, don't perform the bind) */
	/**
	 * @param sourceElement: id, name and type of the source element
	 * @param targetElement: id, name and type of the target element
	 * @return: "true" if a bind can be performed
	 */
	@RequestMapping(value = "/EquipStudioCanPerformBind", method = RequestMethod.POST)
	public @ResponseBody String canPerformBind(@RequestParam("sourceElement") String sourceElement, @RequestParam("targetElement") String targetElement) 
	{		
		DtoJointElement dtoSourceElement = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceElement, DtoJointElement.class);
		DtoJointElement dtoTargetElement = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetElement, DtoJointElement.class);
		
		try{
			PerformBind.canCreateEquipmentBinds(dtoSourceElement, dtoTargetElement);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		
		return "true";		
	}
	
	/** Procedure to check if card can be supervised
	 * @param supervisor
	 * @param card
	 * @return
	 */
	@RequestMapping(value = "/canSupervise",method = RequestMethod.POST)
	public @ResponseBody String canSupervise(@RequestParam("card") String card,@RequestParam("supervisor") String supervisor)
	{
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		DtoJointElement dtoSupervisor = (DtoJointElement) JointUtilManager.getJavaFromJSON(supervisor, DtoJointElement.class);
		
		try{
			StudioSpecificFactory.canSupervise(dtoSupervisor, dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "true";
	}
				
	/** Procedure to check if card can be unsupervised
	 * @param supervisor
	 * @param card
	 * @return
	 */
	@RequestMapping(value = "/canUnsupervise",method = RequestMethod.POST)
	public @ResponseBody String canUnsupervise(@RequestParam("card") String card,@RequestParam("supervisor") String supervisor)
	{
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		DtoJointElement dtoSupervisor = (DtoJointElement) JointUtilManager.getJavaFromJSON(supervisor, DtoJointElement.class);
		
		try{
			StudioSpecificFactory.canUnsupervise(dtoSupervisor, dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "true";
	}
	
	//=============================================================================================
	// Name
	//=============================================================================================
	
	/** Procedure to rename an Equipment
	 * @param equipment
	 * @return
	 */
	@RequestMapping(value = "/setEquipmentName", method = RequestMethod.POST)
	public @ResponseBody String setEquipmentName(@RequestParam("equipment") String equipment)
	{
		DtoJointElement dtoEquipment = (DtoJointElement) JointUtilManager.getJavaFromJSON(equipment, DtoJointElement.class);
		
		try{
			StudioSpecificFactory.setEquipmentName(dtoEquipment);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "success";
	}	
		
	//=============================================================================================
	// Save & Load
	//=============================================================================================
				
	/**
	 * Procedure to check if a Template file exist
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
	 * Procedure to save a Equipment.
	 * @param filename
	 * @param graph
	 */
	@RequestMapping("/saveEquipment")
	public @ResponseBody void saveEquipment(@RequestParam("filename") String filename, @RequestParam("graph") String graph) 
	{
		NOpenFileUtil.createEquipmentRepository(filename);
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename);
		
		System.out.println(filename);
		try {
			File file = NOpenFileUtil.createEquipmentJSONFile(filename);
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
