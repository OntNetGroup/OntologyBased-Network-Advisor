package br.com.padtec.common.application;

import br.com.padtec.common.dto.DtoResult;

public class ReasoningApp {
	
	/**
	 * Running the reasoner, storing a temporary model and cleaning the list of modified.
	 * 
	 * @return
	 */
	public static DtoResult runReasoner()
	{
		DtoResult dto = new DtoResult();
		try {			
			/** Running reasoner... */
			UploadApp.substituteInferredModelFromBaseModel(true);			
			/** Storing a temporary model... */
			UploadApp.storeTemporaryModelFromBaseModel();
			/** Clean List of modified individuals */
			OKCoApp.clearModified();
		}
		catch (Exception e) 
		{
			/** Roll back to the temporary model stored, running the reasoner. */  
			UploadApp.rollBack(true);			
			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			dto.setMessage(error);
			dto.setIsSucceed(false);			
		}
		dto.setIsSucceed(true);
		dto.setMessage("ok");
		return dto;		
	}
	
}
