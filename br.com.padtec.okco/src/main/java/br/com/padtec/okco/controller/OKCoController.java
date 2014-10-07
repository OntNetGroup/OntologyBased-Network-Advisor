package br.com.padtec.okco.controller;

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
import br.com.padtec.common.queries.OntPropertyEnum;
import br.com.padtec.okco.application.AppLoader;
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
import br.com.padtec.okco.domain.DtoResultCommit;
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
		if(AppLoader.ListAllInstances != null) 
		{
			request.getSession().setAttribute("listInstances", AppLoader.ListAllInstances);
			request.getSession().setAttribute("listModifedInstances", AppLoader.ListModifiedInstances);
			return "list";
		} else{
			request.getSession().setAttribute("loadOk", "false");
			return "index";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/details")
	public String details(@RequestParam("id") String id, HttpServletRequest request) {

		// ----- Instance selected ----//

		instanceSelected = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(id));		

		// ----- Remove repeat values -------- //

		ArrayList<DtoDefinitionClass> listSomeClassDefinition = AppLoader.ManagerInstances.removeRepeatValuesOn(instanceSelected, EnumRelationTypeCompletness.SOME);
		ArrayList<DtoDefinitionClass> listMinClassDefinition = AppLoader.ManagerInstances.removeRepeatValuesOn(instanceSelected, EnumRelationTypeCompletness.MIN);	
		ArrayList<DtoDefinitionClass> listMaxClassDefinition = AppLoader.ManagerInstances.removeRepeatValuesOn(instanceSelected, EnumRelationTypeCompletness.MAX);
		ArrayList<DtoDefinitionClass> listExactlyClassDefinition = AppLoader.ManagerInstances.removeRepeatValuesOn(instanceSelected, EnumRelationTypeCompletness.EXACTLY);	

		// ------ Complete classes list ------//

		//ArrayList<String> listClassesMembersToClassify = HomeController.ManagerInstances.getClassesToClassify(instanceSelected, HomeController.InfModel);		

		// ----- List relations ----- //
		List<DtoInstanceRelation> instanceRelationsList = new ArrayList<DtoInstanceRelation>();
		List<String> propertiesURIList = InfModelQueryUtil.getPropertiesURI(AppLoader.InfModel, instanceSelected.ns + instanceSelected.name);
		for(String propertyURI: propertiesURIList){
			DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyURI;
		    dtoItem.Target = InfModelQueryUtil.getRangeURIs(AppLoader.InfModel, propertyURI).get(0);
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
		request.getSession().setAttribute("listInstances", AppLoader.ListAllInstances);

		// ------  View to return ------//

		return "details";
	}

	@RequestMapping(method = RequestMethod.GET, value="/completeProperty")
	public String completeProperty(@RequestParam("idDefinition") String idDefinition, @RequestParam("idInstance") String idInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) {

		//Instance selected
		Instance instance = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(idInstance));

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
			ArrayList<Instance> listInstancesSameDifferent = new ArrayList<Instance>(AppLoader.ListAllInstances);

			//get instances with had this relation
			List<String> listInstancesName = InfModelQueryUtil.getIndividualsURIInRelationRange(AppLoader.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);

			//populate the list of instances with had this relation	    	
			List<Instance> listInstancesInRelation = AppLoader.ManagerInstances.getIntersectionOf(AppLoader.ListAllInstances, listInstancesName);

			//Create others sections
			request.getSession().setAttribute("listInstancesInRelation", listInstancesInRelation);
			request.getSession().setAttribute("listInstancesSameDifferent", listInstancesSameDifferent);
			request.getSession().setAttribute("listInstances", AppLoader.ListAllInstances);

			//return view	    	
			return "completePropertyObject";

		} else if (type.equals("objectMax"))
		{
			//get instances with had this relation
			List<String> listInstancesName = InfModelQueryUtil.getIndividualsURIInRelationRange(AppLoader.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);
			Collections.sort(listInstancesName);

			//populate the list of instances with had this relation	    	
			List<Instance> listInstancesInRelation = AppLoader.ManagerInstances.getIntersectionOf(AppLoader.ListAllInstances, listInstancesName);

			request.getSession().setAttribute("listInstancesInRelation", listInstancesInRelation);

			return "completePropertyObjectMaxCard";

		} else if (type.equals("data"))
		{
			//List auxiliary
			listNewDataValuesRelation = new ArrayList<DataPropertyValue>();

			//Get values with this data property
			List<DataPropertyValue> listValuesInRelation = new ArrayList<DataPropertyValue>();
			List<String> individualsList = InfModelQueryUtil.getIndividualsURIAtPropertyRange(AppLoader.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target);
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
	public String completePropertyAuto(@RequestParam("idDefinition") String idDefinition, @RequestParam("idInstance") String idInstance, @RequestParam("type") String type, @RequestParam("propType") String propType, HttpServletRequest request) {

		/*
		 * ATTENTION: This function works only with object properties: min, exactly and some properties
		 * 
		 * */

		//Instance selected
		Instance instance = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(idInstance));

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
				String instanceName = dtoSelected.Target.split("#")[1] + "-" + (InfModelQueryUtil.getIndividualsURI(AppLoader.InfModel, dtoSelected.Target).size() + 1);
				ArrayList<String> listSame = new ArrayList<String>();		  
				ArrayList<String> listDif = new ArrayList<String>();
				ArrayList<String> listClasses = new ArrayList<String>();
				Instance newInstance = new Instance(AppLoader.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);

				AppLoader.Model = AppLoader.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, AppLoader.Model, AppLoader.InfModel, AppLoader.ListAllInstances);
				AppLoader.ListModifiedInstances.add(newInstance.ns + newInstance.name);
				try {
					AppLoader.updateAddingToLists(newInstance.ns + newInstance.name);
				} catch (InconsistentOntologyException e) {
					
					e.printStackTrace();
				} catch (OKCoExceptionInstanceFormat e) {
					
					e.printStackTrace();
				}

			} else if(typeRelation.equals(EnumRelationTypeCompletness.MIN))
			{
				int quantityInstancesTarget = AppLoader.Search.CheckExistInstancesTargetCardinality(AppLoader.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target, dtoSelected.Cardinality);

				ArrayList<String> listDif = new ArrayList<String>();
				while(quantityInstancesTarget < Integer.parseInt(dtoSelected.Cardinality))
				{
					//create the the new instance
					String instanceName = dtoSelected.Target.split("#")[1] + "-" + (quantityInstancesTarget + 1);
					ArrayList<String> listSame = new ArrayList<String>();		  
					ArrayList<String> listClasses = new ArrayList<String>();
					Instance newInstance = new Instance(AppLoader.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);

					AppLoader.Model = AppLoader.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, AppLoader.Model, AppLoader.InfModel, AppLoader.ListAllInstances);
					AppLoader.ListModifiedInstances.add(newInstance.ns + newInstance.name);
					AppLoader.ListModifiedInstances.add(newInstance.ns + newInstance.name);
					try {
						AppLoader.updateAddingToLists(newInstance.ns + newInstance.name);
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
				int quantityInstancesTarget = AppLoader.Search.CheckExistInstancesTargetCardinality(AppLoader.InfModel, instance.ns + instance.name, dtoSelected.Relation, dtoSelected.Target, dtoSelected.Cardinality);				

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
						Instance newInstance = new Instance(AppLoader.baseRepository.getNameSpace(), instanceName, listClasses, listDif, listSame, false);

						AppLoader.Model = AppLoader.ManagerInstances.CreateInstanceAuto(instance.ns + instance.name, dtoSelected, newInstance, AppLoader.Model, AppLoader.InfModel, AppLoader.ListAllInstances);
						AppLoader.ListModifiedInstances.add(newInstance.ns + newInstance.name);
						AppLoader.ListModifiedInstances.add(newInstance.ns + newInstance.name);
						try {
							AppLoader.updateAddingToLists(newInstance.ns + newInstance.name);
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
		AppLoader.ListModifiedInstances.add(instance.ns + instance.name);

		//Update InfModel without calling reasoner
		AppLoader.InfModel = AppLoader.baseRepository.clone(AppLoader.Model);

		//Update lists
		//HomeController.UpdateLists();

		//Update list instances modified
		AppLoader.updateModifiedList();

		return "redirect:list";
	}

	@RequestMapping(method = RequestMethod.GET, value="/completeInstanceAuto")
	public String completeInstanceAuto(@RequestParam("idInstance") String idInstance, HttpServletRequest request) {

		//Instance selected
		Instance instance = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(idInstance));

		AppLoader.Model = AppLoader.ManagerInstances.CompleteInstanceAuto(instance, AppLoader.baseRepository.getNameSpace(), AppLoader.Model, AppLoader.InfModel, AppLoader.ListAllInstances);

		AppLoader.ListModifiedInstances.add(instance.ns + instance.name);

		try {
			AppLoader.updateLists();
		} catch (InconsistentOntologyException e) {
			e.printStackTrace();
		} catch (OKCoExceptionInstanceFormat e) {
			e.printStackTrace();
		}

		//Update list instances modified
		AppLoader.updateModifiedList();

		return "redirect:list";
	}

	@RequestMapping(method = RequestMethod.GET, value="/graphVisualizer")
	public String graphVisualizer(@RequestParam("id") String id, @RequestParam("typeView") String typeView, HttpServletRequest request) {

		String valuesGraph = "";
		int width;
		int height;
		String subtitle = "";
		GraphPlotting graphPlotting = new WOKCOGraphPlotting();
		Instance i;
		int num = 0;

		try  
		{  
			num = Integer.parseInt(id);  
		}  
		catch(NumberFormatException nfe)  
		{  
			typeView = "ALL";
		}

		//TypeView -> ALL/IN/OUT
		if(typeView.equals("ALL"))
		{

			//All instances
			valuesGraph  = graphPlotting.getArborStructureFor(AppLoader.InfModel); 

		} else if(id != null && num > 0){

			//Get the instance
			i = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(id));

			if(typeView.equals("IN"))			//in on instance
			{				
				//Get the values
				valuesGraph  = graphPlotting.getArborStructureComingInOf(AppLoader.InfModel, i.ns + i.name);

			} else if(typeView.equals("OUT")) {	//out from instance

				//Get the values
				valuesGraph  = graphPlotting.getArborStructureComingOutOf(AppLoader.InfModel, i.ns + i.name);	
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

		Instance i = new Instance(AppLoader.baseRepository.getNameSpace(), name, new ArrayList<String>(), listDif, listSame, false);

		listNewInstancesRelation.add(i);
		return i;
	}

	@RequestMapping(value="/commitInstance", method = RequestMethod.POST)
	public @ResponseBody DtoResultCommit commitInstance(@RequestBody final DtoCommitPost dtoCommit) {    

		boolean isCreate = false;
		boolean isUpdate = false;
		DtoResultCommit dto = new DtoResultCommit();
		if(listNewInstancesRelation.size() != 0)
		{
			Instance iSource = instanceSelected;
			for (Instance iTarget : listNewInstancesRelation) 
			{
				try {

					if(iTarget.existInModel == false)
					{
						//Create instance
						AppLoader.Model = AppLoader.ManagerInstances.CreateInstance(iSource.ns + iSource.name, dtoSelected.Relation, iTarget, dtoSelected.Target, AppLoader.ListAllInstances, AppLoader.Model);
						isCreate = true;

					} else {

						//Selected instance
						AppLoader.Model = AppLoader.ManagerInstances.CreateRelationProperty(iSource.ns + iSource.name, dtoSelected.Relation, iTarget.ns + iTarget.name, AppLoader.Model);
						isUpdate = true;
					}

					if(dtoCommit.commitReasoner.equals("true"))
					{
						//Update InfModel calling reasoner
						AppLoader.InfModel = AppLoader.reasoner.run(AppLoader.Model);

					} else {
						//Update InfModel without calling reasoner
						AppLoader.InfModel = AppLoader.baseRepository.clone(AppLoader.Model);

						//Add on list modified instances
						AppLoader.ListModifiedInstances.add(iTarget.ns + iTarget.name);
					}			 

					//Update list
					//HomeController.UpdateLists();
					AppLoader.updateAddingToLists(iTarget.ns + iTarget.name);

				} catch (Exception e) {

					if(isCreate == true)
						AppLoader.Model = AppLoader.ManagerInstances.DeleteInstance(iTarget, AppLoader.Model);

					if(isUpdate == true)
						AppLoader.Model = AppLoader.ManagerInstances.DeleteRelationProperty(iSource.ns + iSource.name, dtoSelected.Relation, iTarget.ns + iTarget.name, AppLoader.Model);

					dto.result = e.getMessage();
					dto.ok = false;
					return dto;
				}

			} // end for

			if(dtoCommit.commitReasoner.equals("true"))
			{

			} else {
				//Add on list modified instances
				AppLoader.ListModifiedInstances.add(iSource.ns + iSource.name);			
			}

			//Update list instances modified
			AppLoader.updateModifiedList();

			dto.ok = true;
			dto.result = "ok";
			return dto;
		}

		dto.ok = true;
		dto.result = "nothing";
		return dto;
	}

	@RequestMapping(value="/runReasoner", method = RequestMethod.POST)
	public @ResponseBody DtoResultCommit runReasoner() {    

		DtoResultCommit dto = new DtoResultCommit();
		try {

			//Run reasoner
			AppLoader.InfModel = AppLoader.reasoner.run(AppLoader.Model);

			//Save temporary model
			AppLoader.tmpModel = AppLoader.baseRepository.clone(AppLoader.Model);

			//Update list instances
			AppLoader.updateLists();

			//Clean list modified instances
			AppLoader.ListModifiedInstances = new ArrayList<String>();

			//Update list instances modified
			AppLoader.updateModifiedList();

		} catch (Exception e) {

			//Roll back the tempModel
			AppLoader.Model = AppLoader.baseRepository.clone(AppLoader.tmpModel);
			AppLoader.InfModel = AppLoader.reasoner.run(AppLoader.Model);

			//Update list instances
			try {
				AppLoader.updateLists();

			} catch (InconsistentOntologyException e1) {

				//e1.printStackTrace();

			} catch (OKCoExceptionInstanceFormat e1) {

				//e1.printStackTrace();
			}

			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";

			dto.result = error;
			dto.ok = false;
			return dto;
		}

		dto.ok = true;
		dto.result = "ok";
		return dto;
	}

	@RequestMapping(value="/removeInstance", method = RequestMethod.GET)
	public @ResponseBody String removeInstance(@RequestParam String id) {    

		if(id != null)
		{
			Instance.removeFromList(listNewInstancesRelation, id);
			return id;
		}

		return null;		  
	}

	@RequestMapping(value="/editInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance editInstance(@RequestParam String id) {    

		if(id != null)
		{
			Instance i = AppLoader.ManagerInstances.getInstance(listNewInstancesRelation, Integer.parseInt(id));
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, listNewInstancesRelation);
			return dto;
		}

		return null;	  
	}

	@RequestMapping(value="/selectInstance", method = RequestMethod.GET)
	public @ResponseBody DtoViewSelectInstance selectInstance(@RequestParam String id) {    

		if(id != null)
		{
			Instance i = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(id));
			DtoViewSelectInstance dto = new DtoViewSelectInstance(i, AppLoader.ListAllInstances);
			return dto;
		}

		return null;
	}

	@RequestMapping(value="/selectInstanceAdd", method = RequestMethod.GET)
	public @ResponseBody Instance selectInstanceAdd(@RequestParam String id) { 

		//Add in listNewInstancesRelation

		if(id != null)
		{
			Instance i = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(id));
			listNewInstancesRelation.add(i);
			return i;
		}

		return null;
	}

	@RequestMapping(value="/commitMaxCard", method = RequestMethod.POST)
	public @ResponseBody DtoResultCommit commitMaxCard(@RequestBody final DtoCommitMaxCard dto){    
		
		DtoResultCommit dtoResult = new DtoResultCommit();
		
		try {
			
			String separatorValues = "%&&%";

			String[] arrayValues = dto.ListInstanceDifSameIds.split(separatorValues);
			for (String val : arrayValues) {

				if(val.contains("x"))
				{	
					String[] parts = val.split("x");

					String type = parts[0];
					String idInsSource = parts[1];
					String idInsTarget = parts[2];
					
					Instance s1 = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(idInsSource));
					Instance s2 = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, Integer.parseInt(idInsTarget));
					
					if(type.equals("dif"))
					{
						AppLoader.Model = AppLoader.ManagerInstances.setDifferentInstances(s1.ns + s1.name, s2.ns + s2.name, AppLoader.Model);
						
					} else if (type.equals("same"))
					{
						AppLoader.Model = AppLoader.ManagerInstances.setSameInstances(s1.ns + s1.name, s2.ns + s2.name, AppLoader.Model);
						
					} else {
						
						dtoResult.result = "error";
						dtoResult.ok = false;
						return dtoResult;
					}
					
					AppLoader.ListModifiedInstances.add(s1.ns + s1.name);
					AppLoader.ListModifiedInstances.add(s2.ns + s2.name);
				}

			}
			
			if(dto.runReasoner.equals("true"))
			{
				try {

					//Run reasoner
					AppLoader.InfModel = AppLoader.reasoner.run(AppLoader.Model);

					//Save temporary model
					AppLoader.tmpModel = AppLoader.baseRepository.clone(AppLoader.Model);

					//Update list instances
					AppLoader.updateLists();

					//Clean list modified instances
					AppLoader.ListModifiedInstances = new ArrayList<String>();

					//Update list instances modified
					AppLoader.updateModifiedList();

				} catch (Exception e) {

					//Roll back the tempModel
					AppLoader.Model = AppLoader.baseRepository.clone(AppLoader.tmpModel);
					AppLoader.InfModel = AppLoader.reasoner.run(AppLoader.Model);

					//Update list instances
					try {
						AppLoader.updateLists();

					} catch (InconsistentOntologyException e1) {

						//e1.printStackTrace();

					} catch (OKCoExceptionInstanceFormat e1) {

						//e1.printStackTrace();
					}

					String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
					
					dtoResult.result = error;
					dtoResult.ok = false;
					return dtoResult;
				}
				
			} if(dto.runReasoner.equals("false")) {
				
				//Update list instances modified
				AppLoader.updateModifiedList();
				
			} else {
				
				dtoResult.result = "error";
				dtoResult.ok = false;
				return dtoResult;
			}
			
		} catch (Exception e) {
			
			dtoResult.result = "error";
			dtoResult.ok = false;
			return dtoResult;
		}
		

		dtoResult.result = "ok";
		dtoResult.ok = true;
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
	public @ResponseBody DtoResultCommit commitDataValues(@RequestBody final DtoCommitPost dtoCommit) {    

		DtoResultCommit dto = new DtoResultCommit();
		if(listNewDataValuesRelation.size() != 0)
		{
			Instance iSource = instanceSelected;
			for (DataPropertyValue dataTarget : listNewDataValuesRelation) 
			{
				try {

					if(dataTarget.existInModel == false)
					{
						//Create data value
						AppLoader.Model = AppLoader.ManagerInstances.CreateTargetDataProperty(iSource.ns + iSource.name, dtoSelected.Relation, dataTarget.value, dtoSelected.Target, AppLoader.Model);
						dataTarget.existInModel = true;
					}

					if(dtoCommit.commitReasoner.equals("true"))
					{
						//Update InfModel calling reasoner
						AppLoader.InfModel = AppLoader.reasoner.run(AppLoader.Model);

					} else {
						//Update InfModel without calling reasoner
						AppLoader.InfModel = AppLoader.baseRepository.clone(AppLoader.Model);

						//Add on list modified instances
						AppLoader.ListModifiedInstances.add(iSource.ns + iSource.name);
					}						 

					//Update list instances
					AppLoader.updateLists();

				} catch (Exception e) {

					AppLoader.Model = AppLoader.ManagerInstances.DeleteTargetDataProperty(instanceSelected.ns + instanceSelected.name, dtoSelected.Relation, dataTarget.value, dtoSelected.Target, AppLoader.Model);

					dto.result = e.getMessage();
					dto.ok = false;
					return dto;
				}
			} //end for

			//Update list instances modified
			AppLoader.updateModifiedList();

			dto.ok = true;
			dto.result = "ok";
			return dto;
		} //end if

		dto.ok = true;
		dto.result = "nothing";
		return dto;
	}


	/*------ AJAX - Specializations -----*/	

	@RequestMapping(value="/classifyInstanceClasses", method = RequestMethod.POST)
	public @ResponseBody DtoResultCommit classifyInstanceClasses(@RequestBody final DtoClassifyInstancePost dto) throws InconsistentOntologyException, OKCoExceptionInstanceFormat{    

		DtoResultCommit dtoResult = new DtoResultCommit();
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

					AppLoader.Model = AppLoader.ManagerInstances.AddInstanceToClass(instanceSelected.ns + instanceSelected.name, cls, AppLoader.Model);

				} catch (Exception e) {

					dtoResult.result = e.getMessage();
					dtoResult.ok = false;
					return dtoResult;
				}
			}

			try {

				//Validate and update list
				AppLoader.updateAddingToLists(instanceSelected.ns + instanceSelected.name);;

				//Instance selected update
				instanceSelected = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, instanceSelected .id);

			} catch (Exception e) {

				dtoResult.result = e.getMessage();
				dtoResult.ok = false;

				//Remove all created
				for (String clsAux : listCls) {
					AppLoader.Model = AppLoader.ManagerInstances.RemoveInstanceOnClass(instanceSelected.ns + instanceSelected.name, clsAux, AppLoader.Model);
				}

				//Validate and update list and infModel
				AppLoader.updateLists();

				//Instance selected update
				instanceSelected = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, instanceSelected .id);

				return dtoResult;
			}	

			//Add on list modified instances
			AppLoader.ListModifiedInstances.add(instanceSelected.ns + instanceSelected.name);

			//Update list instances modified
			AppLoader.updateModifiedList();

			dtoResult.ok = true;
			dtoResult.result = "ok";
			return dtoResult;
		}

		dtoResult.ok = true;
		dtoResult.result = "nothing";
		return dtoResult;		  
	}

	@RequestMapping(value="/classifyInstanceProperty", method = RequestMethod.POST)
	public @ResponseBody DtoResultCommit classifyInstanceProperty(@RequestBody final DtoClassifyInstancePost dto) throws InconsistentOntologyException, OKCoExceptionInstanceFormat{

		DtoPropertyAndSubProperties dtoSpec = DtoPropertyAndSubProperties.getInstance(ListSpecializationProperties, Integer.parseInt(dto.id));

		DtoResultCommit dtoResult = new DtoResultCommit();
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
						AppLoader.Model = AppLoader.ManagerInstances.CreateTargetDataProperty(instanceSelected.ns + instanceSelected.name, subRel, dtoSpec.iTargetNs.split("\\^\\^")[0], dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName, AppLoader.Model);
					else
						//Case object property
						AppLoader.Model = AppLoader.ManagerInstances.CreateRelationProperty(instanceSelected.ns + instanceSelected.name, subRel, dtoSpec.iTargetNs + dtoSpec.iTargetName, AppLoader.Model);

				} catch (Exception e) {

					dtoResult.result = e.getMessage();
					dtoResult.ok = false;
					return dtoResult;
				}
			}

			try {
				
				//Validate and update list
				AppLoader.updateAddingToLists(instanceSelected.ns + instanceSelected.name);

				//Instance selected update
				instanceSelected = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, instanceSelected .id);

			} catch (Exception e) {

				dtoResult.result = e.getMessage();
				dtoResult.ok = false;

				//Remove all created
				for (String subRelAux : listRelations) {

					if(dtoSpec.propertyType.equals(OntPropertyEnum.DATA_PROPERTY))
						//Case data property
						AppLoader.Model = AppLoader.ManagerInstances.DeleteTargetDataProperty(instanceSelected.ns + instanceSelected.name, subRelAux, dtoSpec.iTargetNs.split("\\^\\^")[0], dtoSpec.iTargetNs.split("\\^\\^")[1] + dtoSpec.iTargetName, AppLoader.Model);
					else
						//Case object property
						AppLoader.Model = AppLoader.ManagerInstances.DeleteRelationProperty(instanceSelected.ns + instanceSelected.name, subRelAux, dtoSpec.iTargetNs + dtoSpec.iTargetName, AppLoader.Model);
				}

				//Validate and update list and infModel
				AppLoader.updateLists();

				//Instance selected update
				instanceSelected = AppLoader.ManagerInstances.getInstance(AppLoader.ListAllInstances, instanceSelected .id);

				return dtoResult;
			}			  

			//Add on list modified instances
			AppLoader.ListModifiedInstances.add(instanceSelected.ns + instanceSelected.name);

			//Update list instances modified
			AppLoader.updateModifiedList();

			dtoResult.ok = true;
			dtoResult.result = "ok";
			return dtoResult;
		}

		dtoResult.ok = true;
		dtoResult.result = "nothing";
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
