/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package my.example.onekeycleaner.imgcache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.onekeycleaner.BuildConfig;
import my.example.onekeycleaner.imgcache.ImageCache.ImageCacheParams;
import my.example.onekeycleaner.util.Constants;
import my.example.onekeycleaner.util.Utils;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images fetched from a URL.
 */
public class ImageFetcher extends ImageResizer {
	private static final String TAG = "ImageFetcher";
	private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
	private static final String HTTP_CACHE_DIR = "http";
	private static final int IO_BUFFER_SIZE = 8 * 1024;

	private DiskLruCache mHttpDiskCache;
	private File mHttpCacheDir;
	private boolean mHttpDiskCacheStarting = true;
	private final Object mHttpDiskCacheLock = new Object();
	private static final int DISK_CACHE_INDEX = 0;

	/**
	 * Initialize providing a target image width and height for the processing images.
	 *
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public ImageFetcher(Context context, int imageWidth, int imageHeight) {
		super(context, imageWidth, imageHeight);
		init(context);
	}

	/**
	 * Initialize providing a single target image size (used for both width and height);
	 *
	 * @param context
	 * @param imageSize
	 */
	public ImageFetcher(Context context, int imageSize) {
		super(context, imageSize);
		init(context);
	}

	/**
	 * 不定义图片大小
	 *
	 * @param context
	 * @param imageSize
	 */
	public ImageFetcher(Context context) {
		super(context, -1);
		init(context);
	}

	private boolean isInit;

	private void init(Context context) {
		if (isInit && getImageCache() != null) {
			Log.d("jackey_log", "init ok");
			return;
		}
		
		Log.d("jackey_log", "start init");

		//checkConnection(context);
		mHttpCacheDir = ImageCache.getDiskCacheDir(mContext, HTTP_CACHE_DIR);

		ImageCacheParams cacheParams = new ImageCacheParams(mContext, Constants.IMAGE_CACHE_DIR);
		//cacheParams.diskCacheEnabled = false;
		//cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
		addImageCache(cacheParams);

		addReceiver();

		isInit = true;
		
		Log.d("jackey_log", "end init");
	}

	@Override
	protected void initDiskCacheInternal() {
		super.initDiskCacheInternal();
		initHttpDiskCache();
	}

	private void initHttpDiskCache() {
		if (!mHttpCacheDir.exists()) {
			mHttpCacheDir.mkdirs();
		}
		synchronized (mHttpDiskCacheLock) {
			if (ImageCache.getUsableSpace(mHttpCacheDir) > HTTP_CACHE_SIZE) {
				try {
					mHttpDiskCache = DiskLruCache.open(mHttpCacheDir, 1, 1, HTTP_CACHE_SIZE);
					if (BuildConfig.DEBUG) {
						Log.d(TAG, "HTTP cache initialized");
					}
				} catch (IOException e) {
					mHttpDiskCache = null;
				}
			}
			mHttpDiskCacheStarting = false;
			mHttpDiskCacheLock.notifyAll();
		}
	}

	@Override
	protected void clearCacheInternal() {
		super.clearCacheInternal();
		synchronized (mHttpDiskCacheLock) {
			if (mHttpDiskCache != null && !mHttpDiskCache.isClosed()) {
				try {
					mHttpDiskCache.delete();
					if (BuildConfig.DEBUG) {
						Log.d(TAG, "HTTP cache cleared");
					}
				} catch (IOException e) {
					Log.e(TAG, "clearCacheInternal - " + e);
				}
				mHttpDiskCache = null;
				mHttpDiskCacheStarting = true;
				initHttpDiskCache();
			}
		}
	}

	@Override
	protected void flushCacheInternal() {
		super.flushCacheInternal();
		synchronized (mHttpDiskCacheLock) {
			if (mHttpDiskCache != null) {
				try {
					mHttpDiskCache.flush();
					if (BuildConfig.DEBUG) {
						Log.d(TAG, "HTTP cache flushed");
					}
				} catch (IOException e) {
					Log.e(TAG, "flush - " + e);
				}
			}
		}
	}

	@Override
	protected void closeCacheInternal() {
		super.closeCacheInternal();
		synchronized (mHttpDiskCacheLock) {
			if (mHttpDiskCache != null) {
				try {
					if (!mHttpDiskCache.isClosed()) {
						mHttpDiskCache.close();
						mHttpDiskCache = null;
						if (BuildConfig.DEBUG) {
							Log.d(TAG, "HTTP cache closed");
						}
					}
				} catch (IOException e) {
					Log.e(TAG, "closeCacheInternal - " + e);
				}
			}
		}

		isInit = false;
		unRegisterReceiver();
	}

	/**
	 * Simple network connection check.
	 *
	 * @param context

    private void checkConnection(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            //Toast.makeText(context, R.string.no_network_connection_toast, Toast.LENGTH_LONG).show();
            Log.e(TAG, "checkConnection - no connection found");
        }
    }*/

