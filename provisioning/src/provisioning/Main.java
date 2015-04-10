package provisioning;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
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
						
			Provisioning provisioning = new Provisioning(tBoxFile, aBoxFile, possibleEquipFile);
			
			//#10 and #11
			int srcInt2ProvIndex = chooseOne(provisioning.getINT_SO_LIST(), "Input Interfaces", "Input Interface that maps an Input Port from Source");
			//int srcInt2ProvIndex = 8;
			Interface interfaceFrom = provisioning.getINT_SO_LIST().get(srcInt2ProvIndex);
			//String equipFromURI = INT_SO_LIST.get(srcInt2ProvIndex);
			
			//#12 and #13
			int tgtInt2ProvIndex = chooseOne(provisioning.getINT_SK_LIST(), "Output Interfaces", "Output Interface that maps an Output Port from Sink");
			//int tgtInt2ProvIndex = 8;
			Interface interfaceTo = provisioning.getINT_SK_LIST().get(tgtInt2ProvIndex);
			//String equipToURI = INT_SK_LIST.get(tgtInt2ProvIndex+1);
			
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
			
			provisioning.provision(interfaceFrom, interfaceTo, option);
			
		} catch (Exception e) {
			e.printStackTrace();
		}				
		
		System.out.println();
		System.out.println("Done.");
	}
	
//	public static void removeInterfaces(List<String> intList, List<String> toRemove){
//		for (int i = intList.size() - 2; i >= 0; i--) {
//			if(toRemove.contains(intList.get(i))){
//				intList.remove(i+1);
//				intList.remove(i);
//			}
//		}
//	}
	
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
			int id = (i+1);
			System.out.println(id + " - " + list.get(i));
		}
		
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
