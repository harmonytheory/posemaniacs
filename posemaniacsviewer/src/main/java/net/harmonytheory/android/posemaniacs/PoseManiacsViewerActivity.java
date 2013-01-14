package net.harmonytheory.android.posemaniacs;

import java.util.List;

import net.harmonytheory.android.posemaniacs.common.PoseManiacsHttpClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * PoseManiacs表示用アクティビティ。
 * @author SPEEDY
 */
@EActivity(R.layout.main)
@OptionsMenu(R.menu.menu_index)
public class PoseManiacsViewerActivity extends FragmentActivity {
	@ViewById
	ViewPager viewpager;
	/**
	 * ビュー表示後処理。
	 */
	@AfterViews
	protected void setting() {
		displayRandomViewer();
	}
	
	/**
	 * PoseManiacsの情報を取得して画像を表示する。
	 */
	@Background
	protected void displayRandomViewer() {
		// 画像パスリストを取得して
		List<String> imgPathList = PoseManiacsHttpClient.getImagePathList();
		settingViewer(imgPathList);
	}

	/**
	 * リクエスト結果からViewPagerの生成を行う。
	 * @param oembed PoseManiacs取得結果
	 */
	@UiThread
	protected void settingViewer(List<String> imgPathList) {
		viewpager.setAdapter(new ImageFragmentPagerAdapter(getSupportFragmentManager(), imgPathList));
	}
}
