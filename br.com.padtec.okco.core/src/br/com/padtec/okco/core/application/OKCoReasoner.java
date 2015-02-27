package br.com.padtec.okco.core.application;

import br.com.padtec.common.dto.DtoResult;

public class OKCoReasoner {
	
	/**
	 * Running the reasoner, storing a temporary model and cleaning the list of modified.
	 * 
	 * @return
	 */
	public static DtoResult runReasoner()
	{
		return runReasoner(true);		
	}
	
	public static DtoResult runReasoner(boolean useInference)
	{
		DtoResult dto = new DtoResult();
		try {
			/** Storing a temporary model... */
			OKCoUploader.storeTemporaryModelFromBaseModel();
			/** Running reasoner... */
			OKCoUploader.substituteInferredModelFromBaseModel(useInference);			
			/** Clean List of modified individuals */
			OKCoSelector.clearModified();
		}
		catch (Exception e) 
		{
			/** Roll back to the temporary model stored, running the reasoner. */  
			OKCoUploader.rollBack(true);			
			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			dto.setMessage(error);
			dto.setIsSucceed(false);			
		}
		dto.setIsSucceed(true);
		dto.setMessage("ok");
		return dto;		
	}
	
}
