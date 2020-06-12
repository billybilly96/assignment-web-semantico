package Assignment.WebSemantico.utils.logger;

public class Logger {
	private static boolean disableDebugLog = true;
	public static boolean disableStackTrace = true;
		
	/**
	 * Write on System.out a message.
	 * @param error type of the message MessageType.
	 * @param className name of the class that want write the message.
	 * @param message 
	 */
	public static void log(MessageType error, String className, String message) {
		if(!disableDebugLog || !error.equals(MessageType.DEBUG)) {
			System.out.println("[" + error + "] [" + className + "] "+ message);
		}		
	}
}
