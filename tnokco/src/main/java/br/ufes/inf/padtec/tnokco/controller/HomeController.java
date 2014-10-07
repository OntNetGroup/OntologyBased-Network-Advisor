package br.ufes.inf.padtec.tnokco.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.com.padtec.okco.domain.DtoDefinitionClass;
import br.com.padtec.okco.domain.DtoResultCommit;
import br.com.padtec.okco.domain.FactoryInstances;
import br.com.padtec.okco.domain.HermitReasonerImpl;
import br.com.padtec.okco.domain.Instance;
import br.com.padtec.okco.domain.ManagerInstances;
import br.com.padtec.okco.domain.OKCoExceptionFileFormat;
import br.com.padtec.okco.domain.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.domain.OKCoExceptionNS;
import br.com.padtec.okco.domain.OKCoExceptionReasoner;
import br.com.padtec.okco.domain.OntologyReasoner;
import br.com.padtec.okco.domain.PelletReasonerImpl;
import br.com.padtec.okco.domain.Search;
import br.com.padtec.okco.persistence.BaseModelRepository;
import br.com.padtec.okco.persistence.BaseModelRepositoryImpl;
import br.ufes.inf.padtec.tnokco.business.ManagerRelations;
import br.ufes.inf.padtec.tnokco.business.Reader;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

@Controller
public class HomeController implements ServletContextAware{
	
	public static BaseModelRepository Repository;
	public static OntologyReasoner Reasoner;
	public static OntModel Model;
	public static OntModel tmpModel;	
	public static InfModel InfModel;

	public static Search Search;
	public static String NS;
	public static FactoryInstances FactoryInstances;
	public static ManagerInstances ManagerInstances;	
	public static ArrayList<Instance> ListAllInstances;
	public static ArrayList<String> ListModifiedInstances;
	
	//servelet context
	private ServletContext servletContext;
	
	//Control reasoner
	public static boolean reasoningOnFirstLoad;

	public static ArrayList<DtoDefinitionClass> ModelDefinitions;

	@RequestMapping(method = RequestMethod.GET, value="/")
	public String index(HttpSession session, HttpServletRequest request) {

		request.getSession().removeAttribute("errorMensage");
		request.getSession().removeAttribute("loadOk");

		return "login";	//View to return
	}

