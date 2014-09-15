package edu.feicui.order_client.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON工具類，将数据转换为Json
 * 
 * @author Sogrey
 * 
 */
public class JsonTools {
	public JsonTools() {
	}

	/**
	 * @param key
	 *            表示json字符串的头信息
	 * @param value
	 *            是对解析的集合的类型
	 * @return
	 */
	// 将数据转换为JSONObject
	public static String createJsonString(String key, Object value) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	// 将数据转换为JSONArray
	public static String createJsonArrayString(String menuId, String number,
			String remark) {
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray.put(0, menuId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			jsonArray.put(1, number);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			jsonArray.put(2, remark);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray.toString();
	}

	class Dish {
		public String menuId;
		public String number;
		public String remark;

		public Dish() {
		}

		public Dish(String menuId, String number, String remark) {
			this.menuId = menuId;
			this.number = number;
			this.remark = remark;
		}

		public String getMenuId() {
			return menuId;
		}

		public void setMenuId(String menuId) {
			this.menuId = menuId;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		// 重写toString()方法
		// @Override
		// public String toString() {
		// return "[" + menuId // jsonArray [0]-menuId-菜品ID
		// + ", " + number // jsonArray [1]-number-數量
		// + ", " + remark // jsonArray [2]-remark-備註
		// + "]";
		// }
	}
}
