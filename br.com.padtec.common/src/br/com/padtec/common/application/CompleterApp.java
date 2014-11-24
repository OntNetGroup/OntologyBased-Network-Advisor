package br.com.padtec.common.application;

import java.util.ArrayList;
import java.util.List;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.common.queries.DtoQueryUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;


public class CompleterApp {
		
	public static ManagerInstances ManagerInstances= new ManagerInstances();
	
	public static List<DtoInstance> ListAllInstances;	
	public static List<DtoDefinitionClass> ModelDefinitions;
	public static List<String> ListModifiedInstances = new ArrayList<String>();
	
	public static void clear() 
	{
		ListModifiedInstances.clear();
		ListAllInstances=null;	
	}
		
	public static void updateModifiedList()
	{
		for (DtoInstance i : ListAllInstances) 
		{
			String s = i.ns + i.name;
			if (ListModifiedInstances.contains(s)) i.setModified(true);			
		}
	}
	
	//Check the validity of this method
	public static void updateLists() throws InconsistentOntologyException, OKCoExceptionInstanceFormat 
	{	
		System.out.println("Updating Lists()...");
		InfModel inferredModel = UploadApp.getInferredModel();
		OntModel Model = UploadApp.getBaseModel();
    	// Refresh list of instances
    	ListAllInstances = DtoQueryUtil.getIndividuals(inferredModel);
    	//Get model definitions on list of instances	    	
	  	ModelDefinitions = DtoQueryUtil.getClassDefinitions(inferredModel);			
		// Organize data (Update the list of all instances)			
    	ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, ModelDefinitions, Model, inferredModel, UploadApp.getBaseRepository().getNameSpace());			
		ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, inferredModel, UploadApp.getBaseRepository().getNameSpace());			
    }
	
	//Check the validity of this method
	public static void updateAddingToLists(String instanceURI) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{							
		System.out.println("Updating and Adding to Lists()...");
		InfModel inferredModel = UploadApp.getInferredModel();
		OntModel Model = UploadApp.getBaseModel();
	    //Get model definitions on list of instances	    	
		List<DtoDefinitionClass> intanceDefinitions = DtoQueryUtil.getClassDefinitions(inferredModel, instanceURI);
		ModelDefinitions.addAll(intanceDefinitions);			
		// Organize data (Update the list of all instances)			
		    ManagerInstances.UpdateInstanceAndRelations(ListAllInstances, intanceDefinitions, Model, inferredModel, UploadApp.getBaseRepository().getNameSpace());			
			ManagerInstances.UpdateInstanceSpecialization(ListAllInstances, Model, inferredModel, UploadApp.getBaseRepository().getNameSpace());			
		}
}
