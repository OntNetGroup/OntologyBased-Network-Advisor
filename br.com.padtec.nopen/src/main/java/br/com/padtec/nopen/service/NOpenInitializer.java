package br.com.padtec.nopen.service;

import java.io.InputStream;
import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.provisioning.service.ProvisioningInitializer;
import br.com.padtec.nopen.service.util.NOpenUtilities;
import br.com.padtec.nopen.studio.service.StudioInitializer;
import br.com.padtec.nopen.topology.service.TopologyInitializer;

public class NOpenInitializer {
	
	public static String uploadNOpenTBox(boolean runReasoner)
	{
		Date beginDate = new Date();
		InputStream s = NOpenInitializer.class.getResourceAsStream("/model/NOpen.owl");
		
		String msg = NOpenUtilities.uploadTBOx(s, runReasoner, NOpenComponents.nopenRepository);
		
		PerformanceUtil.printExecutionTime("NOpen: TBox uploaded.", beginDate);			
		return msg;
	}
	
	public static void uploadTBoxes() throws Exception
	{		

		String eMsg  = NOpenInitializer.uploadNOpenTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
		
		eMsg  = StudioInitializer.uploadEquipStudioTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
		
		eMsg  = ProvisioningInitializer.uploadProvisioningTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
		
		eMsg  = TopologyInitializer.uploadTopologyTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
		
		
		ContainerStructure.buildContainerStructure(NOpenComponents.nopenRepository.getNamespace() + RelationEnum.componentOf.toString());
		
	}
	
	
}
