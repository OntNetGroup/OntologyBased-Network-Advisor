package br.com.padtec.nopen.provisioning.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;
import br.com.padtec.nopen.provisioning.service.ProvisioningManager;
import br.com.padtec.nopen.provisioning.service.ProvisioningReasoner;
import br.com.padtec.nopen.provisioning.util.ProvisioningUtil;
import br.com.padtec.nopen.service.util.NOpenFileUtil;

import com.google.gson.Gson;

@Controller
public class ProvisioningController {

	@RequestMapping("/provisioning")
	public String provisioningRequest() 
	{
		return "provisioning/provisioning";
	}	
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/openTopologyOnProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openTopologyOnProvisioning(@RequestParam("filename") String filename){
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		return NOpenFileUtil.openTopologyJSONFileAsString(filename);
		
	}
	
	/**
	 * 
	 * @param filename
	 * @param nodeId
	 * @return
	 */
	@RequestMapping(value = "/openTopologyEquipmentOnProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openTopologyEquipmentOnProvisioning(@RequestParam("filename") String filename, @RequestParam("nodeId") String nodeId){
			
		filename = NOpenFileUtil.replaceSlash(filename + "/equipments/" + nodeId + ".json");
		return NOpenFileUtil.openTopologyJSONFileAsString(filename);
		
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
	protected @ResponseBody void saveProvisioning(@RequestParam("path") String path, @RequestParam("filename") String filename, @RequestParam("graph") String graph){

		boolean isMainFile = false;
		
		if(path.equals(filename)) {
			isMainFile = true;
		}
		
		NOpenFileUtil.createProvisioningRepository(path);
		path = NOpenFileUtil.replaceSlash(path + "/" + filename);
		
		try {
			File jsonfile = NOpenFileUtil.createProvisioningJSONFile(path);
			NOpenFileUtil.writeToFile(jsonfile, graph);
			
			if(!isMainFile) {
				File owlFile = NOpenFileUtil.createProvisioningOWLFile(path);
				PrintWriter out = new PrintWriter(owlFile);
				ProvisioningComponents.provisioningRepository.getBaseModel().write(out , "RDF/XML");
			}
			
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
	protected @ResponseBody String openProvisioning(@RequestParam("path") String path, @RequestParam("filename") String filename){
		
		if(!path.equals(filename)) {
			
			//load owl file
			String owlFile = NOpenFileUtil.replaceSlash(path + "/" + filename + ".owl");
			
			try {
				InputStream in = new FileInputStream(NOpenFileUtil.provisioningOWLFolder + owlFile);
				ProvisioningComponents.provisioningRepository.getBaseModel().read(in, null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		path = NOpenFileUtil.replaceSlash(path + "/" + filename + ".json");
		return NOpenFileUtil.openProvisioningJSONFileAsString(path);
		
	}
	
	/**
	 * Create card elements in OWL by a JSON file
	 * @param elements
	 * @param links
	 */
	@RequestMapping(value = "/parseCardToOWL", method = RequestMethod.POST)
	protected @ResponseBody void parseCardToOWL(@RequestParam("elements") String elements, @RequestParam("links") String links){
		
		try {
			ProvisioningManager.createElementsInOWL(elements);
			ProvisioningManager.createLinksInOWL(links);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param sourcePort
	 * @param targetPorts
	 * @param connectionType
	 * @return
	 */
	@RequestMapping(value = "/hasPath", method = RequestMethod.POST)
	protected @ResponseBody String hasPath(@RequestParam("sourcePort") String sourcePort, @RequestParam("targetPorts") String targetPorts, @RequestParam("connectionType") String connectionType){
		return ProvisioningManager.hasPath(sourcePort, targetPorts, connectionType);
	}
	
	/**
	 * Connect ports in OWL by a JSON file
	 * @param elements
	 * @param links
	 */
	@RequestMapping(value = "/connectPortsInOWL", method = RequestMethod.POST)
	protected @ResponseBody void connectPortsInOWL(@RequestParam("elements") String elements, @RequestParam("links") String links){
		
		try {
			ProvisioningManager.createElementsInOWL(elements);
			ProvisioningManager.createLinksInOWL(links);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Procedure to execute the provisioning reasoning
	 */
	@RequestMapping(value = "/executeReasoning", method = RequestMethod.POST)
	protected @ResponseBody synchronized void executeReasoning(){
		
		ProvisioningReasoner.runInference(true);
		
	}
	
	/**
	 * Procedure to get a JSON model relationships
	 * @return
	 */
	@RequestMapping(value = "/getModelRelationships", method = RequestMethod.POST)
	protected @ResponseBody String getModelRelationships(){
		return ProvisioningUtil.generateJSONModelRelationships();
	}
	
	/**
	 * Method to get Inputs from OWL
	 * @param equipmentId
	 * @return
	 */
	@RequestMapping(value = "/getInputsFromOWL", method = RequestMethod.POST)
	protected @ResponseBody String getInputsFromOWL(@RequestParam("equipmentId") String equipmentId){
		
		HashMap<String, ArrayList<HashMap<String, String>>> result = new HashMap<String, ArrayList<HashMap<String, String>>>();
		
		try {
			result = ProvisioningManager.getPortsByLayerFromOWL(equipmentId, "Input_Card", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//transform the result mapping in a JSON string
		Gson gson = new Gson(); 
		String json = gson.toJson(result);
		System.out.println(json);
		
		return json;
	}
	
	/**
	 * Method to get Outputs from OWL
	 * @param equipmentId
	 * @return
	 */
	@RequestMapping(value= "/getOutputsFromOWL", method = RequestMethod.POST)
	protected @ResponseBody String getOutputsFromOWL(@RequestParam("equipmentId") String equipmentId){
		
		HashMap<String, ArrayList<HashMap<String, String>>> result = new HashMap<String, ArrayList<HashMap<String, String>>>();
		
		try {
			result = ProvisioningManager.getPortsByLayerFromOWL(equipmentId, "Output_Card", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//transform the result mapping in a JSON string
		Gson gson = new Gson(); 
		String json = gson.toJson(result);
		System.out.println(json);
		
		return json;
	}
	
	
	/**
	 * Method to get the type of the connection between two equipments
	 * @param equipmentSourceId, equipmentTargetId
	 * @return
	 */
	@RequestMapping(value= "/getConnectionTypeFromOWL", method = RequestMethod.POST)
	protected @ResponseBody String getConnectionTypeFromOWL(@RequestParam("equipmentSourceId") String equipmentSourceId, @RequestParam("equipmentTargetId") String equipmentTargetId){
		String result = null;
		try {
			result = ProvisioningManager.getConnectionType(equipmentSourceId, equipmentTargetId);
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
	@RequestMapping(value= "/getPossibleConnectionsFromOWL", method = RequestMethod.POST)
	protected @ResponseBody String getPossibleConnectionsFromOWL(@RequestParam("equipmentSourceId") String equipmentSourceId, @RequestParam("equipmentTargetId") String equipmentTargetId, @RequestParam("connectionType") String connectionType){
		String result = "";
		try {
			result = ProvisioningManager.getConnectionInterfaces(equipmentSourceId, equipmentTargetId, connectionType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
