package provisioning;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.OntModel;

public class Main {
	static OKCoUploader okcoUploader = new OKCoUploader();
	static boolean runReason = true;
	static OntModel model;
	static String ns = "";
	static List<Interface> bindedInterfaces;;
	
	public static void main(String[] args){
		String tBoxFile = "";
		try {
			//#1
			tBoxFile = FileUtil.chooseFile("TBox", ".owl");
			//tBoxFile = "TBox v5.2.owl";
			
			//#2
			String aBoxFile = FileUtil.chooseFile("ABox", ".txt");
			//String aBoxFile = "Declarada 6.0a.txt";
			//#14
			String possibleEquipFile = FileUtil.chooseFile("available Equipment", ".txt");
			//String possibleEquipFile = "Possiveis 6.0.txt";
					
			//#1
			OWLUtil.createTBox(tBoxFile);
			//#3
			OWLUtil.createInstances(aBoxFile);
			List<String> declaredEquip = QueryUtil.getIndividualsURI(model, ns+"Equipment");
			//#4
//			System.out.println("Reasoner pós " + aBoxFile);
//			OWLUtil.runReasoner(true, true, true);
			
			//#7 and #8
			Provisioning.verifiyMinimumEquipment();
			

			bindedInterfaces = Queries.getBindedInterfaces(model);
			
			//#9
			Provisioning.verifyIfEquipmentMapsOutPortsInSource();
			List<Interface> INT_SO_LIST = Provisioning.verifyIfEquipmentMapsInPortsInSource();
			//removeInterfaces(INT_SO_LIST, bindedInterfaces);
			INT_SO_LIST.removeAll(bindedInterfaces);
			Provisioning.verifyIfEquipmentMapsInPortsInSink();
			List<Interface> INT_SK_LIST = Provisioning.verifyIfEquipmentMapsOutPortsInSink();
			//removeInterfaces(INT_SK_LIST, bindedInterfaces);
			INT_SK_LIST.removeAll(bindedInterfaces);
			
			//#10 and #11
			int srcInt2ProvIndex = chooseOne(INT_SO_LIST, "Input Interfaces", "Input Interface that maps an Input Port from Source");
			//int srcInt2ProvIndex = 8;
			Interface interfaceFrom = INT_SO_LIST.get(srcInt2ProvIndex);
			//String equipFromURI = INT_SO_LIST.get(srcInt2ProvIndex);
			
			//#12 and #13
			int tgtInt2ProvIndex = chooseOne(INT_SK_LIST, "Output Interfaces", "Output Interface that maps an Output Port from Sink");
			//int tgtInt2ProvIndex = 8;
			Interface interfaceTo = INT_SK_LIST.get(tgtInt2ProvIndex);
			//String equipToURI = INT_SK_LIST.get(tgtInt2ProvIndex+1);
			
			//#15
			OWLUtil.createInstances(possibleEquipFile);
			bindedInterfaces = Queries.getBindedInterfaces(model);
			List<String> possibleEquip = QueryUtil.getIndividualsURI(model, ns+"Equipment");
			possibleEquip.removeAll(declaredEquip);
			
			//#16
			Provisioning.verifiyMinimumEquipWithPM();
			//#17
			OWLUtil.runReasoner(true, true, true);
			
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			Character option = 'A';
			boolean ok;
			do {
				ok = true;
				System.out.print("Do you want to proceed automatically (A) or (M) manually? ");
				try {
					String optionStr = bufferRead.readLine();
					if(optionStr.length() > 1) ok = false;
					option = optionStr.charAt(0);
				} catch (Exception e) {
					ok = false;
				}			
			} while ((!option.equals('A') || !option.equals('M') || !option.equals('a') || !option.equals('m')) && !ok);
					
			if(option.equals('M') || option.equals('m')){
				Provisioning.callAlgorithmManual(interfaceFrom, interfaceTo);
			}else{
				Provisioning.callAlgorithmSemiAuto(interfaceFrom, interfaceTo);
			}
			
			//#23
			OWLUtil.runReasoner(false, true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}				
		
		//#25
		OWLUtil.saveNewOwl(tBoxFile);
		
		System.out.println();
		System.out.println("Done.");
	}
	
	public static void removeInterfaces(List<String> intList, List<String> toRemove){
		for (int i = intList.size() - 2; i >= 0; i--) {
			if(toRemove.contains(intList.get(i))){
				intList.remove(i+1);
				intList.remove(i);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> int chooseOne(List<T> list, String listName, String message) throws Exception{
		System.out.println();
		System.out.println("--- " + listName + " ---");
		
		if(list.size() == 0){
			throw new Exception("Something went wrong. The list is empty.");
		}
		
		//Collections.sort(list);
		myBubbleSort((List<Comparable>) list);
		
		for (int i = 0; i < list.size(); i+=1) {
//			String elem = "";
//			elem += ((String) list.get(i)).replace(ns, "");
//			
//			if(increment > 1){
//				elem += " [from equipment: ";
//				elem += ((String) list.get(i+1)).replace(ns, "");
//				elem += "]";
//			}
			
			int id = (i+1);
			
			System.out.println(id + " - " + list.get(i));
		}
		
//		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//		Integer index = 0;
//		
//		int highestOption = list.size()/increment;
//		boolean ok;
//		do {
//			ok = true;
//			try {
//				index = Integer.valueOf(bufferRead.readLine());
//			} catch (Exception e) {
//				ok = false;
//			}			
//		} while ((index < 1 || index > highestOption) && !ok);
//				
//		index = (index*increment)-increment;
		
		Integer index = getOptionFromConsole(list, message);
		
		return index;
	}
	
	public static <T> int getOptionFromConsole(List<T> list, String message){
		return getOptionFromConsole(list, message, list.size());
	}
	public static <T> int getOptionFromConsole(List<T> list, String message, int highestOption){
		Integer index = getOptionFromConsole(message, 1, highestOption);	
		index -= 1;
		return index;
	}
	
	public static int getOptionFromConsole(String message, int lowestOption, int highestOption){
		Integer index = 0;
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		boolean ok;
		do {
			ok = true;
			try {
				System.out.print("--- Choose " + message + ": ");
				index = Integer.valueOf(bufferRead.readLine());
			} catch (Exception e) {
				ok = false;
			}
		} while ((index < lowestOption || index > highestOption) || !ok);
		
		return index;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void myBubbleSort(List<Comparable> list) {
		boolean troca = true;
		Comparable aux;
        while (troca) {
            troca = false;
            for (int i = 0; i < list.size() - 1; i += 1) {
//            	String a1 = list.get(i);
//            	String a2 = list.get(i + increment);
            	int comparison = list.get(i).compareTo(list.get(i + 1));
            	if(comparison > 0){
            		for(int j = i; j < i + 1; j++){
            			aux = list.get(j);
            			list.set(j, list.get(j + 1));
                        //list.get(j) = list.get(j + increment);
            			list.set(j + 1, aux);
                        //list[j + increment] = aux;
                        
            		}
            		troca = true;
            	}
            }
        }
    }
}
