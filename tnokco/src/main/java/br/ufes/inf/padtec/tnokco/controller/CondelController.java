package br.ufes.inf.padtec.tnokco.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import br.ufes.inf.nemo.okco.business.FactoryInstances;
import br.ufes.inf.nemo.okco.business.FactoryModel;
import br.ufes.inf.nemo.okco.business.ManagerInstances;
import br.ufes.inf.nemo.okco.business.Search;
import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.nemo.okco.model.EnumReasoner;
import br.ufes.inf.nemo.okco.model.IFactory;
import br.ufes.inf.nemo.okco.model.IRepository;
import br.ufes.inf.nemo.okco.model.OKCoExceptionInstanceFormat;
//import br.ufes.inf.padtec.tnokco.business.Code;

@Controller
public class CondelController implements ServletContextAware{

	public static IFactory Factory;
	public static IRepository Repository;

	//Condel text value
	public static String txtCondelCode;	
	
	//servelet context
	private ServletContext servletContext;
	
//	private ArrayList<Code> ListCodes = new ArrayList<Code>();
	private int totalCodes = 6;
	private int totalCreated = 0;
	
	@RequestMapping(method = RequestMethod.GET, value="/condel")
	public String condel(HttpSession session, HttpServletRequest request) {

		//load g800

		String path = servletContext.getInitParameter("PathG800owl"); 

		// Load Model

		Factory = new FactoryModel();
		Repository = Factory.GetRepository();
		HomeController.Reasoner = Factory.GetReasoner(EnumReasoner.HERMIT);		
		
		// Load Model
		HomeController.Model = Repository.Open(path);

		// Name space
		HomeController.NS = Repository.getNameSpace(HomeController.Model);

		HomeController.Search = new Search(HomeController.NS);
		HomeController.FactoryInstances = new FactoryInstances(HomeController.Search);
		HomeController.ManagerInstances = new ManagerInstances(HomeController.Search, HomeController.FactoryInstances, HomeController.Model);

		//Save temporary model
		HomeController.tmpModel = Repository.CopyModel(HomeController.Model);		

		//List modified instances
		HomeController.ListModifiedInstances = new ArrayList<String>();		

		//Get parameter with tells the condel load from file or not

		if(txtCondelCode == "")
		{
			return "condel";	//View to return

		} else {

			//Create the sections				
			request.getSession().setAttribute("txtCondelCode", txtCondelCode);
//			request.getSession().setAttribute("ListCodes", ListCodes);
			
			return "condel";	//View to return
		}     	
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getCondel")
	public String getCondel(HttpSession session, HttpServletRequest request) throws IOException {

		if(txtCondelCode != "")
		{
			request.getSession().setAttribute("txtCondelCode", txtCondelCode);
			request.getSession().removeAttribute("loadOk");

			return "getCondel";

		} else {

			return "condel";
		}
	}

	/*-------------- -------------- ------------*/
	/*-------------- AJAX FUNCTIONS ------------*/
	/*-------------- -------------- ------------*/
	
	@RequestMapping(method = RequestMethod.POST, value="/getCondelCode")
	public @ResponseBody DtoResultAjax getCondelCode(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {
		
		String condelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();		
		txtCondelCode = condelCode;
		
		if(condelCode == "")
		{
			dto.ok = false;
			dto.result = "No Condel Code to save";
		} else {

			dto.ok = true;
			dto.result = "";
		}

		return dto;
	}

	@RequestMapping(method = RequestMethod.POST, value="/runCondel")
	public @ResponseBody DtoResultAjax runCondel(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {

		String condelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();

		if(HomeController.Model == null)
		{
			dto.ok = false;
			dto.result = "Error! You need to load the model first.";
			return dto;
		}

		try {		  	      
			// Populate the model
			//TODO FABIO!!!

//			HomeController.Model = dtoCondel.model;
//
////			HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);
//			
//			HomeController.InfModel = dtoCondel.model;
			
			// Update list instances
			try {
				HomeController.UpdateLists();
			} catch (OKCoExceptionInstanceFormat e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

		} /*REMOVER DEPOIS!!!
			catch (OKCoExceptionInstanceFormat e) {

			String error = "Entity format error: " + e.toString();
			System.out.println(error);
			request.getSession().setAttribute("errorMensage", error);
			HomeController.Model = null;
			HomeController.InfModel = null;
			HomeController.ListAllInstances = null;

			dto.ok = false;
			dto.result = error;				
			return dto;				
		}*/

		request.getSession().removeAttribute("errorMensage");      

		dto.ok = true;
		dto.result = "ok";

		return dto;
	}

	@RequestMapping(method = RequestMethod.GET, value="/cleanCondelCode")
	public @ResponseBody DtoResultAjax cleanCondelCode(HttpServletRequest request) {

		txtCondelCode = "";
		request.getSession().setAttribute("txtCondelCode", txtCondelCode);

		DtoResultAjax dto = new DtoResultAjax();
		dto.ok = true;
		dto.result = "ok";

		return dto;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		
	}

}
