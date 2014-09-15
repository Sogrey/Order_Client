/**
 * 
 */
package edu.feicui.order_client.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import edu.feicui.order_client.R;
import edu.feicui.order_client.adapter.GalleryAdapter;
import edu.feicui.order_client.base.BaseActivity;
import edu.feicui.order_client.util.Constants;

/**
 * @author Sogrey
 * 
 */
public class MoreActivity extends BaseActivity implements OnClickListener {
	
	/**当前使用账户名*/
	protected String mUser;
	protected Gallery mGlrPic;
	/**查单*/
	protected Button mBtnCheckOut;
	/**换台*/
	protected Button mBtnChangeTable;
	/**并台*/
	protected Button mBtnMergeTable;
	/**数据更新*/
	protected Button mBtnUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		mUser=getIntent().getStringExtra(Constants.USER_NAME);
		initViews();
	}

	private void initViews() {
		mGlrPic = (Gallery) findViewById(R.id.glr_more_showpics);
		mBtnCheckOut = (Button) findViewById(R.id.btn_more_checkout);
		mBtnChangeTable = (Button) findViewById(R.id.btn_more_change_table);
		mBtnMergeTable = (Button) findViewById(R.id.btn_more_combined_table);
		mBtnUpdate = (Button) findViewById(R.id.btn_more_update);
		mBtnCheckOut.setOnClickListener(this);
		mBtnChangeTable.setOnClickListener(this);
		mBtnMergeTable.setOnClickListener(this);
		mBtnUpdate.setOnClickListener(this);
		mGlrPic.setAdapter(new GalleryAdapter(this));
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		Intent intent = new Intent();
		switch (id) {
		case R.id.btn_more_checkout://查单
			intent.setClass(this, CheckOrderListActivity.class);
			intent.putExtra(Constants.CHECK_ORDER, Constants.CHECK_ORDER_INFO);
			break;
		case R.id.btn_more_change_table://换台
			intent.setClass(this, ChangeTableActivity.class);
			intent.putExtra(Constants.CHANGE_TABLE_NAME, Constants.CHANGE_TABLE);
			break;
		case R.id.btn_more_combined_table://并台
			intent.setClass(this, ChangeTableActivity.class);
			intent.putExtra(Constants.CHANGE_TABLE_NAME, Constants.MERGE_TABLE);
			break;
		case R.id.btn_more_update://数据更新
			intent.setClass(this, UpdateActivity.class);
			break;

		default:
			break;
		}
		intent.putExtra(Constants.USER_NAME, mUser);
		startActivity(intent);
	}
}
