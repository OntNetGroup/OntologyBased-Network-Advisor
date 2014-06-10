
package br.ufes.inf.padtec.tnokco.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.ufes.inf.nemo.okco.business.FactoryModel;
import br.ufes.inf.nemo.okco.business.Search;
import br.ufes.inf.nemo.okco.model.DtoInstanceRelation;
import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.nemo.okco.model.Instance;
import br.ufes.inf.nemo.okco.model.OKCoExceptionFileFormat;
import br.ufes.inf.nemo.okco.model.OKCoExceptionInstanceFormat;
import br.ufes.inf.nemo.padtec.Sindel2OWL;
import br.ufes.inf.nemo.padtec.processors.BindsProcessor;
import br.ufes.inf.padtec.tnokco.business.Reader;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Statement;

@Controller
public class ProvisioningController{
	private final int maxElements = 10;

	@RequestMapping(method = RequestMethod.GET, value="/newEquipment")
	public String newEquipment(HttpSession session, HttpServletRequest request) {
		request.getSession().setAttribute("txtSindelCode", "");
		request.getSession().setAttribute("txtSindelCodeBr", "");
		request.getSession().setAttribute("action", "");
		request.getSession().setAttribute("equipNameAux", "");
		request.getSession().setAttribute("equipName", "");
		
		for (int i = 1; i <= maxElements; i++) {
			request.getSession().setAttribute("equipName"+i, "");
			request.getSession().setAttribute("txtSindel"+i, "");
			request.getSession().setAttribute("file"+i, "");
			request.getSession().setAttribute("filename"+i, "");
		}

		/*
		String path = "http://localhost:8080/tnokco/Assets/owl/g800.owl"; 

		// Load Model
		HomeController.Model = HomeController.Repository.Open(path);
		HomeController.tmpModel = HomeController.Repository.Open(path);
		HomeController.NS = HomeController.Repository.getNameSpace(HomeController.Model);
		 */

		if(HomeController.Model == null)
		{
			String error = "Error! You need to load the model first.";
			request.getSession().setAttribute("errorMensage", error);

			return "index";
		}

		this.cleanEquipSindel(request);

		//this.getCandidateInterfacesForConnection(null);
		//this.provision(null, null);

		return "newEquipment";	//View to return
	}

	@RequestMapping(value = "/uploadSindelEquip", method = RequestMethod.POST)
	public String uploadSindelEquip(HttpServletRequest request){

		try {

			HomeController.Factory = new FactoryModel();
			HomeController.Repository = HomeController.Factory.GetRepository();

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");

			if(! file.getOriginalFilename().endsWith(".sindel"))
			{
				throw new OKCoExceptionFileFormat("Please select owl file.");
			}
			// Load sindel file

			InputStream in = file.getInputStream();
			InputStreamReader r = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(r);
			Reader readerFile = new Reader();

			String txtSindel = readerFile.readFile(br);
			//SindelController.txtSindelCode = txtSindel;
			request.getSession().setAttribute("txtSindelCode", txtSindel);
			txtSindel = txtSindel.replaceAll("\n", "<br>");
			request.getSession().setAttribute("txtSindelCodeBr", txtSindel);
			
			String equipNameAux = multipartRequest.getParameter("equipNameAux");
			request.getSession().setAttribute("equipNameAux", equipNameAux);
			request.getSession().setAttribute("equipName", equipNameAux);

		}  catch (IOException e) {
			String error = "File not found.\n" + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			System.out.println("File not found.");
		} catch (OKCoExceptionFileFormat e) {
			String error = "File format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);			
		}

		return "newEquipment";			
	}

