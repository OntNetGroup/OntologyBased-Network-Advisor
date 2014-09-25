package br.ufes.inf.padtec.tnokco.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;





import br.ufes.inf.nemo.condelOwlg805.Condel2owlG805;
import br.ufes.inf.nemo.condelOwlg805.OwlG805toCondel;
import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.nemo.okco.model.EnumReasoner;
import br.ufes.inf.nemo.okco.model.OKCoExceptionInstanceFormat;

@Controller
public class CondelController{

	//Condel text value
	public static String txtCondelCode;	

	
	@RequestMapping(method = RequestMethod.GET, value="/condel")
	public String condel(HttpSession session, HttpServletRequest request) {

		//Get parameter with tells the Condel load from file or not

		if(txtCondelCode == "")
		{
			if(HomeController.Reasoner == null)
			{
				String error = "Reasoner error: Select some reasoner";
				request.getSession().setAttribute("errorMensage", error);
				
				return "index";
				
			} else {
				request.getSession().removeAttribute("errorMensage");
			}
			
			return "condel";	//View to return

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
			request.getSession().setAttribute("txtCondelCode", txtCondelCode);
			
			return "condel";	//View to return
		}     	
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getCondel")
	public String getCondel(HttpSession session, HttpServletRequest request) throws IOException {

		ArrayList<String> instructions = OwlG805toCondel.transformToCondel(HomeController.Model);
		
		if(instructions != null)
		{
			request.getSession().setAttribute("instructions", instructions);
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

			String separator = "%-%-%";			
			
			HomeController.Model = Condel2owlG805.transformToOWL(HomeController.Model, condelCode);

			//Run reasoner
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

}
