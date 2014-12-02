package br.com.padtec.okco.controller;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.application.OKCoApp;
import br.com.padtec.common.dto.DtoClassifyInstancePost;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.exceptions.OKCoExceptionInstanceFormat;

@Controller
public class ClassifierController {

	@RequestMapping(value="/classifyInstanceClasses", method = RequestMethod.POST)
	public @ResponseBody DtoResult classifyInstanceClasses(@RequestBody final DtoClassifyInstancePost dto) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{    
		String[] classes = dto.arrayCls.split("%&&%");

		/** ==================================================
		 * Classifies the individuals classes.
		 *  ================================================== */	
		return OKCoApp.classifyIndividualsClasses(classes);	  
	}
	
	@RequestMapping(value="/classifyInstanceProperty", method = RequestMethod.POST)
	public @ResponseBody DtoResult classifyInstanceProperty(@RequestBody final DtoClassifyInstancePost dto) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{
		String[] properties = dto.arraySubProp.split("%&&%");
		
		/** ==================================================
		 * Classifies the individuals properties.
		 *  ================================================== */	
		return OKCoApp.classifyIndividualsProperties(properties, dto);	  
	}
}
