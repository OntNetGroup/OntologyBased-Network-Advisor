package provisioner.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
			String owlBaseTBoxFile = FileUtil.chooseFile("Choose an OWL file containing a Inference Model: ", "resources/owl/", ".owl", "TBOX chosen file: ",0);
			String owlConsistencyTBoxFile = FileUtil.chooseFile("Choose an OWL file containing a Consistency Model: ", "resources/owl/", ".owl", "TBOX chosen file: ",0);
			//String declaredFile = "resources/declared/Poss�vel 2.1 - 1 Layer.txt";
			String declaredFile = FileUtil.chooseFile("Choose a TXT file containing DECLARED instances:", "resources/declared/", ".txt", "POSSIBLE instances chosen file: ",0);
			//String possibleFile = "resources/possible/Declarado 2.1 - Base.txt";
			String possibleFile = FileUtil.chooseFile("Choose a TXT file containing POSSIBLE instances: ", "resources/possible/", ".txt", "POSSIBLE instances chosen file: ",0);
			
			ArrayList<Test> tests = new ArrayList<Test>(); 
			ArrayList<Character> manualOrFile = new ArrayList<Character>();
			manualOrFile.add('F');
			manualOrFile.add('M');
			
			Character fromFile = ConsoleUtil.getCharOptionFromConsole("Do you want to configure tests from a file (F) or manually (M)? ", manualOrFile);
			if(fromFile.equals('M') || fromFile.equals('m')){
				int option = 0;
				do {
					System.out.println("Configuring test " + tests.size());
					Test test = new Test();
					test.setDeclaredReplicationsFromConsole();
					test.setQtShortPathsFromConsole();
					test.setMaxPathSizeFromConsole();
					test.setMaxNewBindingsFromConsole();
					test.setMaxNewPossibleFromConsole();
					test.setPriorityOptionFromConsole(possibleFile);
					tests.add(test);
					
					option = ConsoleUtil.getOptionFromConsole("Do you want to configure one more test? 1-Yes, 0-No: ", 0, 1,0, false);
				} while (option == 1);
			}else{
				String testsConfiguration = FileUtil.chooseFile("Choose a TXT file containing tests configuration:", "resources/tests/", ".txt", "tests configuration chosen file: ",0);
				FileReader reader = new FileReader(new File(testsConfiguration));
		        Scanner scanner = new Scanner(new BufferedReader(reader));
		        while (scanner.hasNextLine()){
		        	Test test = new Test();
					test.setDeclaredReplications(scanner.nextInt());
					test.setQtShortPaths(scanner.nextInt());
					test.setMaxPathSize(scanner.nextInt());
					test.setMaxNewBindings(scanner.nextInt());
					test.setMaxNewPossible(scanner.nextInt());
					test.setPriorityOption(scanner.nextInt());
					tests.add(test);
		        }
		        scanner.close();
		        
			}
			
			int createPathsFile = ConsoleUtil.getOptionFromConsole("Do you want to export found paths to a file? 1-Yes, 0-No: ", 0, 1,0, false);
			
			Date now = new Date();
			String nowStr = DateFormat.getInstance().format(now).replace("/", "-").replace(":", ".");
			
			File testFileDir = new File(outTesterDir+nowStr);
			testFileDir.mkdirs();
			
			File testFile = new File(outTesterDir+nowStr+"/general-summary.txt");
			FileOutputStream fosTestFile = new FileOutputStream(testFile);
			String execTimes = "TestNumber\tNumberOfLayers\tIncludeTime\tReasoningTime\tFindingPathTimes\tTotalTime\n";
			fosTestFile.write((execTimes).getBytes());
			
			for (Test test : tests) {
				String unitTestDirStr = outTesterDir + nowStr +"/Test " + tests.indexOf(test) + "/";
				File unitTestDir = new File(unitTestDirStr);
				unitTestDir.mkdirs();
				File executionTimes = new File(unitTestDirStr+"executionTimes.txt");
				FileOutputStream fosExecutionTimes = new FileOutputStream(executionTimes);
				fosExecutionTimes.write(execTimes.getBytes());
				
				for (int i = 0; i < test.getDeclaredReplications(); i++) {
					fosExecutionTimes.write((tests.indexOf(test) + "\t").getBytes());
					fosTestFile.write((tests.indexOf(test) + "\t").getBytes());
					Date beginDate = new Date();
					executeForNReplications(i, possibleFile, declaredFile, owlBaseTBoxFile, owlConsistencyTBoxFile, fosTestFile, fosExecutionTimes, test.getQtShortPaths(), test.getMaxPathSize(), test.getPriorityOption(), test.getMaxNewBindings(), test.getMaxNewPossible(), createPathsFile, unitTestDirStr);
					String totalTime = String.valueOf(PerformanceUtil.getExecutionTime(beginDate)) + "\n";
					fosExecutionTimes.write(totalTime.getBytes());
					fosTestFile.write(totalTime.getBytes());
					
				}
				fosExecutionTimes.close();
			}		
			
			fosTestFile.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	private static void executeForNReplications(int i, String possibleFile, String declaredFile, String owlBaseTBoxFile, String owlConsistencyTBoxFile, FileOutputStream fosTestFile, FileOutputStream fosExecutionTimes, int qtShortPaths, int maxPathSize, int priorityOption, int maxNewBindings, int maxNewPossible, int createPathsFile, String testDir) throws Exception{
		int possibleReplications = 1;
		File execTime = new File(testDir + (i+1) + " replication(s)-Summary.txt");   
		if(execTime.exists()){
			execTime.delete();
		}
		FileOutputStream fosExecTime = new FileOutputStream(execTime);
		
		Date beginDate = new Date();				
		Provisioner provisioner = new Provisioner(owlBaseTBoxFile, owlConsistencyTBoxFile, declaredFile, possibleFile, i+1, possibleReplications);
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
		
		long includeTime = provisioner.getCreateInstancesTime();
		long reasoningTimeExecPostInstances = provisioner.getReasoningTimeExecPostInstances();
		//String reasonerExec = "Reasoning execution: " + reasoningTimeExecPostInstances + "ms\n";
		String aux1 = String.valueOf(i+1)+"\t" + includeTime + "\t" + reasoningTimeExecPostInstances + "\t";
		fosExecutionTimes.write(aux1.getBytes());
		fosTestFile.write(aux1.getBytes());
		
		String includeExec = PerformanceUtil.getExecutionMessage("Including instances", includeTime) + "\n";
		fosExecTime.write(includeExec.getBytes());
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
		List<Path> pathsShorters = provisioner.findPaths(interfaceFrom, interfaceTo, qtShortPaths, maxPathSize, priorityOption, maxNewBindings, maxNewPossible);
		long nShortPathsTimeExec = PerformanceUtil.getExecutionTime(beginDate);
		//String pathsExec = "Find paths execution for " + qtShortPaths + " paths with maximum size " + maxPathSize + ": " + nShortPathsTimeExec + "ms\n";
		String aux2 = nShortPathsTimeExec + "\t";
		fosExecutionTimes.write(aux2.getBytes());
		fosTestFile.write(aux2.getBytes());
		String pathsExec = PerformanceUtil.printExecutionTime("Finding paths for " + qtShortPaths + " paths with maximum size " + maxPathSize, beginDate) + "\n";
		fosExecTime.write(pathsExec.getBytes());
		String qtPaths = "Found paths: " + pathsShorters.size() +"\n";
		
		if(pathsShorters.size() > 0){
			qtPaths += "Shortest path: " + pathsShorters.get(0).sizeToString() + "\n"
					+ "Longest path: " + pathsShorters.get(pathsShorters.size()-1).sizeToString() + "\n";
		}
		
		fosExecTime.write(qtPaths.getBytes());
		if(createPathsFile == 1){
			print(testDir + (i+1) + " replication(s)-n paths.txt", pathsShorters);
		}
		fosExecTime.close();
//		String[] split = testDir.split("/");
//		if(split.length > 0) testDir = testDir.replace(split[split.length-1]+"/", "").replace(split[split.length-1], "");
		OWLUtil.saveNewOwl(provisioner.getModel(), testDir, (i+1) + " ");
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
