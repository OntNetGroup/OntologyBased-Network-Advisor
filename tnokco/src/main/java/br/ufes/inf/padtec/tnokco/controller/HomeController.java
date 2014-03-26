package br.ufes.inf.padtec.tnokco.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.ufes.inf.nemo.okco.business.FactoryModel;
import br.ufes.inf.nemo.okco.business.Search;
import br.ufes.inf.nemo.okco.business.ManagerInstances;
import br.ufes.inf.nemo.okco.business.FactoryInstances;
import br.ufes.inf.nemo.okco.model.DtoDefinitionClass;
import br.ufes.inf.nemo.okco.model.Instance;
import br.ufes.inf.nemo.okco.model.DtoResultCommit;
import br.ufes.inf.nemo.okco.model.EnumReasoner;
import br.ufes.inf.nemo.okco.model.EnumRelationType;
import br.ufes.inf.nemo.okco.model.IFactory;
import br.ufes.inf.nemo.okco.model.IReasoner;
import br.ufes.inf.nemo.okco.model.IRepository;
import br.ufes.inf.nemo.okco.model.OKCoExceptionFileFormat;
import br.ufes.inf.nemo.okco.model.OKCoExceptionInstanceFormat;
import br.ufes.inf.nemo.okco.model.OKCoExceptionNS;
import br.ufes.inf.nemo.okco.model.OKCoExceptionReasoner;
import br.ufes.inf.padtec.tnokco.business.Reader;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

@Controller
public class HomeController {

	public static IFactory Factory;
	public static IRepository Repository;
	public static IReasoner Reasoner;
	public static OntModel Model;
	public static OntModel tmpModel;	
	public static InfModel InfModel;

	public static Search Search;
	public static String NS;
	public static FactoryInstances FactoryInstances;
	public static ManagerInstances ManagerInstances;	
	public static ArrayList<Instance> ListAllInstances;
	public static ArrayList<String> ListModifiedInstances;
	
	public static ArrayList<DtoDefinitionClass> dtoSomeRelationsList;
	public static ArrayList<DtoDefinitionClass> dtoMinRelationsList;
	public static ArrayList<DtoDefinitionClass> dtoMaxRelationsList;
	public static ArrayList<DtoDefinitionClass> dtoExactlyRelationsList;	

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
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	  public String login(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password){
		
		if(username.equals("tnokco") && password.equals("1234"))
		{
			request.getSession().setAttribute("login", "true");
			return "redirect:welcome";
			
		} else {
			
			request.getSession().setAttribute("login", "false");
			return "login";
		}
	}

