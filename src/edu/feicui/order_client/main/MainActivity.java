package edu.feicui.order_client.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import edu.feicui.order_client.R;
import edu.feicui.order_client.adapter.GalleryAdapter;
import edu.feicui.order_client.base.BaseActivity;
import edu.feicui.order_client.choosedish.ChooseDishActivity;
import edu.feicui.order_client.more.CheckOrderListActivity;
import edu.feicui.order_client.more.MoreActivity;
import edu.feicui.order_client.util.Constants;

/**
 * 主界面
 * 
 * @author Sogrey
 * 
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	
	/**当前使用账户名*/
	protected String mUser;
	/** 点菜-按钮 */
	protected Button mBtnChooseCish;
	/** 结算-按钮 */
	protected Button mBtnCheckout;
	/** 订单进度-按钮 */
	protected Button mBtnSchedule;
	/** 加单-按钮 */
	protected Button mBtnAdd;
	/** 更多-按钮 */
	protected Button mBtnMore;
	/** 主页面显示可左右滑动的Gallery */
	protected Gallery mGlrPic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_main);
		mUser=getIntent().getStringExtra(Constants.USER_NAME);
		initViews();
	}

	private void initViews() {
		mBtnChooseCish = (Button) findViewById(R.id.btn_main_choosedish);
		mBtnChooseCish.setOnClickListener(this);
		mBtnCheckout = (Button) findViewById(R.id.btn_main_checkout);
		mBtnCheckout.setOnClickListener(this);
		mBtnSchedule = (Button) findViewById(R.id.btn_main_schedule);
		mBtnSchedule.setOnClickListener(this);
		mBtnAdd = (Button) findViewById(R.id.btn_main_add);
		mBtnAdd.setOnClickListener(this);
		mBtnMore = (Button) findViewById(R.id.btn_main_more);
		mBtnMore.setOnClickListener(this);
		mGlrPic = (Gallery) findViewById(R.id.glr_main_showpics);
		mGlrPic.setAdapter(new GalleryAdapter(this));
		
//		Timer timer = new Timer();
//		timer.schedule(task, 3000);
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		int id = view.getId();
		switch (id) {
		case R.id.btn_main_choosedish:// 点菜
			intent.setClass(this, ChooseDishActivity.class);
			intent.putExtra(Constants.CHOOSEDISH, Constants.CHOOSE_ORDER);
			break;
		case R.id.btn_main_checkout:// 结算
			intent.setClass(this, CheckOrderListActivity.class);
			intent.putExtra(Constants.CHECK_ORDER, Constants.CHECKOUT);
			break;
		case R.id.btn_main_schedule:// 订单进度
			intent.setClass(this, CheckOrderListActivity.class);
			intent.putExtra(Constants.CHECK_ORDER, Constants.ORDER_PROGRESS);
			break;
		case R.id.btn_main_add:// 加单
			intent.setClass(this, CheckOrderListActivity.class);
			intent.putExtra(Constants.CHECK_ORDER, Constants.ADD_ORDER);
			break;
		case R.id.btn_main_more:// 更多
			intent.setClass(this, MoreActivity.class);
			break;

		default:
			break;
		}
		intent.putExtra(Constants.USER_NAME, mUser);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:// 退出应用
			showDialog(Constants.DIALOG_EXIT);
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case Constants.DIALOG_EXIT:// 退出应用
			return createExitDialog();

		default:
			return super.onCreateDialog(id);
		}
	}

		/** 创建退出应用对话框 */
	private Dialog createExitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.hint_exit_dialog));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(getString(R.string.txt_exit_ack));
		builder.setPositiveButton(getString(R.string.ok),
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
//						finish();
						System.exit(0);
					}
				});
		builder.setNegativeButton(getString(R.string.back), null);
		return builder.create();
	}

}
