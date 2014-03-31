package org.oryxeditor.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6751711270993480055L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.println(headWithTitle("OntoUML Web Editor")+
				"<BODY>" +
				"<script src='js/jquery-1.9.1.js'></script>" +
				"<center>" +
				"	<a href='webeditor'>" +
				"		<img src='images/logo1v6.png'>" +
				"	</a><br/>" +
				"	<select id='optionSel'>" +
				"		<option value='webeditor'>OntoUML</option>" +
				"		<option value='webeditor?ss=/stencilsets/transportnetwork/transportnetwork.json'>Teste</option>" +
				"		<option value='webeditor?ss=/stencilsets/network/network.json'>Transport Network</option>" +
				"	</select><br/><br/>" +
				"<a id='btnOpen' class='btn' href='webeditor'>Open</a>" +
				"</center>" +
				"<script>" +
				"	$('#optionSel').change(function() {" +
				"		$('#btnOpen').attr('href', $('#optionSel').val());" +
				"});" +
				"</script>" +
				"</BODY></HTML>");
		out.close();
		resp.setStatus(200);
	}
	
	public static String DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">";

	public static String headWithTitle(String title) {
	    return(DOCTYPE + "\n" +
	           "<HTML>\n" +
	           "<HEAD>" +
	           "<STYLE type='text/css'>"+
	           ".btn{"+
	           "padding:10px 15px;"+
	           "	background:#c9e70b;"+
	           "	color:#991059!important;"+
	           "	-webkit-border-radius:4px;"+
	           "	-moz-border-radius:4px;"+
	           "	border-radius:4px;"+
	           "	border:solid 1px #edfa96;"+
	           "	text-shadow:0 -1px 0 rgba(0,0,0,0.4);"+
	           "	-webkit-box-shadow:inset 0 1px 0 rgba(255,255,255,0.4),0 1px 1px rgba(0,0,0,0.2);"+
	           "	-moz-box-shadow:inset 0 1px 0 rgba(255,255,255,0.4),0 1px 1px rgba(0,0,0,0.2);"+
	           "	box-shadow:inset 0 1px 0 rgba(255,255,255,0.4),0 1px 1px rgba(0,0,0,0.2);"+
	           "	-webkit-transition-duration:0.2s;"+
	           "	-moz-transition-duration:0.2s;"+
	           "	transition-duration:0.2s;"+
	           "	text-decoration:none;"+
	           "}"+
	           ".btn:hover{"+
	           "	background:#edfa96;"+
	           "	color:#991059!important;"+
	           "	border:solid 1px #edfa96;"+
	           "	text-decoration:none;"+
	           "}"+
	           ".btn{"+
	           "	-webkit-user-select:none;"+
	           "	-moz-user-select:none;"+
	           "	-ms-user-select:none;"+
	           "	user-select:none;"+
	           "	color:#991059!important;"+
	           "	cursor:pointer"+
	           "	text-decoration:none;"+ 
	           "}"+
	           ".btn:active{"+
	           "	background:#c9e70b;"+
	           "	border:solid 1px #edfa96;"+
	           "	color:#991059!important;"+
	           "	-webkit-box-shadow:inset 0 1px 4px rgba(0,0,0,0.6);"+
	           "	-moz-box-shadow:inset 0 1px 4px rgba(0,0,0,0.6);"+
	           "	box-shadow:inset 0 1px 4px rgba(0,0,0,0.6);"+
	           "	text-decoration:none;"+ 
	           "}"+
	           "</STYLE>"+
	           "<TITLE>" + title + "</TITLE></HEAD>\n");
	  }
}

