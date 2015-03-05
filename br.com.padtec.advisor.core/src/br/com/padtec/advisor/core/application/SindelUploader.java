package br.com.padtec.advisor.core.application;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.trasnformation.sindel.Sindel2OWL;

import com.hp.hpl.jena.ontology.OntModel;

public class SindelUploader {

	private static String sindelCode = new String();
	
	public static void uploadSindelModel(String sindelCode)
	{
		SindelUploader.sindelCode = sindelCode;
	}
	
	public static String getSindelCode()
	{
		return sindelCode;
	}
	
	public static void clear()
	{
		sindelCode = "";
	}
	
	public static DtoResult transformSindelToOwl()
	{
		OntModel basemodel = OKCoUploader.getBaseModel();
				
		Sindel2OWL so = new Sindel2OWL(basemodel);
		so.run(sindelCode);
	
		return OKCoReasoner.runReasoner(OKCoUploader.reasonOnLoading);
	}
	
	public static DtoResult transformSindelToOwl(String individualsPrefixName)
	{
		OntModel basemodel = OKCoUploader.getBaseModel();
				
		Sindel2OWL so = new Sindel2OWL(basemodel, individualsPrefixName);
		so.run(sindelCode);
	
		return OKCoReasoner.runReasoner(OKCoUploader.reasonOnLoading);
	}
	
	public static DtoResult transformSindelToOwl(String individualsPrefixName, boolean runReasoning)
	{
		OntModel basemodel = OKCoUploader.getBaseModel();
				
		Sindel2OWL so = new Sindel2OWL(basemodel, individualsPrefixName);
		so.run(sindelCode);
	
		return OKCoReasoner.runReasoner(runReasoning);
	}
}
