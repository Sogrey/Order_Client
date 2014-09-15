package edu.feicui.order_client.base;

import edu.feicui.order_client.util.LogWrapper;
import android.app.Activity;
import android.os.Bundle;

/**
 * Activity基础类
 * <p>
 * 包含Activity的生命周期及其调试输出
 * <p>
 * 
 * @author Sogrey
 */
public class BaseActivity extends Activity {
	/* 获取当前类的名称 */
	protected final String TAG = this.getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogWrapper.d(TAG, "onCreate");
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogWrapper.d(TAG, "onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogWrapper.d(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		LogWrapper.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogWrapper.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		LogWrapper.d(TAG, "onRestart");
	}

	@Override
	protected void onDestroy() {
		LogWrapper.d(TAG, "onDestroy");
		super.onDestroy();
	}
}
