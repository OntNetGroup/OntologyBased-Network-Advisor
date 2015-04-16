package provisioner;

import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;

import provisioner.business.Provisioner;
import provisioner.domain.Interface;
import provisioner.jenaUtil.SPARQLQueries;

public class AutoTester {

	public static void main(String[] args) {
		String owlTBoxFile = "resources/owl/TBox v5.2.owl";
		String declaredFile = "resources/declared/Declarada 6.0a.txt";
		String possibleFile = "resources/possible/Possiveis 6.0.txt";
		int createNTimes = 1;
		for (int i = 0; i < createNTimes; i++) {
			try {
				Provisioner provisioner = new Provisioner(owlTBoxFile, declaredFile, possibleFile, createNTimes);
				
				OntModel model = provisioner.getModel();
				String ns = provisioner.getModel().getNsPrefixURI("");
				List<String> inInterfacesSrc = SPARQLQueries.getInterfacesFromLayer(model, ns+"Input_Interface", "Source", "layer"+i);
				String inInterfaceSrcURI = inInterfacesSrc.get(0);
				List<String> outInterfacesSk = SPARQLQueries.getInterfacesFromLayer(model, ns+"Output_Interface", "Sink", "layer"+i);
				String outInterfaceSrcURI = outInterfacesSk.get(0);
				
				Interface interfaceFrom = provisioner.getINT_SO_LIST().get(inInterfaceSrcURI);
				
				paths = provisioner.findPaths(interfaceFrom, interfaceTo, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip)
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
