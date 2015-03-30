package br.com.padtec.advisor.core.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.advisor.core.dto.DtoEquipment;
import br.com.padtec.advisor.core.dto.DtoInterfaceOutput;
import br.com.padtec.advisor.core.types.ConceptEnum;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.okco.core.application.OKCoComponents;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

public class AdvisorDtoQueryUtil {

	/** ====================== General ================================= */
	
	public static List<DtoInstance> getIndividualsFromClass(ConceptEnum concept)
	{
		return DtoQueryUtil.getIndividualsFromClass(OKCoComponents.repository.getInferredModel(), concept.toString());		
	}
	
	public static DtoInstance getIndividualByName(String individualURI, Boolean classesEager, Boolean diffFromEager, Boolean sameAsEager)
	{
		return DtoQueryUtil.getIndividualByName(OKCoComponents.repository.getInferredModel(), individualURI, classesEager, diffFromEager, sameAsEager);
	}
	
	public static List<DtoInstanceRelation>  getRelationsFrom(DtoInstance dtoIndividual)
	{
		return DtoQueryUtil.getRelationsFrom(OKCoComponents.repository.getInferredModel(), dtoIndividual);
	}
	
	/** ====================== Specific ================================= */
	
	public static List<DtoEquipment> getDtoEquipmentsFromSite(String siteURI)
	{
		List<String> equipmentsURIList = new ArrayList<String>();
		equipmentsURIList =  AdvisorQueryUtil.getEquipmentsURIAtHasEquipamentRange(siteURI);
		return getDtoEquipments(equipmentsURIList);
	}
	
	public static List<DtoEquipment> getAllDtoEquipments()
	{		
		List<String> equipmentsURIList = AdvisorQueryUtil.getEquipmentsURI();
		return getDtoEquipments(equipmentsURIList);
	}
	
	private static List<DtoEquipment> getDtoEquipments(List<String> equipmentsURIList)
	{
		List<DtoEquipment> result= new ArrayList<DtoEquipment>();
		
		OntModel baseModel = OKCoComponents.repository.getBaseModel();
		String namespace = OKCoComponents.repository.getNamespace();
		
	    /** Initialize a [Input_Interface,Equipment] mapping */
		HashMap<String, String> inputEquipmentMap= new HashMap<String, String>();				
		for (String equipURI: equipmentsURIList) 
		{
			Individual equipIndividual = baseModel.getIndividual(equipURI);
			List<String> inputInterfaceList= AdvisorQueryUtil.getInputInterfacesURIAtComponentOfRange(equipURI);
			for (String inputURI : inputInterfaceList) 
			{
				Individual inputIndividual = baseModel.getIndividual(inputURI);
				inputEquipmentMap.put(inputIndividual.getLocalName(), equipIndividual.getLocalName());
			}
		}
				
		for (String equipURI: equipmentsURIList) 
		{
			Individual equipIndividual= baseModel.getIndividual(equipURI);
			List<String> outputInterfaceList= AdvisorQueryUtil.getOutputInterfacesURIAtComponentOfRange(equipURI);			
			
			/** New DTO Equipment */
			DtoEquipment dtoEquipment = new DtoEquipment(equipIndividual.getLocalName());

			for (String outputURI : outputInterfaceList) 
			{	
				Individual outputIndividual = baseModel.getIndividual(outputURI);
				
				/** Add Interface Output */
				DtoInterfaceOutput dtoInterfaceOutput = new DtoInterfaceOutput();
				dtoInterfaceOutput.setName(outputIndividual.getLocalName());				
				dtoEquipment.addOut(dtoInterfaceOutput);
				
				String inputInterfaceURI = null;
				List<String> inputInterfaceList = AdvisorQueryUtil.getInputInterfacesURIAtInterfaceBindsRange(outputURI);
				if(!inputInterfaceList.isEmpty())
				{
					inputInterfaceURI = inputInterfaceList.get(0);
					if(inputInterfaceURI!=null)
					{
						Individual inputIndividual= baseModel.getIndividual(inputInterfaceURI);
						Individual equipmentIndividual = baseModel.getIndividual(namespace+inputEquipmentMap.get(inputIndividual.getLocalName()));
						
						dtoInterfaceOutput.setConnected(true);
						
						/** Add [Binds, Equipment] to Bind mapping */						
						ArrayList<String> binds = new ArrayList<String>();
						binds.add(outputIndividual.getLocalName());						
						binds.add(inputIndividual.getLocalName());						
						DtoEquipment equip = new DtoEquipment(equipmentIndividual.getLocalName());						
						dtoEquipment.putBinds(binds, equip);
					}
				}
			}
			
			/** Add DTO Equipment */
			result.add(dtoEquipment); 
		}		
		System.out.println(result);
		return result;
	}
}
