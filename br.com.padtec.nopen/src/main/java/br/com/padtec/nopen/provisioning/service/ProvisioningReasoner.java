package br.com.padtec.nopen.provisioning.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;

public class ProvisioningReasoner {

	public static void runInference(boolean runReasoner)
	{
		Date beginDate = new Date();
		ProvisioningComponents.provisioningReasoner.runReasoner(runReasoner);		
		PerformanceUtil.printExecutionTime("Provisioning: Inferences executed successfully.", beginDate);
	}	
}
