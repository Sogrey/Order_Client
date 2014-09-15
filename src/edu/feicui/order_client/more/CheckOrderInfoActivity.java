/**
 * 
 */
package edu.feicui.order_client.more;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.feicui.order_client.R;
import edu.feicui.order_client.base.BaseActivity;
import edu.feicui.order_client.db.DBHelper;
import edu.feicui.order_client.db.MenuDBWrapper;
import edu.feicui.order_client.net.Downloader;
import edu.feicui.order_client.net.OnDownloaderListener;
import edu.feicui.order_client.util.CheckData;
import edu.feicui.order_client.util.Constants;

/**
 * @author Sogrey
 * 
 */
public class CheckOrderInfoActivity extends BaseActivity implements
		OnClickListener, OnDownloaderListener {

	/** 登陆提交数据队列 */
	Map<String, String> mMap;
	/** 向服务器提交请求对象 */
	Downloader mDownloader;
	/** 当前使用账户名 */
	protected String mUser;
	/** 当前流水号 */
	protected String mNumber;
	/** 订单详情列表 */
	ArrayList<ArrayList<String>> mLists;

	LstAdapter myAdapter;
	/** 流水号 */
	protected TextView mTxtSerialNumber;
	/** 详单列表 */
	protected ListView mLstInfos;
	/** 返回和确定按钮 */
	protected Button mBtnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_order_infos);
		initData();
		initViews();
	}

	private void initData() {
		mLists = new ArrayList<ArrayList<String>>();
		Intent intent = getIntent();
		mUser = intent.getStringExtra(Constants.USER_NAME);
		mNumber = intent.getStringExtra(Constants.NUMBER);
		mMap = new HashMap<String, String>();
		mDownloader = new Downloader();
		mDownloader.setOnDownloaderListener(this);
		mMap.put("orders", mNumber);
		mDownloader.execute(Constants.PATH_QUERY_OREDER, mDownloader.HTTP_GET,
				mMap);
	}

	private void initViews() {
		mTxtSerialNumber = (TextView) findViewById(R.id.txt_info_serial_number);
		mTxtSerialNumber.setText(getString(R.string.txt_infos_serial_number,
				mNumber));
		mLstInfos = (ListView) findViewById(R.id.lst_infos);
		mBtnBack = (Button) findViewById(R.id.btn_infos_back);
		mBtnBack.setOnClickListener(this);

		myAdapter = new LstAdapter();
		mLstInfos.setAdapter(myAdapter);
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
		updateDB(json);
	}

	private void updateDB(JSONObject json) {
		ArrayList<String> list = null;
		JSONArray arrays = json.optJSONArray("list");
		MenuDBWrapper dbWrapper = MenuDBWrapper.getInstance(getApplication());
		Cursor cursor = null;
		for (int i = 0; i < arrays.length(); i++) {
			try {
				list = new ArrayList<String>();
				JSONArray array = arrays.getJSONArray(i);
				for (int j = 0; j < array.length(); j++) {
					if (0== j) {// menuID
						cursor = dbWrapper.queryRank(array.getInt(0));
						while (cursor.moveToNext()) {
							int nameIndex = cursor
									.getColumnIndex(DBHelper.COLUMN_MENU_NAME);
							String name = cursor.getString(nameIndex);
							list.add(name);// 菜名 - 0
							int unitPriceIndex = cursor
									.getColumnIndex(DBHelper.COLUMN_MENU_PRICE);
							int unitPrice = cursor.getInt(unitPriceIndex);
							list.add(unitPrice + "");// 单价- 1
							int categoryIndex = cursor
									.getColumnIndex(DBHelper.COLUMN_MENU_CATEGORY);
							int category = cursor.getInt(categoryIndex);
							String type = CheckType(category);
							list.add(type);// 菜类- 2
						}
					} else
						list.add(array.getString(j));// 3：state ；4:数量 ；5：备注
				}
				if (cursor != null) {
					cursor.close();
				}
				mLists.add(list);
				myAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/** 判断菜类 */
	public String CheckType(int category) {
		String type = null;
		switch (category) {
		case Constants.MENU_STAPLE_FOOD:
			type = getString(R.string.txt_sd_staple_food);
			break;
		case Constants.MENU_COOL_FOOD:
			type = getString(R.string.txt_sd_cool_food);
			break;
		case Constants.MENU_HOT_FOOD:
			type = getString(R.string.txt_sd_hot_food);
			break;
		case Constants.MENU_SOUP_FOOD:
			type = getString(R.string.txt_sd_soup_food);
			break;
		case Constants.MENU_DRINK_FOOD:
			type = getString(R.string.txt_sd_drink_food);
			break;
		case Constants.MENU_TABLEWARE_FOOD:
			type = getString(R.string.txt_sd_tableware_food);
			break;

		default:
			break;
		}
		return type;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_infos_back:// 返回按钮
			finish();
			break;

		default:
			break;
		}
	}

	class LstAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mLists.size();
		}

		@Override
		public Object getItem(int position) {
			return mLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mLists.get(position).hashCode();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				view = getLayoutInflater().inflate(
						R.layout.item_checkorder_infos_list, null);
				holder.txtIndex = (TextView) view
						.findViewById(R.id.txt_checkorder_info_item_index);
				holder.txtName = (TextView) view
						.findViewById(R.id.txt_checkorder_info_item_dishname);
				holder.txtUnitPrice = (TextView) view
						.findViewById(R.id.txt_checkorder_info_item_unitprice);
				holder.txtNumber = (TextView) view
						.findViewById(R.id.txt_checkorder_info_item_number);
				holder.txtPrice = (TextView) view
						.findViewById(R.id.txt_checkorder_info_item_price);
				holder.txtType = (TextView) view
						.findViewById(R.id.txt_checkorder_info_item_type);
				holder.btnNote = (Button) view
						.findViewById(R.id.btn_checkorder_info_item_note);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.txtIndex.setText((position+1) + "");// 序号
			holder.txtName.setText(mLists.get(position).get(0));// 菜名
			holder.txtUnitPrice.setText(mLists.get(position).get(1));// 单价
			holder.txtNumber.setText(mLists.get(position).get(4));// 数量
			holder.txtPrice.setText((Integer.parseInt(holder.txtUnitPrice
					.getText().toString()) * Integer.parseInt(holder.txtNumber
					.getText().toString()))
					+ "");// 价格
			holder.txtType.setText(mLists.get(position).get(2));// 菜类
			holder.btnNote.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CheckOrderInfoActivity.this);
					builder.setTitle(R.string.txt_infos_note);
					final TextView txtNote = new TextView(
							CheckOrderInfoActivity.this);
					txtNote.setText(mLists.get(position).get(5));
					builder.setView(txtNote);
					builder.setPositiveButton(R.string.ok, null);
					builder.create().show();
				}
			});
			return view;
		}
	}

	class ViewHolder {
		TextView txtIndex, txtName, txtUnitPrice, txtNumber, txtPrice, txtType;
		Button btnNote;
	}
}
