package provisioner.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class ConsoleUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> int chooseOne(List<T> list, String listName, String message, int optional) throws Exception{
		List<Comparable> comparableList = (List<Comparable>) list;
		System.out.println();
		System.out.println("--- " + listName + " ---");
		
		if(list.size() == 0){
			throw new Exception("Provisioning could not be performed. The list of avaiable interfaces is empty. Please try again using another path.");
		}
		
		Collections.sort(comparableList);
		
		for (int i = 0; i < list.size(); i+=1) {
			int id = (i+1);
			System.out.println(id + " - " + list.get(i));
		}
		
		Integer index = getOptionFromConsole(list, message, optional);
		
		return index;
	}
	
	public static <T> int getOptionFromConsole(List<T> list, String message, int optional){
		return getOptionFromConsole(list, message, list.size(), optional);
	}
	public static <T> int getOptionFromConsole(List<T> list, String message, int highestOption, int optional){
		Integer index = getOptionFromConsole(message, 1, highestOption, optional);	
		index -= 1;
		return index;
	}
	
	public static int getOptionFromConsole(String message, int lowestOption, int highestOption, int optional){
		Integer index = 0;
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		boolean ok;
		do {
			ok = true;
			try {
				if(optional == 1 && index == 0){
					message.replace(":", "");
					message += "(this step is optional. Choose 0 to skip): ";
				}
				System.out.print(message);
				index = Integer.valueOf(bufferRead.readLine());
			} catch (Exception e) {
				ok = false;
			}
			if(optional == 1 && index == 0){
				return index;
			}
		} while ((index < lowestOption || index > highestOption) || !ok);
		
		return index;
	}
	
	public static Character getCharOptionFromConsole(String message, List<Character> options){
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Character option = 'A';
		boolean ok;
		do {
			ok = true;
			System.out.print(message);
			try {
				String optionStr = bufferRead.readLine();
				if(optionStr.length() > 1) ok = false;
				option = optionStr.charAt(0);
			} catch (Exception e) {
				ok = false;
			}			
		} while (!options.contains(option) && !ok);
		
		return option;
	}
}