	@RequestMapping(method = RequestMethod.GET, value="/welcome")
	public String welcome(HttpSession session, HttpServletRequest request) {		

		String login = (String)request.getSession().getAttribute("login");
		if(login == null)
			login = "";

		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");
			
			//Initializing variables
			
			Repository = new BaseModelRepositoryImpl();
			
			//Select Reasoner
			
			Reasoner = new HermitReasonerImpl();
			reasoningOnFirstLoad = false;
			
			//load g800

			String path = servletContext.getInitParameter("PathG800owl"); 
			
			// Load Model
			Repository.readBaseOntModel(path);
			HomeController.Model = Repository.getBaseOntModel();
			
			//Load infModel
			HomeController.InfModel = Repository.clone(HomeController.Model);

			// Name space
			HomeController.NS = Repository.getNameSpace();

			HomeController.Search = new Search();
			HomeController.FactoryInstances = new FactoryInstances(HomeController.Search);
			HomeController.ManagerInstances = new ManagerInstances(HomeController.Search, HomeController.FactoryInstances, HomeController.Model);

			//Save temporary model
			HomeController.tmpModel = Repository.clone(HomeController.Model);		

			//List modified instances
			HomeController.ListModifiedInstances = new ArrayList<String>();			
			
			//List All instances
			HomeController.ListAllInstances = new ArrayList<Instance>(); 

			return "index";	//View to return
			
		} else {

			return "login";	//View to return
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/faq")
	public String faq(HttpSession session, HttpServletRequest request) {

		String login = (String)request.getSession().getAttribute("login");
		if(login == null)
			login = "";

		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");

			return "faq";	//View to return
		} else {

			return "login";	//View to return
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/about")
	public String about(HttpSession session, HttpServletRequest request) {

		String login = (String)request.getSession().getAttribute("login");
		if(login == null)
			login = "";

		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");

			return "about";	//View to return
		} else {

			return "login";	//View to return
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password){

		if(username.equals("advisor") && password.equals("1234"))
		{
			request.getSession().setAttribute("login", "true");
			return "redirect:welcome";

		} else {

			request.getSession().setAttribute("login", "false");
			return "login";
		}
	}
	
	/*AJAX - Select reasoner*/
	@RequestMapping(value="/selectReasoner", method = RequestMethod.GET)
	public @ResponseBody String selectReasoner(@RequestParam String reasoner) {    

		String result = "ok";
		if(reasoner.equals("hermit"))
		{
			Reasoner = new HermitReasonerImpl();

		} else if(reasoner.equals("pellet"))
		{
			Reasoner = new PelletReasonerImpl();
			
		} else {

			try {
				throw new OKCoExceptionReasoner("Please select a reasoner available.");
			} catch (OKCoExceptionReasoner e) {
				
				result = "error";
			}
		}

		return result;		  
	}
	
	/*AJAX - Load first*/
	@RequestMapping(value="/loadFirst", method = RequestMethod.GET)
	public @ResponseBody String loadFirst(@RequestParam String loadFirst) {    

		String result = "ok";
		if(loadFirst.equals("true"))
		{
			reasoningOnFirstLoad = true;

		} else if(loadFirst.equals("false"))
		{
			reasoningOnFirstLoad = false;
			
		} else {	
			
			result = "error";
		}

		return result;		  
	}
	
	@RequestMapping(value = "/uploadOwl", method = RequestMethod.POST) 
	public String uploadOwl(HttpServletRequest request){

		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");

			if(! file.getOriginalFilename().endsWith(".owl"))
			{
				throw new OKCoExceptionFileFormat("Please select owl file.");
			}

			Repository = new BaseModelRepositoryImpl();
			
			// Load Model
			InputStream in = file.getInputStream();
			Repository.readBaseOntModel(in);
			Model = Repository.getBaseOntModel();
			
			// Name space
			NS = Repository.getNameSpace();

			if(NS == null)
			{
				throw new OKCoExceptionNS("Please select owl file with defined namespace.");
			}

			Search = new Search();
			FactoryInstances = new FactoryInstances(Search);
			ManagerInstances = new ManagerInstances(Search, FactoryInstances, Model);

			//Save temporary model
			tmpModel = Repository.clone(HomeController.Model);
			InfModel = Repository.clone(HomeController.Model);
			
			if(reasoningOnFirstLoad == true)
			{
				//Call reasoner
				InfModel = Reasoner.run(Model);
				
			} else {
				
				//Don't call reasoner
				InfModel = Repository.clone(Model);
			}

			//List modified instances
			ListModifiedInstances = new ArrayList<String>();
			
			//List All instances
			HomeController.ListAllInstances = new ArrayList<Instance>();

			// Update list instances
			UpdateLists();

		} catch (InconsistentOntologyException e) {

			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			request.getSession().setAttribute("errorMensage", error);

			//Roll back the last valid model

			Model = HomeController.Repository.clone(HomeController.tmpModel);
			InfModel = HomeController.Repository.clone(HomeController.Model);

			try {

				UpdateLists();

			} catch (InconsistentOntologyException e1) {

				// Never get in here
				e1.printStackTrace();

			} catch (OKCoExceptionInstanceFormat e1) {

				// Never get in here
				e1.printStackTrace();
			}			

			return "index";

		} catch (OKCoExceptionInstanceFormat e) {

			String error = "Entity format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			Model = null;
			tmpModel = null;
			InfModel = null;
			ListAllInstances = null;
			ListModifiedInstances = null;
			Reasoner = null;

			return "index";

		} catch (OKCoExceptionFileFormat e) {

			String error = "File format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			Model = null;
			tmpModel = null;
			InfModel = null;
			ListAllInstances = null;
			ListModifiedInstances = null;
			Reasoner = null;

			return "index";

		} catch (IOException e) {

			String error = "File not found.";
			request.getSession().setAttribute("errorMensage", error);
			Model = null;
			tmpModel = null;
			InfModel = null;
			ListAllInstances = null;
			ListModifiedInstances = null;
			Reasoner = null;

			return "index";

		} catch (OKCoExceptionNS e) {

			String error = "File namespace error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			Model = null;
			tmpModel = null;
			InfModel = null;
			ListAllInstances = null;
			ListModifiedInstances = null;
			Reasoner = null;

			return "index";

		}

		request.getSession().removeAttribute("errorMensage");  
		return "redirect:list";
	}

	@RequestMapping(method = RequestMethod.GET, value="/getModel")
	public String getModel(HttpSession session, HttpServletRequest request) throws IOException {

		if(Model != null)
		{
			request.getSession().removeAttribute("loadOk");
			request.getSession().setAttribute("model", Repository.getBaseOntModelAsString());

			return "model";

		} else {

			request.getSession().removeAttribute("model");
			request.getSession().setAttribute("loadOk", "false");
			return "index";
		}
	}

	@RequestMapping(value = "/uploadSindel", method = RequestMethod.POST)
	public String uploadSindel(HttpServletRequest request){

		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");

			// Load sindel file

			InputStream in = file.getInputStream();
			InputStreamReader r = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(r);
			Reader readerFile = new Reader();

			String txtSindel = readerFile.readFile(br);
			SindelController.txtSindelCode = txtSindel;

		}  catch (IOException e) {

			System.out.println("File not found.");
			return "index";
		}

		return "redirect:sindel";
	}
	
