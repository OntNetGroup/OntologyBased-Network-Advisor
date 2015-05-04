package br.com.padtec.nopen.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.okco.core.application.OKCoUploader;

public class InstanceFabricator {
	
	public static void createTechnology(OKCoUploader repository, String techName) throws Exception
	{
		String indURI = repository.getNamespace()+techName;		
		String techURI = repository.getNamespace()+ConceptEnum.Technology.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI,techURI);
		NOpenLog.appendLine(repository.getName()+": Technology "+techName+" created");
	}
	
	public static void deleteTechnology(OKCoUploader repository, String techName) 
	{		
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+techName);
		NOpenLog.appendLine(repository.getName()+": Technology "+techName+" deleted");
	}
	
	public static void createLayer(OKCoUploader repository, String layerName, String techName) throws Exception
	{
		String indURI = repository.getNamespace()+layerName;		
		String layerURI = repository.getNamespace()+ConceptEnum.Layer.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI, layerURI);
		
		String ind2URI = repository.getNamespace()+techName;
		
		String techToLayerURI = repository.getNamespace()+RelationEnum.ComponentOf8_Technology_Layer.toString();
		
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),ind2URI, techToLayerURI, indURI);
		
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" created for Technology "+techName);
	}	

	public static void deleteLayer(OKCoUploader repository, String layerName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+layerName);
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" deleted");
	}
		
	public static void createService(OKCoUploader repository, String serviceName, String layerName, String techName) throws Exception
	{
		String indLayerURI = repository.getNamespace()+layerName;		
		String indServURI = repository.getNamespace()+serviceName;
		String serviceURI = repository.getNamespace()+ConceptEnum.Service.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indServURI, serviceURI);		
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),indLayerURI, RelationEnum.implements_Layer_Service.toString(), indServURI);		
		NOpenLog.appendLine(repository.getName()+": Service "+serviceName+" created for Layer "+layerName+" and Tech "+techName);
	}	
	
	public static void deleteService(OKCoUploader repository, String serviceName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+serviceName);
		NOpenLog.appendLine(repository.getName()+": Service "+serviceName+" deleted");
	}
	
	public static void createCard(OKCoUploader repository, String cardName) throws Exception
	{
		String indURI = repository.getNamespace()+cardName;		
		String cardURI = repository.getNamespace()+ConceptEnum.Card.toString();
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI,cardURI);
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created");		
	}
		
	public static void createEquipmentHolder(String equipHolderId, OKCoUploader repository) throws Exception
	{
		String individualURI = repository.getNamespace()+equipHolderId;
		if(!QueryUtil.individualExists(repository.getBaseModel(), individualURI))
		{
			String classURI = repository.getNamespace()+ConceptEnum.Equipment_Holder.toString();
			FactoryUtil.createInstanceIndividual(repository.getBaseModel(), individualURI, classURI);
			NOpenLog.appendLine(repository.getName()+": Equipment Holder "+equipHolderId+" created");
		}
	}
}
