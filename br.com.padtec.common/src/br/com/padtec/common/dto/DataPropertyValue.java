package br.com.padtec.common.dto;



public class DataPropertyValue {
	
	public String value;
	public String classValue;	
	public String classValueEncoded;
	public boolean existInModel;
	
	public DataPropertyValue()
	{
		value = "";
		classValue = "";		
		classValueEncoded="";
		existInModel = false;			
	}
}
