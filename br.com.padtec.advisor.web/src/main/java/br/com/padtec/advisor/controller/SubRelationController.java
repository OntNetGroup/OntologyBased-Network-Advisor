package br.com.padtec.advisor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.padtec.advisor.core.application.SubRelationEnforcer;

@Controller
public class SubRelationController {
	
	@RequestMapping(value="/EnforceSubRelation", method = RequestMethod.POST)
	public String enforceSubRelation() 
	{
		/** ==================================================
		 * Enforce sub-relations in the uploaded ontology
		 *  =================================================== */
		SubRelationEnforcer.run();	
		
		return "redirect::okco-details";
	}
}
