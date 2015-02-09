package br.com.padtec.advisor.application.queries;

import java.util.List;

import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class AdvisorQueryUtil {

	/** ====================== Query Individual URIs ================================= */
	
	public static List<String> getSitesURI()
	{
		return QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+ConceptEnum.SITE);
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
	
	/** ====================== Query Range Individual URIs At ================================= */
	
	public static List<String> getRangeInputsURIAtMapsInput(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.MAPS_INPUT, OKCoUploader.getNamespace()+ConceptEnum.INPUT);	
	}	 
	
	public static List<String> getRangeOutputsURIAtMapsOutput(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.MAPS_OUTPUT, OKCoUploader.getNamespace()+ConceptEnum.OUTPUT);
	}
	
	public static List<String> getRangeInputsURIAtBinds(String domainIndividualURI)
	{
		 return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.BINDS, OKCoUploader.getNamespace()+ConceptEnum.INPUT);
	}
	
	public static List<String> getRangeSitesURIAtSiteConnects(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.SITE_CONNECTS, OKCoUploader.getNamespace()+ConceptEnum.SITE);
	}
	
	public static List<String> getRangeEquipmentsURIAtHasEquipament(String domainIndividualURI)
	{
		return QueryUtil.getIndividualsURIAtObjectPropertyRange(OKCoUploader.getInferredModel(), domainIndividualURI, OKCoUploader.getNamespace()+RelationEnum.HAS_EQUIPMENT, OKCoUploader.getNamespace()+ConceptEnum.EQUIPMENT);
	}
}
