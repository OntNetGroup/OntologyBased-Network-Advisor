package provisioner;

import java.io.IOException;

import javax.swing.SwingUtilities;

import provisioner.business.Provisioner;
import provisioner.jenaUtil.OWLUtil;
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
		String owlBaseTBoxFile = "";
		String owlConsistencyTBoxFile = "";
		Provisioner provisioner = null;
		try {
			//#1
			owlBaseTBoxFile = FileUtil.chooseFile("Choose an OWL file containing an Inference Model: ", "resources/owl/", ".owl", "TBOX chosen file: ",0);
			//owlTBoxFile = "resources/owl/TBox v5.2.owl";
			
			owlConsistencyTBoxFile = FileUtil.chooseFile("Choose an OWL file containing a Consistency Model: ", "resources/owl/", ".owl", "TBOX chosen file: ",0);
			
			//#2
			String declaredFile = FileUtil.chooseFile("Choose a TXT file containing DECLARED instances: ", "resources/declared/", ".txt", "DECLARED instances chosen file: ",0);
			//String declaredFile = "resources/declared/Possível 2.1 - 1 Layer.txt";
			
			//#14
			String possibleFile = FileUtil.chooseFile("Choose a TXT file containing POSSIBLE instances: ", "resources/possible/", ".txt", "POSSIBLE instances chosen file: ",1);
			//String possibleFile = "resources/possible/Possiveis 6.0.txt";
			//String possibleFile = "resources/possible/Declarado 2.1 - Base.txt";
			
			provisioner = new Provisioner(owlBaseTBoxFile, owlConsistencyTBoxFile, declaredFile, possibleFile, 1, 1);
					
			provisioner.consoleProvisioner();
		
//			System.out.println();
			System.out.println("Provisioning successfully done!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
		
		try {
			OWLUtil.saveNewOwl(provisioner.getModel(), "resources/output/", "");
			System.out.println("OWL successfully saved!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
	}
}
