package br.com.padtec.okco.controller;

import java.util.Date;
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
		System.out.println("Executing /list...");
		Date beginDate = new Date();
		/** ==================================================
		 *  List All Individuals and Which were modified
		 *  =================================================== */
		String ret = "";
		List<DtoInstance> allIndividuals = OKCoApp.getIndividuals(true, false, false);			
		List<String> modifiedIndividuals = OKCoApp.getModifiedIndividuals();
		
		if(allIndividuals != null) {
			request.getSession().setAttribute("listInstances", allIndividuals);
			request.getSession().setAttribute("listModifedInstances", modifiedIndividuals);
			ret =  "list";
		} else{
			request.getSession().setAttribute("loadOk", "false");
			ret = "index";
		}
		
		Date endDate = new Date();
		long diff = endDate.getTime() - beginDate.getTime();
		long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);         
		long diffHours = diff / (60 * 60 * 1000); 
		System.out.println("Execution time: " + diffHours + "h " + diffMinutes + "m " + diffSeconds + "s");
		
		return ret;
	}	
}