	@RequestMapping(method = RequestMethod.POST, value="/runEquipTypes")
	public @ResponseBody DtoResultAjax runEquipTypes(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {

		DtoResultAjax dto = new DtoResultAjax();
		dto.ok = false;
		dto.result = "It is necessary to inform the name of the new Equipment.";				



		for (int i = 0; i < dtoGet.equipments.length; i++) {
			String individualsPrefixName = dtoGet.equipments[i][0];
			String sindelParsedCode = dtoGet.equipments[i][1];

			if(individualsPrefixName == null){
				return dto;
			}else if(individualsPrefixName == ""){
				return dto;
			}

			try {		  	      

				// Populate the model
				Sindel2OWL so = new Sindel2OWL(HomeController.Model, individualsPrefixName);
				so.run(sindelParsedCode);

				HomeController.Model = so.getDtoSindel().model;

				//HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);
				HomeController.InfModel = so.getDtoSindel().model;
				
				// Update list instances
				HomeController.UpdateLists();

			} catch (InconsistentOntologyException e) {

				String error = "Ontology have inconsistence: " + e.toString();
				System.out.println(error);
				request.getSession().setAttribute("errorMensage", error);
				HomeController.Model = null;
				HomeController.InfModel = null;
				HomeController.ListAllInstances = null;

				dto.ok = false;
				dto.result = error;				
				return dto;

			} catch (OKCoExceptionInstanceFormat e) {

				String error = "Entity format error: " + e.toString();
				System.out.println(error);
				request.getSession().setAttribute("errorMensage", error);
				HomeController.Model = null;
				HomeController.InfModel = null;
				HomeController.ListAllInstances = null;

				dto.ok = false;
				dto.result = error;				
				return dto;				
			}
		}

		request.getSession().removeAttribute("errorMensage");      

		dto.ok = true;
		dto.result = "ok";

		this.cleanEquipSindel(request);

		return dto;
	}

	@RequestMapping(method = RequestMethod.POST, value="/uploadEquipTypes")
	public String uploadEquipTypes(HttpServletRequest request) {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		try{
			//MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

			for(int i = 1; i <= maxElements; i++){
				MultipartFile file = multipartRequest.getFile("file"+i);
				//Object file = request.getAttribute("file"+i);
				//Enumeration teste = request.getAttributeNames();

				if(file.getSize() <= 0){
					throw new OKCoExceptionFileFormat("Select one file on the position " + i + ".");
				}else if(! file.getOriginalFilename().endsWith(".sindel"))
				{
					throw new OKCoExceptionFileFormat("Please select owl file on the position " + i + ".");		
				}

				InputStream in = file.getInputStream();
				InputStreamReader r = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(r);
				Reader readerFile = new Reader();

				String txtSindel = readerFile.readFile(br);
				String equipName = multipartRequest.getParameter("equipName"+i);

				request.getSession().setAttribute("equipName"+i, equipName);
				request.getSession().setAttribute("txtSindel"+i, txtSindel);
				request.getSession().setAttribute("file"+i, file);
				request.getSession().setAttribute("filename"+i, file.getOriginalFilename());

			}
		}  catch (IOException e) {
			String error = "File not found.\n" + e.getMessage();
			multipartRequest.getSession().setAttribute("errorMensage", error);
			System.out.println("File not found.");
		} catch (OKCoExceptionFileFormat e) {
			String error = "File format error: " + e.getMessage();
			multipartRequest.getSession().setAttribute("errorMensage", error);			
		}

		request.getSession().setAttribute("action", "runParser");
		return "newEquipment";
	}

	@RequestMapping(method = RequestMethod.POST, value="/runEquipScratch")
	public @ResponseBody DtoResultAjax runEquipScratch(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {

		String sindelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();
		String individualsPrefixName = dtoGet.equipmentName;

		dto.ok = false;
		dto.result = "It is necessary to inform the name of the new Equipment.";				

		if(individualsPrefixName == null){
			return dto;
		}else if(individualsPrefixName == ""){
			return dto;
		}		


		try {		  	      

			// Populate the model
			Sindel2OWL so = new Sindel2OWL(HomeController.Model, individualsPrefixName);
			so.run(sindelCode);

			HomeController.Model = so.getDtoSindel().model;

			//HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);
			HomeController.InfModel = so.getDtoSindel().model;
			// Update list instances
			HomeController.UpdateLists();

		} catch (InconsistentOntologyException e) {

			String error = "Ontology have inconsistence: " + e.toString();
			System.out.println(error);
			request.getSession().setAttribute("errorMensage", error);
			HomeController.Model = null;
			HomeController.InfModel = null;
			HomeController.ListAllInstances = null;

			dto.ok = false;
			dto.result = error;				
			return dto;

		} catch (OKCoExceptionInstanceFormat e) {

			String error = "Entity format error: " + e.toString();
			System.out.println(error);
			request.getSession().setAttribute("errorMensage", error);
			HomeController.Model = null;
			HomeController.InfModel = null;
			HomeController.ListAllInstances = null;

			dto.ok = false;
			dto.result = error;				
			return dto;				
		}

		//FAZER O CATCH DA SINDEL

		request.getSession().removeAttribute("errorMensage");      

		dto.ok = true;
		dto.result = "ok";

		this.cleanEquipSindel(request);

		return dto;
	}

	@RequestMapping(method = RequestMethod.GET, value="/cleanEquipSindel")
	public @ResponseBody DtoResultAjax cleanEquipSindel(HttpServletRequest request) {

		String txtSindelCode = "";
		request.getSession().setAttribute("txtSindelCode", txtSindelCode);

		DtoResultAjax dto = new DtoResultAjax();
		dto.ok = true;
		dto.result = "ok";

		return dto;
	}

	@RequestMapping(method = RequestMethod.GET, value="/provisioning")
	public String provisioning(HttpSession session, HttpServletRequest request) {

		return "provisioning";	//View to return
	}
	
	public static DtoResultAjax binds(String outInt, String inInt, HttpServletRequest request, Boolean updateListsInTheEnd, ArrayList<String> listInstancesCreated) {

		DtoResultAjax dto = new DtoResultAjax();

		String outputNs = "";
		String inputNs = "";

		Search search = new Search(HomeController.NS);
		ArrayList<DtoInstanceRelation> outIntRelations = search.GetInstanceRelations(HomeController.InfModel, HomeController.NS+outInt);
		for (DtoInstanceRelation outIntRelation : outIntRelations) {
			if(outIntRelation.Property.equalsIgnoreCase(HomeController.NS+"maps_output")){
				outputNs = outIntRelation.Target;
				outputNs = outputNs.replace(HomeController.NS, "");
				break;
			}
		}		

		ArrayList<DtoInstanceRelation> inIntRelations = search.GetInstanceRelations(HomeController.InfModel, HomeController.NS+inInt);
		for (DtoInstanceRelation inIntRelation : inIntRelations) {
			if(inIntRelation.Property.equalsIgnoreCase(HomeController.NS+"maps_input")){
				inputNs = inIntRelation.Target;
				inputNs = inputNs.replace(HomeController.NS, "");
				break;
			}
		}
		
		Boolean inputIsPmInput = false;
		if(inputNs.equals("")){
			Instance inputOrIntInput = getInstanceFromNameSpace(HomeController.NS+inInt);
			for (String inputOrIntInputClass : inputOrIntInput.ListClasses) {
				if(inputOrIntInputClass.equals(HomeController.NS+"Physical_Media_Input")){
					inputNs = inInt;
					inputIsPmInput = true;
					break;
				}
			}
		}
		System.out.println();
		Boolean outputIsPmOutput = false;
		if(outputNs.equals("")){
			Instance outputOrIntOutput = getInstanceFromNameSpace(HomeController.NS+outputNs);
			for (String outputOrIntOutputClass : outputOrIntOutput.ListClasses) {
				if(outputOrIntOutputClass.equals(HomeController.NS+"Physical_Media_Output")){
					outputNs = inInt;
					outputIsPmOutput = true;
					break;
				}
			}
		}
		
		//binds Interface out with in
		Individual a, b;
		ObjectProperty rel = null;
		if(inputIsPmInput && !outputIsPmOutput){
			a = HomeController.Model.getIndividual(HomeController.NS+outInt);
			b = HomeController.Model.getIndividual(HomeController.NS+inputNs);
			rel = HomeController.Model.getObjectProperty(HomeController.NS+"binds_PM_out_interface");
		}else if(!inputIsPmInput && outputIsPmOutput){
			a = HomeController.Model.getIndividual(HomeController.NS+inInt);
			b = HomeController.Model.getIndividual(HomeController.NS+outputNs);
			rel = HomeController.Model.getObjectProperty(HomeController.NS+"binds_PM_in_Interface");
		}else{
			a = HomeController.Model.getIndividual(HomeController.NS+outInt);
			b = HomeController.Model.getIndividual(HomeController.NS+inInt);
			rel = HomeController.Model.getObjectProperty(HomeController.NS+"interface_binds");
		}
		
		Statement stmt = HomeController.Model.createStatement(a, rel, b);
		HomeController.Model.add(stmt);

		if(listInstancesCreated == null){
			listInstancesCreated = new ArrayList<String>();
		}
		listInstancesCreated.add(a.getNameSpace()+a.getLocalName());
		listInstancesCreated.add(b.getNameSpace()+b.getLocalName());
		
		if(!outputNs.equals("") && !inputNs.equals("")){
			
			a = HomeController.Model.getIndividual(HomeController.NS+outputNs);
			b = HomeController.Model.getIndividual(HomeController.NS+inputNs);
			
//			ArrayList<String> tiposA=HomeController.Search.GetClassesFrom(HomeController.NS+a.getLocalName(),HomeController.Model);
//			ArrayList<String> tiposB=HomeController.Search.GetClassesFrom(HomeController.NS+b.getLocalName(),HomeController.Model);
//			tiposA.remove(HomeController.NS+"Geographical_Element");
//			tiposA.remove(HomeController.NS+"Bound_Input-Output");
//			tiposB.remove(HomeController.NS+"Geographical_Element");
//			tiposB.remove(HomeController.NS+"Bound_Input-Output");
//			rel = HomeController.Model.getObjectProperty(HomeController.NS+"binds");
//			stmt = HomeController.Model.createStatement(a, rel, b);
//			HomeController.Model.add(stmt);	
//			HashMap<String, String> hash = new HashMap<String, String>();
//			hash.put("INPUT", tiposB.get(0));
//			hash.put("OUTPUT", tiposA.get(0));
//			HashMap<String, String>element= Provisioning.values.get(hash);
//			Provisioning.bindsSpecific(a,b,tiposA.get(0),tiposB.get(0));
			//BindsProcessor.bindPorts(outputNs, inputNs);
			
			BindsProcessor.bindPorts(null, a, b, null, HomeController.NS, HomeController.Model, listInstancesCreated);

		}

		HomeController.InfModel = HomeController.Model;
		
		
		if(updateListsInTheEnd){
			
			try {
				
				for (String instanceUri : listInstancesCreated) {
					HomeController.UpdateAddIntanceInLists(instanceUri);	
				}
				
			} catch (InconsistentOntologyException e) {
				
				e.printStackTrace();
				
			} catch (OKCoExceptionInstanceFormat e) {
				
				e.printStackTrace();
			}			
		}
		
		dto.ok = true;
		dto.result = "ok";

		return dto;

	}

	public static ArrayList<String> getCandidateInterfacesForConnection(String outIntNs){
		
		ArrayList<String> allowedInputInterfaces = new ArrayList<String>();
		//find the instance of the output interface
		Instance outputInterface = getInstanceFromNameSpace(outIntNs);
//		Instance outputInterface = null;
//		for (Instance instance : HomeController.ListAllInstances) {
//			String instNs = instance.name;
//			instNs = instNs.replace(instance.ns, "");
//			outIntNs = outIntNs.replace(instance.ns, "");
//			if(instNs.equals(outIntNs)){
//				outputInterface = instance;
//				break;
//			}
//		}

		//get all relations of the output interface
		Search search = new Search(HomeController.NS);
		ArrayList<DtoInstanceRelation> outIntRelations = search.GetInstanceRelations(HomeController.InfModel, outputInterface.ns+outputInterface.name);

		//get namespaces of individuals of some output interface relations
		String outputNs = "";
		String eqOutNs = "";
		String interfaceBindsNs = "";
		for (DtoInstanceRelation outRelation : outIntRelations) {
			if(outRelation.Property.equalsIgnoreCase(HomeController.NS+"maps_output")){
				outputNs = outRelation.Target;
			}else if(outRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf")){
				eqOutNs = outRelation.Target;
			}else if(outRelation.Property.equalsIgnoreCase(HomeController.NS+"interface_binds")){
				interfaceBindsNs = outRelation.Target;
			}
		}

		ArrayList<Instance> inputInterfaces = getInstancesFromClass("Input_Interface");
		ArrayList<Instance> physicalMediaInputs = getInstancesFromClass("Physical_Media_Input");
		
		//if the output interface does not maps an output, it can not connects
		if(outputNs.equals("")){
			for (Instance inputInterface : inputInterfaces) {
				ArrayList<DtoInstanceRelation> inIntRelations = search.GetInstanceRelations(HomeController.InfModel, inputInterface.ns+inputInterface.name);
				String eqInNs = "";
				
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inRelation : inIntRelations) {
					if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf")){
						eqInNs = inRelation.Target;		
						break;
					}
				}
				
				//if(!eqInNs.equals(eqOutNs)){
					String interfaceReturn = "";
					interfaceReturn += eqInNs; 
					interfaceReturn += "#";
					interfaceReturn += inputInterface.name;
					interfaceReturn += "#";
					interfaceReturn += "false";
					
					allowedInputInterfaces.add(interfaceReturn);
				//}
				
				
				
			}
			return allowedInputInterfaces;
		}
		
		//verify if the output interface is already binded
		Boolean outputInterfaceAlreadyBinded = false;
		if(!interfaceBindsNs.equals("")){
			outputInterfaceAlreadyBinded = true;
		}
		
		//get the instance of the output mapped by the output interface
		Instance output = null;
		for (Instance instance : HomeController.ListAllInstances) {
			if(outputNs.equals(instance.ns+instance.name)){
				output = instance;
				break;
			}
		}

		//get all input interfaces
		
//		ArrayList<Instance> inputInterfaces = new ArrayList<Instance>(); 
//		for (Instance instance : HomeController.ListAllInstances) {
//			for (String className : instance.ListClasses) {
//				className = className.replace(HomeController.NS, "");
//				if(className.equalsIgnoreCase("Input_Interface")){
//					inputInterfaces.add(instance);
//					break;
//				}
//			}
//		}

		
		//now, the idea is compare all types of the output with all types of all inputs
		for (String outputClassName : output.ListClasses) {
			outputClassName = outputClassName.replace(HomeController.NS, "");
			
			//here, I look for possible connections with input interfaces
			for (Instance inputInterface : inputInterfaces) {
				ArrayList<DtoInstanceRelation> inIntRelations = search.GetInstanceRelations(HomeController.InfModel, inputInterface.ns+inputInterface.name);
				String inputNs = "";
				String eqInNs = "";
				Boolean inputInterfaceAlreadyConnected = false;
				
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inRelation : inIntRelations) {
					if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"maps_input")){
						inputNs = inRelation.Target;
					}else if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf")){
						eqInNs = inRelation.Target;						
					}else if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.interface_binds")){
						inputInterfaceAlreadyConnected = true;
					}
				}

				//since I verify the inverse relation of interface_binds above, 
				//it's necessary to verify if some output interface has the interface_binds relation
				//with the actual input interface
				//the block below it's for this purpose
				if(!inputInterfaceAlreadyConnected){
					for(Instance otherOutput : HomeController.ListAllInstances){
						for (String otherOutputClassName : otherOutput.ListClasses) {
							otherOutputClassName = otherOutputClassName.replace(HomeController.NS, "");
							if(otherOutputClassName.equalsIgnoreCase("Output_Interface")){
								ArrayList<DtoInstanceRelation> otherOutputRelations = search.GetInstanceRelations(HomeController.InfModel, otherOutput.ns+otherOutput.name);
								for (DtoInstanceRelation otherOutputRelation : otherOutputRelations) {
									if(otherOutputRelation.Property.equalsIgnoreCase(HomeController.NS+"interface_binds")){
										if((inputInterface.ns+inputInterface.name).equals(otherOutputRelation.Target)){
											inputInterfaceAlreadyConnected = true;
											break;
										}
									}
								}
								if(inputInterfaceAlreadyConnected){
									break;
								}
							}
						}
						if(inputInterfaceAlreadyConnected){
							break;
						}
					}
				}
				
				Boolean hasAllowedRelation = false;
				if(inputNs != ""){
					//get the input mapped by the input interface 
					Instance input = null;
					for (Instance instance : HomeController.ListAllInstances) {
						if(inputNs.equals(instance.ns+instance.name)){
							input = instance;
							break;
						}
					}
					
					//for each input and output class names, I verify if exist a possible relation of binds
					for(String inputClassName : input.ListClasses){
						inputClassName = inputClassName.replace(HomeController.NS, ""); 
						HashMap<String, String> tf1 = new HashMap<String, String>();
						tf1.put("INPUT", inputClassName);
						tf1.put("OUTPUT", outputClassName);

						HashMap<String, String> allowedRelation = BindsProcessor.values.get(tf1);

						if(allowedRelation != null){
							hasAllowedRelation = true;
							break;
						}
					}
				}
				
				String interfaceReturn = "";
				eqInNs = eqInNs.replace(inputInterface.ns, "");
				inputNs = inputNs.replace(inputInterface.ns, "");
				eqOutNs = eqOutNs.replace(outputInterface.ns, "");
				interfaceReturn += eqInNs; 
				interfaceReturn += "#";
				interfaceReturn += inputInterface.name;
				interfaceReturn += "#";

				//the return only can be true if:
				// - has an allowed relation
				// - it is a different equipment
				// - the output interface it is not binded
				// - the input interface it is not binded
				Boolean hasCyclicalRel = false;
				
				if(hasAllowedRelation && !eqInNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputInterfaceAlreadyConnected){
					hasCyclicalRel = hasCyclicalRelationship(outputInterface.ns, eqOutNs, inputInterface.ns, eqInNs);
				}
				
				if(hasAllowedRelation && !eqInNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputInterfaceAlreadyConnected && !hasCyclicalRel){
					if(allowedInputInterfaces.contains(interfaceReturn+"false;")){
						allowedInputInterfaces.remove(interfaceReturn+"false;");
					}
					interfaceReturn += "true;";
				}else{
					if(!allowedInputInterfaces.contains(interfaceReturn.replace("true;", "false;"))){
						interfaceReturn += "false;";
					}					
				}

				if(!allowedInputInterfaces.contains(interfaceReturn) && !allowedInputInterfaces.contains(interfaceReturn.replace("false;", "true;"))){
					allowedInputInterfaces.add(interfaceReturn);
				}				
			}
			
			//here, I look for possible connections with physical media inputs
			for (Instance pmInput : physicalMediaInputs) {
				ArrayList<DtoInstanceRelation> inPMRelations = search.GetInstanceRelations(HomeController.InfModel, pmInput.ns+pmInput.name);
				String pmNs = "";
				Boolean inputPMAlreadyConnected = false;
				
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inRelation : inPMRelations) {
					if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf.Single_Physical_Media.Physical_Media_Input") || inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf")){
						pmNs = inRelation.Target;
					}else if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.binds_PM_out_interface")){
						inputPMAlreadyConnected = true;
					}
				}
				
				//since I verify the inverse relation of binds_PM_out_interface above, 
				//it's necessary to verify if some output port has the binds_PM_out_interface relation
				//with the actual PM input
				//the block below it's for this purpose
				if(!inputPMAlreadyConnected){
					for(Instance otherOutput : HomeController.ListAllInstances){
						for (String otherOutputClassName : otherOutput.ListClasses) {
							otherOutputClassName = otherOutputClassName.replace(HomeController.NS, "");
							if(otherOutputClassName.equalsIgnoreCase("Output")){
								if(otherOutput.name.equals("out_sotf3")){
									System.out.println();
								}
								ArrayList<DtoInstanceRelation> otherOutputRelations = search.GetInstanceRelations(HomeController.InfModel, otherOutput.ns+otherOutput.name);
								for (DtoInstanceRelation otherOutputRelation : otherOutputRelations) {
									if(otherOutputRelation.Property.equalsIgnoreCase(HomeController.NS+"interface_binds") || otherOutputRelation.Property.equalsIgnoreCase(HomeController.NS+"binds")){
										if((pmInput.ns+pmInput.name).equals(otherOutputRelation.Target)){
											inputPMAlreadyConnected = true;
											break;
										}
									}
								}
								if(inputPMAlreadyConnected){
									break;
								}
							}
						}
						if(inputPMAlreadyConnected){
							break;
						}
					}
				}
				
				Boolean hasAllowedRelation = false;
				
				//for each input and output class names, I verify if exist a possible relation of binds
				for(String pmInputClassName : pmInput.ListClasses){
					pmInputClassName = pmInputClassName.replace(HomeController.NS, ""); 
					HashMap<String, String> tf1 = new HashMap<String, String>();
					tf1.put("INPUT", pmInputClassName);
					tf1.put("OUTPUT", outputClassName);

					HashMap<String, String> allowedRelation = BindsProcessor.values.get(tf1);

					if(allowedRelation != null){
						hasAllowedRelation = true;
						break;
					}
				}
				
				String interfaceReturn = "";
				pmNs = pmNs.replace(pmInput.ns, "");
				//inputNs = inputNs.replace(pmInput.ns, "");
				eqOutNs = eqOutNs.replace(outputInterface.ns, "");
				interfaceReturn += pmNs; 
				interfaceReturn += "#";
				interfaceReturn += pmInput.name;
				interfaceReturn += "#";
				
				if(hasAllowedRelation && !pmNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputPMAlreadyConnected){
					if(allowedInputInterfaces.contains(interfaceReturn+"false;")){
						allowedInputInterfaces.remove(interfaceReturn+"false;");
					}
					interfaceReturn += "true;";
				}else{
					if(!allowedInputInterfaces.contains(interfaceReturn.replace("true;", "false;"))){
						interfaceReturn += "false;";
					}					
				}

				if(!allowedInputInterfaces.contains(interfaceReturn) && !allowedInputInterfaces.contains(interfaceReturn.replace("false;", "true;"))){
					allowedInputInterfaces.add(interfaceReturn);
				}	
			}
		}

		return allowedInputInterfaces;
	}
	
	public static Instance getInstanceFromNameSpace(String nameSpace){
		Instance instance = null;
		for (Instance inst : HomeController.ListAllInstances) {
			String instNs = inst.name;
			instNs = instNs.replace(inst.ns, "");
			nameSpace = nameSpace.replace(inst.ns, "");
			if(instNs.equals(nameSpace)){
				instance = inst;
				break;
			}
		}
		return instance;
	}
	
	public static ArrayList<Instance> getInstancesFromClass(String classNameWithoutNameSpace){
		ArrayList<String> classNamesWithoutNameSpace = new ArrayList<String>();
		classNamesWithoutNameSpace.add(classNameWithoutNameSpace);
		
		ArrayList<Instance> instances = getInstancesFromClasses(classNamesWithoutNameSpace);
		
		return instances;
	}
	
	public static ArrayList<Instance> getInstancesFromClasses(ArrayList<String> classNamesWithoutNameSpace){
		ArrayList<Instance> instances = new ArrayList<Instance>();
		for (Instance instance : HomeController.ListAllInstances) {
			for (String classNameWithoutNameSpace : classNamesWithoutNameSpace) {
				Boolean foundInstance = false;
				if(instance.ListClasses.contains(HomeController.NS+classNameWithoutNameSpace)){
					if(!instances.contains(instance)){
						instances.add(instance);
					}
					foundInstance  = true;
					break;
				}
				if(foundInstance){
					break;
				}
			}			
//			for (String className : instance.ListClasses) {
//				className = className.replace(HomeController.NS, "");
//				if(className.equalsIgnoreCase(classNameWithoutNameSpace)){
//					
//				}
//			}
		}
		return instances;
	}
	
	public static Boolean hasCyclicalRelationship(String searchForEquipNS, String searchForEquipName, String actualEquipNS, String actualEquipName){
		if(searchForEquipNS.equals(actualEquipNS) && searchForEquipName.equals(actualEquipName)){
			return true;
		}
		
		ArrayList<DtoInstanceRelation> equipRelations = HomeController.Search.GetInstanceRelations(HomeController.InfModel, actualEquipNS+actualEquipName);
		
		ArrayList<String> outIntNss = new ArrayList<String>();
		//get namespaces of individuals of some input interface relations
		for (DtoInstanceRelation eqRelation : equipRelations) {
			if(eqRelation.Property.equalsIgnoreCase(HomeController.NS+"componentOf.Equipment.Output_Interface") || eqRelation.Property.equalsIgnoreCase(HomeController.NS+"componentOf")){
				Instance outIntInstance = getInstanceFromNameSpace(eqRelation.Target);
				for (String classOutIntName : outIntInstance.ListClasses) {
					if(classOutIntName.equals(HomeController.NS+"Output_Interface")){
						outIntNss.add(eqRelation.Target);
					}
				}
			}
		}
		
		for (String oiNs : outIntNss) {
			Instance outputInterface = getInstanceFromNameSpace(oiNs);
			
			ArrayList<DtoInstanceRelation> outIntRelations = HomeController.Search.GetInstanceRelations(HomeController.InfModel, outputInterface.ns+outputInterface.name);
			String inIntNs = "";
			//get namespaces of individuals of some input interface relations
			for (DtoInstanceRelation outIntRelation : outIntRelations) {
				if(outIntRelation.Property.equalsIgnoreCase(HomeController.NS+"interface_binds")){
					inIntNs = outIntRelation.Target;
					break;
				}
			}
			//se inIntNs != null
			if(!inIntNs.equals("")){
				ArrayList<DtoInstanceRelation> inIntRelations = HomeController.Search.GetInstanceRelations(HomeController.InfModel, inIntNs);
				String inIntEquipNs = "";
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inIntRelation : inIntRelations) {
					if(inIntRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf")){
						inIntEquipNs = inIntRelation.Target;
						break;
					}
				}
				
				String newActualEquipNs = inIntEquipNs.split("#")[0]+"#";
				String newActualEquipName = inIntEquipNs.split("#")[1];
				
				if(hasCyclicalRelationship(searchForEquipNS, searchForEquipName, newActualEquipNs, newActualEquipName)){
					return true;
				}
			}
			
		}
		
		
		return false;
	}
	
	@RequestMapping(value = "/autoBindsAndConnects", method = RequestMethod.POST)
	public String autoBindsAndConnects(HttpServletRequest request){
		autoBinds(request);
		return autoConnects(request);
	}
	
	@RequestMapping(value = "/autoConnects", method = RequestMethod.POST)
	public String autoConnects(HttpServletRequest request){
		return "";
		
	}
	
	@RequestMapping(value = "/autoBinds", method = RequestMethod.POST)
	public String autoBinds(HttpServletRequest request){
		//pego todas as instancias de interface de output nao conectadas
		ArrayList<Instance> outputInterfaces = new ArrayList<Instance>(); 
		for (Instance instance : HomeController.ListAllInstances) {
			for (String className : instance.ListClasses) {
				if(className.equalsIgnoreCase(HomeController.NS+"Output_Interface")){
					ArrayList<DtoInstanceRelation> outputInterfaceRelations = HomeController.Search.GetInstanceRelations(HomeController.InfModel, instance.ns+instance.name);
					boolean alreadyConnected = false;
					for (DtoInstanceRelation outputInterfaceRelation : outputInterfaceRelations) {
						if(outputInterfaceRelation.Property.equalsIgnoreCase(HomeController.NS+"interface_binds")){
							if((instance.ns+instance.name).equals(outputInterfaceRelation.Target)){
								alreadyConnected  = true;
								break;
							}
						}
					}
					
					if(!alreadyConnected){
						outputInterfaces.add(instance);
						break;
					}					
				}
			}
		}
		
		HashMap<String, ArrayList<String>> uniqueCandidatesForBinds = new HashMap<String, ArrayList<String>>();
		
		for (Instance outputInterface : outputInterfaces) {
			ArrayList<String> candidatesForConnection = getCandidateInterfacesForConnection(outputInterface.ns+outputInterface.name);
			int noCandidates = 0;
			String inputCandidateName = "";
			for (String candidate : candidatesForConnection) {
				if(candidate.contains("true")){
					noCandidates++;
					inputCandidateName = candidate.split("#")[1];
				}
				
				if(noCandidates > 1){
					break;
				}
			}	
			
			if(uniqueCandidatesForBinds.containsKey(inputCandidateName)){
				uniqueCandidatesForBinds.get(inputCandidateName).add(outputInterface.name);
			}else if(noCandidates == 1){
				ArrayList<String> outs = new ArrayList<String>();
				outs.add(outputInterface.name);
				uniqueCandidatesForBinds.put(inputCandidateName, outs);
			}
		}
		
		ArrayList<String> listInstancesCreated = new ArrayList<String>();
		int bindsMade = 0;
		String returnMessage = "Interfaces binded:<br>";
		for(Entry<String, ArrayList<String>> candidates : uniqueCandidatesForBinds.entrySet()) {
			String inputInterface = candidates.getKey();
			ArrayList<String> outs = candidates.getValue();
			
			if(outs.size() == 1){
				binds(outs.get(0), inputInterface, request, false, listInstancesCreated);
				bindsMade++;
				returnMessage += outs.get(0);
				returnMessage += " -> ";
				returnMessage += inputInterface;
				returnMessage += "<br>";
			}
			
		}
		
		if(bindsMade>0){
			try {
				for (String instanceUri : listInstancesCreated) {
					HomeController.UpdateAddIntanceInLists(instanceUri);	
				}
				//HomeController.UpdateLists();
			} catch (InconsistentOntologyException e) {
				e.printStackTrace();
			} catch (OKCoExceptionInstanceFormat e) {
				e.printStackTrace();
			}
		}else{
			returnMessage = "No interfaces binded.";
		}
		
		request.getSession().setAttribute("loadOk", returnMessage);
		
		return VisualizationController.provisoning_visualization(request);
	}
	
	public static void getEquipmentsWithRPs(InfModel infModel, String NS, ArrayList<String> equipsWithRps, ArrayList<String> connectsBetweenEqsAndRps){
		if(equipsWithRps == null){
			equipsWithRps = new ArrayList<String>();
		}
		if(connectsBetweenEqsAndRps == null){
			connectsBetweenEqsAndRps = new ArrayList<String>();
		}
		ArrayList<Instance> rpInstances = getInstancesFromClass("Reference_Point");
		
		for (Instance rp : rpInstances) {
			ArrayList<DtoInstanceRelation> rpRelations = HomeController.Search.GetInstanceAllRelations(infModel, rp.ns+rp.name);
			String bindingNs = "";
			for (DtoInstanceRelation rel : rpRelations) {
				String propertyName = rel.Property.replace(NS, "");
				if(propertyName.equals("INV.binding_is_represented_by")){
					bindingNs = rel.Target;
					break;					
				}
			}
			ArrayList<String> equips = new ArrayList<String>();
			if(!bindingNs.equals("")){
				equips = getEquipmentFromBinding(infModel, NS, bindingNs);
			}
			
			String equipmentWithRp = "";
			if(equips.size() == 1){
				equipmentWithRp += equips.get(0).replace(NS, "");
			}else if(equips.size() >= 2){
				for (String eq : equips) {
					String cnct = "";
					cnct += eq.replace(NS, "");
					cnct += "#";
					cnct += rp.name;
					
					String cnctInv = "";
					cnctInv += rp.name;
					cnctInv += "#";
					cnctInv += eq.replace(NS, "");
					
					if(!connectsBetweenEqsAndRps.contains(cnct) && !connectsBetweenEqsAndRps.contains(cnctInv)){
						connectsBetweenEqsAndRps.add(cnct);
					}
				}								
			}
			equipmentWithRp += "#";
			equipmentWithRp += rp.name;
			
			if(!equipsWithRps.contains(equipmentWithRp)){
				equipsWithRps.add(equipmentWithRp);
			}
			
		}
		
		System.out.println();
	}
	
	public static ArrayList<String> getEquipmentFromBinding(InfModel infModel, String NS, String bindingName){
		bindingName = bindingName.replace(NS, "");
		ArrayList<DtoInstanceRelation> bindingRelations = HomeController.Search.GetInstanceAllRelations(infModel, NS+bindingName);
		
		String bindedPort1Ns="";
		String bindedPort2Ns="";
		for (DtoInstanceRelation rel : bindingRelations) {
			String propertyName = rel.Property.replace(NS, "");
			if(propertyName.equals("is_binding")){
				if(bindedPort1Ns.equals("")){
					bindedPort1Ns = rel.Target;
				}else{
					bindedPort2Ns = rel.Target;
					break;
				}
			}
		}
		ArrayList<String> equips = new ArrayList<String>();
		if(!bindedPort1Ns.equals("")){
			//String equipmentNs = getEquipmentFromPort(infModel, NS, bindedPort1Ns, searchEquipmentFromPortToTop(infModel, NS, bindedPort1Ns));
			//if(!equipmentNs.equals("")){
				//equips.add(equipmentNs);
			//}			
			ArrayList<String> equipsNs = getEquipmentFromPort(infModel, NS, bindedPort1Ns, searchEquipmentFromPortToTop(infModel, NS, bindedPort1Ns));
			equips.addAll(equipsNs);
		}
		if(!bindedPort2Ns.equals("")){
			//String equipmentNs = getEquipmentFromPort(infModel, NS, bindedPort2Ns, searchEquipmentFromPortToTop(infModel, NS, bindedPort2Ns));
			//if(!equips.contains(equipmentNs)){
			//if(!equipmentNs.equals("") && !equips.contains(equipmentNs)){
			//	equips.add(equipmentNs);
			//}
			ArrayList<String> equipsNs = getEquipmentFromPort(infModel, NS, bindedPort2Ns, searchEquipmentFromPortToTop(infModel, NS, bindedPort2Ns));
			for (String eqNs : equipsNs) {
				if(!equips.contains(eqNs)){
					equips.add(eqNs);
				}
			}
		}
		return equips;
	}
	
	public static ArrayList<String> getEquipmentFromPort(InfModel infModel, String NS, String bindedPortNs, Boolean searchToTop){
		ArrayList<String> ret = new ArrayList<String>();
		bindedPortNs = bindedPortNs.replace(NS, "");
		
		ArrayList<DtoInstanceRelation> portRelations = HomeController.Search.GetInstanceAllRelations(infModel, NS+bindedPortNs);
		String outIntNs = "";
		String inIntNs = "";
		String tfNs = "";
		for (DtoInstanceRelation portRel : portRelations) {
			String portRelName = portRel.Property.replace(NS, "");
			if(portRelName.equals("INV.maps_output")){
				outIntNs = portRel.Target;
			}else if(portRelName.equals("INV.maps_input")){
				inIntNs = portRel.Target;
			}else if(portRelName.equals("INV.componentOf")){
				tfNs = portRel.Target;
			}
		}
		
		if(!tfNs.equals("") && outIntNs.equals("") && inIntNs.equals("")){
			tfNs = tfNs.replace(NS, "");
			ArrayList<String> tiposPm=HomeController.Search.GetClassesFrom(NS+tfNs,infModel);
			if(tiposPm.contains(NS+"Physical_Media")){
				ret.add(tfNs);
				return ret;
				//return tfNs;
			}
			
			ArrayList<String> nextPorts = new ArrayList<String>(); 
			ArrayList<DtoInstanceRelation> tfRelations = HomeController.Search.GetInstanceAllRelations(infModel, NS+tfNs);
			String eqNs = "";
			for (DtoInstanceRelation tfRel : tfRelations) {
				String tfRelRelName = tfRel.Property.replace(NS, "");
				if(tfRelRelName.equals("INV.componentOf")){
					eqNs = tfRel.Target;
					eqNs = eqNs.replace(NS, "");
					ArrayList<String> tiposEq=HomeController.Search.GetClassesFrom(NS+eqNs,infModel);
					if(tiposEq.contains(NS+"Equipment")){
						ret.add(eqNs);
						return ret;
						//return eqNs;
					}
				}else if(tfRelRelName.equals("componentOf")){
					if(!tfRel.Target.equals(NS+bindedPortNs)){
						ArrayList<String> tiposTf=HomeController.Search.GetClassesFrom(tfRel.Target,infModel);
						if(tiposTf.contains(NS+"Input") || tiposTf.contains(NS+"Output")){
							nextPorts.add(tfRel.Target);
						}
					}
				}
				
			}
			
			ArrayList<String> nextRps = getNextRpsFromTf(infModel, NS, bindedPortNs, nextPorts, searchToTop);
			ret.addAll(nextRps);
			System.out.println();
			
		}else if(!outIntNs.equals("")){
			ret.add(getEquipmentFromInterface(infModel, NS, outIntNs));
			return ret;
			//return getEquipmentFromInterface(infModel, NS, outIntNs);
		}else if(!inIntNs.equals("")){
			ret.add(getEquipmentFromInterface(infModel, NS, inIntNs));
			return ret;
			//return getEquipmentFromInterface(infModel, NS, inIntNs);
		}
		
		return ret;
	}
	
	public static Boolean searchEquipmentFromPortToTop(InfModel infModel, String NS, String portNs){
		portNs = portNs.replace(NS, "");
		ArrayList<String> tiposPort=HomeController.Search.GetClassesFrom(NS+portNs,infModel);
		if(tiposPort.contains(NS+"Output")){
			return true;
		}
		return false;
	}
	
	public static String getEquipmentFromInterface(InfModel infModel, String NS, String interfaceNs){
		interfaceNs = interfaceNs.replace(NS, "");
		ArrayList<DtoInstanceRelation> portRelations = HomeController.Search.GetInstanceAllRelations(infModel, NS+interfaceNs);
		
		for (DtoInstanceRelation intRel : portRelations) {
			String intRelName = intRel.Property.replace(NS, "");
			if(intRelName.equals("INV.componentOf")){
				return intRel.Target;
			}
		}
		
		return "";
	}
	
	public static String getRPFromBinding(InfModel infModel, String NS, String bindingNs){
		bindingNs = bindingNs.replace(NS, "");
		ArrayList<DtoInstanceRelation> bindingRelations = HomeController.Search.GetInstanceAllRelations(infModel, NS+bindingNs);
		
		for (DtoInstanceRelation bindingRel : bindingRelations) {
			String intRelName = bindingRel.Property.replace(NS, "");
			if(intRelName.equals("binding_is_represented_by")){
				return bindingRel.Target;
			}
		}
		
		return "";
	}
	
	public static ArrayList<String> getNextRpsFromTf(InfModel infModel, String NS, String actualPort, ArrayList<String> nextPorts, Boolean searchToTop){
		ArrayList<String> nextRps = new ArrayList<String>();
		for (String portNs : nextPorts) {
			portNs = portNs.replace(NS, "");
			
			ArrayList<String> nextPortClasses = HomeController.Search.GetClassesFrom(NS+portNs, infModel);
			ArrayList<String> actualPortClasses = HomeController.Search.GetClassesFrom(NS+actualPort, infModel);
			
			if((nextPortClasses.contains(NS+"Output") && actualPortClasses.contains(NS+"Input")) || (nextPortClasses.contains(NS+"Input") && actualPortClasses.contains(NS+"Output"))){
				ArrayList<DtoInstanceRelation> portRelations = HomeController.Search.GetInstanceAllRelations(infModel, NS+portNs);
				
				for (DtoInstanceRelation portRel : portRelations) {
					String portRelName = portRel.Property.replace(NS, "");
					if(portRelName.equals("INV.is_binding")){
						ArrayList<DtoInstanceRelation> bindingRelations = HomeController.Search.GetInstanceAllRelations(infModel, portRel.Target);
						for (DtoInstanceRelation bindingRel : bindingRelations) {
							String bindingRelName = bindingRel.Property.replace(NS, "");
							if(bindingRelName.equals("binding_is_represented_by")){
								nextRps.add(bindingRel.Target);
							}
						}					
					}
				}	
			}
			
					
		}
		
		
		return nextRps;
	}
}
