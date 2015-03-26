package br.com.padtec.nopen.topology.controller;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.topology.service.TopologyExporter;
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
	
	@RequestMapping(value = "/getAllTemplateEquipment", method = RequestMethod.GET)
	protected @ResponseBody String getAllTemplateEquipment(){
		
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
	
}
