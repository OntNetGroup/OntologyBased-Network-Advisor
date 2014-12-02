package br.com.padtec.okco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.application.OKCoApp;
import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoCommitMaxCard;
import br.com.padtec.common.dto.DtoCommitPost;
import br.com.padtec.common.dto.DtoCreateDataValuePost;
import br.com.padtec.common.dto.DtoCreateInstancePost;
import br.com.padtec.common.dto.DtoGetPrevNextSpecProperty;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.dto.DtoViewSelectInstance;
import br.com.padtec.common.types.URIDecoder;

@Controller
public class FactoryController {
				
	@RequestMapping(value="/createInstance", method = RequestMethod.POST)
	public @ResponseBody DtoInstance createInstance(@RequestBody final DtoCreateInstancePost dto)
	{
		String name = dto.name;
		String[] arraySame = dto.arraySame.split("%&&%");
		String[] arrayDif = dto.arrayDif.split("%&&%");
		
		/** ==================================================
		 * Create a New Individual. It does not add the individual to the model. 
		 * Instead, it adds this individual to the set of list of new individuals to be created later on.
		 *  =================================================== */
		return OKCoApp.createNewIndividualAtCommitList(name, arraySame, arrayDif);		
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
			OKCoApp.removeNewIndividualFromCommitList(uri);
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
			return OKCoApp.getEditingIndividualFromCommitList(uri);
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
			return OKCoApp.getEditingIndividualFromModel(uri);
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
			return OKCoApp.addExistingIndividualAtCommitList(uri);
		}

		return null;
	}
	
	@RequestMapping(value="/removeDataValue", method = RequestMethod.GET)
	public @ResponseBody String removeDataValue(@RequestParam String id) 
	{
		if(id != null)
		{
			/** Decode URIs First */
			id = URIDecoder.decodeURI(id);
			
			/** ==================================================
			 * Remove the data values that was going to be created later. 
			 * This data value is not in the model yet. Thus, this method only removes it from the list of new data values.	 
			 *  =================================================== */
			OKCoApp.removeNewDataValueFromCommitList(id);
			
			return id;
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
		return OKCoApp.createNewDataValueAtCommitList(dto.value);		
	}
	
	@RequestMapping(value="/commitInstance", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitInstance(@RequestBody final DtoCommitPost dtoCommit) 
	{				
		/** ==================================================
		 * Performs the commit of the new individuals to be created.
		 * It updates the inferred model from the base model using or not the inference engine.
		 *  =================================================== */
		return OKCoApp.commitNewIndividuals(dtoCommit.commitReasoner);		
	}
	
	@RequestMapping(value="/commitMaxCard", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitMaxCard(@RequestBody final DtoCommitMaxCard dto)
	{    
		/** ==================================================
		 * Performs the commit of the max cardinalities. 
		 * It updates the inferred model from the base model using or not the inference engine.
		 *  =================================================== */
		return OKCoApp.commitMaxCardinalities(dto);
	}
	
	@RequestMapping(value="/commitDataValues", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitDataValues(@RequestBody final DtoCommitPost dtoCommit) 
	{     
		/** ==================================================
		 * Performs the commit of the new data values to be created.
		 * It updates the inferred model from the base model using or not the inference engine.
		 *  ================================================== */	
		return OKCoApp.commitDataValues(dtoCommit.commitReasoner);
	}
	
	@RequestMapping(value="/selectSpecializationProp", method = RequestMethod.GET)
	public @ResponseBody DtoGetPrevNextSpecProperty selectSpecializationProp(@RequestParam String uriProperty) 
	{    
		if(uriProperty != null)
		{
			/** Decode URIs First */
			uriProperty = URIDecoder.decodeURI(uriProperty);
			
			/** ==================================================
			 * Property with the Next and Previous properties from the selected property
			 *  ================================================== */	
			return OKCoApp.getPropertyWithNextAndPreviousFromSelected(uriProperty);	
		}
		return null;
	}


}
