package br.com.padtec.advisor.core.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.advisor.core.queries.AdvisorQueryUtil;
import br.com.padtec.advisor.core.types.ConceptEnum;
import br.com.padtec.advisor.core.types.RelationEnum;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class GeneralConnects extends AdvisorService {

	private static List<String> equipmentsWithRps = new ArrayList<String>();
	private static List<String> connectsBetweenEqsAndRps = new ArrayList<String>();
	private static List<String> connectsBetweenRps = new ArrayList<String>();
	
	public static List<String> getEquipmentsWithRps() 
	{
		return equipmentsWithRps;
	}

	public static List<String> getConnectsBetweenEqsAndRps() 
	{
		return connectsBetweenEqsAndRps;
	}

	public static List<String> getConnectsBetweenRps() 
	{
		return connectsBetweenRps;
	}
	
	public static void clear()
	{
		equipmentsWithRps.clear();
		connectsBetweenEqsAndRps.clear();
		connectsBetweenRps.clear();
	}
	
	//==============================================
	//These are initialization methods because they initialize the values of the lists
	//==============================================

	/** Set Equipments with Reference Points */
	public static void setEquipmentsWithRPs()
	{		
		InfModel inferredModel = OKCoUploader.getInferredModel();
		String namespace = OKCoUploader.getNamespace();
		
		clear();
		
		List<DtoInstance> rpDtoInstanceList = DtoQueryUtil.getIndividualsFromClass(inferredModel, ConceptEnum.REFERENCE_POINT.toString());
		
		for (DtoInstance dtoInstance : rpDtoInstanceList) 
		{
			List<DtoInstanceRelation> rpRelations = DtoQueryUtil.getRelationsFromAndTo(inferredModel, dtoInstance.ns+dtoInstance.name);
			String bindingURI = new String();
			for (DtoInstanceRelation dtoRelation : rpRelations) 
			{
				String propertyName = dtoRelation.Property.replace(namespace, "");
				if(propertyName.equals(RelationEnum.INV_BINDING_IS_REPRESENTED_BY.toString())) bindingURI = dtoRelation.Target;			
				else if(propertyName.equals(RelationEnum.HAS_FORWARDING))
				{
					String cnct = "";
					cnct += dtoInstance.name;
					cnct += "#";
					cnct += dtoRelation.Target.replace(namespace, "");
					connectsBetweenRps.add(cnct);
				}
			}
			
			List<String> equips = new ArrayList<String>();
			if(!bindingURI.isEmpty()) equips = GeneralConnects.getEquipmentFromBinding(bindingURI);
						
			String equipmentWithRp = new String();
			if(equips.size() == 1) equipmentWithRp += equips.get(0).replace(namespace, "");
			else if(equips.size() >= 2)
			{
				for (String eq : equips) 
				{
					String cnct = "";
					cnct += eq.replace(namespace, "");
					cnct += "#";
					cnct += dtoInstance.name;
					
					String cnctInv = "";
					cnctInv += dtoInstance.name;
					cnctInv += "#";
					cnctInv += eq.replace(namespace, "");
					
					if(!connectsBetweenEqsAndRps.contains(cnct) && !connectsBetweenEqsAndRps.contains(cnctInv))
					{
						connectsBetweenEqsAndRps.add(cnct);
					}
				}								
			}
			equipmentWithRp += "#";
			equipmentWithRp += dtoInstance.name;
			
			if(!equipmentsWithRps.contains(equipmentWithRp))
			{
				equipmentsWithRps.add(equipmentWithRp);
			}			
		}		
		return;
	}	
	
	//==============================================
	//These are query methods because they only query values from the ontology
	//==============================================
	
	/** Returns the Equipment from an interface */
	public static String getEquipmentFromInterface(String interfaceURI)
	{		
		InfModel inferredModel = OKCoUploader.getInferredModel();
		String namespace = OKCoUploader.getNamespace();
		
		List<DtoInstanceRelation> portRelations = DtoQueryUtil.getRelationsFromAndTo(inferredModel, interfaceURI);		
		for (DtoInstanceRelation dtoRelation : portRelations) 
		{
			String relationName = dtoRelation.Property.replace(namespace, "");
			if(relationName.equals(RelationEnum.INV_COMPONENTOF.toString())) return dtoRelation.Target;			
		}		
		return new String();
	}
	
	/** Returns the Reference Point from a Binding */
	public static String getRPFromBinding(String bindingURI)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();
		String namespace = OKCoUploader.getNamespace();
		
		List<DtoInstanceRelation> bindingRelations = DtoQueryUtil.getRelationsFromAndTo(inferredModel, bindingURI);		
		for (DtoInstanceRelation dtoRelation : bindingRelations) 
		{
			String intRelName = dtoRelation.Property.replace(namespace, "");
			if(intRelName.equals(RelationEnum.BINDING_IS_REPRESENTED_BY.toString())) return dtoRelation.Target;			
		}		
		return new String();
	}

	/** Returns Next Reference Points from the Transport Function */  
	public static List<String> getNextRPsFromTF(String actualPortName, List<String> nextPortsURIList, Boolean searchToTop)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();
		String namespace = OKCoUploader.getNamespace();	
		
		List<String> nextRps = new ArrayList<String>();
		for (String portURI : nextPortsURIList) 
		{	
			List<String> nextPortClasses = QueryUtil.getClassesURIFromIndividual(inferredModel,portURI);
			List<String> actualPortClasses = QueryUtil.getClassesURIFromIndividual(inferredModel,namespace+actualPortName);
			
			if((nextPortClasses.contains(namespace+ConceptEnum.OUTPUT.toString()) && actualPortClasses.contains(namespace+ConceptEnum.INPUT.toString())) || 
			  (nextPortClasses.contains(namespace+ConceptEnum.INPUT.toString()) && actualPortClasses.contains(namespace+ConceptEnum.OUTPUT.toString())))
			{
				List<DtoInstanceRelation> portRelations = DtoQueryUtil.getRelationsFromAndTo(inferredModel, portURI);
				
				for (DtoInstanceRelation portRel : portRelations) 
				{
					String portRelName = portRel.Property.replace(namespace, "");
					if(portRelName.equals(RelationEnum.INV_IS_BINDING.toString()))
					{
						List<DtoInstanceRelation> bindingRelations =DtoQueryUtil.getRelationsFromAndTo(inferredModel, portRel.Target);
						for (DtoInstanceRelation bindingRel : bindingRelations) 
						{
							String bindingRelName = bindingRel.Property.replace(namespace, "");
							if(bindingRelName.equals(RelationEnum.BINDING_IS_REPRESENTED_BY.toString()))
							{
								nextRps.add(bindingRel.Target);
							}
						}					
					}
				}	
			}		
		}
		return nextRps;
	}
		
	/** Returns the Equipments from a Port */
	public static List<String> getEquipmentFromPort(String bindedPortURI, Boolean searchToTop)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();
		String namespace = OKCoUploader.getNamespace();	
		
		List<String> result = new ArrayList<String>();
				
		List<DtoInstanceRelation> portRelations = DtoQueryUtil.getRelationsFromAndTo(inferredModel, bindedPortURI);
		String outInterfaceURI = new String();
		String inInterfaceURI = new String();
		String TfnURI = new String();
		for (DtoInstanceRelation dtoRelation : portRelations) 
		{
			String relationName = dtoRelation.Property.replace(namespace, "");
			if(relationName.equals(RelationEnum.INV_MAPS_OUTPUT.toString())) outInterfaceURI = dtoRelation.Target;
			else if(relationName.equals(RelationEnum.INV_MAPS_INPUT.toString())) inInterfaceURI = dtoRelation.Target;
			else if(relationName.equals(RelationEnum.INV_COMPONENTOF.toString())) TfnURI = dtoRelation.Target;			
		}
		
		if(!TfnURI.isEmpty() && outInterfaceURI.isEmpty() && inInterfaceURI.isEmpty())
		{			
			List<String> tiposPm=QueryUtil.getClassesURIFromIndividual(inferredModel,TfnURI);
			if(tiposPm.contains(namespace+ConceptEnum.PHYSICAL_MEDIA.toString()))
			{
				result.add(TfnURI);
				return result;
			}			
			ArrayList<String> nextPorts = new ArrayList<String>(); 
			List<DtoInstanceRelation> tfRelations = DtoQueryUtil.getRelationsFromAndTo(inferredModel, TfnURI);
			String eqURI = new String();
			for (DtoInstanceRelation dtoRelation : tfRelations) 
			{
				String RelationName = dtoRelation.Property.replace(namespace, "");
				if(RelationName.equals(RelationEnum.INV_COMPONENTOF.toString()))
				{
					eqURI = dtoRelation.Target;					
					List<String> tiposEq=QueryUtil.getClassesURIFromIndividual(inferredModel,eqURI);
					if(tiposEq.contains(namespace+ConceptEnum.EQUIPMENT.toString()))
					{
						result.add(eqURI);
						return result;					
					}
				}else if(RelationName.equals(RelationEnum.COMPONENTOF.toString()))
				{
					if(!dtoRelation.Target.equals(bindedPortURI))
					{
						List<String> tiposTf=QueryUtil.getClassesURIFromIndividual(inferredModel, dtoRelation.Target);
						if(tiposTf.contains(namespace+ConceptEnum.INPUT.toString()) || tiposTf.contains(namespace+ConceptEnum.OUTPUT.toString()))
						{
							nextPorts.add(dtoRelation.Target);
						}
					}
				}				
			}			
			List<String> nextRps = GeneralConnects.getNextRPsFromTF(bindedPortURI.replace(namespace,""), nextPorts, searchToTop);
			result.addAll(nextRps);			
		}
		else if(!outInterfaceURI.isEmpty())
		{
			result.add(GeneralConnects.getEquipmentFromInterface(outInterfaceURI));
			return result;
		}
		else if(!inInterfaceURI.isEmpty())
		{
			result.add(GeneralConnects.getEquipmentFromInterface(inInterfaceURI));
			return result;
		}		
		return result;
	}
	
	/** Returns the Equipments from a Binding */
	public static List<String> getEquipmentFromBinding(String bindingURI)
	{		
		InfModel inferredModel = OKCoUploader.getInferredModel();
		String namespace = OKCoUploader.getNamespace();	
		
		List<DtoInstanceRelation> bindingRelations = DtoQueryUtil.getRelationsFromAndTo(inferredModel, bindingURI);
		
		String bindedPort1URI = new String();
		String bindedPort2URI = new String();
		for (DtoInstanceRelation dtoRelation : bindingRelations) 
		{
			String propertyName = dtoRelation.Property.replace(namespace, "");
			if(propertyName.equals(RelationEnum.IS_BINDING.toString()))
			{
				if(bindedPort1URI.isEmpty()) bindedPort1URI = dtoRelation.Target;
				else { bindedPort2URI = dtoRelation.Target; break; }				
			}
		}
		
		ArrayList<String> equips = new ArrayList<String>();
		if(!bindedPort1URI.isEmpty())
		{
			List<String> equipsNs = GeneralConnects.getEquipmentFromPort(bindedPort1URI, GeneralConnects.searchEquipmentFromPortToTop(bindedPort1URI));
			equips.addAll(equipsNs);
		}
		if(!bindedPort2URI.isEmpty())
		{		
			List<String> equipsNs = GeneralConnects.getEquipmentFromPort(bindedPort2URI, GeneralConnects.searchEquipmentFromPortToTop(bindedPort2URI));
			for (String eqNs : equipsNs) 
			{
				if(!equips.contains(eqNs)) equips.add(eqNs);				
			}
		}
		return equips;
	}	
	
	/** ? */
	public static Boolean searchEquipmentFromPortToTop(String portURI)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();
		String namespace = OKCoUploader.getNamespace();	
		
		List<String> tiposPort=QueryUtil.getClassesURIFromIndividual(inferredModel,portURI);
		if(tiposPort.contains(namespace+ConceptEnum.OUTPUT.toString())) return true;		
		return false;
	}	
	
	//==============================================
	//These are factory methods because they modify the ontology
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
		//OKCoUploader.substituteInferredModelFromBaseModel(false);
		
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
		//FactoryUtil.createInstanceRelation(baseModel, namespace+rpName,  namespace+RelationEnum.HAS_FORWARDING, namespace+rp2Name);
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
	
	/**
	 * Returns the Reference Point in the layer above.
	 * 
	 * @return the list of Reference Points.
	 */
	public static ArrayList<String> getReferencePointLayerAbove(String rpName, String typeName){
		InfModel inferredModel = OKCoUploader.getInferredModel();		
		
		ArrayList<String> tempResult = new ArrayList<String>();
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> relationsNameList = new ArrayList<String>();
		ArrayList<String> rangesNameList = new ArrayList<String>();
		
		relationsNameList.add(RelationEnum.INV_BINDING_IS_REPRESENTED_BY.toString());
		relationsNameList.add(RelationEnum.IS_BINDING.toString());
		relationsNameList.add(RelationEnum.INV_COMPONENTOF.toString());
		relationsNameList.add(RelationEnum.COMPONENTOF.toString());
		
		rangesNameList.add(" ");
		if(typeName.equalsIgnoreCase("so")){
			rangesNameList.add(ConceptEnum.OUTPUT.toString());
		} else{
			rangesNameList.add(ConceptEnum.INPUT.toString());
		}
		rangesNameList.add(" ");
		if(typeName.equalsIgnoreCase("so")){
			rangesNameList.add(ConceptEnum.INPUT.toString());
		} else{
			rangesNameList.add(ConceptEnum.OUTPUT.toString());
		}
		
		tempResult = QueryUtil.endOfGraphWithRanges(inferredModel, rpName, relationsNameList, rangesNameList);		
		if(tempResult.size() == 1){
			relationsNameList.add(RelationEnum.INV_IS_BINDING.toString());
			relationsNameList.add(RelationEnum.BINDING_IS_REPRESENTED_BY.toString());
			
			rangesNameList.add(" ");
			rangesNameList.add(" ");
			result = QueryUtil.endOfGraphWithRanges(inferredModel, rpName, relationsNameList, rangesNameList);
		}
		return result;
	}

	/**
	 * 
	 * @param model
	 * @return list of tuples, each tuple contains Reference Points that are connected by a has_forwarding relation.
	 */
	public static ArrayList<String[]> getAllHasForwardingRelationships(InfModel model){
		// Create a new query
  		String queryString = 
  		"PREFIX ont: <" + model.getNsPrefixURI("") + "> " +
  		 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
  		+ "SELECT ?var1 ?var2"
  		+ " WHERE { ?var1 rdf:type ont:Connected_Reference_Point . "
		+ "?var2 rdf:type ont:Connected_Reference_Point . "
		+ "?var1 ont:has_forwarding ?var2 . "
		+ "}";
  		
  		Query query = QueryFactory.create(queryString); 
  		
  		// Execute the query and obtain results
  		QueryExecution qe = QueryExecutionFactory.create(query, model);
  		ResultSet results = qe.execSelect();
  		ArrayList<String[]> list = new ArrayList<String[]>();

  		//ResultSetFormatter.out(System.out, results, query);
  		
  		while (results.hasNext()) {
  			QuerySolution row = results.next();
  		    
  		    RDFNode rdfX = row.get("var1");
  		    RDFNode rdfY = row.get("var2");
  		    String[] vec = new String[2];
  		    vec[0] = rdfX.toString();
  		    vec[1] = rdfY.toString();
  	    	list.add(vec);
  		}

		return list;
	}
	
	@SuppressWarnings("unused")
	public static ArrayList<String[]> autoConnect() {
		ArrayList<String[]> has_forwardings = new ArrayList<String[]>();
		ArrayList<String[]> has_forwardings2 = new ArrayList<String[]>();
		ArrayList<String[]> list = new ArrayList<String[]>();
		overloop: //label dos 2 blocos de iteração
		do{ 
			has_forwardings = getAllHasForwardingRelationships(OKCoUploader.getInferredModel());
			for(String[] relationship : has_forwardings){
				ArrayList<String> rplayerabove1 = getReferencePointLayerAbove(relationship[0], "so");
				ArrayList<String> rplayerabove2 = getReferencePointLayerAbove(relationship[1], "sk");
				if((rplayerabove1.size() != 0) && (rplayerabove2.size() != 0)){
					
					//verifico se os RPs encontrados já possuem alguma conexão com outros RPs
					if(!has_forwardings.contains(new String[]{rplayerabove1.get(0), rplayerabove2.get(0)})){
						connects(rplayerabove1.get(0).substring(rplayerabove1.get(0).indexOf("#")+1), rplayerabove2.get(0).substring(rplayerabove2.get(0).indexOf("#")+1), "nc"); 
						String[] str = new String[2];
						str[0] = rplayerabove1.get(0);
						str[1] = rplayerabove2.get(0);
						list.add(str);
						has_forwardings2 = getAllHasForwardingRelationships(OKCoUploader.getInferredModel());
					}
				} 
				else {
					//condição para saida
					break overloop;
				}
				
			}
		} while(!has_forwardings.isEmpty());
		
		return list;
	}
	
	
}