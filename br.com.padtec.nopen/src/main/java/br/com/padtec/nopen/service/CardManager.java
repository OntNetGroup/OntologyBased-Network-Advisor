package br.com.padtec.nopen.service;

import com.hp.hpl.jena.rdf.model.InfModel;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class CardManager {

	public boolean verifySupervisorOfCard(String card, OKCoUploader repository){
		boolean supervisor = false;
		if(NOpenQueryUtil.cardHasSupervisor(card, repository.getBaseModel())){
			supervisor = true;
		}
		return supervisor;
	}
	
}
