package br.com.padtec.nopen.studio.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;

public class StudioReasoner {

	public static void runInference(boolean runReasoner)
	{
		Date beginDate = new Date();
		StudioComponents.studioReasoner.runReasoner(runReasoner);		
		PerformanceUtil.printExecutionTime("Equip Studio: Inferences executed successfully.", beginDate);
	}	
}
