package br.com.padtec.advisor.core.application;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.trasnformation.sindel.Sindel2OWL;

import com.hp.hpl.jena.ontology.OntModel;

public class SindelUploader {

	private String sindelCode = new String();
	
	protected OKCoUploader repository;
	protected OKCoReasoner reasoner;
	
	public SindelUploader(OKCoUploader repository, OKCoReasoner reasoner)
	{
		this.repository = repository;
		this.reasoner = reasoner;
	}
	
	public void uploadSindelModel(String sindelCode)
	{
		this.sindelCode = sindelCode;
	}
	
	public String getSindelCode()
	{
		return sindelCode;
	}
	
	public void clear()
	{
		sindelCode = "";
	}
	
	public DtoResult transformSindelToOwl()
	{
		OntModel basemodel = repository.getBaseModel();
				
		Sindel2OWL so = new Sindel2OWL(basemodel);
		so.run(sindelCode);
	
		return reasoner.runReasoner(repository.isReasonOnLoading());
	}
	
	public DtoResult transformSindelToOwl(String individualsPrefixName)
	{
		OntModel basemodel = repository.getBaseModel();
				
		Sindel2OWL so = new Sindel2OWL(basemodel, individualsPrefixName);
		so.run(sindelCode);
	
		return reasoner.runReasoner(repository.isReasonOnLoading());
	}
	
	public DtoResult transformSindelToOwl(String individualsPrefixName, boolean runReasoning)
	{
		OntModel basemodel = repository.getBaseModel();
				
		Sindel2OWL so = new Sindel2OWL(basemodel, individualsPrefixName);
		so.run(sindelCode);
	
		return reasoner.runReasoner(runReasoning);
	}
}
