package br.com.padtec.okco.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.padtec.common.application.OKCoApp;
import br.com.padtec.common.dto.DtoInstance;

/**
 * Controller responsible for the listing of individuals. 
 */

@Controller
public class ListingController {

	@RequestMapping(method = RequestMethod.GET, value="/list")
	public String list(HttpServletRequest request) 
	{
		/** ==================================================
		 *  List All Individuals and Which were modified
		 *  =================================================== */
		List<DtoInstance> allIndividuals = OKCoApp.getIndividuals(true, false, false);			
		List<String> modifiedIndividuals = OKCoApp.getModifiedIndividuals();
		
		if(allIndividuals != null) {
			request.getSession().setAttribute("listInstances", allIndividuals);
			request.getSession().setAttribute("listModifedInstances", modifiedIndividuals);
			return "list";
		} else{
			request.getSession().setAttribute("loadOk", "false");
			return "index";
		}
	}	
}
