package com.cells.companyapp.view;

import java.io.File;
import java.text.DecimalFormat;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.JsonUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Video;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.utils.HttpUtils;

public class VideoInfoActivity extends BaseActivity {

	private static final String TAG = "VideoInfoActivity";

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.ll_layout)
	private LinearLayout layout;

	@ViewInject(id = R.id.img_video)
	private ImageView image;
	@ViewInject(id = R.id.tv_video_name)
	private TextView name;
	@ViewInject(id = R.id.tv_video_issue)
	private TextView issue;
	@ViewInject(id = R.id.tv_video_issue_date)
	private TextView date;

	@ViewInject(id = R.id.img_video_download, click = "download")
	private ImageView download;

	private Video video = new Video();

	private int id;
	private String download_path;

	private CircularProgressDialog loading;

	HttpHandler<File> handler;

	@ViewInject(id = R.id.tv_video_progress)
	private TextView progress;

	@ViewInject(id = R.id.progress_bar)
	private ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_info);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		id = getIntExtra("id");

		bar.setIndeterminate(false);

		tvTitle.setText("视频画刊信息");
		back.setImageResource(R.drawable.icon_back);

		loading = CircularProgressDialog.show(context);
		loading.show();
		getVideoInfo(id);
	}

	private void getVideoInfo(int id) {
		AjaxParams params = new AjaxParams();
		params.put("id", id + "");

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.VIDEO_DETAIL, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						Log.i(TAG, str);

						layout.setVisibility(View.VISIBLE);

						video = (Video) JsonUtil.fromJson(str, Video.class);

						loading.dismiss();

						if (null != video.getName()) {
							name.setText(video.getName());
						}
						if (null != video.getIssue_date()) {
							date.setText(video.getIssue_date());
						}
						if (null != video.getIssue()) {
							issue.setText(video.getIssue());
						}
						FinalBitmap fb = FinalBitmap.create(context);
						fb.display(image, video.getImage());
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						if (t != null) {
							showToast("加载失败，请稍后再试！");
							loading.dismiss();
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	public void download(View v) {
		download_path = AppConfig.DOWNLOAD_BOOK_PATH + "/" + video.getName()
				+ ".zip";
		bar.setVisibility(View.VISIBLE);
		progress.setVisibility(View.VISIBLE);

		String path = video.getDocument();
		Log.i(TAG, path);
		Log.i(TAG, download_path);
		String lll = path.substring(path.lastIndexOf("/") + 1,
				path.lastIndexOf("?"));
		Log.i(TAG, lll);

		FinalHttp fh = new FinalHttp();
		handler = fh.download(video.getDocument(), download_path,
				new AjaxCallBack<File>() {
					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);

						Double d_current = (((double) current / 1024) / 1024);
						Double d_count = (((double) count / 1024) / 1024);

						DecimalFormat df = new DecimalFormat("#.00");

						progress.setText(df.format(d_current) + "/"
								+ df.format(d_count) + " (M)");
						// 设置进度条最大值
						bar.setMax((int) (count / 1024));
						// 设置ProgressBar当前值
						bar.setProgress((int) (current / 1024));
					}

					@Override
					public void onSuccess(File t) {
						if (t != null) {
							handler.stop();
							showToast("下载完毕！");
							download.setImageResource(R.drawable.icon_read);
						}
					}

					@Override
					public void onFailure(Throwable t, int errorCode,
							String strMsg) {
						super.onFailure(t, errorCode, strMsg);

						if (t != null) {
							Log.i(TAG, t.toString());
						}
						if (strMsg != null) {
							Log.i(TAG, strMsg);
						}
					}
				});
	}

	public void back(View v) {
		finish();
	}
}
