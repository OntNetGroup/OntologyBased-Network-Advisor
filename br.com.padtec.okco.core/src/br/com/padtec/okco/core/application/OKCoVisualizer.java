package br.com.padtec.okco.core.application;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.graph.BaseGraphPlotting;
import br.com.padtec.common.queries.DtoQueryUtil;

public class OKCoVisualizer {
	
	protected OKCoUploader repository; 
	
	public OKCoVisualizer(OKCoUploader repository)
	{
		this.repository = repository;
	}
	
	/**
	 * Get the graph values for the graph visualization.
	 * 
	 * @return
	 */
	public String getGraphValues(String typeView, String individualURI, BaseGraphPlotting graphPlotting)
	{		
		String valuesGraph = new String();				
		if(typeView.equals("ALL")) valuesGraph  = graphPlotting.getArborStructureFor(repository.getInferredModel());
		else if(individualURI != null)
		{			
			DtoInstance dtoIndividual = DtoQueryUtil.getIndividualByName(repository.getInferredModel(), individualURI,true,true,true);
			if(typeView.equals("IN")) 
			{				
				valuesGraph  = graphPlotting.getArborStructureComingInOf(repository.getInferredModel(), dtoIndividual.ns + dtoIndividual.name);
			}
			else if(typeView.equals("OUT")) 
			{					
				valuesGraph  = graphPlotting.getArborStructureComingOutOf(repository.getInferredModel(), dtoIndividual.ns + dtoIndividual.name);	
			}			
		}	
		return valuesGraph;
	}
}
