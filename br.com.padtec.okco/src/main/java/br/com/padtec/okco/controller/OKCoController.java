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
import br.com.padtec.common.factory.DtoFactoryUtil;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.graph.GraphPlotting;
import br.com.padtec.common.graph.WOKCOGraphPlotting;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.OntPropertyEnum;

import com.hp.hpl.jena.ontology.OntModel;

@Controller
//@RequestMapping("/instance")
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
		OKCoApp.getClassSpecializationsFromSelected();
		OKCoApp.getRelationSpecializationsFromSelected();
		
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
		 *  This is done since all the retrieve of information is performed in the inferred model and all the modifications in the base model.  
		 *  In other words: Update InfModel without calling the reasoner but copying the OntModel.
		 *  =================================================== */
		UploadApp.substituteInferredModelFromBaseModel();

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
		 *  This is done since all the retrieve of information is performed in the inferred model and all the modifications in the base model.  
		 *  In other words: Update InfModel without calling the reasoner but copying the OntModel.
		 *  =================================================== */
		UploadApp.substituteInferredModelFromBaseModel();
		
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

	//====================================================================
	//====================================================================
	//====================================================================
	//====================================================================
	
	
	/*------ AJAX - ObjectProperty -----*/
	@RequestMapping(value="/createInstance", method = RequestMethod.POST)
	public @ResponseBody DtoInstance createInstance(@RequestBody final DtoCreateInstancePost dto)
	{
		String separatorValues = "%&&%";

		/* 0 -> name
		 * 1 -> arraySame
		 * 2 -> arrayDif
		 * */
		String name = dto.name;
		String[] arraySame = dto.arraySame.split(separatorValues);
		String[] arrayDif = dto.arrayDif.split(separatorValues);

		ArrayList<String> listSame = new ArrayList<String>();
		for (String s : arraySame) {			  
			if(!(s.equals("")))
				listSame.add(s);			
		}

		ArrayList<String> listDif = new ArrayList<String>();
		for (String s : arrayDif) {			  
			if(!(s.equals("")))
				listDif.add(s);			
		}

		DtoInstance i = new DtoInstance(UploadApp.baseRepository.getNameSpace(), name, new ArrayList<String>(), listDif, listSame, false);

		OKCoApp.listNewInstancesRelation.add(i);
		return i;
	}

	@RequestMapping(value="/commitInstance", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitInstance(@RequestBody final DtoCommitPost dtoCommit) {    

		boolean isCreate = false;
		boolean isUpdate = false;
		DtoResult dto = new DtoResult();
		if(OKCoApp.listNewInstancesRelation.size() != 0)
		{
			DtoInstance iSource = OKCoApp.individualSelected;
			for (DtoInstance iTarget : OKCoApp.listNewInstancesRelation) 
			{
				try {

					if(iTarget.existInModel == false)
					{
						//Create instance
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createIndividual(basemodel, iTarget.ns + iTarget.name, iTarget.ListSameInstances, iTarget.ListDiferentInstances, iSource.ns + iSource.name, OKCoApp.definitionClassSelected.Relation, OKCoApp.definitionClassSelected.Target);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
						isCreate = true;

					} else {

						//Selected instance
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createObjectProperty(basemodel, iSource.ns + iSource.name, OKCoApp.definitionClassSelected.Relation, iTarget.ns + iTarget.name);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
						
						isUpdate = true;
					}

					if(dtoCommit.commitReasoner.equals("true"))
					{
						//Update InfModel calling reasoner
						UploadApp.inferredRepository.setInferredModel(UploadApp.reasoner.run(UploadApp.getBaseModel()));

					} else {
						//Update InfModel without calling reasoner
						UploadApp.inferredRepository.setInferredModel(UploadApp.baseRepository.cloneReplacing(UploadApp.getBaseModel()));

						//Add on list modified instances
						OKCoApp.modifiedIndividualsURIs.add(iTarget.ns + iTarget.name);
					}			 

					//Update list
					//HomeController.UpdateLists();
					OKCoApp.updateAddingToLists(iTarget.ns + iTarget.name);

				} catch (Exception e) {

					if(isCreate == true)
						UploadApp.baseRepository.setBaseOntModel(DtoFactoryUtil.deleteIndividual(UploadApp.getBaseModel(),iTarget));

					if(isUpdate == true){
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.deleteObjectProperty(basemodel, iSource.ns + iSource.name, OKCoApp.definitionClassSelected.Relation, iTarget.ns + iTarget.name);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
					}
					dto.setMessage(e.getMessage());
					dto.setIsSucceed(false);
					return dto;
				}

			} // end for

			if(dtoCommit.commitReasoner.equals("true"))
			{

			} else {
				//Add on list modified instances
				OKCoApp.modifiedIndividualsURIs.add(iSource.ns + iSource.name);			
			}

			//Update list instances modified
			OKCoApp.updateModifiedList();

			dto.setIsSucceed(true);
			dto.setMessage("ok");
			return dto;
		}

		dto.setIsSucceed(true);
		dto.setMessage("nothing");
		return dto;
	}

	@RequestMapping(value="/runReasoner", method = RequestMethod.POST)
	public @ResponseBody DtoResult runReasoner() {    

		DtoResult dto = new DtoResult();
		try {

			//Run reasoner
			UploadApp.inferredRepository.setInferredModel(UploadApp.reasoner.run(UploadApp.getBaseModel()));

			//Save temporary model
			UploadApp.tempModel = UploadApp.baseRepository.cloneReplacing(UploadApp.getBaseModel());

			//Update list instances
			OKCoApp.updateLists();

			//Clean list modified instances
			OKCoApp.modifiedIndividualsURIs = new ArrayList<String>();

			//Update list instances modified
			OKCoApp.updateModifiedList();

		} catch (Exception e) {

			//Roll back the tempModel
			UploadApp.baseRepository.setBaseOntModel(UploadApp.baseRepository.cloneReplacing(UploadApp.tempModel));
			UploadApp.inferredRepository.setInferredModel(UploadApp.reasoner.run(UploadApp.getBaseModel()));

			//Update list instances
			try {
				OKCoApp.updateLists();

			} catch (InconsistentOntologyException e1) {

				e1.printStackTrace();

			} catch (OKCoExceptionInstanceFormat e1) {
				
				e1.printStackTrace();
			}

			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";

			dto.setMessage(error);
			dto.setIsSucceed(false);
			return dto;
		}

		dto.setIsSucceed(true);
		dto.setMessage("ok");
		return dto;
	}

	@RequestMapping(value="/removeInstance", method = RequestMethod.GET)
	public @ResponseBody String removeInstance(@RequestParam String uri) {    

		if(uri != null)
		{
			DtoInstance.removeFromList(OKCoApp.listNewInstancesRelation, uri);
			return uri;
		}

		return null;		  
	}

	public DtoInstance getInstance(List<DtoInstance> listInstances, String instanceName) {		
		
		for (DtoInstance instance : listInstances) {
			System.out.println("Comparing: "+instance.ns + instance.name);
			System.out.println("With: "+instanceName);
			if((instance.ns + instance.name).equals(instanceName))
			{
				return instance;
			}
		}
		
		return null;
	}
		
	@RequestMapping(value="/editInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance editInstance(@RequestParam String uri) {    

		if(uri!=null)
		{
			DtoInstance i = getInstance(OKCoApp.listNewInstancesRelation, uri);
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, OKCoApp.listNewInstancesRelation);
			return dto;
		}

		return null;	  
	}

	@RequestMapping(value="/selectInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance selectInstance(@RequestParam String uri) {    

		if(uri != null)
		{
			DtoInstance i = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uri);
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, OKCoApp.ListAllInstances);
			return dto;
		}

		return null;
	}

	@RequestMapping(value="/selectInstanceAdd", method = RequestMethod.GET)
	public @ResponseBody DtoInstance selectInstanceAdd(@RequestParam String uri) { 

		//Add in listNewInstancesRelation

		if(uri != null)
		{
			DtoInstance i = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uri);
			OKCoApp.listNewInstancesRelation.add(i);
			return i;
		}

		return null;
	}

	@RequestMapping(value="/commitMaxCard", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitMaxCard(@RequestBody final DtoCommitMaxCard dto){    
		
		DtoResult dtoResult = new DtoResult();
		
		try {
			
			String separatorValues = "%&&%";

			String[] arrayValues = dto.ListInstanceDifSameIds.split(separatorValues);
			for (String val : arrayValues) {

				if(val.contains("x"))
				{	
					String[] parts = val.split("x");

					String type = parts[0];
					String uriSource = parts[1];
					String uriTarget = parts[2];
					
					DtoInstance s1 = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriSource);
					DtoInstance s2 = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriTarget);
					
					if(type.equals("dif"))
					{
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.setDifferentFrom(basemodel, s1.ns + s1.name, s2.ns + s2.name);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
						
					} else if (type.equals("same"))
					{
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.setSameAs(basemodel, s1.ns + s1.name, s2.ns + s2.name);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
						
					} else {
						
						dtoResult.setMessage("error");
						dtoResult.setIsSucceed(false);
						return dtoResult;
					}
					
					OKCoApp.modifiedIndividualsURIs.add(s1.ns + s1.name);
					OKCoApp.modifiedIndividualsURIs.add(s2.ns + s2.name);
				}

			}
			
			if(dto.runReasoner.equals("true"))
			{
				try {

					//Run reasoner
					UploadApp.inferredRepository.setInferredModel(UploadApp.reasoner.run(UploadApp.getBaseModel()));

					//Save temporary model
					UploadApp.tempModel = UploadApp.baseRepository.cloneReplacing(UploadApp.getBaseModel());

					//Update list instances
					OKCoApp.updateLists();

					//Clean list modified instances
					OKCoApp.modifiedIndividualsURIs = new ArrayList<String>();

					//Update list instances modified
					OKCoApp.updateModifiedList();

				} catch (Exception e) {

					//Roll back the tempModel
					UploadApp.baseRepository.setBaseOntModel(UploadApp.baseRepository.cloneReplacing(UploadApp.tempModel));
					UploadApp.inferredRepository.setInferredModel( UploadApp.reasoner.run(UploadApp.getBaseModel()));

					//Update list instances
					try {
						OKCoApp.updateLists();

					} catch (InconsistentOntologyException e1) {

						//e1.printStackTrace();

					} 

					String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
					
					dtoResult.setMessage(error);
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
				
			} if(dto.runReasoner.equals("false")) {
				
				//Update list instances modified
				OKCoApp.updateModifiedList();
				
			} else {
				
				dtoResult.setMessage("error");
				dtoResult.setIsSucceed(false);
				return dtoResult;
			}
			
		} catch (Exception e) {
			
			dtoResult.setMessage("error");
			dtoResult.setIsSucceed(false);
			return dtoResult;
		}
		

		dtoResult.setMessage("ok");
		dtoResult.setIsSucceed(true);
		return dtoResult;
	}
	
	/*------ AJAX - DataProperty -----*/	

	@RequestMapping(value="/removeDataValue", method = RequestMethod.GET)
	public @ResponseBody String removeDataValue(@RequestParam String id) {    

		if(id != null)
		{
			DataPropertyValue.removeFromList(OKCoApp.listNewDataValuesRelation, id);
			return id;
		}

		return null;		  
	}

	@RequestMapping(value="/createDataValue", method = RequestMethod.POST)
	public @ResponseBody DataPropertyValue createDataValue(@RequestBody final DtoCreateDataValuePost dto){    

		DataPropertyValue data = new DataPropertyValue();
		data.value = dto.value;
		data.classValue = OKCoApp.definitionClassSelected.Target;
		data.existInModel = false;

		OKCoApp.listNewDataValuesRelation.add(data);
		return data;
	}

	@RequestMapping(value="/commitDataValues", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitDataValues(@RequestBody final DtoCommitPost dtoCommit) {    

		DtoResult dto = new DtoResult();
		if(OKCoApp.listNewDataValuesRelation.size() != 0)
		{
			DtoInstance iSource = OKCoApp.individualSelected;
			for (DataPropertyValue dataTarget : OKCoApp.listNewDataValuesRelation) 
			{
				try {

					if(dataTarget.existInModel == false)
					{
						//Create data value
						OntModel basemodel = UploadApp.getBaseModel();
						FactoryUtil.createRangeDataPropertyValue(basemodel, dataTarget.value, iSource.ns + iSource.name, OKCoApp.definitionClassSelected.Relation, OKCoApp.definitionClassSelected.Target);
						UploadApp.baseRepository.setBaseOntModel(basemodel);						
						dataTarget.existInModel = true;
					}

					if(dtoCommit.commitReasoner.equals("true"))
					{
						//Update InfModel calling reasoner
						UploadApp.inferredRepository.setInferredModel(UploadApp.reasoner.run(UploadApp.getBaseModel()));

					} else {
						//Update InfModel without calling reasoner
						UploadApp.inferredRepository.setInferredModel(UploadApp.baseRepository.cloneReplacing(UploadApp.getBaseModel()));

						//Add on list modified instances
						OKCoApp.modifiedIndividualsURIs.add(iSource.ns + iSource.name);
					}						 

					//Update list instances
					OKCoApp.updateLists();

				} catch (Exception e) {

					OntModel basemodel = UploadApp.getBaseModel();
					FactoryUtil.deleteRangeDataPropertyValue(basemodel, dataTarget.value, OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name, OKCoApp.definitionClassSelected.Relation, OKCoApp.definitionClassSelected.Target);
					UploadApp.baseRepository.setBaseOntModel(basemodel);	

					dto.setMessage(e.getMessage());
					dto.setIsSucceed(false);
					return dto;
				}
			} //end for

			//Update list instances modified
			OKCoApp.updateModifiedList();

			dto.setIsSucceed(true);
			dto.setMessage("ok");
			return dto;
		} //end if

		dto.setIsSucceed(true);
		dto.setMessage("nothing");
		return dto;
	}


	/*------ AJAX - Specializations -----*/	

	@RequestMapping(value="/classifyInstanceClasses", method = RequestMethod.POST)
	public @ResponseBody DtoResult classifyInstanceClasses(@RequestBody final DtoClassifyInstancePost dto) throws InconsistentOntologyException, OKCoExceptionInstanceFormat{    

		DtoResult dtoResult = new DtoResult();
		String separatorValues = "%&&%";

		/* 0 -> arrayCls */
		String[] arrayCls = dto.arrayCls.split(separatorValues);

		ArrayList<String> listCls = new ArrayList<String>();
		for (String s : arrayCls) {			  
			if(!(s.equals("")))
				listCls.add(s);			
		}

		if(listCls.size() > 0)
		{
			for (String cls : listCls) {

				try {

					OntModel basemodel = UploadApp.getBaseModel();
					FactoryUtil.createIndividualOfClass(basemodel, OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name, cls);
					UploadApp.baseRepository.setBaseOntModel(basemodel);
					
				} catch (Exception e) {

					dtoResult.setMessage(e.getMessage());
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
			}

			try {

				//Validate and update list
				OKCoApp.updateAddingToLists(OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name);;

				//Instance selected update
				OKCoApp.individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), OKCoApp.individualSelected .uri);

			} catch (Exception e) {

				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);

				//Remove all created
				for (String clsAux : listCls) {
					OntModel basemodel = UploadApp.getBaseModel();
					FactoryUtil.deleteIndividualOfClass(basemodel, OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name, clsAux);
					UploadApp.baseRepository.setBaseOntModel(basemodel);
				}

				//Validate and update list and infModel
				OKCoApp.updateLists();

				//Instance selected update
				OKCoApp.individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), OKCoApp.individualSelected .uri);

				return dtoResult;
			}	

			//Add on list modified instances
			OKCoApp.modifiedIndividualsURIs.add(OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name);

			//Update list instances modified
			OKCoApp.updateModifiedList();

			dtoResult.setIsSucceed(true);
			dtoResult.setMessage("ok");
			return dtoResult;
		}

		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("nothing");
		return dtoResult;		  
	}

	@RequestMapping(value="/classifyInstanceProperty", method = RequestMethod.POST)
	public @ResponseBody DtoResult classifyInstanceProperty(@RequestBody final DtoClassifyInstancePost dto) throws InconsistentOntologyException, OKCoExceptionInstanceFormat{

		DtoPropertyAndSubProperties dtoSpec = DtoPropertyAndSubProperties.getInstance(OKCoApp.propertiesFromSelected, dto.arraySubProp);

		DtoResult dtoResult = new DtoResult();
		String separatorValues = "%&&%";

		/* 0 -> arrayCls */
		String[] arraySubProp = dto.arraySubProp.split(separatorValues);

		ArrayList<String> listRelations = new ArrayList<String>();
		for (String s : arraySubProp) {			  
			if(!(s.equals("")))
				listRelations.add(s);			
		}

		if(listRelations.size() > 0)
		{
			for (String subRel : listRelations) {

				try {

					if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY)){
						//Case data property
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createRangeDataPropertyValue(basemodel, dtoSpec.iTargetNs.split("\\^\\^")[0], OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name, subRel, dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName);
						UploadApp.baseRepository.setBaseOntModel(basemodel);						
					}else{
						//Case object property
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createObjectProperty(basemodel, OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name,subRel, dtoSpec.iTargetNs + dtoSpec.iTargetName);
						UploadApp.baseRepository.setBaseOntModel(basemodel);						
					}
					
				} catch (Exception e) {

					dtoResult.setMessage(e.getMessage());
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
			}

			try {
				
				//Validate and update list
				OKCoApp.updateAddingToLists(OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name);

				//Instance selected update
				OKCoApp.individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), OKCoApp.individualSelected .uri);

			} catch (Exception e) {

				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);

				//Remove all created
				for (String subRelAux : listRelations) {

					if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY)){						
						//Case data property
						OntModel basemodel = UploadApp.getBaseModel();
						FactoryUtil.deleteRangeDataPropertyValue(basemodel, dtoSpec.iTargetNs.split("\\^\\^")[0], OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name, subRelAux, dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
					}else{
						//Case object property
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createObjectProperty(basemodel, OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name, subRelAux,dtoSpec.iTargetNs + dtoSpec.iTargetName);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
					}
				}

				//Validate and update list and infModel
				OKCoApp.updateLists();

				//Instance selected update
				OKCoApp.individualSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), OKCoApp.individualSelected .uri);

				return dtoResult;
			}			  

			//Add on list modified instances
			OKCoApp.modifiedIndividualsURIs.add(OKCoApp.individualSelected.ns + OKCoApp.individualSelected.name);

			//Update list instances modified
			OKCoApp.updateModifiedList();

			dtoResult.setIsSucceed(true);
			dtoResult.setMessage("ok");
			return dtoResult;
		}

		dtoResult.setIsSucceed(true);
		dtoResult.setMessage("nothing");
		return dtoResult;		  
	}

	@RequestMapping(value="/selectSpecializationProp", method = RequestMethod.GET)
	public @ResponseBody DtoGetPrevNextSpecProperty selectSpecializationProp(@RequestParam String uriProperty) {    

		if(uriProperty != null)
		{
			DtoPropertyAndSubProperties dto = DtoPropertyAndSubProperties.getInstance(OKCoApp.propertiesFromSelected, uriProperty);
			if(dto == null){

				return null;

			} else {

				boolean haveNext = false;
				boolean havePrev = false;
				DtoPropertyAndSubProperties dtoNext = DtoPropertyAndSubProperties.getInstance(OKCoApp.propertiesFromSelected, OKCoApp.propertiesFromSelected.get(OKCoApp.propertiesFromSelected.indexOf(dto)+1).Property);
				DtoPropertyAndSubProperties dtoPrev= DtoPropertyAndSubProperties.getInstance(OKCoApp.propertiesFromSelected, OKCoApp.propertiesFromSelected.get(OKCoApp.propertiesFromSelected.indexOf(dto)-1).Property);
				if(dtoNext != null)
					haveNext = true;
				if(dtoPrev != null)
					havePrev = true;

				DtoGetPrevNextSpecProperty data = new DtoGetPrevNextSpecProperty(OKCoApp.individualSelected.name, OKCoApp.individualSelected.ns, dto, haveNext, havePrev);				  
				return data;
			}
		}

		return null;
	}


}
