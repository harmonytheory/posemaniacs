package net.harmonytheory.android.posemaniacs;

import org.androidannotations.annotations.EApplication;

import android.app.Application;

@EApplication
public class PoseManiacsApplication extends Application {
	/** シングルトンインスタンス。*/
	protected static PoseManiacsApplication instance;
	
	/**
	 * 起動時処理。
	 * 自身をシングルトンインスタンスとして保持する。
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		if (instance == null) {
			instance = this;
		}
	}
	
	/**
	 * シングルトンインスタンス返却処理。
	 * @return シングルトンインスタンス
	 */
	public static PoseManiacsApplication getInstance() {
		return instance;
	}
}
