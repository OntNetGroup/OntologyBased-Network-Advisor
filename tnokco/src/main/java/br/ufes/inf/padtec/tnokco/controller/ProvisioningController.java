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
import br.ufes.inf.nemo.okco.business.ManagerInstances;
import br.ufes.inf.nemo.okco.business.Search;
import br.ufes.inf.nemo.okco.model.DtoDefinitionClass;
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
		this.getCandidateInterfacesForConnection(null);
		
		
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

	public ArrayList<Instance> getCandidateInterfacesForConnection(Instance interfaceOutput){
		Instance outputInterface = null;
		for (Instance instance : HomeController.ListAllInstances) {
			for (String className : instance.ListClasses) {
				className = className.replace(HomeController.NS, "");
				if(className.equalsIgnoreCase("Output_Interface")){
					outputInterface = instance;
					break;
				}
			}
			
			if(outputInterface != null){
				break;
			}
		}
		Search search = new Search(HomeController.NS);
		ArrayList<DtoInstanceRelation> outIntRelations = search.GetInstanceRelations(HomeController.InfModel, outputInterface.ns+outputInterface.name);
		
		String outputNs = "";
		for (DtoInstanceRelation outRelation : outIntRelations) {
			if(outRelation.Property.equalsIgnoreCase("http://www.semanticweb.org/ontologies/2014/4/ontology.owl#maps_output")){
				outputNs = outRelation.Target;
				break;
			}
		}
		
		Instance output;
		for (Instance instance : HomeController.ListAllInstances) {
			if(outputNs.equals(instance.ns+instance.name)){
				output = instance;
				break;
			}
		}
		
		//ArrayList<DtoDefinitionClass> t = HomeController.ModelDefinitions;
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
		
		for (Instance input : inputInterfaces) {
			for(String className : input.ListClasses){
				HashMap<String, String> tf1 = new HashMap<String, String>();
				tf1.put("INPUT", className);
				tf1.put("OUTPUT", "Output_Interface");
				
				Provisioning.getInstance().values.get(null);
			}			
		}
		
		
		
		ArrayList<Instance> equipmentInterfaces = new ArrayList<Instance>();
		
		
		
		return equipmentInterfaces;
	}
}
