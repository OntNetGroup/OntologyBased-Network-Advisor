package br.com.padtec.nopen.studio.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.util.NOpenFactoryUtil;

public class StudioRegister {

	public static void registerDefaultTechnologies(String[] defaultTechs, String[][] defaultLayers, String[] defaultServices) throws Exception
	{
		Date beginDate = new Date();		
				
		int i=0;
		for(String[] layers: defaultLayers)
		{
			registerTechnology(defaultTechs[i]);
			for(String l: layers)
			{
				registerLayer(l, defaultTechs[i]);
			}
			i++;
		}
			
		for(String service: defaultServices)
		{
			NOpenFactoryUtil.createService(StudioComponents.studioRepository, service, null, null);	
		}
		
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
	
	public static void unregisterLayer(String layerName) throws Exception
	{
		NOpenFactoryUtil.deleteLayer(StudioComponents.studioRepository, layerName);
	}
	
	public static void unregisterTechnology(String techName) throws Exception
	{
		NOpenFactoryUtil.deleteTechnology(StudioComponents.studioRepository, techName);
	}
}
