package br.com.padtec.nopen.advisor.okco.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.okco.core.application.OKCoComponents;

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
		List<DtoInstance> allIndividuals = OKCoComponents.selector.getIndividuals(true, false, false);			
		List<String> modifiedIndividuals = OKCoComponents.selector.getModifiedIndividuals();
		
		if(allIndividuals != null) {
			request.getSession().setAttribute("listInstances", allIndividuals);
			request.getSession().setAttribute("listModifedInstances", modifiedIndividuals);
			ret =  "advisor/views/okco-list";
		} else{
			request.getSession().setAttribute("loadOk", "false");
			ret = "advisor/index";
		}
		
		PerformanceUtil.printExecutionTime("/okco-list", beginDate);
		
		return ret;
	}	
}
