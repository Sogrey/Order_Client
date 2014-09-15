package edu.feicui.order_client.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import edu.feicui.order_client.util.Constants;

/**
 * 缓存图片
 * 
 * @author Sogrey
 */
public class AsyncImageLoader {

	public static final byte[] HEX_CHAR = "0123456789ABCDEF".getBytes();
	private static final int MSG_DOWNLOAD = 0x10;

	public AsyncImageLoader() {
		File cache = new File(Constants.PATH_CACHE);// 创建缓存目录
		cache.mkdirs();
	}

	/** 缓存已加载到内存中的图片资源 */
	protected static Map<String, SoftReference<Bitmap>> sCache;

	// static {
	// sCache = new HashMap<String, SoftReference<Bitmap>>();
	// Bitmap bmp = null;
	// sCache.put("asd", new SoftReference<Bitmap>(bmp));
	// SoftReference<Bitmap> ref = sCache.get("asd");
	// bmp = ref.get();
	// if (bmp!=null) {
	// //软引用可能被清理，使用前需判断，不然报空指针
	// }
	// }
	static {
		sCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	protected Handler mHandler = new DownloadHandler();

	public void LoadImage(String url, ImageCallBack callBack) {
		// 一、内存缓存
		if (sCache.containsKey(url)) {// 查到有缓存
			Bitmap bmp = sCache.get(url).get();
			return;
		} else {// 查到没有缓存，清理，当做没有缓存
			sCache.remove(url);
		}
		// 二、文件缓存
		// 从url获取转换后的文件名
		String name = generateName(url);
		// 新建文件到缓存目录中
		File file = new File(Constants.PATH_CACHE, name);

		if (file.exists()) {
			Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
			if (bmp != null) {
				sCache.put(url, new SoftReference<Bitmap>(bmp));// 加载到内存
				callBack.onImageLoaded(url, bmp);
				return;
			} else {
				file.delete();
			}
		}
		// 三、网络缓存
		new DownloadThread(url, file, callBack).start();
	};

	/**
	 * MD5 转码
	 * 
	 * @param rawString
	 *            需要转妈的字符串
	 * */
	protected String generateName(String rawString) {
		try {
			// 得到MD5算法引用
			MessageDigest md5 = MessageDigest.getInstance("md5");
			// update 设置计算数据源
			md5.update(rawString.getBytes());
			// 得到计算结果，16进制数
			return toHexString(md5.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "default";
	}

	/**
	 * @param digest
	 *            需要转换的字符串
	 * @return 转换结果，字符串
	 */
	private String toHexString(byte[] digest) {
		StringBuilder sb = new StringBuilder(digest.length * 2);
		for (int i = 0; i < digest.length; i++) {
			sb.append(HEX_CHAR[(digest[i] & 0xf0) >>> 4]);
			sb.append(HEX_CHAR[digest[i] & 0x0f]);
		}
		return sb.toString();
	}

	class DownloadHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DOWNLOAD:
				Object[] array = (Object[]) msg.obj;
				String url = (String) array[0];
				File file = (File) array[1];
				ImageCallBack callBack = (ImageCallBack) array[2];
				Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
				callBack.onImageLoaded(url, bmp);
				break;

			default:
				break;
			}
		}
	}

	/** 图片加载完成，回调接口 */
	public interface ImageCallBack {
		void onImageLoaded(String url, Bitmap bmp);
	}

	class DownloadThread extends Thread {
		String mUrl;
		File mFile;
		ImageCallBack mCallBack;

		DownloadThread(String url, File file, ImageCallBack callBack) {
			super();
			this.mUrl = url;
			this.mFile = file;
			this.mCallBack = callBack;
		}

		@Override
		public void run() {
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				byte[] buffer = new byte[1024];
				int size = 0;
				URL url = new URL(mUrl);
				HttpURLConnection http = (HttpURLConnection) url
						.openConnection();
				is = http.getInputStream();
				fos = new FileOutputStream(mFile);
				while ((size = is.read(buffer, 0, Constants.SIZE_BUFFER)) > 0) {
					fos.write(buffer, 0, size);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fos.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					is.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			Message msg = Message.obtain();
			msg.what = MSG_DOWNLOAD;
			msg.obj = new Object[] { this.mUrl, this.mFile, this.mCallBack };
			mHandler.sendMessage(msg);
		}
	}
}
