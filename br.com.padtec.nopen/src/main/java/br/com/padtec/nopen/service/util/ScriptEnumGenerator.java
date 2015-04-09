package br.com.padtec.nopen.service.util;

import br.com.padtec.okco.core.application.OKCoUploader;

public class ScriptEnumGenerator {

	public static void main(String[] args)
	{
		String owlPath = "/src/main/resources/model/EquipStudio.owl";
		
		/** Upload */
		OKCoUploader owlRepository = new OKCoUploader();
		NOpenUtilities.uploadTBOx(owlPath, false, owlRepository);
		
		//(classURI)owlRepository.getBaseModel()
	}
}
