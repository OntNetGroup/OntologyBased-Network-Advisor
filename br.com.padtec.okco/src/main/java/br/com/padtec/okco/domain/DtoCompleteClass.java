package br.com.padtec.okco.domain;

import java.util.ArrayList;
import java.util.List;

public class DtoCompleteClass {

	public String CompleteClass;
	public ArrayList<String> Members;
	
	public DtoCompleteClass()
	{
		this.Members = new ArrayList<String>();
	}
	
	public void setCompleteClass(String classURI)
	{
		CompleteClass = classURI;
	}
	
	public void addAllMember(List<String> members)
	{
		Members.addAll(members);
	}
	public static DtoCompleteClass GetDtoCompleteClass(ArrayList<DtoCompleteClass> list, String completeClass)
	{
		
		for (DtoCompleteClass dtoCompleteClass : list) {
			if(dtoCompleteClass.CompleteClass == completeClass)
			{
				return dtoCompleteClass;
			}
		}
		
		return null;
	}

	public void AddMember(String member) {
		this.Members.add(member);		
	}
}
