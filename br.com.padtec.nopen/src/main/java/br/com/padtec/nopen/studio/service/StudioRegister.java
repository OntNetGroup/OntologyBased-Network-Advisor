package br.com.padtec.nopen.studio.service;

import br.com.padtec.nopen.service.util.NOpenFactoryUtil;

public class StudioRegister {

	public static void registerTechnology(String techName)
	{
		NOpenFactoryUtil.createTechnology(StudioComponents.studioRepository, techName);
	}
	
	public static void registerLayer(String layerName, String techName) throws Exception
	{
		NOpenFactoryUtil.createLayer(StudioComponents.studioRepository,layerName, techName);
	}	
	
	public static void unregisterLayer(String layerName)
	{
		NOpenFactoryUtil.deleteLayer(StudioComponents.studioRepository, layerName);
	}
	
	public static void unregisterTechnology(String techName)
	{
		NOpenFactoryUtil.deleteTechnology(StudioComponents.studioRepository, techName);
	}
}
