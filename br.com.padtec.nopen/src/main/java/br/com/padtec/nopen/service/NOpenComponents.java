package br.com.padtec.nopen.service;

import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoSelector;
import br.com.padtec.okco.core.application.OKCoUploader;

public class NOpenComponents {

	public static OKCoUploader nopenRepository= new OKCoUploader("NOpen");
	public static OKCoSelector nopenSelector = new OKCoSelector(nopenRepository);
	public static OKCoReasoner nopenReasoner = new OKCoReasoner(nopenRepository,nopenSelector);	
}
