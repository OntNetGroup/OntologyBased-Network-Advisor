package br.com.padtec.nopen.service.util;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.okco.core.application.OKCoUploader;

public class NOpenFactoryUtil {
	
	public static void createTechnology(OKCoUploader repository, String techName) throws Exception
	{
		String indURI = repository.getNamespace()+techName;		
		String techURI = repository.getNamespace()+ConceptEnum.Technology.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI,techURI);
	}
	
	public static void deleteTechnology(OKCoUploader repository, String techName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+techName);	
	}
	
	public static void createLayer(OKCoUploader repository, String layerName, String techName) throws Exception
	{
		String indURI = repository.getNamespace()+layerName;		
		String layerURI = repository.getNamespace()+ConceptEnum.Layer.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI, layerURI);
		
		String ind2URI = repository.getNamespace()+techName;
		
		String techToLayerURI = repository.getNamespace()+RelationEnum.ComponentOf8_Technology_Layer.toString();
		
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),ind2URI, techToLayerURI, indURI);
	}	

	public static void deleteLayer(OKCoUploader repository, String layerName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+layerName);		
	}
		
	public static void createService(OKCoUploader repository, String serviceName, String layerName, String techName) throws Exception
	{
		//String indLayerURI = repository.getNamespace()+layerName;		
		//String indTechURI = repository.getNamespace()+techName;
		
		String indServURI = repository.getNamespace()+serviceName;
		String serviceURI = repository.getNamespace()+ConceptEnum.Service.toString();		
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indServURI, serviceURI);		
	}	
	
	public static void deleteService(OKCoUploader repository, String serviceName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+serviceName);		
	}
	
}
