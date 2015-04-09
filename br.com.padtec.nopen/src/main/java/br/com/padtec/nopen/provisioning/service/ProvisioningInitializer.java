package br.com.padtec.nopen.provisioning.service;

import java.io.InputStream;
import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.NOpenInitializer;
import br.com.padtec.nopen.service.util.NOpenFactory;
import br.com.padtec.nopen.service.util.NOpenUtilities;

public class ProvisioningInitializer {

	public static void run(boolean runInference) throws Exception
	{	
		String pMsg  = ProvisioningInitializer.uploadProvisioningTBox(false);
		if(!pMsg.isEmpty()) {throw new Exception(pMsg); }
		
		ProvisioningInitializer.registerDefaultTechnologies();
		
		ProvisioningInitializer.runInference(runInference);
	}
	
	public static String uploadProvisioningTBox(boolean runReasoner)
	{
		Date beginDate = new Date();
		InputStream s = NOpenInitializer.class.getResourceAsStream("/model/Provisioning.owl");
		
		String msg =  NOpenUtilities.uploadTBOx(s, runReasoner, ProvisioningComponents.provisioningRepository);
		
		PerformanceUtil.printExecutionTime("Provisioning: TBox uploaded.", beginDate);
		return msg;
	}
	
	public static void registerDefaultTechnologies()
	{
		Date beginDate = new Date();		
		
		NOpenFactory.createMEFTech(ProvisioningComponents.provisioningRepository);
		NOpenFactory.createOTNTech(ProvisioningComponents.provisioningRepository);
		NOpenFactory.createServices(ProvisioningComponents.provisioningRepository);
		
		PerformanceUtil.printExecutionTime("Provisioning: Technologies, Layers and Services registered.", beginDate);
	}
	
	public static void runInference(boolean runReasoner)
	{
		Date beginDate = new Date();		
		ProvisioningComponents.provisioningReasoner.runReasoner(runReasoner);
		PerformanceUtil.printExecutionTime("Provisioning: Inferences executed.", beginDate);
	}	
}
