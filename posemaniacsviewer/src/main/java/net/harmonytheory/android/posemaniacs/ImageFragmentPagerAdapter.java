/**
 * 
 */
package net.harmonytheory.android.posemaniacs;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.harmonytheory.android.posemaniacs.common.Const;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ページング用アダプタ。
 * @author SPEEDY
 */
public class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
	/** 表示データ。*/
	private List<String> imgPathList;
	private Random RANDOM_PATH = new Random(System.currentTimeMillis());
	private Random RANDOM_FILE = new Random(System.currentTimeMillis() * 2);
	private Set<String> createdImgPathList = new LinkedHashSet<String>();
	/**
	 * コンストラクタ。
	 * @param fragmentManager 
	 * @param oembed 表示データ
	 */
	public ImageFragmentPagerAdapter(FragmentManager fragmentManager, List<String> imgPathList) {
		super(fragmentManager);
		this.imgPathList = imgPathList;
	}
	/**
	 * ページ取得処理。
	 * @param position 取得ページ数
	 */
	@Override
	public Fragment getItem(int position) {
		String imgFilePath = null;
		while (!createdImgPathList.add(imgFilePath = getImageFileName()));
        return ImageFragment_.builder().imgPath(imgFilePath).build();
	}
	
	/**
	 * 画像ファイル名生成。
	 * ランダムでファイルパスを生成して返却する。
	 * @return 画像ファイル名
	 */
	private String getImageFileName() {
		int path = RANDOM_PATH.nextInt(imgPathList.size());
		int file = RANDOM_FILE.nextInt(Const.NUM_POSE) + 1;
		return imgPathList.get(path).concat("/").concat(String.format(Const.FILE_NAME_IMAGE, file));
	}
	
	/**
	 * 総ページ数取得処理。
	 */
	@Override
	public int getCount() {
		return imgPathList.size() * Const.NUM_POSE;
	}
}