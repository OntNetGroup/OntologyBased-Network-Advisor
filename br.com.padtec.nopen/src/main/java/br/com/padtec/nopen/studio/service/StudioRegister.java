package br.com.padtec.nopen.studio.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.util.NOpenFactoryUtil;

public class StudioRegister {

	public static void registerDefaultTechnologies() throws Exception
	{
		Date beginDate = new Date();		
				
		NOpenFactoryUtil.createMEFTech(StudioComponents.studioRepository);
		NOpenFactoryUtil.createOTNTech(StudioComponents.studioRepository);		
		NOpenFactoryUtil.createServices(StudioComponents.studioRepository);
		
		PerformanceUtil.printExecutionTime("Equip Studio: Technologies, Layers and Services registered.", beginDate);
	}
	
	public static void registerTechnology(String techName) throws Exception
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
