package br.com.padtec.nopen.provisioning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProvisioningController {

	@RequestMapping("/provisioning")
	public String provisioningRequest() 
	{
		return "provisioning/provisioning";
	}	
}
