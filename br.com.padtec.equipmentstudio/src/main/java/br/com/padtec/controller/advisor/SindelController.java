package br.com.padtec.controller.advisor;

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

import br.com.padtec.advisor.core.application.SindelUploader;
import br.com.padtec.advisor.core.dto.DtoResultAjax;
import br.com.padtec.advisor.core.util.FileReader;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.okco.core.exception.OKCoExceptionFileFormat;
import br.com.padtec.transformation.sindel.dto.DtoResultSindel;

@Controller
public class SindelController{
		
	@RequestMapping(value = "/uploadSindel", method = RequestMethod.POST)
	public String uploadSindel(HttpServletRequest request)
	{
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			if(!file.getOriginalFilename().endsWith(".sindel")) throw new OKCoExceptionFileFormat("Please select *.sindel file.");
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
			String error = "File not found: "+e.getLocalizedMessage();
			request.getSession().setAttribute("errorMensage", error);
			return "index";
			
		}catch (OKCoExceptionFileFormat e) {
			String error = "File format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			return "index";
		}
		return "redirect:sindel";
	}
	
	@RequestMapping(value = "/uploadSindelEquip", method = RequestMethod.POST)
	public String uploadSindelEquip(HttpServletRequest request)
	{
		try {			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			if(!file.getOriginalFilename().endsWith(".sindel")) throw new OKCoExceptionFileFormat("Please select *.sindel file.");			
			InputStream in = file.getInputStream();
			InputStreamReader r = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(r);
			FileReader readerFile = new FileReader();
			String txtSindel = readerFile.readFile(br);
			
			/** ==========================================
			 *  Upload Sindel Model
			 *  ========================================== */
			//SindelUploader.uploadSindelModel(txtSindel);
				
			request.getSession().setAttribute("txtSindelCode", txtSindel);
			
			txtSindel = txtSindel.replaceAll("\n", "<br>");
			request.getSession().setAttribute("txtSindelCodeBr", txtSindel);
			
			String equipNameAux = multipartRequest.getParameter("equipNameAux");
			request.getSession().setAttribute("equipNameAux", equipNameAux);
			request.getSession().setAttribute("equipName", equipNameAux);

		}catch (IOException e) {
			String error = "File not found.\n" + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);
			
		}catch (OKCoExceptionFileFormat e) {
			String error = "File format error: " + e.getMessage();
			request.getSession().setAttribute("errorMensage", error);			
		}
		
		return "add-equipment";			
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/uploadEquipTypes")
	public String uploadEquipTypes(HttpServletRequest request) 
	{
		int maxElements = 10;		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		try{
			for(int i = 1; i <= maxElements; i++)
			{
				MultipartFile file = multipartRequest.getFile("file"+i);

				if(file.getSize() <= 0) throw new OKCoExceptionFileFormat("Select one file on the position " + i + ".");
				else if(! file.getOriginalFilename().endsWith(".sindel")) throw new OKCoExceptionFileFormat("Please select owl file on the position " + i + ".");		
				
				InputStream in = file.getInputStream();
				InputStreamReader r = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(r);
				FileReader readerFile = new FileReader();
				String txtSindel = readerFile.readFile(br);
				
				/** ==========================================
				 *  Upload Sindel Model
				 *  ========================================== */
				//SindelUploader.uploadSindelModel(txtSindel);
				
				request.getSession().setAttribute("txtSindel"+i, txtSindel);
				
				String equipName = multipartRequest.getParameter("equipName"+i);
				request.getSession().setAttribute("equipName"+i, equipName);				

				request.getSession().setAttribute("file"+i, file);
				request.getSession().setAttribute("filename"+i, file.getOriginalFilename());
			}
		}catch (IOException e) {
			String error = "File not found.\n" + e.getMessage();
			multipartRequest.getSession().setAttribute("errorMensage", error);
						
		}catch (OKCoExceptionFileFormat e) {
			String error = "File format error: " + e.getMessage();
			multipartRequest.getSession().setAttribute("errorMensage", error);			
		}
		request.getSession().setAttribute("action", "runParser");
		
		return "add-equipment";
	}
			
	@RequestMapping(method = RequestMethod.POST, value="/runEquipTypes")
	public @ResponseBody DtoResultAjax runEquipTypes(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) 
	{
		DtoResultAjax dtoResult = new DtoResultAjax();
		dtoResult.ok = false;
		dtoResult.result = "It is necessary to inform the name of the new Equipment.";
		
		for (int i = 0; i < dtoGet.equipments.length; i++) 
		{
			/** Individual's prefix name */
			String individualsPrefixName = dtoGet.equipments[i][0];
			if(individualsPrefixName == null) return dtoResult;
			else if(individualsPrefixName == "") return dtoResult;
			
			/** Sindel code, parsed */
			String sindelParsedCode = dtoGet.equipments[i][1];

			/** ==========================================
			 *  Upload Sindel Model
			 *  ========================================== */
			SindelUploader.uploadSindelModel(sindelParsedCode);
			
			/** ==========================================
			 *  Transforms  Sindel into the OWL Base Model
			 *  ========================================== */
			SindelUploader.transformSindelToOwl(individualsPrefixName, false);				
		}
		request.getSession().removeAttribute("errorMensage");
		dtoResult.ok = true;
		dtoResult.result = "ok";

		cleanEquipSindel(request);

		return dtoResult;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/runEquipScratch")
	public @ResponseBody DtoResultAjax runEquipScratch(@RequestBody final DtoResultAjax dtoGet, HttpServletRequest request) 
	{
		DtoResultAjax dtoResult = new DtoResultAjax();
		dtoResult.ok = false;
		dtoResult.result = "It is necessary to inform the name of the new Equipment.";				

		String sindelCode = dtoGet.result;
		String individualsPrefixName = dtoGet.equipmentName;
		
		if(individualsPrefixName == null) return dtoResult;
		else if(individualsPrefixName == "") return dtoResult;
				
		/** ==========================================
		 *  Upload Sindel Model
		 *  ========================================== */
		SindelUploader.uploadSindelModel(sindelCode);
		
		/** ==========================================
		 *  Transforms  Sindel into the OWL Base Model
		 *  ========================================== */
		SindelUploader.transformSindelToOwl(individualsPrefixName, false);	
				
		request.getSession().removeAttribute("errorMensage");
		dtoResult.ok = true;
		dtoResult.result = "ok";
		
		cleanEquipSindel(request);

		return dtoResult;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/sindel")
	public String sindel(HttpSession session, HttpServletRequest request) 
	{					
		request.getSession().setAttribute("txtSindelCode", SindelUploader.getSindelCode());
		
		/** ==========================================
		 *  Clear Sindel code
		 *  ========================================== */
		SindelUploader.clear();
		
		return "sindel";	
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getSindel")
	public String getSindel(HttpSession session, HttpServletRequest request) throws IOException {

		if(SindelUploader.getSindelCode()!="")
		{
			request.getSession().setAttribute("txtSindelCode", SindelUploader.getSindelCode());
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
		SindelUploader.uploadSindelModel(sindelCode);
		
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
		 *  Clear Sindel code
		 *  ========================================== */
		SindelUploader.clear();
		
		request.getSession().setAttribute("txtSindelCode", SindelUploader.getSindelCode());

		DtoResultAjax dto = new DtoResultAjax();
		dto.ok = true;
		dto.result = "ok";
		return dto;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/cleanEquipSindel")
	public @ResponseBody DtoResultAjax cleanEquipSindel(HttpServletRequest request) 
	{
		String txtSindelCode = new String();
		
		request.getSession().setAttribute("txtSindelCode", txtSindelCode);

		DtoResultAjax dtoResult = new DtoResultAjax();
		dtoResult.ok = true;
		dtoResult.result = "ok";
		return dtoResult;
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
		 *  Upload Sindel Model
		 *  ========================================== */
		SindelUploader.uploadSindelModel(dtoGet.result);
		
		/** ==========================================
		 *  Transforms Sindel into the OWL Base Model
		 *  ========================================== */
		DtoResult result = SindelUploader.transformSindelToOwl();

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
			 *  Clear Sindel code
			 *  ========================================== */
			SindelUploader.clear();
			
			return dtoResult;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/add-equipment")
	public String newEquipment(HttpSession session, HttpServletRequest request) 
	{
		int maxElements = 10;
		
		request.getSession().setAttribute("txtSindelCode", "");
		request.getSession().setAttribute("txtSindelCodeBr", "");
		request.getSession().setAttribute("action", "");
		request.getSession().setAttribute("equipNameAux", "");
		request.getSession().setAttribute("equipName", "");		
		for (int i = 1; i <= maxElements; i++) 
		{
			request.getSession().setAttribute("equipName"+i, "");
			request.getSession().setAttribute("txtSindel"+i, "");
			request.getSession().setAttribute("file"+i, "");
			request.getSession().setAttribute("filename"+i, "");
		}	
		if(OKCoUploader.getBaseModel() == null)
		{
			String error = "Error! You need to load the model first.";
			request.getSession().setAttribute("errorMensage", error);
			return "index";
		}
		
		cleanEquipSindel(request);
		
		return "add-equipment";
	}

}
