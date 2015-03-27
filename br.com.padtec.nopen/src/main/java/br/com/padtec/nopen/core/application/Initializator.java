package br.com.padtec.nopen.core.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.advisor.core.util.PerformanceUtil;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.reasoning.HermitReasonerImpl;
import br.com.padtec.nopen.core.types.ConceptEnum;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.core.exception.OKCoExceptionNameSpace;
import br.com.padtec.okco.core.exception.OKCoExceptionReasoner;

public class Initializator {
	
	public static String uploadTBOx(InputStream s)
	{
		Date beginDate = new Date();		
		OKCoUploader.reasonOnLoading=true;
		OKCoUploader.reasoner = new HermitReasonerImpl();
		String error = new String();
		try{						
						
			OKCoUploader.uploadBaseModel(s,OKCoUploader.reasonOnLoading ? "on" : "off", (OKCoUploader.reasoner instanceof HermitReasonerImpl) ? "hermit" : "pellet");	
			PerformanceUtil.printExecutionTime("TBox uploaded.", beginDate);
			
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
		return error;
	}
	
	public static void registerDefaultTechnologies()
	{
		Date beginDate = new Date();
		createMEFTech();
		PerformanceUtil.printExecutionTime("MEF Technology registered.", beginDate);		
		createOTNTech();
		PerformanceUtil.printExecutionTime("OTN Technology registered.", beginDate);
	}
	
	private static void createMEFTech()
	{
		String mefURI = OKCoUploader.getNamespace()+"MEF";		
		String techURI = OKCoUploader.getNamespace()+ConceptEnum.TECHNOLOGY.toString();
		
		String menURI = OKCoUploader.getNamespace()+"MEN";
		String subscribersURI = OKCoUploader.getNamespace()+"Subscribers";
		String layerURI = OKCoUploader.getNamespace()+ConceptEnum.LAYER.toString();
		
		FactoryUtil.createInstanceIndividual(OKCoUploader.getBaseModel(), mefURI,techURI);
		FactoryUtil.createInstanceIndividual(OKCoUploader.getBaseModel(), menURI, layerURI);
		FactoryUtil.createInstanceIndividual(OKCoUploader.getBaseModel(), subscribersURI, layerURI);
		
		String techToLayerURI = OKCoUploader.getNamespace()+"ComponentOf3";
				
		FactoryUtil.createInstanceRelation(OKCoUploader.getBaseModel(),mefURI, techToLayerURI, menURI);
		FactoryUtil.createInstanceRelation(OKCoUploader.getBaseModel(),mefURI, techToLayerURI, subscribersURI);
	}
	
	private static void createOTNTech()
	{
		String otnURI = OKCoUploader.getNamespace()+"OTN";		
		String techURI = OKCoUploader.getNamespace()+ConceptEnum.TECHNOLOGY.toString();
		
		String poukURI = OKCoUploader.getNamespace()+"POUk";
		String odukURI = OKCoUploader.getNamespace()+"ODUk";
		String otukURI = OKCoUploader.getNamespace()+"OTUk";
		String layerURI = OKCoUploader.getNamespace()+ConceptEnum.LAYER.toString();
		
		FactoryUtil.createInstanceIndividual(OKCoUploader.getBaseModel(), otnURI,techURI);
		FactoryUtil.createInstanceIndividual(OKCoUploader.getBaseModel(), poukURI, layerURI);
		FactoryUtil.createInstanceIndividual(OKCoUploader.getBaseModel(), odukURI, layerURI);
		FactoryUtil.createInstanceIndividual(OKCoUploader.getBaseModel(), otukURI, layerURI);
		
		String techToLayerURI = OKCoUploader.getNamespace()+"ComponentOf3";
				
		FactoryUtil.createInstanceRelation(OKCoUploader.getBaseModel(),otnURI, techToLayerURI, poukURI);
		FactoryUtil.createInstanceRelation(OKCoUploader.getBaseModel(),otnURI, techToLayerURI, odukURI);
		FactoryUtil.createInstanceRelation(OKCoUploader.getBaseModel(),otnURI, techToLayerURI, otukURI);
	}	
}
