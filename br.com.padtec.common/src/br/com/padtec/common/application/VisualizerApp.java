package br.com.padtec.common.application;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.graph.GraphPlotting;
import br.com.padtec.common.queries.DtoQueryUtil;

public class VisualizerApp {
	
	/**
	 * Get the graph values for the graph visualization.
	 * 
	 * @return
	 */
	public static String getGraphValues(String typeView, String individualURI, GraphPlotting graphPlotting)
	{		
		String valuesGraph = new String();				
		if(typeView.equals("ALL")) valuesGraph  = graphPlotting.getArborStructureFor(UploadApp.getInferredModel());
		else if(individualURI != null)
		{			
			DtoInstance dtoIndividual = DtoQueryUtil.getIndividual(UploadApp.getInferredModel(), individualURI,true,true,true);
			if(typeView.equals("IN")) 
			{				
				valuesGraph  = graphPlotting.getArborStructureComingInOf(UploadApp.getInferredModel(), dtoIndividual.ns + dtoIndividual.name);
			}
			else if(typeView.equals("OUT")) 
			{					
				valuesGraph  = graphPlotting.getArborStructureComingOutOf(UploadApp.getInferredModel(), dtoIndividual.ns + dtoIndividual.name);	
			}			
		}	
		return valuesGraph;
	}
}
