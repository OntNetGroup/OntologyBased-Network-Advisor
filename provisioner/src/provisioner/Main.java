package provisioner;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import provisioner.business.Provisioner;
import provisioner.domain.Interface;
import provisioner.util.ConsoleUtil;
import provisioner.util.FileUtil;

public class Main {
	public static void main(String[] args){
		FileUtil.createDirs();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				autoTester();
			}
		});	
	}
	
	private static void autoTester(){
		String owlTBoxFile = "";
		Provisioner provisioner = null;
		try {
			//#1
			owlTBoxFile = FileUtil.chooseFile("OWL TBox", "resources/owl/", ".owl");
			//owlTBoxFile = "resources/owl/TBox v5.2.owl";
			
			//#2
			String declaredFile = FileUtil.chooseFile("declared instances", "resources/declared/", ".txt");
			//String declaredFile = "resources/declared/Possível 2.1 - 1 Layer.txt";
			
			//#14
			String possibleFile = FileUtil.chooseFile("possible instances", "resources/possible/", ".txt");
			//String possibleFile = "resources/possible/Possiveis 6.0.txt";
			//String possibleFile = "resources/possible/Declarado 2.1 - Base.txt";
			
			provisioner = new Provisioner(owlTBoxFile, declaredFile, possibleFile, 1, 1);
					
			//#10 and #11
			int srcInt2ProvIndex = ConsoleUtil.chooseOne(provisioner.getINT_SO_LIST(), "Input Interfaces", "Input Interface that maps an Input Port from Source");
			//int srcInt2ProvIndex = 8;
			Interface interfaceFrom = provisioner.getINT_SO_LIST().get(srcInt2ProvIndex);
			//String equipFromURI = INT_SO_LIST.get(srcInt2ProvIndex);
			
			//#12 and #13
			int tgtInt2ProvIndex = ConsoleUtil.chooseOne(provisioner.getINT_SK_LIST(), "Output Interfaces", "Output Interface that maps an Output Port from Sink");
			//int tgtInt2ProvIndex = 8;
			Interface interfaceTo = provisioner.getINT_SK_LIST().get(tgtInt2ProvIndex);
			//String equipToURI = INT_SK_LIST.get(tgtInt2ProvIndex+1);
			
			ArrayList<Character> options = new ArrayList<Character>();
			options.add('A');
			options.add('M');
			options.add('a');
			options.add('m');
			Character option = ConsoleUtil.getCharOptionFromConsole("Do you want to proceed automatically (A) or (M) manually? ", options);
			
			provisioner.consoleProvisioner(interfaceFrom, interfaceTo, option);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		System.out.println();
		System.out.println("Done.");
	}
}
