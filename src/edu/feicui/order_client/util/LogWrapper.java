package edu.feicui.order_client.util;

import edu.feicui.order_client.BuildConfig;
import android.util.Log;

/**
 * 对LogCat进行封装，只在调试时输出日志<br>
 * 这样的日志不能对用户展示<br>
 * 该日志只在调试时打印，发行后由于 BuildConfig.DEBUG 变为 false 而不输出日志<br>
 * 
 * @author Sogrey
 * 
 */
public class LogWrapper {
	private static boolean DEBUG = true && BuildConfig.DEBUG;

	/** info */
	public static void i(String tag, String msg) {
		if (DEBUG)
			Log.i(tag, msg);
	}

	/** debug */
	public static void d(String tag, String msg) {
		if (DEBUG)
			Log.d(tag, msg);
	}

	/** error */
	public static void e(String tag, String msg) {
		if (DEBUG)
			Log.e(tag, msg);
	}

	/** warm */
	public static void w(String tag, String msg) {
		if (DEBUG)
			Log.w(tag, msg);
	}

	/** verbose */
	public static void v(String tag, String msg) {
		if (DEBUG)
			Log.v(tag, msg);
	}
}