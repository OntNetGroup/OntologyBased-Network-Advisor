package br.com.padtec.nopen.service;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class AttributeRecognizer {

	/** @author John Guerson */
	public static void run(String equipmentURI, OKCoUploader srcRepository) throws Exception
	{
		/** tf <-> json content */
		Map<String,String> attrMap = new HashMap<String,String>();
		
		/** Supervisor */
		String supervisorURI = NOpenQueryUtil.getSupervisorURI(srcRepository, equipmentURI);
		
		/** Cards */
		List<String> cards = NOpenQueryUtil.getCardsURI(srcRepository, supervisorURI);				
		for(String cardURI: cards)
		{			
			/** Card Layers */
			List<String> layers = new ArrayList<String>();
			layers = NOpenQueryUtil.getCardLayersURIFromCard(srcRepository, cardURI);			
			for(String layerURI: layers)
			{			
				String layer = new String();
				String variable = new String();
				String tpType = new String();
				boolean isClient = false;
				
				String layerName = layerURI.replace(srcRepository.getNamespace(),"");
				if(layerName.equals("ODU")) { layer = "ODU"; }
				if(layerName.equals("ODU2e")) { layer = "ODU"; }
				if(layerName.equals("ODUk")) { layer = "ODU"; variable = "k"; }
				if(layerName.equals("ODUkt")) { layer = "ODU"; variable = "kt"; }
				
				if(layerName.equals("OTU")) { layer = "OTU"; }
				if(layerName.equals("OTUk")) { layer = "OTU"; variable = "k"; }
				
				if(layerName.equals("OCh")) { layer = "OCh"; }
				if(layerName.equals("OChr")) { layer = "OCH"; variable = "r";}
				
				if(layerName.equals("OMS")) { layer = "OMS"; }				
				if(layerName.equals("OMSn")) { layer = "OMS"; variable = "n"; }
				if(layerName.equals("OMSnp")) { layer = "OMS"; variable = "np"; }
				
				if(layerName.equals("OTM")) { layer = "OTM"; }
				if(layerName.equals("OTMn")) { layer = "OTM"; variable = "n"; }
				
				if(layerName.equals("OTS")) { layer = "OTS"; }
				if(layerName.equals("OTSn")) { layer = "OTS"; variable = "n"; }
				
				/** TTF */
				List<String> ttfs = NOpenQueryUtil.getTTFURIFromCardLayer(srcRepository, srcRepository.getNamespace()+layerURI);
				for(String ttfURI: ttfs){
						
					/** Matrix */
					List<String> matrizes = NOpenQueryUtil.getMatrixURIFromTTF(srcRepository, ttfURI);
					if(matrizes.size()>0) isClient=true;
					
					/**AF*/
					List<String> afs = NOpenQueryUtil.getAFURIFromTTF(srcRepository, ttfURI);
					for(String afURI: afs){
						
						tpType = "ctp";
						if(isClient) tpType = "client-"+tpType;
						String jsonFileName = layer.toLowerCase()+variable.toLowerCase()+"-"+tpType.toLowerCase()+"-grouping.json";
						InputStream s = AttributeRecognizer.class.getResourceAsStream("/attributes/itu-874.1/"+jsonFileName);
						StringWriter writer = new StringWriter();
						IOUtils.copy(s, writer, "UTF-8");
						
						attrMap.put(afURI, writer.toString());
					}
					
					tpType = "ttp";
					if(isClient) tpType = "client-"+tpType;
					String jsonFileName = layer.toLowerCase()+variable.toLowerCase()+"-"+tpType.toLowerCase()+"-grouping.json";
					InputStream s = AttributeRecognizer.class.getResourceAsStream("/attributes/itu-874.1/"+jsonFileName);
					StringWriter writer = new StringWriter();
					IOUtils.copy(s, writer, "UTF-8");
					
					attrMap.put(ttfURI,writer.toString());
				}
			}
		}		
	}
}
