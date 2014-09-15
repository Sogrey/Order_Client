package edu.feicui.order_client.choosedish;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
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
 * 点菜详情列表界面
 * 
 * @author Sogrey
 */
public class ChooseInfoActivity extends BaseActivity implements
		OnClickListener, OnDownloaderListener {

	/** 当前使用账户名 */
	protected String mUser;
	/** 点餐区分-点餐/加单 */
	protected int mType;
	/** 当前订单 */
	protected String mNumber;
	/** 点餐桌号 */
	protected String mTableId;
	/** 返回按钮对话框 */
	public static final int DIALOG_BACK = 0x01;
	/** 确定按钮对话框 */
	public static final int DIALOG_OK = 0x02;
	/** 点菜菜单列表 */
	ArrayList<String> mList;
	/** 点菜菜单详情列表 */
	static ArrayList<ArrayList<String>> mListInfos;
	/** 生成的订单流水号 */
	String serialNumber;
	/** 结单总价 */
	protected int mTotlePrice;
	/** 登陆提交数据队列 */
	Map<String, String> map;
	/** 向服务器提交请求对象 */
	Downloader downloader;
	/** 流水号 */
	protected TextView mTxtSerialNumber;
	/** 点菜详情列表 */
	protected ListView mLstInfos;
	/** 返回按钮 */
	protected Button mBtnBack;
	/** 确定菜单按钮按钮 */
	protected Button mBtnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_infos);
		initDatas();
		initViews();
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		Intent intent = getIntent();
		mUser = intent.getStringExtra(Constants.USER_NAME);
		mType = intent.getIntExtra(Constants.CHOOSEDISH, 0);
		mTableId = intent.getStringExtra(Constants.CHOOSE_TABLE_ID);
		mNumber = intent.getStringExtra(Constants.NUMBER);// 订单号
		if (mNumber == null) {
			SimpleDateFormat format = new SimpleDateFormat(
					Constants.FMT_SERIAL_NUMBER);
			Random random = new Random();
			int intRandom = random.nextInt(1000); // 表示 创建随机数 [0,1000)
			// 订单号格式：8位日期+4位时间+3位桌号+4位“0”（用于合桌换桌用）+3位不重复随机数
			serialNumber = format.format(new Date()) + mTableId + "0000"
					+ intRandom;
		} else {
			serialNumber = mNumber;
		}
		mList = intent.getStringArrayListExtra("list");
		checkList(mList);
	}

	/**
	 * @param list
	 *            点菜单
	 */
	private void checkList(ArrayList<String> list) {
		mListInfos = new ArrayList<ArrayList<String>>();
		ArrayList<String> arrayList;
		MenuDBWrapper dBWrapper = MenuDBWrapper.getInstance(getApplication());
		int unitPrice;// 单价
		int category;// 菜单代号
		String type = "";// 菜类
		Cursor cursor = null;
		for (String name : list) {
			cursor = dBWrapper.rawQueryRank(name);
			while (cursor.moveToNext()) {
				arrayList = new ArrayList<String>();
				unitPrice = cursor.getInt(cursor
						.getColumnIndex(DBHelper.COLUMN_MENU_PRICE));
				category = cursor.getInt(cursor
						.getColumnIndex(DBHelper.COLUMN_MENU_CATEGORY));
				type = CheckType(category);
				arrayList.add(name);// 菜名0
				arrayList.add(unitPrice + "");// 单价1
				arrayList.add(type);// 菜类别2
				arrayList.add(1 + "");// 数量3
				arrayList.add("");// 备注4

				mListInfos.add(arrayList);
			}
		}
		if (cursor != null) {
			cursor.close();
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

	private void initViews() {
		mTxtSerialNumber = (TextView) findViewById(R.id.txt_info_serial_number);
		mLstInfos = (ListView) findViewById(R.id.lst_infos);
		mBtnBack = (Button) findViewById(R.id.btn_infos_back);
		mBtnBack.setOnClickListener(this);
		mBtnOk = (Button) findViewById(R.id.btn_infos_ok);
		mBtnOk.setOnClickListener(this);
		mLstInfos.setAdapter(new LstAdapter());
		mTxtSerialNumber.setText(getString(R.string.txt_infos_serial_number,
				serialNumber));
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_infos_back:// 返回键
			showDialog(DIALOG_BACK);
			break;
		case R.id.btn_infos_ok:// 确定键
			showDialog(DIALOG_OK);
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			showDialog(DIALOG_BACK);
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_BACK:// 返回提示对话框
			builder.setTitle(R.string.tit_infos_back);
			builder.setMessage(R.string.msg_infos_back);
			builder.setPositiveButton(R.string.ok,
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			return builder.create();
		case DIALOG_OK:// 提交订单对话框
			builder.setTitle(R.string.tit_infos_ok);
			builder.setMessage(getString(R.string.msg_infos_ok, mUser,
					serialNumber));
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							JSONObject jsonObject = submitOrder(serialNumber,
									mUser, "1", "备注", mTotlePrice + "");// 提交订单
							System.out.println(jsonObject.toString());
							downloader = new Downloader();
							map = new HashMap<String, String>();
							downloader
									.setOnDownloaderListener(ChooseInfoActivity.this);
							map.put("list", jsonObject.toString());
							switch (mType) {
							case Constants.CHOOSE_ORDER:// 点餐
								downloader.execute(Constants.PATH_OREDER,
										Downloader.HTTP_POST, map);
								break;
							case Constants.CHOOSE_ADD_ORDER:// 加单
								downloader.execute(Constants.PATH_ADD_ORDER,
										Downloader.HTTP_POST, map);

								break;

							default:
								break;
							}
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			return builder.create();
			
		case Constants.DIALOG_ID_UPDATA:// 更新数据对话框
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage(getString(R.string.hint_dialog_updating));
			return dialog;
		default:
			return super.onCreateDialog(id);
		}
	}

	/** 提交订单-向服务器提交订单信息,返回提交状态码 */
	private JSONObject submitOrder(String orderId, String username, String cus,
			String status, String sum) {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject order = null;
		int index = 0;
		for (ArrayList<String> list : mListInfos) {
			order = new JSONObject();
			String name = list.get(0);// 菜名
			// list.get(1);//單價
			// list.get(2);//菜類
			// list.get(3);//數量
			// list.get(4);//备注
			String MenuId = null;
			MenuDBWrapper dbWrapper = MenuDBWrapper.getInstance(this);
			Cursor cursor = dbWrapper.rawQueryRank(name);
			while (cursor.moveToNext()) {
				int indexMenuID = cursor
						.getColumnIndex(DBHelper.COLUMN_MENU_MENUID);
				MenuId = cursor.getInt(indexMenuID) + "";
			}
			cursor.close();
			String note;
			if (TextUtils.isEmpty(list.get(4))) {
				note = "无";
			} else {
				note = list.get(4);
			}
			try {
				order.put("list" + index++, MenuId + "," + list.get(3) + ","
						+ note);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if (mListInfos != null) {
				try {
					jsonObject.put("orders", orderId);
					jsonObject.put("name", username);
					jsonObject.put("cus", cus);
					jsonObject.put("status", status);
					jsonObject.put("sum", sum);
					jsonArray.put(order);
					jsonObject.put("list", jsonArray);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonObject;
	}

	//  向服务器提交订单信息,返回提交状态码public static HashMap<String,
	// Object> submitOrder(String orderId,String memberNum, String username,
	// ArrayList<Dish> dishList)
	// 1) 初始化一个JSONObject对象；
	// 2) 封装数据到JSONObject对象中；
	// 3) 初始化一个JSONArray对象；
	// 4) 封装数据到JSONArray对象中；
	// 5) 配置参数；
	// 6) 提交订单，接收返回信息；
	// 7) 将接收到的的信息返回。


	@Override
	public void onBeforeDownload() {
		showDialog(Constants.DIALOG_ID_UPDATA);// 显示读取数据对话框
	}

	@Override
	public void onProgressChanged(int totleSize, int countSize) {

	}

	@Override
	public void onAfterDownload(File file) {
		dismissDialog(Constants.DIALOG_ID_UPDATA);// 隐藏读取数据对话框
		JSONObject json = CheckData.checkRT(file);
		Toast.makeText(this, json.optString("rtmsg"), Toast.LENGTH_LONG).show();
		if (200==json.optInt("rt",-1)) {
			this.finish();
		}
	}

	class LstAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mListInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return mListInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mListInfos.get(position).hashCode();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.item_infos_list,
						null);
				holder.txtIndex = (TextView) view
						.findViewById(R.id.txt_infos_list_item_index);
				holder.txtName = (TextView) view
						.findViewById(R.id.txt_infos_list_item_dishname);
				holder.txtUnitPrice = (TextView) view
						.findViewById(R.id.txt_infos_list_item_unitprice);
				holder.btnLeft = (Button) view
						.findViewById(R.id.btn_infos_list_item_minus);
				holder.btnRight = (Button) view
						.findViewById(R.id.btn_infos_list_item_add);
				holder.edtNumber = (EditText) view
						.findViewById(R.id.edt_infos_list_item_number);
				holder.txtPrice = (TextView) view
						.findViewById(R.id.txt_infos_list_item_price);
				holder.btnNote = (Button) view
						.findViewById(R.id.btn_infos_list_item_note);
				holder.txtType = (TextView) view
						.findViewById(R.id.txt_infos_list_item_type);
				holder.btnDelete = (Button) view
						.findViewById(R.id.btn_infos_list_item_delete);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.txtName.setText(mListInfos.get(position).get(0));// 菜名
			holder.txtUnitPrice.setText(mListInfos.get(position).get(1));// 單價
			holder.txtType.setText(mListInfos.get(position).get(2));// 菜類
			holder.edtNumber.setText(mListInfos.get(position).get(3));// 數量
			holder.txtPrice.setText(Integer.parseInt(holder.edtNumber.getText()
					.toString())
					* Integer
							.parseInt(holder.txtUnitPrice.getText().toString())
					+ "");// 單菜總價
			holder.txtIndex.setText((position+1) + "");

			mTotlePrice += Integer.parseInt(holder.txtPrice.getText()
					.toString());
			

			holder.btnLeft.setClickable(true);
			holder.btnLeft.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int edtNum = Integer.parseInt(holder.edtNumber.getText()
							.toString());
					if (edtNum > 1) {
						edtNum -= 1;
						holder.edtNumber.setText(edtNum + "");
						holder.txtPrice.setText(Integer
								.parseInt(holder.edtNumber.getText().toString())
								* Integer.parseInt(holder.txtUnitPrice
										.getText().toString()) + "");
						mListInfos.get(position).set(3, edtNum + "");

						mTotlePrice -= Integer.parseInt(holder.txtUnitPrice
								.getText().toString());
					}
				}
			});

			holder.btnRight.setClickable(true);
			holder.btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int edtNum = Integer.parseInt(holder.edtNumber.getText()
							.toString());
					edtNum += 1;
					holder.edtNumber.setText(edtNum + "");
					holder.txtPrice.setText(Integer.parseInt(holder.edtNumber
							.getText().toString())
							* Integer.parseInt(holder.txtUnitPrice.getText()
									.toString()) + "");
					mListInfos.get(position).set(3, edtNum + "");

					mTotlePrice += Integer.parseInt(holder.txtUnitPrice
							.getText().toString());
				}
			});

			holder.btnNote.setClickable(true);
			holder.btnNote.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ChooseInfoActivity.this);
					builder.setTitle("备注");
					final EditText edtText = new EditText(
							ChooseInfoActivity.this);
					edtText.setText(mListInfos.get(position).get(4));
					builder.setView(edtText);
					builder.setPositiveButton(R.string.ok,
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String note = edtText.getText().toString();
									mListInfos.get(position).set(4, note);
								}
							});
					builder.setNegativeButton(R.string.cancel, null);
					builder.show();
				}
			});

			holder.btnDelete.setClickable(true);
			holder.btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ChooseInfoActivity.this);
					builder.setTitle("删除");
					builder.setMessage(getString(R.string.msg_infos_delete,
							holder.txtName.getText()));
					builder.setPositiveButton(R.string.ok,
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mListInfos.remove(position);
									notifyDataSetChanged();// 通知适配器变化
								}
							});
					builder.setNegativeButton(R.string.cancel, null);
					builder.show();
				}
			});
			return view;
		}
	}

	class ViewHolder {
		TextView txtIndex;
		TextView txtName;
		TextView txtUnitPrice;
		Button btnLeft;
		EditText edtNumber;
		Button btnRight;
		TextView txtPrice;
		Button btnNote;
		TextView txtType;
		Button btnDelete;
	}
}
