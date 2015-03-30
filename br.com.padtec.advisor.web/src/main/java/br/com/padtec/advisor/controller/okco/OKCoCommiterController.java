package br.com.padtec.advisor.controller.okco;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoCommitMaxCard;
import br.com.padtec.common.dto.DtoCommitPost;
import br.com.padtec.common.dto.DtoCreateDataValuePost;
import br.com.padtec.common.dto.DtoCreateInstancePost;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.dto.DtoViewSelectInstance;
import br.com.padtec.common.types.URIDecoder;
import br.com.padtec.okco.core.application.OKCoCommiter;
import br.com.padtec.okco.core.application.OKCoComponents;
import br.com.padtec.okco.core.exception.OKCoException;

/**
 * Controller responsible for the create/deletion/update and commit of new individuals and relations.
 * See this class: {@link OKCoCommiter} 
 */

@Controller
public class OKCoCommiterController {
				
	@RequestMapping(value="/createInstance", method = RequestMethod.POST)
	public @ResponseBody DtoInstance createInstance(@RequestBody final DtoCreateInstancePost dto) throws OKCoException
	{
		String name = dto.name;
		 
		String[] arraySame = null;
		String[] arrayDif = null;
		
		if(dto.arraySame!=null) arraySame = dto.arraySame.split("%&&%");
		if(dto.arrayDif!=null) arrayDif = dto.arrayDif.split("%&&%");
		
		/** ==================================================
		 * Create a New Individual. It does not add the individual to the model. 
		 * Instead, it adds this individual to the set of list of new individuals to be created later on.
		 *  =================================================== */
		return OKCoComponents.commiter.createNewIndividualAtCommitList(name, arraySame, arrayDif);		
	}

	@RequestMapping(value="/removeInstance", method = RequestMethod.GET)
	public @ResponseBody String removeInstance(@RequestParam String uri) 
	{
		if(uri != null)
		{
			/** Decode URIs First */
			uri = URIDecoder.decodeURI(uri);
			
			/** ==================================================
			 * Remove the recent individual that was going to be created later. 
			 * This individual is not in the model yet. Thus, this method only removes it from the list of new individuals.
			 *  =================================================== */
			OKCoComponents.commiter.removeNewIndividualFromCommitList(uri);
		}
		return uri;		  
	}
		
	@RequestMapping(value="/editInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance editInstance(@RequestParam String uri) 
	{
		if(uri!=null)
		{			
			/** Decode URIs First */
			uri = URIDecoder.decodeURI(uri);
			
			/** ==================================================
			 * Get individual in the commit list as an Editing Element.
			 *  ================================================== */			 
			return OKCoComponents.commiter.getEditingIndividualFromCommitList(uri);
		}
		return null;	  
	}
	
	@RequestMapping(value="/selectInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance selectInstance(@RequestParam String uri) 
	{
		if(uri != null)
		{
			/** Decode URIs First */
			uri = URIDecoder.decodeURI(uri);
			
			/** ==================================================
			 * Get individual in the model as an Editing Element.
			 *  ================================================== */			 
			return OKCoComponents.commiter.getEditingIndividualFromModel(uri);
		}

		return null;
	}
		
	@RequestMapping(value="/selectInstanceAdd", method = RequestMethod.GET)
	public @ResponseBody DtoInstance selectInstanceAdd(@RequestParam String uri) 
	{
		if(uri != null)
		{
			/** Decode URIs First */
			uri = URIDecoder.decodeURI(uri);
			
			/** ==================================================
			 * Add existing individual to the Commit List
			 *  ================================================== */	
			return OKCoComponents.commiter.addExistingIndividualAtCommitList(uri);
		}

		return null;
	}
			
	@RequestMapping(value="/commitInstance", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitInstance(@RequestBody final DtoCommitPost dtoCommit) 
	{				
		/** ==================================================
		 * Performs the commit of the new individuals to be created.
		 * It updates the inferred model from the base model using or not the inference engine.
		 *  =================================================== */
		return OKCoComponents.commiter.commitNewIndividuals(dtoCommit.commitReasoner);		
	}
	
	@RequestMapping(value="/commitMaxCard", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitMaxCard(@RequestBody final DtoCommitMaxCard dto)
	{    
		/** ==================================================
		 * Performs the commit of the max cardinalities. 
		 * It updates the inferred model from the base model using or not the inference engine.
		 *  =================================================== */
		return OKCoComponents.commiter.commitMaxCardinalities(dto);
	}
	
	@RequestMapping(value="/removeDataValue", method = RequestMethod.GET)
	public @ResponseBody String removeDataValue(@RequestParam String uri) 
	{
		if(uri != null)
		{
			/** Decode URIs First */
			uri = URIDecoder.decodeURI(uri);
			
			/** ==================================================
			 * Remove the data values that was going to be created later. 
			 * This data value is not in the model yet. Thus, this method only removes it from the list of new data values.	 
			 *  =================================================== */
			OKCoComponents.commiter.removeNewDataValueFromCommitList(uri);
			
			return uri;
		}
		return null;		  
	}

	@RequestMapping(value="/createDataValue", method = RequestMethod.POST)
	public @ResponseBody DataPropertyValue createDataValue(@RequestBody final DtoCreateDataValuePost dto)
	{
		/** ==================================================
		 * Create a New Data Value. It does not add the data value to the model. 
		 * Instead, it adds this value to the set of list of new data values to be created later on.
		 *  =================================================== */
		return OKCoComponents.commiter.createNewDataValueAtCommitList(dto.value);		
	}
	
	@RequestMapping(value="/commitDataValues", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitDataValues(@RequestBody final DtoCommitPost dtoCommit) 
	{     
		/** ==================================================
		 * Performs the commit of the new data values to be created.
		 * It updates the inferred model from the base model using or not the inference engine.
		 *  ================================================== */	
		return OKCoComponents.commiter.commitDataValues(dtoCommit.commitReasoner);
	}
}
