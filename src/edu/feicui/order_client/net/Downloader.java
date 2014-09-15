package edu.feicui.order_client.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import edu.feicui.order_client.util.Constants;

import android.os.AsyncTask;
import android.util.Log;

public class Downloader extends AsyncTask<Object, Integer, File> {
	// 只有为一个线程时，不需要线程同步StringBuilder
	/** get请求 */
	public final static int HTTP_GET = 0x01;
	/** post请求 */
	public final static int HTTP_POST = 0x02;
	
	/** 访问网络的监听接口 */
	protected OnDownloaderListener mDownloaderListener;

	public void setOnDownloaderListener(OnDownloaderListener listener) {
		mDownloaderListener = listener;
	}

	@Override
	protected File doInBackground(Object... params) {
		int sizeTotal = 0, sizeCount = 0;

		InputStream is = null;
		File file = null;
		FileOutputStream fos = null;
		OutputStream os = null;
		try {
			String path = (String) params[0];
			int method = (Integer) params[1];
			String args = "";
			Map<String, String> map = (Map<String, String>) params[2];
			if (map != null) {
				args = makeArgs(map);
			}

			if (method == HTTP_GET)
				path += "?" + args;
			URL url = new URL(path);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			if (HTTP_GET == method)
				http.setRequestMethod("GET");
			else {
				http.setRequestMethod("POST");
				http.setDoOutput(true);
			}
			// {
			// 将args传送到请求中
			// }
			// 获取数据
			// http.setDoInput(true);
			// 不使用缓存
			http.setUseCaches(false);
			// 连接服务器
			http.connect();
			if (HTTP_POST==method) {
				os = http.getOutputStream();
				os.write(args.getBytes());
			}
			// 获取返回内容的总大小
			sizeTotal = http.getContentLength();
			byte[] buffer = new byte[Constants.SIZE_BUFFER];
			int size = 0;
			// 获取输出流
			is = http.getInputStream();
			file = File.createTempFile("downloader-", ".dld");
			fos = new FileOutputStream(file);
			while ((size = is.read(buffer, 0, Constants.SIZE_BUFFER)) > 0) {
				fos.write(buffer, 0, size);
				sizeCount += size;
				publishProgress(sizeTotal, sizeCount);
			}
			Log.e("path", file.getAbsolutePath());
			http.disconnect();
			http=null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos!=null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (is!=null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (os!=null) {
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	protected String makeArgs(Map<String, String> args) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : args.entrySet()) {
			sb.append(entry.getKey());
			sb.append("=");
			try {
				sb.append(URLEncoder.encode(entry.getValue(), "utf-8"));
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
			sb.append("&");
		}
		return sb.toString();
	}

	@Override
	protected void onPreExecute() {
		if (mDownloaderListener != null) {
			mDownloaderListener.onBeforeDownload();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if (mDownloaderListener != null) {
			mDownloaderListener.onProgressChanged(values[0], values[1]);
		}
	}

	@Override
	protected void onPostExecute(File result) {
		if (mDownloaderListener != null) {
			mDownloaderListener.onAfterDownload(result);
		}
	}
}
