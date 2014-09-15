/**
 * 
 */
package edu.feicui.order_client.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import edu.feicui.order_client.net.AsyncImageLoader;
import edu.feicui.order_client.net.AsyncImageLoader.ImageCallBack;
import edu.feicui.order_client.util.Constants;

/**
 * Gallery   ≈‰∆˜
 * @author Sogrey
 *
 */

public class GalleryAdapter extends BaseAdapter {
	AsyncImageLoader loader = new AsyncImageLoader();
	protected String urls[] = {
			Constants.PATH_IMAGES+"/dish01.jpg",
			Constants.PATH_IMAGES+"/dish02.jpg",
			Constants.PATH_IMAGES+"/dish03.jpg",
			Constants.PATH_IMAGES+"/dish04.jpg",
			Constants.PATH_IMAGES+"/dish05.jpg",
			Constants.PATH_IMAGES+"/dish06.jpg",
			Constants.PATH_IMAGES+"/dish07.jpg",
			Constants.PATH_IMAGES+"/dish08.jpg",
			Constants.PATH_IMAGES+"/dish09.jpg",
			Constants.PATH_IMAGES+"/dish10.jpg",
			Constants.PATH_IMAGES+"/dish11.jpg",
			Constants.PATH_IMAGES+"/dish12.jpg",
			Constants.PATH_IMAGES+"/dish13.jpg",
			Constants.PATH_IMAGES+"/dish14.jpg" };
//	protected String urls[] = {
//			Constants.PATH_IMAGES+"/colddishes1.png",
//			Constants.PATH_IMAGES+"/colddishes2.png",
//			Constants.PATH_IMAGES+"/colddishes3.png",
//			Constants.PATH_IMAGES+"/colddishes4.png",
//			Constants.PATH_IMAGES+"/drink1.png",
//			Constants.PATH_IMAGES+"/drink2.png",
//			Constants.PATH_IMAGES+"/drink3.png",
//			Constants.PATH_IMAGES+"/drink4.png",
//			Constants.PATH_IMAGES+"/hotdishes1.png",
//			Constants.PATH_IMAGES+"/hotdishes2.png",
//			Constants.PATH_IMAGES+"/hotdishes3.png",
//			Constants.PATH_IMAGES+"/hotdishes4.png",
//			Constants.PATH_IMAGES+"/soup1.png",
//			Constants.PATH_IMAGES+"/soup2.png",
//			Constants.PATH_IMAGES+"/soup3.png",
//			Constants.PATH_IMAGES+"/soup4.png",
//			Constants.PATH_IMAGES+"/staplefood1.png",
//			Constants.PATH_IMAGES+"/staplefood2.png",
//			Constants.PATH_IMAGES+"/staplefood3.png",
//			Constants.PATH_IMAGES+"/staplefood4.png" };
	Context mContext;
	public GalleryAdapter(Context context){
		mContext=context;
	}
	@Override
	public int getCount() {
		return urls.length;
	}

	@Override
	public Object getItem(int position) {
		return urls[position];
	}

	@Override
	public long getItemId(int position) {
		return urls[position].hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageView view;
		if (convertView == null) {
			view = new ImageView(mContext);
//			view.setMaxHeight(360);
//			view.setMaxWidth(500);
		} else {
			view = (ImageView) convertView;
		}
		view.setTag(urls[position]);
		loader.LoadImage(urls[position], new ImageCallBack() {

			@Override
			public void onImageLoaded(String url, Bitmap bmp) {
				if (url.equals(view.getTag())) {
					view.setImageBitmap(bmp);
				}
			}
		});
		return view;
	}
}