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
import br.ufes.inf.padtec.tnokco.business.Provisioning;
import br.ufes.inf.padtec.tnokco.business.Reader;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
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

	public static DtoResultAjax binds(String outInt, String inInt, HttpServletRequest request, Boolean updateListsInTheEnd) {

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

		//binds Interface out with in
		Individual a = HomeController.Model.getIndividual(HomeController.NS+outInt);
		Individual b = HomeController.Model.getIndividual(HomeController.NS+inInt);
		ObjectProperty rel = HomeController.Model.getObjectProperty(HomeController.NS+"interface_binds");
		Statement stmt = HomeController.Model.createStatement(a, rel, b);
		HomeController.Model.add(stmt);


		if(!outputNs.equals("") && !inputNs.equals("")){
			a = HomeController.Model.getIndividual(HomeController.NS+outputNs);
			b = HomeController.Model.getIndividual(HomeController.NS+inputNs);
			ArrayList<String> tiposA=HomeController.Search.GetClassesFrom(HomeController.NS+a.getLocalName(),HomeController.Model);
			ArrayList<String> tiposB=HomeController.Search.GetClassesFrom(HomeController.NS+b.getLocalName(),HomeController.Model);
			tiposA.remove(HomeController.NS+"Geographical_Element");
			tiposA.remove(HomeController.NS+"Bound_Input-Output");
			tiposB.remove(HomeController.NS+"Geographical_Element");
			tiposB.remove(HomeController.NS+"Bound_Input-Output");
			rel = HomeController.Model.getObjectProperty(HomeController.NS+"binds");
			stmt = HomeController.Model.createStatement(a, rel, b);
			HomeController.Model.add(stmt);	
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put("INPUT", tiposB.get(0));
			hash.put("OUTPUT", tiposA.get(0));
			HashMap<String, String>element= Provisioning.values.get(hash);
			Provisioning.bindsSpecific(a,b,tiposA.get(0),tiposB.get(0));

		}

		HomeController.InfModel = HomeController.Model;
		
		if(updateListsInTheEnd){
			try {
				HomeController.UpdateLists();
			} catch (InconsistentOntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OKCoExceptionInstanceFormat e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		dto.ok = true;
		dto.result = "ok";

		return dto;

	}



	public static ArrayList<String> getCandidateInterfacesForConnection(String outIntNs){
		//pego a primeira interface de output que eu achar, só pra simular a entrada de um argumento
		//ou seja, esse bloco vai ser apagado

		//Instance outputInterface = null;
		Instance outputInterface = null;
		for (Instance instance : HomeController.ListAllInstances) {
			/*
			for (String className : instance.ListClasses) {
				className = className.replace(HomeController.NS, "");
				if(className.equalsIgnoreCase("Output_Interface")){
					outputInterface = instance;
					break;
				}
			}
			 */
			String instNs = instance.name;
			instNs = instNs.replace(instance.ns, "");
			outIntNs = outIntNs.replace(instance.ns, "");
			if(instNs.equals(outIntNs)){
				outputInterface = instance;
				break;
			}
		}

		//busco as relações dessa instância de interface
		Search search = new Search(HomeController.NS);
		ArrayList<DtoInstanceRelation> outIntRelations = search.GetInstanceRelations(HomeController.InfModel, outputInterface.ns+outputInterface.name);

		//pego o namespace completo da relação de maps_out
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

		Boolean outputAlreadyConnected = false;
		if(!interfaceBindsNs.equals("")){
			outputAlreadyConnected = true;
		}
		//pego a instancia do output mapeado pela interface de output
		Instance output = null;
		for (Instance instance : HomeController.ListAllInstances) {
			if(outputNs.equals(instance.ns+instance.name)){
				output = instance;
				break;
			}
		}

		//pego todas as instancias de interface de input
		ArrayList<Instance> inputInterfaces = new ArrayList<Instance>(); 
		for (Instance instance : HomeController.ListAllInstances) {
			for (String className : instance.ListClasses) {
				className = className.replace(HomeController.NS, "");
				if(className.equalsIgnoreCase("Input_Interface")){
					inputInterfaces.add(instance);
					break;
				}
			}
		}

		ArrayList<String> allowedInputInterfaces = new ArrayList<String>();
		//percorro todas as classes do output do TPF
		for (String outputClassName : output.ListClasses) {
			outputClassName = outputClassName.replace(HomeController.NS, "");

			//percorro todas as instancias de interface de input encontradas
			for (Instance inputInterface : inputInterfaces) {
				ArrayList<DtoInstanceRelation> inIntRelations = search.GetInstanceRelations(HomeController.InfModel, inputInterface.ns+inputInterface.name);
				String inputNs = "";
				String eqInNs = "";
				Boolean alreadyConnected = false;
				
				//pego o NS do input mapeada pela interface de input
				for (DtoInstanceRelation inRelation : inIntRelations) {
					if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"maps_input")){
						inputNs = inRelation.Target;
					}else if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf")){
						eqInNs = inRelation.Target;						
					}else if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.interface_binds")){
						alreadyConnected = true;
					}
				}

				if(!alreadyConnected){
					for(Instance otherOutput : HomeController.ListAllInstances){
						for (String otherOutputClassName : otherOutput.ListClasses) {
							otherOutputClassName = otherOutputClassName.replace(HomeController.NS, "");
							if(otherOutputClassName.equalsIgnoreCase("Output_Interface")){
								if(otherOutput.name.equals("soeq1_o1") && inputInterface.name.equals("soeq2_i1")){
									System.out.println();
								}
								ArrayList<DtoInstanceRelation> otherOutputRelations = search.GetInstanceRelations(HomeController.InfModel, otherOutput.ns+otherOutput.name);
								for (DtoInstanceRelation otherOutputRelation : otherOutputRelations) {
									if(otherOutputRelation.Property.equalsIgnoreCase(HomeController.NS+"interface_binds")){
										if((inputInterface.ns+inputInterface.name).equals(otherOutputRelation.Target)){
											alreadyConnected = true;
											break;
										}
									}
								}
								if(alreadyConnected){
									break;
								}
							}
						}
						if(alreadyConnected){
							break;
						}
					}
				}
				
				//busco a instancia do input mapeado pela interface 
				Instance input = null;
				for (Instance instance : HomeController.ListAllInstances) {
					if(inputNs.equals(instance.ns+instance.name)){
						input = instance;
						break;
					}
				}

				Boolean hasAllowedRelation = false;
				//percorro todas as classes do input
				for(String inputClassName : input.ListClasses){
					inputClassName = inputClassName.replace(HomeController.NS, ""); 
					HashMap<String, String> tf1 = new HashMap<String, String>();
					tf1.put("INPUT", inputClassName);
					tf1.put("OUTPUT", outputClassName);

					//verifica na hash do cassio se existe a combinacao entre inputClassName e outputClassName
					HashMap<String, String> allowedRelation = Provisioning.values.get(tf1);

					if(allowedRelation != null){
						hasAllowedRelation = true;
						break;
					}
				}

				String interfaceReturn = "";
				eqInNs = eqInNs.replace(inputInterface.ns, "");
				inputNs = inputNs.replace(inputInterface.ns, "");
				eqOutNs = eqOutNs.replace(inputInterface.ns, "");
				interfaceReturn += eqInNs; 
				interfaceReturn += "#";
				//interfaceReturn += inputNs;
				interfaceReturn += inputInterface.name;
				interfaceReturn += "#";

				if(hasAllowedRelation && !eqInNs.equals(eqOutNs) && !outputAlreadyConnected && !alreadyConnected){
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
		
		int bindsMade = 0;
		for(Entry<String, ArrayList<String>> candidates : uniqueCandidatesForBinds.entrySet()) {
			String inputInterface = candidates.getKey();
			ArrayList<String> outs = candidates.getValue();
			
			if(outs.size() == 1){
				binds(outs.get(0), inputInterface, request, false);
				bindsMade++;
			}
			
		}
		
		if(bindsMade>0){
			try {
				HomeController.UpdateLists();
			} catch (InconsistentOntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OKCoExceptionInstanceFormat e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return VisualizationController.provisoning_visualization(request);
	}
}
