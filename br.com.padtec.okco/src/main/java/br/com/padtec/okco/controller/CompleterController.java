package br.com.padtec.okco.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.padtec.common.application.CommiterApp;
import br.com.padtec.common.application.OKCoApp;
import br.com.padtec.common.application.UploadApp;
import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.exceptions.OKCoException;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.URIDecoder;

/**
 * Controller responsible for the functionality of Completing the knowledge.
 * See this class: {@link CommiterApp} 
 */

@Controller
public class CompleterController {
		
	@RequestMapping(method = RequestMethod.GET, value="/completeInstanceAuto")
	public String completeInstanceAuto(@RequestParam("uriInstance") String uriInstance, HttpServletRequest request)
	{
		/** Decode URIs First */
		uriInstance = URIDecoder.decodeURI(uriInstance);
		
		/** ==================================================
		 * Select a Specific Individual
		 *  =================================================== */
		DtoInstance selectedIndividual = OKCoApp.selectIndividual(uriInstance);
		String selectedIndividualURI = selectedIndividual.ns+selectedIndividual.name;
		OKCoApp.setIsModified(selectedIndividualURI);
		
		/** ==================================================
		 * Complete Individuals Automatically
		 *  =================================================== */								
		CommiterApp.createNewIndividualsAutomatically(selectedIndividual);
				
		/** ==================================================
		 *  Bring all the modification from the Base Model to the Inferred Model (OntModel -> InfModel).
		 *  This is done since all the retrieving of information is performed in the inferred model but all the Modifications in the base model.  
		 *  In other words: update the InfModel without calling the reasoner but copying the OntModel to it.
		 *  =================================================== */
		UploadApp.substituteInferredModelFromBaseModel(false);
		
		return "redirect:list";
	}
	
