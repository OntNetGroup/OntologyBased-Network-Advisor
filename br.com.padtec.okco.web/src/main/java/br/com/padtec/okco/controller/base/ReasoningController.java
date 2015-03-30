package br.com.padtec.okco.controller.base;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.OKCoComponents;

/**
 * Controller responsible for the inference.
 * See this class: {@link OKCoComponents}  
 */

public class ReasoningController {

	@RequestMapping(value="/runReasoner", method = RequestMethod.POST)
	public @ResponseBody DtoResult runReasoner() 
	{
		/** ==================================================
		 * Running the reasoner, storing the temporary model and cleaning the list of modified
		 *  =================================================== */
		return OKCoComponents.reasoner.runReasoner();		
	}
}
