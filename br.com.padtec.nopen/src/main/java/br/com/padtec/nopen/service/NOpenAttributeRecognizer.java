package br.com.padtec.nopen.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.nopen.topology.service.TopologyComponents;

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
	    
//	 public static void main(String[] args) throws Exception {
//		 OKCoUploader test = new OKCoUploader("attributes-recognizer-test");
//		 System.out.print("Loading Ontology... ");
//		 test.uploadBaseModel("test/topology.owl", "off", "hermit");
//		 System.out.println("[OK]");
//		 runfromCard("http://www.menthor.net/nOpenModel_light.owl#50d54bc4-9b4c-4031-b21e-32dad94f358f", test);
//	 }
	   
	 /** @author John Guerson 
		 * @throws Exception */
		public static Map<String, String>  runFromOWL(String cardId, String fileName) throws Exception{						
			String cardURI = TopologyComponents.topologyRepository.getNamespace()+cardId;
			if(!isCardPresent(cardURI)){
				System.err.println("Card Not Present: "+cardURI);				
				for(String s: QueryUtil.getIndividualsURI(TopologyComponents.topologyRepository.getBaseModel(),TopologyComponents.topologyRepository.getNamespace()+ConceptEnum.Card.toString())){
					System.err.println(s);
				}				
			}
			return runfromCard(cardURI);	
		}
	 
//	 /** @author John Guerson 
//	 * @throws Exception */
//	public static Map<String, String>  runFromOWLPath(String cardURI, String fileName) throws Exception{
////		String path = System.getProperty("user.home") + "\\nopen\\repository\\template\\owl\\";
//		String path = NOpenFileUtil.templateOWLFolder;
//		return runFromOWLPath(cardURI, fileName,path);	
//	}
			
//	/** @author John Guerson 
//	 * @throws Exception */
//	public static Map<String, String>  runFromOWLPath(String cardURI, String fileName, String path) throws Exception{
//		OKCoUploader repository = new OKCoUploader(fileName.replace(".owl", ""));
//		String entirePath = path+fileName+"\\"+fileName+".owl";
//		File file = new File(entirePath);
//		InputStream targetStream = new FileInputStream(file);
//		repository.uploadBaseModel(targetStream, "off", "hermit");	
//		return runfromCard(cardURI, repository);
//	}
	
	public static boolean isCardPresent(String cardURI){
		for(String s: QueryUtil.getIndividualsURI(TopologyComponents.topologyRepository.getBaseModel(),TopologyComponents.topologyRepository.getNamespace()+ConceptEnum.Card.toString())){
			if(s.equals(cardURI)) return true;			
		}
		return false;
		
	}
	
	/** @author John Guerson */
	@SuppressWarnings("unused")
	public static Map<String, String> runfromCard(String cardURI) throws Exception
	{		
		/** tf <-> json content */
		Map<String,String> attrMap = new HashMap<String,String>();
				
		/** Card Layers*/
		List<String> cardLayers = new ArrayList<String>();
		cardLayers = NOpenQueryUtil.getCardLayersURIFromCard(TopologyComponents.topologyRepository, cardURI);
		System.out.println("Card Layers: "+cardLayers);
		
		/** Layer Types */
		List<String> layerTypes = new ArrayList<String>();
		for(String l: cardLayers) { 
			layerTypes.addAll(NOpenQueryUtil.getLayerTypeURIFromCardLayer(TopologyComponents.topologyRepository, l)); 
		}
		System.out.println("Layer Types: "+layerTypes);
						
		//=====================================
		
		/** Layer Type Names */
		List<String> layerTypeNames = new ArrayList<String>();
		int i=0;
		for(String cardLayer: cardLayers)
		{			
			String layer = new String();
			String variable = new String();	
			String layerTypeName = layerTypes.get(i).replace(TopologyComponents.topologyRepository.getNamespace(), "");
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
		
		//======================================
		
		/**AF*/
		String tpType = new String();
		boolean isClient = false;
		List<String> afs = NOpenQueryUtil.getAFsURIFromCard(TopologyComponents.topologyRepository, cardURI);
		System.out.println("Afs:"+afs);
		int index=0;
		for(String adId: afs){
			
			/** Matrix */
			List<String> matrizes = NOpenQueryUtil.getMatrixURIFromAF(TopologyComponents.topologyRepository, adId);
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
		
		//======================================
		
		for(String cardLayer: cardLayers)
		{
			/** TTF */
			tpType = new String();
			isClient = false;
			List<String> ttfs = NOpenQueryUtil.getTTFURIFromCardLayer(TopologyComponents.topologyRepository, cardLayer);
			System.out.println("Ttfs:"+ttfs);
			index=0;
			for(String ttfId: ttfs){
				
				/** Matrix */
				List<String> matrizes = NOpenQueryUtil.getMatrixURIFromTTF(TopologyComponents.topologyRepository, ttfId);
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
}
