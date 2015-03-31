package br.com.padtec.nopen.model;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class NOpenFactory {

	public static void createOTNTech(OKCoUploader repository)
	{
		String otnURI = repository.getNamespace()+"OTN";		
		String techURI = repository.getNamespace()+ConceptEnum.TECHNOLOGY.toString();
		
		String poukURI = repository.getNamespace()+"POUk";
		String odukURI = repository.getNamespace()+"ODUk";
		String otukURI = repository.getNamespace()+"OTUk";
		String layerURI = repository.getNamespace()+ConceptEnum.LAYER.toString();
		
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), otnURI,techURI);
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), poukURI, layerURI);
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), odukURI, layerURI);
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), otukURI, layerURI);
		
		String techToLayerURI = repository.getNamespace()+RelationEnum.COMPONENTOF3.toString();
				
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),otnURI, techToLayerURI, poukURI);
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),otnURI, techToLayerURI, odukURI);
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),otnURI, techToLayerURI, otukURI);
	}	
	
	public static void createMEFTech(OKCoUploader repository)
	{
		String mefURI = repository.getNamespace()+"MEF";		
		String techURI = repository.getNamespace()+ConceptEnum.TECHNOLOGY.toString();
		
		String menURI = repository.getNamespace()+"MEN";
		String subscribersURI = repository.getNamespace()+"Subscribers";
		String layerURI = repository.getNamespace()+ConceptEnum.LAYER.toString();
		
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), mefURI,techURI);
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), menURI, layerURI);
		FactoryUtil.createInstanceIndividual(repository.getBaseModel(), subscribersURI, layerURI);
		
		String techToLayerURI = repository.getNamespace()+RelationEnum.COMPONENTOF3.toString();
				
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),mefURI, techToLayerURI, menURI);
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),mefURI, techToLayerURI, subscribersURI);
	}	
}
