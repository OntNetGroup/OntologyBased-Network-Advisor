package br.com.padtec.okco.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.padtec.okco.application.CompleterApp;
import br.com.padtec.okco.domain.Instance;

@Controller
public class ListController {

	@RequestMapping(method = RequestMethod.GET, value="/list")
	public String list(HttpServletRequest request) 
	{		
		List<Instance> individuals = CompleterApp.ListAllInstances;				
		List<String> modified = CompleterApp.ListModifiedInstances;
		
		if(individuals != null) 
		{
			request.getSession().setAttribute("listInstances", individuals);
			request.getSession().setAttribute("listModifedInstances", modified);
			return "list";
		} else{
			request.getSession().setAttribute("loadOk", "false");
			return "index";
		}
	}
}
