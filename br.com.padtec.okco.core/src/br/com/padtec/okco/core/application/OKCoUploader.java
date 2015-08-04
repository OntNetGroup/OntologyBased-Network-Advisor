package br.com.padtec.okco.core.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.common.persistence.BaseModelRepository;
import br.com.padtec.common.persistence.BaseModelRepositoryImpl;
import br.com.padtec.common.persistence.InferredModelRepository;
import br.com.padtec.common.persistence.InferredModelRepositoryImpl;
import br.com.padtec.common.queries.OntModelAPI;
import br.com.padtec.common.reasoning.HermitReasonerImpl;
import br.com.padtec.common.reasoning.OntologyReasoner;
import br.com.padtec.common.reasoning.PelletReasonerImpl;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.core.exception.OKCoExceptionNameSpace;
import br.com.padtec.okco.core.exception.OKCoExceptionReasoner;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.reasoner.ValidityReport;

public class OKCoUploader {

	/** Reasoner */
	protected OntologyReasoner reasoner;
	
	/** Base Model Repository */
	protected BaseModelRepository baseRepository;
	protected BaseModelRepository consistencyBaseRepository;
	
	/** Inferred Model Repository */
	protected InferredModelRepository inferredRepository;
	
	/** Temporary Model used to rool back */
	protected OntModel tempModel;
	
	protected String name = new String();
	
	protected boolean reasonOnLoading = false;
	
	public boolean isReasonOnLoading()  { return reasonOnLoading; }
	public void setIsReasonOnLoading(boolean value)  { reasonOnLoading=value; }
	public OntologyReasoner getReasoner() { return reasoner; }
	public void setReasoner(OntologyReasoner r) { reasoner = r; }
	long lastReasoningTimeExec = 0;
	
	public long getLastReasoningTimeExec() {
		return lastReasoningTimeExec;
	}
	
	public String getName(){ return name; }
	private void setName(String name){ this.name = name; }
	
	/**
	 * SET DEFAULT REASONER
	 * @param name
	 */
	public OKCoUploader(String name)
	{
		setName(name);
//		reasoner = new PelletReasonerImpl();
		reasoner = new HermitReasonerImpl();
	}
	
	public boolean isConsistencyModelValid(){
		OntModel model = consistencyBaseRepository.getBaseOntModel();
		ValidityReport valReport = model.validate();
		
		return valReport.isValid();
	}
	
	/**
	 * Upload the base model ontology in OWL. The user might opt for not using the reasoner at the upload.
	 * 
	 * @param in: OWL Input Stream
	 * @param useReasoner: Use the reasoner at the uploading. "on" or "off"
	 * @param optReasoner: Which reasoner must be used in the uploading. "hermit" or "pellet"
	 * 
	 * @throws InconsistentOntologyException
	 * @throws OKCoExceptionInstanceFormat
	 * @throws IOException
	 * @throws OKCoExceptionNameSpace
	 * @throws OKCoExceptionReasoner
	 * 
	 * @author Freddy Brasileiro
	 */
	public void uploadBaseModel(InputStream in, String useReasoner, String optReasoner)
	throws InconsistentOntologyException, OKCoExceptionInstanceFormat, IOException, OKCoExceptionNameSpace, OKCoExceptionReasoner
	{	
		baseRepository = new BaseModelRepositoryImpl();
		uploadBaseModelGenerically(in, baseRepository);
		
		if(useReasoner!=null && useReasoner.equals("on")) reasonOnLoading = true;
		else  reasonOnLoading = false;
		
		InfModel inferredModel;
		/** Run the inference if required, otherwise the inferred model is a clone of the base model */
		if(reasonOnLoading)
		{	
			if(optReasoner.equals("hermit")) reasoner = new HermitReasonerImpl();				  
			else if(optReasoner.equals("pellet")) reasoner = new PelletReasonerImpl();				  
			else throw new OKCoExceptionReasoner("Please select a reasoner available.");
			
			inferredModel = reasoner.run(baseRepository);
		}else{
			
			if(optReasoner.equals("hermit")) reasoner = new HermitReasonerImpl();				  
			else if(optReasoner.equals("pellet")) reasoner = new PelletReasonerImpl();	
			else reasoner = new PelletReasonerImpl();
			
			inferredModel = OntModelAPI.clone(baseRepository.getBaseOntModel());
		}
		inferredRepository = new InferredModelRepositoryImpl(inferredModel);
		
	}
	
