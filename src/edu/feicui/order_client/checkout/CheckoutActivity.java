package edu.feicui.order_client.checkout;

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
 * 结算界面
 * 
 * @author Sogrey
 * 
 */
public class CheckoutActivity extends BaseActivity implements OnClickListener,
		OnDownloaderListener {

	/** 当前使用账户名 */
	protected String mUser;
	/** 当前订单 */
	protected String mNumber;
	/** 向服务器提交数据参数队列 */
	Map<String, String> mMap;
	/** 向服务器提交请求对象 */
	Downloader mDownloader;
	/**总价*/
	protected int mTotlePrice;
	LstAdapter mMyAdapter;
	/** 序号 */
	int mIndex;
	/** 详单数据集合 */
	ArrayList<ItemData> mLists;
	/** 结算界面表头-流水、总价、会员名 */
	protected TextView mTXtCheckoutHead;
	/** 会员名 */
	protected EditText mEdtCustomerName;
	/** 结算详单列表 */
	protected ListView mLstCheckourInfo;
	/** 返回/确定按钮 */
	protected Button mBtnBack, mBtnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout_info);
		initData();
		initViews();
	}

	private void initData() {
		Intent intent = getIntent();
		mUser = intent.getStringExtra(Constants.USER_NAME);
		mNumber = intent.getStringExtra(Constants.NUMBER);
		mLists = new ArrayList<ItemData>();
		mMap = new HashMap<String, String>();
		mDownloader = new Downloader();
		mDownloader.setOnDownloaderListener(this);
		mMap.put("orders", mNumber);
		mDownloader.execute(Constants.PATH_QUERY_OREDER, mDownloader.HTTP_GET,
				mMap);
	}

	private void initViews() {
		mTXtCheckoutHead = (TextView) findViewById(R.id.txt_checkout_info_head);
		mEdtCustomerName = (EditText) findViewById(R.id.edt_checkout_info_vipname);
		mLstCheckourInfo = (ListView) findViewById(R.id.lst_checkout_info);
		mBtnBack = (Button) findViewById(R.id.btn_checkout_info_back);
		mBtnBack.setOnClickListener(this);
		mBtnOk = (Button) findViewById(R.id.btn_checkout_info_ok);
		mBtnOk.setOnClickListener(this);
		 mMyAdapter=new LstAdapter();
		 mLstCheckourInfo.setAdapter(mMyAdapter);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_checkout_info_back:// 返回
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.tit_checkout_info);
			builder.setMessage(R.string.msg_checkout_info);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			builder.create().show();
			break;
		case R.id.btn_checkout_info_ok:// 确定
			String customer =mEdtCustomerName.getText().toString();
			mMap.put("orders", mNumber);
			mMap.put("waiter", mUser);
			mMap.put("cus", customer);
			mMap.put("sum", mTotlePrice+"");
			Downloader downloader = new Downloader();
			downloader.setOnDownloaderListener(new OnDownloaderListener() {
				
				@Override
				public void onProgressChanged(int totleSize, int countSize) {}
				
				@Override
				public void onBeforeDownload() {}
				
				@Override
				public void onAfterDownload(File file) {
					JSONObject json = CheckData.checkRT(file);
					Toast.makeText(CheckoutActivity.this,json.optString("rtmsg"), Toast.LENGTH_LONG).show();
					CheckoutActivity.this.finish();
				}
			});
			downloader.execute(Constants.PATH_PAY_MONEY, mDownloader.HTTP_GET,
					mMap);
			break;

		default:
			break;
		}
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
		if (200 == json.optInt("rt", -1)) {
			ItemData data = null;
			JSONArray arrays = json.optJSONArray("list");
			MenuDBWrapper dbWrapper = MenuDBWrapper
					.getInstance(getApplication());
			Cursor cursor = null;

			for (int i = 0; i < arrays.length(); i++) {
				try {
					data = new ItemData();
					JSONArray array = arrays.getJSONArray(i);

					cursor = dbWrapper.queryRank(array.getInt(0));
					while (cursor.moveToNext()) {
						int nameIndex = cursor
								.getColumnIndex(DBHelper.COLUMN_MENU_NAME);
						String name = cursor.getString(nameIndex);
						data.strName = name;// 菜名 - 0
						int unitPriceIndex = cursor
								.getColumnIndex(DBHelper.COLUMN_MENU_PRICE);
						int unitPrice = cursor.getInt(unitPriceIndex);
						data.intUnitPrice = unitPrice;// 单价- 1
						int categoryIndex = cursor
								.getColumnIndex(DBHelper.COLUMN_MENU_CATEGORY);
						int category = cursor.getInt(categoryIndex);
						String type = CheckType(category);
						data.strType = type;// 菜类- 2
					}
					data.intNumber = array.getInt(2);// 4:数量
					// 3：state ；；5：备注
					data.intPrice = data.intUnitPrice * data.intNumber;
					mTotlePrice+=data.intPrice;
					 
					 mTXtCheckoutHead.setText(getString(R.string.txt_checkout_info_head, mNumber,mTotlePrice+""));
					if (cursor != null) {
						cursor.close();
					}
					mLists.add(data);
					mMyAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		Toast.makeText(this, json.optString("rtmsg"), Toast.LENGTH_SHORT).show();
		
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.item_checkout_info,
						null);
				holder.txtIndex = (TextView) view
						.findViewById(R.id.item_checkout_info_index);
				holder.txtName = (TextView) view
						.findViewById(R.id.item_checkout_info_dishname);
				holder.txtUnitPrice = (TextView) view
						.findViewById(R.id.item_checkout_info_unitpice);
				holder.txtNumber = (TextView) view
						.findViewById(R.id.item_checkout_info_number);
				holder.txtPrice = (TextView) view
						.findViewById(R.id.item_checkout_info_price);
				holder.txtType = (TextView) view
						.findViewById(R.id.item_checkout_info_type);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.txtIndex.setText((++mIndex) + "");// 序号
			holder.txtName.setText(mLists.get(position).strName);//菜名
			holder.txtUnitPrice.setText(mLists.get(position).intUnitPrice+"");
			holder.txtNumber.setText(mLists.get(position).intNumber+"");
			holder.txtPrice.setText(mLists.get(position).intPrice+"");
			holder.txtType.setText(mLists.get(position).strType);
			return view;
		}
	}

	class ViewHolder {
		TextView txtIndex, txtName, txtUnitPrice, txtNumber, txtPrice, txtType;
	}

	class ItemData {
		String strName, strType;
		int intUnitPrice, intNumber, intPrice;
	}
}
