package br.com.padtec.okco.core.application;

import br.com.padtec.common.dto.DtoResult;

public class OKCoReasoner {
	
	protected OKCoUploader repository; 
	protected OKCoSelector selector; 
	long lastReasoningTimeExec = 0;
	
	public long getLastReasoningTimeExec() {
		return lastReasoningTimeExec;
	}
	
	public OKCoReasoner(OKCoUploader repository, OKCoSelector selector)
	{
		this.repository = repository;
		this.selector = selector;
	}	
	
	/**
	 * Running the reasoner, storing a temporary model and cleaning the list of modified.
	 * 
	 * @return
	 */
	public DtoResult runReasoner()
	{
		return runReasoner(true);		
	}
	
	public DtoResult runReasoner(boolean useInference)
	{		
		DtoResult dto = new DtoResult();
		try {
			/** Storing a temporary model... */
			repository.storeTemporaryModelFromBaseModel();
			/** Running reasoner... */
			repository.substituteInferredModelFromBaseModel(useInference);	
			this.lastReasoningTimeExec = repository.getLastReasoningTimeExec();
			/** Clean List of modified individuals */
			selector.clearModified();
		}
		catch (Exception e) 
		{
			/** Roll back to the temporary model stored, running the reasoner. */  
			repository.rollBack(true);			
			String error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";
			dto.setMessage(error);
			dto.setIsSucceed(false);		
			return dto;	
		}		
		dto.setIsSucceed(true);
		dto.setMessage("ok");
		return dto;		
	}
	
}
