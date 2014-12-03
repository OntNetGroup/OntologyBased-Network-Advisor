package br.com.padtec.okco.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.padtec.common.application.OKCoApp;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.URIDecoder;

@Controller
public class DetailingController {

	@RequestMapping(method = RequestMethod.GET, value="/details")
	public String details(@RequestParam("uri") String uri, HttpServletRequest request) 
	{
		/** Decode URI First */
		uri = URIDecoder.decodeURI(uri);
		
		/** ==================================================
		 *  List All Individuals
		 *  =================================================== */
		List<DtoInstance> allIndividuals = OKCoApp.getIndividuals(true, true, true);
		request.getSession().setAttribute("listInstances", allIndividuals);
		
		/** ==================================================
		 * Select a Specific Individual
		 *  =================================================== */
		DtoInstance selectedIndividual = OKCoApp.selectIndividual(uri);
		OKCoApp.setClassSpecializationsInSelected();
		OKCoApp.setRelationSpecializationsInSelected();
		OKCoApp.setDefinitionsInSelected(OntCardinalityEnum.SOME);
		OKCoApp.setDefinitionsInSelected(OntCardinalityEnum.MIN);
		OKCoApp.setDefinitionsInSelected(OntCardinalityEnum.MAX);
		OKCoApp.setDefinitionsInSelected(OntCardinalityEnum.EXACTLY);
		
		
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
		 * List of Properties and SubProperties of the Selected Individual
		 *  =================================================== */	
		List<DtoPropertyAndSubProperties> ListSpecializationProperties = OKCoApp.getPropertiesFromSelected();
		request.getSession().setAttribute("ListSpecializationProperties", ListSpecializationProperties);
		
		List<DtoInstanceRelation> listRelations = OKCoApp.getRelationsFromSelected();	
		request.getSession().setAttribute("instanceListRelations", listRelations);		
		
		return "details";
	}
}
