package br.ufes.inf.padtec.tnokco.controller;

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

import br.com.padtec.common.application.UploadApp;
import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoClassifyInstancePost;
import br.com.padtec.common.dto.DtoCommitMaxCard;
import br.com.padtec.common.dto.DtoCommitPost;
import br.com.padtec.common.dto.DtoCompleteClass;
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
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.OntPropertyEnum;
import br.inf.nemo.padtec.graphplotting.GraphPlotting;
import br.inf.nemo.padtec.wokco.WOKCOGraphPlotting;

@Controller
//@RequestMapping("/instance")
public class OKCoController {

	// Save the new instances before commit in views (completePropertyObject and completePropertyData)

	//Instances to add in relation
	ArrayList<DtoInstance> listNewInstancesRelation;

	//DataValues to add in relation
	ArrayList<DataPropertyValue> listNewDataValuesRelation;

	//All instances in model
	ArrayList<DtoInstance> ListAllInstances;

	//Dto selected
	DtoDefinitionClass dtoSelected;

	//Instance selected
	DtoInstance instanceSelected;

	//Specialization - Complete classes for instance class
	ArrayList<DtoCompleteClass> ListCompleteClsInstaceSelected;

	//Specialization - Property and subProperties
	ArrayList<DtoPropertyAndSubProperties> ListSpecializationProperties;  

	/*------ Common -----*/	

	@RequestMapping(method = RequestMethod.GET, value="/list")
	public String list(HttpServletRequest request) {

		this.ListAllInstances = HomeController.ListAllInstances;

		if(ListAllInstances != null) 
		{
			request.getSession().setAttribute("listInstances", ListAllInstances);
			request.getSession().setAttribute("listModifedInstances", HomeController.ListModifiedInstances);

			return "list";	//View to return

		} else{
			request.getSession().setAttribute("loadOk", "false");
			return "index";

		}

	}

	@RequestMapping(method = RequestMethod.GET, value="/details")
	public String details(@RequestParam("uri") String uri, HttpServletRequest request) {

		// ----- Instance selected ----//

		instanceSelected = HomeController.ManagerInstances.getInstance(ListAllInstances, uri);		

		// ----- Remove repeat values -------- //

		ArrayList<DtoDefinitionClass> listSomeClassDefinition = HomeController.ManagerInstances.removeRepeatValuesOn(instanceSelected, OntCardinalityEnum.SOME);
		ArrayList<DtoDefinitionClass> listMinClassDefinition = HomeController.ManagerInstances.removeRepeatValuesOn(instanceSelected, OntCardinalityEnum.MIN);	
		ArrayList<DtoDefinitionClass> listMaxClassDefinition = HomeController.ManagerInstances.removeRepeatValuesOn(instanceSelected, OntCardinalityEnum.MAX);
		ArrayList<DtoDefinitionClass> listExactlyClassDefinition = HomeController.ManagerInstances.removeRepeatValuesOn(instanceSelected, OntCardinalityEnum.EXACTLY);	

		// ------ Complete classes list ------//

		//ArrayList<String> listClassesMembersToClassify = HomeController.ManagerInstances.getClassesToClassify(instanceSelected, HomeController.InfModel);		

		// ----- List relations ----- //

		
		List<DtoInstanceRelation> instanceListRelationsFromInstance = new ArrayList<DtoInstanceRelation>();
		List<String> propertiesURIList = QueryUtil.getPropertiesURI(HomeController.InfModel, instanceSelected.ns + instanceSelected.name);
		for(String propertyURI: propertiesURIList){
			DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyURI;
		    List<String> ranges = QueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI);
		    if(ranges.size()>0) dtoItem.Target = ranges.get(0);
		    else dtoItem.Target = "";
		    instanceListRelationsFromInstance.add(dtoItem);
		}
		
		ListCompleteClsInstaceSelected = instanceSelected.ListCompleteClasses;

		// ------ Specialization Properties list ------//

		ListSpecializationProperties = instanceSelected.ListSpecializationProperties;

		// ------ Create sections ------//

