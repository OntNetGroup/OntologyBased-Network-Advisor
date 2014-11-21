package br.com.padtec.okco.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.exceptions.OKCoExceptionFileFormat;
import br.com.padtec.common.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.common.exceptions.OKCoExceptionNameSpace;
import br.com.padtec.common.exceptions.OKCoExceptionReasoner;
import br.com.padtec.common.util.CompleterApp;
import br.com.padtec.common.util.UploadApp;

@Controller
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
	
	public void clearAllApps()
	{
		UploadApp.clear(); 
		CompleterApp.clear();	
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(HttpServletRequest request, @RequestParam("optionsReasoner") String optReasoner)
	{		
		try {
			 String useReasoner = request.getParameter("loadReasonerFirstCheckbox");
			 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			 MultipartFile file = multipartRequest.getFile("file");		  
			 if(!file.getOriginalFilename().endsWith(".owl")) throw new OKCoExceptionFileFormat("Please select owl file.");
			 InputStream in = file.getInputStream();			  
			 
			 /** ==================================================
			  *  Performs the upload 
			  *  =================================================== */
			 UploadApp.uploadBaseModel(in, useReasoner, optReasoner);
			  
		}catch (InconsistentOntologyException e){
			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			request.getSession().setAttribute("errorMensage", error);			
			UploadApp.rollBack();			
			return "index";			
		}catch (OKCoExceptionInstanceFormat e){			
			String error = "Entity format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			clearAllApps();		
			return "index";			
		}catch (OKCoExceptionFileFormat e){			
			String error = "File format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			clearAllApps();			
			return "index";			
		}catch (IOException e){
			String error = "File not found.";
			request.getSession().setAttribute("errorMensage", error);
			clearAllApps();			
			return "index";			
		}catch (OKCoExceptionNameSpace e){			
			String error = "File namespace error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			clearAllApps();			
			return "index";			
		}catch (OKCoExceptionReasoner e){
			String error = "Reasoner error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			clearAllApps();			
			return "index";
		}		 
		request.getSession().removeAttribute("errorMensage");  
		return "redirect:list";
	}
		
	@RequestMapping(method = RequestMethod.GET, value="/getModel")
	public String getModel(HttpSession session, HttpServletRequest request) throws IOException 
	{	     
		/** ==================================================
		*  Get the base model which was uploaded as a string text 
		*  =================================================== */
		if(UploadApp.isBaseModelUploaded())
		{
			request.getSession().removeAttribute("loadOk");
			request.getSession().setAttribute("model", UploadApp.getBaseModelAsString());
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
		dto.setIsSucceed(UploadApp.saveBaseModel());
		
		return dto;
	}	
}
