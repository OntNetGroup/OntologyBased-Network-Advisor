package br.com.padtec.nopen.provisioning.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.model.DefaultRegister;
import br.com.padtec.nopen.model.InstanceFabricator;

public class ProvisioningRegister extends DefaultRegister {

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
				
				if(j>0) InstanceFabricator.createIsClientOf(ProvisioningComponents.provisioningRepository, layers[j-1], l);
				
				for(String service: defaultServices)
				{
					InstanceFabricator.createService(ProvisioningComponents.provisioningRepository, service, l, defaultTechs[i]);
				}
				j++;
			}
			i++;
		}

		PerformanceUtil.printExecutionTime("Provisioning: Technologies, Layers and Services registered.", beginDate);
	}
	
	public static void registerTechnology(String techName, boolean validateTech) throws Exception
	{
		/** validation */
		if (validateTech) validateTechnology(techName);
		
		InstanceFabricator.createTechnology(ProvisioningComponents.provisioningRepository, techName);
	}
	
	public static void registerLayer(String layerName, String techName, boolean validateLayer) throws Exception
	{
		if (validateLayer) validateLayer(layerName);
		
		InstanceFabricator.createLayer(ProvisioningComponents.provisioningRepository,layerName, techName);
	}	
	
	public static void unregisterLayer(String layerName) throws Exception
	{
		InstanceFabricator.deleteLayer(ProvisioningComponents.provisioningRepository, layerName);
	}
	
	public static void unregisterTechnology(String techName) throws Exception
	{
		InstanceFabricator.deleteTechnology(ProvisioningComponents.provisioningRepository, techName);
	}
}
