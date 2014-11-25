package br.com.padtec.okco.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.application.CompleterApp;
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
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.OntModelAPI;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.OntPropertyEnum;
import br.com.padtec.okco.util.GraphPlotting;
import br.com.padtec.okco.util.WOKCOGraphPlotting;

import com.hp.hpl.jena.ontology.OntModel;

@Controller
//@RequestMapping("/instance")
public class OKCoController {

	@RequestMapping(method = RequestMethod.GET, value="/list")
	public String list(HttpServletRequest request) 
	{
		List<DtoInstance> allIndividuals = CompleterApp.ListAllInstances;				
		List<String> modifiedIndividuals = CompleterApp.ListModifiedInstances;
		
		if(allIndividuals != null) 
		{
			request.getSession().setAttribute("listInstances", allIndividuals);
			request.getSession().setAttribute("listModifedInstances", modifiedIndividuals);
			return "list";
		} else{
			request.getSession().setAttribute("loadOk", "false");
			return "index";
		}
	}
	
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
		uri = decodeURI(uri);
		
		List<DtoInstance> listAllInstances = CompleterApp.ListAllInstances;
		CompleterApp.instanceSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uri);
		CompleterApp.ListCompleteClsInstaceSelected = CompleterApp.instanceSelected.ListCompleteClasses;
		CompleterApp.ListSpecializationProperties = CompleterApp.instanceSelected.ListSpecializationProperties;		
		List<DtoDefinitionClass> listSomeClassDefinition = DtoFactoryUtil.removeRepeatedInSomeDefinition(CompleterApp.instanceSelected);
		List<DtoDefinitionClass> listMinClassDefinition = DtoFactoryUtil.removeRepeatedInMinDefinition(CompleterApp.instanceSelected);	
		List<DtoDefinitionClass> listMaxClassDefinition = DtoFactoryUtil.removeRepeatedInMaxDefinition(CompleterApp.instanceSelected);
		List<DtoDefinitionClass> listExactlyClassDefinition = DtoFactoryUtil.removeRepeatedInExactDefinition(CompleterApp.instanceSelected);	
		List<DtoInstanceRelation> instanceRelationsList = DtoQueryUtil.getRelations(UploadApp.getInferredModel(),CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name);		
		
		request.getSession().setAttribute("listInstances", listAllInstances);
		request.getSession().setAttribute("instanceSelected", CompleterApp.instanceSelected);
		request.getSession().setAttribute("ListSpecializationProperties", CompleterApp.ListSpecializationProperties);
		request.getSession().setAttribute("listSomeClassDefinition", listSomeClassDefinition);
		request.getSession().setAttribute("listMinClassDefinition", listMinClassDefinition);
		request.getSession().setAttribute("listMaxClassDefinition", listMaxClassDefinition);
		request.getSession().setAttribute("listExactlyClassDefinition", listExactlyClassDefinition);
		request.getSession().setAttribute("instanceListRelations", instanceRelationsList);		
		return "details";
	}

	@RequestMapping(method = RequestMethod.GET, value="/completeProperty")
	public String completeProperty(@RequestParam("idDefinition") String uriProperty, @RequestParam("uriInstance") String uriInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) 
	{
		List<DtoInstance> listAllInstances = CompleterApp.ListAllInstances;
		//Instance selected
		DtoInstance instance = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriInstance);

		//Search for the definition class correctly

		CompleterApp.dtoSelected = DtoDefinitionClass.get(instance.ListSome, uriProperty);
		if(CompleterApp.dtoSelected == null)
			CompleterApp.dtoSelected = DtoDefinitionClass.get(instance.ListMin, uriProperty);
		if(CompleterApp.dtoSelected == null)
			CompleterApp.dtoSelected = DtoDefinitionClass.get(instance.ListMax, uriProperty);
		if(CompleterApp.dtoSelected == null)
			CompleterApp.dtoSelected = DtoDefinitionClass.get(instance.ListExactly, uriProperty);

		//Create the sections

		request.getSession().setAttribute("definitionSelected", CompleterApp.dtoSelected);
		request.getSession().setAttribute("instanceSelected", instance);
		request.getSession().setAttribute("propType", propType);

		if(type.equals("object"))
		{
			//List auxiliary
			CompleterApp.listNewInstancesRelation = new ArrayList<DtoInstance>();
			//Get all instances except the instance selected for Same/Different list		
			ArrayList<DtoInstance> listInstancesSameDifferent = new ArrayList<DtoInstance>(CompleterApp.ListAllInstances);
			//get instances with had this relation
			List<String> listInstancesName = QueryUtil.getIndividualsURIAtObjectPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);
			//populate the list of instances with had this relation	    	
			List<DtoInstance> listInstancesInRelation = DtoFactoryUtil.intersection(CompleterApp.ListAllInstances, listInstancesName);

			request.getSession().setAttribute("listInstancesInRelation", listInstancesInRelation);
			request.getSession().setAttribute("listInstancesSameDifferent", listInstancesSameDifferent);
			request.getSession().setAttribute("listInstances", listAllInstances);
			return "completePropertyObject";

		} else if (type.equals("objectMax"))
		{
			//get instances with had this relation
			List<String> listInstancesName = QueryUtil.getIndividualsURIAtObjectPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);
			Collections.sort(listInstancesName);

			//populate the list of instances with had this relation	    	
			List<DtoInstance> listInstancesInRelation = DtoFactoryUtil.intersection(CompleterApp.ListAllInstances, listInstancesName);

			request.getSession().setAttribute("listInstancesInRelation", listInstancesInRelation);
			return "completePropertyObjectMaxCard";

		} else if (type.equals("data"))
		{
			//List auxiliary
			CompleterApp.listNewDataValuesRelation = new ArrayList<DataPropertyValue>();

			//Get values with this data property
			List<DataPropertyValue> listValuesInRelation = new ArrayList<DataPropertyValue>();
			List<String> individualsList = QueryUtil.getIndividualsURIAtObjectPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);
			for(String individualURI: individualsList){
				DataPropertyValue data = new DataPropertyValue();
				data.value = individualURI.split("\\^\\^")[0];
				data.classValue = CompleterApp.dtoSelected.Target;
				data.existInModel = true;
				listValuesInRelation.add(data);
			}

			request.getSession().setAttribute("listValuesInRelation", listValuesInRelation);
			return "completePropertyData";

		} else {

			return "index";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value="/completePropertyAuto")
	public String completePropertyAuto(@RequestParam("idDefinition") String uriProperty, @RequestParam("uriInstance") String uriInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) {

		/*
		 * ATTENTION: This function works only with object properties: min, exactly and some properties
		 * 
		 * */

		//Instance selected
		DtoInstance instance = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriInstance);

		//Search for the definition class correctly
		CompleterApp.dtoSelected = DtoDefinitionClass.get(instance.ListSome, uriProperty);
		OntCardinalityEnum typeRelation = OntCardinalityEnum.SOME;

		if(CompleterApp.dtoSelected == null){
			CompleterApp.dtoSelected = DtoDefinitionClass.get(instance.ListMin, uriProperty);
			typeRelation = OntCardinalityEnum.MIN;
		}
		if(CompleterApp.dtoSelected == null){
			CompleterApp.dtoSelected = DtoDefinitionClass.get(instance.ListMax, uriProperty);
			typeRelation = OntCardinalityEnum.MAX;
		}
		if(CompleterApp.dtoSelected == null){
			CompleterApp.dtoSelected = DtoDefinitionClass.get(instance.ListExactly, uriProperty);
			typeRelation = OntCardinalityEnum.EXACTLY;
		}		

		if(type.equals("object"))
		{
			if(typeRelation.equals(OntCardinalityEnum.SOME))
			{
				//create the the new instance
				String instanceName = CompleterApp.dtoSelected.Target.split("#")[1] + "-" + (QueryUtil.getIndividualsURI(UploadApp.getInferredModel(), CompleterApp.dtoSelected.Target).size() + 1);
				ArrayList<String> listSame = new ArrayList<String>();		  
				ArrayList<String> listDif = new ArrayList<String>();
				ArrayList<String> listClasses = new ArrayList<String>();
				DtoInstance newInstance = new DtoInstance(UploadApp.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);

				//Create Individual
				OntModel basemodel = UploadApp.getBaseModel();
				basemodel = FactoryUtil.createIndividual(basemodel,newInstance.ns+newInstance.name,newInstance.ListSameInstances,newInstance.ListDiferentInstances, instance.ns + instance.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);
				UploadApp.baseRepository.setBaseOntModel(basemodel);
				
				CompleterApp.ListModifiedInstances.add(newInstance.ns + newInstance.name);
				try {
					CompleterApp.updateAddingToLists(newInstance.ns + newInstance.name);
				} catch (InconsistentOntologyException e) {
					
					e.printStackTrace();
				} catch (OKCoExceptionInstanceFormat e) {
					
					e.printStackTrace();
				}

			} else if(typeRelation.equals(OntCardinalityEnum.MIN))
			{
				int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);

				ArrayList<String> listDif = new ArrayList<String>();
				while(quantityInstancesTarget < Integer.parseInt(CompleterApp.dtoSelected.Cardinality))
				{
					//create the the new instance
					String instanceName = CompleterApp.dtoSelected.Target.split("#")[1] + "-" + (quantityInstancesTarget + 1);
					ArrayList<String> listSame = new ArrayList<String>();		  
					ArrayList<String> listClasses = new ArrayList<String>();
					DtoInstance newInstance = new DtoInstance(UploadApp.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);
					
					//Create Individual
					OntModel basemodel = UploadApp.getBaseModel();
					basemodel = FactoryUtil.createIndividual(basemodel,newInstance.ns+newInstance.name,newInstance.ListSameInstances,newInstance.ListDiferentInstances, instance.ns + instance.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);					
					UploadApp.baseRepository.setBaseOntModel(basemodel);
					
					CompleterApp.ListModifiedInstances.add(newInstance.ns + newInstance.name);
					CompleterApp.ListModifiedInstances.add(newInstance.ns + newInstance.name);
					try {
						CompleterApp.updateAddingToLists(newInstance.ns + newInstance.name);
					} catch (InconsistentOntologyException e) {
						
						e.printStackTrace();
					} catch (OKCoExceptionInstanceFormat e) {
						
						e.printStackTrace();
					}

					listDif.add(newInstance.ns + newInstance.name);
					quantityInstancesTarget ++;
				}				

			} else if(typeRelation.equals(OntCardinalityEnum.EXACTLY))
			{
				int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);				

				// Case 1 - same as min relation
				if(quantityInstancesTarget < Integer.parseInt(CompleterApp.dtoSelected.Cardinality))
				{
					ArrayList<String> listDif = new ArrayList<String>();
					while(quantityInstancesTarget < Integer.parseInt(CompleterApp.dtoSelected.Cardinality))
					{
						//create the the new instance
						String instanceName = CompleterApp.dtoSelected.Target.split("#")[1] + "-" + (quantityInstancesTarget + 1);
						ArrayList<String> listSame = new ArrayList<String>();
						ArrayList<String> listClasses = new ArrayList<String>();
						DtoInstance newInstance = new DtoInstance(UploadApp.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);

						// Create Individual
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createIndividual(basemodel,newInstance.ns+newInstance.name,newInstance.ListSameInstances,newInstance.ListDiferentInstances, instance.ns + instance.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);						
						UploadApp.baseRepository.setBaseOntModel(basemodel);
						
						CompleterApp.ListModifiedInstances.add(newInstance.ns + newInstance.name);
						CompleterApp.ListModifiedInstances.add(newInstance.ns + newInstance.name);
						try {
							CompleterApp.updateAddingToLists(newInstance.ns + newInstance.name);
						} catch (InconsistentOntologyException e) {
							
							e.printStackTrace();
						} catch (OKCoExceptionInstanceFormat e) {
							
							e.printStackTrace();
						}

						listDif.add(newInstance.ns + newInstance.name);
						quantityInstancesTarget ++;
					}
				}

				// Case 2 - more individuals than necessary
				if(quantityInstancesTarget > Integer.parseInt(CompleterApp.dtoSelected.Cardinality))
				{

				}

			} else if(typeRelation.equals(OntCardinalityEnum.MAX))
			{

			}


		}  else if (type.equals("data")){

			//Do nothing yet

		}

		//Add on list modified instances
		CompleterApp.ListModifiedInstances.add(instance.ns + instance.name);

		//Update InfModel without calling reasoner
		UploadApp.inferredRepository.setInferredModel(OntModelAPI.clone(UploadApp.baseRepository.getBaseOntModel()));

		//Update lists
		//HomeController.UpdateLists();

		//Update list instances modified
		CompleterApp.updateModifiedList();

		return "redirect:list";
	}

	@RequestMapping(method = RequestMethod.GET, value="/completeInstanceAuto")
	public String completeInstanceAuto(@RequestParam("uriInstance") String uriInstance, HttpServletRequest request) {

		//Instance selected
		DtoInstance instance = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uriInstance);
		for(DtoInstance i: CompleterApp.ListAllInstances){
			i.print();
		}
		System.out.println("Complete Instance ID="+uriInstance);		
		UploadApp.baseRepository.setBaseOntModel(CompleterApp.CompleteInstanceAuto(instance, UploadApp.baseRepository.getNameSpace(), UploadApp.getBaseModel(), UploadApp.getInferredModel(), CompleterApp.ListAllInstances));

		CompleterApp.ListModifiedInstances.add(instance.ns + instance.name);

		try {
			CompleterApp.updateLists();
		} catch (InconsistentOntologyException e) {
			e.printStackTrace();
		} catch (OKCoExceptionInstanceFormat e) {
			e.printStackTrace();
		} 
		
		//Update list instances modified
		CompleterApp.updateModifiedList();

		return "redirect:list";
	}

	@RequestMapping(method = RequestMethod.GET, value="/graphVisualizer")
	public String graphVisualizer(@RequestParam("uri") String uri, @RequestParam("typeView") String typeView, HttpServletRequest request) {

		String valuesGraph = "";
		int width;
		int height;
		String subtitle = "";
		GraphPlotting graphPlotting = new WOKCOGraphPlotting();
		DtoInstance i;
		
		//TypeView -> ALL/IN/OUT
		if(typeView.equals("ALL"))
		{

			//All instances
			valuesGraph  = graphPlotting.getArborStructureFor(UploadApp.getInferredModel()); 

		} else if(uri != null){

			//Get the instance
			i = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uri);

			if(typeView.equals("IN"))			//in on instance
			{				
				//Get the values
				valuesGraph  = graphPlotting.getArborStructureComingInOf(UploadApp.getInferredModel(), i.ns + i.name);

			} else if(typeView.equals("OUT")) {	//out from instance

				//Get the values
				valuesGraph  = graphPlotting.getArborStructureComingOutOf(UploadApp.getInferredModel(), i.ns + i.name);	
			}			
		}	

		width  = graphPlotting.width;
		height = graphPlotting.height;
		subtitle = graphPlotting.getSubtitle();

		//session
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("subtitle", subtitle);

		return "graphVisualizer";

	}


	/*------ AJAX - ObjectProperty -----*/	

	@RequestMapping(value="/createInstance", method = RequestMethod.POST)
	public @ResponseBody DtoInstance createInstance(@RequestBody final DtoCreateInstancePost dto){    

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

		CompleterApp.listNewInstancesRelation.add(i);
		return i;
	}

	@RequestMapping(value="/commitInstance", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitInstance(@RequestBody final DtoCommitPost dtoCommit) {    

		boolean isCreate = false;
		boolean isUpdate = false;
		DtoResult dto = new DtoResult();
		if(CompleterApp.listNewInstancesRelation.size() != 0)
		{
			DtoInstance iSource = CompleterApp.instanceSelected;
			for (DtoInstance iTarget : CompleterApp.listNewInstancesRelation) 
			{
				try {

					if(iTarget.existInModel == false)
					{
						//Create instance
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createIndividual(basemodel, iTarget.ns + iTarget.name, iTarget.ListSameInstances, iTarget.ListDiferentInstances, iSource.ns + iSource.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
						isCreate = true;

					} else {

						//Selected instance
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createObjectProperty(basemodel, iSource.ns + iSource.name, CompleterApp.dtoSelected.Relation, iTarget.ns + iTarget.name);
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
						CompleterApp.ListModifiedInstances.add(iTarget.ns + iTarget.name);
					}			 

					//Update list
					//HomeController.UpdateLists();
					CompleterApp.updateAddingToLists(iTarget.ns + iTarget.name);

				} catch (Exception e) {

					if(isCreate == true)
						UploadApp.baseRepository.setBaseOntModel(DtoFactoryUtil.deleteIndividual(UploadApp.getBaseModel(),iTarget));

					if(isUpdate == true){
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.deleteObjectProperty(basemodel, iSource.ns + iSource.name, CompleterApp.dtoSelected.Relation, iTarget.ns + iTarget.name);
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
				CompleterApp.ListModifiedInstances.add(iSource.ns + iSource.name);			
			}

			//Update list instances modified
			CompleterApp.updateModifiedList();

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
			CompleterApp.updateLists();

			//Clean list modified instances
			CompleterApp.ListModifiedInstances = new ArrayList<String>();

			//Update list instances modified
			CompleterApp.updateModifiedList();

		} catch (Exception e) {

			//Roll back the tempModel
			UploadApp.baseRepository.setBaseOntModel(UploadApp.baseRepository.cloneReplacing(UploadApp.tempModel));
			UploadApp.inferredRepository.setInferredModel(UploadApp.reasoner.run(UploadApp.getBaseModel()));

			//Update list instances
			try {
				CompleterApp.updateLists();

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
			DtoInstance.removeFromList(CompleterApp.listNewInstancesRelation, uri);
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
			DtoInstance i = getInstance(CompleterApp.listNewInstancesRelation, uri);
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, CompleterApp.listNewInstancesRelation);
			return dto;
		}

		return null;	  
	}

	@RequestMapping(value="/selectInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance selectInstance(@RequestParam String uri) {    

		if(uri != null)
		{
			DtoInstance i = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), uri);
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, CompleterApp.ListAllInstances);
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
			CompleterApp.listNewInstancesRelation.add(i);
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
					
					CompleterApp.ListModifiedInstances.add(s1.ns + s1.name);
					CompleterApp.ListModifiedInstances.add(s2.ns + s2.name);
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
					CompleterApp.updateLists();

					//Clean list modified instances
					CompleterApp.ListModifiedInstances = new ArrayList<String>();

					//Update list instances modified
					CompleterApp.updateModifiedList();

				} catch (Exception e) {

					//Roll back the tempModel
					UploadApp.baseRepository.setBaseOntModel(UploadApp.baseRepository.cloneReplacing(UploadApp.tempModel));
					UploadApp.inferredRepository.setInferredModel( UploadApp.reasoner.run(UploadApp.getBaseModel()));

					//Update list instances
					try {
						CompleterApp.updateLists();

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
				CompleterApp.updateModifiedList();
				
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
			DataPropertyValue.removeFromList(CompleterApp.listNewDataValuesRelation, id);
			return id;
		}

		return null;		  
	}

	@RequestMapping(value="/createDataValue", method = RequestMethod.POST)
	public @ResponseBody DataPropertyValue createDataValue(@RequestBody final DtoCreateDataValuePost dto){    

		DataPropertyValue data = new DataPropertyValue();
		data.value = dto.value;
		data.classValue = CompleterApp.dtoSelected.Target;
		data.existInModel = false;

		CompleterApp.listNewDataValuesRelation.add(data);
		return data;
	}

	@RequestMapping(value="/commitDataValues", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitDataValues(@RequestBody final DtoCommitPost dtoCommit) {    

		DtoResult dto = new DtoResult();
		if(CompleterApp.listNewDataValuesRelation.size() != 0)
		{
			DtoInstance iSource = CompleterApp.instanceSelected;
			for (DataPropertyValue dataTarget : CompleterApp.listNewDataValuesRelation) 
			{
				try {

					if(dataTarget.existInModel == false)
					{
						//Create data value
						OntModel basemodel = UploadApp.getBaseModel();
						FactoryUtil.createRangeDataPropertyValue(basemodel, dataTarget.value, iSource.ns + iSource.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);
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
						CompleterApp.ListModifiedInstances.add(iSource.ns + iSource.name);
					}						 

					//Update list instances
					CompleterApp.updateLists();

				} catch (Exception e) {

					OntModel basemodel = UploadApp.getBaseModel();
					FactoryUtil.deleteRangeDataPropertyValue(basemodel, dataTarget.value, CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name, CompleterApp.dtoSelected.Relation, CompleterApp.dtoSelected.Target);
					UploadApp.baseRepository.setBaseOntModel(basemodel);	

					dto.setMessage(e.getMessage());
					dto.setIsSucceed(false);
					return dto;
				}
			} //end for

			//Update list instances modified
			CompleterApp.updateModifiedList();

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
					FactoryUtil.createIndividualOfClass(basemodel, CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name, cls);
					UploadApp.baseRepository.setBaseOntModel(basemodel);
					
				} catch (Exception e) {

					dtoResult.setMessage(e.getMessage());
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
			}

			try {

				//Validate and update list
				CompleterApp.updateAddingToLists(CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name);;

				//Instance selected update
				CompleterApp.instanceSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), CompleterApp.instanceSelected .uri);

			} catch (Exception e) {

				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);

				//Remove all created
				for (String clsAux : listCls) {
					OntModel basemodel = UploadApp.getBaseModel();
					FactoryUtil.deleteIndividualOfClass(basemodel, CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name, clsAux);
					UploadApp.baseRepository.setBaseOntModel(basemodel);
				}

				//Validate and update list and infModel
				CompleterApp.updateLists();

				//Instance selected update
				CompleterApp.instanceSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), CompleterApp.instanceSelected .uri);

				return dtoResult;
			}	

			//Add on list modified instances
			CompleterApp.ListModifiedInstances.add(CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name);

			//Update list instances modified
			CompleterApp.updateModifiedList();

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

		DtoPropertyAndSubProperties dtoSpec = DtoPropertyAndSubProperties.getInstance(CompleterApp.ListSpecializationProperties, dto.arraySubProp);

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
						basemodel = FactoryUtil.createRangeDataPropertyValue(basemodel, dtoSpec.iTargetNs.split("\\^\\^")[0], CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name, subRel, dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName);
						UploadApp.baseRepository.setBaseOntModel(basemodel);						
					}else{
						//Case object property
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createObjectProperty(basemodel, CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name,subRel, dtoSpec.iTargetNs + dtoSpec.iTargetName);
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
				CompleterApp.updateAddingToLists(CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name);

				//Instance selected update
				CompleterApp.instanceSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), CompleterApp.instanceSelected .uri);

			} catch (Exception e) {

				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);

				//Remove all created
				for (String subRelAux : listRelations) {

					if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY)){						
						//Case data property
						OntModel basemodel = UploadApp.getBaseModel();
						FactoryUtil.deleteRangeDataPropertyValue(basemodel, dtoSpec.iTargetNs.split("\\^\\^")[0], CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name, subRelAux, dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
					}else{
						//Case object property
						OntModel basemodel = UploadApp.getBaseModel();
						basemodel = FactoryUtil.createObjectProperty(basemodel, CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name, subRelAux,dtoSpec.iTargetNs + dtoSpec.iTargetName);
						UploadApp.baseRepository.setBaseOntModel(basemodel);
					}
				}

				//Validate and update list and infModel
				CompleterApp.updateLists();

				//Instance selected update
				CompleterApp.instanceSelected = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), CompleterApp.instanceSelected .uri);

				return dtoResult;
			}			  

			//Add on list modified instances
			CompleterApp.ListModifiedInstances.add(CompleterApp.instanceSelected.ns + CompleterApp.instanceSelected.name);

			//Update list instances modified
			CompleterApp.updateModifiedList();

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
			DtoPropertyAndSubProperties dto = DtoPropertyAndSubProperties.getInstance(CompleterApp.ListSpecializationProperties, uriProperty);
			if(dto == null){

				return null;

			} else {

				boolean haveNext = false;
				boolean havePrev = false;
				DtoPropertyAndSubProperties dtoNext = DtoPropertyAndSubProperties.getInstance(CompleterApp.ListSpecializationProperties, CompleterApp.ListSpecializationProperties.get(CompleterApp.ListSpecializationProperties.indexOf(dto)+1).Property);
				DtoPropertyAndSubProperties dtoPrev= DtoPropertyAndSubProperties.getInstance(CompleterApp.ListSpecializationProperties, CompleterApp.ListSpecializationProperties.get(CompleterApp.ListSpecializationProperties.indexOf(dto)-1).Property);
				if(dtoNext != null)
					haveNext = true;
				if(dtoPrev != null)
					havePrev = true;

				DtoGetPrevNextSpecProperty data = new DtoGetPrevNextSpecProperty(CompleterApp.instanceSelected.name, CompleterApp.instanceSelected.ns, dto, haveNext, havePrev);				  
				return data;
			}
		}

		return null;
	}


}