	@RequestMapping(value = "/uploadCondel", method = RequestMethod.POST)
	public String uploadCondel(HttpServletRequest request){

		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");

			// Load sindel file

			InputStream in = file.getInputStream();
			InputStreamReader r = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(r);
			Reader readerFile = new Reader();

			String txtCondel = readerFile.readFile(br);
			CondelController.txtCondelCode = txtCondel;

		}  catch (IOException e) {

			System.out.println("File not found.");
			return "index";
		}

		return "redirect:condel";
	}

	@RequestMapping(value = "/EnforceSubRelation", method = RequestMethod.GET)
	public String EnforceSubRelation(HttpServletRequest request){

		String login = (String)request.getSession().getAttribute("login");
		if(login == null)
			login = "";

		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");
			
			ManagerRelations mRelations = new ManagerRelations(HomeController.Search, HomeController.ManagerInstances);
		  	HomeController.Model = mRelations.EnforceSubRelation(HomeController.Model, HomeController.InfModel, HomeController.NS);
		  	
		  	//Update list instances
			try {
				HomeController.UpdateLists();
			} catch (InconsistentOntologyException e) {

				e.printStackTrace();
			} catch (OKCoExceptionInstanceFormat e) {

				e.printStackTrace();
			}

			//Clean list modified instances
			//HomeController.ListModifiedInstances = new ArrayList<String>();

			//Update list instances modified
			HomeController.UpdateListsModified();

		  	request.getSession().removeAttribute("errorMensage");  
			return "redirect:list";
			
		} else {

			return "login";	//View to return
		}	
		
	}
	
	public static void UpdateLists() throws InconsistentOntologyException, OKCoExceptionInstanceFormat {

		try {

			//Refresh list of instances
	    	
	    	ListAllInstances = ManagerInstances.getAllInstances(Model, InfModel, NS);
	    	
	    	//Get model definitions on list of instances
	    	
		  	ModelDefinitions = Search.GetModelDefinitionsInInstances(ListAllInstances, InfModel);
		  	
			// Organize data (Update the list of all instances)
			
	    	ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, ModelDefinitions, Model, InfModel, NS);			
			ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, InfModel, NS);	
			
		} catch (InconsistentOntologyException e) {

			throw e;

		} catch (OKCoExceptionInstanceFormat e) {

			throw e;
		}
	}

	public static void UpdateAddIntanceInLists(String instanceURI) throws InconsistentOntologyException, OKCoExceptionInstanceFormat {
		
		try {
			
	    	//Get model definitions on list of instances
	    	
			ArrayList<DtoDefinitionClass> intanceDefinitions = Search.GetModelDefinitionsInInstances(instanceURI, Model, InfModel, ListAllInstances, ManagerInstances);
		  	ModelDefinitions.addAll(intanceDefinitions); 
			
			// Organize data (Update the list of all instances)
			
	    	ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, intanceDefinitions, Model, InfModel, NS);			
			ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, InfModel, NS);				
			
		} catch (InconsistentOntologyException e) {

			throw e;
			
		}
    }
	
	public static void UpdateListsModified()
	{
		// Update list instances modified
		for (Instance i : ListAllInstances) {
			String s = i.ns + i.name;
			if (ListModifiedInstances.contains(s))
			{
				i.setModified(true);
			}
		}
	}


	/* AJAX */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public DtoResultCommit save(HttpServletRequest request)
	{
		DtoResultCommit dto = new DtoResultCommit();
		if(Model != null)
		{
			dto.ok = true;
			Repository.saveBaseOntModel("");

		} else {
			dto.ok = false;
		}

		return dto;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		
	}
}
