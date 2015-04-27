package br.com.padtec.nopen.service;

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
