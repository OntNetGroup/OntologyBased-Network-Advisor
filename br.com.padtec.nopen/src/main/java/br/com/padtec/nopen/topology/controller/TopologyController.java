package br.com.padtec.nopen.topology.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.nopen.topology.service.TopologyExporter;
import br.com.padtec.nopen.topology.service.TopologyImporter;


@Controller
public class TopologyController {

	@RequestMapping("/network-topology")
	public String networkTopologyRequest() {
		return "network-topology/network-topology";
	}
	
	@RequestMapping(value = "/exportTopology", method = RequestMethod.POST)
	protected @ResponseBody String exportTopology(@RequestParam("json") String json, @RequestParam("uuid") String uuid){
		
		TopologyExporter topology = new TopologyExporter();
		String xml = topology.exportTopology(json, uuid);
		
		return xml;
	}
	
	@RequestMapping(value = "/importTopology", method = RequestMethod.POST)
	protected @ResponseBody String importTopology(HttpServletRequest request){
			
		TopologyImporter topology = new TopologyImporter();
		String json = topology.importTopology(request);
		
		return json;
	}
	
	@RequestMapping(value = "/getUUID", method = RequestMethod.POST)
	protected @ResponseBody String getUUID(HttpServletRequest request){
			
		TopologyImporter topology = new TopologyImporter();
		return topology.getTopologyId(request);
	}
	
	@RequestMapping(value = "/checkTopologyFile", method = RequestMethod.POST)
	protected @ResponseBody String checkTopologyFile(@RequestParam("filename") String filename){

		if(NOpenFileUtil.checkTopologyFileExist(filename + ".json")){
			return "exist";
		}
		
		return null;
	}
	
	@RequestMapping(value = "/saveTopology", method = RequestMethod.POST)
	protected @ResponseBody void saveTopology(@RequestParam("filename") String filename, @RequestParam("graph") String graph){
			
		try {
			File file = NOpenFileUtil.createTopologyJSONFile(filename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/getAllTopologies", method = RequestMethod.GET)
	protected @ResponseBody String getAllTopologies(){
			
		HashSet<String> topologies = NOpenFileUtil.getAllTopplogyJSONFileNames();
		return NOpenFileUtil.parseHashSetToJSON("topology", topologies);
		
	}
	
	@RequestMapping(value = "/openTopology", method = RequestMethod.POST)
	protected @ResponseBody String openTopology(@RequestParam("filename") String filename){
			
		return NOpenFileUtil.openTopologyFileAsString(filename + ".json");
		
	}
	
	@RequestMapping(value = "/getAllTemplateEquipment", method = RequestMethod.GET)
	protected @ResponseBody String getAllTemplateEquipment(){
		
//		 TopologyManager managerTopology = new TopologyManager();
//		 HashSet<String> equipments2 = managerTopology.getAllTemplateEquipment();
		
//		 NOpenFactory.createEquipment(StudioComponents.studioRepository);
//		 
//		 TopologyManager managerTopology = new TopologyManager();
//		 HashSet<String> equipments = managerTopology.getAllTemplateEquipment();
		 
		 HashSet<String> equipments = new HashSet<String>();
		 equipments.add("Equipment1");
		 equipments.add("Equipment2");
		 equipments.add("Equipment3");
		 
		 return NOpenFileUtil.parseHashSetToJSON("equipment", equipments);
		 
	}
	
	@RequestMapping(value = "/matchEquipmentToNode", method = RequestMethod.POST)
	protected @ResponseBody void matchEquipmentToNode(@RequestParam("idNode") String idNode, @RequestParam("idEquipment") String idEquipment){
		
		System.out.println("idNode: " + idNode);
		System.out.println("idEquipment: " + idEquipment);
		
		// ManagerTopology managerTopology = new ManagerTopology();
		// managerTopology.matchEquipmentToNode(idNode, idEquipment);
	}	
	
	@RequestMapping(value = "/createConnection", method = RequestMethod.POST)
	protected @ResponseBody void createConnection(@RequestParam("source") String source, @RequestParam("target") String target){
		
		System.out.println("Source: " + source);
		System.out.println("Target: " + target);
		
		// ManagerTopology managerTopology = new ManagerTopology();
		// managerTopology.createConnection(source, target);
		
	}
	
}
