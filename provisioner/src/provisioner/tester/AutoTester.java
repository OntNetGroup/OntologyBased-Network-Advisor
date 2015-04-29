package provisioner.tester;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;

import provisioner.business.Provisioner;
import provisioner.domain.Interface;
import provisioner.domain.Path;
import provisioner.jenaUtil.OWLUtil;
import provisioner.jenaUtil.SPARQLQueries;
import provisioner.util.ConsoleUtil;
import provisioner.util.FileUtil;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.PerformanceUtil;

import com.hp.hpl.jena.ontology.OntModel;

public class AutoTester {
	private static String outTesterDir = "resources/output/tester/";
	

	public static void main(String[] args) {
		FileUtil.createDirs();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				autoTester();
			}
		});	
	}

	private static void autoTester(){
		try{
			
//			File outTestDir = new File(outTesterDir);
//			if(outTestDir.exists()){
//				File[] filesList = outTestDir.listFiles();
//				for (File file : filesList) {
//					file.delete();
//				}
//			}else{
//				outTestDir.mkdirs();
//			}
			
			//String owlTBoxFile = "resources/owl/TBox v5.2.owl";
			String owlTBoxFile = FileUtil.chooseFile("OWL TBox", "resources/owl/", ".owl");
			//String declaredFile = "resources/declared/Possível 2.1 - 1 Layer.txt";
			String declaredFile = FileUtil.chooseFile("declared instances", "resources/declared/", ".txt");
			//String possibleFile = "resources/possible/Declarado 2.1 - Base.txt";
			String possibleFile = FileUtil.chooseFile("possible instances", "resources/possible/", ".txt");
			
			ArrayList<Test> tests = new ArrayList<Test>(); 
			int option = 0;
			do {
				System.out.println("Configuring test " + tests.size());
				Test test = new Test();
				test.setDeclaredReplicationsFromConsole();
				test.setMaxPathSizeFromConsole();
				test.setQtShortPathsFromConsole();
				
				tests.add(test);
				
				option = ConsoleUtil.getOptionFromConsole("Do you want to configure one more test? 1-Yes, 0-No", 0, 1);
			} while (option == 1);
			
//			int declaredReplications = ConsoleUtil.getOptionFromConsole("Choose the number of layer replications", 2, Integer.MAX_VALUE);
			//int declaredReplications = 5;
			
			
//			int qtShortPaths = ConsoleUtil.getOptionFromConsole("Choose the number of paths (enter 0 to show all)", 0, Integer.MAX_VALUE);
			//int qtShortPaths = 10;
//			int maxPathSize = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces in a path (enter 0 for no limit)", 0, Integer.MAX_VALUE);
			//int maxPathSize = 10;
			int declaredWeight = 1;
			int possibleWeight = 1;
			
			int createPathsFile = ConsoleUtil.getOptionFromConsole("Do you want to export found paths to a file? 1-Yes, 0-No", 0, 1);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
			Date now = new Date();
			String nowStr = DateFormat.getInstance().format(now).replace("/", "-").replace(":", ".");
			
			for (Test test : tests) {
				String testDirStr = nowStr + "/Test " + tests.indexOf(test) + "/";
				File testDir = new File(outTesterDir+testDirStr);
				boolean x = testDir.mkdirs();
				File executionTimes = new File(outTesterDir+testDirStr+"executionTimes.txt");
				FileOutputStream fosExecutionTimes = new FileOutputStream(executionTimes);
				String execTimes = "Include instances and reasoning execution\tFinding paths\n";
				fosExecutionTimes.write(execTimes.getBytes());
				
				boolean fewPossibleEquip = false;
				for (int i = 0; i < test.getDeclaredReplications(); i++) {
					executeForNReplications(i, possibleFile, declaredFile, owlTBoxFile, fosExecutionTimes, declaredWeight, fewPossibleEquip, test.getQtShortPaths(), test.getMaxPathSize(), possibleWeight, createPathsFile, testDirStr);	
				}
				fosExecutionTimes.close();
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	private static void executeForNReplications(int i, String possibleFile, String declaredFile, String owlTBoxFile, FileOutputStream fosExecutionTimes, int declaredWeight, boolean fewPossibleEquip, int qtShortPaths, int maxPathSize, int possibleWeight, int createPathsFile, String testDir) throws Exception{
		int possibleReplications = 1;
		File execTime = new File(outTesterDir + testDir + (i+1) + " replication(s)-Summary.txt");   
		if(execTime.exists()){
			execTime.delete();
		}
		FileOutputStream fosExecTime = new FileOutputStream(execTime);
		
		Date beginDate = new Date();				
		Provisioner provisioner = new Provisioner(owlTBoxFile, declaredFile, possibleFile, i+1, possibleReplications);
		String ns = provisioner.getModel().getNsPrefixURI("");
		
		OntModel model = provisioner.getModel();
		List<String> afURIs = QueryUtil.getIndividualsURI(model, ns+"Adaptation_Function");
		for (String afURI : afURIs) {
			List<String> serverLayerURIS = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, afURI, ns+"adapts_to", ns+"Layer_Network");
			for (String serverLayerURI : serverLayerURIS) {
				List<String> clientLayerURIs = QueryUtil.getIndividualsURIAtObjectPropertyDomain(model, serverLayerURI, ns+"client_of", ns+"Layer_Network");
				for (String clientLayerURI : clientLayerURIs) {
					FactoryUtil.createInstanceRelation(model, afURI, ns+"adapts_from", clientLayerURI, false, false, false);
				}
			}
		}
		
		long reasoningTimeExecPostInstances = provisioner.getReasoningTimeExecPostInstances();
		//String reasonerExec = "Reasoning execution: " + reasoningTimeExecPostInstances + "ms\n";
		String aux1 = reasoningTimeExecPostInstances + "\t";
		fosExecutionTimes.write(aux1.getBytes());
		String reasonerExec = PerformanceUtil.getExecutionMessage("Reasoning", reasoningTimeExecPostInstances) + "\n";
		fosExecTime.write(reasonerExec.getBytes());
		
//		OWLUtil.saveNewOwl(model, outTesterDir, "");
		
		List<String> inInterfacesSrc = SPARQLQueries.getInterfacesFromTopLayer(provisioner.getModel(), ns+"Input_Interface", "Source");
		String inInterfaceSrcURI = inInterfacesSrc.get(0);
		List<String> outInterfacesSk = SPARQLQueries.getInterfacesFromTopLayer(provisioner.getModel(), ns+"Output_Interface", "Sink");
		String outInterfaceSrcURI = outInterfacesSk.get(0);
		
		Interface interfaceFrom = provisioner.getInterface(inInterfaceSrcURI);
		Interface interfaceTo = provisioner.getInterface(outInterfaceSrcURI);
		
		beginDate = new Date();
		List<Path> pathsShorters = provisioner.findPaths(interfaceFrom, interfaceTo, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip);
		long nShortPathsTimeExec = PerformanceUtil.getExecutionTime(beginDate);
		//String pathsExec = "Find paths execution for " + qtShortPaths + " paths with maximum size " + maxPathSize + ": " + nShortPathsTimeExec + "ms\n";
		String aux2 = nShortPathsTimeExec + "\n";
		fosExecutionTimes.write(aux2.getBytes());
		String pathsExec = PerformanceUtil.printExecutionTime("Finding paths for " + qtShortPaths + " paths with maximum size " + maxPathSize, beginDate) + "\n";
		fosExecTime.write(pathsExec.getBytes());
		String qtPaths = "Found paths: " + pathsShorters.size() +"\n";
		
		if(pathsShorters.size() > 0){
			qtPaths += "Shortest path: " + pathsShorters.get(0).sizeToString() + "\n"
					+ "Longest path: " + pathsShorters.get(pathsShorters.size()-1).sizeToString() + "\n";
		}
		
		fosExecTime.write(qtPaths.getBytes());
		if(createPathsFile == 1){
			print(outTesterDir + testDir + (i+1) + " replication(s)-n paths.txt", pathsShorters);
		}
		fosExecTime.close();
		OWLUtil.saveNewOwl(provisioner.getModel(), outTesterDir, (i+1) + " ");
	}
	
	private static void print(String fileName, List<Path> pathsShorters) throws Exception {
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		for (int j = 0; j < pathsShorters.size(); j++) {
			String out = (j+1) + " - " + pathsShorters.get(j).toString();
			fos.write(out.getBytes());
		}
		fos.close();
	}

}
