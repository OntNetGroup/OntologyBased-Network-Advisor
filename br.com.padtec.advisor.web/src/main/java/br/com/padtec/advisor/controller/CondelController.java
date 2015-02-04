package br.com.padtec.advisor.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.com.padtec.advisor.application.CondelUploader;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.ufes.inf.padtec.tnokco.business.DtoResultAjax;
import br.ufes.inf.padtec.tnokco.business.Reader;

@Controller
public class CondelController{

	@RequestMapping(value = "/uploadCondel", method = RequestMethod.POST)
	public String uploadCondel(HttpServletRequest request)
	{
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			InputStream in = file.getInputStream();
			InputStreamReader r = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(r);
			Reader readerFile = new Reader();
			String txtCondel = readerFile.readFile(br);
			
			CondelUploader.uploadCondelModel(txtCondel);
			
		}catch (IOException e){
			System.out.println("File not found.");
			return "index";
		}
		return "redirect:condel";
	}	
	
	@RequestMapping(method = RequestMethod.GET, value="/condel")
	public String condel(HttpSession session, HttpServletRequest request) 
	{
		if(CondelUploader.condelCode.isEmpty())
		{
			if(OKCoUploader.reasoner==null)
			{
				String error = "Reasoner error: Select some reasoner";
				request.getSession().setAttribute("errorMensage", error);				
				return "index";				
			}else{
				request.getSession().removeAttribute("errorMensage");
			}			
			return "condel";
		}else{			
			if(OKCoUploader.reasoner==null)
			{
				String error = "Reasoner error: Select some reasoner";
				request.getSession().setAttribute("errorMensage", error);				
				return "index";				
			}else{
				request.getSession().removeAttribute("errorMensage");
			}				
			
			/** =================================================
			 *  Returns the Condel code
			 *  ================================================= */
			request.getSession().setAttribute("txtCondelCode", CondelUploader.condelCode);			
			return "condel";
		}     	
	}
		
	@RequestMapping(method = RequestMethod.POST, value="/getCondelCode")
	public @ResponseBody DtoResultAjax getCondelCode(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) 
	{		
		String condelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();	
		
		/** =====================================================
		 *  Set a new Condel code...
		 *  ===================================================== */
		CondelUploader.condelCode = condelCode;
		
		if(condelCode == "") {
			dto.ok = false;
			dto.result = "No Condel Code to save";			
		}else{
			dto.ok = true;
			dto.result = "";
		}
		return dto;
	}


	@RequestMapping(method = RequestMethod.GET, value="/cleanCondelCode")
	public @ResponseBody DtoResultAjax cleanCondelCode(HttpServletRequest request) 
	{
		/** =====================================================
		 *  Empty Condel code...
		 *  ===================================================== */
		CondelUploader.condelCode = "";
		
		request.getSession().setAttribute("txtCondelCode", CondelUploader.condelCode);
		DtoResultAjax dto = new DtoResultAjax();
		dto.ok = true;
		dto.result = "ok";
		return dto;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getCondel")
	public String getCondel(HttpSession session, HttpServletRequest request) throws IOException 
	{
		/** ====================================================
		 *  Transforms the OWL Base Model into Condel
		 *  ==================================================== */
		List<String> result = CondelUploader.transformLoadedOwltoCondel();
		
		if(result != null)
		{
			request.getSession().setAttribute("instructions", result);
			request.getSession().removeAttribute("loadOk");
			return "getCondel";
		}else{
			return "condel";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/runCondel")
	public @ResponseBody DtoResultAjax runCondel(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) 
	{		
		DtoResultAjax dto = new DtoResultAjax();
		if(OKCoUploader.getBaseModel()==null)
		{
			dto.ok = false;
			dto.result = "Error! You need to load the model first.";
			return dto;
		}
	
		/** ====================================================
		 *  Transforms the Condel into the OWL Base Model
		 *  ==================================================== */
		CondelUploader.condelCode = dtoGet.result;
		CondelUploader.transformCondeltoLoadedOwl();			
	
		request.getSession().removeAttribute("errorMensage");
		dto.ok = true;
		dto.result = "ok";
		return dto;
	}

}
