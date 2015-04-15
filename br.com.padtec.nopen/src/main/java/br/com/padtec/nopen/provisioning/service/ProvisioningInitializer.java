package br.com.padtec.nopen.provisioning.service;

import java.io.InputStream;
import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.NOpenInitializer;
import br.com.padtec.nopen.service.util.NOpenUtilities;

public class ProvisioningInitializer {

	public static String uploadProvisioningTBox(boolean runReasoner)
	{
		Date beginDate = new Date();
		InputStream s = NOpenInitializer.class.getResourceAsStream("/model/Provisioning.owl");
		
		String msg =  NOpenUtilities.uploadTBOx(s, runReasoner, ProvisioningComponents.provisioningRepository);
		
		PerformanceUtil.printExecutionTime("Provisioning: TBox uploaded.", beginDate);
		return msg;
	}	
}
