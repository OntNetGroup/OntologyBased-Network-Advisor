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

import br.com.padtec.common.queries.InfModelQueryUtil;
import br.com.padtec.common.queries.OntModelAPI;
import br.com.padtec.common.queries.OntPropertyEnum;
import br.com.padtec.okco.application.CompleterApp;
import br.com.padtec.okco.application.UploadApp;
import br.com.padtec.okco.domain.DataPropertyValue;
import br.com.padtec.okco.domain.DtoClassifyInstancePost;
import br.com.padtec.okco.domain.DtoCommitMaxCard;
import br.com.padtec.okco.domain.DtoCommitPost;
import br.com.padtec.okco.domain.DtoCompleteClass;
import br.com.padtec.okco.domain.DtoCreateDataValuePost;
import br.com.padtec.okco.domain.DtoCreateInstancePost;
import br.com.padtec.okco.domain.DtoDefinitionClass;
import br.com.padtec.okco.domain.DtoGetPrevNextSpecProperty;
import br.com.padtec.okco.domain.DtoInstanceRelation;
import br.com.padtec.okco.domain.DtoPropertyAndSubProperties;
import br.com.padtec.okco.domain.DtoViewSelectInstance;
import br.com.padtec.okco.domain.EnumRelationTypeCompletness;
import br.com.padtec.okco.domain.Instance;
import br.com.padtec.okco.domain.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.util.GraphPlotting;
import br.com.padtec.okco.util.WOKCOGraphPlotting;

@Controller
//@RequestMapping("/instance")
public class OKCoController {

	// Save the new instances before commit in views (completePropertyObject and completePropertyData)

	//Instances to add in relation
	ArrayList<Instance> listNewInstancesRelation;

	//DataValues to add in relation
	ArrayList<DataPropertyValue> listNewDataValuesRelation;

	//Dto selected
	DtoDefinitionClass dtoSelected;

	//Instance selected
	Instance instanceSelected;

	//Specialization - Complete classes for instance class
	ArrayList<DtoCompleteClass> ListCompleteClsInstaceSelected;

	//Specialization - Property and subProperties
	ArrayList<DtoPropertyAndSubProperties> ListSpecializationProperties;  

	@RequestMapping(method = RequestMethod.GET, value="/list")
	public String list(HttpServletRequest request) 
	{		
		List<Instance> individuals = CompleterApp.ListAllInstances;				
		List<String> modified = CompleterApp.ListModifiedInstances;
		
		if(individuals != null) 
		{
			request.getSession().setAttribute("listInstances", individuals);
			request.getSession().setAttribute("listModifedInstances", modified);
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
	public String details(@RequestParam("uri") String uri, HttpServletRequest request) {
				
		uri = decodeURI(uri);
		
		// ----- Instance selected ----//
		instanceSelected = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uri);		
		instanceSelected.print();
		// ----- Remove repeat values -------- //

		ArrayList<DtoDefinitionClass> listSomeClassDefinition = CompleterApp.ManagerInstances.removeRepeatValuesOn(instanceSelected, EnumRelationTypeCompletness.SOME);
		ArrayList<DtoDefinitionClass> listMinClassDefinition = CompleterApp.ManagerInstances.removeRepeatValuesOn(instanceSelected, EnumRelationTypeCompletness.MIN);	
		ArrayList<DtoDefinitionClass> listMaxClassDefinition = CompleterApp.ManagerInstances.removeRepeatValuesOn(instanceSelected, EnumRelationTypeCompletness.MAX);
		ArrayList<DtoDefinitionClass> listExactlyClassDefinition = CompleterApp.ManagerInstances.removeRepeatValuesOn(instanceSelected, EnumRelationTypeCompletness.EXACTLY);	

		// ------ Complete classes list ------//

		//ArrayList<String> listClassesMembersToClassify = HomeController.ManagerInstances.getClassesToClassify(instanceSelected, HomeController.InfModel);		

		// ----- List relations ----- //
		List<DtoInstanceRelation> instanceRelationsList = new ArrayList<DtoInstanceRelation>();
		List<String> propertiesURIList = InfModelQueryUtil.getPropertiesURI(UploadApp.getInferredModel(), instanceSelected.ns + instanceSelected.name);
		for(String propertyURI: propertiesURIList){
			DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyURI;
		    dtoItem.Target = InfModelQueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI).get(0);
		    instanceRelationsList.add(dtoItem);
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
		request.getSession().setAttribute("instanceListRelations", instanceRelationsList);
		request.getSession().setAttribute("instanceSelected", instanceSelected);		
		request.getSession().setAttribute("listInstances", CompleterApp.ListAllInstances);

		// ------  View to return ------//

		return "details";
	}

	@RequestMapping(method = RequestMethod.GET, value="/completeProperty")
	public String completeProperty(@RequestParam("idDefinition") String idDefinition, @RequestParam("uriInstance") String uriInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) {

		//Instance selected
		Instance instance = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uriInstance);

		//Search for the definition class correctly

		dtoSelected = DtoDefinitionClass.get(instance.ListSome, Integer.parseInt(idDefinition));
		if(dtoSelected == null)
			dtoSelected = DtoDefinitionClass.get(instance.ListMin, Integer.parseInt(idDefinition));
		if(dtoSelected == null)
			dtoSelected = DtoDefinitionClass.get(instance.ListMax, Integer.parseInt(idDefinition));
		if(dtoSelected == null)
			dtoSelected = DtoDefinitionClass.get(instance.ListExactly, Integer.parseInt(idDefinition));

		//Create the sections

		request.getSession().setAttribute("definitionSelected", dtoSelected);
		request.getSession().setAttribute("instanceSelected", instance);
		request.getSession().setAttribute("propType", propType);

		if(type.equals("object"))
		{
			//List auxiliary
			listNewInstancesRelation = new ArrayList<Instance>();

			//Get all instances except the instance selected for Same/Different list		
			ArrayList<Instance> listInstancesSameDifferent = new ArrayList<Instance>(CompleterApp.ListAllInstances);

			//get instances with had this relation
			List<String> listInstancesName = InfModelQueryUtil.getIndividualsURIAtObjectPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);

			//populate the list of instances with had this relation	    	
			List<Instance> listInstancesInRelation = CompleterApp.ManagerInstances.getIntersectionOf(CompleterApp.ListAllInstances, listInstancesName);

			//Create others sections
			request.getSession().setAttribute("listInstancesInRelation", listInstancesInRelation);
			request.getSession().setAttribute("listInstancesSameDifferent", listInstancesSameDifferent);
			request.getSession().setAttribute("listInstances", CompleterApp.ListAllInstances);

			//return view	    	
			return "completePropertyObject";

		} else if (type.equals("objectMax"))
		{
			//get instances with had this relation
			List<String> listInstancesName = InfModelQueryUtil.getIndividualsURIAtObjectPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);
			Collections.sort(listInstancesName);

