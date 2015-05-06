package br.com.padtec.nopen.visualizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.service.util.NOpenFileUtil;

@Controller
public class VisualizerController {

	@RequestMapping("/equipment-visualizer")
	public String equipmentVisualizerRequest() 
	{
		return "visualizer/equipment-visualizer/equipment-visualizer";
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
		return NOpenFileUtil.openTemplateJSONFileAsString(filename);
		
	}
	
	@RequestMapping("/itu-visualizer")
	public String ituVisualizerRequest() 
	{
		return "visualizer/itu-visualizer/itu-visualizer";
	}
}