	/**
	 * The main process method, which will be called by the ImageWorker in the AsyncTask background
	 * thread.
	 *
	 * @param data The data to load the bitmap, in this case, a regular http URL
	 * @param loadFromPm 是否从PackageManager加载APP Icon
	 * @return The downloaded and resized bitmap
	 */
	private Bitmap processBitmap(String data, boolean isLoadPm, boolean isRotate) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "processBitmap - " + data);
		}

		final String key = ImageCache.hashKeyForDisk(data);
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		DiskLruCache.Snapshot snapshot;
		synchronized (mHttpDiskCacheLock) {
			// Wait for disk cache to initialize
			while (mHttpDiskCacheStarting) {
				try {
					mHttpDiskCacheLock.wait();
				} catch (InterruptedException e) {}
			}

			if (mHttpDiskCache != null) {
				try {
					snapshot = mHttpDiskCache.get(key);
					if (snapshot == null) {
						if (BuildConfig.DEBUG) {
							Log.d(TAG, "processBitmap, not found in http cache, downloading...");
						}
						DiskLruCache.Editor editor = mHttpDiskCache.edit(key);
						if (editor != null) {
							if (isLoadPm) {
								// 从PackageManager加载APP Icon
								if (loadIconFromPackageManager(data,
										editor.newOutputStream(DISK_CACHE_INDEX))) {
									editor.commit();
								} else {
									editor.abort();
								}
							} else {
								if (downloadUrlToStream(data,
										editor.newOutputStream(DISK_CACHE_INDEX))) {
									editor.commit();
								} else {
									editor.abort();
								}
							}
						}
						snapshot = mHttpDiskCache.get(key);
					}
					if (snapshot != null) {
						fileInputStream =
								(FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
						fileDescriptor = fileInputStream.getFD();
					}
				} catch (IOException e) {
					Log.e(TAG, "processBitmap - " + e);
				} catch (IllegalStateException e) {
					Log.e(TAG, "processBitmap - " + e);
				} finally {
					if (fileDescriptor == null && fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {}
					}
				}
			}
		}

		Bitmap bitmap = null;
		if (fileDescriptor != null) {
			bitmap = decodeSampledBitmapFromDescriptor(fileDescriptor, mImageWidth,
					mImageHeight, getImageCache(), isRotate);
		}
		if (fileInputStream != null) {
			try {
				fileInputStream.close();
			} catch (IOException e) {}
		}
		return bitmap;
	}

	@Override
	protected Bitmap processBitmap(Object... data) {
		boolean isLoadPm = (Boolean) data[1];
		boolean isRotate = (Boolean) data[2];
		return processBitmap(String.valueOf(data[0]), isLoadPm, isRotate);
	}

	/**
	 * Download a bitmap from a URL and write the content to an output stream.
	 *
	 * @param urlString The URL to fetch
	 * @return true if successful, false otherwise
	 */
	public boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
		disableConnectionReuseIfNecessary();
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;

		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
			out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			Log.e(TAG, "Error in downloadBitmap - " + e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {}
		}
		return false;
	}

	/**
	 * Workaround for bug pre-Froyo, see here for more info:
	 * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
	 */
	public static void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}


	/**
	 * 将Drawable对象转换为Bitmap对象。
	 * 
	 * @param drawable
	 *            要转换的Drawable
	 * @return 转换后的bitmap
	 */
	private Bitmap convertDrawable2Bitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Config config = (drawable.getOpacity() != PixelFormat.OPAQUE)
				? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;

		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap转换为byte数组
	 * 
	 * @param bitmap
	 *            Bitmap
	 * @return byteArray
	 */
	public byte[] convertBitmap2Bytes(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 通过PackageManager进行加载Icon
	 * 
	 * @param data
	 *            AppKey PackageName@Version
	 * @param outputStream
	 *            文件的输出流
	 * @return 是否加载成功
	 */
	private boolean loadIconFromPackageManager(String data, OutputStream outputStream) {
		BufferedOutputStream out = null;
		String packagename = data.substring(0, data.indexOf("@"));
		try {
			PackageInfo pinfo = mContext.getPackageManager().getPackageInfo(packagename, 0);
			Drawable draw = pinfo.applicationInfo.loadIcon(mContext.getPackageManager());
			out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
			out.write(convertBitmap2Bytes(convertDrawable2Bitmap(draw)));
			return true;
		} catch (NameNotFoundException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "findIconFromPackageManager: " + e);
			}
		} catch (IOException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "findIconFromPackageManager: " + e);
			}
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (final IOException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "findIconFromPackageManager: " + e);
				}
			}
		}
		return false;
	}

	/**
	 * 注册广播 接收sd卡移出的广播
	 */
	private void addReceiver() {
		if(mReceiver == null) {
			mReceiver = new CloseCacheReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
		mContext.registerReceiver(mReceiver, filter);
	}

	/**
	 * 取消注册的广播
	 */
	private void unRegisterReceiver() {
		try {
			mContext.unregisterReceiver(mReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private CloseCacheReceiver mReceiver;

	private final class CloseCacheReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (action.equals(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)) {
				closeCacheInternal();
			}
		}
	}
}
