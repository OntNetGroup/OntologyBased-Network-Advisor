package br.com.padtec.okco.controller;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.dto.DtoClassifyInstancePost;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.ClassifierApp;
import br.com.padtec.okco.core.application.UploadApp;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;

/**
 * Controller responsible for the functionality of Classifying the instances and relations.
 * See this class: {@link ClassifierApp} 
 */

@Controller
public class ClassifierController {

	@RequestMapping(value="/classifyInstanceClasses", method = RequestMethod.POST)
	public @ResponseBody DtoResult classifyInstanceClasses(@RequestBody final DtoClassifyInstancePost dto) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{    
		String[] classes = dto.arrayCls.split("%&&%");
		
		/** ==================================================
		 * Classifies the individuals classes.
		 *  ================================================== */	
		DtoResult dtoResult = ClassifierApp.classifyIndividualsClasses(classes);
		
		/** ==================================================
		 *  Bring all the modification from the Base Model to the Inferred Model (OntModel -> InfModel).
		 *  This is done since all the retrieving of information is performed in the inferred model but all the Modifications in the base model.  
		 *  In other words: update the InfModel without calling the reasoner but copying the OntModel to it.
		 *  =================================================== */
		if(dtoResult.isSucceed()) UploadApp.substituteInferredModelFromBaseModel(false);
		
		return dtoResult;		
	}
	
	@RequestMapping(value="/classifyInstanceProperty", method = RequestMethod.POST)
	public @ResponseBody DtoResult classifyInstanceProperty(@RequestBody final DtoClassifyInstancePost dto) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{
		String[] properties = dto.arraySubProp.split("%&&%");
		
		/** ==================================================
		 * Classifies the individuals properties.
		 *  ================================================== */	
		DtoResult dtoResult = ClassifierApp.classifyIndividualsProperties(properties, dto);	
		
		/** ==================================================
		 *  Bring all the modification from the Base Model to the Inferred Model (OntModel -> InfModel).
		 *  This is done since all the retrieving of information is performed in the inferred model but all the Modifications in the base model.  
		 *  In other words: update the InfModel without calling the reasoner but copying the OntModel to it.
		 *  =================================================== */
		if(dtoResult.isSucceed()) UploadApp.substituteInferredModelFromBaseModel(false);
		
		return dtoResult;	
	}
}
