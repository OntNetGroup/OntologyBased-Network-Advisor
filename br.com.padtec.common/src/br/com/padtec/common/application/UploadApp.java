package br.com.padtec.common.application;

import java.io.IOException;
import java.io.InputStream;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.common.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.common.exceptions.OKCoExceptionNameSpace;
import br.com.padtec.common.exceptions.OKCoExceptionReasoner;
import br.com.padtec.common.persistence.BaseModelRepository;
import br.com.padtec.common.persistence.BaseModelRepositoryImpl;
import br.com.padtec.common.persistence.InferredModelRepository;
import br.com.padtec.common.persistence.InferredModelRepositoryImpl;
import br.com.padtec.common.queries.OntModelAPI;
import br.com.padtec.common.reasoning.HermitReasonerImpl;
import br.com.padtec.common.reasoning.OntologyReasoner;
import br.com.padtec.common.reasoning.PelletReasonerImpl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class UploadApp {

	/** Reasoner */
	public static OntologyReasoner reasoner;
	
	/** Base Model Repository */
	public static BaseModelRepository baseRepository;
	
	/** Inferred Model Repository */
	public static InferredModelRepository inferredRepository;
	
	/** Temporary Model used to rool back */
	public static OntModel tempModel;
	
	/**
	 * Upload the base model ontology in OWL. The user might opt for not using the reasoner at the upload.
	 * 
	 * @param in: OWL Input Stream
	 * @param useReasoner: Use the reasoner at the uploading 
	 * @param optReasoner: Which reasoner must be used in the uploading
	 * 
	 * @throws InconsistentOntologyException
	 * @throws OKCoExceptionInstanceFormat
	 * @throws IOException
	 * @throws OKCoExceptionNameSpace
	 * @throws OKCoExceptionReasoner
	 * 
	 * @author John Guerson
	 */
	public static void uploadBaseModel(InputStream in, String useReasoner, String optReasoner)
	throws InconsistentOntologyException, OKCoExceptionInstanceFormat, IOException, OKCoExceptionNameSpace, OKCoExceptionReasoner
	{		
		System.out.println("Cloning repositories...");
		/** Upload the base model to a base repository */
		baseRepository = new BaseModelRepositoryImpl();		 
		baseRepository.readBaseOntModel(in);		 		 			  
		if(baseRepository.getNameSpace() == null) throw new OKCoExceptionNameSpace("Please select owl file with defined namespace.");
		
		/** Keep a temporary model for rollbacking the base model */
		tempModel = OntModelAPI.clone(baseRepository.getBaseOntModel());
		
		
		/** Run the inference if required, otherwise the inferred model is a clone of the base model */
		if(useReasoner!=null && useReasoner.equals("on"))
		{
			System.out.println("Running the reasoner");
			if(optReasoner.equals("hermit")) reasoner = new HermitReasonerImpl();				  
			else if(optReasoner.equals("pellet")) reasoner = new PelletReasonerImpl();				  
			else throw new OKCoExceptionReasoner("Please select a reasoner available.");
			 
			System.out.println("Getting infModel");
			InfModel inferredModel = reasoner.run(baseRepository);
			inferredRepository = new InferredModelRepositoryImpl(inferredModel);
		}else{
			System.out.println("Getting infModel");
			InfModel  inferredModel = OntModelAPI.clone(baseRepository.getBaseOntModel());
			inferredRepository = new InferredModelRepositoryImpl(inferredModel);
		}
		
		/** Fulfill the lists with data from the base ontology */
		//System.out.println("Updating lists...");
		OKCoApp.updateLists();
	}
		
	public static BaseModelRepository getBaseRepository() { return baseRepository; }	
	public static OntModel getBaseModel() { return baseRepository.getBaseOntModel(); }	
	public static InferredModelRepository getInferredRepository() { return inferredRepository; }	
	public static InfModel getInferredModel() { return inferredRepository.getInferredOntModel(); }	
	public static boolean isBaseModelUploaded() { return baseRepository.getBaseOntModel()!=null; }	
	public static String getBaseModelAsString() { return baseRepository.getBaseOntModelAsString(); }
	
	/**
	 * Save Base Model to a file.
	 * 
	 * @author John Guerson
	 */
	public static boolean saveBaseModel()
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
	public static void clear()
	{		
		baseRepository.clear();
		inferredRepository.clear();
		tempModel = null;				
		reasoner = null;
	}
	
	/**
	 * Rollback to a valid model, which is stored in the temporary model
	 * 
	 * @author John Guerson
	 */
	public static void rollBack()
	{				
		baseRepository.cloneReplacing(tempModel);
		inferredRepository.cloneReplacing(tempModel);
		try {			
			OKCoApp.updateLists();			
		} catch (InconsistentOntologyException e1) {			
			e1.printStackTrace();			
		} catch (OKCoExceptionInstanceFormat e) {			
			e.printStackTrace();
		}			
	}	
}
