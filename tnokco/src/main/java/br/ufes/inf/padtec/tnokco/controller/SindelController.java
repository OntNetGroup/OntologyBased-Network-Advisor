package br.ufes.inf.padtec.tnokco.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.nemo.okco.model.OKCoExceptionInstanceFormat;
import br.ufes.inf.nemo.okco.model.util.OntModelUtil;
import br.ufes.inf.nemo.padtec.Sindel2OWL;
import br.ufes.inf.nemo.padtec.DtoSindel.DtoResultSindel;
//import br.ufes.inf.padtec.tnokco.business.Code;



import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Statement;

@Controller
public class SindelController{

	//Sindel text value
	public static String txtSindelCode;	
	
	@RequestMapping(method = RequestMethod.GET, value="/sindel")
	public String sindel(HttpSession session, HttpServletRequest request) {

		//Create the sections				
		request.getSession().setAttribute("txtSindelCode", txtSindelCode);
		
		txtSindelCode = "";			
		
		return "sindel";	//View to return
				
		     	
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
			
			
			/**
			 * Set specific types for connects relations
			 * */
			
			List<String[]> list = OntModelUtil.getDomainAndRangeURI(HomeController.Model, HomeController.NS+"has_forwarding");
			String specificRelation = null;
			
			for(String[] st : list){
				specificRelation = null;
				
				ArrayList<String> st0Types = HomeController.Search.GetClassesFrom(st[0], HomeController.Model);
				ArrayList<String> st1Types = HomeController.Search.GetClassesFrom(st[1], HomeController.Model);
				
				if(st0Types.contains(HomeController.NS+"Source_AP") && (st1Types.contains(HomeController.NS+"Sink_AP"))){
					specificRelation = "Forwarding_Unidirectional_Access_Transport_Entity";
				}else if(st0Types.contains(HomeController.NS+"Source_A-FEP") && (st1Types.contains(HomeController.NS+"Sink_A-FEP"))){
					specificRelation = "Forwarding_Path_NC";
				}else if(st0Types.contains(HomeController.NS+"Source_PM-FEP") && (st1Types.contains(HomeController.NS+"Sink_PM-FEP"))){
					specificRelation = "Forwarding_Path_NC";
				}
				
				if(specificRelation != null){
					ObjectProperty rel = HomeController.Model.getObjectProperty(HomeController.NS+specificRelation);
					Statement stmt = HomeController.Model.createStatement(HomeController.Model.getIndividual(st[0]), rel, HomeController.Model.getIndividual(st[1]));
					HomeController.Model.add(stmt);
				}
			}
			
			if(HomeController.reasoningOnFirstLoad == true)
			{
				//Call reasoner
				HomeController.InfModel = HomeController.Reasoner.run(HomeController.Model);
				
			} else {
				
				//Don't call reasoner
				HomeController.InfModel = HomeController.Repository.clone(HomeController.Model);
			}
			
			//tmp model
			HomeController.tmpModel = HomeController.Repository.clone(HomeController.Model);			
			
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
		
		//clean sindel code
		txtSindelCode = "";		

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
