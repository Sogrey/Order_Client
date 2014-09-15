package edu.feicui.order_client.more;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.feicui.order_client.R;
import edu.feicui.order_client.base.BaseActivity;
import edu.feicui.order_client.db.DBHelper;
import edu.feicui.order_client.db.TableDBWrapper;
import edu.feicui.order_client.net.Downloader;
import edu.feicui.order_client.net.OnDownloaderListener;
import edu.feicui.order_client.util.CheckData;
import edu.feicui.order_client.util.Constants;

/**
 * 换桌、并桌
 * 
 * @author Sogrey
 * 
 */
public class ChangeTableActivity extends BaseActivity implements
		OnClickListener, OnDownloaderListener {

	protected String mUser;
	protected int mType;
	/** 登陆提交数据队列 */
	Map<String, String> mMap;
	/** 向服务器提交请求对象 */
	Downloader mDownloader;
	protected TextView mTxtTableA, mTxtTableB;
	protected Spinner mSprTableA, mSprTableB;
	protected Button mBtnBack, mBtnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_table);
		initComps();
		initViews();
	}

	private void initComps() {
		mMap = new HashMap<String, String>();
		mDownloader = new Downloader();
		mDownloader.setOnDownloaderListener(this);
	}

	private void initViews() {
		mTxtTableA = (TextView) findViewById(R.id.txt_changetable_a);
		mTxtTableB = (TextView) findViewById(R.id.txt_changetable_b);
		mSprTableA = (Spinner) findViewById(R.id.spr_changetable_a);
		mSprTableB = (Spinner) findViewById(R.id.spr_changetable_b);
		mBtnBack = (Button) findViewById(R.id.btn_changetable_back);
		mBtnBack.setOnClickListener(this);
		mBtnOk = (Button) findViewById(R.id.btn_changetable_ok);
		mBtnOk.setOnClickListener(this);

		Intent intent = getIntent();
		mType = intent.getIntExtra(Constants.CHANGE_TABLE_NAME, 0);
		mUser = intent.getStringExtra(Constants.USER_NAME);

		Cursor cursor = TableDBWrapper.getInstance(this).rawQueryRank();
		List<String> tableId = new ArrayList<String>();
		while (cursor.moveToNext()) {
			int index = cursor.getColumnIndex(DBHelper.COLUMN_TABLE_NUM);
			String num = cursor.getInt(index) + "";
			tableId.add(num);
		}
		String[] tableStrings = tableId.toArray(new String[tableId.size()]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, tableStrings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		cursor.close();

		switch (mType) {
		case Constants.CHANGE_TABLE:// 换台
			mTxtTableA.setText(R.string.txt_changetable_currant);
			mTxtTableB.setText(R.string.txt_changetable_chenged);
			mSprTableA.setPromptId(R.string.txt_changetable_currant);
			mSprTableB.setPromptId(R.string.txt_changetable_chenged);
			break;
		case Constants.MERGE_TABLE:// 并台
			mTxtTableA.setText(R.string.txt_changetable_table_a);
			mTxtTableB.setText(R.string.txt_changetable_table_b);
			mSprTableA.setPromptId(R.string.txt_changetable_table_a);
			mSprTableB.setPromptId(R.string.txt_changetable_table_b);
			break;

		default:
			break;
		}
		mSprTableA.setAdapter(adapter);
		mSprTableB.setAdapter(adapter);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_changetable_back:// 返回键
			finish();
			break;
		case R.id.btn_changetable_ok:// 确定键
			updataTable();
			break;

		default:
			break;
		}
	}

	/**
	 * 更新桌号：换桌/并桌
	 */
	private void updataTable() {
		switch (mType) {
		case Constants.CHANGE_TABLE:// 换台
		case Constants.MERGE_TABLE:// 并台
			showDialog(0);// 这里随便传值，没有用到
			break;

		default:
			break;
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String tableA = mSprTableA.getSelectedItem().toString();
		final String tableB = mSprTableB.getSelectedItem().toString();
		SimpleDateFormat format = new SimpleDateFormat(
				Constants.FMT_SERIAL_NUMBER);
		Random random = new Random();
		int intRandom = random.nextInt(1000);
		final String strRandom = initRandom(intRandom);
		; // 表示 创建随机数 [0,1000)
			// 订单号格式：8位日期+4位时间+3位桌号B+3位桌号A+“1”（用于合桌换桌用）+3位不重复随机数
		final String dateString = format.format(new Date());
		switch (mType) {
		case Constants.CHANGE_TABLE:// 换台
			builder.setTitle(R.string.tit_changetable_changetable);
			builder.setMessage(getString(R.string.txt_changetable_chenged_ok,
					tableA, tableB));
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changeTable = dateString + tableB + tableA
									+ "1" + strRandom;
							Log.d("换台", changeTable);
							mMap.put("orders", changeTable);
							mMap.put("waiter", mUser);
							mDownloader.execute(Constants.PATH_CHANGE_TABLE,
									mDownloader.HTTP_POST, mMap);
							Toast.makeText(ChangeTableActivity.this, "换台",
									Toast.LENGTH_SHORT).show();
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			return builder.create();
		case Constants.MERGE_TABLE:// 并台
			builder.setTitle(R.string.tit_changetable_merge);
			builder.setMessage(getString(R.string.txt_changetable_merge_ok,
					tableA, tableB));
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changeTable = dateString + tableB + tableA
									+ "2" + strRandom;
							Log.d("并台", changeTable);
							mMap.put("orders", changeTable);
							mMap.put("waiter", mUser);
							mDownloader.execute(Constants.PATH_CHANGE_TABLE,
									mDownloader.HTTP_POST, mMap);
							Toast.makeText(ChangeTableActivity.this, "并台",
									Toast.LENGTH_SHORT).show();
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			return builder.create();

		default:
			return super.onCreateDialog(id);
		}
	}

	/**
	 * 生成三位随机数<br>
	 * 生成的随机数可能不足三位，不足为补零处理<br>
	 * 
	 * @param intRandom
	 *            需要处理的随机数
	 */
	private String initRandom(int random) {
		String str;
		if (random < 100)
			str = "0" + random;
		else if (random < 10)
			str = "00" + random;
		else
			str = "" + random;
		return str;
	}

	@Override
	public void onBeforeDownload() {
	}

	@Override
	public void onProgressChanged(int totleSize, int countSize) {
	}

	@Override
	public void onAfterDownload(File file) {
		JSONObject json = CheckData.checkRT(file);
		Toast.makeText(this, json.optString("rtmsg"), Toast.LENGTH_LONG).show();
		finish();
	}
}
