package br.com.padtec.common.okco.features.test;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.exceptions.OKCoExceptionInstanceFormat;
import br.com.padtec.common.persistence.HermitReasonerImpl;
import br.com.padtec.common.persistence.OntologyReasoner;
import br.com.padtec.common.persistence.PelletReasonerImpl;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.FactoryInstances;
import br.com.padtec.common.util.ManagerInstances;
import br.com.padtec.common.util.UploadApp;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class TestCompleteRelations {

	public static void main(String[] args) {

		OntologyReasoner Reasoner = null;
		String reasonerOption = "PELLET";
		
		String pathOwlFile = "C://Users//fabio_000//Desktop//OntologiasOWL//subProperty.owl";
		String pathOut = "C://Users//fabio_000//Desktop//subPropertyOut.owl";
		
		//Select reasoner
		if(reasonerOption.equals("HERMIT"))
		{
			Reasoner = new HermitReasonerImpl();
			  
		} else if(reasonerOption.equals("PELLET")) {
			
			Reasoner = new PelletReasonerImpl();
			
		} else if(reasonerOption.equals("NONE")) {
			
			Reasoner = null;
			
		} else {
			
			  System.out.println("Selecione reasoner válido");
		}		
		
		InputStream in = FileManager.get().open(pathOwlFile);
		if (in == null) {
			System.out.println("Arquivo não encontrado");
		}
		
		//Create model
		OntModel model = null;
		model = ModelFactory.createOntologyModel();
		
		model.read(in,null);		
		String ns = model.getNsPrefixURI("");		  
		if(ns == null)
		{
			System.out.println("Namespace não definido");
		}
		
	  	FactoryInstances factoryInstance = new FactoryInstances();
	  	ManagerInstances managerInstances = new ManagerInstances(factoryInstance);
	  	
	  	//Call reasoner
	  	InfModel infModel;
	  	if(Reasoner == null)
	  	{
	  		infModel = model;
	  	} else {
	  		infModel = Reasoner.run(model);	
	  	}
	  	
	  	// --------------------------------------- AQUI ---------------------------------- //
	  	
	  	ManagerRelations mRelations = new ManagerRelations(managerInstances);
	  	model = mRelations.EnforceSubRelation(model, infModel, ns);
	  	
	  	// --------------------------------------- AQUI ---------------------------------- //
	  	
	  	OutputStream output = null;
		try {
			output = new FileOutputStream(pathOut);
			model.write(output,"RDF/XML");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

class ManagerRelations {
	
	ManagerInstances manager;
	
	ManagerRelations(ManagerInstances manager)
	{		
		this.manager = manager;
	}
	
	OntModel EnforceSubRelation(OntModel model, InfModel infModel, String NS)
	{			
		ArrayList<DtoInstance> ListAllInstances;
		try {
			
			// Get all instances			
			ListAllInstances = manager.getAllInstances(infModel);			
			for (DtoInstance instance : ListAllInstances) 
			{				
				List<String> sourceInstanceClasses = QueryUtil.getClassesURI(infModel,instance.ns + instance.name);
				
				//Get instance relations
				List<DtoInstanceRelation> dtoInstanceRelations = new ArrayList<DtoInstanceRelation>();
				List<String> propertiesURIList = QueryUtil.getPropertiesURI(UploadApp.getInferredModel(), instance.ns + instance.name);
				for(String propertyURI: propertiesURIList){
					DtoInstanceRelation dtoItem = new DtoInstanceRelation();
				    dtoItem.Property = propertyURI;
				    List<String> ranges = QueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI);
				    if(ranges.size()>0) dtoItem.Target = ranges.get(0);
				    else dtoItem.Target = "";
				    dtoInstanceRelations.add(dtoItem);
				}

				//Relations from instance
				for (DtoInstanceRelation instanceRelation : dtoInstanceRelations) 
				{					
					String property = instanceRelation.Property;
					String targetInstance = instanceRelation.Target;
					List<String> targetInstanceClasses = QueryUtil.getClassesURI(infModel,targetInstance);
					
					//Get domain-range from property
					List<String> domainList = QueryUtil.getDomainURIs(infModel, property);
					List<String> rangeList = QueryUtil.getRangeURIs(infModel, property);
										
					//List auxiliary for take the sub-properties with domain and range
					List<String> subproperties = QueryUtil.getSubPropertiesURI(infModel, property, true, true);
					for (String subproperty : subproperties) 
					{				
						boolean existInDomainsSource = false;
						boolean existInRangeTarget = false;
						List<String> domainSubPropertyList = QueryUtil.getDomainURIs(infModel, subproperty);
						for(String domain: domainSubPropertyList){
							if(sourceInstanceClasses.contains(domain) && ! (domainList.contains(domain)))
							{
								existInDomainsSource = true;	
							}
						}
						List<String> rangeSubPropertyList = QueryUtil.getRangeURIs(infModel, subproperty);
						for(String range: rangeSubPropertyList){							
							if(targetInstanceClasses.contains(range) && ! (rangeList.contains(range))) 
							{
								existInRangeTarget = true;
							}						
						}						
						if(existInDomainsSource == true && existInRangeTarget == true)
						{
							//Check if the sub property domain and range are not the same as super property
							
							
							//relation have domain and range available
							model = manager.CreateRelationProperty(instance.ns + instance.name, subproperty, targetInstance, model);
						}
					}
				}
			}
			
		} catch (OKCoExceptionInstanceFormat e) {

			e.printStackTrace();
		}		
		
		return model;
	}

}
