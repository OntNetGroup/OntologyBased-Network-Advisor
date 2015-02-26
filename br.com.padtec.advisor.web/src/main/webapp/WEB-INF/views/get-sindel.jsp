<%@ page contentType="text/rdf-xml" %>
<%
	String txtSindelCode = (String)request.getSession().getAttribute("txtSindelCode");

	if(txtSindelCode != null && txtSindelCode != "")
	{
		out.println(txtSindelCode);
		
	} else {
		
		out.println("* You need to load some sindel code.");
	}
%>