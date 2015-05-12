package br.com.padtec.nopen.studio.service;

import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.util.NOpenUtilities;

public class StudioInitializer {

	public static String uploadEquipStudioTBox(boolean runReasoner)
	{
		Date beginDate = new Date();
		InputStream s = StudioInitializer.class.getResourceAsStream("/model/EquipStudio.owl");
		
		String msg = NOpenUtilities.uploadTBOx(s, runReasoner, StudioComponents.studioRepository);
		
		PerformanceUtil.printExecutionTime("Equip Studio: TBox uploaded.", beginDate);
		
		HashSet<String> result = new HashSet<String>();
		result = PerformBind.discoverRPBetweenPorts("http://nemo.inf.ufes.br/NewProject.owl#Trail_Termination_Function_Input", "http://nemo.inf.ufes.br/NewProject.owl#Adaptation_Function_Input", StudioComponents.studioRepository);
	
		
		return msg;
	}	
	
}
