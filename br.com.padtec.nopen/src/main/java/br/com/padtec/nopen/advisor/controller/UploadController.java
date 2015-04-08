package br.com.padtec.nopen.advisor.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.reasoning.HermitReasonerImpl;
import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.okco.core.application.OKCoComponents;
import br.com.padtec.okco.core.exception.OKCoExceptionFileFormat;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.core.exception.OKCoExceptionNameSpace;
import br.com.padtec.okco.core.exception.OKCoExceptionReasoner;

@Controller
public class UploadController implements ServletContextAware{
		
	private ServletContext servletContext;
		
	@Override
	public void setServletContext(ServletContext servletContext) 
	{
		this.servletContext = servletContext;		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/")
	public String index(HttpSession session, HttpServletRequest request) 
	{
		request.getSession().removeAttribute("errorMensage");
		request.getSession().removeAttribute("loadOk");
		return "login";	
	}

	@RequestMapping(method = RequestMethod.GET, value="/faq")
	public String faq(HttpSession session, HttpServletRequest request) 
	{
		String login = (String)request.getSession().getAttribute("login");
		if(login == null) login = "";
		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");
			return "faq";
		}else{
			return "login";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/about")
	public String about(HttpSession session, HttpServletRequest request) 
	{
		String login = (String)request.getSession().getAttribute("login");
		if(login == null) login = "";
		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");
			return "about";
		}else{
			return "login";
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password)
	{
		if(username.equals("advisor") && password.equals("1234"))
		{
			request.getSession().setAttribute("login", "true");
			return "redirect:welcome";
		}else{
			request.getSession().setAttribute("login", "false");
			return "login";
		}
	}
		
	@RequestMapping(method = RequestMethod.GET, value="/welcome")
	public String welcome(HttpSession session, HttpServletRequest request) 
	{	
		String login = (String)request.getSession().getAttribute("login");
		if(login == null) login = "";
		if(login.equals("true"))
		{
			System.out.println("Executing /welcome and loading G800...");
			Date beginDate = new Date();
			
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");
			String path = servletContext.getInitParameter("PathG800owl");	
			
			/** Load G800 */
			String error = uploadOwlG800(path);
			
			if(!error.isEmpty())request.getSession().setAttribute("errorMensage", error);
			
			PerformanceUtil.printExecutionTime("/welcome", beginDate);
			
			return "index";			
		}else{
			return "login";	
		}
	}
	
	/**
	 * Load G800 Specifiction in OWL using the OKCo funciontality
	 * 
	 * @param g800Path
	 * 
	 * @return
	 * @author John Guerson
	 */
	public String uploadOwlG800(String g800Path)
	{
		
		String resultMessage = new String();
		try{

			OKCoComponents.repository.uploadBaseModel(g800Path,OKCoComponents.repository.isReasonOnLoading() ? "on" : "off","hermit");
			
		}catch (InconsistentOntologyException e){
			resultMessage = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";						
			OKCoComponents.repository.rollBack(false);			
		}catch (OKCoExceptionInstanceFormat e){
			resultMessage = "Entity format error: " + e.getMessage();			
			OKCoComponents.repository.clear();			
		}catch (OKCoExceptionNameSpace e){			
			resultMessage = "File namespace error: " + e.getMessage();			
			OKCoComponents.repository.clear();				
		}catch (OKCoExceptionReasoner e){
			resultMessage = "Reasoner error: " + e.getMessage();			
			OKCoComponents.repository.clear();				
		}catch (IOException e){
			resultMessage = "File not found."+e.getMessage();			
			OKCoComponents.repository.clear();	
		}catch (Exception e){
			resultMessage = e.getLocalizedMessage();
			OKCoComponents.repository.clear();				
		}			
		
		return resultMessage;
	}
	
	/**
	 * Upload OWL file using the OKCo funciontality
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadOwl", method = RequestMethod.POST) 
	public String uploadOwl(HttpServletRequest request)
	{
		System.out.println("Executing /uploadOwl...");
		Date beginDate = new Date();
		
		try{
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			if(!file.getOriginalFilename().endsWith(".owl")) throw new OKCoExceptionFileFormat("Please select owl file.");
			InputStream in = file.getInputStream();			
			
			OKCoComponents.repository.uploadBaseModel(in,OKCoComponents.repository.isReasonOnLoading() ? "on" : "off", (OKCoComponents.repository.getReasoner() instanceof HermitReasonerImpl) ? "hermit" : "pellet");	
			
		}catch (InconsistentOntologyException e){
			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			request.getSession().setAttribute("errorMensage", error);			
			OKCoComponents.repository.rollBack(false);			
			return "index";			
		}catch (OKCoExceptionInstanceFormat e){			
			String error = "Entity format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoComponents.repository.clear();
			return "index";			
		}catch (OKCoExceptionFileFormat e){			
			String error = "File format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoComponents.repository.clear();		
			return "index";			
		}catch (IOException e){
			String error = "File not found.";
			request.getSession().setAttribute("errorMensage", error);
			OKCoComponents.repository.clear();	
			return "index";			
		}catch (OKCoExceptionNameSpace e){			
			String error = "File namespace error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoComponents.repository.clear();
			return "index";			
		}catch (OKCoExceptionReasoner e){
			String error = "Reasoner error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoComponents.repository.clear();
			return "index";
		} catch (Exception e){
			String error = e.getLocalizedMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoComponents.repository.clear();
			return "index";
		}	 
		
		PerformanceUtil.printExecutionTime("/uploadOwl", beginDate);
		
		request.getSession().removeAttribute("errorMensage");  
		return "redirect:okco-list";
	}	
	
	@RequestMapping(method = RequestMethod.GET, value="/getModel")
	public String getModel(HttpSession session, HttpServletRequest request) throws IOException 
	{	     
		/** ==================================================
		*  Get the base model which was uploaded as a string text 
		*  =================================================== */
		if(OKCoComponents.repository.isBaseModelUploaded())
		{
			request.getSession().removeAttribute("loadOk");
			request.getSession().setAttribute("model", OKCoComponents.repository.getBaseModelAsString());
			return "okco-model";
		}else{				
			request.getSession().removeAttribute("model");
			request.getSession().setAttribute("loadOk", "false");
		    return "okco-index";
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
		dto.setIsSucceed(OKCoComponents.repository.saveBaseModel());
		
		return dto;
	}	
}
