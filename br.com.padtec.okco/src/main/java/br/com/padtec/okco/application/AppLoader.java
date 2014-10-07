package br.com.padtec.okco.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.okco.domain.DtoDefinitionClass;
import br.com.padtec.okco.domain.FactoryInstances;
import br.com.padtec.okco.domain.HermitReasonerImpl;
import br.com.padtec.okco.domain.Instance;
import br.com.padtec.okco.domain.ManagerInstances;
import br.com.padtec.okco.domain.OntologyReasoner;
import br.com.padtec.okco.domain.PelletReasonerImpl;
import br.com.padtec.okco.domain.Search;
import br.com.padtec.okco.domain.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.okco.domain.exceptions.OKCoExceptionNameSpace;
import br.com.padtec.okco.domain.exceptions.OKCoExceptionReasoner;
import br.com.padtec.okco.persistence.BaseModelRepository;
import br.com.padtec.okco.persistence.BaseModelRepositoryImpl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class AppLoader {

	public static OntologyReasoner reasoner;
	public static BaseModelRepository baseRepository;
	
	public static OntModel Model;	
	public static OntModel tmpModel;	
	public static InfModel InfModel;
	
	public static Search Search;
	public static FactoryInstances FactoryInstances;
	public static ManagerInstances ManagerInstances;		
	public static ArrayList<DtoDefinitionClass> ModelDefinitions;
		
	public static ArrayList<Instance> ListAllInstances;
	public static ArrayList<String> ListModifiedInstances;
		
	public static void uploadBaseModel(InputStream in, String useReasoner, String optReasoner)
	throws InconsistentOntologyException, OKCoExceptionInstanceFormat, IOException, OKCoExceptionNameSpace, OKCoExceptionReasoner
	{
		 //create reasoner
		 boolean reasoning = true;			  
		 if (useReasoner != null)
		 {
			 if(useReasoner.equals("on")) reasoning = true;					
			 else reasoning = false;
			 
			 if(optReasoner.equals("hermit")) reasoner = new HermitReasonerImpl();				  
			 else if(optReasoner.equals("pellet")) reasoner = new PelletReasonerImpl();				  
			 else throw new OKCoExceptionReasoner("Please select a reasoner available.");
		 }
		 
		 baseRepository = new BaseModelRepositoryImpl();
		 baseRepository.readBaseOntModel(in);		 		 			  
		 if(baseRepository.getNameSpace() == null) throw new OKCoExceptionNameSpace("Please select owl file with defined namespace.");			  
			  
		 Model = baseRepository.getBaseOntModel();
		 tmpModel = baseRepository.clone(Model);
		 
		 Search = new Search();
		 FactoryInstances = new FactoryInstances(Search);
		 ManagerInstances = new ManagerInstances(Search, FactoryInstances, Model);
		 			
		 if(reasoning == true) InfModel = reasoner.run(Model);				
		 else InfModel = baseRepository.clone(Model);			 
		  	  
	  	 ListModifiedInstances = new ArrayList<String>();

		 updateLists();		
	}
	
	public static boolean isBaseModelUploaded()
	{
		return baseRepository.getBaseOntModel()!=null;
	}
	
	public static String getBaseModelAsString()
	{
		return baseRepository.getBaseOntModelAsString();
	}
	
	public static boolean saveBaseModel()
	{		
		if(Model != null)
		{			
			baseRepository.saveBaseOntModel("");
			return true;			
		}else{
			return false;
		}
	}
	
	public static void clear()
	{
		Model = null;
		tmpModel = null;
		InfModel = null;
		ListAllInstances = null;
		ListModifiedInstances = null;
		reasoner = null;
	}
	
	public static void rollBack()
	{				
		Model = baseRepository.clone(tmpModel);
		InfModel = baseRepository.clone(Model);
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
    	// Refresh list of instances	    	
    	ListAllInstances = ManagerInstances.getAllInstances(Model, InfModel, baseRepository.getNameSpace());	    	
    	//Get model definitions on list of instances	    	
	  	ModelDefinitions = Search.GetModelDefinitionsInInstances(ListAllInstances, InfModel);			
		// Organize data (Update the list of all instances)			
    	ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, ModelDefinitions, Model, InfModel, baseRepository.getNameSpace());			
		ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, InfModel, baseRepository.getNameSpace());			
    }
	
	public static void updateAddingToLists(String instanceURI) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{							
	    //Get model definitions on list of instances	    	
		ArrayList<DtoDefinitionClass> intanceDefinitions = Search.GetModelDefinitionsInInstances(instanceURI, Model, InfModel, ListAllInstances, ManagerInstances);
		ModelDefinitions.addAll(intanceDefinitions);			
		// Organize data (Update the list of all instances)			
	    ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, intanceDefinitions, Model, InfModel, baseRepository.getNameSpace());			
		ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, InfModel, baseRepository.getNameSpace());			
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
