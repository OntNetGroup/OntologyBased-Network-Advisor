package br.com.padtec.nopen.studio.service;

import java.io.InputStream;
import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.model.NOpenFactory;
import br.com.padtec.nopen.service.util.NOpenUtilities;

public class StudioInitializer {

	public static void run(boolean runInference) throws Exception
	{		
		String eMsg  = StudioInitializer.uploadEquipStudioTBox(false);
		if(!eMsg.isEmpty()) {throw new Exception(eMsg); }
		
		StudioInitializer.registerDefaultTechnologies();
		
		StudioInitializer.runInference(runInference);
	}
	
	public static String uploadEquipStudioTBox(boolean runReasoner)
	{
		Date beginDate = new Date();
		InputStream s = StudioInitializer.class.getResourceAsStream("/model/EquipStudio.owl");
		
		String msg = NOpenUtilities.uploadTBOx(s, runReasoner, StudioComponents.studioRepository);
		
		PerformanceUtil.printExecutionTime("Equip Studio: TBox uploaded.", beginDate);
		return msg;
	}
	
	public static void registerDefaultTechnologies()
	{
		Date beginDate = new Date();		
				
		NOpenFactory.createMEFTech(StudioComponents.studioRepository);
		NOpenFactory.createOTNTech(StudioComponents.studioRepository);		
		NOpenFactory.createServices(StudioComponents.studioRepository);
		
		PerformanceUtil.printExecutionTime("Equip Studio: Technologies, Layers and Services registered.", beginDate);
	}
	
	public static void runInference(boolean runReasoner)
	{
		Date beginDate = new Date();
		StudioComponents.studioReasoner.runReasoner(runReasoner);		
		PerformanceUtil.printExecutionTime("Equip Studio: Inferences executed.", beginDate);
	}	
}
