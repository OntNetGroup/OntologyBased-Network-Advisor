package br.com.padtec.nopen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OptionsController {

	@RequestMapping(value = "/deleteTechOrLayer", method = RequestMethod.POST)
	public @ResponseBody String deleteTechOrLayer(@RequestParam("elemName") String elemName) 
	{
		System.out.println("Deleting a Technology or Layer");
		return "OK";
	}
	
	@RequestMapping(value = "/createTech", method = RequestMethod.POST)
	public @ResponseBody String createTech(@RequestParam("techName") String techName) 
	{
		//NOpenRegister.registerTechnology(techName);
		
		return "OK";
	}	
	
	@RequestMapping(value = "/createLayer", method = RequestMethod.POST)
	public @ResponseBody String createLayer(@RequestParam("layerName") String layerName, @RequestParam("techName") String techName) throws Exception 
	{
		//NOpenRegister.registerLayer(layerName, techName);
		
		return "OK";
	}
}
