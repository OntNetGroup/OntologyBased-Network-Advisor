package br.com.padtec.nopen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OptionsController {

	@RequestMapping(value = "/createLayer", method = RequestMethod.POST)
	public @ResponseBody String createLayer(@RequestParam("layerName") String layerName)
	{
		System.out.println("Layer: \n"+layerName);
		return "OK";
	}
	
	@RequestMapping(value = "/createTech", method = RequestMethod.POST)
	public @ResponseBody String createTech(@RequestParam("techName") String techName)
	{
		System.out.println("Technology: \n"+techName);
		return "OK";
	}
}
