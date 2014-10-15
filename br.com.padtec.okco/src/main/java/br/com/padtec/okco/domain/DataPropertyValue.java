package br.com.padtec.okco.domain;

import java.util.ArrayList;

public class DataPropertyValue {
	
	public String value;
	public String classValue;
	public boolean existInModel;
	
	public DataPropertyValue()
	{
		value = "";
		classValue = "";
		existInModel = false;
	}
	
	public static void removeFromList(ArrayList<DataPropertyValue> list, String uri) {

		for (DataPropertyValue data : list) {
			
			if(data.classValue.equals(uri))
			{
				list.remove(data);
				break;
			}
		}		
	}
	

}
