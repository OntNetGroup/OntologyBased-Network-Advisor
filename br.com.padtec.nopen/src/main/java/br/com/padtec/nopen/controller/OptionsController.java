package br.com.padtec.nopen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;
import br.com.padtec.nopen.service.NOpenRegister;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;

@Controller
public class OptionsController {
	
	@RequestMapping("/options")
	public String configRequest(HttpServletRequest request) 
	{		
		request.getSession().setAttribute("techs", NOpenQueryUtil.getAllTechnologiesNames(ProvisioningComponents.provisioningRepository.getBaseModel()));
		request.getSession().setAttribute("defaultTechs", NOpenRegister.getDefaultTechs());
		
		request.getSession().setAttribute("layers", NOpenQueryUtil.getAllLayerNames(ProvisioningComponents.provisioningRepository.getBaseModel()));
		request.getSession().setAttribute("defaultLayers", NOpenRegister.getDefaultLayers());
		
		request.getSession().setAttribute("services", NOpenQueryUtil.getAllServicesNames(ProvisioningComponents.provisioningRepository.getBaseModel()));		
		request.getSession().setAttribute("defaultServices", NOpenRegister.getDefaultServices());
		
		return "options";
	}
	
	@RequestMapping(value = "/deleteTechOrLayer", method = RequestMethod.POST)
	public @ResponseBody String deleteTechOrLayer(@RequestParam("elemName") String elemName) throws Exception 
	{
		if(elemName.contains("tech-")){
			String techName = elemName.replace("tech-", "");			
			NOpenRegister.unregisterTechnology(techName);	
		}
		if(elemName.contains("layer-")){
			String layerName = elemName.replace("layer-", "");			
			NOpenRegister.unregisterLayer(layerName);	
		}
		
		return "Successfully Registered";
	}
	
	@RequestMapping(value = "/createTech", method = RequestMethod.POST)
	public @ResponseBody String createTech(@RequestParam("techName") String techName) throws Exception 
	{
		NOpenRegister.registerTechnology(techName);
		
		return "Successfully Registered";
	}	
	
	@RequestMapping(value = "/createLayer", method = RequestMethod.POST)
	public @ResponseBody String createLayer(@RequestParam("layerName") String layerName, @RequestParam("techName") String techName) throws Exception 
	{
		NOpenRegister.registerLayer(layerName, techName);
		
		return "Successfully Registered";
	}
}
