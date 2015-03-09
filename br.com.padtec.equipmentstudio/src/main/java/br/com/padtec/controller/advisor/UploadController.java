package br.com.padtec.controller.advisor;


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

import br.com.padtec.advisor.core.util.PerformanceUtil;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.reasoning.HermitReasonerImpl;
import br.com.padtec.okco.core.application.OKCoSelector;
import br.com.padtec.okco.core.application.OKCoUploader;
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
		return "advisor/views/login";	
	}

	@RequestMapping(method = RequestMethod.GET, value="/faq")
	public String faq(HttpSession session, HttpServletRequest request) 
	{
		String login = (String)request.getSession().getAttribute("login");
		if(login == null) login = "true";
		if(login.equals("true"))
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");
			return "advisor/views/faq";
		}else{
			return "advisor/views/login";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/about")
	public String about(HttpSession session, HttpServletRequest request) 
	{
		String login = (String)request.getSession().getAttribute("login");	
		if(login==null) login = "true";
		if(login.equals("true"))		
		{
			request.getSession().removeAttribute("errorMensage");
			request.getSession().removeAttribute("loadOk");
			return "advisor/views/about";
		}else{
			return "advisor/views/login";
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password)
	{
		if(username.equals("advisor") && password.equals("1234"))
		{
			request.getSession().setAttribute("login", "true");
			return "redirect:advisor/views/welcome";
		}else{
			request.getSession().setAttribute("login", "false");
			return "advisor/views/login";
		}
	}
		
	@RequestMapping(method = RequestMethod.GET, value="/welcome")
	public String welcome(HttpSession session, HttpServletRequest request) 
	{	
		String login = (String)request.getSession().getAttribute("login");
		if(login == null) login = "true";
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
			
			return "advisor/index";			
		}else{
			return "advisor/views/login";	
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

			OKCoUploader.uploadBaseModel(g800Path,OKCoUploader.reasonOnLoading ? "on" : "off","hermit");
			
		}catch (InconsistentOntologyException e){
			resultMessage = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";						
			OKCoUploader.rollBack(false);			
		}catch (OKCoExceptionInstanceFormat e){
			resultMessage = "Entity format error: " + e.getMessage();			
			OKCoUploader.clear();			
		}catch (OKCoExceptionNameSpace e){			
			resultMessage = "File namespace error: " + e.getMessage();			
			OKCoUploader.clear();				
		}catch (OKCoExceptionReasoner e){
			resultMessage = "Reasoner error: " + e.getMessage();			
			OKCoUploader.clear();				
		}catch (IOException e){
			resultMessage = "File not found."+e.getMessage();			
			OKCoUploader.clear();	
		}catch (Exception e){
			resultMessage = e.getLocalizedMessage();
			OKCoUploader.clear();				
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
			
			OKCoUploader.uploadBaseModel(in,OKCoUploader.reasonOnLoading ? "on" : "off", (OKCoUploader.reasoner instanceof HermitReasonerImpl) ? "hermit" : "pellet");	
			
		}catch (InconsistentOntologyException e){
			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			request.getSession().setAttribute("errorMensage", error);			
			OKCoUploader.rollBack(false);			
			return "advisor/index";			
		}catch (OKCoExceptionInstanceFormat e){			
			String error = "Entity format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoUploader.clear();
			return "advisor/index";			
		}catch (OKCoExceptionFileFormat e){			
			String error = "File format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoUploader.clear();		
			return "advisor/index";			
		}catch (IOException e){
			String error = "File not found.";
			request.getSession().setAttribute("errorMensage", error);
			OKCoUploader.clear();	
			return "advisor/index";			
		}catch (OKCoExceptionNameSpace e){			
			String error = "File namespace error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoUploader.clear();
			return "advisor/index";			
		}catch (OKCoExceptionReasoner e){
			String error = "Reasoner error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoUploader.clear();
			return "advisor/index";
		} catch (Exception e){
			String error = e.getLocalizedMessage();
			request.getSession().setAttribute("errorMensage", error);
			OKCoUploader.clear();
			return "advisor/index";
		}	 
		
		PerformanceUtil.printExecutionTime("/uploadOwl", beginDate);
		
		request.getSession().removeAttribute("errorMensage");  
		return "redirect:advisor/views/okco-list";
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
			return "advisor/views/okco-model";
		}else{				
			request.getSession().removeAttribute("model");
			request.getSession().setAttribute("loadOk", "false");
		    return "advisor/views/okco-index";
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
	
	public static void clearOKCoUploader()
	{
		OKCoUploader.clear(); 
		OKCoSelector.clearSelected();	
	}
}
