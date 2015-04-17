package provisioner;

import java.util.Date;
import java.util.List;

import br.com.padtec.common.util.PerformanceUtil;

import com.hp.hpl.jena.ontology.OntModel;

import provisioner.business.Provisioner;
import provisioner.domain.Interface;
import provisioner.domain.Path;
import provisioner.jenaUtil.SPARQLQueries;

public class AutoTester {

	public static void main(String[] args) {
		String owlTBoxFile = "resources/owl/TBox v5.2.owl";
		String declaredFile = "resources/declared/Declarado_teste.txt";
		String possibleFile = "";
		int createNTimes = 2;
		int qtShortPaths = 0;
		int maxPathSize = 0;
		int declaredWeight = 1;
		int possibleWeight = 1;
		
		boolean fewPossibleEquip = false;
		for (int i = 0; i < createNTimes; i++) {
			try {
				Date beginDate = new Date();				Provisioner provisioner = new Provisioner(owlTBoxFile, declaredFile, possibleFile, createNTimes);
				long createInstanceAndReasoningTimeExec = PerformanceUtil.getExecutionTime(beginDate);
				
				OntModel model = provisioner.getModel();
				String ns = provisioner.getModel().getNsPrefixURI("");
				List<String> inInterfacesSrc = SPARQLQueries.getInterfacesFromLayer(model, ns+"Input_Interface", "Source", "layer"+i);
				String inInterfaceSrcURI = inInterfacesSrc.get(0);
				List<String> outInterfacesSk = SPARQLQueries.getInterfacesFromLayer(model, ns+"Output_Interface", "Sink", "layer"+i);
				String outInterfaceSrcURI = outInterfacesSk.get(0);
				
				Interface interfaceFrom = provisioner.getInterface(inInterfaceSrcURI);
				Interface interfaceTo = provisioner.getInterface(outInterfaceSrcURI);
				
				qtShortPaths = 0;
				maxPathSize = 0;
				
				beginDate = new Date();
				List<Path> pathsShorters = provisioner.findPaths(interfaceFrom, interfaceTo, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip);
				long nShortPathsTimeExec = PerformanceUtil.getExecutionTime(beginDate);
				
				qtShortPaths = 0;
				maxPathSize = 0;
				beginDate = new Date();
				List<Path> allPaths = provisioner.findPaths(interfaceFrom, interfaceTo, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip);
 				long allPathsTimeExec = PerformanceUtil.getExecutionTime(beginDate );
				
			} catch (Exception e) {
				System.out.println("i: " + i);
				e.printStackTrace();
			}
		}
	}

}
