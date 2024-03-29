package net.harmonytheory.android.posemaniacs;

import java.io.File;

import net.harmonytheory.android.posemaniacs.common.Util;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * スライド画像表示用Fragment
 * @author SPEEDY
 */
@EFragment
public class ImageFragment extends Fragment {
	@FragmentArg
	String imgPath;
	
	public ImageFragment() {
		// 画面回転対応
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// ちょっとメモリ使いすぎな気がしたのでViewの保持はやめておく。
//		// Viewを生成済みのFragmentの場合は再生成しない
//		if (view != null) {
//			((ViewGroup) view.getParent()).removeView(view);
//			return view;
//		}
		final View view = inflater.inflate(R.layout.image, container, false);
		final ImageView imageView = (ImageView) view.findViewById(R.id.image);

		Util.getImage(getActivity(), imgPath, new Util.SlideImageHandler() {
			@Override
			public void onSuccess(byte[] bytes) {
				// 取得結果をImageViewに設定する
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
				// ImaveView更新
				imageView.invalidate();
			}
			public void onFailure(Throwable e) {
			}
			@Override
			public void hasCache(File cacheFile) {
				// 取得結果をImageViewに設定する
				imageView.setImageBitmap(BitmapFactory.decodeFile(cacheFile.getAbsolutePath()));
				imageView.invalidate();
			}
		});
		return view;
	}
}
