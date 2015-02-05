package br.com.padtec.advisor.controller.okco;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.okco.core.application.OKCoSelector;

/**
 * Controller responsible for the listing of individuals. 
 */

@Controller
public class OKCoListingController {

	@RequestMapping(method = RequestMethod.GET, value="/okco-list")
	public String list(HttpServletRequest request) 
	{
		System.out.println("Executing /list...");
		Date beginDate = new Date();
		/** ==================================================
		 *  List All Individuals and Which were modified
		 *  =================================================== */
		String ret = "";
		List<DtoInstance> allIndividuals = OKCoSelector.getIndividuals(true, false, false);			
		List<String> modifiedIndividuals = OKCoSelector.getModifiedIndividuals();
		
		if(allIndividuals != null) {
			request.getSession().setAttribute("listInstances", allIndividuals);
			request.getSession().setAttribute("listModifedInstances", modifiedIndividuals);
			ret =  "okco-list";
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
