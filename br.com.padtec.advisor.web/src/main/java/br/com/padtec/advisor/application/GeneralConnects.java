package br.com.padtec.advisor.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.advisor.application.queries.AdvisorQueryUtil;
import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class GeneralConnects extends AdvisorService {

	//==============================================
	//Factory Methods: because it modifies the ontology
	//==============================================
	
	/**
	 * Connects.
	 * 
	 * @param rpName: Reference Point Name
	 * @param rp2Name: Reference Point Name
	 * @param type: "pm_nc", "nc" or other
	 */
	public static void connects(String rpName, String rp2Name, String type) 
	{
		/** Substitute InferredModel for the Base Model. */
		OKCoUploader.substituteInferredModelFromBaseModel(false);
		
		OntModel baseModel = OKCoUploader.getBaseModel();
		String namespace = OKCoUploader.getNamespace();
				
		ArrayList<Statement> result = new ArrayList<Statement>();		
		
		if(type.equals("pm_nc"))
		{			
			Individual forwarding = baseModel.createIndividual(namespace+rpName+"_fw_"+rp2Name, baseModel.getResource(namespace+ConceptEnum.PM_NC_FORWARDING));
			Individual te = baseModel.createIndividual(namespace+rpName+"_ate_"+rp2Name, baseModel.getResource(namespace+ConceptEnum.UNIDIRECTIONAL_PM_NC));
			
			result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.IS_REPRESENTED_BY_UNI_ACCESS_TRANSPORT_ENTITY), te));
			result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.FORWARDING_FROM_UNI_PM_NC), baseModel.getIndividual(namespace+rpName)));
			result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.FORWARDING_TO_UNI_PM_NC), baseModel.getIndividual(namespace+rp2Name)));	
			
		}else{
			if(type.equals("nc"))
			{
				Individual forwarding = baseModel.createIndividual(namespace+rpName+"_fw_"+rp2Name, baseModel.getResource(namespace+ConceptEnum.PATH_NC_FORWARDING));
				Individual te = baseModel.createIndividual(namespace+rpName+"_ate_"+rp2Name, baseModel.getResource(namespace+ConceptEnum.UNIDIRECTIONAL_PATH_NC));
				
				result = new ArrayList<Statement>();
				
				result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.IS_REPRESENTED_BY_UNI_PATH_NC), te));
				result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.FORWARDING_FROM_UNI_PATH_NC), baseModel.getIndividual(namespace+rpName)));
				result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.FORWARDING_TO_UNI_PATH_NC), baseModel.getIndividual(namespace+rp2Name)));	
			}else{
				Individual forwarding = baseModel.createIndividual(namespace+rpName+"_fw_"+rp2Name,baseModel.getResource(namespace+ConceptEnum.AP_FORWARDING));
				Individual te = baseModel.createIndividual(namespace+rpName+"_ate_"+rp2Name,baseModel.getResource(namespace+ConceptEnum.UNIDIRECTIONAL_ACCESS_TRANSPORT_ENTITY));
				
				result = new ArrayList<Statement>();
				
				result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.IS_REPRESENTED_BY_UNI_ACCESS_TRANSPORT_ENTITY), te));
				result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.FORWARDING_FROM_UNI_ACCESS_TRANSPORT_ENTITY), baseModel.getIndividual(namespace+rpName)));
				result.add(baseModel.createStatement(forwarding, baseModel.getProperty(namespace+RelationEnum.FORWARDING_TO_UNI_ACCESS_TRANSPORT_ENTITY), baseModel.getIndividual(namespace+rp2Name)));					
			}			
		}
		result.add(baseModel.createStatement(baseModel.getIndividual(namespace+rpName), baseModel.getProperty(namespace+RelationEnum.HAS_FORWARDING), baseModel.getIndividual(namespace+rp2Name)));
		baseModel.add(result);
		
		/** Substitute InferredModel for the Base Model. The changes were made at the base model! */
		OKCoUploader.substituteInferredModelFromBaseModel(false);
	}
	
	/**
	 * Infer interface connections in the ontology. 
	 */
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
