package br.com.padtec.nopen.service;

import br.com.padtec.nopen.provisioning.service.ProvisioningInitializer;
import br.com.padtec.nopen.service.util.NOpenFactoryUtil;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.nopen.studio.service.StudioInitializer;

public class NOpenInitializer {
	
	public static void uploadTBoxes() throws Exception
	{		
		String eMsg  = StudioInitializer.uploadEquipStudioTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
		
		eMsg  = ProvisioningInitializer.uploadProvisioningTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
		
		
		 //Testing
		  NOpenFactoryUtil.createCard(StudioComponents.studioRepository, "Card1");
		
		ModelStructureAccessorController.initContainerStructure("Card1", StudioComponents.studioRepository);
		for(String s : ModelStructureAccessorController.getRelationsMapping().keySet()){
			System.out.println(s);
			System.out.println(ModelStructureAccessorController.getRelationsMapping().get(s));
			System.out.println("------------------");
		}
	}
}
