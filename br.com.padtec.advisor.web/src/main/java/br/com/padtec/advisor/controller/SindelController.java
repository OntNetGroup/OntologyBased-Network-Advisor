package br.com.padtec.advisor.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.com.padtec.advisor.application.SindelUploader;
import br.com.padtec.advisor.application.dto.DtoResultAjax;
import br.com.padtec.advisor.application.util.FileReader;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.transformation.sindel.dto.DtoResultSindel;

@Controller
public class SindelController{
		
	@RequestMapping(value = "/uploadSindel", method = RequestMethod.POST)
	public String uploadSindel(HttpServletRequest request)
	{
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			InputStream in = file.getInputStream();
			InputStreamReader r = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(r);
			FileReader readerFile = new FileReader();
			String txtSindel = readerFile.readFile(br);
			
			/** ==========================================
			 *  Upload Sindel Model
			 *  ========================================== */
			SindelUploader.uploadSindelModel(txtSindel);			
			
		} catch (IOException e){
			System.out.println("File not found: "+e.getLocalizedMessage());
			return "index";
		}
		return "redirect:sindel";
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value="/sindel")
	public String sindel(HttpSession session, HttpServletRequest request) 
	{					
		request.getSession().setAttribute("txtSindelCode", SindelUploader.sindelCode);
		
		/** ==========================================
		 *  Empty Sindel code
		 *  ========================================== */
		SindelUploader.sindelCode = "";
		
		return "sindel";	
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getSindel")
	public String getSindel(HttpSession session, HttpServletRequest request) throws IOException {

		if(SindelUploader.sindelCode!="")
		{
			request.getSession().setAttribute("txtSindelCode", SindelUploader.sindelCode);
			request.getSession().removeAttribute("loadOk");
			return "getSindel";
		}else{
			return "sindel";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/getSindelCode")
	public @ResponseBody DtoResultAjax getSindelCode(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) 
	{		
		String sindelCode = dtoGet.result;
		DtoResultAjax dto = new DtoResultAjax();
		
		/** ==========================================
		 *  Save Sindel code
		 *  ========================================== */
		SindelUploader.sindelCode = sindelCode;
		
		if(sindelCode == ""){
			dto.ok = false;
			dto.result = "No Sindel code to save";
		}else{
			dto.ok = true;
			dto.result = "";
		}
		return dto;
	}

	@RequestMapping(method = RequestMethod.GET, value="/cleanSindelCode")
	public @ResponseBody DtoResultAjax cleanSindelCode(HttpServletRequest request) 
	{
		/** ==========================================
		 *  Empty Sindel code
		 *  ========================================== */
		SindelUploader.sindelCode = "";
		request.getSession().setAttribute("txtSindelCode", SindelUploader.sindelCode);

		DtoResultAjax dto = new DtoResultAjax();
		dto.ok = true;
		dto.result = "ok";
		return dto;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/runSindel")
	public @ResponseBody DtoResultAjax runSindel(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) 
	{
		DtoResultAjax dtoResult = new DtoResultAjax();
				
		if(OKCoUploader.getBaseModel()==null)
		{
			dtoResult.ok = false;
			dtoResult.result = "Error! You need to load the model first.";
			return dtoResult;
		}
			
		/** ==========================================
		 *  Transforms the Sindel into the OWL Base Model
		 *  ========================================== */
		SindelUploader.sindelCode = dtoGet.result;
		DtoResult result = SindelUploader.transformSindelToLoadedOwl();

		if(!result.isSucceed()) 
		{			 
			dtoResult.ok = false;
			dtoResult.result = result.getMessage();
			request.getSession().setAttribute("errorMensage", result.getMessage());
			return dtoResult;
		}else{

			request.getSession().removeAttribute("errorMensage");
			dtoResult.ok = true;
			DtoResultSindel dtoSindel = new DtoResultSindel();
			dtoResult.result = dtoSindel.warning;		
			/** ==========================================
			 *  Empty Sindel code
			 *  ========================================== */
			SindelUploader.sindelCode = "";
			return dtoResult;
		}
	}
}
