package br.ufes.inf.padtec.tnokco.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.nemo.okco.model.EnumReasoner;
import br.ufes.inf.nemo.okco.model.OKCoExceptionInstanceFormat;
import br.ufes.inf.nemo.padtec.Sindel2OWL;
import br.ufes.inf.nemo.padtec.DtoSindel.DtoResultSindel;
//import br.ufes.inf.padtec.tnokco.business.Code;

@Controller
public class SindelController{

	//Sindel text value
	public static String txtSindelCode;	
	
	@RequestMapping(method = RequestMethod.GET, value="/sindel")
	public String sindel(HttpSession session, HttpServletRequest request) {

		//Get parameter with tells the sindel load from file or not

		if(txtSindelCode == "")
		{
			if(HomeController.Reasoner == null)
			{
				String error = "Reasoner error: Select some reasoner";
				request.getSession().setAttribute("errorMensage", error);
				
				return "index";
				
			} else {
				request.getSession().removeAttribute("errorMensage");
			}
			
			return "sindel";	//View to return

		} else {
			
			if(HomeController.Reasoner == null)
			{
				String error = "Reasoner error: Select some reasoner";
				request.getSession().setAttribute("errorMensage", error);
				
				return "index";
				
			} else {
				request.getSession().removeAttribute("errorMensage");
			}

			//Create the sections				
			request.getSession().setAttribute("txtSindelCode", txtSindelCode);
			
			return "sindel";	//View to return
		}     	
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getSindel")
	public String getSindel(HttpSession session, HttpServletRequest request) throws IOException {

		if(txtSindelCode != "")
		{
			request.getSession().setAttribute("txtSindelCode", txtSindelCode);
			request.getSession().removeAttribute("loadOk");

			return "getSindel";

		} else {

			return "sindel";
		}
	}

	/*-------------- -------------- ------------*/
	/*-------------- AJAX FUNCTIONS ------------*/
	/*-------------- -------------- ------------*/
	
	@RequestMapping(method = RequestMethod.POST, value="/getSindelCode")
	public @ResponseBody DtoResultAjax getSindelCode(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {
		
		String sindelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();		
		txtSindelCode = sindelCode;
		
		if(sindelCode == "")
		{
			dto.ok = false;
			dto.result = "No Sindel Code to save";
		} else {

			dto.ok = true;
			dto.result = "";
		}

		return dto;
	}

	@RequestMapping(method = RequestMethod.POST, value="/runSindel")
	public @ResponseBody DtoResultAjax runSindel(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) {

		String sindelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();
		DtoResultSindel dtoSindel;
		
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
			dtoSindel = so.getDtoSindel();			

			HomeController.Model = dtoSindel.model;
			
			if(HomeController.reasoningOnFirstLoad == true)
			{
				//Call reasoner
				HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);
				
			} else {
				
				//Don't call reasoner
				HomeController.InfModel = HomeController.Repository.CopyModel(HomeController.Model);
			}
			
			//tmp model
			HomeController.tmpModel = HomeController.Repository.CopyModel(HomeController.Model);			
			
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

		//FAZER O CATCH DA SINDEL

		request.getSession().removeAttribute("errorMensage");      

		dto.ok = true;
		dto.result = dtoSindel.warning;

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
