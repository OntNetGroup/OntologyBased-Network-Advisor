package br.com.padtec.advisor.controller.okco;

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
import br.com.padtec.okco.core.application.OKCoComponents;
import br.com.padtec.okco.core.exception.OKCoException;

/**
 * Controller responsible for the detailing of individuals. 
 */

@Controller
public class OKCoDetailingController {

	@RequestMapping(method = RequestMethod.GET, value="/okco-details")
	public String details(@RequestParam("uri") String uri, HttpServletRequest request) throws OKCoException 
	{
		/** Decode URI First */
		uri = URIDecoder.decodeURI(uri);
		
		/** ==================================================
		 * Select a Specific Individual
		 *  =================================================== */
		DtoInstance selectedIndividual = OKCoComponents.selector.selectIndividual(uri);
		request.getSession().setAttribute("instanceSelected", selectedIndividual);
				
		/** ==================================================
		 * List of Cardinality Details of the Selected Individual
		 *  =================================================== */				
		List<DtoDefinitionClass> listSomeClassDefinition = OKCoComponents.selector.getSomeDefinitionsFromSelected();
		List<DtoDefinitionClass> listMinClassDefinition = OKCoComponents.selector.getMinDefinitionsFromSelected();	
		List<DtoDefinitionClass> listMaxClassDefinition = OKCoComponents.selector.getMaxDefinitionsFromSelected();
		List<DtoDefinitionClass> listExactlyClassDefinition = OKCoComponents.selector.getExactDefinitionsFromSelected();
		request.getSession().setAttribute("listSomeClassDefinition", listSomeClassDefinition);
		request.getSession().setAttribute("listMinClassDefinition", listMinClassDefinition);
		request.getSession().setAttribute("listMaxClassDefinition", listMaxClassDefinition);
		request.getSession().setAttribute("listExactlyClassDefinition", listExactlyClassDefinition);
				
		/** ==================================================
		 * List of relations and Sub-relations of the Selected Individual
		 *  =================================================== */	
		List<DtoPropertyAndSubProperties> ListSpecializationProperties = OKCoComponents.selector.getRelationSpecializationsFromSelected();
		request.getSession().setAttribute("ListSpecializationProperties", ListSpecializationProperties);
		
		/** ==================================================
		 * List of relations and Sub-relations of the Selected Individual
		 *  =================================================== */	
		OKCoComponents.selector.getCompleteClassesFromSelected();
		
		/** ==================================================
		 * List of relations of the Selected Individual
		 *  =================================================== */
		List<DtoInstanceRelation> listRelations = OKCoComponents.selector.getRelationsFromSelected();	
		request.getSession().setAttribute("instanceListRelations", listRelations);		
		
		/** ==================================================
		 *  List All Individuals
		 *  =================================================== */
		List<DtoInstance> allIndividuals = OKCoComponents.selector.getIndividuals(true, true, true);
		request.getSession().setAttribute("listInstances", allIndividuals);
		
		return "okco-details";
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
			return OKCoComponents.selector.getPropertyWithNextAndPreviousFromSelected(uriProperty);	
		}
		return null;
	}
}
