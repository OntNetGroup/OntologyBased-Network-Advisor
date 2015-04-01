package br.com.padtec.nopen.service.util;

import java.io.IOException;
import java.io.InputStream;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.common.reasoning.HermitReasonerImpl;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.core.exception.OKCoExceptionNameSpace;
import br.com.padtec.okco.core.exception.OKCoExceptionReasoner;

public class NOpenUtilities {

	public static String uploadTBOx(InputStream s, boolean runReasoner, OKCoUploader repository)
	{				
		repository.setIsReasonOnLoading(runReasoner);
		repository.setReasoner(new HermitReasonerImpl());
		String error = new String();
		try{						
			repository.uploadBaseModel(s,repository.isReasonOnLoading() ? "on" : "off", (repository.getReasoner() instanceof HermitReasonerImpl) ? "hermit" : "pellet");			
			
		}catch (InconsistentOntologyException e){
			error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";						
			repository.rollBack(false);			
		}catch (OKCoExceptionInstanceFormat e){			
			error = "Entity format error: " + e.getMessage();			
			repository.clear();			
		}catch (IOException e){
			error = "File not found.";			
			repository.clear();				
		}catch (OKCoExceptionNameSpace e){			
			error = "File namespace error: " + e.getMessage();			
			repository.clear();						
		}catch (OKCoExceptionReasoner e){
			error = "Reasoner error: " + e.getMessage();			
			repository.clear();			
		} catch (Exception e){
			error = "Error: "+e.getLocalizedMessage();			
			repository.clear();			
		}		
		return error;
	}	

}
