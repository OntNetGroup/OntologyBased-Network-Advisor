<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/rdf-xml" %>
<%
	ArrayList<String> txtCondelCode = (ArrayList<String>)request.getSession().getAttribute("instructions");

	if(txtCondelCode != null)
	{
		for (String line : txtCondelCode)
		{
			out.println(line);			
		}		
		
	} else {
		
		out.println("* You need to load some condel code.");
	}
%>