package br.com.padtec.nopen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.reasoning.HermitReasonerImpl;
import br.com.padtec.common.reasoning.PelletReasonerImpl;
import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.studio.service.StudioComponents;

@Controller
public class ReasonerController {

	@RequestMapping("/reasoner")
	public String reasonerRequest(HttpServletRequest request) 
	{
		if(StudioComponents.studioRepository.getReasoner() instanceof PelletReasonerImpl) request.getSession().setAttribute("defaultReasoner","pellet");		
		else request.getSession().setAttribute("defaultReasoner","hermit");
		
		return "reasoner";
	}
	
	@RequestMapping(value="/selectReasoner", method = RequestMethod.POST)
	public @ResponseBody String selectReasoner(@RequestParam("reasoner") String reasoner) 
	{
		if(reasoner.equals("pellet")){
			StudioComponents.studioRepository.setReasoner(new PelletReasonerImpl());
			ProvisioningComponents.provisioningRepository.setReasoner(new PelletReasonerImpl());
			NOpenLog.appendLine("Pellet set by default.");
			
		}else if(reasoner.equals("hermit")){
			StudioComponents.studioRepository.setReasoner(new HermitReasonerImpl());
			ProvisioningComponents.provisioningRepository.setReasoner(new HermitReasonerImpl());
			NOpenLog.appendLine("Hermit set by default.");
		}
		
		return "reasoner";
	}
	
}
