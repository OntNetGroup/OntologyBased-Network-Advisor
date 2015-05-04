package br.com.padtec.nopen.studio.service;

import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoSelector;
import br.com.padtec.okco.core.application.OKCoUploader;

public class StudioComponents {
	
	public static OKCoUploader studioRepository= new OKCoUploader("Studio");	
	public static OKCoSelector studioSelector = new OKCoSelector(studioRepository);
	public static OKCoReasoner studioReasoner = new OKCoReasoner(studioRepository,studioSelector);
}
