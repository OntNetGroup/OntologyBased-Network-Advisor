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

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class ProvisioningFunctionality {
		
	//==============================================
	//Query Module: because it only searches for values in the ontology
	//==============================================
	
	/** 
	 * Returns all individuals from the G800 owl ontology.
	 * 
	 * @return
	 */
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
	
	/**
	 * Returns all the tuples {domain,range} of the "site_connects" relation.
	 *  	
	 * @return
	 */
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
	
	/**
	 * Returns all possible connects as tuples
	 * 
	 * @param rpName: Reference Point Name
	 * @return
	 */
	public static ArrayList<String[]> getPossibleConnectsTuples(String rpName)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();		
		String namespace = OKCoUploader.getNamespace();
		
		ArrayList<String[]> result = new ArrayList<String[]>();
			
		List<String> rpClassesList = QueryUtil.getClassesURI(inferredModel,namespace+rpName);
			
		ArrayList<String> relationsNameList = new ArrayList<String>();		
		ArrayList<String> rangesNameList = new ArrayList<String>();
		
		ArrayList<String> rp_sink = new ArrayList<String>();
		ArrayList<String> rp_so = new ArrayList<String>();

		if(rpClassesList.contains(namespace+ConceptEnum.SOURCE_PM_FEP))
		{
			relationsNameList.add(RelationEnum.INV_BINDING_IS_REPRESENTED_BY.toString());
			rangesNameList.add(" ");			
			relationsNameList.add(RelationEnum.IS_BINDING.toString());
			rangesNameList.add(ConceptEnum.INPUT.toString());			
			relationsNameList.add(RelationEnum.INV_COMPONENTOF.toString());
			rangesNameList.add(" ");			
			relationsNameList.add(RelationEnum.COMPONENTOF.toString());
			rangesNameList.add(ConceptEnum.OUTPUT.toString());			
			relationsNameList.add(RelationEnum.INV_IS_BINDING.toString());
			rangesNameList.add(ConceptEnum.BINDING.toString());				
			relationsNameList.add(RelationEnum.BINDING_IS_REPRESENTED_BY.toString());
			
			rangesNameList.add(ConceptEnum.SINK_PM_FEP.toString());			
			
			rp_sink = QueryUtil.endOfGraphWithRanges(inferredModel, rpName, relationsNameList, rangesNameList);			
			for(int i = 0; i < rp_sink.size(); i++)
			{
				List<String> rpList = AdvisorQueryUtil.getReferencePointsURIAtHasForwarding(namespace+rpName);
				if(!rpList.contains(rp_sink.get(i)))
				{
					String[] tuple = new String[2];
					tuple[0] = rp_sink.get(i);
					tuple[1] = "pm_nc";
					result.add(tuple);
				}
			}			
		}else{
			relationsNameList.add(RelationEnum.INV_BINDING_IS_REPRESENTED_BY.toString());
			rangesNameList.add(" ");
			relationsNameList.add(RelationEnum.IS_BINDING.toString());
			rangesNameList.add(ConceptEnum.INPUT.toString());
			relationsNameList.add(RelationEnum.INV_COMPONENTOF.toString());
			rangesNameList.add(" ");			
			relationsNameList.add(RelationEnum.COMPONENTOF.toString());
			rangesNameList.add(ConceptEnum.OUTPUT.toString());		
			relationsNameList.add(RelationEnum.INV_IS_BINDING.toString());
			rangesNameList.add(ConceptEnum.BINDING.toString());		
			relationsNameList.add(RelationEnum.BINDING_IS_REPRESENTED_BY.toString());
			
			rangesNameList.add(ConceptEnum.REFERENCE_POINT.toString());
			
			rp_so = QueryUtil.endOfGraphWithRanges(inferredModel, rpName, relationsNameList, rangesNameList);
			
			relationsNameList.add(RelationEnum.HAS_FORWARDING.toString());
			rangesNameList.add(ConceptEnum.REFERENCE_POINT.toString());
			relationsNameList.add(RelationEnum.INV_BINDING_IS_REPRESENTED_BY.toString());
			rangesNameList.add(ConceptEnum.BINDING.toString());
			relationsNameList.add(RelationEnum.IS_BINDING.toString());
			rangesNameList.add(ConceptEnum.INPUT.toString());
			relationsNameList.add(RelationEnum.INV_COMPONENTOF.toString());
			rangesNameList.add(" ");
			relationsNameList.add(RelationEnum.COMPONENTOF.toString());
			rangesNameList.add(ConceptEnum.OUTPUT.toString());
			relationsNameList.add(RelationEnum.INV_IS_BINDING.toString());
			rangesNameList.add(ConceptEnum.BINDING.toString());
			relationsNameList.add(RelationEnum.BINDING_IS_REPRESENTED_BY.toString());
			rangesNameList.add(ConceptEnum.REFERENCE_POINT.toString());

			rp_sink = QueryUtil.endOfGraphWithRanges(inferredModel, rpName, relationsNameList, rangesNameList);
				
			for(int i = 0; i < rp_sink.size(); i++)
			{
				List<String> rpList = AdvisorQueryUtil.getReferencePointsURIAtHasForwarding(namespace+rpName);
				if(!rpList.contains(rp_sink.get(i)))
				{
					String[] tuple = new String[2];
					tuple[0]= rp_sink.get(i);
					for(int j = 0; j < rp_so.size(); j++)
					{
						if(rp_so.get(j).equals(namespace+ConceptEnum.SOURCE_PM_FEP)|| rp_so.get(j).equals(namespace+ConceptEnum.SOURCE_PATH_FEP)) tuple[1]="nc";
						else {
							tuple[1]="trail";
							result.add(tuple);
						}
					}
				}
			}
			
		}
		return result;
	}
	
	/**
	 * Returns the reference point from an interface.
	 * 
	 * @param eqInterfaceURI: Equipment Interface URI
	 * @param type:  0 for output and 1 for input
	 * @return
	 */
	public static String getRPFromInterface(String eqInterfaceURI, Integer type)
	{
		RelationEnum relation = RelationEnum.MAPS_OUTPUT;
		ConceptEnum range=ConceptEnum.OUTPUT;
		if(type==1)
		{
			relation=RelationEnum.MAPS_INPUT;
			range=ConceptEnum.INPUT;
		}
		List<String> rangeList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(eqInterfaceURI, relation, range);
		
		if(rangeList.size()>0)
		{
			String portURI= rangeList.get(0);
			List<String> bindings = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(portURI, RelationEnum.INV_IS_BINDING, ConceptEnum.BINDING);			
			if(bindings.size()>0)
			{
				String bindingURI = bindings.get(0);
				List<String> boundedRPs = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(bindingURI, RelationEnum.BINDING_IS_REPRESENTED_BY, ConceptEnum.DIRECTLY_BOUND_REFERENCE_POINT);
				if(boundedRPs.size()>0)
				{
					return boundedRPs.get(0);					
				}
			}
		}
		return new String();
	}
	
	/**
	 * Returns all physical media triples.
	 * 
	 * @param value: "input" or "output"
	 * @param pm:
	 * @return
	 */
	public static String[] getPhysicalMediaTriples(String value, String physicalMediaURI) 
	{		
		String[] result = new String[3];

		if(value.equals("input"))
		{
			List<String> pmediaList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(physicalMediaURI,RelationEnum.COMPONENTOF, ConceptEnum.PHYSICAL_MEDIA_INPUT);
			if(pmediaList.size()==0) return result;			
			String portURI= pmediaList.get(0);
			result[0]=portURI;
			
			List<String> terminationList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(portURI, RelationEnum.INV_BINDS, ConceptEnum.TERMINATION_SOURCE_OUTPUT);
			if(terminationList.size()==0) return result;			
			String tfURI= terminationList.get(0);			
			List<String> outputList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(tfURI, RelationEnum.INV_MAPS_OUTPUT, ConceptEnum.OUTPUT_INTERFACE);
			if(outputList.size()==0) return result;			
			String outputURI= outputList.get(0);
			result[1]=outputURI;
			
			List<String> eqList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(outputURI, RelationEnum.INV_COMPONENTOF, ConceptEnum.EQUIPMENT);
			if(eqList.size()==0) return result;			
			result[2]= eqList.get(0);
			
		}else{
			List<String> pmediaList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(physicalMediaURI, RelationEnum.COMPONENTOF, ConceptEnum.PHYSICAL_MEDIA_OUTPUT);
			if(pmediaList.size()==0) return result;
			String portURI= pmediaList.get(0);
			result[0]=portURI;
			
			List<String> terminationList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(portURI, RelationEnum.BINDS, ConceptEnum.TERMINATION_SINK_INPUT);
			if(terminationList.size()==0) return result;
			String tfURI= terminationList.get(0);
			List<String> inputList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(tfURI, RelationEnum.INV_MAPS_INPUT, ConceptEnum.INPUT_INTERFACE);
			if(inputList.size()==0) return result;			
			String inputURI= inputList.get(0);
			result[1]=inputURI;
			
			List<String> eqList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(inputURI, RelationEnum.INV_COMPONENTOF, ConceptEnum.EQUIPMENT);
			if(eqList.size()==0) return result;
			result[2]= eqList.get(0);			
		}
		return result ;
	} 
			
	//==============================================
	//Factory Module: because it modifies the ontology
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