	/**
	 * Upload the base model ontology in OWL. The user might opt for not using the reasoner at the upload.
	 * 
	 * @param in: OWL Input Stream
	 * @param useReasoner: Use the reasoner at the uploading. "on" or "off"
	 * @param optReasoner: Which reasoner must be used in the uploading. "hermit" or "pellet"
	 * 
	 * @throws InconsistentOntologyException
	 * @throws OKCoExceptionInstanceFormat
	 * @throws IOException
	 * @throws OKCoExceptionNameSpace
	 * @throws OKCoExceptionReasoner
	 * 
	 * @author Freddy Brasileiro
	 */
	public void uploadConsistencyBaseModel(InputStream in)
	throws InconsistentOntologyException, OKCoExceptionInstanceFormat, IOException, OKCoExceptionNameSpace, OKCoExceptionReasoner
	{	
		consistencyBaseRepository = new BaseModelRepositoryImpl();
		uploadBaseModelGenerically(in, consistencyBaseRepository);
	}
	
	/**
	 * Upload the base model ontology in OWL. The user might opt for not using the reasoner at the upload.
	 * 
	 * @param in: OWL Input Stream
	 * @param useReasoner: Use the reasoner at the uploading. "on" or "off"
	 * @param optReasoner: Which reasoner must be used in the uploading. "hermit" or "pellet"
	 * 
	 * @throws InconsistentOntologyException
	 * @throws OKCoExceptionInstanceFormat
	 * @throws IOException
	 * @throws OKCoExceptionNameSpace
	 * @throws OKCoExceptionReasoner
	 * 
	 * @author Freddy Brasileiro
	 * @return 
	 */
	public void uploadBaseModelGenerically(InputStream in, BaseModelRepository baseRepositoryAux)
	throws InconsistentOntologyException, OKCoExceptionInstanceFormat, IOException, OKCoExceptionNameSpace, OKCoExceptionReasoner
	{	
		/** Upload the base model to a base repository */
//		baseRepositoryAux = new BaseModelRepositoryImpl();
		
		baseRepositoryAux.readBaseOntModel(in);
		if(baseRepositoryAux.getNameSpace() == null) throw new OKCoExceptionNameSpace("Please select owl file with defined namespace.");
		
		/** Keep a temporary model for rollbacking the base model */
		tempModel = OntModelAPI.clone(baseRepositoryAux.getBaseOntModel());
	}
		
	/**
	 * Upload the base model ontology in OWL. The user might opt for not using the reasoner at the upload.
	 * 
	 * @param path: OWL String Path
	 * @param useReasoner: Use the reasoner at the uploading. "on" or "off"
	 * @param optReasoner: Which reasoner must be used in the Uploading. "hermit" or "pellet"
	 * 
	 * @throws InconsistentOntologyException
	 * @throws OKCoExceptionInstanceFormat
	 * @throws IOException
	 * @throws OKCoExceptionNameSpace
	 * @throws OKCoExceptionReasoner
	 * 
	 * @author John Guerson
	 */
	public void uploadBaseModel(String path, String useReasoner, String optReasoner)
	throws InconsistentOntologyException, OKCoExceptionInstanceFormat, IOException, OKCoExceptionNameSpace, OKCoExceptionReasoner
	{	
		/** Upload the base model to a base repository */
		baseRepository = new BaseModelRepositoryImpl();		 
		baseRepository.readBaseOntModel(path);		 		 			  
		if(baseRepository.getNameSpace() == null) throw new OKCoExceptionNameSpace("Please select owl file with defined namespace.");
		
		/** Keep a temporary model for rollbacking the base model */
		tempModel = OntModelAPI.clone(baseRepository.getBaseOntModel());
				
		if(useReasoner!=null && useReasoner.equals("on")) reasonOnLoading = true;
		else  reasonOnLoading = false;
		
		/** Run the inference if required, otherwise the inferred model is a clone of the base model */
		if(reasonOnLoading)
		{			
			if(optReasoner.equals("hermit")) reasoner = new HermitReasonerImpl();	  
			else if(optReasoner.equals("pellet")) reasoner = new PelletReasonerImpl();				  
			else throw new OKCoExceptionReasoner("Please select a reasoner available.");
			
			InfModel inferredModel = reasoner.run(baseRepository);
			inferredRepository = new InferredModelRepositoryImpl(inferredModel);
		}else{

			if(optReasoner.equals("hermit")) reasoner = new HermitReasonerImpl();				  
			else if(optReasoner.equals("pellet")) reasoner = new PelletReasonerImpl();	
			else reasoner = new PelletReasonerImpl();
			
			InfModel  inferredModel = OntModelAPI.clone(baseRepository.getBaseOntModel());
			inferredRepository = new InferredModelRepositoryImpl(inferredModel);
		}
	}
	
