package log;

/**
 * Central class to take over logging
 * 
 * This is only a temporal solution in order to have a central spot to have all logging calls going
 * 
 * TODO: improve logging functionality, use different framework potentially
 * @author Kenny
 *
 */
public class Logger {
	
	public static void debug(String... message) {
		System.out.println(concat("DEBUG: ", message));
	}
	
	public static void info(String... message)
	{
		System.out.println(concat("INFO: ", message));
	}
	
	public static String concat(String s1, String[] parts)
	{
		return concat(s1, concat(parts));
	}
	
	public static String concat(String ... parts)
	{
		StringBuffer retVal = new StringBuffer();
		
		if(parts != null && parts.length > 0)
		{
			for (String string : parts) {
				retVal.append(string);
			}
		}
		
		return retVal.toString();
	}
}
