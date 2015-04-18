package br.com.padtec.nopen.provisioning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.provisioning.service.ProvisioningManager;
import br.com.padtec.nopen.service.util.NOpenFileUtil;

@Controller
public class ProvisioningController {

	@RequestMapping("/provisioning")
	public String provisioningRequest() 
	{
		return "provisioning/provisioning";
	}	
	
	@RequestMapping(value = "/openTopologyOnProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openTopologyOnProvisioning(@RequestParam("filename") String filename){
			
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		
		ProvisioningManager provisioningManager = new ProvisioningManager();
		String json = provisioningManager.openProvisioning(filename);
		System.out.println(json);
		
		return json;
		
	}
}
