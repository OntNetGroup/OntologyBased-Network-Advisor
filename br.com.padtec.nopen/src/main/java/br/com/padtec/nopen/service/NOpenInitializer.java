package br.com.padtec.nopen.service;

import br.com.padtec.nopen.provisioning.service.ProvisioningInitializer;
import br.com.padtec.nopen.studio.service.StudioInitializer;

public class NOpenInitializer {
	
	public static void run() throws Exception
	{		
		StudioInitializer.run(true);

		ProvisioningInitializer.run(true);	
	}
}
