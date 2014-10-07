package br.com.padtec.okco.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {

	public static void printout(String message)
	{
		System.out.println(getTime()+" - "+message);
	}
	
	public static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		
	public static String getTime()
	{
		return dateFormat.format(new Date());
	}
}
