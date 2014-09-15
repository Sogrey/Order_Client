package edu.feicui.order_client.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

import edu.feicui.order_client.R;
import edu.feicui.order_client.base.BaseActivity;

/**
 * @author Sogrey
 *
 */
public class CheckData{
	
	CheckData(){
		
	}

	public static JSONObject checkRT(File file) {
		FileReader reader = null;
		BufferedReader br = null;
		JSONObject json = null ;
		try {
			reader = new FileReader(file);
			br = new BufferedReader(reader);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			json= new JSONObject(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	


}