			//populate the list of instances with had this relation	    	
			List<Instance> listInstancesInRelation = CompleterApp.ManagerInstances.getIntersectionOf(CompleterApp.ListAllInstances, listInstancesName);

			request.getSession().setAttribute("listInstancesInRelation", listInstancesInRelation);

			return "completePropertyObjectMaxCard";

		} else if (type.equals("data"))
		{
			//List auxiliary
			listNewDataValuesRelation = new ArrayList<DataPropertyValue>();

			//Get values with this data property
			List<DataPropertyValue> listValuesInRelation = new ArrayList<DataPropertyValue>();
			List<String> individualsList = InfModelQueryUtil.getIndividualsURIAtObjectPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);
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
	public String completePropertyAuto(@RequestParam("idDefinition") String idDefinition, @RequestParam("uriInstance") String uriInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) {

		/*
		 * ATTENTION: This function works only with object properties: min, exactly and some properties
		 * 
		 * */

		//Instance selected
		Instance instance = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uriInstance);

		//Search for the definition class correctly
		dtoSelected = DtoDefinitionClass.get(instance.ListSome, Integer.parseInt(idDefinition));
		EnumRelationTypeCompletness typeRelation = EnumRelationTypeCompletness.SOME;

		if(dtoSelected == null){
			dtoSelected = DtoDefinitionClass.get(instance.ListMin, Integer.parseInt(idDefinition));
			typeRelation = EnumRelationTypeCompletness.MIN;
		}
		if(dtoSelected == null){
			dtoSelected = DtoDefinitionClass.get(instance.ListMax, Integer.parseInt(idDefinition));
			typeRelation = EnumRelationTypeCompletness.MAX;
		}
		if(dtoSelected == null){
			dtoSelected = DtoDefinitionClass.get(instance.ListExactly, Integer.parseInt(idDefinition));
			typeRelation = EnumRelationTypeCompletness.EXACTLY;
		}		

		if(type.equals("object"))
		{
			if(typeRelation.equals(EnumRelationTypeCompletness.SOME))
			{
				//create the the new instance
				String instanceName = dtoSelected.Target.split("#")[1] + "-" + (InfModelQueryUtil.getIndividualsURI(UploadApp.getInferredModel(), dtoSelected.Target).size() + 1);
				ArrayList<String> listSame = new ArrayList<String>();		  
				ArrayList<String> listDif = new ArrayList<String>();
				ArrayList<String> listClasses = new ArrayList<String>();
				Instance newInstance = new Instance(UploadApp.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);

				UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, UploadApp.getBaseModel(), UploadApp.getInferredModel(), CompleterApp.ListAllInstances));
				CompleterApp.ListModifiedInstances.add(newInstance.ns + newInstance.name);
				try {
					CompleterApp.updateAddingToLists(newInstance.ns + newInstance.name);
				} catch (InconsistentOntologyException e) {
					
					e.printStackTrace();
				} catch (OKCoExceptionInstanceFormat e) {
					
					e.printStackTrace();
				}

			} else if(typeRelation.equals(EnumRelationTypeCompletness.MIN))
			{
				int quantityInstancesTarget = InfModelQueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);

				ArrayList<String> listDif = new ArrayList<String>();
				while(quantityInstancesTarget < Integer.parseInt(dtoSelected.Cardinality))
				{
					//create the the new instance
					String instanceName = dtoSelected.Target.split("#")[1] + "-" + (quantityInstancesTarget + 1);
					ArrayList<String> listSame = new ArrayList<String>();		  
					ArrayList<String> listClasses = new ArrayList<String>();
					Instance newInstance = new Instance(UploadApp.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);

					UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, UploadApp.getBaseModel(), UploadApp.getInferredModel(), CompleterApp.ListAllInstances));
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

			} else if(typeRelation.equals(EnumRelationTypeCompletness.EXACTLY))
			{
				int quantityInstancesTarget = InfModelQueryUtil.countIndividualsURIAtPropertyRange(UploadApp.getInferredModel(), instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);				

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
						Instance newInstance = new Instance(UploadApp.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);

						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, UploadApp.getBaseModel(), UploadApp.getInferredModel(), CompleterApp.ListAllInstances));
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
				if(quantityInstancesTarget > Integer.parseInt(dtoSelected.Cardinality))
				{

				}

			} else if(typeRelation.equals(EnumRelationTypeCompletness.MAX))
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
		Instance instance = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uriInstance);
		for(Instance i: CompleterApp.ListAllInstances){
			i.print();
		}
		System.out.println("Complete Instance ID="+uriInstance);		
		UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CompleteInstanceAuto(instance, UploadApp.baseRepository.getNameSpace(), UploadApp.getBaseModel(), UploadApp.getInferredModel(), CompleterApp.ListAllInstances));

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
		Instance i;
		
		//TypeView -> ALL/IN/OUT
		if(typeView.equals("ALL"))
		{

			//All instances
			valuesGraph  = graphPlotting.getArborStructureFor(UploadApp.getInferredModel()); 

		} else if(uri != null){

			//Get the instance
			i = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uri);

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
	public @ResponseBody Instance createInstance(@RequestBody final DtoCreateInstancePost dto){    

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

		Instance i = new Instance(UploadApp.baseRepository.getNameSpace(), name, new ArrayList<String>(), listDif, listSame, false);

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
			Instance iSource = instanceSelected;
			for (Instance iTarget : listNewInstancesRelation) 
			{
				try {

					if(iTarget.existInModel == false)
					{
						//Create instance
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CreateInstance(iSource.ns + iSource.name, dtoSelected.Relation, iTarget, dtoSelected.Target, CompleterApp.ListAllInstances, UploadApp.getBaseModel()));
						isCreate = true;

					} else {

						//Selected instance
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CreateRelationProperty(iSource.ns + iSource.name, dtoSelected.Relation, iTarget.ns + iTarget.name, UploadApp.getBaseModel()));
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
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.DeleteInstance(iTarget, UploadApp.getBaseModel()));

					if(isUpdate == true)
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.DeleteRelationProperty(iSource.ns + iSource.name, dtoSelected.Relation, iTarget.ns + iTarget.name, UploadApp.getBaseModel()));

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
			Instance.removeFromList(listNewInstancesRelation, uri);
			return uri;
		}

		return null;		  
	}

	@RequestMapping(value="/editInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance editInstance(@RequestParam String uri) {    

		if(uri!=null)
		{
			Instance i = CompleterApp.ManagerInstances.getInstance(listNewInstancesRelation, uri);
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, listNewInstancesRelation);
			return dto;
		}

		return null;	  
	}

	@RequestMapping(value="/selectInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance selectInstance(@RequestParam String uri) {    

		if(uri != null)
		{
			Instance i = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uri);
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, CompleterApp.ListAllInstances);
			return dto;
		}

		return null;
	}

	@RequestMapping(value="/selectInstanceAdd", method = RequestMethod.GET)
	public @ResponseBody Instance selectInstanceAdd(@RequestParam String uri) { 

		//Add in listNewInstancesRelation

		if(uri != null)
		{
			Instance i = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uri);
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
					
					Instance s1 = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uriSource);
					Instance s2 = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, uriTarget);
					
					if(type.equals("dif"))
					{
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.setDifferentInstances(s1.ns + s1.name, s2.ns + s2.name, UploadApp.getBaseModel()));
						
					} else if (type.equals("same"))
					{
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.setSameInstances(s1.ns + s1.name, s2.ns + s2.name, UploadApp.getBaseModel()));
						
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
			DataPropertyValue.removeFromList(listNewDataValuesRelation, id);
			return id;
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
			Instance iSource = instanceSelected;
			for (DataPropertyValue dataTarget : listNewDataValuesRelation) 
			{
				try {

					if(dataTarget.existInModel == false)
					{
						//Create data value
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CreateTargetDataProperty(iSource.ns + iSource.name, dtoSelected.Relation, dataTarget.value, dtoSelected.Target, UploadApp.getBaseModel()));
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

					UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.DeleteTargetDataProperty(instanceSelected.ns + instanceSelected.name, dtoSelected.Relation, dataTarget.value, dtoSelected.Target, UploadApp.getBaseModel()));

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

					UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.AddInstanceToClass(instanceSelected.ns + instanceSelected.name, cls, UploadApp.getBaseModel()));

				} catch (Exception e) {

					dtoResult.setMessage(e.getMessage());
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
			}

			try {

				//Validate and update list
				CompleterApp.updateAddingToLists(instanceSelected.ns + instanceSelected.name);;

				//Instance selected update
				instanceSelected = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, instanceSelected .uri);

			} catch (Exception e) {

				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);

				//Remove all created
				for (String clsAux : listCls) {
					UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.RemoveInstanceOnClass(instanceSelected.ns + instanceSelected.name, clsAux, UploadApp.getBaseModel()));
				}

				//Validate and update list and infModel
				CompleterApp.updateLists();

				//Instance selected update
				instanceSelected = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, instanceSelected .uri);

				return dtoResult;
			}	

			//Add on list modified instances
			CompleterApp.ListModifiedInstances.add(instanceSelected.ns + instanceSelected.name);

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

		DtoPropertyAndSubProperties dtoSpec = DtoPropertyAndSubProperties.getInstance(ListSpecializationProperties, Integer.parseInt(dto.id));

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
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CreateTargetDataProperty(instanceSelected.ns + instanceSelected.name, subRel, dtoSpec.iTargetNs.split("\\^\\^")[0], dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName, UploadApp.getBaseModel()));
					else
						//Case object property
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.CreateRelationProperty(instanceSelected.ns + instanceSelected.name, subRel, dtoSpec.iTargetNs + dtoSpec.iTargetName, UploadApp.getBaseModel()));

				} catch (Exception e) {

					dtoResult.setMessage(e.getMessage());
					dtoResult.setIsSucceed(false);
					return dtoResult;
				}
			}

			try {
				
				//Validate and update list
				CompleterApp.updateAddingToLists(instanceSelected.ns + instanceSelected.name);

				//Instance selected update
				instanceSelected = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, instanceSelected .uri);

			} catch (Exception e) {

				dtoResult.setMessage(e.getMessage());
				dtoResult.setIsSucceed(false);

				//Remove all created
				for (String subRelAux : listRelations) {

					if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY))
						//Case data property
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.DeleteTargetDataProperty(instanceSelected.ns + instanceSelected.name, subRelAux, dtoSpec.iTargetNs.split("\\^\\^")[0], dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName, UploadApp.getBaseModel()));
					else
						//Case object property
						UploadApp.baseRepository.setBaseOntModel(CompleterApp.ManagerInstances.DeleteRelationProperty(instanceSelected.ns + instanceSelected.name, subRelAux, dtoSpec.iTargetNs + dtoSpec.iTargetName, UploadApp.getBaseModel()));
				}

				//Validate and update list and infModel
				CompleterApp.updateLists();

				//Instance selected update
				instanceSelected = CompleterApp.ManagerInstances.getInstance(CompleterApp.ListAllInstances, instanceSelected .uri);

				return dtoResult;
			}			  

			//Add on list modified instances
			CompleterApp.ListModifiedInstances.add(instanceSelected.ns + instanceSelected.name);

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
	public @ResponseBody DtoGetPrevNextSpecProperty selectSpecializationProp(@RequestParam String id) {    

		if(id != null)
		{
			DtoPropertyAndSubProperties dto = DtoPropertyAndSubProperties.getInstance(ListSpecializationProperties, Integer.parseInt(id));
			if(dto == null){

				return null;

			} else {

				boolean haveNext = false;
				boolean havePrev = false;
				DtoPropertyAndSubProperties dtoNext = DtoPropertyAndSubProperties.getInstance(ListSpecializationProperties, Integer.parseInt(id) + 1);
				DtoPropertyAndSubProperties dtoPrev= DtoPropertyAndSubProperties.getInstance(ListSpecializationProperties, Integer.parseInt(id) - 1);
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
