package net.harmonytheory.android.posemaniacs.common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * PoseManiacs用HttpClientクラス。
 * @author SPEEDY
 */
public class PoseManiacsHttpClient {

	/** REST用HttpClient。*/
	private static RestTemplate REST_TEMPLATE = new RestTemplate(true, new SimpleClientHttpRequestFactory(){
		@Override
		protected void prepareConnection(HttpURLConnection connection,
				String httpMethod) throws IOException {
			super.prepareConnection(connection, httpMethod);
			// PoseManiacsがUser-Agentが不正だと404になるため設定する。
			connection.addRequestProperty("User-Agent", (String) new DefaultHttpClient().getParams().getParameter(CoreProtocolPNames.USER_AGENT));
		}
	});

	/** 非同期HttpClient。*/
	private static AsyncHttpClient ASYNC_CLIENT = new AsyncHttpClient();
	static {
		// デフォルトだとbot扱いされてしまうためデフォルトのUSERAGENTを設定する。
		ASYNC_CLIENT.setUserAgent((String) new DefaultHttpClient().getParams().getParameter(CoreProtocolPNames.USER_AGENT));
		// リダイレクトを無効
		ASYNC_CLIENT.getHttpClient().getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
	}

	/**
	 * 画像パスリスト取得処理。
	 * data.phpにアクセスし、レスポンスの画像パスリストを返却する。
	 * @return 画像パスリスト
	 */
	public static List<String> getImagePathList() {
		List<String> dataList = Util.getDataList();
		if (dataList == null) {
			String data = REST_TEMPLATE.getForObject(Const.URL_BASE.concat(Const.PATH_DATA), String.class);
			Util.writeDataList(data);
			dataList = Arrays.asList(data.split(","));
		}
		return dataList;
	}

	/**
	 * 画像非同期取得処理。
	 * @param context コンテキスト
	 * @param url 取得URL
	 * @param responseHandler レスポンスハンドラ
	 */
	public static void getImage(Context context, String imgPath, AsyncHttpResponseHandler responseHandler) {
		Log.e("get image", "imgPath=" + imgPath);
		ASYNC_CLIENT.get(context, Const.URL_BASE.concat(imgPath), responseHandler);
	}
}
