package br.com.padtec.nopen.studio.service;

import java.io.InputStream;
import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.util.NOpenUtilities;

public class StudioInitializer {

	public static String uploadEquipStudioTBox(boolean runReasoner)
	{
		Date beginDate = new Date();
		InputStream s = StudioInitializer.class.getResourceAsStream("/model/EquipStudio.owl");
		
		String msg = NOpenUtilities.uploadTBOx(s, runReasoner, StudioComponents.studioRepository);
		
		PerformanceUtil.printExecutionTime("Equip Studio: TBox uploaded.", beginDate);		
		
		BuildBindStructure.createBindStructure(StudioComponents.studioRepository.getNamespace() + "binds");
		
		PerformanceUtil.printExecutionTime("Equip Studio: Binding structure created.", beginDate);	
		
		return msg;
	}	
	
}
