package br.com.padtec.advisor.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.advisor.application.queries.AdvisorQueryUtil;
import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.rdf.model.InfModel;

public class AdvisorService {

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
	 * Returns all individuals from G800 filtered by a particular equipment
	 * 
	 * @param equipmentName
	 * @return
	 */
	public static List<String> getAllIndividualsFromG800(String equipmentName)
	{		
		String namespace = OKCoUploader.getNamespace();
		
		List<String> result  = AdvisorQueryUtil.getTransportFunctionsURIAtComponentOfRange(namespace+equipmentName);
		
		List<String> outInterfacesList = AdvisorQueryUtil.getOutputInterfacesURIAtComponentOfRange(namespace+equipmentName);
		for (String interface_out : outInterfacesList) 
		{			
			List<String> outputs = AdvisorQueryUtil.getOutputsURIAtMapsOutputRange(interface_out);
			if(outputs.size()>0) result.add(outputs.get(0));
		}
		
		List<String> inpInt = AdvisorQueryUtil.getInputInterfacesURIAtComponentOfRange(namespace+equipmentName);
		for (String interface_inp : inpInt) 
		{
			List<String> inputs = AdvisorQueryUtil.getInputsURIAtMapsInputRange(interface_inp);
			if(inputs.size()>0) result.add(inputs.get(0));
		}
		return result;
	}
	
	/**
	 * Returns a mapping of every individual and their set of classes in the ontology.
	 * 
	 * @param individualURIList: A list of individuals URIs
	 * @return
	 */
	public static HashMap<String, List<String>> getIndividualVSClassesMap(List<String> individualURIList)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();
		
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (String individualURI : individualURIList) 
		{
			List<String> classesFromIndividual= QueryUtil.getClassesURI(inferredModel,individualURI);
			map.put(individualURI, classesFromIndividual);			
		}
		
		return map;
	}
	
	/**
	 * Return all triples from G800 ontology.
	 * 
	 * @return
	 */
	public static List<String[]> getAllG800Triples()
	{					
		List<String[]> result = new ArrayList<String[]>();
		
		List<String> G800individuals = getAllIndividualsFromG800();
		for (String individualURI : G800individuals) 
		{														
			List<String> propertiesURIList = QueryUtil.getPropertiesURI(OKCoUploader.getInferredModel(), individualURI);
			
			for(String propertyURI: propertiesURIList)
			{				
			    String[] triple = new String[3];
			    triple[0]=individualURI;
				triple[1]=propertyURI;				
			    List<String> ranges = QueryUtil.getRangeURIs(OKCoUploader.getInferredModel(), propertyURI);			    
			    if(ranges.size()>0) triple[2] = ranges.get(0);
			    else triple[2] = "";			    
			    result.add(triple);
			}			
		}
		return result;
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
			
	/**
	 * Returns all physical media triples.
	 * 
	 * @return
	 */
	public static ArrayList<String[]> getAllPhysicalMediaTriples()
	{		
		List<String> mediasList = AdvisorQueryUtil.getPhysicalMediasURI();		
		ArrayList<String[]> result = new ArrayList<String[]>();
		for (String pm : mediasList) 
		{
			String[] triple = new String[7];			
			String[] triple_aux = new String[3];			
			triple_aux= getPhysicalMediaTriples("input", pm);			
			if(triple_aux[0]!=null) triple[1]= triple_aux[0].split("#")[1];
			if(triple_aux[1]!=null) triple[0]= triple_aux[1].split("#")[1];
			if(triple_aux[2]!=null) triple[2]= triple_aux[2].split("#")[1];
			triple[3]=pm.split("#")[1];
			triple_aux= getPhysicalMediaTriples("output", pm);
			if(triple_aux[0]!=null) triple[4]= triple_aux[0].split("#")[1];			
			if(triple_aux[1]!=null) triple[5]= triple_aux[1].split("#")[1];
			if(triple_aux[2]!=null)	triple[6]= triple_aux[2].split("#")[1];
			result.add(triple);
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
//	public static String getRPFromInterface(String interfaceURI, Integer type)
//	{
//		RelationEnum relation = RelationEnum.MAPS_OUTPUT;
//		ConceptEnum range=ConceptEnum.OUTPUT;
//		if(type==1)
//		{
//			relation=RelationEnum.MAPS_INPUT;
//			range=ConceptEnum.INPUT;
//		}
//		List<String> rangeList = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(interfaceURI, relation, range);
//		
//		if(rangeList.size()>0)
//		{
//			String portURI= rangeList.get(0);
//			List<String> bindings = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(portURI, RelationEnum.INV_IS_BINDING, ConceptEnum.BINDING);			
//			if(bindings.size()>0)
//			{
//				String bindingURI = bindings.get(0);
//				List<String> boundedRPs = AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(bindingURI, RelationEnum.BINDING_IS_REPRESENTED_BY, ConceptEnum.DIRECTLY_BOUND_REFERENCE_POINT);
//				if(boundedRPs.size()>0)
//				{
//					return boundedRPs.get(0);					
//				}
//			}
//		}
//		return new String();
//	}
	
}