	@RequestMapping(value = "/uploadOwlTnokco", method = RequestMethod.POST)
	public String uploadOwlTnokco(HttpServletRequest request, @RequestParam("optionsReasoner") String optionsReasoner){

		try {

			 Factory = new FactoryModel();
			 Repository = Factory.GetRepository();
			  
			  //Select reasoner
			  if(optionsReasoner.equals("hermit"))
			  {
				  Reasoner = Factory.GetReasoner(EnumReasoner.HERMIT);
				  
			  } else if(optionsReasoner.equals("pellet"))
			  {
				  Reasoner = Factory.GetReasoner(EnumReasoner.PELLET);
			  } else {
				  
				  throw new OKCoExceptionReasoner("Please select a resoner available.");
			  }			  
				
			  MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			  MultipartFile file = multipartRequest.getFile("file");
			  
			  if(! file.getOriginalFilename().endsWith(".owl"))
			  {
				  throw new OKCoExceptionFileFormat("Please select owl file.");
			  }
				 
			  // Load Model
			  InputStream in = file.getInputStream();
			  Model = Repository.Open(in);
			  InputStream in2 = file.getInputStream();
			  tmpModel = Repository.Open(in2);
			  
			  // Name space
			  NS = Repository.getNameSpace(Model);
			  
			  if(NS == null)
			  {
				  throw new OKCoExceptionNS("Please select owl file with defined namespace.");
			  }
			  
			  Search = new Search(NS);
		  	  FactoryInstances = new FactoryInstances(Search);
		  	  ManagerInstances = new ManagerInstances(Search, FactoryInstances, Model);
		  	  
		  	  //Save temporary model
		  	  tmpModel = Repository.CopyModel(Model);
		  	  
		  	  //Call reasoner
		  	  InfModel = Reasoner.run(Model);
		  	  
		  	  //Nao executa
//		  	  InfModel = Repository.CopyModel(Model);
		  	  
		  	  //List modified instances
		  	  ListModifiedInstances = new ArrayList<String>();
		  	  
		  	  // Gets relations on model
			  dtoSomeRelationsList = Search.GetSomeRelations(InfModel);
			  dtoMinRelationsList = Search.GetMinRelations(InfModel);
			  dtoMaxRelationsList = Search.GetMaxRelations(InfModel);
			  dtoExactlyRelationsList = Search.GetExactlyRelations(InfModel);

			  // Update list instances
			  UpdateLists();
			  
		} catch (InconsistentOntologyException e) {

			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			request.getSession().setAttribute("errorMensage", error);
			
			//Roll back the last valid model
			
			Model = HomeController.Repository.CopyModel(HomeController.tmpModel);
			InfModel = HomeController.Repository.CopyModel(HomeController.Model);

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
			
		} catch (OKCoExceptionReasoner e) {

			String error = "Reasoner error: " + e.getMessage();
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
		return "redirect:sindel";
	}

	@RequestMapping(value = "/uploadOwl", method = RequestMethod.POST)
	public String uploadOwl(HttpServletRequest request, @RequestParam("optionsReasoner") String optionsReasoner){

		System.out.println("RODOU CARALHOOOOOO");
		try {

			 Factory = new FactoryModel();
			 Repository = Factory.GetRepository();
			  
			  //Select reasoner
			  if(optionsReasoner.equals("hermit"))
			  {
				  Reasoner = Factory.GetReasoner(EnumReasoner.HERMIT);
				  
			  } else if(optionsReasoner.equals("pellet"))
			  {
				  Reasoner = Factory.GetReasoner(EnumReasoner.PELLET);
			  } else {
				  
				  throw new OKCoExceptionReasoner("Please select a resoner available.");
			  }			  
				
			  MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			  MultipartFile file = multipartRequest.getFile("file");
			  
			  if(! file.getOriginalFilename().endsWith(".owl"))
			  {
				  throw new OKCoExceptionFileFormat("Please select owl file.");
			  }
				 
			  // Load Model
			  InputStream in = file.getInputStream();
			  Model = Repository.Open(in);
			  InputStream in2 = file.getInputStream();
			  tmpModel = Repository.Open(in2);
			  
			  // Name space
			  NS = Repository.getNameSpace(Model);
			  
			  if(NS == null)
			  {
				  throw new OKCoExceptionNS("Please select owl file with defined namespace.");
			  }
			  
			  Search = new Search(NS);
		  	  FactoryInstances = new FactoryInstances(Search);
		  	  ManagerInstances = new ManagerInstances(Search, FactoryInstances, Model);
		  	  
		  	  //Save temporary model
		  	  tmpModel = Repository.CopyModel(Model);
		  	  
		  	  //Call reasoner
		  	  InfModel = Reasoner.run(Model);
		  	  InfModel = Repository.CopyModel(Model);
		  	  
		  	  //Nao executa
		  	  //InfModel = Repository.CopyModel(Model);
		  	  
		  	  //List modified instances
		  	  ListModifiedInstances = new ArrayList<String>();
		  	  
		  	  // Gets relations on model
			  dtoSomeRelationsList = Search.GetSomeRelations(InfModel);
			  dtoMinRelationsList = Search.GetMinRelations(InfModel);
			  dtoMaxRelationsList = Search.GetMaxRelations(InfModel);
			  dtoExactlyRelationsList = Search.GetExactlyRelations(InfModel);

			  // Update list instances
			  UpdateLists();
			  
		} catch (InconsistentOntologyException e) {

			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			request.getSession().setAttribute("errorMensage", error);
			
			//Roll back the last valid model
			
			Model = HomeController.Repository.CopyModel(HomeController.tmpModel);
			InfModel = HomeController.Repository.CopyModel(HomeController.Model);

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
			
		} catch (OKCoExceptionReasoner e) {

			String error = "Reasoner error: " + e.getMessage();
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
			request.getSession().setAttribute("model", Repository.getModelString(Model));

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

			Factory = new FactoryModel();
			Repository = Factory.GetRepository();

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

	public static void UpdateLists() throws InconsistentOntologyException, OKCoExceptionInstanceFormat {
		
		try {
			
	    	// Refresh list of instances
	    	
	    	ListAllInstances = ManagerInstances.getAllInstances(Model, InfModel, NS);
			
			// Organize data (Update the list of all instances)
			
	    	ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, dtoSomeRelationsList, EnumRelationType.SOME, Model, InfModel, NS);
			ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, dtoMinRelationsList, EnumRelationType.MIN, Model, InfModel, NS);
			ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, dtoMaxRelationsList, EnumRelationType.MAX, Model, InfModel, NS);
			ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, dtoExactlyRelationsList, EnumRelationType.EXACTLY, Model, InfModel, NS);
			ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, InfModel, NS);				
			
		} catch (InconsistentOntologyException e) {

			throw e;
			
		} catch (OKCoExceptionInstanceFormat e) {
			
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
			Repository.Save(Model, "");

		} else {
			dto.ok = false;
		}

		return dto;
	}
}
