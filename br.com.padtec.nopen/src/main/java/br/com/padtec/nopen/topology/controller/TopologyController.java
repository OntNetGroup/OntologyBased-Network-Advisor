package br.com.padtec.nopen.topology.controller;

import java.io.File;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.topology.service.TopologyExporter;
import br.com.padtec.nopen.topology.service.TopologyFile;
import br.com.padtec.nopen.topology.service.TopologyImporter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
	protected @ResponseBody String checkTopologyFile(@RequestParam("filename") String filename, HttpServletRequest request){

		ServletContext sc = request.getSession().getServletContext();
		String path =  "/backend/topology/" ;

		File f = new File(sc.getRealPath(path) + "/" + filename + ".json");

		if(f.exists()){
			return "exist";
		}
		
		return null;
	}
	
	
	@RequestMapping(value = "/saveTopology", method = RequestMethod.POST)
	protected @ResponseBody void saveTopology(@RequestParam("filename") String filename, @RequestParam("graph") String graph, HttpServletRequest request){
			
		TopologyFile topologyFile = new TopologyFile();
		topologyFile.saveTopology(filename, graph, request);
		
	}
	
	@RequestMapping(value = "/getAllTopologies", method = RequestMethod.GET)
	protected @ResponseBody String getAllTopologies(HttpServletRequest request){
			
		TopologyFile topologyFile = new TopologyFile();
		HashSet<String> topologies = topologyFile.getAllTopologies(request);
		
		JsonArray json = new JsonArray();

		for(String topology : topologies){
			JsonObject j = new JsonObject();
			j.addProperty("topology", topology);
			
			json.add(j);
		}
		
		return json.toString();
		
	}
	
	@RequestMapping(value = "/openTopology", method = RequestMethod.POST)
	protected @ResponseBody String openTopology(@RequestParam("filename") String filename, HttpServletRequest request){
			
		TopologyFile topologyFile = new TopologyFile();
		String graph = topologyFile.openTopology(filename, request);
		
		return graph;
		
	}
	
	@RequestMapping(value = "/getAllTemplateEquipment", method = RequestMethod.GET)
	protected @ResponseBody String getAllTemplateEquipment(){
		
		 ManagerTopology managerTopology = new ManagerTopology();
		 HashSet<String> equipments2 = managerTopology.getAllTemplateEquipment();
		
		HashSet<String> equipments = new HashSet<String>();
		equipments.add("Equipment1");
		equipments.add("Equipment2");
		equipments.add("Equipment3");
		equipments.add("Equipment4");
		equipments.add("Equipment5");
		equipments.add("Equipment6");
		equipments.add("Equipment7");
		equipments.add("Equipment8");
		equipments.add("Equipment9");
		
		JsonArray json = new JsonArray();

		for(String equipment : equipments){
			JsonObject j = new JsonObject();
			j.addProperty("equipment", equipment);
			
			json.add(j);
		}
		
		return json.toString();
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
