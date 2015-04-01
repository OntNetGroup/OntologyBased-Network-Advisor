package br.com.padtec.nopen.service;

import java.util.HashSet;

import br.com.padtec.nopen.model.NOpenFactory;
import br.com.padtec.nopen.provisioning.service.ProvisioningInitializer;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.nopen.studio.service.StudioInitializer;
import br.com.padtec.nopen.topology.controller.ManagerTopology;

public class NOpenInitializer {
	
	public static void run() throws Exception
	{		
		StudioInitializer.run(false);
		
		ProvisioningInitializer.run(false);		
		
		
	}
}
