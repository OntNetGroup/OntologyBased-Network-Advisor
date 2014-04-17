package br.ufes.inf.padtec.tnokco.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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

@Controller
public class ProvisioningController{
	
	@RequestMapping(method = RequestMethod.GET, value="/newEquipment")
	public String newEquipment(HttpSession session, HttpServletRequest request) {
		
		
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
	
	@RequestMapping(method = RequestMethod.POST, value="/runEquipSindel")
	public @ResponseBody DtoResultAjax runEquipSindel(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {

		String sindelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();
		String individualsPrefixName = dtoGet.equipmentName;
		
		dto.ok = false;
		dto.result = "It is necessary to inform the name of the new Equipment.";				
		/*
		if(individualsPrefixName == null){
			return dto;
		}else if(individualsPrefixName == ""){
			return dto;
		}		
		*/
		
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
	
	@RequestMapping(method = RequestMethod.GET, value="/provision")
	public @ResponseBody DtoResultAjax provision(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {

		DtoResultAjax dto = new DtoResultAjax();
		
		String outInt = "eq1_outInt";
     	String inInt = "eq2_inInt";
		
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
		
		String sindelCode = "";
		
		sindelCode += "out_int: " + outInt + ";\n";
		sindelCode += "in_int: " + inInt + ";\n";
		sindelCode += "binds(" + outInt + "," + inInt + ");\n";
		
		sindelCode += "output: " + outputNs + ";\n";
		sindelCode += "input: " + inputNs + ";\n";
		sindelCode += "binds(" + outputNs + "," + inputNs + ");\n";
		
		try {		  	      
			
			// Populate the model
			Sindel2OWL so = new Sindel2OWL(HomeController.Model);
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

		//request.getSession().removeAttribute("errorMensage");      
		
		dto.ok = true;
		dto.result = "ok";

		return dto;
	}
	
	

	public ArrayList<String> getCandidateInterfacesForConnection(String outIntNs){
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
			outIntNs = instNs.replace(instance.ns, "");
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
		for (DtoInstanceRelation outRelation : outIntRelations) {
			if(outRelation.Property.equalsIgnoreCase(HomeController.NS+"maps_output")){
				outputNs = outRelation.Target;
			}else if(outRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf")){
				eqOutNs = outRelation.Target;
			}
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
				Boolean alreadyConnected = true;
				//pego o NS do input mapeada pela interface de input
				for (DtoInstanceRelation inRelation : inIntRelations) {
					if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"maps_input")){
						inputNs = inRelation.Target;
					}else if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"INV.componentOf")){
						eqInNs = inRelation.Target;						
					}else if(inRelation.Property.equalsIgnoreCase(HomeController.NS+"binds")){
						alreadyConnected = false;
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
				interfaceReturn += inputNs;
				interfaceReturn += "#";
				
				if(hasAllowedRelation && !eqInNs.equals(eqOutNs)){
					if(allowedInputInterfaces.contains(interfaceReturn)){
						allowedInputInterfaces.remove(interfaceReturn);
					}
					interfaceReturn += "true;";
				}else{
					if(!allowedInputInterfaces.contains(interfaceReturn.replace("true;", "false;"))){
						interfaceReturn += "false;";
					}					
				}
				
				
				
				if(!allowedInputInterfaces.contains(interfaceReturn) && !allowedInputInterfaces.contains(interfaceReturn+"true;")){
					allowedInputInterfaces.add(interfaceReturn);
				}				
			}
		}
		
		return allowedInputInterfaces;
	}
}
