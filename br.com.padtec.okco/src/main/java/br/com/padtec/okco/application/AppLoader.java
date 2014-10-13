package br.com.padtec.okco.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.common.queries.OntModelAPI;
import br.com.padtec.okco.domain.DtoDefinitionClass;
import br.com.padtec.okco.domain.FactoryInstances;
import br.com.padtec.okco.domain.Instance;
import br.com.padtec.okco.domain.ManagerInstances;
import br.com.padtec.okco.domain.Search;
import br.com.padtec.okco.domain.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.domain.exceptions.OKCoExceptionNameSpace;
import br.com.padtec.okco.domain.exceptions.OKCoExceptionReasoner;
import br.com.padtec.okco.persistence.BaseModelRepository;
import br.com.padtec.okco.persistence.BaseModelRepositoryImpl;
import br.com.padtec.okco.persistence.HermitReasonerImpl;
import br.com.padtec.okco.persistence.InferredModelRepository;
import br.com.padtec.okco.persistence.InferredModelRepositoryImpl;
import br.com.padtec.okco.persistence.OntologyReasoner;
import br.com.padtec.okco.persistence.PelletReasonerImpl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class AppLoader {

	/** Reasoner */
	public static OntologyReasoner reasoner;
	
	/** Base Model Repository */
	public static BaseModelRepository baseRepository;
	
	/** Inferred Model Repository */
	public static InferredModelRepository inferredRepository;
	
	/** Temporary Model used to rool back */
	public static OntModel tempModel;
		
	// Checking the validity of these ones...
	public static Search Search;
	public static FactoryInstances FactoryInstances;
	public static ManagerInstances ManagerInstances;		
	public static ArrayList<Instance> ListAllInstances;
	public static ArrayList<String> ListModifiedInstances;
	public static ArrayList<DtoDefinitionClass> ModelDefinitions;
	
	/**
	 * Upload the base model ontology in OWL. The user might opt for using the reasoner at the uploading.
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
		baseRepository = new BaseModelRepositoryImpl();		 
		baseRepository.readBaseOntModel(in);		 		 			  
		if(baseRepository.getNameSpace() == null) throw new OKCoExceptionNameSpace("Please select owl file with defined namespace.");
		
		tempModel = OntModelAPI.clone(baseRepository.getBaseOntModel());
		
		if(useReasoner!=null && useReasoner.equals("on"))
		{	 
			 if(optReasoner.equals("hermit")) reasoner = new HermitReasonerImpl();				  
			 else if(optReasoner.equals("pellet")) reasoner = new PelletReasonerImpl();				  
			 else throw new OKCoExceptionReasoner("Please select a reasoner available.");
			 
			 InfModel inferredModel = reasoner.run(baseRepository);
			 inferredRepository = new InferredModelRepositoryImpl(inferredModel);
		}else{
			 InfModel  inferredModel = OntModelAPI.clone(baseRepository.getBaseOntModel());
			 inferredRepository = new InferredModelRepositoryImpl(inferredModel);
		}		 
		 
		Search = new Search();
		FactoryInstances = new FactoryInstances();
		ManagerInstances = new ManagerInstances(FactoryInstances);
		  	  
	  	ListModifiedInstances = new ArrayList<String>();

		updateLists();		
	}
	
	public static BaseModelRepository getBaseRepository() { return baseRepository; }
	
	public static OntModel getBaseModel() { return baseRepository.getBaseOntModel(); }
	
	public static InferredModelRepository getInferredRepository() { return inferredRepository; }
	
	public static InfModel getInferredModel() { return inferredRepository.getInferredOntModel(); }
	
	public static boolean isBaseModelUploaded() { return baseRepository.getBaseOntModel()!=null; }
	
	public static String getBaseModelAsString() { return baseRepository.getBaseOntModelAsString(); }
	
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
	
	public static void clear()
	{		
		baseRepository.clear();
		inferredRepository.clear();
		tempModel = null;		
		ListAllInstances = null;
		ListModifiedInstances = null;
		reasoner = null;
	}
	
	public static void rollBack()
	{				
		baseRepository.cloneReplacing(tempModel);
		inferredRepository.cloneReplacing(tempModel);
		try {			
			updateLists();			
		} catch (InconsistentOntologyException e1) {			
			e1.printStackTrace();			
		} catch (OKCoExceptionInstanceFormat e1) {			
			e1.printStackTrace();
		}			
	}
	
	public static void updateLists() throws InconsistentOntologyException, OKCoExceptionInstanceFormat 
	{	
		System.out.println("Updating Lists()...");
		InfModel inferredModel = inferredRepository.getInferredOntModel();
		OntModel Model = baseRepository.getBaseOntModel();
    	// Refresh list of instances	    	
    	ListAllInstances = ManagerInstances.getAllInstances(inferredModel);	    	
    	//Get model definitions on list of instances	    	
	  	ModelDefinitions = Search.GetModelDefinitionsInInstances(ListAllInstances, inferredModel);			
		// Organize data (Update the list of all instances)			
    	ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, ModelDefinitions, Model, inferredModel, baseRepository.getNameSpace());			
		ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, inferredModel, baseRepository.getNameSpace());			
    }
	
	
	public static void updateAddingToLists(String instanceURI) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{							
		System.out.println("Updating and Adding to Lists()...");
		InfModel inferredModel = inferredRepository.getInferredOntModel();
		OntModel Model = baseRepository.getBaseOntModel();
	    //Get model definitions on list of instances	    	
		ArrayList<DtoDefinitionClass> intanceDefinitions = Search.GetModelDefinitionsInInstances(instanceURI, Model, inferredModel, ListAllInstances, ManagerInstances);
		ModelDefinitions.addAll(intanceDefinitions);			
		// Organize data (Update the list of all instances)			
	    ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, intanceDefinitions, Model, inferredModel, baseRepository.getNameSpace());			
		ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, inferredModel, baseRepository.getNameSpace());			
	}
	
	public static void updateModifiedList()
	{
		for (Instance i : ListAllInstances) 
		{
			String s = i.ns + i.name;
			if (ListModifiedInstances.contains(s)) i.setModified(true);			
		}
	}
}