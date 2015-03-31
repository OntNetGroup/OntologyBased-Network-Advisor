package br.com.padtec.nopen.provisioning.service;

import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoSelector;
import br.com.padtec.okco.core.application.OKCoUploader;

public class ProvisioningComponents {

	public static OKCoUploader provisioningRepository= new OKCoUploader();
	public static OKCoSelector provisioningSelector = new OKCoSelector(provisioningRepository);
	public static OKCoReasoner provisioningReasoner = new OKCoReasoner(provisioningRepository,provisioningSelector);	
}
