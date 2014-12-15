package br.com.padtec.okco.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoGetPrevNextSpecProperty;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.types.URIDecoder;
import br.com.padtec.okco.core.application.OKCoApp;
import br.com.padtec.okco.core.exception.OKCoException;

/**
 * Controller responsible for the detailing of individuals. 
 */

@Controller
public class DetailingController {

	@RequestMapping(method = RequestMethod.GET, value="/details")
	public String details(@RequestParam("uri") String uri, HttpServletRequest request) throws OKCoException 
	{
		/** Decode URI First */
		uri = URIDecoder.decodeURI(uri);
		
		/** ==================================================
		 * Select a Specific Individual
		 *  =================================================== */
		DtoInstance selectedIndividual = OKCoApp.selectIndividual(uri);
		request.getSession().setAttribute("instanceSelected", selectedIndividual);
				
		/** ==================================================
		 * List of Cardinality Details of the Selected Individual
		 *  =================================================== */				
		List<DtoDefinitionClass> listSomeClassDefinition = OKCoApp.getSomeDefinitionsFromSelected();
		List<DtoDefinitionClass> listMinClassDefinition = OKCoApp.getMinDefinitionsFromSelected();	
		List<DtoDefinitionClass> listMaxClassDefinition = OKCoApp.getMaxDefinitionsFromSelected();
		List<DtoDefinitionClass> listExactlyClassDefinition = OKCoApp.getExactDefinitionsFromSelected();
		request.getSession().setAttribute("listSomeClassDefinition", listSomeClassDefinition);
		request.getSession().setAttribute("listMinClassDefinition", listMinClassDefinition);
		request.getSession().setAttribute("listMaxClassDefinition", listMaxClassDefinition);
		request.getSession().setAttribute("listExactlyClassDefinition", listExactlyClassDefinition);
				
		/** ==================================================
		 * List of relations and Sub-relations of the Selected Individual
		 *  =================================================== */	
		List<DtoPropertyAndSubProperties> ListSpecializationProperties = OKCoApp.getRelationSpecializationsFromSelected();
		request.getSession().setAttribute("ListSpecializationProperties", ListSpecializationProperties);
		
		/** ==================================================
		 * List of relations and Sub-relations of the Selected Individual
		 *  =================================================== */	
		OKCoApp.getCompleteClassesFromSelected();
		
		/** ==================================================
		 * List of relations of the Selected Individual
		 *  =================================================== */
		List<DtoInstanceRelation> listRelations = OKCoApp.getRelationsFromSelected();	
		request.getSession().setAttribute("instanceListRelations", listRelations);		
		
		/** ==================================================
		 *  List All Individuals
		 *  =================================================== */
		List<DtoInstance> allIndividuals = OKCoApp.getIndividuals(true, true, true);
		request.getSession().setAttribute("listInstances", allIndividuals);
		
		return "details";
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
