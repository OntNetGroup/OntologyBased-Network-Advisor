package br.com.padtec.okco.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.application.OKCoApp;
import br.com.padtec.common.application.UploadApp;
import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoClassifyInstancePost;
import br.com.padtec.common.dto.DtoCommitMaxCard;
import br.com.padtec.common.dto.DtoCommitPost;
import br.com.padtec.common.dto.DtoCreateDataValuePost;
import br.com.padtec.common.dto.DtoCreateInstancePost;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoGetPrevNextSpecProperty;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.dto.DtoViewSelectInstance;
import br.com.padtec.common.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.common.graph.GraphPlotting;
import br.com.padtec.common.graph.WOKCOGraphPlotting;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;

@Controller
public class OKCoController {

	@RequestMapping(method = RequestMethod.GET, value="/list")
	public String list(HttpServletRequest request) 
	{
		/** ==================================================
		 *  List All Individuals and Which were modified
		 *  =================================================== */
		List<DtoInstance> allIndividuals = OKCoApp.getIndividuals(true, false, false);			
		List<String> modifiedIndividuals = OKCoApp.getModifiedIndividuals();

		if(allIndividuals != null) {
			request.getSession().setAttribute("listInstances", allIndividuals);
			request.getSession().setAttribute("listModifedInstances", modifiedIndividuals);
			return "list";
		} else{
			request.getSession().setAttribute("loadOk", "false");
			return "index";
		}
	}
	
	/** Decode URI */
	private String decodeURI(String uri)
	{
		try {
			uri = URLDecoder.decode(uri,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return uri;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/details")
	public String details(@RequestParam("uri") String uri, HttpServletRequest request) 
	{
		/** Decode URI First */
		uri = decodeURI(uri);
		
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
		
	@RequestMapping(method = RequestMethod.GET, value="/completeProperty")
	public String completeProperty(@RequestParam("idDefinition") String uriProperty, @RequestParam("uriInstance") String uriInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) 
	{		
		/** Decode URIs First */
		uriProperty = decodeURI(uriProperty);		
		uriInstance = decodeURI(uriInstance);
		
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

	/**
	 * ATTENTION: This function works only with object properties: min, exactly and some properties
	 */
	@RequestMapping(method = RequestMethod.GET, value="/completePropertyAuto")
	public String completePropertyAuto(@RequestParam("idDefinition") String uriProperty, @RequestParam("uriInstance") String uriInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) 
	{
		/** Decode URIs First */
		uriProperty = decodeURI(uriProperty);		
		uriInstance = decodeURI(uriInstance);
		
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
				OKCoApp.createNewIndividualAtClassDefinitionRangeSelected(individualsNumber, null);
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
					OKCoApp.createNewIndividualAtClassDefinitionRangeSelected(individualsNumber, listDifferentFrom);					
									
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
						OKCoApp.createNewIndividualAtClassDefinitionRangeSelected(individualsNumber, listDifferentFrom);	
						
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

	@RequestMapping(method = RequestMethod.GET, value="/completeInstanceAuto")
	public String completeInstanceAuto(@RequestParam("uriInstance") String uriInstance, HttpServletRequest request)
	{
		/** Decode URIs First */
		uriInstance = decodeURI(uriInstance);
		
		/** ==================================================
		 * Select a Specific Individual
		 *  =================================================== */
		DtoInstance selectedIndividual = OKCoApp.selectIndividual(uriInstance);
		String selectedIndividualURI = selectedIndividual.ns+selectedIndividual.name;
		OKCoApp.setIsModified(selectedIndividualURI);
		
		/** ==================================================
		 * Complete Individuals Automatically
		 *  =================================================== */								
		OKCoApp.createNewIndividualsAutomatically(selectedIndividual);
				
		/** ==================================================
		 *  Bring all the modification from the Base Model to the Inferred Model (OntModel -> InfModel).
		 *  This is done since all the retrieving of information is performed in the inferred model but all the Modifications in the base model.  
		 *  In other words: update the InfModel without calling the reasoner but copying the OntModel to it.
		 *  =================================================== */
		UploadApp.substituteInferredModelFromBaseModel(false);
		
		return "redirect:list";
	}

	@RequestMapping(method = RequestMethod.GET, value="/graphVisualizer")
	public String graphVisualizer(@RequestParam("uri") String uri, @RequestParam("typeView") String typeView, HttpServletRequest request) 
	{
		/** Decode URIs First */
		uri = decodeURI(uri);
		
		/** ==================================================
		 * Get Values of Graph
		 *  =================================================== */
		GraphPlotting graphPlotting = new WOKCOGraphPlotting();
		String valuesGraph = OKCoApp.getGraphValues(uri,typeView,graphPlotting);
		request.getSession().setAttribute("valuesGraph", valuesGraph);
	
		/** ==================================================
		 * Get Width, Height and Subtitle of Graph
		 *  =================================================== */
		int width  = graphPlotting.width;
		int height = graphPlotting.height;
		String subtitle = graphPlotting.getSubtitle();		
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("subtitle", subtitle);
		
		return "graphVisualizer";
	}
			
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
			uri = decodeURI(uri);
		
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
			uri = decodeURI(uri);
			
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
			uri = decodeURI(uri);
			
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
			uri = decodeURI(uri);
			
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
			id = decodeURI(id);
			
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
	
	@RequestMapping(value="/runReasoner", method = RequestMethod.POST)
	public @ResponseBody DtoResult runReasoner() 
	{
		/** ==================================================
		 * Running the reasoner, storing the temporary model and cleaning the list of modified
		 *  =================================================== */
		return OKCoApp.runReasoner();		
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
	
	@RequestMapping(value="/selectSpecializationProp", method = RequestMethod.GET)
	public @ResponseBody DtoGetPrevNextSpecProperty selectSpecializationProp(@RequestParam String uriProperty) 
	{    
		if(uriProperty != null)
		{
			/** Decode URIs First */
			uriProperty = decodeURI(uriProperty);
			
			/** ==================================================
			 * Property with the Next and Previous properties from the selected property
			 *  ================================================== */	
			return OKCoApp.getPropertyWithNextAndPreviousFromSelected(uriProperty);	
		}
		return null;
	}


}