	public BaseModelRepository getBaseRepository() { return baseRepository; }	
	public OntModel getBaseModel() 
	{ 
		if(baseRepository!=null) return baseRepository.getBaseOntModel(); 
		else return null; 
	}	
	public OntModel getConsistencyBaseModel() 
	{ 
		if(consistencyBaseRepository!=null) return consistencyBaseRepository.getBaseOntModel(); 
		else return null; 
	}	
	public InferredModelRepository getInferredRepository() { return inferredRepository; }	
	public InfModel getInferredModel() 
	{
		if(inferredRepository == null) return null;
		return inferredRepository.getInferredOntModel(); 
	}	
	public boolean isBaseModelUploaded() { return baseRepository.getBaseOntModel()!=null; }	
	public String getBaseModelAsString() { return baseRepository.getBaseOntModelAsString(); }
	public String getNamespace() { if(baseRepository!=null && baseRepository.getBaseOntModel()!=null) return baseRepository.getNameSpace(); else return ""; }
	
	/**
	 * Save Base Model to a file.
	 * 
	 * @author John Guerson
	 */
	public boolean saveBaseModel()
	{		
		if(baseRepository.getBaseOntModel() != null)
		{			
			baseRepository.saveBaseOntModel("");
			return true;			
		}else{
			return false;
		}
	}
	
	/**
	 * Clear all the repositories (base model, inferred model and temporary model).
	 * As well as the resoner choice.
	 * 
	 * @author John Guerson
	 */
	public void clear()
	{		
		if(baseRepository!=null) baseRepository.clear();
		if(inferredRepository!=null) inferredRepository.clear();
		tempModel = null;				
		reasoner = null;
	}
	
	/**
	 * Rollback to a valid model, which is stored in the temporary model
	 * 
	 * @author John Guerson
	 */
	public void rollBack(boolean runningReasoner)
	{				
		baseRepository.cloneReplacing(tempModel);		
		if(!runningReasoner) inferredRepository.cloneReplacing(tempModel);
		else {
			InfModel newnferredModel = reasoner.run(getBaseModel());
			inferredRepository.setInferredModel(newnferredModel);
		}			
	}	
	
	/**
	 *  Bring all the modification from the Base Model to the Inferred Model (OntModel -> InfModel).
	 *  This is done since all the retrieve of information is performed in the inferred model and all the modifications in the base model.  
	 *  In other words: Update InfModel i) without calling the reasoner, copying the OntModel or ii) running the inference
	 */
	public void substituteInferredModelFromBaseModel(boolean runningReasoner)
	{
		List<Statement> modStms = getBaseModel().listStatements().toList();
        
		if(!runningReasoner){
			OntModel newInferredModel = OntModelAPI.clone(getBaseModel());
			inferredRepository.setInferredModel(newInferredModel);
		}else{
			InfModel newInferredModel = reasoner.run(getBaseModel());
			lastReasoningTimeExec = reasoner.getReasoningTimeExec();
			inferredRepository.setInferredModel(newInferredModel);
			
			if(consistencyBaseRepository != null && consistencyBaseRepository.getBaseOntModel() != null){
				List<Statement> infStms = newInferredModel.listStatements().toList();
		        List<Statement> toAdd = new ArrayList<Statement>();
		        for(Statement stm : infStms){
		        	if(!modStms.contains(stm)){
		        		toAdd.add(stm);
		        	}
		        }
		        consistencyBaseRepository.getBaseOntModel().add(toAdd);
			}
			
		}			
		baseRepository.setBaseOntModel((OntModel) inferredRepository.getInferredOntModel());
	}	
	
		/**
	 * Record a temporary model from the last valid base model in order to allow a roll back in the future.
	 */
	public void storeTemporaryModelFromBaseModel()
	{
		tempModel = OntModelAPI.clone(getBaseModel());
	}
}
