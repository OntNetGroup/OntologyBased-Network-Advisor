package br.com.padtec.nopen.provisioning.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.provisioning.service.InterfaceStructure;
import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;
import br.com.padtec.nopen.service.NOpenEquipmentCloner;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

@Controller
public class ProvisioningController {

	@RequestMapping("/provisioning")
	public String provisioningRequest() 
	{
		return "provisioning/provisioning";
	}	
	
	@RequestMapping(value = "/openTopologyOnProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openTopologyOnProvisioning(@RequestParam("filename") String filename){
			
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		return NOpenFileUtil.openTopologyJSONFileAsString(filename);
		
//		ProvisioningManager provisioningManager = new ProvisioningManager();
//		String json = provisioningManager.openProvisioning(filename);
//		System.out.println(json);
//		
//		return json;
		
	}
	
	@RequestMapping(value = "/openEquipmentOnProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openEquipmentOnProvisioning(@RequestParam("filename") String filename){
			
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		return NOpenFileUtil.openEquipmentJSONFileAsString(filename);
		
	}
	
	@RequestMapping(value = "/openITUOnProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openITUOnProvisioning(@RequestParam("equipment") String equipment, @RequestParam("filename") String filename){
			
		filename = NOpenFileUtil.replaceSlash(equipment + "/itu/" + filename + ".json");
		return NOpenFileUtil.openEquipmentJSONFileAsString(filename);
		
	}
	
	/**
	 * Procedure to check if a Provisioning file exist.
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/checkProvisioningFile", method = RequestMethod.POST)
	protected @ResponseBody String checkProvisioningFile(@RequestParam("filename") String filename){

		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		if(NOpenFileUtil.checkProvisioningFileExist(filename)){
			return "exist";
		}
		
		return null;
	}
	
	/**
	 * Procedure to save a JSON Provisioning file.
	 * @param filename
	 * @param graph
	 */
	@RequestMapping(value = "/saveProvisioning", method = RequestMethod.POST)
	protected @ResponseBody void saveProvisioning(@RequestParam("filename") String filename, @RequestParam("graph") String graph){
		
		NOpenFileUtil.createProvisioningRepository(filename);
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename);
		
		try {
			File file = NOpenFileUtil.createProvisioningJSONFile(filename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Procedure to get all Provisioning saved files.
	 * @return
	 */
	@RequestMapping(value = "/getAllProvisioning", method = RequestMethod.GET)
	protected @ResponseBody String getAllProvisioning(){
			
		String[] provisioningElements = NOpenFileUtil.getAllProvisioningJSONFileNames();
		return NOpenFileUtil.parseStringToJSON("provisioning", provisioningElements);
		
	}
	
	/**
	 * Procedure to open a JSON Provisioning file.
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/openProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openProvisioning(@RequestParam("filename") String filename){
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");	
		return NOpenFileUtil.openProvisioningJSONFileAsString(filename);
		
	}
	
	/**
	 * Procedure that returns all technologies implemented by at least one equipment
	 * @return: array containing the names of the technologies
	 */
	@RequestMapping(value = "/getImplementedTechnologies", method = RequestMethod.POST)
	protected @ResponseBody String[] getImplementedTechnologies(){
		return new String[]{"MEF", "OTN"}; //TODO
	}
	
	/**
	 * @param technology
	 * @return: the uppermost layer of the given technology
	 */
	@RequestMapping(value = "/getUppermostLayer", method = RequestMethod.POST)
	protected @ResponseBody String getUppermostLayer(@RequestParam("technology") String technology){
		//TODO
		if(technology.equals("OTN")) return "MEN";
		if(technology.equals("MEF")) return "POUk";
		return "none";
	}
	
	/**
	 * @param clientMEF: uppermost layer of some technology
	 * @return: list of ids of equipments which implement that layer
	 */
	@RequestMapping(value = "/getEquipmentsByLayer", method = RequestMethod.POST)
	protected @ResponseBody String[] getEquipmentsByLayer(@RequestParam("clientMEF") String clientMEF){
		//TODO
		String[] ids = new String[]{"id0", "id1", "id2", "id3", "id4", "id5", "id6", "id7", "id8", "id9", "id10", "id11", "id12", "id13", "id14", "id15"};
		
		if(clientMEF.equals("MEN")) return ids;
		if(clientMEF.equals("POUk")) return new String[]{"idPOUk0", "idPOUk1", "idPOUk2"};
		return null;
	}
	
	/**
	 * Create card elements in OWL by a JSON file
	 * @param elements
	 * @param links
	 */
	@RequestMapping(value = "/parseCardToOWL", method = RequestMethod.POST)
	protected @ResponseBody void parseCardToOWL(@RequestParam("elements") String elements, @RequestParam("links") String links){
		
		try {
			NOpenEquipmentCloner.cloneEquipmentFromJSON(elements, ProvisioningComponents.provisioningRepository);
			NOpenEquipmentCloner.cloneLinksFromJSON(links, ProvisioningComponents.provisioningRepository);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to get Inputs from OWL
	 * @param equipmentId
	 * @return
	 */
	@RequestMapping(value = "/getInputsFromOWL", method = RequestMethod.POST)
	protected @ResponseBody String getInputsFromOWL(@RequestParam("equipmentId") String equipmentId){
		String result = null;
		try {
			result = InterfaceStructure.getInterfacesFromEquipment(equipmentId, "Input", ProvisioningComponents.provisioningRepository);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Method to get Outputs from OWL
	 * @param equipmentId
	 * @return
	 */
	@RequestMapping(value= "/getOutputsFromOWL", method = RequestMethod.POST)
	protected @ResponseBody String getOutputsFromOWL(@RequestParam("equipmentId") String equipmentId){
		String result = null;
		try {
			result = InterfaceStructure.getInterfacesFromEquipment(equipmentId, "Output", ProvisioningComponents.provisioningRepository);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * Method to get the type of the connection between two equipments
	 * @param equipmentSourceId, equipmentTargetId
	 * @return
	 */
	@RequestMapping(value= "/getTypeOfConnectionFromOWL", method = RequestMethod.POST)
	protected @ResponseBody String getTypeOfConnection(@RequestParam("equipmentSourceId") String equipmentSourceId, @RequestParam("equipmentTargetId") String equipmentTargetId){
		String result = null;
		try {
			result = InterfaceStructure.getTypeOfConnection(equipmentSourceId, equipmentTargetId, ProvisioningComponents.provisioningRepository);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
