package br.com.padtec.nopen.service;

import br.com.padtec.nopen.provisioning.service.ProvisioningInitializer;
import br.com.padtec.nopen.studio.service.StudioInitializer;

public class NOpenInitializer {
	
	public static void uploadTBoxes() throws Exception
	{		
		String eMsg  = StudioInitializer.uploadEquipStudioTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
		
		eMsg  = ProvisioningInitializer.uploadProvisioningTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
	}
}
