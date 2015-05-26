package br.com.padtec.nopen.studio.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.model.DefaultRegister;
import br.com.padtec.nopen.model.InstanceFabricator;

public class StudioRegister extends DefaultRegister {

	public static void registerDefaultTechnologies() throws Exception
	{
		Date beginDate = new Date();		
				
		int i=0;
		for(String[] layers: defaultLayers)
		{
			registerTechnology(defaultTechs[i], false);
			int j=0;
			for(String l: layers)
			{
				registerLayer(l, defaultTechs[i], false);
				
				if(j>0) InstanceFabricator.createIsClientOf(StudioComponents.studioRepository, l, layers[j-1]);
				
				for(String service: defaultServices)
				{					
					InstanceFabricator.createService(StudioComponents.studioRepository, service, l, defaultTechs[i]);	
				}
				j++;
			}
			i++;
		}
					
		PerformanceUtil.printExecutionTime("Equip Studio: Technologies, Layers and Services registered.", beginDate);
	}
	
	public static void registerTechnology(String techName, boolean validateTech) throws Exception
	{
		/** validation */
		if (validateTech) validateTechnology(techName);
		
		InstanceFabricator.createTechnology(StudioComponents.studioRepository, techName);
	}
	
	public static void registerLayer(String layerName, String techName, boolean validateLayer) throws Exception
	{
		/** validation */
		if (validateLayer) validateLayer(layerName);
		
		InstanceFabricator.createLayer(StudioComponents.studioRepository,layerName, techName);
	}	
	
	public static void unregisterLayer(String layerName) throws Exception
	{
		InstanceFabricator.deleteLayer(StudioComponents.studioRepository, layerName);
	}
	
	public static void unregisterTechnology(String techName) throws Exception
	{
		InstanceFabricator.deleteTechnology(StudioComponents.studioRepository, techName);
	}
}
