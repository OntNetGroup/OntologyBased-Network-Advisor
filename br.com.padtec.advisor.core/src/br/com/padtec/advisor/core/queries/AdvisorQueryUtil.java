package br.com.padtec.advisor.core.queries;

import java.util.List;

import br.com.padtec.advisor.core.types.ConceptEnum;
import br.com.padtec.advisor.core.types.RelationEnum;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoComponents;

public class AdvisorQueryUtil {

	/** ====================== General ================================= */
	
	public static List<String> getIndividualsURI(ConceptEnum concept)
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+concept);
	}
	
	public static List<String> getIndividualsURIAtObjectPropertyRange(String domainIndividualURI, RelationEnum relation, ConceptEnum rangeConcept)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+relation, OKCoComponents.repository.getNamespace()+rangeConcept);	
	}	
			
	public static List<String> getIndividualsURIAtObjectPropertyDomain(ConceptEnum domainConcept, RelationEnum relation, String rangeIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyDomain(OKCoComponents.repository.getInferredModel(), rangeIndividualURI, OKCoComponents.repository.getNamespace()+relation, OKCoComponents.repository.getNamespace()+domainConcept);	
	}	
	
	/** ====================== Specific ================================= */
	
	public static List<String> getSitesURI()
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+ConceptEnum.SITE);
	}
	
	public static List<String> getReferencePointsURI()
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+ConceptEnum.REFERENCE_POINT);
	}
	
	public static List<String> getEquipmentsURI()
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+ConceptEnum.EQUIPMENT);
	}
	
	public static List<String> getInputInterfacesURI()
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+ConceptEnum.INPUT_INTERFACE);
	}
	
	public static List<String> getOutputInterfacesURI()
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+ConceptEnum.OUTPUT_INTERFACE);
	}
	
	public static List<String> getOutputsURI()
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+ConceptEnum.OUTPUT);
	}
	
	public static List<String> getInputsURI()
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+ConceptEnum.INPUT);
	}
	
	public static List<String> getPhysicalMediasURI()
	{
		return QueryUtil.getIndividualsURI(OKCoComponents.repository.getInferredModel(), OKCoComponents.repository.getNamespace()+ConceptEnum.PHYSICAL_MEDIA);
	}
	
	public static List<String> getInputsURIAtMapsInputRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.MAPS_INPUT, OKCoComponents.repository.getNamespace()+ConceptEnum.INPUT);	
	}	
	
	public static List<String> getOutputInterfacesURIAtInvMapsInputRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.INV_MAPS_INPUT, OKCoComponents.repository.getNamespace()+ConceptEnum.OUTPUT_INTERFACE);	
	}	
	
	public static List<String> getOutputsURIAtMapsOutputRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.MAPS_OUTPUT, OKCoComponents.repository.getNamespace()+ConceptEnum.OUTPUT);
	}
	
	public static List<String> getInputsURIAtBindsRange(String domainIndividualURI)
	{
		 return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.BINDS, OKCoComponents.repository.getNamespace()+ConceptEnum.INPUT);
	}
	
	public static List<String> getSitesURIAtSiteConnectsRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.SITE_CONNECTS, OKCoComponents.repository.getNamespace()+ConceptEnum.SITE);
	}
	
	public static List<String> getEquipmentsURIAtHasEquipamentRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.HAS_EQUIPMENT, OKCoComponents.repository.getNamespace()+ConceptEnum.EQUIPMENT);
	}
	
	public static List<String> getTransportFunctionsURIAtComponentOfRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.COMPONENTOF, OKCoComponents.repository.getNamespace()+ConceptEnum.TRANSPORT_FUNCTION);
	}
	
	public static List<String> getOutputInterfacesURIAtComponentOfRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.COMPONENTOF, OKCoComponents.repository.getNamespace()+ConceptEnum.OUTPUT_INTERFACE);
	} 
		
	public static List<String> getInputInterfacesURIAtComponentOfRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.COMPONENTOF, OKCoComponents.repository.getNamespace()+ConceptEnum.INPUT_INTERFACE);
	} 
	
	public static List<String> getInputInterfacesURIAtInterfaceBindsRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.INTERFACE_BINDS, OKCoComponents.repository.getNamespace()+ConceptEnum.INPUT_INTERFACE);
	}
	
	public static List<String> getReferencePointsURIAtHasForwarding(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoComponents.repository.getInferredModel(), domainIndividualURI, OKCoComponents.repository.getNamespace()+RelationEnum.HAS_FORWARDING, OKCoComponents.repository.getNamespace()+ConceptEnum.REFERENCE_POINT);
	}	
}
