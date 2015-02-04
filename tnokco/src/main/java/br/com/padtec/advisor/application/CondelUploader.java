package br.com.padtec.advisor.application;

import java.util.ArrayList;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.transformation.condel.Condel2owlG805;
import br.com.padtec.transformation.condel.OwlG805toCondel;

public class CondelUploader {
	
	public static String condelCode = new String();
	
	public static void uploadCondelModel(String condelCode)
	{
		CondelUploader.condelCode = condelCode;		
	}
	
	public static ArrayList<String> transformLoadedOwltoCondel()
	{
		ArrayList<String> result = OwlG805toCondel.transformToCondel(OKCoUploader.getBaseModel());
		
		CondelUploader.condelCode="";
		for(String s: result)
		{
			CondelUploader.condelCode += s +"\n"; 
		}
		
		return result;
	}
	
	public static DtoResult transformCondeltoLoadedOwl()
	{
		Condel2owlG805.transformToOWL(OKCoUploader.getBaseModel(), condelCode);	
		
		return OKCoReasoner.runReasoner(OKCoUploader.reasonOnLoading);
	}
}
