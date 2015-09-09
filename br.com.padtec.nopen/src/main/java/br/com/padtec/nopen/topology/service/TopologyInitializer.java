package br.com.padtec.nopen.topology.service;

import java.io.InputStream;
import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.NOpenInitializer;
import br.com.padtec.nopen.service.util.NOpenUtilities;

public class TopologyInitializer {

	public static String uploadTopologyTBox(boolean runReasoner) {
		Date beginDate = new Date();
		InputStream s = NOpenInitializer.class.getResourceAsStream("/model/StudioLight.owl");
		
		String msg =  NOpenUtilities.uploadTBOx(s, runReasoner, TopologyComponents.topologyRepository);
		
		PerformanceUtil.printExecutionTime("Topology: TBox uploaded.", beginDate);
		return msg;
	}

}
