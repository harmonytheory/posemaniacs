package net.harmonytheory.android.posemaniacs.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import net.harmonytheory.android.posemaniacs.PoseManiacsApplication;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.BinaryHttpResponseHandler;

public class Util {
	/** キャシュディレクトリ名。*/
	private static final String CACHE_DIR = "cache";
	private static final String DATA_FILE = "data.php";
	private static final char PATH_SPLIT = '~';
	public interface SlideImageHandler {
		void hasCache(File cacheFile);
		void onSuccess(byte[] bytes);
		void onFailure(Throwable e);
	}
	
	/**
	 * キャッシュディレクトリ取得処理。
	 * @return キャッシュディレクトリ
	 */
	public static File getCacheDir() {
		PoseManiacsApplication application = PoseManiacsApplication.getInstance();
		File fileDir = application.getFilesDir();
		File cacheDir = new File(fileDir, CACHE_DIR);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir;
	}

	/**
	 * キャッシュディレクトリ削除。
	 * @param slideId スライドID
	 */
	public static void deleteCacheDir() {
		File cachedDir = getCacheDir();
		for (File file : cachedDir.listFiles()) {
			file.delete();
		}
		cachedDir.delete();
	}
	
	/**
	 * キャッシュスライド画像取得。
	 * @param oembed スライド情報
	 * @param slideNo スライド番号
	 * @return
	 */
	public static File getImageCacheFile(final String imgPath) {
		File cacheImageFile = new File(getCacheDir(), imgPath.replace('/', PATH_SPLIT));
		Log.e("getImageCacheFile", cacheImageFile.getAbsolutePath());
		return cacheImageFile;
	}

	/**
	 * スライド画像取得。
	 * キャシュ画像がある場合はそれを返却する。
	 * キャッシュがない場合は非同期で画像を取得する。
	 * @param context コンテキスト
	 * @param oembed スライド情報
	 * @param slideNo スライド番号
	 * @param handler スライド画像取得ハンドラ
	 */
	public static void getImage(final Context context, final String imgPath, final SlideImageHandler handler) {
		// ローカル保持ファイル
		final File cacheFile = getImageCacheFile(imgPath);
		// ローカルにファイルがある場合はそれを使用して、ない場合は取得を行う
		if (cacheFile.exists()) {
			// キャッシュがある場合に使用
			handler.hasCache(cacheFile);
		} else {
			Log.d("getImage", "file not exists!");
			// 画像を非同期で取得する。
			PoseManiacsHttpClient.getImage(context, imgPath, new BinaryHttpResponseHandler() {
				@Override
				public void onSuccess(byte[] bytes) {
					if (bytes == null) {
						throw new RuntimeException("bytes is null!?!?");
					}
					// ディスクキャッシュ書き込み
					saveDisk(cacheFile, bytes);
					handler.onSuccess(bytes);
				}
				@Override
				public void onFailure(Throwable e, byte[] arg1) {
					handler.onFailure(e);
				}
			});
		}
	}

	/**
	 * キャッシュスライド画像情報書き込み。
	 * @param oembed スライド情報
	 * @throws Exception
	 */
	public static void writeDataList(final String data) {
		File cacheDataFile = new File(getCacheDir(), DATA_FILE);
		PrintWriter writer = null;
		try {
			cacheDataFile.createNewFile();
			writer = new PrintWriter(cacheDataFile);
			writer.write(data);
			writer.flush();
		} catch (IOException e) {
			cacheDataFile.delete();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	/**
	 * キャッシュスライド画像情報取得。
	 * @return スライド画像情報
	 */
	public static List<String> getDataList() {
		File cacheDataFile = new File(getCacheDir(), DATA_FILE);
		if (!cacheDataFile.exists()) {
			return null;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(cacheDataFile));
			return Arrays.asList(reader.readLine().split(","));
		} catch (Exception e) {
			// ignore
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return null;
	}
	
	/**
	 * 取得データ書き込み処理。
	 * @param saveFile 書き込みファイル
	 * @param bytes 書き込みデータ
	 * @return 書き込み結果
	 */
	public static boolean saveDisk(File saveFile, byte[] bytes) {
		// キャッシュとしてディスクに書き込み
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(saveFile, false);
			fos.write(bytes);
			fos.flush();
			Log.d("saveDisk", "file write!");
			return true;
		} catch (Exception e) {
			Log.d("saveDisk", "file write error!");
			e.printStackTrace();
			saveFile.delete();
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
