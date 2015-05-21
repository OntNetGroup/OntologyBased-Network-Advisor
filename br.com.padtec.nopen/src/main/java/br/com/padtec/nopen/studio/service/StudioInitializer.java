package br.com.padtec.nopen.studio.service;

import java.io.InputStream;
import java.util.Date;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.util.NOpenUtilities;

public class StudioInitializer {

	public static String uploadEquipStudioTBox(boolean runReasoner)
	{
		Date beginDate = new Date();
		InputStream s = StudioInitializer.class.getResourceAsStream("/model/EquipStudio.owl");
		
		String msg = NOpenUtilities.uploadTBOx(s, runReasoner, StudioComponents.studioRepository);
		
		PerformanceUtil.printExecutionTime("Equip Studio: TBox uploaded.", beginDate);
		
		
		//---------------------------------------Testing Methods-----------------------------------------------//
		PerformBind bind = new PerformBind();
		try {
			FactoryUtil.createInstanceIndividual(
					StudioComponents.studioRepository.getBaseModel(), 
					StudioComponents.studioRepository.getNamespace()+"ttf1", 
					StudioComponents.studioRepository.getNamespace()+"Trail_Termination_Function"
				);
			FactoryUtil.createInstanceIndividual(
					StudioComponents.studioRepository.getBaseModel(), 
					StudioComponents.studioRepository.getNamespace()+"af1", 
					StudioComponents.studioRepository.getNamespace()+"Adaptation_Function"
				);
			boolean result = bind.applyBinds( "ttf1",  "ttf1",  "ttf1",  "af1",  "http://nemo.inf.ufes.br/NewProject.owl#Trail_Termination_Function",  "http://nemo.inf.ufes.br/NewProject.owl#Adaptation_Function",  StudioComponents.studioRepository);
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return msg;
	}	
	
}
