package br.com.padtec.advisor.application.queries;

import java.util.List;

import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class AdvisorQueryUtil {

	/** ====================== General Query Individual URIs ================================= */
	
	public static List<String> getIndividualsURI(ConceptEnum concept)
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+concept);
	}
	
	/** ====================== General Individual URIs At Range ================================= */
	
	public static List<String> getIndividualsURIAtObjectPropertyRange(String domainIndividualURI, RelationEnum relation, ConceptEnum rangeConcept)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+relation, OKCoUploader.getNamespace()+rangeConcept);	
	}	
	
	/** ====================== General Individual URIs At Target ================================= */
	
	public static List<String> getIndividualsURIAtObjectPropertyDomain(ConceptEnum domainConcept, RelationEnum relation, String rangeIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyDomain(OKCoUploader.getInferredModel(), rangeIndividualURI, OKCoUploader.getNamespace()+relation, OKCoUploader.getNamespace()+domainConcept);	
	}	
	
	/** ====================== Specific Query Individual URIs ================================= */
	
	public static List<String> getSitesURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.SITE);
	}
	
	public static List<String> getReferencePointsURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.REFERENCE_POINT);
	}
	
	public static List<String> getEquipmentsURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.EQUIPMENT);
	}
	
	public static List<String> getInputInterfacesURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.INPUT_INTERFACE);
	}
	
	public static List<String> getOutputInterfacesURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.OUTPUT_INTERFACE);
	}
	
	public static List<String> getOutputsURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.OUTPUT);
	}
	
	public static List<String> getInputsURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.INPUT);
	}
	
	public static List<String> getPhysicalMediasURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.PHYSICAL_MEDIA);
	}
	
	/** ====================== Specific Query Individual URIs At Range ================================= */
	
	public static List<String> getInputsURIAtMapsInputRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.MAPS_INPUT, OKCoUploader.getNamespace()+ConceptEnum.INPUT);	
	}	
	
	public static List<String> getOutputInterfacesURIAtInvMapsInputRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.INV_MAPS_INPUT, OKCoUploader.getNamespace()+ConceptEnum.OUTPUT_INTERFACE);	
	}	
	
	public static List<String> getOutputsURIAtMapsOutputRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.MAPS_OUTPUT, OKCoUploader.getNamespace()+ConceptEnum.OUTPUT);
	}
	
	public static List<String> getInputsURIAtBindsRange(String domainIndividualURI)
	{
		 return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.BINDS, OKCoUploader.getNamespace()+ConceptEnum.INPUT);
	}
	
	public static List<String> getSitesURIAtSiteConnectsRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.SITE_CONNECTS, OKCoUploader.getNamespace()+ConceptEnum.SITE);
	}
	
	public static List<String> getEquipmentsURIAtHasEquipamentRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.HAS_EQUIPMENT, OKCoUploader.getNamespace()+ConceptEnum.EQUIPMENT);
	}
	
	public static List<String> getTransportFunctionsURIAtComponentOfRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.COMPONENTOF, OKCoUploader.getNamespace()+ConceptEnum.TRANSPORT_FUNCTION);
	}
	
	public static List<String> getOutputInterfacesURIAtComponentOfRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.COMPONENTOF, OKCoUploader.getNamespace()+ConceptEnum.OUTPUT_INTERFACE);
	} 
		
	public static List<String> getInputInterfacesURIAtComponentOfRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.COMPONENTOF, OKCoUploader.getNamespace()+ConceptEnum.INPUT_INTERFACE);
	} 
	
	public static List<String> getInputInterfacesURIAtInterfaceBindsRange(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.INTERFACE_BINDS, OKCoUploader.getNamespace()+ConceptEnum.INPUT_INTERFACE);
	}
	
	public static List<String> getReferencePointsURIAtHasForwarding(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.HAS_FORWARDING, OKCoUploader.getNamespace()+ConceptEnum.REFERENCE_POINT);
	}	
}
