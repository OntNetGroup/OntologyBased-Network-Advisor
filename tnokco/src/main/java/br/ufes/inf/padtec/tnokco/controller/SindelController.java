package br.ufes.inf.padtec.tnokco.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.ufes.inf.nemo.okco.business.FactoryInstances;
import br.ufes.inf.nemo.okco.business.ManagerInstances;
import br.ufes.inf.nemo.okco.business.Search;
import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.nemo.okco.model.IFactory;
import br.ufes.inf.nemo.okco.model.IRepository;


import br.ufes.inf.nemo.okco.model.OKCoExceptionInstanceFormat;
import br.ufes.inf.nemo.padtec.Sindel2OWL;
import br.ufes.inf.nemo.padtec.DtoSindel.DtoResultSindel;

import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;

@Controller
public class SindelController {

	public static IFactory Factory;
	public static IRepository Repository;

	//Sindel text value
	public static String txtSindelCode;	

	@RequestMapping(method = RequestMethod.GET, value="/sindel")
	public String sindel(HttpSession session, HttpServletRequest request) {

		//Get parameter with tells the sindel load from file or not

		if(txtSindelCode == "")
		{
			return "sindel";	//View to return

		} else {

			//Create the sections				
			request.getSession().setAttribute("txtSindelCode", txtSindelCode);
			return "sindel";	//View to return
		}     	
	}

	/*-------------- -------------- ------------*/
	/*-------------- AJAX FUNCTIONS ------------*/
	/*-------------- -------------- ------------*/

	@RequestMapping(method = RequestMethod.POST, value="/runSindel")
	public @ResponseBody DtoResultAjax runSindel(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {

		String sindelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();

		if(HomeController.Model == null)
		{
			dto.ok = false;
			dto.result = "Error! You need to load the model first.";
			return dto;
		}

		try {		  	      
			// Populate the model
			Sindel2OWL so = new Sindel2OWL(HomeController.Model);
			so.run(sindelCode);
			DtoResultSindel dtoSindel = so.getDtoSindel();
			
			HomeController.Model = dtoSindel.model;

			/* Go to OKCo */
			HomeController.Search = new Search(HomeController.NS);
			HomeController.FactoryInstances = new FactoryInstances(HomeController.Search);
			HomeController.ManagerInstances = new ManagerInstances(HomeController.Search, HomeController.FactoryInstances, HomeController.Model);

			// Reasoning
			Reasoner r = PelletReasonerFactory.theInstance().create();
			HomeController.InfModel = ModelFactory.createInfModel(r, HomeController.Model);

			// Gets relations on model
			HomeController.dtoSomeRelationsList = HomeController.Search.GetSomeRelations(HomeController.InfModel);
			HomeController.dtoMinRelationsList = HomeController.Search.GetMinRelations(HomeController.InfModel);
			HomeController.dtoMaxRelationsList = HomeController.Search.GetMaxRelations(HomeController.InfModel);
			HomeController.dtoExactlyRelationsList = HomeController.Search.GetExactlyRelations(HomeController.InfModel);

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

		return dto;
	}

	@RequestMapping(method = RequestMethod.GET, value="/cleanSindelCode")
	public @ResponseBody DtoResultAjax cleanSindelCode(HttpServletRequest request) {

		txtSindelCode = "";
		request.getSession().setAttribute("txtSindelCode", txtSindelCode);

		DtoResultAjax dto = new DtoResultAjax();
		dto.ok = true;
		dto.result = "ok";

		return dto;
	}

}
