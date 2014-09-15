package edu.feicui.order_client.net;

import java.io.File;

/**
 * @author Sogrey
 *
 */
public interface OnDownloaderListener {
	public void onBeforeDownload();
	public void onProgressChanged(int totleSize,  int countSize);
	public void onAfterDownload(File file);
}
