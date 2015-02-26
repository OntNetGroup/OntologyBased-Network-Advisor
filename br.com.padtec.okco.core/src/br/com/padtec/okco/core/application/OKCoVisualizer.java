package br.com.padtec.okco.core.application;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.graph.BaseGraphPlotting;
import br.com.padtec.common.queries.DtoQueryUtil;

public class OKCoVisualizer {
	
	/**
	 * Get the graph values for the graph visualization.
	 * 
	 * @return
	 */
	public static String getGraphValues(String typeView, String individualURI, BaseGraphPlotting graphPlotting)
	{		
		String valuesGraph = new String();				
		if(typeView.equals("ALL")) valuesGraph  = graphPlotting.getArborStructureFor(OKCoUploader.getInferredModel());
		else if(individualURI != null)
		{			
			DtoInstance dtoIndividual = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), individualURI,true,true,true);
			if(typeView.equals("IN")) 
			{				
				valuesGraph  = graphPlotting.getArborStructureComingInOf(OKCoUploader.getInferredModel(), dtoIndividual.ns + dtoIndividual.name);
			}
			else if(typeView.equals("OUT")) 
			{					
				valuesGraph  = graphPlotting.getArborStructureComingOutOf(OKCoUploader.getInferredModel(), dtoIndividual.ns + dtoIndividual.name);	
			}			
		}	
		return valuesGraph;
	}
}
