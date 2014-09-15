package edu.feicui.order_client.choosedish;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.feicui.order_client.R;
import edu.feicui.order_client.base.BaseActivity;
import edu.feicui.order_client.db.DBHelper;
import edu.feicui.order_client.db.MenuDBWrapper;
import edu.feicui.order_client.db.TableDBWrapper;
import edu.feicui.order_client.net.AsyncImageLoader;
import edu.feicui.order_client.net.AsyncImageLoader.ImageCallBack;
import edu.feicui.order_client.util.Constants;

/**
 * 点菜界面
 * 
 * @author Sogrey
 * 
 */
public class ChooseDishActivity extends BaseActivity implements
		OnClickListener, OnChildClickListener, OnGroupClickListener,
		OnItemSelectedListener {

	/** 当前使用账户名 */
	protected String mUser;
	/** 点餐区分-点餐/加单 */
	protected int mType;
	/** 当前订单 */
	protected String mNumber;
	/** 桌号表操作类 */
	public TableDBWrapper mTableDB;
	/** 菜单表操作类 */
	public MenuDBWrapper mMenuDB;
	/** 数据库查询返回的游标 */
	public Cursor mCursor;
	/** 菜类数组 */
	protected String[] mMenuType;
	/** 菜类下具体菜名集合 */
	protected List<List<String>> mMenuSet;
	/** 需要适配器处理的List */
	protected List<List<ItemData>> mList;
	/** 选择桌号按钮 */
	protected Spinner mSprTableId;
	/** 桌号 */
	protected TextView mTxtTableId;
	/** 选择种类按钮 */
	protected Spinner mSprChoosedishTypes;
	/** 点餐确定按钮 */
	protected Button mBtnChoosedishOk;
	/** 可折叠菜单列表 */
	protected ExpandableListView mEpdTypes;
	/** 菜品图片 */
	protected ImageView mImgPic;
	/** 菜名 */
	protected TextView mTxtName;
	/** 菜价 */
	protected TextView mTxtPrice;
	/** 菜品介绍 */
	protected TextView mTxtRemark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosedish);
		initDB();
		initDatas();
		initViews();
		initAdapter();
	}

	/** 初始化数据库相关 */
	private void initDB() {
		mTableDB = TableDBWrapper.getInstance(getApplication());
		mMenuDB = MenuDBWrapper.getInstance(getApplication());
	}

	/** 初始化数据 */
	private void initDatas() {
		Intent intent = getIntent();
		mUser = intent.getStringExtra(Constants.USER_NAME);
		mType = intent.getIntExtra(Constants.CHOOSEDISH, 0);
		mNumber = intent.getStringExtra(Constants.NUMBER);// 订单号
		mMenuType = getResources().getStringArray(R.array.dish_type);
		mMenuSet = new ArrayList<List<String>>();
		mList = new ArrayList<List<ItemData>>();
		mMenuSet.add(gleanStapleFood());
		mMenuSet.add(gleanHotFood());
		mMenuSet.add(gleanCoolFood());
		mMenuSet.add(gleanSoup());
		mMenuSet.add(gleanDrink());
		mMenuSet.add(gleanTableware());
	}

	/**
	 * @return 主食列表
	 */
	private List<String> gleanStapleFood() {
		List<String> stapleFood = new ArrayList<String>();
		List<ItemData> list = new ArrayList<ItemData>();
		ItemData data;
		mCursor = mMenuDB.rawQueryRank(Constants.MENU_STAPLE_FOOD);
		while (mCursor.moveToNext()) {
			data = new ItemData();
			int nameIndex = mCursor.getColumnIndex(DBHelper.COLUMN_MENU_NAME);
			String name = mCursor.getString(nameIndex);
			stapleFood.add(name);
			data.name = name;
			data.checked = false;
			list.add(data);
		}
		mList.add(list);
		mCursor.close();
		return stapleFood;
	}

	/**
	 * @return 热菜列表
	 */
	private List<String> gleanHotFood() {
		List<String> hotFood = new ArrayList<String>();
		List<ItemData> list = new ArrayList<ItemData>();
		ItemData data;
		mCursor = mMenuDB.rawQueryRank(Constants.MENU_HOT_FOOD);
		while (mCursor.moveToNext()) {
			data = new ItemData();
			int nameIndex = mCursor.getColumnIndex(DBHelper.COLUMN_MENU_NAME);
			String name = mCursor.getString(nameIndex);
			hotFood.add(name);
			data.name = name;
			data.checked = false;
			list.add(data);
		}
		mList.add(list);
		mCursor.close();
		return hotFood;
	}

	/**
	 * @return 凉菜列表
	 */
	private List<String> gleanCoolFood() {
		List<String> coolFood = new ArrayList<String>();
		List<ItemData> list = new ArrayList<ItemData>();
		ItemData data;
		mCursor = mMenuDB.rawQueryRank(Constants.MENU_COOL_FOOD);
		while (mCursor.moveToNext()) {
			data = new ItemData();
			int nameIndex = mCursor.getColumnIndex(DBHelper.COLUMN_MENU_NAME);
			String name = mCursor.getString(nameIndex);
			coolFood.add(name);
			data.name = name;
			data.checked = false;
			list.add(data);
		}
		mList.add(list);
		mCursor.close();
		return coolFood;
	}

	/**
	 * @return 汤羹列表
	 */
	private List<String> gleanSoup() {
		List<String> soup = new ArrayList<String>();
		List<ItemData> list = new ArrayList<ItemData>();
		ItemData data;
		mCursor = mMenuDB.rawQueryRank(Constants.MENU_SOUP_FOOD);
		while (mCursor.moveToNext()) {
			data = new ItemData();
			int nameIndex = mCursor.getColumnIndex(DBHelper.COLUMN_MENU_NAME);
			String name = mCursor.getString(nameIndex);
			soup.add(name);
			data.name = name;
			data.checked = false;
			list.add(data);
		}
		mList.add(list);
		mCursor.close();
		return soup;
	}

	/**
	 * @return 饮料列表
	 */
	private List<String> gleanDrink() {
		List<String> drink = new ArrayList<String>();
		List<ItemData> list = new ArrayList<ItemData>();
		ItemData data;
		mCursor = mMenuDB.rawQueryRank(Constants.MENU_DRINK_FOOD);
		while (mCursor.moveToNext()) {
			data = new ItemData();
			int nameIndex = mCursor.getColumnIndex(DBHelper.COLUMN_MENU_NAME);
			String name = mCursor.getString(nameIndex);
			drink.add(name);
			data.name = name;
			data.checked = false;
			list.add(data);
		}
		mList.add(list);
		mCursor.close();
		return drink;
	}

	/**
	 * @return 餐具列表
	 */
	private List<String> gleanTableware() {
		List<String> Tableware = new ArrayList<String>();
		List<ItemData> list = new ArrayList<ItemData>();
		ItemData data;
		mCursor = mMenuDB.rawQueryRank(Constants.MENU_TABLEWARE_FOOD);
		while (mCursor.moveToNext()) {
			data = new ItemData();
			int nameIndex = mCursor.getColumnIndex(DBHelper.COLUMN_MENU_NAME);
			String name = mCursor.getString(nameIndex);
			Tableware.add(name);
			data.name = name;
			data.checked = false;
			list.add(data);
		}
		mList.add(list);
		mCursor.close();
		return Tableware;
	}

	private void initViews() {
		mImgPic = (ImageView) findViewById(R.id.img_include_choosedish_pic);
		mTxtName = (TextView) findViewById(R.id.txt_include_choosedish_info_name);
		mTxtPrice = (TextView) findViewById(R.id.txt_include_choosedish_info_price);
		mTxtRemark = (TextView) findViewById(R.id.txt_include_choosedish_info);

		mSprTableId = (Spinner) findViewById(R.id.spr_choosedish_tableid);
		mTxtTableId = (TextView) findViewById(R.id.txt_choosedish_tableid);
		mSprChoosedishTypes = (Spinner) findViewById(R.id.spr_choosedish_types);
		mBtnChoosedishOk = (Button) findViewById(R.id.btn_choosedish_ok);
		mBtnChoosedishOk.setOnClickListener(this);
		mEpdTypes = (ExpandableListView) findViewById(R.id.epd_types);

		// mSprTableId.setOnItemSelectedListener(this);
		mSprChoosedishTypes.setOnItemSelectedListener(this);

	}

	private void initAdapter() {
		{
			switch (mType) {
			case Constants.CHOOSE_ORDER:// 点餐
				mTxtTableId.setVisibility(View.GONE);
				mSprTableId.setVisibility(View.VISIBLE);
				mCursor = mTableDB.rawQueryRank();
				List<String> tableId = new ArrayList<String>();
				while (mCursor.moveToNext()) {
					int index = mCursor
							.getColumnIndex(DBHelper.COLUMN_TABLE_NUM);
					String num = mCursor.getInt(index) + "";
					tableId.add(num);
				}
				String[] tableStrings = tableId.toArray(new String[tableId
						.size()]);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, tableStrings);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSprTableId.setPromptId(R.string.txt_tableid);
				mSprTableId.setAdapter(adapter);
				mCursor.close();
				break;
			case Constants.CHOOSE_ADD_ORDER:// 加单
				mTxtTableId.setVisibility(View.VISIBLE);
				mSprTableId.setVisibility(View.GONE);
				String tableNum = mNumber.substring(12, 15);// 从订单流水中抽取桌号
				mTxtTableId.setText(tableNum);
				break;
			default:
				break;
			}

		}
		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, mMenuType);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSprChoosedishTypes.setPromptId(R.string.txt_types);
			mSprChoosedishTypes.setAdapter(adapter);
			mCursor.close();
		}
		mEpdTypes.setAdapter(new EpdAdapter());
		mEpdTypes.setOnGroupClickListener(this);
		mEpdTypes.setOnChildClickListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mEpdTypes.expandGroup(position, true);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/**
	 * 分组点击事件监听
	 * 
	 * @param parent
	 *            ExpandableListView组件
	 * @param view
	 *            被点击的分组条目
	 * @param groupPosition
	 *            被点击组编号
	 * @param id
	 *            被点击组ID
	 * @return true:已折叠的展不开，已展开的收不拢；false：正常展开、收拢
	 * */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View view,
			int groupPosition, long id) {
		Toast.makeText(this, mMenuType[groupPosition], Toast.LENGTH_SHORT)
				.show();
		return false;
	}

	/**
	 * 分组子条目点击事件监听
	 * 
	 * @param parent
	 *            ExpandableListView组件
	 * @param view
	 *            被点击的分组子条目
	 * @param groupPosition
	 *            被点击组编号
	 * @param childPosition
	 *            被点击组子条目编号
	 * @param id
	 *            被点击组子条目ID
	 * @return true:已折叠的展不开，已展开的收不拢；false：正常展开、收拢
	 * */
	@Override
	public boolean onChildClick(ExpandableListView parent, View view,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(this, mMenuSet.get(groupPosition).get(childPosition),
				Toast.LENGTH_SHORT).show();
		ChildHolder holder = (ChildHolder) view.getTag();
		ItemData data = mList.get(groupPosition).get(childPosition);
		holder.chbChild.setChecked(!holder.chbChild.isChecked());
		data.checked = holder.chbChild.isChecked();
		upDateMenuInfo(mMenuSet.get(groupPosition).get(childPosition));
		return false;
	}

	/**
	 * @param string
	 *            被点击的菜目
	 * 
	 */
	private void upDateMenuInfo(String name) {
		mCursor = mMenuDB.rawQueryRank(name);
		while (mCursor.moveToNext()) {
			String pic = mCursor.getString(mCursor
					.getColumnIndex(DBHelper.COLUMN_MENU_PIC));
			int price = mCursor.getInt(mCursor
					.getColumnIndex(DBHelper.COLUMN_MENU_PRICE));
			String remark = mCursor.getString(mCursor
					.getColumnIndex(DBHelper.COLUMN_MENU_REMARK));

			String picUrl = Constants.PATH_SERVER + "/" + pic;
			AsyncImageLoader loader = new AsyncImageLoader();
			loader.LoadImage(picUrl, new ImageCallBack() {

				@Override
				public void onImageLoaded(String url, Bitmap bmp) {
					mImgPic.setImageBitmap(bmp);
				}
			});
			mTxtName.setText(name);
			mTxtPrice.setText(getString(R.string.txt_infos_unitprice, price));
			mTxtRemark.setText(remark);
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_choosedish_ok:// 点餐确定按钮
			Intent intent = new Intent();
			intent.setClass(this, ChooseInfoActivity.class);
			switch (mType) {
			case Constants.CHOOSE_ORDER:// 点餐
				intent.putExtra(Constants.CHOOSEDISH, Constants.CHOOSE_ORDER);
				intent.putExtra(Constants.CHOOSE_TABLE_ID, mSprTableId
						.getSelectedItem().toString());// 桌号
				break;
			case Constants.CHOOSE_ADD_ORDER:// 加单
				intent.putExtra(Constants.NUMBER, mNumber);// 加单，原订单流水号
				intent.putExtra(Constants.CHOOSEDISH,
						Constants.CHOOSE_ADD_ORDER);
				break;

			default:
				break;
			}
			ArrayList<String> list = new ArrayList<String>();
			for (List<ItemData> iterable_element : mList) {
				for (ItemData data : iterable_element) {
					if (data.checked) {
						list.add(data.name);
					}
				}
			}
			intent.putExtra(Constants.USER_NAME, mUser);
			intent.putStringArrayListExtra("list", list);
			startActivity(intent);
			this.finish();
			break;

		default:
			break;
		}
	}

	class EpdAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return mMenuType == null ? 0 : mMenuType.length;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return mMenuSet == null || groupPosition >= mMenuSet.size()
					|| mMenuSet.get(groupPosition) == null ? 0 : mMenuSet.get(
					groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mMenuType[groupPosition];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return mMenuSet.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return mMenuType[groupPosition].hashCode();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return mMenuSet.get(groupPosition).get(childPosition).hashCode();
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = convertView;
			GroupHolder holder;
			if (convertView == null) {
				holder = new GroupHolder();
				view = getLayoutInflater().inflate(
						R.layout.item_expendlist_group, null);
				holder.txtGroup = (TextView) view
						.findViewById(R.id.txt_expendlist_group_name);
				view.setTag(holder);
			} else {
				holder = (GroupHolder) view.getTag();
			}
			holder.txtGroup.setText(mMenuType[groupPosition]);
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			View view = convertView;
			ChildHolder holder;
			if (convertView == null) {
				holder = new ChildHolder();
				view = getLayoutInflater().inflate(
						R.layout.item_expendlist_children, null);
				holder.txtChild = (TextView) view
						.findViewById(R.id.txt_expendlist_child_name);
				holder.chbChild = (CheckBox) view
						.findViewById(R.id.chb_expendlist_child);
				view.setTag(holder);
			} else {
				holder = (ChildHolder) view.getTag();
			}
			ItemData data = mList.get(groupPosition).get(childPosition);
			// holder.txtChild.setText(mMenuSet.get(groupPosition).get(
			// childPosition));
			holder.txtChild.setText(data.name);
			holder.chbChild.setChecked(data.checked);
			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	class GroupHolder {
		TextView txtGroup;
	}

	class ChildHolder {
		TextView txtChild;
		CheckBox chbChild;
	}

	class ItemData {
		String name;
		boolean checked = false;
	}
}
