package br.com.padtec.nopen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.padtec.common.reasoning.PelletReasonerImpl;
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
}
