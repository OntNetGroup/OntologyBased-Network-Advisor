package br.com.padtec.nopen.service;

import br.com.padtec.nopen.provisioning.service.ProvisioningReasoner;
import br.com.padtec.nopen.studio.service.StudioReasoner;

public class NOpenReasoner {

	public static void runInference(boolean runReasoner)
	{	
		ProvisioningReasoner.runInference(runReasoner);
		
		StudioReasoner.runInference(runReasoner);		
	}	
}
