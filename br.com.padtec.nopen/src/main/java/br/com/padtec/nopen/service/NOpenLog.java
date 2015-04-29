package br.com.padtec.nopen.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NOpenLog {

	private static StringBuffer log = new StringBuffer();
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	public static String getTime()
	{
		return dateFormat.format(new Date());
	}
	
	public static void appendLine(String message)
	{
		log.append("["+getTime()+"] "+message+"\n");
		System.out.println("["+getTime()+"] "+message+"\n");
	}
	
	public static void append(String message)
	{		
		log.append(message);
		System.out.println(message);
	}
	
	public static void appendWithTime(String message)
	{		
		log.append("["+getTime()+"] "+message);
		System.out.println("["+getTime()+"] "+message);
	}
}
