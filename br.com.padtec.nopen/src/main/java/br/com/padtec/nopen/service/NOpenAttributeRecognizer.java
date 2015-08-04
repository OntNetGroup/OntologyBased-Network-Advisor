package br.com.padtec.nopen.service;

import java.io.IOException;
import java.io.InputStream;
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
	private static List<String> getAFIdFromTTF(String ttfId, List<PLink> links)
	{
		List<String> result = new ArrayList<String>();
		for(PLink plink: links){
			if((plink.getSourceType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0)&&
			   (plink.getTargetType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0)){
				if(ttfId.compareToIgnoreCase(plink.getTarget())==0) result.add(plink.getSource());
			}
			if((plink.getTargetType().compareToIgnoreCase(ConceptEnum.Adaptation_Function.toString())==0)&&
			   (plink.getSourceType().compareToIgnoreCase(ConceptEnum.Trail_Termination_Function.toString())==0)){
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
				attrMap.put(cardId, runfromCard(cardId,links));
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
		return runfromCard(cardId, plinkList);
	}
	
	/** @author John Guerson 
	 * @throws IOException */
	public static Map<String,String> runfromCard(String cardId, List<PLink> links) throws IOException
	{
		/** tf <-> json content */
		Map<String,String> attrMap = new HashMap<String,String>();
		
		System.out.println("Card:"+cardId); 
//		System.out.println(links);
		/** Card Layers */
		List<String> layersNamesList = new ArrayList<String>();			
		layersNamesList = getCardLayersIdFromCard(cardId, links);		
		System.out.println("Layers:"+layersNamesList);
		for(String layerName: layersNamesList)
		{			
			String layer = new String();
			String variable = new String();
			String tpType = new String();
			boolean isClient = false;
			
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
			List<String> ttfs = getTTFIdFromCardLayer(layerName, links);
			System.out.println("Ttfs:"+ttfs);
			for(String ttfId: ttfs){
					
				/** Matrix */
				List<String> matrizes = getMatrixIdFromTTF(ttfId, links);
				if(matrizes.size()>0) isClient=true;
				
				/**AF*/
				List<String> afs = getAFIdFromTTF(ttfId, links);
				System.out.println("Afs:"+afs);
				for(String adId: afs){
					
					tpType = "ctp";
					if(isClient) tpType = "client-"+tpType;
					String jsonFileName = layer.toLowerCase()+variable.toLowerCase()+"-"+tpType.toLowerCase()+"-grouping.json";
					InputStream s = NOpenAttributeRecognizer.class.getResourceAsStream("/attributes/itu-874.1/"+jsonFileName);					
					StringWriter writer = new StringWriter();
					IOUtils.copy(s, writer, "UTF-8");
					
					System.out.println("AF:"+adId);
					System.out.println("JSON:"+jsonFileName);
					attrMap.put(adId, writer.toString());
				}
				
				tpType = "ttp";
				if(isClient) tpType = "client-"+tpType;
				String jsonFileName = layer.toLowerCase()+variable.toLowerCase()+"-"+tpType.toLowerCase()+"-grouping.json";
				InputStream s = NOpenAttributeRecognizer.class.getResourceAsStream("/attributes/itu-874.1/"+jsonFileName);
				System.out.println("TTF:"+ttfId);
				System.out.println("JSON:"+jsonFileName);
				StringWriter writer = new StringWriter();
				IOUtils.copy(s, writer, "UTF-8");
				
				attrMap.put(ttfId,writer.toString());
			}
		}
		return attrMap;
	}
	
	public static Map<String, Map<String, String>> runfromEquipment(String equipmentURI, OKCoUploader srcRepository) throws Exception
	{
		/** card (tf <-> json content) */
		Map<String, Map<String, String>> attrMap = new HashMap<String, Map<String, String>>();
		
		/** Supervisor */
//		String supervisorURI = NOpenQueryUtil.getSupervisorURI(srcRepository, equipmentURI);
		
		/** Cards */
//		List<String> cards = NOpenQueryUtil.getCardsURI(srcRepository, supervisorURI);				
//		for(String cardURI: cards)
//		{			
		
//		}
		return attrMap;
	}
	
	/** @author John Guerson */
	public static Map<String, String> runfromCard(String cardURI, OKCoUploader srcRepository) throws Exception
	{
		/** tf <-> json content */
		Map<String,String> attrMap = new HashMap<String,String>();
		
		/** Card Layers */
		List<String> layers = new ArrayList<String>();
		layers = NOpenQueryUtil.getCardLayersURIFromCard(srcRepository, cardURI);		
		System.out.println(layers);
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
