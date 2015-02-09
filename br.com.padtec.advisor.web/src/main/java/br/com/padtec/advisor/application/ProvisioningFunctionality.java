package br.com.padtec.advisor.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.advisor.application.queries.AdvisorQueryUtil;
import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.rdf.model.InfModel;

public class ProvisioningFunctionality {
		
	/** Returns all the tuples {domain,range} of the "site_connects" relation. */	
	public static List<String[]> getSiteConnectsTuples()
	{
		List<String[]> result = new ArrayList<String[]>();
		
		List<String> sitesList = AdvisorQueryUtil.getSitesURI();
		for (String siteURI : sitesList) 
		{
			List<String> sitesAtRange = AdvisorQueryUtil.getSitesURIAtSiteConnectsRange(siteURI);
			if(!sitesAtRange.isEmpty())
			{				
				for (String rangeURI : sitesAtRange) 
				{
					String[] tuple = new String[2];
					tuple[0]=siteURI;
					tuple[1]=rangeURI;
					result.add(tuple);
				}
			}
		}
		return result;	
	}
	
	/** Returns all individuals from the G800 owl ontology */
	public static List<String> getAllIndividualsFromG800()
	{	
		InfModel inferredModel = OKCoUploader.getInferredModel();
		String namespace = OKCoUploader.getNamespace();
		
		List<String> allIndividuals = QueryUtil.getIndividualsURIFromAllClasses(inferredModel);
		
		/** Remove classes from query result */
		ArrayList<String> classesList = new ArrayList<String>();
		for (String individualURI : allIndividuals) 
		{
			List<String> classesFromIndividual= QueryUtil.getClassesURI(inferredModel,individualURI);
			if((classesFromIndividual.contains(namespace+ConceptEnum.INPUT_INTERFACE) || classesFromIndividual.contains(namespace+ConceptEnum.OUTPUT_INTERFACE) ||
				classesFromIndividual.contains(namespace+ConceptEnum.SITE) || classesFromIndividual.contains(namespace+ConceptEnum.EQUIPMENT)))
			{				
				classesList.add(individualURI);
			}
		}
		allIndividuals.removeAll(classesList);
		
		return allIndividuals;
	}
	
	/** Infer interface connections in the ontology */
	public static void inferInterfaceConnections()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		/** Input Interfaces */
		List<String> inInterfacesList = AdvisorQueryUtil.getInputInterfacesURI();
		for (String interfaceURI: inInterfacesList) 
		{
			List<String> inputs = AdvisorQueryUtil.getInputsURIAtMapsInputRange(interfaceURI);
			if(inputs.size()>0) map.put(inputs.get(0), interfaceURI);			
		}
		
		/** Output Interfaces */
		List<String> outInterfacesList  = AdvisorQueryUtil.getOutputInterfacesURI();		
		for (String interfaceURI : outInterfacesList) 
		{
			List<String> outputs = AdvisorQueryUtil.getOutputsURIAtMapsOutputRange(interfaceURI);
			if(outputs.size()>0) map.put(outputs.get(0), interfaceURI);			
		}

		/** Outputs... */
		List<String> outputsList = AdvisorQueryUtil.getOutputsURI();
		for (String outputURI : outputsList) 
		{
			List<String> inputsURIList  = AdvisorQueryUtil.getInputsURIAtBindsRange(outputURI);
			if(inputsURIList.size()>0)
			{
				String interfac_input= map.get(inputsURIList.get(0));
				String interfac_output= map.get(outputURI);
				
				/** Create Statement OUTPUT_INTERFACE -> INTERFACE_BINDS -> INPUT_INTERFACE */
				FactoryUtil.createStatement(
					OKCoUploader.getBaseModel(), 
					interfac_output, OKCoUploader.getNamespace()+RelationEnum.INTERFACE_BINDS, interfac_input
				);				
			}
		}
		
		/** Substitute InferredModel for the Base Model. The changes were made at the base model! */
		OKCoUploader.substituteInferredModelFromBaseModel(false);
	}
}
