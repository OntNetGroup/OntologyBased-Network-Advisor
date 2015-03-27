package br.com.padtec.nopen.core.queries;

import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.core.types.ConceptEnum;
import br.com.padtec.nopen.core.types.RelationEnum;
import br.com.padtec.okco.core.application.OKCoUploader;

public class NOpenQueryUtil {

	public static String[] getIndividualsNames(String classURI)
	{		
		List<String> individualsURI = QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), classURI);
		String[] result = new String[individualsURI.size()];
		int i=0;
		for(String s: individualsURI){ result[i] = s.replace(OKCoUploader.getNamespace(), ""); i++; }
		return result;
	}
	
	public static String[] getIndividualsNamesAtObjectPropertyRange(String techName, String opURI, String classURI)
	{		
		List<String> individualsURI = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			OKCoUploader.getInferredModel(), 
			OKCoUploader.getNamespace()+techName, 
			OKCoUploader.getNamespace()+RelationEnum.COMPONENTOF3.toString(), 
			OKCoUploader.getNamespace()+ConceptEnum.LAYER.toString()
		);	
		String[] result = new String[individualsURI.size()];
		int i=0;
		for(String s: individualsURI){ result[i] = s.replace(OKCoUploader.getNamespace(), ""); i++; }
		return result;
	}
	
	public static String[] getTechnologiesNames()
	{
		return getIndividualsNames(OKCoUploader.getNamespace()+ConceptEnum.TECHNOLOGY.toString());		
	}
	
	public static String[] getLayerNames(String techName)
	{
		return  getIndividualsNamesAtObjectPropertyRange(
				techName,
				OKCoUploader.getNamespace()+RelationEnum.COMPONENTOF3.toString(), 
				OKCoUploader.getNamespace()+ConceptEnum.LAYER.toString()
			);					
	}
	
	public static String[][] getLayerNames()
	{
		String[] techs = NOpenQueryUtil.getTechnologiesNames();
		String[][] result = new String[techs.length][];
		int i=0;
		for(String s: techs){
			result[i]= getLayerNames(s);
			i++;
		}
		return result;
	}
}
