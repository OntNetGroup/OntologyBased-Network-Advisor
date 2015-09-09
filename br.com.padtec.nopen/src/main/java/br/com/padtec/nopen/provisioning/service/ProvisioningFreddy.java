package br.com.padtec.nopen.provisioning.service;

import java.util.ArrayList;
import java.util.List;

public class ProvisioningFreddy {
	public SNC callFindSNCs(Port originalPortFrom, Port originalPortTo) throws Exception{
		return null;
	}
	
	public List<SNC> findSNCs(Port originalPortFrom, Port originalPortTo, ProvisioningOptions provOptions) throws Exception{
		ArrayList<Port> usedPorts = new ArrayList<Port>();
		usedPorts.add(originalPortFrom);
		
		SNC usedSNC = new SNC();
		usedSNC.addPort(originalPortFrom);
		ArrayList<SNC> sncList = new ArrayList<SNC>();
		
		findSNCs(sncList, originalPortTo, usedSNC, provOptions);
        
        return sncList;
	}
	
	public void findSNCs(List<SNC> sncList, Port originalPortTo, SNC usedSNC, ProvisioningOptions provOptions) throws Exception{
		Port inputPort = usedSNC.getLastPort();
		
		//verificar se excedeu algum limite do ProvisioningOptions
		
		String transportFunction = "";////////
		
		//verificar se esse tem ligação (in)direta com o transport function final...
		//se não estiver, return
		
		List<Port> outputPorts = new ArrayList<Port>();//////////////////////
		for (Port outputPort : outputPorts) {
			if(usedSNC.containsPort(outputPort)){
				continue;
			}
			
			SNC newUsedSNC = usedSNC;
			newUsedSNC.addPort(outputPort);
			
			if(transportFunction.equals("Matrix")){//////////////////////
				MatrixConnection matrixConnection = new MatrixConnection(inputPort, outputPort);
				newUsedSNC.addMatrixConnection(matrixConnection);
			}
			
			if(outputPort.equals(originalPortTo)){
				//verificar se excedeu algum limite do ProvisioningOptions
				
				sncList.add(newUsedSNC);
			}else{
				Port bindedInputPort = null;//////////////
				
				if(usedSNC.containsPort(bindedInputPort)){
					continue;
				}
				
				usedSNC.addPort(bindedInputPort);
				
				findSNCs(sncList, originalPortTo, usedSNC, provOptions);
			}
		}
	}
}