		//Specialization
		//classes ok
		request.getSession().setAttribute("ListSpecializationProperties", ListSpecializationProperties);

		//Definition
		request.getSession().setAttribute("listSomeClassDefinition", listSomeClassDefinition);
		request.getSession().setAttribute("listMinClassDefinition", listMinClassDefinition);
		request.getSession().setAttribute("listMaxClassDefinition", listMaxClassDefinition);
		request.getSession().setAttribute("listExactlyClassDefinition", listExactlyClassDefinition);

		//Information
		request.getSession().setAttribute("instanceListRelations", instanceListRelationsFromInstance);
		request.getSession().setAttribute("instanceSelected", instanceSelected);		
		request.getSession().setAttribute("listInstances", ListAllInstances);

		// ------  View to return ------//

		return "details";
	}

	@RequestMapping(method = RequestMethod.GET, value="/completeProperty")
	public String completeProperty(@RequestParam("idDefinition") String uriProperty, @RequestParam("uriInstance") String uriInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) {

		//Instance selected
		DtoInstance instance = HomeController.ManagerInstances.getInstance(ListAllInstances, uriInstance);

		//Search for the definition class correctly

		dtoSelected = DtoDefinitionClass.get(instance.ListSome, uriProperty);
		if(dtoSelected == null)
			dtoSelected = DtoDefinitionClass.get(instance.ListMin, uriProperty);
		if(dtoSelected == null)
			dtoSelected = DtoDefinitionClass.get(instance.ListMax, uriProperty);
		if(dtoSelected == null)
			dtoSelected = DtoDefinitionClass.get(instance.ListExactly, uriProperty);

		//Create the sections

		request.getSession().setAttribute("definitionSelected", dtoSelected);
		request.getSession().setAttribute("instanceSelected", instance);
		request.getSession().setAttribute("propType", propType);

		if(type.equals("object"))
		{
			//List auxiliary
			listNewInstancesRelation = new ArrayList<DtoInstance>();

			//Get all instances except the instance selected for Same/Different list		
			ArrayList<DtoInstance> listInstancesSameDifferent = new ArrayList<DtoInstance>(ListAllInstances);

			//get instances with had this relation
			List<String> listInstancesName = QueryUtil.getIndividualsURIAtObjectPropertyRange(HomeController.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);

			//populate the list of instances with had this relation	    	
			List<DtoInstance> listInstancesInRelation = HomeController.ManagerInstances.getIntersectionOf(ListAllInstances, listInstancesName);

			//Create others sections
			request.getSession().setAttribute("listInstancesInRelation", listInstancesInRelation);
			request.getSession().setAttribute("listInstancesSameDifferent", listInstancesSameDifferent);
			request.getSession().setAttribute("listInstances", ListAllInstances);

			//return view	    	
			return "completePropertyObject";

		} else if (type.equals("objectMax"))
		{
			//get instances with had this relation
			List<String> listInstancesName = QueryUtil.getIndividualsURIAtObjectPropertyRange(HomeController.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);
			Collections.sort(listInstancesName);

			//populate the list of instances with had this relation	    	
			List<DtoInstance> listInstancesInRelation = HomeController.ManagerInstances.getIntersectionOf(ListAllInstances, listInstancesName);

			request.getSession().setAttribute("listInstancesInRelation", listInstancesInRelation);

			return "completePropertyObjectMaxCard";

		} else if (type.equals("data"))
		{
			//List auxiliary
			listNewDataValuesRelation = new ArrayList<DataPropertyValue>();

			//Get values with this data property
			List<DataPropertyValue> listValuesInRelation = new ArrayList<DataPropertyValue>();
			List<String> individualsList = QueryUtil.getIndividualsURIAtObjectPropertyRange(HomeController.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);
			for(String individualURI: individualsList){
				DataPropertyValue data = new DataPropertyValue();
				data.value = individualURI.split("\\^\\^")[0];
				data.classValue = dtoSelected.Target;
				data.existInModel = true;
				listValuesInRelation.add(data);
			}
			
			//Create others sections
			request.getSession().setAttribute("listValuesInRelation", listValuesInRelation);

			//return view
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
		DtoInstance instance = HomeController.ManagerInstances.getInstance(ListAllInstances, uriInstance);

		//Search for the definition class correctly
		dtoSelected = DtoDefinitionClass.get(instance.ListSome,uriProperty);
		OntCardinalityEnum typeRelation = OntCardinalityEnum.SOME;

		if(dtoSelected == null){
			dtoSelected = DtoDefinitionClass.get(instance.ListMin, uriProperty);
			typeRelation = OntCardinalityEnum.MIN;
		}
		if(dtoSelected == null){
			dtoSelected = DtoDefinitionClass.get(instance.ListMax, uriProperty);
			typeRelation = OntCardinalityEnum.MAX;
		}
		if(dtoSelected == null){
			dtoSelected = DtoDefinitionClass.get(instance.ListExactly, uriProperty);
			typeRelation = OntCardinalityEnum.EXACTLY;
		}		

		if(type.equals("object"))
		{
			if(typeRelation.equals(OntCardinalityEnum.SOME))
			{
				//create the the new instance
				String instanceName = dtoSelected.Target.split("#")[1] + "-" + (QueryUtil.getIndividualsURI( HomeController.InfModel, dtoSelected.Target).size() + 1);
				ArrayList<String> listSame = new ArrayList<String>();		  
				ArrayList<String> listDif = new ArrayList<String>();
				ArrayList<String> listClasses = new ArrayList<String>();
				DtoInstance newInstance = new DtoInstance(HomeController.NS, instanceName, listClasses, listDif, listSame, false);

				HomeController.Model = HomeController.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, HomeController.Model, HomeController.InfModel, HomeController.ListAllInstances);
				HomeController.ListModifiedInstances.add(newInstance.ns + newInstance.name);
				try {
					HomeController.UpdateAddIntanceInLists(newInstance.ns + newInstance.name);
				} catch (InconsistentOntologyException e) {
					
					e.printStackTrace();
				} catch (OKCoExceptionInstanceFormat e) {
					
					e.printStackTrace();
				}

			} else if(typeRelation.equals(OntCardinalityEnum.MIN))
			{
				int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(HomeController.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);

				ArrayList<String> listDif = new ArrayList<String>();
				while(quantityInstancesTarget < Integer.parseInt(dtoSelected.Cardinality))
				{
					//create the the new instance
					String instanceName = dtoSelected.Target.split("#")[1] + "-" + (quantityInstancesTarget + 1);
					ArrayList<String> listSame = new ArrayList<String>();		  
					ArrayList<String> listClasses = new ArrayList<String>();
					DtoInstance newInstance = new DtoInstance(HomeController.NS, instanceName, listClasses, listDif, listSame, false);

					HomeController.Model = HomeController.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, HomeController.Model, HomeController.InfModel, HomeController.ListAllInstances);
					HomeController.ListModifiedInstances.add(newInstance.ns + newInstance.name);
					HomeController.ListModifiedInstances.add(newInstance.ns + newInstance.name);
					try {
						HomeController.UpdateAddIntanceInLists(newInstance.ns + newInstance.name);
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
				int quantityInstancesTarget = QueryUtil.countIndividualsURIAtPropertyRange(HomeController.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);				

				// Case 1 - same as min relation
				if(quantityInstancesTarget < Integer.parseInt(dtoSelected.Cardinality))
				{
					ArrayList<String> listDif = new ArrayList<String>();
					while(quantityInstancesTarget < Integer.parseInt(dtoSelected.Cardinality))
					{
						//create the the new instance
						String instanceName = dtoSelected.Target.split("#")[1] + "-" + (quantityInstancesTarget + 1);
						ArrayList<String> listSame = new ArrayList<String>();
						ArrayList<String> listClasses = new ArrayList<String>();
						DtoInstance newInstance = new DtoInstance(HomeController.NS, instanceName, listClasses, listDif, listSame, false);

						HomeController.Model = HomeController.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, HomeController.Model, HomeController.InfModel, HomeController.ListAllInstances);
						HomeController.ListModifiedInstances.add(newInstance.ns + newInstance.name);
						HomeController.ListModifiedInstances.add(newInstance.ns + newInstance.name);
						try {
							HomeController.UpdateAddIntanceInLists(newInstance.ns + newInstance.name);
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
				if(quantityInstancesTarget > Integer.parseInt(dtoSelected.Cardinality))
				{

				}

			} else if(typeRelation.equals(OntCardinalityEnum.MAX))
			{

			}


		}  else if (type.equals("data")){

			//Do nothing yet

		}

		//Add on list modified instances
		HomeController.ListModifiedInstances.add(instance.ns + instance.name);

		//Update InfModel without calling reasoner
		HomeController.InfModel = HomeController.Repository.cloneReplacing(HomeController.Model);

		//Update lists
		//HomeController.UpdateLists();

		//Update list instances modified
		HomeController.UpdateListsModified();

		return "redirect:list";
	}

	@RequestMapping(method = RequestMethod.GET, value="/completeInstanceAuto")
	public String completeInstanceAuto(@RequestParam("uriInstance") String uriInstance, HttpServletRequest request) {

		//Instance selected
		DtoInstance instance = HomeController.ManagerInstances.getInstance(ListAllInstances, uriInstance);

		HomeController.Model = HomeController.ManagerInstances.CompleteInstanceAuto(instance, HomeController.NS, HomeController.Model, HomeController.InfModel, HomeController.ListAllInstances);

		HomeController.ListModifiedInstances.add(instance.ns + instance.name);

		try {
			HomeController.UpdateLists();
		} catch (InconsistentOntologyException e) {
			e.printStackTrace();
		} catch (OKCoExceptionInstanceFormat e) {
			e.printStackTrace();
		}

		//Update list instances modified
		HomeController.UpdateListsModified();

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
		int num = 0;

		try  
		{  
			num = Integer.parseInt(uri);  
		}  
		catch(NumberFormatException nfe)  
		{  
			typeView = "ALL";
		}

		//TypeView -> ALL/IN/OUT
		if(typeView.equals("ALL"))
		{

			//All instances
			valuesGraph  = graphPlotting.getArborStructureFor(HomeController.InfModel); 

		} else if(uri != null && num > 0){

			//Get the instance
			i = HomeController.ManagerInstances.getInstance(ListAllInstances, uri);

			if(typeView.equals("IN"))			//in on instance
			{				
				//Get the values
				valuesGraph  = graphPlotting.getArborStructureComingInOf(HomeController.InfModel, i.ns + i.name);

			} else if(typeView.equals("OUT")) {	//out from instance

				//Get the values
				valuesGraph  = graphPlotting.getArborStructureComingOutOf(HomeController.InfModel, i.ns + i.name);	
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

		DtoInstance i = new DtoInstance(HomeController.NS, name, new ArrayList<String>(), listDif, listSame, false);

		listNewInstancesRelation.add(i);
		return i;
	}

	@RequestMapping(value="/commitInstance", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitInstance(@RequestBody final DtoCommitPost dtoCommit) {    

		boolean isCreate = false;
		boolean isUpdate = false;
		DtoResult dto = new DtoResult();
		if(listNewInstancesRelation.size() != 0)
		{
			DtoInstance iSource = instanceSelected;
			for (DtoInstance iTarget : listNewInstancesRelation) 
			{
				try {

					if(iTarget.existInModel == false)
					{
						//Create instance
						HomeController.Model = HomeController.ManagerInstances.CreateInstance(iSource.ns + iSource.name, dtoSelected.Relation, iTarget, dtoSelected.Target, HomeController.ListAllInstances, HomeController.Model);
						isCreate = true;

					} else {

						//Selected instance
						HomeController.Model = HomeController.ManagerInstances.CreateRelationProperty(iSource.ns + iSource.name, dtoSelected.Relation, iTarget.ns + iTarget.name, HomeController.Model);
						isUpdate = true;
					}

					if(dtoCommit.commitReasoner.equals("true"))
					{
						//Update InfModel calling reasoner
						HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);

					} else {
						//Update InfModel without calling reasoner
						HomeController.InfModel = HomeController.Repository.cloneReplacing(HomeController.Model);

						//Add on list modified instances
						HomeController.ListModifiedInstances.add(iTarget.ns + iTarget.name);
					}			 

					//Update list
					//HomeController.UpdateLists();
					HomeController.UpdateAddIntanceInLists(iTarget.ns + iTarget.name);

				} catch (Exception e) {

					if(isCreate == true)
						HomeController.Model = HomeController.ManagerInstances.DeleteInstance(iTarget, HomeController.Model);

					if(isUpdate == true)
						HomeController.Model = HomeController.ManagerInstances.DeleteRelationProperty(iSource.ns + iSource.name, dtoSelected.Relation, iTarget.ns + iTarget.name, HomeController.Model);

					dto.setMessage(e.getMessage());
					dto.setIsSucceed(false);
					return dto;
				}

			} // end for

			if(dtoCommit.commitReasoner.equals("true"))
			{

			} else {
				//Add on list modified instances
				HomeController.ListModifiedInstances.add(iSource.ns + iSource.name);			
			}

			//Update list instances modified
			HomeController.UpdateListsModified();

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
			HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);

			//Save temporary model
			HomeController.tmpModel = HomeController.Repository.cloneReplacing(HomeController.Model);

			//Update list instances
			HomeController.UpdateLists();

			//Clean list modified instances
			HomeController.ListModifiedInstances = new ArrayList<String>();

			//Update list instances modified
			HomeController.UpdateListsModified();

		} catch (Exception e) {

			//Roll back the tempModel
			HomeController.Model = HomeController.Repository.cloneReplacing(HomeController.tmpModel);
			HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);

			//Update list instances
			try {
				HomeController.UpdateLists();

			} catch (InconsistentOntologyException e1) {

				//e1.printStackTrace();

			} catch (OKCoExceptionInstanceFormat e1) {

				//e1.printStackTrace();
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
			DtoFactoryUtil.removeIndividualFrom(listNewInstancesRelation, uri);
			return uri;
		}

		return null;		  
	}

	@RequestMapping(value="/editInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance editInstance(@RequestParam String uriInstance) {    

		if(uriInstance != null)
		{
			DtoInstance i = HomeController.ManagerInstances.getInstance(listNewInstancesRelation, uriInstance);
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, listNewInstancesRelation);
			return dto;
		}

		return null;	  
	}

	@RequestMapping(value="/selectInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance selectInstance(@RequestParam String uriInstance) {    

		if(uriInstance != null)
		{
			DtoInstance i = HomeController.ManagerInstances.getInstance(ListAllInstances, uriInstance);
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, ListAllInstances);
			return dto;
		}

		return null;
	}

	@RequestMapping(value="/selectInstanceAdd", method = RequestMethod.GET)
	public @ResponseBody DtoInstance selectInstanceAdd(@RequestParam String uriInstance) { 

		//Add in listNewInstancesRelation

		if(uriInstance != null)
		{
			DtoInstance i = HomeController.ManagerInstances.getInstance(ListAllInstances, uriInstance);
			listNewInstancesRelation.add(i);
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
					
					DtoInstance s1 = HomeController.ManagerInstances.getInstance(HomeController.ListAllInstances, uriSource);
					DtoInstance s2 = HomeController.ManagerInstances.getInstance(HomeController.ListAllInstances, uriTarget);
					
					if(type.equals("dif"))
					{
						HomeController.Model = HomeController.ManagerInstances.setDifferentInstances(s1.ns + s1.name, s2.ns + s2.name, HomeController.Model);
						
					} else if (type.equals("same"))
					{
						HomeController.Model = HomeController.ManagerInstances.setSameInstances(s1.ns + s1.name, s2.ns + s2.name, HomeController.Model);
						
					} else {
						
						dtoResult.setMessage("error");
						dtoResult.setIsSucceed(false);
						return dtoResult;
					}
					
					HomeController.ListModifiedInstances.add(s1.ns + s1.name);
					HomeController.ListModifiedInstances.add(s2.ns + s2.name);
				}

			}
			
			if(dto.runReasoner.equals("true"))
			{
				try {

					//Run reasoner
					HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);

					//Save temporary model
					HomeController.tmpModel = HomeController.Repository.cloneReplacing(HomeController.Model);

					//Update list instances
					HomeController.UpdateLists();

					//Clean list modified instances
					HomeController.ListModifiedInstances = new ArrayList<String>();

					//Update list instances modified
					HomeController.UpdateListsModified();

				} catch (Exception e) {

					//Roll back the tempModel
					HomeController.Model = HomeController.Repository.cloneReplacing(HomeController.tmpModel);
					HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);

					//Update list instances
					try {
						HomeController.UpdateLists();

					} catch (InconsistentOntologyException e1) {

						//e1.printStackTrace();

					} catch (OKCoExceptionInstanceFormat e1) {

						//e1.printStackTrace();
					}

					String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
					
					dtoResult.setMessage(error);
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
				
			} if(dto.runReasoner.equals("false")) {
				
				//Update list instances modified
				HomeController.UpdateListsModified();
				
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
	public @ResponseBody String removeDataValue(@RequestParam String uri) {    

		if(uri != null)
		{
			DtoFactoryUtil.removeDataValueFrom(listNewDataValuesRelation, uri);
			return uri;
		}

		return null;		  
	}

	@RequestMapping(value="/createDataValue", method = RequestMethod.POST)
	public @ResponseBody DataPropertyValue createDataValue(@RequestBody final DtoCreateDataValuePost dto){    

		DataPropertyValue data = new DataPropertyValue();
		data.value = dto.value;
		data.classValue = dtoSelected.Target;
		data.existInModel = false;

		listNewDataValuesRelation.add(data);
		return data;
	}

	@RequestMapping(value="/commitDataValues", method = RequestMethod.POST)
	public @ResponseBody DtoResult commitDataValues(@RequestBody final DtoCommitPost dtoCommit) {    

		DtoResult dto = new DtoResult();
		if(listNewDataValuesRelation.size() != 0)
		{
			DtoInstance iSource = instanceSelected;
			for (DataPropertyValue dataTarget : listNewDataValuesRelation) 
			{
				try {

					if(dataTarget.existInModel == false)
					{
						//Create data value
						HomeController.Model = HomeController.ManagerInstances.CreateTargetDataProperty(iSource.ns + iSource.name, dtoSelected.Relation, dataTarget.value, dtoSelected.Target, HomeController.Model);
						dataTarget.existInModel = true;
					}

					if(dtoCommit.commitReasoner.equals("true"))
					{
						//Update InfModel calling reasoner
						HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);

					} else {
						//Update InfModel without calling reasoner
						HomeController.InfModel = HomeController.Repository.cloneReplacing(HomeController.Model);

						//Add on list modified instances
						HomeController.ListModifiedInstances.add(iSource.ns + iSource.name);
					}						 

					//Update list instances
					HomeController.UpdateLists();

				} catch (Exception e) {

					HomeController.Model = HomeController.ManagerInstances.DeleteTargetDataProperty(instanceSelected.ns + instanceSelected.name, dtoSelected.Relation, dataTarget.value, dtoSelected.Target, HomeController.Model);

					dto.setMessage(e.getMessage());
					dto.setIsSucceed(false);
					return dto;
				}
			} //end for

			//Update list instances modified
			HomeController.UpdateListsModified();

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

					HomeController.Model = HomeController.ManagerInstances.AddInstanceToClass(instanceSelected.ns + instanceSelected.name, cls, HomeController.Model);

				} catch (Exception e) {

					dtoResult.setMessage(e.getMessage());
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
			}

			try {

				//Validate and update list
				HomeController.UpdateAddIntanceInLists(instanceSelected.ns + instanceSelected.name);;

				//Instance selected update
				instanceSelected = HomeController.ManagerInstances.getInstance(ListAllInstances, instanceSelected .uri);

			} catch (Exception e) {

				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);

				//Remove all created
				for (String clsAux : listCls) {
					HomeController.Model = HomeController.ManagerInstances.RemoveInstanceOnClass(instanceSelected.ns + instanceSelected.name, clsAux, HomeController.Model);
				}

				//Validate and update list and infModel
				HomeController.UpdateLists();

				//Instance selected update
				instanceSelected = HomeController.ManagerInstances.getInstance(ListAllInstances, instanceSelected .uri);

				return dtoResult;
			}	

			//Add on list modified instances
			HomeController.ListModifiedInstances.add(instanceSelected.ns + instanceSelected.name);

			//Update list instances modified
			HomeController.UpdateListsModified();

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

		DtoPropertyAndSubProperties dtoSpec =  DtoFactoryUtil.getPropertyFrom(ListSpecializationProperties, dto.arraySubProp);

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

					if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY))
						//Case data property
						HomeController.Model = HomeController.ManagerInstances.CreateTargetDataProperty(instanceSelected.ns + instanceSelected.name, subRel, dtoSpec.iTargetNs.split("\\^\\^")[0], dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName, HomeController.Model);
					else
						//Case object property
						HomeController.Model = HomeController.ManagerInstances.CreateRelationProperty(instanceSelected.ns + instanceSelected.name, subRel, dtoSpec.iTargetNs + dtoSpec.iTargetName, HomeController.Model);

				} catch (Exception e) {

					dtoResult.setMessage(e.getMessage());
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
			}

			try {
				
				//Validate and update list
				HomeController.UpdateAddIntanceInLists(instanceSelected.ns + instanceSelected.name);

				//Instance selected update
				instanceSelected = HomeController.ManagerInstances.getInstance(ListAllInstances, instanceSelected .uri);

			} catch (Exception e) {

				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);

				//Remove all created
				for (String subRelAux : listRelations) {

					if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY))
						//Case data property
						HomeController.Model = HomeController.ManagerInstances.DeleteTargetDataProperty(instanceSelected.ns + instanceSelected.name, subRelAux, dtoSpec.iTargetNs.split("\\^\\^")[0], dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName, HomeController.Model);
					else
						//Case object property
						HomeController.Model = HomeController.ManagerInstances.DeleteRelationProperty(instanceSelected.ns + instanceSelected.name, subRelAux, dtoSpec.iTargetNs + dtoSpec.iTargetName, HomeController.Model);
				}

				//Validate and update list and infModel
				HomeController.UpdateLists();

				//Instance selected update
				instanceSelected = HomeController.ManagerInstances.getInstance(ListAllInstances, instanceSelected .uri);

				return dtoResult;
			}			  

			//Add on list modified instances
			HomeController.ListModifiedInstances.add(instanceSelected.ns + instanceSelected.name);

			//Update list instances modified
			HomeController.UpdateListsModified();

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
			DtoPropertyAndSubProperties dto =  DtoFactoryUtil.getPropertyFrom(ListSpecializationProperties, uriProperty);
			if(dto == null){

				return null;

			} else {

				boolean haveNext = false;
				boolean havePrev = false;
				DtoPropertyAndSubProperties dtoNext =  DtoFactoryUtil.getPropertyFrom(ListSpecializationProperties, ListSpecializationProperties.get(ListSpecializationProperties.indexOf(dto)+1).Property);
				DtoPropertyAndSubProperties dtoPrev=  DtoFactoryUtil.getPropertyFrom(ListSpecializationProperties, ListSpecializationProperties.get(ListSpecializationProperties.indexOf(dto)-1).Property);
				if(dtoNext != null)
					haveNext = true;
				if(dtoPrev != null)
					havePrev = true;

				DtoGetPrevNextSpecProperty data = new DtoGetPrevNextSpecProperty(instanceSelected.name, instanceSelected.ns, dto, haveNext, havePrev);				  
				return data;
			}
		}

		return null;
	}


}
