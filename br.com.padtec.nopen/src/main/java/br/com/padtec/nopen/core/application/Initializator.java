package br.com.padtec.nopen.core.application;

import java.io.IOException;
import java.util.Date;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.advisor.core.util.PerformanceUtil;
import br.com.padtec.common.reasoning.HermitReasonerImpl;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.core.exception.OKCoExceptionNameSpace;
import br.com.padtec.okco.core.exception.OKCoExceptionReasoner;

public class Initializator {
	
	public static String uploadTBOx()
	{
		Date beginDate = new Date();		
		OKCoUploader.reasonOnLoading=true;
		OKCoUploader.reasoner = new HermitReasonerImpl();
		String error = new String();
		try{						
						
			OKCoUploader.uploadBaseModel("src/main/resources/nOpenModel.owl",OKCoUploader.reasonOnLoading ? "on" : "off", (OKCoUploader.reasoner instanceof HermitReasonerImpl) ? "hermit" : "pellet");	
			
		}catch (InconsistentOntologyException e){
			error = "Ontology have inconsistence:" + e.toString() + ". Return the last consistent model state.";						
			OKCoUploader.rollBack(false);			
		}catch (OKCoExceptionInstanceFormat e){			
			error = "Entity format error: " + e.getMessage();			
			OKCoUploader.clear();			
		}catch (IOException e){
			error = "File not found.";			
			OKCoUploader.clear();				
		}catch (OKCoExceptionNameSpace e){			
			error = "File namespace error: " + e.getMessage();			
			OKCoUploader.clear();						
		}catch (OKCoExceptionReasoner e){
			error = "Reasoner error: " + e.getMessage();			
			OKCoUploader.clear();			
		} catch (Exception e){
			error = "Error: "+e.getLocalizedMessage();			
			OKCoUploader.clear();			
		}	 		
		PerformanceUtil.printExecutionTime("TBox uploaded.", beginDate);
		return error;
	}
}