	/** This function works only with object properties: min, exactly and some properties */
	@RequestMapping(method = RequestMethod.GET, value="/completePropertyAuto")
	public String completePropertyAuto(@RequestParam("uriInstance") String uriInstance, @RequestParam("idDefinition") String uriProperty, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) 
	{
		/** Decode URIs First */				
		uriInstance = URIDecoder.decodeURI(uriInstance);
		uriProperty = URIDecoder.decodeURI(uriProperty);
		
		/** ==================================================
		 * Select a Specific Individual
		 *  =================================================== */
		DtoInstance selectedIndividual = OKCoApp.selectIndividual(uriInstance);
		String selectedIndividualURI = selectedIndividual.ns+selectedIndividual.name;
		OKCoApp.setIsModified(selectedIndividualURI);
		
		/** ==================================================
		 * Select a Specific class definition from the selected individual
		 *  =================================================== */
		DtoDefinitionClass classDefinitionSelected = OKCoApp.selectDefinitionFromSelected(uriProperty);
		OntCardinalityEnum typeRelation = classDefinitionSelected.TypeCompletness;

		if(type.equals("object"))
		{
			if(typeRelation.equals(OntCardinalityEnum.SOME))
			{
				int individualsNumber = QueryUtil.getIndividualsURI(UploadApp.getInferredModel(), classDefinitionSelected.Target).size();
				/** ==================================================
				 * Create a New Individual at the Range of the Selected Class Definition
				 *  =================================================== */				
				CommiterApp.createNewIndividualAtClassDefinitionRangeSelected(individualsNumber, null);
			}
			else if(typeRelation.equals(OntCardinalityEnum.MIN))
			{
				int individualsNumber = QueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), selectedIndividualURI, classDefinitionSelected.Relation, classDefinitionSelected.Target);
				ArrayList<String> listDifferentFrom = new ArrayList<String>();
				while(individualsNumber < Integer.parseInt(classDefinitionSelected.Cardinality))
				{
					/** ==================================================
					 * Create a New Individual at the Range of the Selected Class Definition
					 *  =================================================== */
					CommiterApp.createNewIndividualAtClassDefinitionRangeSelected(individualsNumber, listDifferentFrom);					
									
					individualsNumber ++;
				}				
			} 
			else if(typeRelation.equals(OntCardinalityEnum.EXACTLY))
			{
				int individualsNumber = QueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), selectedIndividualURI, classDefinitionSelected.Relation, classDefinitionSelected.Target);				
				ArrayList<String> listDifferentFrom = new ArrayList<String>();
				if(individualsNumber < Integer.parseInt(classDefinitionSelected.Cardinality))
				{
					while(individualsNumber < Integer.parseInt(classDefinitionSelected.Cardinality))
					{
						/** ==================================================
						 * Create a New Individual at the Range of the Selected Class Definition
						 *  =================================================== */
						CommiterApp.createNewIndividualAtClassDefinitionRangeSelected(individualsNumber, listDifferentFrom);	
						
						individualsNumber ++;
					}
				}				
			}			
		}		

		/** ==================================================
		 *  Bring all the modification from the Base Model to the Inferred Model (OntModel -> InfModel).
		 *  This is done since all the retrieving of information is performed in the inferred model but all the Modifications in the base model.  
		 *  In other words: update the InfModel without calling the reasoner but copying the OntModel to it.
		 *  =================================================== */
		UploadApp.substituteInferredModelFromBaseModel(false);

		return "redirect:list";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/completeProperty")
	public String completeProperty(@RequestParam("uriInstance") String uriInstance, @RequestParam("idDefinition") String uriProperty, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) throws OKCoException  
	{		
		/** Decode URIs First */
		uriInstance = URIDecoder.decodeURI(uriInstance);
		uriProperty = URIDecoder.decodeURI(uriProperty);
		
		/** ==================================================
		 * Select a Specific Individual
		 *  =================================================== */
		DtoInstance selectedIndividual = OKCoApp.selectIndividual(uriInstance);
		request.getSession().setAttribute("instanceSelected", selectedIndividual);

		/** ==================================================
		 * Select a Specific class definition from the selected individual
		 *  =================================================== */		
		DtoDefinitionClass classDefinitionSelected = OKCoApp.selectDefinitionFromSelected(uriProperty);
		request.getSession().setAttribute("definitionSelected", classDefinitionSelected);

		request.getSession().setAttribute("propType", propType);
		if(type.equals("object"))
		{					
			/** ==================================================
			 * List of Individuals that are connected to the Selected Individual through the Selected Class Definition relation
			 *  =================================================== */
			List<DtoInstance> individualsInClassDefinition = OKCoApp.getIndividualsAtClassDefinitionRangeSelected();
			request.getSession().setAttribute("listInstancesInRelation", individualsInClassDefinition);
						
			/** ==================================================
			 *  List All Individuals
			 *  =================================================== */
			List<DtoInstance> allIndividuals = OKCoApp.getIndividuals(true, true, true);
			request.getSession().setAttribute("listInstances", allIndividuals);
			
			/** ==================================================
			 *  List All Individuals Except the Individual Selected for Same/Different List
			 *  =================================================== */
			request.getSession().setAttribute("listInstancesSameDifferent", allIndividuals);
			
			return "completePropertyObject";
		} 
		else if (type.equals("objectMax"))
		{
			/** ==================================================
			 * List of Individuals that are connected to the Selected Individual through the Selected Class Definition relation
			 *  =================================================== */
			List<DtoInstance> individualsInClassDefinition = OKCoApp.getIndividualsAtClassDefinitionRangeSelected();
			request.getSession().setAttribute("listInstancesInRelation", individualsInClassDefinition);
			
			return "completePropertyObjectMaxCard";
		} 
		else if (type.equals("data"))
		{
			/** ==================================================
			 * List of Data Property Values that are connected to the Selected Individual through the Selected Class Definition relation
			 *  =================================================== */
			List<DataPropertyValue> listValuesInRelation = OKCoApp.getDataValuesAtClassDefinitionRangeSelected();			
			request.getSession().setAttribute("listValuesInRelation", listValuesInRelation);
			
			return "completePropertyData";
		}else{
			return "index";
		}
	}


}
