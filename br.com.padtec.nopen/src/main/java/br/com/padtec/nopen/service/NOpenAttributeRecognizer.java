package br.com.padtec.nopen.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class NOpenAttributeRecognizer {

	/** @author John Guerson */
	@SuppressWarnings("unused")
	private static List<String> getCardLayersIdFromCard(PElement card, List<PLink> links)
	{
		return getCardLayersIdFromCard(card.getId(), links);
	}
	
	/** @author John Guerson */
	private static List<String> getCardLayersIdFromCard(String cardURI, List<PLink> links)
	{
		List<String> result = new ArrayList<String>();
		for(PLink plink: links){
			if((plink.getSourceType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0)&&
			   (plink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0)){
				if(cardURI.compareToIgnoreCase(plink.getTarget())==0) result.add(plink.getSource());
			}
			if((plink.getTargetType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0)&&
			   (plink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0)){
				if(cardURI.compareToIgnoreCase(plink.getSource())==0) result.add(plink.getTarget());
			}
		}
		return result;
	}
	
	/** @author John Guerson */
	private static List<String> getLayerTypesIdFromCard(String cardLayerURI, List<PLink> links)
	{
		List<String> result = new ArrayList<String>();
		for(PLink plink: links){
			if((plink.getSourceType().compareToIgnoreCase(ConceptEnum.Layer_Type.toString())==0)&&
			   (plink.getTargetType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0)){
				if(cardLayerURI.compareToIgnoreCase(plink.getTarget())==0) result.add(plink.getSource());
			}
			if((plink.getTargetType().compareToIgnoreCase(ConceptEnum.Layer_Type.toString())==0)&&
			   (plink.getSourceType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0)){
				if(cardLayerURI.compareToIgnoreCase(plink.getSource())==0) result.add(plink.getTarget());
			}
		}
		return result;
	}
	
	/** @author John Guerson */
	private static List<String> getTTFIdFromCardLayer(String cardLayerId, List<PLink> links)
	{
		List<String> result = new ArrayList<String>();
		for(PLink plink: links){
			if((plink.getSourceType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0)&&
			   (plink.getTargetType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0)){
				if(cardLayerId.compareToIgnoreCase(plink.getTarget())==0) result.add(plink.getSource());
			}
			if((plink.getTargetType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0)&&
			   (plink.getSourceType().compareToIgnoreCase(ConceptEnum.Card_Layer.toString())==0)){
				if(cardLayerId.compareToIgnoreCase(plink.getSource())==0) result.add(plink.getTarget());
			}
		}
		return result;
	}
	
	/** @author John Guerson */
	private static List<String> getMatrixIdFromTTF(String ttfId, List<PLink> links)
	{
		List<String> result = new ArrayList<String>();
		for(PLink plink: links){
			if((plink.getSourceType().compareToIgnoreCase(ConceptEnum.Matrix.toString())==0)&&
			   (plink.getTargetType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0)){
				if(ttfId.compareToIgnoreCase(plink.getTarget())==0) result.add(plink.getSource());
			}
			if((plink.getTargetType().compareToIgnoreCase(ConceptEnum.Matrix.toString())==0)&&
			   (plink.getSourceType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0)){
				if(ttfId.compareToIgnoreCase(plink.getSource())==0) result.add(plink.getTarget());
			}
		}
		return result;
	}
	

	/** @author John Guerson */
	private static List<String> getMatrixIdFromAF(String ttfId, List<PLink> links)
	{
		List<String> result = new ArrayList<String>();
		for(PLink plink: links){
			if((plink.getSourceType().compareToIgnoreCase(ConceptEnum.Matrix.toString())==0)&&
			   (plink.getTargetType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0)){
				if(ttfId.compareToIgnoreCase(plink.getTarget())==0) result.add(plink.getSource());
			}
			if((plink.getTargetType().compareToIgnoreCase(ConceptEnum.Matrix.toString())==0)&&
			   (plink.getSourceType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0)){
				if(ttfId.compareToIgnoreCase(plink.getSource())==0) result.add(plink.getTarget());
			}
		}
		return result;
	}
	
	/** @author John Guerson */
	private static List<String> getAFIdFromCard(String ttfId, List<PLink> links)
	{
		List<String> result = new ArrayList<String>();
		for(PLink plink: links){
			if((plink.getSourceType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0)&&
			   (plink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0)){
				if(ttfId.compareToIgnoreCase(plink.getTarget())==0) result.add(plink.getSource());
			}
			if((plink.getTargetType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0)&&
			   (plink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0)){
				if(ttfId.compareToIgnoreCase(plink.getSource())==0) result.add(plink.getTarget());
			}
		}
		return result;
	}
		
	
	/** @author John Guerson */
	private static List<String> getCardsIdFromSupervisor(String supervisorId, List<PLink> links)
	{
		List<String> result = new ArrayList<String>();
		for(PLink plink: links){
			if((plink.getSourceType().compareToIgnoreCase(ConceptEnum.Card.toString())==0)&&
			   (plink.getTargetType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0)){
				if(supervisorId.compareToIgnoreCase(plink.getTarget())==0) result.add(plink.getSource());
			}
			if((plink.getTargetType().compareToIgnoreCase(ConceptEnum.Card.toString())==0)&&
			   (plink.getSourceType().compareToIgnoreCase(ConceptEnum.Supervisor.toString())==0)){
				if(supervisorId.compareToIgnoreCase(plink.getSource())==0) result.add(plink.getTarget());
			}
		}
		return result;
	}
	
	
	/** @author John Guerson */
	@SuppressWarnings("unused")
	public static List<PElement> getIndividualsOfConcept(List<PElement> elements, ConceptEnum concept)
	{
		List<PElement> result = new ArrayList<PElement>();
		for(PElement pElem: elements){
			if((pElem.getType().compareToIgnoreCase(concept.toString())==0)){
				result.add(pElem);
			}			
		}
		return result;
	}
	
	public static Map<String, Map<String, String>> runFromEquipment(String equipmentId, List<PElement> elements, List<PLink> links) throws IOException
	{
		/** card <-> (tfId <-> json content) */
		Map<String, Map<String, String>> attrMap = new HashMap<String, Map<String, String>>();
		
		/** Supervisors */
		List<PElement> supervisors = getIndividualsOfConcept(elements, ConceptEnum.Supervisor);
		
		for(PElement supervisor: supervisors){
			/** Cards */
			List<String> cards = getCardsIdFromSupervisor(supervisor.getId(),links);				
			for(String cardId: cards)
			{			
				attrMap.put(cardId, runfromCard(cardId,elements,links));
			}	
		}
		
		return attrMap;
	}
	
	/** @author John Guerson 
	 * @throws IOException */
	public static Map<String,String> runfromCard(String cardId, PElement[] pelems, PLink[] plinks) throws IOException
	{
		List<PElement> pelemList = new ArrayList<PElement>();
		List<PLink> plinkList = new ArrayList<PLink>();
		for(PElement e: pelems){ pelemList.add(e); }
		for(PLink l: plinks){ plinkList.add(l); }		
		return runfromCard(cardId, pelemList, plinkList);
	}
	
	/** @author John Guerson 
	 * @throws IOException */
	@SuppressWarnings("unused")
	public static Map<String,String> runfromCard(String cardId, List<PElement> pelems, List<PLink> links) throws IOException
	{
		/** tf <-> json content */
		Map<String,String> attrMap = new HashMap<String,String>();
		
		/** Card Layers*/
		List<String> cardLayers = new ArrayList<String>();
		cardLayers = getCardLayersIdFromCard(cardId,links);
		System.out.println("Card Layers: "+cardLayers);
		
		/** Layer Types */
		List<String> layerTypes = new ArrayList<String>();
		for(String l: cardLayers) { 
			List<PElement> list = getIndividualsOfConcept(pelems, ConceptEnum.Card_Layer); 
			for(PElement e: list) { layerTypes.add(e.getName()); }		
		}
		System.out.println("Layer Types: "+layerTypes);
		
		/** Layer Type Names */
		List<String> layerTypeNames = new ArrayList<String>();
		int i=0;
		for(String cardLayer: cardLayers)
		{			
			String layer = new String();
			String variable = new String();	
			String layerTypeName = layerTypes.get(i);
			if(layerTypeName.compareToIgnoreCase("ODU")==0)  { layer = "ODU"; variable = "k"; }
			if(layerTypeName.compareToIgnoreCase("ODU2e")==0){ layer = "ODU"; variable = "k"; }
			if(layerTypeName.compareToIgnoreCase("ODUk")==0) { layer = "ODU"; variable = "k"; }
			if(layerTypeName.compareToIgnoreCase("ODUkt")==0){ layer = "ODU"; variable = "kt";}			
			if(layerTypeName.compareToIgnoreCase("OTU")==0)  { layer = "OTU"; variable = "k"; }
			if(layerTypeName.compareToIgnoreCase("OTUk")==0) { layer = "OTU"; variable = "k"; }			
			if(layerTypeName.compareToIgnoreCase("OCh")==0)  { layer = "OCh"; }
			if(layerTypeName.compareToIgnoreCase("OChr")==0) { layer = "OCH"; variable = "r"; }
			if(layerTypeName.compareToIgnoreCase("OPS")==0)  { layer = "OPS"; variable = "n"; }
			if(layerTypeName.compareToIgnoreCase("OPSn")==0) { layer = "OPS"; variable = "n"; }
			if(layerTypeName.compareToIgnoreCase("OMS")==0)  { layer = "OMS"; variable = "n"; }				
			if(layerTypeName.compareToIgnoreCase("OMSn")==0) { layer = "OMS"; variable = "n"; }
			if(layerTypeName.compareToIgnoreCase("OMSnp")==0){ layer = "OMS"; variable = "np";}			
			if(layerTypeName.compareToIgnoreCase("OTM")==0)  { layer = "OTM"; variable = "n"; }
			if(layerTypeName.compareToIgnoreCase("OTMn")==0) { layer = "OTM"; variable = "n"; }			
			if(layerTypeName.compareToIgnoreCase("OTS")==0)  { layer = "OTS"; variable = "n"; }
			if(layerTypeName.compareToIgnoreCase("OTSn")==0) { layer = "OTS"; variable = "n"; }
			
			layerTypeNames.add(layer.toLowerCase()+variable.toLowerCase());
		}
		System.out.println("Layer Type Names: "+layerTypeNames);
		
		/**AF*/
		String tpType = new String();
		boolean isClient = false;
		List<String> afs = getAFIdFromCard(cardId, links);
		System.out.println("Afs:"+afs);
		int index=0;
		for(String adId: afs){
			
			/** Matrix */
			List<String> matrizes = getMatrixIdFromAF(adId, links);
			if(matrizes.size()>0) isClient=true;
			
			tpType = "ctp";				
			if(isClient) tpType = "client-"+tpType;
			String jsonFileName = layerTypeNames.get(index)+"-"+tpType.toLowerCase()+"-grouping.json";
			InputStream s = NOpenAttributeRecognizer.class.getResourceAsStream("/attributes/itu-874.1/"+jsonFileName);					
			String content = new String();
			if(s!=null) content = readFile(s);
			System.out.println("AF:"+adId);
			System.out.println("JSON:"+jsonFileName);				
			System.out.println("Content:"+content);
			
			attrMap.put(adId, jsonFileName.replace(".json", ""));
			index++;
		}

		for(String cardLayer: cardLayers)
		{
			/** TTF */
			tpType = new String();
			isClient = false;
			List<String> ttfs = getTTFIdFromCardLayer(cardLayer, links);
			System.out.println("Ttfs:"+ttfs);
			index=0;
			for(String ttfId: ttfs){
				
				/** Matrix */
				List<String> matrizes = getMatrixIdFromTTF(ttfId, links);
				if(matrizes.size()>0) isClient=true;
			
				tpType = "ttp";
				if(isClient) tpType = "client-"+tpType;
				String jsonFileName = layerTypeNames.get(index)+"-"+tpType.toLowerCase()+"-grouping.json";
				InputStream s = NOpenAttributeRecognizer.class.getResourceAsStream("/attributes/itu-874.1/"+jsonFileName);
				String content = new String();
				if(s!=null) content = readFile(s);
				System.out.println("TTF:"+ttfId);
				System.out.println("JSON:"+jsonFileName);				
				System.out.println("Content:"+content);
								
				attrMap.put(ttfId,jsonFileName.replace(".json", ""));			
				index++;
			}
		}
		return attrMap;
	}
	
	 public static String readFile (InputStream s) throws IOException
		{
			String result = new String();
						
			DataInputStream in = new DataInputStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;			
			while ((strLine = br.readLine()) != null)   
			{	
				result += strLine+"\n";
			}
			in.close();		
			return result;
		}
	    
		
	/** @author John Guerson */
	public static Map<String, String> runfromCard(String cardURI, OKCoUploader srcRepository) throws Exception
	{
		/** tf <-> json content */
		Map<String,String> attrMap = new HashMap<String,String>();
		
		/** Card Layers && Layer Types*/
		List<String> cardLayers = new ArrayList<String>();
		List<String> layerTypes = new ArrayList<String>();
		
		cardLayers = NOpenQueryUtil.getCardLayersURIFromCard(srcRepository, cardURI);
		for(String l: cardLayers) { layerTypes.addAll(NOpenQueryUtil.getLayerTypeURIFromCardLayer(srcRepository, l)); }
		
		System.out.println("Card Layers: "+cardLayers);
		System.out.println("Layer Types: "+layerTypes);
		
		String layerTypeName = new String();
		if(layerTypes.size()>0) layerTypeName = layerTypes.get(0).replace(srcRepository.getNamespace(),"");
		System.out.println("Layer Type Name: "+layerTypeName);
		
		for(String cardLayerURI: cardLayers)
		{			
			String layer = new String();
			String variable = new String();
			String tpType = new String();
			boolean isClient = false;
			
			if(layerTypeName.equals("ODU")) { layer = "ODU"; }
			if(layerTypeName.equals("ODU2e")) { layer = "ODU"; }
			if(layerTypeName.equals("ODUk")) { layer = "ODU"; variable = "k"; }
			if(layerTypeName.equals("ODUkt")) { layer = "ODU"; variable = "kt"; }			
			if(layerTypeName.equals("OTU")) { layer = "OTU"; }
			if(layerTypeName.equals("OTUk")) { layer = "OTU"; variable = "k"; }			
			if(layerTypeName.equals("OCh")) { layer = "OCh"; }
			if(layerTypeName.equals("OChr")) { layer = "OCH"; variable = "r";}			
			if(layerTypeName.equals("OMS")) { layer = "OMS"; }				
			if(layerTypeName.equals("OMSn")) { layer = "OMS"; variable = "n"; }
			if(layerTypeName.equals("OMSnp")) { layer = "OMS"; variable = "np"; }			
			if(layerTypeName.equals("OTM")) { layer = "OTM"; }
			if(layerTypeName.equals("OTMn")) { layer = "OTM"; variable = "n"; }			
			if(layerTypeName.equals("OTS")) { layer = "OTS"; }
			if(layerTypeName.equals("OTSn")) { layer = "OTS"; variable = "n"; }
			
			/** TTF */
			List<String> ttfs = NOpenQueryUtil.getTTFURIFromCardLayer(srcRepository, srcRepository.getNamespace()+cardLayerURI);
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
					InputStream s = NOpenAttributeRecognizer.class.getResourceAsStream("/attributes/itu-874.1/"+jsonFileName);
					System.out.println(jsonFileName);
					StringWriter writer = new StringWriter();
					IOUtils.copy(s, writer, "UTF-8");
					
					attrMap.put(afURI, writer.toString());
				}
				
				tpType = "ttp";
				if(isClient) tpType = "client-"+tpType;
				String jsonFileName = layer.toLowerCase()+variable.toLowerCase()+"-"+tpType.toLowerCase()+"-grouping.json";
				InputStream s = NOpenAttributeRecognizer.class.getResourceAsStream("/attributes/itu-874.1/"+jsonFileName);
				System.out.println(jsonFileName);
				StringWriter writer = new StringWriter();
				IOUtils.copy(s, writer, "UTF-8");
				
				attrMap.put(ttfURI,writer.toString());
			}
		}
		return attrMap;
	}
}
