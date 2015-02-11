package br.com.padtec.advisor.application;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.trasnformation.sindel.Sindel2OWL;

import com.hp.hpl.jena.ontology.OntModel;

public class SindelUploader {

	public static String sindelCode = new String();
	
	public static void uploadSindelModel(String sindelCode)
	{
		SindelUploader.sindelCode = sindelCode;
	}
	
	public static DtoResult transformSindelToLoadedOwl()
	{
		OntModel basemodel = OKCoUploader.getBaseModel();
				
		Sindel2OWL so = new Sindel2OWL(basemodel);
		so.run(sindelCode);
	
		return OKCoReasoner.runReasoner(OKCoUploader.reasonOnLoading);
	}
}
