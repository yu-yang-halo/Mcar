package cn.service;
/**
 * 
 * @author yuyang
 * 打印输出日志类
 */
public class Logger {

	private static Class _clazz;
	private static Logger instance = new Logger();
	private static boolean console = true;
    
	private Logger() {
	}
	public static Logger getLogger(Class clazz) {
		_clazz = clazz;
		return instance;
	}

	public void info(String info) {
		if (console) {
			System.out.println("[" + _clazz + "]  " + info);
		}

	}

	public void error(String error) {
		if (console) {
			System.err.println("[" + _clazz + "]  " + error);
		}
	}
	/**
	 * console默认为不输出日志，true为输出日志
	 * @param console
	 */
	public static void setConsole(boolean console) {
		Logger.console = console;
	}

	public static void setInstance(Logger instance) {
		Logger.instance = instance;
	}

}
