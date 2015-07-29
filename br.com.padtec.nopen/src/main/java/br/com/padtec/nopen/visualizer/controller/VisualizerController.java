package br.com.padtec.nopen.visualizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.service.NOpenEquipmentCloner;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.nopen.studio.service.StudioComponents;

@Controller
public class VisualizerController {

	@RequestMapping("/equipment-visualizer")
	public String equipmentVisualizerRequest() 
	{
		return "visualizer/equipment-visualizer/equipment-visualizer";
	}
	
	@RequestMapping("/topology-equipment-visualizer")
	public String topologyequipmentVisualizerRequest() 
	{
		return "visualizer/topology-equipment-visualizer/topology-equipment-visualizer";
	}
	
	@RequestMapping("/otn-protocol")
	public String otnprotocolRequest()
	{
		return "visualizer/topology-equipment-visualizer/otn-protocol";
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
	
	@RequestMapping("/itu-visualizer")
	public String ituVisualizerRequest() 
	{
		return "visualizer/itu-visualizer/itu-visualizer";
	}
	
	/**
	 * Create equipment in OWL by a JSON file
	 * @param elements
	 * @param links
	 */
	@RequestMapping(value = "/parseEquipToOWL", method = RequestMethod.POST)
	protected @ResponseBody void parseEquipToOWL(@RequestParam("elements") String elements, @RequestParam("links") String links){
		
		try {
			NOpenEquipmentCloner.cloneEquipmentFromJSON(elements, StudioComponents.studioRepository);
			NOpenEquipmentCloner.cloneLinksFromJSON(links, StudioComponents.studioRepository);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
