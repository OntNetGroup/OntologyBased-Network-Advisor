package br.com.padtec.nopen.model;

import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.studio.service.StudioComponents;

public class NOpenQueries {

	public static String[] getIndividualsNames(String classURI)
	{		
		List<String> individualsURI = QueryUtil.getIndividualsURI(StudioComponents.studioRepository.getInferredModel(), classURI);
		String[] result = new String[individualsURI.size()];
		int i=0;
		for(String s: individualsURI){ result[i] = s.replace(StudioComponents.studioRepository.getNamespace(), ""); i++; }
		return result;
	}
	
	public static String[] getIndividualsNamesAtObjectPropertyRange(String techName, String opURI, String classURI)
	{		
		List<String> individualsURI = QueryUtil.getIndividualsURIAtObjectPropertyRange(
				StudioComponents.studioRepository.getInferredModel(), 
				StudioComponents.studioRepository.getNamespace()+techName, 
				StudioComponents.studioRepository.getNamespace()+RelationEnum.COMPONENTOF3.toString(), 
				StudioComponents.studioRepository.getNamespace()+ConceptEnum.LAYER.toString()
		);	
		String[] result = new String[individualsURI.size()];
		int i=0;
		for(String s: individualsURI){ result[i] = s.replace(StudioComponents.studioRepository.getNamespace(), ""); i++; }
		return result;
	}
	
	public static String[] getTechnologiesNames()
	{
		return getIndividualsNames(StudioComponents.studioRepository.getNamespace()+ConceptEnum.TECHNOLOGY.toString());		
	}
	
	public static String[] getLayerNames(String techName)
	{
		return  getIndividualsNamesAtObjectPropertyRange(
				techName,
				StudioComponents.studioRepository.getNamespace()+RelationEnum.COMPONENTOF3.toString(), 
				StudioComponents.studioRepository.getNamespace()+ConceptEnum.LAYER.toString()
			);					
	}
	
	public static String[][] getLayerNames()
	{
		String[] techs = NOpenQueries.getTechnologiesNames();
		String[][] result = new String[techs.length][];
		int i=0;
		for(String s: techs){
			result[i]= getLayerNames(s);
			i++;
		}
		return result;
	}
}
