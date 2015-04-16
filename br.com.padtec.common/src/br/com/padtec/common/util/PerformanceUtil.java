package br.com.padtec.common.util;

import java.util.Date;

public class PerformanceUtil {
	public static long getExecutionTime(Date beginDate){
		Date endDate = new Date();
		long diff = endDate.getTime() - beginDate.getTime();
		return diff;
	}
	
	public static String printExecutionTime(String functionName, Date beginDate){
		long diff = getExecutionTime(beginDate);
		long diffHours = diff / (60 * 60 * 1000);
		diff -= diffHours * 60 * 60 * 1000;
		long diffMinutes = diff / (60 * 1000);         
		diff -= diffMinutes * 60 * 1000;
		long diffSeconds = diff / 1000;
		diff -= diffSeconds * 1000;
		long diffMiliSeconds = diff;
		
		String message = functionName + " Execution time: " + diffHours + "h " + diffMinutes + "m " + diffSeconds + "s " + diffMiliSeconds + "ms";
		System.out.println(message);
		
		return message;
	}
}
