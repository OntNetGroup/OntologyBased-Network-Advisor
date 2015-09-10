package br.com.padtec.nopen.visualizer.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.nopen.service.NOpenAttributeRecognizer;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.nopen.topology.service.TopologyManager;

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
	
	protected static PElement[] pelems;
	protected static PLink[] plinks;
	
	/**
	 * Create card in OWL by a JSON file
	 * @param elements
	 * @param links
	 */
	@RequestMapping(value = "/parsetopologyCardToOWL", method = RequestMethod.POST)
	protected @ResponseBody void parsetopologyCardToOWL(@RequestParam("elements") String elements, @RequestParam("links") String links){
			
		try {
			TopologyManager.createElementsInOWL(elements);
			TopologyManager.createLinksInOWL(links);
		} catch (Exception e) {
			e.printStackTrace();

		}
		
//		try {
//			pelems = (PElement[]) JointUtilManager.getJavaFromJSON(elements, PElement[].class);
//			System.out.println(pelems);
//			System.out.println(links);
//			plinks = (PLink[]) JointUtilManager.getJavaFromJSON(links, PLink[].class);
//			System.out.println(plinks);
//			// we are not going to clone the content in OWL anymore
//			// we are actually going to search for the attributes from these Java structures: PElements and PLinks
////			NOpenEquipmentCloner.cloneEquipmentFromJSON(elements, StudioComponents.studioRepository);
////			NOpenEquipmentCloner.cloneLinksFromJSON(links, StudioComponents.studioRepository);
//			
//			return "success";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "error";
//		}
	}
	
	/**
	 * Create equipment in OWL by a JSON file
	 * @param card
	 * @param supervisor
	 */
	@RequestMapping(value = "/getCardAttributes", method = RequestMethod.POST)
	protected @ResponseBody void getCardAttributes(@RequestParam("card") String card,@RequestParam("supervisor") String supervisor){
		
		try {			
//			for(PElement e: pelems){
//				System.out.println(e);
//			}
//			for(PLink l: plinks){
//				System.out.println(l);
//			}	
			
			Map<String, String> data = NOpenAttributeRecognizer.runFromOWL(card , supervisor);
//			Map<String, String> result = NOpenAttributeRecognizer.runfromCard(card,pelems,plinks);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
