package br.com.padtec.advisor.controller.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.OKCoSelector;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.okco.core.exception.OKCoExceptionFileFormat;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.core.exception.OKCoExceptionNameSpace;
import br.com.padtec.okco.core.exception.OKCoExceptionReasoner;

/**
 * Controller responsible for the upload/store of the base ontology, inferred ontology and temporary ontology.
 * It also records what reasoner is used.
 * See this class: {@link OKCoUploader}  
 */

public class UploadController {
	
	@RequestMapping(method = RequestMethod.GET, value="/")
	public String index(HttpSession session, HttpServletRequest request) 
	{	     
		request.getSession().removeAttribute("errorMensage");
		request.getSession().removeAttribute("loadOk");			
		return "redirect:welcome";			
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/welcome")
	public String welcome(HttpSession session, HttpServletRequest request) 
	{	
		String login = (String)request.getSession().getAttribute("login");			
		login = "true";							
		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");			
			return "index";
		}else{				
			return "login";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/faq")
	public String faq(HttpSession session, HttpServletRequest request) 
	{
		String login = (String)request.getSession().getAttribute("login");
		login = "true";
		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");			
			return "faq";
		}else{				
			return "faq";
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password)
	{		
		if(username.equals("okco") && password.equals("1234"))
		{
			request.getSession().setAttribute("login", "true");
			return "redirect:welcome";			
		}else{			
			request.getSession().setAttribute("login", "false");
			return "login";
		}
	}
	
	public static void clearOKCoUploader()
	{
		OKCoUploader.clear(); 
		OKCoSelector.clearSelected();	
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(HttpServletRequest request, @RequestParam("optionsReasoner") String optReasoner)
	{		
		System.out.println("Executing /upload...");
		Date beginDate = new Date();
		try {
			 String useReasoner = request.getParameter("loadReasonerFirstCheckbox");
			 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			 MultipartFile file = multipartRequest.getFile("file");		  
			 if(!file.getOriginalFilename().endsWith(".owl")) throw new OKCoExceptionFileFormat("Please select owl file.");
			 InputStream in = file.getInputStream();			  
			 
			 /** ==================================================
			  *  Performs the upload 
			  *  =================================================== */
			 OKCoUploader.uploadBaseModel(in, useReasoner, optReasoner);
			  
		}catch (InconsistentOntologyException e){
			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			request.getSession().setAttribute("errorMensage", error);			
			OKCoUploader.rollBack(false);			
			return "index";			
		}catch (OKCoExceptionInstanceFormat e){			
			String error = "Entity format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			clearOKCoUploader();		
			return "index";			
		}catch (OKCoExceptionFileFormat e){			
			String error = "File format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			clearOKCoUploader();			
			return "index";			
		}catch (IOException e){
			String error = "File not found.";
			request.getSession().setAttribute("errorMensage", error);
			clearOKCoUploader();			
			return "index";			
		}catch (OKCoExceptionNameSpace e){			
			String error = "File namespace error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			clearOKCoUploader();			
			return "index";			
		}catch (OKCoExceptionReasoner e){
			String error = "Reasoner error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			clearOKCoUploader();			
			return "index";
		}		 
		request.getSession().removeAttribute("errorMensage");  
		
		Date endDate = new Date();
		long diff = endDate.getTime() - beginDate.getTime();
		long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);         
		long diffHours = diff / (60 * 60 * 1000); 
		System.out.println("Execution time: " + diffHours + "h " + diffMinutes + "m " + diffSeconds + "s");
		return "redirect:list";
	}
		
	@RequestMapping(method = RequestMethod.GET, value="/getModel")
	public String getModel(HttpSession session, HttpServletRequest request) throws IOException 
	{	     
		/** ==================================================
		*  Get the base model which was uploaded as a string text 
		*  =================================================== */
		if(OKCoUploader.isBaseModelUploaded())
		{
			request.getSession().removeAttribute("loadOk");
			request.getSession().setAttribute("model", OKCoUploader.getBaseModelAsString());
			return "model";
		}else{				
			request.getSession().removeAttribute("model");
			request.getSession().setAttribute("loadOk", "false");
		    return "index";
		}
	}

	/* AJAX */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public DtoResult save(HttpServletRequest request)
	{
		DtoResult dto = new DtoResult();
		
		/** ==================================================
		*  Saves the base model which was uploaded
		*  =================================================== */
		dto.setIsSucceed(OKCoUploader.saveBaseModel());
		
		return dto;
	}	
}
