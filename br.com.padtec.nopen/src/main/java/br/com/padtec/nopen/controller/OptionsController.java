package br.com.padtec.nopen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.service.NOpenRegister;

@Controller
public class OptionsController {
	
	@RequestMapping(value = "/deleteTechOrLayer", method = RequestMethod.POST)
	public @ResponseBody String deleteTechOrLayer(@RequestParam("elemName") String elemName) 
	{
		if(elemName.contains("tech-")){
			String techName = elemName.replace("tech-", "");
			NOpenRegister.unregisterTechnology(techName);	
		}
		if(elemName.contains("layer-")){
			String layerName = elemName.replace("layer-", "");
			NOpenRegister.unregisterLayer(layerName);	
		}
		
		return "Successfull Registered";
	}
	
	@RequestMapping(value = "/createTech", method = RequestMethod.POST)
	public @ResponseBody String createTech(@RequestParam("techName") String techName) throws Exception 
	{
		NOpenRegister.registerTechnology(techName);
		
		return "Successfull Registered";
	}	
	
	@RequestMapping(value = "/createLayer", method = RequestMethod.POST)
	public @ResponseBody String createLayer(@RequestParam("layerName") String layerName, @RequestParam("techName") String techName) throws Exception 
	{
		NOpenRegister.registerLayer(layerName, techName);
		
		return "Successfull Registered";
	}
}
