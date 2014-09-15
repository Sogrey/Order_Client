package edu.feicui.order_client.main;

import edu.feicui.order_client.util.CrashHandler;
import android.app.Application;

/**
 * Application代表整个应用
 * 
 * @author Sogrey
 * 
 */
public class OrderApplication extends Application {

	/** Application 的生命周期只有 onCreate */
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.getInstance().init();// 初始化
	}
}
